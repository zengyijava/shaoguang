package com.montnets.emp.engine.vo.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.exception.SequenceNameNotFoundException;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author Administrator
 * 
 */
public class EngineSuperDAO
{

	protected IConnectionManager connectionManager;

	public EngineSuperDAO()
	{
		connectionManager = new ConnectionManagerImp();
	}

	/**
	 * 关闭数据库资源
	 * @param rs
	 * @param ps
	 * @param conn
	 * @throws Exception
	 */
	public void close(ResultSet rs, PreparedStatement ps, Connection conn)
			throws Exception
	{
		if (rs != null)
		{
			rs.close();
		}
		if (ps != null)
		{
			ps.close();
		}
		if (conn != null)
		{
			conn.close();
		}
	}

	/**
	 * 关闭数据库资源
	 * @param rs
	 *            ResultSet参数
	 * @param ps
	 *            PreparedStatement参数
	 * @throws Exception
	 */
	public void close(ResultSet rs, PreparedStatement ps) throws Exception
	{
		if (rs != null)
		{
			rs.close();
		}
		if (ps != null)
		{
			ps.close();
		}
	}

	/**
	 * 
	 * @param entityClass
	 * @param tablePackagePath
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getORMMap(Class<?> entityClass) throws Exception
	{
		//表名
		String tableName = "Table" + entityClass.getSimpleName();
		String tablePath = entityClass.getPackage().getName() + ".table."
				+ tableName;
		Class<?> tableClass = Class.forName(tablePath);
		Method getMethod = tableClass.getMethod("getORM");
		Map<String, String> columns = (Map<String, String>) getMethod
				.invoke(tableClass);
		return columns;
	}

	/**
	 * 获取实体类与数据库表字段的MAP映射
	 * @param entityClass
	 * @param tablePackagePath
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getVoORMMap(Class<?> voClass) throws Exception
	{
		//表名
		String tableName = "View" + voClass.getSimpleName();
		//xxxVo所在路径
		String tablePath = voClass.getPackage().getName() + ".view."
				+ tableName;
		Class<?> tableClass = Class.forName(tablePath);
		Method getMethod = tableClass.getMethod("getORM");
		Map<String, String> columns = (Map<String, String>) getMethod
				.invoke(tableClass);
		//返回结果
		return columns;
	}

	/**
	 * 
	 * @param connectionManager
	 * @param POOLNAME
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public boolean executeBySQL(String sql, String POOLNAME) throws Exception
	{
		boolean b = false;
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
			//执行sql
			int count = ps.executeUpdate();
			if (count > 0)
			{
				b = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(null, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		return b;
	}

	/**
	 * cretor：lxh
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Integer executeBySQLReturnCount(String sql, String POOLNAME)
			throws Exception
	{
		Integer count = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
			//执行sql
			count = ps.executeUpdate();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(null, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		return count;
	}

	/**
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public boolean executeBySQL(Connection conn, String sql) throws Exception
	{
		boolean b = false;
		PreparedStatement ps = null;
		try
		{
			ps = conn.prepareStatement(sql);
			//执行sql
			int count = ps.executeUpdate();
			if (count > 0)
			{
				b = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(null, ps);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		return b;
	}

	/**
	 * cretor：lxh
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Integer executeBySQLReturnCount(Connection conn, String sql)
			throws Exception
	{
		Integer count = 0;
		PreparedStatement ps = null;
		try
		{
			ps = conn.prepareStatement(sql);
			//执行sql
			count = ps.executeUpdate();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(null, ps);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		return count;
	}

	/**
	 * 
	 * @param connectionManager
	 * @param entityClass
	 * @param sql
	 * @param TABLE_PATH
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findEntityListBySQL(Class<T> entityClass, String sql,
			String POOLNAME) throws Exception
	{
		List<T> returnList = null;
		//获取实体类与数据库表字段的MAP映射
		Map<String, String> columns = getORMMap(entityClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
			//执行查询
			rs = ps.executeQuery();
			//resultSet对象转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		return returnList;
	}

	/**
	 * 
	 * @param connectionManager
	 * @param entityClass
	 * @param sql
	 * @param TABLE_PATH
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPartEntityListBySQL(Class<T> entityClass,
			String sql, String POOLNAME) throws Exception
	{
		List<T> returnList = null;
		//获取实体类与数据库表字段的MAP映射
		Map<String, String> columns = getORMMap(entityClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
			//执行查询
			rs = ps.executeQuery();
			//resultSet对象转换为list集合
			returnList = partrsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		//返回结果
		return returnList;
	}

	/**
	 * 
	 * @param connectionManager
	 * @param entityClass
	 * @param sql
	 * @param TABLE_PATH
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPartEntityListBySQL(Connection conn,
			Class<T> entityClass, String sql, String POOLNAME) throws Exception
	{
		List<T> returnList = null;
		//获取实体类与数据库表字段的MAP映射
		Map<String, String> columns = getORMMap(entityClass);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = conn.prepareStatement(sql);
			//执行查询
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = partrsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(rs, ps, null);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		//返回结果
		return returnList;
	}

	/**
	 * 
	 * @param connectionManager
	 * @param voClass
	 * @param sql
	 * @param TABLE_PATH
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findVoListBySQL(Class<T> voClass, String sql,
			String POOLNAME) throws Exception
	{
		List<T> returnList = null;
		//获取实体类与数据库表字段的MAP映射
		Map<String, String> columns = getVoORMMap(voClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
			//执行查询
			rs = ps.executeQuery();
			//resultset转化为list集合
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		return returnList;
	}

	public <T> List<T> findVoListBySQL(Class<T> voClass, String sql,
			String POOLNAME, List<String> timeList) throws Exception
	{
		List<T> returnList = null;
		//获取实体类与数据库表字段的MAP映射
		Map<String, String> columns = getVoORMMap(voClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
			int psIndex = 0;
			if (timeList != null && timeList.size() > 0)
			{
				for (int i = 0; i < timeList.size(); i++)
				{
					psIndex++;
					String time = timeList.get(i);
					ps.setTimestamp(psIndex, Timestamp.valueOf(time));
				}
			}
			//执行查询
			rs = ps.executeQuery();
			//resultset对象转换为list集合
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		//返回结果
		return returnList;
	}

	/**
	 * 
	 * @param rs
	 * @param cls
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> rsToList(ResultSet rs, Class<T> cls,
			Map<String, String> columns) throws Exception
	{
		//获取实体类属性
		Field[] fields = cls.getDeclaredFields();
		List<T> ls = new ArrayList<T>();

		T o = null;
		//属性类型
		String fieldType = null;
		//属性名称
		String fieldName = null;
		//属性名大写
		String fieldNameUpper = null;
		while (rs.next())
		{
			o = (T) cls.newInstance();
			for (int i = 0; i < fields.length; i++)
			{
				fieldType = fields[i].getType().getSimpleName();
				fieldName = fields[i].getName();
				fieldNameUpper = Character.toUpperCase(fieldName.charAt(0))
						+ fieldName.substring(1);
				if (columns.get(fieldName) != null)
				{
					if (fieldType.equals("String"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, String.class);
						setMethod.invoke(o, rs.getString(columns.get(fieldName)
								.toString()));
					} else if (fieldType.equals("Float")
							|| fieldType.equals("float"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, float.class);
						setMethod.invoke(o, rs.getFloat(columns.get(fieldName)
								.toString()));
					} else if (fieldType.equals("Long")
							|| fieldType.equals("long"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, Long.class);
						setMethod.invoke(o, rs.getLong(columns.get(fieldName)
								.toString().replace("\"", "")));
					} else if (fieldType.equals("Integer")
							|| fieldType.equals("int"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, Integer.class);
						setMethod.invoke(o, rs.getInt(columns.get(fieldName)
								.toString().replace("\"", "")));
					} else if (fieldType.equals("Double")
							|| fieldType.equals("double"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, Double.class);
						setMethod.invoke(o, rs.getDouble(columns.get(fieldName)
								.toString()));
					} else if (fieldType.equals("Timestamp"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, Timestamp.class);
						setMethod.invoke(o, rs.getTimestamp(columns.get(
								fieldName).toString()));
					} else if (fieldType.equals("Boolean")
							|| fieldType.equals("boolean"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, Boolean.class);
						setMethod.invoke(o, rs.getBoolean(columns
								.get(fieldName).toString()));
					} else if (fieldType.equals("Byte"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, Byte.class);
						setMethod.invoke(o, rs.getByte(columns.get(fieldName)
								.toString()));
					}
				}
			}
			ls.add(o);
		}
		return ls;
	}

	/**
	 * 
	 * @param rs
	 * @param cls
	 * @param columns
	 * @author wuxiaotao
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> partrsToList(ResultSet rs, Class<T> cls,
			Map<String, String> columns) throws Exception
	{
		int columnCount = rs.getMetaData().getColumnCount();
		String[] columnName = new String[columnCount];

		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
		{
			columnName[columnIndex] = rs.getMetaData().getColumnName(
					columnIndex + 1);
		}
		//获取实体类属性
		Field[] fields = cls.getDeclaredFields();
		List<T> ls = new ArrayList<T>();

		T o = null;
		//属性类型
		String fieldType = null;
		//属性名
		String fieldName = null;
		//属性名大写
		String fieldNameUpper = null;
		while (rs.next())
		{
			o = (T) cls.newInstance();
			for (int i = 0; i < fields.length; i++)
			{
				fieldType = fields[i].getType().getSimpleName();
				fieldName = fields[i].getName();
				fieldNameUpper = Character.toUpperCase(fieldName.charAt(0))
						+ fieldName.substring(1);
				if (!fieldNameUpper.equals("SerialVersionUID"))
				{
					for (int index = 0; index < columnName.length; index++)
					{
						String tempcolumn = columnName[index].toUpperCase();

						if (fieldType.equals("String"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, String.class);
							if (tempcolumn.equals(columns.get(fieldName)))
								setMethod.invoke(o, rs.getString(columns.get(
										fieldName).toString()));
						} else if (fieldType.equals("Long")
								|| fieldType.equals("long"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Long.class);
							if (tempcolumn.equals(columns.get(fieldName)
									.toString().replace("\"", "")))
								setMethod.invoke(o, rs
										.getLong(columns.get(fieldName)
												.toString().replace("\"", "")));
						} else if (fieldType.equals("Float")
								|| fieldType.equals("float"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, float.class);
							if (tempcolumn.equals(columns.get(fieldName)))
								setMethod.invoke(o, rs.getFloat(columns.get(
										fieldName).toString()));
						} else if (fieldType.equals("Integer")
								|| fieldType.equals("int"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Integer.class);
							if (tempcolumn.equals(columns.get(fieldName)
									.toString().replace("\"", "")))
								setMethod.invoke(o, rs
										.getInt(columns.get(fieldName)
												.toString().replace("\"", "")));
						} else if (fieldType.equals("Double")
								|| fieldType.equals("double"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Double.class);
							if (tempcolumn.equals(columns.get(fieldName)))
								setMethod.invoke(o, rs.getDouble(columns.get(
										fieldName).toString()));
						} else if (fieldType.equals("Timestamp"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Timestamp.class);
							if (tempcolumn.equals(columns.get(fieldName)))
								setMethod.invoke(o, rs.getTimestamp(columns
										.get(fieldName).toString()));
						} else if (fieldType.equals("Byte"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Byte.class);
							if (tempcolumn.equals(columns.get(fieldName)))
								setMethod.invoke(o, rs.getByte(columns.get(
										fieldName).toString()));
						}
					}
				}
			}
			ls.add(o);
		}
		return ls;
	}

	public String getString(String columnName, String sql, String poolName)
			throws Exception
	{

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(poolName);
			ps = conn.prepareStatement(sql);
			//执行查询
			rs = ps.executeQuery();

			if (rs.next())
			{
				return rs.getString(columnName);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		return null;
	}

	public int getInt(String columnName, String sql, String poolName)
			throws Exception
	{

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(poolName);
			ps = conn.prepareStatement(sql);
			//执行查询
			rs = ps.executeQuery();

			if (rs.next())
			{
				return rs.getInt(columnName);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param sequenceName
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	protected Long getSequenceNextValue(Connection conn, String sequenceName)
	{
		if (sequenceName == null)
		{
			throw new SequenceNameNotFoundException();
		}

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try
		{
			//拼接sql
			String sql = " select " + sequenceName + ".nextval from dual";
			preparedStatement = conn.prepareStatement(sql);
			//执行查询
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
			{
				return resultSet.getLong(1);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
		} finally
		{
			try
			{
				//关闭数据库资源
				close(resultSet, preparedStatement);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源异常！");
			}
		}
		return null;
	}

	protected int findCountBySQL(String countSql)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(countSql);
			//执行查询
			rs = ps.executeQuery();
			if (rs.next())
			{
				totalCount = rs.getInt("totalcount");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
		} finally
		{
			try
			{
				//关闭数据库资源
				close(rs, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		return totalCount;
	}

	public int findCountBySQL(String countSql, List<String> timeList)
			throws Exception
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(countSql);
			int psIndex = 0;
			if (timeList != null && timeList.size() > 0)
			{
				for (int i = 0; i < timeList.size(); i++)
				{
					psIndex++;
					String time = timeList.get(i);
					ps.setTimestamp(psIndex, Timestamp.valueOf(time));
				}
			}
			//执行查询
			rs = ps.executeQuery();
			if (rs.next())
			{
				totalCount = rs.getInt("totalcount");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
		} finally
		{
			try
			{
				//关闭数据库资源
				close(rs, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		return totalCount;
	}

	/**
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		return findPageVoListBySQL(voClass, sql, countSql, pageInfo, POOLNAME,
				null, null);
	}

	/**
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			Map<String, String> columns) throws Exception
	{
		return findPageVoListBySQL(voClass, sql, countSql, pageInfo, POOLNAME,
				null, columns);
	}

	/**
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList) throws Exception
	{
		return findPageVoListBySQL(voClass, sql, countSql, pageInfo, POOLNAME,
				timeList, null);
	}

	/**
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			PageInfo pageInfo, String POOLNAME, List<String> timeList)
			throws Exception
	{
		return findPageVoListBySQL(voClass, sql, null, pageInfo, POOLNAME,
				timeList, null);
	}

	/**
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			PageInfo pageInfo, String POOLNAME, List<String> timeList,
			Map<String, String> columns) throws Exception
	{
		return findPageVoListBySQL(voClass, sql, null, pageInfo, POOLNAME,
				timeList, columns);
	}

	protected <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
		{
			//oracle数据库
			return this.findPageVoListBySQLOracle(voClass, sql, countSql,
					pageInfo, POOLNAME, timeList, columns);
		} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
		{
			//sqlserver数据库
			return this.findPageVoListBySQLSqlServer(voClass, sql, countSql,
					pageInfo, POOLNAME, timeList, columns);
		} else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//db2数据库
			return this.findPageVoListBySQLDB2(voClass, sql, countSql,
					pageInfo, POOLNAME, timeList, columns);
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//mysql数据库
			return this.findPageVoListBySQLMySql(voClass, sql, countSql,
					pageInfo, POOLNAME, timeList, columns);
		}
		return null;
	}

	private <T> List<T> findPageVoListBySQLMySql(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
			//获取实体类与数据库表字段的MAP映射
			columns = getVoORMMap(voClass);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			// 获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			if (countSql != null)
			{
				EmpExecutionContext.sql("execute sql : " + countSql);
				ps = conn.prepareStatement(countSql);
				// 查询条件为时间
				if (timeList != null && timeList.size() > 0)
				{
					int psIndex = 0;
					for (int i = 0; i < timeList.size(); i++)
					{
						psIndex++;
						String time = timeList.get(i);
						ps.setTimestamp(psIndex, Timestamp.valueOf(time));
					}
				}
				// 执行SQL查询语句，返回ResultSet
				rs = ps.executeQuery();
				if (rs.next())
				{
					//当前页
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
					- (pageInfo.getPageSize() - 1) - 1;
			// 结束行数
			int endCount = pageInfo.getPageSize();
			sql = sql + new StringBuffer().append(" limit ").append(beginCount)
					.append(",").append(endCount).toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			// 查询条件为时间
			if (timeList != null && timeList.size() > 0)
			{
				int psIndex = 0;
				for (int i = 0; i < timeList.size(); i++)
				{
					psIndex++;
					String time = timeList.get(i);
					ps.setTimestamp(psIndex, Timestamp.valueOf(time));
				}
			}
			rs = ps.executeQuery();
			// 将ResultSet转换成List
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			// 关闭数据库资源。
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源出错！");
			}
		}
		return returnList;
	}

	private <T> List<T> findPageVoListBySQLOracle(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
			//获取实体类与数据库表字段的MAP映射
			columns = getVoORMMap(voClass);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			if (countSql != null)
			{
				ps = conn.prepareStatement(countSql);
				EmpExecutionContext.sql("execute sql : " + countSql);
				if (timeList != null && timeList.size() > 0)
				{
					int psIndex = 0;
					for (int i = 0; i < timeList.size(); i++)
					{
						psIndex++;
						String time = timeList.get(i);
						ps.setTimestamp(psIndex, Timestamp.valueOf(time));
					}
				}
				//执行查询
				rs = ps.executeQuery();
				if (rs.next())
				{
					//当前页
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
			//开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			//结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			StringBuffer sqlSb = new StringBuffer();
			if (pageInfo.getPageIndex() <= 500000)
			{
				sqlSb.append("select * from (select t.*, rownum rn from (")
						.append(sql).append(
								") t where rownum<=" + endCount
										+ ") where rn>=" + beginCount);
			} else
			{
				sqlSb.append("select * from (select t.*, rownum rn from (")
						.append(sql).append(") t) where  rn between ").append(
								beginCount).append(" and ").append(endCount);
			}
			sql = sqlSb.toString();
			ps = conn.prepareStatement(sql);
			EmpExecutionContext.sql(sql);
			// 查询条件为时间
			if (timeList != null && timeList.size() > 0)
			{
				int psIndex = 0;
				for (int i = 0; i < timeList.size(); i++)
				{
					psIndex++;
					String time = timeList.get(i);
					ps.setTimestamp(psIndex, Timestamp.valueOf(time));
				}
			}
			//执行查询
			rs = ps.executeQuery();
			//resultset对象转换为list集合
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{

			try
			{
				//关闭数据库资源
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		//返回结果
		return returnList;
	}

	private <T> List<T> findPageVoListBySQLSqlServer(Class<T> voClass,
			String sql, String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
			//获取实体类与数据库表字段的MAP映射
			columns = getVoORMMap(voClass);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			// 获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			if (countSql != null)
			{
				EmpExecutionContext.sql("execute sql : " + countSql);
				ps = conn.prepareStatement(countSql);
				// 查询条件为时间
				if (timeList != null && timeList.size() > 0)
				{
					int psIndex = 0;
					for (int i = 0; i < timeList.size(); i++)
					{
						psIndex++;
						String time = timeList.get(i);
						ps.setTimestamp(psIndex, Timestamp.valueOf(time));
					}
				}
				// 执行SQL查询语句，返回ResultSet
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
			StringBuffer sqlSb = new StringBuffer();
			sql = sql.toUpperCase();
			if (sql.indexOf("DISTINCT") == -1 || sql.indexOf("DISTINCT") > 20)
			{
				sql = sql.substring(sql.indexOf("SELECT") + 7, sql.length());
				sql = new StringBuffer("select top ").append(endCount).append(
						" 0 as tempColumn,").append(sql).toString();
			} else
			{
				sql = sql.substring(sql.indexOf("DISTINCT") + 9, sql.length());
				sql = new StringBuffer("select distinct top ").append(endCount)
						.append(" 0 as tempColumn,").append(sql).toString();
			}
			sqlSb
					.append(
							"select * from (select row_number() over(order by tempColumn) tempRowNumber,* from (")
					.append(sql).append(
							") t) tt where tempRowNumber>=" + beginCount);
			sql = sqlSb.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			// 查询条件为时间
			if (timeList != null && timeList.size() > 0)
			{
				int psIndex = 0;
				for (int i = 0; i < timeList.size(); i++)
				{
					psIndex++;
					String time = timeList.get(i);
					ps.setTimestamp(psIndex, Timestamp.valueOf(time));
				}
			}
			rs = ps.executeQuery();
			// 将ResultSet转换成List
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			// 关闭数据库资源。
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源出错！");
			}
		}
		return returnList;
	}

	private <T> List<T> findPageVoListBySQLDB2(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
			//获取实体类与数据库表字段的MAP映射
			columns = getVoORMMap(voClass);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			if (countSql != null)
			{
				ps = conn.prepareStatement(countSql);
				EmpExecutionContext.sql("execute sql : " + countSql);
				if (timeList != null && timeList.size() > 0)
				{
					int psIndex = 0;
					for (int i = 0; i < timeList.size(); i++)
					{
						psIndex++;
						String time = timeList.get(i);
						ps.setTimestamp(psIndex, Timestamp.valueOf(time));
					}
				}
				//执行查询
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
					//当前页大于最大页数，跳转到第一页
					if (pageInfo.getPageIndex() > totalPage)
					{
						pageInfo.setPageIndex(1);
					}
				}
			}
			//开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
			//结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			StringBuffer sqlSb = new StringBuffer();
			sqlSb
					.append(
							"select * from(select row_number() over() as rownum ,t.* from (")
					.append(sql).append(")t )temp_t where rownum between ")
					.append(beginCount).append(" and ").append(endCount);
			sql = sqlSb.toString();
			ps = conn.prepareStatement(sql);
			EmpExecutionContext.sql(sql);
			//查询条件---时间
			if (timeList != null && timeList.size() > 0)
			{
				int psIndex = 0;
				for (int i = 0; i < timeList.size(); i++)
				{
					psIndex++;
					String time = timeList.get(i);
					ps.setTimestamp(psIndex, Timestamp.valueOf(time));
				}
			}
			//执行查询
			rs = ps.executeQuery();
			//resultset对象转换为list集合
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQl异常。");
			throw e;
		} finally
		{
			//关闭数据库资源
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库连接异常。");
			}
		}
		return returnList;
	}
}
