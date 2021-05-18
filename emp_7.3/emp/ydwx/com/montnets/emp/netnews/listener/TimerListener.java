package com.montnets.emp.netnews.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.montnets.emp.netnews.common.FileJsp;
import com.montnets.emp.netnews.common.LiTimer;



public class TimerListener implements ServletContextListener {

	/**
	 * 服务停止前，保存缓存网讯访问记录
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		
		 FileJsp.mainTimer(true);
		  
	}
	
	/*****
	 * 网讯定时统计回复信息
	 */
	public void contextInitialized(ServletContextEvent sce) {
		 LiTimer timer = new LiTimer(); 
		 timer.initEXE();//执行方法 

	}
	 

}
