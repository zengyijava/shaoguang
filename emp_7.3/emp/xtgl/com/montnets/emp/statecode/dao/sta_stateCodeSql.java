package com.montnets.emp.statecode.dao;


import com.montnets.emp.entity.statecode.LfStateCode;
import com.montnets.emp.table.biztype.TableLfBusManager;
import com.montnets.emp.table.statecode.TableLfStateCode;

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
public class sta_stateCodeSql
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
		String sql = new StringBuffer("select statecode.").append(
				TableLfStateCode.STATE_ID).append(",statecode.").append(
				TableLfStateCode.STATE_CODE).append(",statecode.").append(
				TableLfStateCode.MAPPING_CODE).append(",statecode.").append(
				TableLfStateCode.STATE_DES).append(",statecode.")
				.append(TableLfStateCode.CORP_CODE).append(",statecode.")
				.append(TableLfStateCode.CREATE_TIME).append(",statecode.")
				.append(TableLfStateCode.UPDATE_TIME).append(",statecode.")
				.append(TableLfStateCode.USER_ID).toString();
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
				TableLfStateCode.TABLE_NAME).append(" statecode ").toString();
		return sql;
	}
	
	/**
	 * FunName:组装排序条件
	 * Description:
	 * @param 
	 * @retuen String
	 */
	public static String getOrderBySql()
	{
		String sql = new StringBuffer(" order by statecode.").append(
				TableLfStateCode.CREATE_TIME).append(" desc").toString();
		return sql;
	}
	
	/**
	 * FunName:组装过滤条件
	 * Description:
	 * @param 
	 * @retuen String
	 */
	public static String getConditionSql(LfStateCode LfStateCode)
	{
		StringBuffer conditionSql = new StringBuffer();
		//状态码
		if (LfStateCode.getStateCode() != null && !"".equals(LfStateCode.getStateCode()))
		{
			conditionSql.append(" and statecode.").append(TableLfStateCode.STATE_CODE)
					.append(" like '%").append(LfStateCode.getStateCode()).append("%'");
		}
		//映射码
		if (LfStateCode.getMappingCode() != null && !"".equals(LfStateCode.getMappingCode()))
		{
			conditionSql.append(" and statecode.").append(TableLfStateCode.MAPPING_CODE)
					.append(" like '%").append(LfStateCode.getMappingCode()).append("%'");
		}
		//状态码描述
		if (LfStateCode.getStateDes() != null && !"".equals(LfStateCode.getStateDes()))
		{
			conditionSql.append(" and statecode.").append(TableLfStateCode.STATE_DES)
			.append(" like '%").append(LfStateCode.getStateDes()).append("%'");
		}
		
		//企业编码
		if (LfStateCode.getCorpCode() != null && !"".equals(LfStateCode.getCorpCode()))
		{
			conditionSql.append(" and statecode.").append(TableLfBusManager.CORP_CODE)
			.append(" in ('0','").append(LfStateCode.getCorpCode()).append("')");
		}
		
		String sql = conditionSql.toString();
		return sql;
	}
}
