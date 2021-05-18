package com.montnets.emp.mmstask.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.common.vo.LfMttaskVo2;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.approveflow.TableLfFlowRecord;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;



public class MmsTaskDao extends SuperDAO{
	
	/**
	 * @description  非数据权限范围查找短信任务
	 * @param lfMttaskVo
	 * @param pageInfo
	 * @return lfMttaskVoList
	 * @throws Exception
	 */
	public List<LfMttaskVo2> findLfMttaskVoWithoutDomination(
			LfMttaskVo2 lfMttaskVo, PageInfo pageInfo) throws Exception
	{
		//查询字段拼接
		String fieldSql = LfMttaskVoSQL.getFieldSql();
		//查询表拼接
		String tableSql = LfMttaskVoSQL.getTableSql();
        //根据机构组装下级机构
		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			//包含子机构
			if(lfMttaskVo.getIsContainsSun()!=null&&!"".equals(lfMttaskVo.getIsContainsSun())&&"1".equals(lfMttaskVo.getIsContainsSun())){
				String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),"dep."+TableLfDep.DEP_ID);
				lfMttaskVo.setDepIds(depid);
			}else{
				 String depIdCondition="dep."+TableLfDep.DEP_ID+"="+lfMttaskVo.getDepIds();
				 lfMttaskVo.setDepIds(depIdCondition);
			}
		}
		//组装过滤条件
		String conditionSql = LfMttaskVoSQL.getConditionSql(lfMttaskVo);
		List<String> timeList = LfMttaskVoSQL
				.getTimeCondition(lfMttaskVo);
		//排序条件拼接
		String orderBySql = LfMttaskVoSQL.getOrderBySql();
		String sql = new StringBuffer(fieldSql).append(tableSql)
				.append(conditionSql).append(orderBySql)
				.toString();
		//组装统计SQL语句
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql)
				.toString();
		//调用查询语句
		List<LfMttaskVo2> lfMttaskVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
						LfMttaskVo2.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME,timeList);
		return lfMttaskVoList;
	}
	
	
	/**
	 * 获取短信发送任务
	 * @param lfMttaskVo
	 * @return
	 * @throws Exception
	 */
	public List<LfMttaskVo2> findLfMttaskVoWithoutDomination(LfMttaskVo2 lfMttaskVo)throws Exception
	{
		//查询字段拼接
		String fieldSql = LfMttaskVoSQL.getFieldSql();
		//查询表拼接
		String tableSql = LfMttaskVoSQL.getTableSql();

		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			if(lfMttaskVo.getIsContainsSun()!=null&&!"".equals(lfMttaskVo.getIsContainsSun())&&"1".equals(lfMttaskVo.getIsContainsSun())){
				String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),"dep."+TableLfDep.DEP_ID);
				lfMttaskVo.setDepIds(depid);
			}else{
				 String depIdCondition="dep."+TableLfDep.DEP_ID+"="+lfMttaskVo.getDepIds();
				  lfMttaskVo.setDepIds(depIdCondition);
			}
		}
		//组装过滤条件
		String conditionSql = LfMttaskVoSQL.getConditionSql(lfMttaskVo);
		List<String> timeList = LfMttaskVoSQL
				.getTimeCondition(lfMttaskVo);
		//组装排序条件
		String orderBySql = LfMttaskVoSQL.getOrderBySql();
		//组装SQL语句
		String sql = new StringBuffer(fieldSql).append(tableSql)
				.append(conditionSql).append(orderBySql)
				.toString();
        //调用查询方法
		List<LfMttaskVo2> lfMttaskVoList = findVoListBySQL(LfMttaskVo2.class,
				sql, StaticValue.EMP_POOLNAME,timeList);

		return lfMttaskVoList;
	}
	

	
	
	/**
	 * 获取短信发送任务
	 * @param GL_UserID
	 * @param lfMttaskVo
	 * @return lfMttaskVoList
	 * @throws Exception
	 */
	public List<LfMttaskVo2> findLfMttaskVo(Long GL_UserID, LfMttaskVo2 lfMttaskVo)
			throws Exception
	{
		//查询字段拼接
		String fieldSql = LfMttaskVoSQL.getFieldSql();
		//查询表拼接
		String tableSql = LfMttaskVoSQL.getTableSql();
		//管辖范围拼接
		String dominationSql = LfMttaskVoSQL.getDominationSql(String
				.valueOf(GL_UserID));
		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			//包含子机构
			if(lfMttaskVo.getIsContainsSun()!=null&&!"".equals(lfMttaskVo.getIsContainsSun())&&"1".equals(lfMttaskVo.getIsContainsSun())){
				String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),"dep."+TableLfDep.DEP_ID);
				lfMttaskVo.setDepIds(depid);
			}else{
				 String depIdCondition="dep."+TableLfDep.DEP_ID+"="+lfMttaskVo.getDepIds();
				 lfMttaskVo.setDepIds(depIdCondition);
			}
		}
		//组装过滤条件
		String conditionSql = LfMttaskVoSQL.getConditionSql(lfMttaskVo);
		List<String> timeList = LfMttaskVoSQL
				.getTimeCondition(lfMttaskVo);
		//组装排序条件
		String orderBySql = LfMttaskVoSQL.getOrderBySql();
		//组装SQL语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//调用查询方法
		List<LfMttaskVo2> lfMttaskVoList = findVoListBySQL(LfMttaskVo2.class,
				sql, StaticValue.EMP_POOLNAME,timeList);
		return lfMttaskVoList;
	}
	
	
	/* 获取短信发送任务
	 * @param GL_UserID
	 * @param lfMttaskVo
	 * @param pageInfo
	 * @return lfMttaskVoList
	 * @throws Exception
	 */
	public List<LfMttaskVo2> findLfMttaskVo(Long GL_UserID,
			LfMttaskVo2 lfMttaskVo, PageInfo pageInfo) throws Exception
	{
		//查询字段拼接
		String fieldSql = LfMttaskVoSQL.getFieldSql();
		//查询表拼接
		String tableSql = LfMttaskVoSQL.getTableSql();
		//管辖范围拼接
		String dominationSql = LfMttaskVoSQL.getDominationSql(String
				.valueOf(GL_UserID));
		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			//包含子机构
			if(lfMttaskVo.getIsContainsSun()!=null&&!"".equals(lfMttaskVo.getIsContainsSun())&&"1".equals(lfMttaskVo.getIsContainsSun())){
				String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),"dep."+TableLfDep.DEP_ID);
				lfMttaskVo.setDepIds(depid);
			}else{
				 String depIdCondition="dep."+TableLfDep.DEP_ID+"="+lfMttaskVo.getDepIds();
				  lfMttaskVo.setDepIds(depIdCondition);
			}
		}
		//组装过滤条件
		String conditionSql = LfMttaskVoSQL.getConditionSql(lfMttaskVo);
		List<String> timeList = LfMttaskVoSQL
				.getTimeCondition(lfMttaskVo);
		//组装排序条件
		String orderBySql = LfMttaskVoSQL.getOrderBySql();
		//组装SQL语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//组装总条数
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(dominationSql).append(conditionSql)
				.toString();
		//调用 查询方法
		List<LfMttaskVo2> lfMttaskVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(LfMttaskVo2.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME,timeList);
		//返回结果
		return lfMttaskVoList;
	}
	
	
	/* @param mtID
	 * @param rLevel
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordVo> findLfFlowRecordVo(String mtID, String rLevel,String reviewType)
			throws Exception
	{
		//字段
		String fieldSql = LfFlowRecordVoSQL.getFieldSql();
		//表名
		String tableSql = LfFlowRecordVoSQL.getTableSql();
		
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
	 * 获取当前登录用户的审批流信息
	 * @param GL_UserID
	 * @param lfFlowRecordVo
	 * @return lfFlowRecordVoList
	 * @throws Exception
	 */
	public List<LfFlowRecordVo> findLfFlowRecordVo(Long GL_UserID,
			LfFlowRecordVo lfFlowRecordVo, PageInfo pageInfo) throws Exception
	{
		//字段sql
		String fieldSql = LfFlowRecordVoSQL.getFieldSql();
		//表名sql
		String tableSql = LfFlowRecordVoSQL.getTableSql();
		tableSql=tableSql+" and mttask."+TableLfMttask.MS_TYPE+"=userdata."+TableUserdata.ACCOUNTTYPE;
		//管辖范围
		String dominationSql = LfFlowRecordVoSQL.getDominationSql(String
				.valueOf(GL_UserID));
		String conditionSql = LfFlowRecordVoSQL
				.getConditionSql(lfFlowRecordVo);
		//时间
		List<String> timeList = LfFlowRecordVoSQL
				.getTimeCondition(lfFlowRecordVo);
		String orderBySql = LfFlowRecordVoSQL.getOrderBySql();
		//查询sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//分页sql
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(dominationSql).append(conditionSql)
				.toString();
		//调用vo的list公共查询方法，并返回查询数据
		List<LfFlowRecordVo> lfFlowRecordVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						LfFlowRecordVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		//返回数据
		return lfFlowRecordVoList;
	}

	/**
	 * 获取当前登录用户的审批流信息
	 * @param loginUserId
	 * @param lfFlowRecordVo
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordVo> findLfFlowRecordVo(Long loginUserId,
			LfFlowRecordVo lfFlowRecordVo) throws Exception
	{
		//字段sql
		String fieldSql = LfFlowRecordVoSQL.getFieldSql();
		//表名sql
		String tableSql = LfFlowRecordVoSQL.getTableSql();
		//管辖范围
		String dominationSql = LfFlowRecordVoSQL.getDominationSql(String
				.valueOf(loginUserId));
		//条件sql
		String conditionSql = LfFlowRecordVoSQL
				.getConditionSql(lfFlowRecordVo);
		//时间
		List<String> timeList = LfFlowRecordVoSQL
				.getTimeCondition(lfFlowRecordVo);
		//排序sql
		String orderBySql = LfFlowRecordVoSQL.getOrderBySql();
		//查询sql拼接
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//调用vo的list公共查询方法，并返回查询数据
		List<LfFlowRecordVo> returnVoList = findVoListBySQL(
				LfFlowRecordVo.class, sql, StaticValue.EMP_POOLNAME, timeList);
		//返回数据
		return returnVoList;
	}
	
	/**
	 * 获取所有状态操作员信息
	 * @param sysuserID
	 * @param depId
	 * @return
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
