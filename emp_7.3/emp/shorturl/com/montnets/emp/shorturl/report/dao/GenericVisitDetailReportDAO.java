package com.montnets.emp.shorturl.report.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.shorturl.report.vo.LinkReportQueryVo;
import com.montnets.emp.shorturl.report.vo.VisitDetailVo;
import com.montnets.emp.shorturl.report.vo.VisitReportVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

public class GenericVisitDetailReportDAO extends SuperDAO {

	/**
	 * 获取访问明细报表
	 * @param queryVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<VisitReportVo> getVisitReport(LinkReportQueryVo queryVo, PageInfo pageInfo) throws Exception{
		String mtTable = "GW_MT_TASK_BAK";
		String visitTable = "";
		String queryField = "PHONE, TASKID, UNICOM, ERRORCODE, MESSAGE, SENDTIME,PKNUMBER";

		List<String> timeList = new ArrayList<String>();
		SimpleDateFormat quertTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeList.add(quertTime.format(new Date(queryVo.getStartTime().getTime())));
		timeList.add(quertTime.format(new Date(queryVo.getEndTime().getTime())));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Timestamp startTime = queryVo.getStartTime();
		String st = sdf.format(new Date(startTime.getTime()));
		Timestamp endTime = queryVo.getEndTime();
		String et = sdf.format(new Date(endTime.getTime()));
		if(st.equals(et)){
			visitTable = " VST_DETAIL"+st;
			mtTable = "(SELECT "+queryField+" from GW_MT_TASK_BAK union all SELECT "+queryField+" from MTTASK"+et+")";
		}else{
			visitTable = "(SELECT * from VST_DETAIL"+st+" union all SELECT * from VST_DETAIL"+et+")";
			mtTable = "(SELECT "+queryField+" from GW_MT_TASK_BAK union all SELECT "+queryField+" from MTTASK"+st+" union all SELECT "+queryField+" from MTTASK"+et+")";
		}

//		String sql = "SELECT * FROM (SELECT CV.PHONE,CV.TASKID,CV.UNICOM,CV.ERRORCODE,CV.MESSAGE,CV.SENDTIME,CV.TITLE,CV.SRCADDRESS AS LASTIP,CV.VSTTM AS LASTVISITTIME,ISNULL(CV.TIMES,0) AS VISITCOUNT,ROW_NUMBER() OVER(PARTITION BY CV.TASKID, CV.PHONE ORDER BY CV.TIMES DESC) RM FROM ( SELECT B.TITLE, A.TASKID, A.PHONE, A.UNICOM, A.ERRORCODE, A.MESSAGE, A.SENDTIME, C.TIMES, C.VSTTM, C.SRCADDRESS FROM LF_URLTASK B LEFT JOIN " + mtTable + " A ON A.TASKID = B.TASKID LEFT JOIN "+visitTable + " C ON C.CUSTID = A.TASKID AND C.PHONE = A.PHONE WHERE A.PKNUMBER = 1 AND A.SENDTIME>=? AND A.SENDTIME<? ";
        String sql="";
		boolean isMysql = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE;
        if(isMysql) {
            timeList.clear();
            if(st.equals(et)) {
                String innerSql = segmentSql("MTTASK" + st, "VST_DETAIL" + st, queryVo, timeList);
                sql = maxRowSql(innerSql);
            } else {
                String innerSql = segmentSql("MTTASK" + st, "VST_DETAIL" + st, queryVo, timeList) + " UNION ALL " + segmentSql("MTTASK" + et, "VST_DETAIL" + et, queryVo, timeList);
                sql = maxRowSql(innerSql);
            }
//            sql = "SELECT * FROM (SELECT PHONE,TASKID,UNICOM,ERRORCODE,MESSAGE,SENDTIME,TITLE,SRCADDRESS AS LASTIP,VSTTM AS LASTVISITTIME,IFNULL(TIMES,0) AS VISITCOUNT FROM ( SELECT * FROM ( SELECT B.TITLE, A.TASKID, A.PHONE, A.UNICOM, A.ERRORCODE, A.MESSAGE, A.SENDTIME, C.TIMES, C.VSTTM, C.SRCADDRESS FROM LF_URLTASK B LEFT JOIN " + mtTable + " A ON A.TASKID = B.TASKID LEFT JOIN "+visitTable + " C ON C.CUSTID = A.TASKID AND C.PHONE = A.PHONE WHERE A.PKNUMBER = 1 AND A.SENDTIME>=? AND A.SENDTIME<? ";
        } else {
        	if(StaticValue.DBTYPE==1){
        		sql = "SELECT * FROM (SELECT CV.PHONE,CV.TASKID,CV.UNICOM,CV.ERRORCODE,CV.MESSAGE,CV.SENDTIME,CV.TITLE,CV.SRCADDRESS AS LASTIP,CV.VSTTM AS LASTVISITTIME,NVL(CV.TIMES,0) AS VISITCOUNT,ROW_NUMBER() OVER(PARTITION BY CV.TASKID, CV.PHONE ORDER BY CV.TIMES DESC) RM FROM ( SELECT B.TITLE, A.TASKID, A.PHONE, A.UNICOM, A.ERRORCODE, A.MESSAGE, A.SENDTIME, C.TIMES, C.VSTTM, C.SRCADDRESS FROM LF_URLTASK B LEFT JOIN " + mtTable + " A ON A.TASKID = B.TASKID LEFT JOIN "+visitTable + " C ON C.CUSTID = A.TASKID AND C.PHONE = A.PHONE WHERE A.PKNUMBER = 1 AND A.SENDTIME>=? AND A.SENDTIME<? ";
        	}
        	if(StaticValue.DBTYPE==4){
        		sql = "SELECT * FROM (SELECT CV.PHONE,CV.TASKID,CV.UNICOM,CV.ERRORCODE,CV.MESSAGE,CV.SENDTIME,CV.TITLE,CV.SRCADDRESS AS LASTIP,CV.VSTTM AS LASTVISITTIME,NVL(CV.TIMES,0) AS VISITCOUNT,ROW_NUMBER() OVER(PARTITION BY CV.TASKID, CV.PHONE ORDER BY CV.TIMES DESC) RM FROM ( SELECT B.TITLE, A.TASKID, A.PHONE, A.UNICOM, A.ERRORCODE, A.MESSAGE, A.SENDTIME, C.TIMES, C.VSTTM, C.SRCADDRESS FROM LF_URLTASK B LEFT JOIN " + mtTable + " A ON A.TASKID = B.TASKID LEFT JOIN "+visitTable + " C ON C.CUSTID = A.TASKID AND C.PHONE = A.PHONE WHERE A.PKNUMBER = 1 AND A.SENDTIME>=? AND A.SENDTIME<? ";
        	}
        	if(StaticValue.DBTYPE==3){
        		sql = "SELECT * FROM (SELECT CV.PHONE,CV.TASKID,CV.UNICOM,CV.ERRORCODE,CV.MESSAGE,CV.SENDTIME,CV.TITLE,CV.SRCADDRESS AS LASTIP,CV.VSTTM AS LASTVISITTIME,ISNULL(CV.TIMES,0) AS VISITCOUNT,ROW_NUMBER() OVER(PARTITION BY CV.TASKID, CV.PHONE ORDER BY CV.TIMES DESC) RM FROM ( SELECT B.TITLE, A.TASKID, A.PHONE, A.UNICOM, A.ERRORCODE, A.MESSAGE, A.SENDTIME, C.TIMES, C.VSTTM, C.SRCADDRESS FROM LF_URLTASK B LEFT JOIN " + mtTable + " A ON A.TASKID = B.TASKID LEFT JOIN "+visitTable + " C ON C.CUSTID = A.TASKID AND C.PHONE = A.PHONE WHERE A.PKNUMBER = 1 AND A.SENDTIME>=? AND A.SENDTIME<? ";
        	}
            
        }


//		if(queryVo.getPhone()!= null && !"".equals(queryVo.getPhone())){
//			sql = sql+" and A.PHONE = '"+queryVo.getPhone()+"'";
//		}
//		if (queryVo.getCorpCode() != null && !"".equals(queryVo.getCorpCode())) {
//			sql = sql + " AND B.CORP_CODE = '" + queryVo.getCorpCode().trim() + "'";
//		}
//		if(queryVo.getTitle()!= null && !"".equals(queryVo.getTitle())){
//			sql = sql+" and B.TITLE = '"+queryVo.getTitle()+"'";
//		}
//		if(queryVo.getTaskId()!= null && !"".equals(queryVo.getTaskId())){
//			sql = sql+" and A.TASKID = "+queryVo.getTaskId();
//		}
//		if(queryVo.getUnicom()!= null && !"".equals(queryVo.getUnicom())){
//			sql = sql+" and A.UNICOM = "+queryVo.getUnicom();
//		}

		if(isMysql) {
            sql = "SELECT * FROM (" + sql + ") RS WHERE 1=1 ";
        } else {
            if(queryVo.getPhone()!= null && !"".equals(queryVo.getPhone())){
                sql = sql+" and A.PHONE = '"+queryVo.getPhone()+"'";
            }
            if (queryVo.getCorpCode() != null && !"".equals(queryVo.getCorpCode())) {
                sql = sql + " AND B.CORP_CODE = '" + queryVo.getCorpCode().trim() + "'";
            }
            if(queryVo.getTitle()!= null && !"".equals(queryVo.getTitle())){
                sql = sql+" and B.TITLE = '"+queryVo.getTitle()+"'";
            }
            if(queryVo.getTaskId()!= null && !"".equals(queryVo.getTaskId())){
                sql = sql+" and A.TASKID = "+queryVo.getTaskId();
            }
            if(queryVo.getUnicom()!= null ){
                sql = sql+" and A.UNICOM = "+queryVo.getUnicom();
            }
            sql = sql+") CV) RS WHERE RS.RM = 1";
        }
//		sql = sql+") CV) RS WHERE RS.RM = 1";

		if(queryVo.getVisitCount()!= null ){
			sql = sql+" AND RS.VISITCOUNT = "+queryVo.getVisitCount();
		}
		if(queryVo.getVisitStatus()!= null && !"".equals(queryVo.getVisitStatus())){
			if("0".equals(queryVo.getVisitStatus())){
				sql = sql+" AND RS.VISITCOUNT = 0";
			}
			if("1".equals(queryVo.getVisitStatus())){
				sql = sql+" AND RS.VISITCOUNT > 0";
			}
		}

		String countSql = new StringBuffer("select count(*) totalcount from (").append(sql).append(") t").toString();

		//增加排序
		sql += " ORDER BY RS.SENDTIME DESC";
        EmpExecutionContext.info("访问明细详情sql: " + sql);
		List<VisitReportVo> rs = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(VisitReportVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME,timeList);
		return rs;
	}

	/**
	 * 根据任务id,手机号,发送年月查询访问明细
	 * @param phone 手机号
	 * @param taskId 任务id
	 * @param yearMonth 发送年月
	 * @param pageInfo 分页信息
	 * @return
	 * @throws Exception
	 */
	public List<VisitDetailVo> getVisitDetail(String phone, String taskId, String yearMonth,PageInfo pageInfo) throws Exception{
		String sql = "SELECT PHONE,vsttm,SRCADDRESS FROM VST_DETAIL"+yearMonth+" WHERE CUSTID = "+taskId+" AND PHONE = "+phone;
		String countSql = new StringBuffer("select count(*) totalcount from (").append(sql).append(") t").toString();
		List<VisitDetailVo> rs = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(VisitDetailVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		return rs;
	}


    private String generateSql(String mtTaskTableName, LinkReportQueryVo queryVo) {
        StringBuffer sql = new StringBuffer();
        String tempField = " SELECT A.STR_TASKID,A.TITLE,B.PHONE,B.TASKID,B.UNICOM,B.ERRORCODE,B.MESSAGE,B.SENDTIME,B.PKNUMBER FROM ";
        sql.append(tempField);
        sql.append("(SELECT TITLE,TASKID,STR_TASKID,SEND_TM FROM LF_URLTASK WHERE 1=1");
        if(StringUtils.isNotEmpty(queryVo.getCorpCode()) && !"100000".equals(queryVo.getCorpCode())) {
            sql.append(" AND CORP_CODE=").append(queryVo.getCorpCode());
        }
        if(StringUtils.isNotEmpty(queryVo.getTitle())) {
            sql.append(" AND TITLE='")
                    .append(queryVo.getTitle()).append("'");
        }
        sql.append(") A ");
        String tableSql = " INNER JOIN " + mtTaskTableName + " B ON A.TASKID = B.TASKID AND B.PKNUMBER=1 AND B.SENDTIME>=? AND B.SENDTIME<? ";
        sql.append(tableSql);
        if(StringUtils.isNotEmpty(queryVo.getPhone())) {
            sql.append(" AND B.PHONE='").append(queryVo.getPhone()).append("'");
        }
        if(StringUtils.isNotEmpty(queryVo.getTaskId())) {
            sql.append(" AND B.TASKID=").append(queryVo.getTaskId());
        }
        if(queryVo.getUnicom() != null) {
            sql.append(" AND B.UNICOM=").append(queryVo.getUnicom());
        }
        return sql.toString();
    }

    private String segmentSql(String mtTaskTableName, String vstTableName, LinkReportQueryVo queryVo, List<String> timeList) {
        if(null != timeList) {
            SimpleDateFormat quertTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 兼容mysql数据库时，筛选条件放到里边去了，因此多处使用了?占位符，同时传参时间也要加上
            for(int i = 0; i < 2; i++) {
                timeList.add(quertTime.format(new Date(queryVo.getStartTime().getTime())));
                timeList.add(quertTime.format(new Date(queryVo.getEndTime().getTime())));
            }
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.PHONE,C.TASKID,C.UNICOM,C.ERRORCODE,C.MESSAGE,C.SENDTIME,C.PKNUMBER,C.TITLE,D.VSTTM AS LASTVISITTIME,D.SRCADDRESS AS LASTIP,IFNULL(D.TIMES,0) AS VISITCOUNT FROM ");
        sql.append("(").append(generateSql("GW_MT_TASK_BAK", queryVo)).append(" UNION ALL ")
                .append(generateSql(mtTaskTableName, queryVo)).append(") C ").append(" LEFT JOIN ").append(vstTableName).append(" D ON C.STR_TASKID=D.CUSTID AND C.PHONE=D.PHONE ");
        return sql.toString();
    }

    private String maxRowSql(String innerSql) {
        StringBuffer sql = new StringBuffer();
//        sql.append("SELECT t.TASKID,t.PHONE,t.UNICOM,t.ERRORCODE,t.MESSAGE,t.SENDTIME,t.PKNUMBER,t.TITLE,t.LASTVISITTIME,t.LASTIP,SUBSTRING_INDEX(GROUP_CONCAT(t.VISITCOUNT ORDER BY t.VISITCOUNT DESC),',',1) VISITCOUNT FROM (");
        sql.append("SELECT t.taskid,t.phone,max(t.UNICOM) AS UNICOM,max(t.ERRORCODE) AS ERRORCODE,max(t.MESSAGE) AS MESSAGE,max(t.SENDTIME) AS SENDTIME,max(t.PKNUMBER) AS PKNUMBER,max(t.TITLE) AS TITLE,max(t.LASTVISITTIME) AS LASTVISITTIME,max(t.LASTIP) AS LASTIP,SUBSTRING_INDEX(GROUP_CONCAT(t.VISITCOUNT ORDER BY t.VISITCOUNT DESC),',',1) VISITCOUNT FROM (");
        sql.append(innerSql);
        sql.append(") t GROUP BY t.TASKID,t.PHONE");
        return sql.toString();
    }

    @Deprecated
    private String innerJoinRTabSql(String innerSql) {
        StringBuffer sql = new StringBuffer();
        sql.append(" INNER JOIN ( ");
        sql.append(innerSql);
        sql.append(") E ON M.TASKID=E.TASKID AND M.PHONE=E.PHONE AND M.VISITCOUNT=E.VISITCOUNT ");
        return sql.toString();
    }


}
