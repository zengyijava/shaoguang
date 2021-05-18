package com.montnets.emp.common.servlet;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 上传进度条控制
 * @author Administrator
 *
 */
public class LoadingServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	public void doPost1(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		//如果获取不到文件大小及文件地址，则返回0
		if (session == null || session.getAttribute("fileSize") == null || session.getAttribute("fileUrl") == null)
		{
			try {
				//输出结果
				response.getWriter().print("0.00");
			} catch (IOException e) {
				EmpExecutionContext.error(e, "获取文件大小及文件地址并输出结果异常。");
			}
			return;
		}
		else
		{
			//获取文件大小
			long size = Long.parseLong(session.getAttribute("fileSize").toString());
			//格式化浮点数
			DecimalFormat percentFormat = new DecimalFormat( "#0.00");
			//判断是否为空文件
			if (size != 0)
			{
				//获取文件路径
				File[] f = (File[])session.getAttribute("fileUrl");
				try {
					response.reset();
					//有效号码文件地址
					long l1 = f[0].length();
					//被过滤号码文件地址
					long l2 = f[1].length();
					//如文件总和大于上传文件，则使总大小更新为新的值
					if ((l1+l2) > size)
					{
						size = (l1+l2);
					}
					//计算进度百分比
					String s = percentFormat.format(((l1+l2)*100/(size*1.0))).toString();
					//输出结果
					response.getWriter().print(s);
				} catch (IOException e) {
 					EmpExecutionContext.error(e, "获取文件大小及文件地址异常。");
				}
			}
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		this.doPost(request,response);
	}
	/*
	 * 短信群发监控服务器是否出现故障
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)  {
		try {
			response.getWriter().write("true");
		} catch (IOException e) {
			EmpExecutionContext.error(e, "短信群发监控服务器是否出现故障异常。");
		}
	}
}
