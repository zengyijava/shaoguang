package com.montnets.emp.clientsms.dao;

import com.montnets.emp.common.vo.LfCustFieldValueVo;
import com.montnets.emp.table.client.TableLfCustField;
import com.montnets.emp.table.client.TableLfCustFieldValue;

public class LfClientSmsVoSQL
{
	/**
	 * 查询字段拼接
	 * 
	 * @return
	 */
	public static String getFieldSql()
	{
		// 拼接sql
		String sql = new StringBuffer("select custFieldValue.*,custField.").append(TableLfCustField.FIELD_REF).append(",custField.").append(TableLfCustField.FIELD_NAME).append(",custField.").append(TableLfCustField.V_TYPE).append(",custField.").append(TableLfCustField.CORP_CODE).append(",custField.").append(TableLfCustField.USERID).toString();
		// 返回结果
		return sql;
	}

	/**
	 * 查询表名拼接
	 * 
	 * @return
	 */
	public static String getTableSql()
	{
		// 拼接sql
		String tableSql = new StringBuffer(" from ").append(TableLfCustField.TABLE_NAME).append(" custField inner join ").append(TableLfCustFieldValue.TABLE_NAME).append(" custFieldValue on custFieldValue.").append(TableLfCustFieldValue.FIELD_ID).append(" = custField.").append(TableLfCustField.ID).toString();
		return tableSql;
	}

	/**
	 * 查询条件拼接
	 * 
	 * @param lfCustFieldValueVo
	 * @return
	 */
	public static String getConditionSql(LfCustFieldValueVo lfCustFieldValueVo)
	{

		StringBuffer conditionSql = new StringBuffer();
		// 查询条件----企业编码
		if(lfCustFieldValueVo.getCorp_code() != null && !"".equals(lfCustFieldValueVo.getCorp_code()))
		{
			conditionSql.append(" and custField.").append(TableLfCustField.CORP_CODE).append("='").append(lfCustFieldValueVo.getCorp_code()).append("'");
		}
		if(lfCustFieldValueVo.getField_ID() != null && !"".equals(lfCustFieldValueVo.getField_ID()))
		{
			/*
			 * conditionSql.append(" and custField.").append(
			 * TableLfCustField.ID).append("='").append(
			 * lfCustFieldValueVo.getField_ID()).append("'");
			 */
			conditionSql.append(" and custField.").append(TableLfCustField.ID).append("=").append(lfCustFieldValueVo.getField_ID());

		}
		if(lfCustFieldValueVo.getField_Value() != null && !"".equals(lfCustFieldValueVo.getField_Value()))
		{
			conditionSql.append(" and custFieldValue.").append(TableLfCustFieldValue.FIELD_VALUE).append(" like '%").append(lfCustFieldValueVo.getField_Value()).append("%'");
		}
		return conditionSql.toString();
	}

	/**
	 * 排序条件拼接
	 * 
	 * @return
	 */
	public static String getOrderBySql()
	{
		String sql = new StringBuffer(" order by custFieldValue.field_ID asc").toString();
		return sql;
	}
}
