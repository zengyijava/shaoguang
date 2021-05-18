package com.montnets.emp.wyquery.listener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.wyquery.biz.RptWyConfBiz;

public class RptWyListener extends HttpServlet
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public void init() throws ServletException
	{
		String basePath = this.getServletContext().getRealPath("/");
		RptWyConfBiz.initRptConf(basePath);
	}
	
	
	
	/**
	 * 销毁
	 */
	public void destroy() {
		
	}


}
