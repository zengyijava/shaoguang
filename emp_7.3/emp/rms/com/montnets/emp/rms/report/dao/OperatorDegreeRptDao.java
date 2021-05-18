package com.montnets.emp.rms.report.dao;

import java.util.List;

import com.montnets.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.rms.report.vo.MtDataReportVo;
import com.montnets.emp.util.PageInfo;

/**
 * Dao层
 *
 * @date 2018-6-14 16:27:12
 * @author Cheng
 */
public class OperatorDegreeRptDao extends SuperDAO {
    /**
     * 查找档位统计报表
     * @param vo 查询VO对象
     * @param pageInfo 分页对象，可为null
     * @return 包含查询结果的List集合
     */
    public List<MtDataReportVo> findOperatorDegreeRpt(MtDataReportVo vo, PageInfo pageInfo) throws Exception{
        IGenericDAO dao = new DataAccessDriver().getGenericDAO();
        List<MtDataReportVo> reportVos;
        try {
            if(vo == null || vo.getReportType() == null){
                throw new EMPException("企业富信>数据查询>档位统计报表>方法：findOperatorDegreeRpt 传入VO对象为空或ReportType值为空");
            }
            Integer reportType = vo.getReportType();
            //获取field
            String fieldSql = OperatorDegreeRptSql.getFieldSql();
            //获取表sql
            String tableSql = OperatorDegreeRptSql.getTableSql();
            //获取条件sql
            String conditionSql = OperatorDegreeRptSql.getConditonSql(vo);
            //获取分组sql
            String groupBySql = OperatorDegreeRptSql.getGroupbySql(reportType,vo.getDes());
            //获取排序Sql
            String orderBySql = OperatorDegreeRptSql.getOrderBySql(vo.getDes());
            //总sql
            String sql = fieldSql + tableSql + conditionSql + groupBySql + orderBySql;

            if(pageInfo == null){
                reportVos = findVoListBySQL(MtDataReportVo.class,sql,StaticValue.EMP_POOLNAME);
            }else {
                //分页
                String countSql = "select count(*) totalcount from (" + "select count(*) tcount" + tableSql + conditionSql + groupBySql + ") A";
                reportVos = dao.findPageVoListBySQL(MtDataReportVo.class,sql,countSql,pageInfo,StaticValue.EMP_POOLNAME);
            }
            return reportVos;
        }catch (EMPException empex){
            EmpExecutionContext.error(empex,empex.getMessage());
            throw empex;
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业富信>数据查询>档位统计报表>方法：findOperatorDegreeRpt 执行异常");
            throw e;
        }
    }
}
