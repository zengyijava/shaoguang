package com.montnets.emp.common.timer.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.table.engine.TableLfService;
import com.montnets.emp.table.system.TableLfTimer;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;

/**
 * 
 * @author Administrator
 *
 */
public class GenericLfTimerVoSQL
{
	//获取查询字段
	public static String getFieldSql()
	{
		//查询字段
		String sql = new StringBuffer("select lfTimer.*").toString();
		//返回sql
		return sql;
	}

	//获取查询表格
	public static String getTableSql()
	{
		//sql语句
		String sql = "";
		//数据库是oracle时
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			//查询表格
			sql = new StringBuffer(" from ").append(TableLfTimer.TABLE_NAME).append(" lfTimer ").append(StaticValue.getWITHNOLOCK()).append(" inner join ").append(
					TableLfService.TABLE_NAME).append(" lfservice ").append(StaticValue.getWITHNOLOCK()).append(" on lfservice.").append(TableLfService.SER_ID)
					.append("=substr(lfTimer.").append(TableLfTimer.TASK_EXPRESSION).append(",").append("instr(")
					.append("lfTimer.").append(TableLfTimer.TASK_EXPRESSION).append(",'|')+1)").append(" inner join ").append(TableLfSysuser.TABLE_NAME)
					.append(" lfSysUser ").append(StaticValue.getWITHNOLOCK()).append(" on lfSysUser.").append(TableLfSysuser.USER_ID).append("=lfservice.")
					.append(TableLfService.USER_ID).append(" left join ").append(
							TableLfSysuser.TABLE_NAME).append(" sysuser2 ").append(StaticValue.getWITHNOLOCK()).append(" on sysuser2.").append(TableLfSysuser.USER_ID)
							.append("=lfservice.").append(TableLfService.OWNER_ID).toString();
		}else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
			//数据库是sql server时
			//查询表
			sql = new StringBuffer(" from ").append(TableLfTimer.TABLE_NAME)
					.append(" lfTimer ").append(StaticValue.getWITHNOLOCK()).append(" inner join ").append(
							TableLfService.TABLE_NAME).append(
							" lfservice ").append(StaticValue.getWITHNOLOCK()).append(" on lfservice.").append(
							TableLfService.SER_ID)
					.append("=substring(lfTimer.").append(
							TableLfTimer.TASK_EXPRESSION).append(",").append(
							"charindex(").append("'|',").append("lfTimer.")
					.append(TableLfTimer.TASK_EXPRESSION).append(
							")+1,len(lfTimer.").append(
							TableLfTimer.TASK_EXPRESSION).append(")-charindex(")
							.append("'|',").append("lfTimer.")
					.append(TableLfTimer.TASK_EXPRESSION).append(
							"))").append(
							" inner join ").append(TableLfSysuser.TABLE_NAME)
					.append(" lfSysUser ").append(StaticValue.getWITHNOLOCK()).append(" on lfSysUser.").append(
							TableLfSysuser.USER_ID).append("=lfservice.")
					.append(TableLfService.USER_ID).append(" left join ").append(
							TableLfSysuser.TABLE_NAME).append(" sysuser2 ").append(StaticValue.getWITHNOLOCK()).append(" on sysuser2.").append(TableLfSysuser.USER_ID)
							.append("=lfservice.").append(TableLfService.OWNER_ID).toString();
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//数据库是db2时
			//查询表
			sql = new StringBuffer(" from ").append(TableLfTimer.TABLE_NAME)
			.append(" lfTimer ").append(StaticValue.getWITHNOLOCK()).append(" inner join ").append(
					TableLfService.TABLE_NAME).append(
					" lfservice ").append(StaticValue.getWITHNOLOCK()).append(" on lfservice.").append(
					TableLfService.SER_ID)
			.append("=bigint(substr(lfTimer.").append(
					TableLfTimer.TASK_EXPRESSION).append(",").append(
					"locate(").append("'|',").append("lfTimer.")
			.append(TableLfTimer.TASK_EXPRESSION).append(
					")+1,length(lfTimer.").append(
					TableLfTimer.TASK_EXPRESSION).append(")-locate(")
					.append("'|',").append("lfTimer.")
			.append(TableLfTimer.TASK_EXPRESSION).append(
					")))").append(
					" inner join ").append(TableLfSysuser.TABLE_NAME)
			.append(" lfSysUser ").append(StaticValue.getWITHNOLOCK()).append(" on lfSysUser.").append(
					TableLfSysuser.USER_ID).append("=lfservice.")
			.append(TableLfService.USER_ID).append(" left join ").append(
					TableLfSysuser.TABLE_NAME).append(" sysuser2 ").append(StaticValue.getWITHNOLOCK()).append(" on sysuser2.").append(TableLfSysuser.USER_ID)
					.append("=lfservice.").append(TableLfService.OWNER_ID).toString();
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			//数据库是mysql时
			//查询表
			sql = new StringBuffer(" from ").append(TableLfTimer.TABLE_NAME)
					.append(" lfTimer ").append(StaticValue.getWITHNOLOCK()).append(" inner join ").append(
							TableLfService.TABLE_NAME).append(
							" lfservice ").append(StaticValue.getWITHNOLOCK()).append(" on lfservice.").append(
							TableLfService.SER_ID).append("=substring(lfTimer.")
					.append(TableLfTimer.TASK_EXPRESSION).append(",").append(
							"instr(").append("lfTimer.").append(
							TableLfTimer.TASK_EXPRESSION).append(",'|')+1)")
					.append(" inner join ").append(TableLfSysuser.TABLE_NAME)
					.append(" lfSysUser ").append(StaticValue.getWITHNOLOCK()).append(" on lfSysUser.").append(
							TableLfSysuser.USER_ID).append("=lfservice.")
					.append(TableLfService.USER_ID).append(" left join ")
					.append(TableLfSysuser.TABLE_NAME).append(
							" sysuser2 ").append(StaticValue.getWITHNOLOCK()).append(" on sysuser2.").append(
							TableLfSysuser.USER_ID).append("=lfservice.")
					.append(TableLfService.OWNER_ID).toString();
		}
		//返回结果
		return sql;
	}

	//获取权限
	public static String getDominationSql(String userId)
	{
		//权限sql
		StringBuffer dominationSql = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userId);
		//组装权限sql
		String sql = new StringBuffer(" where (lfservice.").append(
				TableLfService.USER_ID).append("=").append(userId).append(
				" or lfSysUser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(dominationSql).append(")")
				.append(" or sysuser2.").append(TableLfSysuser.USER_ID).append("=").append(userId).append(")")
				.toString();
		//返回结果
		return sql;
	}

	//获取查询条件
	public static String getConditionSql(LfTimer lftimer)
	{
		//查询条件
		StringBuffer conditionSql = new StringBuffer();
		
		//表达式
		if (lftimer.getTaskExpression() != null
				&& !"".equals(lftimer.getTaskExpression()))
		{
			//构造sql
			conditionSql.append(" and lfTimer.")
					.append(TableLfTimer.TASK_EXPRESSION).append(" like '%").append(
							lftimer.getTaskExpression()).append("%'");
		}
		
		//查询条件sql
		String sql = conditionSql.toString();
		//返回结果
		return sql;
	}

	//排序
	public static String getOrderBySql()
	{
		//排序sql
		String sql = new StringBuffer(" order by lfTimer.").append(
				TableLfTimer.TIMER_TASK_ID).append(" asc").toString();
		//返回sql
		return sql;
	}

}
