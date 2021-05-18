package com.montnets.emp.common.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.exception.MethodNotImplmentedExecption;

/**
 * 
 * @project emp
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-16 上午10:52:45
 * @description
 */
public class GenericEmpTransactionDAO extends SuperDAO implements
		IEmpTransactionDAO
{

	//获取数据库连接
	public Connection getConnection()
	{
		Connection conn = connectionManager
				.getDBConnection(StaticValue.EMP_POOLNAME);
		return conn;
	}

	//开启事务
	public void beginTransaction(Connection conn) throws Exception
	{
		conn.setAutoCommit(false);
	}

	//提交事务
	public void commitTransaction(Connection conn) throws Exception
	{
		conn.commit();
	}

	//事务回滚
	public boolean rollBackTransaction(Connection conn)
	{
		try
		{
			if(conn != null){
				conn.rollback();
			}
			return true;
		} catch (SQLException e)
		{
			EmpExecutionContext.error(e, "回滚失败");
			return false;
		}
	}

	//关闭数据库连接
	public void closeConnection(Connection conn)
	{
		try
		{
			if (conn != null && !conn.isClosed())
			{
				conn.setAutoCommit(true);
				conn.close();
			}
		} catch (SQLException e)
		{
			EmpExecutionContext.error(e, "关闭数据库连接异常。");
		}
	}

	public boolean save(Connection conn, Object object) throws Exception
	{
		boolean isSuccess = false;
		Class<?> entityClass = object.getClass();
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
	//  拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(
				columns.get(entityClass.getSimpleName())).append("(");
		StringBuffer wenhao = new StringBuffer(") values(");
		String entityId = null;
		for (int i = 0; i < fields.length; i++)
		{
			if(!fields[i].getName().equals("serialVersionUID")){
			if (!columns.get(fields[i].getName())
					.equals(columns.get("tableId")))
			{
				sqlSb.append(columns.get(fields[i].getName())).append(",");
				wenhao.append("?").append(",");
			} else
			{
				entityId = fields[i].getName();
			}
			}
		}
		// 去掉最后一个逗号。
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		PreparedStatement ps = null;
		try
		{
			EmpExecutionContext.sql("execute sql : "+sql);
			ps = conn.prepareStatement(sql);
			//属性名
			String fieldName = null;
			//属性名大写
			String fieldNameUpper = null;
			//属性类型
			String fieldType = null;
			Method entityMethod = null;
			//属性值
			Object value = null;
			int psIndex = 0;
			for (int i = 0; i < fields.length; i++)
			{
				if(!fields[i].getName().equals("serialVersionUID")){
				fieldName = fields[i].getName();
				if (!fieldName.equals(entityId))
				{
					fieldNameUpper = Character.toUpperCase(fieldName.charAt(0))
							+ fieldName.substring(1);
					entityMethod = entityClass
							.getMethod("get" + fieldNameUpper);
					//通过反射获取变量的值
					value = entityMethod.invoke(object);
					// 获取变量的类型
					fieldType = fields[i].getGenericType().toString();
					//是否是日期类型
					boolean isDateType = fieldType.equals(StaticValue.TIMESTAMP)
							|| fieldType.equals(StaticValue.DATE_SQL)
							|| fieldType.equals(StaticValue.DATE_UTIL);
					psIndex++;
					//填充value
					if (value != null && isDateType)
					{
						ps.setTimestamp(psIndex, Timestamp.valueOf(value
								.toString()));
					} else if(value == null){
						ps.setNull(psIndex, Types.VARCHAR);
					}else
					{
						ps.setObject(psIndex, value);
					}
				}
				}
			}
			//执行sql
			int count = ps.executeUpdate();
			if (count == 1)
			{
				isSuccess = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			//关闭数据库资源
			super.close(null, ps);
		}
		return isSuccess;
	}

	public Long saveObjectReturnID(Connection conn, Object object)
			throws Exception
	{
		throw new MethodNotImplmentedExecption(" targer : " + getClass());
	}

	/**
	 * 保存记录到数据库并返回自增id，自增id由程序自增
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long saveObjProReturnID(Connection conn, Object object) 
	{
		
		//插入后返回的主键id
		Long returnID = 0L;
		PreparedStatement ps = null;
		try
		{
			
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
			
			//获取序列值
			Long sequenceValue = this.getIdByPro(Integer.valueOf(columns.get("sequence")), 1); //getSequenceNextValue(conn,columns.get("sequence"));
			
			//没取到自增时
			if (sequenceValue == null || sequenceValue == 0)
			{
				return null;
			}
			
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
						}else if(value == null){
							ps.setNull(psIndex, Types.VARCHAR);
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
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "保存记录到数据库并返回自增id异常。");
			return null;
		} finally
		{
			// 关闭数据库资源
			try
			{
				super.close(null, ps, null);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源异常。");
			}
		}
		return returnID;
	
	}

	
	public <T> boolean delete(Connection conn, Class<T> entityClass)
			throws Exception
	{
		boolean isSuccess = false;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接删除的SQL语句
		String sql = new StringBuffer("delete from ").append(
				columns.get(entityClass.getSimpleName())).toString();
		PreparedStatement ps = null;
		try
		{
			EmpExecutionContext.sql("execute sql : "+sql);
			ps = conn.prepareStatement(sql);
			//执行sql
			int count = ps.executeUpdate();
			if (count > 0)
			{
				isSuccess = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			//关闭数据库资源
			super.close(null, ps);
		}
		return isSuccess;
	}

	public <T> int delete(Connection conn, Class<T> entityClass, String ids)
			throws Exception
	{
		//删除记录数
		int deleteCount = 0;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接删除的SQL语句
		String sql = new StringBuffer("delete from ").append(
				columns.get(entityClass.getSimpleName())).append(" where ")
				.append(columns.get("tableId")).append(" in (").append(ids)
				.append(")").toString();
		PreparedStatement ps = null;
		try
		{
			EmpExecutionContext.sql("execute sql : "+sql);
			ps = conn.prepareStatement(sql);
			//执行sql
			deleteCount = ps.executeUpdate();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			//关闭数据库资源
			super.close(null, ps);
		}
		return deleteCount;
	}

	public <T> int delete(Connection conn, Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		//删除记录数
		int deleteCount = 0;
		//根据条件进行删除
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
		//  获取实体类字段与数据库字段映射的map集合
			Map<String, String> columns = getORMMap(entityClass);
			//拼接删除的sql
			StringBuffer sqlSb = new StringBuffer("delete from ").append(
					columns.get(entityClass.getSimpleName())).append(
					" where ");
			Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
			String[] valueArray = null;
			StringBuffer valueSb = null;
			//获取实体类的属性
			Field[] fields = entityClass.getDeclaredFields();
			// 实体类属性的类型
			String fieldType = null;
			//是否添加and
			boolean isAddAnd=false;
			while (iter.hasNext())
			{
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
				
				valueArray = e.getValue().split(",");
				valueSb = new StringBuffer();
				//不同数据库引号的兼容问题
				if (fieldType.equals("class java.lang.String")) {
					for (int i = 0; i < valueArray.length; i++) {
						valueSb.append("'").append(valueArray[i]).append("',");
					}
				} else {
					for (int i = 0; i < valueArray.length; i++) {
						valueSb.append(valueArray[i]).append(",");
					}
				}
				valueSb.deleteCharAt(valueSb.lastIndexOf(","));
				if(isAddAnd){
				sqlSb.append(" and ").append(columns.get(e.getKey())).append(
						" in (").append(valueSb).append(")");
				}else{
					sqlSb.append(" ").append(columns.get(e.getKey())).append(
						" in (").append(valueSb).append(")");
					isAddAnd=true;
				}
			}
			String sql = sqlSb.toString();
			PreparedStatement ps = null;
			try
			{
				EmpExecutionContext.sql("execute sql : "+sql);
				ps = conn.prepareStatement(sql);
				//执行sql
				deleteCount = ps.executeUpdate();
			} catch (Exception ex)
			{
				EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
				throw ex;
			} finally
			{
				//关闭数据库资源
				super.close(null, ps);
			}
		}
		return deleteCount;
	}

	public boolean update(Connection conn, Object object) throws Exception
	{
		boolean isSuccess = false;
		Class<?> entityClass = object.getClass();
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接修改的SQL语句
		StringBuffer sqlSb = new StringBuffer("update ").append(
				columns.get(entityClass.getSimpleName())).append(" set ");
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		LinkedHashMap<String, List<String>> updateMap = new LinkedHashMap<String, List<String>>();
		//实体类属性名
		String fieldName = "";
		String idValue = "";
		//实体类属性值
		Object value = "";
		Method entityMethod = null;
		for (int i = 0; i < fields.length; i++)
		{
			if(!fields[i].getName().equals("serialVersionUID")){
			fieldName = fields[i].getName();
			entityMethod = entityClass.getMethod("get"
					+ Character.toUpperCase(fieldName.charAt(0))
					+ fieldName.substring(1));
			//通过反射获取属性的值
			value = entityMethod.invoke(object);
			if (value != null)
			{
				if (columns.get(fieldName).equals(columns.get("tableId")))
				{
					idValue = value.toString();
				} else
				{
					List<String> valueAndtype = new ArrayList<String>();
					valueAndtype.add(value.toString());
					valueAndtype.add(fields[i].getGenericType().toString());
					updateMap.put(columns.get(fieldName), valueAndtype);
					sqlSb.append(columns.get(fieldName)).append("=?,");
				}
			}
			}
		}
		if (!updateMap.entrySet().isEmpty())
		{
			sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(" where ")
					.append(columns.get("tableId")).append("=")
					.append(idValue);
			String sql = sqlSb.toString();
			PreparedStatement ps = null;
			try
			{
				EmpExecutionContext.sql("execute sql : "+sql);
				ps = conn.prepareStatement(sql);
				Map.Entry<String, List<String>> e = null;
				List<String> valueAndType = null;
				String valueStr = "";
				String fieldType = "";
				int psIndex = 0;
				Iterator<Map.Entry<String, List<String>>> iter = updateMap
						.entrySet().iterator();
				while (iter.hasNext())
				{
					psIndex++;
					e = iter.next();
					valueAndType = e.getValue();
					valueStr = valueAndType.get(0);
					fieldType = valueAndType.get(1);
					if (fieldType.equals(StaticValue.TIMESTAMP)
							|| fieldType.equals(StaticValue.DATE_UTIL))
					{
						//时间类型
						ps.setTimestamp(psIndex, Timestamp.valueOf(valueStr));
					} else
					{
						ps.setString(psIndex, valueStr);
					}
				}
				//执行sql
				int count = ps.executeUpdate();
				if (count > 0)
				{
					isSuccess = true;
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
				throw e;
			} finally
			{
				//关闭数据库资源
				super.close(null, ps);
			}
		}
		return isSuccess;
	}

	public <T> boolean update(Connection conn, Class<T> entityClass,
			LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		boolean isSuccess = false;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接修改的SQL语句
		StringBuffer sqlSb = new StringBuffer("update ").append(
				columns.get(entityClass.getSimpleName())).append(" set ");
		Iterator<Map.Entry<String, String>> objIter = objectMap.entrySet()
				.iterator();
		Map.Entry<String, String> e = null;
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		String fieldType = null;
		while (objIter.hasNext())
		{
			e = objIter.next();
			
			for (int index = 0; index < fields.length; index++)
			{
				if (fields[index].getName().equals(e.getKey()))
				{
					fieldType = fields[index].getGenericType()
							.toString();
					break;
				}
			}
			//不同数据库引号问题的兼容
			if (e.getValue() != null
					&& fieldType.equals("class java.lang.String")) {
				sqlSb.append(columns.get(e.getKey())).append("='").append(
						e.getValue()).append("',");
			} else {
				sqlSb.append(columns.get(e.getKey())).append("=").append(
						e.getValue()).append(",");
			}
		}
		// 去掉最后一个逗号
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
					.iterator();
			String[] valueArray = null;
			StringBuffer valueSb = null;
			//是否添加and
			boolean isAddAnd=false;
			while (iter.hasNext())
			{
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
				
				valueArray = e.getValue().split(",");
				valueSb = new StringBuffer();
				//不同数据库引号问题的兼容
				if (fieldType.equals("class java.lang.String")) {
					for (int i = 0; i < valueArray.length; i++) {
						valueSb.append("'").append(valueArray[i]).append("',");
					}
				} else {
					for (int i = 0; i < valueArray.length; i++) {
						valueSb.append(valueArray[i]).append(",");
					}
				}
				valueSb.deleteCharAt(valueSb.lastIndexOf(","));
				if(isAddAnd){
				sqlSb.append(" and ").append(columns.get(e.getKey())).append(
						" in (").append(valueSb).append(")");
				}else{
					sqlSb.append(" where ").append(columns.get(e.getKey())).append(
					" in (").append(valueSb).append(")");
					isAddAnd=true;
				}
			}
		}
		String sql = sqlSb.toString();
		PreparedStatement ps = null;
		try
		{
			EmpExecutionContext.sql("execute sql : "+sql);
			ps = conn.prepareStatement(sql);
			//执行sql
			int count = ps.executeUpdate();
			if (count > 0)
			{
				isSuccess = true;
			}
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			super.close(null, ps);
		}
		return isSuccess;
	}

	public <T> boolean update(Connection conn, Class<T> entityClass,
			LinkedHashMap<String, Object> objectMap, String id)
			throws Exception
	{
		boolean isSuccess = false;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接修改的SQL语句
		StringBuffer sqlSb = new StringBuffer("update ").append(
				columns.get(entityClass.getSimpleName())).append(" set ");
		LinkedHashMap<String, List<Object>> updateMap = new LinkedHashMap<String, List<Object>>();
		Object value = null;
		if (objectMap != null && !objectMap.entrySet().isEmpty())
		{
			//获取实体类的属性
			Field[] fields = entityClass.getDeclaredFields();
			Iterator<Map.Entry<String, Object>> iter = objectMap.entrySet()
					.iterator();
			Map.Entry<String, Object> e = null;
			String key = null;
			String fieldName = null;
			while (iter.hasNext())
			{
				e = iter.next();
				key = e.getKey();
				value = e.getValue();
				for (int i = 0; i < fields.length; i++)
				{
					if(!fields[i].getName().equals("serialVersionUID")){
					fieldName = fields[i].getName();
					if (fieldName.equals(key))
					{
						List<Object> valueAndtype = new ArrayList<Object>();
						valueAndtype.add(value);
						valueAndtype.add(fields[i].getGenericType().toString());
						updateMap.put(columns.get(fieldName), valueAndtype);
						sqlSb.append(columns.get(fieldName)).append("=?,");
					}
					}
				}
			}
		}
		if (!updateMap.entrySet().isEmpty())
		{
			sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(" where ")
					.append(columns.get("tableId")).append("=").append(id);
			String sql = sqlSb.toString();
			PreparedStatement ps = null;
			try
			{
				EmpExecutionContext.sql("execute sql : "+sql);
				ps = conn.prepareStatement(sql);
				Map.Entry<String, List<Object>> e = null;
				List<Object> valueAndType = null;
				String fieldType = null;
				int psIndex = 0;
				Iterator<Map.Entry<String, List<Object>>> iter = updateMap
						.entrySet().iterator();
				while (iter.hasNext())
				{
					psIndex++;
					e = iter.next();
					valueAndType = e.getValue();
					value = valueAndType.get(0);
					//属性类型
					fieldType = String.valueOf(valueAndType.get(1));
					//是否是日期类型
					boolean isDateType = fieldType.equals(StaticValue.TIMESTAMP)
							|| fieldType.equals(StaticValue.DATE_SQL)
							|| fieldType.equals(StaticValue.DATE_UTIL);
					if (value != null && isDateType)
					{
						ps.setTimestamp(psIndex, Timestamp.valueOf(String
								.valueOf(value)));
					} else
					{
						ps.setObject(psIndex, value);
					}
				}
				//执行sql
				int count = ps.executeUpdate();
				if (count > 0)
				{
					isSuccess = true;
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
				throw e;
			} finally
			{
				//关闭数据库资源
				super.close(null, ps);
			}
		}
		return isSuccess;
	}
	
	/**
	 * 根据条件更新，支持><=等条件
	 * @param <T>
	 * @param conn
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return 返回受影响行数
	 * @throws Exception
	 */
	public <T> int updateBySymbolsCondition(Connection conn, Class<T> entityClass, LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap) throws Exception
	{
		//boolean isSuccess = false;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = new GenericEmpDAO().getORMMap(entityClass);
	//  拼接修改的SQL语句
		StringBuffer sqlSb = new StringBuffer("update ").append(
				columns.get(entityClass.getSimpleName())).append(" set ");
		Iterator<Map.Entry<String, String>> objIter = objectMap.entrySet()
				.iterator();
		Map.Entry<String, String> e = null;
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		String fieldType = null;
		while (objIter.hasNext())
		{
			e = objIter.next();
			
			for (int index = 0; index < fields.length; index++)
			{
				if (fields[index].getName().equals(e.getKey()))
				{
					fieldType = fields[index].getGenericType()
							.toString();
					break;
				}
			}
			
			if (e.getValue() != null && fieldType.equals(StaticValue.TIMESTAMP)
					|| fieldType.equals(StaticValue.DATE_UTIL))
			{
				if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
					sqlSb.append(columns.get(e.getKey())).append("=to_date('").append(
							e.getValue()).append("','yyyy-MM-dd HH24:mi:ss'),");
				}else{
					sqlSb.append(columns.get(e.getKey())).append("='").append(
							e.getValue()).append("',");
				}
			} else if (e.getValue() != null&&(fieldType.equals("class java.lang.String") || fieldType.equals(StaticValue.TIMESTAMP)
					|| fieldType.equals(StaticValue.DATE_UTIL)))
			{
				sqlSb.append(columns.get(e.getKey())).append("='").append(
						e.getValue()).append("',");
			} else
			{
				sqlSb.append(columns.get(e.getKey())).append("=").append(
						e.getValue()).append(",");
			}
		}
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		//查询条件
		StringBuffer conditionSql = new StringBuffer();
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			//实体类字段名
			String field = null;
			//实体类字段值
			String fieldValue = null;
			//操作符
			String symbols = null;
			Iterator<Map.Entry<String, String>> iter = conditionMap
					.entrySet().iterator();
			//是否添加and
			boolean isAddAnd=false;
			while (iter.hasNext())
			{
				e = iter.next();
				fieldValue = e.getValue();
				if (!"".equals(fieldValue))
				{
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
						conditionSql.append(" ").append(symbols)
								.append(" ");
					}
					if (symbols.equalsIgnoreCase(StaticValue.IN)
							|| symbols.equalsIgnoreCase(StaticValue.NOT_IN))
					{
						String[] valueArray = fieldValue.split(",");
						StringBuffer valueSb = new StringBuffer("(");
						//不同数据库引号问题的兼容
						if (fieldType.equals("class java.lang.String")) {
							for (int i = 0; i < valueArray.length; i++) {
								valueSb.append("'").append(valueArray[i])
										.append("',");
							}
						} else {
							for (int i = 0; i < valueArray.length; i++) {
								valueSb.append(valueArray[i]).append(",");
							}
						}
						valueSb.deleteCharAt(valueSb.lastIndexOf(","))
								.append(")");
						conditionSql.append(valueSb);
					} else if (symbols.equalsIgnoreCase(StaticValue.LIKE))
					{
						conditionSql.append("'%").append(fieldValue)
								.append("%'");
					} else if (symbols.equalsIgnoreCase(StaticValue.LIKE1))
					{
						conditionSql.append("'%").append(fieldValue)
								.append("'");
					} else if (symbols.equalsIgnoreCase(StaticValue.LIKE2))
					{
						conditionSql.append("'").append(fieldValue).append(
								"%'");
					} else if (symbols.equals("=") || symbols.equals(">")
							|| symbols.equals("<") || symbols.equals("<>")
							|| symbols.equals(">=") || symbols.equals("<="))
					{
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											fieldValue));
						}else if(fieldType.equals("class java.lang.String")){
							conditionSql.append("'").append(fieldValue)
							.append("'");
						} else
						{
							conditionSql.append(fieldValue);
						}
					} else if (symbols
							.equalsIgnoreCase(StaticValue.BETWEEN))
					{
						String[] valueArray = fieldValue.split(",");
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											valueArray[0], valueArray[1]));
						} else
						{
							conditionSql.append(valueArray[0]).append(
									" and ").append(valueArray[1]);
						}
					}
				}
			}
		}
		String sql = sqlSb.append(conditionSql).toString();
		
		PreparedStatement ps = null;
		int count = 0;
		try
		{
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行sql
			count = ps.executeUpdate();
			
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			super.close(null, ps);
		}
		return count;
	}
	
	//改方法暂时不用。
//	public boolean deleteLfDominationByLfDep(Connection conn, LfDep lfDep,Integer opFlag)
//			throws Exception
//	{
//		String code = lfDep.getDepCode().substring(0, 2 * lfDep.getDepLevel());
//		String depSql = new StringBuffer("select ").append(TableLfDep.DEP_ID)
//				.append(" from ").append(TableLfDep.TABLE_NAME).append(
//						" where ").append(TableLfDep.DEP_CODE)
//				.append(" like '").append(code).append("%'").toString();
//		String userSql="";
//		if(opFlag==0){
//			userSql = new StringBuffer("select lfsysuser.").append(
//					TableLfSysuser.USER_ID).append(" from ").append(
//					TableLfSysuser.TABLE_NAME).append(" lfsysuser inner join ")
//					.append(TableLfDep.TABLE_NAME).append(" lfdep on lfsysuser.")
//					.append(TableLfSysuser.DEP_ID).append("=lfdep.").append(
//							TableLfDep.DEP_ID).append(" where lfdep.").append(
//							TableLfDep.DEP_CODE).append(" like '").append(code)
//					.append("%'").toString();
//		}else{
//			userSql = new StringBuffer("select lfsysuser.").append(
//					TableLfSysuser.USER_ID).append(" from ").append(
//					TableLfSysuser.TABLE_NAME).append(" lfsysuser inner join ")
//					.append(TableLfDep.TABLE_NAME).append(" lfdep on lfsysuser.")
//					.append(TableLfSysuser.DEP_ID).append("=lfdep.").append(
//							TableLfDep.DEP_ID).append(" where lfdep.").append(
//							TableLfDep.DEP_CODE).append(" like '").append(code)
//					.append("%'").append(" and lfdep.").append(TableLfDep.DEP_ID)
//					.append("<>").append(lfDep.getDepId()).toString();
//		}
//		String sql = new StringBuffer("delete from ").append(
//				TableLfDomination.TABLE_NAME).append(" where ").append(
//				TableLfDomination.DEP_ID).append(" in (").append(depSql)
//				.append(")").append(" and ").append(TableLfDomination.USER_ID)
//				.append(" not in (").append(userSql).append(")").toString();
//		boolean b = executeBySQL(conn, sql);
//		return b;
//	}

	  public int saveClientByProc(Connection conn, String depCode,
			String bizCode, String clientCode) throws Exception
	{
		int successCount = 0;
		CallableStatement cstmt = null;
		try
		{
			String procedure = "{call SYNC_INFOS(?,?,?,?,?)}";
			cstmt = conn.prepareCall(procedure);
			cstmt.setString(1, depCode);
			cstmt.setString(2, bizCode);
			cstmt.setString(3, clientCode);
			cstmt.setInt(4, 1);
			cstmt.registerOutParameter(5, oracle.jdbc.OracleTypes.INTEGER);
			cstmt.execute();
			successCount = (Integer) cstmt.getObject(5);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			successCount = -1;
			throw e;
		} finally
		{
			 
			if (cstmt != null)
			{
				cstmt.close();
			}
			 

		}
		return successCount;
	}

	 public int saveEmpolyeeByProc(final Connection conn, String depCode,
			String bizCode, String employeeCode) throws Exception
	{
		int successCount = 0;
		CallableStatement cstmt = null;
		try
		{
			String procedure = "{call SYNC_INFOS(?,?,?,?,?)}";
			cstmt = conn.prepareCall(procedure);
			cstmt.setString(1, depCode);
			cstmt.setString(2, bizCode);
			cstmt.setString(3, employeeCode);
			cstmt.setInt(4, 0);
			cstmt.registerOutParameter(5, oracle.jdbc.OracleTypes.INTEGER);
			cstmt.execute();
			successCount = (Integer) cstmt.getObject(5);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			successCount = -1;
			throw e;
		} finally
		{
			 
			if (cstmt != null)
			{
				cstmt.close();
			}
		 

		}
		return successCount;
	}

	/**
	 * 
	 */
    public int saveEnterpriseProc(Connection conn, String depCode,
			String bizCode,Integer depType) throws Exception
	{
		int successCount = 0;
		CallableStatement cstmt = null;

		try
		{
			String procedure = "{call SYNC_INFOS(?,?,?,?,?)}";
			cstmt = conn.prepareCall(procedure);
			cstmt.setString(1, depCode);
			cstmt.setString(2, bizCode);
			cstmt.setString(3, null);
			cstmt.setInt(4, depType);
			cstmt.registerOutParameter(5, oracle.jdbc.OracleTypes.INTEGER);
			cstmt.execute();
			successCount = (Integer) cstmt.getObject(5);
			/*
			 * while (rs.next()) { lfEnterprise = new LfEnterprise();
			 * lfEnterprise.setDepCode(rs.getString(SyncInfoVo.DEP_CODE));
			 * lfEnterprise.setDepName(rs.getString(SyncInfoVo.DEP_NAME));
			 * 
			 * //lfEnterpriseList.add(lfEnterprise);
			 * 
			 * if(save(conn,lfEnterprise)) { successCount++; }else {
			 * successCount = -1; }
			 */
			/*
			 * if (successCount == 5000) { save(lfEmployeeList,
			 * LfEmployee.class); lfEmployeeList.clear(); count = 0; }
			 */

			/* } */

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			successCount = -1;
			throw e;
		} finally
		{
			/*if (rs != null)
			{
				rs.close();
			}*/
			if (cstmt != null)
			{
				cstmt.close();
			}

		}
		return successCount;
	}

	public <T> int save(Connection conn, List<T> entityList,
			Class<T> entityClass) throws Exception
	{
		int insertCount = 0;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
	//  拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(
				columns.get(entityClass.getSimpleName())).append("(");
		StringBuffer wenhao = new StringBuffer(") values(");
		String entityId = null;
		for (int i = 0; i < fields.length; i++)
		{
			if(!fields[i].getName().equals("serialVersionUID")){
			if (!columns.get(fields[i].getName())
					.equals(columns.get("tableId")))
			{
				sqlSb.append(columns.get(fields[i].getName())).append(",");
				wenhao.append("?").append(",");
			} else
			{
				entityId = fields[i].getName();
			}
			}
		}
		// 去掉最后一个逗号。
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		PreparedStatement ps = null;
		try
		{
			EmpExecutionContext.sql("execute sql : "+sql);
			ps = conn.prepareStatement(sql);
			//属性名
			String fieldName = null;
			//属性名大写
			String fieldNameUpper = null;
			//属性类型
			String fieldType = null;
			Method entityMethod = null;
			//属性值
			Object value = null;
			int psIndex = 0;
			int listSize = entityList.size();
			for (int j = 0; j < listSize; j++)
			{
				psIndex = 0;
				for (int i = 0; i < fields.length; i++)
				{
					if(!fields[i].getName().equals("serialVersionUID")){
					fieldName = fields[i].getName();
					if (!fieldName.equals(entityId))
					{
						fieldNameUpper = Character.toUpperCase(fieldName
								.charAt(0))
								+ fieldName.substring(1);
						entityMethod = entityClass.getMethod("get"
								+ fieldNameUpper);
						//通过反射获取变量的值
						value = entityMethod.invoke(entityList.get(j));
						// 获取变量的类型
						fieldType = fields[i].getGenericType().toString();
						//是否是日期类型
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						psIndex++;
						//填充value
						if (value != null && isDateType)
						{
							ps.setTimestamp(psIndex, Timestamp.valueOf(value
									.toString()));
						}else if(value == null){
							ps.setNull(psIndex, Types.VARCHAR);
						} else
						{
							ps.setObject(psIndex, value);
						}
					}
					}
				}
				ps.addBatch();
			}
			//执行sql
			ps.executeBatch();
			insertCount = listSize;
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			//关闭数据库资源
			super.close(null, ps);
		}
		return insertCount;
	}

	public boolean CreateTableByField(Connection conn,
			List<String> fieldStrings, String tableName) throws Exception {
		throw new MethodNotImplmentedExecption(" targer : " + getClass());
	}

	public boolean DeleteTableByTableName(Connection conn,String tableName) throws Exception {
		throw new MethodNotImplmentedExecption(" targer : " + getClass());
	}
	
	
	public boolean InsertTableByMap(Connection conn,Map<String,String> formvalue,String tableName)throws Exception{
		StringBuffer sqlInsert = new StringBuffer("INSERT INTO ").append(
				tableName.toUpperCase()).append("(");
		StringBuffer val = new StringBuffer(") values(");
		StringBuffer names = new StringBuffer("");
		StringBuffer values = new StringBuffer("");
		boolean result = false;
		PreparedStatement ps = null;
		try {
			for (String key : formvalue.keySet()) {
				names.append(key).append(",");
				values.append("?").append(",");
			}
			names.deleteCharAt(names.lastIndexOf(","));
			values.deleteCharAt(values.lastIndexOf(","));
			sqlInsert.append(names).append(val).append(values).append(")");
			ps = conn.prepareStatement(sqlInsert.toString());
			int i = 0;
			for (String key : formvalue.keySet()) {
				i++;
				ps.setObject(i, formvalue.get(key));
			}
			ps.executeUpdate();
			result = true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		}finally{
			super.close(null, ps);
		}
		return result;
	}
	public boolean UpdateTableByMap(Connection conn,Map<String,String> formvalue,String tableName,Long sxxh)throws Exception{
		StringBuffer update = new StringBuffer("UPDATE ").append(tableName.toUpperCase()).append(" set ");
		boolean result = false;
		PreparedStatement ps = null;
		try {
			for(String key:formvalue.keySet()){
				if(formvalue.get(key)!=null){
					update.append(key).append("=?,");
				}
			}
			update.deleteCharAt(update.lastIndexOf(","));
			update.append(" where AIID=").append(sxxh);
			ps = conn.prepareStatement(update.toString());
			int i = 0;
			for (String key : formvalue.keySet()) {
				if(formvalue.get(key)!=null){
					i++;
					ps.setObject(i, formvalue.get(key));
				}
			}
			ps.executeUpdate();
			result = true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		}finally{
			super.close(null, ps);
		}
		return result;
		
		
	}
	
	public boolean deleteFormDatamByAiid(Connection conn, String tableName,
			String aiId) throws Exception {
		String sqlDelete = new StringBuffer("delete from ").append(tableName)
				.append(" where aiid = ").append(aiId).toString();
		PreparedStatement ps = null;
		int count = 0;
		try {
			ps = conn.prepareStatement(sqlDelete);
			count = ps.executeUpdate();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally {
			super.close(null, ps);
		}
		return count > 0 ? true : false;
	}
	
	public <T> boolean update(Connection conn, Class<T> entityClass,
			LinkedHashMap<String, Object> objectMap, String id,
			LinkedHashMap<String, String> conditionMap)
			throws Exception
	{
		boolean isSuccess = false;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接修改的SQL语句
		StringBuffer sqlSb = new StringBuffer("update ").append(
				columns.get(entityClass.getSimpleName())).append(" set ");
		LinkedHashMap<String, List<Object>> updateMap = new LinkedHashMap<String, List<Object>>();
		Object value = null;
		if (objectMap != null && !objectMap.entrySet().isEmpty())
		{
			//获取实体类的属性
			Field[] fields = entityClass.getDeclaredFields();
			Iterator<Map.Entry<String, Object>> iter = objectMap.entrySet()
					.iterator();
			Map.Entry<String, Object> e = null;
			String key = null;
			String fieldName = null;
			while (iter.hasNext())
			{
				e = iter.next();
				key = e.getKey();
				value = e.getValue();
				for (int i = 0; i < fields.length; i++)
				{
					if(!fields[i].getName().equals("serialVersionUID")){
					fieldName = fields[i].getName();
					if (fieldName.equals(key))
					{
						List<Object> valueAndtype = new ArrayList<Object>();
						valueAndtype.add(value);
						valueAndtype.add(fields[i].getGenericType().toString());
						updateMap.put(columns.get(fieldName), valueAndtype);
						sqlSb.append(columns.get(fieldName)).append("=?,");
					}
					}
				}
			}
		}
		if (!updateMap.entrySet().isEmpty())
		{
			sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(" where ")
					.append(columns.get("tableId")).append("=").append(id);
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
			{
				Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
						.iterator();
				String[] valueArray = null;
				StringBuffer valueSb = null;
				Map.Entry<String, String> e = null;
				Field[] fields = entityClass.getDeclaredFields();
				String fieldType = null;
				while (iter.hasNext())
				{
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
					valueArray = e.getValue().split(",");
					valueSb = new StringBuffer();
					//不同数据库引号问题的兼容
					if(fieldType.equals("class java.lang.String")){
						for (int i = 0; i < valueArray.length; i++)
						{
							valueSb.append("'").append(valueArray[i]).append("',");
						}
					}else{
						for (int i = 0; i < valueArray.length; i++)
						{
							valueSb.append(valueArray[i]).append(",");
						}
					}
					
					valueSb.deleteCharAt(valueSb.lastIndexOf(","));
					sqlSb.append(" and ").append(columns.get(e.getKey())).append(
							" in (").append(valueSb).append(")");
				}
			}
			String sql = sqlSb.toString();
			PreparedStatement ps = null;
			try
			{
				EmpExecutionContext.sql("execute sql : " + sql);
				ps = conn.prepareStatement(sql);
				Map.Entry<String, List<Object>> e = null;
				List<Object> valueAndType = null;
				String fieldType = null;
				int psIndex = 0;
				Iterator<Map.Entry<String, List<Object>>> iter = updateMap
						.entrySet().iterator();
				while (iter.hasNext())
				{
					psIndex++;
					e = iter.next();
					valueAndType = e.getValue();
					value = valueAndType.get(0);
					fieldType = String.valueOf(valueAndType.get(1));
					//是否是日期类型
					boolean isDateType = fieldType
							.equals(StaticValue.TIMESTAMP)
							|| fieldType.equals(StaticValue.DATE_SQL)
							|| fieldType.equals(StaticValue.DATE_UTIL);
					if (value != null && isDateType) {
						ps.setTimestamp(psIndex, Timestamp.valueOf(String
								.valueOf(value)));
					} else if (value == null) {
						ps.setNull(psIndex, Types.VARCHAR);
					} else {
						ps.setObject(psIndex, value);
					}
				}
				int count = ps.executeUpdate();
				if (count > 0)
				{
					isSuccess = true;
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
				throw e;
			} finally {
				//关闭数据库资源
				super.close(null, ps);
			}
		}
		return isSuccess;
	}

	public Long saveObjectReturnIDWithTri(Connection conn, Object object) throws Exception
	{
		return null;
	}
	

    public boolean updateCon(Connection conn, Object object,String key) throws Exception
    {
        boolean isSuccess = false;
        Class<?> entityClass = object.getClass();
        //  获取实体类字段与数据库字段映射的map集合
        Map<String, String> columns = getORMMap(entityClass);
        //  拼接修改的SQL语句
        StringBuffer sqlSb = new StringBuffer("update ").append(
                columns.get(entityClass.getSimpleName())).append(" set ");
        //获取实体类的属性
        Field[] fields = entityClass.getDeclaredFields();
        LinkedHashMap<String, List<String>> updateMap = new LinkedHashMap<String, List<String>>();
        //实体类属性名
        String fieldName = "";
        String idValue = "";
        //实体类属性值
        Object value = "";
        Method entityMethod = null;
        //where条件中对应field value
        String keyValue ="";
        for (int i = 0; i < fields.length; i++)
        {
            if(!fields[i].getName().equals("serialVersionUID")){
                fieldName = fields[i].getName();
                entityMethod = entityClass.getMethod("get"
                        + Character.toUpperCase(fieldName.charAt(0))
                        + fieldName.substring(1));
                //通过反射获取属性的值
                value = entityMethod.invoke(object);
                if (value != null)
                {
                    if (columns.get(fieldName).equals(columns.get("tableId")))
                    {
                        idValue = value.toString();
                    } else if(columns.get(fieldName).equalsIgnoreCase(key))
                    {
                        //为string类型
                        if(fields[i].getGenericType().toString().contains("String")){
                            keyValue = "'" + value.toString() + "'";
                        }else{
                            keyValue = value.toString();
                        }

                    } else
                    {
                        List<String> valueAndtype = new ArrayList<String>();
                        valueAndtype.add(value.toString());
                        valueAndtype.add(fields[i].getGenericType().toString());
                        updateMap.put(columns.get(fieldName), valueAndtype);
                        sqlSb.append(columns.get(fieldName)).append("=?,");
                    }
                }
            }
        }
        if (!updateMap.entrySet().isEmpty())
        {
        	//监控消息入库，更新消息时间为数据库系统时间
        	if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
    			//oracle数据库
        		sqlSb.append("DBSERVTIME=sysdate,");
    	   	} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
    	   		//sqlserver数据库
    	   		sqlSb.append("DBSERVTIME=getdate(),");
    		}else if(StaticValue.DBTYPE==StaticValue.DB2_DBTYPE){
    			//db2数据库
    			sqlSb.append("DBSERVTIME=current timestamp,");
    		}else if(StaticValue.DBTYPE==StaticValue.MYSQL_DBTYPE){
    			//mysql数据库
    			sqlSb.append("DBSERVTIME=now(),");
    		}
        	
            sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(" where ")
                    .append(key).append("=")
                    .append(keyValue);
            String sql = sqlSb.toString();
            PreparedStatement ps = null;
            try
            {
                EmpExecutionContext.sql("execute sql : "+sql);
                ps = conn.prepareStatement(sql);
                Map.Entry<String, List<String>> e = null;
                List<String> valueAndType = null;
                String valueStr = "";
                String fieldType = "";
                int psIndex = 0;
                Iterator<Map.Entry<String, List<String>>> iter = updateMap
                        .entrySet().iterator();
                while (iter.hasNext())
                {
                    psIndex++;
                    e = iter.next();
                    valueAndType = e.getValue();
                    valueStr = valueAndType.get(0);
                    fieldType = valueAndType.get(1);
                    if (fieldType.equals(StaticValue.TIMESTAMP)
                            || fieldType.equals(StaticValue.DATE_UTIL))
                    {
                        //时间类型
                        ps.setTimestamp(psIndex, Timestamp.valueOf(valueStr));
                    } else
                    {
                        ps.setString(psIndex, valueStr);
                    }
                }
                //执行sql
                int count = ps.executeUpdate();
                if (count > 0)
                {
                    isSuccess = true;
                }
            } catch (Exception e)
            {
                EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
                throw e;
            } finally
            {
                //关闭数据库资源
                super.close(null, ps);
            }
        }
        return isSuccess;
    }

    public boolean updateConOld(Connection conn, Object object,String key) throws Exception
    {
        boolean isSuccess = false;
        Class<?> entityClass = object.getClass();
        //  获取实体类字段与数据库字段映射的map集合
        Map<String, String> columns = getORMMap(entityClass);
        //  拼接修改的SQL语句
        StringBuffer sqlSb = new StringBuffer("update ").append(
                columns.get(entityClass.getSimpleName())).append(" set ");
        //获取实体类的属性
        Field[] fields = entityClass.getDeclaredFields();
        LinkedHashMap<String, List<String>> updateMap = new LinkedHashMap<String, List<String>>();
        //实体类属性名
        String fieldName = "";
        String idValue = "";
        //实体类属性值
        Object value = "";
        Method entityMethod = null;
        //where条件中对应field value
        String keyValue ="";
        for (int i = 0; i < fields.length; i++)
        {
            if(!fields[i].getName().equals("serialVersionUID")){
                fieldName = fields[i].getName();
                entityMethod = entityClass.getMethod("get"
                        + Character.toUpperCase(fieldName.charAt(0))
                        + fieldName.substring(1));
                //通过反射获取属性的值
                value = entityMethod.invoke(object);
                if (value != null)
                {
                    if (columns.get(fieldName).equals(columns.get("tableId")))
                    {
                        idValue = value.toString();
                    } else if(columns.get(fieldName).equalsIgnoreCase(key))
                    {
                        //为string类型
                        if(fields[i].getGenericType().toString().contains("String")){
                            keyValue = "'" + value.toString() + "'";
                        }else{
                            keyValue = value.toString();
                        }

                    } else
                    {
                        List<String> valueAndtype = new ArrayList<String>();
                        valueAndtype.add(value.toString());
                        valueAndtype.add(fields[i].getGenericType().toString());
                        updateMap.put(columns.get(fieldName), valueAndtype);
                        sqlSb.append(columns.get(fieldName)).append("=?,");
                    }
                }
            }
        }
        if (!updateMap.entrySet().isEmpty())
        {
            sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(" where ")
                    .append(key).append("=")
                    .append(keyValue);
            String sql = sqlSb.toString();
            PreparedStatement ps = null;
            try
            {
                EmpExecutionContext.sql("execute sql : "+sql);
                ps = conn.prepareStatement(sql);
                Map.Entry<String, List<String>> e = null;
                List<String> valueAndType = null;
                String valueStr = "";
                String fieldType = "";
                int psIndex = 0;
                Iterator<Map.Entry<String, List<String>>> iter = updateMap
                        .entrySet().iterator();
                while (iter.hasNext())
                {
                    psIndex++;
                    e = iter.next();
                    valueAndType = e.getValue();
                    valueStr = valueAndType.get(0);
                    fieldType = valueAndType.get(1);
                    if (fieldType.equals(StaticValue.TIMESTAMP)
                            || fieldType.equals(StaticValue.DATE_UTIL))
                    {
                        //时间类型
                        ps.setTimestamp(psIndex, Timestamp.valueOf(valueStr));
                    } else
                    {
                        ps.setString(psIndex, valueStr);
                    }
                }
                //执行sql
                int count = ps.executeUpdate();
                if (count > 0)
                {
                    isSuccess = true;
                }
            } catch (Exception e)
            {
                EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
                throw e;
            } finally
            {
                //关闭数据库资源
                super.close(null, ps);
            }
        }
        return isSuccess;
    }
    
    /**
     * 更新方法，多个更新条件
     * @description    
     * @param conn
     * @param object
     * @param key
     * @return
     * @throws Exception       			 
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-4-29 上午11:49:02
     */
    public boolean updateConMultiKey(Connection conn, Object object,Map<String, String> keyMap) throws Exception
    {
        boolean isSuccess = false;
        Class<?> entityClass = object.getClass();
        //  获取实体类字段与数据库字段映射的map集合
        Map<String, String> columns = getORMMap(entityClass);
        //  拼接修改的SQL语句
        StringBuffer sqlSb = new StringBuffer("update ").append(
                columns.get(entityClass.getSimpleName())).append(" set ");
        //获取实体类的属性
        Field[] fields = entityClass.getDeclaredFields();
        LinkedHashMap<String, List<String>> updateMap = new LinkedHashMap<String, List<String>>();
        //实体类属性名
        String fieldName = "";
        String idValue = "";
        //实体类属性值
        Object value = "";
        Method entityMethod = null;
        //where条件中对应field value
        String keyValue ="";
        for (int i = 0; i < fields.length; i++)
        {
            if(!fields[i].getName().equals("serialVersionUID")){
                fieldName = fields[i].getName();
                entityMethod = entityClass.getMethod("get"
                        + Character.toUpperCase(fieldName.charAt(0))
                        + fieldName.substring(1));
                //通过反射获取属性的值
                value = entityMethod.invoke(object);
                if (value != null)
                {
                    if (columns.get(fieldName).equals(columns.get("tableId")))
                    {
                        idValue = value.toString();
                    } else if(keyMap.containsKey(columns.get(fieldName).toUpperCase()))
                    {
                        //为string类型
                        if(fields[i].getGenericType().toString().contains("String")){
                            keyValue = "'" + value.toString() + "'";
                        }else{
                            keyValue = value.toString();
                        }
                        keyMap.put(columns.get(fieldName).toUpperCase(), keyValue);

                    }else
                    {
                        List<String> valueAndtype = new ArrayList<String>();
                        valueAndtype.add(value.toString());
                        valueAndtype.add(fields[i].getGenericType().toString());
                        updateMap.put(columns.get(fieldName), valueAndtype);
                        sqlSb.append(columns.get(fieldName)).append("=?,");
                    }
                }
            }
        }
        if (!updateMap.entrySet().isEmpty())
        {
        	//监控消息入库，更新消息时间为数据库系统时间
        	if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
    			//oracle数据库
        		sqlSb.append("DBSERVTIME=sysdate,");
    	   	}else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
    	   		//sqlserver数据库
    	   		sqlSb.append("DBSERVTIME=getdate(),");
    		}else if(StaticValue.DBTYPE==StaticValue.DB2_DBTYPE){
    			//db2数据库
    			sqlSb.append("DBSERVTIME=current timestamp,");
    		}else if(StaticValue.DBTYPE==StaticValue.MYSQL_DBTYPE){
    			//mysql数据库
    			sqlSb.append("DBSERVTIME=now(),");
    		}
        	
            sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
            int i = 0;
            for(String key : keyMap.keySet())
            {
            	if(i == 0)
            	{
            		sqlSb.append(" WHERE ").append(key).append("=").append(keyMap.get(key));
            	}
            	else
            	{
            		sqlSb.append(" AND ").append(key).append("=").append(keyMap.get(key));
            	}
            	i++;
            }
            
            
            String sql = sqlSb.toString();
            PreparedStatement ps = null;
            try
            {
                EmpExecutionContext.sql("execute sql : "+sql);
                ps = conn.prepareStatement(sql);
                Map.Entry<String, List<String>> e = null;
                List<String> valueAndType = null;
                String valueStr = "";
                String fieldType = "";
                int psIndex = 0;
                Iterator<Map.Entry<String, List<String>>> iter = updateMap
                        .entrySet().iterator();
                while (iter.hasNext())
                {
                    psIndex++;
                    e = iter.next();
                    valueAndType = e.getValue();
                    valueStr = valueAndType.get(0);
                    fieldType = valueAndType.get(1);
                    if (fieldType.equals(StaticValue.TIMESTAMP)
                            || fieldType.equals(StaticValue.DATE_UTIL))
                    {
                        //时间类型
                        ps.setTimestamp(psIndex, Timestamp.valueOf(valueStr));
                    } else
                    {
                        ps.setString(psIndex, valueStr);
                    }
                }
                //执行sql
                int count = ps.executeUpdate();
                if (count > 0)
                {
                    isSuccess = true;
                }
            } catch (Exception e)
            {
                EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
                throw e;
            } finally
            {
                //关闭数据库资源
                super.close(null, ps);
            }
        }
        return isSuccess;
    }
    
    public boolean saveCon(Connection conn, Object object) throws Exception
    {
        boolean isSuccess = false;
        Class<?> entityClass = object.getClass();
        //  获取实体类字段与数据库字段映射的map集合
        Map<String, String> columns = getORMMap(entityClass);
        //  拼接SQL语句
        StringBuffer sqlSb = new StringBuffer("insert into ").append(
                columns.get(entityClass.getSimpleName())).append(" (");
        //获取实体类的属性
        Field[] fields = entityClass.getDeclaredFields();
        LinkedHashMap<String, List<String>> updateMap = new LinkedHashMap<String, List<String>>();
        //实体类属性名
        String fieldName = "";
        String idValue = "";
        //实体类属性值
        Object value = "";
        Method entityMethod = null;
        StringBuffer wenhao = new StringBuffer("");
        for (int i = 0; i < fields.length; i++)
        {
            if(!fields[i].getName().equals("serialVersionUID")){
                fieldName = fields[i].getName();
                entityMethod = entityClass.getMethod("get"
                        + Character.toUpperCase(fieldName.charAt(0))
                        + fieldName.substring(1));
                //通过反射获取属性的值
                value = entityMethod.invoke(object);
                if (value != null)
                {
                    if (columns.get(fieldName).equals(columns.get("tableId")))
                    {
                        idValue = value.toString();
                    } else
                    {
                        List<String> valueAndtype = new ArrayList<String>();
                        valueAndtype.add(value.toString());
                        valueAndtype.add(fields[i].getGenericType().toString());
                        updateMap.put(columns.get(fieldName), valueAndtype);
                        sqlSb.append(columns.get(fieldName)).append(",");
                        wenhao.append("?,");
                    }
                }
            }
        }
        if (!updateMap.entrySet().isEmpty())
        {
        	//插入的时候将数据库时间插入到数据库，mysql需要做这个特殊处理。其他数据库利用数据库的默认值机制。
        	if(StaticValue.DBTYPE==StaticValue.MYSQL_DBTYPE){
				//mysql数据库
				sqlSb.append("DBSERVTIME,");
				wenhao.append("now(),");
        	}
        	
            sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(") values (");
            sqlSb.append(wenhao.deleteCharAt(wenhao.lastIndexOf(","))).append(")");
            String sql = sqlSb.toString();
            PreparedStatement ps = null;
            try
            {
                EmpExecutionContext.sql("execute sql : "+sql);
                ps = conn.prepareStatement(sql);
                Map.Entry<String, List<String>> e = null;
                List<String> valueAndType = null;
                String valueStr = "";
                String fieldType = "";
                int psIndex = 0;
                Iterator<Map.Entry<String, List<String>>> iter = updateMap
                        .entrySet().iterator();
                while (iter.hasNext())
                {
                    psIndex++;
                    e = iter.next();
                    valueAndType = e.getValue();
                    valueStr = valueAndType.get(0);
                    fieldType = valueAndType.get(1);
                    if (fieldType.equals(StaticValue.TIMESTAMP)
                            || fieldType.equals(StaticValue.DATE_UTIL))
                    {
                        //时间类型
                        ps.setTimestamp(psIndex, Timestamp.valueOf(valueStr));
                    } else
                    {
                        ps.setString(psIndex, valueStr);
                    }
                }
                //执行sql
                int count = ps.executeUpdate();
                if (count > 0)
                {
                    isSuccess = true;
                }
            } catch (Exception e)
            {
                EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
                throw e;
            } finally
            {
                //关闭数据库资源
                super.close(null, ps);
            }
        }
        return isSuccess;
    }
    
    public boolean saveConOld(Connection conn, Object object) throws Exception
    {
        boolean isSuccess = false;
        Class<?> entityClass = object.getClass();
        //  获取实体类字段与数据库字段映射的map集合
        Map<String, String> columns = getORMMap(entityClass);
        //  拼接SQL语句
        StringBuffer sqlSb = new StringBuffer("insert into ").append(
                columns.get(entityClass.getSimpleName())).append(" (");
        //获取实体类的属性
        Field[] fields = entityClass.getDeclaredFields();
        LinkedHashMap<String, List<String>> updateMap = new LinkedHashMap<String, List<String>>();
        //实体类属性名
        String fieldName = "";
        String idValue = "";
        //实体类属性值
        Object value = "";
        Method entityMethod = null;
        StringBuffer wenhao = new StringBuffer("");
        for (int i = 0; i < fields.length; i++)
        {
            if(!fields[i].getName().equals("serialVersionUID")){
                fieldName = fields[i].getName();
                entityMethod = entityClass.getMethod("get"
                        + Character.toUpperCase(fieldName.charAt(0))
                        + fieldName.substring(1));
                //通过反射获取属性的值
                value = entityMethod.invoke(object);
                if (value != null)
                {
                    if (columns.get(fieldName).equals(columns.get("tableId")))
                    {
                        idValue = value.toString();
                    } else
                    {
                        List<String> valueAndtype = new ArrayList<String>();
                        valueAndtype.add(value.toString());
                        valueAndtype.add(fields[i].getGenericType().toString());
                        updateMap.put(columns.get(fieldName), valueAndtype);
                        sqlSb.append(columns.get(fieldName)).append(",");
                        wenhao.append("?,");
                    }
                }
            }
        }
        if (!updateMap.entrySet().isEmpty())
        {
            sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(") values (");
            sqlSb.append(wenhao.deleteCharAt(wenhao.lastIndexOf(","))).append(")");
            String sql = sqlSb.toString();
            PreparedStatement ps = null;
            try
            {
                EmpExecutionContext.sql("execute sql : "+sql);
                ps = conn.prepareStatement(sql);
                Map.Entry<String, List<String>> e = null;
                List<String> valueAndType = null;
                String valueStr = "";
                String fieldType = "";
                int psIndex = 0;
                Iterator<Map.Entry<String, List<String>>> iter = updateMap
                        .entrySet().iterator();
                while (iter.hasNext())
                {
                    psIndex++;
                    e = iter.next();
                    valueAndType = e.getValue();
                    valueStr = valueAndType.get(0);
                    fieldType = valueAndType.get(1);
                    if (fieldType.equals(StaticValue.TIMESTAMP)
                            || fieldType.equals(StaticValue.DATE_UTIL))
                    {
                        //时间类型
                        ps.setTimestamp(psIndex, Timestamp.valueOf(valueStr));
                    } else
                    {
                        ps.setString(psIndex, valueStr);
                    }
                }
                //执行sql
                int count = ps.executeUpdate();
                if (count > 0)
                {
                    isSuccess = true;
                }
            } catch (Exception e)
            {
                EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
                throw e;
            } finally
            {
                //关闭数据库资源
                super.close(null, ps);
            }
        }
        return isSuccess;
    }
}
