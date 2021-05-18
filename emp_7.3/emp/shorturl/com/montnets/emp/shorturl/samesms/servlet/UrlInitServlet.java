package com.montnets.emp.shorturl.samesms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.constant.PropertiesLoader;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.shorturl.comm.constant.UrlGlobals;
import com.montnets.emp.shorturl.surlmanage.biz.UrlTimerManagerBiz;

public class UrlInitServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UrlInitServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		//加载配置文件参数
		loadProerites();
		//启动所需定时器
		UrlTimerManagerBiz timerBiz = UrlTimerManagerBiz.getTMInstance();
		timerBiz.StartTimer();
		
	}


	private void loadProerites() {
		try
		{
			PropertiesLoader propertiesLoader = new PropertiesLoader();
			Properties properties = propertiesLoader.getProperties(UrlGlobals.URL_SEND_CONFIG);
			UrlGlobals.setProperties(properties);
			UrlGlobals.setCORE_POOL_SIZE(UrlGlobals.getIntValue("montnets.emp.corePoolSize",10));
			UrlGlobals.setMAX_POOL_SIZE(UrlGlobals.getIntValue("montnets.emp.maximumPoolSize",100));
			UrlGlobals.setKEEP_ALIVE_TIME(UrlGlobals.getIntValue("montnets.emp.keepAliveTime",100));
			UrlGlobals.setSHORT_CENTER_URL(UrlGlobals.getValue("montnets.emp.shortUrlCenter"));
			UrlGlobals.setNOTICE_STOP_TIME(UrlGlobals.getLongValue("montnets.emp.noticeTime", 360l));
			UrlGlobals.setREAD_TIME(UrlGlobals.getIntValue("montnets.emp.read.time", 3));
			UrlGlobals.setEMP_NUM(UrlGlobals.getValue("montnets.emp.num","emp0"));
			UrlGlobals.setREAD_COUNT(UrlGlobals.getIntValue("montnets.emp.read.readcount", 100));
			UrlGlobals.setIsExdataEncode(UrlGlobals.getIntValue("montnets.emp.isExtraJsonEncode", 0)); ;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统启动，初始化读取配置信息失败。");
		}
		
	}

}
