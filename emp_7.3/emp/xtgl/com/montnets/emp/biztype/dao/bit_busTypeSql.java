package com.montnets.emp.biztype.dao;

import com.montnets.emp.biztype.vo.LfBusManagerVo;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.biztype.TableLfBusManager;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;

/**
 *<p>
 * project name p_xtgl
 * </p>
 *<p>
 * Title: bit_busTypeSql
 * </p>
 *<p>
 * Description:
 * </p>
 *<p>
 * Company: Montnets Technology CO.,LTD.
 * </p>
 * 
 * @author dingzx
 * @date 2015-1-15下午04:07:59
 */
public class bit_busTypeSql
{

	/**
	 * FunName:组装查询字段 Description:
	 * 
	 * @param
	 * @retuen String
	 */
	public static String getFieldSql()
	{
		// 拼接sql
		String sql = new StringBuffer("select busmanager.").append(
				TableLfBusManager.BUS_ID).append(",busmanager.").append(
				TableLfBusManager.BUS_CODE).append(",busmanager.").append(
				TableLfBusManager.BUS_NAME).append(",busmanager.").append(
				TableLfBusManager.BUS_DESCRIPTION).append(",busmanager.")
				.append(TableLfBusManager.CORP_CODE).append(",busmanager.")
				.append(TableLfBusManager.CREATE_TIME).append(",busmanager.")
				.append(TableLfBusManager.UPDATE_TIME).append(",busmanager.")
				.append(TableLfBusManager.USER_ID).append(",busmanager.")
				.append(TableLfBusManager.STATE).append(",busmanager.")
				.append(TableLfBusManager.BUS_TYPE).append(",busmanager.")
				.append(TableLfBusManager.RISELEVEL).append(",sysuser.")
				.append(TableLfSysuser.NAME).append(",sysuser.").append(
						TableLfSysuser.USER_NAME).append(",dep.").append(
						TableLfDep.DEP_NAME).toString();
		return sql;
	}

	/**
	 * FunName:组装表拼接
	 * Description:
	 * @param 
	 * @retuen String
	 */
	public static String getTableSql()
	{
		String sql = new StringBuffer(" from ").append(
				TableLfBusManager.TABLE_NAME).append(" busmanager left join ")
				.append(TableLfSysuser.TABLE_NAME).append(
						" sysuser on busmanager.").append(
						TableLfBusManager.USER_ID).append("=sysuser.").append(
						TableLfSysuser.USER_ID).append(" left join ").append(
						TableLfDep.TABLE_NAME).append(" dep on busmanager.")
				.append(TableLfBusManager.DEP_ID).append("=dep.").append(
						TableLfDep.DEP_ID).toString();
		return sql;
	}
	
	/**
	 * FunName:组装排序条件
	 * Description:
	 * @param 
	 * @retuen String
	 */
	public static String getOrderBySql() {
		return " order by busmanager." + TableLfBusManager.CREATE_TIME + " desc," + TableLfBusManager.BUS_ID;
	}
	
	/**
	 * FunName:组装过滤条件
	 * Description:
	 * @param 
	 * @retuen String
	 */
	public static String getConditionSql(LfBusManagerVo lfBusManagerVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		//业务名称
		if (lfBusManagerVo.getBusName() != null && !"".equals(lfBusManagerVo.getBusName()))
		{
			conditionSql.append(" and busmanager.").append(TableLfBusManager.BUS_NAME)
					.append(" like '%").append(lfBusManagerVo.getBusName()).append("%'");
		}
		//业务编号
		if (lfBusManagerVo.getBusCode() != null && !"".equals(lfBusManagerVo.getBusCode()))
		{
			conditionSql.append(" and busmanager.").append(TableLfBusManager.BUS_CODE)
			.append(" like '%").append(lfBusManagerVo.getBusCode()).append("%'");
		}
		//状态
		if (lfBusManagerVo.getState() != null)
		{
			conditionSql.append(" and busmanager.").append(TableLfBusManager.STATE)
			.append("=").append(lfBusManagerVo.getState());
		}
		//企业编码
		if (lfBusManagerVo.getCorpCode() != null && !"".equals(lfBusManagerVo.getCorpCode()))
		{
			conditionSql.append(" and busmanager.").append(TableLfBusManager.CORP_CODE)
			.append(" in ('0','").append(lfBusManagerVo.getCorpCode()).append("')");
		}
		//优先级
		if (lfBusManagerVo.getRiseLevel() != null)
		{
			conditionSql.append(" and busmanager.").append(TableLfBusManager.RISELEVEL)
			.append("=").append(lfBusManagerVo.getRiseLevel());
		}
		//业务类型
		if (lfBusManagerVo.getBusType() != null)
		{
			conditionSql.append(" and busmanager.").append(TableLfBusManager.BUS_TYPE)
			.append("=").append(lfBusManagerVo.getBusType());
		}
		//操作员
		if (lfBusManagerVo.getName() != null && !"".equals(lfBusManagerVo.getName()))
		{
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME)
			.append(" like '%").append(lfBusManagerVo.getName()).append("%'");
		}
		//操作员id
//		if (lfBusManagerVo.getUserId() != null && !"".equals(lfBusManagerVo.getUserId()))
//		{
//			conditionSql.append(" and sysuser.").append(TableLfSysuser.USER_ID)
//			.append("=").append(lfBusManagerVo.getUserId());
//		}
		//登录操作员机构id
//		if (lfBusManagerVo.getDepId() != null
//				&& !"".equals(lfBusManagerVo.getDepId()))
//		{
//			conditionSql.append(" and busmanager.").append(lfBusManagerVo.getDepIds());
//		}
		//机构查询条件
//		if (lfBusManagerVo.getDepIds() != null
//				&& !"".equals(lfBusManagerVo.getDepIds()))
//		{
//			conditionSql.append(" and busmanager.").append(lfBusManagerVo.getDepIds());
//		}
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		//开始查询时间
		if (lfBusManagerVo.getStartSubmitTime() != null
				&& !"".equals(lfBusManagerVo.getStartSubmitTime()))
		{
			conditionSql.append(" and busmanager.").append(
					TableLfBusManager.CREATE_TIME).append(">=").append(genericDao.getTimeCondition(lfBusManagerVo.getStartSubmitTime()));
		}
		//结束查询时间
		if (lfBusManagerVo.getEndSubmitTime() != null
				&& !"".equals(lfBusManagerVo.getEndSubmitTime()))
		{
			conditionSql.append(" and busmanager.").append(
					TableLfBusManager.CREATE_TIME).append("<=").append(genericDao.getTimeCondition(lfBusManagerVo.getEndSubmitTime()));
		}
		String sql = conditionSql.toString();
		return sql;
	}
}
