package com.montnets.emp.shorturl.report.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.shorturl.report.vo.BatchVisitVo;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SurlAccessStatisticsDao extends SuperDAO{

    /**
     * 分页
     * @param batchVisitVo
     * @param pageInfo
     * @return
     * @throws Exception
     */
    public List<BatchVisitVo>   findBatchVisitVo(BatchVisitVo batchVisitVo, PageInfo pageInfo) throws Exception {
        List<BatchVisitVo> batchVisitVos = new ArrayList<BatchVisitVo>();
        //查询字段拼接
        String fieldSql = SurlAccessStatisticsSql.getBatchVisitFieldSql();
        //查询表拼接
        String tableSql = SurlAccessStatisticsSql.getBatchVisitTableSql();

        String dominationSql = "";

        if(StringUtils.isEmpty(batchVisitVo.getDepIds()) && StringUtils.isEmpty(batchVisitVo.getUserIds())){
            //如果登录操作员不是管理员，则进行管辖权限控制。因为接入类短信有可能没有操作员ID。
            //标准版的admin用户 与 托管版非10W号的非admin用户
            if(!"admin".equals(batchVisitVo.getCurrUserName()) && (StaticValue.getCORPTYPE() == 1 && !"100000".equals(batchVisitVo.getCurrCorpCode()))) {
                //判断当前操作员的数据权限
                if(batchVisitVo.getCurrUserDataPri() == 2){
                    //管辖范围拼接 机构权限
                    dominationSql = SmstaskSql.getDominationSql(batchVisitVo.getCurrUserId());
                }else {
                    //个人权限
                    dominationSql = SmstaskSql.getDominationSql2(batchVisitVo.getCurrUserId());
                }
            }
        }

        if(!StringUtils.isEmpty(batchVisitVo.getDepIds())) {
            //包含子机构
            if(!StringUtils.isEmpty(batchVisitVo.getIsContainsSun()) && "1".equals(batchVisitVo.getIsContainsSun())){
                LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(batchVisitVo.getDepIds()));
                if(lfDep != null && lfDep.getDepLevel() == 1){
                    if(StaticValue.getCORPTYPE() == 1){
                        //多企业
                        batchVisitVo.setDepIds("");
                        batchVisitVo.setCorpCode(lfDep.getCorpCode());
                    }else{
                        //单企业
                        batchVisitVo.setDepIds("");
                        batchVisitVo.setUserId(0L);
                    }
                }else{
                    String depid= new DepDAO().getChildUserDepByParentID(Long.parseLong(batchVisitVo.getDepIds()), TableLfDep.DEP_ID);
                    batchVisitVo.setDepIds(depid);
                }
                //不包含子机构
            }else{
                String depIdCondition= TableLfDep.DEP_ID+"="+batchVisitVo.getDepIds();
                batchVisitVo.setDepIds(depIdCondition);
            }
        }

        //组装过滤条件
        String conditionSql = SurlAccessStatisticsSql.getConditionSql(batchVisitVo);
        //组装排序条件
        String orderBySql = SmstaskSql.getOrderBySql();
        //处理后的dominationSql + conditionSql条件
        String dominationSqlStr = conditionSql + dominationSql;
        //组装SQL语句
        String sql = fieldSql + tableSql + dominationSqlStr + orderBySql;
        //组装总条数
        String countSql = "select count(*) totalcount" + tableSql + dominationSqlStr;
        //调用 查询方法
        IGenericDAO dao = new DataAccessDriver().getGenericDAO();
        if(pageInfo != null){
            batchVisitVos = dao.findPageVoListBySQLNoCount(BatchVisitVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
        }else {
            batchVisitVos = findVoListBySQL(BatchVisitVo.class, sql, StaticValue.EMP_POOLNAME);
        }
        //返回结果
        return batchVisitVos;
    }
}
