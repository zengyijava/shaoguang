package com.montnets.emp.wyquery.biz;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wyquery.dao.HistoryRecordDAO;
import com.montnets.emp.wyquery.vo.LfMttaskVo;
import com.montnets.emp.wyquery.vo.SendedMttaskVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class HistoryRecordBiz extends SuperBiz{
	
	private HistoryRecordDAO smstaskDao;
	public HistoryRecordBiz()
	{
		smstaskDao=new HistoryRecordDAO();
	}
	
	/**
	 * 机构树
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public String getDepartmentJosnData(Long userid) throws Exception{
		StringBuffer tree = null;
		//根据用户id获取用户信息
		LfSysuser currUser = empDao.findObjectByID(LfSysuser.class, userid);
		if(currUser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			//机构biz
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			
			try {
				//如果是100000的企业
				if(currUser.getCorpCode().equals("100000"))
				{
					LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
					LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
					conditionMap.put("depState", "1");
					orderbyMap.put("depId", StaticValue.ASC);
					
					lfDeps = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
				}else
				{
					//获取管辖范围内的所有机构
					/*备份 pengj
					lfDeps = depBiz.getAllDeps(userid);
					*/
					//新增 pengj
					lfDeps = depBiz.getAllDepByUserIdAndCorpCode(userid, currUser.getCorpCode());
				}
				LfDep lfDep = null;
				//机构树的json数据拼写
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					//tree.append(",level:").append(lfDep.getDepLevel());
					//tree.append(",dlevel:").append(lfDep.getDepLevel());
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
		
			} catch (Exception e) {
				//异常处理
				EmpExecutionContext.error(e,"机构树加载biz层异常！");
			}
		}
		if(tree==null){
			return "[]";
		}
		return tree.toString();
	}
	
	/**
	 * 树
	 * @return
	 */
	public String getDepartmentJosnData2(Long depId,Long userId){
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			sysuser = empDao.findObjectByID(LfSysuser.class, userId);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发历史或群发任务查询操作员信息异常！");
			tree = new StringBuffer("[]");
		}
		if(sysuser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			
			try {
				lfDeps = null;
				
				 if ((sysuser != null) && (sysuser.getCorpCode().equals("100000")))
			        {
			          if (depId == null)
			          {
			            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			            LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();

			            conditionMap.put("superiorId", "0");

			            conditionMap.put("depState", "1");

			            orderbyMap.put("depId", "ASC");
			            orderbyMap.put("deppath", "ASC");
			            lfDeps = new BaseBiz().getByCondition(LfDep.class, conditionMap, orderbyMap);
			          }
			          else
			          { 
			        	/*备份 pengj
			            lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			         	*/
			        	//新增 pengj
			        	lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId, sysuser.getCorpCode());
			          }

			        }
			        else if (depId == null) {
			          lfDeps = new ArrayList();
			          /*备份 pengj
					  LfDep lfDep = (LfDep)depBiz.getAllDeps(userId).get(0);
					  */
					  //新增 pengj
			          LfDep lfDep = (LfDep)depBiz.getAllDepByUserIdAndCorpCode(userId, sysuser.getCorpCode()).get(0);
						
			          lfDeps.add(lfDep);
			        }
			        else {
			        	/*备份 pengj
			            lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			         	*/
			        	//新增 pengj
			        	lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId, sysuser.getCorpCode());
			        }
				
				
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
		
			} catch (Exception e) {
				EmpExecutionContext.error(e,"群发历史或群发任务操作员树加载biz层异常！");
				tree = new StringBuffer("[]");
			}
		}
		return tree.toString();
	}
	
	/**
	 * 发送详情
	 * @param conditionMap
	 * @param orderMap
	 * @param pageInfo
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List <SendedMttaskVo> getMtTask(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String,String> orderMap,PageInfo pageInfo,String tableName)
	throws Exception
	{
		List<SendedMttaskVo> mttaskList = null;
		try {
			//如果分页信息为空则表名是导出数据的查询
			if(pageInfo == null)
			{
				mttaskList =  smstaskDao.findMtTaskVo(conditionMap,orderMap,tableName);
			}
			//如果分页信息为不为空时表示是详情页面的查询
			else
			{
				mttaskList=smstaskDao.findMtTaskVo(conditionMap, orderMap,pageInfo,tableName);

			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "网优群发历史发送详情异常。");
			throw e;
		}
		return mttaskList;
	}
	
	
	/**
	 * 群发历史与群发任务查询的方法
	 * @param curLoginedUser
	 * @param lfMttaskVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfMttaskVo> getLfMttaskVo(Long curLoginedUser,
			LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception
	{
		List<LfMttaskVo> mtTaskVosList;
		try
		{
			if (pageInfo == null)
			{
				mtTaskVosList = smstaskDao.findLfMttaskVo(curLoginedUser,
						lfMttaskVo);
			} else
			{
				mtTaskVosList = smstaskDao.findLfMttaskVo(curLoginedUser,
						lfMttaskVo, pageInfo);
			}
			
			//拼装VO
			mtTaskVosList=completeLfMttaskVo(mtTaskVosList);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "网优群发历史查询异常。");
			//异常处理
			throw e;
		}
		return mtTaskVosList;
	}
	
	/**
	 * 发送详情（查两个表示时调用）
	 * @param conditionMap
	 * @param orderMap
	 * @param pageInfo
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List <SendedMttaskVo> getMtTaskTwo(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String,String> orderMap,PageInfo pageInfo,String tableName)
	throws Exception
	{
		List<SendedMttaskVo> mttaskList = null;
		try {
			//如果分页信息为空则表名是导出数据的查询
			if(pageInfo == null)
			{
				mttaskList = smstaskDao.findMtTaskVoTwoTable(conditionMap,orderMap,tableName);
			}
			//如果分页信息为不为空时表示是详情页面的查询
			else
			{
				mttaskList=smstaskDao.findMtTaskVoTwoTable(conditionMap, orderMap,pageInfo,tableName);

			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "网优群发历史发送详情异常。");
			throw e;
		}
		return mttaskList;
	}
	
	
	/**
	 * 跟据taskid查询mtdatareport表中任务的icount和
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public long getSumIcount(LinkedHashMap<String, String> conditionMap)
	throws Exception
	{
		return smstaskDao.findSumIcount(conditionMap);
		
	}
	
	/**
	 * 群发历史与群发任务查询的方法
	 * @param lfMttaskVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfMttaskVo> getLfMttaskVoWithoutDomination(
			LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception
	{
		List<LfMttaskVo> mtTaskVosList;
		try
		{
			if(pageInfo == null)
			{
				mtTaskVosList = smstaskDao.findLfMttaskVoWithoutDomination(
						lfMttaskVo);
			}
			else
			{
		  	   mtTaskVosList = smstaskDao.findLfMttaskVoWithoutDomination(
					lfMttaskVo, pageInfo);
			}
			//拼装VO
			mtTaskVosList=completeLfMttaskVo(mtTaskVosList);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "网优群发历史不带权限查询异常。");
			throw e;
		}
		return mtTaskVosList;
	}
	
	
	public List<LfSysuser> getAllSysusersOfSmsTaskRecordByDep(Long userId,Long depId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			if (null != userId)
				lfSysuserList = new HistoryRecordDAO().findDomUserBySysuserIDOfSmsTaskRecordByDep(userId
						.toString(),depId.toString());

		} catch (Exception e) {
			EmpExecutionContext.error(e, "网优群发历史获取部门操作员异常。");
			throw e;
		}
		return lfSysuserList;
	}
	
	
	/**
	 *  异步加载机构的主方法
	 * @param depId
	 * @param userid
	 * @return
	 */
	private String getDepartmentJosnData(Long depId, Long userid)
	{

		BaseBiz baseBiz=new BaseBiz();
		StringBuffer tree = null;
		try
		{
			// 当前登录操作员
			LfSysuser curUser = baseBiz.getLfSysuserByUserId(userid);
			// 判断是否个人权限
			if (curUser.getPermissionType() == 1)
			{
				tree = new StringBuffer("[]");
			} else
			{
				// 机构biz
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps;

				lfDeps = null;

				if (curUser.getCorpCode().equals("100000"))
				{
					if (depId == null)
					{
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class,
								conditionMap, orderbyMap);
					} else
					{
						/*备份 pengj
			            lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			         	*/
			        	//新增 pengj
			        	lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId, curUser.getCorpCode());
					}

				} else
				{
					if (depId == null)
					{
						lfDeps = new ArrayList<LfDep>();
						/*备份 pengj
						LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						*/
						//新增 pengj
						LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userid, curUser.getCorpCode()).get(0);
							
						lfDeps.add(lfDep);
					} else
					{
						/*备份 pengj
			            lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			         	*/
			        	//新增 pengj
			        	lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId, curUser.getCorpCode());
					}
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++)
				{
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append(
							"'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if (i != lfDeps.size() - 1)
					{
						tree.append(",");
					}
				}
				tree.append("]");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"异步加载机构树数据biz层查询异常！");
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}
	
	
	
	private List<LfMttaskVo> completeLfMttaskVo(List<LfMttaskVo> mtTaskVosList) throws Exception{
		//mtTaskVosList大于0，则进行Vo拼装。
		if(mtTaskVosList!=null&& mtTaskVosList.size()>0){
			//查询操作员
			String userids="";
			for (int i = 0; i < mtTaskVosList.size(); i++) {
				userids+=mtTaskVosList.get(i).getUserId()+",";
			}
			userids = userids.substring(0, userids.length()-1);
			LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
			conditionMap.put("userId&in", userids);
			List<LfSysuser>  lfSysusers=empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);
			Map<String,LfSysuser>  lfSysuserMap=new LinkedHashMap<String, LfSysuser>();
			for (int i = 0; i < lfSysusers.size(); i++) {
				lfSysuserMap.put(lfSysusers.get(i).getUserId().toString(), lfSysusers.get(i));
			}
			
			//查询操作员机构
			String depids="";
			for (int i = 0; i < lfSysusers.size(); i++) {
				depids+=lfSysusers.get(i).getDepId()+",";
			}
			if(lfSysusers.size()>0){
				depids = depids.substring(0, depids.length()-1);
			}
			Map<String,LfDep>  lfDepMap=new LinkedHashMap<String, LfDep>();
			if(depids!=null&&!"".equals(depids)){
				conditionMap.clear();
				conditionMap.put("depId&in", depids);
				List<LfDep>  lfDeps=empDao.findListBySymbolsCondition(LfDep.class, conditionMap, null);
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDepMap.put(lfDeps.get(i).getDepId().toString(), lfDeps.get(i));
				}
			}
			//拼装VO
			LfSysuser lfSysuser=null;
			LfDep lfDep=null;
			for (int i = 0; i < mtTaskVosList.size(); i++) {
				lfSysuser=lfSysuserMap.get(String.valueOf(mtTaskVosList.get(i).getUserId()));
				if(lfSysuser!=null){
					mtTaskVosList.get(i).setName(lfSysuser.getName());
					mtTaskVosList.get(i).setUserName(lfSysuser.getUserName());
					mtTaskVosList.get(i).setUserState(lfSysuser.getUserState());
					lfDep=lfDepMap.get(String.valueOf(lfSysuser.getDepId()));
					if(lfDep!=null){
						mtTaskVosList.get(i).setDepName(lfDep.getDepName());
						mtTaskVosList.get(i).setDepId(lfDep.getDepId());
					}else{
						mtTaskVosList.get(i).setDepName("-");
						mtTaskVosList.get(i).setDepId(0l);
					}
				}else{
					mtTaskVosList.get(i).setName("-");
					mtTaskVosList.get(i).setUserName("-");
					//找不到操作员
					mtTaskVosList.get(i).setUserState(3);
					mtTaskVosList.get(i).setDepName("-");
					mtTaskVosList.get(i).setDepId(0l);
				}
				lfSysuser=null;
				lfDep=null;
			}
		}
		return mtTaskVosList;
	}

	/**
	 * 获取网优通道map集合，key为通道号，value为通道名称
	 * @return
	 */
	public Map<String,String> getWySpgateName(){
		try
		{
			Map<String,String> spgatesMap = new HashMap<String,String>();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("spgate&like2", "200");
			List<XtGateQueue> spgatesList = empDao.findListBySymbolsCondition(XtGateQueue.class, conditionMap, null);
			if(spgatesList == null || spgatesList.size() == 0){
				return spgatesMap;
			}
			for(int i=0;i<spgatesList.size();i++){
				spgatesMap.put(spgatesList.get(i).getSpgate(), spgatesList.get(i).getGateName());
			}
			return spgatesMap;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取网优通道名称异常。");
			return new HashMap<String,String>(); 
		}
	}
	
	/**
	 * 获取机构树json
	 * @param depId 选择的机构id，为null则查全部
	 * @param curUser 当前登录操作员对象
	 * @return 返回机构树json
	 */
	public String getDepJosn(String depId, LfSysuser curUser)
	{
		if(curUser == null)
		{
			EmpExecutionContext.error("网优，获取机构树json，传入的当前登录操作员对象为空。depId=" + depId);
			return null;
		}
		// 权限类型。 1：个人权限 2：机构权限
		if(curUser.getPermissionType() == 1)
		{
			// 个人权限则不需要加载机构树
			return "";
		}
		try
		{
			List<LfDep> depsList;

			//没选择机构，则加载当前操作员管辖的顶级机构
			if(depId == null || depId.trim().length() < 1)
			{
				depsList = smstaskDao.findTopDepByUserId(curUser.getUserId().toString(), curUser.getCorpCode());
			}
			//根据机构id加载其下级机构
			else
			{
				depsList = getDepsByDepSuperId(depId, curUser.getCorpCode());
			}
			if(depsList == null || depsList.size() < 1)
			{
				EmpExecutionContext.error("网优，获取机构树json，获取机构集合为空。depId=" + depId + ",corpCode=" + curUser.getCorpCode());
				return null;
			}
			
			//生成json字符串
			String depJson = createDepJson(depsList);
			return depJson;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网优，获取机构树json，异常。depId="+depId + ",corpCode=" + curUser.getCorpCode());
			return null;
		}
	}
	
	/**
	 * 获取机构操作员树json
	 * @param depId 机构id
	 * @param curUser 当前登录操作员对象
	 * @return 返回机构操作员树json
	 */
	public String getDepUserJosn(String langName,String depId, LfSysuser curUser)
	{
		if(curUser == null)
		{
			EmpExecutionContext.error("网优，获取机构操作员树json，传入的当前登录操作员对象为空。depId=" + depId);
			return null;
		}
		// 权限类型。 1：个人权限 2：机构权限
		if(curUser.getPermissionType() == 1)
		{
			// 个人权限则不需要加载机构树
			return "";
		}
		
		try
		{
			List<LfDep> depsList;
			List<LfSysuser> sysUsersList;
			//没选择机构，则加载当前操作员管辖的顶级机构
			if(depId == null || depId.trim().length() < 1)
			{
				depsList = smstaskDao.findTopDepByUserId(curUser.getUserId().toString(), curUser.getCorpCode());
				sysUsersList = null;
			}
			//根据机构id加载其下级机构
			else
			{
				//获取机构下的直属机构
				depsList = getDepsByDepSuperId(depId, curUser.getCorpCode());
				//获取机构下的直属操作员
				sysUsersList = getUserInDepId(depId, curUser.getCorpCode());
			}
			//都为空则直接返回
			if((depsList == null || depsList.size() < 1) && (sysUsersList == null || sysUsersList.size() < 1))
			{
				EmpExecutionContext.error("网优，获取机构操作员树json，获取机构集合和操作员集合都为空。depId=" + depId + ",corpCode=" + curUser.getCorpCode());
				return null;
			}
			
			//生成json字符串
			String depUserJson = createDepUserJosn(langName,depsList, sysUsersList);
			return depUserJson;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网优，获取机构操作员树json，异常。depId=" + depId + ",corpCode=" + curUser.getCorpCode());
			return null;
		}
	}
	
	/**
	 * 生成机构操作员树json字符串
	 * @param depsList 机构对象集合
	 * @param sysUsersList 操作员对象集合
	 * @return 返回机构操作员树json字符串
	 */
	private String createDepUserJosn(String langName,List<LfDep> depsList, List<LfSysuser> sysUsersList)
	{
		try
		{
			LfDep lfDep = null;
			StringBuffer tree = new StringBuffer("[");
			//有机构
			if(depsList != null && depsList.size() > 0)
			{
				for (int i = 0; i < depsList.size(); i++)
				{
					lfDep = depsList.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",isParent:").append(true);
					tree.append(",nocheck:").append(true);
					tree.append("}");
					
					if(i < depsList.size() - 1)
					{
						tree.append(",");
					}
				}
			}
			//操作员没记录，不需要构造json
			if(sysUsersList == null || sysUsersList.size() < 1)
			{
				tree.append("]");
				return tree.toString();
			}
			
			LfSysuser lfSysuser;
		
			//前面有机构，这里要加一个,
			if(depsList != null && depsList.size() > 0)
			{
				tree.append(",");
			}
			for (int i = 0; i < sysUsersList.size(); i++)
			{
				lfSysuser = sysUsersList.get(i);
				tree.append("{");
				tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
				tree.append(",name:'").append(lfSysuser.getName()).append("'");
				if(lfSysuser.getUserState()==2)
				{
					//tree.append(",name:'").append(lfSysuser.getName()).append("(已注销)'");
					if(StaticValue.ZH_HK.equals(langName)){
						tree.append(",name:'").append(lfSysuser.getName()).append("(Canceled)'");
					}else if(StaticValue.ZH_TW.equals(langName)){
						tree.append(",name:'").append(lfSysuser.getName()).append("(已註銷)'");
					}else{
						tree.append(",name:'").append(lfSysuser.getName()).append("(已注销)'");
					}
				}
				else
				{
					tree.append(",name:'").append(lfSysuser.getName()).append("'");
				}
				tree.append(",pId:").append(lfSysuser.getDepId());
				tree.append(",depId:'").append(lfSysuser.getDepId()+"'");
				tree.append(",isParent:").append(false);
				tree.append("}");
				
				if(i < sysUsersList.size() - 1)
				{
					tree.append(",");
				}
			}
			
			tree.append("]");
			return tree.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网优，生成机构操作员树json字符串，异常。");
			return null;
		}
	}
	
	/**
	 * 获取机构下的操作员对象集合
	 * @param corpCode 企业编码
	 * @param depId 机构id
	 * @return 返回机构下的操作员对象集合
	 */
	private List<LfSysuser> getUserInDepId(String depId, String corpCode)
	{
		if(corpCode == null || corpCode.trim().length() < 1 || depId == null || depId.trim().length() < 1)
		{
			EmpExecutionContext.error("网优，获取机构下的操作员，传入参数为空。depId="+depId+",corpCode="+corpCode);
			return null;
		}
		try 
		{
			//查询条件集合
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			conditionMap.put("depId", depId);
			conditionMap.put("corpCode", corpCode);
				
			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			orderbyMap.put("name","asc");
				
			List<LfSysuser> lfSysuserList = empDao.findListByCondition(LfSysuser.class, conditionMap, orderbyMap);
			return lfSysuserList;
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "网优，获取机构下的操作员，异常。");
			return null;
		}
		
	}
	
	/**
	 * 通过父机构id获取父机构下的所有子机构
	 * @param superiorId 父机构id
	 * @return 父机构下的所有子机构对象集合
	 */
	private List<LfDep> getDepsByDepSuperId(String superiorId, String corpCode)
	{
		if (superiorId == null || superiorId.trim().length() < 1 || corpCode == null || corpCode.trim().length() < 1)
		{
			EmpExecutionContext.error("网优，通过父机构id获取父机构下的所有子机构，父机构id或企业编码为空。superiorId="+superiorId+",corpCode="+corpCode);
			return null;
		}
		
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//父机构id
			conditionMap.put("superiorId", superiorId);
			//机构状态。1正常；2删除
			conditionMap.put("depState", "1");
			//机构所属企业
			conditionMap.put("corpCode", corpCode);
			
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			orderByMap.put("depName", StaticValue.ASC);
			
			List<LfDep> tempList = empDao.findListByCondition(LfDep.class, conditionMap, orderByMap);
			//返回结果
			return tempList;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网优，通过父机构id获取父机构下的所有子机构，异常。superiorId="+superiorId+",corpCode="+corpCode);
			return null;
		}
	}
	
	/**
	 * 生成机构树json字符串
	 * @param depsList 机构对象集合
	 * @return 返回机构树json字符串
	 */
	private String createDepJson(List<LfDep> depsList)
	{
		try
		{
			LfDep lfDep;
			StringBuffer tree = new StringBuffer("[");
			for (int i = 0; i < depsList.size(); i++)
			{
				lfDep = depsList.get(i);
				tree.append("{");
				tree.append("id:").append(lfDep.getDepId());
				tree.append(",name:'").append(lfDep.getDepName()).append("'");
				tree.append(",pId:").append(lfDep.getSuperiorId());
				tree.append(",depId:'").append(lfDep.getDepId() + "'");
				tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
				tree.append(",isParent:").append(true);
				tree.append("}");
				
				if(i < depsList.size() - 1)
				{
					tree.append(",");
				}
			}
			tree.append("]");
			return tree.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网优，生成机构树json字符串，异常。");
			return null;
		}
	}
	
	
	
	

}
