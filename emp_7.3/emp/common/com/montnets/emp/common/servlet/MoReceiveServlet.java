package com.montnets.emp.common.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.receive.TimerReceiveHandle;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfMotask;

@SuppressWarnings("serial")
public class MoReceiveServlet extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String command = "";
		String momsgid = "";
		String phone = "";
		String spnumber = "";
		String msgContent = "";
		String spid = "";
		String exno= "";
		//指令id，对应A_CMD_ROUTE中的ID字段
		String cmdid= "";
		
		OutputStream os = response.getOutputStream();
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/octet-stream");
		String moStat = "ACCEPTD";
		String moErrCode = "000";
		String testCode = "";
		//测试标识，true为该上行为测试，不进行推送
		boolean isTest = false;
		try
		{
			command = request.getParameter("command");
			if(command == null || "".equals(command.trim()) || "MO_TEST".equals(command.toUpperCase().trim()))
			{
				testCode = "&testcode=motest";
				isTest = true;
			}
			
			momsgid = request.getParameter("momsgid");
			phone = request.getParameter("sa");
			spnumber = request.getParameter("da");
			msgContent = request.getParameter("sm");
			
			spid = request.getParameter("spid");
			//扩展尾号
			exno = request.getParameter("exno");
			//获取指令id
			cmdid = request.getParameter("cmdid");
			
			EmpExecutionContext.info("mo receive " 
					+ "command：" + command
					+ ",spgate：" + spnumber
					+ ",phone: " + phone
					+ ",momsgid: " + momsgid
					+ ",msgContent: " + msgContent
					+ ",spid: " + spid
					+ ",exno: " + exno
					+ ",cmdid: " + cmdid
					);
			
			//不是上行命令
			if(command != null && !"MO_REQUEST".equals(command.toUpperCase()))
			{
				EmpExecutionContext.info("mo receive 命令非上行请求。" 
						+ "spgate：" + spnumber
						+ ",phone: " + phone
						+ ",momsgid: " + momsgid
						+ ",msgContent: " + msgContent
						+ ",spid: " + spid
						+ ",exno: " + exno
						+ ",cmdid: " + cmdid
						);
				return;
			}
			
		}
		catch(Exception e){
			moStat = "REJECTD";
			moErrCode = "500";
			EmpExecutionContext.error(e, "svt接收上行信息异常。"
					+ "spgate：" + spnumber
					+ ",phone: " + phone
					+ ",momsgid: " + momsgid
					+ ",msgContent: " + msgContent
					+ ",spid: " + spid
					+ ",exno: " + exno
					+ ",cmdid: " + cmdid);
			return;
		}
		finally
		{
			String resp = "command=MO_RESPONSE&spid="+spid+"&momsgid="+momsgid+"&mostat="+moStat+"&moerrcode="+moErrCode+testCode;
			response.setContentLength(resp.getBytes().length);
			
			os.write(resp.getBytes());
			os.close();
		}
		
		if(isTest)
		{
			//该上行为测试，不进行推送
			return;
		}
		
		try
		{
			LfMotask moTask = new LfMotask();
			moTask.setPtMsgId(momsgid);
			moTask.setPhone(phone);
			moTask.setSpnumber(spnumber);
			/*if(msgContent != null && msgContent.length() > 0)
			{
				msgContent = ChangeCharset.toStringHex(msgContent);
			}*/
			moTask.setMsgContent(msgContent);
			moTask.setSpUser(spid);
			moTask.setDeliverTime(new Timestamp(System.currentTimeMillis()));
			moTask.setSubno(exno);
			moTask.setMoOrder(cmdid);
			
			TimerReceiveHandle tmHandle = TimerReceiveHandle.getTMoHandleInstance();
			tmHandle.getMosList().add(moTask);
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e, "保存上行信息到队列异常。"
					+ "spgate：" + spnumber
					+ ",phone: " + phone
					+ ",momsgid: " + momsgid
					+ ",msgContent: " + msgContent
					+ ",spid: " + spid
					+ ",exno: " + exno
					+ ",cmdid: " + cmdid);
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
			throws ServletException, IOException {
		this.doGet(request, response);

	}

}
