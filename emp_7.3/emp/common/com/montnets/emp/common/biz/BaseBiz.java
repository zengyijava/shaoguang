package com.montnets.emp.common.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author Administrator
 *
 */
public class BaseBiz extends SuperBiz
{

	/**
	 * 保存单个对象
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean addObj(Object object) throws Exception {
		return empDao.save(object);
	}
	
	/**
	 * 保存单个对象
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean addObj(Connection conn, Object object) throws Exception {
		return empTransDao.save(conn, object);
	}
	
	/**
	 * 保存单个对象
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long addObjReturnId(Object object) throws Exception {
		return empDao.saveObjectReturnID(object);
	}
	
	/**
	 * 保存单个对象，存储过程自增id
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long addObjProReturnId(Object object) throws Exception {
		return empDao.saveObjProReturnID(object);
	}
	
	public Long addObjReturnId(Connection conn, Object object) throws Exception {
		return empTransDao.saveObjectReturnID(conn, object);
	}
	
	/**
	 * 保存对象到数据库，存储过程自增id
	 * @param conn
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long addObjProReturnId(Connection conn, Object object) throws Exception {
		return empTransDao.saveObjProReturnID(conn, object);
	}
	
	/**
	 * 保存一个list集合中的所有对象
	 * @param <T>
	 * @param entityClass
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public <T> Integer addList(Class<T> entityClass, List<T> list) throws Exception {

		return empDao.save(list, entityClass);
	}
	
	
	public <T> List<T> findListByCondition(Class<T> entityClass,LinkedHashMap<String, String> conditionMap,LinkedHashMap<String, String> orderbyMap) throws Exception 
	{

		return empDao.findListByCondition(entityClass, conditionMap, orderbyMap);
	}
	/**
	 * 保存一个list集合中的所有对象
	 * @param <T>
	 * @param entityClass
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public <T> Integer addList(Connection conn, Class<T> entityClass, List<T> list) throws Exception {

		return empTransDao.save(conn, list, entityClass);
	}
	
	/**
	 * 根据过滤条件及分页信息查询一个对象集合
	 * @param <T>
	 * @param entityClass
	 * @param loginUserID
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getByCondition(Class<T> entityClass, Long loginUserID, LinkedHashMap<String,String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo) throws Exception {

		return empDao.findPageListBySymbolsCondition(loginUserID,entityClass,conditionMap, orderbyMap, pageInfo);

	}
	
	/**
	 * 根据过滤条件及分页信息查询一个对象集合，第一页查总数，后续不查总数，pageInfo对象需包含总页数和总记录数
	 * @param <T>
	 * @param entityClass
	 * @param loginUserID
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getByConditionNoCount(Class<T> entityClass, Long loginUserID, LinkedHashMap<String,String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo) throws Exception {

		return empDao.findPageListBySymbolsConditionNoCount(loginUserID,entityClass,conditionMap, orderbyMap, pageInfo);

	}
	
	/**
	 * 根据过滤条件查询一个对象集合
	 * @param <T>
	 * @param entityClass
	 * @param loginUserID
	 * @param conditionMap
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getByCondition(Class<T> entityClass, Long loginUserID, LinkedHashMap<String,String> conditionMap, LinkedHashMap<String, String> orderbyMap) throws Exception {

		return empDao.findListBySymbolsCondition(loginUserID,entityClass,conditionMap, orderbyMap);

	}
	
	/**
	 * 根据过滤条件查询对象集合
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getByCondition(Class<T> entityClass, LinkedHashMap<String,String> conditionMap, LinkedHashMap<String, String> orderbyMap) throws Exception {

		return empDao.findListBySymbolsCondition(entityClass, conditionMap, orderbyMap);
		
	}
	
	/**
	 * 根据过滤条件查询对象集合 支持传入连接
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap
	 * @param orderbyMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getByCondition(Connection conn,boolean isOutConn,Class<T> entityClass, LinkedHashMap<String,String> conditionMap, LinkedHashMap<String, String> orderbyMap) throws Exception {

		return empDao.findListBySymbolsCondition(conn,isOutConn,entityClass, conditionMap, orderbyMap);
		
	}
	
	/**
	 * 查找对象根据过滤条件且带分页信息
	 * @description    
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
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-13 下午07:01:45
	 */
	public <T> List<T> getByCondition(Connection conn,boolean isOutConn,Class<T> entityClass, LinkedHashMap<String,String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo) throws Exception {

		return empDao.findPageListByCondition(conn,isOutConn,null,entityClass, conditionMap, orderbyMap, pageInfo);
		
	}
	
	/**
	 * 查询某个对象表中的所有数据
	 * @param <T>
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getEntityList(Class<T> entityClass) throws Exception {
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		return empDao.findListBySymbolsCondition(entityClass, conditionMap, orderbyMap);
	}
	
	/**
	 * 根据id获取一个对象
	 * @param <T>
	 * @param entityClass
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T> T getById(Class<T> entityClass, Long id) throws Exception {

		return empDao.findObjectByID(entityClass, id);

	}
	
	/**
	 * 根据id获取一个对象
	 * @param <T>
	 * @param entityClass
	 * @param strId
	 * @return
	 * @throws Exception
	 */
	public <T> T getById(Class<T> entityClass, String strId) throws Exception {

		return empDao.findObjectByID(entityClass, Long.valueOf(strId));

	}
	
	/**
	 * 根据guid获取一个对象
	 * @param <T>
	 * @param entityClass
	 * @param guId
	 * @return
	 * @throws Exception
	 */
	public <T> T getByGuId(Class<T> entityClass, Long guId) throws Exception {
		
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("guId", String.valueOf(guId));
		List<T> tempsList = empDao.findListByCondition(entityClass, conditionMap, null);
		if(tempsList != null && tempsList.size() > 0){
			return tempsList.get(0);
		}
		return null;
		
	}
	
	/**
	 * 根据ids删除所有对象
	 * @param <T>
	 * @param entityClass
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public <T> Integer deleteByIds(Class<T> entityClass, String ids) throws Exception {

		return empDao.delete(entityClass, ids);
	}
	
	/**
	 * 根据ids删除所有对象
	 * @param <T>
	 * @param entityClass
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public <T> Integer deleteByIds(Connection conn, Class<T> entityClass, String ids) throws Exception {

		return empTransDao.delete(conn, entityClass, ids);
	}
	
	/**
	 * 根据过滤条件删除对象
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> Integer deleteByCondition(Class<T> entityClass, LinkedHashMap<String, String> conditionMap) throws Exception {

		return empDao.delete(entityClass, conditionMap);
	}
	
	/**
	 * 根据过滤条件删除对象
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> Integer deleteByCondition(Connection conn, Class<T> entityClass, LinkedHashMap<String, String> conditionMap) throws Exception {

		return empTransDao.delete(conn, entityClass, conditionMap);
	}
	
	/**
	 * 删除某个对象表中的所有数据
	 * @param <T>
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> boolean deleteAll(Class<T> entityClass) throws Exception {
		return empDao.delete(entityClass);
	}
	
	/**
	 * 删除某个对象表中的所有数据
	 * @param <T>
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public <T> boolean deleteAll(Connection conn, Class<T> entityClass) throws Exception {
		return empTransDao.delete(conn, entityClass);
	}
	
	/**
	 * 修改一个对象
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean updateObj(Object object) throws Exception {

		return empDao.update(object);

	}
	
	/**
	 * 修改一个对象
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public boolean updateObj(Connection conn, Object object) throws Exception {

		return empTransDao.update(conn, object);

	}

	/**
	 * 根据过滤条件 修改对象
	 * @param <T>
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Class<T> entityClass,LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		return empDao.update(entityClass, objectMap, conditionMap);
	}
	
	/**
	 * 根据过滤条件 修改对象     支持传连接  
	 * @param <T>
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Connection conn,boolean isOutConn,Class<T> entityClass,LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		return empDao.update(conn,isOutConn,entityClass, objectMap, conditionMap);
	}
	
	/**
	 * 根据过滤条件 修改对象     支持传连接    支持时间更新（监控专用）
	 * @param <T>
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> boolean updateForMonitor(Connection conn,boolean isOutConn,Class<T> entityClass,LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		return empDao.updateForMonitor(conn,isOutConn,entityClass, objectMap, conditionMap);
	}
	
	/**
	 * 根据过滤条件 修改对象
	 * @param <T>
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public <T> boolean update(Connection conn, Class<T> entityClass,LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		return empTransDao.update(conn, entityClass, objectMap, conditionMap);
	}
	
	/**
	 * 根据条件更新，支持><=等条件
	 * @param <T>
	 * @param entityClass
	 * @param objectMap
	 * @param conditionMap
	 * @return 返回受影响行数
	 * @throws Exception
	 */
	public <T> int updateBySymbolsCondition(Class<T> entityClass, LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap) throws Exception{
		return empDao.updateBySymbolsCondition(entityClass, objectMap, conditionMap);
	}
	
	/**
	 * 根据操作员id获取操作员对象
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public LfSysuser getLfSysuserByUserId(String userId)  throws Exception
	{
		return empDao.findObjectByID(LfSysuser.class, Long.valueOf(userId));
	}
	
	/**
	 * 根据操作员id获取操作员对象
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public LfSysuser getLfSysuserByUserId(Long userId)  throws Exception
	{
		return empDao.findObjectByID(LfSysuser.class, userId);
	}
	
	/**
	 * 获取一个连接
	 * @return
	 */
	public Connection getConnection()
	{
		return empTransDao.getConnection();
	}
	
	/**
	 * 开启一个事务
	 * @param conn
	 * @throws Exception
	 */
	public void beginTransaction(Connection conn) throws Exception
	{
		empTransDao.beginTransaction(conn);
	}
	
	/**
	 * 提交事务
	 * @param conn
	 * @throws Exception
	 */
	public void commitTransaction(Connection conn) throws Exception
	{
		empTransDao.commitTransaction(conn);
	}
	
	/**
	 * 回滚事务
	 * @param conn
	 */
	public void rollBackTransaction(Connection conn)
	{
		empTransDao.rollBackTransaction(conn);
	}
	
	/**
	 * 关闭连接
	 * @param conn
	 */
	public void closeConnection(Connection conn)
	{
		empTransDao.closeConnection(conn);
	}
	
}
