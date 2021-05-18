package com.montnets.emp.qyll.biz;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.qyll.biz.cache.LlOrderCache;
import com.montnets.emp.qyll.dao.CommonDao;
import com.montnets.emp.qyll.dao.LldgDao;
import com.montnets.emp.qyll.entity.LLOrderTask;
import com.montnets.emp.qyll.entity.LlOrderDetail;
import com.montnets.emp.qyll.utils.StringUtil;

/**
 * 流量订购读取线程
 * @author Administrator
 *
 */
public class LlDetailReadThread extends Thread {
	private boolean isExit = false;
	
	LldgDao llDao = new LldgDao();
	
	int row = 0;
	
	private LlOrderCache orderCache = LlOrderCache.INSTANCE;
	
	private List<LlOrderDetail> cacheList = new ArrayList<LlOrderDetail>();
	
	CommonDao commDao = new CommonDao();
	
	SuperDAO superDao = new SuperDAO();
	/**
	 * 构造方法
	 */
	public LlDetailReadThread(){
		//设置线程名称
		this.setName("[流量订购读取线程]");
		//设置每次读取条数
		row = 500;
	}
	
	public void run(){
		
		List<LLOrderTask> allTasks = new ArrayList<LLOrderTask>();
		while(!isExit){
			try {
				//扫描订单任务表。查询是否有未处理的订单任务
				allTasks = llDao.findOrderNumUnsent();
				
				//没有可处理的订单任务，休息1s，继续扫描
				if(null == allTasks || allTasks.size() == 0){
					Thread.sleep(5000L);
					continue;
				}
				////ORDERNO,TIMER_TIME,RE_STATUS,TIMER_STATUS,ISRETRY
				for(LLOrderTask task : allTasks){//开始处理未处理的订购任务
					String orderNo = task.getOrderNo();
					//判断如果是定时短信
					if("1".equals(task.getTimer_status())){//定时短信
						//判断操作员是否禁用
						if("0".equals(getUserStateByUserId(task.getUser_id()))){
							//更新发送状态为已冻结
							commDao.updateReadTaskFlowStat(orderNo,"4");
							commDao.updateReadTaskSMSStat(orderNo, "4");
							continue;
						}
						
						if("2".equals(getUserStateByUserId(task.getUser_id()))){
							//更新发送状态为已撤销
							commDao.updateReadTaskFlowStat(orderNo,"3");
							commDao.updateReadTaskSMSStat(orderNo, "3");
							continue;
						}
						
						//判断定时时间是否超时
						if(isOverTimerTime(task.getTimer_time())){
							//更新发送状态为超时未发送
							commDao.updateReadTaskFlowStat(orderNo,"5");
							commDao.updateReadTaskSMSStat(orderNo, "5");
							continue;
						}
					}
					
					//1.更新任务节点编号，表示本节点处理
					if(!llDao.updateServerNumByOrdernum(StaticValue.getServerNumber(), orderNo)){
						//未更新成功，跳过当前处理
						continue;
					}
					//根据订单编号，循环读取数据订购
					queryOrderDetailByOrderNo(orderNo);
				}
			} catch (InterruptedException e) {
				EmpExecutionContext.error(e, "流量订购读取线程被异常中断" );
				Thread.currentThread().interrupt();
			} catch (Exception e) {
				EmpExecutionContext.error(e, "流量订购读取线程异常" );
			}
		}
	
	}
	
	public void queryOrderDetailByOrderNo(String orderNum) throws Exception {
		List<LlOrderDetail> orderDetais = new ArrayList<LlOrderDetail>();
		while(true){
			if(orderCache.getSize() > 5000){//如果队列长度大于5000，休眠1s再读取
				Thread.sleep(1000);
				continue;
			}
			//2.读取订购内容
			orderDetais = llDao.findOrderDetail(StaticValue.getServerNumber(), orderNum,row);
			
			if(null == orderDetais || orderDetais.isEmpty()){
				break;//当前订单编号已无可发数据，退出循环
			}
			//3.处理，并放入队列
			orderDetailToQueue(orderDetais);
			
			//4.清空list
			if(orderDetais != null){
				orderDetais.clear();
			}
		}
		
	}
	
	public void orderDetailToQueue(List<LlOrderDetail> orderDetais) throws Exception{
		//未获取到待订购的流量套餐
		if(null == orderDetais || orderDetais.size() == 0){
			return;
		}
		
		String year = StringUtil.subOrderNoYear(orderDetais.get(0).getOrderno());
		
		//有需要订购的数据
		int count = 0;
		StringBuffer ids = new StringBuffer();
		for(LlOrderDetail ord : orderDetais){
			cacheList.add(ord);
			//拼接ID
			ids.append(ord.getId()).append(",");
			count++;
			//每30条，更新数据库状态为已读
			if(count >= 100){
				ids.delete(ids.lastIndexOf(","),ids.length());
				while(!commDao.updateReadDetailStatus(ids.toString(),year)){
					EmpExecutionContext.error("更新已读状态失败："+ids);
					Thread.sleep(1000);
				}
				//入发送缓存队列
				orderCache.produceList(cacheList);
				cacheList.clear();
				ids.delete(0, ids.length());
				count = 0;
			}
		}
		
		if(count > 0 || ids.length() > 0){
			ids.delete(ids.lastIndexOf(","),ids.length());
			while(!commDao.updateReadDetailStatus(ids.toString(),year)){
				EmpExecutionContext.error("更新已读状态失败："+ids);
				Thread.sleep(1000);
			}
			//入发送缓存队列
			orderCache.produceList(cacheList);
			cacheList.clear();
			ids.delete(0, ids.length());
			count = 0;
		}
	}
	
	/**
	 * 获取操作员禁用状态
	 * @param userId
	 * @return
	 */
	public String getUserStateByUserId(int userId){
		String sql = "SELECT USER_STATE FROM LF_SYSUSER WHERE USER_ID = "+userId+"";
		
		String userState = "";
		try {
			userState = superDao.getString("USER_STATE", sql, StaticValue.EMP_POOLNAME);
		} catch (Exception e) {
			EmpExecutionContext.error("获取操作员禁用状态失败："+e);
		}
		
		return userState;
	}
	
	/**
	 * 判断短信定时时间是否超时(超时时间，5分钟)
	 * @return
	 */
	public boolean isOverTimerTime(Timestamp timerTime){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(timerTime);
		c1.add(Calendar.MINUTE, 5);
		
		//当前时间
		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date());
		
		if(c1.before(c2)){//如果定时时间+5min依旧小于当前时间，超时
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		LlDetailReadThread read = new LlDetailReadThread();
		read.start();
	}
}
