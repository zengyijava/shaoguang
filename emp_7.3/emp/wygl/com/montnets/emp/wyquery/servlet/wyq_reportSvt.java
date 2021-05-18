package com.montnets.emp.wyquery.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wyquery.biz.QueryBiz;
import com.montnets.emp.wyquery.biz.WyReportBiz;
import com.montnets.emp.wyquery.vo.WyReportVo;

/**
 * 网优统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:48:30
 * @description
 */
@SuppressWarnings("serial")
public class wyq_reportSvt extends BaseServlet
{

	// 模块名称
	private final String	empRoot	= "wygl";

	// 功能文件夹
	private final String	base	= "/wyquery";
	

	protected int getIntParameter(String param, int defaultValue, HttpServletRequest request)
	{
		try
		{
			return Integer.parseInt(request.getParameter(param));
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e,"网优统计报表分页数字字符串转换成数字失败！");
			return defaultValue;
		}
	}

	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		// 起始ms数
		long startl = System.currentTimeMillis();
		// 开始时间
		String starthms = hms.format(startl);
		// 企业编码
		String corpcode = request.getParameter("lgcorpcode");
		//年报表/月报表
		String reporttype = request.getParameter("reportType");
		boolean isFirstEnter = false;
		PageInfo pageInfo = new PageInfo();
		WyReportBiz drp = new WyReportBiz();
		try
		{
			boolean isError = true;
			// 操作员机构修改，多次点击出现org.apache.catalina.connector.Request.parseParameters异常
			isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
			if(isError)
			{
				isFirstEnter = true;
			}
			else
			{
				// 设置分页
				pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
				pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
				isFirstEnter = false;
			}

			if(!isFirstEnter)
			{
				//通道名称
				String gateName=request.getParameter("gateName");
				//通道号码
				String spgate=request.getParameter("spgate");
				//开始时间
				String begintime = null == request.getParameter("begintime") ? "" : request.getParameter("begintime").toString().trim();// 开始时间
				//结束时间
				String endtime = null == request.getParameter("endtime") ? "" : request.getParameter("endtime").toString().trim();// 结束时间
				//查询条件对象
				WyReportVo wyrptvo = new WyReportVo();
				//企业编码
				wyrptvo.setCorpCode(corpcode);
				//通道名称
				wyrptvo.setGateName(gateName);
				
				wyrptvo.setSpgate(spgate);
			
				
				if("1".equals(reporttype)){
					endtime=begintime;
					begintime=begintime+"-01";
				}else if("2".equals(reporttype)){
					endtime=begintime;
					begintime=begintime+"-01-01";
				}
				// 开始时间
				wyrptvo.setSendtime(begintime);
				// 结束时间
				if("1".equals(reporttype)){//月报表
					//拆分年月
					if(endtime.indexOf("-")>-1&&endtime.split("-").length==2){
					 //GregorianCalendar构造方法参数依次为：年，月-1，日，时，分，秒.
						String[] end=  endtime.split("-");
						int year= Integer.parseInt(end[0]);
						int mouth=Integer.parseInt(end[1])-1;
						Calendar ca=new GregorianCalendar(year, mouth, 1,0,0,0);    
				        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH)); 
				        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				        endtime= df.format(ca.getTime());
					}
					
				}else if("2".equals(reporttype)){//年报表
					endtime=endtime+"-12-31";
				}
				wyrptvo.setEndtime(endtime);
				List<WyReportVo> wyreportList = null;
				wyreportList = drp.getReportInfosByDate(wyrptvo, pageInfo);
				long[]sum =drp.findSumCount(wyrptvo);
				//提交总数
				request.setAttribute("icount", sum[0]);
				//接收成功数
				request.setAttribute("rsucc", sum[1]);
				//发送失败数
				request.setAttribute("rfail1", sum[2]);
				//接收失败数
				request.setAttribute("rfail2", sum[3]);
				//未返数
				request.setAttribute("rnret", sum[4]);
				request.getSession(false).setAttribute("wyreportList", wyreportList);
			}

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("spgate&"+StaticValue.LIKE2, "200");
			List<XtGateQueue> xtGateQueueList=new BaseBiz().getByCondition(XtGateQueue.class, conditionMap, null);
			request.setAttribute("xtGateQueueList",xtGateQueueList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("isFirstEnter",isFirstEnter);
			
			long count = 0l;
			// 从pageinfo中获取查询总条数
			if(pageInfo != null)
			{
				count = pageInfo.getTotalRec();
			}
			// 写日志
			String opContent = "网优统计报表查询共" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
			new QueryBiz().setLog(request, "网优统计报表", opContent, StaticValue.GET);

		}
		catch (Exception e)
		{
			request.setAttribute("findresult", "-1");
			try {
				request.getSession(false).setAttribute("wyreportList", null);
			} catch (Exception e1) {
				EmpExecutionContext.error(e1,"session为null");
			}
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "网优统计报表servlet异常");

		}
		finally
		{
			request.getRequestDispatcher(this.empRoot + base + "/wyq_report.jsp").forward(request, response);
		}
	}

	/**
	 * 网优统计报表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	// excel导出全部数据
	public void r_smRptExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		try
		{
			// 设置语言
			String langName  = request.getParameter("langName");
			// 起始ms数
			long startl = System.currentTimeMillis();
			// 开始时间
			String starthms = hms.format(startl);
			// 企业编码
			String corpcode = request.getParameter("lgcorpcode");
			//年报表/月报表
			String reporttype = request.getParameter("reportType");
			//通道名称
			// 时间段字符串
			String gateName = request.getParameter("gatename");
			//通道号码
			String spgate =request.getParameter("spgate");
			//开始时间
			String begintime = null == request.getParameter("sendtime") ? "" : request.getParameter("sendtime").toString().trim();// 开始时间
			//结束时间
			String endtime = null == request.getParameter("endtime") ? "" : request.getParameter("endtime").toString().trim();// 结束时间
			//查询条件对象
			WyReportVo wyrptvo = new WyReportVo();
			//企业编码
			wyrptvo.setCorpCode(corpcode);
			//通道名称
			wyrptvo.setGateName(gateName);
			
			wyrptvo.setSpgate(spgate);
		
			
			if("1".equals(reporttype)){
				endtime=begintime;
				begintime=begintime+"-01";
			}else if("2".equals(reporttype)){
				endtime=begintime;
				begintime=begintime+"-01-01";
			}
			// 开始时间
			wyrptvo.setSendtime(begintime);
			// 结束时间
			if("1".equals(reporttype)){//月报表
				//拆分年月
				if(endtime.indexOf("-")>-1&&endtime.split("-").length==2){
				 //GregorianCalendar构造方法参数依次为：年，月-1，日，时，分，秒.
					String[] end=  endtime.split("-");
					int year= Integer.parseInt(end[0]);
					int mouth=Integer.parseInt(end[1])-1;
					Calendar ca=new GregorianCalendar(year, mouth, 1,0,0,0);    
			        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH)); 
			        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			        endtime= df.format(ca.getTime());
				}
			}else if("2".equals(reporttype)){//年报表
				endtime=endtime+"-12-31";
			}
			wyrptvo.setEndtime(endtime);
			WyReportBiz drp = new WyReportBiz();
			long[]sum =drp.findSumCount(wyrptvo);
			List<WyReportVo> wyreportList = drp.getReportInfosByDate(wyrptvo, null);
			String wyshowdatestr = (String)request.getSession(false).getAttribute("wyshowTime");
			response.setContentType("html/text");
			PrintWriter out = response.getWriter();
			if(wyreportList != null && wyreportList.size() > 0)
			{
				wyrptvo.setIcount(sum[0]);
				wyrptvo.setRsucc(sum[1]);
				wyrptvo.setRfail1(sum[2]);
				wyrptvo.setRfail2(sum[3]);
				wyrptvo.setRnret(sum[4]);
				Map<String, String> resultMap = drp.createSpMtReportExcel(langName,wyreportList, wyrptvo, wyshowdatestr);
				String fileName = (String) resultMap.get("FILE_NAME");
				// 写日志
				String opContent = "导出网优统计报表记录共" + wyreportList.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
				new QueryBiz().setLog(request, "网优统计报表", opContent, StaticValue.GET);
				HttpSession session = request.getSession(false);
				session.setAttribute("wymtd_export_map", resultMap);
				out.print("true");
//				String filePath = (String) resultMap.get("FILE_PATH");
//				DownloadFile df = new DownloadFile();
//				df.downFile(request, response, filePath, fileName);
				
			}else{
				out.print("false");
			}

		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e,"网优统计报表导出异常");
		}
	}
	
	/**
	 * 网优群发历史详情导出下载
	 * @param request
	 * @param response
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession(false);
		Object obj =  null;
		try {
			obj = session.getAttribute("wymtd_export_map");
			session.removeAttribute("wymtd_export_map");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"session为null");
		}
		if(obj != null)
		{
			// 弹出下载页面。
			DownloadFile dfs = new DownloadFile();
			Map<String, String> resultMap = (Map<String, String>) obj;
			String fileName = (String) resultMap.get("FILE_NAME");
			String filePath = (String) resultMap.get("FILE_PATH");
			dfs.downFile(request, response, filePath, fileName);
		}
	}
	
	
	
	
	public void findInfoById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		// 企业编码
		String corpcode = request.getParameter("lgcorpcode");
		//年报表/月报表
		String reporttype = request.getParameter("reportType");
		PageInfo pageInfo = new PageInfo();
		WyReportBiz drp = new WyReportBiz();
		try
		{
			boolean isError = true;
			isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
			if(!isError)
			{
				// 设置分页
				pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
				pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
			}

				//通道号码
				String spgate=request.getParameter("spgate");
				//开始时间
				String begintime = null == request.getParameter("begintime") ? "" : request.getParameter("begintime").toString().trim();// 开始时间
				//结束时间
				String endtime = null == request.getParameter("endtime") ? "" : request.getParameter("endtime").toString().trim();// 结束时间
				//查询条件对象
				WyReportVo wyrptvo = new WyReportVo();
				//企业编码
				wyrptvo.setCorpCode(corpcode);
				//通道号
				wyrptvo.setSpgate(spgate);
			
				if("1".equals(reporttype)){
					endtime=begintime;
					begintime=begintime+"-01";
				}else if("2".equals(reporttype)){
					endtime=begintime;
					begintime=begintime+"-01-01";
				}
				// 开始时间
				wyrptvo.setSendtime(begintime);
				// 结束时间
				if("1".equals(reporttype)){//月报表
					//拆分年月
					if(endtime.indexOf("-")>-1&&endtime.split("-").length==2){
					 //GregorianCalendar构造方法参数依次为：年，月-1，日，时，分，秒.
						String[] end=  endtime.split("-");
						int year= Integer.parseInt(end[0]);
						int mouth=Integer.parseInt(end[1])-1;
						Calendar ca=new GregorianCalendar(year, mouth, 1,0,0,0);    
				        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH)); 
				        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				        endtime= df.format(ca.getTime());
					}
					
				}else if("2".equals(reporttype)){//年报表
					endtime=endtime+"-12-31";
				}
				wyrptvo.setEndtime(endtime);
				List<DynaBean> wydetailreportList = null;
				wydetailreportList = drp.getReportInfosDetailByDate(wyrptvo,reporttype, pageInfo);
				request.getSession(false).setAttribute("detailwyreportList", wydetailreportList);
				long[]sum =drp.findSumCount(wyrptvo);
				//提交总数
				request.setAttribute("icount", sum[0]);
				//接收成功数
				request.setAttribute("rsucc", sum[1]);
				//发送失败数
				request.setAttribute("rfail1", sum[2]);
				//接收失败数
				request.setAttribute("rfail2", sum[3]);
				//未返数
				request.setAttribute("rnret", sum[4]);
			request.setAttribute("pageInfo", pageInfo);
			
			long count=0l;
			//从pageinfo中获取查询总条数
			if(pageInfo!=null){
				count=pageInfo.getTotalRec();
			}
			// 写日志
			String opContent = "网优统计报表详情查询：" + count + "条 开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
			new QueryBiz().setLog(request, "网优统计报表详情", opContent, StaticValue.GET);
			
		}
		catch (Exception e)
		{
			request.setAttribute("findresult", "-1");
			try {
				request.getSession(false).setAttribute("detailwyreportList", null);
			} catch (Exception e1) {
				EmpExecutionContext.error(e1,"session为null");
			}
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e,"网优统计详情报表servlet异常");
		}
		finally
		{
			request.getRequestDispatcher(this.empRoot + base + "/wyq_detailreport.jsp").forward(request, response);
		}
	}
	
	
	
	
	/**
	 * 网优统计报表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	// excel导出全部数据
	public void r_smRptDetailExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		try
		{

			// 设置语言
			String langName  = request.getParameter("langName");
			//起始ms数
			long startl=System.currentTimeMillis();
			//开始时间
			String starthms=hms.format(startl);
			// 企业编码
			String corpcode = request.getParameter("lgcorpcode");
			//年报表/月报表
			String reporttype = request.getParameter("reportType");
			//通道号码
			String spgate = request.getParameter("spgate");
			//开始时间
			String begintime = null == request.getParameter("begintime") ? "" : request.getParameter("begintime").toString().trim();// 开始时间
			//结束时间
			String endtime = null == request.getParameter("endtime") ? "" : request.getParameter("endtime").toString().trim();// 结束时间
			//查询条件对象
			WyReportVo wyrptvo = new WyReportVo();
			//企业编码
			wyrptvo.setCorpCode(corpcode);
			
			wyrptvo.setSpgate(spgate);
		
			
			if("1".equals(reporttype)){
				endtime=begintime;
				begintime=begintime+"-01";
			}else if("2".equals(reporttype)){
				endtime=begintime;
				begintime=begintime+"-01-01";
			}
			// 开始时间
			wyrptvo.setSendtime(begintime);
			// 结束时间
			if("1".equals(reporttype)){//月报表
				//拆分年月
				if(endtime.indexOf("-")>-1&&endtime.split("-").length==2){
				 //GregorianCalendar构造方法参数依次为：年，月-1，日，时，分，秒.
					String[] end=  endtime.split("-");
					int year= Integer.parseInt(end[0]);
					int mouth=Integer.parseInt(end[1])-1;
					Calendar ca=new GregorianCalendar(year, mouth, 1,0,0,0);    
			        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH)); 
			        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			        endtime= df.format(ca.getTime());
				}
			}else if("2".equals(reporttype)){//年报表
				endtime=endtime+"-12-31";
			}
			wyrptvo.setEndtime(endtime);
			WyReportBiz drp = new WyReportBiz();
			long[]sum =drp.findSumCount(wyrptvo);
			List<DynaBean> wydetailreportList = drp.getReportInfosDetailByDate(wyrptvo, reporttype, null);
			response.setContentType("html/text");
			PrintWriter out = response.getWriter();
			if(wydetailreportList != null && wydetailreportList.size() > 0)
			{
				wyrptvo.setIcount(sum[0]);
				wyrptvo.setRsucc(sum[1]);
				wyrptvo.setRfail1(sum[2]);
				wyrptvo.setRfail2(sum[3]);
				wyrptvo.setRnret(sum[4]);
				Map<String, String> resultMap = drp.createDetailSpMtReportExcel(langName,wydetailreportList,wyrptvo, reporttype);
				String fileName = (String) resultMap.get("FILE_NAME");
				// 写日志
				String opContent = "导出网优统计报表详情记录：" +wydetailreportList.size() + "条  ,文件名："+fileName+",开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
				new QueryBiz().setLog(request, "网优统计报表详情", opContent, StaticValue.GET);
				HttpSession session =request.getSession(false);
				session.setAttribute("wymtddetail_export_map",resultMap);
				out.print("true");
//				String filePath = (String) resultMap.get("FILE_PATH");
//				DownloadFile df = new DownloadFile();
//				df.downFile(request, response, filePath, fileName);
			}else{
				out.print("false");
			}

		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e,"网优统计报表详情导出异常");
		}
	}
	
	/**
	 * 网优统计报表详情下载
	 * @param request
	 * @param response
	 */
	public void detaildownloadFile(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession(false);
		Object obj = null;
		try {
			obj = session.getAttribute("wymtddetail_export_map");
			session.removeAttribute("wymtddetail_export_map");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"session为null");
		}
		if(obj != null)
		{
			// 弹出下载页面。
			DownloadFile dfs = new DownloadFile();
			Map<String, String> resultMap = (Map<String, String>) obj;
			String fileName = (String) resultMap.get("FILE_NAME");
			String filePath = (String) resultMap.get("FILE_PATH");
			dfs.downFile(request, response, filePath, fileName);
		}
	}
	
	
	
}
