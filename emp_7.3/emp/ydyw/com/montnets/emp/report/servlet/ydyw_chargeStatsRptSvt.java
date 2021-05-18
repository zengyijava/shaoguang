package com.montnets.emp.report.servlet;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.report.biz.ChargeStatsRptBiz;
import com.montnets.emp.report.vo.ChargeStatsRptVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 业务套餐统计servlet
 * 
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:48:43
 * @description
 */
public class ydyw_chargeStatsRptSvt extends BaseServlet
{
	private static final long	serialVersionUID	= 1908901989360839320L;
	// 业务套餐统计biz
	private final ChargeStatsRptBiz		cstatBiz		= new ChargeStatsRptBiz();
	// 模块名称
	private final String						empRoot				= "ydyw";
	// 基路径
	private final String						base				= "/rpt";

	/**
	 * 分页使用方法
	 * 
	 * @param param
	 * @param defaultValue
	 * @param request
	 * @return
	 */
	protected int getIntParameter(String param, int defaultValue, HttpServletRequest request)
	{
		try
		{
			return Integer.parseInt(request.getParameter(param));
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e, "获取分页信息异常");
			return defaultValue;
		}
	}

	/**
	 * 业务套餐统计查询方法
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 是否第一次访问
		boolean isFirstEnter = false;
		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		// 时间月份
		String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
		// 年份或月份查询条件
		String countTime = request.getParameter("countTime");
		// 报表类型
		String reporttypestr = request.getParameter("reportType");
		// 业务套餐统计报表条件对象
		ChargeStatsRptVo cstatrptvo = new ChargeStatsRptVo();
		// 报表类型
		int reportType = 0;
		// 分页对象
		PageInfo pageInfo = new PageInfo();
		
		//添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
		
		try
		{
			// 获得session
			HttpSession session = request.getSession(false);
			List<ChargeStatsRptVo> cstateList = null;
			// 如果月份超过两位
			if(timeMonth.length() > 2)
			{
				timeMonth = timeMonth.substring(1);
			}
			// 判断报表类型不为空则类型转换
			if(reporttypestr != null)
			{
				reportType = Integer.parseInt(reporttypestr);
			}
			// 年份或月份查询条件不为空则提取年份 和月份
			if(!"".equals(countTime) && countTime != null)
			{
				timeYear = countTime.substring(0, 4);
				timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
			}
			else
			{
				countTime = "";
			}
			boolean isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
			if(isError){
				isFirstEnter = true;
			}else{
				// 设置分页
				pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
				pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
				pageInfo.setTotalPage(getIntParameter("totalPage", pageInfo.getTotalPage(), request));
				pageInfo.setTotalRec(getIntParameter("totalRec", pageInfo.getTotalRec(), request));
				isFirstEnter = false;
			}
			//时间
			cstatrptvo.setY(timeYear);
			if(reportType == 0)
			{
				// 月报表
				cstatrptvo.setImonth(timeMonth);
			}else if(reportType == 1){
				// 年报表
				cstatrptvo.setImonth(null);
			}
			
			if(!isFirstEnter)
			{
				//套餐编码
				String taocancode=request.getParameter("taocancode");
				//套餐名称
				String taocanname=request.getParameter("taocanname");
				//套餐类型
				String taocantype=request.getParameter("taocantype");
				//套餐编码
				cstatrptvo.setTaocancode(taocancode);
				//套餐名称
				cstatrptvo.setTaocanname(taocanname);
				//套餐类型
				if(taocantype!=null&&!"".equals(taocantype)){
					cstatrptvo.setTaocantype(Integer.parseInt(taocantype.trim()));
				}				

				//标准版和托管版都需要传正确的企业编码
/*				if(StaticValue.CORPTYPE == 0)
				{
					corpCode = "0";
				}*/
				// 合计
				long sumtaocanmoney;
				cstateList = cstatBiz.getChargeStatsRptVoList(cstatrptvo, corpCode, pageInfo);
						
				
				sumtaocanmoney = cstatBiz.findSumCount(cstatrptvo, corpCode);
				session.setAttribute("sumtaocanmoney", sumtaocanmoney);
//				if(sumtaocanmoney == 0){
//					pageInfo = new PageInfo();
//				}
				// 机构结果
				session.setAttribute("catresultList", cstateList);
				//新增 将结果放到request中 p
				request.setAttribute("catresultList_p", cstateList);
			}
			//查询条件
			request.setAttribute("cstatrptvo", cstatrptvo);
			// 分页对象
			request.setAttribute("pageInfo", pageInfo);
			//时间
			request.setAttribute("countTime", timeYear + "-" + timeMonth);
		}
		catch (Exception e)
		{
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "后台异常");

		}
		finally
		{
			
			//添加与日志相关 p
			long endTimeByLog = System.currentTimeMillis();  //查询结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时
			
			//增加操作日志 p
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
				
				EmpExecutionContext.info("业务套餐计费统计", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent, "GET");
			}
			
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.getRequestDispatcher(this.empRoot + base + "/ydyw_chargeStatsRpt.jsp?lgcorpcode=" + corpCode).forward(request, response);
		}
	}


	/**
	 * 业务套餐统计报表 导出
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void r_sdRptExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		List<ChargeStatsRptVo> mdlist = null;
		String countTime = URLDecoder.decode(request.getParameter("countTime"), "UTF-8");
		PrintWriter out = response.getWriter();
		
		//添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
		
		
		try
		{
			HttpSession session = request.getSession(false);
			// 获取当前用户的企业编码
			String corpCode = request.getParameter("lgcorpcode");
			// 报表类型
			String reporttypestr = request.getParameter("reportType");
			// 时间格式
			String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
			//语言格式
			String empLangName = request.getParameter("empLangName");
			String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
			if(timeMonth.length() > 2)
			{
				timeMonth = timeMonth.substring(1);
			}
			// 获得报表的类型（0月报表 1年报表）
			int reportType = 0;
			if(reporttypestr != null)
			{
				reportType = Integer.parseInt(reporttypestr);
			}
			if(!"".equals(countTime) && countTime != null)
			{
				timeYear = countTime.substring(0, 4);
				timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
			}
			else
			{
				countTime = "";
			}
			// 当前操作员所拥有的权限
			ChargeStatsRptVo cstatsvo = new ChargeStatsRptVo();
			//标准版和托管版都需要传正确的企业编码
/*			if(StaticValue.CORPTYPE == 0)
			{
				corpCode = "0";
			}*/
			// 设置运营商账户时间
			cstatsvo.setY(timeYear);
			if(reportType == 0)
			{
				// 月报表
				cstatsvo.setImonth(timeMonth);
			}
			else if(reportType == 1)
			{
				// 年报表
				cstatsvo.setImonth(null);
			}
			//套餐编码
			String taocancode = URLDecoder.decode(request.getParameter("taocancode"), "utf-8");
			//套餐名称
			String taocanname = URLDecoder.decode(request.getParameter("taocanname"), "utf-8");
			//套餐类型
			String taocantype=request.getParameter("taocantype");
			//套餐编码
			cstatsvo.setTaocancode(taocancode);
			//套餐名称
			cstatsvo.setTaocanname(taocanname);
			//套餐类型
			if(taocantype!=null&&!"".equals(taocantype)){
				cstatsvo.setTaocantype(Integer.parseInt(taocantype.trim()));
			}
			String sumtaocanmoney = session.getAttribute("sumtaocanmoney").toString();
			mdlist = cstatBiz.getChargeStatsRptVoListUnPage(cstatsvo, corpCode);
			if(mdlist != null && mdlist.size() > 0)
			{
				Map<String, String> resultMap = cstatBiz.createChargeStatsRptExcel(mdlist, countTime, sumtaocanmoney,empLangName);
				//String fileName = (String) resultMap.get("FILE_NAME");
				//String filePath = (String) resultMap.get("FILE_PATH");
				
				/*新增 p*/
	            session.setAttribute("r_sdRpt_export_map2",resultMap);
				
				// 写日志
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				if(lfSysuser != null)
				{
					String opUser = lfSysuser.getUserName();
					String opContent = "导出业务套餐统计：" + mdlist.size() + "条 ";
					String corpcode = lfSysuser.getCorpCode();
					opUser = opUser == null ? "" : opUser;
					corpcode = corpcode == null ? "" : corpcode;
					EmpExecutionContext.info("企业：" + corpcode + ",操作员：" + opUser + opContent + "成功");
					
					//增加操作日志
					Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
						String opContent1 = "导出业务套餐统计成功。(数量："+mdlist.size()+"条)";
						EmpExecutionContext.info("业务套餐计费统", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
								loginSysuser.getUserName(), opContent1, "OTHER");
					}						
					mdlist.clear();
					mdlist = null;
				}
				//DownloadFile df = new DownloadFile();
				//df.downFile(request, response, filePath, fileName);
				/*新增 p*/
				out.print("true");
			}else{
				out.print("false");
			}
			
			
			//添加与日志相关 p
			long endTimeByLog = System.currentTimeMillis();  //导出结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //导出耗时
			
			//增加操作日志 p
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent = "导出开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms";
				
				EmpExecutionContext.info("业务套餐计费统计", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent, "OTHER");
			}
			
			
		}
		catch (Exception e)
		{
			out.print("false");
			// 异常处理
			EmpExecutionContext.error(e, "业务套餐统计导出异常");
		}
	}
	
	/**
	 * 下载导出文件 新增方法 p
	 * @param request
	 * @param response
	 */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response)   {
    	
        try {
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute("r_sdRpt_export_map2");
			session.removeAttribute("r_sdRpt_export_map2");
			if(obj != null){
				//弹出下载页面
			    DownloadFile dfs = new DownloadFile();
			    Map<String, String> resultMap = (Map<String, String>) obj;
			    String fileName = (String) resultMap.get("FILE_NAME");
			    String filePath = (String) resultMap.get("FILE_PATH");
			    dfs.downFile(request, response, filePath, fileName);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "下载方法异常！");
		}
        
        
        
    }
}
