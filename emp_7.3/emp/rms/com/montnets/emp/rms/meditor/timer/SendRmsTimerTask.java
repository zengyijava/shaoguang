package com.montnets.emp.rms.meditor.timer;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.RmsBalanceLogBiz;
import com.montnets.emp.common.biz.SendMessage;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.MontnetTimerTask;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.rms.LfRmsTaskCtrl;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.meditor.biz.imp.OTTTaskBiz;
import com.montnets.emp.util.CheckUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 富信的定时任务类
 * @author qiy
 */
public class SendRmsTimerTask extends MontnetTimerTask{

	private  Long taskId;
	
	public SendRmsTimerTask() {
		LfTimer timer = new LfTimer();
		timer.setClassName(this.getClass().getName());
		this.setTeTask(timer);
	}
	
	public SendRmsTimerTask(String taskName, Integer state, Date nextTime,
			Integer runInterval, String taskExpression){
		LfTimer te = new LfTimer();
		te.setRunPerCount(1);
		te.setClassName(this.getClass().getName());
		te.setTimerTaskName(taskName);
		te.setRunState(state);
		Timestamp tsNextRunTime = Timestamp.valueOf((new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss")).format(nextTime.getTime()));
		te.setStartTime(tsNextRunTime);
		te.setNextTime(tsNextRunTime);
		te.setRunInterval(runInterval);
		te.setTaskExpression(taskExpression);
		this.setTeTask(te);
	}
	
	@Override
	public boolean taskMethod(boolean isAllowRun) {
		SendMessage sendMessage = new SendMessage();
		boolean resultFlag=false;
		try {
			//获取短信任务Id
			String taskid = this.getTeTask().getTaskExpression();
			
			//判断是否有重复发送记录
			TaskManagerBiz timerBiz = new TaskManagerBiz();
			//检查任务是否未成功执行过,返回true表示未执行过
			boolean ckResult = timerBiz.checkIsRun(String.valueOf(taskId));

			if(!ckResult) {
				EmpExecutionContext.error("定时执行短信发送任务时，检查任务已成功执行过，ckResult："+ckResult+"，taskid="+taskid);
				return false;
			}
			
			BaseBiz baseBiz = new BaseBiz();
			//通过taskid查找任务对象
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
			conditionMap.put("taskId", taskid);
			//查找发送任务
			List<LfMttask> mtList = baseBiz.getByCondition(LfMttask.class, conditionMap, null);
			// 判断是否查找到任务
			if(mtList == null || mtList.size() == 0) {
				EmpExecutionContext.error("定时执行短信发送任务时，通过taskid="+taskid+"获取不到任务对象！");
				return false;
			}
			LfMttask mt = mtList.get(0);
			//重置条件
			conditionMap.clear();
			//设置企业编码查询条件
			conditionMap.put("corpCode", mt.getCorpCode());
			//获取企业信息
			List<LfCorp> corpList = baseBiz.getByCondition(LfCorp.class, conditionMap, null);
			if(corpList == null || corpList.size() == 0) {
				EmpExecutionContext.error("定时执行短信发送任务时，通过corpCode="+mt.getCorpCode()+"获取不到任务对象！taskid:"+taskid);
				//更新任务状态为已撤销
				mt.setSubState(3);
				baseBiz.updateObj(mt);
				return false;
			}
			//企业状态为禁用
			if(corpList.get(0).getCorpState() == 0) {
				sendMessage.ChangeSendState(mt);
				EmpExecutionContext.error("定时执行短信发送任务时，企业状态为禁用，corpCode:"+mt.getCorpCode()+"，taskid:"+taskid);
				//更新任务状态为已撤销
				mt.setSubState(3);
				baseBiz.updateObj(mt);
				return false;
			}
			
			//如果此条发送记录的操作员已禁用，则不进行发送操作
			LfSysuser user = baseBiz.getById(LfSysuser.class, mt.getUserId());
			boolean checkFlag = new CheckUtil().checkSysuserInCorp(user, mt.getCorpCode(), mt.getSpUser(), null);
			if(!checkFlag) {
				EmpExecutionContext.error("定时执行短信发送任务时，检查操作员和发送账号是否是当前企业下，checkFlag:"+checkFlag
											+"，userid:"+user.getUserId()
											+"，spuser:"+mt.getSpUser()
											+"，taskid:"+taskid);
				//更新任务状态为已撤销
				mt.setSubState(3);
				baseBiz.updateObj(mt);
				return false;
			}
			//如果此用户的状态是 0：禁用  2：注销
			if(user.getUserState() == 0 || user.getUserState() == 2) {
				sendMessage.ChangeSendState(mt);
				EmpExecutionContext.error("定时执行短信发送任务时，当前操作员已经禁用或注销，corpCode:"+mt.getCorpCode()+"，taskid:"+taskid + ",userName:" + user.getUserName());
				//更新任务状态为已撤销
				mt.setSubState(3);
				baseBiz.updateObj(mt);
                return false;
			}else if(mt.getSendstate() != 0) {
				// 如果发送状态不为未发送
				EmpExecutionContext.error("发送任务状态不为未发送,定时任务执行失败！sendstate:"+
						mt.getSendstate().toString() + ";taskid:"+
						mt.getTaskId().toString()+";mtId:"+mt.getMtId().toString());
				//更新任务状态为已撤销
				mt.setSubState(3);
				baseBiz.updateObj(mt);
				return false;
			} else {
				if(null != mt.getMobileUrl()&& !"".equals(mt.getMobileUrl())){
					OTTTaskBiz ottTaskBiz = new OTTTaskBiz();
					//String res = ottTaskBiz.sendRms(mt.getTaskId());
					String res = ottTaskBiz.sendTimerRms(mt.getTaskId());
					if(res.contains("sendSuccess")){
						resultFlag= true;
					}
					huishoufee(mt.getTaskId());
				} else {
					EmpExecutionContext.error("定时执行富信发送任务时,获取发送文件地址失败！mobileUrl:"+mt.getMobileUrl()
										+ ";taskid:"+mt.getTaskId().toString()+";mtId:"+mt.getMtId().toString());
					//更新任务状态为已撤销
					mt.setSubState(3);
					baseBiz.updateObj(mt);
					resultFlag= false;
				}
			}
			
			return resultFlag;
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e, "定时框架执行定时任务异常。");
			return false;
		}
	}

	/**
	 * 回收定时短信
	 *@anthor qiyin<15112605627@163.com>
	 *@param taskId
	 */
	private void huishoufee(long taskId)
	{
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
			//获取发送控制表
			conditionMap.put("taskId", taskId+"");
			List<LfRmsTaskCtrl> taskCtrlList=new BaseBiz().findListByCondition(LfRmsTaskCtrl.class, conditionMap, null);
			LfRmsTaskCtrl lfrmstCtrl=null;
			long currentCount=0l;
			if(taskCtrlList!=null&&!taskCtrlList.isEmpty()){
				lfrmstCtrl=taskCtrlList.get(0);
				currentCount=lfrmstCtrl.getCurrentCount();
			}

			CommonBiz common=new CommonBiz();
			LfMttask current = common.getLfMttaskbyTaskId(taskId);
			long huishouFee = current.getEffCount() -currentCount;
			
			if (huishouFee>0)
			{
				RmsBalanceLogBiz rmsBalanceLogBiz = new RmsBalanceLogBiz();
				rmsBalanceLogBiz.huishouFee(Integer.parseInt(String.valueOf(huishouFee)), current.getSpUser(), 1);
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "富信回收余额失败。");
		}
	}

	@Override
	public boolean taskMethodForTimeOut(String taskExpression) {
		EmpExecutionContext.info("处理富信过期任务空实现。");
		return false;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	
	
}
