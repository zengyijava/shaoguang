package com.montnets.emp.shorturl.report.servlet;

import java.io.IOException;
import java.net.URLDecoder;
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
import com.montnets.emp.shorturl.report.biz.Surl_VisitDetailReportBiz;
import com.montnets.emp.shorturl.report.vo.LinkDetailVo;
import com.montnets.emp.shorturl.report.vo.LinkReportQueryVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;

public class Surl_LinkDetailSvt extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5220111312410605308L;

	final String empRoot = "shorturl";

	final String basePath = "/report";
	private final String path = new TxtFileUtil().getWebRoot();
	private final String excelPath = path + "shorturl/report/file";

	final Surl_LinkVisitReportBiz linkDetailBiz = new Surl_LinkVisitReportBiz();
	final Surl_VisitDetailReportBiz biz = new Surl_VisitDetailReportBiz();

	public void find(HttpServletRequest request, HttpServletResponse response) {
		try {
			String requestPath = request.getRequestURI();
			String actionPath = requestPath.substring(requestPath.lastIndexOf("/") + 1, requestPath.length());
			String titlePath = requestPath.substring(requestPath.lastIndexOf("_") + 1, requestPath.lastIndexOf("."));
			String url = request.getParameter("url");

			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo, request);
			List<LinkDetailVo> linkDetailVoList = null;
			if (null != url && !"".equals(url)) {
//				url = URLDecoder.decode(url, "UTF-8");
				//接收taskId,修改人moll
				String taskId = request.getParameter("taskId");

				String phone = request.getParameter("phone");
				String visitStatus = request.getParameter("visitStatus");
				String visitIP = request.getParameter("visitIP");
				String startTime = request.getParameter("startTime");
				String endTime = request.getParameter("endTime");

				LinkReportQueryVo queryVo = new LinkReportQueryVo();
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				String corpCode = lfSysuser.getCorpCode();
				if(!"100000".equals(corpCode)){
					queryVo.setCorpCode(corpCode);
				}
				//添加taskId到条件中,修改人moll
				if(StringUtils.isNotEmpty(taskId)){
					queryVo.setTaskId(taskId);
				}
				queryVo.setUrl(url);
				queryVo.setPhone(phone);
				queryVo.setVisitStatus(visitStatus);
				queryVo.setLastIP(visitIP);

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

				linkDetailVoList = linkDetailBiz.getLinkDetail(queryVo, pageInfo);
			}
			request.setAttribute("linkVisitStatics", linkDetailVoList);

			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("actionPath", actionPath);
			request.setAttribute("titlePath", titlePath);
			request.getRequestDispatcher(this.empRoot + basePath + "/surl_LinkDetail.jsp").forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "链接访问统计详情查询异常！");
		}
	}

	public void exportAllReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = request.getParameter("url");
		long sTime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		pageSet(pageInfo, request);
		List<LinkDetailVo> mtVoList = null;
		if (null != url && !"".equals(url)) {
			url = URLDecoder.decode(url, "UTF-8");

			String phone = request.getParameter("phone");
			String visitStatus = request.getParameter("visitStatus");
			String visitIP = request.getParameter("visitIP");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");

			LinkReportQueryVo queryVo = new LinkReportQueryVo();
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String corpCode = lfSysuser.getCorpCode();
			if(!"100000".equals(corpCode)){
				queryVo.setCorpCode(corpCode);
			}
			queryVo.setUrl(url);
			queryVo.setPhone(phone);
			queryVo.setVisitStatus(visitStatus);
			queryVo.setLastIP(visitIP);


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

			mtVoList = linkDetailBiz.getLinkDetail(queryVo, null);
		}
		// 返回状态
		String result = "false";
		// 操作日志信息
		String opContent = "";
		if (mtVoList != null && mtVoList.size() > 0) {
			SurlTaskReportExcelTool et = new SurlTaskReportExcelTool(excelPath);
			Map<String, String> resultMap = et.createLinkDetailExcel(mtVoList, request);
			request.getSession(false).setAttribute("batchSend", resultMap);
			// 操作日志信息
			opContent = "企业短链，链接号码详情导出，导出成功。";
			result = "true";
		} else {
			opContent = "导出失败。";
		}
		SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
		opContent += "开始：" + sdformat.format(sTime) + "，耗时：" + (System.currentTimeMillis() - sTime) / 1000 + "秒，总数：" + (mtVoList==null?0:mtVoList.size());
		LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		EmpExecutionContext.info("企业短链，链接号码详情导出", lfSysuser.getCorpCode(), lfSysuser.getUserId() + "", lfSysuser.getUserName(), opContent, "GET");

		try {
			response.getWriter().print(result);
		} catch (IOException e) {EmpExecutionContext.error(e, "链接号码详情导出异常");}
	}

	/**
	 * EXCEL文件下载
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 文件名
		String fileName = "";
		// 文件路径
		String filePath = "";
		// 导出类型：smssendedbox_export(历史任务查看);smstaskrecord_export(群发历史查询)；smstaskreply_export（群发历史查询详情）
		String exportType = "";
		try {
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute("batchSend");
			if (obj != null) {
				Map<String, String> resultMap = (Map<String, String>) obj;
				fileName = resultMap.get("FILE_NAME");
				filePath = resultMap.get("FILE_PATH");
				// 弹出下载页面
				new DownloadFile().downFile(request, response, filePath, fileName);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "链接号码详情EXCEL文件下载失败，fileName:" + fileName + "，filePath:" + filePath + "，exportType:" + exportType);
		}
	}
}
