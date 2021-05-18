package com.montnets.emp.common.dao;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.system.LfdepPassUser;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDeppwdReceiver;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

public class DepSpecialDAO extends SuperDAO {
	 
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
	 * 通过操作员id和企业编码获取其所以管辖机构的机构对象
	 * @description    
	 * @param sysuserID  操作员id
	 * @param corpCode   企业编码
	 * @param orderbyMap 查询条件
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-11 上午11:18:50
	 */
	public List<LfDep> findDomDepByUserIdAndCorpCode(String sysuserID, String corpCode, 
			LinkedHashMap<String, String> orderbyMap) throws Exception {
		//拼接sql
		String sql = "select dep.* from " + TableLfDep.TABLE_NAME
				+ " dep inner join " + TableLfDomination.TABLE_NAME
				+ " domination on dep." + TableLfDep.DEP_ID + "=domination."
				+ TableLfDomination.DEP_ID + " where domination."
				+ TableLfDomination.USER_ID + "=" + sysuserID + " and dep."+TableLfDep.DEP_STATE + "=1" 
				+ " AND dep." + TableLfDep.CORP_CODE + " = '"+corpCode+"'";
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
	
	 
    public List<LfSysuser> findDomSysuserByDepID(String depID,
                                                 LinkedHashMap<String, String> orderbyMap) throws Exception {
		//拼接sql
		String sql = "select sysuser.* from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDomination.TABLE_NAME
				+ " domination on sysuser." + TableLfSysuser.USER_ID
				+ "=domination." + TableLfDomination.USER_ID
				+ " where domination." + TableLfDomination.DEP_ID + "=" + depID;
		//排序条件拼接
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
		//返回结果
		return findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
	}
	
	 
    public List<LfDep> findDepBySysuserID(String depId, String sysuserID,
                                          LinkedHashMap<String, String> orderbyMap) throws Exception {
		List<LfDep> lfDepList = null;
		//拼接sql
		String sql = "select dep.* from " + TableLfDep.TABLE_NAME
				+ " dep inner join " + TableLfDomination.TABLE_NAME
				+ " domination on dep." + TableLfDep.DEP_ID + "=domination."
				+ TableLfDomination.DEP_ID + " where domination."
				+ TableLfDomination.USER_ID + "=" + sysuserID + " and dep."+TableLfDep.DEP_STATE + "=1 order by dep."
				+TableLfDomination.DEP_ID;
		
		List<LfDep> lfDep = findEntityListBySQL(LfDep.class,
				sql, StaticValue.EMP_POOLNAME);
		if(lfDep != null && lfDep.size()>0){
			if(depId != null && !"".equals(depId)){	
				//不是进入 返回当前机构
				String depCodeSql = "select dep.* from " + TableLfDep.TABLE_NAME
				+ " dep inner join " + TableLfDomination.TABLE_NAME
				+ " domination on dep." + TableLfDep.DEP_ID + "=domination."
				+ TableLfDomination.DEP_ID + " where domination."
				+ TableLfDomination.USER_ID + "=" + sysuserID + " and dep."+TableLfDep.DEP_STATE + "=1 " 
				+ "and dep."+TableLfDep.SUPERIOR_ID+"="+depId +" order by dep."
				+TableLfDomination.DEP_ID;
				lfDepList = findEntityListBySQL(LfDep.class,
						depCodeSql, StaticValue.EMP_POOLNAME);
			}else{
				LfDep dep = lfDep.get(0);
				Long dId = dep.getDepId();
				String depCodeSql = "select dep.* from " + TableLfDep.TABLE_NAME
				+ " dep inner join " + TableLfDomination.TABLE_NAME
				+ " domination on dep." + TableLfDep.DEP_ID + "=domination."
				+ TableLfDomination.DEP_ID + " where domination."
				+ TableLfDomination.USER_ID + "=" + sysuserID + " and dep."+TableLfDep.DEP_STATE + "=1 " 
				+ "and (dep."+TableLfDep.SUPERIOR_ID+"="+dId +" or dep."
				+ TableLfDep.DEP_ID+"="+dId+ ") order by dep."
				+TableLfDomination.DEP_ID;
				lfDepList = findEntityListBySQL(LfDep.class,
						depCodeSql, StaticValue.EMP_POOLNAME);
			}
		}
		//返回结果
		return lfDepList;
	}

	/**
	 *  公共查询方法
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap
	 * @param columName
	 * @param orderbyMap
	 * @param groupColum
	 * @return
	 * @throws Exception
	 */
	 
    public <T> List<String[]> findListByConditionByColumNameWithGroupBy(Class<T> entityClass,
                                                                        LinkedHashMap<String, String> conditionMap, String columName,
                                                                        LinkedHashMap<String, String> orderbyMap, String groupColum) throws Exception
	{
		List<String[]> returnList = new ArrayList<String[]>();
		Iterator<Map.Entry<String, String>> iter = null;
		Map.Entry<String, String> e = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		String[] columArray = columName.split(",");
	//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select ").append(columName).append(" from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK());
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			StringBuffer condtionSb = new StringBuffer();
			Field[] fields = entityClass.getDeclaredFields();
			appendCondition(conditionMap,fields,condtionSb,columns);
			sqlSb.append(new SmsSpecialDAO().getConditionSql(condtionSb.toString()));
		}
		if (groupColum != null && !"".equals(groupColum))
		{
			sqlSb.append(" group by ").append(groupColum);
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			sqlSb.append(" order by ");
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				sqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			// 去掉最后一个逗号。
			sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		}
		String sql = sqlSb.toString();
		//数据库连接
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//打印sql处理
			EmpExecutionContext.sql("execute sql : " + sql);
			
			ps = conn.prepareStatement(sql);
		/*	if (conditionMap != null && !conditionMap.entrySet().isEmpty())
			{
				int psIndex = 0;
				iter = conditionMap.entrySet().iterator();
				while (iter.hasNext())
				{
					e = iter.next();
					if (!"".equals(e.getValue()))
					{
						psIndex++;
						ps.setString(psIndex, e.getValue());
					}
				}
			}*/
			//执行数据操作
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				String[] strArray = new String[columArray.length];
				for(int i=0;i< columArray.length;i++)
				{
					strArray[i]=rs.getString(i+1);
					//strArray[i]=rs.getString(columArray[i]);
				}
				returnList.add(strArray);
			}
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex, "按照一定条件查询数据出错！");
		} finally
		{
			// 关闭数据库资源
			super.close(rs, ps, conn);
		}
		//返回结果
		return returnList;
	}
	/**
	 * 拼接查询条件方法
	 * @param conditionMap
	 * @param fields
	 * @param conditionSql
	 * @param columns
	 */
	private void appendCondition(LinkedHashMap<String, String> conditionMap,
			Field[] fields,StringBuffer conditionSql,Map<String, String> columns)
	{
		Map.Entry<String, String> e = null;
		//列名
		String field = null;
		//值
		String fieldValue = null;
		String symbols = null;
		//类型
		String fieldType = null;
		Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
				.iterator();
		while (iter.hasNext())
		{
			e = iter.next();
			fieldValue = e.getValue();
			// 如果查询条件的值为空字符串，则过滤该条件。
			if (!"".equals(fieldValue))
			{
				// 实体bean字段和操作符分离
				if (e.getKey().contains("&"))
				{
					field = e.getKey().split("&")[0];
					symbols = e.getKey().split("&")[1];
				} else
				{
					field = e.getKey();
					// 默认操作符为等于号
					symbols = "=";
				}
				// 获得实体类变量类型
				for (int index = 0; index < fields.length; index++)
				{
					if (fields[index].getName().equals(field))
					{
						fieldType = fields[index].getGenericType()
								.toString();
						break;
					}
				}
				// 追加查询条件的数据库字段
				conditionSql.append(" and ").append(columns.get(field));
				// 追加查询条件的操作符
				if (symbols.equalsIgnoreCase(StaticValue.LIKE1)
						|| symbols.equalsIgnoreCase(StaticValue.LIKE2))
				{
					conditionSql.append(" like ");
				} else
				{
					conditionSql.append(" ").append(symbols).append(" ");
				}
				// 追加查询条件的值
				if (symbols.equalsIgnoreCase(StaticValue.IN)
						|| symbols.equalsIgnoreCase(StaticValue.NOT_IN))
				{
					String[] valueArray = fieldValue.split(",");
					StringBuffer valueSb = new StringBuffer("(");
					for (int i = 0; i < valueArray.length; i++)
					{
						/*valueSb.append("'").append(valueArray[i]).append(
								"',");*/
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							valueSb.append("'").append(valueArray[i]).append(
									"',");
						} else if(fieldType.equals("class java.lang.String"))
						{
							valueSb.append("'").append(valueArray[i]).append(
									"',");
						}else {
							valueSb.append(valueArray[i]).append(",");
						}
					}
					valueSb.deleteCharAt(valueSb.lastIndexOf(",")).append(
							")");
					conditionSql.append(valueSb);
				} else if (symbols.equalsIgnoreCase(StaticValue.LIKE))
				{
					conditionSql.append("'%").append(fieldValue).append(
							"%'");
				} else if (symbols.equalsIgnoreCase(StaticValue.LIKE1))
				{
					conditionSql.append("'%").append(fieldValue)
							.append("'");
				} else if (symbols.equalsIgnoreCase(StaticValue.LIKE2))
				{
					conditionSql.append("'").append(fieldValue)
							.append("%'");
				} else if (symbols.equals("=") || symbols.equals(">")
						|| symbols.equals("<") || symbols.equals("<>")
						|| symbols.equals(">=") || symbols.equals("<="))
				{
					if (fieldType.equals("class java.sql.Timestamp")
							|| fieldType.equals("class java.util.Date"))
					{
						conditionSql.append("'").append(fieldValue).append(
								"'");
					}else if(fieldType.equals("class java.lang.String"))
					{
						conditionSql.append("'").append(fieldValue).append(
								"'");
					}else {
						conditionSql.append(fieldValue);
					}
				} else if (symbols.equalsIgnoreCase(StaticValue.BETWEEN))
				{
					String[] valueArray = fieldValue.split(",");
					if (fieldType.equals("class java.sql.Timestamp")
							|| fieldType.equals("class java.util.Date"))
					{
						conditionSql.append("'").append(valueArray[0])
								.append("' and '").append(valueArray[1])
								.append("'");
					} else
					{
						conditionSql.append(valueArray[0]).append(" and ")
								.append(valueArray[1]);
					}
				}
			}
		}
	}
	
	/**
	 * 通过depID查询操作员机构集合
	 * @return
	 * @throws Exception 
	 */
	 
    public List<LfDep> getDepListByDepIdCon(String depIdCon,
                                            Map<String, String> conditionMap, Map<String, String> orderbyMap,
                                            PageInfo pageInfo) throws Exception{
		//拼接sql
		String sql = "select * from " + TableLfDep.TABLE_NAME
				+ "  where " + depIdCon ;

		Iterator<Map.Entry<String, String>> iter = null;
		//获取实体类与数据库表字段的MAP映射
		Map<String, String> columns = getORMMap(LfDep.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			//获取实体类属性
			Field[] fields = LfDep.class.getDeclaredFields();
			//属性类型
			String fieldType = null;
			while (iter.hasNext()) {
				e = iter.next();
				for (int index = 0; index < fields.length; index++) {
					if (fields[index].getName().equals(e.getKey())) {
						fieldType = fields[index].getGenericType().toString();
						break;
					}
				}
				if (!"".equals(e.getValue())) {
					String eKey = e.getKey();
					columnName = eKey.indexOf("&") < 0 ? columns.get(eKey)
							: columns.get(eKey
									.subSequence(0, eKey.indexOf("&")));
					if (eKey.contains("&like")) {
						sql = sql + " and " + columnName + " like '%"
								+ e.getValue() + "%' ";
					} else if (eKey.contains("&>")) {
						sql = sql + " and " + columnName + ">" + e.getValue()
								+ " ";
					} else {
						//是否是日期类型
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						if (fieldType.equals("class java.lang.String")
								|| isDateType) {
							sql = sql + " and " + columnName + "='"
									+ e.getValue() + "' ";
						} else {
							sql = sql + " and " + columnName + "="
									+ e.getValue();
						}
					}

				}
			}
		}
		String tempSql = sql;
		//排序条件拼接
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			sql += " order by ";
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext()) {
				e = iter.next();
				sql = sql + columns.get(e.getKey()) + " " + e.getValue() + ",";
			}

			sql = sql.substring(0, sql.lastIndexOf(","));
		}

		if (pageInfo == null) {
			//不分页，执行查询，返回结果
			return findEntityListBySQL(LfDep.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			//记录数sql拼接
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(tempSql)
					.append(") temp_table").toString();
			//分页，执行查询，返回结果
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(LfDep.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	/**
	 * 获取操作员对应机构的机构编码
	 * @param userId
	 * @return
	 */
	 
    public String getDepPathByUserId(Long userId)
	{
		String depPath="";
		String sql = "select "+TableLfDep.DEP_PATH+" from lf_dep where dep_id = " +
				"(select dep_id from lf_sysuser where user_id ="+userId.toString()+")";
		try {
			depPath = getString(TableLfDep.DEP_PATH, sql, StaticValue.EMP_POOLNAME);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取操作员(userid="+userId.toString()+")对应机构的机构编码异常！");
		}
		return depPath;
	}
	/**
	 * 查找操作员所在部门的审核员
	 * @param userid
	 * @return
	 */
	 
    public Long[] getUserOfDepReviwer(String userid)
	{
		String sql = "select ls.user_id from lf_sysuser ls where exists (select dep_id from lf_sysuser where dep_id=ls.dep_id and user_id="+userid+") "
			+ " and IS_REVIEWER=1 and ls.user_state = 1 and  ls.user_id <> "+userid;
		
		List<DynaBean> userList =getListDynaBeanBySql(sql);
		Long[] idArray = null;
		if(userList != null && userList.size() > 0 )
		{
			idArray = new Long[userList.size()];
			int index = 0;
			for(DynaBean dyb : userList)
			{
				Long longid = (Long)dyb.get("user_id");
				idArray[index] = longid;
				index ++;
			}
		}
		return idArray;
	}
	
	/**
	 * 查找操作员所在部门的审核员
	 * @param userid
	 * @return
	 */
	 
    public Long[] getUserOfDepReviwer2(String depId, String userid)
	{
		String sql = "select ls.user_id from lf_sysuser ls where dep_id="+depId
			+ " and IS_REVIEWER=1 and ls.user_state = 1 and ls.user_id <> "+userid;
		
		List<DynaBean> userList =getListDynaBeanBySql(sql);
		Long[] idArray = null;
		if(userList != null && userList.size() > 0 )
		{
			idArray = new Long[userList.size()];
			int index = 0;
			for(DynaBean dyb : userList)
			{
				Long longid = Long.parseLong(dyb.get("user_id").toString());
				idArray[index] = longid;
				index ++;
			}
		}
		return idArray;
	}
	
	//写一个不需要分页的获取员工机构下面所有子机构的方法
	 
    public List<LfEmployeeDep> findEmployeeDepsByDepId(String corpCode, String depId) throws Exception {
		String sql = "";
		StringBuffer conditionSql = new StringBuffer();
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
				//适用ORACEL数据库的SQL语句
		   	sql = "select lfemployeedep.*";
		   	String depIds = executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(depId)});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeDep>();
	   	 	}
		   	 conditionSql = getInConditionSql(conditionSql, depIds);
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			  sql = "select lfemployeedep.*, ROW_NUMBER() Over(Order By lfemployeedep.corp_code) As rn";
			  conditionSql.append("and lfemployeedep.").append(TableLfEmployeeDep.DEP_ID)
			  	.append(" in ( select DepId from GetEmpDepChildByPID(1,").append(depId).append(")) ");
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = "select lfemployeedep.*";
			 /*String depIds = executeProcessReutrnCursorOfMySql(
		   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
		   	 			new Integer[]{1,Integer.valueOf(depId)});*/
			 String depIds = getEmpChildIdByDepId(depId.toString());
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeDep>();
	   	 	}
		   	 conditionSql = getInConditionSql(conditionSql, depIds);
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = "select lfemployeedep.*";
			 String depIds = getEmpChildIdByDepId(depId.toString());
		   	 if(depIds == null || depIds.length() == 0)
		   	 {
		   	 	return new ArrayList<LfEmployeeDep>();
		   	 }
		   	conditionSql = getInConditionSql(conditionSql, depIds);
		}

		String tableSql = new StringBuffer(" from ").append(
				TableLfEmployeeDep.TABLE_NAME).append(" lfemployeeDep ").toString();

		String moreWhere = "";
		if(corpCode!=null) {
            moreWhere = new StringBuffer(" and lfemployeedep.").append(TableLfEmployeeDep.CORP_CODE).append("= '").append(corpCode+"'").toString();
        }
		
		sql += tableSql;
		//处理后的条件
		String conditionStr = new SmsSpecialDAO().getConditionSql(moreWhere + conditionSql);
		sql += conditionStr;

		List<LfEmployeeDep> returnVoList = findEntityListBySQL(LfEmployeeDep.class, sql, StaticValue.EMP_POOLNAME);
		return returnVoList;
	}
	
	private String executeProcessReutrnCursorOfOracle( String POOLNAME,
			String processStr,Integer[] params)
			throws Exception
	{
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement proc = null;
		StringBuffer deps = new StringBuffer();
		String depIds = "";
		try
		{
			
			conn = connectionManager.getDBConnection(POOLNAME);
			proc = conn.prepareCall("{ call "+processStr+"(?,?,?,?) }");
			proc.setInt(1, params[0]);
			proc.setInt(2, params[1]);
			proc.setString(3, StringUtils.getRandom());
			proc.registerOutParameter(4,oracle.jdbc.OracleTypes.CURSOR);
			proc.execute();
			rs = (ResultSet)proc.getObject(4);
			while (rs.next())
			{
				deps.append(rs.getLong("DepID")).append(",");
			}
			if(deps.length() > 0) {
                depIds = deps.substring(0, deps.lastIndexOf(","));
            }
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行oracle存储过程并返回游标异常。");
			throw e;
		} finally
		{
			
			try
			{
				close(rs, proc, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源异常。");
			}
		}
		return depIds;
	}
	
	private StringBuffer getInConditionSql(StringBuffer conditionSql,String depIds)
	{
		/*******机构in查询处理***********/
		//int maxCount = StaticValue.inConditionMax;//in查询最大个数限制
		int maxCount = StaticValue.getInConditionMax();//in查询最大个数限制
		String[] depIdArry = depIds.split(",");
		int size = depIdArry.length;
		if(size>maxCount)
		{
			conditionSql.append(" and (lfemployeedep.").append(
					TableLfEmployee.DEP_ID).append(" in (");
			
			String inString = "";
			for(int i=0;i<size;i++)
			{
				inString +=  depIdArry[i] ;
				if( i > maxCount-2 && (i+1)%maxCount==0  && i<size - 1  )
				{
					conditionSql.append(inString).append(") or ").append(" lfemployeedep.").append(
							TableLfEmployee.DEP_ID).append(" in (");
					inString = "";
				}else if( i<size - 1 )
				{
					inString += ",";
				}
			}
			conditionSql.append(inString).append("))");
		}else
		{
		
			conditionSql.append(" and lfemployeedep.").append(
					TableLfEmployee.DEP_ID).append(" in (").append(depIds).append(")");
		}
		/*******END-机构in查询处理**********/
		
		return conditionSql;
	}
	
	/**
	 * 获取员工机构的所有子级机构Id
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	 
    public String getEmpChildIdByDepId(String depId) throws Exception
	{
		String depIds = depId ;
		String conditionDepid = depId;
		String sql = "";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int incount = 0;
		//int maxIncount = StaticValue.inConditionMax;
		int maxIncount = StaticValue.getInConditionMax();
		int n = 1;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			boolean hasNext = true;
			while(hasNext)
			{	
				incount = 0;
				sql = "select dep_Id from lf_employee_dep where PARENT_ID in (" + conditionDepid + ")";
				ps = conn.prepareStatement(sql);
				// ps.setString(1, conditionDepid);
				rs = ps.executeQuery();
				conditionDepid = "";
				while(rs.next())
				{
					incount ++;
					depIds += "," + rs.getString("dep_id");
					if(incount > maxIncount*n)
					{
						n++;
						conditionDepid = conditionDepid.substring(0,conditionDepid.length()-1);
						conditionDepid += ") or PARENT_ID in (";
					}
					conditionDepid += rs.getString("dep_id") + ",";
				}
				if(conditionDepid.length() == 0)
				{
					hasNext = false;
				}else
				{
					hasNext = true;
					conditionDepid = conditionDepid.substring(0,conditionDepid.length()-1);
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取员工机构的所有子级机构Id异常!");
			throw e;
			
		} finally
		{
			try{

				close(rs, ps, conn);
			}catch (Exception e){
                EmpExecutionContext.error(e, "发现异常");
			}
		}
		return depIds;	
	}
	
	//写一个不需要分页的获取员工机构下面所有员工电话的方法
	 
    public List<LfEmployee> findEmpMobileByDepIds(String corpCode,
                                                  String depId) throws Exception {
		String sql = "";
		StringBuffer conditionSql = new StringBuffer();
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
				//适用ORACEL数据库的SQL语句
		   	 sql = "select lfemployee."+TableLfEmployee.MOBILE;
		   	String depIds = executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(depId)});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployee>();
	   	 	}
	   	 conditionSql.append(" and lfemployee.DEP_ID in (").append(depIds).append(")");
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			  sql = "select lfemployee.MOBILE, ROW_NUMBER() Over(Order By lfemployee.CORP_CODE) As rn";
			  conditionSql.append("and lfemployee.").append(TableLfEmployee.DEP_ID)
			  	.append(" in ( select DepId from GetEmpDepChildByPID(1,").append(depId).append(")) ");
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = "select lfemployee.MOBILE";
			 /*String depIds = executeProcessReutrnCursorOfMySql(
	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	 			new Integer[]{1,Integer.valueOf(depId)});*/
			 String depIds = getEmpChildIdByDepId(depId.toString());
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployee>();
	   	 	}
		   	 conditionSql.append(" and lfemployee.DEP_ID in (").append(depIds).append(")");
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = "select lfemployee.MOBILE";
			 String depIds = getEmpChildIdByDepId(depId.toString());
		   	 if(depIds == null || depIds.length() == 0)
		   	 {
		   	 	return new ArrayList<LfEmployee>();
		   	 }
			 conditionSql.append(" and lfemployee.DEP_ID in (").append(depIds).append(")");
		}

		String tableSql = new StringBuffer(" from ").append(TableLfEmployee.TABLE_NAME).append(" lfemployee ").toString();

		String moreWhere = "";
		if(corpCode!=null)
		{
			moreWhere = new StringBuffer(" and lfemployee.").append(TableLfEmployee.CORP_CODE).append("= '").append(corpCode+"'").toString();
		}
		
		sql += tableSql;
		//处理后的moreWhere + conditionSql条件
		String conditionStr = new SmsSpecialDAO().getConditionSql(moreWhere + conditionSql);
		sql +=conditionStr;
		List<LfEmployee> returnList =findPartEntityListBySQL(LfEmployee.class, sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	/* @param orderbyMap
	 *            排序条件
	 * @param pageInfo
	 *            分页信息，无需分析时传入null
	 * @return 员工信息的集合
	 * @throws Exception
	 */
	 
    public List<LfEmployee> getEmployeeByGuid(String udgId, String loginId,
                                              LinkedHashMap<String, String> conditionMap,
                                              LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<LfEmployee> employees = null;
		List<LfMalist> malists = null;
		String sql = "";
		String sql2 = "";
		//第一次进入选择员工页面时群组id为空，就根据loginId查询。点击群组之后udgId不会空
		if (udgId != null && !"".equals(udgId))
		{
			sql = "select * from " + TableLfEmployee.TABLE_NAME + "  where "
					+ TableLfEmployee.GUID + "  in (select "
					+ TableLfList2gro.GUID + " from "
					+ TableLfList2gro.TABLE_NAME + "  where "
					+ TableLfList2gro.UDG_ID + " in (" + udgId + ") and "
					+ TableLfList2gro.L2G_TYPE + "=0)";
			sql2 = "select * from " + TableLfMalist.TABLE_NAME + "  where "
					+ TableLfMalist.GUID + "  in (select "
					+ TableLfList2gro.GUID + " from "
					+ TableLfList2gro.TABLE_NAME + "  where "
					+ TableLfList2gro.UDG_ID + " in (" + udgId + ") and "
					+ TableLfList2gro.L2G_TYPE + "=2)";
		}else if (loginId != null && !"".equals(loginId))
		{
			sql = "select * from " + TableLfEmployee.TABLE_NAME + "  where "
					+ TableLfEmployee.GUID + "  in (select "
					+ TableLfList2gro.GUID + " from "
					+ TableLfList2gro.TABLE_NAME + "  where "
					+ TableLfList2gro.UDG_ID + " in(select  "
					+ TableLfUdgroup.UDG_ID + " from "
					+ TableLfUdgroup.TABLE_NAME + " where "
					+ TableLfUdgroup.USER_ID + "=" + loginId + "))";
			sql2 = "select * from " + TableLfMalist.TABLE_NAME + "  where "
					+ TableLfMalist.GUID + "  in (select "
					+ TableLfList2gro.GUID + " from "
					+ TableLfList2gro.TABLE_NAME + "  where "
					+ TableLfList2gro.UDG_ID + " in(select  "
					+ TableLfUdgroup.UDG_ID + " from "
					+ TableLfUdgroup.TABLE_NAME + " where "
					+ TableLfUdgroup.USER_ID + "=" + loginId + "))";
		}

		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(LfEmployee.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			Field[] fields = LfSpDepBind.class.getDeclaredFields();
			String fieldType = null;
			while (iter.hasNext())
			{
				e = iter.next();
				for (int index = 0; index < fields.length; index++)
				{
					if (fields[index].getName().equals(e.getKey()))
					{
						fieldType = fields[index].getGenericType().toString();
						break;
					}
				}
				if (!"".equals(e.getValue()))
				{
					String eKey = e.getKey();
					columnName = eKey.indexOf("&") < 0 ? columns.get(eKey)
							: columns.get(eKey
									.subSequence(0, eKey.indexOf("&")));
					if (eKey.contains("&like"))
					{
						sql = sql + " and " + columnName + " like '%"+ e.getValue() + "%' ";
						sql2 = sql2 + " and " + columnName + " like '%"+ e.getValue() + "%' ";
					} else if (eKey.contains("&>"))
					{
						sql = sql + " and " + columnName + ">" + e.getValue()+ " ";
						sql2 = sql2 + " and " + columnName + ">" + e.getValue()+ " ";
					} else
					{
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						if (fieldType.equals("class java.lang.String")
								|| isDateType)
						{
							sql = sql + " and " + columnName + "='"+ e.getValue() + "' ";
							sql2 = sql2 + " and " + columnName + "='"+ e.getValue() + "' ";
						} else
						{
							sql = sql + " and " + columnName + "="+ e.getValue();
							sql2 = sql2 + " and " + columnName + "="+ e.getValue();
						}
					}

				}
			}
		}

		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			sql += " order by ";
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				sql = sql + columns.get(e.getKey()) + " " + e.getValue() + ",";
			}
 			
			sql = sql.substring(0, sql.lastIndexOf(","));
		}
		if (pageInfo == null)
		{
			employees= findEntityListBySQL(LfEmployee.class, sql,
					StaticValue.EMP_POOLNAME);
			malists =  findEntityListBySQL(LfMalist.class, sql2,
					StaticValue.EMP_POOLNAME);
			LfEmployee employee = null;
			for (LfMalist ma :malists) 
			{
				employee = new LfEmployee();
				employee.setGuId(ma.getGuId());
				employee.setRecState(2);
				employee.setMobile(ma.getMobile());
				employee.setName(ma.getName());
				
				employees.add(employee);
			}
			return employees;
		} else
		{
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql.substring(0, sql.lastIndexOf(" order by ")))
					.append(")").append(" tmp").toString();
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(LfEmployee.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	

	/**
	 * 获取机构成员,只支持分页查询
	 * @description    
	 * @param udgId
	 * @param loginId
	 * @param epname
	 * @param pageInfo
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-5 下午01:24:08
	 */
	public List<LfEmployee> getEmployeeByGuid(String udgId, String loginId, String epname,PageInfo pageInfo)throws Exception
	{
		List<LfEmployee> employees = new ArrayList<LfEmployee>();
		StringBuffer sql = new StringBuffer().append("select * from (select guid,rec_state as recstate,mobile,name from LF_EMPLOYEE ")
												.append("union all select guid,2 as recstate ,mobile,name from LF_MALIST) a ")
												.append("where a.GUID  in (select GUID from LF_LIST2GRO where UDG_ID in (");
		//第一次进入选择员工页面时群组id为空，就根据loginId查询。点击群组之后udgId不会空
		if (udgId != null && !"".equals(udgId))
		{
			sql.append(udgId).append(")");
		}
		else if (loginId != null && !"".equals(loginId))
		{
			sql.append("select UDG_ID from LF_UDGROUP where USER_ID=").append(loginId).append(")");
		}
		sql.append(" and L2G_TYPE in(0,2))");
		if(epname != null && !"".equals(epname))
		{
			sql.append(" and name like '%").append(epname).append("%'");
		}
		String sqlStr = sql.toString();
		sql.append(" order by NAME ASC");

		String countSql = new StringBuffer(
				"select count(*) totalcount from (").append(sqlStr).append(")").append(" tmp").toString();
		List<DynaBean>  employeeList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql.toString(), 
				countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		if(employeeList != null && employeeList.size()>0)
		{
			LfEmployee employee = null;
			for(DynaBean employeeBean:employeeList)
			{
				employee = new LfEmployee();
				employee.setGuId(Long.parseLong(employeeBean.get("guid").toString()));
				employee.setRecState(2);
				employee.setMobile(employeeBean.get("mobile").toString());
				employee.setName(employeeBean.get("name")==null?"":employeeBean.get("name").toString());
				employees.add(employee);
			}
		}
		return employees;
	}
	
	/**
	 * 通过GUID和企业编码获取机构成员,只支持分页查询
	 * @description    
	 * @param udgId
	 * @param loginId
	 * @param epname
	 * @param pageInfo
	 * @param lgcorpcode
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-18 下午03:58:23
	 */
	public List<LfEmployee> getEmployeeByGuidAndCorpCode(String udgId, String loginId, String epname,PageInfo pageInfo, String lgcorpcode)throws Exception
	{
		List<LfEmployee> employees = new ArrayList<LfEmployee>();
		StringBuffer sql = new StringBuffer().append("select * from (select guid,rec_state as recstate,mobile,name,corp_code from LF_EMPLOYEE ")
												.append("union all select guid,2 as recstate ,mobile,name,corp_code from LF_MALIST) a ")
												.append("where a.corp_code = '").append(lgcorpcode)
												.append("' and a.GUID  in (select GUID from LF_LIST2GRO where UDG_ID in (");
		//第一次进入选择员工页面时群组id为空，就根据loginId查询。点击群组之后udgId不会空
		if (udgId != null && !"".equals(udgId))
		{
			sql.append(udgId).append(")");
		}
		else if (loginId != null && !"".equals(loginId))
		{
			sql.append("select UDG_ID from LF_UDGROUP where USER_ID=").append(loginId).append(")");
		}
		sql.append(" and L2G_TYPE in(0,2))");
		if(epname != null && !"".equals(epname))
		{
			sql.append(" and name like '%").append(epname).append("%'");
		}
		String sqlStr = sql.toString();
		sql.append(" order by NAME ASC");

		String countSql = new StringBuffer(
				"select count(*) totalcount from (").append(sqlStr).append(")").append(" tmp").toString();
		List<DynaBean>  employeeList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql.toString(), 
				countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		if(employeeList != null && employeeList.size()>0)
		{
			LfEmployee employee = null;
			for(DynaBean employeeBean:employeeList)
			{
				employee = new LfEmployee();
				employee.setGuId(Long.parseLong(employeeBean.get("guid").toString()));
				employee.setRecState(2);
				employee.setMobile(employeeBean.get("mobile").toString());
				employee.setName(employeeBean.get("name")==null?"":employeeBean.get("name").toString());
				employees.add(employee);
			}
		}
		return employees;
	}
	
	/**
	 * 获取员工机构列表——用于构建机构树
	 * 
	 * @param userId
	 *            当前操作员的ID
	 * @param depId
	 *            传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	 
    public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(
			String userId, String depId) throws Exception
	{
		String sql = "";
		List<LfEmployeeDep> lfEmployeeDepList = null;
		//如果没有掺入机构id，则查找当前操作员员工机构权限的数据
		if (depId == null || "".equals(depId))
		{
			// 这里是为了解决员工机构关联表出现了多条记录的处理方法，产生2条记录的原因可能是机器卡，或者同步
			List<LfEmpDepConn> connList = null;
			StringBuffer sb = new StringBuffer();
			sb.append(" select * from ").append(TableLfEmpDepConn.TABLE_NAME)
					.append(" c " + StaticValue.getWITHNOLOCK()).append(" where c.")
					.append(TableLfEmpDepConn.USER_ID).append(" = ").append(
							userId);
			connList = findEntityListBySQL(LfEmpDepConn.class, sb.toString(),
					StaticValue.EMP_POOLNAME);
			//LfEmpDepConn conn = null;
			if (connList != null && connList.size() > 0)
			{
				//conn = connList.get(0);
				//Long id = conn.getDepId();
				String ids = "";
				for(LfEmpDepConn co:connList){
					ids+=co.getDepId()+",";
				}
				ids = ids.substring(0,ids.lastIndexOf(","));
				sql = new StringBuffer(" select e.* from ").append(
						TableLfEmployeeDep.TABLE_NAME).append(
						" e " + StaticValue.getWITHNOLOCK()).append(" where  e.")
						.append(TableLfEmployeeDep.DEP_ID).append(" in(")
						.append(ids).append(") or ").append(
								TableLfEmployeeDep.PARENT_ID).append(" in(")
						.append(ids).append(") order by ").append(
								TableLfEmployeeDep.ADD_TYPE).append(
								" " + StaticValue.ASC).toString();
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
						sql, StaticValue.EMP_POOLNAME);
			}
		}
		//传入机构则查找该机构下的员工
		else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfEmployeeDep.PARENT_ID).append(" = ").append(depId)
					.toString();
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
		}

		return lfEmployeeDepList;
	}
	
	/**
	 * 获取员工机构列表——用于构建机构树(重载方法)
	 * 
	 * @param userId
	 *            当前操作员的ID
	 * @param depId
	 *            传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @param corpCode 企业编码
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(
			String userId, String depId,String corpCode) throws Exception
	{
		//存放SQL语句的字符串
		String sql = "";
		List<LfEmployeeDep> lfEmployeeDepList = null;
		//如果没有掺入机构id，则查找当前操作员员工机构权限的数据
		if (depId == null || "".equals(depId))
		{
			// 这里是为了解决员工机构关联表出现了多条记录的处理方法，产生2条记录的原因可能是机器卡，或者同步
			List<LfEmpDepConn> connList = null;
			StringBuffer sb = new StringBuffer();
			sb.append(" select * from ").append(TableLfEmpDepConn.TABLE_NAME)
					.append(" c " + StaticValue.getWITHNOLOCK()).append(" where c.")
					.append(TableLfEmpDepConn.USER_ID).append(" = ").append(
							userId);
			connList = findEntityListBySQL(LfEmpDepConn.class, sb.toString(),
					StaticValue.EMP_POOLNAME);
			if (connList != null && connList.size() > 0)
			{
				//机构ID的字符串
				String ids = "";
				for(LfEmpDepConn co:connList){
					ids+=co.getDepId()+",";
				}
				//截取掉最后一个逗号
				ids = ids.substring(0,ids.lastIndexOf(","));
				//查询员工机构必须带企业编码
				sql = new StringBuffer(" select * from ").append(
						TableLfEmployeeDep.TABLE_NAME).append(
						" " + StaticValue.getWITHNOLOCK()).append(" where  (")
						.append(TableLfEmployeeDep.DEP_ID).append(" in(")
						.append(ids).append(") or ").append(
								TableLfEmployeeDep.PARENT_ID).append(" in(")
						.append(ids).append("))").append(" AND CORP_CODE='"+corpCode+"' ").append(" order by ").append(
								TableLfEmployeeDep.ADD_TYPE).append(
								" " + StaticValue.ASC).toString();
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
						sql, StaticValue.EMP_POOLNAME);
			}
		}
		//传入机构则查找该机构下的员工
		else
		{
			//查询员工机构必须带企业编码
			sql = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfEmployeeDep.PARENT_ID).append(" = ").append(depId).append(" AND CORP_CODE='"+corpCode+"' ")
					.toString();
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
		}
		//返回员工机构的集合
		return lfEmployeeDepList;
	}
	
	/**
	 * 通过操作员编码获取机构编码
	 * @param userCode
	 * @return
	 */
	 
    public String getDepCodeByUserCode(String userCode)
	{
		SuperDAO superdao=new SuperDAO();
		//拼接sql
		String sql ="select DEP_CODE_THIRD from LF_DEP ld left join LF_SYSUSER ls " +
				"on ld.DEP_ID=ls.DEP_ID where ls.USER_CODE='"+userCode+"' ";
		List<DynaBean> deps=superdao.getListDynaBeanBySql(sql);
		//如果返回结果
		if(deps.size()>0)
		{
			DynaBean lfdep=deps.get(0);
			String depcodethird=null;
			if(lfdep.get("dep_code_third")!=null)
			{
				depcodethird=(String)lfdep.get("dep_code_third");
			}
			if(depcodethird!=null&&!"".equals(depcodethird)){
				return depcodethird;
			}
		}else
		{
			return null;
		}
		return null;
	}
	
	/**
	 *   获取其机构的密码接收人
	 * @param depId
	 * @param pageInfo
	 * @return
	 */
	 
    public List<LfdepPassUser> getSysuserbyDepId(String depId, PageInfo pageInfo)
	{
		List<LfdepPassUser> depPassUserVos = null;
		try{
			StringBuffer sqlstr = new StringBuffer();
			sqlstr.append(" SELECT usertable.").append(TableLfSysuser.USER_ID)
			.append(",usertable.").append(TableLfSysuser.NAME)
			.append(",usertable.").append(TableLfSysuser.MOBILE)
			.append(",usertable.").append(TableLfSysuser.WORKNUMBER)
			.append(",dep.").append(TableLfDep.DEP_ID)
			.append(",dep.").append(TableLfDep.DEP_NAME).append(" FROM ")
			.append(TableLfDeppwdReceiver.TABLE_NAME).append(" receiver ")
			.append(" LEFT JOIN ").append(TableLfSysuser.TABLE_NAME).append(" usertable ON usertable.")
			.append(TableLfSysuser.USER_ID).append(" = receiver.").append(TableLfDeppwdReceiver.USER_ID)
			.append(" LEFT JOIN ").append(TableLfDep.TABLE_NAME).append(" dep ON dep.")
			.append(TableLfDep.DEP_ID).append(" = usertable.").append(TableLfSysuser.DEP_ID)
			.append(" WHERE receiver.").append(TableLfDeppwdReceiver.DEP_ID)
			.append(" = ").append(depId);
			depPassUserVos =findEntityListBySQL(LfdepPassUser.class, sqlstr.toString(), StaticValue.EMP_POOLNAME);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "获取其机构的密码接收人异常。");
		}
		return depPassUserVos;
	}
	
	/**
	 * 查询机构员工信息
	 * @description    
	 * @param epname 员工姓名
	 * @param depId 机构编号
	 * @param lgcorpcode 企业编号
	 * @param pageInfo 分页
	 * @return       机构员工信息			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-5-22 上午10:46:15
	 */
	 
    public  List<DynaBean> findDepLoyee(String epname, String depId, String lgcorpcode, PageInfo pageInfo)
	{
		List<DynaBean> beanList = null;
		try
		{
			//查询详细信息SQL
			String sql = new StringBuffer().append("SELECT ele.").append(TableLfEmployee.EMPLOYEE_ID)
			.append(",ele.").append(TableLfEmployee.MOBILE)
			.append(",ele.").append(TableLfEmployee.NAME)
			.append(",elep.").append(TableLfEmployeeDep.DEP_NAME)
			.append(" FROM ").toString();
			//分页查询SQL
			String countSql = "SELECT COUNT(*) totalcount FROM ";
			//查询条件
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append(TableLfEmployeeDep.TABLE_NAME).append(" elep")
			.append(" INNER JOIN ").append(TableLfEmployee.TABLE_NAME).append(" ele ON ele.")
			.append(TableLfEmployeeDep.DEP_ID).append(" = ").append("elep.").append(TableLfEmployee.DEP_ID);
			
			//条件
			StringBuffer conditionSql = new StringBuffer("");
			if(lgcorpcode != null && !"".equals(lgcorpcode))
			{
				conditionSql.append(" AND ele.").append(TableLfEmployee.CORP_CODE).append(" = '").append(lgcorpcode).append("'");
			}
			if(epname != null && !"".equals(epname.trim()))
			{
				conditionSql.append(" AND ele.").append(TableLfEmployee.NAME).append(" like '%").append(epname).append("%'");
			}
			if(depId != null && !"".equals(depId))
			{
				conditionSql.append(" AND ele.").append(TableLfEmployee.DEP_ID).append(" = ").append(depId);
			}
			String orderSql = " ORDER BY ele.EMPLOYEEID DESC";
			//处理后的条件
			sqlStr.append(new SmsSpecialDAO().getConditionSql(conditionSql.toString()));
			sql += sqlStr.toString();
			countSql += sqlStr.toString();
			sql += orderSql;
			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询机构员工异常！depId:" + depId+"，epname："+epname+"，lgcorpcode：" +lgcorpcode);
		}
		return beanList;
	}
}
