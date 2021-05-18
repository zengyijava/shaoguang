package com.montnets.emp.shorturl.report.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.shorturl.surlmanage.table.TableLfUrlTask;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sms.TableMtTask01_12;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import java.util.LinkedHashMap;
import java.util.List;

public class MtRecordRealDAO extends SuperDAO{

    public List<DynaBean> findMtTasksReal(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
        try {
            //获取表名
            String tableName = conditionMap.get("realTableName");
            //没找到则用使用gw_mt_task_bak表
            if(tableName == null || tableName.trim().length() < 1 || (!"gw_mt_task_bak".equals(tableName) && !tableName.contains("mttask"))) {
                tableName = "gw_mt_task_bak";
            }
            //查询表名
            //需要与LF_MTTASK联合查询筛选出短链的下行记录
//            String fieldSql = "select gwmttask.ID,gwmttask.USERID,gwmttask.SPGATE,gwmttask.CPNO,gwmttask.PHONE," +
//                    "gwmttask.TASKID,gwmttask.SENDSTATUS,gwmttask.ERRORCODE,gwmttask.SENDTIME,gwmttask.MESSAGE," +
//                    "gwmttask.UNICOM,gwmttask.RECVTIME,gwmttask.SVRTYPE,gwmttask.PKNUMBER,gwmttask.PKTOTAL,gwmttask.MSGFMT," +
//                    "gwmttask.P1,gwmttask.P2,gwmttask.usermsgid,gwmttask.CUSTID,lfmttask.CORP_CODE,urltask." + TableLfUrlTask.NETURL +
//                    ",urltask." + TableLfUrlTask.DOMAIN_URL;
            String fieldSql = "select gwmttask.ID,gwmttask.USERID,gwmttask.SPGATE,gwmttask.CPNO,gwmttask.PHONE," +
            "gwmttask.TASKID,gwmttask.SENDSTATUS,gwmttask.ERRORCODE,gwmttask.SENDTIME,gwmttask.MESSAGE," +
            "gwmttask.UNICOM,gwmttask.RECVTIME,gwmttask.SVRTYPE,gwmttask.PKNUMBER,gwmttask.PKTOTAL,gwmttask.MSGFMT," +
            "gwmttask.P1,gwmttask.P2,gwmttask.usermsgid,gwmttask.CUSTID,gwmttask.ECID,urltask." + TableLfUrlTask.NETURL +
            ",urltask." + TableLfUrlTask.DOMAIN_URL;
//            String tableSql = " from " + tableName + " gwmttask " + StaticValue.WITHNOLOCK +
//                    " LEFT JOIN "+ TableLfMttask.TABLE_NAME +" lfmttask "+ StaticValue.WITHNOLOCK +" ON gwmttask.TASKID = lfmttask." +
//                    TableLfMttask.TASKID + " left join " + TableLfUrlTask.TABLE_NAME +
//                    " urltask " + StaticValue.WITHNOLOCK + " on urltask." + TableLfUrlTask.TASKID + " = gwmttask." + TableMtTask01_12.TASK_ID ;
            String tableSql = " from " + TableLfUrlTask.TABLE_NAME +
            " urltask " + StaticValue.getWITHNOLOCK() + " inner join " + tableName + " gwmttask " + StaticValue.getWITHNOLOCK() +
            " on urltask." + TableLfUrlTask.TASKID + " = gwmttask." + TableMtTask01_12.TASK_ID;
            //组装过滤条件(传入别名)
            String conditionSql = MtRecordDAOSql.getConditionSql(conditionMap, "gwmttask");
            //获得语句
            /**
             *  + " AND " + "lfmttask." + TableLfMttask.MS_TYPE + " = 31"
             *  移动到MtRecordDAOSql.getConditionSql()这个公共方法去了
             */
            String sql = fieldSql + tableSql + conditionSql;
            //时间段
            List<String> timeList = MtRecordDAOSql.getTimeCondition(conditionMap);

//            String countSql = "select count(*) totalcount" + " from (" + sql + ") totalcount";
            String countSql = "select count(*) as totalcount" + tableSql + conditionSql;
            
//            sql += " order by gwmttask.SENDTIME DESC";
            sql += " order by urltask.taskid DESC,gwmttask.ID DESC";
            //返回list
            List<DynaBean> returnList;

            //记录查询语句
            EmpExecutionContext.info("企业短链-发送明细详情，查询sql："+sql);

            IGenericDAO dao = new DataAccessDriver().getGenericDAO();
            returnList = dao.findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
            //返回LIST
            return returnList;
        }catch (Exception e){
            EmpExecutionContext.error(e, "DAO查询，下行实时记录，异常。");
            return null;
        }
    }
}
