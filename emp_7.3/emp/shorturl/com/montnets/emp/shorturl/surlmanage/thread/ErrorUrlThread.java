package com.montnets.emp.shorturl.surlmanage.thread;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.entity.LfTimeSerCtrl;
import com.montnets.emp.shorturl.comm.constant.UrlGlobals;
import com.montnets.emp.shorturl.surlmanage.biz.UrlTimerManagerBiz;
import com.montnets.emp.shorturl.surlmanage.dao.UrlSendDao;
import com.montnets.emp.shorturl.surlmanage.entity.LfUrlTask;


public class ErrorUrlThread extends UrlThread {
	
	private final UrlTimerManagerBiz timerBiz = UrlTimerManagerBiz.getTMInstance();
	@Override
	public void run() {
		while (isThreadExit) {
			//处理执行失败的短链接发送任务
			
			// 查询定时服务控制表，获取当前定时服务信息
			LfTimeSerCtrl serCtrl = heartbeatBiz.getTimeSerCtrl();
			if(serCtrl == null)
			{
				EmpExecutionContext.error(StaticValue.getServerNumber() + "，获取定时服务控制表记录为空。");
				return;
			}
			// 当前定时服务就是本机定时服务，则执行定时内容
			if(!serCtrl.getTimeServerID().equals(timerBiz2.getProperty().getTimeServerID()))
			{
				return;
			}
			dealv1();
			
			SleepTime(UrlGlobals.getREAD_TIME()*1000L);
		}
	}

	private void dealv1() {
		//从数据库中批量获取未处理或处理失败的任务
		List<LfUrlTask> taskList = new UrlSendDao().getTaskList();
		if (taskList.isEmpty()) {
			EmpExecutionContext.info("一个月内没有失败和未处理的任务");
			return;
		}
		for (LfUrlTask task : taskList) {
			timerBiz.execute(task);
		}
	}

}
