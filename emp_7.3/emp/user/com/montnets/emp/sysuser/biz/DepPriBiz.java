package com.montnets.emp.sysuser.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.DepSpecialDAO;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDepUserBalance;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.sysuser.dao.SysuserDepDAO;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

public class DepPriBiz extends SuperBiz{

	/**
	 * 
	 * @param depCodeThird
	 * @return
	 * @throws Exception
	 */
	//验证机构编码是否已经存在
	public boolean isDepCodeThirdExists(String depCodeThird) throws Exception {
		boolean result = true;
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("depCodeThird", depCodeThird);
			List<LfDep> depsList = empDao.findListByCondition(LfDep.class, conditionMap, null);
			if(depsList == null || depsList.size() == 0){
				result = false;
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"验证机构编码是否存在出现异常！");
			throw e;
		}
		return result;
	}
	
	/**
	 *  判断该机构下多少人，超过1K返回FALSE
	 * @param depId
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public boolean isDepCount(Long depId,String corpCode) throws Exception {
		boolean result = true;
		List<LfSysuser> sysuserList = null;
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("depId", String.valueOf(depId));
			conditionMap.put("corpCode", corpCode);
			sysuserList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
			if(sysuserList != null && sysuserList.size() > StaticValue.MAX_PEOPLE_COUNT){
				result = false;
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"获取机构下人数失败！");
		}
		return result;
	}
	

	/**
	 * 删除机构
	 * @param depId
	 * @param commonVar
	 * @return
	 * @throws Exception
	 */
	public Integer deleteDep(Long depId, CommonVariables commonVar) throws Exception
	{

		if (depId == null)
		{
			return 0;
		}

		List<LfDep> sonsOfCurDelDep = this.getSonByParentDepId(depId);
		if (sonsOfCurDelDep != null && sonsOfCurDelDep.size() != 0)
		{
			//子机构不为空
			commonVar.setErrorCode(1);
			return 0;
		}

		SysuserBiz userBiz = new SysuserBiz();
		//获取该机构的操作�?
		List<LfSysuser> usersList = userBiz.getSysuserByDepId(depId);
		if (usersList != null && usersList.size() != 0)
		{
			commonVar.setErrorCode(2);
			return 0;
		}

		LfDep delDep = empDao.findObjectByID(LfDep.class, depId);
		//获取数据库连�?
		Connection conn = empTransDao.getConnection();
		Integer resultDelDep = 0;
		DepSpUserBindBiz domDepBiz = new DepSpUserBindBiz();
		try{
			//�?��事务
			empTransDao.beginTransaction(conn);
			domDepBiz.delDomDepUserByDepCode(conn, delDep.getDepCodeThird());
			this.deleteDomByDepId(conn, depId);
			resultDelDep = empTransDao.delete(conn, LfDep.class, depId.toString());
			//提交事务
			empTransDao.commitTransaction(conn);
		}catch(Exception e){
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "删除机构信息异常！");
			throw e;
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}

		return resultDelDep;
	}

	/**
	 * 通过机构id删除管辖机构
	 * @param conn
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	private Integer deleteDomByDepId(Connection conn, Long depId) throws Exception
	{
		Integer result = 0;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("depId", depId.toString());
			result = empTransDao.delete(conn, LfDomination.class, conditionMap);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "删除管辖机构异常！");
			throw e;
		}
		return result;
	}

	/**
	 *   删除机构并且将它�?��有的短信彩信�?值给它上级机�?
	 * @param depId
	 * @param commonVar
	 * @return
	 * @throws Exception
	 */
	public Integer deleteDepAndBalance(Long depId, CommonVariables commonVar,String corpCode,String pareDepId) throws Exception
	{

		if (depId == null)
		{
			return 0;
		}

		List<LfDep> sonsOfCurDelDep = this.getSonByParentDepId(depId);
		if (sonsOfCurDelDep != null && sonsOfCurDelDep.size() != 0)
		{
			commonVar.setErrorCode(1);
			return 0;
		}

		SysuserBiz userBiz = new SysuserBiz();
		//List<LfSysuser> usersList = userBiz.getSysuserByDepId(depId);
		List<LfSysuser> usersList = userBiz.getSysuserByDepIdNoDelete(depId);
		if (usersList != null && usersList.size() != 0)
		{
			commonVar.setErrorCode(2);
			return 0;
		}

		LfDep delDep = empDao.findObjectByID(LfDep.class, depId);
		//获取数据库连�?
		Connection conn = empTransDao.getConnection();
		Integer resultDelDep = 0;
		DepSpUserBindBiz domDepBiz = new DepSpUserBindBiz();
		try{
			//�?��事务
			empTransDao.beginTransaction(conn);
			domDepBiz.delDomDepUserByDepCode(conn, delDep.getDepCodeThird());
			this.deleteDomByDepId(conn, depId);
			
			if(SystemGlobals.isDepBilling(corpCode)){			
				//对改机构的充值回收做处理
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("corpCode", corpCode);
				conditionMap.put("targetId", String.valueOf(depId));
				List<LfDepUserBalance> depUserBalances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
				if(depUserBalances != null && depUserBalances.size()>0){
					LfDepUserBalance sonBalance = depUserBalances.get(0);
					conditionMap.clear();
					conditionMap.put("corpCode", corpCode);
					conditionMap.put("targetId", pareDepId);
					List<LfDepUserBalance> balances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
					if(balances!=null && balances.size()>0){
						LfDepUserBalance pareBalance = balances.get(0);
						pareBalance.setMmsBalance(pareBalance.getMmsBalance()+sonBalance.getMmsBalance());//彩信余额
						pareBalance.setMmsCount(pareBalance.getMmsCount()+sonBalance.getMmsCount());
						pareBalance.setSmsBalance(sonBalance.getSmsBalance()+pareBalance.getSmsBalance());//短信余额
						pareBalance.setSmsCount(sonBalance.getSmsCount()+pareBalance.getSmsCount());
						empTransDao.update(conn, pareBalance);
						empTransDao.delete(conn, LfDepUserBalance.class, String.valueOf(sonBalance.getBlId()));
					}
				}
			}
			//resultDelDep = empTransDao.delete(conn, LfDep.class, depId.toString());
			//设置为隐�?
			delDep.setDepState(2);
			empTransDao.update(conn, delDep);
			//提交事务
			empTransDao.commitTransaction(conn);
			resultDelDep = 1; 
		}catch(Exception e){
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"删除操作员机构失败！");
			resultDelDep = 2;
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}

		return resultDelDep;
	}
	
	/**
	 * 通过机构id获取子机�?
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> getSonByParentDepId(Long depId) throws Exception
	{

		if (depId == null)
		{
			//如果传入的机构id为空，则抛出异常
			throw new NullPointerException("传入的depId为空");
		}
		List<LfDep> itemsList;

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try
		{
			//机构id
			conditionMap.put("superiorId&=", depId.toString());
			//机构状�?
			conditionMap.put("depState", "1");
			orderbyMap.put("depId", "asc");

			conditionMap.put("depId&<>", depId.toString());
			itemsList = empDao.findListBySymbolsCondition(LfDep.class,
					conditionMap, orderbyMap);

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取子机构发生异常！");
			throw e;
		}
		return itemsList;

	}
	/**
	 * 判断机构名称是否存在
	 * @param depName
	 * @return
	 * @throws Exception
	 */
	public boolean isDepNameExists(String depName) throws Exception
	{
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
			//机构名称
			conditionMap.put("depName", depName);
			List<LfDep> tempList = empDao.findListByCondition(LfDep.class,
					conditionMap, null);
			if (tempList == null || tempList.size() == 0)
			{
				result = false;
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "判断机构名称是否存在发生异常！");
			throw e;
		}
		return result;
	}


	/**
	 * 是否存在子机�?
	 * @param code
	 * @param level
	 * @return
	 * @throws Exception
	 */
	/*public boolean isExistsSonDep(String code, Integer level,String corpCode) throws Exception {
		boolean result = true;
		if(code == null || "".equals(code.trim()) || level == null){
			throw new NullPointerException("传入的机构编码或机构登记为空");
		}
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			String codes = code.substring(0, level * 2);
			//机构编码
			conditionMap.put("depCode&like2", codes);
			//企业编码
			conditionMap.put("corpCode&=", corpCode);
			//机构级别
			conditionMap.put("depLevel&<>", level.toString());
			List<LfDep> depsList = empDao.findListBySymbolsCondition(LfDep.class,conditionMap, null);
			if(depsList == null || depsList.size() == 0){
				result = false;
			}
		}catch(Exception e){
			throw e;
		}
		//返回结果
		return result;
	}*/
	
	/**
	 * 	判断当前新增机构是否已超�?机构�?��级数�?同级机构数�?�?��机构数量
	 * @param corpCode
	 * @param depId
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 */
	public String checkDepsCount(String corpCode,String depId) throws Exception {
		//同级�?��机构�?
		SysuserDepDAO lfDepVoDAO = new SysuserDepDAO();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", corpCode);
		try{
			//获取全局配置表的机构数配
			LinkedHashMap<String, String> confMap = SystemGlobals.getSysParamLfcorpConf(corpCode);
			Integer currentLevel = new DepDAO().getUserDepLevel(Long.valueOf(depId));
			//当前机构是否超出机构�?��层级
			if(currentLevel >= Integer.parseInt(confMap.get(StaticValue.DEP_MAXLEVEL))){
				return "maxLevel";
			}
			//当前机构是否超出同级机构
			//将该注释放开，手动添加操作员机构需要做限制判断,不然最大子机构数配置参数不生效 --tanjy 2021/2/22
			Integer sameLevelCount = lfDepVoDAO.getDepCount(corpCode, depId);
			if(sameLevelCount >= Integer.parseInt(confMap.get(StaticValue.DEP_MAXCHILD))){
				return "maxChild";
			} 
			//当前机构是否超出机构总数
			Integer allDepsCount =  lfDepVoDAO.getDepCount(corpCode, null);
			if(allDepsCount >= Integer.parseInt(confMap.get(StaticValue.DEP_MAXDEP)) ){
				return "maxDep";
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "判断机构是否超出限制出现异常！");
			throw e;
		}
		return "true";
	}
	
	/**
	 * 
	 * @param dep
	 * @return
	 * @throws Exception
	 */ 
	//添加机构
	public boolean addDep(LfDep dep) throws Exception {
		if (dep == null) {
			return false;
		}
		if(dep.getCorpCode()==null||"".equals(dep.getCorpCode())){
			return false;
		}
		boolean result = false;
		List<LfSysuser> usersList = new DepSpecialDAO().findDomSysuserByDepID(dep
				.getSuperiorId().toString(), null);
		//获取数据库连�?
		Connection conn = empTransDao.getConnection();
		try{
			//�?��事务
			empTransDao.beginTransaction(conn);
			Long depId = empTransDao.saveObjectReturnID(conn, dep);
			dep.setDepId(depId);
			dep.setDeppath(dep.getDeppath()+depId+"/");
			empTransDao.update(conn, dep);

			result = this.addDomAtAddDep(conn,usersList, dep);
			//提交事务
			empTransDao.commitTransaction(conn);
		}catch(Exception e){
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"新增操作员机构失败！");
			throw new Exception(e.getMessage());
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param conn
	 * @param dep
	 * @return
	 * @throws Exception
	 */
	private boolean addDomAtAddDep(Connection conn,List<LfSysuser> usersList, LfDep dep) throws Exception
	{
		boolean result = false;
		for (int i = 0; i < usersList.size(); i++) {
			LfDomination dom = new LfDomination();
			dom.setDepId(dep.getDepId());
			dom.setUserId(usersList.get(i).getUserId());
			result = empTransDao.save(conn, dom);
		}
		return result;
	}
	
	/**
	 * 获取操作员机构级�?
	 * @param depId 机构ID
	 * @return
	 * @throws Exception 
	
	public Integer getUserDepLevel(Long depId) throws Exception{
		return new GenericLfDepVoDAO().getUserDepLevel(depId);
	}
 */
	
	/**
	 *  根据用户的guid
	 * @param guid
	 * @return
	 * @throws Exception
	
	public Integer getDepLevelByUserGuId(Long guid) throws Exception {
		
		LfSysuser lfSysuser = new BaseBiz().getByGuId(LfSysuser.class, guid);
		
		return getUserDepLevel(lfSysuser.getDepId());
	}
	 */
	
	
	/**
	 * 获取操作员对象
	 * @param request
	 * @return
	 */
	public LfSysuser getCurrenUser(HttpServletRequest request){
		try
		{
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj == null)
			{
				return null;
			}
			
			return (LfSysuser)loginSysuserObj;
			
		}catch(Exception e)
		{
			EmpExecutionContext.error("SESSION获取操作员对象失败。");
			return null;
		}
	}
	
}
