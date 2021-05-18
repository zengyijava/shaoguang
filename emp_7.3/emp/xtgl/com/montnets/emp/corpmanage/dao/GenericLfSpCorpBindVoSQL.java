package com.montnets.emp.corpmanage.dao;

import com.montnets.emp.corpmanage.vo.LfSpCorpBindVo;
import com.montnets.emp.table.corp.TableLfCorp;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;


/**
 * 
 * @project sinolife
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-19 下午07:44:52
 * @description
 */
public class GenericLfSpCorpBindVoSQL
{
	/**
	 * 获取查询字段
	 * @return
	 */
	public static String getFieldSql()
	{
		//拼接查询字段
		String sql = new StringBuffer("select ")
				.append("lfspdepbind.").append(TableLfSpDepBind.SP_USER)
				.append(",lfspdepbind.").append(TableLfSpDepBind.CORP_CODE)
				.append(",lfspdepbind.").append(TableLfSpDepBind.PLATFORM_TYPE)
				.append(",lfcorp.").append(TableLfCorp.CORP_ID)
				.append(",lfcorp.").append(TableLfCorp.CORP_NAME)
				.append(",lfspdepbind.").append(TableLfSpDepBind.IS_VALIDATE)
				.toString();
		//返回查询字段字符串
		return sql;
	}
	/**
	 * 获取查询表名sql
	 * @return
	 */
	public static String getTableSql()
	{
		//拼接sql
		String sql = new StringBuffer(" from ")
				.append(TableLfSpDepBind.TABLE_NAME).append(" lfspdepbind inner join ")
				.append(TableLfCorp.TABLE_NAME).append(" lfcorp on lfcorp.")
				.append(TableLfCorp.CORP_CODE).append("=lfspdepbind.")
				.append(TableLfSpDepBind.CORP_CODE).append(" inner join ")
				.append(TableUserdata.TABLE_NAME).append(" userdata on userdata.").append(TableUserdata.USER_ID)
				.append("=lfspdepbind.").append(TableLfSpDepBind.SP_USER)
				.append(" where userdata.").append(TableUserdata.ACCOUNTTYPE).append("=1  ").toString();
		//返回查询表名字符串
		return sql;
	}

	
	/**
	 *获取查询条件sql
	 * @param lfSpCorpBindVo
	 * @return
	 */
	public static String getConditionSql(LfSpCorpBindVo lfSpCorpBindVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		//查询条件-----企业编码
		if (lfSpCorpBindVo.getCorpCode() != null
				&& !"".equals(lfSpCorpBindVo.getCorpCode()))
		{
			conditionSql.append(" and lfspdepbind.").append(
					TableLfSpDepBind.CORP_CODE).append(" like '%").append(
							lfSpCorpBindVo.getCorpCode()).append("%' ");
		}
		
		//查询条件-----是否有效
		if (lfSpCorpBindVo.getIsValidate() != null)
		{
			conditionSql.append(" and lfspdepbind.").append(
					TableLfSpDepBind.IS_VALIDATE).append("=").append(
					lfSpCorpBindVo.getIsValidate());
		}
		//查询条件-----企业名称
		if (lfSpCorpBindVo.getCorpName() != null
				&& !"".equals(lfSpCorpBindVo.getCorpName()))
		{
			conditionSql.append(" and lfcorp.").append(
					TableLfCorp.CORP_NAME).append(" like '%").append(
							lfSpCorpBindVo.getCorpName()).append("%' ");
		}
		
		//查询条件-----
		if (lfSpCorpBindVo.getPlatFormType() != null)
		{
			conditionSql.append(" and lfspdepbind.").append(
					TableLfSpDepBind.PLATFORM_TYPE).append(" = ").append(
							lfSpCorpBindVo.getPlatFormType());
		}
		//查询条件-----发送账号
		if (lfSpCorpBindVo.getSpUser() != null
				&& !"".equals(lfSpCorpBindVo.getSpUser()))
		{
			conditionSql.append(" and lfspdepbind.").append(
					TableLfSpDepBind.SP_USER).append("='").append(
							lfSpCorpBindVo.getSpUser()).append("'");
		}
		String sql = conditionSql.toString();
		//返回拼接sql
		return sql;
	}
	
	/**
	 * 获取排序sql
	 * @return
	 */
	public static String getOrderBySql()
	{
		//拼接sql
		String sql = new StringBuffer(" order by lfcorp.").append(
				TableLfCorp.CORP_CODE).append(" asc").toString();
		return sql;
	}
}
