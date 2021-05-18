package com.montnets.emp.qyll.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.biz.FlowRptMateThread;
import com.montnets.emp.qyll.biz.LLDetailOrderThread;
import com.montnets.emp.qyll.biz.LlDetailReadThread;

public class FlowSynchronizeTaskListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		EmpExecutionContext.info("正在销毁FlowSynchronizeTimer...");
	}

	public void contextInitialized(ServletContextEvent arg0) {
		new FlowSynchronizeTimer();
		if(StaticValue.ISRUNTIMEJOB == 1){
			EmpExecutionContext.info("正在开启流量读取和订购线程...");
			LlDetailReadThread  read = new LlDetailReadThread();
			LLDetailOrderThread order = new LLDetailOrderThread();
			FlowRptMateThread rpt = new FlowRptMateThread();
			read.start();
			order.start();
			rpt.start();
			EmpExecutionContext.info("流量读取和订购线程启动完成...");
		}
	}
	
}
