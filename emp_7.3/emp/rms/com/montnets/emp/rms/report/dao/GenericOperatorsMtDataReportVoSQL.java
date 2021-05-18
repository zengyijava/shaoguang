package com.montnets.emp.rms.report.dao;

import com.montnets.emp.rms.report.table.TableLfRmsReport;
import com.montnets.emp.rms.report.vo.LfRmsReportVo;

/**
 * sql工具方法
 * @project p_ydcx
 * @author lvxin 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2018-01-13 下午06:51:21
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
		String sql = new StringBuffer("select sum(lfrmsreport.").append(TableLfRmsReport.ICOUNT).append(") ICOUNT")
				.append(",sum(lfrmsreport.").append(TableLfRmsReport.RSUCC).append(") RSUCC")
				.append(",sum(lfrmsreport.").append(TableLfRmsReport.RFAIL).append(") RFAIL")
				.append(",max(lfrmsreport.").append(TableLfRmsReport.IYMD).append(") ").append(TableLfRmsReport.IYMD)
				.append(",max(substring(CONVERT(varchar(10),"+TableLfRmsReport.IYMD+"),1,4)) ").append("Y")
				.append(",max(substring(CONVERT(varchar(10),"+TableLfRmsReport.IYMD+"),5,2)) ").append("IMONTH")
				.append(",lfrmsreport.").append(TableLfRmsReport.DEGREE)
				.append(",lfrmsreport.").append(TableLfRmsReport.SPISUNCM)
				.append(",lfrmsreport.").append(TableLfRmsReport.USER_ID).toString();
				
		return sql;
	}

	/**
	 * 月报表获取表格
	 * 
	 * @return
	 */
	public static String getMonthTableSql()
	{
		String sql = null;
		sql = new StringBuffer(" from ").append(TableLfRmsReport.TABLE_NAME).append(" lfrmsreport ").toString();
		return sql;
	}

	/**
	 * 月报表回去查询条件
	 * 
	 * @param 
	 * @return
	 */
	public static String getMonthConditionSql(LfRmsReportVo lfRmsReportVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		// 账号
		if(lfRmsReportVo.getSpID() != null && !"".equals(lfRmsReportVo.getSpID()))
		{
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.USER_ID).append("='").append(lfRmsReportVo.getSpID()).append("'");
		}
		
		// 年份
		if(lfRmsReportVo.getY() != null && !"".equals(lfRmsReportVo.getY()))
		{
			conditionSql.append(" and substring(CONVERT(varchar(10),lfrmsreport.").append(TableLfRmsReport.IYMD).append("),1,4)").append("=").append(lfRmsReportVo.getY());
		}

		// 月份
		if(lfRmsReportVo.getImonth() != null && !"".equals(lfRmsReportVo.getImonth()))
		{
			conditionSql.append(" and substring(CONVERT(varchar(10),lfrmsreport.").append(TableLfRmsReport.IYMD).append("),5,2)").append("=").append(lfRmsReportVo.getImonth());
		}

		// 运营商
		if(lfRmsReportVo.getSpisuncm() != null )
		{
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.SPISUNCM).append("='").append(lfRmsReportVo.getSpisuncm()).append("'");
		}
		
		//档位
		if(lfRmsReportVo.getDegree() != null ){
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.DEGREE).append("=").append(lfRmsReportVo.getDegree());
		}
		
		//企业编码
		if(lfRmsReportVo.getCorpCode() != null && !"".equals(lfRmsReportVo.getCorpCode()) && !lfRmsReportVo.getCorpCode().equals("100000")){
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.CORP_CODE).append("='").append(lfRmsReportVo.getCorpCode()).append("'");
		}
		
		String sql = conditionSql.toString();
		return sql;
	}

	public static String getMonthGroupBySql(LfRmsReportVo lfRmsReportVo)
	{
		String sql="";
		if(!lfRmsReportVo.getIsDes()){
			sql = new StringBuffer(" group by ")
					.append("substring(CONVERT(varchar(10),lfrmsreport.")
					.append(TableLfRmsReport.IYMD)
					.append("),1,4),")
					.append("substring(CONVERT(varchar(10),lfrmsreport.")
					.append(TableLfRmsReport.IYMD)
					.append("),5,2),")
					.append("lfrmsreport.")
					.append(TableLfRmsReport.SPISUNCM)
					.append(",lfrmsreport.")
					.append(TableLfRmsReport.DEGREE)
					.append(",lfrmsreport.")
					.append(TableLfRmsReport.USER_ID)
					.toString();
		}
		else
		{
			sql = new StringBuffer(" group by ")
					.append("substring(CONVERT(varchar(10),lfrmsreport.")
					.append(TableLfRmsReport.IYMD)
					.append("),1,4),")
					.append("substring(CONVERT(varchar(10),lfrmsreport.")
					.append(TableLfRmsReport.IYMD)
					.append("),5,2)")
					.append(",lfrmsreport.")
					.append(TableLfRmsReport.IYMD)
					.append(",lfrmsreport.")
					.append(TableLfRmsReport.SPISUNCM)
					.append(",lfrmsreport.")
					.append(TableLfRmsReport.DEGREE)
					.append(",lfrmsreport.")
					.append(TableLfRmsReport.USER_ID)
					.toString();
		}
		
		return sql;
	}

	/**
	 * 月报表获取排序sql
	 * 
	 * @return
	 */
	public static String getMonthOrderBySql(LfRmsReportVo lfRmsReportVo)
	{
		String sql ="";
		if(!lfRmsReportVo.getIsDes())
		{
			sql=new StringBuffer(" order by lfrmsreport.").append(TableLfRmsReport.DEGREE).append(" asc").toString();
		}
		else
		{
			sql=new StringBuffer(" order by lfrmsreport.").append(TableLfRmsReport.IYMD).append(" desc").toString();
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
		String sql = new StringBuffer("select sum(lfrmsreport.").append(TableLfRmsReport.ICOUNT).append(") ICOUNT")
				.append(",sum(lfrmsreport.").append(TableLfRmsReport.RSUCC).append(") RSUCC")
				.append(",sum(lfrmsreport.").append(TableLfRmsReport.RFAIL).append(") RFAIL")
				.append(",max(lfrmsreport.").append(TableLfRmsReport.IYMD).append(") ").append(TableLfRmsReport.IYMD)
				.append(",max(substring(CONVERT(varchar(10),"+TableLfRmsReport.IYMD+"),1,4)) ").append("Y")
				.append(",max(substring(CONVERT(varchar(10),"+TableLfRmsReport.IYMD+"),5,2)) ").append("IMONTH")
				.append(",lfrmsreport.").append(TableLfRmsReport.DEGREE)
				.append(",lfrmsreport.").append(TableLfRmsReport.SPISUNCM)
				.append(",lfrmsreport.").append(TableLfRmsReport.USER_ID).toString();
				
		return sql;
	}

	/**
	 * 年报表表格
	 * 
	 * @return
	 */
	public static String getYearTableSql()
	{

		String sql = null;
		sql = new StringBuffer(" from ").append(TableLfRmsReport.TABLE_NAME).append(" lfrmsreport ").toString();
		return sql;
	}

	/**
	 * 年报表查询条件
	 * 
	 * @param lfRmsReportVo
	 * @return
	 */
	public static String getYearConditionSql(LfRmsReportVo lfRmsReportVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		// sp账号
		if(lfRmsReportVo.getSpID() != null && !"".equals(lfRmsReportVo.getSpID()))
		{
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.USER_ID).append("='").append(lfRmsReportVo.getSpID()).append("'");
		}

		// 年份
		if(lfRmsReportVo.getY() != null && !"".equals(lfRmsReportVo.getY()))
		{
			conditionSql.append("and substring(CONVERT(varchar(10),lfrmsreport."+TableLfRmsReport.IYMD+"),1,4)= ").append(lfRmsReportVo.getY());
		}

		// 运营商
		if(lfRmsReportVo.getSpisuncm() != null )
		{
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.SPISUNCM).append("='").append(lfRmsReportVo.getSpisuncm()).append("'");
		}
		
		//档位
		if(lfRmsReportVo.getDegree() != null ){
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.DEGREE).append("=").append(lfRmsReportVo.getDegree());
		}

		//企业编码
		if(lfRmsReportVo.getCorpCode() != null && !"".equals(lfRmsReportVo.getCorpCode()) && !lfRmsReportVo.getCorpCode().equals("100000")){
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.CORP_CODE).append("='").append(lfRmsReportVo.getCorpCode()).append("'");
		}
				
		String sql = conditionSql.toString();
		return sql;
	}

	/**
	 * 年报表分组sql
	 * 
	 * @return
	 */
	public static String getYearGroupBySql(LfRmsReportVo lfRmsReportVo)
	{
		String sql="";
		/*if(!lfRmsReportVo.getIsDes()){*/
		sql = new StringBuffer(" group by ")
				.append("substring(CONVERT(varchar(10),lfrmsreport.")
				.append(TableLfRmsReport.IYMD)
				.append("),1,4)")
				.append(",lfrmsreport.")
				.append(TableLfRmsReport.SPISUNCM)
				.append(",lfrmsreport.")
				.append(TableLfRmsReport.DEGREE)
				.append(",lfrmsreport.")
				.append(TableLfRmsReport.USER_ID).toString();
		
		return sql;
	}

	/**
	 * 年报表排序
	 * 
	 * @return
	 */
	public static String getYearOrderBySql(LfRmsReportVo lfRmsReportVo)
	{	
		String sql ="";
		if(!lfRmsReportVo.getIsDes()) {
			sql= " order by lfrmsreport." + TableLfRmsReport.DEGREE + " asc";
		} else {
			sql= " order by "+ "substring(CONVERT(varchar(10),lfrmsreport.IYMD),1,4)" + " desc";
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
		String sql =new StringBuffer("select sum(lfrmsreport.").append(TableLfRmsReport.ICOUNT).append(") ICOUNT")
				.append(",sum(lfrmsreport.").append(TableLfRmsReport.RFAIL).append(") RFAIL")
				.append(",sum(lfrmsreport.").append(TableLfRmsReport.RSUCC).append(") RSUCC")
				.append(",max(lfrmsreport.").append(TableLfRmsReport.IYMD).append(") ").append(TableLfRmsReport.IYMD)
				.append(",lfrmsreport.").append(TableLfRmsReport.DEGREE)
				.append(",lfrmsreport.").append(TableLfRmsReport.SPISUNCM)
				.append(",lfrmsreport.").append(TableLfRmsReport.USER_ID)
				.toString();
		return sql;
	}
	
	/**
	 * 日报表表名
	 * 
	 * @return
	 */
	public static String getDaysTableSql()
	{

		String sql = null;
		
			sql = new StringBuffer(" from ").append(TableLfRmsReport.TABLE_NAME).append(" lfrmsreport ").toString();
		
		return sql;
	}

	/**
	 * 日报表查询条件
	 * 
	 * @param lfRmsReportVo
	 * @return
	 */
	public static String getDaysConditionSql(LfRmsReportVo lfRmsReportVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		// sp账号
		if(lfRmsReportVo.getSpID() != null && !"".equals(lfRmsReportVo.getSpID()))
		{
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.USER_ID).append("='").append(lfRmsReportVo.getSpID()).append("'");
		}

		// 开始时间
		if(lfRmsReportVo.getStartTime() != null && !"".equals(lfRmsReportVo.getStartTime()))
		{
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.IYMD).append(">=").append(lfRmsReportVo.getStartTime().replaceAll("-", ""));
		}
		
		//结束时间
		if(lfRmsReportVo.getEndTime() != null && !"".equals(lfRmsReportVo.getEndTime()))
		{
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.IYMD).append("<=").append(lfRmsReportVo.getEndTime().replaceAll("-", ""));
		}

		// 运营商
		if(lfRmsReportVo.getSpisuncm() != null )
		{
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.SPISUNCM).append("='").append(lfRmsReportVo.getSpisuncm()).append("'");
		}
		//档位
		if(lfRmsReportVo.getDegree() != null ){
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.DEGREE).append("=").append(lfRmsReportVo.getDegree());
		}
		//企业编码
		if(lfRmsReportVo.getCorpCode() != null && !"".equals(lfRmsReportVo.getCorpCode()) && !lfRmsReportVo.getCorpCode().equals("100000")){
			conditionSql.append(" and lfrmsreport.").append(TableLfRmsReport.CORP_CODE).append("='").append(lfRmsReportVo.getCorpCode()).append("'");
		}
		String sql = conditionSql.toString();
		return sql;
	}
	
	/**
	 * 日报表分组sql
	 * 
	 * @return
	 */
	public static String getDaysGroupBySql(LfRmsReportVo lfRmsReportVo)
	{
		String sql="";
		if(!lfRmsReportVo.getIsDes()){
			sql = new StringBuffer(" group by lfrmsreport.")
					.append(TableLfRmsReport.SPISUNCM)
					.append(",lfrmsreport.")
					.append(TableLfRmsReport.DEGREE)
					.append(",lfrmsreport.")
					.append(TableLfRmsReport.USER_ID).toString();
		}
		
		return sql;
	}

	/**
	 * 日报表排序
	 * 
	 * @return
	 */
	public static String getDaysOrderBySql(LfRmsReportVo lfRmsReportVo)
	{
		String sql ="";
		if(!lfRmsReportVo.getIsDes())
		{
			sql=new StringBuffer(" order by lfrmsreport.").append(TableLfRmsReport.DEGREE).append(" asc").toString();
		}
		else
		{
			sql=new StringBuffer(" order by lfrmsreport.").append(TableLfRmsReport.IYMD).append(" desc").toString();
		}
		return sql;
	}
	
	
	
	

	

}
