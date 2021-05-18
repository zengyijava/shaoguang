package com.montnets.emp.monitorpas.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitorpas.MmonSpateinfo;
import com.montnets.emp.util.PageInfo;

@SuppressWarnings("serial")
public class mon_accountGateMonitorSvt extends BaseServlet {
	final String empRoot="ptjk";
	final String base="/monitor";
	
	/**
	 * 通道账号监控查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		PageInfo pageInfo = new PageInfo();
		BaseBiz baseBiz = new BaseBiz();
		try {
			List<MmonSpateinfo> monitorList = null;
			pageSet(pageInfo,request);
			monitorList = baseBiz.getByCondition(MmonSpateinfo.class, null, null,null, pageInfo);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("monitorList", monitorList);
			request.getRequestDispatcher(this.empRoot+base+"/mon_accountGateMonitor.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通道账号监控查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_accountGateMonitor.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"通道账号监控查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"通道账号监控查询异常！");
			}
		}
	}
}
