package com.montnets.emp.jfcx.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.jfcx.biz.CrmBillQueryBiz;
import com.montnets.emp.jfcx.biz.ReportBiz;
import com.montnets.emp.jfcx.vo.CrmBillQueryVo;
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
 * 客户计费查询servlet
 * 
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:48:43
 * @description
 */
public class ydyw_crmBillQuerySvt extends BaseServlet
{
	private static final long	serialVersionUID	= 1908901989360839320L;

	// 客户计费查询biz
	private final CrmBillQueryBiz		crmbillquerybz		= new CrmBillQueryBiz();

	// 报表biz
	private final ReportBiz			reportBiz			= new ReportBiz();

	// 模块名称
	private final String						empRoot				= "ydyw";

	// 基路径
	private final String						base				= "/jfcx";

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
	 * 客户计费查询查询方法
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 获得session
		// 是否第一次访问
		boolean isFirstEnter = false;
		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 登录操作员id
		//String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);

		// 分页对象
		PageInfo pageInfo = new PageInfo();
		
		//添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
		
		try
		{
			List<CrmBillQueryVo> mtreportList = null;
			boolean isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
			if(!isError)
			{
				// 设置分页
				pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
				pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
			}
			else
			{
				//默认选择子机构
				request.setAttribute("isContainsSun", "1");
			}
			if(!isFirstEnter)
			{
				BaseBiz baseBiz = new BaseBiz();
				// 获取当前登录操作员对象
				LfSysuser user = baseBiz.getById(LfSysuser.class, userid);
				// 手机号
				String mobile = request.getParameter("mobile");
				// 姓名
				String custoname = request.getParameter("custoname");
				// 签约账号
				String openbusaccount = request.getParameter("openbusaccount");
				// 计费类型
				String taocantype = request.getParameter("taocantype");
				// 扣费状态
				String oprstate = request.getParameter("oprstate");
				//签约状态
				String contractstate = request.getParameter("contractstate");
				// 扣费账号
				String debitaccount = request.getParameter("debitaccount");
				// 签约套餐
				String taocanname = request.getParameter("taocanname");
				// 部门名称组合字符串
				String dName = request.getParameter("depNam");
				// 部门ID组合字符串
				String depids = request.getParameter("deptString");
				// 扣费时间
				// 开始时间
				String begintime = request.getParameter("begintime");
				// 结束时间
				String endtime = request.getParameter("endtime");
				//默认查询当月记录
				if((begintime==null||"".equals(begintime))&&(endtime==null||"".equals(endtime)))
				{
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DAY_OF_MONTH, -1);
					c.add(Calendar.DATE,1);
					begintime = df.format(c.getTime()).substring(0, 8) + "01 00:00:00";
					endtime = df.format(c.getTime()).substring(0, 10) + " 23:59:59";
				}
				// 查询对象vo
				CrmBillQueryVo crmbillqueryvo = new CrmBillQueryVo();
				// 手机号
				crmbillqueryvo.setMobile(mobile);
				// 姓名
				crmbillqueryvo.setCustomname(custoname);
				// 签约账号
				crmbillqueryvo.setAcctno(openbusaccount);
				// 计费类型
				if(taocantype != null && !"".equals(taocantype))
				{
					crmbillqueryvo.setTaocantype(Integer.parseInt(taocantype));
				}
				// 扣费状态
				if(oprstate != null && !"".equals(oprstate))
				{
					crmbillqueryvo.setOprstate(Integer.parseInt(oprstate));
				}
				//签约状态
				if(contractstate!=null && !"".equals(contractstate))
				{
					crmbillqueryvo.setContractstate(Integer.parseInt(contractstate));
				}
				// 扣费账号
				crmbillqueryvo.setDebitaccount(debitaccount);
				// 签约套餐
				crmbillqueryvo.setTaocanname(taocanname);
				// 起始时间
				crmbillqueryvo.setSendTime(begintime);
				// 结束时间
				crmbillqueryvo.setEndTime(endtime);
				// 是否包含子机构
				String isContainsSun = request.getParameter("isContainsSun");
				if(isContainsSun != null && !"".equals(isContainsSun))
				{
					crmbillqueryvo.setIsContainsSun(isContainsSun);
					request.setAttribute("isContainsSun", isContainsSun);
				}
				else
				{
					crmbillqueryvo.setIsContainsSun("");
					//如果没选部门，包含子机构
					if(depids==null||"".equals(depids))
					{
						crmbillqueryvo.setIsContainsSun("1");
					}
				}
				//标准版和托管版都需要传正确的企业编码
/*				if(StaticValue.CORPTYPE == 0)
				{
					corpCode = "0";
				}*/
				mtreportList = crmbillquerybz.getCrmBillQueryVoList(depids, crmbillqueryvo, user.getUserId(), corpCode, pageInfo);
				// 分页对象
				request.setAttribute("pageInfo", pageInfo);
				// 机构结果
				request.setAttribute("resultList", mtreportList);
				// 部门名称
				request.setAttribute("dName", dName);
				// 部门id字符串
				request.setAttribute("deptString", depids);
			}
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
    		try {
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
					
					EmpExecutionContext.info("客户计费查询", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent, "GET");
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "记录操作日志异常！");
			}
			
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.getRequestDispatcher(this.empRoot + base + "/ydyw_crmBillQuery.jsp?lgcorpcode=" + corpCode + "&lguserid=" + userid).forward(request, response);
		}
	}

	/**
	 * 查询计费历史详情
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findBillDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 是否第一次访问
		boolean isFirstEnter = false;
		// 类型
		String msType = request.getParameter("msType");
		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 登录操作员id
		//String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);


		// 判断短信类型是否为空如果为空设置默认为短信
		if(msType == null)
		{
			msType = "0";
		}

		PageInfo pageInfo = new PageInfo();
		try
		{
			// 获得session
			HttpSession session = request.getSession(false);
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
				isFirstEnter = false;
			}
			if(!isFirstEnter)
			{
				// 分页对象
				request.setAttribute("pageInfo", pageInfo);

			}
			else
			{
				session.setAttribute("succ", 0);
				session.setAttribute("rfail2", 0);
			}

			GlobConfigBiz gcBiz = new GlobConfigBiz();
			// 短彩类型数据获取
			List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");
			// 短彩类型数据
			request.getSession(false).setAttribute("pagefileddeps", pagefileds);
			// 短彩类型值
			request.setAttribute("msType", msType);
		}
		catch (Exception e)
		{
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "后台异常");

		}
		finally
		{
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.getRequestDispatcher(this.empRoot + base + "/ydyw_BillDetailQuery.jsp?lgcorpcode=" + corpCode + "&lguserid=" + userid).forward(request, response);
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
			//session获取当前操作员对象
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

	// 输出人员代码数据
	public void createUserTree(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			// 企业编码
			String corpcode = request.getParameter("lgcorpcode");
			// 部门操作员树
			String deptUserTree = getDeptUserJosnData(corpcode);
			response.getWriter().print(deptUserTree);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "输出人员代码数据异常");
		}
	}

	// 获取人员代码数据
	public String getDeptUserJosnData(String corpCode)
	{
		List<LfDep> lfDeps;
		List<LfSysuser> lfSysusers;
		StringBuffer tree = null;
		try
		{
			// 查询出所有的机构
			lfDeps = reportBiz.getAllDepsByCorpCode(corpCode);
			LfDep lfDep = null;
			tree = new StringBuffer("[");
			for (int i = 0; i < lfDeps.size(); i++)
			{
				lfDep = lfDeps.get(i);
				tree.append("{");
				tree.append("id:").append(lfDep.getDepId());
				tree.append(",name:'").append(lfDep.getDepName()).append("'");
				tree.append(",pId:").append(lfDep.getSuperiorId());
				tree.append(",isParent:").append(true);
				tree.append(",nocheck:").append(true);
				tree.append("}");
				if(i != lfDeps.size() - 1)
				{
					tree.append(",");
				}
			}

			lfSysusers = reportBiz.getAllSysusersByCorpCode(corpCode);
			LfSysuser lfSysuser = null;
			if(!lfSysusers.isEmpty())
			{
				tree.append(",");
			}
			for (int i = 0; i < lfSysusers.size(); i++)
			{
				lfSysuser = lfSysusers.get(i);
				tree.append("{");
				tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
				tree.append(",name:'").append(lfSysuser.getName()).append("'");
				tree.append(",pId:").append(lfSysuser.getDepId());
				tree.append(",isParent:").append(false);
				tree.append("}");

				if(i != lfSysusers.size() - 1)
				{
					tree.append(",");
				}
			}
			tree.append(",{");
			tree.append("id:'u").append("-1'");
			tree.append(",name:'").append("未知操作员'");
			tree.append(",pId:").append("1");
			tree.append(",isParent:").append(false);
			tree.append("}");

			tree.append("]");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取人员数据异常");
		}
		return tree.toString();
	}

	/**
	 * 将传递过来的机构，进行格式转换
	 * 
	 * @param deptName
	 * @return
	 */
	public String getSpiltDepName(String deptName)
	{

		String returndepIds = "";

		// 选择机构名称，则按机构查询
		if(!"".equals(deptName) && null != deptName)
		{
			String[] deptIds = null;

			// 拼接机构ID字符串
			StringBuffer sbDep = new StringBuffer();

			deptIds = deptName.split(",");
			// 存储机构ID
			List<String> depIds = new ArrayList<String>();

			for (int i = 0; i < deptIds.length; i++)
			{

				depIds.add(deptIds[i].substring(deptIds[i].lastIndexOf(":") + 1));

			}
			for (int j = 0; j < depIds.size(); j++)
			{

				if(j == depIds.size() - 1)
				{
					sbDep = sbDep.append(depIds.get(j).toString());

				}
				else
				{

					sbDep = sbDep.append(depIds.get(j).toString() + ",");
				}

			}

			returndepIds = sbDep.toString();

		}
		return returndepIds;

	}

	/**
	 * 将传递过来的用户数据，进行格式转换
	 * 
	 * @param userName
	 * @return
	 */
	public String getSplitUserIds(String userName)
	{
		String usersIds = "";

		if(!"".equals(userName) && null != userName)
		{

			String[] userIds = null;
			// 拼接操作员ID字符串
			StringBuffer sb = new StringBuffer();
			if(userName != null && !userName.equals(""))
			{

				userIds = userName.split(",");

			}
			// 存储操作员ID
			List<String> sysIds = new ArrayList<String>();
			if(userName != null && !userName.equals("") && userIds!=null) for (int i = 0; i < userIds.length; i++)
			{
				sysIds.add(userIds[i].substring(1));

			}
			for (int j = 0; j < sysIds.size(); j++)
			{
				if(j == sysIds.size() - 1) sb = sb.append(sysIds.get(j).toString());
				else sb = sb.append(sysIds.get(j).toString() + ",");
			}
			usersIds = sb.toString();

		}

		return usersIds;
	}

	/**
	 * 下载导出文件
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute("r_sdRpt_export_map2");
			session.removeAttribute("r_sdRpt_export_map2");
			if (obj != null) {
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

	/**
	 * 客户计费查询导出
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	// excel导出全部数据
	public void r_sdRptExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		List<CrmBillQueryVo> mdlist = null;
		String queryTime = URLDecoder.decode(request.getParameter("queryTime"), "UTF-8");
		PrintWriter out = response.getWriter();
		try
		{
			HttpSession session = request.getSession(false);
			// 获取当前用户的企业编码
			String corpCode = request.getParameter("lgcorpcode");
			// 查询条件中的机构IDs
			String depids = request.getParameter("depNam");
			// 短彩类型
			String mstype = request.getParameter("mstype");
			// 数据源类型
			String datasourcetype = request.getParameter("datasourcetype");
			// 开始时间
			String begintime = null == request.getParameter("begintime") ? "" : request.getParameter("begintime").toString().trim();
			// 结束时间
			String endtime = null == request.getParameter("endtime") ? "" : request.getParameter("endtime").toString().trim();
			// 判断短彩类型如果为空则默认为短信
			if(mstype == null || "".equals(mstype))
			{
				mstype = "0";
			}

			if(datasourcetype == null || "".equals(datasourcetype))
			{
				datasourcetype = "0";
			}

			// 当前操作员所拥有的权限
			BaseBiz baseBiz = new BaseBiz();
			LfSysuser lfUser = baseBiz.getLfSysuserByUserId(request.getParameter("lguserid"));
			Integer permissionType = lfUser.getPermissionType();
			CrmBillQueryVo mtDataReportVo = new CrmBillQueryVo();
			mtDataReportVo.setSendTime(begintime);
			mtDataReportVo.setEndTime(endtime);
			CrmBillQueryBiz depReportBiz = new CrmBillQueryBiz();
			//标准版和托管版都需要传正确的企业编码
/*			if(StaticValue.CORPTYPE == 0)
			{
				corpCode = "0";
			}*/
			mdlist = depReportBiz.getCrmBillQueryVoListUnPage(permissionType, depids, mtDataReportVo, lfUser.getUserId(), corpCode);

			Object succObj = session.getAttribute("succ");
			Object rfail2Obj = session.getAttribute("rfail2");
			Object showTimeObj = session.getAttribute("showTime");
			Object datasourcenameObj = session.getAttribute("datasourcename");
			String succ = succObj == null ? "":succObj.toString();
			String rfail2 = rfail2Obj == null ? "":rfail2Obj.toString();
			String showTime = showTimeObj == null ? "":showTimeObj.toString();
			String datasourcename = datasourcenameObj == null ? "":datasourcenameObj.toString();
			//语言格式
			String empLangName = request.getParameter("empLangName");

			if(mdlist != null && mdlist.size() > 0)
			{

				Map<String, String> resultMap = depReportBiz.createCrmBillQueryExcel(mdlist, queryTime, succ, rfail2, showTime, datasourcename,empLangName);
				//String fileName =  resultMap.get("FILE_NAME");
				//String filePath =  resultMap.get("FILE_PATH");
				/*新增 p*/
				session.setAttribute("r_sdRpt_export_map2",resultMap);
				// 写日志
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				if(lfSysuser != null)
				{
					String opUser = lfSysuser.getUserName();
					String opContent = "导出客户计费查询：" + mdlist.size() + "条 ";
					String corpcode = lfSysuser.getCorpCode();
					opUser = opUser == null ? "" : opUser;
					corpcode = corpcode == null ? "" : corpcode;
					EmpExecutionContext.info("企业：" + corpcode + ",操作员：" + opUser + opContent + "成功");
					mdlist.clear();
					mdlist = null;
				}
				//DownloadFile df = new DownloadFile();
				//df.downFile(request, response, filePath, fileName);
				out.print("true");
			}else{
				out.print("false");
			}
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "客户计费查询导出异常");
		}
	}
}
