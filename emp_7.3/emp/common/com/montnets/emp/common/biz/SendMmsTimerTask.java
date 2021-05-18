package com.montnets.emp.common.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.timer.MontnetTimerTask;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.common.timer.TimerStaticValue;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfSysuser;

/**
 *  彩信定时任务发送
 * @description 
 * @project p_ydcx
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-10-18 下午02:32:39
 */
public class SendMmsTimerTask extends MontnetTimerTask
{
	//private Long mtId;
	//改成taskid传入值
	private Long taskId;
	

	private IEmpDAO empDao=new DataAccessDriver().getEmpDAO();
	/*private EmpTransactionDAO empTransDao = new EmpTransactionDAO();*/
	private IEmpTransactionDAO empTransDao = new DataAccessDriver().getEmpTransDAO();
	/**
	 * 发送定时彩信任务
	 */
	public SendMmsTimerTask()
	{
		LfTimer timer = new LfTimer();
		timer.setClassName(this.getClass().getName());
		this.setTeTask(timer);
	}

	/**
	 * 发送定时彩信任务
	 * @param taskName
	 * @param state
	 * @param nextTime
	 * @param runInterval
	 * @param taskExpression
	 */
	public SendMmsTimerTask(String taskName, Integer state, Date nextTime,
			Integer runInterval, String taskExpression)
	{
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

	/**
	 * 彩信定时发送
	 *  返回值信息 			
	 * 		    1:彩信回收成功
	 * 			0:彩信扣费成功
	 * 		   -1:彩信扣费失败
	 * 		   -2:彩信余额不足
	 *  	   -4:用户所属机构没有充值
	 *  	   -5:彩信发送或回收条数不能为空
	 *  	   -8:彩信回收已发送条数异常
	 * 		   -9:彩信回收失败
	 * 		-9999:彩信发送扣费回收接口调用异常
	 */
	public boolean taskMethod(boolean isAllowRun)
	{
		BalanceLogBiz balanceLogBiz = new BalanceLogBiz();
		MttaskBiz mttaskBiz = new MttaskBiz();
		try
		{
			taskId =Long.parseLong(this.getTeTask().getTaskExpression());
			//判断是否有重复发送记录
			TaskManagerBiz timerBiz = new TaskManagerBiz();
			boolean ckResult = timerBiz.checkIsRun(String.valueOf(taskId));//检查任务是否未成功执行过,返回true表示未执行过

			if(!ckResult)
			{
				return false;
			}
			//LfMttask lfMttask = empDao.findObjectByID(LfMttask.class, mtId);
			LfMttask lfMttask = new CommonBiz().getLfMttaskbyTaskId(taskId);
			if(lfMttask == null){
				EmpExecutionContext.error("彩信定时任务ID："+taskId+"获取失败！");
				return false;
			}
			if(lfMttask != null && lfMttask.getTimerTime() != null && ((lfMttask.getTimerTime().getTime()+ TimerStaticValue.SYSTEMINTERVAL)<System.currentTimeMillis())){
				lfMttask.setSendstate(5);
				if(SystemGlobals.isDepBilling(lfMttask.getUserId())){
					Connection connection = empTransDao.getConnection();
					try{
						empTransDao.beginTransaction(connection);
						Integer result = balanceLogBiz.sendMmsAmountByUserId(connection, lfMttask.getUserId(), lfMttask.getEffCount()*-1);
						if(result != null && result == 1){
							empTransDao.commitTransaction(connection);
						}else{
							empTransDao.rollBackTransaction(connection);
						}
					}catch (Exception e) {
						 empTransDao.rollBackTransaction(connection);
						 EmpExecutionContext.error(e, "彩信定时发送异常。");
					}finally{
						empTransDao.closeConnection(connection);
					}
					
				}
				empDao.update(lfMttask);
				return false;
			}
			
			//彩信验证企业是否禁用，禁用的企业不发送彩信
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
			//设置企业编码查询条件
			conditionMap.put("corpCode", lfMttask.getCorpCode());
			//获取企业信息
			List<LfCorp> corpList = new BaseBiz().getByCondition(LfCorp.class, conditionMap, null);
			if(corpList == null || corpList.size() == 0)
			{
				EmpExecutionContext.error("定时执行彩信发送任务时，通过corpCode="+lfMttask.getCorpCode()+"获取不到企业对象！taskid:"+taskId);
				return false;
			}
			//企业状态为禁用
			if(corpList.get(0).getCorpState() == 0)
			{
				//企业禁用时，撤销任务，并退费
				mttaskBiz.ChangeSendState(lfMttask);
				EmpExecutionContext.error("定时执行彩信发送任务时，企业状态为禁用，corpCode:"+lfMttask.getCorpCode()+"，taskid:"+taskId);
				return false;
			}
			
			//如果此条发送记录的操作员已禁用，则不进行发送操作
			BaseBiz baseBiz = new BaseBiz();
			//LfMttask mt = baseBiz.getById(LfMttask.class, mtId);
			LfSysuser user = baseBiz.getById(LfSysuser.class, lfMttask.getUserId());
			//如果此用户的状态是0：禁用
			if(user.getUserState() == 0)
			{
				boolean flag = mttaskBiz.ChangeSendState(lfMttask);
                return false;
			}
			else{
				//获取文件所在的服务器地址
				CommonBiz commonBiz = new CommonBiz();
				String fileUrl = commonBiz.checkServerFile(lfMttask.getMobileUrl());
				if(fileUrl != null && !"".equals(fileUrl))
				{
						//调用发送接口
						String returnStr = mttaskBiz.sendMms(taskId,fileUrl);
						//发送不成功，则回收彩信条数
						if(!"000".equals(returnStr)){
							if(SystemGlobals.isDepBilling(lfMttask.getUserId())){
								Integer str= balanceLogBiz.sendMmsAmountByUserId(null, lfMttask.getUserId(), lfMttask.getEffCount()*-1);
			                    if(str != null && str == 1)
			                    {
			                    	return true;
			                    }
			                    else
			                    {
									return false;
			                    }
							}
						}
				}
				else
				{
					EmpExecutionContext.error("定时执行移动彩信发送任务时,获取发送文件地址失败！fileUrl:"+fileUrl+";mobileUrl:"+lfMttask.getMobileUrl()
										+ ";taskid:"+lfMttask.getTaskId().toString()+";mtId:"+lfMttask.getMtId().toString());
					return false;
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "彩信定时发送异常。");
			return false;
		}
		return true;
	}

	/*public Long getMtId()
	{
		return mtId;
	}

	public void setMtId(Long mtId)
	{
		this.mtId = mtId;
	}*/
	public Long getTaskId()
	{
		return taskId;
	}

	public void setTaskId(Long taskId)
	{
		this.taskId = taskId;
	}

	/* (non-Javadoc)
	 * @see com.montnets.emp.common.timer.MontnetTimerTask#taskMethodForTimeOut(java.lang.String)
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-9-26 上午11:23:48
	 */
	public boolean taskMethodForTimeOut(String taskExpression)
	{
		EmpExecutionContext.info("处理移动彩信过期任务空实现。");
		return false;
	}


}
