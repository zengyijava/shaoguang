package com.montnets.emp.reportform.dao.impl;

import com.montnets.emp.common.biz.SpUserBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.report.AprovinceCity;
import com.montnets.emp.entity.selfparam.LfWgParmDefinition;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.reportform.bean.ReportVo;
import com.montnets.emp.reportform.cxtjenum.JumpPathEnum;
import com.montnets.emp.reportform.dao.IReportDao;
import com.montnets.emp.reportform.sql.ReportMtDataRptSql;
import com.montnets.emp.table.report.TableAprovinceCity;
import com.montnets.emp.table.selfparam.TableLfWgParamConfig;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 查询统计报表 Dao 层实现类
 *
 * @author lianghuageng
 * @date 2018/12/10 17:48
 */
public class ReportDaoImpl extends SuperDAO implements IReportDao {

    private final IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
    private final IEmpDAO empDAO = new DataAccessDriver().getEmpDAO();
    private final SpUserBiz spUserBiz = new SpUserBiz();

    @Override
    public List<AprovinceCity> getProvinceAndCity() {
        //定义区域集合
        List<AprovinceCity> aProvinceCities = new ArrayList<AprovinceCity>();
        try {
            String sql = "SELECT " + TableAprovinceCity.ID + "," + TableAprovinceCity.PROVINCE + "," +
                    TableAprovinceCity.CITY + "," + TableAprovinceCity.AREACODE + "," +
                    TableAprovinceCity.PROVINCECODE + " FROM " + TableAprovinceCity.TABLE_NAME;
            aProvinceCities = findEntityListBySQL(AprovinceCity.class, sql, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> getProvinceAndCity() 从数据库中获取省份信息异常");
        }
        return aProvinceCities;
    }

    @Override
    public List<LfDep> getDepByDomination(LinkedHashMap<String, String> conditionMap) {
        List<LfDep> lfDeps = new ArrayList<LfDep>();
        try {
            String corpCodeSql = StringUtils.isEmpty(conditionMap.get("corpCode")) ? "" : " AND " + TableLfDep.CORP_CODE + " = '" + conditionMap.get("corpCode") + "' ";
            String sql = "SELECT * from " + TableLfDep.TABLE_NAME + " WHERE " + TableLfDep.DEP_STATE + " = 1 AND " +
                    TableLfDep.DEP_ID + " IN (SELECT " + TableLfDomination.DEP_ID + " FROM " + TableLfDomination.TABLE_NAME
                    + " WHERE " + TableLfDomination.USER_ID + " = " + conditionMap.get("userId") + ")" + corpCodeSql + " ORDER BY " + TableLfDep.DEP_LEVEL;
            lfDeps = findEntityListBySQL(LfDep.class, sql, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> getDepByDomination() 从数据库中获取机构信息异常");
        }
        return lfDeps;
    }

    @Override
    public List<LfSysuser> findSysUserByDepIds(String depIds) {
        List<LfSysuser> lfSysusers = new ArrayList<LfSysuser>();
        try {
            String sql = "SELECT * FROM " + TableLfSysuser.TABLE_NAME + " WHERE " + TableLfSysuser.USER_ID + " <> 1 AND " + TableLfSysuser.DEP_ID + " IN (" + depIds + ")";
            lfSysusers = findEntityListBySQL(LfSysuser.class, sql, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> findSysUserByDepIds() 从数据库中获取操作员信息异常");
        }
        return lfSysusers;
    }

    @Override
    public List<ReportVo> findMtDataRptByModuleName(LfSysuser sysUser, ReportVo queryEntity, String module, PageInfo page) {
        List<ReportVo> reportVos = new ArrayList<ReportVo>();
        try {
            if (JumpPathEnum.areaReport.getUrl().equals(module)) {
                //区域统计报表
                reportVos = getAreaReportResultList(queryEntity, page, module, sysUser);
            } else if (JumpPathEnum.busReport.getUrl().equals(module)) {
                //业务类型统计报表
                reportVos = getBusReportResultList(queryEntity, page, module, sysUser);
            } else if (JumpPathEnum.dynParamReport.getUrl().equals(module)) {
                //自定义参数统计报表
                reportVos = getDynParamReportResultList(queryEntity, page, module, sysUser);
            } else if (JumpPathEnum.spisuncmMtReport.getUrl().equals(module)) {
                //运营商统计报表
                reportVos = getSpisuncmMtReportResultList(queryEntity, page, module);
            } else if (JumpPathEnum.spMtReport.getUrl().equals(module)) {
                //SP账号统计报表
                //获取当前操作员可查看的SP账号
                queryEntity.setCurrentSpUser(spUserBiz.getAllSpUser(sysUser.getUserId().toString()));
                StringBuilder spUserIdSb = new StringBuilder();
                //遍历,查询SP账号
                //如果是托管版的10W号
                String sysAdminId = "2";
                if(StaticValue.getCORPTYPE() == 1 && sysAdminId.equals(sysUser.getUserId())){
                    spUserIdSb.append(queryEntity.getSpUserId());
                    spUserIdSb.append(",");
                }
                else if(StringUtils.isEmpty(queryEntity.getSpUserId())){
                    //如果条件为空,则查询所有的
                    spUserIdSb.append(queryEntity.getCurrentSpUser()).append(",");
                }else{
                    for(String tempCurrentSP:queryEntity.getCurrentSpUser().split(",")){
                        for(String tempSP:queryEntity.getSpUserId().split(",")){
                            tempSP = "'"+tempSP+"'";
                            if(tempCurrentSP.equalsIgnoreCase(tempSP)){
                                spUserIdSb.append(tempCurrentSP);
                                spUserIdSb.append(",");
                            }
                        }
                    }
                }
                String realSpUser = spUserIdSb.toString();
                if(StringUtils.isNotEmpty(realSpUser)){
                    realSpUser = realSpUser.substring(0,realSpUser.lastIndexOf(","));
                }
                queryEntity.setCurrentSpUser(realSpUser);
                reportVos = getSpMtReportResultList(queryEntity, page, module, sysUser);
            } else if (JumpPathEnum.sysUserReport.getUrl().equals(module)) {
                //操作员统计报表
                reportVos = getSysUserReportResultList(queryEntity, page, module, sysUser);
            } else if (JumpPathEnum.sysDepReport.getUrl().equals(module)) {
                // 机构统计报表
                reportVos = getSysDeptReportResultList(queryEntity, page, module, sysUser);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> 查询统计 > " + JumpPathEnum.valueOf(module).getName() + "查询异常！方法：findMtDataRptByModuleName() ");
        }
        return reportVos;
    }

    /**
     * 机构统计报表查询
     *
     * @param queryEntity 查询条件实体
     * @param page        分页
     * @param module      对应模块
     * @param sysUser     当前登录用户
     * @return 查询结果集
     */
    private List<ReportVo> getSysDeptReportResultList(ReportVo queryEntity, PageInfo page, String module, LfSysuser sysUser) {
        if (null != sysUser) {
            ReportSQL reportSQL = getReportSQL(queryEntity, sysUser);
            if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
                reportSQL.setSql(reportSQL.getSql().replaceAll("LENGTH\\(","LEN\\(").replaceAll("SUBSTR\\(","SUBSTRING\\("));
                reportSQL.setCountSql(reportSQL.getCountSql().replaceAll("LENGTH\\(","LEN\\(").replaceAll("SUBSTR\\(","SUBSTRING\\("));
            }
            return getReportVos(reportSQL.getSql(), reportSQL.getCountSql(), page, module);
        }
        return null;
    }

    /**
     * 获取报表SQl
     * @param queryEntity
     * @param sysUser
     * @return
     */
    private ReportSQL getReportSQL(ReportVo queryEntity, LfSysuser sysUser) {
        String sql = null;
        final String sql1 = ReportMtDataRptSql.getSysDeptRptSql1(queryEntity, sysUser);
        final String sql2 = ReportMtDataRptSql.getSysDeptRptSql2(queryEntity, sysUser);
        String sql3 = "";
        final Integer corpCode = Integer.parseInt(sysUser.getCorpCode());
        if (ReportMtDataRptSql.isTopLevelDept(corpCode, sysUser.getDepId())) {
            sql3 = " UNION ALL " + ReportMtDataRptSql.getSysDeptRptSql3(queryEntity, sysUser);
        }
        final Integer idType = queryEntity.getIdtype();
        if (queryEntity.isDetail() && null != idType) {
            switch (idType) {
                case 1:
                    sql = sql1 + " UNION ALL " + sql2 + sql3;
                    break;
                case 2:
                    sql = sql2;
                    break;
                case 3:
                    sql = sql3;
                    break;
                default:
            }
        } else if(queryEntity.isContainSubDep()) {
            // 包含子机构
            sql = sql1 + " UNION ALL " + sql2 + sql3;
        } else {
            // 不包含子机构
            sql = sql2 + sql3;
        }
        String countSql = "select count(*) totalcount from (" + sql + ") A";
        return new ReportSQL(sql, countSql);
    }


    private List<ReportVo> getSysUserReportResultList(ReportVo queryEntity, PageInfo page, String module, LfSysuser sysUser) {
        String fieldSql = ReportMtDataRptSql.getSysUserReportFieldSql();
        String tableSql = ReportMtDataRptSql.getSysUserReportTableSql(queryEntity.getMstype());
        String conditionSql = ReportMtDataRptSql.getSysUserReportConditionSql(queryEntity, sysUser);
        String groupBySql = ReportMtDataRptSql.getSysUserReportGroupSql(queryEntity.getReportType(), queryEntity.isDetail());
        String sql = fieldSql + tableSql + conditionSql + groupBySql;
        String countSql = "select count(*) totalcount from (" + sql + ") A";
        return getReportVos(sql, countSql, page, module);
    }

    private List<ReportVo> getSpMtReportResultList(ReportVo queryEntity, PageInfo page, String module, LfSysuser sysUser) {
        String fieldSql = ReportMtDataRptSql.getSpMtRptFieldSql();
        String tableSql = ReportMtDataRptSql.getSpMTRptTableSql(queryEntity.getMstype());
        String conditionSql = ReportMtDataRptSql.getSpMTRptConditionSql(queryEntity, sysUser);
        String groupBySql = ReportMtDataRptSql.getSpMTRptGroupSql(queryEntity.getReportType(), queryEntity.isDetail());
        String orderSql = ReportMtDataRptSql.getSpMTRptOrderSql();
        String sql = fieldSql + tableSql + conditionSql + groupBySql + orderSql;
        String countSql = "select count(*) totalcount from (" + fieldSql + tableSql + conditionSql + groupBySql + ") A";
        return getReportVos(sql, countSql, page, module);
    }

    private List<ReportVo> getSpisuncmMtReportResultList(ReportVo queryEntity, PageInfo page, String module) {
        String fieldSql = ReportMtDataRptSql.getSpisuncmMtRptFieldSql();
        String tableSql = ReportMtDataRptSql.getSpisuncmRptTableSql(queryEntity.getMstype());
        String conditionSql = ReportMtDataRptSql.getSpisuncmRptConditionSql(queryEntity);
        String groupBySql = ReportMtDataRptSql.getSpisuncmRptGroupSql(queryEntity.getReportType(), queryEntity.isDetail());
        String sql = fieldSql + tableSql + conditionSql + groupBySql;
        String countSql = "select count(*) totalcount from (" + sql + ") A";
        return getReportVos(sql, countSql, page, module);
    }

    private List<ReportVo> getDynParamReportResultList(ReportVo queryEntity, PageInfo page, String module, LfSysuser sysUser) {
        String fieldSql = ReportMtDataRptSql.getDynParamRptFieldSql(queryEntity.getParam(), Integer.parseInt(queryEntity.getParamNum()));
        String tableSql = ReportMtDataRptSql.getDynParamRptTableSql();
        String conditionSql = ReportMtDataRptSql.getDynParamConditionSql(queryEntity, sysUser, new StringBuilder());
        String groupBySql = ReportMtDataRptSql.getDynParamGroupSql(queryEntity.getParam(), Integer.parseInt(queryEntity.getParamNum()));
        String sql = fieldSql + tableSql + conditionSql + groupBySql;
        //在外部再拼接处理一次
        sql = handleDynParamReport(sql, queryEntity.getParam(), Integer.parseInt(queryEntity.getParamNum()));
        String countSql = "select count(*) totalcount from (" + sql + ") A";
        return getReportVos(sql, countSql, page, module);
    }

    private List<ReportVo> getBusReportResultList(ReportVo queryEntity, PageInfo page, String module, LfSysuser sysUser) {
        String fieldSql = ReportMtDataRptSql.getBusRptFieldSql();
        String tableSql = ReportMtDataRptSql.getBusRptTableSql(queryEntity.getMstype(), sysUser);
        String conditionSql = ReportMtDataRptSql.getBusRptConditionSql(queryEntity, sysUser);
        String groupBySql = ReportMtDataRptSql.getBusRptGroupSql(queryEntity.getReportType(), queryEntity.isDetail());
        //总sql
        String sql = fieldSql + tableSql + conditionSql + groupBySql;
        String countSql = "select count(*) totalcount from (" + "select count(*) tcount" + tableSql + conditionSql + groupBySql + ") A";
        return getReportVos(sql, countSql, page, module);
    }

    private List<ReportVo> getReportVos(String sql, String countSql, PageInfo page, String module) {
        List<ReportVo> reportVos = new ArrayList<ReportVo>();
        //将查询出的动态bean转换为对应的VO
        List<DynaBean> dynaBeans = getDynaBeanListBySql(sql, countSql, page);
        if (null != dynaBeans && dynaBeans.size() > 0) {
            reportVos = dybean2ReportVo(dynaBeans, module);
        }
        return reportVos;
    }

    private List<ReportVo> getAreaReportResultList(ReportVo queryEntity, PageInfo page, String module, LfSysuser sysUser) {
        String fieldSql = ReportMtDataRptSql.getAreaRptFieldSql();
        String tableSql = ReportMtDataRptSql.getAreaRptTableSql(queryEntity.getMstype());
        String conditionSql = ReportMtDataRptSql.getAreaRptConditionSql(queryEntity, sysUser);
        String groupBySql = ReportMtDataRptSql.getAreaRptGroupSql(queryEntity.getReportType(), queryEntity.isDetail());
        //总sql
        String sql = fieldSql + tableSql + conditionSql + groupBySql;
        String countSql = "select count(*) totalcount from (" + "select count(*) tcount" + tableSql + conditionSql + groupBySql + ") A";
        return getReportVos(sql, countSql, page, module);
    }

    private String handleDynParamReport(String sql, String param, int paramNum) {
        return "SELECT T1.ICOUNT,T1.RSUCC,T1.RFAIL1,T1.RFAIL2," +
                "T1.RNRET,PARAMCONF.PARAMVALUE,PARAMCONF.PARAMNAME FROM (" + sql +
                ") T1 LEFT JOIN " + TableLfWgParamConfig.TABLE_NAME + " PARAMCONF ON T1.PA = PARAMCONF." + TableLfWgParamConfig.PARAMVALUE +
                " WHERE ( PARAMCONF." + TableLfWgParamConfig.PARAM + " = '" + param + "' OR PARAMCONF." + TableLfWgParamConfig.PARAM + " IS NULL ) " +
                "AND ( PARAMCONF." + TableLfWgParamConfig.PARAMSUBNUM + " = " + paramNum + " OR PARAMCONF." + TableLfWgParamConfig.PARAMSUBNUM + " IS NULL)";
    }

    /**
     * 利用反射将动态bean转换为对应的VO
     *
     * @param dybeans 查询出的动态bean List集合
     * @param module  模块
     * @return 对应的vo集合
     */
    private List<ReportVo> dybean2ReportVo(List<DynaBean> dybeans, String module) {
        List<ReportVo> reportVos = new ArrayList<ReportVo>();
        try {
            Class cls = ReportVo.class;
            for (DynaBean bean : dybeans) {
                ReportVo reportVo = new ReportVo();
                //遍历所有查询出的字段
                String[] colList = JumpPathEnum.valueOf(module).getColName().split(",");
                for (String col : colList) {
                    //反射获取ReportVo类的所有字段，当相等时利用反射赋值
                    Field[] fields = cls.getDeclaredFields();
                    for (Field field : fields) {
                        String fieldType = field.getType().getSimpleName();
                        String fieldName = field.getName();
                        String fieldNameUpper = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                        if (col.equals(fieldName)) {
                            col = col.toLowerCase();
                            if ("String".equals(fieldType)) {
                                Method setMethod = cls.getMethod("set" + fieldNameUpper, String.class);
                                setMethod.invoke(reportVo, bean.get(col) == null ? null : bean.get(col).toString());
                            }
                            if ("Long".equals(fieldType)) {
                                Method setMethod = cls.getMethod("set" + fieldNameUpper, Long.class);
                                setMethod.invoke(reportVo, bean.get(col) == null ? null : Long.parseLong(bean.get(col).toString()));
                            }
                            if ("Integer".equals(fieldType)) {
                                Method setMethod = cls.getMethod("set" + fieldNameUpper, Integer.class);
                                setMethod.invoke(reportVo, bean.get(col) == null ? null : Integer.parseInt(bean.get(col).toString()));
                            }
                            break;
                        }
                    }
                }
                reportVos.add(reportVo);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> 查询统计 > " + JumpPathEnum.valueOf(module).getName() + "查询异常！方法：dybean2ReportVo() ");
        }
        return reportVos;
    }

    @Override
    public List<LfWgParmDefinition> getLfWgParmConfList(LinkedHashMap<String, String> conditionMap, String module) {
        List<LfWgParmDefinition> lfWgParmDefinitions = new ArrayList<LfWgParmDefinition>();
        try {
            lfWgParmDefinitions = empDAO.findListBySymbolsCondition(LfWgParmDefinition.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> 查询统计 > " + JumpPathEnum.valueOf(module).getName() + "查询异常！方法：dybean2ReportVo() ");
        }
        return lfWgParmDefinitions;
    }

    private List<DynaBean> getDynaBeanListBySql(String sql, String countSql, PageInfo page) {
        List<DynaBean> dybeans;
        //去掉Where 1=1
        sql = sql.replaceAll("WHERE\\s+1=1\\s+(AND)?", "WHERE");
        if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
            sql = sql.replaceAll("LENGTH\\(","LEN\\(").replaceAll("SUBSTR\\(","SUBSTRING\\(");
            countSql = countSql.replaceAll("LENGTH\\(","LEN\\(").replaceAll("SUBSTR\\(","SUBSTRING\\(");
        }
        if (page != null) {
            dybeans = genericDAO.findPageDynaBeanBySQL(sql, countSql, page, StaticValue.EMP_POOLNAME, null);
        } else {
            dybeans = genericDAO.findDynaBeanBySql(sql);
        }
        return dybeans;
    }

    /**
     * 获取短彩信运营商账号
     *
     * @param type:0代表短信，1代表短信
     * @return 集合
     */
    public List<DynaBean> getListByMsType(Integer type) {
        String sql;
        // 测试人员如果用默认账号发，spid就会为空格，页面显示会多一行空白，这里加上去空格ltrim限制
        if (type == 0) {
            sql = "select t.spid spid from mt_datareport t where ltrim(t.spid) != '' group by t.spid";
        } else {
            sql = "select t.spid spid from MMS_DATAREPORT t where ltrim(t.spid) != '' group by t.spid";
        }
        return getListDynaBeanBySql(sql);
    }

    /**
     * 根据操作员Id获取其管辖机构
     *
     * @param userId 操作员Id
     * @return 机构list集合
     */
    @Override
    public List<LfDep> getDominationByUserId(Long userId) {
        List<LfDep> lfDeps = null;
        try {
            String sql = "SELECT * from lf_dep dep where DEP_ID in (SELECT DEP_ID from LF_DOMINATION DOMINATION WHERE USER_ID = " + userId + ") ORDER BY DEP_LEVEL";
            lfDeps = findEntityListBySQL(LfDep.class, sql, StaticValue.EMP_POOLNAME);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> 查询统计 根据操作员Id获取其管辖机构异常！方法：getDominationByUserId() ");
        }
        return lfDeps;
    }

    /**
     * 根据机构Id找到下级子机构
     *
     * @param depId 机构Id
     * @return 集合
     */
    @Override
    public List<LfDep> getChildrenDepById(String depId) {
        List<LfDep> lfDeps = null;
        try {
            String sql = "SELECT * from lf_dep WHERE SUPERIOR_ID = " + depId;
            lfDeps = findEntityListBySQL(LfDep.class, sql, StaticValue.EMP_POOLNAME);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> 查询统计 根据机构Id找到下级子机构异常！方法：getChildrenDepById() ");
        }
        return lfDeps;
    }

    /**
     * 获取指定机构下的操作员
     *
     * @param depId 机构Id
     * @return 集合
     */
    @Override
    public List<LfSysuser> getSysUserById(Long depId) {
        List<LfSysuser> lfSysusers = null;
        try {
            String sql = "SELECT * FROM LF_SYSUSER WHERE USER_STATE = 1 AND DEP_ID = " + depId;
            lfSysusers = findEntityListBySQL(LfSysuser.class, sql, StaticValue.EMP_POOLNAME);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> 查询统计 获取指定机构下的操作员异常！方法：getSysUserById() ");
        }
        return lfSysusers;
    }
}
