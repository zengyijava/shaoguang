package com.montnets.emp.common.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.GenericGenericDAO;
import com.montnets.emp.util.PageInfo;

/**
 * @project emp
 * @author liujianjun <646654831@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-10 下午06:59:45
 * @description
 */

/**
 * @author Administrator
 *
 */
public class OracleGenericDAO extends GenericGenericDAO
{
	protected <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			//oracle数据库
			return this.findPageVoListBySQLOracle(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
	   	} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
	   	//sqlserver数据库
	   		return this.findPageVoListBySQLSqlServer(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//db2数据库
			return this.findPageVoListBySQLDB2(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			//mysql数据库
			return this.findPageVoListBySQLMySql(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
		}
		return null;
	}
	
	protected <T> List<T> findPageVoListBySQLNoCount(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		//oracle数据库
		return this.findPageVoListBySQLNoCountOracle(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
	}
	
	public List<DynaBean> findPageDynaBeanBySQL(String sql,
			String countSql ,PageInfo pageInfo, String POOLNAME, List<String> timeList)
	{
		return this.findPageDynaBeanByOracle(sql, countSql, pageInfo, POOLNAME, timeList);
	}
	
	public List<DynaBean> findPageDynaBeanBySQLNoCount(String sql,
			String countSql ,PageInfo pageInfo, String POOLNAME, List<String> timeList)
	{
		return this.findPageDynaBeanByOracleNoCount(sql, countSql, pageInfo, POOLNAME, timeList);
	}
	
	public List<DynaBean> findDynaBeanBySql(String sql)
	{
		return this.getListDynaBeanBySql(sql);
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
	 * @return returnList
	 * @throws Exception
	 */
	private <T> List<T> findPageVoListBySQLOracle(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
		//  获取实体类字段与数据库字段映射的map集合
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
			EmpExecutionContext.sql("execute sql : " + sql);
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
			//执行SQL
			rs = ps.executeQuery();
			//RESULTSET转换为list集合
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			//关闭数据库资源
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
	private <T> List<T> findPageVoListBySQLNoCountOracle(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
		//  获取实体类字段与数据库字段映射的map集合
			columns = getVoORMMap(voClass);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			//只有第一页才需要查总数
			if (countSql != null && pageInfo.getPageIndex() == 1)
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
			EmpExecutionContext.sql("execute sql : " + sql);
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
			//执行SQL
			rs = ps.executeQuery();
			//RESULTSET转换为list集合
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			//关闭数据库资源
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
	private List<DynaBean> findPageDynaBeanByOracle(String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList)
	{
		List<DynaBean> returnList = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
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
			EmpExecutionContext.sql("execute sql : " + sql);
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
			//执行SQL
			rs = ps.executeQuery();
			// 将ResultSet转换成List
			RowSetDynaClass  rsdc = new RowSetDynaClass (rs);
			returnList = rsdc.getRows();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "动态bean分页查询异常。");
		} finally
		{
			//关闭数据库资源
			try
			{
				close(rs, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源出错！");
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
	 * @return returnList
	 * @throws Exception
	 */
	private List<DynaBean> findPageDynaBeanByOracleNoCount(String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList)
	{
		List<DynaBean> returnList = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			//只有第一页才需要查总数
			if (countSql != null && pageInfo.getPageIndex() == 1)
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
			EmpExecutionContext.sql("execute sql : " + sql);
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
			//执行SQL
			rs = ps.executeQuery();
			// 将ResultSet转换成List
			RowSetDynaClass  rsdc = new RowSetDynaClass (rs);
			returnList = rsdc.getRows();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "动态bean分页查询异常。");
		} finally
		{
			//关闭数据库资源
			try
			{
				close(rs, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源出错！");
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
	 * @return returnList
	 * @throws Exception
	 */
	private <T> List<T> findPageVoListBySQLSqlServer(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
		//  获取实体类字段与数据库字段映射的map集合
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
					- (pageInfo.getPageSize() - 1);// 开始行数
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();// 结束行数
			StringBuffer sqlSb = new StringBuffer();
			if (sql.indexOf("distinct") == -1 || sql.indexOf("distinct") > 20)
			{
				sql = sql.substring(sql.indexOf("select") + 7, sql.length());
				sql = new StringBuffer("select top ").append(endCount).append(
						" 0 as tempColumn,").append(sql).toString();
			} else
			{
				sql = sql.substring(sql.indexOf("distinct") + 9, sql.length());
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
			//执行SQL
			rs = ps.executeQuery();
			// 将ResultSet转换成List
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			// 关闭数据库资源。
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
	
	private <T> List<T> findPageVoListBySQLDB2(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
		//  获取实体类字段与数据库字段映射的map集合
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
				EmpExecutionContext.sql("execute sql : " + countSql);
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
			StringBuffer sqlSb = new StringBuffer();
			sqlSb.append("select * from(select row_number() over() as rownum ,t.* from (").
			append(sql).append(")t )temp_t where rownum between ")
			.append(beginCount).append(" and ").append(endCount);
			sql = sqlSb.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
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
			//执行SQL
			rs = ps.executeQuery();
			//ResultSet转换为list集合
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			// 关闭数据库资源
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
	private <T> List<T> findPageVoListBySQLMySql(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
		//  获取实体类字段与数据库字段映射的map集合
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
					- (pageInfo.getPageSize() - 1)-1;// 开始行数
			// 结束行数
			int endCount = pageInfo.getPageSize();// 结束行数
			sql = new StringBuffer(sql).append(" limit ").append(beginCount)
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
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			// 关闭数据库资源。
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
	/* (non-Javadoc)
	 * @see com.montnets.emp.dao.IGenericDAO#findPageEntityListBySQL(java.lang.Class, java.lang.String, java.lang.String, com.montnets.emp.util.PageInfo, java.lang.String)
	 */
	public <T> List<T> findPageEntityListBySQL(Class<T> entityClass,
			String sql, String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			//oracle数据库
			return this.findPageEntityListBySQLOracle(entityClass, sql, countSql, pageInfo, POOLNAME);
		}else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
			//sqlserver数据库
			return this.findPageEntityListBySQLSqlServer(entityClass, sql, countSql, pageInfo, POOLNAME);
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//db2数据库
			return this.findPageEntityListBySQLDB2(entityClass, sql, countSql, pageInfo, POOLNAME);
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			//mysql数据库
			return this.findPageEntityListBySQLMySql(entityClass, sql, countSql, pageInfo, POOLNAME);
		}
		return null;
	}
	
	public <T> List<T> findPageEntityListBySQLNoCount(Class<T> entityClass,
			String sql, String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
			//oracle数据库
			return this.findPageEntityListBySQLNoCountOracle(entityClass, sql, countSql, pageInfo, POOLNAME);
	}
	
	/**
	 * @param <T>
	 * @param entityClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @return returnList
	 * @throws Exception
	 */
	private <T> List<T> findPageEntityListBySQLOracle(Class<T> entityClass,
			String sql, String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
			//执行sql
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
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			// 关闭数据库资源
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
	 * @param <T>
	 * @param entityClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @return returnList
	 * @throws Exception
	 */
	private <T> List<T> findPageEntityListBySQLNoCountOracle(Class<T> entityClass,
			String sql, String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			//第一页才查总数
			if (countSql != null && pageInfo.getPageIndex() == 1)
			{
				EmpExecutionContext.sql("execute sql : " + countSql);
				ps = conn.prepareStatement(countSql);
				//执行sql
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
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			// 关闭数据库资源
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
	 * @param <T>
	 * @param entityClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @return returnList
	 * @throws Exception
	 */
	private <T> List<T> findPageEntityListBySQLSqlServer(Class<T> entityClass,
			String sql, String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			// 获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
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
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);// 开始行数
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();// 结束行数
			StringBuffer sqlSb = new StringBuffer();
			if (sql.indexOf("distinct") == -1 || sql.indexOf("distinct") > 20)
			{
				sql = sql.substring(sql.indexOf("select") + 7, sql.length());
				sql = new StringBuffer("select top ").append(endCount).append(
						" 0 as tempColumn,").append(sql).toString();
			} else
			{
				sql = sql.substring(sql.indexOf("distinct") + 9, sql.length());
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
			//执行SQL
			rs = ps.executeQuery();
			// 将ResultSet转换成List
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			// 关闭数据库资源。
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
	 * @param <T>
	 * @param entityClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @return returnList
	 * @throws Exception
	 */
	private <T> List<T> findPageEntityListBySQLDB2(Class<T> entityClass,
			String sql, String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
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
			StringBuffer sqlSb = new StringBuffer();
			sqlSb.append(
							"select * from(select row_number() over() as rownum ,t.* from (")
					.append(sql).append(")t )temp_t where rownum between ").append(
							beginCount).append(" and ").append(endCount);
			sql = sqlSb.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行SQL
			rs = ps.executeQuery();
			//resultset转换为list集合
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			//关闭数据库资源
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
	
	private <T> List<T> findPageEntityListBySQLMySql(Class<T> entityClass,
			String sql, String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		List<T> returnList = null;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			// 获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			EmpExecutionContext.sql("execute sql : " + countSql);
			ps = conn.prepareStatement(countSql);
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
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1)-1;// 开始行数
			// 结束行数
			int endCount = pageInfo.getPageSize() ;// 结束行数
			StringBuffer sqlSb = new StringBuffer(sql).append(" limit ")
					.append(beginCount).append(",").append(endCount);
			sql = sqlSb.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// 将ResultSet转换成List
			returnList = rsToList(rs, entityClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			// 关闭数据库资源。
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error("关闭数据库资源出错！");
			}
		}
		return returnList;
	}
	/* (non-Javadoc)
	 * @see com.montnets.emp.dao.IGenericDAO#getTimeCondition(java.lang.String)
	 */
	public String getTimeCondition(String time)
	{
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			//oracle数据库
			return this.getTimeConditionOracle(time);
		}else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
		//sqlserver数据库
			return this.getTimeConditionSqlServer(time);
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//db2数据库
			return this.getTimeConditionSqlServer(time);
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			//mysql数据库
			return this.getTimeConditionSqlServer(time);
		}
		return null;
	}
	
	/**
	 * @param time
	 * @return String
	 */
	private String getTimeConditionOracle(String time) {
		return new StringBuffer("to_date('").append(time).append(
				"','yyyy-MM-dd HH24:mi:ss')").toString();
	}
	
	/**
	 * @param time
	 * @return String
	 */
	private String getTimeConditionSqlServer(String time) {
		return new StringBuffer("'").append(time).append("'").toString();
	}

	
	/* (non-Javadoc)
	 * @see com.montnets.emp.dao.IGenericDAO#getTimeCondition(java.lang.String, java.lang.String)
	 */
	public String getTimeCondition(String startTime, String endTime)
	{
	   	if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
	   	//oracle数据库
		    return this.getTimeConditionOracle(startTime, endTime);
	   	} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
	   	//sqlserver数据库
	   		return this.getTimeConditionSqlServer(startTime, endTime);
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//db2数据库
			return this.getTimeConditionSqlServer(startTime, endTime);
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			//mysql数据库
			return this.getTimeConditionSqlServer(startTime, endTime);
		}
		return null;
	}
	
	/**
	 * @param startTime
	 * @param endTime
	 * @return String
	 */
	private String getTimeConditionOracle(String startTime, String endTime) {
		return new StringBuffer("to_date('").append(startTime).append(
				"','yyyy-MM-dd HH24:mi:ss') and to_date('").append(endTime)
				.append("','yyyy-MM-dd HH24:mi:ss')").toString();

	}
	
	/**
	 * @param startTime
	 * @param endTime
	 * @return String
	 */
	private String getTimeConditionSqlServer(String startTime, String endTime) {
		return new StringBuffer("'").append(startTime).append("' and '")
		.append(endTime).append("'").toString();

	}
}
