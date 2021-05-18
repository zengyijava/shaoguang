package com.montnets.emp.rms.report.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.rms.report.vo.MtDataReportVo;
import com.montnets.emp.rms.table.TableMtDataReport;
import com.montnets.emp.table.corp.TableLfCorp;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import org.apache.commons.lang.StringUtils;

/**
 * 档位统计报表的sql
 * @date 2018-6-13 10:45:04
 * @author CHENG
 */
public class OperatorDegreeRptSql {
    /**
     * 获取档位统计报表的查询字段SQL
     *
     * @return sql
     */
    static String getFieldSql() {
        String sql = StaticValue.getCORPTYPE() == 1 ? ",lfcorp." + TableLfSpDepBind.CORP_CODE +
                ",lfcorp." + TableLfCorp.CORP_NAME : "";
        return "SELECT SUM(degree." + TableMtDataReport.ICOUNT + ") " + TableMtDataReport.ICOUNT +
        		",SUM(degree." + TableMtDataReport.RECFAIL + ") " + TableMtDataReport.RECFAIL +
                ",SUM(degree." + TableMtDataReport.RFAIL1 + ") " + TableMtDataReport.RFAIL1 +
                ",SUM(degree." + TableMtDataReport.RNRET + ") " + TableMtDataReport.RNRET +
                ",SUM(degree." + TableMtDataReport.RSUCC + ") " + TableMtDataReport.RSUCC +
                //",SUM(degree." + TableMtDataReport.DWSUCC + ") " + TableMtDataReport.DWSUCC +
                ",MAX(degree." + TableMtDataReport.IYMD + ") " + TableMtDataReport.IYMD +
                ",degree." + TableMtDataReport.CHGRADE +
                ",degree." + TableMtDataReport.SPISUNCM +
                ",degree." + TableMtDataReport.USERID +sql;

    }

    static String getTableSql() {
        if(StaticValue.getCORPTYPE() == 1){
            return " FROM " + TableMtDataReport.TABLE_NAME + " degree "+ StaticValue.getWITHNOLOCK()
                    + " LEFT JOIN " + TableLfSpDepBind.TABLE_NAME + " lfspbind ON degree."
                    + TableMtDataReport.USERID + "= lfspbind." + TableLfSpDepBind.SP_USER + " LEFT JOIN "
                    + TableLfCorp.TABLE_NAME + " lfcorp ON lfcorp." + TableLfCorp.CORP_CODE +
                    " = lfspbind." + TableLfSpDepBind.CORP_CODE;
        }else {
            return " FROM " + TableMtDataReport.TABLE_NAME + " degree "+ StaticValue.getWITHNOLOCK();
        }
    }

    static String getConditonSql(MtDataReportVo vo) {
        StringBuilder sql = new StringBuilder();
        String loginCorpCode = vo.getLoginCorpCode();
        String corpName = vo.getCorpName();
        String corpCode = vo.getCorpCode();
        String spUser = vo.getUserId();
        Integer degree = vo.getChgrade();
        Integer spisuncm = vo.getSpisuncm();
        Integer reportType = vo.getReportType();
        Integer year = vo.getY();
        Integer iMonth = vo.getImonth();
        String startTime = vo.getStartTime();
        String endTime = vo.getEndTime();
        //msg_type 11表示富信
        sql.append(" WHERE degree.").append(TableMtDataReport.MSGTYPE).append("= 11");
        //SP账号
        if(StringUtils.isNotEmpty(spUser)){
            sql.append(" AND degree.").append(TableMtDataReport.USERID).append("='").append(spUser).append("'");
        }
        //运营商
        if(spisuncm != null){
            sql.append(" AND degree.").append(TableMtDataReport.SPISUNCM).append("=").append(spisuncm);
        }
        //档位
        if(degree != null){
            sql.append(" AND degree.").append(TableMtDataReport.CHGRADE).append("=").append(degree);
        }
        if(StaticValue.getCORPTYPE() == 1){
            //如果为10W号企业则增加企业编码与企业名称查询
            if("100000".equals(loginCorpCode)){
                if(StringUtils.isNotEmpty(corpCode)){
                    sql.append(" AND lfspbind.").append(TableLfSpDepBind.CORP_CODE).append(" LIKE '%").append(corpCode).append("%'");
                }
                if(StringUtils.isNotEmpty(corpName)){
                    sql.append(" AND lfcorp.").append(TableLfCorp.CORP_NAME).append(" LIKE '%").append(corpName).append("%'");
                }
            }else {
                //非10W号加上企业编码筛选
                if(StringUtils.isNotEmpty(loginCorpCode)){
                    sql.append(" AND lfspbind.").append(TableLfSpDepBind.CORP_CODE).append("='").append(loginCorpCode).append("'");
                }
            }
        }
        // 年报表
        if(reportType != null && reportType == 2 && year != null) {
            sql.append(" AND  degree.").append(TableMtDataReport.Y).append("=").append(year);
        }
        //月报表
        if(reportType != null && reportType == 1){
            if(year != null) {
                sql.append(" AND  degree.").append(TableMtDataReport.Y).append("=").append(year);
            }
            // 月份
            if(iMonth != null) {
                sql.append(" AND  degree.").append(TableMtDataReport.IMONTH).append("=").append(iMonth);
            }
        }
        //日报表
        if(reportType != null && reportType == 0){
            // 开始时间
            if(StringUtils.isNotEmpty(startTime)) {
                sql.append(" AND degree.").append(TableMtDataReport.IYMD).append(">=").append(startTime.replaceAll("-", ""));
            }

            //结束时间
            if(StringUtils.isNotEmpty(endTime)) {
                sql.append(" AND degree.").append(TableMtDataReport.IYMD).append("<=").append(endTime.replaceAll("-", ""));
            }
        }
        return sql.toString();
    }

    public static String getOrderBySql(Boolean isDes) {
        StringBuilder sql = new StringBuilder();
        if(!isDes) {
            //不是详情则按档位排序
            sql.append(" ORDER BY degree.").append(TableMtDataReport.CHGRADE).append(" ASC");
        } else {
            sql.append(" ORDER BY ").append(TableMtDataReport.IYMD).append(" DESC");
        }
        return sql.toString();
    }

     static String getGroupbySql(Integer reportType, Boolean isDes) {
        StringBuilder commonSql = new StringBuilder()
                .append("degree.")
                .append(TableMtDataReport.SPISUNCM)
                .append(",degree.")
                .append(TableMtDataReport.CHGRADE)
                .append(",degree.")
                .append(TableMtDataReport.USERID);
                //.append(",lfspbind.")
                //.append(TableLfSpDepBind.CORP_CODE)
                //.append(",lfcorp.")
                //.append(TableLfCorp.CORP_NAME);
        StringBuilder sql = new StringBuilder();
        if(reportType == 2){
            sql.append(" GROUP BY ");
            if(isDes) {
                //点击详情时
                sql.append("degree.").append(TableMtDataReport.Y)
                        .append(",degree.")
                        .append(TableMtDataReport.IMONTH)
                        .append(",").append(commonSql);
            }else {
                sql.append("degree.")
                    .append(TableMtDataReport.Y)
                    .append(",")
                    .append(commonSql);
            }
        }else if(reportType == 1){
            sql.append(" GROUP BY ");
            if(isDes){
                //点击详情时
                sql.append("degree.")
                        .append(TableMtDataReport.IYMD)
                        .append(",").append(commonSql);
            }else {
                sql.append("degree.").append(TableMtDataReport.Y)
                        .append(",degree.")
                        .append(TableMtDataReport.IMONTH)
                        .append(",").append(commonSql);
            }
        }else if(reportType == 0){
            sql.append(" GROUP BY ");
            if(!isDes){
                sql.append(commonSql);
            }else {
                //点击详情时
                sql.append("degree.")
                    .append(TableMtDataReport.IYMD)
                    .append(",").append(commonSql);
            }
        }
        return sql.toString();
    }
}
