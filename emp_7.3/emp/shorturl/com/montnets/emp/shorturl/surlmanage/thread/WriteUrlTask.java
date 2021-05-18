package com.montnets.emp.shorturl.surlmanage.thread;

import com.montnets.emp.shorturl.surlmanage.biz.UrlSendBiz;
import com.montnets.emp.shorturl.surlmanage.entity.LfUrlTask;

public class WriteUrlTask implements Runnable {
	
	private LfUrlTask task = null;
	public WriteUrlTask(LfUrlTask task){
		this.task = task;
	}

	public void run() {
		
		new UrlSendBiz().runUrlTask(task);
		
	}

}
