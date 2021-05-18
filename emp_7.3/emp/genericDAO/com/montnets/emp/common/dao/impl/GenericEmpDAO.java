package com.montnets.emp.common.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.exception.MethodNotImplmentedExecption;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * @project emp
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-9 上午10:09:09
 * @description
 */

public class GenericEmpDAO extends SuperDAO implements IEmpDAO
{
	/**
	 * 保存方法
	 */
	public boolean save(Object object) throws Exception
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
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
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
			super.close(null, ps, conn);
		}
		return isSuccess;
	}
	
	/**
	 * 保存方法
	 */
	public boolean save(Connection conn,boolean isOutConn,Object object) throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return false;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return false;
		}
		
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
		//Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			EmpExecutionContext.sql("execute sql : " + sql);
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
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(null, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(null, ps,conn);
			}
		}
		return isSuccess;
	}
	
	
    /**
     * 保存方法
     */
	public <T> int save(List<T> entityList, Class<T> entityClass)
			throws Exception
	{
		//插入条数
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
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			conn.setAutoCommit(false);
			EmpExecutionContext.sql("execute sql : " + sql);
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
			//插入的记录总数
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
						//通过反射获取属性的值
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
			//批量插入
			ps.executeBatch();
			conn.commit();
			insertCount = listSize;
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			//回滚
			conn.rollback();
			throw e;
		} finally
		{
			conn.setAutoCommit(true);
			//关闭数据库资源
			super.close(null, ps, conn);
		}
		return insertCount;
	}
	
    /**
     * 保存方法
     */
	public <T> int save(Connection conn,boolean isOutConn,List<T> entityList, Class<T> entityClass)
			throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return -1;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return -1;
		}
		
		//插入条数
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
		//Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			conn.setAutoCommit(false);
			EmpExecutionContext.sql("execute sql : " + sql);
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
			//插入的记录总数
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
						//通过反射获取属性的值
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
			//批量插入
			ps.executeBatch();
			conn.commit();
			insertCount = listSize;
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			//回滚
			conn.rollback();
			throw e;
		} finally
		{
			conn.setAutoCommit(true);
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(null, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(null, ps,conn);
			}
		}
		return insertCount;
	}
	
	public Long saveObjectReturnID(Object object) throws Exception
	{
		throw new MethodNotImplmentedExecption(" targer : " + getClass());
	}
	
	public Long saveObjectReturnID(Connection conn,boolean isOutConn,Object object) throws Exception
	{
		throw new MethodNotImplmentedExecption(" targer : " + getClass());
	}
	
	/**
	 * 保存记录到数据库并返回自增id，自增id由程序自增
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long saveObjProReturnID(Object object) 
	{
		
		//插入后返回的主键id
		Long returnID = 0L;
		Connection conn = null;
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
			
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
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
						} else if(value == null){
							ps.setNull(psIndex, Types.VARCHAR);
						}else {
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
				super.close(null, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源异常。");
			}
		}
		return returnID;
	
	}
	
	/**
	 * 保存记录到数据库并返回自增id，自增id由程序自增
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long saveObjProReturnID(Connection conn,boolean isOutConn,Object object) 
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
		
		//插入后返回的主键id
		Long returnID = 0L;
		//Connection conn = null;
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
			
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
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
						} else if(value == null){
							ps.setNull(psIndex, Types.VARCHAR);
						}else {
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
				if(isOutConn)
				{
					//方法外部传递数据库连接进来,在方法内部不关闭连接
					//关闭数据库资源
					super.close(null, ps);
				}
				else
				{
					//方法内部生成数据库连接，需要在方法内关闭连接
					//关闭数据库资源
					super.close(null, ps,conn);
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源异常。");
			}
		}
		return returnID;
	
	}
	
	
    /**
     * 删除方法
     */
	public <T> boolean delete(Class<T> entityClass) throws Exception
	{
		boolean isSuccess = false;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接删除的SQL语句
		String sql = new StringBuffer("delete from ").append(
				columns.get(entityClass.getSimpleName())).toString();
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
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
			super.close(null, ps, conn);
		}
		return isSuccess;
	}
	
    /**
     * 删除方法
     */
	public <T> boolean delete(Connection conn,boolean isOutConn,Class<T> entityClass) throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return false;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return false;
		}
		
		boolean isSuccess = false;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接删除的SQL语句
		String sql = new StringBuffer("delete from ").append(
				columns.get(entityClass.getSimpleName())).toString();
		//Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			EmpExecutionContext.sql("execute sql : " + sql);
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
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(null, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(null, ps,conn);
			}
		}
		return isSuccess;
	}
	
    /**
     * 删除方法
     */
	public <T> int delete(Class<T> entityClass, String ids) throws Exception
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
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
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
			super.close(null, ps, conn);
		}
		return deleteCount;
	}
	
    /**
     * 删除方法
     */
	public <T> int delete(Connection conn,boolean isOutConn,Class<T> entityClass, String ids) throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return -1;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return -1;
		}
		
		//删除记录数
		int deleteCount = 0;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接删除的SQL语句
		String sql = new StringBuffer("delete from ").append(
				columns.get(entityClass.getSimpleName())).append(" where ")
				.append(columns.get("tableId")).append(" in (").append(ids)
				.append(")").toString();
		//Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行sql
			deleteCount = ps.executeUpdate();
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
				super.close(null, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(null, ps,conn);
			}
		}
		return deleteCount;
	}
	
    /**
     * 删除方法
     */
	public <T> int delete(Class<T> entityClass,
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
			//获取实体类的属性
			Field[] fields = entityClass.getDeclaredFields();
			/*Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
			String[] valueArray = null;
			StringBuffer valueSb = null;
			
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
			*/
			//拼结过滤条件
			StringBuffer conditionsql = new StringBuffer();
			if(conditionMap != null && conditionMap.size()>0)
			{
			    appendCondition(conditionMap,fields,conditionsql,columns);
			}
            //组装sql语句
			String sql = sqlSb.append(conditionsql).toString();
			Connection conn = null;
			PreparedStatement ps = null;
			try
			{
				//获取数据库连接
				conn = connectionManager
						.getDBConnection(StaticValue.EMP_POOLNAME);
				EmpExecutionContext.sql("execute sql : " + sql);
				ps = conn.prepareStatement(sql);
				//执行SQL
				deleteCount = ps.executeUpdate();
			} catch (Exception ex)
			{
				EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
				throw ex;
			} finally
			{
				//关闭数据库资源
				super.close(null, ps, conn);
			}
		}
		return deleteCount;
	}
	
    /**
     * 删除方法
     */
	public <T> int delete(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return -1;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return -1;
		}
		
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
			//获取实体类的属性
			Field[] fields = entityClass.getDeclaredFields();
			/*Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
			String[] valueArray = null;
			StringBuffer valueSb = null;
			
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
			*/
			//拼结过滤条件
			StringBuffer conditionsql = new StringBuffer();
			if(conditionMap != null && conditionMap.size()>0)
			{
			    appendCondition(conditionMap,fields,conditionsql,columns);
			}
            //组装sql语句
			String sql = sqlSb.append(conditionsql).toString();
			//Connection conn = null;
			PreparedStatement ps = null;
			try
			{
				//获取数据库连接
				if(conn==null)
				{
					conn = connectionManager
							.getDBConnection(StaticValue.EMP_POOLNAME);
				}
				EmpExecutionContext.sql("execute sql : " + sql);
				ps = conn.prepareStatement(sql);
				//执行SQL
				deleteCount = ps.executeUpdate();
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
					super.close(null, ps);
				}
				else
				{
					//方法内部生成数据库连接，需要在方法内关闭连接
					//关闭数据库资源
					super.close(null, ps,conn);
				}
			}
		}
		return deleteCount;
	}
	
    /**
     * 修改方法
     */
	public boolean update(Object object) throws Exception
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
			Connection conn = null;
			PreparedStatement ps = null;
			try
			{
				//获取数据库连接
				conn = connectionManager
						.getDBConnection(StaticValue.EMP_POOLNAME);
				EmpExecutionContext.sql("execute sql : " + sql);
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
				super.close(null, ps, conn);
			}
		}
		return isSuccess;
	}
	
    /**
     * 修改方法
     */
	public boolean update(Connection conn,boolean isOutConn,Object object) throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return false;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return false;
		}
		
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
			//Connection conn = null;
			PreparedStatement ps = null;
			try
			{
				//获取数据库连接
				if(conn==null)
				{
					conn = connectionManager
							.getDBConnection(StaticValue.EMP_POOLNAME);
				}
				EmpExecutionContext.sql("execute sql : " + sql);
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
				if(isOutConn)
				{
					//方法外部传递数据库连接进来,在方法内部不关闭连接
					//关闭数据库资源
					super.close(null, ps);
				}
				else
				{
					//方法内部生成数据库连接，需要在方法内关闭连接
					//关闭数据库资源
					super.close(null, ps,conn);
				}
			}
		}
		return isSuccess;
	}
	
    /**
     * 修改方法
     */
	public <T> boolean update(Class<T> entityClass,
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
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
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
			super.close(null, ps, conn);
		}
		return isSuccess;
	}

    /**
     * 修改方法
     */
	public <T> boolean update(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return false;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return false;
		}
		
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
		//Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			EmpExecutionContext.sql("execute sql : " + sql);
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
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(null, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(null, ps,conn);
			}
		}
		return isSuccess;
	}
	
    /**
     * 修改方法
     */
	public <T> boolean updateForMonitor(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return false;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return false;
		}
		
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
		//Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			EmpExecutionContext.sql("execute sql : " + sql);
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
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(null, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(null, ps,conn);
			}
		}
		return isSuccess;
	}
	
	public <T> boolean update(Class<T> entityClass,
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
			// 去掉最后一个逗号。
			sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(" where ")
					.append(columns.get("tableId")).append("=").append(id);
			String sql = sqlSb.toString();
			Connection conn = null;
			PreparedStatement ps = null;
			try
			{
				//获取数据库连接
				conn = connectionManager
						.getDBConnection(StaticValue.EMP_POOLNAME);
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
					//属性类型
					fieldType = String.valueOf(valueAndType.get(1));
					//是否是日期类型
					boolean isDateType = fieldType
							.equals(StaticValue.TIMESTAMP)
							|| fieldType.equals(StaticValue.DATE_SQL)
							|| fieldType.equals(StaticValue.DATE_UTIL);
					if (value != null && isDateType)
					{
						ps.setTimestamp(psIndex, Timestamp.valueOf(String
								.valueOf(value)));
					}else if(value == null){
						ps.setNull(psIndex, Types.VARCHAR);
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
				super.close(null, ps, conn);
			}
		}
		return isSuccess;
	}

	public <T> boolean update(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, Object> objectMap, String id)
			throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return false;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return false;
		}
		
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
			// 去掉最后一个逗号。
			sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(" where ")
					.append(columns.get("tableId")).append("=").append(id);
			String sql = sqlSb.toString();
			//Connection conn = null;
			PreparedStatement ps = null;
			try
			{
				//获取数据库连接
				if(conn==null)
				{
					conn = connectionManager
							.getDBConnection(StaticValue.EMP_POOLNAME);
				}
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
					//属性类型
					fieldType = String.valueOf(valueAndType.get(1));
					//是否是日期类型
					boolean isDateType = fieldType
							.equals(StaticValue.TIMESTAMP)
							|| fieldType.equals(StaticValue.DATE_SQL)
							|| fieldType.equals(StaticValue.DATE_UTIL);
					if (value != null && isDateType)
					{
						ps.setTimestamp(psIndex, Timestamp.valueOf(String
								.valueOf(value)));
					}else if(value == null){
						ps.setNull(psIndex, Types.VARCHAR);
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
				if(isOutConn)
				{
					//方法外部传递数据库连接进来,在方法内部不关闭连接
					//关闭数据库资源
					super.close(null, ps);
				}
				else
				{
					//方法内部生成数据库连接，需要在方法内关闭连接
					//关闭数据库资源
					super.close(null, ps,conn);
				}
			}
		}
		return isSuccess;
	}
	
	/**
	 * 根据条件更新，支持><=等条件
	 * @param <T>
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return 返回受影响行数
	 * @throws Exception
	 */
	public <T> int updateBySymbolsCondition(Class<T> entityClass, LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap) throws Exception
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
		Connection conn = null;
		PreparedStatement ps = null;
		int count = 0;
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
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
			super.close(null, ps, conn);
		}
		return count;
	}
	
	/**
	 * 根据条件更新，支持><=等条件
	 * @param <T>
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return 返回受影响行数
	 * @throws Exception
	 */
	public <T> int updateBySymbolsCondition(Connection conn,boolean isOutConn,Class<T> entityClass, LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap) throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return -1;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return -1;
		}
		
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
		//Connection conn = null;
		PreparedStatement ps = null;
		int count = 0;
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			//获取数据库连接
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
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
			if(isOutConn)
			{
				//方法外部传递数据库连接进来,在方法内部不关闭连接
				//关闭数据库资源
				super.close(null, ps);
			}
			else
			{
				//方法内部生成数据库连接，需要在方法内关闭连接
				//关闭数据库资源
				super.close(null, ps,conn);
			}
		}
		return count;
	}
	
	public <T> T findObjectByID(Class<T> entityClass, long id) throws Exception
	{
		T returnObj = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接查询的SQL语句
		String sql = new StringBuffer("select * from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK()).append(" where ")
				.append(columns.get("tableId")).append("=").append(id)
				.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//RESULTSET转换为list集合
			List<T> returnList = rsToList(rs, entityClass, columns);
			if (returnList != null && returnList.size() > 0)
			{
				returnObj = returnList.get(0);
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
		return returnObj;
	}

	public <T> T findObjectByID(Connection conn,boolean isOutConn,Class<T> entityClass, long id) throws Exception
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
		
		T returnObj = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接查询的SQL语句
		String sql = new StringBuffer("select * from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK()).append(" where ")
				.append(columns.get("tableId")).append("=").append(id)
				.toString();
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
			//执行SQL
			rs = ps.executeQuery();
			//RESULTSET转换为list集合
			List<T> returnList = rsToList(rs, entityClass, columns);
			if (returnList != null && returnList.size() > 0)
			{
				returnObj = returnList.get(0);
			}
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
		return returnObj;
	}
	
	public <T> List<T> findDistinctListBySymbolsCondition(Class<T> entityClass,
			List<String> distinctFieldList,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select distinct ");
		if (distinctFieldList != null && distinctFieldList.size() > 0)
		{
			String column = null;
			for (int i = 0; i < distinctFieldList.size(); i++)
			{
				column = columns.get(distinctFieldList.get(i));
				sqlSb.append(column).append(",");
			}
			sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(" from ").append(
					columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK()).append(
					" ");
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
			{
				//获取实体类属性
				Field[] fields = entityClass.getDeclaredFields();
				Map.Entry<String, String> e = null;
				//实体类字段名
				String field = null;
				//实体类字段值
				String fieldValue = null;
				//操作符
				String symbols = null;
				//字段类型
				String fieldType = null;
				//查询条件
				StringBuffer conditionSql = new StringBuffer();
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
				sqlSb.append(conditionSql);
			}
			//拼接排序条件
			if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
			{
				sqlSb.append(" order by ");
				Iterator<Map.Entry<String, String>> iter = orderbyMap
						.entrySet().iterator();
				Map.Entry<String, String> e = null;
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
				conn = connectionManager
						.getDBConnection(StaticValue.EMP_POOLNAME);
				EmpExecutionContext.sql("execute sql : " + sql);
				ps = conn.prepareStatement(sql);
				//执行SQL
				rs = ps.executeQuery();
				//resultset转换为list集合
				returnList = partrsToList(rs, entityClass, columns);
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
		return returnList;
	}

	public <T> List<T> findDistinctListBySymbolsCondition(Connection conn,boolean isOutConn,Class<T> entityClass,
			List<String> distinctFieldList,
			LinkedHashMap<String, String> conditionMap,
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
		
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select distinct ");
		if (distinctFieldList != null && distinctFieldList.size() > 0)
		{
			String column = null;
			for (int i = 0; i < distinctFieldList.size(); i++)
			{
				column = columns.get(distinctFieldList.get(i));
				sqlSb.append(column).append(",");
			}
			sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(" from ").append(
					columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK()).append(
					" ");
			if (conditionMap != null && !conditionMap.entrySet().isEmpty())
			{
				//获取实体类属性
				Field[] fields = entityClass.getDeclaredFields();
				Map.Entry<String, String> e = null;
				//实体类字段名
				String field = null;
				//实体类字段值
				String fieldValue = null;
				//操作符
				String symbols = null;
				//字段类型
				String fieldType = null;
				//查询条件
				StringBuffer conditionSql = new StringBuffer();
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
				sqlSb.append(conditionSql);
			}
			//拼接排序条件
			if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
			{
				sqlSb.append(" order by ");
				Iterator<Map.Entry<String, String>> iter = orderbyMap
						.entrySet().iterator();
				Map.Entry<String, String> e = null;
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
					conn = connectionManager
							.getDBConnection(StaticValue.EMP_POOLNAME);
				}
				EmpExecutionContext.sql("execute sql : " + sql);
				ps = conn.prepareStatement(sql);
				//执行SQL
				rs = ps.executeQuery();
				//resultset转换为list集合
				returnList = partrsToList(rs, entityClass, columns);
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
		return returnList;
	}
	
	public <T> List<T> findListByCondition(Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception
	{
		List<T> returnList = null;
		Iterator<Map.Entry<String, String>> iter = null;
		Map.Entry<String, String> e = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select * from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK());
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			//是否添加and
			boolean isAddAnd=false;
			while (iter.hasNext())
			{
				e = iter.next();
				if (!"".equals(e.getValue()))
				{
					if(isAddAnd){
					sqlSb.append(" and ").append(columns.get(e.getKey()))
							.append("=?");
					}else{
						sqlSb.append(" where ").append(columns.get(e.getKey()))
						.append("=?");
						isAddAnd=true;
					}
				}
			}
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
			//执行
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

	public <T> List<T> findListByCondition(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,
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
		
		List<T> returnList = null;
		Iterator<Map.Entry<String, String>> iter = null;
		Map.Entry<String, String> e = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select * from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK());
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			//是否添加and
			boolean isAddAnd=false;
			while (iter.hasNext())
			{
				e = iter.next();
				if (!"".equals(e.getValue()))
				{
					if(isAddAnd){
					sqlSb.append(" and ").append(columns.get(e.getKey()))
							.append("=?");
					}else{
						sqlSb.append(" where ").append(columns.get(e.getKey()))
						.append("=?");
						isAddAnd=true;
					}
				}
			}
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
			//执行
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
	
	public <T> List<T> findListBySymbolsCondition(Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception
	{
		List<T> returnList = null;
		//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select * from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK());
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			String field = null;
			String fieldValue = null;
			String symbols = null;
			String fieldType = null;
			StringBuffer conditionSql = new StringBuffer();
			Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
					.iterator();
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
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											fieldValue));
						}else if(fieldType.equals("class java.lang.String")){
							conditionSql.append("'").append(fieldValue).append(
							"'");
						} else
						{
							conditionSql.append(fieldValue);
						}
					} else if (symbols.equalsIgnoreCase(StaticValue.BETWEEN))
					{
						String[] valueArray = fieldValue.split(",");
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											valueArray[0], valueArray[1]));
						} else
						{
							conditionSql.append(valueArray[0]).append(" and ")
									.append(valueArray[1]);
						}
					}
				}
			}
			sqlSb.append(conditionSql);
		}
		//拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			sqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
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

	public <T> List<T> findListBySymbolsCondition(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,
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
		
		List<T> returnList = null;
		//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select * from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK());
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			String field = null;
			String fieldValue = null;
			String symbols = null;
			String fieldType = null;
			StringBuffer conditionSql = new StringBuffer();
			Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
					.iterator();
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
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											fieldValue));
						}else if(fieldType.equals("class java.lang.String")){
							conditionSql.append("'").append(fieldValue).append(
							"'");
						} else
						{
							conditionSql.append(fieldValue);
						}
					} else if (symbols.equalsIgnoreCase(StaticValue.BETWEEN))
					{
						String[] valueArray = fieldValue.split(",");
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											valueArray[0], valueArray[1]));
						} else
						{
							conditionSql.append(valueArray[0]).append(" and ")
									.append(valueArray[1]);
						}
					}
				}
			}
			sqlSb.append(conditionSql);
		}
		//拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			sqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
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
	
	public <T> List<T> findListBySymbolsCondition(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select * from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK());
		//是否添加and
		boolean isAddAnd=false;
		//查询条件拼接
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			//获取实体类属性
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			//实体类属性名
			String field = null;
			//实体类属性值
			String fieldValue = null;
			//操作符
			String symbols = null;
			//实体类属性类型
			String fieldType = null;
			//查询条件
			StringBuffer conditionSql = new StringBuffer();
			Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
					.iterator();
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
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											fieldValue));
						}else if(fieldType.equals("class java.lang.String")){
							conditionSql.append("'").append(fieldValue).append(
							"'");
						} else
						{
							conditionSql.append(fieldValue);
						}
					} else if (symbols.equalsIgnoreCase(StaticValue.BETWEEN))
					{
						String[] valueArray = fieldValue.split(",");
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											valueArray[0], valueArray[1]));
						} else
						{
							conditionSql.append(valueArray[0]).append(" and ")
									.append(valueArray[1]);
						}
					}
				}
			}
			sqlSb.append(conditionSql);
		}
		//加上数据权限
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
			sqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				sqlSb.append(dom);
				isAddAnd=true;
			}
		}
		//拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			sqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
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

	public <T> List<T> findListBySymbolsCondition(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
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
		
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select * from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK());
		//是否添加and
		boolean isAddAnd=false;
		//查询条件拼接
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			//获取实体类属性
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			//实体类属性名
			String field = null;
			//实体类属性值
			String fieldValue = null;
			//操作符
			String symbols = null;
			//实体类属性类型
			String fieldType = null;
			//查询条件
			StringBuffer conditionSql = new StringBuffer();
			Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
					.iterator();
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
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											fieldValue));
						}else if(fieldType.equals("class java.lang.String")){
							conditionSql.append("'").append(fieldValue).append(
							"'");
						} else
						{
							conditionSql.append(fieldValue);
						}
					} else if (symbols.equalsIgnoreCase(StaticValue.BETWEEN))
					{
						String[] valueArray = fieldValue.split(",");
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											valueArray[0], valueArray[1]));
						} else
						{
							conditionSql.append(valueArray[0]).append(" and ")
									.append(valueArray[1]);
						}
					}
				}
			}
			sqlSb.append(conditionSql);
		}
		//加上数据权限
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
			sqlSb.append(dom);
			}else{
				String dom = new StringBuffer(" where (").append(
						columns.get("userId")).append(" in (").append(sysuserSql)
						.append(") or ").append(columns.get("userId")).append("=")
						.append(loginUserID).append(")").toString();
				sqlSb.append(dom);
				isAddAnd=true;
			}
		}
		//拼接排序条件
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			sqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
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
	
	public <T> List<T> findPageListByCondition(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		throw new MethodNotImplmentedExecption(" source : " + this.getClass());
	}
	
	public <T> List<T> findPageListByCondition(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		throw new MethodNotImplmentedExecption(" source : " + this.getClass());
	}

	public <T> List<T> findPageListBySymbolsCondition(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		return null;
	}
	
	public <T> List<T> findPageListBySymbolsCondition(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		return null;
	}
	
	public <T> List<T> findPageListBySymbolsConditionNoCount(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		return null;
	}
	
	public <T> List<T> findPageListBySymbolsConditionNoCount(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		return null;
	}

	//通过节点条件查询条数
	public Integer findLztj(String tableName,String wheresql)throws Exception{
		//拼接SQL
		StringBuffer sqltab = new StringBuffer("select count(*) as count from ");
		sqltab.append(tableName).append(" where ").append(wheresql);
		return getInt("count", sqltab.toString(),  StaticValue.EMP_POOLNAME);
	}

	public Map<String,String> findMapByTableCondition(String tableName,Long aiId) throws Exception{
		//拼接SQL
		String sql = new StringBuffer("select tb.* from ").append(tableName)
				.append(" tb ").append(" where ").append("tb.aiid=").append(
						aiId).toString();
		//获取数据库连接
		Connection conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
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
		} catch (SQLException e) {
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		}finally{
			// 关闭数据库资源
			close(rs,ps,conn);
		}
		return map;
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
		} catch (SQLException e) {
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
	
	/**
	 * 拼接查询条件方法
	 * @param conditionMap
	 * @param fields
	 * @param conditionSql
	 * @param columns
	 */
	protected void appendCondition(LinkedHashMap<String, String> conditionMap,
			Field[] fields,StringBuffer conditionSql,Map<String, String> columns)
	{
		Map.Entry<String, String> e = null;
		//属性名
		String field = null;
		//属性值
		String fieldValue = null;
		//操作符
		String symbols = null;
		//属性类型
		String fieldType = null;
		Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
				.iterator();
		//是否添加and
		boolean isAddAnd=false;
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
					conditionSql.append(" ").append(columns.get(field));
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
					}else if(fieldType.equals("class java.lang.String")){
						conditionSql.append("'").append(fieldValue).append(
						"'");
					}else
					{
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
	public <T> boolean update(Class<T> entityClass,
			LinkedHashMap<String, Object> objectMap, String id,
			LinkedHashMap<String, String> conditionMap) throws Exception
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
		if (!updateMap.entrySet().isEmpty()) {
			// 去掉最后一个逗号。
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
					if (fieldType.equals("class java.lang.String")) {
						for (int i = 0; i < valueArray.length; i++) {
							valueSb.append("'").append(valueArray[i]).append(
									"',");
						}
					} else {
						for (int i = 0; i < valueArray.length; i++) {
							valueSb.append(valueArray[i]).append(",");
						}
					}
					
					valueSb.deleteCharAt(valueSb.lastIndexOf(","));
					sqlSb.append(" and ").append(columns.get(e.getKey())).append(
							" in (").append(valueSb).append(")");
				}
			}
			String sql = sqlSb.toString();
			Connection conn = null;
			PreparedStatement ps = null;
			try
			{
				//获取数据库连接
				conn = connectionManager
						.getDBConnection(StaticValue.EMP_POOLNAME);
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
				// 关闭数据库资源
				super.close(null, ps, conn);
			}
		}
		return isSuccess;
	}
	
	public <T> boolean update(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, Object> objectMap, String id,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		//如果数据库连接为空并且isOutConn参数为true，则参数不合法
		if(conn==null&&isOutConn)
		{
			EmpExecutionContext.error("conn参数为null，isOutConn参数为true，参数不合法，不执行数据库操作。");
			return false;
		}
		//如果数据库连接不为空并且isOutConn参数为false，则参数不合法
		if(conn!=null&&!isOutConn)
		{
			EmpExecutionContext.error("conn参数不为null，isOutConn参数为false，参数不合法，不执行数据库操作。");
			return false;
		}
		
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
		if (!updateMap.entrySet().isEmpty()) {
			// 去掉最后一个逗号。
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
					if (fieldType.equals("class java.lang.String")) {
						for (int i = 0; i < valueArray.length; i++) {
							valueSb.append("'").append(valueArray[i]).append(
									"',");
						}
					} else {
						for (int i = 0; i < valueArray.length; i++) {
							valueSb.append(valueArray[i]).append(",");
						}
					}
					
					valueSb.deleteCharAt(valueSb.lastIndexOf(","));
					sqlSb.append(" and ").append(columns.get(e.getKey())).append(
							" in (").append(valueSb).append(")");
				}
			}
			String sql = sqlSb.toString();
			//Connection conn = null;
			PreparedStatement ps = null;
			try
			{
				//获取数据库连接
				if(conn==null)
				{
					conn = connectionManager
							.getDBConnection(StaticValue.EMP_POOLNAME);
				}
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
				if(isOutConn)
				{
					//方法外部传递数据库连接进来,在方法内部不关闭连接
					//关闭数据库资源
					super.close(null, ps);
				}
				else
				{
					//方法内部生成数据库连接，需要在方法内关闭连接
					//关闭数据库资源
					super.close(null, ps,conn);
				}
			}
		}
		return isSuccess;
	}
	
	public <T> List<String[]> findListByConditionByColumNameWithGroupBy(Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,String columName,
			LinkedHashMap<String, String> orderbyMap,String groupColum) throws Exception
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
			sqlSb.append(" where ");
			StringBuffer condtionSb = new StringBuffer();
			Field[] fields = entityClass.getDeclaredFields();
			appendCondition(conditionMap,fields,condtionSb,columns);
			sqlSb.append(condtionSb);
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
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			//抛出异常
			throw ex;
		} finally
		{
			// 关闭数据库资源
			super.close(rs, ps, conn);
		}
		//返回结果
		return returnList;
	}
	
	public <T> List<String[]> findListByConditionByColumNameWithGroupBy(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,String columName,
			LinkedHashMap<String, String> orderbyMap,String groupColum) throws Exception
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
		String[] columArray = columName.split(",");
	//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer("select ").append(columName).append(" from ").append(
				columns.get(entityClass.getSimpleName())).append(StaticValue.getWITHNOLOCK());
		// 拼接查询条件
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			sqlSb.append(" where ");
			StringBuffer condtionSb = new StringBuffer();
			Field[] fields = entityClass.getDeclaredFields();
			appendCondition(conditionMap,fields,condtionSb,columns);
			sqlSb.append(condtionSb);
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
			EmpExecutionContext.error(ex,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			//抛出异常
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
		//返回结果
		return returnList;
	}
	
	public Long saveObjectReturnIDWithTri(Object object) throws Exception
	{
		return null;
	}
	
	
}