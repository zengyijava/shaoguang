package com.montnets.emp.engine.vo.dao;

import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.engine.TableLfService;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.engine.vo.LfServiceVo;

/**
 * @project sinolife
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-31 上午11:50:04
 * @description
 */

public class GenericLfServiceVoSQL
{

	// LfServicelVoDAO

	//获取查询字段
	public static String getFieldSql()
	{
		//查询字段
		String sql = new StringBuffer("select lfservice.").append(TableLfService.SER_ID)
				.append(",lfservice.").append(TableLfService.SER_TYPE)
				.append(",lfservice.").append(TableLfService.SER_NAME)
				.append(",lfservice.").append(TableLfService.USER_ID)
				.append(",lfservice.").append(TableLfService.OWNER_ID)
				.append(",lfservice.").append(TableLfService.RUN_STATE)
				.append(",lfservice.").append(TableLfService.MENU_CODE)
				.append(",lfservice.").append(TableLfService.COMMENTS)
				.append(",lfservice.").append(TableLfService.ORDER_CODE)
				.append(",lfservice.").append(TableLfService.CREATE_TIME)
				.append(",lfservice.").append(TableLfService.IDENTIFY_MODE)
				.append(",lfservice.").append(TableLfService.CORP_CODE)
				.append(",sysuser1.").append(TableLfSysuser.NAME)
				.append(",sysuser1.").append(TableLfSysuser.USER_NAME)
				.append(",sysuser2.").append(TableLfSysuser.NAME).append(" OWNER_NAME ")
				.append(",sysuser2.").append(TableLfSysuser.USER_NAME).append(" OWNERUSER_NAME ")
				.append(",sysuser1.").append(TableLfSysuser.USER_STATE).toString();
		//返回sql
		return sql;
	}

	//获取表
	public static String getTableSql()
	{
		//查询表
		String sql = new StringBuffer(" from ").append(
				TableLfService.TABLE_NAME).append(" lfservice inner join ")
				.append(TableLfSysuser.TABLE_NAME).append(
						" sysuser1 on sysuser1.")
				.append(TableLfSysuser.USER_ID).append("=lfservice.").append(
						TableLfService.USER_ID).append(" left join ").append(
						TableLfSysuser.TABLE_NAME).append(
						" sysuser2 on sysuser2.")
				.append(TableLfSysuser.USER_ID).append("=lfservice.").append(
						TableLfService.OWNER_ID).toString();
		//返回sql
		return sql;
	}

	//获取权限
	public static String getDominationSql(String userId)
	{
		//权限
		StringBuffer dominationSql = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userId);
		//权限sql
		String sql = new StringBuffer(" where (sysuser1.").append(
				TableLfSysuser.USER_ID).append("=").append(userId).append(
				" or sysuser1.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(dominationSql).append(")").append(" or sysuser2.")
				.append(TableLfSysuser.USER_ID).append("=").append(userId)
				.append(")")

				.toString();
		//返回sql
		return sql;
	}

	/**
	 * LfServiceVoDAO
	 * 
	 * @param LfServiceVo
	 * @return
	 */
	public static String getConditionSql(LfServiceVo lfServiceVo)
	{
		//查询条件sql
		StringBuffer conditionSql = new StringBuffer();
		conditionSql.append(" and lfservice.CORP_CODE='").append(lfServiceVo.getCorpCode()).append("'");
		
		//服务名
		if (lfServiceVo.getSerName() != null
				&& !"".equals(lfServiceVo.getSerName()))
		{
			conditionSql.append(" and lfservice.").append(
					TableLfService.SER_NAME).append(" like '%").append(
					lfServiceVo.getSerName()).append("%'");
		}
		// 状态
		if (lfServiceVo.getRunState() != null)
		{
			conditionSql.append(" and lfservice.").append(
					TableLfService.RUN_STATE).append("=").append(
					lfServiceVo.getRunState());
		}
		// menuCode
		if (lfServiceVo.getMenuCode() != null
				&& !"".equals(lfServiceVo.getMenuCode()))
		{
			conditionSql.append(" and lfservice.").append(
					TableLfService.MENU_CODE).append("='").append(
					lfServiceVo.getMenuCode()).append("'");
		}
		// 上行下行
		if (lfServiceVo.getSerType() != null)
		{
			conditionSql.append(" and lfservice.").append(
					TableLfService.SER_TYPE).append("=").append(
					lfServiceVo.getSerType());
		}

		// 创建者名称
		if (lfServiceVo.getName() != null && !"".equals(lfServiceVo.getName()))
		{
			conditionSql.append(" and sysuser1.").append(TableLfSysuser.NAME)
					.append(" like '%").append(lfServiceVo.getName()).append(
							"%'");
		}
		// 拥有者名称
		if (lfServiceVo.getOwnerName() != null
				&& !"".equals(lfServiceVo.getOwnerName()))
		{
			conditionSql.append(" and sysuser2.").append(TableLfSysuser.NAME)
					.append(" like '%").append(lfServiceVo.getOwnerName())
					.append("%'");
		}
		//指令
		if (lfServiceVo.getOrderCode() != null
				&& !"".equals(lfServiceVo.getOrderCode()))
		{
			conditionSql.append(" and lfservice.").append(
					TableLfService.ORDER_CODE).append(" like '%").append(
					lfServiceVo.getOrderCode().toLowerCase()).append("%'");
		}
		if (lfServiceVo.getIdentifyMode() != null)
		{
			conditionSql.append(" and lfservice.").append(
					TableLfService.IDENTIFY_MODE).append(" = ").append(
					lfServiceVo.getIdentifyMode());
		}
		
		//组装sql
		String sql = conditionSql.toString();
		//返回结果
		return sql;
	}

	//排序
	public static String getOrderBySql()
	{
		//排序sql
		String sql = new StringBuffer(" order by lfservice.").append(
				TableLfService.CREATE_TIME).append(" desc").toString();
		//返回sql
		return sql;
	}

}
