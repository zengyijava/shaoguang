package com.montnets.emp.common.dao;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author Administrator
 *
 */
public interface IEmpDAO
{
	/**
	 * 保存对象
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean save(Object object) throws Exception;
	
	/**
	 * 保存对象 支持传连接
	 * @param conn isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean save(Connection conn,boolean isOutConn,Object object) throws Exception;


	/**
	 * 保存对象集合
	 * @param <T>
	 * @param entityList
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> int save(List<T> entityList, Class<T> entityClass)
			throws Exception;

	/**
	 * 保存对象集合  支持传连接
	 * @param <T>
	 * @param conn  isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityList
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> int save(Connection conn,boolean isOutConn,List<T> entityList, Class<T> entityClass)
			throws Exception;
	
	/**
	 * 保存对象
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long saveObjectReturnID(Object object) throws Exception;
	
	/**
	 * 保存对象  支持传连接
	 * @param conn  isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long saveObjectReturnID(Connection conn,boolean isOutConn,Object object) throws Exception;
	
	/**
	 * 保存记录到数据库并返回自增id，自增id由程序自增
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long saveObjProReturnID(Object object);
	
	/**
	 * 保存记录到数据库并返回自增id，自增id由程序自增  支持传连接 
	 * @param conn  isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param object
	 * @return
	 */
	public Long saveObjProReturnID(Connection conn,boolean isOutConn,Object object);
	
	public Long saveObjectReturnIDWithTri(Object object) throws Exception;
	
	/**
	 * 获取自增值通过存储过程
	 * @param fildId 行id。1-taskId; 2-guid; 3-serialNum; 4-orderCode; 5-tableNum; 6-batchNumber;
	 * @param count 步长
	 * @return 返回自增
	 */
	public long getIdByPro(int fildId, int count);

	/**
	 * 删除对象
	 * @param <T>
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> boolean delete(Class<T> entityClass) throws Exception;
	
	/**
	 * 删除对象    支持传连接
	 * @param <T>
	 * @param conn   isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn  false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> boolean delete(Connection conn,boolean isOutConn,Class<T> entityClass) throws Exception;

	/**
	 * 删除对象根据ids
	 * @param <T>
	 * @param entityClass
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public <T> int delete(Class<T> entityClass, String ids) throws Exception;
	
	/**
	 * 删除对象根据ids  支持传连接
	 * @param <T>
	 * @param conn   isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn  false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public <T> int delete(Connection conn,boolean isOutConn,Class<T> entityClass, String ids) throws Exception;

	/**
	 * 删除对象根据过滤条件
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> int delete(Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap) throws Exception;
	
	/**
	 * 删除对象根据过滤条件  支持传连接
	 * @param <T>
	 * @param conn    isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn   false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> int delete(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap) throws Exception;

	/**
	 * 修改对象
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean update(Object object) throws Exception;
	
	/**
	 * 修改对象    支持传连接 
	 * @param conn  isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean update(Connection conn,boolean isOutConn,Object object) throws Exception;

	/**
	 * 修改对象根据过滤条件
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
	 * 修改对象根据过滤条件     支持传连接 
	 * @param <T>
	 * @param conn  isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception;
	
	/**
	 * 修改对象根据过滤条件     支持传连接  支持时间更新（监控专用）
	 * @param <T>
	 * @param conn  isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> boolean updateForMonitor(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception;
	
	/**
	 * 修改对象根据id
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
	 * 修改对象根据id    支持传连接  
	 * @param <T>
	 * @param conn   isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn  false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param objectMap
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, Object> objectMap, String id)
			throws Exception;
	
	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param objectMap
	 * @param id
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Class<T> entityClass,
			LinkedHashMap<String, Object> objectMap, String id,
			LinkedHashMap<String, String> conditionMap)
			throws Exception;
	
	
	/**
	 *           支持传连接  
	 * @param <T>
	 * @param conn   isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn  false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param objectMap
	 * @param id
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, Object> objectMap, String id,
			LinkedHashMap<String, String> conditionMap)
			throws Exception;
	
	/**
	 * 根据条件更新，支持><=等条件
	 * @param <T>
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return 返回受影响行数
	 * @throws Exception
	 */
	public <T> int updateBySymbolsCondition(Class<T> entityClass, LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap) throws Exception;
	
	/**
	 * 根据条件更新，支持><=等条件 支持传连接  
	 * @param <T>
	 * @param conn    isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn   false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> int updateBySymbolsCondition(Connection conn,boolean isOutConn,Class<T> entityClass, LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap) throws Exception;
	
	/**
	 * 查找对象根据id
	 * @param <T>
	 * @param entityClass
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T> T findObjectByID(Class<T> entityClass, long id) throws Exception;
	
	/**
	 * 查找对象根据id 支持传连接  
	 * @param <T>
	 * @param conn     isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn    false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T> T findObjectByID(Connection conn,boolean isOutConn,Class<T> entityClass, long id) throws Exception;


	/**
	 * 查找对象根据过滤条件
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
	 * 查找对象根据过滤条件 支持传连接 
	 * @param <T>
	 * @param conn       isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn  false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param distinctFieldList
	 * @param conditionMap
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findDistinctListBySymbolsCondition(Connection conn,boolean isOutConn,Class<T> entityClass,
			List<String> distinctFieldList,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception;

	/**
	 * 查找对象根据过滤条件
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
	 * 查找对象根据过滤条件   支持传连接 
	 * @param <T>
	 * @param conn     isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn    false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findListByCondition(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception;

	/**
	 * 查找对象根据过滤条件
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
	 * 查找对象根据过滤条件   支持传连接 
	 * @param <T>
	 * @param conn      isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn  false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findListBySymbolsCondition(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception;

	/**
	 * 查找对象根据过滤条件
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
	 * 查找对象根据过滤条件    支持传连接  
	 * @param <T>
	 * @param conn   isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn  false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param loginUserID
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findListBySymbolsCondition(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap) throws Exception;

	/**
	 * 查找对象根据过滤条件且带分页信息
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
	 * 查找对象根据过滤条件且带分页信息
	 * @param <T>
	 * @param conn   isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn  false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param loginUserID
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageListByCondition(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception;

	/**
	 * 查找对象根据过滤条件且带分页信息
	 * @param <T>
	 * @param GL_UserID
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
	
	/**
	 * 查找对象根据过滤条件且带分页信息
	 * @param <T>
	 * @param conn   isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn   false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param loginUserID
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageListBySymbolsCondition(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception;
	
	/**
	 * 查找对象根据过滤条件且带分页信息，第一页查总数，后续不查总数，pageInfo对象需包含总页数和总记录数
	 * @param <T>
	 * @param GL_UserID
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageListBySymbolsConditionNoCount(Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception;
	
	/**
	 * 查找对象根据过滤条件且带分页信息，第一页查总数，后续不查总数，pageInfo对象需包含总页数和总记录数
	 * @param <T>
	 * @param conn   isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn   false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param loginUserID
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPageListBySymbolsConditionNoCount(Connection conn,boolean isOutConn,Long loginUserID,
			Class<T> entityClass, LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception;
	
	//通过节点条件查询条数
	public Integer findLztj(String tableName,String wheresql)throws Exception;
	
	//通过表名，aiid查询
	public Map<String,String> findMapByTableCondition(String tableName,Long aiId)throws Exception;
	
	/**
	 * 通过表名，aiid查询
	 * @param conn    isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn    false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param tableName
	 * @param aiId
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> findMapByTableCondition(Connection conn,boolean isOutConn,String tableName,Long aiId)throws Exception;
	
	public <T> List<String[]> findListByConditionByColumName(Class<T> entityClass, LinkedHashMap<String, String> conditionMap,String columName,LinkedHashMap<String, String> orderbyMap) throws Exception;
	
	/**
	 * 
	 * @param <T>
	 * @param conn   isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn  false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param conditionMap
	 * @param columName
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<String[]> findListByConditionByColumName(Connection conn,boolean isOutConn,Class<T> entityClass, LinkedHashMap<String, String> conditionMap,String columName,LinkedHashMap<String, String> orderbyMap) throws Exception;
	
	/**
	 *  公共查询方法
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap
	 * @param columName
	 * @param orderbyMap
	 * @param groupColum
	 * @return
	 * @throws Exception
	 */
	public <T> List<String[]> findListByConditionByColumNameWithGroupBy(Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,String columName,
			LinkedHashMap<String, String> orderbyMap,String groupColum) throws Exception;
	
	/**
	 * 公共查询方法
	 * @param <T>
	 * @param conn   isOutConn为false时，conn传递null；isOutConn为true时，conn传递连接
	 * @param isOutConn  false：方法内部生成数据库连接；true：方法外部传递数据库连接进来
	 * @param entityClass
	 * @param conditionMap
	 * @param columName
	 * @param orderbyMap
	 * @param groupColum
	 * @return
	 * @throws Exception
	 */
	public <T> List<String[]> findListByConditionByColumNameWithGroupBy(Connection conn,boolean isOutConn,Class<T> entityClass,
			LinkedHashMap<String, String> conditionMap,String columName,
			LinkedHashMap<String, String> orderbyMap,String groupColum) throws Exception;
}
