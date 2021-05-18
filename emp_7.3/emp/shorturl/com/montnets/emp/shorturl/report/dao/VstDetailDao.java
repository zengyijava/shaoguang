package com.montnets.emp.shorturl.report.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.shorturl.report.entity.VstDetail;
import com.montnets.emp.shorturl.report.util.DBConnectionUtil;
import com.montnets.emp.shorturl.report.vo.BatchVisitVo;
import com.montnets.emp.shorturl.report.vo.VstDetailVo;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VstDetailDao extends SuperDAO{

    private SurlAccessStatisticsSql statisticsSql = new SurlAccessStatisticsSql();

    public HashMap<String,Integer> findVisitCountBySQL(Long taskId, Timestamp sendTime){
        //因为短链有效期最长一个月，因此要联合发送当月跟下月一起查询
        HashMap<String,Integer> map = new HashMap<String, Integer>();
        Integer visitCount = 0;
        Integer visitorCount = 0;
        String yyyyMM = new SimpleDateFormat("yyyyMM").format(sendTime.getTime());
        String visitAmountSql = statisticsSql.getVisitCountSql(taskId,yyyyMM);
        try {
            List<DynaBean> beans = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(visitAmountSql);
            if( beans != null && beans.size() > 0){
                for(DynaBean bean:beans){
                    visitCount = Integer.valueOf(bean.get("visitcount").toString());
                    visitorCount = Integer.valueOf(bean.get("visitorcount").toString());
                }
            }
            map.put("visitCount",visitCount);
            map.put("visitorCount", visitorCount);

        }catch (Exception e){
            EmpExecutionContext.error(e,"企业短链-批次访问统计-访问详情查询，获取访问人数与访问次数。查询VstDetail表，数据库异常");
        }
        return map;
    }

    public List<VstDetailVo> getAllVstDetail(VstDetailVo vstDetailVo,PageInfo pageInfo) throws Exception {
        List<VstDetailVo> vstDetailVos = new ArrayList<VstDetailVo>();

        String sql = SurlAccessStatisticsSql.getAllVisitedSql(vstDetailVo);

        String countSql = "select count(*) totalcount" + " from (" + sql + ") totalcount";

        //调用 查询方法
        IGenericDAO dao = new DataAccessDriver().getGenericDAO();
        if(pageInfo != null){
            vstDetailVos = dao.findPageVoListBySQLNoCount(
                    VstDetailVo.class, sql, countSql, pageInfo,
                    StaticValue.EMP_POOLNAME);
        }else {
            vstDetailVos = findVoListBySQL(VstDetailVo.class, sql,StaticValue.EMP_POOLNAME);
        }
        return vstDetailVos;
    }

    public List<VstDetailVo> findVisitedDetail(VstDetailVo vstDetailVo, PageInfo pageInfo) throws Exception {
        List<VstDetailVo> vstDetailVos = new ArrayList<VstDetailVo>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        //根据批次发送时间获取表名
        String vstDetailTableName = "VST_DETAIL" + sdf.format(vstDetailVo.getSendTime().getTime());
        //分别获取已访问的sql
        String hasVisitedSql = SurlAccessStatisticsSql.getHasVisitedSql(vstDetailVo.getTaskId(),vstDetailTableName);

        String conditionSql = SurlAccessStatisticsSql.getVstDetailConditionSql(vstDetailVo);
        //将第一个where改为and
        conditionSql = conditionSql.replaceAll("WHERE","and");
        String sql = hasVisitedSql + conditionSql;

        String countSql = "select count(*) totalcount" + " from (" + sql + ") totalcount";

        //调用 查询方法
        IGenericDAO dao = new DataAccessDriver().getGenericDAO();
        if(pageInfo != null){
            vstDetailVos = dao.findPageVoListBySQLNoCount(
                    VstDetailVo.class, sql, countSql, pageInfo,
                    StaticValue.EMP_POOLNAME);
        }else {
            vstDetailVos = findVoListBySQL(VstDetailVo.class, sql,StaticValue.EMP_POOLNAME);
        }
        return vstDetailVos;
    }
}

