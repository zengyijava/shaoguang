package com.montnets.emp.qyll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.util.PageInfo;

public class MtTaskGenericDAO extends SuperDAO
{
	/**
	 * VO分页查询
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql 查询总数sql（该参数暂无作用，传null即可）
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @return
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME, List<String> timeList)
	{
		return findPageVoListBySQL(voClass, sql, countSql, pageInfo, POOLNAME, timeList, null);
	}
	
	/**
	 * 动态bean分页查询
	 * @param sql 查询sql
	 * @param countSql 查询总数sql（该参数暂无作用，传null即可）
	 * @param pageInfo 分页信息对象
	 * @param POOLNAME 连接池名称
	 * @param timeList 查询时间对象
	 * @return 返回动态bean记录集合
	 */
	public List<DynaBean> findPageDynaBeanBySQL(String sql, String countSql ,PageInfo pageInfo, String POOLNAME, List<String> timeList)
	{
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
		{
			//oracle数据库
			return findPageDynaBeanByOracle(sql, countSql, pageInfo, POOLNAME, timeList);
	   	} 
		else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
		{
			//sqlserver数据库
			return findPageDynaBeanBySQLSqlServer(sql, countSql, pageInfo, POOLNAME, timeList);
		}
		else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//db2数据库
			return findPageDynaBeanBySQLDB2(sql, countSql, pageInfo, POOLNAME, timeList);
		}
		else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//mysql数据库
			return findPageDynaBeanBySQLMySql(sql, countSql, pageInfo, POOLNAME, timeList);
		}
		return null;
	}
	
	/**
	 * 根据分页sql查询分页信息
	 * @param countSql 查询sql
	 * @param pageInfo 分页信息对象
	 * @param POOLNAME 数据库连接池
	 * @param timeList 时间查询条件集合
	 * @return 成功返回true
	 */
	public boolean findPageInfoBySQL(String countSql, PageInfo pageInfo, String POOLNAME, List<String> timeList) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			if(countSql == null || countSql.length() == 0)
			{
				EmpExecutionContext.error("DAO查询，分页信息，countSql为空。");
				return false;
			}
			EmpExecutionContext.sql("execute sql : " + countSql);
			
			ps = conn.prepareStatement(countSql);
			//预编译sql
			boolean prepResult = setPageParamForTime(timeList, ps);
			if(!prepResult)
			{
				EmpExecutionContext.error("DAO查询，分页信息，预处理sql参数失败。");
				return false;
			}
			
			//执行sql
			rs = ps.executeQuery();
			//没记录
			if(!rs.next())
			{
				return true;
			}
			
			//设置分页对象并返回
			return setPageInfo(rs.getInt("totalcount"), pageInfo);
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，分页信息，异常。countSql="+countSql);
			return false;
		} 
		finally
		{
			//关闭数据库资源
			try
			{
				close(rs, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "DAO查询，分页信息，关闭数据库资源出错。");
			}
		}
	}
	
	/**
	 * VO分页查询
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql 查询总数sql（该参数暂无作用，传null即可）
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
	{
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
		{
			//oracle数据库
			return findPageVoListBySQLOracle(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
	   	} 
		else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
		{
			//sqlserver数据库
	   		return findPageVoListBySQLSqlServer(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
		}
		else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//db2数据库
			return findPageVoListBySQLDB2(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
		}
		else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//mysql数据库
			return findPageVoListBySQLMySql(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
		}
		return null;
	}
	
	/**
	 * VO分页查询（Oracle）
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return returnList
	 * @throws Exception
	 */
	private <T> List<T> findPageVoListBySQLOracle(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//构造分页sql
			sql = getPageSQLForOracle(sql, pageInfo);
			if(sql == null)
			{
				EmpExecutionContext.error("DAO查询，Oracle，VO分页查询，构造分页sql失败。");
				return null;
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
			
			//执行SQL
			boolean prepResult = setSQLParamForOracle(timeList, ps);
			if(!prepResult)
			{
				EmpExecutionContext.error("DAO查询，Oracle，VO分页查询，预处理sql参数失败。");
				return null;
			}
			
			//执行SQL
			rs = ps.executeQuery();
			
			if (columns == null)
			{
				//  获取实体类字段与数据库字段映射的map集合
				columns = getVoORMMap(voClass);
			}
			//RESULTSET转换为list集合
			List<T> returnList = rsToList(rs, voClass, columns);
			return returnList;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO查询，Oracle，VO分页查询，异常。");
			return null;
		} 
		finally
		{
			//关闭数据库资源
			try
			{
				close(rs, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "DAO查询，Oracle，VO分页查询，关闭数据库资源，异常。");
			}
		}
	}

	/**
	 * VO分页查询（Oracle），不构造分页sql
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return returnList
	 */
	private <T> List<T> findPageVoListBySQLOracle(Class<T> voClass, String sql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			EmpExecutionContext.sql("execute sql : " + sql);
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
			
			//执行SQL
			boolean prepResult = setSQLParamForOracle(pageInfo, timeList, ps);
			if(!prepResult)
			{
				EmpExecutionContext.error("DAO查询，Oracle，VO分页查询，预处理sql参数失败。");
				return null;
			}
			
			//执行SQL
			rs = ps.executeQuery();
			
			if (columns == null)
			{
				//  获取实体类字段与数据库字段映射的map集合
				columns = getVoORMMap(voClass);
			}
			//RESULTSET转换为list集合
			List<T> returnList = rsToList(rs, voClass, columns);
			return returnList;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，Oracle，VO分页查询，异常。");
			return null;
		} 
		finally
		{
			//关闭数据库资源
			try
			{
				close(rs, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "DAO查询，Oracle，VO分页查询，关闭数据库资源，异常。");
			}
		}
	}
	
	/**
	 * VO分页查询（SqlServer）
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return returnList
	 * @throws Exception
	 */
	private <T> List<T> findPageVoListBySQLSqlServer(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//构造分页sql
			sql = getPageSQLForSqlServer(sql, pageInfo);
			if(sql == null)
			{
				EmpExecutionContext.error("DAO查询，Sqlserver，VO分页查询，构造分页sql失败。");
				return null;
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			
			// 获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			ps = conn.prepareStatement(sql);
			
			//预处理sql参数
			boolean prepResult = setSQLParamForSqlServer(timeList, ps);
			if(!prepResult)
			{
				EmpExecutionContext.error("DAO查询，Sqlserver，VO分页查询，预处理sql参数失败。");
				return null;
			}
			
			//执行SQL
			rs = ps.executeQuery();
			
			if (columns == null)
			{
			//  获取实体类字段与数据库字段映射的map集合
				columns = getVoORMMap(voClass);
			}
			// 将ResultSet转换成List
			List<T> returnList = rsToList(rs, voClass, columns);
			return returnList;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，Sqlserver，VO分页查询，异常。");
			return null;
		} 
		finally
		{
			// 关闭数据库资源。
			try
			{
				close(rs, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"DAO查询，Sqlserver，VO分页查询，关闭数据库资源，异常。");
			}
		}
	}

	/**
	 * VO分页查询（DB2）
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return
	 */
	private <T> List<T> findPageVoListBySQLDB2(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//构造分页sql
			sql = getPageSQLForDB2(sql, pageInfo);
			if(sql == null)
			{
				EmpExecutionContext.error("DAO查询，DB2，VO分页查询，构造分页sql失败。");
				return null;
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			
			ps = conn.prepareStatement(sql);
			//预处理sql参数
			boolean prepResult = setSQLParamForDB2(timeList, ps);
			if(!prepResult)
			{
				EmpExecutionContext.error("DAO查询，DB2，VO分页查询，预处理sql参数失败。");
				return null;
			}
			//执行SQL
			rs = ps.executeQuery();
			
			if (columns == null)
			{
				//  获取实体类字段与数据库字段映射的map集合
				columns = getVoORMMap(voClass);
			}
			//ResultSet转换为list集合
			List<T> returnList = rsToList(rs, voClass, columns);
			return returnList;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO查询，DB2，VO分页查询，异常。");
			return null;
		} 
		finally
		{
			// 关闭数据库资源
			try
			{
				close(rs, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"DAO查询，DB2，VO分页查询，关闭数据库资源，异常。");
			}
		}
	}

	/**
	 * VO分页查询（MySql）
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return
	 */
	private <T> List<T> findPageVoListBySQLMySql(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//构造分页sql
			sql = getPageSQLForMySql(sql, pageInfo);
			if(sql == null)
			{
				EmpExecutionContext.error("DAO查询，MySql，VO分页查询，构造分页sql失败。");
				return null;
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			
			ps = conn.prepareStatement(sql);
			//预处理sql参数
			boolean prepResult = setSQLParamForMySql(timeList, ps);
			if(!prepResult)
			{
				EmpExecutionContext.error("DAO查询，MySql，VO分页查询，预处理sql参数失败。");
				return null;
			}
			
			rs = ps.executeQuery();
			
			if (columns == null)
			{
				//  获取实体类字段与数据库字段映射的map集合
				columns = getVoORMMap(voClass);
			}
			// 将ResultSet转换成List
			List<T> returnList = rsToList(rs, voClass, columns);
			return returnList;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，MySql，VO分页查询，异常。");
			return null;
		} 
		finally
		{
			// 关闭数据库资源。
			try
			{
				close(rs, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"DAO查询，MySql，VO分页查询，关闭数据库资源，异常。");
			}
		}
	}

	/**
	 * 动态bean分页查询（DB2）
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @return
	 */
	private List<DynaBean> findPageDynaBeanBySQLDB2(String sql, String countSql, PageInfo pageInfo, String POOLNAME, List<String> timeList)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//构造分页sql
			sql = getPageSQLForDB2(sql, pageInfo);
			if(sql == null)
			{
				EmpExecutionContext.error("DAO查询，DB2，动态bean分页查询，构造分页sql失败。");
				return null;
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			
			ps = conn.prepareStatement(sql);
			//预处理sql参数
			boolean prepResult = setSQLParamForDB2(timeList, ps);
			if(!prepResult)
			{
				EmpExecutionContext.error("DAO查询，DB2，动态bean分页查询，预处理sql参数失败。");
				return null;
			}
			
			//执行SQL
			rs = ps.executeQuery();
			
			// 将ResultSet转换成List
			RowSetDynaClass rsdc = new RowSetDynaClass(rs);
			return rsdc.getRows();
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，DB2，动态bean分页查询，异常。");
			return null;
		} 
		finally
		{
			// 关闭数据库资源
			try
			{
				close(rs, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "DAO查询，DB2，动态bean分页查询，关闭数据库资源，异常。");
			}
		}
	}
	
	/**
	 * 动态bean分页查询（MySql）
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @return
	 */
	private List<DynaBean> findPageDynaBeanBySQLMySql(String sql, String countSql, PageInfo pageInfo, String POOLNAME, List<String> timeList)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//构造分页sql
			sql = getPageSQLForMySql(sql, pageInfo);
			if(sql == null)
			{
				EmpExecutionContext.error("DAO查询，MySql，动态bean分页查询，构造分页sql失败。");
				return null;
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			
			ps = conn.prepareStatement(sql);
			//预处理sql参数
			boolean prepResult = setSQLParamForMySql(timeList, ps);
			if(!prepResult)
			{
				EmpExecutionContext.error("DAO查询，MySql，动态bean分页查询，预处理sql参数失败。");
				return null;
			}
			
			rs = ps.executeQuery();
			
			// 将ResultSet转换成List，这里设置useColumnLabel为true，使用这种方式获取列名
			RowSetDynaClass rsdc = new RowSetDynaClass(rs, true, true);
			return rsdc.getRows();
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，MySql，动态bean分页查询，异常。");
			return null;
		} 
		finally
		{
			// 关闭数据库资源。
			try
			{
				close(rs, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "DAO查询，MySql，动态bean分页查询，关闭数据库资源，异常。");
			}
		}
	}
	
	/**
	 * 动态bean分页查询（Oracle）
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @return
	 */
	private List<DynaBean> findPageDynaBeanByOracle(String sql, String countSql, PageInfo pageInfo, String POOLNAME, List<String> timeList)
	{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try
		{
			//构造分页sql
			sql = getPageSQLForOracle(sql, pageInfo);
			if(sql == null)
			{
				EmpExecutionContext.error("DAO查询，Oracle，动态bean分页查询，构造分页sql失败。");
				return null;
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			
			ps = conn.prepareStatement(sql);
			//预处理sql
			boolean prepResult = setSQLParamForOracle(timeList, ps);
			if(!prepResult)
			{
				EmpExecutionContext.error("DAO查询，Oracle，动态bean分页查询，预处理sql参数失败。");
				return null;
			}
			
			//执行SQL
			rs = ps.executeQuery();
			
			// 将ResultSet转换成List
			RowSetDynaClass rsdc = new RowSetDynaClass(rs);
			return rsdc.getRows();
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，Oracle，动态bean分页查询，异常。");
			return null;
		} 
		finally
		{
			//关闭数据库资源
			try
			{
				close(rs, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "DAO查询，Oracle，动态bean分页查询，关闭数据库资源，异常。");
			}
		}
	}
	
	/**
	 * 动态bean分页查询（Sqlserver）
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @return
	 */
	private List<DynaBean> findPageDynaBeanBySQLSqlServer(String sql, String countSql, PageInfo pageInfo, String POOLNAME, List<String> timeList)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//构造分页sql
			sql = getPageSQLForSqlServer(sql, pageInfo);
			if(sql == null)
			{
				EmpExecutionContext.error("DAO查询，Sqlserver，动态bean分页查询，构造分页sql失败。");
				return null;
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			
			// 获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			
			ps = conn.prepareStatement(sql);
			//预处理sql参数
			boolean prepResult = setSQLParamForSqlServer(timeList, ps);
			if(!prepResult)
			{
				EmpExecutionContext.error("DAO查询，Sqlserver，动态bean分页查询，预处理sql参数失败。");
				return null;
			}
			
			//执行SQL
			rs = ps.executeQuery();
			// 将ResultSet转换成List
			RowSetDynaClass rsdc = new RowSetDynaClass(rs); 
			return rsdc.getRows();
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，Sqlserver，动态bean分页查询，异常。");
			return null;
		}
		finally
		{
			// 关闭数据库资源。
			try
			{
				close(rs, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"DAO查询，Sqlserver，动态bean分页查询，关闭数据库资源，异常。");
			}
		}
	}
	
	/**
	 * 构造分页sql（Oracle）
	 * @param sql 查询sql
	 * @param pageInfo 分页信息对象
	 * @return 返回分页sql
	 */
	private String getPageSQLForOracle(String sql, PageInfo pageInfo)
	{
		try
		{
			//开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex() - (pageInfo.getPageSize() - 1);
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
			return sql;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，Oracle，构造Sql分页，异常。sql="+sql);
			return null;
		}
	}
	
	/**
	 * 构造分页sql（SqlServer）
	 * @param sql 查询sql
	 * @param pageInfo 分页信息对象
	 * @return 返回分页sql
	 */
	private String getPageSQLForSqlServer(String sql, PageInfo pageInfo)
	{
		try
		{
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex() - (pageInfo.getPageSize() - 1);
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			
			StringBuffer sqlSb = new StringBuffer();
			if (sql.indexOf("distinct") == -1 || sql.indexOf("distinct") > 20)
			{
				sql = sql.substring(sql.indexOf("select") + 7, sql.length());
				sql = new StringBuffer("select top ").append(endCount).append(" 0 as tempColumn,").append(sql).toString();
			} else
			{
				sql = sql.substring(sql.indexOf("distinct") + 9, sql.length());
				sql = new StringBuffer("select distinct top ").append(endCount).append(" 0 as tempColumn,").append(sql).toString();
			}
			sqlSb.append("select * from (select row_number() over(order by tempColumn) tempRowNumber,* from (")
				.append(sql).append(") t) tt where tempRowNumber>=").append(beginCount);
			sql = sqlSb.toString();
			return sql;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，SqlServer，构造Sql分页，异常。sql="+sql);
			return null;
		}
	}
	
	/**
	 * 构造分页sql（DB2）
	 * @param sql 查询sql
	 * @param pageInfo 分页信息对象
	 * @return 返回分页sql
	 */
	private String getPageSQLForDB2(String sql, PageInfo pageInfo)
	{
		try
		{
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex() - (pageInfo.getPageSize() - 1);
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			StringBuffer sqlSb = new StringBuffer();
			sqlSb.append("select * from(select row_number() over() as rownum ,t.* from (").
			append(sql).append(")t )temp_t where rownum between ")
			.append(beginCount).append(" and ").append(endCount);
			sql = sqlSb.toString();
			return sql;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，DB2，构造Sql分页，异常。sql="+sql);
			return null;
		} 
	}
	
	/**
	 * 构造分页sql（Mysql）
	 * @param sql 查询sql
	 * @param pageInfo 分页信息对象
	 * @return 返回分页sql
	 */
	private String getPageSQLForMySql(String sql, PageInfo pageInfo)
	{
		try
		{
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex() - (pageInfo.getPageSize() - 1)-1;// 开始行数
			// 结束行数
			int endCount = pageInfo.getPageSize();// 结束行数
			sql = new StringBuffer(sql).append(" limit ").append(beginCount)
					.append(",").append(endCount).toString();
			return sql;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，MySql，构造Sql分页，异常。sql="+sql);
			return null;
		}
	}
	
	/**
	 * sql参数预编译（Oracle），会设置分页参数
	 * @param pageInfo 分页信息
	 * @param timeList 时间条件对象
	 * @param ps 预编译对象
	 * @return 成功返回true
	 */
	private boolean setSQLParamForOracle(PageInfo pageInfo, List<String> timeList, PreparedStatement ps)
	{
		try
		{
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
			
			//开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex() - (pageInfo.getPageSize() - 1);
			//结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			psIndex++;
			ps.setInt(psIndex, endCount);
			psIndex++;
			ps.setInt(psIndex, beginCount);
			
			return true;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，Oracle，参数预编译，异常。");
			return false;
		}
	}
	
	/**
	 * sql预编译（Oracle）
	 * @param timeList 时间查询条件对象
	 * @param ps sql预处理对象
	 * @return 成功返回true
	 */
	private boolean setSQLParamForOracle(List<String> timeList, PreparedStatement ps)
	{
		try
		{
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
			return true;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，Oracle，参数预编译，异常。");
			return false;
		}
	}
	
	/**
	 * sql预编译（SqlServer）
	 * @param timeList 时间查询条件对象
	 * @param ps sql预处理对象
	 * @return 成功返回true
	 */
	private boolean setSQLParamForSqlServer(List<String> timeList, PreparedStatement ps)
	{
		try
		{
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
			return true;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，SqlServer，参数预编译，异常。");
			return false;
		}
	}
	
	/**
	 * sql预编译（DB2）
	 * @param timeList 时间查询条件对象
	 * @param ps sql预处理对象
	 * @return 成功返回true
	 */
	private boolean setSQLParamForDB2(List<String> timeList, PreparedStatement ps)
	{
		try
		{
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
			return true;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，DB2，参数预编译，异常。");
			return false;
		}
	}
	
	/**
	 * sql预编译（Mysql）
	 * @param timeList 时间查询条件对象
	 * @param ps sql预处理对象
	 * @return 成功返回true
	 */
	private boolean setSQLParamForMySql(List<String> timeList, PreparedStatement ps)
	{
		try
		{
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
			return true;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，MySql，参数预编译，异常。");
			return false;
		}
	}
	
	/**
	 * 分页时间查询条件sql预编译
	 * @param timeList 时间查询条件
	 * @param ps sql预处理对象
	 * @return 成功返回true
	 */
	private boolean setPageParamForTime(List<String> timeList, PreparedStatement ps)
	{
		try
		{
			//为空，不需要设置时间
			if(timeList == null || timeList.size() < 1)
			{
				return true;
			}
			
			int psIndex = 0;
			for (int i = 0; i < timeList.size(); i++)
			{
				psIndex++;
				String time = timeList.get(i);
				ps.setTimestamp(psIndex, Timestamp.valueOf(time));
			}
			return true;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，分页，时间条件参数预编译，异常。");
			return false;
		}
	}
	
	/**
	 * 设置分页信息对象
	 * @param totalCount 总记录数
	 * @param pageInfo 分页信息对象
	 * @return 成功返回true
	 */
	private boolean setPageInfo(int totalCount, PageInfo pageInfo)
	{
		try
		{
			//当前页数
			int pageSize = pageInfo.getPageSize();
			//总页数
			int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
			
			pageInfo.setTotalRec(totalCount);
			pageInfo.setTotalPage(totalPage);
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，分页，设置分页信息对象，异常。");
			return false;
		}
	}
	
}
