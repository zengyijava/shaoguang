package com.montnets.emp.ottbase.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.wxsysuser.LfSysPrivilege;
import com.montnets.emp.entity.wxsysuser.LfSysUser;
import com.montnets.emp.table.wxsysuser.TableLfSysImpower;
import com.montnets.emp.table.wxsysuser.TableLfSysPrivilege;
import com.montnets.emp.table.wxsysuser.TableLfSysUser;
import com.montnets.emp.table.wxsysuser.TableLfSysUserRole;
import org.apache.commons.beanutils.DynaBean;

import java.util.List;

public class OttSpecialDAO extends SuperDAO
{

	
	
	/**
	 * 查询菜单模块
	 */
	public List<LfSysPrivilege> findPrivilegesBySysuserId(String sysuserID,
			String functionType) throws Exception {
		
		
		String privilegeSql = "";
		
		if (functionType.equals("MENU")) {
			privilegeSql = "select  lfprivilege." + TableLfSysPrivilege.MENU_CODE
					+ "  ,lfprivilege." + TableLfSysPrivilege.MENU_SITE
					+ "  ,lfprivilege." + TableLfSysPrivilege.RESOURCE_ID
					+ "  ,lfprivilege." + TableLfSysPrivilege.MENU_NAME + "  ,"
					+ TableLfSysPrivilege.MOD_NAME + "  from "
					+ TableLfSysPrivilege.TABLE_NAME + " lfprivilege" + " where "
					+ TableLfSysPrivilege.PRIVILEGE_ID + " in ( select "
					+ TableLfSysImpower.PRIVILEGE_ID + " from "
					+ TableLfSysImpower.TABLE_NAME + " where "
					+ TableLfSysImpower.ROLE_ID + " in (select "
					+ TableLfSysUserRole.ROLE_ID + " from "
					+ TableLfSysUserRole.TABLE_NAME + " where "
					+ TableLfSysUserRole.USER_ID + " = " + sysuserID + ")) and "
					+ TableLfSysPrivilege.OPERATE_ID + "=1 " ;
					privilegeSql += "group by ";
					privilegeSql +=  TableLfSysPrivilege.MENU_CODE + ",";
					privilegeSql +=  TableLfSysPrivilege.MENU_SITE + ",";
					privilegeSql +=  TableLfSysPrivilege.RESOURCE_ID + ",";
					privilegeSql +=  TableLfSysPrivilege.MENU_NAME + ",";
					privilegeSql +=  TableLfSysPrivilege.MOD_NAME + " order by ";
					privilegeSql +=  TableLfSysPrivilege.MENU_CODE + " asc";
		} else if ("SITE".equals(functionType)) {
			
			StringBuffer tempSql =  new StringBuffer("select * from ").append(
					TableLfSysPrivilege.TABLE_NAME).append(" where ");
			tempSql.append(
					TableLfSysPrivilege.OPERATE_ID).append("=1");
			privilegeSql = tempSql.toString();
		} else {
			privilegeSql = "select *  from " + TableLfSysPrivilege.TABLE_NAME
					+ " where " + TableLfSysPrivilege.PRIVILEGE_ID
					+ " in ( select " + TableLfSysImpower.PRIVILEGE_ID + " from "
					+ TableLfSysImpower.TABLE_NAME + " where "
					+ TableLfSysImpower.ROLE_ID + " in (select "
					+ TableLfSysUserRole.ROLE_ID + " from "
					+ TableLfSysUserRole.TABLE_NAME + " where "
					+ TableLfSysUserRole.USER_ID + " = " + sysuserID + "))  ";
			privilegeSql += " order by " + TableLfSysPrivilege.PRIV_CODE + " asc";
		}
		List<LfSysPrivilege> lfPrivilegesList = findPartEntityListBySQL(
				LfSysPrivilege.class, privilegeSql, StaticValue.EMP_POOLNAME);
		return lfPrivilegesList;
	}
	
	/**
	 * 根据用户名和密码查询操作员
	 * @param userName
	 * @param passWord
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<LfSysUser> getLfSysUserByUP(String userName, String passWord,String corpCode)
			throws Exception {
		StringBuffer sql = new StringBuffer("select * from ").append(
				TableLfSysUser.TABLE_NAME).append(" lfSysUser ").append(StaticValue.getWITHNOLOCK());
		sql.append(" where upper(lfSysUser.").append(
				TableLfSysUser.USER_NAME).append(") = '").append(
				userName).append("' ");
		//查询条件：password
		if (passWord != null && !"".equals(passWord)) {
			sql.append(" and lfSysUser.").append(TableLfSysUser.PAZZWORD)
					.append(" = '").append(passWord).append("'");
		}
		//查询条件：企业编码
		if (corpCode != null && !"".equals(corpCode)) {
			sql.append(" and lfSysUser.").append(TableLfSysUser.CORP_CODE)
					.append(" = '").append(corpCode).append("'");
		}
		List<LfSysUser> returnList = findEntityListBySQL(LfSysUser.class, sql
				.toString(), StaticValue.EMP_POOLNAME);
		return returnList;
	}
	

	/**
	 * 获取当前位置
	 * @return 当前位置集合 
	 */
	public List<DynaBean> getPosition()
	{
		String sql = "select pr.menucode,pr.menuName,pr.modname,tm.TITLE,pr.RESOURCE_ID from OT_SYS_PRIVILEGE pr "+StaticValue.getWITHNOLOCK()+
			" left join OT_SYS_MENU tm on tm.PRI_MENU= pr.RESOURCE_ID where pr.OPERATE_ID = 1";
		
		return getListDynaBeanBySql(sql);
	}
	
	 /**
     * 根据公众账号查询该公众账号的每日关注人数
     * @description    
     * @param aid
     * @return                   
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-20 下午12:35:43
     */
    public List<DynaBean> getSubscribeCount(String aid)
    {
        String sql = "select A_ID,CONVERT(varchar(100), subscribe_time, 23) as subtime,count(subscribe_time) subcount from lf_wei_userInfo  group by A_ID, CONVERT(varchar(100), subscribe_time, 23) HAVING A_ID = " + aid + " order by a_id, CONVERT(varchar(100), subscribe_time, 23)";

        return getListDynaBeanBySql(sql);
    }
	
    /**
     * 获取客服数量
     * @description    
     * @return
     * @throws Exception       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-21 上午11:14:45
     */
	public int getUserCount() throws Exception
	{
	    String sql = "select count(user_id) totalcount from lf_wei_user2acc";
	    return findCountBySQL(sql);
	}
	
    /**
     * 更新群组数量
     * @description    
     * @param aId
     * @return       			 
     * @author fanglu <fanglu@montnets.com>
     * @throws Exception 
     * @datetime 2014-2-25 下午05:50:54
     */
    public boolean updateGroupCount(String aid) throws Exception
    {
    	  String  sql ="update lf_wei_group set lf_wei_group.count = " +
	  		"(select "+(StaticValue.DBTYPE==StaticValue.DB2_DBTYPE?"varchar(rtrim(char(count(g_id))))":"count(g_id)")+" from lf_wei_userInfo " +
	  		"where g_id = lf_wei_group.g_id) where lf_wei_group.a_id ="+aid;
          return executeBySQL(sql, StaticValue.EMP_POOLNAME);
    }
    
    public int getGroupId0(String aid,String corpCode)
    {
    	String sql = "select G_ID as totalcount from lf_wei_group where WG_ID=0 and corp_code = '"+corpCode+"' and A_ID = "+aid;
    	return findCountBySQL(sql);
    }
}