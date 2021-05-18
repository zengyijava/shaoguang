package com.montnets.emp.common.timer.thread;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TimerManagerBiz;
import com.montnets.emp.common.timer.TimerStaticValue;


/**
 * 
 * @功能概要：定时服务线程
 * @项目名称： emp_std_192.169.1.81_2new
 * @初创作者： huangzb <huangzb@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-3-24 下午04:30:24
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class TimeSerThread extends Thread
{
	public TimeSerThread()
	{
		this.setName("定时服务线程");
	}
	
	//线程是否继续运行
	private boolean isThreadExit = true;
	//线程是否已启动，true为已启动
	private boolean isThreadStart = false;
	//线程等待时间
	private long threadWaitTime = TimerStaticValue.EXECUTEINTERVAL;
	
	private TimerManagerBiz timerBiz = TimerManagerBiz.getTMInstance();
	
	
	public final void run()
	{
		this.isThreadStart = true;
		//等待10s
		this.SleepTime(TimerStaticValue.LOADDELAY);
		
		while(this.isThreadExit)
		{
			timerBiz.continuteTimerTask();
			
			//等待
			this.SleepTime(threadWaitTime);
		}
		//线程跳出了循环，则设置为未启动
		this.isThreadStart = false;
	}
	
	private void SleepTime(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (Exception e)
		{
			EmpExecutionContext.info(this.getName()+"，强制退出休眠等待。");
		}
	}
	
	/**
	 * 启动线程
	 * @return 成功返回true
	 */
	public boolean StartThread()
	{
		try
		{
			this.start();
			
			EmpExecutionContext.info(this.getName()+" 启动成功。");
			
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, this.getName()+" 启动失败。");
			return false;
		}
	}
	
	/**
	 * 线程是否已启动
	 * @return true为已启动
	 */
	public boolean isThreadStart()
	{
		//未启动，直接返回
		if(!this.isThreadStart)
		{
			return false;
		}
		if(!this.isAlive())
		{
			return false;
		}
		
		return true;
	}

	/**
	 * 停止线程
	 */
	public void StopThread()
	{
		try
		{
			this.isThreadExit = false;
			//中断线程
			interrupt();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "停止" + this.getName() + "，异常。");
		}
	}
	
}
