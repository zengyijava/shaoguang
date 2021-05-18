package com.montnets.emp.engine.vo.dao;

import java.util.List;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.engine.vo.LfServiceVo;

/**
 * @project sinolife
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-31 上午11:42:18
 * @description
 */

public class GenericLfServiceVoDAO  extends EngineSuperDAO
{

	public List<LfServiceVo> findLfServiceVo(LfServiceVo lfServiceVo,
			Long userId, PageInfo pageInfo) throws Exception
	{
		//查询字段
		String fieldSql = GenericLfServiceVoSQL.getFieldSql();
		//查询表
		String tableSql = GenericLfServiceVoSQL.getTableSql();
		//权限
		String dominationSql = GenericLfServiceVoSQL.getDominationSql(String
				.valueOf(userId));
		//查询条件
		String conditionSql = GenericLfServiceVoSQL
				.getConditionSql(lfServiceVo);
		//排序
		String orderBySql = GenericLfServiceVoSQL.getOrderBySql();
		//组装sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//分页
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(dominationSql).append(conditionSql)
				.toString();
		//执行查询并返回结果
		List<LfServiceVo> returnVoList =findPageVoListBySQL(
						LfServiceVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		//返回结果
		return returnVoList;
	}
}
