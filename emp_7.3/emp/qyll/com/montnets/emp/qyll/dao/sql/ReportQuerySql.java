package com.montnets.emp.qyll.dao.sql;

import com.montnets.emp.qyll.entity.LlProduct;
import com.montnets.emp.qyll.vo.LlMechanismReportVo;
import com.montnets.emp.qyll.vo.LlOperatorReportVo;
import com.montnets.emp.qyll.vo.LlOrderReportVo;

/**
 * 父类sql,基于Sqlserver编写
 * @author pengyc
 *
 */
public class ReportQuerySql {
	
	public String getOrderBySql(String params) {
		return " ORDER BY "+params+" DESC ";
	}

	public String getllOrderDetailFiledSql() {
		return "SELECT  REPORTDATE,SUNMITNUM,SUCCNUM,FAILDNUM,pro.PRODUCTNAME,pro.ISP,pro.PRODUCTID";
	}

	public String getllOrderDetailConditionSql(LlOrderReportVo llOrderReport,
			String sendTime, String recvTime, String statisticsTime,
			String tableName,String lastDay) {
		StringBuilder conditionSql = null;
		if("1".equals(llOrderReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT REPORTDATE REPORTDATE,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE PRODUCTID='").append(llOrderReport.getProductId()).append("' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY REPORTDATE) x ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND PRODUCTID='").append(llOrderReport.getProductId()).append("' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY REPORTDATE) y ON x.REPORTDATE=y.REPORTDATE2 ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND PRODUCTID='").append(llOrderReport.getProductId()).append("' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY REPORTDATE) z ON x.REPORTDATE = z.REPORTDATE3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID ='").append(llOrderReport.getProductId()).append("' WHERE  pro.status =1 AND pro.ECID =").append(llOrderReport.getEcid());
		}else if("2".equals(llOrderReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT REPORTDATE REPORTDATE,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE PRODUCTID='").append(llOrderReport.getProductId()).append("' AND REPORTDATE >= '").append(statisticsTime).append("01' AND REPORTDATE <= '").append(lastDay).append("' GROUP BY REPORTDATE) x ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND PRODUCTID='").append(llOrderReport.getProductId()).append("' AND REPORTDATE >= '").append(statisticsTime).append("01' AND REPORTDATE <= '").append(lastDay).append("' GROUP BY REPORTDATE) y ON x.REPORTDATE=y.REPORTDATE2 ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND PRODUCTID='").append(llOrderReport.getProductId()).append("' AND REPORTDATE >= '").append(statisticsTime).append("01' AND REPORTDATE <= '").append(lastDay).append("' GROUP BY REPORTDATE) z ON x.REPORTDATE = z.REPORTDATE3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID ='").append(llOrderReport.getProductId()).append("' WHERE  pro.status =1 AND pro.ECID =").append(llOrderReport.getEcid());
		}else{
			conditionSql = new StringBuilder(" FROM ").append("(SELECT SUBSTRING(REPORTDATE,1,6) REPORTDATE,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE PRODUCTID='").append(llOrderReport.getProductId()).append("' AND SUBSTRING(REPORTDATE,1,6) >= '").append(statisticsTime).append("01' AND SUBSTRING(REPORTDATE,1,6) <= '").append(statisticsTime).append("12' GROUP BY SUBSTRING(REPORTDATE, 1, 6)) x ")
					.append("LEFT JOIN (SELECT SUBSTRING(REPORTDATE,1,6) REPORTDATE2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND PRODUCTID='").append(llOrderReport.getProductId()).append("' AND SUBSTRING(REPORTDATE,1,6) >= '").append(statisticsTime).append("01' AND SUBSTRING(REPORTDATE,1,6) <= '").append(statisticsTime).append("12' GROUP BY SUBSTRING(REPORTDATE, 1, 6)) y ON x.REPORTDATE=y.REPORTDATE2 ")
					.append("LEFT JOIN (SELECT SUBSTRING(REPORTDATE,1,6) REPORTDATE3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND PRODUCTID='").append(llOrderReport.getProductId()).append("' AND SUBSTRING(REPORTDATE,1,6) >= '").append(statisticsTime).append("01' AND SUBSTRING(REPORTDATE,1,6) <= '").append(statisticsTime).append("12' GROUP BY SUBSTRING(REPORTDATE, 1, 6)) z ON x.REPORTDATE = z.REPORTDATE3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID ='").append(llOrderReport.getProductId()).append("' WHERE  pro.status =1 AND pro.ECID =").append(llOrderReport.getEcid());
		}
		if(!"".equals(llOrderReport.getProductName()) && llOrderReport.getProductName() != null){
			conditionSql = conditionSql.append(" AND PRODUCTNAME LIKE '%").append(llOrderReport.getProductName()).append("%'");
		}
		if(!"".equals(llOrderReport.getProductId()) && llOrderReport.getProductId() != null){
			conditionSql = conditionSql.append(" AND PRO.PRODUCTID ='").append(llOrderReport.getProductId()).append("'");
		}
		if(!"9999".equals(llOrderReport.getIsp())){
			conditionSql = conditionSql.append(" AND ISP ='").append(llOrderReport.getIsp()).append("'");
		}
		return conditionSql.toString();
	}

	public String getllOrderFiledSql() {
		return "SELECT REPORTDATE,SUNMITNUM,SUCCNUM,FAILDNUM,pro.PRODUCTNAME PRODUCTNAME,pro.ISP ISP,pro.PRODUCTID PRODUCTID";
	}
	
	public String getllOrderConditionSql(LlOrderReportVo llOrderReport,
			String sendTime, String recvTime, String statisticsTime,
			String tableName, String lastDay) {
		StringBuilder conditionSql = null;
		if("1".equals(llOrderReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT max(REPORTDATE) REPORTDATE, max(PRODUCTID) PRODUCTID,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE 1=1 AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY PRODUCTID) x ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY PRODUCTID) y ON x.PRODUCTID=y.PRODUCTID2 ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY PRODUCTID) z ON x.PRODUCTID = z.PRODUCTID3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID = x.PRODUCTID WHERE pro.status =1 AND pro.ECID =").append(llOrderReport.getEcid());
		}else if("2".equals(llOrderReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT max(REPORTDATE) REPORTDATE, max(PRODUCTID) PRODUCTID,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE 1=1 AND SUBSTRING(REPORTDATE,1,6)= '").append(statisticsTime).append("' GROUP BY PRODUCTID) x ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND SUBSTRING(REPORTDATE,1,6)= '").append(statisticsTime).append("' GROUP BY PRODUCTID) y ON x.PRODUCTID=y.PRODUCTID2 ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND SUBSTRING(REPORTDATE,1,6)= '").append(statisticsTime).append("' GROUP BY PRODUCTID) z ON x.PRODUCTID = z.PRODUCTID3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID = x.PRODUCTID WHERE pro.status =1 AND pro.ECID =").append(llOrderReport.getEcid());
		}else{
			conditionSql = new StringBuilder(" FROM ").append("(SELECT max(REPORTDATE) REPORTDATE, max(PRODUCTID) PRODUCTID,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE 1=1 AND SUBSTRING(REPORTDATE,1,4)= '").append(statisticsTime.substring(0,4)).append("' GROUP BY PRODUCTID) x ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND SUBSTRING(REPORTDATE,1,4)= '").append(statisticsTime.substring(0,4)).append("' GROUP BY PRODUCTID) y ON x.PRODUCTID=y.PRODUCTID2 ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND SUBSTRING(REPORTDATE,1,4)= '").append(statisticsTime.substring(0,4)).append("' GROUP BY PRODUCTID) z ON x.PRODUCTID = z.PRODUCTID3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID = x.PRODUCTID WHERE pro.status =1 AND pro.ECID =").append(llOrderReport.getEcid());
		}
		if(!"".equals(llOrderReport.getProductName()) && llOrderReport.getProductName() != null){
			conditionSql = conditionSql.append(" AND PRODUCTNAME LIKE '%").append(llOrderReport.getProductName()).append("%'");
		}
		if(!"".equals(llOrderReport.getProductId()) && llOrderReport.getProductId() != null){
			conditionSql = conditionSql.append(" AND PRO.PRODUCTID ='").append(llOrderReport.getProductId()).append("'");
		}
		if(!"9999".equals(llOrderReport.getIsp())){
			conditionSql = conditionSql.append(" AND ISP ='").append(llOrderReport.getIsp()).append("'");
		}
		return conditionSql.toString();
	}

	public String getllOperatorDetailFiledSql() {
		return "SELECT  REPORTDATE,SUNMITNUM,SUCCNUM,FAILDNUM,pro.PRODUCTNAME PRODUCTNAME,pro.ISP ISP,pro.PRODUCTID PRODUCTID,SYSUSER.NAME UNAME,DEP.DEP_NAME DEPNAM ";
	}

	public String getllOperatorDetailConditionSql(
			LlOperatorReportVo llOperatorReport, String sendTime,
			String recvTime, String statisticsTime, String tableName,
			String lastDay,String uName) {
		StringBuilder conditionSql = null;
		if("1".equals(llOperatorReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT REPORTDATE REPORTDATE,count(1) SUNMITNUM,max(USER_ID) USER_ID FROM ").append(tableName).append(" WHERE PRODUCTID='").append(llOperatorReport.getProductId()).append("' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY REPORTDATE) x ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND PRODUCTID='").append(llOperatorReport.getProductId()).append("' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY REPORTDATE) y ON x.REPORTDATE=y.REPORTDATE2 ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND PRODUCTID='").append(llOperatorReport.getProductId()).append("' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY REPORTDATE) z ON x.REPORTDATE = z.REPORTDATE3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID ='").append(llOperatorReport.getProductId()).append("'  LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_ID = x.USER_ID LEFT JOIN LF_DEP DEP ON SYSUSER.DEP_ID = DEP.DEP_ID WHERE  pro.status =1 AND pro.ECID =").append(llOperatorReport.getEcid());
		}else if("2".equals(llOperatorReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT REPORTDATE REPORTDATE,count(1) SUNMITNUM,max(USER_ID) USER_ID FROM ").append(tableName).append(" WHERE PRODUCTID='").append(llOperatorReport.getProductId()).append("' AND REPORTDATE >= '").append(statisticsTime).append("01' AND REPORTDATE <= '").append(lastDay).append("' GROUP BY REPORTDATE) x ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND PRODUCTID='").append(llOperatorReport.getProductId()).append("' AND REPORTDATE >= '").append(statisticsTime).append("01' AND REPORTDATE <= '").append(lastDay).append("' GROUP BY REPORTDATE) y ON x.REPORTDATE=y.REPORTDATE2 ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND PRODUCTID='").append(llOperatorReport.getProductId()).append("' AND REPORTDATE >= '").append(statisticsTime).append("01' AND REPORTDATE <= '").append(lastDay).append("' GROUP BY REPORTDATE) z ON x.REPORTDATE = z.REPORTDATE3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID ='").append(llOperatorReport.getProductId()).append("'  LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_ID = x.USER_ID LEFT JOIN LF_DEP DEP ON SYSUSER.DEP_ID = DEP.DEP_ID WHERE  pro.status =1 AND pro.ECID =").append(llOperatorReport.getEcid());
		}else{
			conditionSql = new StringBuilder(" FROM ").append("(SELECT SUBSTRING(REPORTDATE,1,6) REPORTDATE,count(1) SUNMITNUM,max(USER_ID) USER_ID FROM ").append(tableName).append(" WHERE PRODUCTID='").append(llOperatorReport.getProductId()).append("' AND SUBSTRING(REPORTDATE,1,6) >= '").append(statisticsTime).append("01' AND SUBSTRING(REPORTDATE,1,6) <= '").append(statisticsTime).append("12' GROUP BY SUBSTRING(REPORTDATE, 1, 6)) x ")
					.append("LEFT JOIN (SELECT SUBSTRING(REPORTDATE,1,6) REPORTDATE2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND PRODUCTID='").append(llOperatorReport.getProductId()).append("' AND SUBSTRING(REPORTDATE,1,6) >= '").append(statisticsTime).append("01' AND SUBSTRING(REPORTDATE,1,6) <= '").append(statisticsTime).append("12' GROUP BY SUBSTRING(REPORTDATE, 1, 6)) y ON x.REPORTDATE=y.REPORTDATE2 ")
					.append("LEFT JOIN (SELECT SUBSTRING(REPORTDATE,1,6) REPORTDATE3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND PRODUCTID='").append(llOperatorReport.getProductId()).append("' AND SUBSTRING(REPORTDATE,1,6) >= '").append(statisticsTime).append("01' AND SUBSTRING(REPORTDATE,1,6) <= '").append(statisticsTime).append("12' GROUP BY SUBSTRING(REPORTDATE, 1, 6)) z ON x.REPORTDATE = z.REPORTDATE3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID ='").append(llOperatorReport.getProductId()).append("' LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_ID = x.USER_ID LEFT JOIN LF_DEP DEP ON SYSUSER.DEP_ID = DEP.DEP_ID WHERE  pro.status =1 AND pro.ECID =").append(llOperatorReport.getEcid());
		}
		if(!"".equals(llOperatorReport.getProductName()) && llOperatorReport.getProductName() != null){
			conditionSql = conditionSql.append(" AND PRODUCTNAME LIKE '%").append(llOperatorReport.getProductName()).append("%'");
		}
		if(!"".equals(llOperatorReport.getProductId()) && llOperatorReport.getProductId() != null){
			conditionSql = conditionSql.append(" AND PRO.PRODUCTID ='").append(llOperatorReport.getProductId()).append("'");
		}
		if(!"9999".equals(llOperatorReport.getIsp())){
			conditionSql = conditionSql.append(" AND ISP ='").append(llOperatorReport.getIsp()).append("'");
		}
		if(!"-9999".equals(llOperatorReport.getDepNam())){
			conditionSql = conditionSql.append(" AND DEP_NAME ='").append(llOperatorReport.getDepNam()).append("'");
		}
		if(!"-9999".equals(uName) &&uName.contains(",")){
			conditionSql = conditionSql.append(" AND NAME in (").append(uName).append(")");
		}else if(!"-9999".equals(uName) && !uName.contains(",")){
			conditionSql = conditionSql.append(" AND NAME =").append(uName);
		}
		return conditionSql.toString();
	}

	public String getllOperatorFiledSql() {
		return "SELECT REPORTDATE,SUNMITNUM,SUCCNUM,FAILDNUM,pro.PRODUCTNAME PRODUCTNAME,pro.ISP ISP,pro.PRODUCTID PRODUCTID,SYSUSER.NAME UNAME,DEP.DEP_NAME DEPNAM ";
	}

	public String getllOperatorConditionSql(
			LlOperatorReportVo llOperatorReport, String sendTime,
			String recvTime, String statisticsTime, String tableName,
			String lastDay, String uName) {
		StringBuilder conditionSql = null;
		if("1".equals(llOperatorReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT max(REPORTDATE) REPORTDATE, max(PRODUCTID) PRODUCTID,max(USER_ID) USER_ID,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE 1=1 AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY PRODUCTID) x ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY PRODUCTID) y ON x.PRODUCTID=y.PRODUCTID2 ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY PRODUCTID) z ON x.PRODUCTID = z.PRODUCTID3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID = x.PRODUCTID LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_ID = x.USER_ID LEFT JOIN LF_DEP DEP ON SYSUSER.DEP_ID = DEP.DEP_ID WHERE pro.status =1 AND pro.ECID =").append(llOperatorReport.getEcid());
		}else if("2".equals(llOperatorReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT max(REPORTDATE) REPORTDATE, max(PRODUCTID) PRODUCTID,max(USER_ID) USER_ID,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE 1=1 AND SUBSTRING(REPORTDATE,1,6)= '").append(statisticsTime).append("' GROUP BY PRODUCTID) x ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND SUBSTRING(REPORTDATE,1,6)= '").append(statisticsTime).append("' GROUP BY PRODUCTID) y ON x.PRODUCTID=y.PRODUCTID2 ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND SUBSTRING(REPORTDATE,1,6)= '").append(statisticsTime).append("' GROUP BY PRODUCTID) z ON x.PRODUCTID = z.PRODUCTID3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID = x.PRODUCTID LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_ID = x.USER_ID LEFT JOIN LF_DEP DEP ON SYSUSER.DEP_ID = DEP.DEP_ID WHERE pro.status =1 AND pro.ECID =").append(llOperatorReport.getEcid());
		}else{
			conditionSql = new StringBuilder(" FROM ").append("(SELECT max(REPORTDATE) REPORTDATE, max(PRODUCTID) PRODUCTID,max(USER_ID) USER_ID,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE 1=1 AND SUBSTRING(REPORTDATE,1,4)= '").append(statisticsTime.substring(0,4)).append("' GROUP BY PRODUCTID) x ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND SUBSTRING(REPORTDATE,1,4)= '").append(statisticsTime.substring(0,4)).append("' GROUP BY PRODUCTID) y ON x.PRODUCTID=y.PRODUCTID2 ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND SUBSTRING(REPORTDATE,1,4)= '").append(statisticsTime.substring(0,4)).append("' GROUP BY PRODUCTID) z ON x.PRODUCTID = z.PRODUCTID3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID = x.PRODUCTID LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_ID = x.USER_ID LEFT JOIN LF_DEP DEP ON SYSUSER.DEP_ID = DEP.DEP_ID WHERE pro.status =1 AND pro.ECID =").append(llOperatorReport.getEcid());
		}
		if(!"".equals(llOperatorReport.getProductName()) && llOperatorReport.getProductName() != null){
			conditionSql = conditionSql.append(" AND PRODUCTNAME LIKE '%").append(llOperatorReport.getProductName()).append("%'");
		}
		if(!"".equals(llOperatorReport.getProductId()) && llOperatorReport.getProductId() != null){
			conditionSql = conditionSql.append(" AND PRO.PRODUCTID ='").append(llOperatorReport.getProductId()).append("'");
		}
		if(!"9999".equals(llOperatorReport.getIsp())){
			conditionSql = conditionSql.append(" AND ISP ='").append(llOperatorReport.getIsp()).append("'");
		}
		if(!"-9999".equals(llOperatorReport.getDepNam())){
			conditionSql = conditionSql.append(" AND DEP_NAME ='").append(llOperatorReport.getDepNam()).append("'");
		}
		if(!"-9999".equals(uName) &&uName.contains(",")){
			conditionSql = conditionSql.append(" AND NAME in (").append(uName).append(")");
		}else if(!"-9999".equals(uName) && !uName.contains(",")){
			conditionSql = conditionSql.append(" AND NAME =").append(uName);
		}
		return conditionSql.toString();
	}

	public String getllMechanismDetailFiledSql() {
		return "SELECT  REPORTDATE,SUNMITNUM,SUCCNUM,FAILDNUM,pro.PRODUCTNAME PRODUCTNAME,pro.ISP ISP,pro.PRODUCTID PRODUCTID,DEP.DEP_NAME DEPNAM ";
	}

	public String getllMechanismDetailConditionSql(
			LlMechanismReportVo llMechanismReport, String sendTime,
			String recvTime, String statisticsTime, String tableName,
			String lastDay) {
		StringBuilder conditionSql = null;
		if("1".equals(llMechanismReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT REPORTDATE REPORTDATE,count(1) SUNMITNUM,max(ORG_ID) ORG_ID FROM ").append(tableName).append(" WHERE PRODUCTID='").append(llMechanismReport.getProductId()).append("' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY REPORTDATE) x ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND PRODUCTID='").append(llMechanismReport.getProductId()).append("' AND REPORTDATE >= ").append(sendTime).append(" AND REPORTDATE <= '").append(recvTime).append("' GROUP BY REPORTDATE) y ON x.REPORTDATE=y.REPORTDATE2 ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND PRODUCTID='").append(llMechanismReport.getProductId()).append("' AND REPORTDATE >= ").append(sendTime).append(" AND REPORTDATE <= '").append(recvTime).append("' GROUP BY REPORTDATE) z ON x.REPORTDATE = z.REPORTDATE3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID ='").append(llMechanismReport.getProductId()).append("' LEFT JOIN LF_DEP DEP ON DEP.DEP_ID = x.ORG_ID WHERE  pro.status =1 AND pro.ECID =").append(llMechanismReport.getEcid());
		}else if("2".equals(llMechanismReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT REPORTDATE REPORTDATE,count(1) SUNMITNUM,max(ORG_ID) ORG_ID FROM ").append(tableName).append(" WHERE PRODUCTID='").append(llMechanismReport.getProductId()).append("' AND REPORTDATE >= '").append(statisticsTime).append("01' AND REPORTDATE <= '").append(lastDay).append("' GROUP BY REPORTDATE) x ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND PRODUCTID='").append(llMechanismReport.getProductId()).append("' AND REPORTDATE >= '").append(statisticsTime).append("01' AND REPORTDATE <= '").append(lastDay).append("' GROUP BY REPORTDATE) y ON x.REPORTDATE=y.REPORTDATE2 ")
					.append("LEFT JOIN (SELECT REPORTDATE REPORTDATE3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND PRODUCTID='").append(llMechanismReport.getProductId()).append("' AND REPORTDATE >= '").append(statisticsTime).append("01' AND REPORTDATE <= '").append(lastDay).append("' GROUP BY REPORTDATE) z ON x.REPORTDATE = z.REPORTDATE3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID ='").append(llMechanismReport.getProductId()).append("' LEFT JOIN LF_DEP DEP ON DEP.DEP_ID = x.ORG_ID WHERE  pro.status =1 AND pro.ECID =").append(llMechanismReport.getEcid());
		}else{
			conditionSql = new StringBuilder(" FROM ").append("(SELECT SUBSTRING(REPORTDATE,1,6) REPORTDATE,count(1) SUNMITNUM,max(ORG_ID) ORG_ID FROM ").append(tableName).append(" WHERE PRODUCTID='").append(llMechanismReport.getProductId()).append("' AND SUBSTRING(REPORTDATE,1,6) >= '").append(statisticsTime).append("01' AND SUBSTRING(REPORTDATE,1,6) <= '").append(statisticsTime).append("12' GROUP BY SUBSTRING(REPORTDATE, 1, 6)) x ")
					.append("LEFT JOIN (SELECT SUBSTRING(REPORTDATE,1,6) REPORTDATE2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND PRODUCTID='").append(llMechanismReport.getProductId()).append("' AND SUBSTRING(REPORTDATE,1,6) >= '").append(statisticsTime).append("01' AND SUBSTRING(REPORTDATE,1,6) <= '").append(statisticsTime).append("12' GROUP BY SUBSTRING(REPORTDATE, 1, 6)) y ON x.REPORTDATE=y.REPORTDATE2 ")
					.append("LEFT JOIN (SELECT SUBSTRING(REPORTDATE,1,6) REPORTDATE3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND PRODUCTID='").append(llMechanismReport.getProductId()).append("' AND SUBSTRING(REPORTDATE,1,6) >= '").append(statisticsTime).append("01' AND SUBSTRING(REPORTDATE,1,6) <= '").append(statisticsTime).append("12' GROUP BY SUBSTRING(REPORTDATE, 1, 6)) z ON x.REPORTDATE = z.REPORTDATE3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID ='").append(llMechanismReport.getProductId()).append("' LEFT JOIN LF_DEP DEP ON DEP.DEP_ID = x.ORG_ID WHERE  pro.status =1 AND pro.ECID =").append(llMechanismReport.getEcid());
		}
		if(!"".equals(llMechanismReport.getProductName()) && llMechanismReport.getProductName() != null){
			conditionSql = conditionSql.append(" AND PRODUCTNAME LIKE '%").append(llMechanismReport.getProductName()).append("%'");
		}
		if(!"".equals(llMechanismReport.getProductId()) && llMechanismReport.getProductId() != null){
			conditionSql = conditionSql.append(" AND PRO.PRODUCTID ='").append(llMechanismReport.getProductId()).append("'");
		}
		if(!"9999".equals(llMechanismReport.getIsp())){
			conditionSql = conditionSql.append(" AND ISP ='").append(llMechanismReport.getIsp()).append("'");
		}
		if(!"-9999".equals(llMechanismReport.getDepNam())){
			conditionSql = conditionSql.append(" AND DEP_NAME ='").append(llMechanismReport.getDepNam()).append("'");
		}
		return conditionSql.toString();
	}

	public String getllMechanismFiledSql() {
		return "SELECT  REPORTDATE,SUNMITNUM,SUCCNUM,FAILDNUM,pro.PRODUCTNAME PRODUCTNAME,pro.ISP ISP,pro.PRODUCTID PRODUCTID,DEP.DEP_NAME DEPNAM ";
	}

	public String getllMechanismConditionSql(
			LlMechanismReportVo llMechanismReport, String sendTime,
			String recvTime, String statisticsTime, String tableName,
			String lastDay) {
		StringBuilder conditionSql = null;
		if("1".equals(llMechanismReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT max(REPORTDATE) REPORTDATE, max(PRODUCTID) PRODUCTID,max(ORG_ID) ORG_ID,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE 1=1 AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY PRODUCTID) x ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY PRODUCTID) y ON x.PRODUCTID=y.PRODUCTID2 ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND REPORTDATE >= '").append(sendTime).append("' AND REPORTDATE <= '").append(recvTime).append("' GROUP BY PRODUCTID) z ON x.PRODUCTID = z.PRODUCTID3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID = x.PRODUCTID LEFT JOIN LF_DEP DEP ON DEP.DEP_ID = x.ORG_ID WHERE  pro.status =1 AND pro.ECID =").append(llMechanismReport.getEcid());
		}else if("2".equals(llMechanismReport.getReportType())){
			conditionSql = new StringBuilder(" FROM ").append("(SELECT max(REPORTDATE) REPORTDATE, max(PRODUCTID) PRODUCTID,max(ORG_ID) ORG_ID,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE 1=1 AND SUBSTRING(REPORTDATE,1,6)= '").append(statisticsTime).append("' GROUP BY PRODUCTID) x ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND SUBSTRING(REPORTDATE,1,6)= '").append(statisticsTime).append("' GROUP BY PRODUCTID) y ON x.PRODUCTID=y.PRODUCTID2 ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND SUBSTRING(REPORTDATE,1,6)= '").append(statisticsTime).append("' GROUP BY PRODUCTID) z ON x.PRODUCTID = z.PRODUCTID3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID = x.PRODUCTID  LEFT JOIN LF_DEP DEP ON DEP.DEP_ID = x.ORG_ID WHERE  pro.status =1 AND pro.ECID =").append(llMechanismReport.getEcid());
		}else{
			conditionSql = new StringBuilder(" FROM ").append("(SELECT max(REPORTDATE) REPORTDATE, max(PRODUCTID) PRODUCTID,max(ORG_ID) ORG_ID,count(1) SUNMITNUM FROM ").append(tableName).append(" WHERE 1=1 AND SUBSTRING(REPORTDATE,1,4)= '").append(statisticsTime.substring(0,4)).append("' GROUP BY PRODUCTID) x ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID2,count(1) SUCCNUM FROM ").append(tableName).append(" WHERE LLRPT = '1' AND SUBSTRING(REPORTDATE,1,4)= '").append(statisticsTime.substring(0,4)).append("' GROUP BY PRODUCTID) y ON x.PRODUCTID=y.PRODUCTID2 ")
					.append("LEFT JOIN (SELECT max(PRODUCTID) PRODUCTID3,count(1) FAILDNUM FROM ").append(tableName).append(" WHERE LLRPT = '2' AND SUBSTRING(REPORTDATE,1,4)= '").append(statisticsTime.substring(0,4)).append("' GROUP BY PRODUCTID) z ON x.PRODUCTID = z.PRODUCTID3 ")
					.append("LEFT JOIN LL_PRODUCT pro ON  pro.PRODUCTID = x.PRODUCTID  LEFT JOIN LF_DEP DEP ON DEP.DEP_ID = x.ORG_ID WHERE  pro.status =1 AND pro.ECID =").append(llMechanismReport.getEcid());
		}
		if(!"".equals(llMechanismReport.getProductName()) && llMechanismReport.getProductName() != null){
			conditionSql = conditionSql.append(" AND PRODUCTNAME LIKE '%").append(llMechanismReport.getProductName()).append("%'");
		}
		if(!"".equals(llMechanismReport.getProductId()) && llMechanismReport.getProductId() != null){
			conditionSql = conditionSql.append(" AND PRO.PRODUCTID ='").append(llMechanismReport.getProductId()).append("'");
		}
		if(!"9999".equals(llMechanismReport.getIsp())){
			conditionSql = conditionSql.append(" AND ISP ='").append(llMechanismReport.getIsp()).append("'");
		}
		if(!"-9999".equals(llMechanismReport.getDepNam())){
			conditionSql = conditionSql.append(" AND DEP_NAME ='").append(llMechanismReport.getDepNam()).append("'");
		}
		return conditionSql.toString();
	}

	public String getLlCompInfoInsertSql() {
		return "INSERT INTO LL_COMP_INFO (ID,PASSWORD,ECID,ECNAME,IP,PORT,REMARK,PUSHADDR,UPDATETM,CREATETM) VALUES(1,?,?,?,?,?,?,?,GETDATE(),GETDATE())";
	}

	public String getLlCompInfoUpdateSql() {
		return "UPDATE LL_COMP_INFO SET PASSWORD=?,ECID=?,ECNAME=?,IP=?,PORT=?,REMARK=?,PUSHADDR=?,UPDATETM=GETDATE()";
	}

	public String getLlProductInsertSql() {
		return "INSERT INTO LL_PRODUCT (ECID,PRODUCTID,PRODUCTNAME,ISP,VOLUME,PRICE,DISCPRICE,AREA,PTYPE,PMOLD,RTYPE,STATUS,UPDATETM,CREATETM,OPERATORID) VALUES(?,?,?,?,?,?,?,?,?,?,?,1,GETDATE(),GETDATE(),?)";
	}

	public String getLlProductUpdateSql(LlProduct llProduct) {
		StringBuilder sql = new StringBuilder().append("UPDATE LL_PRODUCT SET STATUS=0,OPERATORID=?,UPDATETM=GETDATE() WHERE PRODUCTID=? AND ECID =").append(llProduct.getEcid());
		return sql.toString();
	}

	public String getLlProductUpdateInSql(LlProduct llProduct, String pros) {
		StringBuilder sql = new StringBuilder().append("UPDATE LL_PRODUCT SET STATUS=0,OPERATORID=?,UPDATETM=GETDATE() WHERE PRODUCTID in(").append(pros).append(") AND ECID =").append(llProduct.getEcid());
		return sql.toString();
	}
	
}
