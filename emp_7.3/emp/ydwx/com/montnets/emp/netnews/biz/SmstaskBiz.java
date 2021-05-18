/**
 * 
 */
package com.montnets.emp.netnews.biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.netnews.dao.SmstaskDAO;
import com.montnets.emp.netnews.vo.LfMttaskVo;
import com.montnets.emp.util.PageInfo;


	public class SmstaskBiz extends SuperBiz{
	
	private SmstaskDAO smstaskDao;
	public SmstaskBiz()
	{
		smstaskDao=new SmstaskDAO();
	}
	
	/**
	 * 机构树
	 * @param userid 用户id
	 * @param corpcode 公司编码
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
				EmpExecutionContext.error(e,"获取机构树");
			}
		}
		if(tree==null){
			return "[]";
		}
		return tree.toString();
	}
	
	/**
	 * 机构树
	 * @param userid 用户id
	 * @param corpcode 公司编码
	 * @return
	 * @throws Exception
	 */
	public String getDepartmentJosnData(Long userid,String corpcode) throws Exception{
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
					lfDeps = depBiz.getAllDepByUserIdAndCorpCode(userid,corpcode);
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
				EmpExecutionContext.error(e,"获取机构树");
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
			EmpExecutionContext.error(e,"查询用户表");
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
			            lfDeps = new DepBiz().getAllDepByUserIdAndCorpCode(depId,sysuser.getCorpCode());
			          }

			        }
			        else if (depId == null) {
			          lfDeps = new ArrayList();
			          LfDep lfDep = (LfDep)depBiz.getAllDepByUserIdAndCorpCode(userId,sysuser.getCorpCode()).get(0);
			          lfDeps.add(lfDep);
			        }
			        else {
			          lfDeps = new DepBiz().getAllDepByUserIdAndCorpCode(depId,sysuser.getCorpCode());
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
				EmpExecutionContext.error(e,"查询部门相关信息");
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
			EmpExecutionContext.error(e,"获取审批信息失败");
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
			EmpExecutionContext.error(e,"获得发送详情失败");
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
			//异常处理
			EmpExecutionContext.error(e,"群发历史与群发任务查询异常");
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
			EmpExecutionContext.error(e,"发送详情（查两个表示时调用）异常");
			throw e;
		}
		return mttaskList;
	}
	
	
	
	
	/**
	 * 获取表名使用
	 * @param conditionMap
	 * @param orderMap
	 * @param pageInfo
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public String getTableName(String tableqz,String year,int month)	throws Exception
	{
		String tableName="";
		try {
			//如果分页信息为空则表名是导出数据的查询
			if(month<10)
			{
				//表名
				tableName=tableqz+year+"0"+month;
				String tableName2=tableqz+"0"+month;
				String sql="";
				String sql1="";
				switch (StaticValue.DBTYPE) {
				case 1:
					// oracle
					sql="select count(*) ICOUNT from all_tables where table_name=upper('"+tableName+"') and OWNER=upper('"+SystemGlobals.getValue("montnets.emp.user")+"')";
					sql1="select count(*) ICOUNT from all_tables where table_name=upper('"+tableName2+"') and OWNER=upper('"+SystemGlobals.getValue("montnets.emp.user")+"')";
					break;
				case 2:
					// sqlserver2005
					sql="select count(*) ICOUNT from sysobjects where id = object_id('"+tableName+"') and type = 'u'";
					sql1="select count(*) ICOUNT from sysobjects where id = object_id('"+tableName2+"') and type = 'u'";
					break;
				case 3:
					// MYSQL
					sql="select count(*) ICOUNT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='"
						+SystemGlobals.getValue("montnets.emp.databaseName")+"' and TABLE_NAME=upper('"+tableName+"')";
					sql1="select count(*) ICOUNT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='"
						+SystemGlobals.getValue("montnets.emp.databaseName")+"' and TABLE_NAME=upper('"+tableName2+"')";
					break;
				case 4:
					// DB2
					sql="select count(*) ICOUNT from syscat.tables where tabname=upper('"+tableName+"')";
					sql1="select count(*) ICOUNT from syscat.tables where tabname=upper('"+tableName2+"')";
					break;
				default:
					break;
				}
				List<DynaBean> bbs=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
				List<DynaBean> bbs1=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql1);
				int i=0;
				int j=0;
				for (DynaBean db:bbs) {
					if(db.get("icount")!=null){
						String count=db.get("icount").toString();
						if(!"0".equals(count)){
							i=1;
						}
					}
				}
				
				for (DynaBean db:bbs1) {
					if(db.get("icount")!=null){
						String count=db.get("icount").toString();
						if(!"0".equals(count)){
							j=1;
						}
					}
				}
				
				if(i==0&&j==0){
					return "";
				}else if(i==1){
					return tableName;
				}else if(i==0&&j==1){
					return tableName2;
				}
				
				
			}
			else
			{
				//表名
				tableName=tableqz+year+month;
				String tableName2=tableqz+month;
				String sql="";
				String sql1="";
				switch (StaticValue.DBTYPE) {
				case 1:
					// oracle
					sql="select count(*) ICOUNT from all_tables where table_name=upper('"+tableName+"') and OWNER=upper('"+SystemGlobals.getValue("montnets.emp.user")+"')";
					sql1="select count(*) ICOUNT from all_tables where table_name=upper('"+tableName2+"') and OWNER=upper('"+SystemGlobals.getValue("montnets.emp.user")+"')";
					break;
				case 2:
					// sqlserver2005
					sql="select count(*) ICOUNT from sysobjects where id = object_id('"+tableName+"') and type = 'u'";
					sql1="select count(*) ICOUNT from sysobjects where id = object_id('"+tableName2+"') and type = 'u'";
					break;
				case 3:
					// MYSQL
					sql="select count(*) ICOUNT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='"
						+SystemGlobals.getValue("montnets.emp.databaseName")+"' and TABLE_NAME=upper('"+tableName+"')";
					sql1="select count(*) ICOUNT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='"
						+SystemGlobals.getValue("montnets.emp.databaseName")+"' and TABLE_NAME=upper('"+tableName2+"')";
					break;
				case 4:
					// DB2
					sql="select count(*) ICOUNT from syscat.tables where tabname=upper('"+tableName+"')";
					sql1="select count(*) ICOUNT from syscat.tables where tabname=upper('"+tableName2+"')";
					break;
				default:
					break;
				}
				List<DynaBean> bbs=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
				List<DynaBean> bbs1=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql1);
				int i=0;
				int j=0;
				for (DynaBean db:bbs) {
					if(db.get("icount")!=null){
						String count=db.get("icount").toString();
						if(!"0".equals(count)){
							i=1;
						}
					}
				}
				
				for (DynaBean db:bbs1) {
					if(db.get("icount")!=null){
						String count=db.get("icount").toString();
						if(!"0".equals(count)){
							j=1;
						}
					}
				}
				
				if(i==0&&j==0){
					return "";
				}else if(i==1){
					return tableName;
				}else if(i==0&&j==1){
					return tableName2;
				}
				
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取表名使用异常");
			throw e;
		}
		return tableName;
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
			EmpExecutionContext.error(e,"获取短信发送任务或非数据权限范围查找短信任务异常");
			throw e;
		}
		return mtTaskVosList;
	}
	
	/****
	 * 通过用户ID，部门ID查询用户相关信息
	 * @param userId 用户ID
	 * @param depId 部门ID
	 * @return 用户集合
	 * @throws Exception
	 */
	public List<LfSysuser> getAllSysusersOfSmsTaskRecordByDep(Long userId,Long depId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			if (null != userId)
				lfSysuserList = new SmstaskDAO().findDomUserBySysuserIDOfSmsTaskRecordByDep(userId
						.toString(),depId.toString());

		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过用户ID,部门ID查询用户相关信息异常");
			throw e;
		}
		return lfSysuserList;
	}
	
	
	// 异步加载机构的主方法
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
			EmpExecutionContext.error(e,"异步加载机构异常");
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}
	
	//进行Vo拼装
	private List<LfMttaskVo> completeLfMttaskVo(List<LfMttaskVo> mtTaskVosList) throws Exception{
		//mtTaskVosList大于0，则进行Vo拼装。
		if(mtTaskVosList!=null&& mtTaskVosList.size()>0){
			//查询操作员
			String userids="";
			for (int i = 0; i < mtTaskVosList.size(); i++) {
				userids+=mtTaskVosList.get(i).getUserId()+",";
			}
			if(!"".equals(userids)){
				userids=userids.substring(0, userids.length()-1);
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
			if(lfSysusers.size()>0){
				depids=depids.substring(0, depids.length()-1);
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
						mtTaskVosList.get(i).setDepName("");
						mtTaskVosList.get(i).setDepId(0l);
					}
				}else{
					mtTaskVosList.get(i).setName("");
					mtTaskVosList.get(i).setUserName("");
					mtTaskVosList.get(i).setUserState(2);
					mtTaskVosList.get(i).setDepName("");
					mtTaskVosList.get(i).setDepId(0l);
				}
				lfSysuser=null;
				lfDep=null;
			}
		}
		return mtTaskVosList;
	}

	/**
	 * 获取发送详情
	 * @param Lfmttask
	 * @param conditionMap 查询条件
	 * @param orderMap
	 * @param pageInfo
	 * @return
	 */
	public List <SendedMttaskVo> getMtTaskList(LfMttask Lfmttask, LinkedHashMap<String, String> conditionMap,PageInfo pageInfo,PageInfo pageInfoTemp)
	{
		try
		{
			//发送详情
    		List<SendedMttaskVo> MttaskvoList;
			//1-查询实时记录；2-查询历史记录；3-查询实时和历史记录
			int searchType = getSerchType(Lfmttask);
			if(searchType == 1)
			{
				//查询实时表
    			MttaskvoList = smstaskDao.findMtTaskReal(conditionMap, pageInfo);
			}
			else if(searchType == 2)
			{
				//查询历史表
				String tableName = getSerchTable(Lfmttask.getTimerTime().getTime());
				MttaskvoList = smstaskDao.findMtTaskHis(conditionMap, pageInfo, tableName,Lfmttask.getTimerTime().getTime(),pageInfoTemp);
			}
			else if(searchType == 3)
			{
				//查询历史和实时表
				String tableName = getSerchTable(Lfmttask.getTimerTime().getTime());
				MttaskvoList = smstaskDao.findMtTaskHisAndReal(conditionMap, pageInfo, tableName);
			}
			else
			{
				MttaskvoList = null;
			}
			//没数据则重置分页对象
			if(MttaskvoList == null || MttaskvoList.size() == 0)
			{
				pageInfo.setTotalPage(1);
				pageInfo.setTotalRec(0);
			}
			
			return MttaskvoList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计获取详情，biz查询，异常。");
			return null;
		}
	}
	
	
	/**
	 * 获取发送详情分页信息
	 * @param Lfmttask
	 * @param conditionMap 查询条件
	 * @param orderMap
	 * @param pageInfo
	 * @return
	 */
	public boolean getMtTaskListPageInfo(LfMttask Lfmttask, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, PageInfo realDbpageInfo)
	{
		try
		{
			//1-查询实时记录；2-查询历史记录；3-查询实时和历史记录
			int searchType = getSerchType(Lfmttask);
			if(searchType == 1)
			{
				//查询实时表
    			return smstaskDao.findMtTaskRealPageInfo(conditionMap, pageInfo);
			}
			else if(searchType == 2)
			{
				//查询历史表
				String tableName = getSerchTable(Lfmttask.getTimerTime().getTime());
				return smstaskDao.findMtTaskHisPageInfo(conditionMap, pageInfo, tableName, realDbpageInfo);
			}
			else if(searchType == 3)
			{
				//查询历史和实时表
				String tableName = getSerchTable(Lfmttask.getTimerTime().getTime());
				return smstaskDao.findMtTaskHisAndRealPageInfo(conditionMap, pageInfo, tableName);
			}
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计获取详情，biz查询，异常。");
			return false;
		}
	}
	
	/**
	 * 获取发送详情
	 * @param Lfmttask
	 * @param conditionMap 查询条件
	 * @param orderMap
	 * @param pageInfo
	 * @return
	 */
	public List <SendedMttaskVo> getMtTaskList(LfMttask Lfmttask, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			//发送详情
    		List<SendedMttaskVo> MttaskvoList;
			//1-查询实时记录；2-查询历史记录；3-查询实时和历史记录
			int searchType = getSerchType(Lfmttask);
			if(searchType == 1)
			{
				//查询实时表
    			MttaskvoList = smstaskDao.findMtTaskReal(conditionMap, pageInfo);
			}
			else if(searchType == 2)
			{
				//查询历史表
				String tableName = getSerchTable(Lfmttask.getTimerTime().getTime());
				MttaskvoList = smstaskDao.findMtTaskHis(conditionMap, pageInfo, tableName);
			}
			else if(searchType == 3)
			{
				//查询历史和实时表
				String tableName = getSerchTable(Lfmttask.getTimerTime().getTime());
				MttaskvoList = smstaskDao.findMtTaskHisAndReal(conditionMap, pageInfo, tableName);
			}
			else
			{
				MttaskvoList = null;
			}
			//没数据则重置分页对象
			if(MttaskvoList == null || MttaskvoList.size() == 0)
			{
				pageInfo.setTotalPage(1);
				pageInfo.setTotalRec(0);
			}
			
			return MttaskvoList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计获取详情，biz查询，异常。");
			return null;
		}
	}
	
	/**
	 * 获取发送详情分页信息
	 * @param Lfmttask
	 * @param conditionMap 查询条件
	 * @param orderMap
	 * @param pageInfo
	 * @return
	 */
	public boolean getMtTaskListPageInfo(LfMttask Lfmttask, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			//1-查询实时记录；2-查询历史记录；3-查询实时和历史记录
			int searchType = getSerchType(Lfmttask);
			if(searchType == 1)
			{
				//查询实时表
    			return smstaskDao.findMtTaskRealPageInfo(conditionMap, pageInfo);
			}
			else if(searchType == 2)
			{
				//查询历史表
				String tableName = getSerchTable(Lfmttask.getTimerTime().getTime());
				return smstaskDao.findMtTaskHisPageInfo(conditionMap, pageInfo, tableName);
			}
			else if(searchType == 3)
			{
				//查询历史和实时表
				String tableName = getSerchTable(Lfmttask.getTimerTime().getTime());
				return smstaskDao.findMtTaskHisAndRealPageInfo(conditionMap, pageInfo, tableName);
			}
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计获取详情，biz查询，异常。");
			return false;
		}
	}
	
	/**
	 * 获取查询类型
	 * @param Lfmttask
	 * @param conditionMap
	 * @return 1-查询实时记录；2-查询历史记录；3-查询实时和历史记录
	 */
	private int getSerchType(LfMttask Lfmttask)
	{
		try
		{
			//当前时间
    		Calendar curCalTime=Calendar.getInstance();
    		//发送时间
    		Calendar sendCalTime = Calendar.getInstance();
    		sendCalTime.setTimeInMillis(Lfmttask.getTimerTime().getTime());
    		//计算时间，发送时间加上4天
    		sendCalTime.add(Calendar.DATE, 4);
    		
    		//发送时间加上4天仍然大于当前时间，则还没调度，查实时表
    		if(sendCalTime.after(curCalTime))
    		{
    			//查询实时表
    			return 1;
    		}
    		//发送时间加上4天小于当前时间，表示已调度，查历史表
    		else
    		{
    			//查询对应月份历史表信息
    			
    			SimpleDateFormat dfMM = new SimpleDateFormat("MM");
    			SimpleDateFormat dfyyyy = new SimpleDateFormat("yyyy");
    			Date sendDateTime = new Date(Lfmttask.getTimerTime().getTime());
    			//表名
				String tableName = "MTTASK" + dfyyyy.format(sendDateTime) + dfMM.format(sendDateTime);
    			
				//验证历史表是否存在
				tableName = new CommonBiz().getTableName(tableName);

				LinkedHashMap<String, String> conMapcount=new LinkedHashMap<String, String>();
				//跟据taskid统计mt_datareport表中的icout值
				conMapcount.put("taskid",Lfmttask.getTaskId().toString());
				
				//当前时间
				Calendar curTime=Calendar.getInstance();
				//当前时间减三天
				curTime.add(Calendar.DATE, -3);
				SimpleDateFormat sdfyyyyMMdd = new SimpleDateFormat("yyyyMMdd");
				//使用mtdatareport的iymd字段，以便用来查询三天前的mtdatareport表的此任务的icount字段的和
				String iymd = sdfyyyyMMdd.format(curTime.getTime());
				conMapcount.put("iymd", iymd);
				//mtdatareport表里面对应任务的icount的总和
				long sumCount = getSumIcount(conMapcount);
				//预发送条数
				long count = Lfmttask.getIcount()==null?0l : Long.parseLong(Lfmttask.getIcount());
				//如果三天前的mtdatareport表里的此任务的icount的总和少于lfmttask的预发送条数icount，则查历史表和实时两张表，否则只查对应历史表
				if(sumCount < count)
				{
					//需要查询历史和实时表
					return 3;
				}
				else
				{
					//查询历史表
					return 2;
				}
    		}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计获取详情，获取查询类型，异常。");
			return -9999;
		}
	}
	
	/**
	 * 获取查询数据表名
	 * @param Lfmttask
	 * @param conditionMap
	 * @return 
	 */
	public String getSerchTable(long millTime)
	{
		try
		{
			SimpleDateFormat dfMM = new SimpleDateFormat("MM");
			SimpleDateFormat dfyyyy = new SimpleDateFormat("yyyy");
			Date sendDateTime = new Date(millTime);
			//表名
			String tableName = "MTTASK" + dfyyyy.format(sendDateTime) + dfMM.format(sendDateTime);
			
			//验证历史表是否存在
			tableName = new CommonBiz().getTableName(tableName);

			return tableName;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计获取详情，获取查询数据表名，异常。");
			return null;
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
			EmpExecutionContext.error("移动网讯，获取机构树json，传入的当前登录操作员对象为空。depId=" + depId);
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
				EmpExecutionContext.info("移动网讯，获取机构树json，该机构已无下级机构。depId=" + depId + ",corpCode=" + curUser.getCorpCode());
				return null;
			}
			
			//生成json字符串
			String depJson = createDepJson(depsList);
			return depJson;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "移动网讯，获取机构树json，异常。depId="+depId + ",corpCode=" + curUser.getCorpCode());
			return null;
		}
	}
	
	/**
	 * 获取机构操作员树json
	 * @param depId 机构id
	 * @param curUser 当前登录操作员对象
	 * @return 返回机构操作员树json
	 */
	public String getDepUserJosn(String depId, LfSysuser curUser)
	{
		if(curUser == null)
		{
			EmpExecutionContext.error("移动网讯，获取机构操作员树json，传入的当前登录操作员对象为空。depId=" + depId);
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
				EmpExecutionContext.info("移动网讯，获取机构操作员树json，该机构已无下级机构。depId=" + depId + ",corpCode=" + curUser.getCorpCode());
				return null;
			}
			
			//生成json字符串
			String depUserJson = createDepUserJosn(depsList, sysUsersList);
			return depUserJson;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "移动网讯，获取机构操作员树json，异常。depId=" + depId + ",corpCode=" + curUser.getCorpCode());
			return null;
		}
	}
	
	/**
	 * 生成机构操作员树json字符串
	 * @param depsList 机构对象集合
	 * @param sysUsersList 操作员对象集合
	 * @return 返回机构操作员树json字符串
	 */
	private String createDepUserJosn(List<LfDep> depsList, List<LfSysuser> sysUsersList)
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
					tree.append(",depId:'").append(lfDep.getDepId()).append("'");
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
				if(lfSysuser.getUserState() == 2)
				{
					tree.append(",name:'").append(lfSysuser.getName()).append("(已注销)'");
				}
				else
				{
					tree.append(",name:'").append(lfSysuser.getName()).append("'");
				}
				tree.append(",pId:").append(lfSysuser.getDepId());
				tree.append(",depId:'").append(lfSysuser.getDepId() + "'");
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
			EmpExecutionContext.error(e, "移动网讯，生成机构操作员树json字符串，异常。");
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
			EmpExecutionContext.error("移动网讯，获取机构下的操作员，传入参数为空。depId="+depId+",corpCode="+corpCode);
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
			EmpExecutionContext.error(e, "移动网讯，获取机构下的操作员，异常。");
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
			EmpExecutionContext.error("移动网讯，通过父机构id获取父机构下的所有子机构，父机构id或企业编码为空。superiorId="+superiorId+",corpCode="+corpCode);
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
			EmpExecutionContext.error(e, "移动网讯，通过父机构id获取父机构下的所有子机构，异常。superiorId="+superiorId+",corpCode="+corpCode);
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
			EmpExecutionContext.error(e, "移动网讯，生成机构树json字符串，异常。");
			return null;
		}
	}
	
}

