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
import com.montnets.emp.entity.ydyw.LfDeductionsList;
import com.montnets.emp.jfcx.biz.CrmBillQueryBiz;
import com.montnets.emp.jfcx.biz.CrmRfMgrBiz;
import com.montnets.emp.jfcx.biz.ReportBiz;
import com.montnets.emp.jfcx.vo.CrmBillQueryVo;
import com.montnets.emp.jfcx.vo.CrmRfMgrVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 客户退费管理查询
 * 
 * @project p_ydyw
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-12-29 下午04:42:19
 * @description
 */
public class ydyw_crmRfMgrSvt extends BaseServlet
{
	private static final long	serialVersionUID	= 1908901989360839320L;

	// 报表biz
	private final ReportBiz			reportBiz			= new ReportBiz();
	
	//
	private final BaseBiz				baseBiz 			= new BaseBiz();

	//退费biz
	private final CrmRfMgrBiz 		crmRfMgrBiz   		= new CrmRfMgrBiz();
	
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
	 * 客户退费管理查询
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
		
		//添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
		
		
		try
		{
			List<CrmRfMgrVo> mtreportList = null;
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
				//电话
				String mobile = request.getParameter("mobile");
				//姓名
				String customname = request.getParameter("customname");
				//签约账号
				String acctno = request.getParameter("acctno");
				//套餐类型
				String taocantype = request.getParameter("taocantype");
				//签约状态
				String contractstate = request.getParameter("contractstate");
				//扣费状态
				String deductionstype = request.getParameter("deductionstype");
				//扣费账号
				String debitaccount = request.getParameter("debitaccount");
				//签约套餐
				String taocanname = request.getParameter("taocanname");
				//操作员
				String username = request.getParameter("username");
				// 部门名称组合字符串
				String dName = request.getParameter("depNam");
				// 部门ID组合字符串
				String depids = request.getParameter("deptString");
				// 是否包含子机构
				String isContainsSun = request.getParameter("isContainsSun");
				// 	开始时间
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
					endtime = df.format(c.getTime()).substring(0, 10) + " 23:59:59"; //change by dj
				}
				// 查询对象vo
				CrmRfMgrVo crmRfMgrVo = new CrmRfMgrVo(); 
				crmRfMgrVo.setMobile(mobile);
				crmRfMgrVo.setCustomname(customname);
				crmRfMgrVo.setAcctno(acctno);
				crmRfMgrVo.setUsername(username);
				if(taocantype!=null&&!"".equals(taocantype))
				{
					crmRfMgrVo.setTaocantype(Integer.parseInt(taocantype));
				}
				if(contractstate!=null&&!"".equals(contractstate))
				{
					crmRfMgrVo.setContractstate(Integer.parseInt(contractstate));
				}
				if(deductionstype!=null&&!"".equals(deductionstype))
				{
					crmRfMgrVo.setDeductionstype(Integer.parseInt(deductionstype));
				}
				crmRfMgrVo.setTaocanname(taocanname);
				crmRfMgrVo.setDebitaccount(debitaccount);
				if(isContainsSun != null && !"".equals(isContainsSun))
				{
					crmRfMgrVo.setIsContainsSun(isContainsSun);
					request.setAttribute("isContainsSun", isContainsSun);
				}
				else
				{
					crmRfMgrVo.setIsContainsSun("");
					//如果没选部门，包含子机构
					if(depids==null||"".equals(depids))
					{
						crmRfMgrVo.setIsContainsSun("1");
					}
				}
				// 起始时间
				crmRfMgrVo.setSendTime(begintime);
				// 结束时间
				crmRfMgrVo.setEndTime(endtime);
				//标准版和托管版都需要传正确的企业编码
/*				if(StaticValue.CORPTYPE == 0)
				{
					corpCode = "0";
				}*/
				BaseBiz baseBiz = new BaseBiz();
				// 获取当前登录操作员对象
				LfSysuser user = baseBiz.getById(LfSysuser.class, userid);
				mtreportList = crmRfMgrBiz.getCrmRfMgrVoList(depids, crmRfMgrVo, user.getUserId(), corpCode, pageInfo);
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
					
					EmpExecutionContext.info("客户退费管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent, "GET");
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "记录操作日志异常！");
			}
			
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.getRequestDispatcher(this.empRoot + base + "/ydyw_crmRfMgr.jsp?lgcorpcode=" + corpCode + "&lguserid=" + userid).forward(request, response);
		}

	}

	/**
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
	 * 机构报表导出
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

			String succ = session.getAttribute("succ").toString();

			String rfail2 = session.getAttribute("rfail2").toString();

			String showTime = session.getAttribute("showTime").toString();

			String datasourcename = session.getAttribute("datasourcename").toString();

			if(mdlist != null && mdlist.size() > 0)
			{

				Map<String, String> resultMap = depReportBiz.createCrmBillQueryExcel(mdlist, queryTime, succ, rfail2, showTime, datasourcename);
				String fileName = (String) resultMap.get("FILE_NAME");
				String filePath = (String) resultMap.get("FILE_PATH");
				// 写日志
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				if(lfSysuser != null)
				{
					String opUser = lfSysuser.getUserName();
					String opContent = "导出机构统计报表：" + mdlist.size() + "条 ";
					String corpcode = lfSysuser.getCorpCode();
					opUser = opUser == null ? "" : opUser;
					corpcode = corpcode == null ? "" : corpcode;
					EmpExecutionContext.info("企业：" + corpcode + ",操作员：" + opUser + opContent + "成功");
					mdlist.clear();
					mdlist = null;
				}
				DownloadFile df = new DownloadFile();
				df.downFile(request, response, filePath, fileName);
			}
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "机构报表导出异常");
		}
	}

	/**
	 * 获取退费信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getBackMoney(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 登录操作员id
		//String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);

		//退费类型
		String type = request.getParameter("type");
		//退费信息ID
		String id = request.getParameter("id");
		try
		{
			//单个退费
			if("1".equals(type))
			{
				LfDeductionsList lfDeductionsList = baseBiz.getById(LfDeductionsList.class, id);
				request.setAttribute("lfDeductionsList", lfDeductionsList);

				/*LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("id", id);
				List<LfDeductionsList> resultList = baseBiz.getByCondition(LfDeductionsList.class, conditionMap, null);
				request.setAttribute("resultList", resultList);*/
			}
			//批量退费
			else
			{
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取客户消费信息异常");
		}
		finally
		{
			request.getRequestDispatcher(this.empRoot + base + "/ydyw_refund.jsp?lgcorpcode=" + corpCode + "&lguserid=" + userid).forward(request, response);
		}
	}
	/**
	 * 单个退费
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void singleRefund(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 登录操作员id
		//String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);

		//退费信息ID
		String id = request.getParameter("ids");
		//退费金额
		String money = request.getParameter("money");
		//返回消息
		String result = "";
		//退款是否比资费高
		boolean compare = false;
		try
		{
			int intMoney = Integer.parseInt(money);
			//客户计费信息
			LfDeductionsList list = baseBiz.getById(LfDeductionsList.class, id);
			
			//查询操作之前记录
			String befchgCont = list.getId()+"，"+list.getTaocanmoney()+"，"+list.getUserid();
			
			if(intMoney>list.getTaocanmoney())
			{
				compare = true;
			}
			if(!compare)
			{
				result = crmRfMgrBiz.singleRefund(list, money, userid);
				
				//增加操作日志
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent1 = "单个退费"+("success".equals(result)?"成功":"失败")+"。[退费信息ID，退费金额，操作员id]" +
							"("+befchgCont+")->("+id+"，"+money+"，"+userid+")";
					EmpExecutionContext.info("客户退费管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent1, "UPDATE");
				}
			}
			else
			{
				result = "more";
			}
			
		}
		catch (Exception e)
		{
			result = "fail";
			EmpExecutionContext.error(e, "单个退费异常");
		}
		finally
		{
			response.getWriter().print(result);
		}
	}
	
	/**
	 * 批量退费
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void muliteRefund(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 登录操作员id
		//String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);
		//退费信息ID
		String id = request.getParameter("ids");
		id = id.substring(0,id.lastIndexOf(","));
		//退费金额
		String money = request.getParameter("money");
		int intMoney = Integer.parseInt(money);
		//退款是否比资费高
		boolean compare = false;
		//返回值
		String message = "";
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("id&in", id);
			List<LfDeductionsList> list = baseBiz.getByCondition(LfDeductionsList.class, conditionMap, null);
			
			//查询操作之前记录
			String befchgCont = "";
			if(list.size()>0){
				for(int j=0 ;j<list.size();j++){
					befchgCont += "("+list.get(j).getId()+"，"+list.get(j).getTaocanmoney()+"，"+list.get(j).getUserid()+")";
				}
			}
			
			//比较退费与最低套餐资费
			if(list!=null&&list.size()>0)
			{
				for(int i = 0;i<list.size();i++)
				{
					if(intMoney>list.get(i).getTaocanmoney())
					{
						compare = true;
					}
				}
			}
			if(compare)
			{
				message = "more";
			}
			else
			{
				message = crmRfMgrBiz.muliteRefund(list, money, userid);
				
				//增加操作日志
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent1 = "批量退费"+("success".equals(message)?"成功":"失败")+"。[退费信息ID，退费金额，操作员id]" +
							"("+befchgCont+")->("+id+"，"+money+"，"+userid+")";
					EmpExecutionContext.info("客户退费管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent1, "UPDATE");
				}
			}
		}
		catch (Exception e)
		{
			message = "fail";
			EmpExecutionContext.error(e, "批量退费异常");
		}
		finally
		{
			response.getWriter().print(message);
		}
	}
}
