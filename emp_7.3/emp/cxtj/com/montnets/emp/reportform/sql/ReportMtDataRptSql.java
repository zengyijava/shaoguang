package com.montnets.emp.reportform.sql;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.reportform.bean.ReportVo;
import com.montnets.emp.reportform.cxtjenum.JumpPathEnum;
import com.montnets.emp.reportform.dao.impl.ReportDaoImpl;
import com.montnets.emp.table.biztype.TableLfBusManager;
import com.montnets.emp.table.report.TableAprovinceCity;
import com.montnets.emp.table.report.TableMmsDatareport;
import com.montnets.emp.table.report.TableMtDatareport;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 处理查询统计报表sql
 *
 * @author chenguang
 * @date 2018-12-13 14:09:09
 */
public class ReportMtDataRptSql {
    /**
     * 日报表
     */
    public static final int DAY_REPORT = 2;
    /**
     * 月报表
     */
    public static final int MONTH_REPORT = 0;
    /**
     * 年报表
     */
    public static final int YEAR_REPORT = 1;
    /**
     * 短信标识
     */
    private static final int SMS_TYPE = 0;
    /**
     * 10W 号
     */
    public static final String CORPCODE_10W = "100000";

    private static final String COMMON_FIELD_SQL = "SELECT SUM(" + TableMtDatareport.ICOUNT + ") ICOUNT," +
            "SUM(" + TableMtDatareport.RSUCC + ") RSUCC," +
            "SUM(" + TableMtDatareport.RFAIL1 + ") RFAIL1," +
            "SUM(" + TableMtDatareport.RFAIL2 + ") RFAIL2," +
            "SUM(" + TableMtDatareport.RNRET + ") RNRET";

    public static String getAreaRptFieldSql() {
        return COMMON_FIELD_SQL +
                ",MAX(" + TableAprovinceCity.CITY + ") CITY," +
                "MAX(" + TableMtDatareport.IYMD + ") IYMD," +
                "APCITY." + TableAprovinceCity.PROVINCE + "  PROVINCE ";
    }

    public static String getAreaRptTableSql(Integer msType) {
        String talbeName = msType == SMS_TYPE ? TableMtDatareport.TABLE_NAME : TableMmsDatareport.TABLE_NAME;
        return " FROM " + talbeName + " MD LEFT JOIN ( SELECT * FROM  " + TableAprovinceCity.TABLE_NAME + ") APCITY ON MD." + TableMtDatareport.MOBILEAREA + " = APCITY." + TableAprovinceCity.AREACODE;
    }

    public static String getAreaRptConditionSql(ReportVo queryEntity, LfSysuser sysUser) {
        StringBuilder sql = new StringBuilder(" WHERE 1=1");
        handleTimeCondition(queryEntity.getReportType(), queryEntity.getQueryTime(), sql);
        //区域
        if (!StringUtils.isEmpty(queryEntity.getProvinces()) && !"[]".equals(queryEntity.getProvinces())) {
            sql.append(" AND APCITY." + TableAprovinceCity.PROVINCECODE + " = ").append(queryEntity.getProvince());
            if (!StringUtils.isEmpty(queryEntity.getCity())) {
                sql.append(" AND APCITY." + TableAprovinceCity.AREACODE + " = ").append(queryEntity.getCity());
            }
        }else {
            if(queryEntity.isDetail()){
                if(StringUtils.isNotBlank(queryEntity.getProvince())){
                    sql.append(" AND APCITY.").append(TableAprovinceCity.PROVINCE).append("= '").append(queryEntity.getProvince()).append("'");
                }
                if(StringUtils.isNotBlank(queryEntity.getCity())){
                    sql.append(" AND APCITY.").append(TableAprovinceCity.CITY).append("= '").append(queryEntity.getCity()).append("'");
                }
                if("未知".equals(queryEntity.getProvinceAndCity())){
                    sql.append(" AND APCITY.").append(TableAprovinceCity.PROVINCE).append(" IS NULL");
                }
            }
        }
        //处理更多维度
        handleMoreDimensionByModule(queryEntity, sql, sysUser, JumpPathEnum.areaReport.getUrl());
        return sql.toString();
    }

    /**
     * 处理更多维度（包含数据权限控制）
     * @param queryEntity 查询实体类对象
     * @param sql 带组装sql
     * @param sysUser 操作员对象
     * @param url 当前模块标记
     */
    private static void handleMoreDimensionByModule(ReportVo queryEntity, StringBuilder sql, LfSysuser sysUser, String url) {
        HashMap<String, String[]> map = new HashMap<String, String[]>(16);
        map.put(JumpPathEnum.areaReport.getUrl(), new String[]{"spUser"});
        map.put(JumpPathEnum.sysUserReport.getUrl(), new String[]{"spUser", "areaCode"});
        map.put(JumpPathEnum.sysDepReport.getUrl(), new String[]{"spUser", "areaCode"});
        map.put(JumpPathEnum.spMtReport.getUrl(), new String[]{"areaCode"});
        map.put(JumpPathEnum.busReport.getUrl(), new String[]{"spUser", "areaCode"});

        List<String> stringList = Arrays.asList(map.get(url));

        //业务类型
        if (StringUtils.isNotBlank(queryEntity.getBusCode())) {
            String sqlStr = "-1".equals(queryEntity.getBusCode()) ? " IS NULL" : " = '" + queryEntity.getBusCode() + "' ";
            sql.append(" AND MD.").append(TableMtDatareport.SVRTYPE).append(sqlStr);
        }
        //运营商
        if (null != queryEntity.getSpisuncm() && JumpPathEnum.areaReport.getUrl().equals(url) && queryEntity.getSpisuncm() != -1) {
            sql.append(" AND MD.").append(TableMtDatareport.SPISUNCM).append(" = ").append(queryEntity.getSpisuncm());
        }
        //SP账号
        if (StringUtils.isNotBlank(queryEntity.getSpUserId()) && stringList.contains("spUser")) {
            sql.append(" AND MD.").append(TableMtDatareport.USER_ID).append(" = '").append(queryEntity.getSpUserId()).append("'");
        }
        //区域
        if(stringList.contains("areaCode")){
            if (StringUtils.isNotEmpty(queryEntity.getCity())) {
                sql.append(" AND MD." + TableMtDatareport.MOBILEAREA + " = ").append(queryEntity.getCity());
            }else if(StringUtils.isEmpty(queryEntity.getCity()) && notEmptyProvinces(queryEntity.getProvinces())){
                sql.append(" AND MD." + TableMtDatareport.MOBILEAREA + " IN (").append("SELECT AREACODE from A_PROVINCECITY WHERE PROVINCECODE =").append(queryEntity.getProvince()).append(")");
            }
        }
        if(!JumpPathEnum.sysUserReport.getUrl().equals(url) ){
            //&& !JumpPathEnum.sysDepReport.getUrl().equals(url)
            //操作员与机构
            handleDataPermission(queryEntity, sql, sysUser);
        }
    }

    private static boolean notEmptyProvinces(String provinces) {
        return StringUtils.isNotEmpty(provinces) && !"[]".equals(provinces);
    }

    private static void handleDataPermission(ReportVo queryEntity, StringBuilder sql, LfSysuser sysUser) {
        //如果为个人权限，则只查自己发送的数据
        if (sysUser.getPermissionType() == 1) {
            sql.append(" AND MD." + TableMtDatareport.P1 + "=").append(sysUser.getUserCode());
        }
        //机构权限
        if (sysUser.getPermissionType() == 2) {
            //操作员
            if (!StringUtils.isEmpty(queryEntity.getUserIdStr()) && !"[]".equals(queryEntity.getUserIdStr())) {
                String userIdStr = queryEntity.getUserIdStr().replaceAll("mem_", "").replaceAll("[\\[\\]]", "").replaceAll("\"", "");
                sql.append(" AND MD.").append(TableMtDatareport.P1).append(" IN (").append("SELECT USER_CODE FROM LF_SYSUSER WHERE USER_ID IN (").append(userIdStr).append("))");
            }
            //机构
            if (!StringUtils.isEmpty(queryEntity.getOrgId())) {
                sql.append(" AND MD.").append(TableMtDatareport.P1).append(" IN (").append("SELECT USER_CODE FROM LF_SYSUSER WHERE DEP_ID ");
                String depId = queryEntity.getOrgId().replace("org_", "");
                //是否包含子机构
                if(queryEntity.isContainSubDep()){
                    sql.append(" IN (SELECT DEP.DEP_ID FROM LF_DEP D INNER JOIN LF_DEP DEP ON D.DEP_PATH = SUBSTR(DEP.DEP_PATH,1,LENGTH(D.DEP_PATH)) WHERE D.DEP_ID =").append(depId).append(")");
                }else {
                    sql.append(" = ").append(depId);
                }
                sql.append(")");
            } else {
                //页面未选择任何机构则查询当前操作员管辖机构
                //如果为10W号或者admin或者管辖范围为最高机构则直接忽略
                List<LfDep> lfDeps = new ReportDaoImpl().getDominationByUserId(sysUser.getUserId());
                //判断当前操作员是否管辖最高机构
                boolean flag = lfDeps.get(0).getDepLevel() == 1;
                if (!(CORPCODE_10W.equals(sysUser.getCorpCode()) || "admin".equals(sysUser.getUserName()) || flag)) {
                    //拼接机构Id
                    StringBuilder depIds = new StringBuilder();
                    for (LfDep dep : lfDeps) {
                        depIds.append(dep.getDepId()).append(",");
                    }
                    sql.append(" AND (MD.").append(TableMtDatareport.P1).append(" IN (").append("SELECT USER_CODE FROM LF_SYSUSER WHERE DEP_ID IN (").append(depIds.deleteCharAt(depIds.lastIndexOf(","))).append("))");
                    sql.append(" OR MD." + TableMtDatareport.P1 + "=").append(sysUser.getUserCode()).append(")");
                }
            }
        }
    }

    public static String getAreaRptGroupSql(Integer reportType, boolean isDetail) {
        //过滤掉为0的数据
        String havingSql = " HAVING SUM( MD.ICOUNT ) - SUM( MD.RFAIL1 ) > 0";
        String timeSql = handleTimeConditionWithDetail(reportType, isDetail);
        return " GROUP BY APCITY." + TableAprovinceCity.PROVINCE + timeSql + havingSql;
    }

    /**
     * 业务类型需要查询的列
     *
     * @return sql
     */
    public static String getBusRptFieldSql() {
        return COMMON_FIELD_SQL + "," +
                "MAX(MD." + TableMtDatareport.IYMD + ")  IYMD," +
                "MAX(MD." + TableMtDatareport.SVRTYPE + ")  SVRTYPE," +
                "MAX(BUS." + TableLfBusManager.BUS_NAME + ")  BUSNAME ";
    }

    /**
     * 需要查询的表
     *
     * @param msType:区分短彩信
     * @return sql
     */
    public static String getBusRptTableSql(Integer msType, LfSysuser sysUser) {
        String tablename;
        String meansSql;
        if (msType == SMS_TYPE) {
            tablename = " MT_DATAREPORT MD ";
            meansSql = " LEFT JOIN (SELECT DISTINCT CORP_CODE,SPUSER FROM LF_SP_DEP_BIND) LSDB ON LSDB.SPUSER=MD.USERID ";
        } else {
            tablename = " MMS_DATAREPORT MD ";
            meansSql = " LEFT JOIN (SELECT DISTINCT CORP_CODE,MMS_USER FROM LF_MMSACCBIND) LSDB ON LSDB.MMS_USER =MD.USERID ";
        }
        if (StaticValue.getCORPTYPE() == 1) {
            return " FROM " + tablename + " LEFT JOIN (SELECT BUS_CODE,BUS_NAME FROM LF_BUSMANAGER WHERE CORP_CODE IN ('0','" + sysUser.getCorpCode().trim() + "')  BUS ON MD.SVRTYPE=BUS.BUS_CODE " + meansSql;
        }
        return " FROM " + tablename + " LEFT JOIN LF_BUSMANAGER BUS ON MD.SVRTYPE=BUS.BUS_CODE ";
    }

    /**
     * 查询的条件
     *
     * @param queryEntity:页面传入的参数
     * @return sql
     */
    public static String getBusRptConditionSql(ReportVo queryEntity, LfSysuser sysUser) {
        StringBuilder sql = new StringBuilder(" WHERE 1=1");
        //多企业
        if (StaticValue.getCORPTYPE() == 1 && sysUser.getCorpCode() != null && !"".equals(sysUser.getCorpCode())) {
            sql.append(" AND LSDB.CORP_CODE='").append(sysUser.getCorpCode().trim()).append("' ");
        }

        //发送类型
        if (queryEntity.getSendtype() != null && queryEntity.getSendtype() != 0) {
            sql.append(" AND MD.SENDTYPE= ").append(queryEntity.getSendtype());
        }
        //运营商
        if (queryEntity.getSpisuncm() != null && queryEntity.getSpisuncm() != -1) {
            sql.append(" AND MD.SPISUNCM = ").append(queryEntity.getSpisuncm());
        }
        //判断如果彩信而数据源又选择了接入类 则让其查不到数据
        if (queryEntity.getMstype() != null && queryEntity.getMstype() == 1 && queryEntity.getSendtype() != null && queryEntity.getSendtype() == 2) {
            sql.append(" AND 1=2 ");
        }
        //处理更多维度
        handleMoreDimensionByModule(queryEntity, sql, sysUser, JumpPathEnum.busReport.getUrl());
        handleTimeCondition(queryEntity.getReportType(), queryEntity.getQueryTime(), sql);
        return sql.toString();
    }

    /**
     * 查询的分组条件
     *
     * @param reportType:报表类型
     * @param isDetail:是否是详情
     * @return sql
     */
    public static String getBusRptGroupSql(Integer reportType, boolean isDetail) {
        String havingSql = " HAVING SUM( MD.ICOUNT ) - SUM( MD.RFAIL1 ) > 0";
        String timeSql = handleTimeConditionWithDetail(reportType, isDetail);
        return " GROUP BY BUS." + TableLfBusManager.BUS_NAME + timeSql + havingSql;
    }

    public static String getDynParamRptFieldSql(String param, int paramNum) {
        String sql = getSubStrFuctionSqlByDbType(param.replace("aram", ""), paramNum);
        return COMMON_FIELD_SQL + "," + sql + " PA";
    }

    private static String getSubStrFuctionSqlByDbType(String param, int paramNum) {
        String sql = "";
        switch (StaticValue.DBTYPE) {
            case StaticValue.ORACLE_DBTYPE:
                if (paramNum == 2) {
                    sql = "SUBSTR(MD." + param + ",(CASE WHEN INSTR( MD." + param + ", '#' ) < 1 THEN 64 ELSE INSTR( MD." + param + ", '#' ) + 1 END ),LENGTH( MD." + param + " ))";
                } else {
                    sql = "SUBSTR(MD." + param + ",0,(CASE WHEN INSTR( MD." + param + ", '#' ) < 1 THEN 64 ELSE INSTR( MD." + param + ", '#' ) - 1 END ))";
                }
                break;
            case StaticValue.MYSQL_DBTYPE:
                if (paramNum == 2) {
                    sql = "SUBSTRING(MD." + param + ",length(substring_index(MD." + param + ",'#',1))+2)";
                } else {
                    sql = "substring_index(MD." + param + ",'#',1)";
                }
                break;
            case StaticValue.SQLSERVER_DBTYPE:
                if (paramNum == 2) {
                    sql = "SUBSTRING(MD." + param + ",(CASE WHEN CHARINDEX('#',MD." + param + ")<1 THEN 64 ELSE CHARINDEX('#',MD." + param + ")+1 END),64) ";
                } else {
                    sql = "SUBSTRING(MD." + param + ",0,(CASE WHEN CHARINDEX('#',MD." + param + ")<1 THEN 64 ELSE CHARINDEX('#',MD." + param + ") END)) ";
                }
                break;
            case StaticValue.DB2_DBTYPE:
                if (paramNum == 2) {
                    sql = "SUBSTR(MD." + param + ",(CASE WHEN LOCATE(MD." + param + ",'#')<1 THEN 64 ELSE LOCATE(MD." + param + ",'#')+1 END),LENGTH(MD." + param + "))";
                } else {
                    sql = "SUBSTR(MD." + param + ",1,(CASE WHEN LOCATE(MD." + param + ",'#')<1 THEN 64 ELSE LOCATE(MD." + param + ",'#')-1 END))";
                }
                break;
            default:
                //do nothing
        }
        return sql;
    }

    public static String getDynParamRptTableSql() {
        return " FROM " + TableMtDatareport.TABLE_NAME + " MD";
    }

    public static String getDynParamConditionSql(ReportVo queryEntity, LfSysuser sysuser, StringBuilder sql) {
        List<String> list = JSONObject.parseArray(queryEntity.getQueryTime(), String.class);
        sql.append(" WHERE MD." + TableMtDatareport.IYMD + " >= ").append(list.get(0).replaceAll("-", "")).
                append(" AND MD.").append(TableMtDatareport.IYMD).append(" <= ").append(list.get(1).replaceAll("-", ""));
        handleDataPermission(queryEntity, sql, sysuser);
        return sql.toString();
    }

    public static String getDynParamGroupSql(String param, int paramNum) {
        String sql = getSubStrFuctionSqlByDbType(param.replace("aram", ""), paramNum);
        return " GROUP BY " + sql;
    }

    /**
     * 运营商统计报表需要查询的列
     *
     * @return sql
     */
    public static String getSpisuncmMtRptFieldSql() {
        return COMMON_FIELD_SQL + "," +
                "MAX(MD." + TableMtDatareport.IYMD + ")  IYMD," +
                "MD." + TableMtDatareport.SPID + "  SPID," +
                "MD." + TableMtDatareport.SPISUNCM + "  SPISUNCM ";
    }

    /**
     * 运营商统计报表需要查询的表
     *
     * @param msType 短彩信标记
     * @return sql
     */
    public static String getSpisuncmRptTableSql(Integer msType) {
        String talbeName = msType == SMS_TYPE ? TableMtDatareport.TABLE_NAME : TableMmsDatareport.TABLE_NAME;
        return " FROM " + talbeName + " MD ";
    }

    /**
     * 查询的条件
     *
     * @param queryEntity:页面传入的参数
     * @return sql
     */
    public static String getSpisuncmRptConditionSql(ReportVo queryEntity) {
        StringBuilder sql = new StringBuilder(" WHERE 1=1");
        //运营商
        if (queryEntity.getSpisuncm() != null) {
            sql.append(" AND MD.SPISUNCM = ").append(queryEntity.getSpisuncm());
        }
        //运营商ID
        if (!"-1".equals(queryEntity.getSpId())) {
            sql.append(" AND MD." + TableMtDatareport.SPID + " = '").append(queryEntity.getSpId()).append("'");
        }
        handleTimeCondition(queryEntity.getReportType(), queryEntity.getQueryTime(), sql);
        return sql.toString();
    }

    /**
     * 获取运营商报表的分组条件
     *
     * @param reportType 报表类型
     * @param isDetail   是否为详情
     * @return sql
     */
    public static String getSpisuncmRptGroupSql(Integer reportType, boolean isDetail) {
        String timeSql = handleTimeConditionWithDetail(reportType, isDetail);
        return " GROUP BY MD." + TableMtDatareport.SPID + ",MD." + TableMtDatareport.SPISUNCM + timeSql;
    }

    /**
     * SP账号统计报表需要查询的列
     *
     * @return sql
     */
    public static String getSpMtRptFieldSql() {
        return COMMON_FIELD_SQL + "," +
                "MAX(MD." + TableMtDatareport.IYMD + ")  IYMD," +
                "max(MD.USERID) USERID," +
                "MAX(u.STAFFNAME) STAFFNAME," +
                "MAX(u.SPTYPE) SPTYPE," +
                "MAX(MD.SENDTYPE) SENDTYPE," +
                "MAX(MD.SPISUNCM) SPISUNCM ";
    }

    /**
     * SP账号统计报表需要查询的表
     *
     * @param msType 短彩信标记
     * @return sql
     */
    public static String getSpMTRptTableSql(Integer msType) {
        String talbeName = msType == SMS_TYPE ? TableMtDatareport.TABLE_NAME : TableMmsDatareport.TABLE_NAME;
        //由于在别的地方都是用0和1作为短彩信的标识，在USERDATA表中的ACCOUNTTYPE使用1和2作为短彩信的标识所以就+1
        int accType=msType+1;
        return " FROM " + talbeName + " MD " +
                "LEFT JOIN " +
                "(SELECT STAFFNAME, SPTYPE, ACCOUNTTYPE, USERID FROM USERDATA) U " +
                "ON MD.USERID = U.USERID AND U.ACCOUNTTYPE = " + accType;
    }

    /**
     * sp账号统计报表查询的条件
     *
     * @param queryEntity:页面传入的参数实体类对象
     * @return sql
     */
    public static String getSpMTRptConditionSql(ReportVo queryEntity, LfSysuser sysUser) {
        StringBuilder sql = new StringBuilder(" WHERE 1=1 ");

        //如果是标准版
        if (StaticValue.getCORPTYPE() == 0) {
            //如果是非admin账号则需要进行判断
            if (!"2".equals(sysUser.getUserId().toString())) {
                //如果没有匹配的,则查询的SP账号没有权限
                if ("".equals(queryEntity.getCurrentSpUser())){
                    sql.append(" AND 1 = 2 ");
                }else{
                    //权限
                    sql.append(" AND MD.USERID IN  (").append(queryEntity.getCurrentSpUser()).append(") ");
                }
            }
            //如果是admin并且有条件,就要根据条件进行查询
            else if("2".equals(sysUser.getUserId().toString()) && !"".equals(queryEntity.getSpUserId())){
                sql.append(" AND MD.USERID = ").append("'").append(queryEntity.getSpUserId()).append("'");
            }
        }
        //如果是托管版
        else if (StaticValue.getCORPTYPE() == 1) {
            if (!"3".equals(sysUser.getUserId().toString())) {
                //权限
                sql.append(" AND MD.USERID IN (").append(queryEntity.getCurrentSpUser()).append(") ");
            }
        }
        if (!StringUtils.isEmpty(queryEntity.getStaffname())) {
            sql.append(" AND u.STAFFNAME like").append("'%").append(queryEntity.getStaffname().trim()).append("%'");

        }
        //sp账号类型
        if (queryEntity.getSptype() != 0) {
            sql.append(" AND u.SPTYPE = ").append(queryEntity.getSptype());
        }
        //发送类型
        if (queryEntity.getSendtype() != null && queryEntity.getSendtype() != 0) {
            sql.append(" AND MD.SENDTYPE = ").append(queryEntity.getSendtype());
        }
        //判断如果彩信而数据源又选择了接入类 则让其查不到数据
        if (queryEntity.getMstype() != null && queryEntity.getMstype() == 1 && queryEntity.getSendtype() != null && queryEntity.getSendtype() == 2) {
            sql.append(" AND 1=2 ");
        }
        //运营商
        if (queryEntity.getSpisuncm() != null && queryEntity.getSpisuncm() != -1) {
            sql.append(" AND MD.SPISUNCM = ").append(queryEntity.getSpisuncm());
        }
        //处理更多维度
        handleMoreDimensionByModule(queryEntity, sql, sysUser, JumpPathEnum.spMtReport.getUrl());
        handleTimeCondition(queryEntity.getReportType(), queryEntity.getQueryTime(), sql);
        return sql.toString();
    }

    /**
     * 获取sp账号统计报表的分组条件
     *
     * @param reportType 报表类型
     * @param isDetail   是否详情
     * @return sql
     */
    public static String getSpMTRptGroupSql(Integer reportType, boolean isDetail) {
        String timeSql = handleTimeConditionWithDetail(reportType, isDetail);
        return " GROUP BY MD." + TableMtDatareport.USER_ID + ",MD.SENDTYPE" + timeSql;
    }

    public static String getSpMTRptOrderSql() {
        return " ORDER BY MD.USERID, MD.SENDTYPE DESC ";
    }

    public static String getSysUserReportFieldSql() {
        return COMMON_FIELD_SQL + ",MAX(SYSUSER." + TableLfSysuser.USER_STATE + ") USERSTATE," +
                "MAX(SYSUSER." + TableLfSysuser.NAME + ") NAME," +
                "MAX(MD." + TableMtDatareport.IYMD + ")  IYMD," +
                "MAX(DEP." + TableLfDep.DEP_NAME + ") DEPNAME";
    }

    public static String getSysUserReportTableSql(Integer msType) {
        String talbeName = msType == SMS_TYPE ? TableMtDatareport.TABLE_NAME : TableMmsDatareport.TABLE_NAME;
        String sql;
        //获取是否开启p2参数配置
        if (!isOpenP2()) {
            sql = "LEFT JOIN " + TableLfSysuser.TABLE_NAME + " SYSUSER ON SYSUSER." + TableLfSysuser.USER_CODE + " = MD." + TableMtDatareport.P1 + " LEFT JOIN " + TableLfDep.TABLE_NAME + " DEP ON SYSUSER." + TableLfSysuser.DEP_ID + "= DEP." + TableLfDep.DEP_ID;
        } else {
            sql = "LEFT JOIN " + TableLfDep.TABLE_NAME + " DEP ON dep." + TableLfDep.DEP_CODE_THIRD + " = MD." + TableMtDatareport.P2 + " LEFT JOIN " + TableLfSysuser.TABLE_NAME + " SYSUSER ON SYSUSER." + TableLfSysuser.USER_CODE + " = MD." + TableMtDatareport.P1;
        }
        return " FROM " + talbeName + " MD " + sql;
    }

    public static String getSysUserReportConditionSql(ReportVo queryEntity, LfSysuser sysUser) {
        StringBuilder sql = new StringBuilder(" WHERE 1=1");
        //如果为个人权限，则只查自己发送的数据
        if (sysUser.getPermissionType() == 1) {
            sql.append(" AND MD." + TableMtDatareport.P1 + "=").append(sysUser.getUserCode());
        }
        //运营商
        if (queryEntity.getSpisuncm() != null && queryEntity.getSpisuncm() > -1) {
            sql.append(" AND MD.SPISUNCM = ").append(queryEntity.getSpisuncm());
        }
        //操作员名字
        if (queryEntity.isDetail() && StringUtils.isNotBlank(queryEntity.getName())) {
            if(null == queryEntity.getUserState()){
                sql.append(" AND SYSUSER.").append(TableLfSysuser.NAME).append(" = '' OR SYSUSER.").append(TableLfSysuser.NAME).append(" IS NULL");
            }else {
                sql.append(" AND SYSUSER.").append(TableLfSysuser.NAME).append(" = '").append(queryEntity.getName()).append("'");
            }
        }
        //时间
        handleTimeCondition(queryEntity.getReportType(), queryEntity.getQueryTime(), sql);
        //如果为多企业加上企业编码查询
        if (StaticValue.getCORPTYPE() == 1 && !CORPCODE_10W.equals(sysUser.getCorpCode())) {
            sql.append(" AND SYSUSER.").append(TableLfSysuser.CORP_CODE).append(" = '").append(sysUser.getCorpCode()).append("'");
        }
        //机构权限
        if (sysUser.getPermissionType() == 2) {
            //操作员
            if (!StringUtils.isEmpty(queryEntity.getUserIdStr()) && !"[]".equals(queryEntity.getUserIdStr())) {
                String userIdStr = queryEntity.getUserIdStr().replaceAll("mem_", "").replaceAll("[\\[\\]]", "").replaceAll("\"", "");
                sql.append(" AND SYSUSER.").append(TableLfSysuser.USER_ID).append(" IN (").append(userIdStr).append(")");
            }
            //机构
            if (!StringUtils.isEmpty(queryEntity.getOrgId())) {
                //默认包含子机构
                sql.append(" AND DEP.").append(TableLfDep.DEP_ID).append(" IN (").append("SELECT DEP.DEP_ID FROM LF_DEP D INNER JOIN LF_DEP DEP ON D.DEP_PATH = SUBSTR(DEP.DEP_PATH,1,LENGTH(D.DEP_PATH)) WHERE D.DEP_ID =").append(queryEntity.getOrgId().replace("org_", "")).append(")");
            } else {
                //页面未选择任何机构则查询当前操作员管辖机构
                //如果为10W号或者admin或者管辖范围为最高机构则直接忽略
                List<LfDep> lfDeps = new ReportDaoImpl().getDominationByUserId(sysUser.getUserId());
                //判断当前操作员是否管辖最高机构
                boolean flag = lfDeps.get(0).getDepLevel() == 1;
                if (!(CORPCODE_10W.equals(sysUser.getCorpCode()) || "admin".equals(sysUser.getUserName()) || flag)) {
                    //拼接机构Id
                    StringBuilder depIds = new StringBuilder();
                    for (LfDep dep : lfDeps) {
                        depIds.append(dep.getDepId()).append(",");
                    }
                    sql.append(" AND DEP.").append(TableLfDep.DEP_ID).append(" IN (").append(depIds.deleteCharAt(depIds.lastIndexOf(","))).append(")");
                }
            }
        }
        handleMoreDimensionByModule(queryEntity, sql, sysUser, JumpPathEnum.sysUserReport.getUrl());
        return sql.toString();
    }

    private static void handleTimeCondition(Integer reportType, String queryTime, StringBuilder sql) {
        switch (reportType) {
            case MONTH_REPORT:
                sql.append(" AND MD." + TableMtDatareport.Y + " = ").append(queryTime, 0, 4).append(" AND MD.").append(TableMtDatareport.IMONTH).append(" = ").append(Integer.parseInt(queryTime.substring(5)));
                break;
            case YEAR_REPORT:
                sql.append(" AND MD." + TableMtDatareport.Y + " = ").append(queryTime);
                break;
            case DAY_REPORT:
                List<String> list = JSONObject.parseArray(queryTime, String.class);
                sql.append(" AND MD." + TableMtDatareport.IYMD + " >= ").append(list.get(0).replaceAll("-", "")).append(" AND MD.").append(TableMtDatareport.IYMD).append(" <= ").append(list.get(1).replaceAll("-", ""));
                break;
            default:
                // Do nothing
        }
    }

    private static String handleTimeConditionWithDetail(Integer reportType, boolean isDetail) {
        String timeSql;
        switch (reportType) {
            case MONTH_REPORT:
                timeSql = isDetail ? ",MD." + TableMtDatareport.IYMD : ",MD." + TableMtDatareport.IMONTH;
                break;
            case YEAR_REPORT:
                timeSql = isDetail ? ",MD." + TableMtDatareport.IMONTH : ",MD." + TableMtDatareport.Y;
                break;
            case DAY_REPORT:
                timeSql = isDetail ? ",MD." + TableMtDatareport.IYMD : "";
                break;
            default:
                timeSql = "";
        }
        return timeSql;
    }

    public static String getSysUserReportGroupSql(Integer reportType, boolean detail) {
        String havingSql = " HAVING SUM( MD.ICOUNT ) - SUM( MD.RFAIL1 ) > 0";
        String withDetail = handleTimeConditionWithDetail(reportType, detail);
        return " GROUP BY SYSUSER." + TableLfSysuser.USER_ID + withDetail + havingSql;
    }


    private static String getSysDeptReportFieldSql() {
        return COMMON_FIELD_SQL;
    }

    private static String getSysDeptReportTableSql1(Integer msgType) {
        String tableName = msgType == SMS_TYPE ? TableMtDatareport.TABLE_NAME : TableMmsDatareport.TABLE_NAME;
        StringBuilder sql = new StringBuilder();
        //获取是否开启p2参数配置
        if (isOpenP2()) {
            sql.append(" LEFT JOIN LF_DEP dep ON dep.DEP_CODE_THIRD=MD.p2 ");
        } else {
            sql.append(" LEFT JOIN LF_SYSUSER su ON su.USER_CODE=MD.P1 ");
            sql.append(" LEFT JOIN LF_DEP dep ON dep.DEP_ID=su.DEP_ID ");
        }
        sql.append(" INNER JOIN LF_DEP d ON d.DEP_PATH=SUBSTR(dep.DEP_PATH,1,LENGTH(d.DEP_PATH)) ");
        return " FROM " + tableName + " MD " + sql.toString();
    }

    private static String getSysDeptReportTableSql2(Integer msgType) {
        String tableName = msgType == SMS_TYPE ? TableMtDatareport.TABLE_NAME : TableMmsDatareport.TABLE_NAME;
        StringBuilder sql = new StringBuilder();
        if (isOpenP2()) {
            sql.append(" LEFT JOIN LF_DEP dep ON dep.DEP_CODE_THIRD = MD.P2 ");
        }
        sql.append(" LEFT JOIN LF_SYSUSER su ON su.USER_CODE=MD.P1 ");
        return " FROM " + tableName + " MD " + sql.toString();
    }

    public static String getSysDeptRptSql1(ReportVo queryEntity, LfSysuser sysuser) {
        if (null != queryEntity) {
            return getSysDeptReportFieldSql() +
                    ",(SELECT ld.DEP_ID FROM LF_DEP ld WHERE ld.dep_path=SUBSTR(DEP.DEP_PATH,1,LENGTH(D.DEP_PATH))) depId" +
                    ",'1' IDTYPE,NULL userState" +
                    ",(SELECT ld.DEP_NAME FROM LF_DEP ld WHERE ld.dep_path=SUBSTR(DEP.DEP_PATH,1,LENGTH(D.DEP_PATH))) depName" +
                    ",max(MD.IYMD) IYMD" +
                    getSysDeptReportTableSql1(queryEntity.getMstype()) +
                    getSysDeptReportConditionSql1(queryEntity, sysuser);
        }
        return null;
    }

    public static String getSysDeptRptSql2(ReportVo queryEntity, LfSysuser sysuser) {
        if (null != queryEntity) {
            return getSysDeptReportFieldSql() + ",su.USER_ID depId,'2' IDTYPE,max(su.USER_STATE) userState,su.USER_NAME depName,max(MD.IYMD) IYMD " +
                    getSysDeptReportTableSql2(queryEntity.getMstype()) +
                    getSysDeptReportConditionSql2(queryEntity, sysuser);
        }
        return null;
    }

    public static String getSysDeptRptSql3(ReportVo queryEntity, LfSysuser sysuser) {
        if (null != queryEntity) {
            StringBuilder sb = new StringBuilder();
            final Integer msgType = queryEntity.getMstype();
            String tableName = msgType == SMS_TYPE ? TableMtDatareport.TABLE_NAME : TableMmsDatareport.TABLE_NAME;
            sb.append(getSysDeptReportFieldSql()).append(",0 depId,'3' IDTYPE,-1 userState,'未知机构' depName,max(MD.IYMD) IYMD ");
            sb.append(" FROM ").append(tableName).append(" MD ");
            if (isOpenP2()) {
                sb.append(" INNER JOIN LF_DEP dep ON dep.DEP_CODE_THIRD=MD.P2 ");
            } else {
                sb.append(" LEFT JOIN LF_SYSUSER su ON su.USER_CODE=MD.p1 ");
            }
            sb.append(getSysDeptReportConditionSql3(queryEntity, sysuser));
            return sb.toString();
        }
        return "";
    }

    private static String getSysDeptReportConditionSql1(ReportVo queryEntity, LfSysuser sysUser) {
        StringBuilder sb = new StringBuilder(" WHERE 1=1 ");
        //如果为多企业加上企业编码查询
        if (StaticValue.getCORPTYPE() == 1 && !CORPCODE_10W.equals(sysUser.getCorpCode())) {
            sb.append(" AND D.CORP=").append(sysUser.getCorpCode());
        }
        final String orgId = !StringUtils.isEmpty(queryEntity.getOrgId()) ? queryEntity.getOrgId().replaceAll("org_", "") : "";
        final String depId = queryEntity.isDetail() ? queryEntity.getDepId().toString() : orgId;
        if(queryEntity.isDetail()) {
            sb.append(" AND D.DEP_ID=").append(depId);
        } else {
           sb.append(" AND D.SUPERIOR_ID=").append(depId);
           sb.append(" AND DEP.DEP_ID<>").append(depId);
        }
//        if(!StringUtils.isEmpty(depId)){
//            sb.append(" AND D.SUPERIOR_ID=").append(depId);
//            sb.append(" AND DEP.DEP_ID<>").append(depId);
//        }
        sb.append(" AND D.DEP_STATE=1 AND DEP.DEP_STATE=1 ");
        String condition = getSysDeptRptCommonCondition(queryEntity, sysUser);
        sb.append(condition);
        //处理更多维度
        handleMoreDimensionByModule(queryEntity, sb, sysUser, JumpPathEnum.sysDepReport.getUrl());
        String timeSql = handleTimeConditionWithDetail(queryEntity.getReportType(), queryEntity.isDetail());
        sb.append(" GROUP BY SUBSTR(DEP.DEP_PATH,1,LENGTH(D.DEP_PATH)) ").append(timeSql);
        return sb.toString();
    }

    private static String getSysDeptReportConditionSql2(ReportVo queryEntity, LfSysuser sysuser) {
        StringBuilder sb = new StringBuilder(" WHERE 1=1 ");
        if (StaticValue.getCORPTYPE() == 1 && !CORPCODE_10W.equals(sysuser.getCorpCode())) {
            sb.append(" AND su.CORP_CODE=").append(sysuser.getCorpCode());
        }
        final String orgId = !StringUtils.isEmpty(queryEntity.getOrgId()) ? queryEntity.getOrgId().replaceAll("org_", "") : "";
        if(!StringUtils.isEmpty(orgId) && !queryEntity.isDetail()){
            sb.append(" AND su.USER_ID <>1 AND su.DEP_ID=").append(orgId);
        }
        if (queryEntity.isDetail()) {
            if (queryEntity.getIdtype() == 2) {
                sb.append(" AND su.USER_ID=").append(queryEntity.getDepId());
            } else if (queryEntity.getIdtype() == 1) {
                sb.append(" AND su.USER_ID <>1 AND su.DEP_ID=").append(queryEntity.getDepId());
            }
        }
        String condition = getSysDeptRptCommonCondition(queryEntity, sysuser);
        sb.append(condition);
        //处理时间
        String timeSql = handleTimeConditionWithDetail(queryEntity.getReportType(), queryEntity.isDetail());
        //处理更多维度
        handleMoreDimensionByModule(queryEntity, sb, sysuser, JumpPathEnum.sysDepReport.getUrl());
        sb.append(" GROUP BY su.USER_ID,su.USER_NAME ").append(timeSql);
        return sb.toString();
    }

    private static String getSysDeptReportConditionSql3(ReportVo queryEntity, LfSysuser sysuser) {
        StringBuilder sb = new StringBuilder(" WHERE 1=1 ");
        if (null != queryEntity) {
            if (isOpenP2()) {
                sb.append(" AND COALESCE(dep.DEP_CODE_THIRD,'0')='0' ");
            } else {
                sb.append(" AND COALESCE(su.user_id,0)=0 ");
            }
            sb.append(getSysDeptRptCommonCondition(queryEntity, sysuser));
            //处理更多维度
            handleMoreDimensionByModule(queryEntity, sb, sysuser, JumpPathEnum.sysDepReport.getUrl());
            String timeSql = handleTimeConditionWithDetail(queryEntity.getReportType(), queryEntity.isDetail());
            sb.append(" GROUP BY ").append(isOpenP2() ? " dep.dep_id " : " su.USER_ID ").append(timeSql);
            return sb.toString();
        }
        return null;
    }

    private static String getSysDeptRptCommonCondition(ReportVo queryEntity, LfSysuser sysuser) {
        StringBuilder sb = new StringBuilder();
        if (null != queryEntity) {
            final Integer spisumcm = queryEntity.getSpisuncm();
            if (null != spisumcm && spisumcm > -1) {
                sb.append(" AND MD.SPISUNCM=").append(spisumcm);
            }
            if (null != queryEntity.getSendtype() && queryEntity.getSendtype() != 0) {
                sb.append(" AND MD.SENDTYPE=").append(queryEntity.getSendtype());
            }
            handleTimeCondition(queryEntity.getReportType(), queryEntity.getQueryTime(), sb);
            if (null != sysuser) {
                // 1. 个人权限 2. 机构权限 个人权限不能查询出数据
                if (null != sysuser.getPermissionType() && sysuser.getPermissionType() == 1) {
                    sb.append(" AND 1=2 ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    private static boolean isOpenP2() {
        String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2", "false");
        return depcodethird2p2 != null && depcodethird2p2.trim().length() != 0 && !"false".equals(depcodethird2p2.trim());
    }

    public static boolean isTopLevelDept(Integer corpCode, Long deptId) {
        int depId;
        try {
            depId = new SuperDAO().getInt("dep_Id",
                    "select dep_id from  lf_dep where corp_code='" + corpCode + "' and dep_level=1", StaticValue.EMP_POOLNAME);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取该企业顶级机构发生错误");
            return false;
        }
        return deptId.intValue() == depId;
    }

}
