package com.montnets.emp.common.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.receive.TimerReceiveHandle;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfReport;


@SuppressWarnings("serial")
public class RptReceiveServlet extends HttpServlet
{

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
			throws ServletException, IOException
	{

		String command = "";
		String mtmsgid = "";
		String mtstat = "";
		String spid = "";
		String sppassword = null;
		String moduleid="";
		String mterrorcode = "";
		String msgid="";
		String phone="";
		
		OutputStream os = response.getOutputStream();
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/octet-stream");
		String rtstat = "ACCEPTD";
		String rterrcode = "000";
		String testCode = "";
		//测试标识，true为该状态报告为测试，不进行推送
		boolean isTest = false;
		
		TimerReceiveHandle tmHandle = TimerReceiveHandle.getTMoHandleInstance();
		
		try
		{

			EmpExecutionContext.debug("emp当前接收rpt数："+tmHandle.getRptsList().size());
			if(tmHandle.getRptsList().size() > StaticValue.MAX_RPT_COUNT)
			{
				EmpExecutionContext.info("状态报告接收，已达到最大接收量("+StaticValue.MAX_RPT_COUNT+")，不再接收。");
				rtstat="";
				return;
			}
			
			command = request.getParameter("command");
			if(command == null || "".equals(command.trim()) || "RT_TEST".equals(command.toUpperCase().trim()))
			{
				testCode = "&testcode=rpttest";
				isTest = true;
			}
			
			mtmsgid = request.getParameter("mtmsgid");
			mtstat = request.getParameter("mtstat");
			spid = request.getParameter("spid");
			sppassword = request.getParameter("sppassword");
			mterrorcode = request.getParameter("mterrcode");
			moduleid = request.getParameter("moduleid");
			msgid = request.getParameter("msgid");//taskid
			phone = request.getParameter("sa");//电话

			EmpExecutionContext.info("rpt receive " 
					+ "command：" + command
					+ ",mtmsgid：" + mtmsgid
					+ ",spid: " + spid
					+ ",phone: " + phone
					+ ",mtstat: " + mtstat
					+ ",mterrcode:" + mterrorcode
					+ ",msgid: " + msgid
					+ ",moduleid: " + moduleid
					);
			
			//不是状态报告命令
			if(command != null && !"RT_REQUEST".equals(command.toUpperCase()))
			{
				EmpExecutionContext.info("rpt receive 命令非状态报告请求。" 
						+ "command：" + command
						+ ",mtmsgid：" + mtmsgid
						+ ",spid: " + spid
						+ ",phone: " + phone
						+ ",mtstat: " + mtstat
						+ ",mterrcode:" + mterrorcode
						+ ",msgid: " + msgid
						+ ",moduleid: " + moduleid
						);
				return;
			}
			
		}
		catch(Exception e)
		{
			rtstat="";
			//rterrcode = "500";
			EmpExecutionContext.error(e, "状态报告接收，svt接收状态报告异常。"
					+ "mtmsgid：" + mtmsgid
					+ ",spid: " + spid
					+ ",phone: " + phone
					+ ",mtstat: " + mtstat
					+ ",mterrcode:" + mterrorcode
					+ ",msgid: " + msgid
					+ ",moduleid: " + moduleid
					);
			return;
		}
		finally
		{
			
			String resp = "command=RT_RESPONSE&spid="+spid+"&mtmsgid="+mtmsgid+"&rtstat="+rtstat+"&rterrcode="+rterrcode+testCode;
			response.setContentLength(resp.getBytes().length);
				
			os.write(resp.getBytes());
			os.close();
		}
		
		if(isTest)
		{
			//该状态报告为测试，不进行推送
			return;
		}
		//moduleid为null或0时，不做推送
		if(moduleid == null || "0".equals(moduleid.trim()) || moduleid.trim().length() == 0)
		{
			EmpExecutionContext.info("状态报告接收，moduleid为空或0，丢弃。"
					+ "mtmsgid：" + mtmsgid
					+ ",spid: " + spid
					+ ",phone: " + phone
					+ ",mtstat: " + mtstat
					+ ",mterrcode:" + mterrorcode
					+ ",msgid: " + msgid
					+ ",moduleid: " + moduleid
					);
			return;
		}
		
		EmpExecutionContext.debug("开始处理rpt!");
		EmpExecutionContext.debug("开始处理rpt:"+mtmsgid);
		LfReport report = new LfReport();
		
		try{
			report.setMtmsgid(mtmsgid);
			report.setMtstat(mtstat);
			report.setSpid(spid);
			report.setSppassword(sppassword);
			report.setMterrorcode(mterrorcode);
			report.setModuleId(Integer.valueOf(moduleid));
			
			if(msgid != null && msgid.trim().length() > 0)
			{
				report.setTaskId(Long.valueOf(msgid));
			}
			else
			{
				EmpExecutionContext.info("状态报告接收，msgid为空，不设置该值。");
			}
			
			report.setPhone(phone);
			
			tmHandle.getRptsList().add(report);
			
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e, "状态报告接收，保存状态报告到集合异常。mtmsgid："+mtmsgid+",spid: "+spid+",phone: "+phone+",mtstat: "+mtstat+",mterrcode:"+mterrorcode+",msgid: "+msgid+",moduleid: "+moduleid);
		}
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
			throws ServletException, IOException
	{

		this.doGet(request, response);
	}

}
