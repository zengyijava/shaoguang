package com.montnets.emp.employee.servlet;


import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.employee.biz.EmployeeBookBiz;
import com.montnets.emp.employee.vo.AddrBookPermissionsVo;
import com.montnets.emp.employee.vo.LfEnterpriseVo;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 通讯录权限绑定
 * a_permissionsServlet
 * @author Administrator
 *
 */

@SuppressWarnings("serial")
public class epl_permissionsSvt extends BaseServlet {
	
	//操作日志模块
	final String opModule=StaticValue.ADDBR_MANAGER;
    //操作员
	final String opSper = StaticValue.OPSPER;
	private final EmployeeBookBiz employeeBiz = new EmployeeBookBiz();
	private final String empRoot = "user";
	private final String basePath = "/employee";
	final BaseBiz baseBiz=new BaseBiz();
	final SuperOpLog spLog=new SuperOpLog();
	/**
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//操作员GUID
		String guid = request.getParameter("lgguid");
		Long lgguid = null;
		LfSysuser sysUser = null;
		BaseBiz baseBiz = new BaseBiz();
		String corpCode = "";
		Long userId = null;
		try{
			lgguid = Long.valueOf(guid);
			//操作员对象
			sysUser = baseBiz.getByGuId(LfSysuser.class, lgguid);
			//操作员用户ID
			userId = sysUser.getUserId();
			//企业编码
			corpCode = sysUser.getCorpCode();
		}catch (Exception e) {
			EmpExecutionContext.error(e,"员工权限管理获取当前操作员对象失败！");
		}
		PageInfo pageInfo=new PageInfo();
		
		try {
			//请求URL
			String url=request.getRequestURL().toString();
			url=url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("_"));
			//是否第一次打开
			pageSet(pageInfo, request);
			//员工权限绑定
			if(url.equals("eqa")){
				/*List<LfEnterpriseVo> enterprisesList = eb.getEnterpriseVos(1, userId,corpCode);*/
				List<LfEnterpriseVo> enterprisesList = employeeBiz.getEnterpriseVos(1, userId,corpCode);
				if(enterprisesList == null || enterprisesList.size() == 0){
					request.getRequestDispatcher(empRoot + basePath + "/epl_nopermission.jsp").forward(request,
							response);
					return ;
				}
			}
			List<LfSysuser> usersList=employeeBiz.getAllSysusers(userId);
			request.setAttribute("userlist", usersList);
			request.setAttribute("pageInfo", pageInfo);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", String.valueOf(userId));
			//员工
			if(url.equals("eqa"))
			{
				request.setAttribute("bookType", "employee");
				List<LfEmpDepConn> conn = baseBiz.getByCondition(LfEmpDepConn.class, conditionMap, null);
				if(conn != null && conn.size() > 0)
				{
					//	request.setAttribute("connDepId", conn.get(0).getDepId().toString());
				}
			}
			request.getRequestDispatcher(empRoot + basePath + "/epl_permissions.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"员工权限管理页面跳转失败！");
			try {
				request.getSession(false).setAttribute("error", e);
			} catch (Exception e1) {
				EmpExecutionContext.error(e1,"session为null");
			}
		}
		 
	}

	/**
	 * @description 显示
	 * @param request
	 * @param response
	 */
	public void toEmployeePm(HttpServletRequest request, HttpServletResponse response)
	{
		long startTime = System.currentTimeMillis();
		//企业编码
		String lfcorpcode = request.getParameter("lgcorpcode");
		//用户ID
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		Long userid = null;
		try{
			userid = Long.valueOf(lguserid);
		}catch (Exception e) {
			//进入异常
			EmpExecutionContext.error(e,"员工权限管理当前操作员lguser转化异常！");
		}
	
		List<AddrBookPermissionsVo>  bookList = new ArrayList<AddrBookPermissionsVo>();
		
		try
		{
			LfSysuser lfSysuser=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			
			String depId = "";
			String userName = "";
		//	bookVo.setDepIds(depId);
			//是否第一次打开
			boolean isFirstEnter = false;
			PageInfo pageInfo=new PageInfo();
			isFirstEnter=pageSet(pageInfo, request);
			if(!isFirstEnter){
				//操作员名称
				depId = request.getParameter("depCode");
				userName = request.getParameter("userName");
			/*	if(null != userName && 0 != userName.length())
				{
					bookVo.setName(userName);
				}*/
			}
			//查询对应的操作员绑定信息
			//之前的绑定查询语句
			//bookList = domBiz.getEmpBindPermissionsListByDepIds(userid,lfcorpcode,bookVo, pageInfo);
			String depPath = "";
			if(depId != null && !"".equals(depId)){
				BaseBiz baseBiz = new BaseBiz();
				LfEmployeeDep dep = baseBiz.getById(LfEmployeeDep.class, depId);
				depPath = dep.getDeppath();
			}
			bookList = employeeBiz.getEmpBindPermissionsList(userid,depPath,lfcorpcode,userName, pageInfo);
			
			if(lfSysuser.getPermissionType()==1)
			{
				AddrBookPermissionsVo voTemp=null;
				for(AddrBookPermissionsVo vo : bookList)
				{
					if(vo.getUserId().equals(String.valueOf(userid)))
					{
						voTemp = vo;
					}
				}
				bookList.clear();
				if(voTemp==null)
				{
					pageInfo.setPageIndex(1);
					pageInfo.setTotalRec(0);
					pageInfo.setTotalPage(1);
				}else {
					bookList.add(voTemp);
					pageInfo.setPageIndex(1);
					pageInfo.setTotalRec(1);
					pageInfo.setTotalPage(1);
				}
			}
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("bookList", bookList);

			//格式化时间
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			//操作日志信息
			String opContent = "查询："+sdf.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，机构ID:"+depId+"，总数：" + pageInfo.getTotalRec();
			EmpExecutionContext.info("通讯录权限管理", lfcorpcode, lguserid, lfSysuser.getUserName(), opContent, "GET");
			request.getRequestDispatcher(empRoot + basePath + "/epl_permissionsTable.jsp")
				.forward(request, response);

		} catch (Exception e)
		{
			//保存和打印异常信息
			EmpExecutionContext.error(e,"获取员工权限绑定列表失败！"); 
		}
	}
	
	
	/**
	 * @param request
	 * @param response
	 */
	//对绑定信息进行操作
	public void getInfo(HttpServletRequest request, HttpServletResponse response)
	{
		int temp = 0;
		PrintWriter out = null;
		String opUser = getOpUser(request);
		try
		{
			out = response.getWriter();
		} catch (IOException e)
		{
			EmpExecutionContext.error(e,"员工权限绑定获取PrintWriter对象失败！");
		}
		//操作类型
		String opType = request.getParameter("opType");
		String bookType = "employee";

		/*AddrBookBaseBiz baseBiz = new AddrBookBaseBiz();*/
	/*	AddrBookDomBiz domBiz = new AddrBookDomBiz();*/
		AddrBookPermissionsVo bookVo = new AddrBookPermissionsVo();
		String lfcorpcode = request.getParameter("lgcorpcode");
		LfSysuser lfSysuser = null;
		try {
			lfSysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
		} catch (Exception e1) {
			EmpExecutionContext.error(e1,"session为null");
		}
		//进行删除操作
		if ("delete".equals(opType))
		{
			//所关联的信息
			String connIds = request.getParameter("ids");
			Integer resultCount = 0;
			try
			{	//员工绑定页面进行删除
				if ("employee".equals(bookType))
				{
					/*EmployeeAddrBookBiz employeeBiz = new EmployeeAddrBookBiz();*/
					String[] id = connIds.split(",");
					String conns = "";
				
					for (int i = 0; i<id.length;i++)
					{
						//这里是判断删除的人是否为管理员，管理员是不能删除的，
						//现在改为在页面上限制管理员admin不能勾选，这样就减少的每次去查库判断
						conns +=id[i]+",";
						/*	
					    boolean result =  employeeBiz.filtAdmin(Long.parseLong(id[i]),lfcorpcode);
						//true不是admin
						if(result){
							conns +=id[i]+",";
						}
						else{
							temp++;
						}*/
					}
					if(conns.length()>1){
					 conns = conns.substring(0,conns.length()-1);
					 //查询解除绑定的员工机构权限
					 LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					 conditionMap.put("connId&in", conns);
					 List<LfEmpDepConn> infos = baseBiz.getByCondition(LfEmpDepConn.class, conditionMap, null);
					 resultCount = baseBiz.deleteByIds(LfEmpDepConn.class, conns);
					 if(resultCount>0)
					 {
						 String content = "删除员工通讯录权限绑定 [员工ID，机构ID]（";
						 for(LfEmpDepConn info:infos){
							 content += "["+info.getUserId()+"，"+info.getDepCodeThird()+"]，";
						 }
						 content += "）成功。";
						 opSucLog(request, opModule, content, "DELETE");
						 //成功日志
						 spLog.logSuccessString(opUser, opModule, StaticValue.DELETE, "删除员工通讯录权限绑定", lfcorpcode);
						 
					 }
					}
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"删除员工通讯录权限绑定失败！");
				//失败日志
				spLog.logFailureString(opUser, opModule, StaticValue.DELETE, "删除通讯录权限绑定", e, lfcorpcode);
			} finally
			{
				if(temp==0)
					out.print(resultCount);
				else 
					out.print(resultCount+"企业管理员不能删除权限");
			}
		} else if ("add".equals(opType))
		{
			//操作员ID
			String sysUserId = request.getParameter("sysUserId");
			//机构ID
			String depId = request.getParameter("selDepId");

			String[] sysUserIds = sysUserId.split(",");
			boolean result = false;
			try
			{	//员工页面进行新增操作
				if ("employee".equals(bookType))
				{
					 String content = "新增员工通讯录权限绑定 [员工ID，机构ID]（";
					List<LfEmpDepConn> empList = new ArrayList<LfEmpDepConn>();
					for (int i = 0; i < sysUserIds.length; i++)
					{
						LfEmpDepConn empDepConn = new LfEmpDepConn();
						empDepConn.setDepCodeThird(depId);
						//本字段已废弃,改为0因为数据库不允许此字段为空
						empDepConn.setDepId(Long.valueOf(depId));
						empDepConn.setUserId(Long.valueOf(sysUserIds[i]));
						empList.add(empDepConn);
						content += "["+empDepConn.getUserId()+"，"+empDepConn.getDepCodeThird()+"]，";
					}
					content += "）成功。";
					result = baseBiz.addList(LfEmpDepConn.class, empList) > 0;
					
					if(result)
					 {
						opSucLog(request, opModule, content, "ADD");
						//成功日志
						 spLog.logSuccessString(opUser, opModule, StaticValue.ADD, "绑定员工通讯录权限", lfcorpcode);
					 }
				}
			} catch (Exception e)
			{
				//异常打印信息
				EmpExecutionContext.error(e,"绑定员工通讯录权限失败！");
				spLog.logFailureString(opUser, opModule, StaticValue.ADD, "绑定通讯录权限", e, lfcorpcode);
			} finally
			{
				//异步输出结果
				out.print(result);
			}
		} else if ("getSysuserList".equals(opType))
		{
			//这里是获取操作员列表
			StringBuffer sb = new StringBuffer(
					"<table id='content'><thead> <tr><th style='width:10%''>选择</th>"
							+ "<th style='width:45%'>操作员ID</th><th style='width:45%'>操作员名称</th></tr></thead><tbody>");
			try
			{
				List<LfSysuser> sysuserList = new ArrayList<LfSysuser>();
				bookVo.setDepCode(request.getParameter("depCode"));
				if ("employee".equals(bookType))
				{

					/*sysuserList = domBiz.getEmpUnBindPermissionsSysuserList(
							lfSysuser.getUserId(), lfSysuser.getCorpCode(),bookVo, null);*/
					sysuserList = employeeBiz.getEmpUnBindPermissionsSysuserList(
							lfSysuser.getUserId(), lfSysuser.getCorpCode(),bookVo, null);

				} 
			/*	else if ("client".equals(bookType))
				{
					Long lguserid = Long.valueOf(request.getParameter("lguserid"));
					//当前登录操作员id
					String lgcorpcode = request.getParameter("lgcorpcode");
					//当前登录企业
					sysuserList = domBiz.getClientUnBindPermissionsSysuserList(
							lguserid,lgcorpcode, bookVo, null);
				}*/
				if (sysuserList != null)
				{
					for (LfSysuser sysuser : sysuserList)
					{
						//拼html代码
						sb.append("<tr>");
						sb
								.append("<td><input type='checkbox' name='sysUserName' id='"
										+ sysuser.getUserName()
										+ "' value="
										+ sysuser.getUserId()
										+ "  /></td><td>"
										+ sysuser.getUserName()
										+ "</td><td><xmp>"
										+ sysuser.getName() + "</xmp></td></tr>");
					}
					if(sysuserList.size()==0){
						sb.append("<tr><td colspan='3'>无记录</td></tr>");
					}
					sb.append("</tbody></table>");
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"获取操作员列表失败！");
			} finally
			{
 				out.print(sb.toString());
			}
		}
	}
	//分页信息 
	public void pageSet3(HttpServletRequest request, HttpServletResponse response) {
		PageInfo pageInfo3 = null;
		pageInfo3 = new PageInfo();
		//是否第一次打开
		boolean isFirstEnter = false;
 		if(request.getParameter("pageIndex3") == null && request.getParameter("pageSize3") == null){
 			pageInfo3.setPageSize(10);
			isFirstEnter = true;
		}else{
			String pageSize3 = null == request.getParameter("pageSize3")?"10":request.getParameter("pageSize3");
			String pageIndex3 = null == request.getParameter("pageIndex3")?"1":request.getParameter("pageIndex3");
			int size = Integer.parseInt(pageSize3);
			int index = Integer.parseInt(pageIndex3);
			pageInfo3.setPageSize(size);
			pageInfo3.setPageIndex(index);
			isFirstEnter = false;
		}
	}
	//获取操作员列表的信息
	public void getSysuserList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//企业编码
		String lfcorpcode = request.getParameter("lgcorpcode");
		//操作员的用户ID
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		//当前选择机构名称
		String selDepName = request.getParameter("selDepName");
		if(StringUtils.isNotEmpty(selDepName)){
			selDepName = new String(selDepName.getBytes("iso8859-1"), "UTF-8");
		}
		//当前选择机构id
		String selDepId = request.getParameter("selDepId");
		Long userid = null;
		try{
			userid = Long.valueOf(lguserid);
		}catch (Exception e) {
			//进入异常
			EmpExecutionContext.error(e,"获取操作员ID异常！");
		}
		
		
		try {
			//是否第一次打开
			boolean isFirstEnter = false;
			PageInfo pageInfo=new PageInfo();
			isFirstEnter=pageSet(pageInfo, request);
			
/*			AddrBookDomBiz domBiz = new AddrBookDomBiz();
*/			AddrBookPermissionsVo bookVo = new AddrBookPermissionsVo();
			List<LfSysuser> sysuserList = new ArrayList<LfSysuser>();
			String depName=null;
			String sysName=null;
			String depId="";
			String bookType =request.getParameter("bookType");
			//查询功能
			if(!isFirstEnter){
				//操作员名称
				sysName = request.getParameter("sysName");
				//机构名称
				//depName = request.getParameter("depName");
				depName = request.getParameter("depname");
				//机构id
				depId = request.getParameter("depid");
				if(null != sysName && 0 != sysName.length())
				{
					bookVo.setName(sysName);
				}
				if(null != depName && 0 != depName.length())
				{
					bookVo.setDepName(depName);
				}
				if(null != depId && 0 != depId.length())
				{
					bookVo.setDepId(Long.valueOf(depId));
				}
			}
			//如果页面传递的depid为空则查询当前操作员的对哪个机构有权限
			if(bookVo.getDepId()==null)
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
				conditionMap.put("userId", String.valueOf(userid));
				orderMap.put("depId", StaticValue.ASC);
				//查询绑定权限
				List<LfDomination> lfDominations = baseBiz.getByCondition(LfDomination.class, conditionMap, orderMap);
				if(lfDominations!=null && lfDominations.size()>0)
				{
					bookVo.setDepId(lfDominations.get(0).getDepId());
				}
			}
				//员工查询权限
				/*sysuserList = domBiz.getEmpUnBindPermissionsSysuserList(userid, lfcorpcode,bookVo, pageInfo);*/
				sysuserList = employeeBiz.getEmpUnBindPermissionsSysuserList(userid, lfcorpcode,bookVo, pageInfo);

			//返回查询结果
			request.setAttribute("sysuserList", sysuserList);
			//返回分页信息
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("depName", depName);
			request.setAttribute("sysName", sysName);
			request.setAttribute("depid", depId);
			request.setAttribute("lguserid", lguserid);
			request.setAttribute("lgcorpcode", lfcorpcode);
			LfEmployeeDep selDep = baseBiz.getById(LfEmployeeDep.class, selDepId);
			request.setAttribute("selDepName", selDep==null?"":selDep.getDepName());
			request.setAttribute("selDepId", selDepId);
			request.getRequestDispatcher(empRoot + basePath + "/epl_perbindSysuser.jsp").forward(request, response);
		} catch (Exception e) {
			//记录和打印异常信息
			EmpExecutionContext.error(e,"获取操作员列表异常！");
		}
	}
	
	
	
	public void isHandAdd(HttpServletRequest request, HttpServletResponse response)
	{
		String dType = request.getParameter("m");
		try
		{
			if ("getEmpSecondDepJson".equals(dType))
			{
				boolean r = employeeBiz.isEmployeeDepCustomAdd();
				if (r)
				{
					response.getWriter().print("true");
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"员工权限绑定管理机构查询出现异常！");
		}
	}	
	
	
	
	/**
	 * 获取操作员机构树
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createTree2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			Long depId = null;
			//机构id
			String depStr = request.getParameter("depId");
			//String userIdStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userIdStr = SysuserUtil.strLguserid(request);

			Long userId = null;
			//获取当前登录账号的userid
			try{
				userId = Long.parseLong(userIdStr);
			}catch (Exception e) {
				EmpExecutionContext.error(e,"员工权限管理操作员lguserid转化异常！");
			}
			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			//获取机构树字符串
			String departmentTree = this.getDepartmentJosnData2(depId,userId);
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取操作员机构树出现异常！");
		}
	}
	
	/**
	 *  操作员机构树
	 * @param depId
	 * @param userId
	 * @return
	 */
	private String getDepartmentJosnData2(Long depId,Long userId){
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		
		try {
			sysuser = baseBiz.getById(LfSysuser.class, userId);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"机构权限管理获取当前操作员异常！");
			tree = new StringBuffer("[]");
		}
		if(sysuser!=null&&sysuser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			try {
				lfDeps = null;
				if(depId == null){
					lfDeps = new ArrayList<LfDep>();
					/*备份 pengj
					LfDep lfDep = depBiz.getAllDeps(userId).get(0);//这里需要优化
					*/
					//新增 pengj
					LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userId, sysuser==null?"":sysuser.getCorpCode()).get(0);//这里需要优化
					lfDeps.add(lfDep);
				}else{
					/*原始方法 备份 pengj
					lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					*/
					//新增方法 增加企业编码 pengj
					lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId, sysuser==null?"":sysuser.getCorpCode());
				}
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
		
			} catch (Exception e) {
				EmpExecutionContext.error(e,"机构权限管理获取机构树出现异常！");
				tree = new StringBuffer("[]");
			}
		}
		return tree.toString();
	}
	
	/**
	 * 获取操作员名称
	 * @description    
	 * @param request
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-4-2 上午09:17:49
	 */
	public String getOpUser(HttpServletRequest request){
		String opUser = "";
		LfSysuser sysuser = null;
		try {
			sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"session为null");
		}
		if(sysuser != null){
			opUser = sysuser.getUserName();
		}
		return opUser;
	}

	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		LfSysuser lfSysuser = null;
		Object obj = null;
		try {
			obj = request.getSession(false).getAttribute("loginSysuser");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"session为null");
		}
		if(obj == null) return;
		lfSysuser = (LfSysuser)obj;
		EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
	}
	
	/**
	 * @description  检查机构是否被删除了
	 * @param request
	 * @param response
	 * @author pengj
	 * @datetime 2015-9-10 下午19:25:00
	 * 
	 */
	public void checkdepDel(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = null;
		String result="";
		//当前选择机构id
		String depId = request.getParameter("selDepId");
		try {
			out = response.getWriter();
			
			LfEmployeeDep lfEmployeeDep=baseBiz.getById(LfEmployeeDep.class, depId);
			if(lfEmployeeDep==null){
				result="-1";	
			}
		} catch (Exception e) {
			//异常打印信息
			EmpExecutionContext.error(e,"员工通讯录，异步检查机构是否存在失败，传入参数dep_id："+depId);
		}
		if(out!=null){
			out.print(result);
		}
	}

}
