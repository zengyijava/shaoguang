package com.montnets.emp.client.dao;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.util.PageInfo;


public interface IAddrBookDAO
{
	/**
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean save(Object object) throws Exception;

	/**
	 * 
	 * @param <T>
	 * @param entityList
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> int save(List<T> entityList, Class<T> entityClass)
			throws Exception;

	/**
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long saveObjectReturnID(Object object) throws Exception;

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> boolean delete(Class<T> entityClass) throws Exception;

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public <T> int delete(Class<T> entityClass, String ids) throws Exception;

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> int delete(Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap) throws Exception;

	/**
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean update(Object object) throws Exception;

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Class<T> entityClass,
			LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception;
	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param objectMap
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Class<T> entityClass,
			LinkedHashMap<String, Object> objectMap, String id)
			throws Exception;

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T> T findObjectByID(Class<T> entityClass, long id) throws Exception;

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param distinctFieldList
	 * @param conditionMap
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findDistinctListBySymbolsCondition(Class<T> entityClass,
			List<String> distinctFieldList,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception;

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findListByCondition(Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception;

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findListBySymbolsCondition(Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception;

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findListBySymbolsCondition(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception;


	/**
	 * @param <T>
	 * @param loginUserID
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageListByCondition(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception;

	
	/**
	 * @param <T>
	 * @param loginUserID
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageListBySymbolsCondition(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception;
}
