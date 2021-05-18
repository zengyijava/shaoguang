package com.montnets.emp.sysuser.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.entity.sysuser.LfImpower;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfRoles;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.sysuser.LfUser2role;

/**
 * 
 * @author Administrator
 *
 */
public class RoleBiz extends SuperBiz
{
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */ 
	//获取所有角色
	public List<LfRoles> getAllRoles(Long userId)throws Exception{
		List<LfRoles> roles = new ArrayList<LfRoles>();
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		try{
			String roleIds = getRoleIdByUserId(userId);
			//roleid为1、2、3的角色为系统管理员默认角色，界面不可见
			conditionMap.put("roleId&not in", "1,2,3");
			if (null == roleIds || roleIds.length() == 0) {
				roleIds = "-1";
			}
			if (userId != 1) {
				conditionMap.put("roleId&in", roleIds);
			}
			roles = empDao.findListBySymbolsCondition(LfRoles.class, conditionMap, null);
			
		}catch(Exception e){
			EmpExecutionContext.error(e, "获取所有角色信息异常！");
			throw e;
		}
		return roles;
	}
	
	/**
	 * 
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	//通过角色id获取权限列表
	public List<LfImpower> getPrivilegeByRoleId(Integer roleId)throws Exception{
		List<LfImpower> lfImpowers = new ArrayList<LfImpower>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		try{
			conditionMap.put("roleId", roleId.toString());
			lfImpowers = empDao.findListByCondition(LfImpower.class, conditionMap,null);
		}catch(Exception e){
			EmpExecutionContext.error(e, "获取角色权限列表发生异常！");
			throw e;
		}
		return lfImpowers;
	}
	
	/**
	 * 
	 * @param roleName
	 * @return
	 * @throws Exception
	 */
	//判断角色名是否已经存在
	public boolean roleNameExists(String corpCode,String roleName)throws Exception{
		boolean exists = false;
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		List<LfRoles> lfRolesList = new ArrayList<LfRoles>();
		try{
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("roleName", roleName);
			lfRolesList = empDao.findListByCondition(LfRoles.class, conditionMap, null);
			if(lfRolesList.size()>0){
				exists = true;
			} 
		}catch(Exception e){
			EmpExecutionContext.error(e, "角色名存在判断发生异常！");
			throw e;
		}
		return exists;
	}
	
	/**
	 * 
	 * @param lfRoles
	 * @param privilegeIds
	 * @param sysuser
	 * @return
	 * @throws Exception
	 */
	public boolean addRole(LfRoles lfRoles,Long[] privilegeIds,LfSysuser sysuser)throws Exception{
		Long maxRoleId = null;
		boolean addOk = false;
		Connection conn = empTransDao.getConnection();
		try{
			//开启事务
			empTransDao.beginTransaction(conn);
			maxRoleId=empTransDao.saveObjectReturnID(conn,lfRoles);
			
//			for(int index=0;index<privilegeIds.length;index++){
//				addLfImpower(conn,maxRoleId,privilegeIds[index]);
//			}
			
			List<LfImpower> impowers=new ArrayList<LfImpower>();
			for(int index=0;index<privilegeIds.length;index++){
				LfImpower lfImpower = new LfImpower();
				lfImpower.setPrivilegeId(privilegeIds[index]);
				lfImpower.setRoleId(maxRoleId);
				impowers.add(lfImpower);
			}
			empTransDao.save(conn, impowers, LfImpower.class);
			
			LfUser2role user2Role = null;
			if("admin".equals(sysuser.getUserName()) || "sysadmin".equals(sysuser.getUserName())){
				//如果当前操作用户是admin或者sysadmin则该角色只赋给自己
			    user2Role = new LfUser2role();
				user2Role.setRoleId(maxRoleId);
				user2Role.setUserId(sysuser.getUserId());
				empTransDao.save(conn,user2Role);
			}else{
				//如果当前操作用户不是admin也不是sysadmin，则该角色不仅要赋给自己
				//还需要赋给admin
				user2Role = new LfUser2role();
				user2Role.setRoleId(maxRoleId);
				user2Role.setUserId(sysuser.getUserId());
				empTransDao.save(conn,user2Role);
			
				LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
				conditionMap.put("userName", "admin");
				conditionMap.put("corpCode", lfRoles.getCorpCode());
				List<LfSysuser> sysuList = new ArrayList<LfSysuser>();
				sysuList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
				if(sysuList.size() == 0){
					//该操作员是sysadmin所在机构，如果该机构没有admin用户，则将角色赋给sysadmin
					conditionMap.clear();
					conditionMap.put("userName", "sysadmin");
					conditionMap.put("corpCode", lfRoles.getCorpCode());
					sysuList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
				}
				LfSysuser admin = sysuList.get(0);
				
				LfUser2role adminuser2Role = new LfUser2role();
				adminuser2Role.setRoleId(maxRoleId);
				adminuser2Role.setUserId(admin.getUserId());
				empTransDao.save(conn,adminuser2Role);
			}
			//提交事务
			empTransDao.commitTransaction(conn);
			if (maxRoleId != null) {
				addOk = true;
			}
		}catch(Exception e){
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			addOk = false;
			EmpExecutionContext.error(e, "添加角色发生异常！");
			throw e;
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return addOk;
	}
 
	/**
	 * 
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteRoleByRoleId(Long roleId)throws Exception{
		
		int deleteOk = 0;
		Connection conn = empTransDao.getConnection();
		
		try{
			//开启事务
			empTransDao.beginTransaction(conn);
			
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();	 
			conditionMap.put("roleId", roleId.toString());
			empTransDao.delete(conn, LfUser2role.class,conditionMap);
			empTransDao.delete(conn, LfImpower.class,conditionMap);
			deleteOk = empTransDao.delete(conn, LfRoles.class, conditionMap);
			//提交事务
			empTransDao.commitTransaction(conn);
		}catch(Exception e){
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"删除角色失败！");
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		if(deleteOk>=1){
 			return true;
		}else{
			return false;
		}
	}
 
	/**
	 * 
	 * @param afRoleName
	 * @param beRoleId
	 * @return
	 * @throws Exception
	 */
	public Integer validate(String corpCode,String afRoleName,Long beRoleId)throws Exception{
		Integer validate;
		Long afRoleId = getRoleIdByRoleName(corpCode,afRoleName);
		try{
			if(afRoleId==null){
				validate = new Integer(1);
			}else if(afRoleId-beRoleId==0){
				validate = new Integer(2);
			}else{
				validate = new Integer(3);
			}
			 
		}catch(Exception e){
			EmpExecutionContext.error(e, "验证角色信息发生异常！");
			throw e;
		}
		return validate;
	}
	

	/**
	 * 
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	//删除角色
	public int deletePrivilegeByRoleId(Long roleId)throws Exception{
		int deleteNum = 0;
		LinkedHashMap<String,String> econditionMap = new LinkedHashMap<String,String>();
 		List<String> roleIds = new ArrayList<String>();
		if(roleIds!=null)
 		try{
			econditionMap.put("roleId",roleId.toString());
			deleteNum = empDao.delete(LfImpower.class, econditionMap);
		}catch(Exception e){
			deleteNum=0;
			EmpExecutionContext.error(e, "删除角色发生异常！");
 			throw e;
		}
		return deleteNum;
	}
	
	/**
	 * 
	 * @param conn
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	//删除角色
	private int deletePrivilegeByRoleId(Connection conn, Long roleId)throws Exception{
		
		int deleteNum = 0;
		LinkedHashMap<String,String> econditionMap = new LinkedHashMap<String,String>();
 		List<String> roleIds = new ArrayList<String>();
		if(roleIds!=null)
 		try{
			econditionMap.put("roleId",roleId.toString());
			deleteNum = empTransDao.delete(conn, LfImpower.class, econditionMap);
		
		}catch(Exception e){
			deleteNum=0;
 			throw e;
		}
		return deleteNum;
	}
	
	/**
	 * 
	 * @param conn
	 * @param roleId
	 * @param privilegeId
	 * @return
	 * @throws Exception
	 */
	public boolean addLfImpower(Connection conn,Long roleId,Long privilegeId)throws Exception{

		boolean addOk = false;
		try{
			LfImpower lfImpower = new LfImpower();
			lfImpower.setPrivilegeId(privilegeId);
			lfImpower.setRoleId(roleId);
			addOk = empTransDao.save(conn,lfImpower);
		}catch(Exception e){
			addOk=false;
/*			spLog.logFailureString(opModule, opType, opContent, e);
*/			EmpExecutionContext.error(e, "删除角色发生异常！");
			throw e;
		}/*finally{
			if(addOk){
				spLog.logSuccessString(opModule, opType, opContent);
			}
		}*/
		return addOk;
		
	}
 
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfPrivilege> getPrivByPrivCodeAsc(String userId)throws Exception{
		List<LfPrivilege> privileges = new ArrayList<LfPrivilege>();
		try{
			
			privileges =  new SpecialDAO().findPrivilegesBySysuserId(userId, "BUTTON");
		}catch(Exception e){
			EmpExecutionContext.error(e,"获取权限列表发生异常！");
			throw e;
		}
		return privileges;
	}
	
	/**
	 * 
	 * @param userId
	 * @param menuMap
	 * @return
	 * @throws Exception
	 */
	//通过权限code获取标题菜单
	public List<LfPrivilege> getMenuByPrivCodeAsc(String userId)throws Exception{
		List<LfPrivilege> privileges = new ArrayList<LfPrivilege>();
		try{
			
			List<LfPrivilege> lfPrivilegeList =  new SpecialDAO().findPrivilegesBySysuserId(userId, "MENU");
			String modName2="";
			for (int i = 0; i < lfPrivilegeList.size(); i++) {
				LfPrivilege pri = lfPrivilegeList.get(i);
				if (!modName2.equals(pri.getMenuName())
						&& pri.getMenuSite() != null) {
					modName2 = pri.getMenuName();
					privileges.add(pri);
				}
			}
		}catch(Exception e){
			EmpExecutionContext.error(e, "获取权限标题菜单发生异常！");
			throw e;
		}
		return privileges;
	}
	
	/**
	 * 
	 * @param roleId
	 * @param privilegeIds
	 * @return
	 * @throws Exception
	 */
	public boolean updatePrivRole(Long roleId,Long[] privilegeIds)throws Exception{
	 
		boolean updateOk = false;
		Connection conn = empTransDao.getConnection();
		try{
			//开启事务
			empTransDao.beginTransaction(conn);
			
			deletePrivilegeByRoleId(conn, roleId);
			for(int index=0;index<privilegeIds.length;index++){
				addLfImpower(conn,roleId,privilegeIds[index]);
			}
			//提交事务
			empTransDao.commitTransaction(conn);
			updateOk = true;
		}catch(Exception e){
			updateOk=false;
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "更新角色权限发生异常！");
 			throw e;
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return updateOk;
	}
	
	/**
	 * 
	 * @param roleName
	 * @return
	 * @throws Exception
	 */
	//通过角色名获取角色id
	private Long getRoleIdByRoleName(String corpCode,String roleName)throws Exception{
		Long roleId = null;
		try{
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("roleName", roleName);
			List<LfRoles> roles =  empDao.findListByCondition(LfRoles.class, conditionMap, null);
			LfRoles lr = null;
			if(roles!=null&&roles.size()!=0){
			lr = roles.get(0);
			roleId = lr.getRoleId();
			}
			
		}catch(Exception e){
			EmpExecutionContext.error(e,"通过角色名获取角色id出现异常！");
			throw e;
		}
		return roleId;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	//通过操作员userid获取角色id
	public String getRoleIdByUserId(Long userId)throws Exception{
		String roleIds = "";
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		List<LfUser2role> user2roleList = new ArrayList<LfUser2role>();
		try{
			conditionMap.put("userId",userId.toString());
			user2roleList = empDao.findListBySymbolsCondition(LfUser2role.class, conditionMap, null);
			if(user2roleList!=null&&user2roleList.size()!=0){
				for(int index=0;index<user2roleList.size();index++){
				LfUser2role user2role = user2roleList.get(index);
				roleIds+=user2role.getRoleId()+",";
				}
				if(roleIds!=null&&!"".equals(roleIds)){
					roleIds=roleIds.substring(0,roleIds.lastIndexOf(","));
				}
			}
		}catch(Exception e){
			EmpExecutionContext.error(e, "通过操作员获取角色id出现异常！");
			throw e;
		}
		return roleIds;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	//通过userid获取角色id（不包含系统管理员默认角色）
	public String getRoleIdByUserIdNoAdmin(Long userId)throws Exception{
		String roleIds = "";
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		List<LfUser2role> user2roleList = new ArrayList<LfUser2role>();
		try{
			conditionMap.put("userId",userId.toString());
			//roleid排除系统管理员默认角色id
			conditionMap.put("roleId&not in","1,2,3");
			user2roleList = empDao.findListBySymbolsCondition(LfUser2role.class, conditionMap, null);
			if(user2roleList!=null&&user2roleList.size()!=0){
				for(int index=0;index<user2roleList.size();index++){
					LfUser2role user2role = user2roleList.get(index);
					roleIds+=user2role.getRoleId()+",";
				}
				if(roleIds!=null&&!"".equals(roleIds)){
					roleIds=roleIds.substring(0,roleIds.lastIndexOf(","));
				}
			}
		}catch(Exception e){
			EmpExecutionContext.error(e, "获取角色id发生异常！");
			throw e;
		}
		return roleIds;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	
	public Map<String,String> getTitleMap() throws Exception
	{
		List<LfPrivilege> prList=new SpecialDAO().findPrivilegesBySysuserId("1", "MENU");
		Map<String, String> titleMap=new HashMap<String, String>();
		String modName="";
		int size=prList.size();
		for (int i = 0; i < size; i++) {
			LfPrivilege pri=prList.get(i);
			
			if (!modName.equals(pri.getModName())) {
				modName=pri.getModName();
				titleMap.put(pri.getMenuCode().substring(0,pri.getMenuCode().indexOf("-")), modName);
			}
			titleMap.put(pri.getMenuCode(), pri.getMenuName());
			String menuSite = pri.getMenuSite();
			if (menuSite.indexOf("/") == menuSite.lastIndexOf("/")) {
				titleMap.put(menuSite.substring(menuSite.indexOf("_")+1,
						menuSite.lastIndexOf(".")), pri.getMenuCode());
			} else {
				titleMap.put(menuSite.substring(menuSite.lastIndexOf("/")+1,
					menuSite.lastIndexOf(".")), pri.getMenuCode());
			}
		}
		return titleMap;
	}
 */
}
