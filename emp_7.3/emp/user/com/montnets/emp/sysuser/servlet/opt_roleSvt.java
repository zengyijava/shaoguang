package com.montnets.emp.sysuser.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.LoginBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.LfPrivilegeAndMenuVo;
import com.montnets.emp.entity.sysuser.LfImpower;
import com.montnets.emp.entity.sysuser.LfRoles;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.sysuser.biz.RoleBiz;
import com.montnets.emp.sysuser.biz.SysuserPriBiz;
import com.montnets.emp.sysuser.vo.LfRolesVo;
import com.montnets.emp.sysuser.vo.LfSysuser2Vo;
import com.montnets.emp.util.SuperOpLog;

/**
 * 
 * 
 * @project emp
 * @author Sam Wang 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-08-01
 * @description
 */
@SuppressWarnings("serial")
public class opt_roleSvt extends BaseServlet {

	final RoleBiz roleBiz = new RoleBiz();
	private final SysuserPriBiz sysuserPriBiz = new SysuserPriBiz();
	//操作模块
	final String opModule = "角色管理";
	//操作用户
	final String opSper = StaticValue.OPSPER;
	final BaseBiz baseBiz=new BaseBiz();
	final SuperOpLog spLog=new SuperOpLog();
	private static final String PATH = "/user/operator";
	public void find(HttpServletRequest request, HttpServletResponse response) {
		//Long lguserid = Long.parseLong(request.getParameter("lguserid"));
		//漏洞修复 session里获取操作员信息
		Long lguserid = SysuserUtil.longLguserid(request);

		String lgcorpcode = request.getParameter("lgcorpcode");
		//LfSysuser sysuser = (LfSysuser) getSession().getAttribute("loginSysuser");
		try {
			List<LfRoles> roleList = roleBiz.getAllRoles(lguserid);
			List<LfRolesVo> roleVoList = new ArrayList<LfRolesVo>();
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			conditionMap.put("userName", "admin");
			conditionMap.put("corpCode", lgcorpcode);
			List<LfSysuser> sysuList = new ArrayList<LfSysuser>();
			sysuList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
			if(sysuList.size() == 0){
				conditionMap.clear();
				conditionMap.put("userName", "sysadmin");
				conditionMap.put("corpCode", lgcorpcode);
				sysuList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
			}
			//LfSysuser admin = (LfSysuser)sysuList.get(0);
			if(roleList != null && roleList.size()>0){
				//if(admin.getGuId() == lfSysuser.getGuId()){				//说明登录上来的是该企业的管理�?
					LfRolesVo rolesVo = null;
					LfRoles roles = null;
					for(int i=0;i<roleList.size();i++){
						roles = roleList.get(i);
						rolesVo = new LfRolesVo();
						rolesVo.setComments(roles.getComments());
						rolesVo.setGuId(roles.getGuId());
						rolesVo.setRoleId(roles.getRoleId());
						rolesVo.setRoleName(roles.getRoleName());
						rolesVo.setUserName("");
					/*	if((admin.getGuId() == lfSysuser.getGuId()) && rolesVo.getGuId() != null){
								LfSysuser user = baseBiz.getByGuId(LfSysuser.class, rolesVo.getGuId());
								//rolesVo.setUserName("创建�?: "+user.getUserName());
								rolesVo.setUserName("");
						}else{
								rolesVo.setUserName("");
						}*/	
						roleVoList.add(rolesVo);
					}
				
			}
			
			
			//List<LfPrivilege> prList = roleBiz.getPrivByPrivCodeAsc(lguserid.toString());
			LoginBiz loginBiz=new LoginBiz();
			List<LfPrivilegeAndMenuVo> prList=loginBiz.getAllMenuByUserId(lgcorpcode, lguserid.toString(),"1");
			
			//循环角色菜单列表，与配置文件里配置的模块匹配，没有配置的模块不显示  
			List<LfPrivilegeAndMenuVo> prMenuList=new ArrayList<LfPrivilegeAndMenuVo>();
			if(prList!=null && prList.size()>0)
			{
				
				String menuCode="";
				for(int i=0;i<prList.size();i++)
				{
					LfPrivilegeAndMenuVo priVo=prList.get(i);
					if(StaticValue.getInniMenuMap().get(priVo.getMenuSite())!=null || menuCode.equals(priVo.getMenuCode()))
					{
						menuCode=priVo.getMenuCode();
						prMenuList.add(priVo);
					}
					//号码是否可见和运营商查看不是左边栏的模块，做特殊处理
					else if("1600-2000-0".equals(priVo.getPrivCode()) || "1600-1900-0".equals(priVo.getPrivCode()))
					{
						prMenuList.add(priVo);
					}
				}
			}
			request.setAttribute("privilegeList", prMenuList);
			request.setAttribute("roleVoList", roleVoList);
			request.setAttribute("roleList", roleList);
			request.getRequestDispatcher(PATH + "/opt_role.jsp").forward(
					request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"角色管理跳转失败！");
		}
	}
	/**
	 * 新建角色
	 * @Title: add
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @return void
	 */
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		PrintWriter out = response.getWriter();
		Long lgguid = Long.parseLong(request.getParameter("lgguid"));
		LfSysuser sysuser = null;
		try {
			sysuser = baseBiz.getByGuId(LfSysuser.class, lgguid);
			String roleName = request.getParameter("roleName");
			boolean result = false;

			oppType = StaticValue.ADD;
			opContent = "新建角色（角色名：" + roleName + "）";
			if (!roleBiz.roleNameExists(sysuser.getCorpCode(),roleName)) {
				String comments = request.getParameter("comments");
				String privileges = request.getParameter("Privilege");
				String[] pri = privileges.split(",");
				Long[] pris = new Long[pri.length];
				for (int k = 0; k < pri.length; k++) {
					pris[k] = Long.valueOf(pri[k]);
				}
				LfRoles role = new LfRoles();
				role.setComments(comments);
				role.setRoleName(roleName);
				role.setCorpCode(sysuser.getCorpCode());
				role.setGuId(sysuser.getGuId());		//创建该角色�?GUID
				result = roleBiz.addRole(role, pris, sysuser);
				if (result) {
					//添加操作成功日志
					opSucLog(request, opModule, opContent+"成功。", "ADD");
					spLog.logSuccessString(opUser, opModule, oppType,opContent,sysuser.getCorpCode());
				} else {
					//添加操作失败日志
					EmpExecutionContext.error("企业："+sysuser.getCorpCode()+",操作员："+opUser+","+opContent+"失败");
					spLog.logFailureString(opUser, opModule, oppType, opContent + opSper, null,sysuser.getCorpCode());
				}
				response.getWriter().print(result);
			} else {
				opContent = "已存在相同的角色名称";
				//添加操作失败日志
				EmpExecutionContext.error("企业："+sysuser.getCorpCode()+",操作员："+opUser+","+opContent);
				spLog.logFailureString(opUser, opModule, oppType, opContent
						+ opSper, null,sysuser.getCorpCode());
				response.getWriter().print("mid");
			}
		} catch (Exception e) {
			out.print(false);
			//添加操作失败日志
			EmpExecutionContext.error("企业："+request.getParameter("lgcorpcode")+",操作员："+opUser+",新建角色异常");
			spLog.logFailureString(opUser, opModule, oppType, opContent
					+ opSper, e,request.getParameter("lgcorpcode"));
			EmpExecutionContext.error(e,"新建角色失败！");
		}

	}

	//通过角色id获取对应的权限
	public void getPrivilegeByRole(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String pri = "";
		String roleId = request.getParameter("roleId");
		try {
			if(roleId != null && !"".equals(roleId))
			{
				List<LfImpower> list = roleBiz.getPrivilegeByRoleId(Integer
						.parseInt(roleId));
				for (int i = 0; i < list.size(); i++) {
					pri += list.get(i).getPrivilegeId();
					if (i < list.size() - 1) {
						pri += ",";
					}
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取角色权限失败！");
		} finally {
			out.print(pri);
		}
	}

	//修改角色
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		boolean result = false;
		PrintWriter out = response.getWriter();
		//企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		try {
			//角色�?
			String roleName = request.getParameter("roleName");
			oppType = StaticValue.UPDATE;
			opContent = "修改（角色名:"+roleName+")角色权限";
			Long roleId = Long.valueOf(request.getParameter("roleId"));
			if (roleBiz.validate(lgcorpcode, roleName, roleId) - 0 != 3) {
				String privileges = request.getParameter("Privilege");
				String comments = request.getParameter("comments");
				
				String[] pri = privileges.split(",");
				Long[] pris = new Long[pri.length];
				for (int k = 0; k < pri.length; k++) {
					pris[k] = Long.valueOf(pri[k]);
				}
				LfRoles role = new LfRoles();
				role.setRoleName(roleName);
				role.setComments(comments);
				role.setRoleId(roleId);
			//	role.setGuId(lfSysuser.getGuId());	
				result = baseBiz.updateObj(role);
				boolean upPriResult = roleBiz.updatePrivRole(roleId, pris);
				if (upPriResult && result) {
					//添加操作成功日志
					opSucLog(request, opModule, opContent+"成功。", "UPDATE");
					spLog.logSuccessString(opUser, opModule, oppType,
							opContent, lgcorpcode);
				} else {
					//添加操作失败日志
					EmpExecutionContext.error("企业："+lgcorpcode+",操作员："+opUser+","+opContent+"失败");
					spLog.logFailureString(opUser, opModule, oppType, opContent
							+ opSper, null, lgcorpcode);
				}
			
				out.println(result);
			} else {
				opContent = "已经存在相同的角色名";
				//添加操作失败日志
				EmpExecutionContext.error("企业："+lgcorpcode+",操作员："+opUser+","+opContent);
				spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, null,lgcorpcode);
				out.print("mid");
			}
			
		} catch (Exception e) {
			out.print(false);
			//添加操作失败日志
			EmpExecutionContext.error("企业："+lgcorpcode+",操作员："+opUser+",修改角色异常");
			spLog.logFailureString(opUser, opModule, oppType, opContent
					+ opSper, e,lgcorpcode);
			EmpExecutionContext.error(e,"修改角色失败！");
		}
	}

	//删除角色
	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		boolean result = false;
		//企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		try {
			//角色�?
			String roleName = request.getParameter("roleName");
			oppType = StaticValue.DELETE;
			opContent = "删除角色（角色名:" + roleName + "）";
			Long roleId = Long.valueOf(request.getParameter("roleId"));
			result = roleBiz.deleteRoleByRoleId(roleId);
			roleBiz.deletePrivilegeByRoleId(roleId);
			if (result) {
				response.getWriter().print("true");
				//添加操作成功日志
				opSucLog(request, opModule, opContent+"成功。", "DELETE");
				spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
			}
		} catch (Exception e) {
			response.getWriter().print("false");
			//添加操作失败日志
			EmpExecutionContext.error("企业："+lgcorpcode+",操作员："+opUser+",删除角色异常");
			spLog.logFailureString(opUser, opModule, oppType, opContent
					+ opSper, e,lgcorpcode);
			EmpExecutionContext.error(e,"删除角色失败！");
		}

	}
	
	//判断该角色能否删除
	public void canDelete(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		String userName = request.getParameter("lgusername");
		try {
			Long roleId = Long.valueOf(request.getParameter("roleId"));
			List<LfSysuser2Vo> sysuserList = sysuserPriBiz.getUsersByRoleId(roleId);
			//String userName = lfSysuser.getUserName();
			//1、如果该角色由admin创建，如果已经赋给了其他操作员，则不能删�?
			//2、如果该角色由sysadmin创建,如果已经赋给了其他操作员，则不能删除
			//3、如果该角色由其他操作员创建，如果已赋给其他操作�?admin除外，其他操作员在创建角色时就将该角色赋给了admin)，则不能删除
			if(sysuserList != null &&(("admin".equals(userName) && sysuserList.size()>1) ||(("sysadmin".equals(userName) && sysuserList.size()>1))||
					(!"admin".equals(userName)&&!"sysadmin".equals(userName)&& sysuserList.size()>2))){
				response.getWriter().print("false");
			}else{
				response.getWriter().print("true");
			}
		} catch (Exception e) {
			response.getWriter().print("false");
			EmpExecutionContext.error(e,"判断角色是否能删除出现异常！");
		}
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
		try {
			Object sysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(sysuserObj != null){
				LfSysuser sysuser = (LfSysuser) sysuserObj;
				opUser = sysuser.getUserName();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取操作员名称异常，session为空！");
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
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "session获取当前操作员失败,session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}
	}	
}
