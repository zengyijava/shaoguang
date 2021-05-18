package com.montnets.emp.common.timer.svt;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TimerManagerBiz;
import com.montnets.emp.common.timer.TimerStaticValue;


@SuppressWarnings("serial")
public class TimerReceSvt extends HttpServlet
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
		String resp = TimerStaticValue.TIME_SER_RESP_ERR;
		
		try
		{
			String senderSerID = request.getParameter("senderSerID");
			String recerSerID = request.getParameter("recerSerID");
			String dealState = request.getParameter("dealState");

			EmpExecutionContext.info("timer receive " 
					+ "senderSerID：" + senderSerID
					+ ",recerSerID：" + recerSerID
					+ ",dealState: " + dealState
					);
			
			
			TimerManagerBiz timerBiz = TimerManagerBiz.getTMInstance();
			
			if(recerSerID == null || recerSerID.trim().length() < 1)
			{
				resp = TimerStaticValue.TIME_SER_RESP_ERR;
			}
			//接收者并不是本机
			else if(!recerSerID.equals(timerBiz.getProperty().getTimeServerID()))
			{
				resp = TimerStaticValue.TIME_SER_RESP_ERR;
			}
			//接管
			else if(dealState != null && TimerStaticValue.TIME_SER_DEAL_STATE_TRUE.equals(dealState))
			{
				timerBiz.getProperty().setIsOtherCtrl(true);
				resp = TimerStaticValue.TIME_SER_RECE;
			}
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e, "timer接收请求，异常。");
		}
		
		try
		{
			OutputStream os = response.getOutputStream();
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/octet-stream");
			response.setContentLength(resp.getBytes().length);
			os.write(resp.getBytes());
			os.close();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "timer接收请求，回应，异常。");
			return;
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
