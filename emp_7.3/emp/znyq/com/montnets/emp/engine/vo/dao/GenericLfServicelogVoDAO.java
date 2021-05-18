package com.montnets.emp.engine.vo.dao;

import java.util.List;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.engine.vo.LfServicelogVo;

/**
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-9 下午09:08:10
 * @description
 */
public class GenericLfServicelogVoDAO extends EngineSuperDAO
{
	public List<LfServicelogVo> findLfServicelogVo(Long GL_UserID,
			LfServicelogVo lfServicelogVo, PageInfo pageInfo) throws Exception
	{
		//查询字段
		String fieldSql = GenericLfServicelogVoSQL.getFieldSql();
		//查询表
		String tableSql = GenericLfServicelogVoSQL.getTableSql();
		//权限
		String dominationSql = GenericLfServicelogVoSQL.getDominationSql(String
				.valueOf(GL_UserID));
		//条件
		String conditionSql = GenericLfServicelogVoSQL
				.getConditionSql(lfServicelogVo);
		List<String> timeList = GenericLfServicelogVoSQL
				.getTimeCondition(lfServicelogVo);
		//排序
		String orderBySql = GenericLfServicelogVoSQL.getOrderBySql();
		//组装完整sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//分页
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(dominationSql).append(conditionSql)
				.toString();
		//执行查询
		List<LfServicelogVo> returnVoList = findPageVoListBySQL(
						LfServicelogVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME, timeList);
		//返回结果
		return returnVoList;
	}

	public List<LfServicelogVo> findLfServicelogVo(Long GL_UserID,
			LfServicelogVo lfServicelogVo) throws Exception
	{
		//查询字段
		String fieldSql = GenericLfServicelogVoSQL.getFieldSql();
		//查询表
		String tableSql = GenericLfServicelogVoSQL.getTableSql();
		//权限
		String dominationSql = GenericLfServicelogVoSQL.getDominationSql(String
				.valueOf(GL_UserID));
		//条件
		String conditionSql = GenericLfServicelogVoSQL
				.getConditionSql(lfServicelogVo);
		List<String> timeList = GenericLfServicelogVoSQL
				.getTimeCondition(lfServicelogVo);
		//排序
		String orderBySql = GenericLfServicelogVoSQL.getOrderBySql();
		//组装完整sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//执行查询
		List<LfServicelogVo> returnVoList = findVoListBySQL(
				LfServicelogVo.class, sql, StaticValue.EMP_POOLNAME, timeList);
		//返回结果
		return returnVoList;
	}

}
