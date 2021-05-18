package com.montnets.emp.shorturl.surlmanage.thread;

import java.util.Observable;
import java.util.Observer;

import com.montnets.emp.common.context.EmpExecutionContext;

public class Listener implements Observer{

	public void update(Observable o, Object arg) {
		
	
		
	
		
		if (o instanceof ErrorUrlThread) {
			ErrorUrlThread thread = new ErrorUrlThread();
			thread.addObserver(this);
			new Thread((ErrorUrlThread)o).start();
			EmpExecutionContext.info("重启ErrorUrlThread线程成功");
		}
		
		if (o instanceof UrlThread) {
			UrlThread thread = new UrlThread();
			thread.addObserver(this);
			new Thread((UrlThread)o).start();
			EmpExecutionContext.info("重启UrlThread线程成功");
		}
		
		if (o instanceof UrlSendThread) {
			UrlSendThread thread = new UrlSendThread();
			thread.addObserver(this);
			new Thread((UrlSendThread)o).start();
			EmpExecutionContext.info("重启UrlSendThread线程成功");
		}
	}

}
