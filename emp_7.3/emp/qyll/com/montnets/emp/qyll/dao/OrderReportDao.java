package com.montnets.emp.qyll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.qyll.dao.sql.DataQuerySQLFactory;
import com.montnets.emp.qyll.dao.sql.ReportQuerySql;
import com.montnets.emp.qyll.vo.LlOrderReportVo;
import com.montnets.emp.util.PageInfo;

public class OrderReportDao extends SuperDAO{

	public List<LlOrderReportVo> getllOrderReportSql(LlOrderReportVo llOrderReport,
			PageInfo pageInfo) throws Exception{
		
		String searchSql= getSearchSql(llOrderReport);
		
		if(searchSql == null || searchSql.trim().length() == 0)
		{
			EmpExecutionContext.error("套餐订购统计报表，获取查询sql为空。");
			return null;
		}
		List<LlOrderReportVo> returnList;
		if(pageInfo != null)
		{
			//总条数语句
			String countSql = "select count(*) totalcount FROM (" + searchSql + " ) A";
			returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
					LlOrderReportVo.class, searchSql, countSql, pageInfo,
					StaticValue.EMP_POOLNAME);
		}
		else
		{
			returnList = findVoListBySQL(LlOrderReportVo.class, searchSql, StaticValue.EMP_POOLNAME);
		}
		return returnList;
	}


	private String getSearchSql(LlOrderReportVo llOrderReport) throws ParseException {
		// 通过SQL工厂生产对应数据库的SQL实例
		ReportQuerySql reportQuerySql = new DataQuerySQLFactory().getSearchSql(StaticValue.DBTYPE);
		//获取查询sql
		String sendTime = null;
		String recvTime = null;
		String statisticsTime = null;
		String lastDay = null;
		String tableName = "LL_ORDER_DETAIL";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
		if(llOrderReport.getSendtime()!=null&&!"".equals(llOrderReport.getSendtime())){
			Date sendDate = sdfs.parse(llOrderReport.getSendtime()); 
			sendTime = sdf.format(sendDate);
		}
		if(llOrderReport.getRecvtime()!=null&&!"".equals(llOrderReport.getRecvtime())){
			Date recvDate = sdfs.parse(llOrderReport.getRecvtime()); 
			recvTime = sdf.format(recvDate);
		}
		if(llOrderReport.getStatisticsTime()!=null&&!"".equals(llOrderReport.getStatisticsTime())){
			if("2".equals(llOrderReport.getReportType())){
				Date statisticsDate = new SimpleDateFormat("yyyy-MM").parse(llOrderReport.getStatisticsTime()); 
				statisticsTime = new SimpleDateFormat("yyyyMM").format(statisticsDate);
				//获取该年该月份最后一天
				lastDay = getLastDayOfMonth(Integer.parseInt(statisticsTime.substring(0,4)),Integer.parseInt(statisticsTime.substring(4, 6)));
			}else{
				Date statisticsDate = new SimpleDateFormat("yyyy").parse(llOrderReport.getStatisticsTime()); 
				statisticsTime = new SimpleDateFormat("yyyy").format(statisticsDate);
			}
		}
		if(sendTime !=null){
			tableName = tableName+sendTime.substring(0,4);
		}else if(statisticsTime !=null){
			tableName = tableName+statisticsTime.substring(0,4);
		}
		//总的查询sql
		String searchSql = "";
		//获取查询列数
		String selectSql ="";
		//获取条件语句
		String conditionSql ="";
		//获取排序语句
		String orderbySql ="";
		//排序的参数
		String params = "";
		//获取详情
		if("1".equals(llOrderReport.getIsDel())){
			params = "REPORTDATE";
			selectSql = reportQuerySql.getllOrderDetailFiledSql();
			conditionSql =  reportQuerySql.getllOrderDetailConditionSql(llOrderReport,sendTime,recvTime,statisticsTime,tableName,lastDay);
			orderbySql = reportQuerySql.getOrderBySql(params);
		}else{
			params = "SUNMITNUM";
			selectSql = reportQuerySql.getllOrderFiledSql();
			conditionSql =  reportQuerySql.getllOrderConditionSql(llOrderReport,sendTime,recvTime,statisticsTime,tableName,lastDay);
			orderbySql = reportQuerySql.getOrderBySql(params);
		}
		searchSql = selectSql+conditionSql+orderbySql;
		return searchSql;
	}
	//获取该年该月份最后一天 用于获取月报表详情
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();  
        //设置年份  
        cal.set(Calendar.YEAR,year);  
        //设置月份  
        cal.set(Calendar.MONTH, month-1);  
        //获取某月最大天数  
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);  
        //设置日历中月份的最大天数  
        cal.set(Calendar.DAY_OF_MONTH, lastDay);  
        //格式化日期  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
        String lastDayOfMonth = sdf.format(cal.getTime());  
          
        return lastDayOfMonth; 
	}
	/*
	 * 获取合计数据
	 */
	public long[] findSumCount(LlOrderReportVo llOrderReport) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			String selectSql = "SELECT "+OrderReportDao.getPublicCountSql("TCREPORT")+" FROM  ";
			//获取查询sql
			String searchSql = getSearchSql(llOrderReport);
			if(searchSql == null || searchSql.trim().length() == 0)
			{
				EmpExecutionContext.error("套餐订购统计报表，获取查询sql为空。");
				return new long[]{0,0};
			}
			String totalSql = selectSql + " (" + searchSql + " ) TCREPORT";
			//合计提交总数
			long sunMitNum = 0L;
			//合计订购成功数
			long succNum = 0L;
			//合计订购失败数
			long faildNum = 0L;
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(totalSql);
			rs = ps.executeQuery();
			if (rs.next()) 
			{
				sunMitNum = rs.getLong("SUNMITNUM");
				succNum = rs.getLong("SUCCNUM");
				faildNum = rs.getLong("FAILDNUM");
			}
			long[] returnLong = new long[3];
			returnLong[0] = sunMitNum;
			returnLong[1] = succNum;
			returnLong[2] = faildNum;
			return returnLong;
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"根据查询的条件，进行合计处理异常");
			return new long[]{0,0};
		} 
		finally 
		{
			close(rs, ps, conn);
		}
	}
	/*
	 * 查询公共的合计数据
	 */
	private static String getPublicCountSql(String countName) {
		if(countName!=null&&!"".equals(countName)){
			return "SUM(" + countName + ".SUNMITNUM) SUNMITNUM,SUM(" + countName + ".SUCCNUM) SUCCNUM,SUM(" + countName + ".FAILDNUM) FAILDNUM";
		}else{
			return "SUM(SUNMITNUM) SUNMITNUM,SUM(SUCCNUM) SUCCNUM,SUM(FAILDNUM) FAILDNUM";
		}
	}

}
