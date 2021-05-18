package com.montnets.emp.common.biz.receive;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.entity.system.LfReport;
import com.montnets.emp.entity.system.MoWaitA;
import com.montnets.emp.entity.system.RptWaitA;
import com.montnets.emp.util.PageInfo;
/**
 * 
 * @author Administrator
 *
 */
public class TimerReceiveHandle extends SuperBiz
{
	
	private static TimerReceiveHandle timerMoHandleInstance = null;
	
	//上行信息集合
	private List<LfMotask> mosList = null;
	//状态报告集合
	private List<LfReport> rptsList = null;
	//指令信息集合
	private List<LfMotask> ordersList = null;
	
	//状态报告集合
	private List<RptWaitA> rptWaitAsList = null;
	//上行信息集合
	private List<MoWaitA> moWaitAsList = null;
	
	private Map<String,String[]> sendedsMap = null;
	//推送类型
	private int pushType;
	//定时器
	private ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(1);
	
	private ScheduledFuture<?> moSF = null;
	private ScheduledFuture<?> rptSF = null;
	
	private ScheduledFuture<?> dbMoSF = null;
	private ScheduledFuture<?> dbRptSF = null;
	
	//
	private static final long DELAY = 1000*3L;
	//接收biz
	private ReceiveBiz receiveBiz = null;
	//条件map
	private LinkedHashMap<String, String> conditionMap;
	//排序
	private LinkedHashMap<String, String> orderbyMap;
	//分页信息
	private PageInfo pageInfo;
	
	//处理上行数据线程
	private ReceiveDataDealThread dataDealThread;
	
	private TimerReceiveHandle(){
		//systemGlobals.properties配置文件里面
		//pushType=SystemGlobals.getIntValue("pushType", 1);
		pushType=1;
		//出事化接收biz
		receiveBiz = new ReceiveBiz();
		//初始化上行集合
		mosList = Collections.synchronizedList(new LinkedList<LfMotask>());
		//推送状态报告
		rptsList = Collections.synchronizedList(new LinkedList<LfReport>());
		sendedsMap = Collections.synchronizedMap(new HashMap<String,String[]>());
		//数据库取状态报告
		rptWaitAsList = Collections.synchronizedList(new LinkedList<RptWaitA>());
		//数据库取上行记录
		moWaitAsList = Collections.synchronizedList(new LinkedList<MoWaitA>());
		//初始化排序map
		orderbyMap = new LinkedHashMap<String, String>();
		//初始化条件map
		conditionMap = new LinkedHashMap<String, String>();
		//初始化分页
		pageInfo = new PageInfo();
		
		//如果类型是推送
		if(pushType==1)
		{
			dataDealThread = new ReceiveDataDealThread();
			dataDealThread.StartThread();
			//推送上行
			this.lanuchMoTimer();
			
			//推送状态报告
			this.lanuchRptTimer();
			
			//推送指令
			//this.lanuchOrderTimer();
		}else 
		{
			//类型是从数据库读取
			
			//从数据库取状态报告
			this.loadDBRptTimer();
			//从数据库中取上行记录
			this.loadDBMoTimer();
		}
	}
	
	public void refreshTimerReceive()
	{
		//重新初始化biz
		receiveBiz = new ReceiveBiz();
	}
	
	/**
	 * 
	 */
	synchronized public static TimerReceiveHandle getTMoHandleInstance(){
		//如果没初始化过，则初始化
		if(timerMoHandleInstance == null || timerMoHandleInstance.mosList == null || timerMoHandleInstance.rptsList == null){
			timerMoHandleInstance = new TimerReceiveHandle();
		}
		//返回对象
		return timerMoHandleInstance;
	}
	
	/**
	 * 启动上行推送任务
	 */
	private void lanuchMoTimer()
	{
		//任务类
		Runnable task = new Runnable() 
		{
			public void run() 
			{
				//没上行信息，着返回
				if(mosList == null || mosList.size() == 0)
				{
					return;
				}
				//上行对象
				LfMotask moTask = null;
				try
				{
					synchronized(mosList)
					{
						//循环读取上行信息集合，并处理每条记录
						while(mosList != null && mosList.size() > 0)
						{
							//移除并获取记录
							moTask = mosList.remove(0);
							if(moTask == null)
							{
								break;
							}
							//调用处理方法
							receiveBiz.addMotask1(moTask);
						}
					}
				}
				catch(Exception e)
				{
					//异常处理
					String errorMsg = "上行推送，异常。";
					if(moTask !=null){
						errorMsg =  "momsgid="+moTask.getPtMsgId()
								+ ",phone="+moTask.getPhone()
								+ ",spuser="+moTask.getSpUser()
								+ ",spnumber="+moTask.getSpnumber()
								+ ",exno="+moTask.getSubno()
								+ ",msg="+moTask.getMsgContent()
								+ ",cmdid="+moTask.getMoOrder()
								+ ",menuCode="+moTask.getMenuCode()
								+ ",depId="+moTask.getDepId()
								+ ",taskId="+moTask.getTaskId()
								+ ",busCode="+moTask.getBusCode()
								+ ",corpCode="+moTask.getCorpCode();
					}
					EmpExecutionContext.error(e,errorMsg);
				}
			}
		};
		try
		{
			//设置定时任务
			moSF = scheduExec.scheduleWithFixedDelay(task, 1000, DELAY, TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置上行推送任务异常。");
		}
	}

	/**
	 * 启动状态报告推送任务
	 */
	private void lanuchRptTimer(){
		//任务对象
		Runnable task = new Runnable() {
			public void run() {
				//状态报告为空，没状态报告，返回
				if(rptsList == null || rptsList.size() == 0){
					return;
				}
				//状态报告对象
				LfReport report = null;
				//结果
				boolean result = false;
				try{
					synchronized(rptsList){
						//循环处理状态报告
						while(rptsList != null && rptsList.size() > 0){
							//移除并获取状态报告
							report = rptsList.remove(0);
							//状态报告为空
							if(report == null){
								EmpExecutionContext.debug("定时分发前发现状态报告对象为空！");
								break;
							}
							
							EmpExecutionContext.debug("定时分发状态报告：mtmsgid:"+report.getMtmsgid()+"------");
							//处理状态报告
							result = receiveBiz.addReport(report);
							
							EmpExecutionContext.debug("定时分发状态报告结果："+result+",mtmsgid:"+report.getMtmsgid()+"------");
						}
					}

				}catch(Exception e){
					//异常处理
					EmpExecutionContext.error(e, "启动状态报告推送任务异常。");
				}

			}
		};
		
		try
		{
			rptSF = scheduExec.scheduleWithFixedDelay(task, 1000, DELAY, TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置状态报告推送任务异常。");
		}
	}
	/**
	 * 数据库拿取上行记录
	 */
	private void loadDBMoTimer(){
		Runnable task = new Runnable() {
			public void run() {
				
				try
				{
					//每页记录数
					pageInfo.setPageSize(5000);
					//以id排序
					orderbyMap.put("id", StaticValue.DESC);
					//从数据库获取上行记录
					List<MoWaitA> moTempList = empDao.findPageListByCondition(null, MoWaitA.class, null, orderbyMap, pageInfo);
					//有记录，则加入上行信息集合
					if(moTempList != null && moTempList.size() > 0){
						moWaitAsList.addAll(moTempList);
					}
					
				} catch (Exception e1)
				{
					//异常处理
					EmpExecutionContext.error(e1, "数据库拿取上行记录异常。");
					return;
				}
				//没上行记录，返回
				if(moWaitAsList == null || moWaitAsList.size() == 0){
					return;
				}
				//上行对象
				LfMotask motask = null;
				MoWaitA moWaitA = null;
				//记录上行记录id
				StringBuffer moIds = new StringBuffer();
				boolean resultAdd = false;
				boolean saveMoresult=false;
				
				try{
					synchronized(moWaitAsList){
						//循环处理上行记录
						while(moWaitAsList != null && moWaitAsList.size() > 0){
							//移除并获取记录
							moWaitA = moWaitAsList.remove(0);
							if(moWaitA == null){
								break;
							}
							//初始化上行对象
							motask = new LfMotask();
							//流水号
							motask.setPtMsgId(moWaitA.getPtMsgId().toString());
							//发送账号
							motask.setSpUser(moWaitA.getUserId());
							//手机号
							motask.setPhone(moWaitA.getPhone());
							//运营商
							motask.setSpisuncm(moWaitA.getUnicom());
							//编码格式
							motask.setMsgFmt(moWaitA.getMsgFmt());
							//短信内容
							motask.setMsgContent(moWaitA.getMessage());
							//上行时间
							motask.setDeliverTime(moWaitA.getDeliverTime());
							//通道号
							motask.setSpnumber(moWaitA.getSpNumber());
							motask.setSubno(moWaitA.getCpno());
							//用户UID
							//motask.setUserGuid(moWaitA.getLoginUid());
							//处理上行记录
							resultAdd = receiveBiz.addMotask1(motask);
							
							if(resultAdd){
								//成功推送，则放进删除列表
								
								EmpExecutionContext.debug(new SimpleDateFormat("上行推送 ：yyyy-MM-dd HH:mm:ss").format(new Date())+" 发送账号："+moWaitA.getUserId()+"  上行任务id："+moWaitA.getId()+"的上行任务加入删除列表");
								//入库
								saveMoresult = empDao.save(motask);
								if(saveMoresult)
								{
									EmpExecutionContext.debug("上行MoWaitA(id:"+moWaitA.getId()+")对应的LfMotask对象插入LF_MOTASK表成功");
								}else {
									EmpExecutionContext.debug("上行MoWaitA(id:"+moWaitA.getId()+")对应的LfMotask对象插入LF_MOTASK表失败");
								}
								moIds.append(moWaitA.getId()).append(",");
							}
						}
						//有上行记录id
						if(moIds != null && moIds.length() > 0){
							
							//rptIds = rptIds.substring(0, rptIds.lastIndexOf(","));
							String strRpt = moIds.substring(0, moIds.lastIndexOf(","));
							//删掉已推送的状态报告
							empDao.delete(MoWaitA.class, strRpt);
							
						}
					}
				}
				catch(Exception e)
				{
					//异常处理
					EmpExecutionContext.error(e, "数据库拿取上行记录异常。");
				}
			}
		};
		try
		{
			//设置定时
			dbMoSF = scheduExec.scheduleWithFixedDelay(task, 1000, DELAY, TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置数据库获取上行信息任务异常。");
		}
	}
	/**
	 * 数据库拿取状态报告
	 */
	private void loadDBRptTimer(){
		Runnable task = new Runnable() {
			public void run() {
				
				try
				{
					//每页记录数
					pageInfo.setPageSize(5000);
					//conditionMap.put("moduleId", StaticValue.WMNOTICECODE);
					orderbyMap.put("id", StaticValue.DESC);
					//获取rpt记录
					List<RptWaitA> rptTempList = empDao.findPageListByCondition(null, RptWaitA.class, conditionMap, orderbyMap, pageInfo);
					if(rptTempList != null && rptTempList.size() > 0){
						rptWaitAsList.addAll(rptTempList);
					}
					
				} catch (Exception e1)
				{
					//异常处理
					EmpExecutionContext.error(e1, "数据库拿取状态报告异常。");
					return;
				}
				//没状态报告，则返回
				if(rptWaitAsList == null || rptWaitAsList.size() == 0){
					return;
				}
				//状态报告对象
				LfReport report = null;
				RptWaitA rptWaitA = null;
				//状态报告id
				StringBuffer rptIds = new StringBuffer();
				//结果
				boolean resultAdd = false;
				
				try{
					synchronized(rptWaitAsList){
						//循环处理状态报告对象集合
						while(rptWaitAsList != null && rptWaitAsList.size() > 0){
							//移除并获取
							rptWaitA = rptWaitAsList.remove(0);
							//没记录，退出
							if(rptWaitA == null){
								break;
							}
							
							report = new LfReport();
							report.setMtmsgid(rptWaitA.getPtMsgId().toString());
							//report.setMtstat(mtstat);
							report.setSpid(rptWaitA.getUserId());
							//report.setSppassword(sppassword);
							report.setMterrorcode(rptWaitA.getErrorCode());
							report.setPhone(rptWaitA.getPhone());
							report.setTaskId(rptWaitA.getUserMsgId());
							report.setUserMsgId(rptWaitA.getUserMsgId());
							report.setModuleId(rptWaitA.getModuleId());
							
							resultAdd = receiveBiz.addReport(report);
							
							if(resultAdd){
								//成功推送，则放进删除列表
								EmpExecutionContext.debug(new SimpleDateFormat("状态报告 ：yyyy-MM-dd HH:mm:ss").format(new Date())+" 发送账号："+rptWaitA.getUserId()+"  报告id："+rptWaitA.getId()+"模块id："+rptWaitA.getModuleId()+"的状态报告加入删除列表");
								rptIds.append(rptWaitA.getId()).append(",");
							}
						}
						
						if(rptIds != null && rptIds.length() > 0){
							
							//rptIds = rptIds.substring(0, rptIds.lastIndexOf(","));
							String strRpt = rptIds.substring(0, rptIds.lastIndexOf(","));
							//删掉已推送的状态报告
							empDao.delete(RptWaitA.class, strRpt);
							
						}
					}

				}catch(Exception e){
					EmpExecutionContext.error(e, "数据库拿取状态报告异常。");
				}

			}
		};
		
		try
		{
			dbRptSF = scheduExec.scheduleWithFixedDelay(task, 1000, DELAY, TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置数据库获取状态报告任务异常。");
		}
	}
	
	/**
	 * 停止接收推送
	 */
	public void stopTimerReceive(){
		if(moSF != null){
			moSF.cancel(true);
			moSF = null;
		}
		if(rptSF != null){
			rptSF.cancel(true);
			rptSF = null;
		}
		if(dbMoSF != null){
			dbMoSF.cancel(true);
			dbMoSF = null;
		}
		if(dbRptSF != null){
			dbRptSF.cancel(true);
			dbRptSF = null;
		}
		
		try
		{
			if(scheduExec != null){
				scheduExec.shutdownNow();
				scheduExec = null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "停止接收推送，停止接收定时任务对象异常。");
		}
		
		if(dataDealThread != null)
		{
			dataDealThread.StopThread();
		}
		
		//实例设置为空
		timerMoHandleInstance = null;
	}
	
	public List<LfMotask> getMosList()
	{
		return mosList;
	}

	public List<LfReport> getRptsList()
	{
		return rptsList;
	}
	
	public List<LfMotask> getOrdersList()
	{
		return ordersList;
	}

	public Map<String, String[]> getSendedsMap()
	{
		return sendedsMap;
	}

	public void setSendedsMap(Map<String, String[]> sendedsMap)
	{
		this.sendedsMap = sendedsMap;
	}
	
}
