package com.montnets.emp.sysuser.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasroute.LfSpDepBind;

/**
 * 
 * @author Administrator
 *
 */
public class DepSpUserBindBiz extends SuperBiz 
{
	
	
	
	/**
	 * 
	 * @param conn
	 * @param spUsers
	 * @return
	 * @throws Exception
	 */
	public Integer delDomDepBandByUserId(Connection conn, String spUsers) throws Exception {
		Integer result = null;
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("spUser", spUsers);
			result = empTransDao.delete(conn, LfSpDepBind.class, conditionMap);
		}catch(Exception e){
			EmpExecutionContext.error(e, "删除机构绑定关系出现异常！");
			throw e;
		}
		return result;
	}
	
	/**
	 * 
	 * @param wgempsList
	 * @return
	 * @throws Exception
	 */
	public boolean updateDomDepUser(List<LfSpDepBind> wgempsList) throws Exception {
		if(wgempsList == null || wgempsList.size() == 0){
			return false;
		}
		boolean result = false;
		//获取连接
		Connection conn = empTransDao.getConnection();
		try{
			//开启事务
			empTransDao.beginTransaction(conn);
			
			this.delDomDepUserByDepCode(conn, wgempsList.get(0).getDepCodeThird());
			empTransDao.save(conn, wgempsList, LfSpDepBind.class);
			//提交事务
			empTransDao.commitTransaction(conn);
			result = true;
		}catch(Exception e){
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "保存机构绑定关系出现异常！");
			throw e;
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 
	 * @param conn
	 * @param newCodeThird
	 * @param oldCodeThirds
	 * @return
	 * @throws Exception
	 */

	public boolean updateCodeThirdByCode(Connection conn, String newCodeThird, String oldCodeThirds) throws Exception {
		boolean result = false;
		try{
			if(newCodeThird==null){
				newCodeThird="";
			}
			if(oldCodeThirds==null){
				oldCodeThirds="";
			}
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			objectMap.put("depCodeThird", newCodeThird);
			conditionMap.put("depCodeThird", oldCodeThirds);
			result = empTransDao.update(conn, LfSpDepBind.class, objectMap, conditionMap);
		}catch(Exception e){
			EmpExecutionContext.error(e, "更新机构绑定关系出现异常！");
			throw e;
		}
		return result;
	}
	
	/**
	 * 
	 * @param conn
	 * @param depCodeThirds
	 * @return
	 * @throws Exception
	 */

	public Integer delDomDepUserByDepCode(Connection conn, String depCodeThirds) throws Exception {
		Integer result = null;
		try{
			if(depCodeThirds==null){
				depCodeThirds="";
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("depCodeThird", depCodeThirds);
			result = empTransDao.delete(conn, LfSpDepBind.class, conditionMap);
		}catch(Exception e){
			EmpExecutionContext.error(e, "删除机构绑定关系出现异常！");
			throw e;
		}
		return result;
	}
	
	/**
	 * 
	 * @param depCodeThirds
	 * @return
	 * @throws Exception
	 */

	public Integer delDomDepUserByDepCode(String depCodeThirds) throws Exception {
		Integer result = null;
		try{
			if(depCodeThirds==null){
				depCodeThirds="";
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("depCodeThird", depCodeThirds);
			result = empDao.delete(LfSpDepBind.class, conditionMap);
		}catch(Exception e){
			EmpExecutionContext.error(e, "删除机构绑定关系出现异常！");
			throw e;
		}
		return result;
	}
	
	/**
	 * 
	 * @param conn
	 * @param userId
	 * @param bindType
	 * @return
	 * @throws Exception
	 */

	public Integer delDomSpUserByUserId(Connection conn, String userId, Integer bindType) throws Exception {
		Integer result = null;
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", userId);
			conditionMap.put("bindType", bindType.toString());
			result = empTransDao.delete(conn, LfSpDepBind.class, conditionMap);
		}catch(Exception e){
			EmpExecutionContext.error(e, "删除机构绑定关系出现异常！");
			throw e;
		}
		return result;
	}
	
	/**
	 * 
	 * @param depCodeThird
	 * @return
	 * @throws Exception
	 */

	public List<LfSpDepBind> getDepsByCode(String depCodeThird) throws Exception {
		List<LfSpDepBind> wg2empsList = null;
		try{
			if(depCodeThird==null){
				depCodeThird="";
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("depCodeThird", depCodeThird);
			wg2empsList = empDao.findListByCondition(LfSpDepBind.class, conditionMap, null);
		}catch(Exception e){
			EmpExecutionContext.error(e, "获取机构绑定列表出现异常！");
			throw e;
		}
		return wg2empsList;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
/*
	public List<GtPortUsed> findGtPortUsedMonopolize() throws Exception{
		return empSpecialDAO.findGtPortUsedMonopolize();
	}*/
	
	/**
	 * 
	 * @param depCodeThird
	 * @return
	 * @throws Exception
	 */
	/*
	public List<GtPortUsed> findUpdateGtPortUsedMonopolize(String depCodeThird) throws Exception{
		return empSpecialDAO.findUpdateGtPortUsedMonopolize(depCodeThird);
	}*/
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	/*public List<GtPortUsed> findGtPortUsedShare() throws Exception{
		return empSpecialDAO.findGtPortUsedShare();
	}*/
	
	/**
	 * 
	 * @param curLoginedUserId
	 * @return
	 * @throws Exception
	 */
	public List<LfSpDepBind> getSpUserIsBindByUserId(Long curLoginedUserId) throws Exception {
		if(curLoginedUserId == null){
			return null;
		}
		List<LfSpDepBind> wg2empsList = null;
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", curLoginedUserId.toString());
			wg2empsList = empDao.findListByCondition(LfSpDepBind.class, conditionMap, null);
		}catch(Exception e){
			EmpExecutionContext.error(e, "获取机构绑定列表出现异常！");
			throw e;
		}
		return wg2empsList;
	}
	
}
