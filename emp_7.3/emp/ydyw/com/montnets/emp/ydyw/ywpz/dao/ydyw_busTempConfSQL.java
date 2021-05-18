package com.montnets.emp.ydyw.ywpz.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.montnets.emp.common.constant.StaticValue;

public class ydyw_busTempConfSQL {
	
	/**
	 * 拼接SQL语句
	 * @param busName
	 * @param busCode
	 * @param depId
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String retfindByParamsSQL(String busName, String busCode,
			String depId, String userId, String startTime, String endTime, String corpCode) {
		//beginning
		StringBuffer sbSql = new StringBuffer();		
		
		sbSql.append("select lb.BUS_ID,BUS_NAME,BUS_CODE,ICOUNT,")
			.append("tb2.CREATE_TIME,tb2.UPDATE_TIME,NAME,")
			.append("USER_NAME,DEP_NAME,lb.STATE ")
			.append("from (select")
			.append(" BUS_ID,COUNT(*) ICOUNT ")
			.append("from LF_BUS_TAILTMP ")
			.append("where ASSOCIATE_TYPE=1 ")
			.append("group by BUS_ID) tb1 ")
			.append("left join LF_BUSMANAGER lb ")
			.append(" on tb1.BUS_ID=lb.BUS_ID ")
			.append("left join (")
			.append("select tb.BUS_ID,USER_ID,DEP_ID,")
			.append("CREATE_TIME,UPDATE_TIME ")
			.append("from (select BUS_ID,max(ID) ID ")
			.append("from LF_BUS_TAILTMP ")
			.append("where ASSOCIATE_TYPE=1 ")
			.append("group by BUS_ID) tb ")
			.append("left join LF_BUS_TAILTMP lbt ")
			.append("on tb.ID=lbt.ID) tb2 ")
			.append("on tb2.BUS_ID=tb1.BUS_ID ")
			.append("left join LF_SYSUSER ls ")
			.append("on ls.USER_ID=tb2.USER_ID ")
			.append("left join LF_DEP ld ")
			.append("on ld.DEP_ID=tb2.DEP_ID ");
		
		sbSql.append("where lb.CORP_CODE='").append(corpCode).append("' ");
		
		//根据业务名称查询
		if (busName != null && !busName.equals("")) {
			sbSql.append("and lb.BUS_NAME like '%").append(busName.trim()).append("%' ");
		}

		//根据业务编号查询
		if (busCode != null && !busCode.equals("")) {
			sbSql.append("and lb.BUS_CODE like '%").append(busCode.trim()).append("%' ");
		}

		//根据机构查询
		if (depId != null && !depId.equals("")) {
			sbSql.append("and tb2.DEP_ID in (").append(depId).append(") ");
		}

		//根据用户查询
		if (userId != null && !userId.equals("")) {
			sbSql.append("and ls.USER_ID in (").append(userId).append(") ");
		}
		
		//当结束时间为空的时候，默认为当前时间
		String date = endTime;
		
		if (endTime == null || endTime.equals("")) {
			Calendar cal = Calendar.getInstance();
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		}
		
		date = date.replace("  ", " ");
		
		if (startTime != null && !startTime.equals("")) {		
			startTime = startTime.replace("  ", " ");			
			
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
				//ORACLE数据库
				sbSql.append("and to_char(tb2.UPDATE_TIME,'yyyy-MM-dd hh24:mi:ss') between '")
					.append(startTime).append("' and '")
					.append(date).append("' ");
				
			} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE) {
				//SQLSERVER数据库
				sbSql.append("and convert(varchar(10),tb2.UPDATE_TIME,120) between '")
					.append(startTime).append("' and '")
					.append(date).append("' ");
			} else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) {
				//DB2数据库to_date('2009-01-01 12:23:45','yyyy-mm-dd hh24:mi:ss')
				sbSql.append("and to_char(tb2.UPDATE_TIME,'yyyy-MM-dd hh24:mi:ss') between '")
					.append(startTime).append("' and '")
					.append(date).append("' ");
				
			} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE) {
				//MYSQL数据库DATE_FORMAT( rq, '%Y-%m-%d')
				sbSql.append("and tb2.UPDATE_TIME between '")
					.append(startTime).append("' and '")
					.append(date).append("' ");
				
			}
		} else {
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
				//ORACLE数据库
				sbSql.append("and to_char(tb2.UPDATE_TIME,'yyyy-MM-dd hh24:mi:ss') <= '")
					.append(date).append("' ");
				
			} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE) {
				//SQLSERVER数据库
				sbSql.append("and convert(varchar(19),tb2.UPDATE_TIME,120) <= '")
					.append(date).append("' ");
			} else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) {
				//DB2数据库to_date('2009-01-01 12:23:45','yyyy-mm-dd hh24:mi:ss')
				sbSql.append("and to_char(tb2.UPDATE_TIME,'yyyy-MM-dd hh24:mi:ss') <= '")
					.append(date).append("' ");
				
			} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE) {
				//MYSQL数据库DATE_FORMAT( rq, '%Y-%m-%d')
				sbSql.append("and tb2.UPDATE_TIME <= '")
					.append(date).append("' ");
				
			}
		}	
		
		return sbSql.toString();
	}
	
	/**
	 * 查询用来排序
	 * @return
	 */
	public static String retSqlOrdby() {
		StringBuffer sbSql = new StringBuffer("order by tb2.UPDATE_TIME desc");	
		return sbSql.toString();
	}
	
	
	/**
	 * 拼接SQL语句
	 * @param busName
	 * @param busCode
	 * @param depId
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String retfindByParamsSQL2(String busName, String busCode,
			String depId, String userId, String startTime, String endTime, String corpCode) {
		//beginning
		StringBuffer sbSql = new StringBuffer();		
		
		sbSql.append("select lb.BUS_ID,BUS_NAME,BUS_CODE,ICOUNT,")
			.append("tb2.CREATE_TIME,tb2.UPDATE_TIME,NAME,")
			.append("USER_NAME,DEP_NAME,lb.STATE ")
			.append("from (select")
			.append(" BUS_ID,COUNT(*) ICOUNT ")
			.append("from LF_BUS_TAILTMP ")
			.append("where ASSOCIATE_TYPE=1 ")
			.append("group by BUS_ID) tb1 ")
			.append("left join LF_BUSMANAGER lb ")
			.append(" on tb1.BUS_ID=lb.BUS_ID ")
			.append("left join (")
			.append("select tb.BUS_ID,USER_ID,DEP_ID,")
			.append("CREATE_TIME,UPDATE_TIME ")
			.append("from (select BUS_ID,max(ID) ID ")
			.append("from LF_BUS_TAILTMP ")
			.append("where ASSOCIATE_TYPE=1 ")
			.append("group by BUS_ID) tb ")
			.append("left join LF_BUS_TAILTMP lbt ")
			.append("on tb.ID=lbt.ID) tb2 ")
			.append("on tb2.BUS_ID=tb1.BUS_ID ")
			.append("left join LF_SYSUSER ls ")
			.append("on ls.USER_ID=tb2.USER_ID ")
			.append("left join LF_DEP ld ")
			.append("on ld.DEP_ID=tb2.DEP_ID ");
		
		sbSql.append("where lb.CORP_CODE='").append(corpCode).append("' ");
		//sbSql.append("where ls.USER_ID in (").append(userId).append(") and (lb.CORP_CODE='").append(corpCode).append("' ");
		
		//根据业务名称查询
		if (busName != null && !busName.equals("")) {
			sbSql.append("and lb.BUS_NAME like '%").append(busName.trim()).append("%' ");
		}

		//根据业务编号查询
		if (busCode != null && !busCode.equals("")) {
			sbSql.append("and lb.BUS_CODE like '%").append(busCode.trim()).append("%' ");
		}

		//根据机构查询
		if (depId != null && !depId.equals("")) {
			sbSql.append("and (ld.DEP_ID in (").append(depId).append(") or ls.USER_ID in (").append(userId).append(") ");
		}

		//根据用户查询
		if (userId != null && !userId.equals("")) {
			sbSql.append("and ls.USER_ID in (").append(userId).append(") ");
		}
		
		//当结束时间为空的时候，默认为当前时间
		String date = endTime;
		
		if (endTime == null || endTime.equals("")) {
			Calendar cal = Calendar.getInstance();
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		}
		
		date = date.replace("  ", " ");
		
		if (startTime != null && !startTime.equals("")) {		
			startTime = startTime.replace("  ", " ");			
			
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
				//ORACLE数据库
				sbSql.append("and to_char(tb2.UPDATE_TIME,'yyyy-MM-dd hh24:mi:ss') between '")
					.append(startTime).append("' and '")
					.append(date).append("' ");
				
			} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE) {
				//SQLSERVER数据库
				sbSql.append("and convert(varchar(10),tb2.UPDATE_TIME,120) between '")
					.append(startTime).append("' and '")
					.append(date).append("' ");
			} else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) {
				//DB2数据库to_date('2009-01-01 12:23:45','yyyy-mm-dd hh24:mi:ss')
				sbSql.append("and to_char(tb2.UPDATE_TIME,'yyyy-MM-dd hh24:mi:ss') between '")
					.append(startTime).append("' and '")
					.append(date).append("' ");
				
			} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE) {
				//MYSQL数据库DATE_FORMAT( rq, '%Y-%m-%d')
				sbSql.append("and tb2.UPDATE_TIME between '")
					.append(startTime).append("' and '")
					.append(date).append("' ");
				
			}
		} else {
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
				//ORACLE数据库
				sbSql.append("and to_char(tb2.UPDATE_TIME,'yyyy-MM-dd hh24:mi:ss') <= '")
					.append(date).append("' ");
				
			} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE) {
				//SQLSERVER数据库
				sbSql.append("and convert(varchar(19),tb2.UPDATE_TIME,120) <= '")
					.append(date).append("' ");
			} else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) {
				//DB2数据库to_date('2009-01-01 12:23:45','yyyy-mm-dd hh24:mi:ss')
				sbSql.append("and to_char(tb2.UPDATE_TIME,'yyyy-MM-dd hh24:mi:ss') <= '")
					.append(date).append("' ");
				
			} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE) {
				//MYSQL数据库DATE_FORMAT( rq, '%Y-%m-%d')
				sbSql.append("and tb2.UPDATE_TIME <= '")
					.append(date).append("' ");
				
			}
		}	
		
		//sbSql.append(") ");
		return sbSql.toString();
	}
	
	/**
	 * 查询用来排序
	 * @return
	 */
	public static String retSqlOrdby2() {
		StringBuffer sbSql = new StringBuffer("order by tb2.UPDATE_TIME desc");	
		return sbSql.toString();
	}

}
