package com.montnets.emp.netnews.common;

import java.util.Timer;

public class LiTimer
{

	public void initEXE()
	{
		ChkScheduleTask task = new ChkScheduleTask();
		Timer myTimer = new Timer();
		// 第二个参数为执行前等待时间，第三个参数为执行周期，以ms为单位
		// myTimer.schedule(task,1000,24*60*60*1000);
		
		/*
		// 表示在服务启动的当天14:52分开始执行，然后每24小时执行一次
		// 如果服务启动时已大于startDate时间，则立即执行
		myTimer.schedule(task,startDate, 1000);*/
		
		//启动时60s后开始执行，然后每6分钟执行一次
		myTimer.schedule(task, 60*1000L, 6*60L*1000L);
	}

}
