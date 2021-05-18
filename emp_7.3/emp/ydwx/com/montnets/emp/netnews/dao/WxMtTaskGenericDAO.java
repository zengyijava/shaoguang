package com.montnets.emp.netnews.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.util.PageInfo;

public class WxMtTaskGenericDAO extends SuperDAO
{
	
	/**
	 * 只按分页查记录，不查记录总数
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList) throws Exception
	{
		return findPageVoListBySQL(voClass, sql, countSql, pageInfo, POOLNAME,
				timeList, null);
	}
	
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
		{
			//oracle数据库
			return this.findPageVoListBySQLOracle(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
	   	} 
		else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
		{
			//sqlserver数据库
	   		return this.findPageVoListBySQLSqlServer(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
		}
		else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//db2数据库
			return this.findPageVoListBySQLDB2(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
		}
		else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//mysql数据库
			return this.findPageVoListBySQLMySql(voClass, sql, countSql, pageInfo, POOLNAME, timeList, columns);
		}
		return null;
	}
	
	/**
	 * 根据分页sql查询分页信息
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
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
				EmpExecutionContext.error("DAO方法查询分页信息，countSql为空。");
				return false;
			}
			
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
			//没记录
			if(!rs.next())
			{
				return true;
			}
			
			//当前页数
			int pageSize = pageInfo.getPageSize();
			//记录总条数
			int totalCount = rs.getInt("totalcount");
			//总页数
			int totalPage = totalCount % pageSize == 0 ? totalCount
					/ pageSize : totalCount / pageSize + 1;
			
			pageInfo.setTotalRec(totalCount);
			pageInfo.setTotalPage(totalPage);
			
			return true;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法查询分页信息，异常。countSql="+countSql);
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
				EmpExecutionContext.error(e,"DAO方法查询分页信息，关闭数据库资源出错。");
			}
		}
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
			//第一页的需要查总数
			/*if (countSql != null && pageInfo.getNeedNewData() == 1)
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
			}*/
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
			close(rs, ps, conn);
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
			/*if (countSql != null && pageInfo.getNeedNewData() == 1)
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
			}*/
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
			close(rs, ps, conn);
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
			/*if (countSql != null && pageInfo.getNeedNewData() == 1)
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
			}*/
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
			close(rs, ps, conn);
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
			/*if (countSql != null && pageInfo.getNeedNewData() == 1)
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
			}*/
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
			close(rs, ps, conn);
		}
		return returnList;
	}

	
}
