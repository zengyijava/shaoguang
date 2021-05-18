package com.montnets.emp.client.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.table.sysuser.TableLfSysuser;

/**
 * 
*    
* 子项目名称：客户通讯录   
* 类名称：GenericClientGroupInfoVoSQL   
* 类描述：   拼装sql语句
* 创建时间：2015-8-31 上午09:26:33    
* 修改时间：2015-8-31 上午09:26:33   
* 修改备注：   
* @version    
*
 */
public class GenericClientGroupInfoVoSQL
{
	/***
	 * 处理sql语句中field段拼接
	* @Description: TODO
	* @param @return
	* @return String
	 */
	public static String getFieldSql()
	{
		String sql ="";
	 	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
	 		 sql = new StringBuffer("select list2gro.").append(
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
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			 sql = new StringBuffer("select list2gro.").append(
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
						.append(TableLfUdgroup.UDG_NAME).append(",ROW_NUMBER() Over(Order By lfsysuser.name) As rn ").toString();
			
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = new StringBuffer("select list2gro.").append(
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
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = new StringBuffer("select list2gro.").append(
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
		}
	 	
	
		return sql;
	}
	
	/***
	 *  获得sql语句中table段部分
	* @Description: TODO
	* @param @return
	* @return String  table段部分
	 */
	public static String getTableSql()
	{
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
				.append("=udgroup.").append(TableLfUdgroup.UDG_ID).toString();
		return tableSql;
	}

	/***
	 *  处理查询sql中的条件
	* @Description: TODO
	* @param @param groupInfoVo
	* @param @return
	* @return String  sql语句
	 */
	public static String getConditionSql(GroupInfoVo groupInfoVo)
	{
		
		StringBuffer conditionSql = new StringBuffer();
		if (groupInfoVo.getUdgId() != null)
		{
			conditionSql.append(" and list2gro.")
					.append(TableLfList2gro.UDG_ID).append("=").append(
							groupInfoVo.getUdgId());
		}
		
		if (groupInfoVo.getL2gType() != null)
		{
			conditionSql.append(" and list2gro.").append(
					TableLfList2gro.L2G_TYPE).append("=").append(
					groupInfoVo.getL2gType());
		}
		
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
		
		if (groupInfoVo.getUserId() != null)
		{
			conditionSql.append(" and udgroup.").append(TableLfUdgroup.USER_ID)
					.append("=").append(groupInfoVo.getUserId());
		}

		
		if (groupInfoVo.getGpAttribute() != null)
		{
			conditionSql.append(" and udgroup.").append(
					TableLfUdgroup.GP_ATTRIBUTE).append(" = ").append(
					groupInfoVo.getGpAttribute());
		}
		
		if(groupInfoVo.getGroupType()!=null){
			conditionSql.append(" and udgroup.").append(TableLfUdgroup.GROUP_TYPE).append("=").append(groupInfoVo.getGroupType());
		}
		
		String condition="";
		//存在查询条件
		if(conditionSql != null && conditionSql.length() > 0)
		{
			//将条件字符串首个and替换为where,
			condition = conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		return condition;
	}
	
	/***
	 *  处理查询sql中的条件
	* @Description: TODO
	* @param @param groupInfoVo
	* @param @return
	* @return String  sql语句
	 */
	public static String getConditionSql1(GroupInfoVo groupInfoVo)
	{
		
		StringBuffer conditionSql = new StringBuffer();
		if (groupInfoVo.getUdgId() != null)
		{
			conditionSql.append(" and list2gro.")
					.append(TableLfList2gro.UDG_ID).append("=").append(
							groupInfoVo.getUdgId());
		}
		
		if (groupInfoVo.getL2gType() != null)
		{
			conditionSql.append(" and list2gro.").append(
					TableLfList2gro.L2G_TYPE).append("=").append(
					groupInfoVo.getL2gType());
		}
		
		if (groupInfoVo.getName() != null && !"".equals(groupInfoVo.getName()))
		{
			
			conditionSql.append(" and ((case(list2gro.").append(
					TableLfList2gro.L2G_TYPE).append(") when 0 then employee.")
					.append(TableLfEmployee.NAME)
					.append(" when 1 then client.").append(TableLfClient.NAME)
					.append(" when 2 then malist.").append(TableLfMalist.NAME)
					.append(" else lfsysuser.").append(TableLfSysuser.NAME)
					.append(" end) like '").append(groupInfoVo.getName())
					.append("%'");

			
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
		
		if (groupInfoVo.getUserId() != null)
		{
			conditionSql.append(" and udgroup.").append(TableLfUdgroup.USER_ID)
					.append("=").append(groupInfoVo.getUserId());
		}
		String condition="";
		//存在查询条件
		if(conditionSql != null && conditionSql.length() > 0)
		{
			//将条件字符串首个and替换为where
			condition = conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		return condition;
	}

	/**
	 * 排序处理
	 * @return
	 */
	public static String getOrderBySql()
	{
		String sql = new StringBuffer(" order by NAME asc").toString();
		return sql;
	}
}
