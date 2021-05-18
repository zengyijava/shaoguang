package com.montnets.emp.client.biz;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author Administrator
 *
 */
public class AddrBookBaseBiz extends SuperBiz
{
	
	public AddrBookBaseBiz(){
		//初始化数据库操作dao
	}
	
	/**
	 * 保存到数据库
	 * @param object 保存对象
	 * @return 成功返回true
	 * @throws Exception
	 */
	public boolean addObj(Object object) throws Exception {
		//返回结果
		return empDao.save(object);
	}
	
	/**
	 * 保存到数据库并返回自增id
	 * @param object 保存对象
	 * @return 成功返回自增id
	 * @throws Exception
	 */
	public Long addObjReturnId(Object object) throws Exception {
		//返回自增id
		return empDao.saveObjectReturnID(object);
	}
	
	/**
	 * 批量保存到数据库
	 * @param <T>
	 * @param entityClass
	 * @param list 保存对象集合
	 * @return 返回影响行数
	 * @throws Exception
	 */
	public <T> Integer addList(Class<T> entityClass, List<T> list) throws Exception {
		//返回结果数
		return empDao.save(list, entityClass);
	}
	
	/**
	 * 通过条件查询记录
	 * @param <T>
	 * @param entityClass
	 * @param loginUserID 当前登录操作员id
	 * @param conditionMap 条件
	 * @param orderbyMap 排序
	 * @param pageInfo 分页
	 * @return 返回记录
	 * @throws Exception
	 */
	public <T> List<T> getByCondition(Class<T> entityClass, Long loginUserID, LinkedHashMap<String,String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo) throws Exception {
		//返回结果
		return empDao.findPageListBySymbolsConditionNoCount(loginUserID,entityClass,conditionMap, orderbyMap, pageInfo);

	}
	
	/**
	 * 通过条件查询记录
	 * @param <T>
	 * @param entityClass
	 * @param loginUserID 当前登录操作员id
	 * @param conditionMap 条件
	 * @param orderbyMap 排序
	 * @return 返回记录
	 * @throws Exception
	 */
	public <T> List<T> getByCondition(Class<T> entityClass, Long loginUserID, LinkedHashMap<String,String> conditionMap, LinkedHashMap<String, String> orderbyMap) throws Exception {
		//返回结果
		return empDao.findListBySymbolsCondition(loginUserID,entityClass,conditionMap, orderbyMap);

	}
	
	/**
	 * 通过条件查询记录
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap 条件
	 * @param orderbyMap 排序
	 * @return 返回记录
	 * @throws Exception
	 */
	public <T> List<T> getByCondition(Class<T> entityClass, LinkedHashMap<String,String> conditionMap, LinkedHashMap<String, String> orderbyMap) throws Exception {
		//返回结果
		return empDao.findListBySymbolsCondition(entityClass, conditionMap, orderbyMap);
		
	}
	
	/**
	 * 获取所有记录
	 * @param <T>
	 * @param entityClass
	 * @return 返回记录
	 * @throws Exception
	 */
	public <T> List<T> getEntityList(Class<T> entityClass) throws Exception {
		//条件
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		//排序
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		//返回结果
		return empDao.findListBySymbolsCondition(entityClass, conditionMap, orderbyMap);
	}
	
	/**
	 * 根据id获取对象
	 * @param <T>
	 * @param entityClass
	 * @param id 标识id
	 * @return 返回对象
	 * @throws Exception
	 */
	public <T> T getById(Class<T> entityClass, Long id) throws Exception {
		//返回结果
		return empDao.findObjectByID(entityClass, id);

	}
	
	/**
	 * 根据id获取对象
	 * @param <T>
	 * @param entityClass
	 * @param strId 标识id
	 * @return 返回对象
	 * @throws Exception
	 */
	public <T> T getById(Class<T> entityClass, String strId) throws Exception {
		//返回结果
		return empDao.findObjectByID(entityClass, Long.valueOf(strId));

	}
	
	/**
	 * 根据guid获取对象
	 * @param <T>
	 * @param entityClass
	 * @param guId
	 * @return 返回对象
	 * @throws Exception
	 */
	public <T> T getByGuId(Class<T> entityClass, Long guId) throws Exception {
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//guid
		conditionMap.put("guId", String.valueOf(guId));
		//执行查询
		List<T> tempsList = empDao.findListByCondition(entityClass, conditionMap, null);
		if(tempsList != null && tempsList.size() > 0){
			//返回结果
			return tempsList.get(0);
		}
		//返回null
		return null;
		
	}
	
	/**
	 * 根据id删除记录
	 * @param <T>
	 * @param entityClass
	 * @param ids 标识id
	 * @return 返回影响行数
	 * @throws Exception
	 */
	public <T> Integer deleteByIds(Class<T> entityClass, String ids) throws Exception {
		//返回结果
		return empDao.delete(entityClass, ids);
	}
	
	/**
	 * 根据条件删除记录
	 * @param <T>
	 * @param entityClass
	 * @param conditionMap 条件
	 * @return 返回影响行数
	 * @throws Exception
	 */
	public <T> Integer deleteByCondition(Class<T> entityClass, LinkedHashMap<String, String> conditionMap) throws Exception {
		//返回结果
		return empDao.delete(entityClass, conditionMap);
	}
	
	/**
	 * 删除所有记录
	 * @param <T>
	 * @param entityClass
	 * @return 成功返回true
	 * @throws Exception
	 */
	public <T> boolean deleteAll(Class<T> entityClass) throws Exception {
		//返回结果
		return empDao.delete(entityClass);
	}
	
	/**
	 * 更新对象
	 * @param object 要更新对象
	 * @return 成功返回true
	 * @throws Exception
	 */
	public boolean updateObj(Object object) throws Exception {
		//返回结果
		return empDao.update(object);

	}

	/**
	 * 按条件更新对象
	 * @param <T>
	 * @param entityClass
	 * @param objectMap 更新值
	 * @param conditionMap 条件
	 * @return 成功返回true
	 * @throws Exception
	 */
	public <T> boolean update(Class<T> entityClass,LinkedHashMap<String, String> objectMap,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		//返回结果
		return empDao.update(entityClass, objectMap, conditionMap);
	}
	
	/**
	 * 根据guid获取对象
	 * @param <T>
	 * @param entityClass
	 * @param guId
	 * @return 返回对象
	 */
	public <T> T getObjByGuid(Class<T> entityClass, Long guId) {
		//不允许为空
		if(entityClass == null || guId == null){
			//返回
			return null;
		}
		
		T obj = null;
		try{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//guid
			conditionMap.put("guId", String.valueOf(guId));
			//查询
			List<T> objsList = empDao.findListByCondition(entityClass, conditionMap, null);
			if(objsList != null && objsList.size() > 0){
				//返回结果
				obj = objsList.get(0);
			}
			
		}catch(Exception e){
			EmpExecutionContext.error(e,"获取guid所对应的对象失败！");
		}
		//返回
		return obj;
	}
	
	/**
	 * 根据操作员id获取操作员对象
	 * @param userId 操作员id
	 * @return 返回对象
	 * @throws Exception
	 */
	public LfSysuser getLfSysuserByUserId(String userId)  throws Exception
	{
		//返回结果
		return empDao.findObjectByID(LfSysuser.class, Long.valueOf(userId));
	}
	
	/**
	 * 根据操作员id获取操作员对象
	 * @param userId 操作员id
	 * @return 返回对象
	 * @throws Exception
	 */
	public LfSysuser getLfSysuserByUserId(Long userId)  throws Exception
	{
		//返回结果
		return empDao.findObjectByID(LfSysuser.class, userId);
	}
	
}
