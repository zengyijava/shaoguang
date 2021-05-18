package com.montnets.emp.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

/**
 * 
 * @author Administrator
 *
 */

public interface ISuperDAO
{

	/**
	 * 关闭连接
	 * @param rs
	 * @param ps
	 * @param conn
	 * @throws Exception
	 */
	public void close(ResultSet rs, PreparedStatement ps, Connection conn)
			throws Exception;

	/**
	 * 关闭连接
	 * @param rs
	 * @param ps
	 * @throws Exception
	 */
	public void close(ResultSet rs, PreparedStatement ps) throws Exception;

	/**
	 * 获取对象映射的map
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getORMMap(Class<?> entityClass) throws Exception;

	/**
	 * 获取Vo对象映射的map
	 * @param voClass
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getVoORMMap(Class<?> voClass) throws Exception;

	/**
	 * 执行SQl语句
	 * @param connectionManager
	 * @param POOLNAME
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public boolean executeBySQL(String sql, String POOLNAME) throws Exception;

	/**
	 * 执行SQl语句
	 * @param connectionManager
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public boolean executeBySQL(Connection conn, String sql) throws Exception;
	
	/**
	 * 执行SQl语句
	 * @param connectionManager
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Integer executeBySQLReturnCount(Connection conn, String sql) throws Exception;
	
	/**
	 * 执行SQl语句
	 * @param sql
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public Integer executeBySQLReturnCount(String sql, String POOLNAME) throws Exception;

	/**
	 * 根据SQL语句查询对象
	 * @param connectionManager
	 * @param entityClass
	 * @param sql
	 * @param TABLE_PATH
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findEntityListBySQL(Class<T> entityClass, String sql,
			String POOLNAME) throws Exception;

	/**
	 * 根据SQL语句查询对象
	 * @param connectionManager
	 * @param entityClass
	 * @param sql
	 * @param TABLE_PATH
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPartEntityListBySQL(Class<T> entityClass,
			String sql, String POOLNAME) throws Exception;

	/**
	 * 根据SQL语句查询对象
	 * @param connectionManager
	 * @param entityClass
	 * @param sql
	 * @param TABLE_PATH
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPartEntityListBySQL(Connection conn,
			Class<T> entityClass, String sql, String POOLNAME) throws Exception;

	/**
	 * 根据SQL语句查询VO对象
	 * @param connectionManager
	 * @param voClass
	 * @param sql
	 * @param TABLE_PATH
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findVoListBySQL(Class<T> voClass, String sql,
			String POOLNAME) throws Exception;

	/**
	 * 根据SQL语句查询VO对象
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param POOLNAME
	 * @param timeList
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findVoListBySQL(Class<T> voClass, String sql,
			String POOLNAME,List<String> timeList) throws Exception;
	
	/**
	 * 根据SQL语句获取总条数
	 * @param countSql
	 * @param timeList
	 * @return
	 * @throws Exception
	 */
	public int findCountBySQL(String countSql, List<String> timeList) throws Exception;
	/**
	 * 将集合中的数据转换成对象集合
	 * @param rs
	 * @param cls
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> rsToList(ResultSet rs, Class<T> cls,
			Map<String, String> columns) throws Exception;

	/**
	 * 将集合中的数据转换成对象集合
	 * @param rs
	 * @param cls
	 * @param columns
	 * @author wuxiaotao
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> partrsToList(ResultSet rs, Class<T> cls,
			Map<String, String> columns) throws Exception;
	
	/**
	 * 将集合中的对象转换成map对象
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> partrsToMap(ResultSet rs) throws Exception;
	
	/**
	 * sql查询并返回动态bean集合
	 * @param sql 查询语句
	 * @return 返回动态bean集合
	 */
	public List<DynaBean> getListDynaBeanBySql(String sql);
	
	/**
	 * sql查询并返回动态bean集合
	 * @param conn  isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn   false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param sql  查询语句
	 * @return 返回动态bean集合
	 */
	public List<DynaBean> getListDynaBeanBySql(Connection conn,boolean isOutConn,String sql);
	
}
