package com.montnets.emp.shorturl.surlmanage.thread;

import java.util.Observable;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.shorturl.surlmanage.biz.UrlTimerManagerBiz;
import com.montnets.emp.shorturl.surlmanage.entity.LfUrlTask;

public class UrlSendThread extends Observable implements Runnable {

	protected boolean isThreadExit = true;
	private UrlTimerManagerBiz timerBiz = UrlTimerManagerBiz.getTMInstance();
	
	
	public void run() {
		while (isThreadExit) {
			try {
				
				deal();
			} catch (Exception e) {
				EmpExecutionContext.error(e, "短链任务发送线程异常");
				doBusiness();
			}
		}
		
		
	}
	private void deal() {
		try {
			//使用阻塞方法，没有数据的情况下进行休眠
			LfUrlTask task = timerBiz.getTaskqueue().take();
			timerBiz.execute(task);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "短链任务发送线程执行任务异常");
			
		}
		
	}
	public void doBusiness() {
		if (true) {
			super.setChanged();
		}
		notifyObservers();
	}
	
	public void StopThread()
	{
		try
		{
			this.isThreadExit = false;
					}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, this.getClass().getName()+"停止线程异常。");
		}
	}
	
	protected void SleepTime(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (Exception e)
		{
			EmpExecutionContext.info(this.getClass().getName()+"强制退出休眠等待。");
		}
	}
}
