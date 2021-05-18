package com.montnets.emp.inbox.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.inbox.vo.LfMotaskVo1;
import com.montnets.emp.table.engine.TableLfMotask;
import com.montnets.emp.table.query.TableMoTask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.util.PageInfo;

public class LfMotaskVoDao extends SuperDAO {
	
	/**
	 * 收件箱查询方法
	 */
	public List<LfMotaskVo1> findLfmotaskRecive(String sql,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception {		
		
		conditionMap.remove("userId");
		conditionMap.remove("userGuid");
		//根据模块编码过滤（主要过滤企业快快等一些模块的上行信息）
		sql=sql+"and mo."+TableLfMotask.MENUCODE+" is null";
		//拼接查询条件
		if(conditionMap.get("deliverTime&>=")!=null)
		{
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				//oracle数据库
				sql=sql+" and mo."+TableLfMotask.DELIVERTIME+">=to_date('"+conditionMap.get("deliverTime&>=")+"','yyyy-MM-dd HH24:mi:ss')";
			} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				//sqlserver数据库
				sql=sql+" and mo."+TableLfMotask.DELIVERTIME+">='"+conditionMap.get("deliverTime&>=")+"'";
			}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
				//db2数据库
				sql=sql+" and mo."+TableLfMotask.DELIVERTIME+">='"+conditionMap.get("deliverTime&>=")+"'";
			}
			else if(StaticValue.DBTYPE==StaticValue.MYSQL_DBTYPE){
				//mysql数据库
				sql=sql+" and mo."+TableLfMotask.DELIVERTIME+">='"+conditionMap.get("deliverTime&>=")+"'";
			}
			conditionMap.remove("deliverTime&>=");
		}
		
		if(conditionMap.get("deliverTime&<=")!=null)
		{
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				//oracle数据库
				sql=sql+" and mo."+TableLfMotask.DELIVERTIME+"<=to_date('"+conditionMap.get("deliverTime&<=")+"','yyyy-MM-dd HH24:mi:ss')";
			} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				//sqlserver数据库
				sql=sql+" and mo."+TableLfMotask.DELIVERTIME+"<='"+conditionMap.get("deliverTime&<=")+"'";
			}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
				//db2数据库
				sql=sql+" and mo."+TableLfMotask.DELIVERTIME+"<='"+conditionMap.get("deliverTime&<=")+"'";
			}
			else if(StaticValue.DBTYPE==StaticValue.MYSQL_DBTYPE){
				//mysql数据库
				sql=sql+" and mo."+TableLfMotask.DELIVERTIME+"<='"+conditionMap.get("deliverTime&<=")+"'";
			}
			conditionMap.remove("deliverTime&<=");
		}
		
		if(conditionMap.get("employeeName")!=null && !"".equals(conditionMap.get("employeeName")))
		{
			
		//	sql=sql+" and employee."+TableLfEmployee.NAME+" like '%"+conditionMap.get("employeeName")+"%' ";
			
			StringBuffer sb = new StringBuffer();

			sb.append(" and (mo.PHONE in (select lf.MOBILE from ")
			.append(" LF_EMPLOYEE lf where lf.NAME ")
			.append(" LIKE '%").append(conditionMap.get("employeeName").trim()).append("%' ) or mo.PHONE in ")
			.append("(select client.MOBILE from LF_CLIENT client where client.NAME")
			.append(" LIKE '%").append(conditionMap.get("employeeName").trim()).append("%' )) ");

			sql=sql + sb.toString();
			
			conditionMap.remove("employeeName");
		}
		
		if(conditionMap.get("msgContent")!=null)
		{
			
			sql=sql+" and mo."+TableMoTask.MSG_CONTENT+" like '%"+conditionMap.get("msgContent")+"%' ";
			
			conditionMap.remove("msgContent");
		}
		
		if(conditionMap.get("spUser")!=null&&!"".equals(conditionMap.get("spUser")))
		{
			//如果是DB2或者oracle 则做兼容小写处理
			if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE==StaticValue.ORACLE_DBTYPE
					||StaticValue.DBTYPE==StaticValue.DB2_DBTYPE)){
				String useridstr=conditionMap.get("spUser");
				sql=sql+" and mo."+TableLfMotask.SP_USER+ " in ('"+useridstr.toUpperCase().trim()+"','"
				+useridstr.toLowerCase().trim()+"') ";
			}else{
				sql=sql+" and mo."+TableLfMotask.SP_USER+" = '"+conditionMap.get("spUser")+"' ";
			}
			conditionMap.remove("spUser");
		}
		
		
		Iterator<Map.Entry<String, String>> iter = null;
		//获取实体类与数据库字段的MAP映射
		Map<String, String> columns = getORMMap(LfMotask.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			//获取实体类属性
			Field[] fields = LfMotask.class.getDeclaredFields();
			//属性类型
			String fieldType = null;
			while (iter.hasNext()) {
				e = iter.next();
				for (int index = 0; index < fields.length; index++)
				{
					if (fields[index].getName().equals(e.getKey()))
					{
						fieldType = fields[index].getGenericType()
								.toString();
						break;
					}
				}
				if (!"".equals(e.getValue())) {
					String eKey = e.getKey();
					columnName = eKey.indexOf("&") < 0 ? columns.get(eKey)
							: columns.get(eKey
									.subSequence(0, eKey.indexOf("&")));
					if (eKey.contains("&like")) {
						sql = sql + " and mo." + columnName + " like '%"
								+ e.getValue() + "%' ";
					} else if (eKey.contains("&>")) {
						sql = sql + " and mo." + columnName + ">" + e.getValue()
								+ " ";
					} else {
						boolean isDateType = fieldType
						.equals(StaticValue.TIMESTAMP)
						|| fieldType.equals(StaticValue.DATE_SQL)
						|| fieldType.equals(StaticValue.DATE_UTIL);
						if(fieldType.equals("class java.lang.String") || isDateType){
							sql = sql + " and mo." + columnName + "='" + e.getValue()
							+ "' ";
						}else{
							sql = sql + " and mo." + columnName + "=" + e.getValue();
						}
					}
		
				}
			}
		}
		
		//sql += " group by mo.mo_id ";
		//记录数sql拼接
		String countSql = new StringBuffer(
		"select count(*) totalcount from (").append(sql)
		.append(") moo").toString();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			sql += " order by ";
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext()) {
				e = iter.next();
				sql = sql + "mo." +columns.get(e.getKey()) + " " + e.getValue() + ",";
			}
		
			sql = sql.substring(0, sql.lastIndexOf(","));
		}
		
		
		
		if (pageInfo == null) {
			//不分页，执行查询，返回结果
			return findVoListBySQL(LfMotaskVo1.class, sql, StaticValue.EMP_POOLNAME);
		} else {
			//分页，支持查询，返回结果
			return new DataAccessDriver().getGenericDAO().findPageVoListBySQL(LfMotaskVo1.class,sql,countSql,pageInfo,StaticValue.EMP_POOLNAME);
		}
	}

	/**
	 * 根据操作员userid获取此操作员管辖的所有机构
	 * @param curUserId
	 * @return
	 * @throws Exception
	 */
	public String getDepIds(Long curUserId) throws Exception
	{
		String depIds = "";
		//通用查询方法排序字段集合
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		//排序字段
		orderbyMap.put("depId","asc");
		//部门集合
		List<LfDep> depList = new ArrayList<LfDep>();
		
		//通过当前操作员的机构获取所有下级机构
		depList = findDomDepBySysuserID(curUserId.toString(), orderbyMap);
		
		//判断下级机构集合是否为空
		if(null !=depList && 0!=depList.size())
		{
			for (LfDep dep :depList)
			{
				depIds+=dep.getDepId().toString()+",";
			}
			
			if(depIds.contains(","))
			{
				depIds=depIds.substring(0,depIds.lastIndexOf(","));
			}
		}
		
		return depIds;
	}
	
	/**
	 * 通过当前操作员的机构获取所有下级机构
	 */
	public List<LfDep> findDomDepBySysuserID(String sysuserID,
			LinkedHashMap<String, String> orderbyMap) throws Exception {
		//拼接sql
		String sql = "select dep.* from " + TableLfDep.TABLE_NAME
				+ " dep inner join " + TableLfDomination.TABLE_NAME
				+ " domination on dep." + TableLfDep.DEP_ID + "=domination."
				+ TableLfDomination.DEP_ID + " where domination."
				+ TableLfDomination.USER_ID + "=" + sysuserID + " and dep."+TableLfDep.DEP_STATE + "=1" ;
	//排序条件拼接
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
		//返回结果
		return findEntityListBySQL(LfDep.class, sql, StaticValue.EMP_POOLNAME);
	}
	
	/**
	 * 个人收件箱专用
	 * @param sysuserID
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> findDomDepBySysuserID_V1(String sysuserID,String corpcode,
			LinkedHashMap<String, String> orderbyMap) throws Exception {
		//拼接sql
		String sql = "select dep.* from " + TableLfDep.TABLE_NAME
				+ " dep inner join " + TableLfDomination.TABLE_NAME
				+ " domination on dep." + TableLfDep.DEP_ID + "=domination."
				+ TableLfDomination.DEP_ID + " where domination."
				+ TableLfDomination.USER_ID + "=" + sysuserID + " and dep."+TableLfDep.DEP_STATE + "=1 AND dep.CORP_CODE='"+corpcode+"' " ;
	//排序条件拼接
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
		//返回结果
		return findEntityListBySQL(LfDep.class, sql, StaticValue.EMP_POOLNAME);
	}
	
	
	/**
	 * 根据机构id获得操作员list
	 * @param depids
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> findUsersByDepIds(String depids,String corpcode) throws Exception {
		String conditionsql="";
		//拼接sql
		String sql = "select * from LF_SYSUSER WHERE CORP_CODE='"+corpcode+"'";
		conditionsql=new ReciveBoxDao().getSqlStr(depids, "DEP_ID");
		sql=sql+" AND ("+conditionsql+") ";
		//返回结果
		return findEntityListBySQL(LfSysuser.class, sql, StaticValue.EMP_POOLNAME);
	}
	
	
}
