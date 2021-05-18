package com.montnets.emp.appmage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.common.vo.LfMttaskVo;
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.approveflow.TableLfFlowRecord;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sms.TableMtTask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;



public class SmstaskDAO extends SuperDAO{

	/**
	 * 获取审批信息
	 * @param mtID
	 * @param rLevel
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordVo> findLfFlowRecordVo(String mtID, String rLevel,String reviewType)
			throws Exception
	{
		//字段
		String fieldSql = "select flowrecord.FR_ID,flowrecord.MT_ID,flowrecord.F_ID,flowrecord.REVIEW_TYPE,flowrecord.R_TIME,flowrecord.R_LEVEL," +
							"flowrecord.R_LEVELAMOUNT,flowrecord.R_CONTENT,flowrecord.R_STATE,flowrecord.COMMENTS,prerevisysuser.NAME preReviName,revisysuser.NAME reviName," +
							"sysuser.NAME,sysuser.USER_NAME,dep.DEP_NAME,mttask.TIMER_STATUS,mttask.TIMER_TIME,mttask.TITLE,mttask.TASKNAME,mttask.BMTTYPE,mttask.SP_USER," +
							"mttask.SUBMITTIME,mttask.MOBILE_URL,mttask.MSG_TYPE,mttask.EFF_COUNT,mttask.MSG,mttask.TMPL_PATH,userdata.STAFFNAME,sysuser.USER_STATE";
		//表名
		String tableSql =  new StringBuffer(" from ").append(
				TableLfFlowRecord.TABLE_NAME).append(" flowrecord ").append(
						StaticValue.getWITHNOLOCK()).append(" left join ").append(
						TableLfSysuser.TABLE_NAME).append(" prerevisysuser ").append(
						StaticValue.getWITHNOLOCK()).append(" on flowrecord.").append(
						TableLfFlowRecord.PRE_RV).append("=prerevisysuser.").append(
						TableLfSysuser.USER_ID).append(" inner join ").append(
						TableLfSysuser.TABLE_NAME).append(" revisysuser ").append(
						StaticValue.getWITHNOLOCK()).append(" on flowrecord.").append(
						TableLfFlowRecord.REVIEWER).append("=revisysuser.").append(
						TableLfSysuser.USER_ID).append(" inner join ").append(
						TableLfMttask.TABLE_NAME).append(" mttask ").append(
						StaticValue.getWITHNOLOCK()).append(" on mttask.").append(
						TableLfMttask.MT_ID).append("=flowrecord.").append(
						TableLfFlowRecord.MT_ID).append(" inner join ").append(
						TableLfSysuser.TABLE_NAME).append(" sysuser ").append(
						StaticValue.getWITHNOLOCK()).append(" on sysuser.").append(
						TableLfSysuser.USER_ID).append("=mttask.").append(
						TableLfMttask.USER_ID).append(" inner join ").append(
						TableLfDep.TABLE_NAME).append(" dep ").append(
						StaticValue.getWITHNOLOCK()).append(" on dep.").append(
						TableLfDep.DEP_ID).append("=sysuser.").append(
						TableLfSysuser.DEP_ID).append(" left join ").append(
						TableUserdata.TABLE_NAME).append(" userdata ").append(
						StaticValue.getWITHNOLOCK()).append(" on mttask.").append(
						TableLfMttask.SP_USER).append("=userdata.").append(
						TableUserdata.USER_ID).toString();
		
		tableSql = tableSql + " and userdata."+TableUserdata.ACCOUNTTYPE + " = " + reviewType;
		//条件
		StringBuffer conditionSql = new StringBuffer(" where flowrecord.").append(
				TableLfFlowRecord.MT_ID).append("=").append(mtID);
		//审批级别
		if(rLevel != null)
		{
			conditionSql = conditionSql.append(" and flowrecord.").append(TableLfFlowRecord.R_LEVEL).append(
					"<=").append(rLevel);
		}
		//类型
		if(reviewType != null)
		{
			conditionSql = conditionSql.append(" and flowrecord.").append(TableLfFlowRecord.REVIEW_TYPE).append(
					"=").append(reviewType);
		}
		//排序
		String orderBySql = new StringBuffer(" order by flowrecord.").append(
				TableLfFlowRecord.R_LEVEL).append(" asc").toString();
		//查询sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		//调用公共方法查询
		List<LfFlowRecordVo> returnVoList = findVoListBySQL(
				LfFlowRecordVo.class, sql, StaticValue.EMP_POOLNAME);
		//返回查询的数据
		return returnVoList;
	}
	
	
	/**
	 * 查询短信发送任务
	 * @param conditionMap
	 * @param orderMap
	 * @param pageInfo
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<SendedMttaskVo> findMtTaskVo(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String,String> orderMap,PageInfo pageInfo,String tableName) 
	throws Exception
	{
		//查询字段拼接
	    String fieldSql = SmstaskSql.getPartmttaskFieldSql();
	    //查询表拼接
	    String tableSql = SmstaskSql.getmttaskTableSql(tableName);
	    //组装过滤条件
	    String conditionSql = SmstaskSql.getmttaksConditionSql(conditionMap);
	    //组装SQL语句
	    String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
	    //组装统计语句
	    String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql)
	    .toString();
	    //调用查询语句
	    List<SendedMttaskVo> mttaskList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(SendedMttaskVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
	    //返回结果集
	    return mttaskList;
	}
	
	/**
	 * 获取短信发送任务
	 * @param conditionMap
	 * @param orderbyMap
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<SendedMttaskVo> findMtTaskVo(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String,String> orderbyMap,String tableName)
	         throws Exception
	{
		//查询字段拼接
		    String fieldSql = SmstaskSql.getPartmttaskFieldSql();
		    String tableSql = SmstaskSql.getmttaskTableSql(tableName);
		    //组装过滤条件
		    String conditionSql = SmstaskSql.getmttaksConditionSql(conditionMap);
		    //组装SQL语句
		    String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
            //调用查询方法
		    List<SendedMttaskVo> mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
		    //返回结果集
		    return mttaskList;
	}
	
	/**
	 * 发送详情的导入查询（同时查实时表和历史表）
	 * @param conditionMap
	 * @param orderbyMap
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<SendedMttaskVo> findMtTaskVoTwoTable(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String,String> orderbyMap,String tableName)
    throws Exception
	{
		//查询字段拼接
   	   	String fieldSql = SmstaskSql.getPartmttaskFieldSql();
   	   	//查询表拼接
	    String tableSql = SmstaskSql.getmttaskTableSqlTwo(tableName,conditionMap);
	    //查询条件拼接
	    String conditionSql =""; 
	    //组装SQL语句
	    String sql = new StringBuffer(fieldSql).append(" ,mt.").append(TableMtTask.TASK_ID).append(" from (").append(tableSql).append(") mt").append(conditionSql).toString();
        //调用查询方法
	    List<SendedMttaskVo> mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
	   
	   return mttaskList;
	}
	
	/**
	 * 发送详情的详情查询（同时查实时表和历史表）
	 * @param conditionMap
	 * @param orderbyMap
	 * @param tableName
	 * @param pageInfo 分页
	 * @return
	 * @throws Exception
	 */
	public List<SendedMttaskVo> findMtTaskVoTwoTable(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String,String> orderMap,PageInfo pageInfo,String tableName) 
	throws Exception
	{
		//查询字段拼接
	    String fieldSql = SmstaskSql.getPartmttaskFieldSql();
	    String tableSql = SmstaskSql.getmttaskTableSqlTwo(tableName,conditionMap);
	    //组装过滤条件
	    String conditionSql = "";//GenericLfMttaskVoSQL.getmttaksConditionSql(conditionMap);
	    //组装SQL语句
	    String sql = new StringBuffer(fieldSql).append(" ,mt.").append(TableMtTask.TASK_ID).append(" from (").append(tableSql).append(") mt").append(conditionSql).toString();
	    //组装统计语句
	    String countSql = new StringBuffer("select count(*) totalcount from (").append(tableSql).append(") mt").append(conditionSql)
	    .toString();
	    //调用查询方法 
	    List<SendedMttaskVo> mttaskList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(SendedMttaskVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
	    return mttaskList;
	}
	
	/**
	 * 跟据taskid查询mtdatareport表中任务的icount和
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public long findSumIcount(LinkedHashMap<String, String> conditionMap) 
	throws Exception
	{
		//查询字段拼接
	    String fieldSql = SmstaskSql.getSumIcountMtdatareportFieldSql();
	    String tableSql = SmstaskSql.getSumIcountMtdatareportTableSql();
	    //组装过滤条件 
	    String conditionSql = SmstaskSql.getSumIcountMtdatareportcConditionSql(conditionMap);
	    //组装SQL语句
	    String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
        //调用统计总条数语句
	    long  strCount=findCountBySQL(sql, null);
	    return strCount;
	}
	
	/**
	 * 获取短信发送任务
	 * @param GL_UserID
	 * @param lfMttaskVo
	 * @return lfMttaskVoList
	 * @throws Exception
	 */
	public List<LfMttaskVo> findLfMttaskVo(Long GL_UserID, LfMttaskVo lfMttaskVo)
			throws Exception
	{
		//查询字段拼接
		String fieldSql = SmstaskSql.getFieldSql();
		//查询表拼接
		String tableSql = SmstaskSql.getTableSql(lfMttaskVo.getCorpCode());
		//管辖范围拼接
		String dominationSql ="";
		String dominationSql2="";
		
		if((lfMttaskVo.getDepIds()==null||"".equals(lfMttaskVo.getDepIds()))&&(lfMttaskVo.getUserIds()==null||"".equals(lfMttaskVo.getUserIds()))){
			//获取操作员
			LfSysuser lfSysuser=new DataAccessDriver().getEmpDAO().findObjectByID(LfSysuser.class,GL_UserID);
			//如果登录操作员不是管理员，则进行管辖权限控制。因为接入类短信有可能没有操作员ID。
			if(!"admin".equals(lfSysuser.getUserName())){
				//管辖范围拼接
				dominationSql = SmstaskSql.getDominationSql(String
						.valueOf(GL_UserID));
				dominationSql2 = SmstaskSql.getDominationSql2(String
						.valueOf(GL_UserID));
			}else{
				//如果登录操作员是管理员，并且是多企业版的话，就加上企业编码的查询条件。
				 if(StaticValue.getCORPTYPE() ==1){
					 lfMttaskVo.setCorpCode(lfSysuser.getCorpCode());
				 }
			}
		}
		
		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			//包含子机构
		  if(lfMttaskVo.getIsContainsSun()!=null&&!"".equals(lfMttaskVo.getIsContainsSun())&&"1".equals(lfMttaskVo.getIsContainsSun())){
			LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
			if(lfDep!=null&&lfDep.getDepLevel().intValue()==1){
				    if(StaticValue.getCORPTYPE() ==1){
				    	//多企业
				    	lfMttaskVo.setDepIds("");
						lfMttaskVo.setCorpCode(lfDep.getCorpCode());
				    }else{
				    	//单企业
				    	lfMttaskVo.setDepIds("");
				    	lfMttaskVo.setUserId(0L);
				    }
			}else{
				String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),TableLfDep.DEP_ID);
				lfMttaskVo.setDepIds(depid);
			}
		  //不包含子机构
		  }else{
			  String depIdCondition=TableLfDep.DEP_ID+"="+lfMttaskVo.getDepIds();
			  lfMttaskVo.setDepIds(depIdCondition);
		  }
		}
		
		//组装过滤条件
		String conditionSql = SmstaskSql.getConditionSql(lfMttaskVo);
		//组装排序条件
		String orderBySql = SmstaskSql.getOrderBySql();
		//组装SQL语句
		String sql="";
		if((lfMttaskVo.getDepIds()==null||"".equals(lfMttaskVo.getDepIds()))&&
				(lfMttaskVo.getUserIds()==null||"".equals(lfMttaskVo.getUserIds()))&&
				lfMttaskVo.getCorpCode()==null||"".equals(lfMttaskVo.getCorpCode())){
			sql = new StringBuffer(fieldSql+" from ( "+fieldSql).append(tableSql).append(
				dominationSql2).append(conditionSql).append("  union  ").append(fieldSql).append(tableSql).append(
						dominationSql).append(conditionSql+" ) mttask ").append(orderBySql)
				.toString();
		}else{
			//组装SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					dominationSql).append(conditionSql).append(orderBySql)
					.toString();
		}
		//调用查询方法
		List<LfMttaskVo> lfMttaskVoList = findVoListBySQL(LfMttaskVo.class,
				sql, StaticValue.EMP_POOLNAME);
		return lfMttaskVoList;
	}
	
	/**
	 * 获取短信发送任务
	 * @param GL_UserID
	 * @param lfMttaskVo
	 * @param pageInfo
	 * @return lfMttaskVoList
	 * @throws Exception
	 */
	public List<LfMttaskVo> findLfMttaskVo(Long GL_UserID,
			LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception
	{
		//查询字段拼接
		String fieldSql = SmstaskSql.getFieldSql();
		//查询表拼接
		String tableSql = SmstaskSql.getTableSql(lfMttaskVo.getCorpCode());
		String dominationSql ="";
		String dominationSql2="";
		
		
		if((lfMttaskVo.getDepIds()==null||"".equals(lfMttaskVo.getDepIds()))&&(lfMttaskVo.getUserIds()==null||"".equals(lfMttaskVo.getUserIds()))){
			//获取操作员
			LfSysuser lfSysuser=new DataAccessDriver().getEmpDAO().findObjectByID(LfSysuser.class,GL_UserID);
			//如果登录操作员不是管理员，则进行管辖权限控制。因为接入类短信有可能没有操作员ID。
			if(!"admin".equals(lfSysuser.getUserName())){
				//管辖范围拼接
				dominationSql = SmstaskSql.getDominationSql(String
						.valueOf(GL_UserID));
				dominationSql2 = SmstaskSql.getDominationSql2(String
						.valueOf(GL_UserID));
			}else{
				//如果登录操作员是管理员，并且是多企业版的话，就加上企业编码的查询条件。
				 if(StaticValue.getCORPTYPE() ==1){
					 lfMttaskVo.setCorpCode(lfSysuser.getCorpCode());
				 }
			}
		}
		
		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			//包含子机构
		  if(lfMttaskVo.getIsContainsSun()!=null&&!"".equals(lfMttaskVo.getIsContainsSun())&&"1".equals(lfMttaskVo.getIsContainsSun())){
			LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
			if(lfDep!=null&&lfDep.getDepLevel().intValue()==1){
				    if(StaticValue.getCORPTYPE() ==1){
				    	//多企业
				    	lfMttaskVo.setDepIds("");
						lfMttaskVo.setCorpCode(lfDep.getCorpCode());
				    }else{
				    	//单企业
				    	lfMttaskVo.setDepIds("");
				    	lfMttaskVo.setUserId(0L);
				    }
			}else{
				String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),TableLfDep.DEP_ID);
				lfMttaskVo.setDepIds(depid);
			}
		  //不包含子机构
		  }else{
			  String depIdCondition=TableLfDep.DEP_ID+"="+lfMttaskVo.getDepIds();
			  lfMttaskVo.setDepIds(depIdCondition);
		  }
		}
		
		//组装过滤条件
		String conditionSql = SmstaskSql.getConditionSql(lfMttaskVo);
		//组装排序条件
		String orderBySql = SmstaskSql.getOrderBySql();
		//组装SQL语句
		String sql="";
		String countSql="";
		if((lfMttaskVo.getDepIds()==null||"".equals(lfMttaskVo.getDepIds()))&&
				(lfMttaskVo.getUserIds()==null||"".equals(lfMttaskVo.getUserIds()))&&
				lfMttaskVo.getCorpCode()==null||"".equals(lfMttaskVo.getCorpCode())){
			sql = new StringBuffer(fieldSql+" from ( "+fieldSql).append(tableSql).append(
				dominationSql2).append(conditionSql).append("  union  ").append(fieldSql).append(tableSql).append(
						dominationSql).append(conditionSql+" ) mttask ").append(orderBySql)
				.toString();
			countSql = new StringBuffer("select count(*) totalcount")
			.append("  from ((").append(fieldSql).append(tableSql).append(
					dominationSql2).append(conditionSql).append(") union (").append(fieldSql).append(tableSql).append(
							dominationSql).append(conditionSql).append(") ) A")
					.toString();
		}else{
			//组装SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					dominationSql).append(conditionSql).append(orderBySql)
					.toString();
			//组装总条数
			countSql = new StringBuffer("select count(*) totalcount")
					.append(tableSql).append(dominationSql).append(conditionSql)
					.toString();
		}
		//组装总条数
		//调用 查询方法
		List<LfMttaskVo> lfMttaskVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
						LfMttaskVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		//返回结果
		return lfMttaskVoList;
	}
	
	/**
	 * @description  非数据权限范围查找短信任务
	 * @param lfMttaskVo
	 * @param pageInfo
	 * @return lfMttaskVoList
	 * @throws Exception
	 */
	public List<LfMttaskVo> findLfMttaskVoWithoutDomination(
			LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception
	{
		//查询字段拼接
		String fieldSql = SmstaskSql.getFieldSql();
		//查询表拼接
		String tableSql = SmstaskSql.getTableSql(lfMttaskVo.getCorpCode());
        //根据机构组装下级机构
		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			//包含子机构
			if(lfMttaskVo.getIsContainsSun()!=null&&!"".equals(lfMttaskVo.getIsContainsSun())&&"1".equals(lfMttaskVo.getIsContainsSun())){
					LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
					if(lfDep!=null&&lfDep.getDepLevel().intValue()==1){
							lfMttaskVo.setDepIds("");
							lfMttaskVo.setCorpCode(lfDep.getCorpCode());
					}else{
						String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),TableLfDep.DEP_ID);
						lfMttaskVo.setDepIds(depid);
					}
			}else{
				 //不包含子机构
				String depIdCondition=TableLfDep.DEP_ID+"="+lfMttaskVo.getDepIds();
				lfMttaskVo.setDepIds(depIdCondition);
			}
		}
		//组装过滤条件
		String conditionSql = SmstaskSql.getConditionSql(lfMttaskVo);
		//排序条件拼接
		String orderBySql = SmstaskSql.getOrderBySql();
		String sql = new StringBuffer(fieldSql).append(tableSql)
				.append(conditionSql).append(orderBySql)
				.toString();
		//组装统计SQL语句
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql)
				.toString();
		//调用查询语句
		List<LfMttaskVo> lfMttaskVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
						LfMttaskVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return lfMttaskVoList;
	}

	/**
	 * 获取短信发送任务
	 * @param lfMttaskVo
	 * @return
	 * @throws Exception
	 */
	public List<LfMttaskVo> findLfMttaskVoWithoutDomination(LfMttaskVo lfMttaskVo)throws Exception
	{
		//查询字段拼接
		String fieldSql = SmstaskSql.getFieldSql();
		//查询表拼接
		String tableSql = SmstaskSql.getTableSql(lfMttaskVo.getCorpCode());

        //根据机构组装下级机构
		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			//包含子机构
			if(lfMttaskVo.getIsContainsSun()!=null&&!"".equals(lfMttaskVo.getIsContainsSun())&&"1".equals(lfMttaskVo.getIsContainsSun())){
					LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
					if(lfDep!=null&&lfDep.getDepLevel().intValue()==1){
							lfMttaskVo.setDepIds("");
							lfMttaskVo.setCorpCode(lfDep.getCorpCode());
					}else{
						String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),TableLfDep.DEP_ID);
						lfMttaskVo.setDepIds(depid);
					}
			}else{
				 //不包含子机构
				String depIdCondition=TableLfDep.DEP_ID+"="+lfMttaskVo.getDepIds();
				lfMttaskVo.setDepIds(depIdCondition);
			}
		}
		
		//组装过滤条件
		String conditionSql = SmstaskSql.getConditionSql(lfMttaskVo);
		//组装排序条件
		String orderBySql = SmstaskSql.getOrderBySql();
		//组装SQL语句
		String sql = new StringBuffer(fieldSql).append(tableSql)
				.append(conditionSql).append(orderBySql)
				.toString();
        //调用查询方法
		List<LfMttaskVo> lfMttaskVoList = findVoListBySQL(LfMttaskVo.class,
				sql, StaticValue.EMP_POOLNAME);

		return lfMttaskVoList;
	}
	
	/**
	 * 获取用户编号及机构编号对应关系
	 * @param sysuserID
	 * @param depId
	 * @return List<LfSysuser>
	 * @throws Exception
	 */
	public List<LfSysuser> findDomUserBySysuserIDOfSmsTaskRecordByDep(String sysuserID,String depId)
    throws Exception {
		//拼接sql
		StringBuffer domination = new StringBuffer("select ").append(
			TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(" where ").append(
							TableLfDomination.USER_ID).append("=").append(sysuserID);
		StringBuffer dominationSql = new StringBuffer(" where (").append(
			TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
			" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
					domination).append(")) and ").append(TableLfSysuser.USER_ID)
					.append("<>1 and ").append(TableLfSysuser.DEP_ID).append("=").append(depId);
		String sql = new StringBuffer("select * from ").append(
			TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
		//排序条件拼接
		sql += " order by " + TableLfSysuser.NAME + " asc";
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
			StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
