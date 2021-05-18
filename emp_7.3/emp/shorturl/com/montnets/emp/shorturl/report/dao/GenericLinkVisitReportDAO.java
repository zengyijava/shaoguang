package com.montnets.emp.shorturl.report.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.montnets.emp.util.StringUtils;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.shorturl.report.vo.LinkDetailVo;
import com.montnets.emp.shorturl.report.vo.LinkReportQueryVo;
import com.montnets.emp.shorturl.report.vo.LinkReportVo;
import com.montnets.emp.util.PageInfo;

public class GenericLinkVisitReportDAO extends SuperDAO {

	/**
	 * 获取链接访问统计报表
	 *
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	public List<LinkReportVo> getLinkReport(LinkReportQueryVo queryVo) throws Exception {
		String visitTable = "";
		List<String> timeList = new ArrayList<String>();
		SimpleDateFormat quertTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeList.add(quertTime.format(new Date(queryVo.getStartTime().getTime())));
		timeList.add(quertTime.format(new Date(queryVo.getEndTime().getTime())));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Timestamp startTime = queryVo.getStartTime();
		String st = sdf.format(new Date(startTime.getTime()));
		Timestamp endTime = queryVo.getEndTime();
		String et = sdf.format(new Date(endTime.getTime()));
		if (st.equals(et)) {
			visitTable = "VST_DETAIL" + st;
		} else {
			visitTable = "(SELECT * FROM VST_DETAIL" + st + " UNION ALL SELECT * FROM VST_DETAIL" + et + ")";
		}
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		String sql="SELECT NETURL,SUM(mt.EFF_COUNT) EFF_COUNT FROM LF_URLTASK a INNER JOIN LF_MTTASK mt ON a.TASKID = mt.TASKID where 1=1 AND mt.SENDSTATE=3 ";
		String visitSql="SELECT a.NETURL NETURL,COUNT(DISTINCT PHONE) AS visitCount,COUNT( SRCADDRESS) AS visitNum  from LF_URLTASK a LEFT JOIN "+visitTable+" b ON a.STR_TASKID = b.CUSTID where 1=1 ";

//		String sql = "SELECT NETURL,SUM(SUB_COUNT) SUB_COUNT,SUM(EFF_COUNT) EFF_COUNT,COUNT(PHONE) as visitCount,COUNT(distinct SRCADDRESS) as visitNum FROM (SELECT b.phone PHONE,SRCADDRESS,a.NETURL NETURL, mt.SUB_COUNT SUB_COUNT,mt.EFF_COUNT EFF_COUNT FROM LF_URLTASK a inner join LF_MTTASK mt on a.TASKID = mt.TASKID left join "
//				+ visitTable + " b on a.TASKID = b.CUSTID WHERE 1=1";
		sql = sql + " AND a.plan_time>="+genericDao.getTimeCondition(quertTime.format(new Date(queryVo.getStartTime().getTime())));        //DATE_FORMAT('"+quertTime.format(new Date(queryVo.getStartTime().getTime()))+"','%Y-%m-%d %H:%i:%s')";
		visitSql = visitSql + " AND a.plan_time>="+genericDao.getTimeCondition(quertTime.format(new Date(queryVo.getStartTime().getTime())));        //DATE_FORMAT('"+quertTime.format(new Date(queryVo.getStartTime().getTime()))+"','%Y-%m-%d %H:%i:%s')";

		sql = sql + " AND a.plan_time<"+genericDao.getTimeCondition(quertTime.format(new Date(queryVo.getEndTime().getTime())));        //DATE_FORMAT('"+quertTime.format(new Date(queryVo.getEndTime().getTime()))+"','%Y-%m-%d %H:%i:%s')";
		visitSql = visitSql + " AND a.plan_time<"+genericDao.getTimeCondition(quertTime.format(new Date(queryVo.getEndTime().getTime())));        //DATE_FORMAT('"+quertTime.format(new Date(queryVo.getEndTime().getTime()))+"','%Y-%m-%d %H:%i:%s')";
		if (queryVo.getCorpCode() != null && !"".equals(queryVo.getCorpCode())) {
			sql = sql + " AND a.CORP_CODE = '" + queryVo.getCorpCode().trim() + "'";
			visitSql = visitSql + " AND a.CORP_CODE = '" + queryVo.getCorpCode().trim() + "'";
		}

		if (queryVo.getUrl() != null && !"".equals(queryVo.getUrl())) {
			sql = sql + " AND a.NETURL = '" + queryVo.getUrl().trim() + "'";
			visitSql = visitSql + " AND a.NETURL = '" + queryVo.getUrl().trim() + "'";
		}

		if (queryVo.getTitle() != null && !"".equals(queryVo.getTitle())) {
			sql = sql + " AND a.title = '" + queryVo.getTitle().trim() + "'";
			visitSql = visitSql + " AND a.title = '" + queryVo.getTitle().trim() + "'";
		}

		if (queryVo.getTaskId() != null && !"".equals(queryVo.getTaskId())) {
			sql = sql + " AND a.TASKID = '" + queryVo.getTaskId().trim() + "'";
			visitSql = visitSql + " AND a.TASKID = '" + queryVo.getTaskId().trim() + "'";
		}
		sql = sql + " group by NETURL";
		visitSql = visitSql + " group by NETURL";

//		sql = sql + " GROUP BY a.TASKID,b.phone) c group by NETURL";

		List<DynaBean> linkList=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		List<DynaBean> visitLinkList=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(visitSql);

	//	List<LinkReportVo> returnList = findVoListBySQL(LinkReportVo.class, sql, StaticValue.EMP_POOLNAME, timeList);

		List<LinkReportVo> returnList=new ArrayList<LinkReportVo>();
		LinkReportVo linkReportVo=null;
		for (int i = 0; i < linkList.size(); i++) {
			linkReportVo=new LinkReportVo();
			String url=linkList.get(i).get("neturl").toString();
			String effCount=linkList.get(i).get("eff_count").toString();
			String visitCount="";
			String visitNum="";
			linkReportVo.setEffCount(Long.valueOf(effCount));
			linkReportVo.setUrl(url);
			for (int j = 0; j < visitLinkList.size(); j++) {
				if(url.equals(visitLinkList.get(j).get("neturl").toString())){
					visitCount=visitLinkList.get(j).get("visitcount").toString();
					visitNum=visitLinkList.get(j).get("visitnum").toString();
				}
			}
			linkReportVo.setVisitCount(Long.valueOf(visitCount.equals("")?"0":visitCount));
			linkReportVo.setVisitNum(Long.valueOf(visitNum.equals("")?"0":visitNum));
			returnList.add(linkReportVo);
		}
		return returnList;
	}

	/**
	 * 获取链接访问统计报表
	 *
	 * @param queryVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LinkReportVo> getLinkReport(LinkReportQueryVo queryVo, PageInfo pageInfo) throws Exception {
		String visitTable = "";
		List<String> timeList = new ArrayList<String>();
		SimpleDateFormat quertTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeList.add(quertTime.format(new Date(queryVo.getStartTime().getTime())));
		timeList.add(quertTime.format(new Date(queryVo.getEndTime().getTime())));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Timestamp startTime = queryVo.getStartTime();
		String st = sdf.format(new Date(startTime.getTime()));
		Timestamp endTime = queryVo.getEndTime();
		String et = sdf.format(new Date(endTime.getTime()));
		if (st.equals(et)) {
			visitTable = "VST_DETAIL" + st;
		} else {
			visitTable = "(SELECT * FROM VST_DETAIL" + st + " UNION ALL SELECT * FROM VST_DETAIL" + et + ")";
		}
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		String sql="SELECT NETURL,SUM(mt.EFF_COUNT) EFF_COUNT FROM LF_URLTASK a INNER JOIN LF_MTTASK mt ON a.TASKID = mt.TASKID where 1=1 AND mt.SENDSTATE=3 ";
		String visitSql="SELECT a.NETURL NETURL,COUNT(DISTINCT PHONE) AS visitCount,COUNT( SRCADDRESS) AS visitNum  from LF_URLTASK a LEFT JOIN "+visitTable+" b ON a.STR_TASKID = b.CUSTID where 1=1 ";
//		String sql = "SELECT NETURL,SUM(SUB_COUNT) SUB_COUNT,SUM(EFF_COUNT) EFF_COUNT,COUNT(PHONE) as visitCount,COUNT(distinct SRCADDRESS) as visitNum FROM (SELECT b.phone PHONE,SRCADDRESS,a.NETURL NETURL, mt.SUB_COUNT SUB_COUNT,mt.EFF_COUNT EFF_COUNT FROM LF_URLTASK a inner join LF_MTTASK mt on a.TASKID = mt.TASKID left join "
//				+ visitTable + " b on a.TASKID = b.CUSTID WHERE 1=1";
		sql = sql + " AND a.plan_time>=?";
		visitSql = visitSql + " AND a.plan_time>= "+genericDao.getTimeCondition(quertTime.format(new Date(queryVo.getStartTime().getTime())));        //DATE_FORMAT('"+quertTime.format(new Date(queryVo.getStartTime().getTime()))+"','%Y-%m-%d %H:%i:%s')";
		sql = sql + " AND a.plan_time<?";
		visitSql = visitSql + " AND a.plan_time<"+genericDao.getTimeCondition(quertTime.format(new Date(queryVo.getEndTime().getTime())));        //DATE_FORMAT('"+quertTime.format(new Date(queryVo.getEndTime().getTime()))+"','%Y-%m-%d %H:%i:%s')";
		if (queryVo.getCorpCode() != null && !"".equals(queryVo.getCorpCode())) {
			sql = sql + " AND a.CORP_CODE = '" + queryVo.getCorpCode().trim() + "'";
			visitSql = visitSql + " AND a.CORP_CODE = '" + queryVo.getCorpCode().trim() + "'";
		}

		if (queryVo.getUrl() != null && !"".equals(queryVo.getUrl())) {
			sql = sql + " AND a.NETURL = '" + queryVo.getUrl().trim() + "'";
			visitSql = visitSql + " AND a.NETURL = '" + queryVo.getUrl().trim() + "'";
		}

		if (queryVo.getTitle() != null && !"".equals(queryVo.getTitle())) {
			sql = sql + " AND a.title = '" + queryVo.getTitle().trim() + "'";
			visitSql = visitSql + " AND a.title = '" + queryVo.getTitle().trim() + "'";
		}

		if (queryVo.getTaskId() != null && !"".equals(queryVo.getTaskId())) {
			sql = sql + " AND a.TASKID = '" + queryVo.getTaskId().trim() + "'";
			visitSql = visitSql + " AND a.TASKID = '" + queryVo.getTaskId().trim() + "'";
		}

		sql = sql + " group by NETURL";

		String countSql = new StringBuffer("select count(*) totalcount from (").append(sql).append(") pg").toString();
		//String countvisitSql = new StringBuffer("select count(*) totalcount from (").append(visitSql).append(") pg").toString();
		List<DynaBean> linkList=new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
		String buffer="";
		if(linkList.size()>0){
			buffer=" AND NETURL IN (";
			for (int i = 0; i < linkList.size(); i++) {
				buffer+="'"+linkList.get(i).get("neturl").toString()+"',";
			}
			buffer=buffer.substring(0, buffer.length()-1)+")";
		}
		visitSql =visitSql + buffer;
		visitSql = visitSql + " group by NETURL";
		List<DynaBean> visitLinkList=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(visitSql);
		//List<LinkReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(LinkReportVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
		List<LinkReportVo> returnList=new ArrayList<LinkReportVo>();
		LinkReportVo linkReportVo=null;
		for (int i = 0; i < linkList.size(); i++) {
			linkReportVo=new LinkReportVo();
			String url=linkList.get(i).get("neturl").toString();
			String effCount=linkList.get(i).get("eff_count").toString();
			String visitCount="";
			String visitNum="";
			linkReportVo.setEffCount(Long.valueOf(effCount));
			linkReportVo.setUrl(url);
			for (int j = 0; j < visitLinkList.size(); j++) {
				if(url.equals(visitLinkList.get(j).get("neturl").toString())){
					visitCount=visitLinkList.get(j).get("visitcount").toString();
					visitNum=visitLinkList.get(j).get("visitnum").toString();
				}
			}
			linkReportVo.setVisitCount(Long.valueOf(visitCount.equals("")?"0":visitCount));
			linkReportVo.setVisitNum(Long.valueOf(visitNum.equals("")?"0":visitNum));
			returnList.add(linkReportVo);
		}

		return returnList;
	}

	/**
	 * 获取链接访问详情
	 *
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	public List<LinkDetailVo> getLinkDetail(LinkReportQueryVo queryVo) throws Exception {

		List<String> timeList = new ArrayList<String>();
		SimpleDateFormat quertTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeList.add(quertTime.format(new Date(queryVo.getStartTime().getTime())));
		timeList.add(quertTime.format(new Date(queryVo.getEndTime().getTime())));

		String sql = commonSql(queryVo, timeList);

		List<LinkDetailVo> returnList = findVoListBySQL(LinkDetailVo.class, sql, StaticValue.EMP_POOLNAME,timeList);
		return returnList;
	}

	/**
	 * 获取链接访问详情
	 *
	 * @param queryVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LinkDetailVo> getLinkDetail(LinkReportQueryVo queryVo, PageInfo pageInfo) throws Exception {

		List<String> timeList = new ArrayList<String>();
		SimpleDateFormat quertTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeList.add(quertTime.format(new Date(queryVo.getStartTime().getTime())));
		timeList.add(quertTime.format(new Date(queryVo.getEndTime().getTime())));

        String sql = commonSql(queryVo, timeList);

		String countSql = new StringBuffer("select count(*) totalcount from (").append(sql).append(") t").toString();

		List<LinkDetailVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(LinkDetailVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME,timeList);
		return returnList;
	}

	private String commonSql(LinkReportQueryVo queryVo, List<String> timeList) {

        String sql;
        boolean isMysql = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE;

        String mtTable = "GW_MT_TASK_BAK";
        String visitTable = "";
        String queryField = "PHONE, TASKID, UNICOM, ERRORCODE, MESSAGE, SENDTIME,PKNUMBER";

//        List<String> timeList = new ArrayList<String>();
//        SimpleDateFormat quertTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        timeList.add(quertTime.format(new Date(queryVo.getStartTime().getTime())));
//        timeList.add(quertTime.format(new Date(queryVo.getEndTime().getTime())));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Timestamp startTime = queryVo.getStartTime();
        String st = sdf.format(new Date(startTime.getTime()));
        Timestamp endTime = queryVo.getEndTime();
        String et = sdf.format(new Date(endTime.getTime()));
        if (st.equals(et)) {
            visitTable = "VST_DETAIL" + st;
            mtTable = "(SELECT " + queryField + " FROM GW_MT_TASK_BAK UNION ALL SELECT " + queryField + " FROM MTTASK" + et + ")";
        } else {
            visitTable = "(SELECT * from VST_DETAIL" + st + " union all SELECT * from VST_DETAIL" + et + ")";
            mtTable = "(SELECT " + queryField + " FROM GW_MT_TASK_BAK UNION ALL SELECT " + queryField + " FROM MTTASK" + st + " UNION ALL SELECT " + queryField + " FROM MTTASK"
                    + et + ")";
        }

        if(isMysql) {
            timeList.clear();
            if(st.equals(et)) {
                String innerSql = segmentSql("MTTASK" + st, "VST_DETAIL" + st, queryVo, timeList);
                sql = maxRowSql(innerSql);
            } else {
                String innerSql = segmentSql("MTTASK" + st, "VST_DETAIL" + st, queryVo, timeList) + " UNION ALL " + segmentSql("MTTASK" + et, "VST_DETAIL" + et, queryVo, timeList);
                sql = maxRowSql(innerSql);
            }
//            sql = "SELECT * FROM (SELECT RV.PHONE,RV.TASKID,RV.TITLE,RV.SENDTIME,RV.LASTVSTTIME,RV.LASTVSTIP,RV.VSTCOUNT FROM (SELECT * FROM( SELECT A.TITLE,B.TASKID,B.PHONE,B.SENDTIME,C.VSTTM AS LASTVSTTIME,C.SRCADDRESS AS LASTVSTIP,IFNULL(C.TIMES, 0) AS VSTCOUNT FROM LF_URLTASK A LEFT JOIN "+mtTable+" B ON A.TASKID = B.TASKID LEFT JOIN "+visitTable+" C ON B.TASKID = C.CUSTID AND B.PHONE = C.PHONE WHERE B.PKNUMBER = 1 AND B.SENDTIME>=? AND B.SENDTIME<? ";
        } else {
            sql = "SELECT * FROM (SELECT RV.PHONE,RV.TASKID,RV.TITLE,RV.SENDTIME,RV.LASTVSTTIME,RV.LASTVSTIP,RV.VSTCOUNT,ROW_NUMBER() OVER(PARTITION BY TASKID, PHONE ORDER BY VSTCOUNT DESC) AS RM FROM ( SELECT A.TITLE,B.TASKID,B.PHONE,B.SENDTIME,C.VSTTM AS LASTVSTTIME,C.SRCADDRESS AS LASTVSTIP,ISNULL(C.TIMES, 0) AS VSTCOUNT FROM LF_URLTASK A LEFT JOIN "+mtTable+" B ON A.TASKID = B.TASKID LEFT JOIN "+visitTable+" C ON B.TASKID = C.CUSTID AND B.PHONE = C.PHONE WHERE B.PKNUMBER = 1 AND B.SENDTIME>=? AND B.SENDTIME<? ";
        }

//        String sql = "SELECT * FROM (SELECT RV.PHONE,RV.TASKID,RV.TITLE,RV.SENDTIME,RV.LASTVSTTIME,RV.LASTVSTIP,RV.VSTCOUNT,ROW_NUMBER() OVER(PARTITION BY TASKID, PHONE ORDER BY VSTCOUNT DESC) AS RM FROM ( SELECT A.TITLE,B.TASKID,B.PHONE,B.SENDTIME,C.VSTTM AS LASTVSTTIME,C.SRCADDRESS AS LASTVSTIP,ISNULL(C.TIMES, 0) AS VSTCOUNT FROM LF_URLTASK A LEFT JOIN "+mtTable+" B ON A.TASKID = B.TASKID LEFT JOIN "+visitTable+" C ON B.TASKID = C.CUSTID AND B.PHONE = C.PHONE WHERE B.PKNUMBER = 1 AND B.SENDTIME>=? AND B.SENDTIME<? ";

//        if (queryVo.getUrl() != null && !"".equals(queryVo.getUrl())) {
//            sql = sql + " AND A.NETURL = '" + queryVo.getUrl().trim() + "'";
//        }
//        if (queryVo.getCorpCode() != null && !"".equals(queryVo.getCorpCode())) {
//            sql = sql + " AND A.CORP_CODE = '" + queryVo.getCorpCode().trim() + "'";
//        }
//        if (queryVo.getPhone() != null && !"".equals(queryVo.getPhone())) {
//            sql = sql + " AND B.PHONE = '" + queryVo.getPhone().trim() + "'";
//        }

        if(isMysql) {
//            sql = sql + ") temp ORDER BY temp.VSTCOUNT DESC) RV GROUP BY RV.TASKID,RV.PHONE) RS WHERE 1=1 ";
            sql = "SELECT * FROM (" + sql + ") RS WHERE 1=1 ";
        } else {
            if (queryVo.getUrl() != null && !"".equals(queryVo.getUrl())) {
                sql = sql + " AND A.NETURL = '" + queryVo.getUrl().trim() + "'";
            }
            if (queryVo.getCorpCode() != null && !"".equals(queryVo.getCorpCode())) {
                sql = sql + " AND A.CORP_CODE = '" + queryVo.getCorpCode().trim() + "'";
            }
            if (queryVo.getPhone() != null && !"".equals(queryVo.getPhone())) {
                sql = sql + " AND B.PHONE = '" + queryVo.getPhone().trim() + "'";
            }
            sql = sql+") RV ) RS WHERE RS.RM=1";
        }

        if (queryVo.getLastIP() != null && !"".equals(queryVo.getLastIP())) {
            sql = sql + " AND RS.lastVstIP LIKE '%" + queryVo.getLastIP() + "%'";
        }
        if (queryVo.getVisitStatus() != null && !"".equals(queryVo.getVisitStatus())) {
            if ("0".equals(queryVo.getVisitStatus())) {
                sql = sql + " AND RS.vstCount = 0";
            }
            if ("1".equals(queryVo.getVisitStatus())) {
                sql = sql + " AND RS.vstCount > 0";
            }
        }
		/**
		 * 新增taskId查询,修改人moll
		 */
		if(queryVo.getTaskId()!=null && !"".equals(queryVo.getTaskId())){
			sql = sql+" AND RS.TASKID ="+queryVo.getTaskId();
		}

        return sql;
    }

    private String generateSql(String mtTaskTableName, LinkReportQueryVo queryVo) {
	    StringBuffer sql = new StringBuffer();
        String tempField = " SELECT A.STR_TASKID,A.TITLE,B.PHONE,B.TASKID,B.UNICOM,B.ERRORCODE,B.MESSAGE,B.SENDTIME,B.PKNUMBER FROM ";
        sql.append(tempField);
        sql.append("(SELECT TITLE,STR_TASKID,TASKID,SEND_TM FROM LF_URLTASK WHERE 1=1 ");
        if(StringUtils.isNotEmpty(queryVo.getCorpCode()) && !"100000".equals(queryVo.getCorpCode())) {
            sql.append(" AND CORP_CODE=").append(queryVo.getCorpCode());
        }
        sql.append(" AND NETURL='")
                .append(queryVo.getUrl()).append("'");
        sql.append(") A ");
        String tableSql = " INNER JOIN " + mtTaskTableName + " B ON A.TASKID = B.TASKID AND B.PKNUMBER=1 AND B.SENDTIME>=? AND B.SENDTIME<? ";
        sql.append(tableSql);
        if(StringUtils.isNotEmpty(queryVo.getPhone())) {
            sql.append(" AND B.PHONE='").append(queryVo.getPhone()).append("'");
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
	    sql.append(" SELECT C.PHONE,C.TASKID,C.UNICOM,C.ERRORCODE,C.MESSAGE,C.SENDTIME,C.PKNUMBER,C.TITLE,D.VSTTM AS LASTVSTTIME,D.SRCADDRESS AS LASTVSTIP,IFNULL(D.TIMES,0) AS VSTCOUNT FROM ");
        sql.append("(").append(generateSql("GW_MT_TASK_BAK", queryVo)).append(" UNION ALL ")
                .append(generateSql(mtTaskTableName, queryVo)).append(") C ").append(" LEFT JOIN ").append(vstTableName).append(" D ON C.STR_TASKID=D.CUSTID AND C.PHONE=D.PHONE ");
	    return sql.toString();
    }

    private String maxRowSql(String innerSql) {
	    StringBuffer sql = new StringBuffer();
	    sql.append("SELECT t.UNICOM,t.ERRORCODE,t.MESSAGE,t.SENDTIME,t.PKNUMBER,t.TITLE,t.LASTVSTTIME,t.LASTVSTIP,t.TASKID,t.PHONE,SUBSTRING_INDEX(GROUP_CONCAT(t.VSTCOUNT ORDER BY t.VSTCOUNT DESC),',',1) VSTCOUNT FROM (");
	    sql.append(innerSql);
	    sql.append(") t GROUP BY t.TASKID,t.PHONE ");
	    return sql.toString();
    }

    @Deprecated
    private String innerJoinRTabSql(String innerSql) {
        StringBuffer sql = new StringBuffer();
        sql.append(" INNER JOIN ( ");
        sql.append(innerSql);
        sql.append(") E ON M.TASKID=E.TASKID AND M.PHONE=E.PHONE AND M.VSTCOUNT=E.VSTCOUNT ");
        return sql.toString();
    }


}
