package com.montnets.emp.common.dao;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author Administrator
 *
 */
public interface IEmpTransactionDAO
{
	/**
	 * 获取一个连接
	 * @return
	 */
	public Connection getConnection();

	/**
	 * 开启一个事务
	 * @param conn
	 * @throws Exception
	 */
	public void beginTransaction(Connection conn) throws Exception;

	/**
	 * 提交事务
	 * @param conn
	 * @throws Exception
	 */
	public void commitTransaction(Connection conn) throws Exception;

	/**
	 * 回滚事务
	 * @param conn
	 */
	public boolean rollBackTransaction(Connection conn);

	/**
	 * 关闭连接
	 * @param conn
	 */
	public void closeConnection(Connection conn);

	/**
	 * 保存对象
	 * @param conn
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean save(Connection conn, Object object) throws Exception;

	/**
	 * 保存对象
	 * @param conn
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long saveObjectReturnID(Connection conn, Object object)
			throws Exception;
	
	public Long saveObjectReturnIDWithTri(Connection conn,Object object) throws Exception;
	
	/**
	 * 保存记录到数据库并返回自增id，自增id由程序自增
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long saveObjProReturnID(Connection conn, Object object);

	/**
	 * 删除对象
	 * @param <T>
	 * @param conn
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> boolean delete(Connection conn, Class<T> entityClass)
			throws Exception;

	/**
	 * 删除对象
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
	 * 删除对象
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
	 * 修改对象
	 * @param conn
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean update(Connection conn, Object object) throws Exception;

	/**
	 * 修改对象根据过滤条件
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
	 * 修改对象根据id
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
	 * @param <T>
	 * @param conn
	 * @param entityClass
	 * @param objectMap
	 * @param id
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Connection conn, Class<T> entityClass,
			LinkedHashMap<String, Object> objectMap, String id,
			LinkedHashMap<String, String> conditionMap)
			throws Exception;
	
	/**
	 * 根据条件更新，支持><=等条件
	 * @param <T>
	 * @param conn
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return 返回受影响行数
	 * @throws Exception
	 */
	public <T> int updateBySymbolsCondition(Connection conn, Class<T> entityClass, LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap) throws Exception;

	
	/**
	 * 改方法暂时不用
	 * @param conn
	 * @param lfDep
	 * @param opFlag
	 * @return
	 * @throws Exception
	 */
//	public boolean deleteLfDominationByLfDep(Connection conn, LfDep lfDep,Integer opFlag)
//			throws Exception;

	/**
	 * 保存对象
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
	 * 保存对象
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
	 * 保存对象
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
	 * 保存对象集合
	 * @param <T>
	 * @param conn
	 * @param entityList
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> int save(Connection conn, List<T> entityList,
			Class<T> entityClass) throws Exception;
	
	public boolean CreateTableByField(Connection conn,List<String> fieldStrings,String tableName)throws Exception;
	
	public boolean DeleteTableByTableName(Connection conn,String tableName)throws Exception;
	
	public boolean InsertTableByMap(Connection conn,Map<String,String> formvalue,String tableName)throws Exception;
	
	public boolean UpdateTableByMap(Connection conn,Map<String,String> formvalue,String tableName,Long sxxh)throws Exception;
	
	public boolean deleteFormDatamByAiid(Connection conn,String tableName,String aiId)throws Exception;
	
	public Integer executeBySQLReturnCount(Connection conn, String sql)
	throws Exception;
}
