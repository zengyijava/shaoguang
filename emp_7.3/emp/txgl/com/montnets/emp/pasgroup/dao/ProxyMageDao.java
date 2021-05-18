package com.montnets.emp.pasgroup.dao;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.pasgroup.entity.Userdata;
import com.montnets.emp.pasgroup.table.TableUserdata;
import com.montnets.emp.util.PageInfo;

public class ProxyMageDao extends SuperDAO{

	/**
	 * 查询SP账号
	 */
	public List<Userdata> findSpUser(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
			throws Exception {
		//拼接sql
		String sql = "select * from " + TableUserdata.TABLE_NAME ;

		// 拼接sql
		sql +=" where USERPRIVILEGE = 11  and USERID <> 'WBS00A' ";
		String staffname = conditionMap.get("STAFFNAME&like");
		if(staffname != null && !"".equals(staffname)){
			sql += " and STAFFNAME like '%"+staffname+"%'";
		}

		String userid = conditionMap.get("USERID&like");
		if(userid != null && !"".equals(userid)){
			sql += " and USERID like '%"+userid+"%'";
		}
		
		String status = conditionMap.get("STATUS");
		if(status != null && !"".equals(status)){
			sql += " and STATUS ="+status;
		}
		String countSqlStr=sql;
		//排序条件拼接
		sql +=  " ORDER BY ORDERTIME DESC ";

		if (pageInfo == null) {
			//不分页，执行查询返回结果
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
	
	
}
