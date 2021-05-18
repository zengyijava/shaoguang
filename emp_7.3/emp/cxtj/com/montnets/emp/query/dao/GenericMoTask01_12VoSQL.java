package com.montnets.emp.query.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.inbox.dao.ReciveBoxDao;
import com.montnets.emp.query.vo.MoTask01_12Vo;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.query.TableMoTask01_12;

/**
 * 系统上行历史记录查询
 * @project sinolife
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-27 下午07:46:38
 * @description
 */
public class GenericMoTask01_12VoSQL
{
	/**
	 * 系统上行历史记录查询列名
	 * @return
	 */
	public static String getFieldSql()
	{
		String sql = new StringBuffer("select ")
		.append(" mtt.").append(TableMoTask01_12.ID)
		.append(", max(mtt.").append(TableMoTask01_12.SPNUMBER).append(") as ").append(TableMoTask01_12.SPNUMBER)
		.append(", max(mtt.").append(TableMoTask01_12.DELIVERTIME).append(") as ").append(TableMoTask01_12.DELIVERTIME)
		.append(", max(mtt.").append(TableMoTask01_12.PHONE).append(") as ").append(TableMoTask01_12.PHONE)
		.append(", max(employee.").append(TableLfEmployee.NAME).append(") as ").append(TableLfEmployee.NAME)
		.append(", max(mtt.").append(TableMoTask01_12.MSGCONTENT).append(") as ").append(TableMoTask01_12.MSGCONTENT)
		.append(", max(mtt.").append(TableMoTask01_12.UNICOM).append(") as ").append(TableMoTask01_12.UNICOM)
		.append(", max(mtt.").append(TableMoTask01_12.USERID).append(") as ").append(TableMoTask01_12.USERID)
		.append(", max(mtt.").append(TableMoTask01_12.MSGFMT).append(") as ").append(TableMoTask01_12.MSGFMT)
		.toString();
		return sql;
	}

	/**
	 * 系统上行记录历史查询
	 * @param tableName
	 * @return
	 */
	public static String getTableSql(String tableName)
	{
		String sql = new StringBuffer(" from ").append(tableName).append(" left join ").append(TableLfEmployee.TABLE_NAME).append(" employee "+StaticValue.getWITHNOLOCK()+" on mtt.phone=employee.mobile ").append(
				" ").toString();
		return sql;
	}

	/**
	 * 系统上行历史记录查询，过滤条件
	 * @param moTaskVo01_12
	 * @return
	 */
	public static String getConditionSql(MoTask01_12Vo moTaskVo01_12)
	{
		StringBuffer conditionSql = new StringBuffer();
		int corpType = StaticValue.getCORPTYPE();
		boolean iswhere=false;
		//通道号
		if (moTaskVo01_12.getSpnumber() != null
				&& !"".equals(moTaskVo01_12.getSpnumber()))
		{
			if(iswhere){
				conditionSql.append(" and mtt.").append(TableMoTask01_12.SPNUMBER)
						.append(" like'").append(moTaskVo01_12.getSpnumber())
						.append("%'");
			}else{
				conditionSql.append(" WHERE mtt.").append(TableMoTask01_12.SPNUMBER)
				.append(" like'").append(moTaskVo01_12.getSpnumber())
				.append("%'");
				iswhere=true;
			}
		}
		
		//2012.05.19 当用户查询时选择了发送账号，则需要根据发送账号过滤。
		if(moTaskVo01_12.getUserId() != null && !"".equals(moTaskVo01_12.getUserId()))
		{
			String useridstr=moTaskVo01_12.getUserId();
			//如果是db2 oracle 则大写小写都加一份 兼容小写
			if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
					||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE ))
			{
				useridstr=useridstr.toUpperCase()+"','"+useridstr.toLowerCase();
			}
			if(iswhere){
				conditionSql.append(" and mtt.").append(TableMoTask01_12.USERID).append(
				" in ('").append(useridstr).append("')");
			}else{
				conditionSql.append(" WHERE mtt.").append(TableMoTask01_12.USERID).append(
				" in ('").append(useridstr).append("')");
				iswhere=true;
			}
			
		}else 
			if (moTaskVo01_12.getSpUser() != null && !"".equals(moTaskVo01_12.getSpUser()))
			{
				String useridstrs=moTaskVo01_12.getSpUser();
				//如果是db2 oracle 则大写小写都加一份 兼容小写
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
						||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE ))
				{
					useridstrs=useridstrs.toUpperCase()+","+useridstrs.toLowerCase();
				}
				String insqlstr=new ReciveBoxDao().getSqlStr(useridstrs, "mtt."+TableMoTask01_12.USERID);
				if(iswhere){
					conditionSql.append(" and ("+insqlstr+")");
				}else{
					conditionSql.append(" WHERE ("+insqlstr+")");
					iswhere=true;
				}
		//			conditionSql.append(" and mtt.").append(TableMoTask01_12.USERID).append(
		//					" in (").append(moTaskVo01_12.getSpUser()).append(")");
			}
        //如果未绑定发送账号，且是多企业的，则查询不到数据
		if ("".equals(moTaskVo01_12.getSpUser()) && corpType == 1)
		{
			if(iswhere){
				conditionSql.append(" and 1=2");
			}else{
				conditionSql.append(" WHERE 1=2");
				iswhere=true;
			}
		}
		//手机号
		if (moTaskVo01_12.getPhone() != null
				&& !"".equals(moTaskVo01_12.getPhone()))
		{
				if(iswhere){
					conditionSql.append(" and mtt.").append(TableMoTask01_12.PHONE).append(
					" like '%").append(moTaskVo01_12.getPhone()).append("%'");
				}else{
					conditionSql.append(" WHERE mtt.").append(TableMoTask01_12.PHONE).append(
					" like '%").append(moTaskVo01_12.getPhone()).append("%'");
					iswhere=true;
				}
		}
        //指定时间段开始
		if (moTaskVo01_12.getStartSubmitTime() != null
				&& !"".equals(moTaskVo01_12.getStartSubmitTime()))
		{
			if(iswhere){
				conditionSql.append(" and mtt.").append(TableMoTask01_12.DELIVERTIME)
				.append(">=?");
			}else{
				conditionSql.append(" WHERE mtt.").append(TableMoTask01_12.DELIVERTIME)
				.append(">=?");
				iswhere=true;
			}
		}
        //指定时间段结束
		if (moTaskVo01_12.getEndSubmitTime() != null
				&& !"".equals(moTaskVo01_12.getEndSubmitTime()))
		{
			if(iswhere){
				conditionSql.append(" and mtt.").append(TableMoTask01_12.DELIVERTIME)
				.append("<=?");
			}else{
				conditionSql.append(" WHERE mtt.").append(TableMoTask01_12.DELIVERTIME)
				.append("<=?");
				iswhere=true;
			}
		}
		//姓名
		if (moTaskVo01_12.getName() != null && !"".equals(moTaskVo01_12.getName()))
		{
			/*conditionSql.append(" and ").append(TableLfEmployee.NAME).append(
					" like '%").append(moTaskVo01_12.getName()).append("%'");*/
			if(iswhere){
				conditionSql.append(" and (mtt.PHONE in (select employee.MOBILE from ")
				.append(" LF_EMPLOYEE employee "+StaticValue.getWITHNOLOCK()+" where employee.NAME ")
				.append(" LIKE '%").append(moTaskVo01_12.getName()).append("%' ) or mtt.PHONE in ")
				.append("(select client.MOBILE from LF_CLIENT client "+StaticValue.getWITHNOLOCK()+" where client.NAME")
				.append(" LIKE '%").append(moTaskVo01_12.getName()).append("%' )) ");
			}else{
				conditionSql.append(" WHERE (mtt.PHONE in (select employee.MOBILE from ")
				.append(" LF_EMPLOYEE employee "+StaticValue.getWITHNOLOCK()+" where employee.NAME ")
				.append(" LIKE '%").append(moTaskVo01_12.getName()).append("%' ) or mtt.PHONE in ")
				.append("(select client.MOBILE from LF_CLIENT client "+StaticValue.getWITHNOLOCK()+" where client.NAME")
				.append(" LIKE '%").append(moTaskVo01_12.getName()).append("%' )) ");
				iswhere=true;
			}
			
		}
				
		//短信内容
		if (moTaskVo01_12.getMsgContent() != null && !"".equals(moTaskVo01_12.getMsgContent()))
		{
			if(iswhere){
				conditionSql.append(" and mtt.").append(TableMoTask01_12.MSGCONTENT).append(" like '%").append(moTaskVo01_12.getMsgContent()).append("%' ");
			}else{
				conditionSql.append(" WHERE mtt.").append(TableMoTask01_12.MSGCONTENT).append(" like '%").append(moTaskVo01_12.getMsgContent()).append("%' ");
				iswhere=true;
			}
		}
		//运营商
		if (moTaskVo01_12.getUnicom() != null)
		{
			if(iswhere){
				conditionSql.append(" and mtt.").append(TableMoTask01_12.UNICOM).append(" = ").append(moTaskVo01_12.getUnicom());
			}else{
				conditionSql.append(" WHERE mtt.").append(TableMoTask01_12.UNICOM).append(" = ").append(moTaskVo01_12.getUnicom());
				iswhere=true;
			}
		}
		
		String sql = conditionSql.toString();
		return sql;
	}

	/**
	 * 系统上行历史记录查询
	 * @param moTaskVo01_12
	 * @return
	 */
	public static List<String> getTimeCondition(MoTask01_12Vo moTaskVo01_12)
	{
		List<String> timeList = new ArrayList<String>();
		if (moTaskVo01_12.getStartSubmitTime() != null
				&& !"".equals(moTaskVo01_12.getStartSubmitTime()))
		{
			timeList.add(moTaskVo01_12.getStartSubmitTime());
		}
		if (moTaskVo01_12.getEndSubmitTime() != null
				&& !"".equals(moTaskVo01_12.getEndSubmitTime()))
		{
			timeList.add(moTaskVo01_12.getEndSubmitTime());
		}
		return timeList;
	}

	/**
	 * 系统上行历史记录查询
	 * @return
	 */
	public static String getOrderBySql()
	{
		String sql = new StringBuffer(" group by mtt.").append(TableMoTask01_12.ID).append(" order by ")
		.append(" max(mtt.").append(TableMoTask01_12.DELIVERTIME).append(") ")
		.append(" desc").toString();
		return sql;
	}
	
	
	
	/**
	 * 系统上行历史记录查询列名 没查询出名字
	 * @return
	 */
	public static String getFieldNoNameSql()
	{
		String sql = new StringBuffer("select ")
		.append(" mtt.").append(TableMoTask01_12.ID)
		.append(", max(mtt.").append(TableMoTask01_12.SPNUMBER).append(") as ").append(TableMoTask01_12.SPNUMBER)
		.append(", max(mtt.").append(TableMoTask01_12.DELIVERTIME).append(") as ").append(TableMoTask01_12.DELIVERTIME)
		.append(", max(mtt.").append(TableMoTask01_12.PHONE).append(") as ").append(TableMoTask01_12.PHONE)
		.append(", max(mtt.").append(TableMoTask01_12.MSGCONTENT).append(") as ").append(TableMoTask01_12.MSGCONTENT)
		.append(", max(mtt.").append(TableMoTask01_12.UNICOM).append(") as ").append(TableMoTask01_12.UNICOM)
		.append(", max(mtt.").append(TableMoTask01_12.USERID).append(") as ").append(TableMoTask01_12.USERID)
		.append(", max(mtt.").append(TableMoTask01_12.MSGFMT).append(") as ").append(TableMoTask01_12.MSGFMT)
		.toString();
		return sql;
	}
	
	
	
	/**
	 * 系统上行记录历史查询没查询出名字
	 * @param tableName
	 * @return
	 */
	public static String getTableNoNameSql(String tableName)
	{
		String sql = new StringBuffer(" from ").append(tableName).append(" ").toString();
		return sql;
	}
}
