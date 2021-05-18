package com.montnets.emp.zxkf.biz;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.montnets.emp.common.constant.SystemGlobals;


/**
 * 
 * @author Administrator
 * 
 */
public class OttScheduledBiz
{
	private ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(1);
	
	/***
	 * 加载定时同步的时间
	* @Description: TODO
	* @param 
	* @return void
	 */
	public void startTimer()
	{
		MonitorStatus monitorStatus = new MonitorStatus();
		//加载定时同步的时间间隔
		String checkTime = SystemGlobals.getValue("montnets.ott.clientstatussynch.interval");
		int checkTimeInt = checkTime==null?30:Integer.parseInt(checkTime);
		//定时同步配置信息。
		scheduExec.scheduleAtFixedRate(monitorStatus, 30, checkTimeInt, TimeUnit.SECONDS);
	}
}