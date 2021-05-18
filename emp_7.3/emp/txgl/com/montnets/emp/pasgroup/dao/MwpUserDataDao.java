package com.montnets.emp.pasgroup.dao;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.servmodule.txgl.table.TableUserdata;
import com.montnets.emp.util.PageInfo;

public class MwpUserDataDao extends SuperDAO{
	public List<Userdata> findSpUserByCorp(String corp,
			LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
			throws Exception {
		//拼接sql
		String sql = "SELECT * FROM USERDATA WHERE USERID IN (SELECT SPUSER FROM LF_SP_DEP_BIND WHERE CORP_CODE='"+corp
		+"' AND IS_VALIDATE=1) AND SPTYPE=2 AND  ACCOUNTTYPE=1 AND "+TableUserdata.UID+">100001 AND USERTYPE=0 ";
		
		//账户名称
		if(conditionMap!=null&&conditionMap.get("staffName")!=null&&conditionMap.get("staffName").length()!=0){
			sql=sql+" AND STAFFNAME LIKE '%"+conditionMap.get("staffName")+"%'";
		}
		//SP账号
		if(conditionMap!=null&&conditionMap.get("userId")!=null&&conditionMap.get("userId").length()!=0){
			sql=sql+" AND USERID LIKE '%"+conditionMap.get("userId")+"%'";
		}
		//账户状态
		if(conditionMap!=null&&conditionMap.get("status")!=null&&conditionMap.get("status").length()!=0){
			sql=sql+" AND STATUS = "+conditionMap.get("status")+" ";
		}
		
		String countSqlStr=sql;
		String orderbystr=" ORDER BY ORDERTIME DESC,USERID DESC ";
		sql=sql+orderbystr;
		if (pageInfo == null) {
			return findEntityListBySQL(Userdata.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(countSqlStr)
					.append(") A").toString();
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(Userdata.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	
	/**
	 * 查询SP账号
	 */
	public List<Userdata> findSpUser(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
			throws Exception {
		//拼接sql
		String sql = "SELECT * FROM USERDATA WHERE SPTYPE=2 AND  ACCOUNTTYPE=1 AND "+TableUserdata.UID+">100001 AND USERTYPE=0 ";
		
		//账户名称
		if(conditionMap!=null&&conditionMap.get("staffName")!=null&&conditionMap.get("staffName").length()!=0){
			sql=sql+" AND STAFFNAME LIKE '%"+conditionMap.get("staffName")+"%'";
		}
		//SP账号
		if(conditionMap!=null&&conditionMap.get("userId")!=null&&conditionMap.get("userId").length()!=0){
			sql=sql+" AND USERID LIKE '%"+conditionMap.get("userId")+"%'";
		}
		//账户状态
		if(conditionMap!=null&&conditionMap.get("status")!=null&&conditionMap.get("status").length()!=0){
			sql=sql+" AND STATUS = "+conditionMap.get("status")+" ";
		}
		
		String countSqlStr=sql;
		String orderbystr=" ORDER BY ORDERTIME DESC,USERID DESC ";
		sql=sql+orderbystr;
		if (pageInfo == null) {
			return findEntityListBySQL(Userdata.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(countSqlStr)
					.append(") A").toString();
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(Userdata.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	
	
}
