package com.montnets.emp.tempflow.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.tempflow.vo.LfFlowRecordTemplateVo;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @project montnets_dao
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-16 下午02:12:34
 * @description
 */
public class GenericLfFlowRecordTemplateVoDAO extends SuperDAO
{

	/**
	 * 短信模板审批流信息
	 * @param loginUserID
	 * @param lfFlowRecordTemplateVo
	 * @param pageInfo
	 * @return lfFlowRecordVoList
	 * @throws Exception
	 */
	public List<LfFlowRecordTemplateVo> findLfFlowRecordTemplateVo(
			Long loginUserID, LfFlowRecordTemplateVo lfFlowRecordTemplateVo,
			PageInfo pageInfo) throws Exception
	{
		//字段
		String fieldSql = GenericLfFlowRecordTemplateVoSQL.getFieldSql();
		//表名
		String tableSql = GenericLfFlowRecordTemplateVoSQL.getTableSql();
		//管辖范围
		String dominationSql = GenericLfFlowRecordTemplateVoSQL
				.getDominationSql(loginUserID);
		//条件
		String conditionSql = GenericLfFlowRecordTemplateVoSQL
				.getConditionSql(lfFlowRecordTemplateVo);
		//排序
		String orderBySql = GenericLfFlowRecordTemplateVoSQL.getOrderBySql();
		//查询sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//分页sql
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(dominationSql).append(conditionSql)
				.toString();
		//调用查询方法
		List<LfFlowRecordTemplateVo> lfFlowRecordVoList =new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(LfFlowRecordTemplateVo.class, sql,
						countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//返回查询结果
		return lfFlowRecordVoList;
	}

	/**
	 * 短信模板审批流信息
	 * @param loginUserID
	 * @param LfFlowRecordTemplateVo
	 * @return returnVoList
	 * @throws Exception
	 */
	public List<LfFlowRecordTemplateVo> findLfFlowRecordTemplateVo(
			Long loginUserID, LfFlowRecordTemplateVo LfFlowRecordTemplateVo)
			throws Exception
	{
		//字段
		String fieldSql = GenericLfFlowRecordTemplateVoSQL.getFieldSql();
		//表名
		String tableSql = GenericLfFlowRecordTemplateVoSQL.getTableSql();
		//管辖范围
		String dominationSql = GenericLfFlowRecordTemplateVoSQL
				.getDominationSql(loginUserID);
		//条件
		String conditionSql = GenericLfFlowRecordTemplateVoSQL
				.getConditionSql(LfFlowRecordTemplateVo);
		//排序
		String orderBySql = GenericLfFlowRecordTemplateVoSQL.getOrderBySql();
		//查询sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//调用查询方法
		List<LfFlowRecordTemplateVo> returnVoList = findVoListBySQL(
				LfFlowRecordTemplateVo.class, sql, StaticValue.EMP_POOLNAME);
		//返回查询结果
		return returnVoList;
	}

}
