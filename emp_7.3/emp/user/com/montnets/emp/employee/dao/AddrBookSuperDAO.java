package com.montnets.emp.employee.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
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
import com.montnets.emp.util.StringUtils;

/**
 * 
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class AddrBookSuperDAO
{
	
	protected IConnectionManager connectionManager;
	
	public AddrBookSuperDAO(){
		connectionManager = new ConnectionManagerImp();
	}

	
	/**
	 * @description 关闭Result返回的结果集，PreparedStatements批量执行语句 connection数据库链接
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
	 * @param rs
	 * @param ps
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
	 * @param entityClass
	 * @return columns
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getORMMap(Class<?> entityClass) throws Exception
	{
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
	 * @param voClass
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
	 * @param sql
	 * @param POOLNAME
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
			ps = conn.prepareStatement(sql);
			int count = ps.executeUpdate();
			if (count > 0)
			{
				b = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(null, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return b;
	}


	
	/**
	 * @param sql
	 * @param POOLNAME
	 * @return Integer
	 * @throws Exception
	 */
	public Integer executeBySQLReturnCount(String sql, String POOLNAME) throws Exception
	{
		Integer count = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
			count = ps.executeUpdate();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(null, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return count;
	}
	
	
	
	

	/**
	 * @param conn
	 * @param sql
	 * @return boolean
	 * @throws Exception
	 */
	public boolean executeBySQL(Connection conn, String sql) throws Exception
	{
		boolean b = false;
		PreparedStatement ps = null;
		try
		{
			ps = conn.prepareStatement(sql);
			int count = ps.executeUpdate();
			if (count > 0)
			{
				b = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行员工通讯录sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(null, ps);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭PreparedStatement失败！");
			}
		}
		return b;
	}
	
	
	
	
	/**
	 * cretor：lxh
	 * @param conn
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Integer executeBySQLReturnCount(Connection conn, String sql) throws Exception
	{
		Integer count = 0;
		PreparedStatement ps = null;
		try
		{
			ps = conn.prepareStatement(sql);
			count = ps.executeUpdate();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(null, ps);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭PreparedStatement失败！");
			}
		}
		return count;
	}
	
	

	/**
	 * 
	 * @param //connectionManager
	 * @param entityClass
	 * @param sql
	 * @param //TABLE_PATH
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
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return returnList;
	}

	/**
	 * 
	 * @param //connectionManager
	 * @param entityClass
	 * @param sql
	 * @param //TABLE_PATH
	 * @param POOLNAME
	 * @return
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
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			returnList = partrsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return returnList;
	}

	/**
	 * 
	 * @param //connectionManager
	 * @param entityClass
	 * @param sql
	 * @param //TABLE_PATH
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
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			returnList = partrsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, null);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return returnList;
	}

	/**
	 * 
	 * @param //connectionManager
	 * @param voClass
	 * @param sql
	 * @param //TABLE_PATH
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
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return returnList;
	}

	
	
	
	
	//获得通讯录类型
	public <T> List<T> findVoTypeListBySQL(Class<T> voClass, PageInfo pageInfo,String sql,String countSql,
			String POOLNAME,List<String> timeList,Map<String, String> columns) throws Exception
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
			
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				//适用ORACEL数据库的SQL语句
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
			} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				//适用SQLSERVER2005数据库的SQL语句
				if (pageInfo.getPageIndex() <= 500000)
				{
					sqlSb.append("select * from (")
							.append(sql).append(
									" )as t  where rn>=" + beginCount+" and rn <="+endCount);
				} else
				{
					sqlSb.append("select * from (")
							.append(sql).append(") as t where  rn between ").append(
									beginCount).append(" and ").append(endCount);
				}
			} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
			{
				//适用MYSQL数据库的SQL语句
				endCount = pageInfo.getPageSize();
				sqlSb.append(sql).append(" limit ").append(
						beginCount-1).append(",").append(endCount).toString();
			}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
			{
				//适用DB2数据库的SQL语句
				sqlSb.append("select * from(select row_number() over() as rownum ,t.* from (")
				.append(sql).append(")t )temp_t where rownum between ")
				.append(beginCount).append(" and ").append(endCount);
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
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return returnList;
	}
	
	
	//获得所有通讯录类型
	public <T> List<T> findAllVoTypeListBySQL(Class<T> voClass,String sql,
			String POOLNAME,List<String> timeList,Map<String, String> columns) throws Exception
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
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return returnList;
	}


	
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
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
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
				if (columns.get(fieldName) != null)
				{
					if (fieldType.equals("String"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, String.class);
						setMethod
								.invoke(o, rs.getString(columns.get(fieldName)
										.toString()));
					} else if (fieldType.equals("Float")
							|| fieldType.equals("float"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, float.class);
						setMethod.invoke(o,
								rs.getFloat(columns.get(fieldName).toString()));
					} else if (fieldType.equals("Long")
							|| fieldType.equals("long"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, Long.class);
						setMethod.invoke(
								o,
								rs.getLong(columns.get(fieldName).toString()
										.replace("\"", "")));
					} else if (fieldType.equals("Integer")
							|| fieldType.equals("int"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, Integer.class);
						setMethod.invoke(
								o,
								rs.getInt(columns.get(fieldName).toString()
										.replace("\"", "")));
					} else if (fieldType.equals("Double")
							|| fieldType.equals("double"))
					{
						Method setMethod = cls.getMethod(
								"set" + fieldNameUpper, Double.class);
						setMethod
								.invoke(o, rs.getDouble(columns.get(fieldName)
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
						setMethod.invoke(o,
								rs.getByte(columns.get(fieldName).toString()));
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
			    if(!fieldNameUpper.equals("SerialVersionUID")){
				for (int index = 0; index < columnName.length; index++)
				{
						String tempcolumn = columnName[index].toUpperCase();

						if (fieldType.equals("String")) {
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, String.class);
							if (tempcolumn.equals(columns.get(fieldName)))
								setMethod.invoke(o, rs.getString(columns.get(
										fieldName).toString()));
						} else if (fieldType.equals("Long")
								|| fieldType.equals("long")) {
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Long.class);
							if (tempcolumn.equals(columns.get(fieldName)
									.toString().replace("\"", "")))
								setMethod.invoke(o, rs
										.getLong(columns.get(fieldName)
												.toString().replace("\"", "")));
						} else if (fieldType.equals("Float")
								|| fieldType.equals("float")) {
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, float.class);
							if (tempcolumn.equals(columns.get(fieldName)))
								setMethod.invoke(o, rs.getFloat(columns.get(
										fieldName).toString()));
						} else if (fieldType.equals("Integer")
								|| fieldType.equals("int")) {
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Integer.class);
							if (tempcolumn.equals(columns.get(fieldName)
									.toString().replace("\"", "")))
								setMethod.invoke(o, rs
										.getInt(columns.get(fieldName)
												.toString().replace("\"", "")));
						} else if (fieldType.equals("Double")
								|| fieldType.equals("double")) {
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Double.class);
							if (tempcolumn.equals(columns.get(fieldName)))
								setMethod.invoke(o, rs.getDouble(columns.get(
										fieldName).toString()));
						} else if (fieldType.equals("Timestamp")) {
							Method setMethod = cls.getMethod("set"
									+ fieldNameUpper, Timestamp.class);
							if (tempcolumn.equals(columns.get(fieldName)))
								setMethod.invoke(o, rs.getTimestamp(columns
										.get(fieldName).toString()));
						} else if (fieldType.equals("Byte")) {
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
			conn = connectionManager.getDBConnection(poolName);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next())
			{
				return rs.getString(columnName);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
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
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next())
			{
				return rs.getInt(columnName);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
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
		if(sequenceName == null){
			throw new SequenceNameNotFoundException();
		}
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try
		{
			String sql = " select " + sequenceName + ".nextval from dual";
			preparedStatement = conn.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
			{
				return resultSet.getLong(1);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"员工通讯录获取数据库 下一个序列号失败！");
		} finally
		{
			try
			{
				close(resultSet, preparedStatement);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
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
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(countSql);
			rs = ps.executeQuery();
			if (rs.next())
			{
				totalCount = rs.getInt("totalcount");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"员工通讯录获取查询总数出现异常！");
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
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
			rs = ps.executeQuery();
			if (rs.next())
			{
				totalCount = rs.getInt("totalcount");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"员工通讯录获取查询总数量失败！");
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
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
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @return List
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList) throws Exception
	{
		return findPageVoListBySQL(voClass, sql, countSql, pageInfo, POOLNAME,
				timeList, null);
	}

	/**
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @return List
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			PageInfo pageInfo, String POOLNAME, List<String> timeList)
			throws Exception
	{
		return findPageVoListBySQL(voClass, sql, null, pageInfo, POOLNAME,
				timeList, null);
	}

	/**
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return List
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			PageInfo pageInfo, String POOLNAME, List<String> timeList,
			Map<String, String> columns) throws Exception
	{
		return findPageVoListBySQL(voClass, sql, null, pageInfo, POOLNAME,
				timeList, columns);
	}

	/**
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return List
	 * @throws Exception
	 */
	protected <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
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
			
		   	if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				//适用ORACEL数据库的SQL语句
		   		if (pageInfo.getPageIndex() <= 500000)
				{
					sqlSb.append("select * from (select t.*, rownum rn from (")
							.append(sql).append(
									") t ) where rn>=" + beginCount+" and rn <="+endCount);
				} else
				{
					sqlSb.append("select * from (select t.*, rownum rn from (")
							.append(sql).append(") t) where  rn between ").append(
									beginCount).append(" and ").append(endCount);
				}
			} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				//适用SQLSERVER2005数据库的SQL语句
				if (pageInfo.getPageIndex() <= 500000)
				{
					sqlSb.append("select * from (")
							.append(sql).append(
									" )as t  where rn>=" + beginCount+" and rn <="+endCount);
				} else
				{
					sqlSb.append("select * from (")
							.append(sql).append(") as t where  rn between ").append(
									beginCount).append(" and ").append(endCount);
				}
			} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
			{
				//适用MYSQL数据库的SQL语句
				endCount = pageInfo.getPageSize();
				sqlSb.append(sql).append(" limit ").append(
						beginCount-1).append(",").append(endCount).toString();
			}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
			{
				//适用DB2数据库的SQL语句

				sqlSb.append(
						"select * from(select row_number() over() as rownum ,t.* from (")
						.append(sql).append(")t )temp_t where rownum between ")
						.append(beginCount).append(" and ").append(endCount);
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
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return returnList;
	}
	
	//
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
			String countSql, String POOLNAME,
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
			
			//int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
			//		- (pageInfo.getPageSize() - 1);
			//int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			StringBuffer sqlSb = new StringBuffer();
			
			
			//sqlSb.append("select * from (select t.*, rownum rn from (")
			//			.append(sql).append(") t)");
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
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return returnList;
	}
	
	/**
	 * @param <T>
	 * @param entityClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @return List
	 * @throws Exception
	 */
	public <T> List<T> findPageEntityListBySQL(Class<T> entityClass,
			String sql, String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		List<T> returnList = null;
		Map<String, String> columns = getORMMap(entityClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(countSql);
			
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
			rs = ps.executeQuery();
			
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return returnList;
	}
	
	
	/**
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return List
	 * @throws Exception
	 */
	protected <T> List<T> findPageVoTypeListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
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
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return returnList;
	}
	
	public String executeProcessReutrnCursorOfOracle( String POOLNAME,
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
			if(deps.length() > 0)
				depIds = deps.substring(0, deps.lastIndexOf(","));
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			
			try
			{
				close(rs, proc, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return depIds;
	}
	
	public String executeProcessReutrnCursorOfMySql(String POOLNAME,
			String processStr,Integer[] params) throws Exception{
		String sql  = "call "+processStr+"(?,?,?)";
		Connection conn = null;
		ResultSet rs = null;
		StringBuffer deps = new StringBuffer();
		String depIds = "";
		CallableStatement comm = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			comm = conn.prepareCall(sql);
			comm.setInt(1,params[0]);
			comm.setLong(2,params[1]);
			comm.setString(3,StringUtils.getRandom());
			comm.execute();
			rs = comm.getResultSet();
			while (rs.next()) {
				deps.append(rs.getLong("DepID")).append(",");
			}
			depIds = deps.substring(0,deps.lastIndexOf(",")).toString();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, comm, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.debug("关闭数据库资源出错！");
			}
		}
		return depIds;
	}
	public String getEmpChildIdByDepId( String depId) throws Exception
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
				sql = "select dep_Id from lf_employee_dep where PARENT_ID in ( "+conditionDepid+")";
				ps = conn.prepareStatement(sql);
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
			EmpExecutionContext.error(e, "员工查询失败 ！");
			
		} finally
		{
			
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.debug("关闭数据库资源出错！");
			}
		}
		return depIds;	
	}
	public String getClientChildIdByDepId( String depId) throws Exception
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
				sql = "select dep_Id from lf_client_dep where PARENT_ID in ( "+conditionDepid+")";
				ps = conn.prepareStatement(sql);
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
			EmpExecutionContext.error(e, "员工查询失败！");
			
		} finally
		{
			
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.debug("关闭数据库资源出错！");
			}
		}
		return depIds;	
	}
}
