package com.montnets.emp.common.biz;

import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 
 * @author Administrator
 * 
 */
public class ScheduledBiz
{
	//corePoolSize由1改成2 modify by tanglili 20210129
	private ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(2);

	public void startTimer()
	{
		try
		{
			TimerLoadCache timerLoadParams = new TimerLoadCache();
			//加载定时同步的时间间隔
			String checkTime = SystemGlobals.getValue("emp.black.check.interval");
			int checkTimeInt = checkTime==null?600:Integer.parseInt(checkTime);
			//定时同步配置信息。
			scheduExec.scheduleWithFixedDelay(timerLoadParams, 600, checkTimeInt, TimeUnit.SECONDS);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动关键字、黑名单、发送账号、号段信息加载定时任务异常!");
		}

		//add by tanglili 20210129
		try
		{
			LoadSysConfThread loadSysConfThread = new LoadSysConfThread();
			//定时同步的时间间隔，2分钟
			long delay =2*60L;
			//定时同步配置信息
			scheduExec.scheduleWithFixedDelay(loadSysConfThread, 1L, delay, TimeUnit.SECONDS);
			EmpExecutionContext.info("启动对LF_CORP_CONF、LF_SYS_PARAM、LF_GLOBAL_VARIABLE表信息定时加载的任务成功！");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动对LF_CORP_CONF、LF_SYS_PARAM、LF_GLOBAL_VARIABLE表信息定时加载的任务失败！");
		}
	}
}