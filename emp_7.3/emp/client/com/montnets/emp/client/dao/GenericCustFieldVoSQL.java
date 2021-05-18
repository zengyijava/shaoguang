package com.montnets.emp.client.dao;

import com.montnets.emp.client.vo.CustFieldValueVo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.client.TableLfCustField;
import com.montnets.emp.table.client.TableLfCustFieldValue;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.table.sysuser.TableLfSysuser;

/***
 * 
*    
* 项目名称：客户通讯录   
* 类名称：GenericCustFieldVoSQL   
* 类描述：     拼装sql语句
* 创建时间：2015-8-31 上午09:29:33   
* 修改时间：2015-8-31 上午09:29:33   
* 修改备注：   
* @version    
*
 */
public class GenericCustFieldVoSQL
{
	
	/***
	 * 处理sql语句中field段拼接
	* @Description: TODO
	* @param @return
	* @return String
	 */
	public static String getFieldSql()
	{
		//查询sql
		String sql = "";
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句    lirj add 
	   		 sql = new StringBuffer("select cust.").append(
					TableLfCustFieldValue.ID).append(",cust.").append(
					TableLfCustFieldValue.FIELD_ID).append(",cust.").append(
					TableLfCustFieldValue.FIELD_VALUE).toString();
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			 sql = new StringBuffer("select cust.").append(
						TableLfCustFieldValue.ID).append(",cust.").append(
						TableLfCustFieldValue.FIELD_ID).append(",cust.").append(
						TableLfCustFieldValue.FIELD_VALUE).append(", ROW_NUMBER() Over(Order By ").append(TableLfCustFieldValue.FIELD_ID).append(") As rn").toString();
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			sql = new StringBuffer("select cust.").append(
					TableLfCustFieldValue.ID).append(",cust.").append(
					TableLfCustFieldValue.FIELD_ID).append(",cust.").append(
					TableLfCustFieldValue.FIELD_VALUE).toString();
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = new StringBuffer("select cust.").append(
						TableLfCustFieldValue.ID).append(",cust.").append(
						TableLfCustFieldValue.FIELD_ID).append(",cust.").append(
						TableLfCustFieldValue.FIELD_VALUE).toString();
		}
	   	//返回sql
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
		//查询表格
		String tableSql = new StringBuffer(" from ").append(
				TableLfCustFieldValue.TABLE_NAME).append(" cust ")
				.append(" where ").toString();
		//返回sql
		return tableSql;
	}

	/***
	 *  处理查询sql中的条件
	* @Description: TODO
	* @param @param groupInfoVo
	* @param @return
	* @return String  sql语句
	 */
	public static String getConditionSql(CustFieldValueVo custFieldValueVo)
	{
		//查询条件sql
		StringBuffer conditionSql = new StringBuffer();
		//构造sql
		conditionSql.append(
				TableLfCustFieldValue.FIELD_ID).append(" in (select ").append(
						TableLfCustField.ID).append(" from ").append(TableLfCustField.TABLE_NAME).append(
						" where ").append(TableLfCustField.CORP_CODE).append(" = '").append(
								custFieldValueVo.getCorp_code()).append("')");
						
		//id
		if (custFieldValueVo.getId() != null)
		{
			conditionSql.append(" and cust.").append(
					TableLfCustFieldValue.ID).append("=").append(
							custFieldValueVo.getId());
		}
		//属性id
		if (custFieldValueVo.getField_ID() != null)
		{
			conditionSql.append(" and cust.").append(
					TableLfCustFieldValue.FIELD_ID).append("=").append(
							custFieldValueVo.getField_ID());
		}
		//属性值
		if (custFieldValueVo.getField_Value() != null
				&& !"".equals(custFieldValueVo.getField_Value()))
		{
			conditionSql.append(" and cust.").append(
					TableLfCustFieldValue.FIELD_VALUE).append(" like '%").append(
							custFieldValueVo.getField_Value()).append("%'");
		}
		//返回sql
		return conditionSql.toString();
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
		//条件sql
		StringBuffer conditionSql = new StringBuffer();
		//群组id
		if (groupInfoVo.getUdgId() != null)
		{
			conditionSql.append(" and list2gro.")
					.append(TableLfList2gro.UDG_ID).append("=").append(
							groupInfoVo.getUdgId());
		}
		//类型
		if (groupInfoVo.getL2gType() != null)
		{
			conditionSql.append(" and list2gro.").append(
					TableLfList2gro.L2G_TYPE).append("=").append(
					groupInfoVo.getL2gType());
		}
		//姓名
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
		//操作员id
		if (groupInfoVo.getUserId() != null)
		{
			conditionSql.append(" and udgroup.").append(TableLfUdgroup.USER_ID)
					.append("=").append(groupInfoVo.getUserId());
		}
		//返回sql
		return conditionSql.toString();
	}

	/**
	 * 排序处理
	 * @return
	 */
	public static String getOrderBySql()
	{
		//排序sql
		String sql = new StringBuffer(" order by cust.FIELD_VALUE asc").toString();
		//返回sql
		return sql;
	}
}
