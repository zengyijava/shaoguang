package com.montnets.emp.shorturl.report.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.shorturl.report.table.TableVstDetail;
import com.montnets.emp.shorturl.report.vo.BatchVisitVo;
import com.montnets.emp.shorturl.report.vo.VstDetailVo;
import com.montnets.emp.shorturl.surlmanage.table.TableLfUrlTask;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sms.TableMtTask;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;

public class SurlAccessStatisticsSql {
    private static IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();

    public String getVisitCountSql(Long taskId,String yyyyMM){
        if(taskId == null){
            EmpExecutionContext.error("getVisitCountSql，获取sql异常，传入参数有误，taskId为null");
            return "";
        }
        StringBuffer sql = new StringBuffer();
        /*
        SELECT ISNULL(sum(TIMES), 0) visitCount,COUNT(*) visitorCount FROM (
        SELECT TIMES,PHONE,CUTID,ROW_NUMBER() OVER(PARTITION BY PHONE ORDER BY TIMES DESC) RM
        FROM VST_DETAIL201802 WHERE CUTID = '1234567')a WHERE RM =1
         */
        if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE) {
            sql.append("select ").append("ifnull(sum(").append(TableVstDetail.TIMES).
                    append("),0) visitCount,count(*) visitorCount from ( select ").append("max(temp.").
                    append(TableVstDetail.TIMES).append(") as times,").append("temp.").append(TableVstDetail.PHONE).
                    append(" from (select * from ").
                    append(TableVstDetail.TABLE_NAME).append(yyyyMM).append(" where ").append(TableVstDetail.CUTID).
                    append("='").append(taskId).append("' ").append(" ORDER BY ").
                    append(TableVstDetail.TIMES).append(" desc) as temp ").
                    append(" group by temp.").append(TableVstDetail.PHONE).append(") a");
        } else {
            sql.append("select ").append("isnull(sum(").append(TableVstDetail.TIMES).
                    append("),0) visitCount,count(*) visitorCount from ( select ").
                    append(TableVstDetail.TIMES).append(",").append(TableVstDetail.PHONE).
                    append(",").append(TableVstDetail.CUTID).append(",row_number() over(partition by ").
                    append(TableVstDetail.PHONE).append(" order by ").append(TableVstDetail.TIMES).
                    append(" desc) RM from ").append(TableVstDetail.TABLE_NAME).append(yyyyMM).
                    append(" where ").append(TableVstDetail.CUTID).append("='").append(taskId).
                    append("')a where RM = 1");
        }

        return sql.toString();
    }

    public static String getFieldSql() {
        StringBuffer sql = new StringBuffer();
        sql.append("select detail.").append(TableVstDetail.PHONE).
                append(",detail.").append(TableVstDetail.VSTTM).
                append(",detail.").append(TableVstDetail.SRCADDRESS);
                //append(",detail.").append(TableVstDetail.MOBILEAREA).
                //append(",detail.").append(TableVstDetail.PTCODE).
                //append(",detail.").append(TableVstDetail.WGNO).
                //append(",detail.").append(TableVstDetail.XWAPPROF).
                //append(",detail.").append(TableVstDetail.XBROTYPE).
                //append(",detail.").append(TableVstDetail.DRS).
                //append(",detail.").append(TableVstDetail.TIMES).
                //append(",detail.").append(TableVstDetail.CUTID).
                //append(",detail.").append(TableVstDetail.SURL).
                //append(",detail.").append(TableVstDetail.LURL).
                //append(",detail.").append(TableVstDetail.HTTP_HEADER).
                //append(",detail.").append(TableVstDetail.EXDATA).
                //append(",detail.").append(TableVstDetail.SRCPT).
                //append(",detail.").append(TableVstDetail.VSTMSGID).
                //append(",detail.").append(TableVstDetail.USERID).
                //append(",detail.").append(TableVstDetail.ID).
                //append(",detail.").append(TableVstDetail.ECID).
                //append(",detail.").append(TableVstDetail.PTMSGID).
                //append(",detail.").append(TableVstDetail.CTTM);
        return sql.toString();
    }

    public static String getVstDetailConditionSql(VstDetailVo vo) {
        boolean hasWhere = false;
        StringBuffer conditionSql = new StringBuffer();
        if(vo == null){
            EmpExecutionContext.error("getconditionSql，获取sql异常，传入参数有误，vo为null");
            return "";
        }
        //手机号
        if(vo.getPhone() != null && !"".equals(vo.getPhone())){
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append(TableVstDetail.PHONE).append("='").append(vo.getPhone().trim()).append("'");
        }
        /*
        //企业编码 多企业才启用企业编码筛选
        if(vo.getCropCode() != null && !"".equals(vo.getCropCode()) && !"100000".equals(vo.getCropCode()) && StaticValue.CORPTYPE == 1){
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append(TableVstDetail.ECID).append("=").append(Integer.parseInt(vo.getCropCode().trim()));
        }
        */
        //访问IP
        if(vo.getVisitIP() != null && !"".equals(vo.getVisitIP())){
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append(TableVstDetail.SRCADDRESS).append(" like '%").append(vo.getVisitIP()).append("%'");
        }
        //访问区域
        if(vo.getMobileArea() != null && !"".equals(vo.getMobileArea())){
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append(TableVstDetail.MOBILEAREA).append("=").append(vo.getMobileArea());
        }
        //指定时间段，访问时间
        if (vo.getVsttm() != null && !"".equals(vo.getVsttm())) {
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append(TableVstDetail.VSTTM).append(">=").append(genericDao.getTimeCondition(vo.getVsttm()));
        }
        //指定时间段，结束时间
        if (vo.getEndTime() != null && !"".equals(vo.getEndTime())) {
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append(TableVstDetail.VSTTM).append("<=").append(genericDao.getTimeCondition(vo.getEndTime()));
        }
        return conditionSql.toString();
    }

    private static boolean addWhereOrAnd(StringBuffer sql, boolean hasWhere) {
        if(hasWhere) {
            //有where则加and
            sql.append(" AND ");
        } else {
            //没where则加where
            sql.append(" WHERE ");
        }
        return true;
    }

    public static String getConditionSql(BatchVisitVo visitVo) {
        boolean hasWhere = false;
        StringBuffer conditionSql = new StringBuffer();
        if(visitVo == null){
            EmpExecutionContext.error("getconditionSql，获取sql异常，传入参数有误，visitVo为null");
            return "";
        }
        //任务批次号
        if(visitVo.getTaskId() != null ){
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append("mttask.").append(TableLfMttask.TASKID).append("=").append(visitVo.getTaskId());
        }
        //发送主题
        if(visitVo.getTitle() != null && !"".equals(visitVo.getTitle())){
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append("mttask.").append(TableLfMttask.TITLE).append(" like '%").append(visitVo.getTitle()).append("%'");
        }
        //长地址
        if(visitVo.getNetUrl() != null && !"".equals(visitVo.getNetUrl())){
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append("urlTask.").append(TableLfUrlTask.NETURL).append(" like '%").append(visitVo.getNetUrl()).append("%'");
        }
        //指定时间段，访问时间
        if (visitVo.getStartSendTime() != null && !"".equals(visitVo.getStartSendTime())) {
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append(TableLfUrlTask.PLAN_TIME).append(">=").append(genericDao.getTimeCondition(visitVo.getStartSendTime()));
        }
        //指定时间段，结束时间
        if (visitVo.getEndSendTime() != null && !"".equals(visitVo.getEndSendTime())) {
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append(TableLfUrlTask.PLAN_TIME).append("<=").append(genericDao.getTimeCondition(visitVo.getEndSendTime()));
        }
        //MS_TYPE
        if (visitVo.getMsType() != null) {
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append("mttask.").append(TableLfMttask.MS_TYPE).append("=").append(31);
        }
        //机构
        if (visitVo.getDepIds() != null && !"".equals(visitVo.getDepIds())) {
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append("mttask.").append(TableLfMttask.USER_ID).append(" in (select user_id from lf_sysuser where ").append(visitVo.getDepIds()).append(")");
        }
        //操作员
        if (visitVo.getUserIds() != null && !"".equals(visitVo.getUserIds())) {
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append("mttask.").append(TableLfMttask.USER_ID).append(" in (").append(visitVo.getUserIds()).append(")");
        }
        //企业编码 多企业才启用企业编码筛选
        if(!"100000".equals(visitVo.getCurrCorpCode()) && StaticValue.getCORPTYPE() == 1){
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append("mttask.").append(TableLfMttask.CORP_CODE).append("='").append(visitVo.getCurrCorpCode()).append("'");
        }
        //此处对应为如果查询一级机构则直接查询企业
        if(!StringUtils.isEmpty(visitVo.getDepIds()) && !StringUtils.isEmpty(visitVo.getCorpCode()) && StaticValue.getCORPTYPE() == 1){
            hasWhere = addWhereOrAnd(conditionSql, hasWhere);
            conditionSql.append("mttask.").append(TableLfMttask.CORP_CODE).append("='").append(visitVo.getCorpCode()).append("'");
        }
        return conditionSql.toString();
    }

    public static String getBatchVisitTableSql() {
        StringBuffer sql = new StringBuffer();
        //FROM LF_MTTASK mttask LEFT JOIN LF_URLTASK urlTask ON mttask.TASKID = urlTask.TASKID
        sql.append(" FROM ").append(TableLfMttask.TABLE_NAME).append(" mttask ").append(StaticValue.getWITHNOLOCK()).append("LEFT JOIN ")
                .append(TableLfUrlTask.TABLE_NAME).append(" urlTask ").append(StaticValue.getWITHNOLOCK()).append("ON mttask.").append(TableLfMttask.TASKID)
                .append(" = urlTask.").append(TableLfUrlTask.TASKID);
        return sql.toString();
    }

    public static String getBatchVisitFieldSql() {
        StringBuffer sql = new StringBuffer();
        sql.append("select mttask.").append(TableLfMttask.USER_ID).
                append(",mttask.").append(TableLfMttask.TASKID).
                append(",mttask.").append(TableLfMttask.EFF_COUNT).
                append(",mttask.").append(TableLfMttask.MSG).
                append(",mttask.").append(TableLfMttask.TITLE).
                append(",urlTask.").append(TableLfUrlTask.NETURL).
                append(",urlTask.").append(TableLfUrlTask.DOMAIN_URL).
                append(",urlTask.").append(TableLfUrlTask.VALID_DAYS).
                append(",urlTask.").append(TableLfUrlTask.PLAN_TIME).
                append(",urlTask.").append(TableLfUrlTask.CREATE_TM);
        //需要拼接一个select语句
        //SELECT COUNT(DISTINCT PHONE) VISITORCOUNT FROM VST_DETAIL WHERE CUTID = ''
        return sql.toString();
    }

    public static String getHasVisitedSql(String taskId, String vstDetailTableName) {
        StringBuffer sql = new StringBuffer();
        //select detail.PHONE,detail.VSTTM,detail.SRCADDRESS
        //FROM VST_DETAIL detail where detail.CUTID = ''
        String fieldSql = getFieldSql();
        sql.append(fieldSql).
            append(" from ").append(vstDetailTableName).
            append(" detail where detail.").append(TableVstDetail.CUTID).
            append("='").append(taskId).append("'");
        return sql.toString();
    }

    private static String getUnVisitSql(String taskId, String mttaskTableName, String vstDetailTableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("select a2p.").append(TableMtTask.PHONE).
            append(",null ").append(TableVstDetail.VSTTM).
            append(",null ").append(TableVstDetail.SRCADDRESS).
            append(" from (select a2.").append(TableMtTask.PHONE).
            append(" from ( select ").append(TableMtTask.PHONE).
//            append(",").append(TableMtTask.TASK_ID).append(" from ").
//            append(mttaskTableName).append(" a union all select ").
//            append(TableMtTask.PHONE).append(",").append(TableMtTask.TASK_ID).
//            append(" from gw_mt_task_bak) a2 where a2.").append(TableMtTask.TASK_ID).
//            append("=").append(taskId).append(") a2p where a2p.").append(TableMtTask.PHONE).
            append(" from gw_mt_task_bak where ").append(TableMtTask.TASK_ID).
            append("=").append(taskId).append(")a2) a2p where a2p.").append(TableMtTask.PHONE).
            append(" not in(select distinct ").append(vstDetailTableName).append(".").append(TableVstDetail.PHONE).
            append(" from ").append(vstDetailTableName).append(" where ").append(TableVstDetail.CUTID).
            append("='").append(taskId).append("')");
        return sql.toString();
    }

    public static String getAllVisitedSql(VstDetailVo vstDetailVo) {
        StringBuffer sql = new StringBuffer();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        //根据批次发送时间获取表名
        String mttaskTableName = "MTTASK" + sdf.format(vstDetailVo.getSendTime().getTime());
        String vstDetailTableName = "VST_DETAIL" + sdf.format(vstDetailVo.getSendTime().getTime());

        //分别获取已访问的sql和未访问的sql然后用union all拼接
        String hasVisitedSql = getHasVisitedSql(vstDetailVo.getTaskId(),vstDetailTableName);

        String unVisitSql = getUnVisitSql(vstDetailVo.getTaskId(),mttaskTableName,vstDetailTableName);

        String conditionSql = getVstDetailConditionSql(vstDetailVo);

        //未访问只查 unVisitSql
        if(vstDetailVo.getVisitStatus() != null && !"".equals(vstDetailVo.getVisitStatus()) && !"1".equals(vstDetailVo.getVisitStatus())){
            sql.append("select * from (").append(unVisitSql).append(") allvisit").append(conditionSql);
            return sql.toString();
        }
        sql.append("select * from (").append(hasVisitedSql).
            append(" union all ").append(unVisitSql).append(") allvisit").append(conditionSql);
        return sql.toString();
    }
}
