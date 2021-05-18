package com.montnets.emp.shorturl.task.biz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.common.vo.LfMttaskVo;
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.shorturl.task.dao.SmstaskDAO;
import com.montnets.emp.util.PageInfo;


public class SmstaskBiz extends SuperBiz {
	
	private SmstaskDAO smstaskDao;
	public SmstaskBiz()
	{
		smstaskDao=new SmstaskDAO();
	}
	
	/**
	 * 机构树
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public String getDepartmentJosnData(Long userid) throws Exception{
		StringBuffer tree = new StringBuffer();
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
					lfDeps = depBiz.getAllDeps(userid);
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
		return tree.toString();
	}
	
	/**
	 * 机构树(重载方法)
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public String getDepartmentJosnData(Long userid,LfSysuser loginSysuser) throws Exception{
		StringBuffer tree = new StringBuffer();
		//根据用户id获取用户信息
		LfSysuser currUser = loginSysuser;
		//个人权限
		if(currUser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			//机构权限
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
					//orderbyMap.put("depId", StaticValue.ASC);
					//查询所有机构
					lfDeps = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
				}else
				{
					//获取管辖范围内的所有机构
					lfDeps = depBiz.getAllDepByUserIdAndCorpCode(userid,currUser.getCorpCode());
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
			            lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			          }

			        }
			        else if (depId == null) {
			          lfDeps = new ArrayList();
			          LfDep lfDep = (LfDep)depBiz.getAllDeps(userId).get(0);
			          lfDeps.add(lfDep);
			        }
			        else {
			          lfDeps = new DepBiz().getDepsByDepSuperId(depId);
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
	 * 获取树的方法(重载方法)
	 * @param depId 机构ID
	 * @param userId 操作员ID
	 * @param loginSysuser 当前登录操作员
	 * @return 返回生成树的字符串
	 */
	public String getDepartmentJosnData2(Long depId,Long userId,LfSysuser loginSysuser){
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			//当前登录操作员
			sysuser = loginSysuser;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发历史或群发任务查询操作员信息异常！");
			tree = new StringBuffer("[]");
		}
		//判断当前登录操作员的权限，个人权限不能显示机构树
		if(sysuser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			
			try {
				lfDeps = null;
				 //10000企业查询
				 if ((sysuser != null) && (sysuser.getCorpCode().equals("100000")))
			        {
			          if (depId == null)
			          {
			        	 //设置查询条件
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
			            lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			          }

			        }
			        else if (depId == null) {
			          lfDeps = new ArrayList();
			          //根据操作员ID和企业编码，查询操作员所管辖的机构
			          LfDep lfDep = (LfDep)depBiz.getAllDepByUserIdAndCorpCode(userId,sysuser.getCorpCode()).get(0);
			          lfDeps.add(lfDep);
			        }
			        else {
			          //根据机构ID、企业编码查询出所有子机构
			          lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,sysuser.getCorpCode());
			        }
				
				//组装字符串，返回前台
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
				//异常信息记录日志
				EmpExecutionContext.error(e,"群发历史或群发任务操作员树加载biz层异常！");
				tree = new StringBuffer("[]");
			}
		}
		//返回值
		return tree.toString();
	}
	
	/**
	 * 获取审批信息
	 * @param mtID
	 * @param rLevel
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordVo> getFlowRecordVos(String mtID, String rLevel,String reviewType)
			throws Exception
	{
		List<LfFlowRecordVo> frVosList;
		try
		{
			frVosList = smstaskDao
					.findLfFlowRecordVo(mtID, rLevel,reviewType);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取审批信息失败。");
			throw e;
		}
		return frVosList;
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
			EmpExecutionContext.error(e, "完美通短发送详情查询异常。");
			throw e;
		}
		return mttaskList;
	}
	
	/**
	 * 群发历史任务查询发送详情
	 * @description    
	 * @param conditionMap
	 * @param orderMap
	 * @param pageInfo
	 * @param tableName
	 * @param isBackDb
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-22 下午02:38:49
	 */
	public List <SendedMttaskVo> getMtTask(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String,String> orderMap,PageInfo pageInfo,String tableName, boolean isBackDb, PageInfo realDbpageInfo)
	throws Exception
	{
		List<SendedMttaskVo> mttaskList = null;
		try {
			//如果分页信息为空则表名是导出数据的查询
			if(pageInfo == null)
			{
				mttaskList =  smstaskDao.findMtTaskVo(conditionMap,orderMap,tableName, isBackDb);
			}
			//如果分页信息为不为空时表示是详情页面的查询
			else
			{
				mttaskList=smstaskDao.findMtTaskVo(conditionMap, orderMap,pageInfo,tableName, isBackDb, realDbpageInfo);

			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "群发任务送详情查询异常。");
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
			EmpExecutionContext.error(e, "群发历史与群发任务查询异常。");
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
			EmpExecutionContext.error(e, "完美通知发送详情（查两个表示调用）查询异常。");
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
			EmpExecutionContext.error(e, "群发历史与群发任务查询异常。");
			throw e;
		}
		return mtTaskVosList;
	}
	
	
	public List<LfSysuser> getAllSysusersOfSmsTaskRecordByDep(Long userId,Long depId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			if (null != userId)
				lfSysuserList = new SmstaskDAO().findDomUserBySysuserIDOfSmsTaskRecordByDep(userId
						.toString(),depId.toString());

		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询用户编号及机构编号对应关系异常。");
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
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}

				} else
				{
					if (depId == null)
					{
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						lfDeps.add(lfDep);
					} else
					{
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
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
			if(userids.length() > 0 && userids.endsWith(","))
			{
				userids = userids.substring(0, userids.length()-1);
			}
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
			if(depids.length() > 0 && depids.endsWith(",")){
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

	
	public Map<Long, Long> getTaskRemains(String taskids){
		return smstaskDao.getTaskRemains(taskids);
	}	
	
	
	/**
	 * 获取群发历史回复信息
	 * @param conditionMap 条件
	 * @param pageInfo  分页
	 * @return
	 */
	public List<DynaBean> getReplyDetailList(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try {
		
			//查询回复记录
			List<DynaBean> replyDetailList = smstaskDao.getReplyDetailList(conditionMap, pageInfo);
			//设置回复姓名
			if(replyDetailList != null && replyDetailList.size() > 0)
			{
				//回复号码
				StringBuffer phoneSb = new StringBuffer();
				//回复号码
				String phones = "";
				//KEY:回复号码;value:姓名
				Map<String, String> phoneNameMap = new LinkedHashMap<String, String>();
				//获取所有回复号码
				for(DynaBean replyDetail:replyDetailList)
				{
					if(replyDetail.get("phone") != null && !"".equals(replyDetail.get("phone").toString().trim()))
					{
						phoneSb.append(replyDetail.get("phone").toString().trim()).append(",");
					}
				}
				//存在回复号码
				if(phoneSb.length() > 0 && phoneSb.indexOf(",")>-1)
				{
					phones = phoneSb.substring(0, phoneSb.length()-1);
					//查询条件存在姓名,则直接使用
					if(conditionMap.get("replyName") == null || "".equals(conditionMap.get("replyName")))
					{
						//获取回复详情用户姓名,匹配优先级:员工>客户通讯录>操作员
						smstaskDao.getReplyDetailName(phones, conditionMap.get("corpCode"), phoneNameMap);
						//存在回复号码姓名
						if(phoneNameMap.size() > 0)
						{
							String phone = "";
							for(int i=0; i<replyDetailList.size(); i++ )
							{
								if(replyDetailList.get(i) != null)
								{
									phone = replyDetailList.get(i).get("phone")==null?"":replyDetailList.get(i).get("phone").toString();
									if(phone != null && !"".equals(phone))
									{
										replyDetailList.get(i).set("name", phoneNameMap.get(phone));
									}
									else
									{
										replyDetailList.get(i).set("name", "-");
									}
								}
							}
						}
					}
				}
			}
			return replyDetailList;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取群发历史回复信息失败！");
			return null;
		}
	}
	
	

}
