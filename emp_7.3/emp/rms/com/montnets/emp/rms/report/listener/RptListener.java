package com.montnets.emp.rms.report.listener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.rms.report.biz.RptConfBiz;

public class RptListener extends HttpServlet
{

	/**
	 *@author lvxin 
	 *获取sp账号监听
	 */
	private static final long serialVersionUID = -7868245531905767596L;

	public void init() throws ServletException
	{
		
		String basePath = this.getServletContext().getRealPath("/");
		RptConfBiz.initRptConf(basePath);
		
	}
	
	
	
	/**
	 * 销毁
	 */
	public void destroy() {
		
	}


}
