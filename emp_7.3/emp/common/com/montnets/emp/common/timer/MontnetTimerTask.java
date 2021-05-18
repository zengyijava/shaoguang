package com.montnets.emp.common.timer;

import java.util.concurrent.ScheduledFuture;

import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.system.LfTimerHistory;

/**
 * 
 * @project sinolife
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-29 下午04:14:17
 * @description 定时器任务类
 */
public abstract class MontnetTimerTask 
{
	//定时线程类构造定时对象
	public MontnetTimerTask(){
		teTask = new LfTimer();
		timerHistory = new LfTimerHistory();
	}
	//定时任务对象
	private LfTimer teTask;
	//定时任务历史
	private LfTimerHistory timerHistory;
	//定时任务对象
	private ScheduledFuture<?> futureTask;
	

	//定时执行抽象方法
	public abstract boolean taskMethod(boolean isAllowRun);
	
	//定时执行抽象方法，超时任务调用
	public abstract boolean taskMethodForTimeOut(String taskExpression);
	
	//获取定时任务对象
	public LfTimer getTeTask()
	{
		//返回定时任务对象
		return teTask;
	}

	//设置定时任务对象
	public void setTeTask(LfTimer teTask)
	{
		this.teTask = teTask;
	}

	//获取定时任务历史对象
	public LfTimerHistory getTimerHistory()
	{
		//返回定时任务历史对象
		return timerHistory;
	}

	//设置定时任务历史对象
	public void setTimerHistory(LfTimerHistory timerHistory)
	{
		this.timerHistory = timerHistory;
	}

	//获取定时任务对象
	public ScheduledFuture<?> getFutureTask()
	{
		//返回定时任务对象
		return futureTask;
	}

	//设置定时任务对象
	public void setFutureTask(ScheduledFuture<?> futureTask)
	{
		this.futureTask = futureTask;
	}

	
}
