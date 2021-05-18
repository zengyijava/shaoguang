package com.montnets.emp.rms.detailsend.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.rms.vo.RmsMtRecordVo;
import com.montnets.emp.util.PageInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RmsMtRecordDao extends SuperDAO {
    private IGenericDAO dao = new DataAccessDriver().getGenericDAO();
    /**
     * 查询历史表下行
     * @param conditionMap 条件集合的Map
     * @param pageInfo 分页对象 可以为null，为null则查询全部
     * @return 结果集
     */
    public List<RmsMtRecordVo> findRecordHis(LinkedHashMap<String,String> conditionMap, PageInfo pageInfo) {
        List<RmsMtRecordVo> rmsMtRecordVos = new ArrayList<RmsMtRecordVo>();
        String sql = "";
        try {
            String fieldSql = RmsMtRecordSql.getFieldSql("mttask");
            String conditionSql = RmsMtRecordSql.getConditionSql(conditionMap,"mttask");
            String sendTime = conditionMap.get("sendTime");
            String recvTime = conditionMap.get("recvTime");
            String tableSql = RmsMtRecordSql.getHisTableSql(sendTime,recvTime);
            //组sql语句
            sql = fieldSql + tableSql + conditionSql;
            sql += " order by mttask.SENDTIME DESC";
            String countSql = "select count(*) totalcount" + " from (" + sql + ") totalcount";
            if(pageInfo != null){
                if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
                    String tempSql = sql.replaceFirst("SELECT","SELECT TOP 100 PERCENT ");
                    countSql = "select count(*) totalcount from (" + tempSql + ") totalcount";
                }
                rmsMtRecordVos = dao.findPageVoListBySQL(RmsMtRecordVo.class,sql,countSql,pageInfo,StaticValue.EMP_POOLNAME);
            }else {
                rmsMtRecordVos = findVoListBySQL(RmsMtRecordVo.class, sql, StaticValue.EMP_POOLNAME);
            }
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业富信-数据查询-发送明细查询-查询历史下行记录-执行sql查询结果异常，SQL语句：" + sql);
        }
        return rmsMtRecordVos;
    }

    /**
     * 查询实时表下行
     * @param conditionMap 条件集合的Map
     * @param pageInfo 分页对象 可以为null，为null则查询全部
     * @return 结果集
     */
    public List<RmsMtRecordVo> findRecordReal(LinkedHashMap<String,String> conditionMap, PageInfo pageInfo) {
        List<RmsMtRecordVo> rmsMtRecordVos = new ArrayList<RmsMtRecordVo>();
        String sql = "";
        try {
            String fieldSql = RmsMtRecordSql.getFieldSql("gwmttask");
            String tableSql = RmsMtRecordSql.getRealTableSql();
            String conditionSql = RmsMtRecordSql.getConditionSql(conditionMap,"gwmttask");
            //组sql语句
            sql = fieldSql + tableSql + conditionSql;
            String countSql = "select count(*) totalcount" + " from (" + sql + ") totalcount";
            sql += " order by gwmttask.SENDTIME DESC";
            if(pageInfo != null){
                if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
                    String tempSql = sql.replaceFirst("SELECT","SELECT TOP 100 PERCENT ");
                    countSql = "select count(*) totalcount from (" + tempSql + ") A";
                }
                rmsMtRecordVos = dao.findPageVoListBySQL(RmsMtRecordVo.class,sql,countSql,pageInfo,StaticValue.EMP_POOLNAME);
            }else {
                rmsMtRecordVos = findVoListBySQL(RmsMtRecordVo.class, sql, StaticValue.EMP_POOLNAME);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"企业富信-数据查询-发送明细查询-查询实时下行记录-执行sql查询结果异常，SQL语句：" + sql);
        }
        return rmsMtRecordVos;
    }
}
