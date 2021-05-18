package com.montnets.emp.common.biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.MontnetTimerTask;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.entity.system.LfTimer;

public class SendBirthDateTimerTask extends MontnetTimerTask
{
	
	public SendBirthDateTimerTask()
	{
		LfTimer timer = new LfTimer();
		timer.setClassName(this.getClass().getName());
		this.setTeTask(timer);
	}
    /**
     * 设置定时
     * @param taskName
     * @param state
     * @param nextTime
     * @param runInterval
     * @param taskExpression
     */
	public SendBirthDateTimerTask(String taskName, Integer state, Date nextTime,
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
     * 定时任务
     */
	public boolean taskMethod(boolean isAllowRun)
	{
		
		try
		{
			String[] idArray = this.getTeTask().getTaskExpression().split("\\|");//格式为：BD|12

			if(idArray == null || "".equals(idArray[1].trim())){
				EmpExecutionContext.error("定时执行祝福失败，id为空");
				return false;
			}

			Long id =Long.parseLong(idArray[1]);

			//判断是否有重复发送记录
			TaskManagerBiz timerBiz = new TaskManagerBiz();
			boolean ckResult = timerBiz.checkIsRun(String.valueOf(id));//检查任务是否未成功执行过,返回true表示未执行过

			if(!ckResult)
			{
				return false;
			}
            //发送 
			String recResult = new SendBirthDateBiz().sendSms(id);
			if (recResult != null && "000".equals(recResult)) 
			{
				return true;
			}else 
			{
				return false;
			}
			
		} 
		catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "生日祝福执行定时任务异常。");
			return false;
		}
		//return true;
	}
	/* (non-Javadoc)
	 * @see com.montnets.emp.common.timer.MontnetTimerTask#taskMethodForTimeOut(java.lang.String)
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-9-26 上午11:23:48
	 */
	public boolean taskMethodForTimeOut(String taskExpression)
	{
		EmpExecutionContext.info("处理生日祝福过期任务空实现。");
		return false;
	}

}
