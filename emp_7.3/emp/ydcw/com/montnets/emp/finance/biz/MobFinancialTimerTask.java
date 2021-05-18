package com.montnets.emp.finance.biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.MontnetTimerTask;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.Logger;

public class MobFinancialTimerTask extends MontnetTimerTask{
	private Long mtId;
	private static Logger logger = Logger.get();
	
	public MobFinancialTimerTask(){
		LfTimer timer = new LfTimer();
		timer.setClassName(this.getClass().getName());
		this.setTeTask(timer);
	}
	
	/**
	 * 
	 * @param taskName
	 * @param time
	 * @param taskExpression
	 */
	public MobFinancialTimerTask(String taskName, Date time, String taskExpression) {
		LfTimer te = new LfTimer();
		te.setClassName(this.getClass().getName());
		te.setTimerTaskName(taskName);
		te.setRunState(1);
		Timestamp tsNextRunTime = Timestamp.valueOf((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(time.getTime()));
		te.setStartTime(tsNextRunTime);
		te.setNextTime(tsNextRunTime);
		te.setRunInterval(0);
		te.setTaskExpression(taskExpression);
		te.setRunPerCount(1);
		
		this.setTeTask(te);

		
	}
	
	public Long getMtId(){
		return mtId;
	}

	public void setMtId(Long mtId){
		this.mtId = mtId;
	}

	@Override
	public boolean taskMethod(boolean arg0)  {
		boolean result = false;
		Long taskid = null;
		try {
			MobFinancialBiz sendMessage = new MobFinancialBiz();
			taskid = Long.parseLong(this.getTeTask().getTaskExpression());
			
			//如果此条发送记录的操作员已禁用，则不进行发送操作
			BaseBiz baseBiz = new BaseBiz();
			//通过taskid查找任务对象
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
			conditionMap.put("taskId", taskid.toString());
			//查找发送任务
			List<LfMttask> mtList = baseBiz.getByCondition(LfMttask.class, conditionMap, null);
			// 判断是否查找到任务
			if(mtList == null || mtList.size() == 0)
			{
				EmpExecutionContext.error("定时执行移动财务短信发送任务时，通过taskid="+taskid+"获取不到任务对象！");
				return false;
			}
			LfMttask mt = mtList.get(0);
			
			//移动财务验证企业是否禁用，禁用的企业不发送移动财务
			conditionMap.clear();
			//设置企业编码查询条件
			conditionMap.put("corpCode", mt.getCorpCode());
			//获取企业信息
			List<LfCorp> corpList = new BaseBiz().getByCondition(LfCorp.class, conditionMap, null);
			if(corpList == null || corpList.size() == 0)
			{
				EmpExecutionContext.error("定时执行移动财务发送任务时，通过corpCode="+mt.getCorpCode()+"获取不到企业对象！taskid:"+taskid);
				return false;
			}
			//企业状态为禁用
			if(corpList.get(0).getCorpState() == 0)
			{
				//企业禁用时，撤销任务，并退费
				sendMessage.ChangeSendState(mt);
				EmpExecutionContext.error("定时执行移动财务发送任务时，企业状态为禁用，corpCode:"+mt.getCorpCode()+"，taskid:"+taskid);
				return false;
			}
			
			LfSysuser user = baseBiz.getById(LfSysuser.class, mt.getUserId());
			//如果此用户的状态是0为禁用，1为启用，2为注销
			if(user.getUserState() != 1)
			{
				sendMessage.ChangeSendState(mt);
				EmpExecutionContext.info("执行定时移动财务短信，操作员状态非启用:taskid="+taskid);
                return false;
			}
			else if(mt.getSendstate() != 0)
			{
				EmpExecutionContext.error("发送任务状态不为未发送,定时任务执行失败！sendstate:"+
						mt.getSendstate().toString() + ";taskid:"+
						mt.getTaskId().toString()+";mtId:"+mt.getMtId().toString());
				return false;
			}
			else
			{
				//获取文件所在的服务器地址
				CommonBiz commonBiz = new CommonBiz();
				String fileUrl = commonBiz.checkServerFile(mt.getMobileUrl());
				if(fileUrl != null && !"".equals(fileUrl))
				{
					mt.setFileuri(fileUrl);
						String str = sendMessage.sendSms(mt,user);
						if(str!=null && str.equals("000")){
							EmpExecutionContext.info("定时任务执行发送成功:taskid="+taskid);
							result= true;
						}else{
							EmpExecutionContext.info("定时任务执行发送失败:taskid="+taskid);
							result= false;
						}
				 }
				else
				{
					EmpExecutionContext.error("定时执行移动财务发送任务时,获取发送文件地址失败！fileUrl:"+fileUrl+";mobileUrl:"+mt.getMobileUrl()
										+ ";taskid:"+mt.getTaskId().toString()+";mtId:"+mt.getMtId().toString());
					  return false;
				 }
			}
		} catch (Exception e) {
			result= false;
			EmpExecutionContext.error(e, "定时任务执行异常!taskid="+taskid);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.montnets.emp.common.timer.MontnetTimerTask#taskMethodForTimeOut(java.lang.String)
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-9-26 上午11:23:48
	 */
	public boolean taskMethodForTimeOut(String taskExpression)
	{
		EmpExecutionContext.info("处理移动财务过期任务空实现。");
		return false;
	}
}
