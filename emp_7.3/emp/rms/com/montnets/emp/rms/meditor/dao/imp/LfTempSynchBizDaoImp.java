package com.montnets.emp.rms.meditor.dao.imp;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.rms.meditor.dao.LfTempSynchBizDao;
import com.montnets.emp.rms.meditor.entity.LfTempSynch;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LfTempSynchBizDaoImp extends SuperDAO implements LfTempSynchBizDao {

    IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
    IEmpDAO empDAO = new DataAccessDriver().getEmpDAO();

    Integer sycIntevalTime = SystemGlobals.getIntValue("montnets.rcos.template.syn.retry", 300);
    Integer sycCounts = SystemGlobals.getIntValue("montnets.rcos.template.syn.counts", 3);

    @Override
    public List<LfTempSynch> findSynFailTemp(int isMaterial) {

        //更新时间+设定间隔时间<当前时间

        StringBuffer fieldSql = new StringBuffer("SELECT * FROM ");
        StringBuffer tableSql = new StringBuffer(" LF_TEMP_SYNCH ");
        StringBuffer conditionSql = new StringBuffer();

        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.set(Calendar.SECOND, -sycIntevalTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = sdf.format(calendar.getTime());
        String conditionTime = genericDAO.getTimeCondition(updateTime);

        conditionSql.append(" AND SYNSTATUS=0")
                .append(" AND ISMATERIAL="+isMaterial)
                .append(" AND COUNT<").append(sycCounts)
                .append(" AND UPDATE_TIME<").append(conditionTime);

        String conditionSqlEnd = getConditionSql(String.valueOf(conditionSql));
        String sql = String.valueOf(fieldSql.append(tableSql)) + conditionSqlEnd;

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(Integer.MAX_VALUE);
        List<LfTempSynch> lfTempSynches = null;
        try {
            lfTempSynches = genericDAO.findPageEntityListBySQLNoCount(LfTempSynch.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询同步失败还可以重试的模板信息异常");
        }

        return lfTempSynches;
    }

    public static String getConditionSql(String conSql) {
        String conditionSql = "";
        try {
            //存在查询条件
            if (conSql != null && conSql.length() > 0) {
                //将条件字符串首个and替换为where,不允许1 =1方式
                conditionSql = conSql.replaceFirst("^(\\s*)(?i)and", "$1where");
            }
            return conditionSql;
        } catch (Exception e) {
            EmpExecutionContext.error("处理SQL条件异常，conSql:" + conSql);
            return null;
        }
    }

    @Override
    public long findMaxFailTempId(String isMaterial,Integer synCounts) {
        String sql = "select max(SP_TEMPLID) as SP_TEMPLID from lf_temp_synch where SYNSTATUS = 1 and ISMATERIAL = "+isMaterial;
        try {
            List<DynaBean> lists = genericDAO.findDynaBeanBySql(sql);
            if(lists != null && lists.size() > 0){
                String sp_templid = lists.get(0).get("sp_templid") == null ? "0" : lists.get(0).get("sp_templid").toString();
                return  Long.parseLong(sp_templid);
            }
        } catch (Exception e) {
           EmpExecutionContext.error(e,"读取lf_temp_synch表同步当前最大ID值出现异常！");
        }
        return  0;
    }
}
