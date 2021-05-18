package com.montnets.emp.report.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.report.biz.DepBillQueryBiz;
import com.montnets.emp.report.vo.DepBillQueryVo;
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
import java.util.*;

/**
 * 机构计费统计报表servlet
 * 
 * @project p_ydyw
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-7 上午10:52:35
 * @description
 */
public class ydyw_depBillQuerySvt extends BaseServlet
{
	private static final long	serialVersionUID	= 1908901989360839320L;

	// 机构计费统计报表biz
	private final DepBillQueryBiz		dbqvbiz		= new DepBillQueryBiz();

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
	 * 机构计费统计报表查询方法
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
		// 登录操作员id
		///String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);


		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		// 时间月份
		String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
		// 年份或月份查询条件
		String countTime = request.getParameter("countTime");
		// 报表类型
		String reporttypestr = request.getParameter("reportType");
		// 机构计费统计条件对象
		DepBillQueryVo depbillqueryvo = new DepBillQueryVo();
		// 报表类型
		int reportType = 0;
		PageInfo pageInfo = new PageInfo();
		
		//添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
		
		try
		{
			// 获得session
			HttpSession session = request.getSession(false);
			List<DepBillQueryVo> billQueryList = null;
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
			if(isError)
			{
				isFirstEnter = true;
			}
			else
			{
				// 设置分页
				pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
				pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
				pageInfo.setTotalPage(getIntParameter("totalPage", pageInfo.getTotalPage(), request));
				pageInfo.setTotalRec(getIntParameter("totalRec", pageInfo.getTotalRec(), request));
				isFirstEnter = false;
			}
			
			//时间
			depbillqueryvo.setY(timeYear);
			if(reportType == 0)
			{
				// 月报表
				depbillqueryvo.setImonth(timeMonth);
			}
			else if(reportType == 1)
			{
				// 年报表
				depbillqueryvo.setImonth(null);
			}
			
			if(!isFirstEnter)
			{
				// 部门名称组合字符串
				String dName = request.getParameter("depNam");
				// 部门ID组合字符串
				String depids = request.getParameter("deptString");
				BaseBiz baseBiz = new BaseBiz();
				// 获取当前登录操作员对象
				LfSysuser user = baseBiz.getById(LfSysuser.class, userid);
				// 当前操作员所拥有的权限
				Integer permissionType = user.getPermissionType();
				if(permissionType != 1)
				{
					//标准版和托管版都需要传正确的企业编码
/*					if(StaticValue.CORPTYPE == 0)
					{
						corpCode = "0";
					}*/
					//套餐名称
					String taocanname=request.getParameter("taocanname");
					//套餐类型
					String taocantype=request.getParameter("taocantype");
					//套餐名称
					depbillqueryvo.setTaocanname(taocanname);
					//套餐类型
					if(taocantype!=null&&!"".equals(taocantype)){
						depbillqueryvo.setTaocantype(Integer.parseInt(taocantype.trim()));
					}
					// 合计
					long sumtotalmoney;
					billQueryList = dbqvbiz.getDepBillQueryVoList(depids, depbillqueryvo, user.getUserId(), corpCode, pageInfo);
					sumtotalmoney = dbqvbiz.findSumCount(user.getUserId(), depids, depbillqueryvo, corpCode);
					session.setAttribute("sumtotalmoney", sumtotalmoney);
//					if(sumtotalmoney == 0){
//						pageInfo = new PageInfo();
//					}
					// 机构结果
					session.setAttribute("depresultList", billQueryList);
					//新增 将结果放到request中 p
					request.setAttribute("depresultList_p", billQueryList);
				}
				else
				{
					session.setAttribute("sumtotalmoney", 0);
				}
				// 部门名称
				request.setAttribute("dName", dName);
				// 部门id字符串
				request.setAttribute("deptString", depids);
			}
			//查询条件
			request.setAttribute("depbillqueryvo", depbillqueryvo);
			// 分页对象
			request.setAttribute("pageInfo", pageInfo);
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
			
			try {
				//增加操作日志 p
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
					
					EmpExecutionContext.info("机构计费统计", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent, "GET");
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "添加操作日志异常！");
			}
			
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.getRequestDispatcher(this.empRoot + base + "/ydyw_depBillQuery.jsp?lgcorpcode=" + corpCode + "&lguserid=" + userid).forward(request, response);
		}

	}

	// 输出机构代码数据
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			Long depId = null;
			Long userid = null;
			// 部门iD
			String depStr = request.getParameter("depId");
			// 操作员账号
			//String userStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userStr = SysuserUtil.strLguserid(request);

			if(depStr != null && !"".equals(depStr.trim()))
			{
				depId = Long.parseLong(depStr);
			}
			if(userStr != null && !"".equals(userStr.trim()))
			{
				userid = Long.parseLong(userStr);
			}
			//从session获取当前操作员
			LfSysuser lfSysuser = getLoginUser(request);
			String corpCode = lfSysuser.getCorpCode();
			String departmentTree = this.getDepartmentJosnData(depId, userid,corpCode);
			response.getWriter().print(departmentTree);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "后台异常");
		}
	}

	// 异步加载机构的主方法
	private String getDepartmentJosnData(Long depId, Long userid,String corpCode)
	{

		StringBuffer tree = null;
		try
		{
			// 当前登录操作员
			BaseBiz baseBiz = new BaseBiz();
			LfSysuser curUser = baseBiz.getLfSysuserByUserId(userid);
			// 判断是否个人权限
			if(curUser.getPermissionType() == 1)
			{
				tree = new StringBuffer("[]");
			}
			else
			{
				// 机构biz
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps;

				lfDeps = null;

				if(curUser.getCorpCode().equals("100000"))
				{
					if(depId == null)
					{
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
					}
					else
					{
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}

				}
				else
				{
					if(depId == null)
					{
						lfDeps = new ArrayList<LfDep>();
						//LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userid, corpCode).get(0);
						lfDeps.add(lfDep);
					}
					else
					{
						//lfDeps = new DepBiz().getDepsByDepSuperId(depId);
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,corpCode);
					}
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++)
				{
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size() - 1)
					{
						tree.append(",");
					}
				}
				tree.append("]");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "后台异常");
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}

	// 异步加载机构的主方法
	private String getDepartmentJosnData(Long depId, Long userid)
	{

		StringBuffer tree = null;
		try
		{
			// 当前登录操作员
			BaseBiz baseBiz = new BaseBiz();
			LfSysuser curUser = baseBiz.getLfSysuserByUserId(userid);
			// 判断是否个人权限
			if(curUser.getPermissionType() == 1)
			{
				tree = new StringBuffer("[]");
			}
			else
			{
				// 机构biz
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps;

				lfDeps = null;

				if(curUser.getCorpCode().equals("100000"))
				{
					if(depId == null)
					{
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
					}
					else
					{
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}

				}
				else
				{
					if(depId == null)
					{
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						lfDeps.add(lfDep);
					}
					else
					{
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++)
				{
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size() - 1)
					{
						tree.append(",");
					}
				}
				tree.append("]");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "后台异常");
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}
	
	/**
	 * 机构计费报表导出(全部)
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void r_sdRptExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		List<DepBillQueryVo> mdlist = null;
		// 获取页面上的时间段
		String countTime = URLDecoder.decode(request.getParameter("countTime"), "utf-8");
		PrintWriter out = response.getWriter();
		
		//添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
		try
		{
			HttpSession session = request.getSession(false);
			// 企业编码
			String corpCode = request.getParameter("lgcorpcode");
			// 查询条件中的机构IDs
			String depids = request.getParameter("deptString");
			// 报表类型
			String reporttypestr = request.getParameter("reportType");
			//语言格式
			String empLangName = request.getParameter("empLangName");
			// 时间格式
			String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
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
			BaseBiz baseBiz = new BaseBiz();
			LfSysuser lfUser = baseBiz.getLfSysuserByUserId(request.getParameter("lguserid"));
			DepBillQueryVo dbqv = new DepBillQueryVo();
			if(StaticValue.getCORPTYPE() ==0)
			{
				corpCode="0";
			}
			// 设置运营商账户时间
			dbqv.setY(timeYear);
			if(reportType == 0)
			{
				// 月报表
				dbqv.setImonth(timeMonth);
			}
			else if(reportType == 1)
			{
				// 年报表
				dbqv.setImonth(null);
			}
			//套餐名称
			String taocanname = URLDecoder.decode(request.getParameter("taocanname"), "utf-8");
			//套餐类型
			String taocantype=request.getParameter("taocantype");
			//套餐名称
			dbqv.setTaocanname(taocanname);
			//套餐类型
			if(taocantype!=null&&!"".equals(taocantype)){
				dbqv.setTaocantype(Integer.parseInt(taocantype.trim()));
			}

			String sumtotalmoney = session.getAttribute("sumtotalmoney").toString();
			mdlist = dbqvbiz.getDepBillQueryVoDepUnPage(depids, dbqv, lfUser.getUserId(), corpCode);
			//判断结果集是否有数据有则导出
			if(mdlist != null && mdlist.size() > 0)
			{
				Map<String, String> resultMap = dbqvbiz.createDepBillQueryExcel(mdlist, countTime, sumtotalmoney,empLangName);
				
				/*备份 p
				String fileName = (String) resultMap.get("FILE_NAME");
				String filePath = (String) resultMap.get("FILE_PATH");
				*/
				
				/*新增 p*/
	            session.setAttribute("r_sdRpt_export_map1",resultMap);

				// 写日志
				LfSysuser lfSysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
				if(lfSysuser != null)
				{
					String opUser = lfSysuser.getUserName();
					String opContent = "导出机构计费统计报表：" + mdlist.size() + "条 ";
					String corpcode = lfSysuser.getCorpCode();
					opUser = opUser == null ? "" : opUser;
					corpcode = corpcode == null ? "" : corpcode;
					EmpExecutionContext.info("企业：" + corpcode + ",操作员：" + opUser + opContent + "成功");
					
					//增加操作日志
					Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
						String opContent1 = "导出机构计费统计成功。(数量："+mdlist.size()+"条)";
						EmpExecutionContext.info("机构计费统计", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
								loginSysuser.getUserName(), opContent1, "OTHER");
					}					
					mdlist.clear();
					mdlist=null;
				}
				//DownloadFile df = new DownloadFile();
				//df.downFile(request, response, filePath, fileName);
				/*新增 p*/
				out.print("true");
			}else{
				out.print("false");
			}
			
			//添加与日志相关 p
			long endTimeByLog = System.currentTimeMillis();  //查询结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时
			
			//增加操作日志 p
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent = "导出开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms";
				
				EmpExecutionContext.info("机构计费统计", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent, "OTHER");
			}
			
		}
		catch (Exception e)
		{
			out.print("false");
			// 异常处理
			EmpExecutionContext.error(e, "机构计费统计导出异常");
		}

	}
	
	
	
	
	/**
	 * 下载导出文件  新增方法 p
	 * @param request
	 * @param response
	 */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response)   {
        try {
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute("r_sdRpt_export_map1");
			session.removeAttribute("r_sdRpt_export_map1");
			if(obj != null){
				//弹出下载页面
			    DownloadFile dfs = new DownloadFile();
			    Map<String, String> resultMap = (Map<String, String>) obj;
			    String fileName = (String) resultMap.get("FILE_NAME");
			    String filePath = (String) resultMap.get("FILE_PATH");
			    dfs.downFile(request, response, filePath, fileName);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "下载导出文件异常！");
		}
    }
	
}
