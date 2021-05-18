package com.montnets.emp.shorturl.report.servlet;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.shorturl.report.biz.Surl_VisitDetailReportBiz;
import com.montnets.emp.shorturl.report.vo.VisitDetailVo;
import com.montnets.emp.util.PageInfo;

public class Surl_VisitDetailSvt extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5220111312410605308L;

	final String empRoot = "shorturl";

	final String basePath = "/report";

	final Surl_VisitDetailReportBiz visitReportBiz = new Surl_VisitDetailReportBiz();

	public void find(HttpServletRequest request, HttpServletResponse response) {
		try {
			String taskId = request.getParameter("taskId");
			String phone = request.getParameter("phone");
			String sendTime = request.getParameter("sendTime");
			String title = request.getParameter("title");
			title = URLDecoder.decode(title,"UTF-8");

			if (taskId != null && !"".equals(taskId) && phone != null && !"".equals(phone) && sendTime != null && !"".equals(sendTime)) {
				String requestPath = request.getRequestURI();
				String actionPath = requestPath.substring(requestPath.lastIndexOf("/") + 1, requestPath.length());
				String titlePath = requestPath.substring(requestPath.lastIndexOf("_") + 1, requestPath.lastIndexOf("."));

				PageInfo pageInfo = new PageInfo();
				pageSet(pageInfo, request);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date sd = sdf.parse(sendTime);
				Calendar c = Calendar.getInstance();
				c.setTime(sd);
				int mon = c.get(Calendar.MONTH) + 1;
				String month = "";
				if (mon < 10) {
					month = "0" + mon;
				} else {
					month = "" + mon;
				}
				String yearMonth = c.get(Calendar.YEAR) + month;

				List<VisitDetailVo> linkVisitStatics = visitReportBiz.getVisitDetail(phone, taskId, yearMonth, pageInfo);

				request.setAttribute("visitReportVoList", linkVisitStatics);

				request.setAttribute("title", title);
				request.setAttribute("pageInfo", pageInfo);
				request.setAttribute("actionPath", actionPath);
				request.setAttribute("titlePath", titlePath);
				request.getRequestDispatcher(this.empRoot + basePath + "/surl_VisitDetail.jsp").forward(request, response);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "链接访问统计报表查询异常！");
		}
	}

}
