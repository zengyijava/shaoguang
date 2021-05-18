package com.montnets.emp.common.biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.MontnetTimerTask;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.CheckUtil;

public class SendSmsTimerTask extends MontnetTimerTask
{
	private Long mtId;
	//private EmpDAO empDao=new EmpDAO();
	public SendSmsTimerTask()
	{
		LfTimer timer = new LfTimer();
		timer.setClassName(this.getClass().getName());
		this.setTeTask(timer);
	}

	/**
	 * 祝福短信任务
	 * @param taskName
	 * @param state
	 * @param nextTime
	 * @param runInterval
	 * @param taskExpression
	 */
	public SendSmsTimerTask(String taskName, Integer state, Date nextTime,
			Integer runInterval, String taskExpression)
	{
		LfTimer te = new LfTimer();
		//运行次数（每天定时任务允许运行的次数)
		te.setRunPerCount(1);
		//类名（用于区分类，加载类）
		te.setClassName(this.getClass().getName());
		//任务名
		te.setTimerTaskName(taskName);
		te.setRunState(state);
		Timestamp tsNextRunTime = Timestamp.valueOf((new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss")).format(nextTime.getTime()));
		//开始时间
		te.setStartTime(tsNextRunTime);
		//下次执行时间
		te.setNextTime(tsNextRunTime);
		//下次执行时间
		te.setRunInterval(runInterval);
		//参数表达式,每个参数用 | 该字符隔开
		te.setTaskExpression(taskExpression);
		this.setTeTask(te);
	}
	
	/**
	 * 处理过期任务
	 * @description    
	 * @param taskExpression 任务ID
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-17 上午11:33:04
	 */
	public boolean taskMethodForTimeOut(String taskExpression)
	{
		try
		{
			if(taskExpression != null && !"".equals(taskExpression))
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
				conditionMap.put("taskId", taskExpression);
				List<LfMttask> taskList = new BaseBiz().findListByCondition(LfMttask.class, conditionMap, null);
				if(taskList != null && taskList.size() > 0)
				{
					LfMttask mttask = taskList.get(0);
					if(mttask.getMsType().intValue() == 1 || mttask.getMsType().intValue() == 5)// 短信或移动财务
					{
						BalanceLogBiz biz = new BalanceLogBiz();
						//机构退费
						biz.sendSmsAmountByUserId(null, mttask.getUserId(), -1 * Integer.parseInt(mttask.getIcount()));
						//运营商退费
						biz.huishouFee(Integer.parseInt(mttask.getIcount()), mttask.getSpUser(), mttask.getMsgType());
					}
					LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
					objectMap.put("sendstate", "5");
					return(new BaseBiz().update(LfMttask.class, objectMap, conditionMap));
				}
				return false;
			}
			else
			{
				EmpExecutionContext.error("处理过期任务参数错误！taskId:" + taskExpression);
				return false;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理过期任务异常！taskId:" + taskExpression);
		}
		return false;
	}
	
	/**
	 * 发送定时任务的短信
	 */
	public boolean taskMethod(boolean isAllowRun)
	{
		SendMessage sendMessage = new SendMessage();
		try
		{
			//获取短信任务Id
			String taskid = this.getTeTask().getTaskExpression();
			
			//判断是否有重复发送记录
			TaskManagerBiz timerBiz = new TaskManagerBiz();
			//检查任务是否未成功执行过,返回true表示未执行过
			boolean ckResult = timerBiz.checkIsRun(String.valueOf(mtId));

			if(!ckResult)
			{
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
			if(mtList == null || mtList.size() == 0)
			{
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
			if(corpList == null || corpList.size() == 0)
			{
				EmpExecutionContext.error("定时执行短信发送任务时，通过corpCode="+mt.getCorpCode()+"获取不到任务对象！taskid:"+taskid);
				return false;
			}
			//企业状态为禁用
			if(corpList.get(0).getCorpState() == 0)
			{
				sendMessage.ChangeSendState(mt);
				EmpExecutionContext.error("定时执行短信发送任务时，企业状态为禁用，corpCode:"+mt.getCorpCode()+"，taskid:"+taskid);
				return false;
			}
			
			//如果此条发送记录的操作员已禁用，则不进行发送操作
			LfSysuser user = baseBiz.getById(LfSysuser.class, mt.getUserId());
			boolean checkFlag = new CheckUtil().checkSysuserInCorp(user, mt.getCorpCode(), mt.getSpUser(), null);
			if(!checkFlag)
			{
				EmpExecutionContext.error("定时执行短信发送任务时，检查操作员和发送账号是否是当前企业下，checkFlag:"+checkFlag
											+"，userid:"+user.getUserId()
											+"，spuser:"+mt.getSpUser()
											+"，taskid:"+taskid);
				return false;
			}
			//如果此用户的状态是0：禁用
			if(user.getUserState() == 0)
			{
				sendMessage.ChangeSendState(mt);
				EmpExecutionContext.error("定时执行短信发送任务时，企业状态为禁用，corpCode:"+mt.getCorpCode()+"，taskid:"+taskid);
                return false;
			}else
			// 如果发送状态不为未发送
			if(mt.getSendstate() != 0)
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
					String recResult = sendMessage.sendSms(mt, null);
					if (recResult != null && "000".equals(recResult)) {
						return true;
					}else {
						return false;
					}
				}
				else
				{
					EmpExecutionContext.error("定时执行短信发送任务时,获取发送文件地址失败！fileUrl:"+fileUrl+";mobileUrl:"+mt.getMobileUrl()
										+ ";taskid:"+mt.getTaskId().toString()+";mtId:"+mt.getMtId().toString());
					return false;
				}
			}
			
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "定时框架执行定时任务异常。");
			return false;
		}
		//return true;
	}

	/**
	 * 短信idget方法
	 * @return
	 */
	public Long getMtId()
	{
		return mtId;
	}

	public void setMtId(Long mtId)
	{
		this.mtId = mtId;
	}

}
