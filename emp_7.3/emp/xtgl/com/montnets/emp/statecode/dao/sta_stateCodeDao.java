package com.montnets.emp.statecode.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.statecode.LfStateCode;
import com.montnets.emp.util.PageInfo;

/**
 *<p>project name p_xtgl</p>
 *<p>Title: sta_stateCodeDao</p>
 *<p>Description: </p>
 *<p>Company: Montnets Technology CO.,LTD.</p>
 * @author pengj
 * @date 2016-1-15 上午10:00:05
 */
public class sta_stateCodeDao extends SuperDAO
{
	/**
	 * FunName:条件查询业务集合（带分页）
	 * Description:
	 * @param  LfStateCode 查询条件 、pageInfo 分页信息
	 * @retuen List<LfBusManagerVo>
	 */
	public List<LfStateCode> findLfStateCode(LfStateCode lfStateCode, PageInfo pageInfo)throws Exception
	{
		//查询字段拼接
		String fieldSql = sta_stateCodeSql.getFieldSql();
		//查询表拼接
		String tableSql = sta_stateCodeSql.getTableSql();
		//组装过滤条件
		String conditionSql = sta_stateCodeSql.getConditionSql(lfStateCode);
		
		conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		
		//排序条件拼接
		String orderBySql = sta_stateCodeSql.getOrderBySql();
		String sql = new StringBuffer(fieldSql).append(tableSql)
				.append(conditionSql).append(orderBySql)
				.toString();
		//组装统计SQL语句
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql)
				.toString();
//		System.out.println(sql);
		//调用查询语句
		List<LfStateCode> lfStateCodeList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LfStateCode.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		return lfStateCodeList;
	}
	
	
	/**
	 * 
	 * Description:按条件查询出要导出的状态码数据信息
	 * @param  LfStateCode 查询条件
	 * @retuen List<LfStateCode>   符合条件的状态码信息
	 */
	public List<LfStateCode> findLfStateCode(LfStateCode lfStateCode)throws Exception
	{
		// 查询字段拼接
		String fieldSql = sta_stateCodeSql.getFieldSql();
		// 查询表拼接
		String tableSql = sta_stateCodeSql.getTableSql();
		// 组装过滤条件
		String conditionSql = sta_stateCodeSql.getConditionSql(lfStateCode);

		conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		// 排序条件拼接
		String orderBySql = sta_stateCodeSql.getOrderBySql();
		String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).append(orderBySql).toString();
		// 调用查询方法
//		List<LfStateCode> lfMttaskVoList = findVoListBySQL(LfStateCode.class, sql, StaticValue.EMP_POOLNAME);
		List<LfStateCode> lfMttaskVoList = findEntityListBySQL(LfStateCode.class, sql, StaticValue.EMP_POOLNAME);

		return lfMttaskVoList;
	}

}
