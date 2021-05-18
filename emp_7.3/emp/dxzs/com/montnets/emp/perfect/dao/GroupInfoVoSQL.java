package com.montnets.emp.perfect.dao;

import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.table.sysuser.TableLfSysuser;



public class GroupInfoVoSQL
{
	/**
	 * 查询字段拼接
	 * @return
	 */
	public static String getFieldSql()
	{
		//拼接sql
		String sql = new StringBuffer("select list2gro.").append(
				TableLfList2gro.UDG_ID).append(",list2gro.").append(
				TableLfList2gro.L2G_ID).append(",list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(",list2gro.").append(
				TableLfList2gro.GUID).append(",udgroup.").append(
				TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(
				TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(
				TableLfUdgroup.USER_ID).append(",case(list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(") when 0 then employee.")
				.append(TableLfEmployee.MOBILE).append(" when 1 then client.")
				.append(TableLfClient.MOBILE).append(" when 2 then malist.")
				.append(TableLfMalist.MOBILE).append(" else lfsysuser.")
				.append(TableLfSysuser.MOBILE).append(
						" end as MOBILE,case(list2gro.").append(
						TableLfList2gro.L2G_TYPE).append(
						") when 0 then employee.").append(TableLfEmployee.NAME)
				.append(" when 1 then client.").append(TableLfMalist.NAME)
				.append(" when 2 then malist.").append(TableLfClient.NAME)
				.append(" else lfsysuser.").append(TableLfSysuser.NAME).append(
						" end as NAME,udgroup.")
				.append(TableLfUdgroup.UDG_NAME).toString();
		//返回结果
		return sql;
	}

	/**
	 * 查询表名拼接(此方法无使用,直接去掉1 =1)
	 * @return
	 */
	public static String getTableSql()
	{
		//拼接sql
		String tableSql = new StringBuffer(" from ").append(
				TableLfList2gro.TABLE_NAME).append(" list2gro left join ")
				.append(TableLfEmployee.TABLE_NAME).append(
						" employee on list2gro.").append(TableLfList2gro.GUID)
				.append("=employee.").append(TableLfEmployee.GUID).append(
						" left join ").append(TableLfClient.TABLE_NAME).append(
						" client on list2gro.").append(TableLfList2gro.GUID)
				.append("=client.").append(TableLfClient.GUID).append(
						" left join ").append(TableLfMalist.TABLE_NAME).append(
						" malist on list2gro.").append(TableLfList2gro.GUID)
				.append("=malist.").append(TableLfMalist.GUID).append(
						" left join ").append(TableLfSysuser.TABLE_NAME)
				.append(" lfsysuser on list2gro.").append(TableLfList2gro.GUID)
				.append("=lfsysuser.").append(TableLfSysuser.GUID).append(
						" inner join ").append(TableLfUdgroup.TABLE_NAME)
				.append(" udgroup on list2gro.").append(TableLfList2gro.UDG_ID)
				.append("=udgroup.").append(TableLfUdgroup.UDG_ID).append(
						" where ").toString();
		//返回结果
		return tableSql;
	}

	/**
	 * 查询条件拼接
	 * @param groupInfoVo
	 * @return
	 */
	public static String getConditionSql(GroupInfoVo groupInfoVo)
	{
		
		StringBuffer conditionSql = new StringBuffer();
		//查询条件----群组id
		if (groupInfoVo.getUdgId() != null)
		{
			conditionSql.append(" and list2gro.")
					.append(TableLfList2gro.UDG_ID).append("=").append(
							groupInfoVo.getUdgId());
		}
		//查询条件----群组类型
		if (groupInfoVo.getL2gType() != null)
		{
			conditionSql.append(" and list2gro.").append(
					TableLfList2gro.L2G_TYPE).append("=").append(
					groupInfoVo.getL2gType());
		}
		//查询条件----群组名称
		if (groupInfoVo.getName() != null && !"".equals(groupInfoVo.getName()))
		{
			conditionSql.append(" and (case(list2gro.").append(
					TableLfList2gro.L2G_TYPE).append(") when 0 then employee.")
					.append(TableLfEmployee.NAME)
					.append(" when 1 then client.").append(TableLfClient.NAME)
					.append(" when 2 then malist.").append(TableLfMalist.NAME)
					.append(" else lfsysuser.").append(TableLfSysuser.NAME)
					.append(" end) like '%").append(groupInfoVo.getName())
					.append("%'");
		}
		//查询条件---手机号码
		if (groupInfoVo.getMobile() != null
				&& !"".equals(groupInfoVo.getMobile()))
		{
			conditionSql.append(" and (case(list2gro.").append(
					TableLfList2gro.L2G_TYPE).append(") when 0 then employee.")
					.append(TableLfEmployee.MOBILE).append(
							" when 1 then client.")
					.append(TableLfClient.MOBILE)
					.append(" when 2 then malist.")
					.append(TableLfMalist.MOBILE).append(" else lfsysuser.")
					.append(TableLfSysuser.MOBILE).append(" end) like '%")
					.append(groupInfoVo.getMobile()).append("%'");
		}
		//查询条件---userid
		if (groupInfoVo.getUserId() != null)
		{
			conditionSql.append(" and udgroup.").append(TableLfUdgroup.USER_ID)
					.append("=").append(groupInfoVo.getUserId());
		}

		//查询条件---分组属性
		if (groupInfoVo.getGpAttribute() != null)
		{
			conditionSql.append(" and udgroup.").append(
					TableLfUdgroup.GP_ATTRIBUTE).append(" = ").append(
					groupInfoVo.getGpAttribute());
		}
		//查询条件---分组类型
		if(groupInfoVo.getGroupType()!=null){
			conditionSql.append(" and udgroup.").append(TableLfUdgroup.GROUP_TYPE).append("=").append(groupInfoVo.getGroupType());
		}
		//返回结果
		return conditionSql.toString();
	}

	/**
	 * 查询条件拼接
	 * @param groupInfoVo
	 * @return
	 */
	public static String getConditionSql1(GroupInfoVo groupInfoVo)
	{
		
		StringBuffer conditionSql = new StringBuffer();
		//查询条件---群组id
		if (groupInfoVo.getUdgId() != null)
		{
			conditionSql.append(" and list2gro.")
					.append(TableLfList2gro.UDG_ID).append("=").append(
							groupInfoVo.getUdgId());
		}
		//查询条件---群组类型
		if (groupInfoVo.getL2gType() != null)
		{
			conditionSql.append(" and list2gro.").append(
					TableLfList2gro.L2G_TYPE).append("=").append(
					groupInfoVo.getL2gType());
		}
		//查询条件---群组名称
		if (groupInfoVo.getName() != null && !"".equals(groupInfoVo.getName()))
		{
		//拼接sql	
			conditionSql.append(" and ((case(list2gro.").append(
					TableLfList2gro.L2G_TYPE).append(") when 0 then employee.")
					.append(TableLfEmployee.NAME)
					.append(" when 1 then client.").append(TableLfClient.NAME)
					.append(" when 2 then malist.").append(TableLfMalist.NAME)
					.append(" else lfsysuser.").append(TableLfSysuser.NAME)
					.append(" end) like '").append(groupInfoVo.getName())
					.append("%'");

			//拼接sql
			conditionSql.append(" or (case(list2gro.").append(
					TableLfList2gro.L2G_TYPE).append(") when 0 then employee.")
					.append(TableLfEmployee.MOBILE).append(
							" when 1 then client.")
					.append(TableLfClient.MOBILE)
					.append(" when 1 then malist.")
					.append(TableLfMalist.MOBILE).append(" else lfsysuser.")
					.append(TableLfSysuser.MOBILE).append(" end) like '")
					.append(groupInfoVo.getMobile()).append("%')");
		}
		//查询条件----userid
		if (groupInfoVo.getUserId() != null)
		{
			conditionSql.append(" and udgroup.").append(TableLfUdgroup.USER_ID)
					.append("=").append(groupInfoVo.getUserId());
		}
		//返回结果
		return conditionSql.toString();
	}

	/**
	 * 排序条件拼接
	 * @return
	 */
	public static String getOrderBySql()
	{
		String sql = new StringBuffer(" order by NAME asc").toString();
		return sql;
	}
}
