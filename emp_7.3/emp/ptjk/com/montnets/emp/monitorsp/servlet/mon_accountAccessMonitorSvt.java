package com.montnets.emp.monitorsp.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitorsp.MmonUserinfo;
import com.montnets.emp.util.PageInfo;


@SuppressWarnings("serial")
public class mon_accountAccessMonitorSvt extends BaseServlet {
	final String empRoot="ptjk";
	final String base="/monitor";
	
	/**
	 * SP账号监控查询异常
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		PageInfo pageInfo = new PageInfo();
		BaseBiz baseBiz = new BaseBiz();
		try {
			List<MmonUserinfo> monitorList = null;
			pageSet(pageInfo,request);
			monitorList = baseBiz.getByCondition(MmonUserinfo.class, null, null,null, pageInfo);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("monitorList", monitorList);
			request.getRequestDispatcher(this.empRoot+base+"/mon_accountAccessMonitor.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"SP账号监控查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_accountAccessMonitor.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"SP账号监控查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"SP账号监控查询异常！");
			}
		}
	}
}
