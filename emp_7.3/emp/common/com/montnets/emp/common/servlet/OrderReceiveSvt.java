package com.montnets.emp.common.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.receive.TimerReceiveHandle;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.util.ChangeCharset;

@SuppressWarnings("serial")
public class OrderReceiveSvt extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String command = "";
		String momsgid = "";
		String phone = "";
		String spnumber = "";
		String msgContent = "";
		String spid = "";
		String exno="";
		
		OutputStream os = response.getOutputStream();
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/octet-stream");
		String moStat = "ACCEPTD";
		String moErrCode = "000";
		String testCode = "";
		//测试标识，true为该上行为测试，不进行推送
		boolean isTest = false;
		try{
			command = request.getParameter("command");
			if(command == null || "".equals(command.trim()) || "ORDER_TEST".equals(command.toUpperCase().trim()))
			{
				testCode = "&testcode=ordertest";
				isTest = true;
			}
			
			momsgid = request.getParameter("momsgid");
			phone = request.getParameter("sa");
			spnumber = request.getParameter("da");
			msgContent = request.getParameter("sm");
			spid = request.getParameter("spid");
			exno = request.getParameter("exno");//扩展尾号
			if(msgContent != null && !"".equals(msgContent)){
				msgContent = ChangeCharset.toStringHex(msgContent);
			}
		}catch(Exception e){
			moStat = "REJECTD";
			moErrCode = "500";
			EmpExecutionContext.error(e, "svt接收上行信息异常。");
			return;
		}finally{
			//TODO 回应需要修改
			String resp = "command=MO_RESPONSE&spid="+spid+"&momsgid="+momsgid+"&mostat="+moStat+"&moerrcode="+moErrCode+testCode;
			response.setContentLength(resp.getBytes().length);
			
			os.write(resp.getBytes());
			os.close();
		}
		
		if(isTest)
		{
			EmpExecutionContext.debug("测试接收指令，不进行推送!");
			//该上行为测试，不进行推送
			return;
		}
		
		LfMotask moTask = new LfMotask();
		
		try{
			moTask.setPtMsgId(momsgid);
			moTask.setPhone(phone);
			moTask.setSpnumber(spnumber);
			moTask.setMsgContent(msgContent);
			moTask.setSpUser(spid);
			moTask.setDeliverTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			moTask.setSubno(exno);
			EmpExecutionContext.info("order receive spgate："+spnumber+",phone: "+phone+",momsgid: "+momsgid+",msgContent: "+msgContent+",spid: "+spid+",exno: "+exno);
			
			TimerReceiveHandle tmHandle = TimerReceiveHandle.getTMoHandleInstance();
			tmHandle.getOrdersList().add(moTask);
		}catch(Exception e){
			EmpExecutionContext.error(e, "保存上行信息到集合异常。");
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
