package com.montnets.emp.shorturl.surlmanage.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.timer.biz.TimerHeartbeatBiz;
import com.montnets.emp.common.timer.entity.LfTimeSerCtrl;
import com.montnets.emp.shorturl.comm.constant.UrlGlobals;
import com.montnets.emp.shorturl.surlmanage.biz.UrlTimerManagerBiz;
import com.montnets.emp.shorturl.surlmanage.dao.UrlSendDao;
import com.montnets.emp.shorturl.surlmanage.entity.LfUrlTask;

public class UrlThread extends Observable implements Runnable {

	com.montnets.emp.common.timer.TimerManagerBiz timerBiz2 =com.montnets.emp.common.timer.TimerManagerBiz .getTMInstance();
	TimerHeartbeatBiz heartbeatBiz = new TimerHeartbeatBiz();
	private SmsSpecialDAO smsSpecialDAO = new SmsSpecialDAO();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	protected boolean isThreadExit = true;
	public void run() {
		
		while (isThreadExit) {
			
			
			try {
				// 查询定时服务控制表，获取当前定时服务信息
				LfTimeSerCtrl serCtrl = heartbeatBiz.getTimeSerCtrl();
				if(serCtrl == null)
				{
					EmpExecutionContext.error(StaticValue.getServerNumber() + "，获取定时服务控制表记录为空。");
					SleepTime(UrlGlobals.getREAD_TIME()*1000L);
					continue;
				}
				// 当前定时服务就是本机定时服务，则执行定时内容
				if(!serCtrl.getTimeServerID().equals(timerBiz2.getProperty().getTimeServerID()))
				{
					SleepTime(UrlGlobals.getREAD_TIME()*1000L);
					continue;
				}
				deal();
				
				
			} catch (Exception e) {
				EmpExecutionContext.error(e, "短链任务读取线程异常");
				doBusiness();
			}
		}
	}

	private void deal() {
		//从数据库中批量获取未处理的任务
		//不查询失败任务
		List<LfUrlTask> taskList = new UrlSendDao().getTaskList();
		if (taskList.isEmpty()) {
			EmpExecutionContext.info("企业短链->从数据库中批量获取未处理的任务为空,一个月内没有失败和未处理的任务");
			SleepTime(UrlGlobals.getREAD_TIME()*1000L);
			return;
		}
		for (LfUrlTask task : taskList) {
			try {
				//TODO 判断任务是否是定时，是的话未到时间不放入队列
				String status = task.getStatus().toString();
				if (status.indexOf("2")==0) {
					//获取定时时间
					String planTime = sdf.format(task.getPlantime());
					String nowTime = sdf.format(new Date());
					if (!nowTime.equals(planTime)) {
						continue;
					}
				}
				//判断taskId是否已经在Lf_mttask表使用，防止重发
				if (!smsSpecialDAO.checkTaskIdNotUse(task.getTaskId().longValue())) {
					continue;
				}
				//增加一个缓存，存放已经放入队列里的任务id,防止同一任务重复放入
				Long urlTaskId = null;
					urlTaskId = UrlTimerManagerBiz.getTaskmap().get(task.getId());
				if (urlTaskId!=null) {
					continue;
				}
				UrlTimerManagerBiz.getTaskmap().put(task.getId(), task.getId());
				UrlTimerManagerBiz.getTaskqueue().put(task);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "短链接任务添加队列异常");
			}
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
