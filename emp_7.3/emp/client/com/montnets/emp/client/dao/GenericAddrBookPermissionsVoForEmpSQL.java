package com.montnets.emp.client.dao;

import com.montnets.emp.client.vo.AddrBookPermissionsVo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;


/**
 * @project 
 * @author 通讯录查询语句
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 
 * @description 
 */

public class GenericAddrBookPermissionsVoForEmpSQL
{
	/**
	 * 界面上需要获得的值
	 * @return
	 */
	public static String getFieldSql()
	{	
	
		String sql ="";
	 	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
	 		 sql = new StringBuffer("select lfempdepconn.").append(TableLfEmpDepConn.DEP_ID)
			.append(",lfempdepconn.").append(TableLfEmpDepConn.CONN_ID)
			//.append(",lfempdepconn.").append(TableLfEmpDepConn.DEP_ID)
			.append(",lfempdepconn.").append(TableLfEmpDepConn.USER_ID)
			.append(",lfempdep.").append(TableLfEmployeeDep.DEP_NAME).append(",lfsysuser.").append(TableLfSysuser.USER_NAME)
			.append(",lfsysuser.").append(TableLfSysuser.NAME).toString();
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			 sql = new StringBuffer("select lfempdepconn.").append(TableLfEmpDepConn.DEP_ID)
				.append(",lfempdepconn.").append(TableLfEmpDepConn.CONN_ID)
				//.append(",lfempdepconn.").append(TableLfEmpDepConn.DEP_ID)
				.append(",lfempdepconn.").append(TableLfEmpDepConn.USER_ID)
				.append(",lfempdep.").append(TableLfEmployeeDep.DEP_NAME).append(",lfsysuser.").append(TableLfSysuser.USER_NAME)
				.append(",lfsysuser.").append(TableLfSysuser.NAME).append(",ROW_NUMBER() Over(Order By lfsysuser.DEP_CODE) As rn").toString();
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = new StringBuffer("select lfempdepconn.").append(TableLfEmpDepConn.DEP_ID)
				.append(",lfempdepconn.").append(TableLfEmpDepConn.CONN_ID)
				//.append(",lfempdepconn.").append(TableLfEmpDepConn.DEP_ID)
				.append(",lfempdepconn.").append(TableLfEmpDepConn.USER_ID)
				.append(",lfempdep.").append(TableLfEmployeeDep.DEP_NAME).append(",lfsysuser.").append(TableLfSysuser.USER_NAME)
				.append(",lfsysuser.").append(TableLfSysuser.NAME).toString();
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			sql = new StringBuffer("select lfempdepconn.").append(TableLfEmpDepConn.DEP_ID)
			.append(",lfempdepconn.").append(TableLfEmpDepConn.CONN_ID)
			//.append(",lfempdepconn.").append(TableLfEmpDepConn.DEP_ID)
			.append(",lfempdepconn.").append(TableLfEmpDepConn.USER_ID)
			.append(",lfempdep.").append(TableLfEmployeeDep.DEP_NAME).append(",lfsysuser.").append(TableLfSysuser.USER_NAME)
			.append(",lfsysuser.").append(TableLfSysuser.NAME).toString();
		}
	 	
		return sql;
	}
	
	/**
	 * 查询所涉及的表
	 * @return
	 */
	public static String getTableSql(String corpCode)
	{
		String sql = new StringBuffer(" from ").append(TableLfEmployeeDep.TABLE_NAME).append(" lfempdep ")
		.append(" right join ").append(TableLfEmpDepConn.TABLE_NAME).append(" lfempdepconn on")
		.append(" lfempdepconn.").append(TableLfEmpDepConn.DEP_ID).append("=lfempdep.").append(TableLfEmployeeDep.DEP_ID).append(" and ")
		.append("lfempdep.").append(TableLfEmployeeDep.CORP_CODE).append("='").append(corpCode+"'")
		.append(" left join ").append(TableLfSysuser.TABLE_NAME).append(" lfsysuser on lfsysuser.").append(TableLfSysuser.USER_ID)
		.append("=lfempdepconn.").append(TableLfEmpDepConn.USER_ID).toString();
			
		String moreWShere = "";
		if(corpCode!=null)
			moreWShere = new StringBuffer().append(" and lfsysuser.").append(TableLfSysuser.CORP_CODE).append("='").append(corpCode+"'").toString();
		else
			moreWShere  = new StringBuffer().toString();
		sql += moreWShere;
		return sql;
	}
	
	/**
	 * 查询中，用户选择的条件
	 * @param addrBookPermissionsVo
	 * @return
	 */
	public static String getConditionSql(AddrBookPermissionsVo addrBookPermissionsVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		
	
		if(addrBookPermissionsVo.getName() != null && !"".equals(addrBookPermissionsVo.getName())){
			conditionSql.append(" and lfsysuser.").append(
					TableLfSysuser.NAME).append(" like'%").append(addrBookPermissionsVo.getName()).append("%'");
		}
		String sql = conditionSql.toString();
		return sql;
	}
	
	/**
	 *  查询中的排序
	 * @return
	 */
	public static String getOrderBySql()
	{
		return null;
	}
	
}
