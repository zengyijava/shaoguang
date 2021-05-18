package com.montnets.emp.monitorsys.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitorsys.MmonBufinfo;
import com.montnets.emp.entity.monitorsys.MmonSysinfo;
import com.montnets.emp.util.PageInfo;


@SuppressWarnings("serial")
public class mon_systemRuntimerMonitorSvt extends BaseServlet {
	final String empRoot="ptjk";
	final String base="/monitor";
	
	/**
	 * 查询系统运行监控信息
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		PageInfo pageInfo = new PageInfo();
		BaseBiz baseBiz = new BaseBiz();
		try {
			pageSet(pageInfo,request);
			List<MmonBufinfo> monitorList = baseBiz.getByCondition(MmonBufinfo.class, null, null,null, pageInfo);
			Iterator<MmonBufinfo> itr = monitorList.iterator();
			MmonBufinfo bufinfo = null;
			if(itr.hasNext()){
				bufinfo = itr.next();
			}
			request.setAttribute("bufinfo", bufinfo);
			List<MmonSysinfo> sysInfoList = baseBiz.getByCondition(MmonSysinfo.class, null, null,null, pageInfo);
			Iterator<MmonSysinfo> bufInfoItr = sysInfoList.iterator();
			MmonSysinfo sysinfo = null;
			if(bufInfoItr.hasNext()){
				sysinfo = bufInfoItr.next();
			}
			request.setAttribute("sysinfo", sysinfo);
			request.getRequestDispatcher(this.empRoot+base+"/mon_systemRuntimerMonitor.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询系统运行监控信息异常！");
			request.setAttribute("findresult", "-1");
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_systemRuntimerMonitor.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"查询系统运行监控信息异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"查询系统运行监控信息异常！");
			}
		}
	}
}
