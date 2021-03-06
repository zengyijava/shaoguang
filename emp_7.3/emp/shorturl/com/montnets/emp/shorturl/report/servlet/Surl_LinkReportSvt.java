package com.montnets.emp.shorturl.report.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.shorturl.report.biz.SurlTaskReportExcelTool;
import com.montnets.emp.shorturl.report.biz.Surl_LinkVisitReportBiz;
import com.montnets.emp.shorturl.report.vo.LinkReportQueryVo;
import com.montnets.emp.shorturl.report.vo.LinkReportVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;

public class Surl_LinkReportSvt extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5220111312410605308L;

	final String empRoot = "shorturl";

	final String basePath = "/report";
	private final String path = new TxtFileUtil().getWebRoot();
	private final String excelPath = path + "shorturl/report/file";
	final Surl_LinkVisitReportBiz linkReportBiz = new Surl_LinkVisitReportBiz();

	public void find(HttpServletRequest request, HttpServletResponse response) {
		try {

			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String corpCode = lfSysuser.getCorpCode();

			String requestPath = request.getRequestURI();
			String actionPath = requestPath.substring(requestPath.lastIndexOf("/") + 1, requestPath.length());
			String titlePath = requestPath.substring(requestPath.lastIndexOf("_") + 1, requestPath.lastIndexOf("."));
			String lurl = request.getParameter("lurl");
			String subject = request.getParameter("subject");
			String taskId = request.getParameter("taskId");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String recordType = request.getParameter("recordType");

			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo, request);

			LinkReportQueryVo queryVo = new LinkReportQueryVo();

			queryVo.setUrl(lurl);
			queryVo.setTitle(subject);
			queryVo.setTaskId(taskId);
			queryVo.setRecordType(recordType);
			if (!"100000".equals(corpCode)) {
				queryVo.setCorpCode(corpCode);
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (startTime != null && !"".equals(startTime)) {
				queryVo.setStartTime(new Timestamp(sdf.parse(startTime).getTime()));
			} else {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.DAY_OF_MONTH, 1);
				c.set(Calendar.HOUR, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				queryVo.setStartTime(new Timestamp(c.getTime().getTime()));
			}
			if (endTime != null && !"".equals(endTime)) {
				queryVo.setEndTime(new Timestamp(sdf.parse(endTime).getTime()));
			} else {
				queryVo.setEndTime(new Timestamp(new Date().getTime()));
			}

			List<LinkReportVo> linkVisitVoList = linkReportBiz.getLinkReport(queryVo, pageInfo);
			request.setAttribute("linkVisitStatics", linkVisitVoList);

			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("actionPath", actionPath);
			request.setAttribute("titlePath", titlePath);
			request.getRequestDispatcher(this.empRoot + basePath + "/surl_LinkReport.jsp").forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "???????????????????????????????????????");
		}
	}

	public void exportAllReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long sTime = System.currentTimeMillis();
		LinkReportQueryVo queryVo = new LinkReportQueryVo();

		String lurl = request.getParameter("lurl");
		String subject = request.getParameter("subject");
		String taskId = request.getParameter("taskId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String recordType = request.getParameter("recordType");
		LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		String corpCode = lfSysuser.getCorpCode();
		if (!"100000".equals(corpCode)) {
			queryVo.setCorpCode(corpCode);
		}
		PageInfo pageInfo = new PageInfo();
		pageSet(pageInfo, request);

		queryVo.setUrl(lurl);
		queryVo.setTitle(subject);
		queryVo.setTaskId(taskId);
		queryVo.setRecordType(recordType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (startTime != null && !"".equals(startTime)) {
			queryVo.setStartTime(new Timestamp(sdf.parse(startTime).getTime()));
		} else {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			queryVo.setStartTime(new Timestamp(c.getTime().getTime()));
		}
		if (endTime != null && !"".equals(endTime)) {
			queryVo.setEndTime(new Timestamp(sdf.parse(endTime).getTime()));
		} else {
			queryVo.setEndTime(new Timestamp(new Date().getTime()));
		}
		// ????????????
		String result = "false";
		// ??????????????????
		String opContent = "";
		List<LinkReportVo> mtVoList = linkReportBiz.getLinkReport(queryVo, null);
		if (mtVoList != null && mtVoList.size() > 0) {
			SurlTaskReportExcelTool et = new SurlTaskReportExcelTool(excelPath);
			Map<String, String> resultMap = et.createLinkReportExcel(mtVoList, request);
			request.getSession(false).setAttribute("batchSend", resultMap);
			// ??????????????????
			opContent = "?????????????????????????????????????????????????????????";
			result = "true";
		} else {
			opContent = "???????????????";
		}
		SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
		opContent += "?????????" + sdformat.format(sTime) + "????????????" + (System.currentTimeMillis() - sTime) / 1000 + "???????????????" + (mtVoList==null?0:mtVoList.size());
		EmpExecutionContext.info("???????????????????????????????????????", lfSysuser.getCorpCode(), lfSysuser.getUserId() + "", lfSysuser.getUserName(), opContent, "GET");

		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			EmpExecutionContext.error(e, "???????????????????????????????????????");
		}
	}

	/**
	 * EXCEL????????????
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("????????????...");
		// ?????????
		String fileName = "";
		// ????????????
		String filePath = "";
		// ???????????????smssendedbox_export(??????????????????);smstaskrecord_export(??????????????????)???smstaskreply_export??????????????????????????????
		String exportType = "";
		try {
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute("batchSend");
			if (obj != null) {
				Map<String, String> resultMap = (Map<String, String>) obj;
				fileName = resultMap.get("FILE_NAME");
				filePath = resultMap.get("FILE_PATH");
				// ??????????????????
				new DownloadFile().downFile(request, response, filePath, fileName);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "??????????????????EXCEL?????????????????????fileName:" + fileName + "???filePath:" + filePath + "???exportType:" + exportType);
		}
	}
}
