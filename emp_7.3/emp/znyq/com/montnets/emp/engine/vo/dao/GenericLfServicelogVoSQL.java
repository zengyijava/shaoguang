package com.montnets.emp.engine.vo.dao;

import java.util.ArrayList;
import java.util.List;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.engine.TableLfService;
import com.montnets.emp.table.engine.TableLfServicelog;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.engine.vo.LfServicelogVo;

/**
 * 
 * @project sinolife
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-13 上午10:06:39
 * @description
 */
public class GenericLfServicelogVoSQL
{
	// LfServicelogVoDAO

	//获取查询字段
	public static String getFieldSql()
	{
		//拼装查询语句
		String sql = new StringBuffer("select")
				.append(" servicelog.").append(TableLfServicelog.SL_ID)
				.append(",servicelog.").append(TableLfServicelog.RUNTIME)
				.append(",servicelog.").append(TableLfServicelog.SL_STATE)
				.append(",servicelog.").append(TableLfServicelog.URL)
				.append(",service.").append(TableLfService.SP_USER)
				.append(",service.").append(TableLfService.SER_TYPE)
				.append(",service.").append(TableLfService.SER_NAME)
				.append(",service.").append(TableLfService.SER_ID)
				.append(",sysuser.").append(TableLfSysuser.NAME)
				.append(",userdata.").append(TableUserdata.STAFF_NAME)
				.toString();
		//返回语句
		return sql;
	}

	/**
	 * LfServicelogVoDAO
	 * 
	 * @return
	 */
	public static String getTableSql()
	{
		//查询表格
		String sql = new StringBuffer(" from ").append(
				TableLfServicelog.TABLE_NAME).append(" servicelog inner join ")
				.append(TableLfService.TABLE_NAME).append(
						" service on servicelog.").append(
						TableLfServicelog.SER_ID).append("=service.").append(
						TableLfService.SER_ID).append(" inner join ").append(
						TableLfSysuser.TABLE_NAME).append(
						" sysuser on sysuser.").append(TableLfSysuser.USER_ID)
				.append("=service.").append(TableLfService.USER_ID).append(
						" left join ").append(TableUserdata.TABLE_NAME).append(
						" userdata on service.").append(TableLfService.SP_USER)
				.append("=userdata.").append(TableUserdata.USER_ID).append(" left join ").append(
						TableLfSysuser.TABLE_NAME).append(" sysuser2 on sysuser2.").append(TableLfSysuser.USER_ID)
						.append("=service.").append(TableLfService.OWNER_ID).toString();
		//返回sql
		return sql;
	}

	/**
	 * LfServicelogVoDAO
	 * 
	 * @param GL_UserID
	 * @return
	 */
	public static String getDominationSql(String GL_UserID)
	{
		//权限
		StringBuffer dominationSql = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(GL_UserID);
		//组装sql
		String sql = new StringBuffer(" where (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(GL_UserID).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(dominationSql).append(")")
				.append(" or sysuser2.").append(TableLfSysuser.USER_ID).append("=").append(GL_UserID)
				.append(")").toString();
		//返回sql
		return sql;
	}

	/**
	 * LfServicelogVoDAO
	 * 
	 * @param LfServicelogVo
	 * @return
	 */
	public static String getConditionSql(LfServicelogVo lfServicelogVo)
	{
		//查询条件
		StringBuffer conditionSql = new StringBuffer();
		
		//服务名
		if (lfServicelogVo.getSerName() != null
				&& !"".equals(lfServicelogVo.getSerName()))
		{
			conditionSql.append(" and service.")
					.append(TableLfService.SER_NAME).append(" like '%").append(
							lfServicelogVo.getSerName()).append("%'");
		}
		
		//名称
		if (lfServicelogVo.getName() != null
				&& !"".equals(lfServicelogVo.getName()))
		{
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME)
					.append("='").append(lfServicelogVo.getName()).append("'");
		}
		
		//发送账户
		if (lfServicelogVo.getSpUser() != null
				&& !"".equals(lfServicelogVo.getSpUser()))
		{
			conditionSql.append(" and service.").append(TableLfService.SP_USER)
					.append(" like '%").append(lfServicelogVo.getSpUser())
					.append("%'");
		}
		
		//提交时间
		if (lfServicelogVo.getStartSubmitTime() != null
				&& !"".equals(lfServicelogVo.getStartSubmitTime()))
		{
			conditionSql.append(" and servicelog.").append(
					TableLfServicelog.RUNTIME).append(">=?");
		}
		//结束时间
		if (lfServicelogVo.getEndSubmitTime() != null
				&& !"".equals(lfServicelogVo.getEndSubmitTime()))
		{
			conditionSql.append(" and servicelog.").append(
					TableLfServicelog.RUNTIME).append("<=?");
		}
		
		conditionSql.append(" and userdata.accounttype=1 ");
		//sql语句
		String sql = conditionSql.toString();
		//返回sql
		return sql;
	}

	/**
	 * 
	 * 
	 * @param lfServicelogVo
	 * @return
	 */
	public static List<String> getTimeCondition(LfServicelogVo lfServicelogVo)
	{
		//任务集合
		List<String> timeList = new ArrayList<String>();
		//提交时间
		if (lfServicelogVo.getStartSubmitTime() != null
				&& !"".equals(lfServicelogVo.getStartSubmitTime()))
		{
			timeList.add(lfServicelogVo.getStartSubmitTime());
		}
		//结束时间
		if (lfServicelogVo.getEndSubmitTime() != null
				&& !"".equals(lfServicelogVo.getEndSubmitTime()))
		{
			timeList.add(lfServicelogVo.getEndSubmitTime());
		}
		//返回任务
		return timeList;
	}

	//获取排序sql
	public static String getOrderBySql()
	{
		//排序sql
		String sql = new StringBuffer(" order by servicelog.").append(
				TableLfServicelog.RUNTIME).append(" desc").toString();
		//返回sql
		return sql;
	}
}
