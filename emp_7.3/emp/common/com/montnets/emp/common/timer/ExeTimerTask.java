package com.montnets.emp.common.timer;

import com.montnets.emp.entity.system.LfTimer;

/**
 * 定时任务类，用于线程池执行任务
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-10-14 下午07:16:52
 * @description
 */
public class ExeTimerTask implements Runnable
{
	//需要执行的任务对象
	private LfTimer timerTask = null;
	
	public ExeTimerTask(LfTimer lfTimer){
		timerTask = lfTimer;
	}
	
	public void run(){
		//任务设置为繁忙
		TimerManagerBiz.setTaskRunStateBusy(timerTask.getTimerTaskId());
		
		new TaskBiz().runTask(timerTask);
		
		//任务设置为空闲
		TimerManagerBiz.setTaskRunStateFree(timerTask.getTimerTaskId());
	}
}
