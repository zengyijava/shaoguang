package com.montnets.emp.appmage.listener;

import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.appwg.biz.WgMwCommuteBiz;
import com.montnets.emp.appwg.wginterface.IWgMwCommuteBiz;
import com.montnets.emp.common.context.EmpExecutionContext;

public class AppWgListener extends HttpServlet
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public void init() throws ServletException
	{
		try
		{
			ResourceBundle rb = ResourceBundle.getBundle("SystemGlobals");
			String connectDataBaseFlag=rb.getString("ConnectDataBaseFlag");
			//增加配置判断，当第一次加载时候，不连接数据库
			if("0".equals(connectDataBaseFlag)){
				EmpExecutionContext.info("App网关，未配置数据库信息，不初始化");
				return;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "App网关，初始化时检查数据库配置异常。");
		}
		IWgMwCommuteBiz wgBiz = new WgMwCommuteBiz();
		wgBiz.InitMwApp();
		wgBiz.LoginForServiceStart();
	}
	
	/**
	 * 销毁
	 */
	public void destroy() {
		IWgMwCommuteBiz wgBiz = new WgMwCommuteBiz();
		wgBiz.StopAppWg();
	}


}
