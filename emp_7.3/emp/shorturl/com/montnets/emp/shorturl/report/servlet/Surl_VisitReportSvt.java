package com.montnets.emp.shorturl.report.servlet;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.shorturl.report.biz.Surl_VisitDetailReportBiz;
import com.montnets.emp.shorturl.report.vo.LinkReportQueryVo;
import com.montnets.emp.shorturl.report.vo.VisitReportVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

public class Surl_VisitReportSvt extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5220111312410605308L;

	final String			empRoot		= "shorturl";

	final String			basePath	= "/report";
	
	final Surl_VisitDetailReportBiz visitReportBiz = new Surl_VisitDetailReportBiz();
	public void find(HttpServletRequest request, HttpServletResponse response){
		try {
			String requestPath = request.getRequestURI();
	        String actionPath=requestPath.substring(requestPath.lastIndexOf("/")+1, requestPath.length());
	        String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));
	        LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String corpCode = lfSysuser.getCorpCode();
			
	        PageInfo pageInfo = new PageInfo();
            boolean isFirstEnter = pageSet(pageInfo,request);

			String phone = request.getParameter("phone");
			String subject = request.getParameter("subject");
			String taskId = request.getParameter("taskId");
			String visitStatus = request.getParameter("visitStatus");
			String visitCount = request.getParameter("visitCount");
			String unicom = request.getParameter("unicom");
			String recordType = request.getParameter("recordType");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			
			LinkReportQueryVo queryVo = new LinkReportQueryVo();
			queryVo.setPhone(phone);
			queryVo.setTitle(subject);
			queryVo.setTaskId(taskId);
			queryVo.setRecordType(recordType);
			queryVo.setVisitStatus(visitStatus);
			if (!"100000".equals(corpCode)) {
				queryVo.setCorpCode(corpCode);
			}
			if(unicom != null && !"".equals(unicom.trim())){
				queryVo.setUnicom(Long.valueOf(unicom));
			}
			if(visitCount != null && !"".equals(visitCount.trim())){
				queryVo.setVisitCount(Long.valueOf(visitCount));
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (startTime != null && !"".equals(startTime)) {
				queryVo.setStartTime(new Timestamp(sdf.parse(startTime).getTime()));
			}else{
				Calendar c = Calendar.getInstance();
				c.set(Calendar.DAY_OF_MONTH, 1);
				c.set(Calendar.HOUR, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				queryVo.setStartTime(new Timestamp(c.getTime().getTime()));
			}
			if (endTime != null && !"".equals(endTime)) {
				queryVo.setEndTime(new Timestamp(sdf.parse(endTime).getTime()));
			}else{
				queryVo.setEndTime(new Timestamp(new Date().getTime()));
			}
            List<VisitReportVo> visitReportVoList = null;
            /**
             * 由于数据量过大，因此控制查询条件中，批次id和手机号码必须至少输入一个条件
             */
			if(!isFirstEnter) {
                if(StringUtils.isNotEmpty(queryVo.getPhone()) || StringUtils.isNotEmpty(queryVo.getTaskId())) {
                    visitReportVoList = visitReportBiz.getVisitReport(queryVo, pageInfo);
                } else {
                    //1.表示提示用户应该输入查询条件
                    request.setAttribute("tips", 1);
                }
            }

			request.setAttribute("visitReportVoList", visitReportVoList);
			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("actionPath", actionPath);
			request.setAttribute("titlePath", titlePath);
            request.setAttribute("isFirstEnter", isFirstEnter);
			request.getRequestDispatcher(this.empRoot + basePath + "/surl_VisitReport.jsp").forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "链接访问统计报表查询异常！");
		}
	}
	
}
