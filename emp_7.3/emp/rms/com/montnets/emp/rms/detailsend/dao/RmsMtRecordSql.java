package com.montnets.emp.rms.detailsend.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.biztype.TableLfBusManager;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sms.TableMtTask01_12;
import com.montnets.emp.table.template.TableLfTemplate;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

class RmsMtRecordSql{
     private static IGenericDAO genericDao = new DataAccessDriver().getGenericDAO();
     /**
      * 查询日期格式化：yyyy-MM-dd HH:mm:ss
      */
     private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     /**
      * 查询日期格式化：yyyy
      */
     private SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");

     /**
      * 查询日期格式化：MM
      */
     private  SimpleDateFormat sdf_MM = new SimpleDateFormat("MM");
     //无业务类型
    private static final String NONBUSTYPE = "-1";

    static String getFieldSql(String aliasName){
        if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE || StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
            return  "SELECT " +
                    aliasName + "."+ TableMtTask01_12.USER_ID +"," +/*sp账号*/
                    aliasName + "."+ TableMtTask01_12.SPGATE +"," +/*通道*/
                    aliasName + "."+ TableMtTask01_12.PHONE +"," +/*手机号*/
                    aliasName + "."+ TableMtTask01_12.SVRTYPE +"," +/*业务类型*/
                    aliasName + "."+ TableMtTask01_12.TASK_ID +"," +/*任务批次*/
                    "to_char("+ aliasName + "."+ TableMtTask01_12.SEND_TIME + ",\'YYYY-MM-DD hh24:mi:ss\') " + TableMtTask01_12.SEND_TIME + "," +/*发送时间*/
                    aliasName + "."+ TableMtTask01_12.UNICOM +"," +/*运营商*/
                    "to_char("+ aliasName + "."+ TableMtTask01_12.RECV_TIME + ",\'YYYY-MM-DD hh24:mi:ss\') " + TableMtTask01_12.RECV_TIME +"," +/*接收时间*/
                    aliasName + "."+ TableMtTask01_12.USERMSGID +"," +/*自定义流水号(旧)*/
                    aliasName + "."+ TableMtTask01_12.PT_MSG_ID +"," +/*运营商流水号*/
                    aliasName + "."+ TableMtTask01_12.CUSTID +"," +/*自定义流水号(新)*/
                    aliasName + "."+ TableMtTask01_12.ERROR_CODE2 +"," +/*富信下载状态报告错误码*/
                    aliasName + "."+ TableMtTask01_12.ERRO_RCODE +"," +/*接收状态报告错误码*/
                    "to_char("+ aliasName + "."+ TableMtTask01_12.DOWNTM + ",\'YYYY-MM-DD hh24:mi:ss\') " + TableMtTask01_12.DOWNTM +"," +/*下载时间*/
                    aliasName + "."+ TableMtTask01_12.CHGRADE +"," +/*信息档级*/
                    "lfmttask" + "."+ TableLfMttask.TITLE +"," +/*发送主题*/
                    "lftemplate" + "."+ TableLfTemplate.TM_NAME +"," +/*富信主题*/
                    "lftemplate" + "."+ TableLfTemplate.TM_ID +"," +/*富信自增id*/
                    aliasName + "."+ TableMtTask01_12.TMPLID +" "/*模板Id*/;
        }
        return  "SELECT " +
                aliasName + "."+ TableMtTask01_12.USER_ID +"," +/*sp账号*/
                aliasName + "."+ TableMtTask01_12.SPGATE +"," +/*通道*/
                aliasName + "."+ TableMtTask01_12.PHONE +"," +/*手机号*/
                aliasName + "."+ TableMtTask01_12.SVRTYPE +"," +/*业务类型*/
                aliasName + "."+ TableMtTask01_12.TASK_ID +"," +/*任务批次*/
                aliasName + "."+ TableMtTask01_12.SEND_TIME +"," +/*发送时间*/
                aliasName + "."+ TableMtTask01_12.UNICOM +"," +/*运营商*/
                aliasName + "."+ TableMtTask01_12.RECV_TIME +"," +/*接收时间*/
                aliasName + "."+ TableMtTask01_12.USERMSGID +"," +/*自定义流水号(旧)*/
                aliasName + "."+ TableMtTask01_12.PT_MSG_ID +"," +/*运营商流水号*/
                aliasName + "."+ TableMtTask01_12.CUSTID +"," +/*自定义流水号(新)*/
                aliasName + "."+ TableMtTask01_12.ERROR_CODE2 +"," +/*富信下载状态报告错误码*/
                aliasName + "."+ TableMtTask01_12.ERRO_RCODE +"," +/*接收状态报告错误码*/
                aliasName + "."+ TableMtTask01_12.DOWNTM +"," +/*下载时间*/
                aliasName + "."+ TableMtTask01_12.CHGRADE +"," +/*信息档级*/
                "lfmttask" + "."+ TableLfMttask.TITLE +"," +/*发送主题*/
                "lftemplate" + "."+ TableLfTemplate.TM_NAME +"," +/*富信主题*/
                "lftemplate" + "."+ TableLfTemplate.TM_ID +"," +/*富信自增id*/
                aliasName + "."+ TableMtTask01_12.TMPLID +" "/*模板Id*/;
    }

    static String getRealTableSql() {
        return "FROM  GW_MT_TASK_BAK gwmttask " + StaticValue.getWITHNOLOCK() +
                " LEFT JOIN " + TableLfTemplate.TABLE_NAME + " lftemplate " +
                StaticValue.getWITHNOLOCK() + " ON gwmttask."+ TableMtTask01_12.TMPLID +" = " +
                "lftemplate." + TableLfTemplate.SP_TEMPLID + " LEFT JOIN " +
                TableLfMttask.TABLE_NAME + " lfmttask " + StaticValue.getWITHNOLOCK() +
                " ON lfmttask." + TableLfMttask.TASKID + " = gwmttask."+ TableMtTask01_12.TASK_ID +" AND lfmttask."
                + TableLfMttask.MS_TYPE + "= 21";
    }

    static String getConditionSql(LinkedHashMap<String,String> conditionMap,String aliasName) {
        StringBuilder sql = new StringBuilder();
        //从conditionMap中获取属性值
        String spUsers = conditionMap.get("spUsers");
        String taskId = conditionMap.get("taskId");
        String phone = conditionMap.get("phone");
        String sendSubject = conditionMap.get("sendSubject");
        String rmsSubject = conditionMap.get("rmsSubject");
        String busType = conditionMap.get("busType");
        String spUser = conditionMap.get("spUser");
        String statusCode = conditionMap.get("statusCode");
        String sendTime = conditionMap.get("sendTime");
        String recvTime = conditionMap.get("recvTime");
        String downStatus = conditionMap.get("downStatus");
        String curUserCode = conditionMap.get("curUserCode");
        String curUserId = conditionMap.get("curUserId");
        String domUserCode = conditionMap.get("domUserCode");
        String spUserPri = conditionMap.get("spUserPri");
        String curCorpCode = conditionMap.get("curCorpCode");

        sql.append(" WHERE ").append(aliasName).append(".MSGTYPE = 11");
        //多企业，且未绑定sp账号，则查询结果为空
        if(StaticValue.getCORPTYPE() == 1 && StringUtils.isEmpty(spUsers)) {
            sql.append(" AND 1=2 ");
        }
        //任务批次
        if (StringUtils.isNotEmpty(taskId)) {
            sql.append(" AND ").append(aliasName).append(".").append(TableMtTask01_12.TASK_ID).append("=").append(taskId.trim());
        }
        //手机号
        if (StringUtils.isNotEmpty(phone)) {
            sql.append(" AND ").append(aliasName).append(".").append(TableMtTask01_12.PHONE).append("='").append(phone.trim()).append("'");
        }
        //发送主题
        if (StringUtils.isNotEmpty(sendSubject)) {
            sql.append(" AND ").append("lfmttask").append(".").append(TableLfMttask.TITLE).append(" LIKE '%").append(sendSubject.trim()).append("%'");
        }
        //富信主题
        if (StringUtils.isNotEmpty(rmsSubject)) {
            sql.append(" AND ").append("lftemplate").append(".").append(TableLfTemplate.TM_NAME).append(" LIKE '%").append(rmsSubject.trim()).append("%'");
        }
        //发送时间
        if (StringUtils.isNotEmpty(sendTime)) {
            sql.append(" AND ").append(aliasName).append(".").append(TableMtTask01_12.SEND_TIME).append(" >= ").append(genericDao.getTimeCondition(sendTime));
        }
        if (StringUtils.isNotEmpty(recvTime)) {
            sql.append(" AND ").append(aliasName).append(".").append(TableMtTask01_12.SEND_TIME).append(" <= ").append(genericDao.getTimeCondition(recvTime));
        }
        //状态码
        if (StringUtils.isNotEmpty(statusCode)) {
            sql.append(" AND ").append(aliasName).append(".").append(TableMtTask01_12.ERRO_RCODE).append(" LIKE '%").append(statusCode).append("%'");
        }
        //下载状态
        if (StringUtils.isNotEmpty(downStatus)) {
            if("0".equals(downStatus)){
                //成功
                sql.append(" AND ").append(aliasName).append(".").append(TableMtTask01_12.ERROR_CODE2).append("='DELIVRD'");
            }else if("1".equals(downStatus)){
                //失败
                sql.append(" AND ").append(aliasName).append(".").append(TableMtTask01_12.ERROR_CODE2).append("!='DELIVRD'");
                sql.append(" AND ").append(aliasName).append(".").append(TableMtTask01_12.ERROR_CODE2).append("!=''");
                sql.append(" AND ").append(aliasName).append(".").append(TableMtTask01_12.ERROR_CODE2).append(" IS NOT NULL");
            }else if("2".equals(downStatus)){
                //未返
                sql.append(" AND ").append(aliasName).append(".").append(TableMtTask01_12.ERROR_CODE2).append("=''");
            }
        }
        //业务类型
        if (StringUtils.isNotEmpty(busType)) {
            //无业务类型
            if(NONBUSTYPE.equals(busType)){
                sql.append(" AND ").append(" NOT EXISTS (SELECT BUS_CODE FROM ").append(TableLfBusManager.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" WHERE BUS_CODE=").append(aliasName).append(".").append(TableMtTask01_12.SVRTYPE).append( ")");
            }else {
                sql.append(" AND ").append(aliasName).append(".").append(TableMtTask01_12.SVRTYPE).append("='").append(busType.trim()).append("'");
            }
        }
        //SP账号
        if (StringUtils.isNotEmpty(spUser)) {
            //如果是DB2或者oracle 则做兼容小写处理
            if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE== StaticValue.ORACLE_DBTYPE || StaticValue.DBTYPE== StaticValue.DB2_DBTYPE)){
                sql.append(" AND ").append(aliasName).append(".USERID").append(" in ('").append(spUser.toUpperCase().trim()).append("','").append(spUser.toLowerCase().trim()).append("') ");
            }else{
                sql.append(" AND ").append(aliasName).append(".USERID").append("='").append(spUser.trim()).append("'");
            }
        }
        //为多企业，且不是100000企业，且查询条件没选sp账号的情况下，则拼入绑定的发送账号
        if (StaticValue.getCORPTYPE() == 1 && !"100000".equals(curCorpCode) && StringUtils.isEmpty(spUser)) {
            if(spUsers != null){
                //sp账号个数
                int length = 0;
                if(spUsers.contains(",")) {
                    length = spUsers.split(",").length;
                }
                //如果个数小于50则用in
                if(length < 50) {
                    //如果是DB2或者oracle 则做兼容小写处理
                    if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE== StaticValue.ORACLE_DBTYPE || StaticValue.DBTYPE== StaticValue.DB2_DBTYPE)){
                        spUsers = spUsers.toUpperCase() + "," + spUsers.toLowerCase();
                    }
                    sql.append(" AND ").append(aliasName).append(".USERID IN (").append(spUsers).append(")");
                } else {
                    //如果是DB2或者oracle 则做兼容小写处理
                    if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE== StaticValue.ORACLE_DBTYPE || StaticValue.DBTYPE== StaticValue.DB2_DBTYPE)){
                        sql.append(" AND ").append(" EXISTS (SELECT SPUSER FROM LF_SP_DEP_BIND ").append(StaticValue.getWITHNOLOCK())
                                .append(" WHERE UPPER(SPUSER)=UPPER(").append(aliasName).append(".USERID) ");
                    }else{
                        sql.append(" AND ").append(" EXISTS (SELECT SPUSER FROM LF_SP_DEP_BIND ").append(StaticValue.getWITHNOLOCK())
                                .append(" WHERE SPUSER=").append(aliasName).append(".USERID ");
                    }
                    if(StringUtils.isNotEmpty(curUserCode)) {
                        sql.append(" AND CORP_CODE=").append(curUserCode);
                    }
                    sql.append(")");
                }
            }
        }
        /*//多企业需要加入企业编码进行筛选
        if (StaticValue.CORPTYPE == 1 && !"100000".equals(curCorpCode)){
            sql.append(" AND ").append("lfmttask.").append(TableLfMttask.CORP_CODE).append("='").append(curCorpCode).append("'");
        }*/
        //控制数据权限
        StringBuilder domUserCodeSql = new StringBuilder();
        //获取是否开启p2参数配置
        String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
        depcodethird2p2 = StringUtils.defaultIfEmpty(depcodethird2p2,"false");
        if(StringUtils.isNotEmpty(domUserCode)){
            //有权限查看的操作员编码有值，则需要拼入p1条件，以控制查询权限
            if(!"true".equals(depcodethird2p2)){
                domUserCodeSql.append(aliasName).append(".p1 in (").append(domUserCode).append(")");
            }else {
                domUserCodeSql.append(" (").append(aliasName).
                append(".p2 IN (SELECT DEP_CODE_THIRD FROM LF_DEP WHERE DEP_ID IN (SELECT DEP_ID FROM LF_DOMINATION WHERE USER_ID=").
                 append(curUserId).append("))").append(" OR ").
                append(aliasName).append(".p1=").append(curUserCode).append(")");
            }
        }else if(domUserCode == null){
            //有权限查看的操作员编码有值，则需要拼入p1条件，以控制查询权限
            if(!"true".equals(depcodethird2p2)){
                domUserCodeSql.append(" (").append(aliasName).append(".p1 IN (SELECT USER_CODE FROM LF_SYSUSER WHERE DEP_ID IN (SELECT DEP_ID FROM LF_DOMINATION WHERE USER_ID=").append(curUserId).append("))")
                        .append(" OR ").append(aliasName).append(".p1=").append(curUserCode).append(")");
            }else {
                domUserCodeSql.append(" (").append(aliasName).append(".p2 IN (SELECT DEP_CODE_THIRD FROM LF_DEP WHERE DEP_ID IN (SELECT DEP_ID FROM LF_DOMINATION WHERE USER_ID=").append(curUserId).append("))")
                        .append(" OR ").append(aliasName).append(".p1=").append(curUserCode).append(")");
            }
        }

        StringBuilder spUserPriSql = new StringBuilder();
        if(StringUtils.isNotEmpty(spUserPri)){
            spUserPriSql.append(aliasName).append(".USERID in (").append(spUserPri).append(")");
        }else if(spUserPri == null){
            spUserPriSql.append(aliasName).
            append(".USERID IN (SELECT SPUSERID FROM LF_MT_PRI WHERE USER_ID=").
            append(curUserId).append(" AND CORP_CODE='").
            append(curCorpCode).append("')");
        }
        if(domUserCodeSql.length() != 0 && spUserPriSql.length() != 0){
            //sql.append(" AND (").append(depcodethird2p2).append(" OR ").append(spUserPriSql).append(")");
            sql.append(" AND (").append(domUserCodeSql).append(" OR ").append(spUserPriSql).append(")");
        }
        if(domUserCodeSql.length() != 0 && spUserPriSql.length() == 0){
            sql.append(" AND ").append(domUserCodeSql);
        }
        if(domUserCodeSql.length() == 0 && spUserPriSql.length() != 0){
            sql.append(" AND ").append(spUserPriSql);
        }
        return sql.toString();
    }

    static String getHisTableSql(String sendTime, String recvTime) throws Exception{
        try {
        	SimpleDateFormat sdf_MM = new SimpleDateFormat("MM");
        	SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");
        	SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //开始时间
            Calendar caStartTime = Calendar.getInstance();
            caStartTime.setTime(yyyyMMdd.parse(sendTime));
            //结束时间
            Calendar caEndTime = Calendar.getInstance();
            caEndTime.setTime(yyyyMMdd.parse(recvTime));

            String preTableName = "MTTASK" + sdf_yyyy.format(caStartTime.getTime())+ sdf_MM.format(caStartTime.getTime());
            String nextTableName = "MTTASK" + sdf_yyyy.format(caEndTime.getTime())+ sdf_MM.format(caEndTime.getTime());

            //两个表都不存在直接抛异常
            if(getMtHisTableExists(preTableName) <= 0 && getMtHisTableExists(nextTableName) <= 0){
                throw new Exception("企业富信-数据查询-发送明细查询-DAO查询下行历史记录，获取表名sql，两个表都不存在。"
                        + "preTableName=" + preTableName
                        + ",nextTableName=" + nextTableName);
            }
            //如果是同一个月，则查单表
            if(caStartTime.get(Calendar.MONTH) == caEndTime.get(Calendar.MONTH)) {
                return " FROM " + preTableName + " mttask " + StaticValue.getWITHNOLOCK() + "LEFT JOIN " + TableLfMttask.TABLE_NAME + " lfmttask "+ StaticValue.getWITHNOLOCK() + " ON mttask." +
                        TableMtTask01_12.TASK_ID + " = lfmttask." + TableLfMttask.TASKID + " and lfmttask." + TableLfMttask.MS_TYPE + "= 21" + " LEFT JOIN " +
                        TableLfTemplate.TABLE_NAME + " lftemplate " + StaticValue.getWITHNOLOCK() + " on lftemplate." + TableLfTemplate.SP_TEMPLID + " = mttask." + TableMtTask01_12.TMPLID;
            }
            //跨月查询，则需要查询两张表的记录，只允许查两个月以内的数据
            if(getMtHisTableExists(preTableName) <= 0){
                //上一个月表不存在
                EmpExecutionContext.error("企业富信-数据查询-发送明细查询-DAO查询下行历史记录，获取表名sql，表不存在。" + "tableName=" +  preTableName);
                return " FROM " + nextTableName + " mttask " + StaticValue.getWITHNOLOCK() + "LEFT JOIN " + TableLfMttask.TABLE_NAME + " lfmttask "+ StaticValue.getWITHNOLOCK() + " ON mttask." +
                        TableMtTask01_12.TASK_ID + " = lfmttask." + TableLfMttask.TASKID + " and lfmttask." + TableLfMttask.MS_TYPE + "= 21" + " LEFT JOIN " +
                        TableLfTemplate.TABLE_NAME + " lftemplate " + StaticValue.getWITHNOLOCK() + " on lftemplate." + TableLfTemplate.SP_TEMPLID + " = mttask." + TableMtTask01_12.TMPLID;
            }
            if(getMtHisTableExists(nextTableName) <= 0){
                //下一个月表不存在
                EmpExecutionContext.error("企业富信-数据查询-发送明细查询-DAO查询下行历史记录，获取表名sql，表不存在。" + "tableName=" +  nextTableName);
                return " FROM " + preTableName + " mttask " + StaticValue.getWITHNOLOCK() + "LEFT JOIN " + TableLfMttask.TABLE_NAME + " lfmttask "+ StaticValue.getWITHNOLOCK() + " ON mttask." +
                        TableMtTask01_12.TASK_ID + " = lfmttask." + TableLfMttask.TASKID + " and lfmttask." + TableLfMttask.MS_TYPE + "= 21" + " LEFT JOIN " +
                        TableLfTemplate.TABLE_NAME + " lftemplate " + StaticValue.getWITHNOLOCK() + " ON lftemplate." + TableLfTemplate.SP_TEMPLID + " = mttask." + TableMtTask01_12.TMPLID;
            }
            //两个表都存在，则用联合查询
            String fieldSql = "SELECT "+ TableMtTask01_12.USER_ID +","+ TableMtTask01_12.SPGATE +","+ TableMtTask01_12.PHONE +
                    ","+ TableMtTask01_12.TASK_ID +","+ TableMtTask01_12.ERRO_RCODE +","+ TableMtTask01_12.SEND_TIME +
                    ","+ TableMtTask01_12.UNICOM +","+ TableMtTask01_12.RECV_TIME +","+ TableMtTask01_12.USERMSGID +
                    ","+ TableMtTask01_12.CUSTID +","+ TableMtTask01_12.ERROR_CODE2 +","+ TableMtTask01_12.TMPLID +
                    ","+ TableMtTask01_12.DOWNTM +","+ TableMtTask01_12.CHGRADE + "," +  TableMtTask01_12.PT_MSG_ID+","+TableMtTask01_12.SVRTYPE  + ",MSGTYPE,P1";

            return " FROM (" + fieldSql + " FROM " + preTableName + StaticValue.getWITHNOLOCK() +
                    " UNION ALL " + fieldSql + " FROM " + nextTableName + StaticValue.getWITHNOLOCK() +
                    ") " + "mttask LEFT JOIN " + TableLfMttask.TABLE_NAME + " lfmttask " + StaticValue.getWITHNOLOCK() +" ON mttask." +
                    TableMtTask01_12.TASK_ID + " = lfmttask." + TableLfMttask.TASKID + " AND lfmttask." + TableLfMttask.MS_TYPE + "= 21" + " LEFT JOIN " +
                    TableLfTemplate.TABLE_NAME + " lftemplate " + StaticValue.getWITHNOLOCK() + " ON lftemplate." + TableLfTemplate.SP_TEMPLID + " = mttask." + TableMtTask01_12.TMPLID;
        } catch (Exception e) {
            EmpExecutionContext.error(e,"企业富信-数据查询-发送明细查询-查询历史下行记录-根据时间确定查询表出现异常");
            throw e;
        }
    }
     /**
      * 获取下行历史记录表名是否存在。
      * @param tableName 下行历史记录表名
      * @return 0：不存在；1：存在；小于0：异常情况。
      */
     private static int getMtHisTableExists(String tableName) {
         try {
             //查询sql
             String sql;
             switch (StaticValue.DBTYPE) {
                 case 1:
                     // oracle
                     sql = "SELECT count(*) ICOUNT FROM all_tables where table_name=upper('" + tableName + "') and OWNER=upper('" + SystemGlobals.getValue("montnets.emp.user") + "')";
                     break;
                 case 2:
                     // sqlserver2005
                     sql = "SELECT count(*) ICOUNT FROM sysobjects where id = object_id('" + tableName + "') and type = 'u'";
                     break;
                 case 3:
                     // MYSQL
                     sql = "SELECT count(*) ICOUNT FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='" + SystemGlobals.getValue("montnets.emp.databaseName") + "' and TABLE_NAME=upper('" + tableName + "')";
                     break;
                 case 4:
                     // DB2
                     sql = "SELECT count(*) ICOUNT FROM syscat.tables where tabname=upper('" + tableName + "')";
                     break;
                 default:
                     EmpExecutionContext.error("获取下行历史记录表名是否存在，不支持的数据库类型。dbType=" + StaticValue.DBTYPE);
                     return -1;
             }

             //查询新表是否存在
             List<DynaBean> resultList = new RmsMtRecordDao().getListDynaBeanBySql(sql);
             //出异常查不到记录
             if(resultList == null || resultList.size() == 0) {
                 EmpExecutionContext.error("获取下行历史记录表名是否存在，查询新表是否存在失败。sql="+sql);
                 return -2;
             }
             //出异常取不到结果（这种情况不太可能出现）
             if(resultList.get(0).get("icount") == null) {
                 EmpExecutionContext.error("获取下行历史记录表名是否存在，查询新表是否存在，获取结果为空。sql="+sql);
                 return -3;
             }

             String icount = resultList.get(0).get("icount").toString();
             return Integer.parseInt(icount);
         } catch (Exception e) {
             EmpExecutionContext.error("获取下行历史记录表名是否存在，异常。");
             return -9999;
         }
     }
 }
