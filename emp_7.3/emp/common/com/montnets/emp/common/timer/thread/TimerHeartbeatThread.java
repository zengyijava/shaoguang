package com.montnets.emp.common.timer.thread;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TimerManagerBiz;
import com.montnets.emp.common.timer.TimerStaticValue;
import com.montnets.emp.common.timer.biz.TimerHeartbeatBiz;
import com.montnets.emp.common.timer.entity.LfTimeSerCtrl;


/**
 * 
 * @功能概要：定时服务心跳线程
 * @项目名称： emp_std_192.169.1.81_2new
 * @初创作者： huangzb <huangzb@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-3-23 下午04:42:24
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class TimerHeartbeatThread extends Thread
{
	public TimerHeartbeatThread()
	{
		this.setName("定时服务心跳线程");
	}
	
	private boolean isThreadExit = true;
	//线程是否已启动，true为已启动
	private boolean isThreadStart = false;
	//线程等待时间
	private long threadWaitTime = TimerStaticValue.HEART_BEAT_WAIT_TIME;
	
	private TimerManagerBiz timerBiz = TimerManagerBiz.getTMInstance();
	private TimerHeartbeatBiz heartbeatBiz = new TimerHeartbeatBiz();
	
	public final void run()
	{
		this.isThreadStart = true;
		//等待10s
		this.SleepTime(threadWaitTime);
		
		while(this.isThreadExit)
		{
			deal();
			
			//等待10s
			this.SleepTime(threadWaitTime);
		}
		//线程跳出了循环，则设置为未启动
		this.isThreadStart = false;
	}
	
	private void deal()
	{
		try
		{
			//计算定时服务主线程未更新活动时间差
			long activeTime = System.currentTimeMillis() - timerBiz.getProperty().getTimerActiveTime();
			//服务主线程超过60s仍没更新活动时间
			if(activeTime > TimerStaticValue.TIME_SER_ACTIVE_TIME)
			{
				EmpExecutionContext.info("定时服务心跳，服务主线程停止更新活动时间达到"+activeTime+"ms，超过"+TimerStaticValue.TIME_SER_ACTIVE_TIME+"ms的系统值。");
				return;
			}
			
			//查询定时服务控制表，获取当前定时服务信息
			LfTimeSerCtrl serCtrl = heartbeatBiz.getTimeSerCtrl();
			if(serCtrl == null)
			{
				EmpExecutionContext.error("定时服务心跳，获取定时服务控制表记录为空。");
				return;
			}
			
			//心跳时间差。数据库当前时间-上次更新时间
			long heartbeatTime = serCtrl.getDbCurrentTime().getTime() - serCtrl.getUpdateTime().getTime();
			//未超时， 且当前定时服务不是本机定时服务。不管
			if(heartbeatTime >= 0 && heartbeatTime < TimerStaticValue.HEART_BEAT_ACTIVE_TIME && !serCtrl.getTimeServerID().equals(timerBiz.getProperty().getTimeServerID()))
			{
				return;
			}
			
			//更新的参数
			LfTimeSerCtrl updateObj = new LfTimeSerCtrl();
			
			//更新条件
			LfTimeSerCtrl conditionObj = new LfTimeSerCtrl();
			
			//当前定时服务就是本机定时服务，则只更新活动时间
			if(serCtrl.getTimeServerID().equals(timerBiz.getProperty().getTimeServerID()))
			{
				conditionObj.setTimeServerID(timerBiz.getProperty().getTimeServerID());
			}
			//当前定时服务不是本机定时服务，更新定时服务ID为本机定时服务id
			else
			{
				updateObj.setTimeServerID(timerBiz.getProperty().getTimeServerID());
				updateObj.setNodeId(timerBiz.getProperty().getNodeId());
				updateObj.setServerIP(timerBiz.getProperty().getServerIp());
				updateObj.setServerPort(timerBiz.getProperty().getServerPort());
				conditionObj.setTimeServerID(serCtrl.getTimeServerID());
				
				EmpExecutionContext.info("定时服务心跳，" 
						+ "本机TimeServerID=" + timerBiz.getProperty().getTimeServerID()
						+ "。其他定时服务超时：" 
						+ "TimeServerID="+serCtrl.getTimeServerID() 
						+ ",UpdateTime="+serCtrl.getUpdateTime() 
						+ ",DbCurrentTime="+serCtrl.getDbCurrentTime()
						+ ",NodeId="+serCtrl.getNodeId() 
						+ ",ServerIP="+serCtrl.getServerIP()
						+ ",ServerPort="+serCtrl.getServerPort()
						);
				
			}
			boolean updateRes = heartbeatBiz.UpdateTimeSerCtrl(updateObj, conditionObj);
			//更新成功
			if(updateRes)
			{
				return;
			}
			//更新失败
			EmpExecutionContext.info("定时服务心跳，" 
					+ "本机TimeServerID=" + timerBiz.getProperty().getTimeServerID()
					+ "。更新失败前数据：" 
					+ "TimeServerID="+serCtrl.getTimeServerID() 
					+ ",UpdateTime="+serCtrl.getUpdateTime() 
					+ ",NodeId="+serCtrl.getNodeId() 
					+ ",ServerIP="+serCtrl.getServerIP()
					+ ",ServerPort="+serCtrl.getServerPort()
					);
			
			//查询定时服务控制表更新后的数据
			LfTimeSerCtrl serCtrlAfter = heartbeatBiz.getTimeSerCtrl();
			if(serCtrlAfter == null)
			{
				EmpExecutionContext.info("定时服务心跳，" 
						+ "本机：TimeServerID=" + timerBiz.getProperty().getTimeServerID()
						+ "。更新失败后数据：查询当前定时服务控制表数据失败。" 
						);
				return;
			}
			EmpExecutionContext.info("定时服务心跳，" 
					+ "本机TimeServerID=" + timerBiz.getProperty().getTimeServerID()
					+ "。更新失败后数据：" 
					+ "TimeServerID="+serCtrlAfter.getTimeServerID() 
					+ ",UpdateTime="+serCtrlAfter.getUpdateTime() 
					+ ",NodeId="+serCtrlAfter.getNodeId() 
					+ ",ServerIP="+serCtrlAfter.getServerIP()
					+ ",ServerPort="+serCtrlAfter.getServerPort()
					);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, this.getName()+"，处理，异常。TimeServerID=" + timerBiz.getProperty().getTimeServerID());
		}
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
