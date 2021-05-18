package com.montnets.emp.common.listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.common.constant.PropertiesLoader;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.Logger;
import com.montnets.emp.util.MonLogger;
import com.montnets.emp.util.TxtFileUtil;


public class WebContextLoaderListener extends HttpServlet implements ServletContextListener {

	private static final long serialVersionUID = -1550181542455735913L;
	
	// @Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		initSystemGlobals();
		initLogger();
		initFolder();
	}

	/**
	 * 初始化系统的一些全局路径
	 */
	private void initFolder() {
		try
		{
			List<String> foldersPath = new ArrayList<String>();
			foldersPath.add(StaticValue.FILEDIRNAME);
			foldersPath.add(StaticValue.FILE_UPLOAD_PATH);
			foldersPath.add(StaticValue.MMS_MATERIAL);
			foldersPath.add(StaticValue.MMS_MTTASKS);
			foldersPath.add(StaticValue.MMS_TEMPLATES);
			foldersPath.add(StaticValue.MMS_PREVIEW);
			foldersPath.add(StaticValue.MMS_TEMPRESOURCE);
			foldersPath.add(StaticValue.MMS_SENDRESOURCE);
			foldersPath.add(StaticValue.MANUAL_SMSTXT);
			foldersPath.add(StaticValue.CLIENT_UPLOAD);
			foldersPath.add(StaticValue.EMP_UPLOAD);
			foldersPath.add(StaticValue.WX_MATER);
			foldersPath.add(StaticValue.WX_PAGE);
			foldersPath.add(StaticValue.WC_ACCOUNT);
			foldersPath.add(StaticValue.WC_RIMG);
			
			//循环初始化文件夹路径
			for (int i = 0; i < foldersPath.size(); i++) {
				String dir = new TxtFileUtil().getWebRoot()+foldersPath.get(i);
				dir = dir.replace('\\', '/');
				File dirFile = new File(dir);
				dirFile.mkdirs();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("系统启动，初始化系统全局路径失败。");
		}
	}

	// @Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		
	}

	/**
	 * 初始化读取配置文件信息
	 */
	private void initSystemGlobals()
	{
		try
		{
			PropertiesLoader propertiesLoader = new PropertiesLoader();
			Properties properties = propertiesLoader.getProperties(SystemGlobals.SYSTEM_GLOBALS_NAME);
			SystemGlobals.setProperties(properties);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("系统启动，初始化读取配置文件信息失败。");
		}
	}
	
	/**
	 * 初始化日志相关信息
	 */
	private void initLogger(){
		try
		{
			Logger logger = Logger.get();
			//日志文件存放地址
			String dir = "";
			//开启自定义路径且路径不为空，使用自定义路径保存日志
			if(StaticValue.LOG_SAVE_PATH != null && StaticValue.LOG_SAVE_PATH.trim().length() > 0 && !"-1".equals(StaticValue.LOG_SAVE_PATH.trim()))
			{
				dir = StaticValue.LOG_SAVE_PATH;
				if(!StaticValue.LOG_SAVE_PATH.endsWith("/")&&!StaticValue.LOG_SAVE_PATH.endsWith("\\"))
				{
					dir += String.valueOf(File.separatorChar);
				}
			}
			else
			{
				dir = new TxtFileUtil().getWebRoot();
			}
			dir += "logger"+File.separatorChar;
			dir = dir.replace('\\', '/');
			File dirFile = new File(dir);
			//创建对应文件夹
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			logger.init(dir, SystemGlobals.getIntValue(StaticValue.EMP_LOG_BUFFERSIZE, 2048));
			
			//系统保存日志文件级别
			String level = SystemGlobals.getValue(StaticValue.EMP_LOG_SAVEFILTERLEVEL);
			if (level != null) {
				if ("LEVEL_ALL".equals(level)) {
					//所有
					logger.setSaveFilter(Logger.LEVEL_ALL);
				} else if ("LEVEL_OFF".equals(level)) {
					//关闭
					logger.setSaveFilter(Logger.LEVEL_OFF);
				} else if ("LEVEL_FATAL".equals(level)) {
					logger.setSaveFilter(Logger.LEVEL_FATAL);
				} else if ("LEVEL_ERROR".equals(level)) {
					//错误
					logger.setSaveFilter(Logger.LEVEL_ERROR);
				} else if ("LEVEL_WARN".equals(level)) {
					logger.setSaveFilter(Logger.LEVEL_WARN);
				} else if ("LEVEL_INFO".equals(level)) {
					logger.setSaveFilter(Logger.LEVEL_INFO);
				} else if ("LEVEL_DEBUG".equals(level)) {
					//异常debug
					logger.setSaveFilter(Logger.LEVEL_DEBUG);
				} else {
				}
			}else{
				logger.setSaveFilter(Logger.LEVEL_OFF);
			}
			
			//是否显示sql
			String isShowSql = SystemGlobals.getValue(StaticValue.EMP_LOG_SHOW_SQL);
			//如果是true，显示，否则不显示
			if(isShowSql != null && "true".equals(isShowSql.trim())){
				logger.setShowSql(true);
			}else{
				logger.setShowSql(false);
			}
			
			EmpExecutionContext.setLogger(logger);
			
			//启动日志线程
			Thread processThread = new Thread(logger);
			processThread.start();
			
			//数据监控报文对象
			MonLogger monLogger = MonLogger.get();
			//设置监控报文对象
			EmpExecutionContext.setDataLogger(monLogger);
			//启动监控报文线程
			new Thread(monLogger).start();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("系统启动，初始始化日志对象失败！");
		}
	}
}
