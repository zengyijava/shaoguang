/**
 * 
 */
package com.montnets.emp.netnews.dao;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.netnews.vo.LfMttaskVo;
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
	    List<SendedMttaskVo> mttaskList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(SendedMttaskVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
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
	    List<SendedMttaskVo> mttaskList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(SendedMttaskVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
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
		String tableSql = SmstaskSql.getTableSql();
		//管辖范围拼接
		String dominationSql ="";
		String dominationSql2="";
		
		if((lfMttaskVo.getDepIds()==null||"".equals(lfMttaskVo.getDepIds()))&&(lfMttaskVo.getUserIds()==null||"".equals(lfMttaskVo.getUserIds()))){
			//管辖范围拼接
			dominationSql = SmstaskSql.getDominationSql(String
					.valueOf(GL_UserID));
			dominationSql2 = SmstaskSql.getDominationSql2(String
					.valueOf(GL_UserID));
		}
		
		if(lfMttaskVo.getDepIds() != null && !"".equals(lfMttaskVo.getDepIds()))
		{
			if(lfMttaskVo.getIsContainsSun() == null || (lfMttaskVo.getIsContainsSun() != null && !"".equals(lfMttaskVo.getIsContainsSun()) && "1".equals(lfMttaskVo.getIsContainsSun())))
			{
				LfDep lfDep = new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
				if(lfDep != null && lfDep.getDepLevel().intValue() == 1)
				{
					lfMttaskVo.setDepIds("");
					lfMttaskVo.setCorpCode(lfDep.getCorpCode());
				}
				else
				{
					String depid = new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()), TableLfDep.DEP_ID);
					lfMttaskVo.setDepIds(depid);
				}

				// 不包含子机构
			}
			else
			{
				String depIdCondition = TableLfDep.DEP_ID + "=" + lfMttaskVo.getDepIds();
				lfMttaskVo.setDepIds(depIdCondition);
			}
		}
		
		
		//组装过滤条件
		String conditionSql = SmstaskSql.getConditionSql(lfMttaskVo);
		
		if("".equals(dominationSql)){
			//存在查询条件
			if(conditionSql != null && conditionSql.length() > 0)
			{
				//将条件字符串首个and替换为where,
				conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
			}
		}
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
		String tableSql = SmstaskSql.getTableSql();
		String dominationSql ="";
		String dominationSql2="";

		
		if((lfMttaskVo.getDepIds()==null||"".equals(lfMttaskVo.getDepIds()))&&(lfMttaskVo.getUserIds()==null||"".equals(lfMttaskVo.getUserIds()))){
		//管辖范围拼接
		dominationSql = SmstaskSql.getDominationSql(String
				.valueOf(GL_UserID));
		dominationSql2 = SmstaskSql.getDominationSql2(String
				.valueOf(GL_UserID));
		}
		
		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			if(lfMttaskVo.getIsContainsSun()==null||(lfMttaskVo.getIsContainsSun()!=null&&!"".equals(lfMttaskVo.getIsContainsSun())&&"1".equals(lfMttaskVo.getIsContainsSun()))){
				LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
				if(lfDep!=null&&lfDep.getDepLevel().intValue()==1){
					  lfMttaskVo.setDepIds("");
					  lfMttaskVo.setCorpCode(lfDep.getCorpCode());
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
		if("".equals(dominationSql)){
			//存在查询条件
			if(conditionSql != null && conditionSql.length() > 0)
			{
				//将条件字符串首个and替换为where,
				conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
			}
		}
		
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
		String tableSql = SmstaskSql.getTableSql();
		
		// 根据机构组装下级机构
		if(lfMttaskVo.getDepIds() != null && !"".equals(lfMttaskVo.getDepIds()))
		{
			if(lfMttaskVo.getIsContainsSun() == null || (lfMttaskVo.getIsContainsSun() != null && !"".equals(lfMttaskVo.getIsContainsSun()) && "1".equals(lfMttaskVo.getIsContainsSun())))
			{
				LfDep lfDep = new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
				if(lfDep != null && lfDep.getDepLevel().intValue() == 1)
				{
					lfMttaskVo.setDepIds("");
					lfMttaskVo.setCorpCode(lfDep.getCorpCode());
				}
				else
				{
					String depid = new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()), TableLfDep.DEP_ID);
					lfMttaskVo.setDepIds(depid);
				}
				// 不包含子机构
			}
			else
			{
				String depIdCondition = TableLfDep.DEP_ID + "=" + lfMttaskVo.getDepIds();
				lfMttaskVo.setDepIds(depIdCondition);
			}
		}
		
		//组装过滤条件
		String conditionSql = SmstaskSql.getConditionSql(lfMttaskVo);
		
		//存在查询条件
		if(conditionSql != null && conditionSql.length() > 0)
		{
			//将条件字符串首个and替换为where,
			conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		
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
		String tableSql = SmstaskSql.getTableSql();

		if(lfMttaskVo.getDepIds() != null && !"".equals(lfMttaskVo.getDepIds()))
		{
			if(lfMttaskVo.getIsContainsSun() == null || (lfMttaskVo.getIsContainsSun() != null && !"".equals(lfMttaskVo.getIsContainsSun()) && "1".equals(lfMttaskVo.getIsContainsSun())))
			{
				// 根据机构组装下级机构
				LfDep lfDep = new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
				if(lfDep != null && lfDep.getDepLevel().intValue() == 1)
				{
					lfMttaskVo.setDepIds("");
					lfMttaskVo.setCorpCode(lfDep.getCorpCode());
				}
				else
				{
					String depid = new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()), TableLfDep.DEP_ID);
					lfMttaskVo.setDepIds(depid);
				}
				// 不包含子机构
			}
			else
			{
				String depIdCondition = TableLfDep.DEP_ID + "=" + lfMttaskVo.getDepIds();
				lfMttaskVo.setDepIds(depIdCondition);
			}
		}
	
		
		
		
		
		
		//组装过滤条件
		String conditionSql = SmstaskSql.getConditionSql(lfMttaskVo);
		
		//存在查询条件
		if(conditionSql != null && conditionSql.length() > 0)
		{
			//将条件字符串首个and替换为where,
			conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		}
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
	
	/**
	 * 查询下行实时记录
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @return 返回下行实时记录对象集合
	 */
	public List<SendedMttaskVo> findMtTaskReal(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			//查询字段拼接
			String fieldSql = "select mt.UNICOM,mt.PHONE,mt.MESSAGE,mt.ERRORCODE,mt.PKNUMBER,mt.PKTOTAL ";
			
			//查询表拼接
			String tableSql = findMtTaskRealTableSql();
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			//组装SQL语句
			String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
			
			List<SendedMttaskVo> mttaskList;
			//有分页
			if(pageInfo != null)
			{
				//组装统计语句
				//String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
				WxMtTaskGenericDAO mtTaskDao = new WxMtTaskGenericDAO();
				//调用查询语句
				mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME, null);
			}
			else
			{
				//调用查询方法
			    mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
			}
			//返回结果集
			return mttaskList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计查询，实时查询sql查询异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行实时记录
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @return 返回下行实时记录对象集合
	 */
	public boolean findMtTaskRealPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			//查询表拼接
			String tableSql = findMtTaskRealTableSql();
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			
			//组装统计语句
			String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
			return new WxMtTaskGenericDAO().findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计查询，实时查询sql查询分页信息，异常。");
			return false;
		}
	}
	
	/**
	 * 获取下行实时记录表名
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @return 返回下行实时记录对象集合
	 */
	private String findMtTaskRealTableSql()
	{
		try
		{
			String tableName = "gw_mt_task_bak";;
			//查询表拼接
			String tableSql = new StringBuffer(" from ").append(tableName).append(" mt ").append(StaticValue.getWITHNOLOCK()).toString();
			
			//返回结果集
			return tableSql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计查询，实时查询sql获取表名，异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行历史记录
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @param tableName 历史表名
	 * @return 返回下行实时记录对象集合
	 */
	public List<SendedMttaskVo> findMtTaskHis(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName,long millTime,PageInfo realDbpageInfo)
	{
		try
		{
			SimpleDateFormat dfMM = new SimpleDateFormat("MM");
			SimpleDateFormat dfyyyy = new SimpleDateFormat("yyyy");
			Date sendDateTime = new Date(millTime);
			String YandM=dfyyyy.format(sendDateTime) + dfMM.format(sendDateTime);
			SimpleDateFormat sdf_yyyyMM=new SimpleDateFormat("yyyyMM");
			int curDate = Integer.parseInt(sdf_yyyyMM.format(System.currentTimeMillis()));

			//查询字段拼接
			String fieldSql = "select mt.UNICOM,mt.PHONE,mt.MESSAGE,mt.ERRORCODE,mt.PKNUMBER,mt.PKTOTAL ";
			//查询表拼接
			String tableSql = findMtTaskHisTable(tableName);
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			//组装SQL语句
			String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
			
			List<SendedMttaskVo> mttaskList = null;
			int yearAndMonth=201610;
			if(StaticValue.getUseHistoryDBTime() !=null&&!"".equals(StaticValue.getUseHistoryDBTime())){
				yearAndMonth=Integer.parseInt(StaticValue.getUseHistoryDBTime());
			}
			//有分页
			//由于网讯发送详情导出execl数据出现重复,现在按页面查询一样的逻辑 。时间：2018-11-12
//			if(pageInfo != null)
//			{
				if(pageInfo == null){
					pageInfo=new PageInfo();
					pageInfo.setPageSize(500000);
				}
				//根据时间确定查询数据的数据库,true:查历史库；false:查实时库
				boolean isbackDb = false;
				//未开启实时数据保留实时库时间
				if(StaticValue.getReadDataSaveTime() == -1)
				{
				if(Integer.parseInt(YandM) < yearAndMonth)
				{
					//查历史库
					isbackDb = true;
				}
				}else {
					//当前日期减去数据日期大于数据保留实时库时间，查询历史数据库
					if(curDate - Integer.parseInt(YandM) > StaticValue.getReadDataSaveTime())
					{
						//查历史库
						isbackDb = true;
					}
				}
				//组装统计语句
				//String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
				WxMtTaskGenericDAO mtTaskDao = new WxMtTaskGenericDAO();
			    String use_his_server = SystemGlobals.getValue("montnets.emp.use_history_server");
			    //如果是MTTASK201610表
			    if(("MTTASK"+yearAndMonth).equals(tableName.toUpperCase())&& "1".equals(use_his_server))
			    {
			    	//是否第一页，查两个库这两张表的总数
			    	if(pageInfo.getPageIndex() == 1)
			    	{
			    		mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME, null);
			    		
			    		//设置实时库的总记录数
			    		realDbpageInfo.setTotalPage(pageInfo.getTotalPage());
			    		//设置实时库的总页数
			    		realDbpageInfo.setTotalRec(pageInfo.getTotalRec());
			    		realDbpageInfo.setPageSize(pageInfo.getPageSize());
				    	//获取历史库的总数
			    		PageInfo pageInfoTemp = new PageInfo();
				    	//设置查询一页条数
				    	pageInfoTemp.setPageSize(pageInfo.getPageSize());
				    	List<SendedMttaskVo> mttaskBackDbList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfoTemp, StaticValue.EMP_BACKUP,null);
				    	EmpExecutionContext.info("查询网讯发送历史任务详情实时库第一页，PageSize:"+pageInfoTemp.getPageSize()+"，PageIndex:"+pageInfoTemp.getPageIndex()+"，sql:"+sql);
				    	if(pageInfoTemp.getTotalRec() > 0)
				    	{
				    		if(pageInfo.getTotalRec() == 0)
				    		{
				    			//直接赋值
				    			pageInfo.setTotalPage(pageInfoTemp.getTotalPage());
				    		}
				    		else
				    		{
				    			//累加总页数
				    			pageInfo.setTotalPage(pageInfo.getTotalPage() + pageInfoTemp.getTotalPage());
				    		}
				    		//累加总记录数
				    		pageInfo.setTotalRec(pageInfo.getTotalRec() + pageInfoTemp.getTotalRec());
				    	}
				    	//实时库有数据，直接返回
				    	if(mttaskList != null && mttaskList.size() > 0)
				    	{
				    		return mttaskList;
				    	}
				    	return mttaskBackDbList;
			    	}else
			    	{
			    		//根据分页查询实时库数据
			    		mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME,null);
			    		//如果实时库有数据
			    		if(mttaskList != null && mttaskList.size() > 0)
			    		{
			    			//返回实时库查询结果
			    			return mttaskList;
			    		}
			    		//实时库无数据
			    		else
			    		{	
			    			PageInfo pageInfoTemp = new PageInfo();
			    			pageInfoTemp.setPageSize(pageInfo.getPageSize());
			    			int index=1;
			    			if(realDbpageInfo.getTotalRec()>realDbpageInfo.getPageSize()){
			    				index=realDbpageInfo.getTotalRec()/realDbpageInfo.getPageSize()-1;
			    			}
			    			pageInfoTemp.setPageIndex(pageInfo.getPageIndex() - index);
			    			//返回历史库查询结果
			    			mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfoTemp, StaticValue.EMP_BACKUP,null);
			    			EmpExecutionContext.info("查询网讯发送历史任务详情历史库，PageSize:"+pageInfoTemp.getPageSize()+"，PageIndex:"+pageInfoTemp.getPageIndex()+"，sql:"+sql);
			    			return mttaskList;
			    		}
			    	}
			    }else{
			    	String poolName = StaticValue.EMP_POOLNAME;
			    	//查询历史库
			    	if(isbackDb&& "1".equals(use_his_server))
			    	{
			    		poolName = StaticValue.EMP_BACKUP;
			    	}
					//调用查询语句
					mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, poolName, null);
			    }
		    	
//			}
//			else
//			{
//				//如果没有分页 调用查询方法
//				mttaskList =new ArrayList<SendedMttaskVo>();
//				List<SendedMttaskVo> mttaskList_real = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
//			    List<SendedMttaskVo> mttaskList_bak = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_BACKUP);
//			    if(mttaskList_real!=null&&mttaskList_bak.size()>0){
//			    	mttaskList.addAll(mttaskList_real);
//			    }
//			    if(mttaskList_bak!=null&&mttaskList_bak.size()>0){
//			    	mttaskList.addAll(mttaskList_bak);
//			    }
//			}
			//返回结果集
			return mttaskList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计查询，历史查询sql查询异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行历史记录分页信息(分库查询)
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @param tableName 历史表名
	 * @return 返回下行实时记录对象集合
	 */
	public boolean findMtTaskHisPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName, PageInfo realDbpageInfo)
	{
		WxMtTaskGenericDAO wxMtTaskGenericDAO= new WxMtTaskGenericDAO();
		try
		{
			//查询表拼接
			String tableSql = findMtTaskHisTable(tableName);
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			
			//组装统计语句
			String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
			
			
			//读取配置文件里是否启用备用服务器
			String use_his_server = SystemGlobals.getValue("montnets.emp.use_history_server");
			if("1".equals(use_his_server))
			{

				wxMtTaskGenericDAO.findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
    			//设置实时库的总页数
    			realDbpageInfo.setTotalPage(pageInfo.getTotalPage());
    			//设置实时库的总记录数
    			realDbpageInfo.setTotalRec(pageInfo.getTotalRec());
    			
		    	//获取历史库的总数
	    		PageInfo pageInfoTemp = new PageInfo();
		    	//设置查询一页条数
		    	pageInfoTemp.setPageSize(pageInfo.getPageSize());
		    	wxMtTaskGenericDAO.findPageInfoBySQL(countSql, pageInfoTemp, StaticValue.EMP_BACKUP, null);
		    	if(pageInfoTemp.getTotalRec() > 0)
		    	{
		    		if(pageInfo.getTotalRec() == 0)
		    		{
		    			//直接赋值
		    			pageInfo.setTotalPage(pageInfoTemp.getTotalPage());
		    		}
		    		else
		    		{
		    			//累加总页数
		    			pageInfo.setTotalPage(pageInfo.getTotalPage() + pageInfoTemp.getTotalPage());
		    		}
		    		//累加总记录数
		    		pageInfo.setTotalRec(pageInfo.getTotalRec() + pageInfoTemp.getTotalRec());
		    	}
		    	return true;
			}
			else
			{
				return wxMtTaskGenericDAO.findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
			}
			
			
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计查询，历史查询sql查询异常。");
			return false;
		}
	}
	
	
	/**
	 * 查询下行历史记录
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @param tableName 历史表名
	 * @return 返回下行实时记录对象集合
	 */
	public List<SendedMttaskVo> findMtTaskHis(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName)
	{
		try
		{
			//查询字段拼接
			String fieldSql = "select mt.UNICOM,mt.PHONE,mt.MESSAGE,mt.ERRORCODE,mt.PKNUMBER,mt.PKTOTAL ";
			//查询表拼接
			String tableSql = findMtTaskHisTable(tableName);
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			//组装SQL语句
			String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
			
			List<SendedMttaskVo> mttaskList;
			//有分页
			if(pageInfo != null)
			{
				//组装统计语句
				//String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
				WxMtTaskGenericDAO mtTaskDao = new WxMtTaskGenericDAO();
				//调用查询语句
				mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME, null);
			}
			else
			{
				//调用查询方法
			    mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
			}
			//返回结果集
			return mttaskList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计查询，历史查询sql查询异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行历史记录分页信息
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @param tableName 历史表名
	 * @return 返回下行实时记录对象集合
	 */
	public boolean findMtTaskHisPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName)
	{
		try
		{
			//查询表拼接
			String tableSql = findMtTaskHisTable(tableName);
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			
			//组装统计语句
			String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
			return new WxMtTaskGenericDAO().findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计查询，历史查询sql查询异常。");
			return false;
		}
	}
	
	/**
	 * 获取下行历史表名
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @param tableName 历史表名
	 * @return 返回下行实时记录对象集合
	 */
	private String findMtTaskHisTable(String tableName)
	{
		try
		{
			//查询表拼接
			String tableSql = new StringBuffer(" from ").append(tableName).append(" mt ").append(StaticValue.getWITHNOLOCK()).toString();
			return tableSql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计查询，历史查询sql获取表名，异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行业务记录，历史和实时查询
	 * @param conditionMap 查询条件
	 * @param tableName 历史表名
	 * @param pageInfo 分页信息对象
	 * @return 返回下行记录对象集合
	 */
	public List<SendedMttaskVo> findMtTaskHisAndReal(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName)
	{
		try
		{
			//查询字段拼接
			String fieldSql = "select mt.UNICOM,mt.PHONE,mt.MESSAGE,mt.ERRORCODE,mt.PKNUMBER,mt.PKTOTAL,mt.TASKID ";
			
			StringBuffer tableSql = findMtTaskHisAndRealTable(conditionMap, tableName);
			
			//组装SQL语句
			String sql = new StringBuffer(fieldSql).append(" from (").append(tableSql).append(") mt").toString();
			
			List<SendedMttaskVo> mttaskList;
			//要分页
			if(pageInfo != null)
			{
				//组装统计语句
			    //String countSql = new StringBuffer("select count(*) totalcount from (").append(tableSql).append(") mt").toString();
			    WxMtTaskGenericDAO mtTaskDao = new WxMtTaskGenericDAO();
			    //调用查询方法 
			    mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME, null);
			}
			else
			{
				//调用查询方法 
				mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
			}
			return mttaskList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计记录，历史和实时查询异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行业务记录，历史和实时查询分页信息
	 * @param conditionMap 查询条件
	 * @param tableName 历史表名
	 * @param pageInfo 分页信息对象
	 * @return 返回下行记录对象集合
	 */
	public boolean findMtTaskHisAndRealPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName)
	{
		try
		{
			StringBuffer tableSql = findMtTaskHisAndRealTable(conditionMap, tableName);
			
			//组装统计语句
		    String countSql = new StringBuffer("select count(*) totalcount from (").append(tableSql).append(") mt").toString();
		    return new WxMtTaskGenericDAO().findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询网讯发送统计，历史和实时sql查询分页信息，异常。");
			return false;
		}
	}
	
	/**
	 * 获取历史和实时查询表名
	 * @param conditionMap
	 * @param tableName
	 * @return
	 */
	private StringBuffer findMtTaskHisAndRealTable(LinkedHashMap<String, String> conditionMap, String tableName)
	{
		try
		{
			//实时记录表名
			String realTableName = "gw_mt_task_bak";;
			
			//查询历史表sql
			StringBuffer sqlHis = new StringBuffer("select ms.UNICOM,ms.PHONE,ms.TASKID,ms.MESSAGE,ms.ERRORCODE,ms.PKNUMBER,ms.PKTOTAL")
			   .append(" from ").append(tableName).append(" ms ").append(StaticValue.getWITHNOLOCK())
			   .append(getMttaskConditionSql(conditionMap,"ms"));
			
			//查询实时表sql
			StringBuffer sqlReal = new StringBuffer("select ml.UNICOM,ml.PHONE,ml.TASKID,ml.MESSAGE,ml.ERRORCODE,ml.PKNUMBER,ml.PKTOTAL")
			   .append(" from ").append(realTableName).append(" ml ").append(StaticValue.getWITHNOLOCK())
			   .append(getMttaskConditionSql(conditionMap,"ml"));
			
			StringBuffer tableSql = new StringBuffer().append(sqlHis).append(" union all ").append(sqlReal);
			
			return tableSql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询网讯发送统计，获取历史和实时查询表名，异常。");
			return null;
		}
	}
	
	/**
	 * 组装过滤条件
	 * @param conditionMap 查询条件
	 * @return 返回sql，包含where
	 */
	private String getMttaskConditionSql(LinkedHashMap<String, String> conditionMap, String tableName)
	{
		StringBuffer conditionSql = new StringBuffer();
		if(conditionMap.get("taskid") != null && conditionMap.get("taskid").length() > 0)
		{
			conditionSql.append(" where ").append(tableName).append(".TASKID").append("=").append(conditionMap.get("taskid"));
		}	
		else
		{
			conditionSql.append(" where 1=2");
		}
		
		//错误编码查询类型
		String type = conditionMap.get("type");
		//接收失败
        if(type.equals("2"))
        {
        	conditionSql
        	.append(" and ").append(tableName).append(".ERRORCODE not like 'E1:%' ")
			.append(" and ").append(tableName).append(".ERRORCODE not like 'E2:%' ")
			.append(" and ").append(tableName).append(".ERRORCODE not in ('DELIVRD' ,'0      ','       ')");
        }
        //提交失败
        else if(type.equals("3"))
        {
        	conditionSql.append(" and (").append(tableName).append(".ERRORCODE like 'E1:%' or ")
			.append(tableName).append(".ERRORCODE like 'E2:%' )");
        }
        //发送成功
        else if(type.equals("5"))
        {
        	//发送成功
			conditionSql.append(" and ").append(tableName).append(".ERRORCODE in ('DELIVRD' ,'0      ') ");
        }
        //状态未返
        else if(type.equals("6"))
        {
        	//状态未返
			conditionSql.append(" and ").append(tableName).append(".ERRORCODE = '       ' ");
        }
		
		if(conditionMap.get("phone") != null && conditionMap.get("phone").length() > 0)
		{
			conditionSql.append(" and ").append(tableName).append(".PHONE = '").append(conditionMap.get("phone")).append("'");
		}
		if(conditionMap.get("unicom") != null && conditionMap.get("unicom").length() > 0)
		{
			conditionSql.append(" and ").append(tableName).append(".UNICOM=").append(conditionMap.get("unicom"));
		}
		return conditionSql.toString();
	}
	
	/**
	 * 根据操作员id获取管辖机构的顶级机构对象集合
	 * @param sysuserID 操作员id
	 * @param corpCode 操作员所属企业
	 * @return 返回操作员管辖机构的顶级机构对象集合
	 */
	public List<LfDep> findTopDepByUserId(String sysuserID, String corpCode)
	{
		try
		{
			//拼接sql
			StringBuffer sql = new StringBuffer();
			sql.append("select dep.* from LF_DEP dep inner join LF_DOMINATION domination on dep.DEP_ID=domination.DEP_ID")
				.append(" where domination.USER_ID=").append(sysuserID)
				.append(" and dep.DEP_STATE=1")
				.append(" and dep.CORP_CODE='").append(corpCode).append("'")
				.append(" order by dep.DEP_ID asc");
			
			//只拿第一条顶级机构
			PageInfo pageInfo = new PageInfo();
			pageInfo.setPageSize(1);
			//返回结果
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LfDep.class, sql.toString(), null, pageInfo, StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯DAO，根据操作员id获取管辖机构的顶级机构对象集合，异常。sysuserID="+sysuserID+",corpCode="+corpCode);
			return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}

