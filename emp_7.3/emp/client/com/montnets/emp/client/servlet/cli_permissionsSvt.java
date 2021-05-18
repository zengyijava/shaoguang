package com.montnets.emp.client.servlet;


import com.montnets.emp.client.biz.AddrBookBaseBiz;
import com.montnets.emp.client.biz.AddrBookBiz;
import com.montnets.emp.client.biz.AddrBookDomBiz;
import com.montnets.emp.client.biz.ClientAddrBookBiz;
import com.montnets.emp.client.biz.EnterpriseBiz;
import com.montnets.emp.client.vo.AddrBookPermissionsVo;
import com.montnets.emp.client.vo.LfEnterpriseVo;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.client.LfCliDepConn;
import com.montnets.emp.entity.client.LfClientDep;
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
 * 
 * 客户权限
 * @project emp
 * @author liuj
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 
 * @description 
 */
//通讯录权限绑定
@SuppressWarnings("serial")
public class cli_permissionsSvt extends BaseServlet {
	
	//操作日志模块
	private final String opModule=StaticValue.ADDBR_MANAGER;
	//操作类型 
	//String opType = "";
	//操作内容
    //String opContent ="";
    //操作员
	private final  String opSper = StaticValue.OPSPER;
	private final EnterpriseBiz eb = new EnterpriseBiz();
	private final  String empRoot = "client";
	//String opUser="";
	private final SuperOpLog spLog=new SuperOpLog();
	
	/**
	 * 跳转到查询页面
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
			EmpExecutionContext.error(e,"客户权限获取当前操作员对象失败！");
			//用户ID
		}
		
		try {
			//请求URL
			String url=request.getRequestURL().toString();
			url=url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("_"));
			//是否第一次打开
			boolean isFirstEnter = false;
			PageInfo pageInfo=new PageInfo();
			isFirstEnter=pageSet(pageInfo, request);
			if(!isFirstEnter){
	
			}
			
			//客户权限绑定
			List<LfEnterpriseVo> enterprisesList = eb.getEnterpriseVos(0, userId,corpCode);
			
			if(enterprisesList == null || enterprisesList.size() == 0){
				request.getRequestDispatcher(this.empRoot+"/climan/cli_noPermission.jsp").forward(request,
						response);
				return ;
			}
			//获取操作员的列表
			AddrBookBiz addrBookBiz=new AddrBookBiz();
			List<LfSysuser> usersList=addrBookBiz.getAllSysusers(userId);
			request.setAttribute("userlist", usersList);
			request.setAttribute("pageInfo", pageInfo);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", String.valueOf(userId));

			//客户
			request.setAttribute("bookType", "client");
			request.setAttribute("lguserid", userId);
			request.getRequestDispatcher(this.empRoot+"/climan/cli_clientPermissions.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"客户权限页面跳转失败！");
		}
	}

	/**
	 * 客户通讯录权限管理查询
	 * @param request
	 * @param response
	 */
	public void toClientPm(HttpServletRequest request, HttpServletResponse response)
	{
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		try
		{
		AddrBookDomBiz domBiz = new AddrBookDomBiz();
		//搜索条件封装对象
		List<AddrBookPermissionsVo>  bookList = new ArrayList<AddrBookPermissionsVo>();
		
		PageInfo pageInfo=new PageInfo();
		pageSet(pageInfo, request);
		//机构id
		String depId = request.getParameter("depCode");
		//姓名
		String userName = request.getParameter("userName");
		String depPath="";
		if(depId!=null && !"".equals(depId))
		{
			BaseBiz baseBiz = new BaseBiz();
			LfClientDep dep = baseBiz.getById(LfClientDep.class, depId);
			depPath = dep.getDeppath();
		}
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


			//当前登录操作员id
		String lgcorpcode = request.getParameter("lgcorpcode");
		//当前登录企业
		AddrBookBaseBiz baseBiz = new AddrBookBaseBiz();
		LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
		//查询权限绑定记录
		bookList = domBiz.getClientBindPermissionsList(Long.parseLong(lguserid), depPath, lgcorpcode, userName, pageInfo);
		if(curSysuser.getPermissionType()==1)
		{
			AddrBookPermissionsVo voTemp=null;
			for(AddrBookPermissionsVo vo : bookList)
			{
				if(vo.getUserId().equals(lguserid))
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
		//增加查询日志
		if(pageInfo!=null){
			long end_time=System.currentTimeMillis();
			String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"毫秒，数量："+pageInfo.getTotalRec();
			opSucLog(request, "客户通讯录权限管理查询", opContent, "GET");
		}
		request.getRequestDispatcher(this.empRoot + "/climan/cli_permissionsTable.jsp").forward(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取权限列表信息失败！"); 
		}
	}
	
	/**
	 * 对绑定信息进行操作
	 * @param request
	 * @param response
	 */
	public void getInfo(HttpServletRequest request, HttpServletResponse response)
	{
		String opUser="";
		LfSysuser sysuser=null;
		try{
			 sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			 opUser = sysuser.getUserName();
		}catch (Exception e) {
			EmpExecutionContext.error(e,"从Session取出用户信息出现异常！");
		}
		int temp = 0;
		PrintWriter out = null;
		try
		{
			out = response.getWriter();
		} catch (IOException e)
		{
			EmpExecutionContext.error(e,"获取PrintWriter对象出现异常！");
		}
		//操作类型
		String opType = request.getParameter("opType");

		AddrBookBaseBiz baseBiz = new AddrBookBaseBiz();
		//企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		//进行删除操作
		if ("delete".equals(opType))
		{
			//所关联的信息
			String connIds = request.getParameter("ids");
			Integer resultCount = 0;
			try
			{	
				//客户页面进行删除操作	
				ClientAddrBookBiz clientBiz = new ClientAddrBookBiz();
				String[] id = connIds.split(",");
				String conns = "";
			
				for (int i = 0; i<id.length;i++)
				{
					conns +=id[i]+",";
				}
				if(conns.length()>1){
					 conns = conns.substring(0,conns.length()-1);
					 resultCount = baseBiz.deleteByIds(LfCliDepConn.class, conns);
					 if(resultCount>0)
					 {
						 //EmpExecutionContext.info("模块名称：通讯录权限管理，企业："+lgcorpcode+"，操作员："+opUser+"，解除客户通讯录权限绑定（id值:"+connIds+"）成功");
						 EmpExecutionContext.info("通讯录权限管理", sysuser == null ? null : sysuser.getCorpCode(), sysuser == null ? null : sysuser.getUserId()+"", sysuser == null ? null : sysuser.getUserName(), "解除客户通讯录权限绑定成功。[关联id值]（"+connIds+"）", "OTHER");
						 //成功日志
						 spLog.logSuccessString(opUser, opModule, StaticValue.DELETE, "删除客户通讯录权限绑定",lgcorpcode);
					 }else{
						 //EmpExecutionContext.error("模块名称：通讯录权限管理，企业："+lgcorpcode+"，操作员："+opUser+"，删除客户通讯录权限绑定（id值:"+connIds+"）失败");
						 EmpExecutionContext.info("通讯录权限管理", sysuser == null ? null : sysuser.getCorpCode(), sysuser == null ? null : sysuser.getUserId()+"", sysuser == null ? null : sysuser.getUserName(), "解除客户通讯录权限绑定失败。[关联id值]（"+connIds+"）", "OTHER");
					 }
				}
				
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"删除通讯录权限绑定出现异常！");
				//失败日志
				spLog.logFailureString(opUser, opModule, StaticValue.DELETE, "删除通讯录权限绑定", e, lgcorpcode);
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
			{
				//当前登录企业
				List<LfCliDepConn> cliList = new ArrayList<LfCliDepConn>();
				for (int i = 0; i < sysUserIds.length; i++)
				{
					LfCliDepConn cliDepConn = new LfCliDepConn();
					//本字段已废弃
					cliDepConn.setDepCodeThird(depId);
					cliDepConn.setDepId(Long.valueOf(depId));
					cliDepConn.setUserId(Long.valueOf(sysUserIds[i]));
					cliList.add(cliDepConn);
				}
				result = new ClientAddrBookBiz().addPermissionList(cliList);
				if(result)
				{
					 EmpExecutionContext.info("通讯录权限管理", sysuser == null ? null : sysuser.getCorpCode(), sysuser == null ? null : sysuser.getUserId()+"", sysuser == null ? null : sysuser.getUserName(), "增加客户通讯录权限绑定成功。[绑定操作员ID|部门机构ID]（"+sysUserId+"|"+depId+"）", "ADD");
					//成功日志
					//EmpExecutionContext.info("模块名称：通讯录权限管理，企业："+lgcorpcode+"，操作员："+opUser+"，绑定客户通讯录权限（机构ID:"+depId+"）成功");
					 spLog.logSuccessString(opUser, opModule, StaticValue.ADD, "绑定客户通讯录权限", lgcorpcode);
				}else{
					EmpExecutionContext.info("通讯录权限管理", sysuser == null ? null : sysuser.getCorpCode(), sysuser == null ? null : sysuser.getUserId()+"", sysuser == null ? null : sysuser.getUserName(), "增加客户通讯录权限绑定失败。[绑定操作员ID|部门机构ID]（"+sysUserId+"|"+depId+"）", "ADD");
					//EmpExecutionContext.error("模块名称：通讯录权限管理，企业："+lgcorpcode+"，操作员："+opUser+"，绑定客户通讯录权限（机构ID:"+depId+"）失败");
				}
				
			} catch (Exception e)
			{
				//异常打印信息
				EmpExecutionContext.error(e,"绑定通讯录权限出现异常！");
				spLog.logFailureString(opUser, opModule, StaticValue.ADD, "绑定通讯录权限", e, lgcorpcode);
			} finally
			{
				//异步输出结果
				out.print(result);
			}
		}
	}
	
	//检查部门是否被删除了
	public void checkdepDel(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = null;
		String ret="";
		try {
			out = response.getWriter();
			//当前选择机构id
			String selDepId = request.getParameter("selDepId");
			LfClientDep selDep = new BaseBiz().getById(LfClientDep.class, selDepId);
			if(selDep==null){
				ret="-1";	
			}
		} catch (Exception e) {
			//异常打印信息
			EmpExecutionContext.error(e,"通过ID查询部门异常！");
		}
		if(out != null){
			out.print(ret);
		}
	}
	
	
	
	
	//获取操作员列表的信息
	/**
	 * @param request
	 * @param response
	 */
	public void getSysuserList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
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
		
		try {
			//是否第一次打开
			boolean isFirstEnter = false;
			PageInfo pageInfo=new PageInfo();
			isFirstEnter=pageSet(pageInfo, request);
			
			AddrBookDomBiz domBiz = new AddrBookDomBiz();
			AddrBookPermissionsVo bookVo = new AddrBookPermissionsVo();
			List<LfSysuser> sysuserList = new ArrayList<LfSysuser>();
			String depName=null;
			String sysName=null;
			String depId="";
			
			//查询功能
			if(!isFirstEnter){
				//操作员名称
				sysName = request.getParameter("sysName");
				//机构名称
				depName = request.getParameter("depName");
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
				conditionMap.put("userId", lguserid);
				orderMap.put("depId", StaticValue.ASC);
				//查询绑定权限
				List<LfDomination> lfDominations = new BaseBiz().getByCondition(LfDomination.class, conditionMap, orderMap);
				if(lfDominations!=null && lfDominations.size()>0)
				{
					bookVo.setDepId(lfDominations.get(0).getDepId());
				}
			}
			
			//客户
			sysuserList = domBiz.getClientUnBindPermissionsSysuserList(Long.valueOf(lguserid),lgcorpcode, bookVo, pageInfo);
			
			//返回查询结果
			request.setAttribute("sysuserList", sysuserList);
			//返回分页信息
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("depName", depName);
			request.setAttribute("sysName", sysName);
			request.setAttribute("depid", depId);
			request.setAttribute("lguserid", lguserid);
			request.setAttribute("lgcorpcode", lgcorpcode);
			LfClientDep selDep = new BaseBiz().getById(LfClientDep.class, selDepId);
			request.setAttribute("selDepName", selDep.getDepName());
			request.setAttribute("selDepId", selDepId);
			
			request.getRequestDispatcher(this.empRoot + "/climan/cli_unbindCliSysusers.jsp").forward(request, response);
		} catch (Exception e) {
			//记录和打印异常信息
			EmpExecutionContext.error(e,"获取操作员列表的信息出现异常！");
		}
	}
	
	/**
	 * 获取机构树(权限绑定)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createTree2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			/*java.util.Enumeration<String> a = request.getParameterNames();
			
			while (a.hasMoreElements()) {
				String string = (String) a.nextElement();
			}*/
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
				EmpExecutionContext.error(e,"当前操作员lguserid转化异常！");
			}
			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			LfSysuser user=getLoginUser(request);
			String corpCode=user.getCorpCode();
			//获取机构树字符串
			String departmentTree = getDepartmentJosnData2(depId,userId,corpCode);
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取机构树出现异常！");
		}
	}
	
	/**
	 * 树
	 * @return
	 */
	protected String getDepartmentJosnData2(Long depId,Long userId,String corpCode){
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			sysuser = new BaseBiz().getById(LfSysuser.class, userId);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取当前操作员对象失败！");
			tree = new StringBuffer("[]");
		}
		if(sysuser != null && sysuser.getPermissionType()==1)
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
					LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userId,corpCode).get(0);//这里需要优化
					lfDeps.add(lfDep);
					//lfDeps.addAll(new DepBiz().getDepsByDepSuperId(lfDep.getDepId()));
				}else{
					lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,corpCode);
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
				EmpExecutionContext.error(e,"获取操作员机构数出现异常！");
				tree = new StringBuffer("[]");
			}
		}
		return tree.toString();
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
		try{
			LfSysuser lfSysuser = null;
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"记录操作日志出现异常！");
		}
	}
}
