package com.montnets.emp.common.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author Administrator
 *
 */

public interface IGenericDAO
{
	/**
	 * 组装时间的查询条件
	 * @param time
	 * @return
	 */
	public String getTimeCondition(String time);

	/**
	 * 组装时间 的查询条件
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String getTimeCondition(String startTime, String endTime);

	/**
	 * 根据SQL语句查询分页信息
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception;
	
	/**
	 * 根据SQL语句查询分页信息   支持传连接
	 * @param <T>
	 * @param conn  isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn  false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQL(Connection conn,boolean isOutConn,Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception;
	
	/**
	 * 根据SQL语句查询分页信息，第一页查总数，后续不查总数，pageInfo对象需包含总页数和总记录数
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQLNoCount(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception;
	
	/**
	 * 根据SQL语句查询分页信息，第一页查总数，后续不查总数，pageInfo对象需包含总页数和总记录数    支持传连接
	 * @param <T>
	 * @param conn   isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn   false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQLNoCount(Connection conn,boolean isOutConn,Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception;

	/**
	 * 根据SQL语句查询分页信息
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			Map<String, String> columns) throws Exception;
	
	/**
	 * 根据SQL语句查询分页信息，第一页查总数，后续不查总数，pageInfo对象需包含总页数和总记录数
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQLNoCount(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			Map<String, String> columns) throws Exception;

	/**
	 * 根据SQL语句查询分页信息
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
			List<String> timeList) throws Exception;
	
	/**
	 * 根据SQL语句查询分页信息，第一页查总数，后续不查总数，pageInfo对象需包含总页数和总记录数
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
	public <T> List<T> findPageVoListBySQLNoCount(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList) throws Exception;

	/**
	 * 根据SQL语句查询分页信息
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			PageInfo pageInfo, String POOLNAME, List<String> timeList)
			throws Exception;

	/**
	 * 根据SQL语句查询分页信息
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			PageInfo pageInfo, String POOLNAME, List<String> timeList,
			Map<String, String> columns) throws Exception;

	/**
	 * 根据SQL语句查询分页信息
	 * @param <T>
	 * @param entityClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageEntityListBySQL(Class<T> entityClass,
			String sql, String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception;
	
	/**
	 * 根据SQL语句查询分页信息，第一页查总数，后续不查总数，pageInfo对象需包含总页数和总记录数
	 * @param <T>
	 * @param entityClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageEntityListBySQLNoCount(Class<T> entityClass,
			String sql, String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception;
	
	/**
	 * 根据SQL语句查询分页信息
	 * @param sql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @return 返回动态bean集合
	 * @throws Exception
	 */
	public  List<DynaBean> findPageDynaBeanBySQL(String sql,
			String countSql ,PageInfo pageInfo, String POOLNAME, List<String> timeList);
	
	/**
	 * 根据SQL语句查询分页信息，第一页查总数，后续不查总数，pageInfo对象需包含总页数和总记录数
	 * @param sql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @return 返回动态bean集合
	 * @throws Exception
	 */
	public  List<DynaBean> findPageDynaBeanBySQLNoCount(String sql,
			String countSql ,PageInfo pageInfo, String POOLNAME, List<String> timeList);
	
	public List<DynaBean> findDynaBeanBySql(String sql);
}
