package com.montnets.emp.common.dao.sqlserver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.GenericEmpDAO;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @project emp
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-9 上午10:12:54
 * @description
 */
public class SQLServerEmpDAO extends GenericEmpDAO
{
	@Override
	public Long saveObjectReturnID(Object object) throws Exception
	{
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			//oracle数据库
			return this.saveObjectReturnIDOracle(object);
	   	} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
	   		//sqlserver数据库
	   		return this.saveObjectReturnIDSqlServer(object);
		}else if(StaticValue.DBTYPE==StaticValue.DB2_DBTYPE){
			//db2数据库
			return this.saveObjectReturnIDDB2(object);
		}else if(StaticValue.DBTYPE==StaticValue.MYSQL_DBTYPE){
			//mysql数据库
			return this.saveObjectReturnIDMySql(object);
		}
		return 0L;
	}
	
	
	public Long saveObjectReturnID(Connection conn,boolean isOutConn,Object object) throws Exception
	{
	   		return this.saveObjectReturnIDSqlServer(conn,isOutConn,object);
	}
	
	private Long saveObjectReturnIDOracle(Object object) throws Exception
	{
		//插入后返回的主键id
		Long returnID = 0L;
		Class<?> entityClass = object.getClass();
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(tableName)
				.append("(");
		StringBuffer wenhao = new StringBuffer(") values(");
		//获取实体类属性
		Field[] fields = entityClass.getDeclaredFields();
		String entityId = null;
		for (int i = 0; i < fields.length; i++)
		{
			if(!fields[i].getName().equals("serialVersionUID")){
			sqlSb.append(columns.get(fields[i].getName())).append(",");
			wenhao.append("?").append(",");
			if (columns.get(fields[i].getName()).equals(columns.get("tableId")))
			{
				entityId = fields[i].getName();
			}
			}
		}
		//去掉最后一个逗号
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			Long sequenceValue = getSequenceNextValue(conn,columns.get("sequence"));
			//获取序列值
			if (sequenceValue != null)
			{
				EmpExecutionContext.sql("execute sql : " + sql);
				ps = conn.prepareStatement(sql);
				//属性类型
				String fieldType = null;
				//属性名称大写
				String fieldNameUpper = null;
				Method entityMethod = null;
				//属性值
				Object value = null;
				int psIndex = 0;
				for (int i = 0; i < fields.length; i++)
				{
					if (!fields[i].getName().equals("serialVersionUID")) {
						psIndex++;
						String fieldName = fields[i].getName();
						if (!fieldName.equals(entityId)) {
							fieldNameUpper = Character.toUpperCase(fieldName
									.charAt(0))
									+ fieldName.substring(1);
							entityMethod = entityClass.getMethod("get"
									+ fieldNameUpper);
							//通过反射获取变量的值
							value = entityMethod.invoke(object);
							// 获取变量的类型
							fieldType = fields[i].getGenericType().toString();
							// 是否是日期类型
							boolean isDateType = fieldType
									.equals(StaticValue.TIMESTAMP)
									|| fieldType.equals(StaticValue.DATE_SQL)
									|| fieldType.equals(StaticValue.DATE_UTIL);
							//填充value
							if (value != null && isDateType) {
								ps.setTimestamp(psIndex, Timestamp
										.valueOf(value.toString()));
							} else {
								ps.setObject(psIndex, value);
							}
						} else {
							ps.setObject(psIndex, sequenceValue);
						}
					}
				}
				//执行SQL
				int count = ps.executeUpdate();
				if (count == 1) {
					returnID = sequenceValue;
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			// 关闭数据库资源
			super.close(null, ps, conn);
		}
		return returnID;
	}

	private Long saveObjectReturnIDSqlServer(Object object) throws Exception
	{
		//插入后返回的主键ID
		Long returnID = 0L;
		Class<?> entityClass = object.getClass();
		//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
		//  拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(tableName)
				.append("(");
		StringBuffer wenhao = new StringBuffer(") output inserted.").append(columns.get("tableId")).append(" values(");
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		String entityId = null;
		for (int i = 0; i < fields.length; i++)
		{
			if (!fields[i].getName().equals("serialVersionUID")) {
				if (columns.get(fields[i].getName()).equals(
						columns.get("tableId"))) {
					entityId = fields[i].getName();
				} else {
					sqlSb.append(columns.get(fields[i].getName())).append(",");
					wenhao.append("?").append(",");
				}
			}
		}
		// 去掉最后一个逗号。
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//属性类型
			String fieldType = null;
			//属性名大写
			String fieldNameUpper = null;
			Method entityMethod = null;
			//属性值
			Object value = null;
			int psIndex = 0;
			for (int i = 0; i < fields.length; i++)
			{
				if (!fields[i].getName().equals("serialVersionUID")) {
					String fieldName = fields[i].getName();
					if (!fieldName.equals(entityId)) {
						psIndex++;
						fieldNameUpper = Character.toUpperCase(fieldName
								.charAt(0))
								+ fieldName.substring(1);
						entityMethod = entityClass.getMethod("get"
								+ fieldNameUpper);
						//获取属性值
						value = entityMethod.invoke(object);
						// 获取变量的类型
						fieldType = fields[i].getGenericType().toString();
						//是否是日期类型
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						//填充value
						if (value != null && isDateType) {
							ps.setTimestamp(psIndex, Timestamp.valueOf(value
									.toString()));
						} else {
							ps.setObject(psIndex, value);
						}
					}
				}
			}
			// 采用prepareStatement方法执行SQL语句。
//			int count = ps.executeUpdate();
//			if (count == 1)
//			{
//				ps = conn.prepareStatement("select IDENT_CURRENT('" + tableName
//						+ "')");
				//执行SQL
				rs = ps.executeQuery();
				if (rs.next())
				{
					returnID = rs.getLong(1);
				}
//			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			//关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnID;
	}
	
	private Long saveObjectReturnIDSqlServer(Connection conn,boolean isOutConn,Object object) throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return null;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return null;
		}
		
		//插入后返回的主键ID
		Long returnID = 0L;
		Class<?> entityClass = object.getClass();
		//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
		//  拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(tableName)
				.append("(");
		StringBuffer wenhao = new StringBuffer(") output inserted.").append(columns.get("tableId")).append(" values(");
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		String entityId = null;
		for (int i = 0; i < fields.length; i++)
		{
			if (!fields[i].getName().equals("serialVersionUID")) {
				if (columns.get(fields[i].getName()).equals(
						columns.get("tableId"))) {
					entityId = fields[i].getName();
				} else {
					sqlSb.append(columns.get(fields[i].getName())).append(",");
					wenhao.append("?").append(",");
				}
			}
		}
		// 去掉最后一个逗号。
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//属性类型
			String fieldType = null;
			//属性名大写
			String fieldNameUpper = null;
			Method entityMethod = null;
			//属性值
			Object value = null;
			int psIndex = 0;
			for (int i = 0; i < fields.length; i++)
			{
				if (!fields[i].getName().equals("serialVersionUID")) {
					String fieldName = fields[i].getName();
					if (!fieldName.equals(entityId)) {
						psIndex++;
						fieldNameUpper = Character.toUpperCase(fieldName
								.charAt(0))
								+ fieldName.substring(1);
						entityMethod = entityClass.getMethod("get"
								+ fieldNameUpper);
						//获取属性值
						value = entityMethod.invoke(object);
						// 获取变量的类型
						fieldType = fields[i].getGenericType().toString();
						//是否是日期类型
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						//填充value
						if (value != null && isDateType) {
							ps.setTimestamp(psIndex, Timestamp.valueOf(value
									.toString()));
						} else {
							ps.setObject(psIndex, value);
						}
					}
				}
			}
			// 采用prepareStatement方法执行SQL语句。
//			int count = ps.executeUpdate();
//			if (count == 1)
//			{
//				ps = conn.prepareStatement("select IDENT_CURRENT('" + tableName
//						+ "')");
				//执行SQL
				rs = ps.executeQuery();
				if (rs.next())
				{
					returnID = rs.getLong(1);
				}
//			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(rs, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(rs, ps,conn);
			}
		}
		return returnID;
	}
	
	private Long saveObjectReturnIDDB2(Object object) throws Exception
	{
		//插入后返回的主键id
		Long returnID = 0L;
		Class<?> entityClass = object.getClass();
		//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
		//  拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(tableName)
				.append("(");
		StringBuffer wenhao = new StringBuffer(") values(");
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		String entityId = null;
		for (int i = 0; i < fields.length; i++)
		{
			if (!fields[i].getName().equals("serialVersionUID")) {
				if (columns.get(fields[i].getName()).equals(
						columns.get("tableId"))) {
					entityId = fields[i].getName();
				} else {
					sqlSb.append(columns.get(fields[i].getName())).append(",");
					wenhao.append("?").append(",");
				}
			}
		}
		// 去掉最后一个逗号。
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//属性类型
			String fieldType = null;
			//属性名大写
			String fieldNameUpper = null;
			Method entityMethod = null;
			//属性值
			Object value = null;
			int psIndex = 0;
			for (int i = 0; i < fields.length; i++)
			{
				if (!fields[i].getName().equals("serialVersionUID")) {
					String fieldName = fields[i].getName();
					if (!fieldName.equals(entityId)) {
						psIndex++;
						fieldNameUpper = Character.toUpperCase(fieldName
								.charAt(0))
								+ fieldName.substring(1);
						entityMethod = entityClass.getMethod("get"
								+ fieldNameUpper);
						//获取变量的值
						value = entityMethod.invoke(object);
						// 获取变量的类型
						fieldType = fields[i].getGenericType().toString();
						//是否是日期类型
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						//填充value
						if (value != null && isDateType) {
							ps.setTimestamp(psIndex, Timestamp.valueOf(value
									.toString()));
						}else if(value == null){
							ps.setNull(psIndex, Types.VARCHAR);
						}else {
							ps.setObject(psIndex, value);
						}
					}
				}
			}
			// 采用prepareStatement方法执行SQL语句。
			int count = ps.executeUpdate();
			if (count == 1)
			{
				ps = conn.prepareStatement("values IDENTITY_VAL_LOCAL()");
				//执行SQL
				rs = ps.executeQuery();
				if (rs.next())
				{
					returnID = rs.getLong(1);
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			//关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnID;
	}
	
	private Long saveObjectReturnIDMySql(Object object) throws Exception
	{
		//插入后返回的主键id
		Long returnID = 0L;
		Class<?> entityClass = object.getClass();
		// 获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
		// 拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(tableName)
				.append("(");
		StringBuffer wenhao = new StringBuffer(") values(");
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		String entityId = null;
		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].getName().equals("serialVersionUID")) {
				if (columns.get(fields[i].getName()).equals(
						columns.get("tableId"))) {
					entityId = fields[i].getName();
				} else {
					sqlSb.append(columns.get(fields[i].getName())).append(",");
					wenhao.append("?").append(",");
				}
			}
		}
		// 去掉最后一个逗号。
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//属性类型
			String fieldType = null;
			//属性名大写
			String fieldNameUpper = null;
			Method entityMethod = null;
			//属性值
			Object value = null;
			int psIndex = 0;
			for (int i = 0; i < fields.length; i++)
			{
				if (!fields[i].getName().equals("serialVersionUID")) {
					String fieldName = fields[i].getName();
					if (!fieldName.equals(entityId)) {
						psIndex++;
						fieldNameUpper = Character.toUpperCase(fieldName
								.charAt(0))
								+ fieldName.substring(1);
						entityMethod = entityClass.getMethod("get"
								+ fieldNameUpper);
						//获取属性值
						value = entityMethod.invoke(object);
						// 获取变量的类型
						fieldType = fields[i].getGenericType().toString();
						//是否是日期类型
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						//填充value
						if (value != null && isDateType) {
							ps.setTimestamp(psIndex, Timestamp.valueOf(value
									.toString()));
						} else {
							ps.setObject(psIndex, value);
						}
					}
				}
			}
			// 执行SQL语句。
			int count = ps.executeUpdate();
			if (count == 1)
			{
				ps = conn.prepareStatement("select last_insert_id()");
				//执行SQL
				rs = ps.executeQuery();
				if (rs.next())
				{
					returnID = rs.getLong(1);
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			//关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnID;
	}
	public <T> List<T> findPageListByCondition(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			//oracle数据库
			return this.findPageListByConditionOracle(loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
	   	} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
	   	//sqlserver数据库
	   		return this.findPageListByConditionSqlServer(loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//db2数据库
			return this.findPageListByConditionDB2(loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			//mysql数据库
			return this.findPageListByConditionMySql(loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
		}
		return null;
	}
	
	public <T> List<T> findPageListByCondition(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
	   	//sqlserver数据库
	   	return this.findPageListByConditionSqlServer(conn,isOutConn,loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
	}
	
	private <T> List<T> findPageListByConditionOracle(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<T> returnList = null;
		Iterator<Map.Entry<String, String>> iter = null;
		Map.Entry<String, String> e = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("select * from ").append(
				tableName);
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName);
		//是否添加and
		boolean isAddAnd=false;
		//拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			while (iter.hasNext())
			{
				e = iter.next();
				
				if (!"".equals(e.getValue()))
				{
					columnName = columns.get(e.getKey());
					if(isAddAnd){
					lastSqlSb.append(" and ").append(columnName).append("=?");
					countSqlSb.append(" and ").append(columnName).append("=?");
					}else{
						lastSqlSb.append(" where ").append(columnName).append("=?");
						countSqlSb.append(" where ").append(columnName).append("=?");
						isAddAnd=true;
					}
				}
			}
		}
		//加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		//拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
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
			}
			//执行sql
			rs = ps.executeQuery();
			if (rs.next())
			{
				//当前页数
				int pageSize = pageInfo.getPageSize();
				//记录总条数
				int totalCount = rs.getInt("totalcount");
				//总页数
				int totalPage = totalCount % pageSize == 0 ? totalCount
						/ pageSize : totalCount / pageSize + 1;
				pageInfo.setTotalRec(totalCount);
				pageInfo.setTotalPage(totalPage);
				//如果当前页数大于最大页数，则跳转到第一页
				if (pageInfo.getPageIndex() > totalPage)
				{
					pageInfo.setPageIndex(1);
				}
			}
			//开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			//结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			String sql = new StringBuffer(
					"select * from (select t.*, rownum rn from (").append(
					lastSql).append(") t where rownum<=").append(endCount)
					.append(") where rn>=").append(beginCount).toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//拼接条件sql
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
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
			}
			//执行SQL
			rs = ps.executeQuery();
			//RESULTSET转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			//关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnList;
	}

	private <T> List<T> findPageListByConditionSqlServer(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<T> returnList = null;
		Iterator<Map.Entry<String, String>> iter = null;
		Map.Entry<String, String> e = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		// 获取实体类的表名保存于tableName字符串内。
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("* from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		//是否添加and
		boolean isAddAnd=false;
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			while (iter.hasNext())
			{
				e = iter.next();
				// 如果查询条件的值为空字符串，则过滤该条件。
				if (!"".equals(e.getValue()))
				{
					columnName = columns.get(e.getKey());
					if(isAddAnd){
					lastSqlSb.append(" and ").append(columnName).append("=?");
					countSqlSb.append(" and ").append(columnName).append("=?");
					}else{
						lastSqlSb.append(" where ").append(columnName).append("=?");
						countSqlSb.append(" where ").append(columnName).append("=?");
						isAddAnd=true;
					}
				}
			}
		}
		// 加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			// 去掉最后一个逗号。
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			// 设置查询条件
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
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
			}
			// 执行SQL语句。
			rs = ps.executeQuery();
			if (rs.next())
			{
				//当前页数
				int pageSize = pageInfo.getPageSize();
				//总记录数
				int totalCount = rs.getInt("totalcount");
				//总页数
				int totalPage = totalCount % pageSize == 0 ? totalCount
						/ pageSize : totalCount / pageSize + 1;
				pageInfo.setTotalRec(totalCount);
				pageInfo.setTotalPage(totalPage);
				// 当前页大于总页数则跳转到第一页
				if (pageInfo.getPageIndex() > totalPage)
				{
					pageInfo.setPageIndex(1);
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			lastSql = "select top " + endCount + " 0 as tempColumn," + lastSql;
			String sql = new StringBuffer(
					"select * from (select row_number() over(order by tempColumn) tempRowNumber,* from (")
					.append(lastSql).append(
							") t) tt where tempRowNumber>=" + beginCount)
					.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			// 设置查询条件
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
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
			}
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnList;
	}
	
	private <T> List<T> findPageListByConditionSqlServer(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return null;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return null;
		}
		
		List<T> returnList = null;
		Iterator<Map.Entry<String, String>> iter = null;
		Map.Entry<String, String> e = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		// 获取实体类的表名保存于tableName字符串内。
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("* from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		//是否添加and
		boolean isAddAnd=false;
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			while (iter.hasNext())
			{
				e = iter.next();
				// 如果查询条件的值为空字符串，则过滤该条件。
				if (!"".equals(e.getValue()))
				{
					columnName = columns.get(e.getKey());
					if(isAddAnd){
					lastSqlSb.append(" and ").append(columnName).append("=?");
					countSqlSb.append(" and ").append(columnName).append("=?");
					}else{
						lastSqlSb.append(" where ").append(columnName).append("=?");
						countSqlSb.append(" where ").append(columnName).append("=?");
						isAddAnd=true;
					}
				}
			}
		}
		// 加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			// 去掉最后一个逗号。
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			// 设置查询条件
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
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
			}
			// 执行SQL语句。
			rs = ps.executeQuery();
			if (rs.next())
			{
				//当前页数
				int pageSize = pageInfo.getPageSize();
				//总记录数
				int totalCount = rs.getInt("totalcount");
				//总页数
				int totalPage = totalCount % pageSize == 0 ? totalCount
						/ pageSize : totalCount / pageSize + 1;
				pageInfo.setTotalRec(totalCount);
				pageInfo.setTotalPage(totalPage);
				// 当前页大于总页数则跳转到第一页
				if (pageInfo.getPageIndex() > totalPage)
				{
					pageInfo.setPageIndex(1);
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			lastSql = "select top " + endCount + " 0 as tempColumn," + lastSql;
			String sql = new StringBuffer(
					"select * from (select row_number() over(order by tempColumn) tempRowNumber,* from (")
					.append(lastSql).append(
							") t) tt where tempRowNumber>=" + beginCount)
					.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			// 设置查询条件
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
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
			}
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(rs, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(rs, ps,conn);
			}
		}
		return returnList;
	}
	
	private <T> List<T> findPageListByConditionDB2(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<T> returnList = null;
		Iterator<Map.Entry<String, String>> iter = null;
		Map.Entry<String, String> e = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("select * from ").append(
				tableName);
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName);
		//是否添加and
		boolean isAddAnd=false;
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			while (iter.hasNext())
			{
				e = iter.next();
				// 如果查询条件的值为空字符串，则过滤该条件
				if (!"".equals(e.getValue()))
				{
					columnName = columns.get(e.getKey());
					if(isAddAnd){
					lastSqlSb.append(" and ").append(columnName).append("=?");
					countSqlSb.append(" and ").append(columnName).append("=?");
					}else{
						lastSqlSb.append(" where ").append(columnName).append("=?");
						countSqlSb.append(" where ").append(columnName).append("=?");
						isAddAnd=true;
					}
				}
			}
		}
		// 加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			// 去掉最后一个逗号。
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			// 设置查询条件
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
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
			}
			// 执行SQL语句。
			rs = ps.executeQuery();
			if (rs.next())
			{
				//当前页数
				int pageSize = pageInfo.getPageSize();
				//总记录数
				int totalCount = rs.getInt("totalcount");
				//总页数
				int totalPage = totalCount % pageSize == 0 ? totalCount
						/ pageSize : totalCount / pageSize + 1;
				pageInfo.setTotalRec(totalCount);
				pageInfo.setTotalPage(totalPage);
				// 当前页大于总页数则跳转到第一页
				if (pageInfo.getPageIndex() > totalPage)
				{
					pageInfo.setPageIndex(1);
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			String sql = new StringBuffer("select * from (select row_number() over() as rownum ,t.* from (").
			 append(lastSql).append(")t ) temp_t where rownum between ")
			.append(beginCount).append(" and ").append(endCount).toString();
			
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			// 设置查询条件
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
			{
				int psIndex = 0;
				iter = conditionMap.entrySet().iterator();
				while (iter.hasNext())
				{
					e = iter.next();
					// 如果查询条件的值为空字符串，则过滤该条件。
					if (!"".equals(e.getValue()))
					{
						psIndex++;
						ps.setString(psIndex, e.getValue());
					}
				}
			}
			//执行SQL
			rs = ps.executeQuery();
			//ResultSet转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnList;
	}

	private <T> List<T> findPageListByConditionMySql(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<T> returnList = null;
		Iterator<Map.Entry<String, String>> iter = null;
		Map.Entry<String, String> e = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		// 表名
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("select * from ").append(
				tableName);
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName);
		//是否添加and
		boolean isAddAnd=false;
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			while (iter.hasNext())
			{
				e = iter.next();
				// 如果查询条件的值为空字符串，则过滤该条件。
				if (!"".equals(e.getValue()))
				{
					columnName = columns.get(e.getKey());
					if(isAddAnd){
					lastSqlSb.append(" and ").append(columnName).append("=?");
					countSqlSb.append(" and ").append(columnName).append("=?");
					}else{
						lastSqlSb.append(" where ").append(columnName).append("=?");
						countSqlSb.append(" where ").append(columnName).append("=?");
						isAddAnd=true;
					}
				}
			}
		}
		// 加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			// 去掉最后一个逗号。
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			// 设置查询条件
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
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
			}
			// 执行SQL语句。
			rs = ps.executeQuery();
			if (rs.next())
			{
				//当前页数
				int pageSize = pageInfo.getPageSize();
				//总记录数
				int totalCount = rs.getInt("totalcount");
				//总页数
				int totalPage = totalCount % pageSize == 0 ? totalCount
						/ pageSize : totalCount / pageSize + 1;
				pageInfo.setTotalRec(totalCount);
				pageInfo.setTotalPage(totalPage);
				// 当前页大于总页数则跳转到第一页
				if (pageInfo.getPageIndex() > totalPage)
				{
					pageInfo.setPageIndex(1);
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1)-1;
			// 结束行数
			int endCount = pageInfo.getPageSize() ;
			String sql = new StringBuffer(lastSql).append(" limit ").append(
					beginCount).append(",").append(endCount).toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			// 设置查询条件
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
			{
				int psIndex = 0;
				iter = conditionMap.entrySet().iterator();
				while (iter.hasNext())
				{
					e = iter.next();
					// 如果查询条件的值为空字符串，则过滤该条件。
					if (!"".equals(e.getValue()))
					{
						psIndex++;
						ps.setString(psIndex, e.getValue());
					}
				}
			}
			// 执行SQL语句。
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnList;
	}
	public <T> List<T> findPageListBySymbolsCondition(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			//oracle数据库
			return this.findPageListBySymbolsConditionOracle(loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
	   	} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
	   		//sqlserver数据库
	   		return this.findPageListBySymbolsConditionSqlServer(loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//db2数据库
			return this.findPageListBySymbolsConditionDB2(loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			//mysql数据库
			return this.findPageListBySymbolsConditionMySql(loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
		}
		return null;
	}
	
	public <T> List<T> findPageListBySymbolsCondition(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
	   	//sqlserver数据库
	   	return this.findPageListBySymbolsConditionSqlServer(conn,isOutConn,loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
	}
	
	public <T> List<T> findPageListBySymbolsConditionNoCount(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
	   	//sqlserver数据库
	   	return this.findPageListBySymbolsConditionNoCountSqlServer(loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
	}
	
	public <T> List<T> findPageListBySymbolsConditionNoCount(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
	   	//sqlserver数据库
	   	return this.findPageListBySymbolsConditionNoCountSqlServer(conn,isOutConn,loginUserID, entityClass, conditionMap, orderbyMap, pageInfo);
	}
	
	private <T> List<T> findPageListBySymbolsConditionOracle(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		// 表名
		String tableName = columns.get(entityClass.getSimpleName());
		//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("select * from ").append(
				tableName);
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName);
		//是否添加and
		boolean isAddAnd=false;
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			//获取实体类属性
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			//属性名
			String field = null;
			//属性值
			String fieldValue = null;
			//操作符
			String symbols = null;
			//属性类型
			String fieldType = null;
			//查询条件
			StringBuffer conditionSql = new StringBuffer();
			Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
					.iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				fieldValue = e.getValue();
				// 如果查询条件的值为空字符串，则过滤该条件。
				if (!"".equals(fieldValue))
				{
					//分离操作符
					if (e.getKey().contains("&"))
					{
						field = e.getKey().split("&")[0];
						symbols = e.getKey().split("&")[1];
					} else
					{
						field = e.getKey();
						
						symbols = "=";
					}
					for (int index = 0; index < fields.length; index++)
					{
						if (fields[index].getName().equals(field))
						{
							fieldType = fields[index].getGenericType()
									.toString();
							break;
						}
					}
					//拼接查询条件
					if(isAddAnd){
					conditionSql.append(" and ").append(columns.get(field));
					}else{
						conditionSql.append(" where ").append(columns.get(field));
						isAddAnd=true;
					}
					
					if (symbols.equalsIgnoreCase(StaticValue.LIKE1)
							|| symbols.equalsIgnoreCase(StaticValue.LIKE2))
					{
						conditionSql.append(" like ");
					} else
					{
						conditionSql.append(" ").append(symbols).append(" ");
					}
					
					if (symbols.equalsIgnoreCase(StaticValue.IN)
							|| symbols.equalsIgnoreCase(StaticValue.NOT_IN))
					{
						String[] valueArray = fieldValue.split(",");
						StringBuffer valueSb = new StringBuffer("(");
						for (int i = 0; i < valueArray.length; i++)
						{
							valueSb.append("'").append(valueArray[i]).append(
									"',");
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
							conditionSql.append("to_date('").append(fieldValue)
									.append("','yyyy-MM-dd HH24:mi:ss')");
						} else
						{
							conditionSql.append("'").append(fieldValue).append(
									"'");
						}
					} else if (symbols.equalsIgnoreCase(StaticValue.BETWEEN))
					{
						String[] valueArray = fieldValue.split(",");
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							conditionSql.append("to_date('").append(
									valueArray[0]).append(
									"','yyyy-MM-dd HH24:mi:ss') and to_date('")
									.append(valueArray[1]).append(
											"','yyyy-MM-dd HH24:mi:ss')");
						} else
						{
							conditionSql.append(valueArray[0]).append(" and ")
									.append(valueArray[1]);
						}
					}
				}
			}
			
			lastSqlSb.append(conditionSql);
			countSqlSb.append(conditionSql);
		}
		// 加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			// 执行SQL语句。
			rs = ps.executeQuery();
			if (rs.next())
			{
				//当前页数
				int pageSize = pageInfo.getPageSize();
				//总记录数
				int totalCount = rs.getInt("totalcount");
				//总页数
				int totalPage = totalCount % pageSize == 0 ? totalCount
						/ pageSize : totalCount / pageSize + 1;
				pageInfo.setTotalRec(totalCount);
				pageInfo.setTotalPage(totalPage);
				// 当前页大于总页数则跳转到第一页
				if (pageInfo.getPageIndex() > totalPage)
				{
					pageInfo.setPageIndex(1);
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			String sql = new StringBuffer(
					"select * from (select t.*, rownum rn from (").append(
					lastSql).append(") t where rownum<=").append(endCount)
					.append(") where rn>=").append(beginCount).toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnList;
	}

	private <T> List<T> findPageListBySymbolsConditionSqlServer(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		// 表名
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("* from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		//是否添加and
		boolean isAddAnd=false;
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			//获取实体类属性
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			// 实体类变量
			String field = null;
			// 查询的条件值
			String fieldValue = null;
			// 操作符
			String symbols = null;
			// 实体类变量的类型
			String fieldType = null;
			// 查询条件
			StringBuffer conditionSql = new StringBuffer();
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
					if(isAddAnd){
					conditionSql.append(" and ").append(columns.get(field));
					}else{
						conditionSql.append(" where ").append(columns.get(field));
						isAddAnd=true;
					}
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
							valueSb.append("'").append(valueArray[i]).append(
									"',");
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

                        conditionSql.append("'").append(fieldValue).append(
                                "'");

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
			// 添加查询条件
			lastSqlSb.append(conditionSql);
			countSqlSb.append(conditionSql);
		}
		// 加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			// 去掉最后一个逗号。
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			// 执行SQL语句。
			rs = ps.executeQuery();
			if (rs.next())
			{
				//当前页数
				int pageSize = pageInfo.getPageSize();
				//总记录数
				int totalCount = rs.getInt("totalcount");
				//总页数
				int totalPage = totalCount % pageSize == 0 ? totalCount
						/ pageSize : totalCount / pageSize + 1;
				pageInfo.setTotalRec(totalCount);
				pageInfo.setTotalPage(totalPage);
				// 当前页大于总页数则跳转到第一页
				if (pageInfo.getPageIndex() > totalPage)
				{
					pageInfo.setPageIndex(1);
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			lastSql = "select top " + endCount + " 0 as tempColumn," + lastSql;
			String sql = new StringBuffer(
					"select * from (select row_number() over(order by tempColumn) tempRowNumber,* from (")
					.append(lastSql).append(
							") t) tt where tempRowNumber>=" + beginCount)
					.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnList;
	}
	
	private <T> List<T> findPageListBySymbolsConditionSqlServer(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return null;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return null;
		}
		
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		// 表名
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("* from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		//是否添加and
		boolean isAddAnd=false;
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			//获取实体类属性
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			// 实体类变量
			String field = null;
			// 查询的条件值
			String fieldValue = null;
			// 操作符
			String symbols = null;
			// 实体类变量的类型
			String fieldType = null;
			// 查询条件
			StringBuffer conditionSql = new StringBuffer();
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
					if(isAddAnd){
					conditionSql.append(" and ").append(columns.get(field));
					}else{
						conditionSql.append(" where ").append(columns.get(field));
						isAddAnd=true;
					}
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
							valueSb.append("'").append(valueArray[i]).append(
									"',");
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

                        conditionSql.append("'").append(fieldValue).append(
                                "'");

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
			// 添加查询条件
			lastSqlSb.append(conditionSql);
			countSqlSb.append(conditionSql);
		}
		// 加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			// 去掉最后一个逗号。
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			// 执行SQL语句。
			rs = ps.executeQuery();
			if (rs.next())
			{
				//当前页数
				int pageSize = pageInfo.getPageSize();
				//总记录数
				int totalCount = rs.getInt("totalcount");
				//总页数
				int totalPage = totalCount % pageSize == 0 ? totalCount
						/ pageSize : totalCount / pageSize + 1;
				pageInfo.setTotalRec(totalCount);
				pageInfo.setTotalPage(totalPage);
				// 当前页大于总页数则跳转到第一页
				if (pageInfo.getPageIndex() > totalPage)
				{
					pageInfo.setPageIndex(1);
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			lastSql = "select top " + endCount + " 0 as tempColumn," + lastSql;
			String sql = new StringBuffer(
					"select * from (select row_number() over(order by tempColumn) tempRowNumber,* from (")
					.append(lastSql).append(
							") t) tt where tempRowNumber>=" + beginCount)
					.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(rs, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(rs, ps,conn);
			}
		}
		return returnList;
	}
	
	private <T> List<T> findPageListBySymbolsConditionNoCountSqlServer(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		// 表名
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("* from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		//是否添加and
		boolean isAddAnd=false;
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			//获取实体类属性
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			// 实体类变量
			String field = null;
			// 查询的条件值
			String fieldValue = null;
			// 操作符
			String symbols = null;
			// 实体类变量的类型
			String fieldType = null;
			// 查询条件
			StringBuffer conditionSql = new StringBuffer();
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
					if(isAddAnd){
					conditionSql.append(" and ").append(columns.get(field));
					}else{
						conditionSql.append(" where ").append(columns.get(field));
						isAddAnd=true;
					}
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
							valueSb.append("'").append(valueArray[i]).append(
									"',");
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

                        conditionSql.append("'").append(fieldValue).append(
                                "'");

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
			// 添加查询条件
			lastSqlSb.append(conditionSql);
			countSqlSb.append(conditionSql);
		}
		// 加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			// 去掉最后一个逗号。
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//第一页才查总数
			if (countSql != null && pageInfo.getPageIndex() == 1)
			{
				EmpExecutionContext.sql("execute sql : " + countSql);
				ps = conn.prepareStatement(countSql);
				// 执行SQL语句。
				rs = ps.executeQuery();
				if (rs.next())
				{
					//当前页数
					int pageSize = pageInfo.getPageSize();
					//总记录数
					int totalCount = rs.getInt("totalcount");
					//总页数
					int totalPage = totalCount % pageSize == 0 ? totalCount
							/ pageSize : totalCount / pageSize + 1;
					pageInfo.setTotalRec(totalCount);
					pageInfo.setTotalPage(totalPage);
					// 当前页大于总页数则跳转到第一页
					if (pageInfo.getPageIndex() > totalPage)
					{
						pageInfo.setPageIndex(1);
					}
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			lastSql = "select top " + endCount + " 0 as tempColumn," + lastSql;
			String sql = new StringBuffer(
					"select * from (select row_number() over(order by tempColumn) tempRowNumber,* from (")
					.append(lastSql).append(
							") t) tt where tempRowNumber>=" + beginCount)
					.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnList;
	}

	private <T> List<T> findPageListBySymbolsConditionNoCountSqlServer(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return null;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return null;
		}
		
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		// 表名
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("* from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName).append(StaticValue.getWITHNOLOCK());
		//是否添加and
		boolean isAddAnd=false;
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			//获取实体类属性
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			// 实体类变量
			String field = null;
			// 查询的条件值
			String fieldValue = null;
			// 操作符
			String symbols = null;
			// 实体类变量的类型
			String fieldType = null;
			// 查询条件
			StringBuffer conditionSql = new StringBuffer();
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
					if(isAddAnd){
					conditionSql.append(" and ").append(columns.get(field));
					}else{
						conditionSql.append(" where ").append(columns.get(field));
						isAddAnd=true;
					}
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
							valueSb.append("'").append(valueArray[i]).append(
									"',");
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

                        conditionSql.append("'").append(fieldValue).append(
                                "'");

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
			// 添加查询条件
			lastSqlSb.append(conditionSql);
			countSqlSb.append(conditionSql);
		}
		// 加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			// 去掉最后一个逗号。
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			//第一页才查总数
			if (countSql != null && pageInfo.getPageIndex() == 1)
			{
				EmpExecutionContext.sql("execute sql : " + countSql);
				ps = conn.prepareStatement(countSql);
				// 执行SQL语句。
				rs = ps.executeQuery();
				if (rs.next())
				{
					//当前页数
					int pageSize = pageInfo.getPageSize();
					//总记录数
					int totalCount = rs.getInt("totalcount");
					//总页数
					int totalPage = totalCount % pageSize == 0 ? totalCount
							/ pageSize : totalCount / pageSize + 1;
					pageInfo.setTotalRec(totalCount);
					pageInfo.setTotalPage(totalPage);
					// 当前页大于总页数则跳转到第一页
					if (pageInfo.getPageIndex() > totalPage)
					{
						pageInfo.setPageIndex(1);
					}
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			lastSql = "select top " + endCount + " 0 as tempColumn," + lastSql;
			String sql = new StringBuffer(
					"select * from (select row_number() over(order by tempColumn) tempRowNumber,* from (")
					.append(lastSql).append(
							") t) tt where tempRowNumber>=" + beginCount)
					.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(rs, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(rs, ps,conn);
			}
		}
		return returnList;
	}
	
	private <T> List<T> findPageListBySymbolsConditionDB2(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		// 表名
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("select * from ").append(tableName);
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName);
		//是否添加and
		boolean isAddAnd=false;
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			//获取实体类属性名
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			// 实体类变量
			String field = null;
			// 查询的条件值
			String fieldValue = null;
			// 操作符
			String symbols = null;
			// 实体类变量的类型
			String fieldType = null;
			// 查询条件
			StringBuffer conditionSql = new StringBuffer();
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
					if(isAddAnd){
					conditionSql.append(" and ").append(columns.get(field));
					}else{
						conditionSql.append(" where ").append(columns.get(field));
						isAddAnd=true;
					}
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
						if(fieldType.equals("class java.lang.String")){
							for (int i = 0; i < valueArray.length; i++)
							{
								valueSb.append("'").append(valueArray[i]).append(
										"',");
							}
						}else{
							for (int i = 0; i < valueArray.length; i++)
							{
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
						} else if(fieldType.equals("class java.lang.String"))
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
			// 添加查询条件
			lastSqlSb.append(conditionSql);
			countSqlSb.append(conditionSql);
		}
		// 加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			// 去掉最后一个逗号。
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			// 执行SQL语句。
			rs = ps.executeQuery();
			if (rs.next())
			{
				//当前页数
				int pageSize = pageInfo.getPageSize();
				//总记录数
				int totalCount = rs.getInt("totalcount");
				//总页数
				int totalPage = totalCount % pageSize == 0 ? totalCount
						/ pageSize : totalCount / pageSize + 1;
				pageInfo.setTotalRec(totalCount);
				pageInfo.setTotalPage(totalPage);
				// 当前页大于总页数则跳转到第一页
				if (pageInfo.getPageIndex() > totalPage)
				{
					pageInfo.setPageIndex(1);
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			String sql = new StringBuffer(
					"select * from(select row_number() over() as rownum ,t.* from (")
					.append(lastSql).append(")t ) temp_t where rownum between ")
					.append(beginCount).append(" and ").append(endCount)
					.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			//关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnList;
	}

	private <T> List<T> findPageListBySymbolsConditionMySql(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		// 表名
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接查询的SQL语句
		StringBuffer lastSqlSb = new StringBuffer("select * from ").append(
				tableName);
		StringBuffer countSqlSb = new StringBuffer(
				"select count(*) totalcount from ").append(tableName);
		//是否添加and
		boolean isAddAnd=false;
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			// 实体类变量
			String field = null;
			// 查询的条件值
			String fieldValue = null;
			// 操作符
			String symbols = null;
			// 实体类变量的类型
			String fieldType = null;
			// 查询条件
			StringBuffer conditionSql = new StringBuffer();
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
					if(isAddAnd){
					conditionSql.append(" and ").append(columns.get(field));
					}else{
						conditionSql.append(" where ").append(columns.get(field));
						isAddAnd=true;
					}
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
							valueSb.append("'").append(valueArray[i]).append(
									"',");
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

                        conditionSql.append("'").append(fieldValue).append(
							"'");

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
			// 添加查询条件
			lastSqlSb.append(conditionSql);
			countSqlSb.append(conditionSql);
		}
		// 加上数据权限
		if (loginUserID != null)
		{
			String domDepSql = new StringBuffer("select ").append(
					TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(" where ").append(
					TableLfDomination.USER_ID).append("=").append(loginUserID)
					.toString();
			String sysuserSql = new StringBuffer("select ").append(
					TableLfSysuser.USER_ID).append(" from ").append(
					TableLfSysuser.TABLE_NAME).append(" where ").append(
					TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
					.append(")").toString();
			if(isAddAnd){
			String dom = new StringBuffer(" and (").append(
					columns.get("userId")).append(" in (").append(sysuserSql)
					.append(") or ").append(columns.get("userId")).append("=")
					.append(loginUserID).append(")").toString();
			lastSqlSb.append(dom);
			countSqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				lastSqlSb.append(dom);
				countSqlSb.append(dom);
				isAddAnd=true;
			}
		}
		// 拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			lastSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
			while (iter.hasNext())
			{
				e = iter.next();
				lastSqlSb.append(columns.get(e.getKey())).append(" ").append(
						e.getValue()).append(",");
			}
			// 去掉最后一个逗号。
			lastSqlSb.deleteCharAt(lastSqlSb.lastIndexOf(","));
		}
		String lastSql = lastSqlSb.toString();
		String countSql = countSqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			// 执行SQL语句。
			rs = ps.executeQuery();
			if (rs.next())
			{
				//当前页数
				int pageSize = pageInfo.getPageSize();
				//总记录数
				int totalCount = rs.getInt("totalcount");
				//总页数
				int totalPage = totalCount % pageSize == 0 ? totalCount
						/ pageSize : totalCount / pageSize + 1;
				pageInfo.setTotalRec(totalCount);
				pageInfo.setTotalPage(totalPage);
				// 当前页大于总页数则跳转到第一页
				if (pageInfo.getPageIndex() > totalPage)
				{
					pageInfo.setPageIndex(1);
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1)-1;
			// 结束行数
			int endCount = pageInfo.getPageSize() ;
			String sql = new StringBuffer(lastSql).append(" limit ").append(
					beginCount).append(",").append(endCount).toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			super.close(rs, ps, conn);
		}
		return returnList;
	}
	
	/**
	 * 公共实体类list查询方法
	 * @param <T>
	 * @param entityClass 
	 * @param conditionMap 条件
	 * @param columName 列名
	 * @param orderbyMap 排序
	 * @return
	 * @throws Exception 异常
	 */
	public <T> List<String[]> findListByConditionByColumName(Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,String columName,
			LinkedHashMap<String, String> orderbyMap) throws Exception
	{
		List<String[]> returnList = new ArrayList<String[]>();
		Iterator<Map.Entry<String, String>> iter = null;
		Map.Entry<String, String> e = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		
	//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select ").append(columName).append(" from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK());
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			/*iter = conditionMap.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				if (!"".equals(e.getValue()))
				{
					sqlSb.append(" and ").append(columns.get(e.getKey()))
							.append("=?");
				}
			}*/
			sqlSb.append(" where ");
			Field[] fields = entityClass.getDeclaredFields();
			StringBuffer conditionSql = new StringBuffer();
			appendCondition(conditionMap,fields,conditionSql,columns);
			// 添加查询条件
			sqlSb.append(conditionSql);
		}
		//拼接排序条件
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
			sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		}
		String sql = sqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			
			ps = conn.prepareStatement(sql);
			/*if (conditionMap != null && !conditionMap.entrySet().isEmpty())
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
			//执行SQL
			rs = ps.executeQuery();
			String[] columArray = columName.split(",");
			
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
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			//关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnList;
	}
	
	
	/**
	 * 公共实体类list查询方法
	 * @param <T>
	 * @param entityClass 
	 * @param conditionMap 条件
	 * @param columName 列名
	 * @param orderbyMap 排序
	 * @return
	 * @throws Exception 异常
	 */
	public <T> List<String[]> findListByConditionByColumName(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,String columName,
			LinkedHashMap<String, String> orderbyMap) throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return null;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return null;
		}
		
		List<String[]> returnList = new ArrayList<String[]>();
		Iterator<Map.Entry<String, String>> iter = null;
		Map.Entry<String, String> e = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		
	//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select ").append(columName).append(" from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK());
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			/*iter = conditionMap.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				if (!"".equals(e.getValue()))
				{
					sqlSb.append(" and ").append(columns.get(e.getKey()))
							.append("=?");
				}
			}*/
			sqlSb.append(" where ");
			Field[] fields = entityClass.getDeclaredFields();
			StringBuffer conditionSql = new StringBuffer();
			appendCondition(conditionMap,fields,conditionSql,columns);
			// 添加查询条件
			sqlSb.append(conditionSql);
		}
		//拼接排序条件
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
			sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		}
		String sql = sqlSb.toString();
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			
			ps = conn.prepareStatement(sql);
			/*if (conditionMap != null && !conditionMap.entrySet().isEmpty())
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
			//执行SQL
			rs = ps.executeQuery();
			String[] columArray = columName.split(",");
			
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
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(rs, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(rs, ps,conn);
			}
		}
		return returnList;
	}

	
	public Long saveObjectReturnIDWithTri(Object object) throws Exception
	{
		//插入后返回的主键ID
		Long returnID = 0L;
		Class<?> entityClass = object.getClass();
		//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
		//  拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(tableName)
				.append("(");
		StringBuffer wenhao = new StringBuffer(") values(");
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		String entityId = null;
		for (int i = 0; i < fields.length; i++)
		{
			if (!fields[i].getName().equals("serialVersionUID")) {
				if (columns.get(fields[i].getName()).equals(
						columns.get("tableId"))) {
					entityId = fields[i].getName();
				} else {
					sqlSb.append(columns.get(fields[i].getName())).append(",");
					wenhao.append("?").append(",");
				}
			}
		}
		// 去掉最后一个逗号。
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//属性类型
			String fieldType = null;
			//属性名大写
			String fieldNameUpper = null;
			Method entityMethod = null;
			//属性值
			Object value = null;
			int psIndex = 0;
			for (int i = 0; i < fields.length; i++)
			{
				if (!fields[i].getName().equals("serialVersionUID")) {
					String fieldName = fields[i].getName();
					if (!fieldName.equals(entityId)) {
						psIndex++;
						fieldNameUpper = Character.toUpperCase(fieldName
								.charAt(0))
								+ fieldName.substring(1);
						entityMethod = entityClass.getMethod("get"
								+ fieldNameUpper);
						//获取属性值
						value = entityMethod.invoke(object);
						// 获取变量的类型
						fieldType = fields[i].getGenericType().toString();
						//是否是日期类型
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						//填充value
						if (value != null && isDateType) {
							ps.setTimestamp(psIndex, Timestamp.valueOf(value
									.toString()));
						} else {
							ps.setObject(psIndex, value);
						}
					}
				}
			}
			// 采用prepareStatement方法执行SQL语句。
			int count = ps.executeUpdate();
			if (count == 1)
			{
				ps = conn.prepareStatement("select IDENT_CURRENT('" + tableName
						+ "')");
				//执行SQL
				rs = ps.executeQuery();
				if (rs.next())
				{
					returnID = rs.getLong(1);
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			//关闭数据库资源
			super.close(rs, ps, conn);
		}
		return returnID;
	}

	/**
	 * 通过表名，aiid查询
	 * @param conn    isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn    false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param tableName
	 * @param aiId
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> findMapByTableCondition(Connection conn,boolean isOutConn,String tableName,Long aiId) throws Exception{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return null;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return null;
		}
		
		
		//拼接SQL
		String sql = new StringBuffer("select tb.* from ").append(tableName)
				.append(" tb ").append(" where ").append("tb.aiid=").append(
						aiId).toString();
		//获取数据库连接
		if(conn==null)
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		}
		EmpExecutionContext.sql("execute sql : " + sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,String> map = null;
		try {
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为map集合
			map = partrsToMap(rs);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		}finally{
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(rs, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(rs, ps,conn);
			}
		}
		return map;
	}
}