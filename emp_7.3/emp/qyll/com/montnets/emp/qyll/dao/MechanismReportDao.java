package com.montnets.emp.qyll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.qyll.dao.sql.DataQuerySQLFactory;
import com.montnets.emp.qyll.dao.sql.ReportQuerySql;
import com.montnets.emp.qyll.vo.LlMechanismReportVo;
import com.montnets.emp.util.PageInfo;

public class MechanismReportDao  extends SuperDAO  {

	public List<LlMechanismReportVo> getllMechanismReportSql(
			LlMechanismReportVo llMechanismReport, PageInfo pageInfo) throws Exception{
		//获取查询sql
				String searchSql = getSearchSql(llMechanismReport);
				if(searchSql == null || searchSql.trim().length() == 0)
				{
					EmpExecutionContext.error("机构统计报表，获取查询sql为空。");
					return null;
				}
				List<LlMechanismReportVo> returnList;
				if(pageInfo != null)
				{
					//总条数语句
					String countSql = "select count(*) totalcount FROM (" + searchSql + " ) A";
					returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
							LlMechanismReportVo.class, searchSql, countSql, pageInfo,
							StaticValue.EMP_POOLNAME);
				}
				else
				{
					returnList = findVoListBySQL(LlMechanismReportVo.class, searchSql, StaticValue.EMP_POOLNAME);
				}
				return returnList;
	}

	private String getSearchSql(LlMechanismReportVo llMechanismReport) throws ParseException {
		// 通过SQL工厂生产对应数据库的SQL实例
		ReportQuerySql reportQuerySql = new DataQuerySQLFactory().getSearchSql(StaticValue.DBTYPE);
		String sendTime = null;
		String recvTime = null;
		String statisticsTime = null;
		String lastDay = null;
		String tableName = "LL_ORDER_DETAIL";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
		if(llMechanismReport.getSendtime()!=null&&!"".equals(llMechanismReport.getSendtime())){
			Date sendDate = sdfs.parse(llMechanismReport.getSendtime()); 
			sendTime = sdf.format(sendDate);
		}
		if(llMechanismReport.getRecvtime()!=null&&!"".equals(llMechanismReport.getRecvtime())){
			Date recvDate = sdfs.parse(llMechanismReport.getRecvtime()); 
			recvTime = sdf.format(recvDate);
		}
		if(llMechanismReport.getStatisticsTime()!=null&&!"".equals(llMechanismReport.getStatisticsTime())){
			if("2".equals(llMechanismReport.getReportType())){
				Date statisticsDate = new SimpleDateFormat("yyyy-MM").parse(llMechanismReport.getStatisticsTime()); 
				statisticsTime = new SimpleDateFormat("yyyyMM").format(statisticsDate);
				//获取该年该月份最后一天
				lastDay = OrderReportDao.getLastDayOfMonth(Integer.parseInt(statisticsTime.substring(0,4)),Integer.parseInt(statisticsTime.substring(4, 6)));
			}else{
				Date statisticsDate = new SimpleDateFormat("yyyy").parse(llMechanismReport.getStatisticsTime()); 
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
		if("1".equals(llMechanismReport.getIsDel())){
			params = "REPORTDATE";
			orderbySql = reportQuerySql.getOrderBySql(params);
			selectSql = reportQuerySql.getllMechanismDetailFiledSql();
			conditionSql = reportQuerySql.getllMechanismDetailConditionSql(llMechanismReport,sendTime,recvTime,statisticsTime,tableName,lastDay);
		}else{
			params = "SUNMITNUM";
			orderbySql = reportQuerySql.getOrderBySql(params);
			selectSql = reportQuerySql.getllMechanismFiledSql();
			conditionSql = reportQuerySql.getllMechanismConditionSql(llMechanismReport,sendTime,recvTime,statisticsTime,tableName,lastDay);
		}
		searchSql = selectSql  + conditionSql  + orderbySql;
		return searchSql;
	}

	public long[] findSumCount(LlMechanismReportVo llMechanismReport) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			String selectSql = "SELECT "+MechanismReportDao.getPublicCountSql("TCREPORT")+" FROM  ";
			//获取查询sql
			String searchSql = getSearchSql(llMechanismReport);
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

	private static String getPublicCountSql(String countName) {
		if(countName!=null&&!"".equals(countName)){
			return "SUM(" + countName + ".SUNMITNUM) SUNMITNUM,SUM(" + countName + ".SUCCNUM) SUCCNUM,SUM(" + countName + ".FAILDNUM) FAILDNUM";
		}else{
			return "SUM(SUNMITNUM) SUNMITNUM,SUM(SUCCNUM) SUCCNUM,SUM(FAILDNUM) FAILDNUM";
		}
	}

	public List<LfDep> findTopDepByUserId(String sysuserID, String corpCode)
			throws Exception {
		try
		{
			//拼接sql
			StringBuffer sql = new StringBuffer();
			sql.append("select dep.* from LF_DEP dep inner join LF_DOMINATION domination on dep.DEP_ID=domination.DEP_ID")
				.append(" where domination.USER_ID=").append(sysuserID)
				.append(" and dep.DEP_STATE=1")
				.append(" and dep.CORP_CODE='").append(corpCode).append("'")
				.append(" order by dep.DEP_ID asc");
			
			//只拿第一条顶级机构
			PageInfo pageInfo = new PageInfo();
			pageInfo.setPageSize(1);
			//返回结果
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LfDep.class, sql.toString(), null, pageInfo, StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询统计DAO，根据操作员id获取管辖机构的顶级机构对象集合，异常。sysuserID="+sysuserID+",corpCode="+corpCode);
			return null;
		}
	}

}
