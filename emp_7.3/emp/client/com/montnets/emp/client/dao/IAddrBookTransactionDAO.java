package com.montnets.emp.client.dao;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;



/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public interface IAddrBookTransactionDAO
{

	/**
	 * @return
	 */
	public Connection getConnection();

	/**
	 * @param conn
	 * @throws Exception
	 */
	public void beginTransaction(Connection conn) throws Exception;

	
	/**
	 * @param conn
	 * @throws Exception
	 */
	public void commitTransaction(Connection conn) throws Exception;

	/**
	 * 
	 * @param conn
	 */
	public void rollBackTransaction(Connection conn);

	/**
	 * 
	 * @param conn
	 */
	public void closeConnection(Connection conn);

	
	/**
	 * @param conn
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean save(Connection conn, Object object) throws Exception;


	/**
	 * @param conn
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long saveObjectReturnID(Connection conn, Object object)
			throws Exception;

	
	/**
	 * @param <T>
	 * @param conn
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> boolean delete(Connection conn, Class<T> entityClass)
			throws Exception;


	/**
	 * @param <T>
	 * @param conn
	 * @param entityClass
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public <T> int delete(Connection conn, Class<T> entityClass, String ids)
			throws Exception;

	
	/**
	 * @param <T>
	 * @param conn
	 * @param entityClass
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> int delete(Connection conn, Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap) throws Exception;

	/**
	 * 
	 * @param conn
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean update(Connection conn, Object object) throws Exception;


	/**
	 * @param <T>
	 * @param conn
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Connection conn, Class<T> entityClass,
			LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception;

	
	/**
	 * @param <T>
	 * @param conn
	 * @param entityClass
	 * @param objectMap
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Connection conn, Class<T> entityClass,
			LinkedHashMap<String, Object> objectMap, String id)
			throws Exception;


	/**
	 * 
	 * @param conn
	 * @param depCode
	 * @param bizCode
	 * @param clientCode
	 * @return
	 * @throws Exception
	 */
	public int saveClientByProc(Connection conn, String depCode,
			String bizCode, String clientCode) throws Exception;


	/**
	 * @param conn
	 * @param depCode
	 * @param bizCode
	 * @param employeeCode
	 * @return
	 * @throws Exception
	 */
	public int saveEmpolyeeByProc(Connection conn, String depCode,
			String bizCode, String employeeCode) throws Exception;

	/**
	 * @param conn
	 * @param depCode
	 * @param bizCode
	 * @param depType
	 * @return
	 * @throws Exception
	 */
	public int saveEnterpriseProc(Connection conn, String depCode,
			String bizCode,Integer depType) throws Exception;


	/**
	 * @param <T>
	 * @param conn
	 * @param entityList
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> int save(Connection conn, List<T> entityList,
			Class<T> entityClass) throws Exception;
}
