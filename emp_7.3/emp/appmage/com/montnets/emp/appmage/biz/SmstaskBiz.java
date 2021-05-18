package com.montnets.emp.appmage.biz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.appmage.dao.SmstaskDAO;
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
import com.montnets.emp.util.PageInfo;


public class SmstaskBiz extends SuperBiz{
	
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
			tree.append("[]");
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
					lfDeps = depBiz.getDepsByDepSuperId(userid);
				}
				LfDep lfDep = null;
				//机构树的json数据拼写
				tree.append("[");
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
	 * 机构树
	 * @param userid
	 * @param corpCode 公司编码
	 * @return
	 * @throws Exception
	 */
	public String getDepartmentByCorpCode(Long userid,String corpCode) throws Exception{
		StringBuffer tree = new StringBuffer();
		//根据用户id获取用户信息
		LfSysuser currUser = empDao.findObjectByID(LfSysuser.class, userid);
		if(currUser.getPermissionType()==1)
		{
			tree.append("[]");
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
					lfDeps = depBiz.getDepsByDepSuperIdAndCorpCode(userid,corpCode);
				}
				LfDep lfDep = null;
				//机构树的json数据拼写
				tree.append("[");
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
			List<LfDep> lfDeps = null;
			
			try {
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
			          lfDeps = new ArrayList<LfDep>();
			          LfDep lfDep = (LfDep)depBiz.getDepsByDepSuperId(userId).get(0);
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
	 * 树
	 * @return
	 */
	public String getDepByCorpCodeAndUser(Long depId,Long userId,String corpCode){
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
			List<LfDep> lfDeps = null;
			
			try {
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
			            lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,corpCode);
			          }

			        }
			        else if (depId == null) {
			          lfDeps = new ArrayList<LfDep>();
			          LfDep lfDep = (LfDep)depBiz.getAllDepByUserIdAndCorpCode(userId,corpCode).get(0);
			          lfDeps.add(lfDep);
			        }
			        else {
			          lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,corpCode);
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
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,curUser.getCorpCode());
					}

				} else
				{
					if (depId == null)
					{
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userid,curUser.getCorpCode()).get(0);
						lfDeps.add(lfDep);
					} else
					{
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,curUser.getCorpCode());
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
			//userids.substring(0, userids.length()-1);  //findbugs
			if(!("".equals(userids))){
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
			/*findbugs
			if(lfSysusers.size()>0){
				depids.substring(0, depids.length()-1);
			}
			*/
			if(lfSysusers.size()>0 && depids.length()>0){
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

	
	
	
	
	
	
	

}
