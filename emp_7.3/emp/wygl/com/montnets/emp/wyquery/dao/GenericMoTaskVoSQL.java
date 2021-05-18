package com.montnets.emp.wyquery.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.query.TableMoTask;
import com.montnets.emp.wyquery.vo.MoTaskVo;

/**
 * 系统上行实时记录查询
 * @project sinolife
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-27 下午07:45:39
 * @description
 */
public class GenericMoTaskVoSQL
{
	/**
	 * 系统上行实时记录查询列名
	 * @return
	 */
	public static String getFieldSql()
	{
		String sql = new StringBuffer("select ")
					.append(TableMoTask.ID).append(",")
					.append("max(").append(TableMoTask.SPNUMBER).append(") as ").append(TableMoTask.SPNUMBER).append(",")
					.append("max(").append(TableMoTask.DELIVER_TIME).append(") as ").append(TableMoTask.DELIVER_TIME).append(",")
					.append("max(").append(TableMoTask.PHONE).append(") as ").append(TableMoTask.PHONE).append(",")
					.append("max(").append(TableMoTask.MSG_CONTENT).append(") as ").append(TableMoTask.MSG_CONTENT).append(",")
					.append("max(").append(TableMoTask.PKNUMBER).append(") as ").append(TableMoTask.PKNUMBER).append(",")
					.append("max(").append(TableMoTask.PKTOTAL).append(") as ").append(TableMoTask.PKTOTAL).append(",")
					.append("max(").append(TableMoTask.MSG_FMT).append(") as ").append(TableMoTask.MSG_FMT).append(",")
					.append("max(").append(TableMoTask.USER_ID).append(") as ").append(TableMoTask.USER_ID).append(",")
					.append("max(").append(TableMoTask.UNICOM).append(") as ").append(TableMoTask.UNICOM).append(",")
					.append("max(").append(TableLfEmployee.NAME).append(") as ").append(TableLfEmployee.NAME)
					.toString();
		return sql;
	}

	/**
	 * 系统上行实时记录查询
	 * @return
	 */
	public static String getTableSql()
	{
		String sql = new StringBuffer(" from ").append(TableMoTask.TABLE_NAME).append(" motask left join ").append(TableLfEmployee.TABLE_NAME).append(" employee on motask.PHONE=employee.mobile ")
				.toString();
		return sql;
	}

	/**
	 * 系统上行实时记录查询，组装过滤条件
	 * @param moTaskVo
	 * @return
	 */
	public static String getConditionSql(MoTaskVo moTaskVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		int corpType = StaticValue.getCORPTYPE();
		//通道号
		if (moTaskVo.getSpnumber() != null
				&& !"".equals(moTaskVo.getSpnumber()))
		{
			conditionSql.append(" and ").append(TableMoTask.SPNUMBER).append(
					" like'").append(moTaskVo.getSpnumber()).append("%'");
		}
		//sp账号
		if(moTaskVo.getUserId() != null && !"".equals(moTaskVo.getUserId()))
		{
			conditionSql.append(" and ").append(TableMoTask.USER_ID).append(
			" in ('").append(moTaskVo.getUserId()).append("')");
		}else
		if (moTaskVo.getSpUser() != null && !"".equals(moTaskVo.getSpUser()))
		{
			conditionSql.append(" and ").append(TableMoTask.USER_ID).append(
					" in (").append(moTaskVo.getSpUser()).append(")");
		}
		//如果未绑定sp账户，且是多企业的，则查询不到数据
		if ("".equals(moTaskVo.getSpUser()) && corpType == 1)
		{
			conditionSql.append(" and 1=2");
		}
        //手机号
		if (moTaskVo.getPhone() != null && !"".equals(moTaskVo.getPhone()))
		{
			if(moTaskVo.getPhone().length()==11){
				conditionSql.append(" and ").append(TableMoTask.PHONE).append(
				" = '").append(moTaskVo.getPhone()).append("'");
			}else{
				conditionSql.append(" and ").append(TableMoTask.PHONE).append(
					" like '%").append(moTaskVo.getPhone()).append("%'");
			}
		}
        //指定时间段，开始时间
		if (moTaskVo.getStartSubmitTime() != null
				&& !"".equals(moTaskVo.getStartSubmitTime()))
		{
			conditionSql.append(" and ").append(TableMoTask.DELIVER_TIME)
					.append(">=?");
		}
        //指定时间段，结束时间
		if (moTaskVo.getEndSubmitTime() != null
				&& !"".equals(moTaskVo.getEndSubmitTime()))
		{
			conditionSql.append(" and ").append(TableMoTask.DELIVER_TIME)
					.append("<=?");
		}
        //姓名
		if (moTaskVo.getName() != null && !"".equals(moTaskVo.getName()))
		{
			/*conditionSql.append(" and ").append(TableLfEmployee.NAME).append(
					" like '%").append(moTaskVo.getName()).append("%'");*/
			conditionSql.append(" and (motask.PHONE in (select employee.MOBILE from ")
			.append(" LF_EMPLOYEE employee where employee.NAME ")
			.append(" LIKE '%").append(moTaskVo.getName()).append("%' ) or motask.PHONE in ")
			.append("(select client.MOBILE from LF_CLIENT client where client.NAME")
			.append(" LIKE '%").append(moTaskVo.getName()).append("%' )) ");
		}
		//短信内容
		if (moTaskVo.getMsgContent() != null && !"".equals(moTaskVo.getMsgContent()))
		{
			conditionSql.append(" and ").append(TableMoTask.MSG_CONTENT).append(
					" like '%").append(moTaskVo.getMsgContent()).append("%'");
		}
		
		String sql = conditionSql.toString();
		return sql;
	}

	/**
	 * 系统上行实时记录查询
	 * @param moTaskVo
	 * @return
	 */
	public static List<String> getTimeCondition(MoTaskVo moTaskVo)
	{
		List<String> timeList = new ArrayList<String>();
		if (moTaskVo.getStartSubmitTime() != null
				&& !"".equals(moTaskVo.getStartSubmitTime()))
		{
			timeList.add(moTaskVo.getStartSubmitTime());
		}
		if (moTaskVo.getEndSubmitTime() != null
				&& !"".equals(moTaskVo.getEndSubmitTime()))
		{
			timeList.add(moTaskVo.getEndSubmitTime());
		}
		return timeList;
	}

	/**
	 * 系统上行实时记录查询
	 * @return
	 */
	public static String getOrderBySql()
	{
		String sql = new StringBuffer(" group by motask.").append(TableMoTask.ID)
					.append(" order by ").append(" max(motask.").append(TableMoTask.DELIVER_TIME).append(") ").append(" desc")
		.toString();
		return sql;
	}
	

	/**
	 * 系统上行实时记录查询列名  没有查询出该VO的名字
	 * @return
	 */
	public static String getFieldNoNameSql()
	{
		String sql = new StringBuffer("select ")
					.append(TableMoTask.ID).append(",")
					.append("max(").append(TableMoTask.SPNUMBER).append(") as ").append(TableMoTask.SPNUMBER).append(",")
					.append("max(").append(TableMoTask.DELIVER_TIME).append(") as ").append(TableMoTask.DELIVER_TIME).append(",")
					.append("max(").append(TableMoTask.PHONE).append(") as ").append(TableMoTask.PHONE).append(",")
					.append("max(").append(TableMoTask.MSG_CONTENT).append(") as ").append(TableMoTask.MSG_CONTENT).append(",")
					.append("max(").append(TableMoTask.PKNUMBER).append(") as ").append(TableMoTask.PKNUMBER).append(",")
					.append("max(").append(TableMoTask.PKTOTAL).append(") as ").append(TableMoTask.PKTOTAL).append(",")
					.append("max(").append(TableMoTask.MSG_FMT).append(") as ").append(TableMoTask.MSG_FMT).append(",")
					.append("max(").append(TableMoTask.USER_ID).append(") as ").append(TableMoTask.USER_ID).append(",")
					.append("max(").append(TableMoTask.UNICOM).append(") as ").append(TableMoTask.UNICOM)
					.toString();
		return sql;
	}
	
	
	
	/**
	 * 系统上行实时记录查询   没有关联手机号码
	 * @return
	 */
	public static String getTableNoNameSql()
	{
		String sql = new StringBuffer(" from ").append(TableMoTask.TABLE_NAME).append(" motask ").toString();
		return sql;
	}
	
}
