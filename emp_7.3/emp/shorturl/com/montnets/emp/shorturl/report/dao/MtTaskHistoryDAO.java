package com.montnets.emp.shorturl.report.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.shorturl.surlmanage.table.TableLfUrlTask;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sms.TableMtTask;
import com.montnets.emp.table.sms.TableMtTask01_12;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

public class MtTaskHistoryDAO extends SuperDAO{
    /**
     * 查询日期格式化：yyyy-MM-dd HH:mm:ss
     */
    private SimpleDateFormat sdfSeachTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 查询日期格式化：yyyy
     */
    private SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");

    /**
     * 查询日期格式化：MM
     */
    private SimpleDateFormat sdf_MM = new SimpleDateFormat("MM");
    /**
     * 下行历史记录查询
     * @param conditionMap 查询条件集合
     * @param pageInfo 分页信息对象
     * @return 返回下行历史记录动态bean集合
     */
    public List<DynaBean> findMtTasksHis(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
        try {
            //获取查询的列名
            String fieldSql = "select mttask." + TableMtTask01_12.ID + ",mttask."+TableMtTask01_12.USER_ID +
                            ",mttask." + TableMtTask01_12.SPGATE + ",mttask." + TableMtTask01_12.CPNO +
                            ",mttask."+TableMtTask01_12.PHONE + ",mttask."+TableMtTask01_12.SEND_STATUS +
                            ",mttask." + TableMtTask01_12.TASK_ID + ",mttask." + TableMtTask01_12.ERRO_RCODE +
                            ",mttask." + TableMtTask01_12.SEND_TIME + ",mttask."+ TableMtTask01_12.MESSAGE +
                            ",mttask." + TableMtTask01_12.UNICOM + ",mttask." + TableMtTask01_12.PK_NUMBER +
                            ",mttask." + TableMtTask01_12.PK_TOTAL + ",mttask." + TableMtTask01_12.SVRTYPE +
                            ",mttask." + TableMtTask01_12.RECV_TIME + ",mttask.P1,mttask.P2,mttask.USERMSGID,"+
//                            "mttask.CUSTID,lfmttask.CORP_CODE,urltask."+ TableLfUrlTask.NETURL+",urltask."+
                            "mttask.CUSTID,mttask.ECID,urltask."+ TableLfUrlTask.NETURL+",urltask."+ //lfmttask.CORP_CODE
                            TableLfUrlTask.DOMAIN_URL;
            //获取查询表名的sql语句
            String tableSql = getMttaskHisTableSql(conditionMap);

            if(tableSql == null || tableSql.length() == 0) {
                return null;
            }
            //组装过滤条件语句
            String conditionSql = MtRecordDAOSql.getConditionSql(conditionMap, "mttask");
            //时间段的过滤条件
            List<String> timeList = MtRecordDAOSql.getTimeCondition(conditionMap);
            //组sql语句
            String sql = fieldSql + tableSql + conditionSql;

//            String countSql = "select count(*) totalcount" + " from (" + sql + ") totalcount";
            String countSql = "select count(*) as totalcount" + tableSql + conditionSql;
//            sql += " order by mttask.SENDTIME DESC";
            sql += " order by urltask.taskid DESC,mttask.id DESC";
            List<DynaBean> returnList;

            //记录查询语句
            EmpExecutionContext.info("企业短链-发送明细详情，查询sql："+sql);

            IGenericDAO dao = new DataAccessDriver().getGenericDAO();
            returnList = dao.findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
            //返回LIST
            return returnList;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "DAO查询，下行历史记录，异常。");
            return null;
        }
    }
    /**
     * 获取表名sql
     * @param conditionMap 查询条件
     * @return 表名sql
     */
    private String getMttaskHisTableSql(LinkedHashMap<String, String> conditionMap) {
        try {
            //开始时间
            Calendar caStartTime = Calendar.getInstance();
            caStartTime.setTime(sdfSeachTime.parse(conditionMap.get("sendtime")));

            //结束时间
            Calendar caEndTime = Calendar.getInstance();
            caEndTime.setTime(sdfSeachTime.parse(conditionMap.get("recvtime")));

            String newTableName = "MTTASK" + sdf_yyyy.format(caStartTime.getTime())+ sdf_MM.format(caStartTime.getTime());
            String secondTableName = "MTTASK" + sdf_yyyy.format(caEndTime.getTime())+ sdf_MM.format(caEndTime.getTime());

            if(getMtHisTableExists(newTableName) <= 0 && getMtHisTableExists(secondTableName) <= 0){
                //两个表都不存在
                EmpExecutionContext.error("企业短链报表查询，发送明细详情查询，DAO查询下行历史记录，获取表名sql，两个表都不存在。"
                        + "tableName=" + newTableName
                        + ",secondTableName=" + secondTableName);
                return null;
            }

            //如果是同一个月，则查单表
            if(caStartTime.get(Calendar.MONTH) == caEndTime.get(Calendar.MONTH)) {
                newTableName = getMtHisTableExists(newTableName) <= 0 ? secondTableName : newTableName;
//                return " from " + newTableName + " mttask " + StaticValue.WITHNOLOCK + "left join " + TableLfMttask.TABLE_NAME + " lfmttask "+ StaticValue.WITHNOLOCK + " ON mttask." +
//                        TableMtTask01_12.TASK_ID + " = lfmttask." + TableMtTask.TASK_ID + " and lfmttask." + TableLfMttask.MS_TYPE + "=31" + " left join " + TableLfUrlTask.TABLE_NAME +
//                        " urltask " + StaticValue.WITHNOLOCK + " on urltask." + TableLfUrlTask.TASKID + " = mttask." + TableMtTask01_12.TASK_ID;
                return " from " + TableLfUrlTask.TABLE_NAME +
                " urltask " + StaticValue.getWITHNOLOCK() + " inner join " + newTableName + " mttask " + StaticValue.getWITHNOLOCK() +
                " on urltask." + TableLfUrlTask.TASKID + " = mttask." + TableMtTask01_12.TASK_ID;
            }

            //跨月查询，则需要查询两张表的记录，只允许查两个月以内的数据
            if(getMtHisTableExists(newTableName) <= 0){
                //上一个月表不存在
                EmpExecutionContext.error("企业短链报表查询，发送明细详情查询,DAO查询下行历史记录，获取表名sql，表不存在。" + "tableName=" +  newTableName);
//                return " from " + secondTableName + " mttask " + StaticValue.WITHNOLOCK + "left join " + TableLfMttask.TABLE_NAME + " lfmttask "+ StaticValue.WITHNOLOCK + " ON mttask." +
//                        TableMtTask01_12.TASK_ID + " = lfmttask." + TableMtTask.TASK_ID + " and lfmttask." + TableLfMttask.MS_TYPE + "=31" + " left join " + TableLfUrlTask.TABLE_NAME +
//                        " urltask " + StaticValue.WITHNOLOCK + " on urltask." + TableLfUrlTask.TASKID + " = mttask." + TableMtTask01_12.TASK_ID;
                return " from " + TableLfUrlTask.TABLE_NAME +
                " urltask " + StaticValue.getWITHNOLOCK() + " inner join " + secondTableName + " mttask " + StaticValue.getWITHNOLOCK() +
                " on urltask." + TableLfUrlTask.TASKID + " = mttask." + TableMtTask01_12.TASK_ID;
            }

            if(getMtHisTableExists(secondTableName) <= 0){
                //下一个月表不存在
                EmpExecutionContext.error("企业短链报表查询，发送明细详情查询,DAO查询下行历史记录，获取表名sql，表不存在。" + "tableName=" +  secondTableName);
//                return " from " + newTableName + " mttask " + StaticValue.WITHNOLOCK + "left join " + TableLfMttask.TABLE_NAME + " lfmttask "+ StaticValue.WITHNOLOCK + " ON mttask." +
//                        TableMtTask01_12.TASK_ID + " = lfmttask." + TableMtTask.TASK_ID + " and lfmttask." + TableLfMttask.MS_TYPE + "=31" + " left join " + TableLfUrlTask.TABLE_NAME +
//                        " urltask " + StaticValue.WITHNOLOCK + " on urltask." + TableLfUrlTask.TASKID + " = mttask." + TableMtTask01_12.TASK_ID;
                return " from " + TableLfUrlTask.TABLE_NAME +
                " urltask " + StaticValue.getWITHNOLOCK() + " inner join " + newTableName + " mttask " + StaticValue.getWITHNOLOCK() +
                " on urltask." + TableLfUrlTask.TASKID + " = mttask." + TableMtTask01_12.TASK_ID;
            }
            conditionMap.put("singleTable", "false");
//          newTableName = assembleSql(newTableName, conditionMap);
//          secondTableName = assembleSql(secondTableName, conditionMap);

            //两个表都存在，则用联合查询
            String fieldSql = "select ID,userid,spgate,cpno,phone,sendstatus,taskid,errorcode,sendtime,message,unicom,pknumber,pktotal,svrtype,recvtime,p1,p2,usermsgid,custid";
            return " from (" + fieldSql + " from " + newTableName + StaticValue.getWITHNOLOCK() +
                    " union all " + fieldSql + " from " + secondTableName + StaticValue.getWITHNOLOCK() +
                    ") " + "mttask left join " + TableLfMttask.TABLE_NAME + " lfmttask " + StaticValue.getWITHNOLOCK() +" ON mttask." +
//                    TableMtTask01_12.TASK_ID + " = lfmttask." + TableMtTask.TASK_ID + " and lfmttask." + TableLfMttask.MS_TYPE + "=31" + " left join " + TableLfUrlTask.TABLE_NAME +
                    TableMtTask01_12.TASK_ID + " = lfmttask." + TableMtTask.TASK_ID + " left join " + TableLfUrlTask.TABLE_NAME +
                    " urltask " + StaticValue.getWITHNOLOCK() + " on urltask." + TableLfUrlTask.TASKID + " = mttask." + TableMtTask01_12.TASK_ID;

        } catch (Exception e) {
            EmpExecutionContext.error(e, "DAO查询下行历史记录，获取表名sql，异常。");
            return null;
        }
    }

    /**
     * 获取下行历史记录表名是否存在。
     * @param tableName 下行历史记录表名
     * @return 0：不存在；1：存在；小于0：异常情况。
     */
    private int getMtHisTableExists(String tableName) {
        try {
            //查询sql
            String sql;
            switch (StaticValue.DBTYPE) {
                case 1:
                    // oracle
                    sql = "select count(*) ICOUNT from all_tables where table_name=upper('" + tableName + "') and OWNER=upper('" + SystemGlobals.getValue("montnets.emp.user") + "')";
                    break;
                case 2:
                    // sqlserver2005
                    sql = "select count(*) ICOUNT from sysobjects where id = object_id('" + tableName + "') and type = 'u'";
                    break;
                case 3:
                    // MYSQL
                    sql = "select count(*) ICOUNT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='" + SystemGlobals.getValue("montnets.emp.databaseName") + "' and TABLE_NAME=upper('" + tableName + "')";
                    break;
                case 4:
                    // DB2
                    sql = "select count(*) ICOUNT from syscat.tables where tabname=upper('" + tableName + "')";
                    break;
                default:
                    EmpExecutionContext.error("获取下行历史记录表名是否存在，不支持的数据库类型。dbType=" + StaticValue.DBTYPE);
                    return -1;
            }

            //查询新表是否存在
            List<DynaBean> resultList = getListDynaBeanBySql(sql);
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
