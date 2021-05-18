package com.montnets.emp.report.listener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.report.biz.RptConfBiz;

public class RptListener extends HttpServlet
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

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
