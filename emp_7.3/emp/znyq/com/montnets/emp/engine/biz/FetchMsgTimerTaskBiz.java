package com.montnets.emp.engine.biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.MontnetTimerTask;
import com.montnets.emp.entity.system.LfTimer;


public class FetchMsgTimerTaskBiz extends MontnetTimerTask
{

	public FetchMsgTimerTaskBiz()
	{
		//出事话定时类
		LfTimer timer = new LfTimer();
		//设置定时classname
		timer.setClassName(this.getClass().getName());
		//设置任务对象
		this.setTeTask(timer);
	}

	/**
	 * 
	 * @param taskName
	 * @param state
	 * @param nextTime
	 * @param runInterval
	 * @param taskExpression
	 */
	public FetchMsgTimerTaskBiz(String taskName, Integer state, Date nextTime,
			Integer runInterval, String taskExpression, Long customInterval,
			Integer isRerun, Integer intervalUnit)
	{
		//定时任务对象
		LfTimer te = new LfTimer();
		//设置classname
		te.setClassName(this.getClass().getName());
		te.setTimerTaskName(taskName);
		//设置运行状态
		te.setRunState(state);
		Timestamp tsNextRunTime = Timestamp.valueOf((new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss")).format(nextTime.getTime()));
		te.setStartTime(tsNextRunTime);
		//设置执行时间
		te.setNextTime(tsNextRunTime);
		te.setRunInterval(runInterval);
		//设置表达式
		te.setTaskExpression(taskExpression);
		te.setRunPerCount(1);
		//设置单位
		te.setIntervalUnit(intervalUnit);
		if (customInterval != null && customInterval != 0L)
		{
			//有运行间隔则设置
			te.setCustomInterval(customInterval);
		} else
		{
			// 设置自定义时间差为24h
			te.setCustomInterval(1 * 3600 * 1000L);
		}
		//设置运行状态
		te.setIsRerun(isRerun);
		//设置回初始化好的定时对象
		this.setTeTask(te);

	}

	/**
	 * 定时执行下行服务业务
	 */
	public boolean taskMethod(boolean isAllowRun)
	{
		EmpExecutionContext.info("下行业务任务执行，开始。"
				+ "serId=" + getTeTask().getTaskExpression()
		);
		try
		{
			if (!isAllowRun)
			{
				//是否允许运行
				return isAllowRun;
			}
			
			if(this.getTeTask().getTaskExpression() == null)
			{
				EmpExecutionContext.error("下行业务任务执行，失败，serId为空，结束。");
				return false;
			}
			
			//获取保存的id,格式为：ser|1
			String[] strSerIdArrays = this.getTeTask().getTaskExpression().split("\\|");

			if (strSerIdArrays == null || strSerIdArrays.length != 2 || "".equals(strSerIdArrays[1].trim()))
			{
				//id为空则不执行
				EmpExecutionContext.error("下行业务任务执行，失败，获取serId异常，serId为空，结束。"
						+ "TaskExpression=" + this.getTeTask().getTaskExpression()
				);
				return false;
			}

			//从数据库获取任务对象
			Long lSerId = Long.parseLong(strSerIdArrays[1]);
			FetchSendMsgBiz fetchSendBiz = new FetchSendMsgBiz();
			//执行任务并得到执行结果
			boolean result = fetchSendBiz.startService(lSerId);
			
			EmpExecutionContext.info("下行业务任务执行。serId="+lSerId+"，结束");
			//返回结果
			return result;
		} catch (Exception e)
		{
			//捕获异常并打印
			EmpExecutionContext.error(e,"下行业务任务执行，执行异常。"
					+ "serId=" + getTeTask().getTaskExpression()		
			);
			return false;
		}
		
	}

	public boolean taskMethodForTimeOut(String taskExpression)
	{
		EmpExecutionContext.info("智能引擎定时任务过期不需要做处理。");
		return true;
	}

}
