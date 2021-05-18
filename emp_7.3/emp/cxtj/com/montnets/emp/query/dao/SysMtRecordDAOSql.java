package com.montnets.emp.query.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.inbox.dao.ReciveBoxDao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @project emp_std_192.169.1.81_new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-12-3 上午10:06:58
 * @description
 */
public class SysMtRecordDAOSql
{
    private static IGenericDAO genericDao = new DataAccessDriver().getGenericDAO();
    /**
     * 构造查询条件sql
     * @param conditionMap 查询条件
     * @param tableName 表名
     * @return 返回查询条件sql，包含where
     */
    public static String getConditionSql(LinkedHashMap<String, String> conditionMap, String tableName) {
        try {
            StringBuffer conditionSql = new StringBuffer();
            //是否有where，默认为false即没where
            boolean hasWhere = false;
            //多企业，且未绑定sp账号，则查询结果为空
            if(StaticValue.getCORPTYPE() == 1 && (conditionMap.get("spUsers") == null || conditionMap.get("spUsers").length() < 1)) {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(" 1=2 ");
            }
            //手机号
            if (conditionMap.get("phone") != null && conditionMap.get("phone").trim().length() > 0)
            {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(tableName).append(".PHONE='").append(conditionMap.get("phone").trim()).append("'");
            }
            //任务批次
            if (conditionMap.get("taskid") != null && conditionMap.get("taskid").trim().length() > 0)
            {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(tableName).append(".TASKID=").append(conditionMap.get("taskid").trim());
            }
            //主键id
            if (null != conditionMap.get("ids") && conditionMap.get("ids").trim().length() > 0){
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(tableName).append(".ID IN(").append(conditionMap.get("ids").trim()).append(")");
            }
            
            //modify by tanglili20181217---------------开始
            //ptmsgid 平台流水号查询条件
            if (null != conditionMap.get("ptmsgids") && conditionMap.get("ptmsgids").trim().length() > 0){
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(tableName).append(".PTMSGID IN(").append(conditionMap.get("ptmsgids").trim()).append(")");
            }
            //modify by tanglili20181217---------------结束

            /*//自定义流水号
            if (conditionMap.get("usermsgid") != null && conditionMap.get("usermsgid").trim().length() > 0)
            {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(tableName).append(".usermsgid=").append(conditionMap.get("usermsgid").trim());
            }*/

            //自定义流水号
            /*1.为null或空字符串或者0，不加查询过滤条件
            2.为非纯数字组合即字符串，加 custid='zdymsgid' 的查询过滤条件
            3.为非0纯数字，加 ( usermsgid=数字 或 custid='数字'字符串 )  查询过滤条件*/

            if (conditionMap.get("usermsgid") != null && conditionMap.get("usermsgid").trim().length() > 0 && !conditionMap.get("usermsgid").equals("0") )
            {
                if (!conditionMap.get("usermsgid").matches(("[0-9]+")))
                {
                    //为非纯数字组合即字符串，加 custid='zdymsgid' 的查询过滤条件
                    hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                    conditionSql.append(tableName).append(".custid='").append(conditionMap.get("usermsgid").trim()).append("' ");
                }else{
                    //为非0纯数字，加 ( usermsgid=数字 或 custid='数字'字符串 )  查询过滤条件
                    hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                    conditionSql.append(" ( ").append(tableName).append(".usermsgid=").append(conditionMap.get("usermsgid").trim()).append(" or ")
                    .append(tableName).append(".custid='").append(conditionMap.get("usermsgid").trim()).append("' ").append(" ) ");
                }
            }

            //指定时间 段，开始时间 
            if (conditionMap.get("sendtime") != null && conditionMap.get("sendtime").trim().length() > 0)
            {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(tableName).append(".SENDTIME>=").append(genericDao.getTimeCondition(conditionMap.get("sendtime")));
            }
            //指定时间段，结束时间
            if (conditionMap.get("recvtime") != null && conditionMap.get("recvtime").trim().length() > 0)
            {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(tableName).append(".SENDTIME<=").append(genericDao.getTimeCondition(conditionMap.get("recvtime")));
            }
            // sp账号
            if (conditionMap.get("userid") != null && conditionMap.get("userid").trim().length() > 0)
            {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                //如果是DB2或者oracle 则做兼容小写处理
                if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE==StaticValue.ORACLE_DBTYPE
                        ||StaticValue.DBTYPE==StaticValue.DB2_DBTYPE)){
                    String useridstr=conditionMap.get("userid");
                    conditionSql.append(tableName).append(".USERID").append(" in ('").append(useridstr.toUpperCase().trim()).append("','")
                    .append(useridstr.toLowerCase().trim()).append("') ");
                }else{
                    conditionSql.append(tableName).append(".USERID").append("='").append(conditionMap.get("userid").trim()).append("'");
                }

            }
            //通道号
            if (conditionMap.get("spgate") != null && conditionMap.get("spgate").trim().length() > 0)
            {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(tableName).append(".SPGATE='").append(conditionMap.get("spgate").trim()).append("'");
            }
            //运营商
            if (conditionMap.get("spisuncm") != null && conditionMap.get("spisuncm").trim().length() > 0)
            {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(tableName).append(".UNICOM=").append(conditionMap.get("spisuncm").trim());
            }
            //业务类型
            if (conditionMap.get("buscode") != null && conditionMap.get("buscode").trim().length() > 0)
            {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                //无业务类型
                if("-1".equals(conditionMap.get("buscode").trim()))
                {
                    conditionSql.append(" not exists (select BUS_CODE from LF_BUSMANAGER ").append(StaticValue.getWITHNOLOCK())
                                .append(" where BUS_CODE=").append(tableName).append(".SVRTYPE) ");
                }
                else
                {
                    conditionSql.append(tableName).append(".SVRTYPE='").append(conditionMap.get("buscode").trim()).append("'");
                }
            }
            //为多企业，且不是100000企业，且查询条件没选sp账号的情况下，则拼入绑定的发送账号
            if (StaticValue.getCORPTYPE() == 1 && !"100000".equals(conditionMap.get("lgcorpcode")) && (conditionMap.get("userid") == null || conditionMap.get("userid").trim().length() < 1))
            {
                String spusers = conditionMap.get("spUsers");
                if(spusers!=null){
                    //sp账号个数
                    int length = 0;
                    if(spusers.contains(","))
                    {
                        String[] useriday = spusers.split(",");
                        length = useriday.length;
                    }
                    hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                    //如果个数小于50则用in
                    if(length<50)
                    {
                        //如果是DB2或者oracle 则做兼容小写处理
                        if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE==StaticValue.ORACLE_DBTYPE
                                ||StaticValue.DBTYPE==StaticValue.DB2_DBTYPE)){
                            spusers = spusers.toUpperCase() + "," + spusers.toLowerCase();
                        }
                        String insqlstr = new ReciveBoxDao().getSqlStr(spusers, tableName + ".USERID");
                        conditionSql.append(" (" + insqlstr + ") ");
                    }
                    else
                    {
                        //如果是DB2或者oracle 则做兼容小写处理
                        if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE==StaticValue.ORACLE_DBTYPE
                                ||StaticValue.DBTYPE==StaticValue.DB2_DBTYPE)){
                            conditionSql.append(" exists (select SPUSER from lf_sp_dep_bind ").append(StaticValue.getWITHNOLOCK())
                            .append(" where upper(SPUSER)=upper(").append(tableName).append(".USERID) ");
                        }else{
                            conditionSql.append(" exists (select SPUSER from lf_sp_dep_bind ").append(StaticValue.getWITHNOLOCK())
                                        .append(" where SPUSER=").append(tableName).append(".USERID ");
                        }
                        if(conditionMap.get("lgcorpcode") != null && conditionMap.get("lgcorpcode").length() > 0)
                        {
                            conditionSql.append(" and CORP_CODE= '").append(conditionMap.get("lgcorpcode").trim()).append("' ");
                        }
                        conditionSql.append(") ");
                    }
                }
            }

            //错误码查询
            if (conditionMap.get("mterrorcode") != null && conditionMap.get("mterrorcode").trim().length() > 0)
            {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(tableName).append(".ERRORCODE='").append(conditionMap.get("mterrorcode")).append("'");
            }
            //操作员
            /*if(conditionMap.get("p1") != null && conditionMap.get("p1").trim().length() > 0)
            {
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(" exists (select USER_CODE from lf_sysuser ").append(StaticValue.WITHNOLOCK)
                            .append(" where USER_CODE=").append(tableName).append(".p1")
                            .append(" and (USER_NAME like '%").append(conditionMap.get("p1").trim()).append("%' or NAME like '%").append(conditionMap.get("p1").trim()).append("%') ");

                if(conditionMap.get("lgcorpcode") != null && conditionMap.get("lgcorpcode").trim().length() > 0)
                {
                    conditionSql.append(" and CORP_CODE= '").append(conditionMap.get("lgcorpcode").trim()).append("' ");
                }
                conditionSql.append(") ");
            }*/

            //控制数据权限
            StringBuffer domusercodesql=new StringBuffer("");
            //有权限查看的操作员编码为空字符串，则是admin或者是管辖顶级机构，不需要拼接p1条件
            if(conditionMap.get("domUsercode") != null && conditionMap.get("domUsercode").length() < 1)
            {
                //
            }
            //有权限查看的操作员编码有值，则需要拼入p1条件，以控制查询权限
            else if(conditionMap.get("domUsercode") != null && conditionMap.get("domUsercode").length() > 0)
            {
                //hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                //获取是否开启p2参数配置
                String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
                //获取不到配置文件值
                if(depcodethird2p2 == null || depcodethird2p2.trim().length() == 0)
                {
                    depcodethird2p2 = "false";
                }
                if(!"true".equals(depcodethird2p2)){
                    //hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                    domusercodesql.append(tableName).append(".p1 in (").append(conditionMap.get("domUsercode")).append(")");
                    //conditionSql.append(tableName).append(".p1 in(").append(conditionMap.get("domUsercode")).append(")");
                }else{
                    domusercodesql.append(" (").append(tableName).append(".p2 in (select DEP_CODE_THIRD from LF_DEP where DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=").append(conditionMap.get("curUserId")).append("))")
                    .append(" or ").append(tableName).append(".p1=").append(conditionMap.get("curUsercode")).append(")");
                }
            }
            //不能用已知权限，则使用语句，在操作员编码超过1000时使用，性能差
            else
            {
                //hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                //获取是否开启p2参数配置
                String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
                //获取不到配置文件值
                if(depcodethird2p2 == null || depcodethird2p2.trim().length() == 0)
                {
                    depcodethird2p2 = "false";
                }
                if("true".equals(depcodethird2p2)){
                    domusercodesql.append(" (").append(tableName).append(".p2 in (select DEP_CODE_THIRD from LF_DEP where DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=").append(conditionMap.get("curUserId")).append("))")
                    .append(" or ").append(tableName).append(".p1=").append(conditionMap.get("curUsercode")).append(")");
                }else{
                    domusercodesql.append(" (").append(tableName).append(".p1 in (select USER_CODE from LF_SYSUSER where DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=").append(conditionMap.get("curUserId")).append("))")
                    .append(" or ").append(tableName).append(".p1=").append(conditionMap.get("curUsercode")).append(")");
                }


                //conditionSql.append(" (").append(tableName).append(".p1 in (select USER_CODE from LF_SYSUSER where DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=").append(conditionMap.get("curUserId")).append("))")
                //.append(" or ").append(tableName).append(".p1=").append(conditionMap.get("curUsercode")).append(")");
            }

            StringBuffer spuserprisql=new StringBuffer("");
            //有权限查看的操作员编码为空字符串，则是admin或者是管辖顶级机构，不需要拼接p1条件
            if(conditionMap.get("spuserpri") != null && conditionMap.get("spuserpri").length() < 1)
            {
                //
            }
            //有权限查看的操作员编码有值，则需要拼入p1条件，以控制查询权限
            else if(conditionMap.get("spuserpri") != null && conditionMap.get("spuserpri").length() > 0)
            {
                spuserprisql.append(tableName).append(".USERID in (").append(conditionMap.get("spuserpri")).append(")");
            }
            //不能用已知权限，则使用语句，在操作员编码超过1000时使用，性能差
            else
            {
                spuserprisql.append(" ").append(tableName).append(".USERID in (select SPUSERID FROM LF_MT_PRI WHERE USER_ID=").append(conditionMap.get("spcurUserId")).append(" AND CORP_CODE=").append(conditionMap.get("spcurcorpcode")).append(")");
                //conditionSql.append(" (").append(tableName).append(".p1 in (select USER_CODE from LF_SYSUSER where DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=").append(conditionMap.get("curUserId")).append("))")
                //.append(" or ").append(tableName).append(".p1=").append(conditionMap.get("curUsercode")).append(")");
            }

            if(domusercodesql.length()!=0&&spuserprisql.length()!=0){
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(" ( ").append(domusercodesql).append(" or ").append(spuserprisql).append(" ) ");
            }else if(domusercodesql.length()!=0&&spuserprisql.length()==0){
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(domusercodesql);
            }else if(domusercodesql.length()==0&&spuserprisql.length()!=0){
                hasWhere = addWhereOrAnd(conditionSql, hasWhere);
                conditionSql.append(spuserprisql);
            }else{
                //如果都为空则不拼接条件
            }


            return conditionSql.toString();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "DAO查询下行记录，获取查询条件sql，异常。");
            return " where 1=2 ";
        }
    }

    /**
     * 获取查询时间对象
     * @param conditionMap 查询条件
     * @return 返回查询时间对象集合
     */
    public static List<String> getTimeCondition(LinkedHashMap<String, String> conditionMap)
    {
        try
        {
            List<String> timeList = new ArrayList<String>();
            if (conditionMap.get("sendtime") != null && conditionMap.get("sendtime").trim().length() > 0)
            {
                timeList.add(conditionMap.get("sendtime").trim());
            }
            if (conditionMap.get("recvtime") != null && conditionMap.get("recvtime").trim().length() > 0)
            {
                timeList.add(conditionMap.get("recvtime").trim());
            }
            return timeList;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "DAO查询，获取查询时间对象，异常。");
            return null;
        }
    }

    /**
     * 按情况拼接where或and
     * @param sql
     * @param hasWhere false为没where关键词
     * @return 第一次拼接后将会有where，直接返回true
     */
    private static boolean addWhereOrAnd(StringBuffer sql, boolean hasWhere)
    {
        //有where则加and
        if(hasWhere)
        {
            sql.append(" and ");
        }
        //没where则加where
        else
        {
            sql.append(" where ");
        }
        return true;
    }

}
