package com.montnets.emp.smstask.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.common.vo.LfMttaskVo;
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.approveflow.TableLfFlowRecord;
import com.montnets.emp.table.engine.TableLfMotask;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sms.TableMtTask;
import com.montnets.emp.table.sms.TableMtTaskC;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;


public class SmstaskDAO extends SuperDAO {

    /**
     * 获取审批信息
     *
     * @param mtID
     * @param rLevel
     * @return
     * @throws Exception
     */
    public List<LfFlowRecordVo> findLfFlowRecordVo(String mtID, String rLevel, String reviewType)
            throws Exception {
        //字段
        String fieldSql = "select flowrecord.FR_ID,flowrecord.MT_ID,flowrecord.F_ID,flowrecord.REVIEW_TYPE,flowrecord.R_TIME,flowrecord.R_LEVEL," +
                "flowrecord.R_LEVELAMOUNT,flowrecord.R_CONTENT,flowrecord.R_STATE,flowrecord.COMMENTS,prerevisysuser.NAME preReviName,revisysuser.NAME reviName," +
                "sysuser.NAME,sysuser.USER_NAME,dep.DEP_NAME,mttask.TIMER_STATUS,mttask.TIMER_TIME,mttask.TITLE,mttask.TASKNAME,mttask.BMTTYPE,mttask.SP_USER," +
                "mttask.SUBMITTIME,mttask.MOBILE_URL,mttask.MSG_TYPE,mttask.EFF_COUNT,mttask.MSG,mttask.TMPL_PATH,userdata.STAFFNAME,sysuser.USER_STATE";
        //表名
        String tableSql = new StringBuffer(" from ").append(
                TableLfFlowRecord.TABLE_NAME).append(" flowrecord ").append(
                StaticValue.getWITHNOLOCK()).append(" left join ").append(
                TableLfSysuser.TABLE_NAME).append(" prerevisysuser ").append(
                StaticValue.getWITHNOLOCK()).append(" on flowrecord.").append(
                TableLfFlowRecord.PRE_RV).append("=prerevisysuser.").append(
                TableLfSysuser.USER_ID).append(" inner join ").append(
                TableLfSysuser.TABLE_NAME).append(" revisysuser ").append(
                StaticValue.getWITHNOLOCK()).append(" on flowrecord.").append(
                TableLfFlowRecord.REVIEWER).append("=revisysuser.").append(
                TableLfSysuser.USER_ID).append(" inner join ").append(
                TableLfMttask.TABLE_NAME).append(" mttask ").append(
                StaticValue.getWITHNOLOCK()).append(" on mttask.").append(
                TableLfMttask.MT_ID).append("=flowrecord.").append(
                TableLfFlowRecord.MT_ID).append(" inner join ").append(
                TableLfSysuser.TABLE_NAME).append(" sysuser ").append(
                StaticValue.getWITHNOLOCK()).append(" on sysuser.").append(
                TableLfSysuser.USER_ID).append("=mttask.").append(
                TableLfMttask.USER_ID).append(" inner join ").append(
                TableLfDep.TABLE_NAME).append(" dep ").append(
                StaticValue.getWITHNOLOCK()).append(" on dep.").append(
                TableLfDep.DEP_ID).append("=sysuser.").append(
                TableLfSysuser.DEP_ID).append(" left join ").append(
                TableUserdata.TABLE_NAME).append(" userdata ").append(
                StaticValue.getWITHNOLOCK()).append(" on mttask.").append(
                TableLfMttask.SP_USER).append("=userdata.").append(
                TableUserdata.USER_ID).toString();

        tableSql = tableSql + " and userdata." + TableUserdata.ACCOUNTTYPE + " = " + reviewType;
        //条件
        StringBuffer conditionSql = new StringBuffer(" where flowrecord.").append(
                TableLfFlowRecord.MT_ID).append("=").append(mtID);
        //审批级别
        if (rLevel != null) {
            conditionSql = conditionSql.append(" and flowrecord.").append(TableLfFlowRecord.R_LEVEL).append(
                    "<=").append(rLevel);
        }
        //类型
        if (reviewType != null) {
            conditionSql = conditionSql.append(" and flowrecord.").append(TableLfFlowRecord.REVIEW_TYPE).append(
                    "=").append(reviewType);
        }
        //排序
        String orderBySql = new StringBuffer(" order by flowrecord.").append(
                TableLfFlowRecord.R_LEVEL).append(" asc").toString();
        //查询sql
        String sql = new StringBuffer(fieldSql).append(tableSql).append(
                conditionSql).append(orderBySql).toString();
        //调用公共方法查询
        List<LfFlowRecordVo> returnVoList = findVoListBySQL(
                LfFlowRecordVo.class, sql, StaticValue.EMP_POOLNAME);
        //返回查询的数据
        return returnVoList;
    }


    /**
     * 查询短信发送任务
     *
     * @param conditionMap
     * @param orderMap
     * @param pageInfo
     * @param tableName
     * @return
     * @throws Exception
     */
    public List<SendedMttaskVo> findMtTaskVo(LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderMap, PageInfo pageInfo, String tableName)
            throws Exception {
        //查询字段拼接
        String fieldSql = SmstaskSql.getPartmttaskFieldSql();
        //查询表拼接
        String tableSql = SmstaskSql.getmttaskTableSql(tableName);
        //组装过滤条件
        String conditionSql = SmstaskSql.getmttaksConditionSql(conditionMap);
        //组装SQL语句
        String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
        //组装统计语句
        String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql)
                .toString();
        //调用查询语句
        List<SendedMttaskVo> mttaskList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(SendedMttaskVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
        if (mttaskList == null) {
            EmpExecutionContext.error("群发历史详情查询异常sql：" + sql);
        }
        //返回结果集
        return mttaskList;
    }

    /**
     * 群发历史任务查询发送详情
     *
     * @param conditionMap
     * @param orderMap
     * @param pageInfo
     * @param tableName
     * @param isBackDb
     * @return
     * @throws Exception
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-10-22 下午02:39:19
     */
    public List<SendedMttaskVo> findMtTaskVo(LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderMap, PageInfo pageInfo, String tableName, boolean isBackDb, PageInfo realDbpageInfo)
            throws Exception {
        IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
        //查询字段拼接
        String fieldSql = SmstaskSql.getPartmttaskFieldSql();
        //查询表拼接
        String tableSql = SmstaskSql.getmttaskTableSql(tableName);
        //组装过滤条件
        String conditionSql = SmstaskSql.getmttaksConditionSql(conditionMap);
        //组装SQL语句
        String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
        //组装统计语句
        String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql)
                .toString();

        //返回记录
        List<SendedMttaskVo> mttaskList;
        String use_his_server = SystemGlobals.getValue("montnets.emp.use_history_server");
        EmpExecutionContext.info("查询历史任务群发发送详情，tableName:" + tableName
                + "，use_history_server:" + use_his_server + "，sql:" + sql);

        //构造需要特殊查询的历史表名
        String hisTableName = "MTTASK" + StaticValue.getUseHistoryDBTime();

        //如果是配置日期的表
        if (hisTableName.equals(tableName.toUpperCase()) && "1".equals(use_his_server)) {
            //是否第一页，查两个库这两张表的总数
            if (pageInfo.getPageIndex() == 1) {
                mttaskList = genericDAO.findPageVoListBySQLNoCount(SendedMttaskVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
                //设置实时库的总记录数
                realDbpageInfo.setTotalPage(pageInfo.getTotalPage());
                //设置实时库的总页数
                realDbpageInfo.setTotalRec(pageInfo.getTotalRec());

                //获取历史库的总数
                PageInfo pageInfoTemp = new PageInfo();
                //设置查询一页条数
                pageInfoTemp.setPageSize(pageInfo.getPageSize());
                List<SendedMttaskVo> mttaskBackDbList = genericDAO.findPageVoListBySQLNoCount(SendedMttaskVo.class, sql, countSql, pageInfoTemp, StaticValue.EMP_BACKUP);
                //历史库有数据，累加总记录数和总页数
                if (pageInfoTemp.getTotalRec() > 0) {
                    //如果实时库总记录数为0
                    if (pageInfo.getTotalRec() == 0) {
                        //直接赋值
                        pageInfo.setTotalPage(pageInfoTemp.getTotalPage());
                    } else {
                        //累加总页数
                        pageInfo.setTotalPage(pageInfo.getTotalPage() + pageInfoTemp.getTotalPage());
                    }
                    //累加总记录数
                    pageInfo.setTotalRec(pageInfo.getTotalRec() + pageInfoTemp.getTotalRec());
                }
                //实时库有数据，直接返回
                if (mttaskList != null && mttaskList.size() > 0) {
                    return mttaskList;
                } else {
                    EmpExecutionContext.info("查询历史任务群发发送详情分页信息，实时库无数据，tableName:" + tableName + "，sql:" + sql);
                }
                return mttaskBackDbList;
            } else {
                //根据分页查询实时库数据
                mttaskList = genericDAO.findPageVoListBySQLNoCount(SendedMttaskVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
                //如果实时库有数据
                if (mttaskList != null && mttaskList.size() > 0) {
                    //返回实时库查询结果
                    return mttaskList;
                }
                //实时库无数据
                else {
                    EmpExecutionContext.info("查询历史任务群发发送详情，实时库无数据，tableName:" + tableName + "，sql:" + sql);
                    PageInfo pageInfoTemp = new PageInfo();
                    pageInfoTemp.setPageSize(pageInfo.getPageSize());
                    //实时库的总记录数为0
                    if (realDbpageInfo.getTotalRec() == 0) {
                        //以实际的查询页数
                        pageInfoTemp.setPageIndex(pageInfo.getPageIndex());
                    } else {
                        //查询页数减去实时总页数
                        pageInfoTemp.setPageIndex(pageInfo.getPageIndex() - realDbpageInfo.getTotalPage());
                    }
                    //返回历史库查询结果
                    mttaskList = genericDAO.findPageVoListBySQLNoCount(SendedMttaskVo.class, sql, countSql, pageInfoTemp, StaticValue.EMP_BACKUP);
                    return mttaskList;
                }
            }
        } else {
            String poolName = StaticValue.EMP_POOLNAME;
            //查询历史库
            if (isBackDb && "1".equals(use_his_server)) {
                poolName = StaticValue.EMP_BACKUP;
            }
            //调用查询语句
            mttaskList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(SendedMttaskVo.class, sql, countSql, pageInfo, poolName);
            if (mttaskList == null) {
                EmpExecutionContext.error("群发历史详情查询异常sql：" + sql);
            }
            //返回结果集
            return mttaskList;
        }
    }

    /**
     * 获取短信发送任务
     *
     * @param conditionMap
     * @param orderbyMap
     * @param tableName
     * @return
     * @throws Exception
     */
    public List<SendedMttaskVo> findMtTaskVo(LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap, String tableName)
            throws Exception {
        //查询字段拼接
        String fieldSql = SmstaskSql.getPartmttaskFieldSql();
        String tableSql = SmstaskSql.getmttaskTableSql(tableName);
        //组装过滤条件
        String conditionSql = SmstaskSql.getmttaksConditionSql(conditionMap);
        //组装SQL语句
        String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
        //调用查询方法
        List<SendedMttaskVo> mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
        if (mttaskList == null) {
            EmpExecutionContext.error("群发历史查询异常sql：" + sql);
        }
        //返回结果集
        return mttaskList;
    }

    /**
     * 获取短信发送任务
     *
     * @param conditionMap
     * @param orderbyMap
     * @param tableName
     * @return
     * @throws Exception
     */
    public List<SendedMttaskVo> findMtTaskVo(LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap, String tableName, boolean isBackDb)
            throws Exception {
        try {
            //查询字段拼接
            String fieldSql = SmstaskSql.getPartmttaskFieldSql();
            String tableSql = SmstaskSql.getmttaskTableSql(tableName);
            //组装过滤条件
            String conditionSql = SmstaskSql.getmttaksConditionSql(conditionMap);
            //组装SQL语句
            String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
            List<SendedMttaskVo> mttaskList;
            //读取配置文件里是否启用备用服务器
            String use_his_server = SystemGlobals.getValue("montnets.emp.use_history_server");
            //构造需要特殊查询的历史表名
            String hisTableName = "MTTASK" + StaticValue.getUseHistoryDBTime();
            //如果是配置日期的表
            if (hisTableName.equals(tableName.toUpperCase()) && "1".equals(use_his_server)) {
                mttaskList = new ArrayList<SendedMttaskVo>();
                //查询实时库
                List<SendedMttaskVo> mttaskRealList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
                if (mttaskRealList != null && mttaskRealList.size() > 0) {
                    mttaskList.addAll(mttaskRealList);
                }
                //查询历史库
                List<SendedMttaskVo> mttaskBackList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_BACKUP);
                if (mttaskBackList != null && mttaskBackList.size() > 0) {
                    mttaskList.addAll(mttaskBackList);
                }
            } else {
                String poolName = StaticValue.EMP_POOLNAME;
                //查询历史库
                if (isBackDb && "1".equals(use_his_server)) {
                    poolName = StaticValue.EMP_BACKUP;
                }
                //调用查询方法
                mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, poolName);
                if (mttaskList == null) {
                    EmpExecutionContext.error("群发历史查询导出异常sql：" + sql);
                }
            }
            //返回结果集
            return mttaskList;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "群发历史查询导出异常!tableName:" + tableName + "，isbackDb:" + isBackDb);
            return null;
        }
    }

    /**
     * 发送详情的导入查询（同时查实时表和历史表）
     *
     * @param conditionMap
     * @param orderbyMap
     * @param tableName
     * @return
     * @throws Exception
     */
    public List<SendedMttaskVo> findMtTaskVoTwoTable(LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap, String tableName)
            throws Exception {
        //查询字段拼接
        String fieldSql = SmstaskSql.getPartmttaskFieldSql();
        //查询表拼接
        String tableSql = SmstaskSql.getmttaskTableSqlTwo(tableName, conditionMap);
        //查询条件拼接
        String conditionSql = "";
        //组装SQL语句
        String sql = new StringBuffer(fieldSql).append(" ,mt.").append(TableMtTask.TASK_ID).append(" from (").append(tableSql).append(") mt").append(conditionSql).toString();
        //调用查询方法
        List<SendedMttaskVo> mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);

        return mttaskList;
    }

    /**
     * 发送详情的详情查询（同时查实时表和历史表）
     *
     * @param conditionMap
     * @param orderbyMap
     * @param tableName
     * @param pageInfo     分页
     * @return
     * @throws Exception
     */
    public List<SendedMttaskVo> findMtTaskVoTwoTable(LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderMap, PageInfo pageInfo, String tableName)
            throws Exception {
        //查询字段拼接
        String fieldSql = SmstaskSql.getPartmttaskFieldSql();
        String tableSql = SmstaskSql.getmttaskTableSqlTwo(tableName, conditionMap);
        //组装过滤条件
        String conditionSql = "";//GenericLfMttaskVoSQL.getmttaksConditionSql(conditionMap);
        //组装SQL语句
        String sql = new StringBuffer(fieldSql).append(" ,mt.").append(TableMtTask.TASK_ID).append(" from (").append(tableSql).append(") mt").append(conditionSql).toString();
        //组装统计语句
        String countSql = new StringBuffer("select count(*) totalcount from (").append(tableSql).append(") mt").append(conditionSql)
                .toString();
        //调用查询方法
        List<SendedMttaskVo> mttaskList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(SendedMttaskVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
        if (mttaskList == null) {
            EmpExecutionContext.error("群发历史详情查询异常sql：" + sql);
        }
        return mttaskList;
    }

    /**
     * 跟据taskid查询mtdatareport表中任务的icount和
     *
     * @param conditionMap
     * @return
     * @throws Exception
     */
    public long findSumIcount(LinkedHashMap<String, String> conditionMap)
            throws Exception {
        //查询字段拼接
        String fieldSql = SmstaskSql.getSumIcountMtdatareportFieldSql();
        String tableSql = SmstaskSql.getSumIcountMtdatareportTableSql();
        //组装过滤条件
        String conditionSql = SmstaskSql.getSumIcountMtdatareportcConditionSql(conditionMap);
        //组装SQL语句
        String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
        //调用统计总条数语句
        long strCount = findCountBySQL(sql, null);
        return strCount;
    }

    /**
     * 获取短信发送任务
     *
     * @param GL_UserID
     * @param lfMttaskVo
     * @return lfMttaskVoList
     * @throws Exception
     */
    public List<LfMttaskVo> findLfMttaskVo(Long GL_UserID, LfMttaskVo lfMttaskVo)
            throws Exception {
        //查询字段拼接
        String fieldSql = SmstaskSql.getFieldSql();
        //查询表拼接
        String tableSql = SmstaskSql.getTableSql();
        //管辖范围拼接
        String dominationSql = "";
        String dominationSql2 = "";

        if ((lfMttaskVo.getDepIds() == null || "".equals(lfMttaskVo.getDepIds())) && (lfMttaskVo.getUserIds() == null || "".equals(lfMttaskVo.getUserIds()))) {
            //获取操作员
            LfSysuser lfSysuser = new DataAccessDriver().getEmpDAO().findObjectByID(LfSysuser.class, GL_UserID);
            //如果登录操作员不是管理员，则进行管辖权限控制。因为接入类短信有可能没有操作员ID。
            if (!"admin".equals(lfSysuser.getUserName())) {
                //管辖范围拼接
                dominationSql = SmstaskSql.getDominationSql(String
                        .valueOf(GL_UserID));
                dominationSql2 = SmstaskSql.getDominationSql2(String
                        .valueOf(GL_UserID));
            } else {
                //如果登录操作员是管理员，并且是多企业版的话，就加上企业编码的查询条件。
                if (StaticValue.getCORPTYPE() == 1) {
                    lfMttaskVo.setCorpCode(lfSysuser.getCorpCode());
                }
            }
        }

        if (lfMttaskVo.getDepIds() != null && !"".equals(lfMttaskVo.getDepIds())) {
            //包含子机构
            if (lfMttaskVo.getIsContainsSun() != null && !"".equals(lfMttaskVo.getIsContainsSun()) && "1".equals(lfMttaskVo.getIsContainsSun())) {
                LfDep lfDep = new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
                if (lfDep != null && lfDep.getDepLevel().intValue() == 1) {
                    if (StaticValue.getCORPTYPE() == 1) {
                        //多企业
                        lfMttaskVo.setDepIds("");
                        lfMttaskVo.setCorpCode(lfDep.getCorpCode());
                    } else {
                        //单企业
                        lfMttaskVo.setDepIds("");
                        lfMttaskVo.setUserId(0L);
                    }
                } else {
                    String depid = new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()), TableLfDep.DEP_ID);
                    lfMttaskVo.setDepIds(depid);
                }
                //不包含子机构
            } else {
                String depIdCondition = TableLfDep.DEP_ID + "=" + lfMttaskVo.getDepIds();
                lfMttaskVo.setDepIds(depIdCondition);
            }
        }

        //组装过滤条件
        String conditionSql = SmstaskSql.getConditionSql(lfMttaskVo);
        //组装排序条件
        String orderBySql = SmstaskSql.getOrderBySql();
        //组装SQL语句
        String sql = "";
        //处理后的dominationSql + conditionSql条件
        String dominationSqlStr = SmstaskSql.getConditionSql(dominationSql + conditionSql);
        //处理后的dominationSql2 + conditionSql条件
        String dominationSql2Str = SmstaskSql.getConditionSql(dominationSql2 + conditionSql);

        if ((lfMttaskVo.getDepIds() == null || "".equals(lfMttaskVo.getDepIds())) &&
                (lfMttaskVo.getUserIds() == null || "".equals(lfMttaskVo.getUserIds())) &&
                lfMttaskVo.getCorpCode() == null || "".equals(lfMttaskVo.getCorpCode())) {
            sql = new StringBuffer(fieldSql + " from ( " + fieldSql).append(tableSql).append(
                    dominationSql2Str).append("  union  ").append(fieldSql).append(tableSql).append(
                    dominationSqlStr + " ) mttask ").append(orderBySql)
                    .toString();
        } else {
            //组装SQL语句
            sql = new StringBuffer(fieldSql).append(tableSql).append(
                    dominationSqlStr).append(orderBySql)
                    .toString();
        }

        sql = assembleSql(sql);

        //调用查询方法
        List<LfMttaskVo> lfMttaskVoList = findVoListBySQL(LfMttaskVo.class,
                sql, StaticValue.EMP_POOLNAME);
        return lfMttaskVoList;
    }

    /**
     * 获取短信发送任务
     *
     * @param GL_UserID
     * @param lfMttaskVo
     * @param pageInfo
     * @return lfMttaskVoList
     * @throws Exception
     */
    public List<LfMttaskVo> findLfMttaskVo(Long GL_UserID,
                                           LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception {
        //查询字段拼接
        String fieldSql = SmstaskSql.getFieldSql();
        //查询表拼接
        String tableSql = SmstaskSql.getTableSql();
        String dominationSql = "";
        String dominationSql2 = "";


        if ((lfMttaskVo.getDepIds() == null || "".equals(lfMttaskVo.getDepIds())) && (lfMttaskVo.getUserIds() == null || "".equals(lfMttaskVo.getUserIds()))) {
            //获取操作员
            LfSysuser lfSysuser = new DataAccessDriver().getEmpDAO().findObjectByID(LfSysuser.class, GL_UserID);
            //如果登录操作员不是管理员，则进行管辖权限控制。因为接入类短信有可能没有操作员ID。
            if (!"admin".equals(lfSysuser.getUserName())) {
                //管辖范围拼接
                dominationSql = SmstaskSql.getDominationSql(String
                        .valueOf(GL_UserID));
                dominationSql2 = SmstaskSql.getDominationSql2(String
                        .valueOf(GL_UserID));
            } else {
                //如果登录操作员是管理员，并且是多企业版的话，就加上企业编码的查询条件。
                if (StaticValue.getCORPTYPE() == 1) {
                    lfMttaskVo.setCorpCode(lfSysuser.getCorpCode());
                }
            }
        }

        if (lfMttaskVo.getDepIds() != null && !"".equals(lfMttaskVo.getDepIds())) {
            //包含子机构
            if (lfMttaskVo.getIsContainsSun() != null && !"".equals(lfMttaskVo.getIsContainsSun()) && "1".equals(lfMttaskVo.getIsContainsSun())) {
                LfDep lfDep = new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
                if (lfDep != null && lfDep.getDepLevel().intValue() == 1) {
                    if (StaticValue.getCORPTYPE() == 1) {
                        //多企业
                        lfMttaskVo.setDepIds("");
                        lfMttaskVo.setCorpCode(lfDep.getCorpCode());
                    } else {
                        //单企业
                        lfMttaskVo.setDepIds("");
                        lfMttaskVo.setUserId(0L);
                    }
                } else {
                    String depid = new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()), TableLfDep.DEP_ID);
                    lfMttaskVo.setDepIds(depid);
                }
                //不包含子机构
            } else {
                String depIdCondition = TableLfDep.DEP_ID + "=" + lfMttaskVo.getDepIds();
                lfMttaskVo.setDepIds(depIdCondition);
            }
        }

        //组装过滤条件
        String conditionSql = SmstaskSql.getConditionSql(lfMttaskVo);
        //组装排序条件
        String orderBySql = SmstaskSql.getOrderBySql();
        //组装SQL语句
        String sql = "";
        String countSql = "";
        //处理后的dominationSql + conditionSql条件
        String dominationSqlStr = SmstaskSql.getConditionSql(dominationSql + conditionSql);
        //处理后的dominationSql2 + conditionSql条件
        String dominationSql2Str = SmstaskSql.getConditionSql(dominationSql2 + conditionSql);
        if ((lfMttaskVo.getDepIds() == null || "".equals(lfMttaskVo.getDepIds())) &&
                (lfMttaskVo.getUserIds() == null || "".equals(lfMttaskVo.getUserIds())) &&
                lfMttaskVo.getCorpCode() == null || "".equals(lfMttaskVo.getCorpCode())) {
            sql = new StringBuffer(fieldSql + " from ( " + fieldSql).append(tableSql).append(
                    dominationSql2Str).append("  union  ").append(fieldSql).append(tableSql).append(
                    dominationSqlStr + " ) mttask ").append(orderBySql)
                    .toString();
            countSql = new StringBuffer("select count(*) totalcount")
                    .append("  from ((").append(fieldSql).append(tableSql).append(
                            dominationSql2Str).append(") union (").append(fieldSql).append(tableSql).append(
                            dominationSqlStr).append(") ) A")
                    .toString();
        } else {
            //组装SQL语句
            sql = new StringBuffer(fieldSql).append(tableSql).append(
                    dominationSqlStr).append(orderBySql)
                    .toString();
            //组装总条数
            countSql = new StringBuffer("select count(*) totalcount")
                    .append(tableSql).append(dominationSqlStr)
                    .toString();
        }
        //组装总条数
        //重新组装sql
        sql = assembleSql(sql);

        //调用 查询方法
        List<LfMttaskVo> lfMttaskVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
                LfMttaskVo.class, sql, countSql, pageInfo,
                StaticValue.EMP_POOLNAME);
        //返回结果
        return lfMttaskVoList;
    }

    /**
     * @param lfMttaskVo
     * @param pageInfo
     * @return lfMttaskVoList
     * @throws Exception
     * @description 非数据权限范围查找短信任务
     */
    public List<LfMttaskVo> findLfMttaskVoWithoutDomination(
            LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception {
        //查询字段拼接
        String fieldSql = SmstaskSql.getFieldSql();
        //查询表拼接
        String tableSql = SmstaskSql.getTableSql();
        //根据机构组装下级机构
        if (lfMttaskVo.getDepIds() != null && !"".equals(lfMttaskVo.getDepIds())) {
            //包含子机构
            if (lfMttaskVo.getIsContainsSun() != null && !"".equals(lfMttaskVo.getIsContainsSun()) && "1".equals(lfMttaskVo.getIsContainsSun())) {
                LfDep lfDep = new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
                if (lfDep != null && lfDep.getDepLevel().intValue() == 1) {
                    lfMttaskVo.setDepIds("");
                    lfMttaskVo.setCorpCode(lfDep.getCorpCode());
                } else {
                    String depid = new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()), TableLfDep.DEP_ID);
                    lfMttaskVo.setDepIds(depid);
                }
            } else {
                //不包含子机构
                String depIdCondition = TableLfDep.DEP_ID + "=" + lfMttaskVo.getDepIds();
                lfMttaskVo.setDepIds(depIdCondition);
            }
        }
        //组装过滤条件
        String conditionSql = SmstaskSql.getConditionSql(lfMttaskVo);
        //处理后的 conditionSql条件
        conditionSql = SmstaskSql.getConditionSql(conditionSql);
        //排序条件拼接
        String orderBySql = SmstaskSql.getOrderBySql();
        String sql = new StringBuffer(fieldSql).append(tableSql)
                .append(conditionSql).append(orderBySql)
                .toString();
        //组装统计SQL语句
        String countSql = new StringBuffer("select count(*) totalcount")
                .append(tableSql).append(conditionSql)
                .toString();
        //调用查询语句
        List<LfMttaskVo> lfMttaskVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
                LfMttaskVo.class, sql, countSql, pageInfo,
                StaticValue.EMP_POOLNAME);
        return lfMttaskVoList;
    }

    /**
     * 获取短信发送任务
     *
     * @param lfMttaskVo
     * @return
     * @throws Exception
     */
    public List<LfMttaskVo> findLfMttaskVoWithoutDomination(LfMttaskVo lfMttaskVo) throws Exception {
        //查询字段拼接
        String fieldSql = SmstaskSql.getFieldSql();
        //查询表拼接
        String tableSql = SmstaskSql.getTableSql();

        //根据机构组装下级机构
        if (lfMttaskVo.getDepIds() != null && !"".equals(lfMttaskVo.getDepIds())) {
            //包含子机构
            if (lfMttaskVo.getIsContainsSun() != null && !"".equals(lfMttaskVo.getIsContainsSun()) && "1".equals(lfMttaskVo.getIsContainsSun())) {
                LfDep lfDep = new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
                if (lfDep != null && lfDep.getDepLevel().intValue() == 1) {
                    lfMttaskVo.setDepIds("");
                    lfMttaskVo.setCorpCode(lfDep.getCorpCode());
                } else {
                    String depid = new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()), TableLfDep.DEP_ID);
                    lfMttaskVo.setDepIds(depid);
                }
            } else {
                //不包含子机构
                String depIdCondition = TableLfDep.DEP_ID + "=" + lfMttaskVo.getDepIds();
                lfMttaskVo.setDepIds(depIdCondition);
            }
        }

        //组装过滤条件
        String conditionSql = SmstaskSql.getConditionSql(lfMttaskVo);
        //处理后的 conditionSql条件
        conditionSql = SmstaskSql.getConditionSql(conditionSql);
        //组装排序条件
        String orderBySql = SmstaskSql.getOrderBySql();
        //组装SQL语句
        String sql = new StringBuffer(fieldSql).append(tableSql)
                .append(conditionSql).append(orderBySql)
                .toString();


        //调用查询方法
        List<LfMttaskVo> lfMttaskVoList = findVoListBySQL(LfMttaskVo.class,
                sql, StaticValue.EMP_POOLNAME);

        return lfMttaskVoList;
    }

    public List<LfSysuser> findDomUserBySysuserIDOfSmsTaskRecordByDep(String sysuserID, String depId)
            throws Exception {
        //拼接sql
        StringBuffer domination = new StringBuffer("select ").append(
                TableLfDomination.DEP_ID).append(" from ").append(
                TableLfDomination.TABLE_NAME).append(" where ").append(
                TableLfDomination.USER_ID).append("=").append(sysuserID);
        StringBuffer dominationSql = new StringBuffer(" where (").append(
                TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
                " or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
                domination).append(")) and ").append(TableLfSysuser.USER_ID)
                .append("<>1 and ").append(TableLfSysuser.DEP_ID).append("=").append(depId);
        String sql = new StringBuffer("select * from ").append(
                TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
        //排序条件拼接
        sql += " order by " + TableLfSysuser.NAME + " asc";
        List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
                StaticValue.EMP_POOLNAME);
        //返回结果
        return returnList;
    }

    /**
     * @param taskids
     * @return
     * @description 获取任务滞留条数
     * @author zousy <zousy999@qq.com>
     * @datetime 2015-4-3 上午10:05:07
     */
    public Map<Long, Long> getTaskRemains(String taskids) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);//一天前
        IGenericDAO genericDao = new DataAccessDriver().getGenericDAO();
        String time = genericDao.getTimeCondition(sdf.format(calendar.getTime()));
        String sql = "select " + TableMtTaskC.TASK_ID + ",count(*) as total from " + TableMtTaskC.TABLE_NAME + " where " +
                TableMtTaskC.SEND_STATUS + " = 208 and " +
                TableMtTaskC.TASK_ID + " in (" + taskids + ") and " + TableMtTaskC.SEND_TIME + " >= " + time + " group by " +
                TableMtTaskC.TASK_ID;
        List<DynaBean> list = getListDynaBeanBySql(sql);
        if (list == null || list.size() == 0) {
            return null;
        }
        Map<Long, Long> map = new HashMap<Long, Long>();
        for (DynaBean bean : list) {
            long taskId = Long.valueOf(bean.get("taskid").toString());
            long total = Long.valueOf(bean.get("total").toString());
            map.put(taskId, total);
        }
        return map;
    }


    /**
     * 获取群发历史回复信息
     *
     * @param conditionMap 条件
     * @param pageInfo     分页
     * @return
     */
    public List<DynaBean> getReplyDetailList(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
        try {
            if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
                String fieldSql = "select mo.phone, mo.delivertime, mo.msgcontent, '' as name";
                //拼接SQL
                StringBuffer tableSql = new StringBuffer();
                tableSql.append(" FROM ").append(TableLfMotask.TABLE_NAME).append(" mo").append(" WHERE");
                // 查询条件
                StringBuffer conSql = new StringBuffer();
                //任务ID
                if (conditionMap.get("taskId") != null && !"".equals(conditionMap.get("taskId"))) {
                    conSql.append(" mo.task_id=" + conditionMap.get("taskId"));
                } else {
                    EmpExecutionContext.error("获取群发历史回复信息失败，taskId为空。");
                    return null;
                }
                //企业编码
                String corpCode = conditionMap.get("corpCode");
                if (corpCode != null && !"".equals(corpCode)) {
                    conSql.append(" and mo.corpcode='" + corpCode + "'");
                } else {
                    EmpExecutionContext.error("获取群发历史回复信息失败，企业编码为空。");
                    return null;
                }
                //号码
                if (conditionMap.get("phone") != null && !"".equals(conditionMap.get("phone"))) {
                    conSql.append(" and mo.phone='" + conditionMap.get("phone") + "'");
                }
                //内容
                if (conditionMap.get("msgContent") != null && !"".equals(conditionMap.get("msgContent"))) {
                    //回复详情查询增加模糊查询
                    conSql.append(" and mo.msgContent like '%" + conditionMap.get("msgContent") + "%'");
                }
                //姓名
                String replyName = conditionMap.get("replyName");
                if (replyName != null && !"".equals(replyName)) {
                    //查询条件存在用户名,查询结果直接使用
                    fieldSql = "select mo.phone, mo.delivertime, mo.msgcontent, '" + replyName + "' as name";
                    conSql.append(" and ((mo.phone in(select emp.MOBILE from lf_employee emp where emp.NAME='").append(replyName)
                            .append("'  and emp.corp_code='").append(corpCode).append("')) or (mo.phone in(select cl.MOBILE from lf_client cl where cl.NAME ='").append(replyName)
                            .append("'  and cl.corp_code='").append(corpCode).append("')) or (mo.phone in(select sys.MOBILE from lf_sysuser sys where sys.NAME ='").append(replyName)
                            .append("'  and sys.corp_code='").append(corpCode).append("')))");
                }
                // 排序
                String orderbySql = " order by delivertime DESC";
                // 返回结果
                String sql = fieldSql + tableSql.toString() + conSql.toString() + orderbySql;
                if (pageInfo == null) {
                    return getListDynaBeanBySql(sql);
                } else {
                    String countSql = "select count(*) totalcount" + tableSql.toString() + conSql.toString();
                    return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
                }
            } else {
                EmpExecutionContext.error("获取群发历史回复信息失败，没有查询条件。");
                return null;
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取群发历史回复信息失败！");
            return null;
        }

    }

    /**
     * 从员工通讯录、客户通讯录、操作员表中获取回复详情用户姓名
     *
     * @param phones
     * @param corpCode
     * @param phoneNameMap
     */
    public void getReplyDetailName(String phones, String corpCode, Map<String, String> phoneNameMap) {
        try {
            if (phones != null && !"".equals(phones) && corpCode != null && !"".equals(corpCode)) {
                IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
                String insqlstr = getSqlStr(phones, "MOBILE");
                List<DynaBean> replyDetailNameList = null;
                // 从操作员表中获取
                String sysUserSql = "select mobile,name from lf_sysuser where (" + insqlstr + ") and corp_code='" + corpCode + "'";
                replyDetailNameList = genericDAO.findDynaBeanBySql(sysUserSql);
                if (replyDetailNameList != null && replyDetailNameList.size() > 0) {
                    for (DynaBean db : replyDetailNameList) {
                        if (db.get("mobile") != null && db.get("name") != null) {
                            phoneNameMap.put(db.get("mobile").toString(), db.get("name").toString());
                        }
                    }
                    replyDetailNameList = null;
                }

                // 从客户通讯录表中获取
                String ClientSql = "select mobile,name from LF_CLIENT where (" + insqlstr + ") and corp_code='" + corpCode + "'";
                replyDetailNameList = genericDAO.findDynaBeanBySql(ClientSql);
                if (replyDetailNameList != null && replyDetailNameList.size() > 0) {
                    for (DynaBean db : replyDetailNameList) {
                        if (db.get("mobile") != null && db.get("name") != null) {
                            phoneNameMap.put(db.get("mobile").toString(), db.get("name").toString());
                        }
                    }
                    replyDetailNameList = null;
                }

                // 从员工通讯录表中获取
                String employeeSql = "select mobile,name from lf_employee where (" + insqlstr + ") and corp_code='" + corpCode + "'";
                replyDetailNameList = genericDAO.findDynaBeanBySql(employeeSql);
                if (replyDetailNameList != null && replyDetailNameList.size() > 0) {
                    for (DynaBean db : replyDetailNameList) {
                        if (db.get("mobile") != null && db.get("name") != null) {
                            phoneNameMap.put(db.get("mobile").toString(), db.get("name").toString());
                        }
                    }
                }
            } else {
                return;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取回复详情用户姓名失败！");
            return;
        }
    }

    /**
     * 兼容IN语句超过900个条件
     *
     * @param idstr     查询条件
     * @param columnstr 查询字段名
     * @return
     */
    public String getSqlStr(String idstr, String columnstr) {
        String sql = " 1=2 ";
        if (idstr != null && !"".equals(idstr) && columnstr != null && !"".equals(columnstr)) {
            if (idstr.contains(",")) {
                String[] useriday = idstr.split(",");
                if (useriday.length < 900) {
                    sql = " " + columnstr + " IN (" + idstr + ") ";
                } else {
                    String zidstr = "";
                    sql = "";
                    for (int i = 0; i < useriday.length; i++) {
                        if ((i + 1) % 900 == 0) {
                            zidstr = zidstr + useriday[i];
                            sql = sql + " " + columnstr + " IN (" + zidstr + ") OR ";
                            zidstr = "";

                        } else {
                            zidstr = zidstr + useriday[i] + ",";
                        }
                    }
                    if (!"".equals(sql) && "".equals(zidstr)) {
                        sql = sql.substring(0, sql.lastIndexOf("OR"));
                    } else if (!"".equals(sql) && !"".equals(zidstr)) {
                        zidstr = zidstr.substring(0, zidstr.length() - 1);
                        sql = sql + " " + columnstr + " IN (" + zidstr + ") ";
                    } else {
                        sql = " 1=2 ";
                    }
                }
            } else {
                sql = " " + columnstr + " = " + idstr;
            }
        }
        return sql;
    }


    /**
     * 重发获取手机号码
     *
     * @param resendType 重发类型  1：提交失败重发;2：接收失败重发;3：选中重发
     * @param mtid
     * @return
     */
    public List<SendedMttaskVo> getReSendPhone(String resendType, String mtid) {
        //发送详情
        List<SendedMttaskVo> MttaskvoList = null;
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //设置mtid
            conditionMap.put("mtid", mtid);

            if ("1".equals(resendType)) {
                //提交失败重发
                conditionMap.put("errorcode&like", "E1:,E2:");
            } else if ("2".equals(resendType)) {
                //接收失败重发
                conditionMap.put("errorcode&not like", "E1:,E2:,DELIVRD");
            }

            //根据mtid获取任务信息
            LfMttask Lfmttask = new BaseBiz().getById(LfMttask.class, mtid);
            //获取当前任务的发送时间
            Timestamp subTime = Lfmttask.getTimerTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            Date date1 = df.parse(df.format(new Date()));
            Date date2 = df.parse(df.format(subTime.getTime()));
            //当前时间
            Calendar c1 = Calendar.getInstance();
            //发送时间
            Calendar c2 = Calendar.getInstance();
            c1.setTime(date1);
            c2.setTime(date2);
            //计算时间，当前时间减去发送时间小于3天,查实时表，否则查对应月的历史表
            c2.add(c2.DATE, 4);

            if (Lfmttask.getTaskType() == 1) {
                conditionMap.put("taskid", Lfmttask.getTaskId().toString());
            } else {
                conditionMap.put("batchid", Lfmttask.getBatchID().toString());
            }

            if (c2.after(c1)) {
                String fieldSql = new StringBuffer("SELECT UNICOM,PHONE,MESSAGE,ERRORCODE,PKNUMBER,PKTOTAL,TASKID ").toString();
                String tablename = new StringBuffer()
                        .append(" (")
                        .append(fieldSql).append(" FROM ").append("gw_mt_task_bak").append(StaticValue.getWITHNOLOCK())
                        .append(" union all ")
                        .append(fieldSql).append(" FROM ").append("mt_task").append(StaticValue.getWITHNOLOCK())
                        .append(")  ").toString();
                MttaskvoList = findMtTaskVo(conditionMap, null, tablename);
            } else {
                //计算获得历史表的表名（发送时间的月份）
                int month = subTime.getMonth() + 1;
                String year = df.format(subTime.getTime()).substring(0, 4);
                String date = year;
                if (month < 10) {
                    //日期
                    date += "0" + month;
                } else {
                    //日期
                    date += month;
                }
                //表名
                String tableName = "MTTASK" + date;
                boolean isBackDb = false;
                //验证历史表是否存在
                tableName = new CommonBiz().getTableName(tableName);

                LinkedHashMap<String, String> conMapcount = new LinkedHashMap<String, String>();

                if (Lfmttask.getTaskType() == 1) {
                    conMapcount.put("taskid", String.valueOf(Lfmttask.getTaskId()));
                } else {
                    conMapcount.put("batchid", String.valueOf(Lfmttask.getBatchID()));
                }

                //跟据taskid统计mt_datareport表中的icout值
                String iymd;
                //当前时间
                Calendar curTime = Calendar.getInstance();
                //当前时间减三天
                curTime.add(curTime.DATE, -4);
                SimpleDateFormat sidf = new SimpleDateFormat("yyyy-MM-dd");
                //截取转换成mtdatareport的iymd字段的number,以便用来查询三天前的mtdatareport表的此任务的icount字段的和
                iymd = sidf.format(curTime.getTime()).replaceAll("-", "");
                conMapcount.put("iymd", iymd);
                //mtdatareport表里面对应任务的icount的总和
                long sumCount = findSumIcount(conMapcount);

                //预发送条数
                String count = Lfmttask.getIcount() == null ? "0" : Lfmttask.getIcount();
                //如果三天前的mtdatareport表里的此任务的icount的总和lfmttask的预发送条数icount则查历史表和实时两张表，否则只差对应历史表
                if (sumCount < Long.parseLong(count)) {
                    MttaskvoList = findMtTaskVoTwoTable(conditionMap, null, tableName);
                } else {
                    MttaskvoList = findMtTaskVo(conditionMap, null, tableName, isBackDb);
                }
            }
            return MttaskvoList;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "重发获取手机号码失败！");
            return null;
        }
    }

    private String assembleSql(String originSql) {
        //重新组装sql
//        StringBuffer tempSqlSb = new StringBuffer();
//        tempSqlSb.append("SELECT * FROM ( ");
//        tempSqlSb.append(originSql);
//        tempSqlSb.append(" ) tempTask");
        String tempSql = "";
        if (null != originSql) {
            tempSql = originSql.replaceFirst("mttask.BATCHID", "mttask.BATCHID,(SELECT COUNT(1) FROM LF_MOTASK WHERE TASK_ID = mttask.TASKID) MONUMBER");
        }
        return tempSql;
    }

    /**
     * 群发历史任务汇总
     *
     * @param tableName 实时表/历史表
     * @param taskId    任务id
     * @return sql
     */
    public boolean updateLfmttask(String tableName, String taskId) throws Exception{
        String sql = SmstaskSql.getlfmttaskTableSqlTwo(tableName, taskId);
        executeBySQL(sql, StaticValue.EMP_POOLNAME);
        return true;
    }

}
