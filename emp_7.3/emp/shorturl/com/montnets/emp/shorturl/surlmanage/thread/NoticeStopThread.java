package com.montnets.emp.shorturl.surlmanage.thread;

import java.util.List;
import java.util.Map;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TimerManagerBiz;
import com.montnets.emp.common.timer.biz.TimerHeartbeatBiz;
import com.montnets.emp.common.timer.entity.LfTimeSerCtrl;
import com.montnets.emp.shorturl.comm.constant.UrlGlobals;
import com.montnets.emp.shorturl.surlmanage.biz.NoticeStopBiz;
import com.montnets.emp.shorturl.surlmanage.dao.NoticeStopDao;
import com.montnets.emp.shorturl.surlmanage.entity.StopNeturl;
import com.montnets.emp.shorturl.surlmanage.util.HttpClientUtil;


public class NoticeStopThread extends Thread{
	
	// 线程是否继续运行
	private boolean				isThreadExit			= true;
	
	// 线程是否已启动，true为已启动
	private boolean				isThreadStart			= false;
	
	// 空运行次数
	private long				runCount				= 0L;
	
	// 最后一次写日志时间
	private long				lastWriteLogTime		= 0L;
	
	// 最后一次写日志时间
	private long				execLastWriteLogTime	= 0L;
	
	// 写日志间隔
	private int					writeLogInterval		= 30;
	
	// 空运行次数
	private long				execCount				= 0L;
	
	public NoticeStopThread()
	{
		this.setName("URL扫描禁用任务线程");
	}
	
	NoticeStopDao noticeStopDao  = new NoticeStopDao();
	
	NoticeStopBiz noticeStopBiz = new NoticeStopBiz();
	
	@Override
	public void run() {
		this.isThreadStart = true;
		EmpExecutionContext.info(this.getName() + "启动。");

		TimerManagerBiz timerBiz = TimerManagerBiz.getTMInstance();
		
		TimerHeartbeatBiz heartbeatBiz = new TimerHeartbeatBiz();

		while (HttpClientUtil.isSYSTEM_THREAD_RUN_FLAG() && isThreadExit) {
			try {
				Thread.sleep(UrlGlobals.getNOTICE_STOP_TIME() * 1000L);
				long start = System.currentTimeMillis();
				// 查询定时服务控制表，获取当前定时服务信息
				LfTimeSerCtrl serCtrl = heartbeatBiz.getTimeSerCtrl();
				if (serCtrl == null) {
					EmpExecutionContext.error(this.getName() + "，获取定时服务控制表记录为空。");
					continue;
				}
				runCount++;
				// 当前定时服务就是本机定时服务，则执行定时内容
//				if(!serCtrl.getTimeServerID().equals(timerBiz.getProperty().getTimeServerID()))
				if(!serCtrl.getNodeId().equals(timerBiz.getProperty().getNodeId()))
				{
					// 为非主机，超过30分钟记录一次日志
					if(start - lastWriteLogTime >= (writeLogInterval * 60 * 1000))
					{
						EmpExecutionContext.info(this.getName() + "，运行定时服务ID为" + serCtrl.getTimeServerID() + "，本服务器服务ID为：" 
								+ timerBiz.getProperty().getTimeServerID() + "。运行标识为非本节点日志" + writeLogInterval + "分钟记录一次，已持续空运行" + runCount + "次。");
						lastWriteLogTime = start;
					}
					continue;
				}
				runCount = 0;
				lastWriteLogTime = start;
				execCount++;
				// 执行处理方法
				Integer count = this.exec();
				if(count == 0)
				{
					// 处理记录为0，超过30分钟记录一次日志
					if(start - execLastWriteLogTime >= (writeLogInterval * 60 * 1000))
					{
						EmpExecutionContext.info(this.getName() + "，执行定时扫描禁用地址总数为0，空运行日志" + writeLogInterval + "分钟记录一次，已持续空运行" + execCount + "次。");
						execLastWriteLogTime = start;
					}
					continue;
				}
				
				execCount = 0;
				execLastWriteLogTime = start;
				
				EmpExecutionContext.info(this.getName() + "，执行定时扫描禁用地址任务完成，共:" + count + "个地址被禁用，耗时:" + (System.currentTimeMillis() - start) + "ms");
				
			} catch (Exception e) {
				EmpExecutionContext.error(e, this.getName() + "，线程执行异常。");
			}
		}
		// 线程跳出了循环，则设置为未启动
		this.isThreadStart = false;
		
	}
	
	
	
	
	/**
	 * 执行方法
	 * @return
	 */
	private Integer exec() {
		//执行条数
		Integer count = 0;
		
		try {
			//查询客户端禁用的url
			List<StopNeturl> cusUrList = noticeStopDao.findCusUrl();
			//查询运营商端禁用的url
			List<StopNeturl> operUrList = noticeStopDao.findOperUrl();
			
			Integer count1 =  noticeStopBiz.setUrlStop(cusUrList, "-1");
			Integer count2 = noticeStopBiz.setUrlStop(operUrList, "-2");
			count = count1+count2;
		} catch (Exception e) {
			EmpExecutionContext.error(e, this.getName() + "，线程执行禁用禁用异常。");
		}
		return count;
	}



	/**
	 * 启动线程
	 * 
	 * @return 成功返回true
	 */
	public boolean StartThread()
	{
		try
		{
			this.start();
			EmpExecutionContext.info(this.getName() + " 启动成功。");

			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, this.getName() + " 启动失败。");
			return false;
		}
	}
	
	/**
	 * 线程是否已启动
	 * 
	 * @return true为已启动
	 */
	public boolean isThreadStart()
	{
		// 未启动，直接返回
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
			// 中断线程
			interrupt();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "停止" + this.getName() + "，异常。");
		}
	}
}
