package com.montnets.emp.client.dao;

import java.util.List;

import com.montnets.emp.client.vo.CustFieldValueVo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @project emp
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime
 * @description
 */
public class GenericCustFieldVoDAO extends AddrBookSuperDAO
{
	/***
	 * 通过客户属性值条件查询相关信息
	 */
	public List<CustFieldValueVo> findCustInfoVo(
			CustFieldValueVo custFieldValueVo, PageInfo pageInfo)
			throws Exception
	{
		//查询字段
		String fieldSql = GenericCustFieldVoSQL.getFieldSql();
		//查询表
		String tableSql = GenericCustFieldVoSQL.getTableSql();
		//查询条件
		String conditionSql = GenericCustFieldVoSQL
				.getConditionSql(custFieldValueVo);
		//排序
		String orderBySql = GenericCustFieldVoSQL.getOrderBySql();
		//查询sql
		String sql = "";
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
		{
			// 适用ORACEL数据库的SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).append(orderBySql).toString();
		} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
		{
			// 适用SQLSERVER2005数据库的SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).toString();
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			// 适用MYSQL数据库的SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).toString();
		} else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			// 适用DB2数据库的SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).toString();
		}
		//分页sql
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql).toString();
		//执行查询
		List<CustFieldValueVo> returnVoList = findPageVoListBySQL(
				CustFieldValueVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		//返回结果
		return returnVoList;
	}

	/**
	 * 
	 * 通过通讯录分组条件查询通讯录分组信息
	 */
	public List<GroupInfoVo> findGroupInfoVo(GroupInfoVo groupInfoVo)
			throws Exception
	{
		//查询字段
		String fieldSql = GenericClientGroupInfoVoSQL.getFieldSql();
		//查询表格
		String tableSql = GenericClientGroupInfoVoSQL.getTableSql();
		//条件
		String conditionSql = GenericClientGroupInfoVoSQL
				.getConditionSql(groupInfoVo);
		//排序
		String orderBySql = GenericClientGroupInfoVoSQL.getOrderBySql();
		//查询sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		//执行查询
		List<GroupInfoVo> returnVoList = findVoListBySQL(GroupInfoVo.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回结果
		return returnVoList;
	}

	/**
	 * 
	 * 通过用户id与通讯录分组条件查询通讯录分组信息
	 */
	public List<GroupInfoVo> findLikeGroupInfoVo(Long loginUserID,
			GroupInfoVo groupInfoVo, PageInfo pageInfo) throws Exception
	{
		//查询字段
		String fieldSql = GenericClientGroupInfoVoSQL.getFieldSql();
		//查询表格
		String tableSql = GenericClientGroupInfoVoSQL.getTableSql();
		//条件
		String conditionSql = GenericClientGroupInfoVoSQL
				.getConditionSql1(groupInfoVo);
		//排序
		String orderBySql = GenericClientGroupInfoVoSQL.getOrderBySql();
		//查询sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		//分页sql
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql).toString();
		//执行查询
		List<GroupInfoVo> returnVoList = findPageVoListBySQL(GroupInfoVo.class,
				sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//返回结果
		return returnVoList;
	}

	/**
	 * 
	 * 通过用户id与通讯录分组条件查询通讯录分组信息
	 */
	public List<GroupInfoVo> findGroupInfoVo(Long loginUserID,
			GroupInfoVo groupInfoVo) throws Exception
	{
		//查询字段
		String fieldSql = GenericClientGroupInfoVoSQL.getFieldSql();
		//查询表格
		String tableSql = GenericClientGroupInfoVoSQL.getTableSql();
		//条件
		String conditionSql = GenericClientGroupInfoVoSQL
				.getConditionSql(groupInfoVo);
		//排序
		String orderBySql = GenericClientGroupInfoVoSQL.getOrderBySql();
		//查询sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		//执行查询
		List<GroupInfoVo> returnVoList = findVoListBySQL(GroupInfoVo.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回结果
		return returnVoList;
	}

}
