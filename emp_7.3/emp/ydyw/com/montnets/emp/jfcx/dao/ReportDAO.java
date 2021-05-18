package com.montnets.emp.jfcx.dao;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;

public class ReportDAO extends SuperDAO
{
	public List<LfDep> findDomDepBySysuserID(String sysuserID,
			LinkedHashMap<String, String> orderbyMap) throws Exception {
		String sql = "select dep.* from " + TableLfDep.TABLE_NAME
				+ " dep inner join " + TableLfDomination.TABLE_NAME
				+ " domination on dep." + TableLfDep.DEP_ID + "=domination."
				+ TableLfDomination.DEP_ID + " where domination."
				+ TableLfDomination.USER_ID + "=" + sysuserID;
		StringBuffer orderSqlSb = new StringBuffer();
		Map<String, String> columns = TableLfDep.getORM();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			orderSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> e = iter.next();
				if (e.getValue() != null && !"".equals(e.getValue())) {
					orderSqlSb.append("dep.").append(columns.get(e.getKey()))
							.append(" ").append(e.getValue()).append(",");
				}
			}
		}
		String orderSql = orderSqlSb.toString();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			orderSql = orderSql.substring(0, orderSql.length() - 1);
		}
		sql += orderSql;
		return findEntityListBySQL(LfDep.class, sql, StaticValue.EMP_POOLNAME);
	}
	
	/**
	 * 通过部门id获取操作员的管辖范围内的操作员
	 * @param depID
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> findDomSysuserByDepID(String depID,
			LinkedHashMap<String, String> orderbyMap) throws Exception {
		String sql = "select sysuser.* from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDomination.TABLE_NAME
				+ " domination on sysuser." + TableLfSysuser.USER_ID
				+ "=domination." + TableLfDomination.USER_ID
				+ " where domination." + TableLfDomination.DEP_ID + "=" + depID;
		StringBuffer orderSqlSb = new StringBuffer();
		Map<String, String> columns = TableLfSysuser.getORM();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			orderSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> e = iter.next();
				if (e.getValue() != null && !"".equals(e.getValue().toString())) {
					orderSqlSb.append("sysuser.").append(
							columns.get(e.getKey())).append(" ").append(
							e.getValue()).append(",");
				}
			}
		}
		String orderSql = orderSqlSb.toString();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			orderSql = orderSql.substring(0, orderSql.length() - 1);
		}
		sql += orderSql;
		return findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
	}
}
