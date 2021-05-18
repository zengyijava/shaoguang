package com.montnets.emp.query.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;
import com.mysql.jdbc.Statement;
import org.apache.commons.beanutils.DynaBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 系统下行导出记录查询dao
 * @date 20181205
 * @author yangbo
 */
public class MtRptRecordDAO extends SuperDAO  {

    private static IGenericDAO genericDao = new DataAccessDriver().getGenericDAO();

    public List<DynaBean> findPageList(LinkedHashMap<String, String> conditionMap, PageInfo page, LfSysuser sysUser) {
        String permissionSql;
        //增加权限控制
        if(sysUser.getPermissionType() ==  2){
            //机构权限
            permissionSql = " AND (a.USERID IN (SELECT USER_ID from LF_SYSUSER WHERE DEP_ID in (SELECT DEP_ID from LF_DOMINATION WHERE USER_ID = " + sysUser.getUserId() + ") OR a.USERID = " + sysUser.getUserId() + "))";
        }else {
            //个人权限
            permissionSql = " AND a.USERID = " + sysUser.getUserId();
        }
        String sql = "select * from (select a.id,a.filename,b.NAME optname,a.status filestatus,a.createtime,a.generatetime,a.downloadtime FROM a_rptrecord a,lf_sysuser b where a.userid=b.user_id "+ permissionSql + " order by a.id desc) t";
        String conditionSql = getConditionSql(conditionMap);
        String execSql = sql + conditionSql;
        //去掉	1 = 1
        execSql = execSql.replaceAll("WHERE\\s+1=1\\s+(AND)?", "WHERE");
        String countSql = "select count(ID) totalcount from (" + execSql + ") s";
        //加上order by条件
        execSql += " ORDER BY T.createtime";
        return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(execSql, countSql, page, StaticValue.EMP_POOLNAME, null);
    }

    
    public Long addRptRecord(String fileName, String userid) {

        Connection conn = null;
        PreparedStatement ps = null;
        Long id = null;
        ResultSet generatedKeys = null;
        String timeFuncSql = "";
        String sql;
        try {
            conn = this.connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
            if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
                //如果为oracle库直接从序列取
                id = getSequenceNextValue(conn,"A_RPTRECORD_S");
                sql = "INSERT INTO A_RPTRECORD (id, filename, userid, status, createtime) VALUES ("+ id +", ?, ?, 1, SYSTIMESTAMP)";
            }else {
                if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
                    timeFuncSql = "NOW()";
                }else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
                    timeFuncSql = "GETDATE()";
                }else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
                    timeFuncSql = "SYSDATE";
                }
                sql = "INSERT INTO A_RPTRECORD (filename, userid, status, createtime) VALUES (?, ?, 1, "+ timeFuncSql +")";
            }

            EmpExecutionContext.sql("execute sql : " + sql);
            ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, fileName);
            ps.setString(2,userid);
            ps.executeUpdate();
            generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next() && (null == id || id == 0)) {
                id = generatedKeys.getLong(1);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "DAO方法插入下行导出记录获取主键执行失败！");
        } finally {
            try {
                super.close(generatedKeys,ps,conn);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "关闭数据库资源出错！");
            }

        }
        return id;
    }

    /**
     * 更新下行下载记录
     * @param fileId 主键
     * @param fileStatus 下载状态
     * @param needGeneratetime 是否需要更新生成时间
     * @param needDownloadtime 是否需要更新下载时间
     */
    
    public void updateRptRecord(String fileId, String fileStatus, boolean needGeneratetime, boolean needDownloadtime) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        String sqlContext = "";
        String dateSql = "";
        if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
            dateSql = "SYSTIMESTAMP";
        }else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
            dateSql = "NOW()";
        }else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
            dateSql = "GETDATE()";
        }else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
            dateSql = "CURRENT TIMESTAMP ";
        }
        if(needGeneratetime){
            sqlContext += ",generatetime=" + dateSql;
        }
        if(needDownloadtime){
            sqlContext += ",downloadtime=" + dateSql;
        }
        String sql = "UPDATE a_rptrecord set status=? " + sqlContext + " where id=?";

        try{
            conn = this.connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
            EmpExecutionContext.sql("execute sql : " + sql);
            ps = conn.prepareStatement(sql);
            ps.setString(1, fileStatus);
            ps.setString(2,fileId);
            ps.executeUpdate();
        } finally {
            try {
                super.close(null,ps,conn);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "关闭数据库资源出错！");
            }

        }
    }

    private String getConditionSql(LinkedHashMap<String, String> conditionMap){
        if (conditionMap == null) {
            return "";
        }
        StringBuilder conditionSql = new StringBuilder(" WHERE 1=1");
        String fileName = conditionMap.get("fileName");
        String name = conditionMap.get("name");
        String fileStatus = conditionMap.get("fileStatus");
        String startDate = conditionMap.get("startDate");
        String endDate = conditionMap.get("endDate");
        if(StringUtils.isNotBlank(fileName)){
            conditionSql.append(" AND T.FILENAME = '").append(fileName).append("'");
        }
        if(StringUtils.isNotBlank(name)){
            conditionSql.append(" AND T.OPTNAME = '").append(name).append("'");
        }
        if(StringUtils.isNotBlank(fileStatus)){
            conditionSql.append(" AND T.FILESTATUS = '").append(fileStatus).append("'");
        }
        if(StringUtils.isNotBlank(startDate)){
            conditionSql.append(" AND T.GENERATETIME >= ").append(genericDao.getTimeCondition(startDate));
        }
        if(StringUtils.isNotBlank(endDate)){
            conditionSql.append(" AND T.GENERATETIME <= ").append(genericDao.getTimeCondition(endDate));
        }
        return conditionSql.toString();
    }
}
