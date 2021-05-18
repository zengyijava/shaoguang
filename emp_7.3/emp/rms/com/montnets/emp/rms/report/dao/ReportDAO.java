package com.montnets.emp.rms.report.dao;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

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
	
	/**
	 * 根据操作员id获取管辖机构的顶级机构对象集合
	 * @param sysuserID 操作员id
	 * @param corpCode 操作员所属企业
	 * @return 返回操作员管辖机构的顶级机构对象集合
	 */
	public List<LfDep> findTopDepByUserId(String sysuserID, String corpCode)
	{
		try
		{
			//拼接sql
			StringBuffer sql = new StringBuffer();
			sql.append("select dep.* from LF_DEP dep inner join LF_DOMINATION domination on dep.DEP_ID=domination.DEP_ID")
				.append(" where domination.USER_ID=").append(sysuserID)
				.append(" and dep.DEP_STATE=1")
				.append(" and dep.CORP_CODE='").append(corpCode).append("'")
				.append(" order by dep.DEP_ID asc");
			
			//只拿第一条顶级机构
			PageInfo pageInfo = new PageInfo();
			pageInfo.setPageSize(1);
			//返回结果
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LfDep.class, sql.toString(), null, pageInfo, StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询统计DAO，根据操作员id获取管辖机构的顶级机构对象集合，异常。sysuserID="+sysuserID+",corpCode="+corpCode);
			return null;
		}
	}
	
	
	
	/**
	 * 获取公用 汇总原始数据
	 * @description    
	 * @param asname
	 * @return       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-4-13 上午09:40:43
	 */
	public static String getPublicCountSql(String asname)
	{
		if(asname!=null&&!"".equals(asname)){
			return "SUM(" + asname + ".ICOUNT) ICOUNT,SUM(" + asname + ".RSUCC) RSUCC,SUM(" + asname + ".RFAIL1) RFAIL1,SUM(" + asname + ".RFAIL2) RFAIL2,SUM(" + asname + ".RNRET) RNRET";
		}else{
			return "SUM(ICOUNT) ICOUNT,SUM(RSUCC) RSUCC,SUM(RFAIL1) RFAIL1,SUM(RFAIL2) RFAIL2,SUM(RNRET) RNRET";
		}
	}
	
	
}
