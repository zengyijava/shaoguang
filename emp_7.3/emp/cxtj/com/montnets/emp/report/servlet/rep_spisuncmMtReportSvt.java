package com.montnets.emp.report.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.biz.OperatorsMtReportBiz;
import com.montnets.emp.report.vo.OperatorsAreaMtDataReportVo;
import com.montnets.emp.report.vo.OperatorsMtDataReportVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;

/**
 * 运营商统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:48:17
 * @description
 */
@SuppressWarnings("serial")
public class rep_spisuncmMtReportSvt extends BaseServlet
{

	// 模块名称
	private final String	empRoot	= "cxtj";

	// 功能文件夹名
	private final String	base	= "/report";
	//时分秒格式化
	private final SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");

	protected int getIntParameter(String param, int defaultValue, HttpServletRequest request)
	{
		try
		{
			if(request.getParameter(param)!=null && !"".equals(request.getParameter(param)))
			{
				return Integer.parseInt(request.getParameter(param));
			}
			else
			{
				return defaultValue;
			}
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e,"获取分页信息异常");
			return defaultValue;
		}
	}

	/**
	 * 运营商报表查询方法
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 短彩类型
		//String msType = request.getParameter("msType");
		//年份或月份查询条件
		//String countTime = request.getParameter("countTime");
		//运营商查询条件
		//String spisuncm = request.getParameter("spisuncm");
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		// 时间月份
		String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
		//日报表结束时间
		//String endTime = request.getParameter("endTime");
		//是否详情
		boolean isDes = "1".equals(request.getParameter("isDes"));
		//是否返回
		boolean isBack = request.getParameter("isback")!=null;
		// 报表类型
		String reporttypestr = request.getParameter("reportType");
		// 运营商查询条件对象
		OperatorsMtDataReportVo spisuncmMtDataReportVo = new OperatorsMtDataReportVo();
		// 分页对象
		PageInfo pageInfo = new PageInfo();
		// 报表类型
		int reportType = 0;
		// 是否第一次执行
		boolean isFirstEnter = false;
		boolean isError = true;
		//运营商报表结果集定义
		List<OperatorsMtDataReportVo> reportList = null;
		//运营商报表biz
		OperatorsMtReportBiz omrbiz = new OperatorsMtReportBiz();
		
		try
		{
			// 操作员机构修改，多次点击出现org.apache.catalina.connector.Request.parseParameters异常
			isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
			if(!isBack && isError)
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
				HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
				//运营商账户id
				spisuncmMtDataReportVo.setSpID((request.getParameter("staffId")));
				if(!isBack)
				{
					// 短彩类型
					String msType = request.getParameter("msType");
					//年份或月份查询条件
					String countTime = request.getParameter("countTime");
					//运营商查询条件
					String spisuncm = request.getParameter("spisuncm");	
					//日报表结束时间
					String endTime = request.getParameter("endTime");
					
					//短信类型
					if(msType == null)
					{
						msType = "0";
					}
					spisuncmMtDataReportVo.setMstype(Integer.parseInt(msType));
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
						timeMonth = countTime.length() == 7 ? countTime.substring(5,7) : timeMonth;
					}
					else
					{
						countTime = "";
					}
					//判断运营商
					if(spisuncm != null && !"".equals(spisuncm))
					{
						spisuncmMtDataReportVo.setSpisuncm(Long.parseLong(spisuncm));
					}
					if(reportType == 0)
					{
						spisuncmMtDataReportVo.setImonth(timeMonth);
						spisuncmMtDataReportVo.setY(timeYear);
					}
					else if(reportType == 1)
					{
						spisuncmMtDataReportVo.setImonth(null);
						spisuncmMtDataReportVo.setY(timeYear);
					}
					else if(reportType == 2){
						spisuncmMtDataReportVo.setStartTime(countTime);
						spisuncmMtDataReportVo.setEndTime(endTime);
					}
					spisuncmMtDataReportVo.setIsDes(isDes);
					spisuncmMtDataReportVo.setReporttype(reportType);
					//月报表、年报表返回时获取contTime
					spisuncmMtDataReportVo.setStartTime(countTime);
					
					request.setAttribute("startTime", countTime);
					request.setAttribute("endTime", endTime);
					
				}else{
					spisuncmMtDataReportVo = (OperatorsMtDataReportVo) session.getAttribute("req_spisuncmMtDataReportVo");
					pageInfo = (PageInfo) session.getAttribute("req_mtDataReportVo");
					reportType = spisuncmMtDataReportVo.getReporttype();
				}
				//存储 spisuncmMtDataReportVo对象及pageinfo对象 处理二级返回
				if(!isDes){
					session.setAttribute("req_spisuncmMtDataReportVo", spisuncmMtDataReportVo);
					session.setAttribute("req_mtDataReportVo", pageInfo);
					if(spisuncmMtDataReportVo.getStartTime() != null && !"".equals(spisuncmMtDataReportVo.getStartTime()) ||
							spisuncmMtDataReportVo.getEndTime() != null && !"".equals(spisuncmMtDataReportVo.getEndTime())){
						request.setAttribute("startTime", spisuncmMtDataReportVo.getStartTime());
						request.setAttribute("endTime", spisuncmMtDataReportVo.getEndTime());
					}

				}
				//结果集
				if(reportType == 0)
				{
					// 月报表
					reportList = omrbiz.getReportInfosByMonth(spisuncmMtDataReportVo, pageInfo);
				}
				else if(reportType == 1)
				{
					// 年报表
					reportList = omrbiz.getReportInfosByYear(spisuncmMtDataReportVo, pageInfo);
				}
				else if(reportType == 2){
					//日报表
					reportList = omrbiz.getReportInfosByDays(spisuncmMtDataReportVo, pageInfo);
				}
				long[] sumCount = omrbiz.findSumCount(spisuncmMtDataReportVo);
//				if(sumCount[0]-sumCount[1]==0){
//					reportList = new ArrayList<OperatorsMtDataReportVo>();
//					pageInfo = new PageInfo();
//				}
				// 总数
				request.setAttribute("sumCount",sumCount );
				// 分页对象
				request.setAttribute("pageInfo", pageInfo);
				//存储对象
				request.setAttribute("spisuncmMtDataReportVo", spisuncmMtDataReportVo);
				//查询结果集
				request.setAttribute("reportList", reportList);
			}
			request.setAttribute("countTime", sdf.format(calendar.getTime()));

			// 查询短彩运营商帐号spid
			List<OperatorsMtDataReportVo> smsuserList = omrbiz.getListByMsType(0);
			List<OperatorsMtDataReportVo> mmsuserList = omrbiz.getListByMsType(1);
			
			//获取系统定义的短彩类型值
			GlobConfigBiz gcBiz = new GlobConfigBiz();
			List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");

			request.setAttribute("reportType", reportType);
			request.setAttribute("spisuncmMtDataReportVo", spisuncmMtDataReportVo);
			request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("smsspUserList", smsuserList);
			request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("mmsspUserList", mmsuserList);
			request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("pagefileds", pagefileds);
			request.setAttribute("isFirstEnter", isFirstEnter);
			long count=0l;
			//从pageinfo中获取查询总条数
			if(pageInfo!=null){
				count=pageInfo.getTotalRec();
			}
			// 写日志
			String opContent = "运营商报表查询：" + count + "条开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
			new QueryBiz().setLog(request, "运营商报表", opContent, StaticValue.GET);

		}
		catch (Exception e)
		{
			request.setAttribute("countTime", sdf.format(calendar.getTime()));
			request.setAttribute("reportType", reportType);
			long[] returnLong = new long[5];
			returnLong[0] = 0;
			returnLong[1] = 0;
			returnLong[2] = 0;
			returnLong[3] = 0;
			returnLong[4] = 0;
			request.setAttribute("sumCount", returnLong);
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "查询异常");
		}
		finally
		{
			request.getRequestDispatcher(this.empRoot + base + "/rep_spisuncmMtReport.jsp").forward(request, response);
		}
	}

	/**
	 * 运营商统计报表   导出
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void r_stRptExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		// 获取页面上的时间段
		String countTime = request.getParameter("countTime");
		//获取结束时间
		//String stratTime = URLDecoder.decode(request.getParameter("stratTime"), "utf-8");
		String endTime = request.getParameter("endTime")==null?"":request.getParameter("endTime");
		// 报表类型
		String reporttypestr = request.getParameter("reportType");
		//短彩类型
		String mstype = request.getParameter("mstype");
		//运营商
		String spisuncm = request.getParameter("spisuncm");
		//提交总数合计
		long totalIcount = Long.parseLong(request.getParameter("totalIcount").toString());
		//发送失败数合计
		long totalRfail1 = Long.parseLong(request.getParameter("totalRfail1").toString());
		//接收失败数合计
		long totalRfail2 = Long.parseLong(request.getParameter("totalRfail2").toString());
		//未返数合计
		long totalRnret = Long.parseLong(request.getParameter("totalRnret").toString());		
		//接收成功数合计
		long totalRsucc = Long.parseLong(request.getParameter("totalRsucc").toString());
		//是否为详情界面
		boolean isDes = "1".equals(request.getParameter("isDes"));
		//运营商报表查询条件对象
		OperatorsMtDataReportVo spisuncmMtDataReportVo = new OperatorsMtDataReportVo();
		//运营商报表BIZ
		OperatorsMtReportBiz omrbiz = new OperatorsMtReportBiz();
		//导出结果集
		List<OperatorsMtDataReportVo> reportList = null;
		try
		{
			// 时间格式
			String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
			String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);

			if(timeMonth.length() > 2)
			{
				timeMonth = timeMonth.substring(1);
			}
			// 获得报表的类型（0月报表 1年报表2日报表）
			int reportType = 0;
			if(reporttypestr != null)
			{
				reportType = Integer.parseInt(reporttypestr);
			}
			if(!"".equals(countTime) && countTime != null)
			{
				timeYear = countTime.substring(0, 4);
				timeMonth = countTime.length() == 7 ? countTime.substring(5,7) : timeMonth;
			}
			else
			{
				countTime = "";
			}
			
			//短信类型
			if(mstype == null || "".equals(mstype))
			{
				mstype = "0";
			}
			spisuncmMtDataReportVo.setIsDes(isDes);
			spisuncmMtDataReportVo.setMstype(Integer.parseInt(mstype));
			// 设置运营商账户ID
			spisuncmMtDataReportVo.setSpID((request.getParameter("staffId")));
			// 设置运营商账户时间
			spisuncmMtDataReportVo.setY(timeYear);
			if(spisuncm != null && !"".equals(spisuncm))
			{
				spisuncmMtDataReportVo.setSpisuncm(Long.parseLong(spisuncm));
			}
			spisuncmMtDataReportVo.setReporttype(reportType);
			if(reportType == 0)
			{
				// 月报表
				spisuncmMtDataReportVo.setImonth(timeMonth);
				reportList = omrbiz.getReportInfosByMonth(spisuncmMtDataReportVo, null);
			}
			else if(reportType == 1)
			{
				// 年报表
				spisuncmMtDataReportVo.setImonth(null);
				reportList = omrbiz.getReportInfosByYear(spisuncmMtDataReportVo, null);
			}
			else if(reportType == 2)
			{
				//日报表
				spisuncmMtDataReportVo.setStartTime(countTime);
				spisuncmMtDataReportVo.setEndTime(endTime);
				reportList = omrbiz.getReportInfosByDays(spisuncmMtDataReportVo, null);
			}
			response.setContentType("html/text");
			PrintWriter out = response.getWriter();
			//判断结果集是否有数据有则导出
			if(reportList != null && reportList.size() > 0)
			{	
				//createSpReportExcel(reportList, countTime, succ, rfail2);
				Map<String, String> resultMap = omrbiz.createSpReportExcel(reportList, countTime, totalIcount, totalRfail1, totalRfail2, totalRnret, totalRsucc,spisuncmMtDataReportVo,request);
				String fileName = (String) resultMap.get("FILE_NAME");
				// 写日志
				String opContent = "导出运营商统计报表：" + reportList.size() + "条 文件名："+fileName+",开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
				new QueryBiz().setLog(request, "运营商统计报表", opContent, StaticValue.GET);
				reportList.clear();
				reportList=null;
				HttpSession session =request.getSession(RptStaticValue.GET_SESSION_FALSE);
				session.setAttribute("spisuncm_export_map",resultMap);
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
			EmpExecutionContext.error(e, "导出异常");
		}
	}
	
	
	/**
	 * 运营商报表下载文件
	 * @param request
	 * @param response
	 */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response)   {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("spisuncm_export_map");
        session.removeAttribute("spisuncm_export_map");
        if(obj != null){
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            Map<String, String> resultMap = (Map<String, String>) obj;
            String fileName = (String) resultMap.get("FILE_NAME");
            String filePath = (String) resultMap.get("FILE_PATH");
            dfs.downFile(request, response, filePath, fileName);
        }
    }
	
	
	/**
	 * 各国发送详情查询
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void infoDetilByArea(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		//国家/地区代码
		String areaNo = request.getParameter("areacode");
		//国家名称
		String areaName = request.getParameter("areaname");
		// 登录企业编码信息
		String corpcode = request.getParameter("lgcorpcode");
		// 短彩类型
		String msType = request.getParameter("msType");
		//年份或月份查询条件
		String countTime = request.getParameter("countTime");
		//运营商查询条件
		String spisuncm = request.getParameter("spisuncm");
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		// 时间月份
		String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
		//日报表开始时间
		String startTime = request.getParameter("startTime");
		//日报表结束时间
		String endTime = request.getParameter("endTime");
		// 报表类型
		String reporttypestr = request.getParameter("reportType");
		//运营商id
		String staffId = request.getParameter("staffId");
		int reportType = 0;
		// 是否第一次执行
		boolean isFirstEnter = false;
		boolean isError = true;
		// 运营商查询条件对象
		OperatorsAreaMtDataReportVo operatorsAreaMtDataReportVo = new OperatorsAreaMtDataReportVo();
		// 分页对象
		PageInfo pageInfo = new PageInfo();
		//运营商报表结果集定义
		List<OperatorsAreaMtDataReportVo> reportList = null;
		//运营商报表biz
		OperatorsMtReportBiz omrbiz = new OperatorsMtReportBiz();
		
		try 
		{
			//运营商id
			operatorsAreaMtDataReportVo.setSpID(staffId);
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
			//短信类型
			if(msType == null)
			{
				msType = "0";
			}
			operatorsAreaMtDataReportVo.setMstype(Integer.parseInt(msType));
			
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
			operatorsAreaMtDataReportVo.setReporttype(reportType);
			// 年份或月份查询条件不为空则提取年份 和月份
			if(!"".equals(countTime) && countTime != null)
			{
				timeYear = countTime.substring(0, 4);
				timeMonth = countTime.length() == 7 ? countTime.substring(5,7) : timeMonth;
			}
			else
			{
				countTime = "";
			}
			//判断运营商
			if(spisuncm != null && !"".equals(spisuncm))
			{
				operatorsAreaMtDataReportVo.setSpisuncm(Long.parseLong(spisuncm));
			}
			//判断国家、地区代码
			if(areaNo != null && !"".equals(areaNo))
			{
				operatorsAreaMtDataReportVo.setAreacode(areaNo);
			}
			//判断国家、地区名称
			if(areaName != null && !"".equals(areaName))
			{
				operatorsAreaMtDataReportVo.setAreaname(areaName);
			}
			//时间
			operatorsAreaMtDataReportVo.setImonth(timeMonth);
			operatorsAreaMtDataReportVo.setY(timeYear);
			if(startTime != null && !"".equals(startTime))
			{
				operatorsAreaMtDataReportVo.setStartTime(startTime);
			}
			if(endTime != null && !"".equals(endTime))
			{
				operatorsAreaMtDataReportVo.setEndTime(endTime);
			}
			
			
			//结果集
			reportList = omrbiz.getListByAreadetail(operatorsAreaMtDataReportVo, pageInfo);
			
			// 总数
			request.setAttribute("sumCount", omrbiz.findSumCount(operatorsAreaMtDataReportVo));
			// 分页对象
			request.setAttribute("pageInfo", pageInfo);
			//存储对象
			request.setAttribute("operatorsAreaMtDataReportVo", operatorsAreaMtDataReportVo);
			//查询结果集
			request.setAttribute("reportList", reportList);
			
			//数据
			request.setAttribute("msType", msType);
			request.setAttribute("countTime",countTime );
			request.setAttribute("endTime", endTime);
			request.setAttribute("reportType", reportType);
			request.setAttribute("spisuncm", spisuncm);
			request.setAttribute("staffId", staffId);
			long count=0l;
			//从pageinfo中获取查询总条数
			if(pageInfo!=null){
				count=pageInfo.getTotalRec();
			}
			// 写日志
			String opContent = "运营商报表国家详情查询：" + count + "条开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
			new QueryBiz().setLog(request, "运营商报表国家详情", opContent, StaticValue.GET);
			
		} catch (Exception e) {
			long[] returnLong = new long[5];
			returnLong[0] = 0;
			returnLong[1] = 0;
			returnLong[2] = 0;
			returnLong[3] = 0;
			returnLong[4] = 0;
			request.setAttribute("sumCount", returnLong);
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "查询异常");
		}
		finally
		{
			request.getRequestDispatcher(this.empRoot + base + "/rep_spisuncmMtReportArea.jsp").forward(request, response);
		}
		
	}
	
	
	
	/**
	 * 运营商统计报表  各国发送详情  导出
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void r_stRptExportExcelByArea(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		//国家/地区代码
		String areaNo = request.getParameter("areacode");
		//国家名称
		String areaName = request.getParameter("areaname");
		// 短彩类型
		String msType = request.getParameter("mstype");
		//年份或月份查询条件
		String countTime = request.getParameter("countTime");
		//运营商查询条件
		String spisuncm = request.getParameter("spisuncm");
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		// 时间月份
		String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
		//日报表结束时间
		String endTime = request.getParameter("endTime");
		// 报表类型
		String reporttypestr = request.getParameter("reportType");
		//运营商id
		String staffId = request.getParameter("staffId");
		//提交总数合计
		long totalIcount = Long.parseLong(request.getParameter("totalIcount").toString());
		//发送失败数合计
		long totalRfail1 = Long.parseLong(request.getParameter("totalRfail1").toString());
		//接收失败数合计
		long totalRfail2 = Long.parseLong(request.getParameter("totalRfail2").toString());
		//未返数合计
		long totalRnret = Long.parseLong(request.getParameter("totalRnret").toString());		
		//接收成功数合计
		long totalRsucc = Long.parseLong(request.getParameter("totalRsucc").toString());
		int reportType = 0;
		// 是否第一次执行
		boolean isFirstEnter = false;
		boolean isError = true;
		// 运营商查询条件对象
		OperatorsAreaMtDataReportVo operatorsAreaMtDataReportVo = new OperatorsAreaMtDataReportVo();
		// 分页对象
		PageInfo pageInfo = new PageInfo();
		//运营商报表结果集定义
		List<OperatorsAreaMtDataReportVo> reportList = null;
		//运营商报表biz
		OperatorsMtReportBiz omrbiz = new OperatorsMtReportBiz();
		
		try 
		{
			//运营商id
			operatorsAreaMtDataReportVo.setSpID(staffId);
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
			//短信类型
			if(msType == null)
			{
				msType = "0";
			}
			operatorsAreaMtDataReportVo.setMstype(Integer.parseInt(msType));
			
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
			operatorsAreaMtDataReportVo.setReporttype(reportType);
			// 年份或月份查询条件不为空则提取年份 和月份
			if(!"".equals(countTime) && countTime != null)
			{
				timeYear = countTime.substring(0, 4);
				timeMonth = countTime.length() == 7 ? countTime.substring(5,7) : timeMonth;
			}
			else
			{
				countTime = "";
			}
			//判断运营商
			if(spisuncm != null && !"".equals(spisuncm))
			{
				operatorsAreaMtDataReportVo.setSpisuncm(Long.parseLong(spisuncm));
			}
			//判断国家、地区代码
			if(areaNo != null && !"".equals(areaNo))
			{
				operatorsAreaMtDataReportVo.setAreacode(areaNo);
			}
			//判断国家、地区名称
			if(areaName != null && !"".equals(areaName))
			{
				operatorsAreaMtDataReportVo.setAreaname(areaName);
			}
			//时间
			operatorsAreaMtDataReportVo.setImonth(timeMonth);
			operatorsAreaMtDataReportVo.setY(timeYear);
			if(countTime != null && !"".equals(countTime))
			{
				operatorsAreaMtDataReportVo.setStartTime(countTime);
			}
			if(endTime != null && !"".equals(endTime))
			{
				operatorsAreaMtDataReportVo.setEndTime(endTime);
			}
				
			//结果集
			reportList = omrbiz.getListByAreadetail(operatorsAreaMtDataReportVo, null);
			
			response.setContentType("html/text");
			PrintWriter out = response.getWriter();
			//判断结果集是否有数据有则导出
			if(reportList != null && reportList.size() > 0)
			{	
				//createSpReportExcel(reportList, countTime, succ, rfail2);
				Map<String, String> resultMap = omrbiz.createSpReportExcelByArea(reportList, countTime,totalIcount, totalRfail1, totalRfail2, totalRnret, totalRsucc,operatorsAreaMtDataReportVo,request);
				String fileName = (String) resultMap.get("FILE_NAME");
				// 写日志
				String opContent = "导出运营商统计各国详情报表：" + reportList.size() + "条 文件名："+fileName+",开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
				new QueryBiz().setLog(request, "运营商统计报表-各国详情", opContent, StaticValue.GET);
				reportList.clear();
				reportList=null;
				HttpSession session =request.getSession(RptStaticValue.GET_SESSION_FALSE);
				session.setAttribute("spisuncmarea_export_map",resultMap);
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
			EmpExecutionContext.error(e, "导出异常");
		}
	}
	
	/**
	 * 运营商报表国家详情下载文件
	 * @param request
	 * @param response
	 */
    public void spisuncmareadownloadFile(HttpServletRequest request, HttpServletResponse response)   {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("spisuncmarea_export_map");
        session.removeAttribute("spisuncmarea_export_map");
        if(obj != null){
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            Map<String, String> resultMap = (Map<String, String>) obj;
            String fileName = (String) resultMap.get("FILE_NAME");
            String filePath = (String) resultMap.get("FILE_PATH");
            dfs.downFile(request, response, filePath, fileName);
        }
    }
	
	
}
