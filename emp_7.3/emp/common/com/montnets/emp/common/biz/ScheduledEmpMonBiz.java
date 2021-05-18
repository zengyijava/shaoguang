package com.montnets.emp.common.biz;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.montnets.emp.common.context.EmpExecutionContext;

public class ScheduledEmpMonBiz
{
	//任务间隔时间（秒）
	protected static int delay = 30;
	
	public void startTimer()
	{
		ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(1);
		try
		{
			EmpMonBiz memMonBiz = new EmpMonBiz();
			//定时记录EMP服务器监控信息
			scheduExec.scheduleAtFixedRate(memMonBiz, 60, delay, TimeUnit.SECONDS);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动EMP服务器监控信息定时任务异常!");
		}

	}
}
