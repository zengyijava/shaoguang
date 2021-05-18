package com.montnets.emp.common.dao.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.util.PageInfo;

public abstract class GenericGenericDAO extends SuperDAO implements IGenericDAO
{
	/**
	 */
	public <T> List<T> findPageVoListBySQL(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		return findPageVoListBySQL(voClass, sql, countSql, pageInfo, POOLNAME,
				null, null);
	}
	
	public <T> List<T> findPageVoListBySQL(Connection conn,boolean isOutConn,Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		return findPageVoListBySQL(conn,isOutConn,voClass, sql, countSql, pageInfo, POOLNAME,
				null, null);
	}
	
	/**
	 */
	public <T> List<T> findPageVoListBySQLNoCount(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		return findPageVoListBySQLNoCount(voClass, sql, countSql, pageInfo, POOLNAME,
				null, null);
	}
	
	/**
	 */
	public <T> List<T> findPageVoListBySQLNoCount(Connection conn,boolean isOutConn,Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME)
			throws Exception
	{
		return findPageVoListBySQLNoCount(conn,isOutConn,voClass, sql, countSql, pageInfo, POOLNAME,
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
	public <T> List<T> findPageVoListBySQLNoCount(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			Map<String, String> columns) throws Exception
	{
		return findPageVoListBySQLNoCount(voClass, sql, countSql, pageInfo, POOLNAME,
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
	public <T> List<T> findPageVoListBySQLNoCount(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList) throws Exception
	{
		return findPageVoListBySQLNoCount(voClass, sql, countSql, pageInfo, POOLNAME,
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
		return null;
	}
	
	protected <T> List<T> findPageVoListBySQL(Connection conn,boolean isOutConn,Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		return null;
	}
	
	protected <T> List<T> findPageVoListBySQLNoCount(Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		return null;
	}
	
	protected <T> List<T> findPageVoListBySQLNoCount(Connection conn,boolean isOutConn,Class<T> voClass, String sql,
			String countSql, PageInfo pageInfo, String POOLNAME,
			List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		return null;
	}
	
	/**
	 * 
	 */
	public  List<DynaBean> findPageDynaBeanBySQL(String sql,
			String countSql ,PageInfo pageInfo, String POOLNAME, List<String> timeList)
			{
		return new ArrayList<DynaBean>();
			}
	
	/**
	 * 
	 */
	public  List<DynaBean> findPageDynaBeanBySQLNoCount(String sql,
			String countSql ,PageInfo pageInfo, String POOLNAME, List<String> timeList)
			{
		return new ArrayList<DynaBean>();
			}
}
