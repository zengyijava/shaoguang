package com.montnets.emp.report.dao;

import com.montnets.emp.report.vo.OperatorsAreaMtDataReportVo;
import com.montnets.emp.report.vo.OperatorsMtDataReportVo;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.passage.TableXtGateQueue;
import com.montnets.emp.table.report.TableMmsDatareport;
import com.montnets.emp.table.report.TableMtDatareport;
import com.montnets.emp.table.wy.TableAAreacode;

/**
 * 运营商报表sql工具方法
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:47:21
 * @description
 */
public class GenericOperatorsMtDataReportVoSQL
{
	/**
	 * 月报表获取字段
	 * 
	 * @return
	 */
	public static String getMonthFieldSql()
	{

		String sql = new StringBuffer("select sum(mtdatareport.").append(TableMtDatareport.ICOUNT).append(") ICOUNT")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RFAIL1).append(") RFAIL1")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RFAIL2).append(") RFAIL2")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RNRET).append(") RNRET")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RSUCC).append(") RSUCC")
				.append(",max(mtdatareport.").append(TableMtDatareport.Y).append(") ").append(TableMtDatareport.Y)
				.append(",max(mtdatareport.").append(TableMtDatareport.IMONTH).append(") ").append(TableMtDatareport.IMONTH)
				.append(",max(mtdatareport.").append(TableMtDatareport.IYMD).append(") ").append(TableMtDatareport.IYMD)
				.append(",mtdatareport.").append(TableMtDatareport.SPID)
				.append(",mtdatareport.").append(TableMtDatareport.SPISUNCM).toString();
		return sql;
	}

	/**
	 * 月报表获取表格
	 * 
	 * @return
	 */
	public static String getMonthTableSql(Integer mstype)
	{
		String sql = null;
		if(mstype == 0)
		{
			sql = new StringBuffer(" from ").append(TableMtDatareport.TABLE_NAME).append(" mtdatareport ").toString();
		}
		else
		{
			sql = new StringBuffer(" from ").append(TableMmsDatareport.TABLE_NAME).append(" mtdatareport ").toString();
		}
		return sql;
	}

	/**
	 * 月报表回去查询条件
	 * 
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getMonthConditionSql(OperatorsMtDataReportVo operatorsMtDataReportVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		// 账号
		if(operatorsMtDataReportVo.getSpID() != null && !"".equals(operatorsMtDataReportVo.getSpID()))
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.SPID).append("='").append(operatorsMtDataReportVo.getSpID()).append("'");
		}

		// 年份
		if(operatorsMtDataReportVo.getY() != null && !"".equals(operatorsMtDataReportVo.getY()))
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.Y).append("=").append(operatorsMtDataReportVo.getY());
		}

		// 月份
		if(operatorsMtDataReportVo.getImonth() != null && !"".equals(operatorsMtDataReportVo.getImonth()))
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.IMONTH).append("=").append(operatorsMtDataReportVo.getImonth());
		}

		// 运营商
		if(operatorsMtDataReportVo.getSpisuncm() != null)
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.SPISUNCM).append("=").append(operatorsMtDataReportVo.getSpisuncm());
		}

		String sql = conditionSql.toString();
		return sql;
	}

	public static String getMonthGroupBySql(OperatorsMtDataReportVo operatorsMtDataReportVo)
	{
		String sql="";
		if(!operatorsMtDataReportVo.getIsDes()){
			sql = new StringBuffer(" group by mtdatareport.")
					.append(TableMtDatareport.IMONTH)
					.append(",mtdatareport.")
					.append(TableMtDatareport.Y)
					.append(",mtdatareport.")
					.append(TableMtDatareport.SPID)
					.append(",mtdatareport.")
					.append(TableMtDatareport.SPISUNCM)
					.toString();
		}
		else
		{
			sql = new StringBuffer(" group by mtdatareport.")
					.append(TableMtDatareport.IMONTH)
					.append(",mtdatareport.")
					.append(TableMtDatareport.Y)
					.append(",mtdatareport.")
					.append(TableMtDatareport.IYMD)
					.append(",mtdatareport.")
					.append(TableMtDatareport.SPID)
					.append(",mtdatareport.")
					.append(TableMtDatareport.SPISUNCM)
					.toString();
		}
		
		return sql;
	}

	/**
	 * 月报表获取排序sql
	 * 
	 * @return
	 */
	public static String getMonthOrderBySql(OperatorsMtDataReportVo operatorsMtDataReportVo)
	{
		String sql ="";
		if(!operatorsMtDataReportVo.getIsDes())
		{
			sql=new StringBuffer(" order by mtdatareport.").append(TableMtDatareport.IMONTH).append(" desc").toString();
		}
		else
		{
			sql=new StringBuffer(" order by mtdatareport.").append(TableMtDatareport.IYMD).append(" desc").toString();
		}
		return sql;
	}

	/**
	 * 年报表sql字段
	 * 
	 * @return
	 */
	public static String getYearFieldSql()
	{
		String sql = new StringBuffer("select ")
				.append("sum(mtdatareport.").append(TableMtDatareport.ICOUNT).append(") ICOUNT")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RFAIL1).append(") RFAIL1")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RFAIL2).append(") RFAIL2")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RNRET).append(") RNRET")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RSUCC).append(") RSUCC")
				.append(",mtdatareport.").append(TableMtDatareport.Y)
				.append(",max(mtdatareport.").append(TableMtDatareport.IMONTH).append(") ").append(TableMtDatareport.IMONTH)
				.append(",max(mtdatareport.").append(TableMtDatareport.IYMD).append(") ").append(TableMtDatareport.IYMD)
				.append(",mtdatareport.").append(TableMtDatareport.SPID)
				.append(",mtdatareport.").append(TableMtDatareport.SPISUNCM)
				.toString();
		return sql;
	}

	/**
	 * 年报表表格
	 * 
	 * @return
	 */
	public static String getYearTableSql(Integer mstype)
	{

		String sql = null;
		if(mstype == 0)
		{
			sql = new StringBuffer(" from ").append(TableMtDatareport.TABLE_NAME).append(" mtdatareport ").toString();
		}
		else
		{
			sql = new StringBuffer(" from ").append(TableMmsDatareport.TABLE_NAME).append(" mtdatareport ").toString();
		}
		return sql;
	}

	/**
	 * 年报表查询条件
	 * 
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getYearConditionSql(OperatorsMtDataReportVo operatorsMtDataReportVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		// sp账号
		if(operatorsMtDataReportVo.getSpID() != null && !"".equals(operatorsMtDataReportVo.getSpID()))
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.SPID).append("='").append(operatorsMtDataReportVo.getSpID()).append("'");
		}

		// 年份
		if(operatorsMtDataReportVo.getY() != null && !"".equals(operatorsMtDataReportVo.getY()))
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.Y).append("=").append(operatorsMtDataReportVo.getY());
		}

		// 运营商
		if(operatorsMtDataReportVo.getSpisuncm() != null)
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.SPISUNCM).append("=").append(operatorsMtDataReportVo.getSpisuncm());
		}

		String sql = conditionSql.toString();
		return sql;
	}

	/**
	 * 年报表分组sql
	 * 
	 * @return
	 */
	public static String getYearGroupBySql(OperatorsMtDataReportVo operatorsMtDataReportVo)
	{
		String sql="";
		if(!operatorsMtDataReportVo.getIsDes()){
			sql = new StringBuffer(" group by mtdatareport.").append(TableMtDatareport.Y).append(",mtdatareport.").append(TableMtDatareport.SPID).append(",mtdatareport.").append(TableMtDatareport.SPISUNCM).toString();
		}
		else
		{
			sql = new StringBuffer(" group by mtdatareport.").append(TableMtDatareport.Y).append(",mtdatareport.").append(TableMtDatareport.IMONTH).append(",mtdatareport.").append(TableMtDatareport.SPID).append(",mtdatareport.").append(TableMtDatareport.SPISUNCM).toString();
		}
		 
		return sql;
	}

	/**
	 * 年报表排序
	 * 
	 * @return
	 */
	public static String getYearOrderBySql(OperatorsMtDataReportVo operatorsMtDataReportVo)
	{	
		String sql = ""; 
		if(!operatorsMtDataReportVo.getIsDes())
		{
			sql=new StringBuffer(" order by mtdatareport.").append(TableMtDatareport.Y).append(" desc").toString();
		}
		else 
		{
			sql=new StringBuffer(" order by mtdatareport.").append(TableMtDatareport.IMONTH).append(" desc").toString();
		} 
		
		return sql;
	}

	/**
	 * 日报表sql字段
	 * 
	 * @return
	 */
	public static String getDaysFieldSql()
	{								   
		String sql =new StringBuffer("select sum(mtdatareport.").append(TableMtDatareport.ICOUNT).append(") ICOUNT")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RFAIL1).append(") RFAIL1")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RFAIL2).append(") RFAIL2")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RNRET).append(") RNRET")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RSUCC).append(") RSUCC")
				.append(",max(mtdatareport.").append(TableMtDatareport.Y).append(") ").append(TableMtDatareport.Y)
				.append(",max(mtdatareport.").append(TableMtDatareport.IMONTH).append(") ").append(TableMtDatareport.IMONTH)
				.append(",max(mtdatareport.").append(TableMtDatareport.IYMD).append(") ").append(TableMtDatareport.IYMD)
				.append(",mtdatareport.").append(TableMtDatareport.SPID)
				.append(",mtdatareport.").append(TableMtDatareport.SPISUNCM)
				.toString();
		return sql;
	}
	
	/**
	 * 日报表表格
	 * 
	 * @return
	 */
	public static String getDaysTableSql(Integer mstype)
	{

		String sql = null;
		if(mstype == 0)
		{
			sql = new StringBuffer(" from ").append(TableMtDatareport.TABLE_NAME).append(" mtdatareport ").toString();
		}
		else
		{
			sql = new StringBuffer(" from ").append(TableMmsDatareport.TABLE_NAME).append(" mtdatareport ").toString();
		}
		return sql;
	}

	/**
	 * 日报表查询条件
	 * 
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getDaysConditionSql(OperatorsMtDataReportVo operatorsMtDataReportVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		// sp账号
		if(operatorsMtDataReportVo.getSpID() != null && !"".equals(operatorsMtDataReportVo.getSpID()))
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.SPID).append("='").append(operatorsMtDataReportVo.getSpID()).append("'");
		}

		// 开始时间
		if(operatorsMtDataReportVo.getStartTime() != null && !"".equals(operatorsMtDataReportVo.getStartTime()))
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.IYMD).append(">=").append(operatorsMtDataReportVo.getStartTime().replaceAll("-", ""));
		}
		
		//结束时间
		if(operatorsMtDataReportVo.getEndTime() != null && !"".equals(operatorsMtDataReportVo.getEndTime()))
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.IYMD).append("<=").append(operatorsMtDataReportVo.getEndTime().replaceAll("-", ""));
		}

		// 运营商
		if(operatorsMtDataReportVo.getSpisuncm() != null)
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.SPISUNCM).append("=").append(operatorsMtDataReportVo.getSpisuncm());
		}

		String sql = conditionSql.toString();
		return sql;
	}
	
	/**
	 * 日报表分组sql
	 * 
	 * @return
	 */
	public static String getDaysGroupBySql(OperatorsMtDataReportVo operatorsMtDataReportVo)
	{
		String sql="";
		if(!operatorsMtDataReportVo.getIsDes()){
			sql = new StringBuffer(" group by mtdatareport.").append(TableMtDatareport.SPID).append(",mtdatareport.").append(TableMtDatareport.SPISUNCM).toString();
		}else{
			sql = new StringBuffer(" group by mtdatareport.").append(TableMtDatareport.IYMD).append(",mtdatareport.").append(TableMtDatareport.SPID).append(",mtdatareport.").append(TableMtDatareport.SPISUNCM).toString();
		}
		
		return sql;
	}

	/**
	 * 日报表排序
	 * 
	 * @return
	 */
	public static String getDaysOrderBySql()
	{
		String sql = new StringBuffer(" order by mtdatareport.").append(TableMtDatareport.IYMD).append(" desc").toString();
		return sql;
	}
	
	
	
	/**
	 * 各国详情获取字段
	 * 
	 * @return
	 */
	public static String getAreaFieldSql()
	{
		String sql = new StringBuffer("select sum(mtdatareport.").append(TableMtDatareport.ICOUNT).append(") ICOUNT")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RFAIL1).append(") RFAIL1")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RFAIL2).append(") RFAIL2")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RNRET).append(") RNRET")
				.append(",sum(mtdatareport.").append(TableMtDatareport.RSUCC).append(") RSUCC")
				.append(",max(mtdatareport.").append(TableMtDatareport.Y).append(") ").append(TableMtDatareport.Y)
				.append(",max(mtdatareport.").append(TableMtDatareport.IMONTH).append(") ").append(TableMtDatareport.IMONTH)
				.append(",max(mtdatareport.").append(TableMtDatareport.IYMD).append(") ").append(TableMtDatareport.IYMD)
				.append(",mtdatareport.").append(TableMtDatareport.SPID)
				.append(",mtdatareport.").append(TableMtDatareport.SPISUNCM)
				.append(", areacode.").append(TableAAreacode.AREACODE)
				.append(", areacode.").append(TableAAreacode.AREANAME)
				.append(",xtgate.").append(TableXtGateQueue.SPGATE)
				.append(",xtgate.").append(TableXtGateQueue.GATE_NAME)
				.append(",mtdatareport.SENDTYPE").append(", u.")
				.append(TableUserdata.SPTYPE).toString();

		return sql;
	}
	
	/**
	 * 各国详情获取表格
	 * 
	 * @return
	 */
	public static String getAreaTableSql(Integer mstype)
	{
		String sql = null;
		if(mstype == 0)
		{
			sql = new StringBuffer(" from ").append(TableMtDatareport.TABLE_NAME).append(" mtdatareport inner join ").append("(select AREACODE,AREANAME,CODE from ").append(TableAAreacode.TABLE_NAME).append(") areacode on mtdatareport.AREACODE = areacode.CODE  left join ")
			.append("(select USERID ,SPTYPE from ").append(TableUserdata.TABLE_NAME).append(") u on  mtdatareport.").append(TableMtDatareport.USER_ID).append(" = u.").append(TableUserdata.USER_ID).append(" left join ")
			.append("(select SPGATE,GATENAME,GATETYPE from ").append(TableXtGateQueue.TABLE_NAME).append(") xtgate on ").append(" mtdatareport.").append(TableMtDatareport.SPGATE).append(" = xtgate.").append(TableXtGateQueue.SPGATE)
			.append(" AND xtgate.GATETYPE=1 where mtdatareport.").append(TableMtDatareport.SPISUNCM).append("=5 ").toString();
		}
		else
		{
			sql = new StringBuffer(" from ").append(TableMmsDatareport.TABLE_NAME).append(" mtdatareport inner join ").append("(select AREACODE,AREANAME,CODE from ").append(TableAAreacode.TABLE_NAME).append(") areacode on mtdatareport.AREACODE = areacode.CODE  left join ")
			.append("(select USERID ,SPTYPE from ").append(TableUserdata.TABLE_NAME).append(") u on  mtdatareport.").append(TableMmsDatareport.USER_ID).append(" = u.").append(TableUserdata.USER_ID).append(" left join ")
			.append("(select SPGATE,GATENAME,GATETYPE from ").append(TableXtGateQueue.TABLE_NAME).append(") xtgate on ").append(" mtdatareport.").append(TableMmsDatareport.SPGATE).append(" = xtgate.").append(TableXtGateQueue.SPGATE)
			.append(" AND xtgate.GATETYPE=2 where mtdatareport.").append(TableMtDatareport.SPISUNCM).append("=5 ").toString();
		}
		return sql;
	}
	
	/**
	 * 各国详情查询条件
	 * 
	 * @param operatorsAreaMtDataReportVo
	 * @return
	 */
	public static String getAreaConditionSql(OperatorsAreaMtDataReportVo operatorsAreaMtDataReportVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		// 账号
		if(operatorsAreaMtDataReportVo.getSpID() != null && !"".equals(operatorsAreaMtDataReportVo.getSpID()))
		{
			conditionSql.append(" and mtdatareport.").append(TableMtDatareport.SPID).append("='").append(operatorsAreaMtDataReportVo.getSpID()).append("'");
		}
		//报表类型
		if (operatorsAreaMtDataReportVo.getReporttype() != null){
			//月报表
			if(operatorsAreaMtDataReportVo.getReporttype() == 0){
				// 年份
				if(operatorsAreaMtDataReportVo.getY() != null && !"".equals(operatorsAreaMtDataReportVo.getY()))
				{
					conditionSql.append(" and mtdatareport.").append(TableMtDatareport.Y).append("=").append(operatorsAreaMtDataReportVo.getY());
				}

				// 月份
				if(operatorsAreaMtDataReportVo.getImonth() != null && !"".equals(operatorsAreaMtDataReportVo.getImonth()))
				{
					conditionSql.append(" and mtdatareport.").append(TableMtDatareport.IMONTH).append("=").append(operatorsAreaMtDataReportVo.getImonth());
				}
			}
			//年报表
			else if(operatorsAreaMtDataReportVo.getReporttype() == 1)
			{
				// 年份
				if(operatorsAreaMtDataReportVo.getY() != null && !"".equals(operatorsAreaMtDataReportVo.getY()))
				{
					conditionSql.append(" and mtdatareport.").append(TableMtDatareport.Y).append("=").append(operatorsAreaMtDataReportVo.getY());
				}
			}
			//日报表
			else if(operatorsAreaMtDataReportVo.getReporttype() == 2)
			{
				// 开始时间
				if(operatorsAreaMtDataReportVo.getStartTime() != null && !"".equals(operatorsAreaMtDataReportVo.getStartTime()))
				{
					conditionSql.append(" and mtdatareport.").append(TableMtDatareport.IYMD).append(">=").append(operatorsAreaMtDataReportVo.getStartTime().replaceAll("-", ""));
				}
				
				//结束时间
				if(operatorsAreaMtDataReportVo.getEndTime() != null && !"".equals(operatorsAreaMtDataReportVo.getEndTime()))
				{
					conditionSql.append(" and mtdatareport.").append(TableMtDatareport.IYMD).append("<=").append(operatorsAreaMtDataReportVo.getEndTime().replaceAll("-", ""));
				}
			}
//			// 运营商
//			if(operatorsAreaMtDataReportVo.getSpisuncm() != null )
//			{
//				conditionSql.append(" and mtdatareport.").append(TableMtDatareport.SPISUNCM).append("=").append(operatorsAreaMtDataReportVo.getSpisuncm());
//			}
			//国家、地区代码
			if(operatorsAreaMtDataReportVo.getAreacode() != null && !"".equals(operatorsAreaMtDataReportVo.getAreacode()))
			{
				conditionSql.append(" and areacode.").append(TableAAreacode.AREACODE).append(" = ").append(operatorsAreaMtDataReportVo.getAreacode());
			}
			//国家、地区名称
			if(operatorsAreaMtDataReportVo.getAreaname() != null && !"".equals(operatorsAreaMtDataReportVo.getAreaname()))
			{
				conditionSql.append(" and areacode.").append(TableAAreacode.AREANAME).append(" like '%").append(operatorsAreaMtDataReportVo.getAreaname()).append("%'");
			}
		}

		String sql = conditionSql.toString();
		return sql;
	}
	
	/**
	 * 各国详情分组sql
	 * 
	 * @return
	 */
	
	public static String getAreaGroupBySql(OperatorsAreaMtDataReportVo operatorsAreaMtDataReportVo)
	{
		String	sql = "";
		if(operatorsAreaMtDataReportVo.getReporttype()==0) //月
		{
			sql = new StringBuffer(" group by mtdatareport.").append(TableMtDatareport.IMONTH).append(",mtdatareport.").append(TableMtDatareport.Y).append(",mtdatareport.").append(TableMtDatareport.SPID).append(",mtdatareport.").append(TableMtDatareport.SPISUNCM)
			.append(", areacode.").append(TableAAreacode.AREACODE).append(", areacode.").append(TableAAreacode.AREANAME).append(", xtgate.").append(TableXtGateQueue.SPGATE).append(",xtgate.").append(TableXtGateQueue.GATE_NAME).append(",mtdatareport.SENDTYPE").append(",u.").append(TableUserdata.SPTYPE)
			.toString();
		}
		else if(operatorsAreaMtDataReportVo.getReporttype()==1)//年
		{
			sql = new StringBuffer(" group by mtdatareport.").append(TableMtDatareport.Y).append(",mtdatareport.").append(TableMtDatareport.SPID).append(",mtdatareport.").append(TableMtDatareport.SPISUNCM)
			.append(", areacode.").append(TableAAreacode.AREACODE).append(", areacode.").append(TableAAreacode.AREANAME).append(", xtgate.").append(TableXtGateQueue.SPGATE).append(",xtgate.").append(TableXtGateQueue.GATE_NAME).append(",mtdatareport.SENDTYPE").append(",u.").append(TableUserdata.SPTYPE)
			.toString();
		}
		else if(operatorsAreaMtDataReportVo.getReporttype()==2) //日
		{
			sql = new StringBuffer(" group by mtdatareport.").append(TableMtDatareport.SPID).append(",mtdatareport.").append(TableMtDatareport.SPISUNCM)
			.append(", areacode.").append(TableAAreacode.AREACODE).append(", areacode.").append(TableAAreacode.AREANAME).append(", xtgate.").append(TableXtGateQueue.SPGATE).append(",xtgate.").append(TableXtGateQueue.GATE_NAME).append(",mtdatareport.SENDTYPE").append(",u.").append(TableUserdata.SPTYPE)
			.toString();
		}
		
		return sql;
	}

	

}
