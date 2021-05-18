package com.montnets.emp.common.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.exception.SequenceNameNotFoundException;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 *
 */
public class SuperDAO implements ISuperDAO
{

	protected IConnectionManager connectionManager;

	public SuperDAO()
	{
		connectionManager = new ConnectionManagerImp();
	}

	/**
	 * 关闭连接
	 *
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
	 * 关闭连接
	 *
	 * @param rs
	 * @param ps
	 * @param conn
	 * @throws Exception
	 */
	public void close(ResultSet rs, CallableStatement cs, Connection conn)
			throws Exception
	{
		if (rs != null)
		{
			rs.close();
		}
		if (cs != null)
		{
			cs.close();
		}
		if (conn != null)
		{
			conn.close();
		}
	}

	/**
	 *
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
		String tableName = "Table" + entityClass.getSimpleName();
		String tablePath = entityClass.getPackage().getName().replace("entity",
				"table")
				+ "." + tableName;
		Class<?> tableClass = Class.forName(tablePath);
		Method getMethod = tableClass.getMethod("getORM");
		Map<String, String> columns = (Map<String, String>) getMethod
				.invoke(tableClass);
		return columns;
	}

	/**
	 *
	 * @param entityClass
	 * @param tablePackagePath
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getVoORMMap(Class<?> voClass) throws Exception
	{
		String tableName = "View" + voClass.getSimpleName();
		String tablePath = voClass.getPackage().getName() + ".view."
				+ tableName;
		Class<?> tableClass = Class.forName(tablePath);
		Method getMethod = tableClass.getMethod("getORM");
		Map<String, String> columns = (Map<String, String>) getMethod
				.invoke(tableClass);
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
			conn = connectionManager.getDBConnection(POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			int count = ps.executeUpdate();
			if (count > 0)
			{
				b = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				close(null, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
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
			conn = connectionManager.getDBConnection(POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			count = ps.executeUpdate();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				close(null, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
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
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			int count = ps.executeUpdate();
			if (count > 0)
			{
				b = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				close(null, ps);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
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
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			count = ps.executeUpdate();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				close(null, ps);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
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
		Map<String, String> columns = getORMMap(entityClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		return returnList;
	}

	/**
	 * @description 新增方法，找到所有员工或客户
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return returnList
	 * @throws Exception
	 */
	protected <T> List<T> findAllVoListBySQL(Class<T> voClass, String sql,
			String POOLNAME, List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
			columns = getVoORMMap(voClass);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{

			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
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

			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{

			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		return returnList;
	}

	/**
	 * @description 删除时找到所有员工或客户
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return List
	 * @throws Exception
	 */
	protected <T> List<T> findAllVoListBySQL(Class<T> voClass, String sql,
			String countSql, String POOLNAME, List<String> timeList,
			Map<String, String> columns) throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
			columns = getVoORMMap(voClass);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{

			conn = connectionManager.getDBConnection(POOLNAME);

			// int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
			// - (pageInfo.getPageSize() - 1);
			// int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			StringBuffer sqlSb = new StringBuffer();

			// sqlSb.append("select * from (select t.*, rownum rn from (")
			// .append(sql).append(") t)");
			sqlSb.append(sql);
			sql = sqlSb.toString();
			ps = conn.prepareStatement(sql);
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

			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{

			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "根据sql获取对象集合异常。");
			}
		}
		return returnList;
	}

	/**
	 * @description 获得通讯录类型
	 * @param <T>
	 * @param voClass
	 * @param pageInfo
	 * @param sql
	 * @param countSql
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return List
	 * @throws Exception
	 */
	public <T> List<T> findVoTypeListBySQL(Class<T> voClass, PageInfo pageInfo,
			String sql, String countSql, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
			columns = getVoORMMap(voClass);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{

			conn = connectionManager.getDBConnection(POOLNAME);
			if (countSql != null)
			{
				ps = conn.prepareStatement(countSql);
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
				if (rs.next())
				{
					int pageSize = pageInfo.getPageSize();
					int totalCount = rs.getInt("totalcount");
					int totalPage = totalCount % pageSize == 0 ? totalCount
							/ pageSize : totalCount / pageSize + 1;
					pageInfo.setTotalRec(totalCount);
					pageInfo.setTotalPage(totalPage);

					if (pageInfo.getPageIndex() > totalPage)
					{
						pageInfo.setPageIndex(1);
					}
				}
			}
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);
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

			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{

			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		return returnList;
	}

	// 获得所有通讯录类型
	public <T> List<T> findAllVoTypeListBySQL(Class<T> voClass, String sql,
			String POOLNAME, List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
			columns = getVoORMMap(voClass);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{

			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "根据sql语句查询并获取对象集合异常。");
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
	 * @return list
	 * @throws Exception
	 */
	public <T> List<T> findPartEntityListBySQL(Class<T> entityClass,
			String sql, String POOLNAME) throws Exception
	{
		List<T> returnList = null;
		Map<String, String> columns = getORMMap(entityClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			returnList = partrsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
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
	public <T> List<T> findPartEntityListBySQL(Connection conn,
			Class<T> entityClass, String sql, String POOLNAME) throws Exception
	{
		List<T> returnList = null;
		Map<String, String> columns = getORMMap(entityClass);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			returnList = partrsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, null);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
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
		Map<String, String> columns = getVoORMMap(voClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		return returnList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.montnets.emp.dao.ISuperDAO#findVoListBySQL(java.lang.Class,
	 * java.lang.String, java.lang.String, java.util.List)
	 */
	public <T> List<T> findVoListBySQL(Class<T> voClass, String sql,
			String POOLNAME, List<String> timeList) throws Exception
	{
		List<T> returnList = null;
		Map<String, String> columns = getVoORMMap(voClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
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
			rs = ps.executeQuery();
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		return returnList;
	}

	/**
	 * 结果集处理
	 *
	 * @param rs
	 * @param cls
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> rsToList(ResultSet rs, Class<T> cls,
			Map<String, String> columns)
	{
		Field[] fields = cls.getDeclaredFields();
		List<T> ls = new ArrayList<T>();

		T o = null;
		String fieldType = null;
		String fieldName = null;
		String fieldNameUpper = null;
		try
		{
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
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, String.class);
							setMethod.invoke(o, rs.getString(columns.get(
									fieldName).toString()));
						} else if (fieldType.equals("Float")
								|| fieldType.equals("float"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, float.class);
							setMethod.invoke(o, rs.getFloat(columns.get(
									fieldName).toString()));
						} else if (fieldType.equals("Long")
								|| fieldType.equals("long"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Long.class);
							setMethod.invoke(o, rs.getLong(columns.get(
									fieldName).toString().replace("\"", "")));
						} else if (fieldType.equals("Integer")
								|| fieldType.equals("int"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Integer.class);
							setMethod.invoke(o, rs.getInt(columns
									.get(fieldName).toString()
									.replace("\"", "")));
						} else if (fieldType.equals("Double")
								|| fieldType.equals("double"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Double.class);
							setMethod.invoke(o, rs.getDouble(columns.get(
									fieldName).toString()));
						} else if (fieldType.equals("Timestamp"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Timestamp.class);
							setMethod.invoke(o, rs.getTimestamp(columns.get(
									fieldName).toString()));
						} else if (fieldType.equals("Boolean")
								|| fieldType.equals("boolean"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Boolean.class);
							setMethod.invoke(o, rs.getBoolean(columns.get(
									fieldName).toString()));
						} else if (fieldType.equals("Byte"))
						{
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Byte.class);
							setMethod.invoke(o, rs.getByte(columns.get(
									fieldName).toString()));
						}
					}
				}
				ls.add(o);
			}
		} catch (SecurityException e)
		{
			EmpExecutionContext.error(e, "结果集处理异常！");
		} catch (IllegalArgumentException e)
		{
			EmpExecutionContext.error(e, "结果集处理异常！");
		} catch (SQLException e)
		{
			EmpExecutionContext.error(e, "结果集处理异常！");
		} catch (InstantiationException e)
		{
			EmpExecutionContext.error(e, "结果集处理异常！");
		} catch (IllegalAccessException e)
		{
			EmpExecutionContext.error(e, "结果集处理异常！");
		} catch (NoSuchMethodException e)
		{
			EmpExecutionContext.error(e, "结果集处理异常！");
		} catch (InvocationTargetException e)
		{
			EmpExecutionContext.error(e, "结果集处理异常！");
		}
		return ls;
	}

	/**
	 *
	 * @param rs
	 * @param cls
	 * @param columns
	 * @author lj
	 * @return list
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

		Field[] fields = cls.getDeclaredFields();
		List<T> ls = new ArrayList<T>();

		T o = null;
		String fieldType = null;
		String fieldName = null;
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

						if (columns.get(fieldName) != null)
						{
							if (fieldType.equals("String"))
							{
								Method setMethod = cls.getMethod("set"
										+ fieldNameUpper, String.class);
								if (tempcolumn.equals(columns.get(fieldName)))
									setMethod.invoke(o, rs.getString(columns
											.get(fieldName).toString()));
							} else if (fieldType.equals("Long")
									|| fieldType.equals("long"))
							{
								Method setMethod = cls.getMethod("set"
										+ fieldNameUpper, Long.class);
								if (tempcolumn.equals(columns.get(fieldName)
										.toString().replace("\"", "")))
									setMethod.invoke(o, rs.getLong(columns.get(
											fieldName).toString().replace("\"",
											"")));
							} else if (fieldType.equals("Float")
									|| fieldType.equals("float"))
							{
								Method setMethod = cls.getMethod("set"
										+ fieldNameUpper, float.class);
								if (tempcolumn.equals(columns.get(fieldName)))
									setMethod.invoke(o, rs.getFloat(columns
											.get(fieldName).toString()));
							} else if (fieldType.equals("Integer")
									|| fieldType.equals("int"))
							{
								Method setMethod = cls.getMethod("set"
										+ fieldNameUpper, Integer.class);
								if (tempcolumn.equals(columns.get(fieldName)
										.toString().replace("\"", "")))
									setMethod.invoke(o, rs.getInt(columns.get(
											fieldName).toString().replace("\"",
											"")));
							} else if (fieldType.equals("Double")
									|| fieldType.equals("double"))
							{
								Method setMethod = cls.getMethod("set"
										+ fieldNameUpper, Double.class);
								if (tempcolumn.equals(columns.get(fieldName)))
									setMethod.invoke(o, rs.getDouble(columns
											.get(fieldName).toString()));
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
			}
			ls.add(o);
		}
		return ls;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.montnets.emp.dao.ISuperDAO#partrsToMap(java.sql.ResultSet)
	 */
	public Map<String, String> partrsToMap(ResultSet rs) throws Exception
	{
		int columnCount = rs.getMetaData().getColumnCount();
		Map<String, String> map = new HashMap<String, String>();
		if (rs.next())
		{
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++)
			{
				if (rs.getObject(columnIndex) == null)
				{
					map.put(rs.getMetaData().getColumnName(columnIndex), null);
				} else
				{
					map.put(rs.getMetaData().getColumnName(columnIndex), rs
							.getObject(columnIndex).toString());
				}
			}
		}
		return map;
	}

	/**
	 * @param columnName
	 * @param sql
	 * @param poolName
	 * @return String
	 * @throws Exception
	 */
	public String getString(String columnName, String sql, String poolName)
			throws Exception
	{

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(poolName);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next())
			{
				return rs.getString(columnName);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
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
			conn = connectionManager.getDBConnection(poolName);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next())
			{
				return rs.getInt(columnName);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		return -1;
	}

	/**
	 *
	 * @param sequenceName
	 * @param conn
	 * @return Long
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
			String sql = " select " + sequenceName + ".nextval from dual";
			EmpExecutionContext.sql("execute sql : " + sql);
			preparedStatement = conn.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
			{
				return resultSet.getLong(1);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取序列下一个id异常。");
		} finally
		{
			try
			{
				close(resultSet, preparedStatement);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取序列下一个id异常。");
			}
		}
		return null;
	}

	/**
	 * @param countSql
	 * @return
	 */
	public int findCountBySQL(String countSql)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			rs = ps.executeQuery();
			if (rs.next())
			{
				totalCount = rs.getInt("totalcount");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据sql语句查询数量异常。");
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		return totalCount;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.montnets.emp.dao.ISuperDAO#findCountBySQL(java.lang.String,
	 * java.util.List)
	 */
	public int findCountBySQL(String countSql, List<String> timeList)
			throws Exception
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
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
			rs = ps.executeQuery();
			if (rs.next())
			{
				totalCount = rs.getInt("totalcount");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询总页数出错！");
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		return totalCount;
	}

//	/**
//	 * @param sql
//	 * @return
//	 * @throws Exception
//	 */
//	public String[] findStrArrayBySQL(String sql) throws Exception
//	{
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		String[] strArray = null;
//		try
//		{
//			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
//			EmpExecutionContext.sql("execute sql : " + sql);
//			ps = conn.prepareStatement(sql);
//			rs = ps.executeQuery();
//			int rowIndex = rs.getRow();
//			strArray = new String[rowIndex];
//
//			int index = 0;
//			while (rs.next())
//			{
//				strArray[index] = rs.getString(0);
//			}
//		} catch (Exception e)
//		{
//			EmpExecutionContext.error(e,"查询总页数出错！");
//		} finally
//		{
//			try
//			{
//				close(rs, ps, conn);
//			} catch (Exception e)
//			{
//				EmpExecutionContext.error(e,"关闭数据库资源出错！");
//			}
//		}
//		return strArray;
//	}

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
	public <T> List<T> findVoListBySQL(Connection conn, Class<T> voClass,
			String sql, String POOLNAME) throws Exception
	{
		List<T> returnList = null;
		// 获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getVoORMMap(voClass);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = conn.prepareStatement(sql);
			// 执行sql
			rs = ps.executeQuery();
			// resultSet转换为list集合
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			try
			{
				// 关闭数据库资源
				close(rs, ps, null);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源失败。");
			}
		}
		ps = null;
		rs = null;
		return returnList;
	}

	public List<DynaBean> getListDynaBeanBySql(String sql)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DynaBean> dynaBeanList = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			RowSetDynaClass rsdc = new RowSetDynaClass(rs,true,true);
			dynaBeanList = rsdc.getRows();
			if(dynaBeanList==null){
				dynaBeanList=new ArrayList<DynaBean>(0);
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据sql语句查询并获取动态bean异常。");
			return null;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源失败。");
			}
		}
		return dynaBeanList;
	}
	
	public List<DynaBean> getListDynaBeanBySql(Connection conn,boolean isOutConn,String sql)
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
		
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DynaBean> dynaBeanList = null;
		try
		{
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			RowSetDynaClass rsdc = new RowSetDynaClass(rs,true,true);
			dynaBeanList = rsdc.getRows();
			if(dynaBeanList==null){
				dynaBeanList=new ArrayList<DynaBean>(0);
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据sql语句查询并获取动态bean异常。");
			return null;
		} finally
		{
			try
			{
				if(isOutConn)
				{
					//方法外部传递数据库连接进来,在方法内部不关闭连接
					//关闭数据库资源
					close(rs, ps);
				}
				else
				{
					//方法内部生成数据库连接，需要在方法内关闭连接
					//关闭数据库资源
					close(rs, ps,conn);
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源失败。");
			}
		}
		return dynaBeanList;
	}
	
	/**
	 * 获取自增值通过存储过程
	 * @param fildId 行id。1-taskId; 2-guid; 3-serialNum; 4-orderCode; 5-tableNum; 6-batchNumber;
	 * @param count 步长
	 * @return 返回自增
	 */
	public long getIdByPro(int fildId, int count)
	{
		Connection conn = null;
		CallableStatement cstmt = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			String procedure = "{call PRO_GET_INT_ID(?,?,?)}";
			cstmt = conn.prepareCall(procedure);
			cstmt.setInt(1, fildId);
			cstmt.setInt(2, count);
			cstmt.registerOutParameter(3, Types.BIGINT);
			
			cstmt.execute();
			return cstmt.getLong(3);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过存储过程获取自增值id异常。");
			return 0;
		} finally
		{
			try
			{
				if (cstmt != null)
				{
					cstmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源异常。");
			}
		}
	}
	public int findCountBySQLCon(String countSql, List<Object> conList)
    	throws Exception
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		try
		{
		    conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		    EmpExecutionContext.sql("execute sql : " + countSql);
		    ps = conn.prepareStatement(countSql);
		    int psIndex = 0;
		    if (conList != null && conList.size() > 0)
		    {
		        for (int i = 0; i < conList.size(); i++)
		        {
		            psIndex++;
		            Object cond = conList.get(i);
		            if(cond instanceof Timestamp)
		            {
		                ps.setTimestamp(psIndex, (Timestamp)cond);
		            }else {
		                ps.setString(psIndex, cond.toString());
		            }
		        }
		    }
		    rs = ps.executeQuery();
		    if (rs.next())
		    {
		        totalCount = rs.getInt("totalcount");
		    }
		} catch (Exception e)
		{
		    EmpExecutionContext.error(e,"查询总页数出错！");
		} finally
		{
		    try
		    {
		        close(rs, ps, conn);
		    } catch (Exception e)
		    {
		        EmpExecutionContext.error(e,"关闭数据库资源出错！");
		    }
		}
		return totalCount;
	}
	
	public int findCountBySQLCon(Connection conn,boolean isOutConn,String countSql, List<Object> conList)
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
		
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		try
		{
			if(conn==null)
			{
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
		    EmpExecutionContext.sql("execute sql : " + countSql);
		    ps = conn.prepareStatement(countSql);
		    int psIndex = 0;
		    if (conList != null && conList.size() > 0)
		    {
		        for (int i = 0; i < conList.size(); i++)
		        {
		            psIndex++;
		            Object cond = conList.get(i);
		            if(cond instanceof Timestamp)
		            {
		                ps.setTimestamp(psIndex, (Timestamp)cond);
		            }else {
		                ps.setString(psIndex, cond.toString());
		            }
		        }
		    }
		    rs = ps.executeQuery();
		    if (rs.next())
		    {
		        totalCount = rs.getInt("totalcount");
		    }
		} catch (Exception e)
		{
		    EmpExecutionContext.error(e,"查询总页数出错！");
		} finally
		{
		    try
		    {
				if(isOutConn)
				{
					//方法外部传递数据库连接进来,在方法内部不关闭连接
					//关闭数据库资源
					close(rs, ps);
				}
				else
				{
					//方法内部生成数据库连接，需要在方法内关闭连接
					//关闭数据库资源
					close(rs, ps,conn);
				}
		    } catch (Exception e)
		    {
		        EmpExecutionContext.error(e,"关闭数据库资源出错！");
		    }
		}
		return totalCount;
	}
}
