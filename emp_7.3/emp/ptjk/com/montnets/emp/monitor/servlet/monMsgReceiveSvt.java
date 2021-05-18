package com.montnets.emp.monitor.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.monitor.biz.monMsgReceiveBiz;
import com.montnets.emp.monitor.constant.MonitorStaticValue;

public class monMsgReceiveSvt extends HttpServlet
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	monMsgReceiveBiz			monMsgReceiveBiz	= new monMsgReceiveBiz();

	SmsBiz						smsBiz				= new SmsBiz();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			response.getWriter().print("Receiving Get request is successful");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "monMsgReceiveSvt doGet请求返回异常！");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 是否加载监控模块，16：监控
		if(smsBiz.isWyModule("16"))
		{
			try
			{
				//初始化加载完成
				if(MonitorStaticValue.isLoadFinish())
				{
					// 消息流
					InputStream monitorMsgStream = null;
					try
					{
						// 获取消息流
						monitorMsgStream = request.getInputStream();
						// 消息处理结果，0:成功；-1：消息流转字符串异常；-2：消息内容为空；-3：解密异常；-4：消息入列失败；-99：未知异常
						int processResult = 0;
						if(monitorMsgStream != null)
						{
							// 0:成功；-1：消息流转字符串异常；-2：消息内容为空；-3：解密异常；-4：消息入列失败；-99：未知异常
							processResult = monMsgReceiveBiz.processMonitorMsg(monitorMsgStream);
							if(0 != processResult)
							{
								EmpExecutionContext.error("监控消息处理失败，返回错误码：" + processResult);
							}
						}
						else
						{
							EmpExecutionContext.error("监控消息内容为NULL！");
						}
					}
					catch (Exception e)
					{
						EmpExecutionContext.error(e, "处理监控消息异常！");
					}
					finally
					{
						if(null != monitorMsgStream)
						{
							// 关闭流
							try
							{
								monitorMsgStream.close();
							}
							catch (Exception e)
							{
								EmpExecutionContext.error(e, "接收监控消息关闭流异常！");
							}
						}
					}
				}
				else
				{
					EmpExecutionContext.info("接收监控消息未处理，监控初始化未加载完成。");
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "接收处理监控消息异常。");
			}					
			finally
			{
				//回应包
				response.getWriter().print("EMP Receive Success");
			}
		}
	}

}
