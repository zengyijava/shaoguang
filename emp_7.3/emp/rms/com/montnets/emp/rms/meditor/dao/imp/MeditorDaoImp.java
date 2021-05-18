package com.montnets.emp.rms.meditor.dao.imp;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTmplRela;
import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.meditor.base.BaseConfig;
import com.montnets.emp.rms.meditor.config.LfTemplateConfig;
import com.montnets.emp.rms.meditor.dao.MeditorDao;
import com.montnets.emp.rms.meditor.dto.PageListDto;
import com.montnets.emp.rms.meditor.entity.LfHfive;
import com.montnets.emp.rms.meditor.entity.LfTempContent;
import com.montnets.emp.rms.meditor.table.TableLfSubTemplate;
import com.montnets.emp.rms.meditor.tools.UserUtil;
import com.montnets.emp.rms.meditor.vo.TempHFiveVo;
import com.montnets.emp.rms.meditor.vo.TempsVo;
import com.montnets.emp.table.template.TableLfTemplate;
import com.montnets.emp.table.template.TableLfTmplRela;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class MeditorDaoImp extends SuperDAO implements MeditorDao {
    UserUtil userUtil = new UserUtil();
    IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();

    @Override
    public PageListDto getTemps(TempsVo tempsVo, HttpServletRequest request, HttpServletResponse response) throws Exception {


        //字段sql
        StringBuffer fieldSql = new StringBuffer("select distinct ");//为了关联分享模板group by，模板共享不让重新设计
        fieldSql.append(TableLfSubTemplate.TABLE_NAME).append(".").append(TableLfSubTemplate.TMP_TYPE).append(",")
                .append(TableLfTemplate.TM_NAME).append(",")
                .append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.TM_ID).append(",")
                .append(TableLfTemplate.USECOUNT).append(",")
                .append(TableLfSubTemplate.TABLE_NAME).append(".").append(TableLfSubTemplate.DEGREE_SIZE).append(",")
                .append(TableLfSubTemplate.TABLE_NAME).append(".").append(TableLfSubTemplate.DEGREE).append(",")
                .append(TableLfTemplate.DS_FLAG).append(",")
                .append(TableLfTemplate.USER_ID).append(",")
                .append(TableLfTemplate.ADD_TIME).append(",")
                .append(TableLfTemplate.TM_STATE).append(",")
                .append(TableLfTemplate.AUDITSTATUS).append(",")
                .append(TableLfTemplate.ISSHORTTEMP).append(",")
                .append(TableLfTemplate.SP_TEMPLID).append(",")
                .append(TableLfSubTemplate.CONTENT).append(",")
                .append(TableLfTemplate.USEID).append(",")
                .append(TableLfTemplate.INDUSTRYID).append(",")
                .append(TableLfSubTemplate.CARD_HTML).append(",")
                .append(TableLfSubTemplate.FILEURL).append(",")
                .append(TableLfSubTemplate.H5TYPE).append(",")
                .append(TableLfSubTemplate.H5URL).append(",")
                .append(TableLfTemplate.TM_MSG).append(",")
                .append(TableLfTemplate.VER).append(",")
                .append(TableLfTemplate.SOURCE).append(",")
                .append(TableLfTemplate.ISMATERIAL);

        //表sql  LF_TEMPLATE  LF_TEMP_PARAM  LF_SUB_TEMPLATE
        StringBuffer tableSql = new StringBuffer(" from ");
        tableSql.append(TableLfTemplate.TABLE_NAME)
                .append(" left join ")
                .append(TableLfSubTemplate.TABLE_NAME)
                .append(" on ")
                .append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.TM_ID).append("=").append(TableLfSubTemplate.TABLE_NAME).append(".").append(TableLfSubTemplate.TM_ID);
        //.append(" left join ")
        //.append(TableLfTempParam.TABLE_NAME)
        //.append(" on ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.TM_ID).append("=").append(TableLfTempParam.TABLE_NAME).append(".").append(TableLfTempParam.TM_ID);
        //是否包含共享模板
        if (tempsVo.getContainShare() == 1) {
            tableSql.append(" left join ")
                    .append(TableLfTmplRela.TABLE_NAME)
                    .append(" on ")
                    .append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.TM_ID).append(" = ").append(TableLfTmplRela.TABLE_NAME).append(".").append(TableLfTmplRela.templId);
        }
        //条件sql
        StringBuffer conditionSql = new StringBuffer(" where ");
        conditionSql.append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.TMP_TYPE).append(" IN (11,12,13,15) AND ");
        conditionSql.append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.TM_STATE).append(" != 3 AND ");
        //模板库列表 rcos删除了emp不显示 0启动 1禁用 2删除
        conditionSql.append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.RCOSTMPSTATE).append(" != 2 AND ");
        //第一权重(priority=0)、传入条件
        conditionSql.append(TableLfSubTemplate.PRIORITY).append("=").append(LfTemplateConfig.SUB_TEMP_PRIORITY);
        //模板类型
        if (null != tempsVo) {
            if (null != tempsVo.getTmpType()) {
                //conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.TMP_TYPE).append("=").append(tempsVo.getTmpType());
                conditionSql.append(" and ").append(TableLfSubTemplate.TABLE_NAME).append(".").append(TableLfSubTemplate.TMP_TYPE).append("=").append(tempsVo.getTmpType());
            }
            //模板名称
            if (StringUtils.isNotEmpty(tempsVo.getTmName())) {
                //conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.TM_NAME).append("='").append(tempsVo.getTmName()).append("'");
                conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.TM_NAME).append(" LIKE '%").append(tempsVo.getTmName()).append("%'");
            }
            //审核状态
            if (null != tempsVo.getAuditStatus()) {
                conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.AUDITSTATUS).append("=").append(tempsVo.getAuditStatus());
            }
            //模板静动态类型
            if (null != tempsVo.getDsFlag()) {
                conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.DS_FLAG).append("=").append(tempsVo.getDsFlag());
            }
            //模板启用禁用状态1.启用，0.禁用
            if (null != tempsVo.getTmState()) {
                conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.TM_STATE).append("=").append(tempsVo.getTmState());
            }
            //行业id
            if (null != tempsVo.getIndustryId()) {
                conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.INDUSTRYID).append("=").append(tempsVo.getIndustryId());
            }
            //用途id
            if (null != tempsVo.getUseId()) {
                conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.USEID).append("=").append(tempsVo.getUseId());
            }
            //审核id
            if (null != tempsVo.getSptemplid()) {
                conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.SP_TEMPLID).append("=").append(tempsVo.getSptemplid());
            }
            //是否是素材
            if (null != tempsVo.getIsMaterial()) {
                conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.ISMATERIAL).append("=").append(tempsVo.getIsMaterial());
            }
            //模板来源
            Integer source = 0;
            if (null == tempsVo.getSource() || tempsVo.getSource() == 0) {//默认查询emp创建的模板
                if (StaticValue.getCORPTYPE() == 0) {
                    source = 2;//单企业版
                }
                if (StaticValue.getCORPTYPE() == 1) {
                    source = 1;//托管版
                }
            } else {
                source = tempsVo.getSource();
            }
            conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.SOURCE).append(" in(").append(source);
            if (LfTemplateConfig.IS_PUBLIC == tempsVo.getIsPublic()) {
                conditionSql.append(",3");
            }
            conditionSql.append(")");
            //获取当前登录用户企业信息
            LfSysuser sysuser = UserUtil.getUser(request);
            //是公共场景(模板库)还是我的场景
            if (null != tempsVo.getIsPublic()) {
                //公共场景(模板库) 过滤条件:1.是否是公共场景
                if (LfTemplateConfig.IS_PUBLIC == tempsVo.getIsPublic()) {
                    //只能查看自己企业下的和10W号建的公共场景
                    if (!("100000".equals(sysuser.getCorpCode()))) {
                        conditionSql.append(" and (").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.CORP_CODE).append("='").append(sysuser.getCorpCode()).append("'")
                                .append(" or ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.CORP_CODE).append("=").append("'100000')");
                    }
                    conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.ISPUBLIC).append("=").append(tempsVo.getIsPublic());

                    //如果是非10万号，只能查看审核通过的模板,只能查看非草稿模板
                    if (!("100000".equals(sysuser.getCorpCode()))) {
                        if (null == tempsVo.getAuditStatus()){
                            conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.AUDITSTATUS).append("=").append(1);
                        }
                        //conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.TM_STATE).append("=").append(1);
                    }

                }
                //我的场景 过滤条件:1.是否是公共场景 2获取权限 下属ids
                if (LfTemplateConfig.IS_NOT_PUBLIC == tempsVo.getIsPublic()) {
                    //只能查看自己企业下的我的场景
                    conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.CORP_CODE).append("=").append(sysuser.getCorpCode());
                    conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.ISPUBLIC).append("=").append(tempsVo.getIsPublic());
                    String ids = userUtil.getPermissionUserCode(sysuser);
                    if (StringUtils.isNotBlank(ids)) {
                        if (tempsVo.getContainShare() == 1) {
                            String containShareSql = "OR " +
                                    "(LF_TMPLRELA.TOUSER_TYPE = 1 AND LF_TMPLRELA.TOUSER IN ( SELECT DEP_ID FROM LF_SYSUSER WHERE USER_ID = " + sysuser.getUserId() + " ))" +
                                    " OR " +
                                    "(LF_TMPLRELA.TOUSER_TYPE = 2 AND LF_TMPLRELA.TOUSER = " + sysuser.getUserId() + ")";
                            conditionSql.append(" and (").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.USER_ID).append(" in(").append(ids).append(")").append(containShareSql).append(")");
                        } else {
                            conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.USER_ID).append(" in(").append(ids).append(")");
                        }
                    }
                }
            }

            //创建时间区间
            if (StringUtils.isNotEmpty(tempsVo.getAddtimeBeg()) || StringUtils.isNotEmpty(tempsVo.getAddtimeEnd())) {
                //若起始时间为空终止时间不为空，则起始时间取最早时间
                if (StringUtils.isEmpty(tempsVo.getAddtimeBeg())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseConfig.DATEFORMAT);
                    String str = simpleDateFormat.format(new Date(0));
                    tempsVo.setAddtimeBeg(genericDAO.getTimeCondition(str));
                } else {
                    tempsVo.setAddtimeBeg(genericDAO.getTimeCondition(tempsVo.getAddtimeBeg()));
                }
                //若起始时间不为空终止时间为空，则终止时间取当前时间
                if (StringUtils.isEmpty(tempsVo.getAddtimeEnd())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseConfig.DATEFORMAT);
                    String str = simpleDateFormat.format(new Date());
                    tempsVo.setAddtimeEnd(genericDAO.getTimeCondition(str));
                } else {
                    tempsVo.setAddtimeEnd(genericDAO.getTimeCondition(tempsVo.getAddtimeEnd()));
                }
                conditionSql.append(" and ").append(TableLfTemplate.TABLE_NAME).append(".").append(TableLfTemplate.ADD_TIME).
                        append(" between ").append(tempsVo.getAddtimeBeg()).append(" and ").append(tempsVo.getAddtimeEnd());
            }
        }
        //String orderSql = " order by USECOUNT DESC,ADDTIME DESC";
//        String orderSql = " order by ADDTIME DESC";
        String orderSql = " order by TM_ID DESC";
        String countSql = "select count(*) totalcount from ( select distinct LF_TEMPLATE.TM_ID " + tableSql + conditionSql + ") countTab";
        String sql = "" + fieldSql + tableSql + conditionSql + orderSql;
        //分页数据
        PageInfo pageInfo = new PageInfo();
        if (null != tempsVo) {
            if (null != tempsVo.getPageSize()) {
                pageInfo.setPageSize(tempsVo.getPageSize());
            }
            if (null != tempsVo.getCurrentPage()) {
                pageInfo.setPageIndex(tempsVo.getCurrentPage());
            }
        }
        List<TempsVo> tempsVos = genericDAO.findPageVoListBySQL(TempsVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);

        PageListDto pageListDto = new PageListDto();
        pageListDto.setPageInfo(pageInfo);
        pageListDto.setOb(tempsVos);
        return pageListDto;
    }

    @Override
    public List<LfIndustryUse> getIndustryUseList(LfIndustryUse lfIndustryUse) throws Exception {
        // 调用查询语句
        List<LfIndustryUse> lfTemplateList = null;
        try {
            StringBuffer sql = new StringBuffer("SELECT * FROM LF_INDUSTRY_USE WHERE 1=1");

            //行业-用途类型
            if ((null != lfIndustryUse) && (null != lfIndustryUse.getType())) {
                sql.append(" AND TYPE = " + lfIndustryUse.getType());
            }

            //行业-用途 名称
            if ((null != lfIndustryUse) && (!StringUtils.IsNullOrEmpty(lfIndustryUse.getName()))) {
                sql.append(" AND  NAME ='" + lfIndustryUse.getName() + "'");
            }

            //模板类型
            if ((null != lfIndustryUse) && (!StringUtils.IsNullOrEmpty(String.valueOf(lfIndustryUse.getTmpType())))) {
                sql.append(" AND  TMPTYPE ='" + lfIndustryUse.getTmpType() + "'");
            }

            lfTemplateList = new SuperDAO().findEntityListBySQL(LfIndustryUse.class, sql.toString(), null);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询 LF_INDUSTRY_USE Dao层出现异常");
        }
        return lfTemplateList;
    }

    @Override
    public List<LfHfive> getH5s(TempHFiveVo tempHFiveVo, PageInfo pageInfo) throws Exception {
        // TODO Auto-generated method stub
        IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
        StringBuffer sql = new StringBuffer("SELECT HID,TITLE,AUTHOR,URL,STAUS,UPDATETIME FROM LF_HFIVE WHERE 1=1");
        if (!StringUtils.IsNullOrEmpty(tempHFiveVo.getPageFuzzyMatch())) {
            sql.append(" AND (HID LIKE '%" + tempHFiveVo.getPageFuzzyMatch() + "%' OR TITLE LIKE '%" + tempHFiveVo.getPageFuzzyMatch() + "%' OR AUTHOR LIKE '%" + tempHFiveVo.getPageFuzzyMatch() + "%') ");
        }
        if (!StringUtils.IsNullOrEmpty(tempHFiveVo.getStatus())) {
            sql.append(" AND STAUS =" + tempHFiveVo.getStatus());
        }
        if (!StringUtils.IsNullOrEmpty(tempHFiveVo.getUpdateTimeBeg())) {
            sql.append(" AND DATEDIFF(SS, '" + tempHFiveVo.getUpdateTimeBeg() + "', UPDATETIME )>=0");
        }
        if (!StringUtils.IsNullOrEmpty(tempHFiveVo.getUpdateTimeEnd())) {
            sql.append(" AND DATEDIFF(SS, '" + tempHFiveVo.getUpdateTimeEnd() + "', UPDATETIME )<=0");
        }

        pageInfo.setPageSize(Integer.parseInt(tempHFiveVo.getPageSize()));
        pageInfo.setPageIndex(Integer.parseInt(tempHFiveVo.getCurrentPage()));

        StringBuffer countSql = new StringBuffer("SELECT count(*) totalcount FROM ( ");
        countSql.append(sql.toString());
        countSql.append(" ) temp");
        List<LfHfive> lfHfiveList = genericDAO.findPageVoListBySQL(LfHfive.class, sql.toString(), countSql.toString(), pageInfo, StaticValue.EMP_POOLNAME);
        //Collections.reverse(lfHfiveList);
        return lfHfiveList;
    }

    @Override
    public List<String> getTempContents(String tmId, String contType, String tmpType) throws Exception {
        IEmpDAO empDAO = new DataAccessDriver().getEmpDAO();

        List<String> tempContents = new ArrayList<String>();
        //查询条件
        LinkedHashMap<String, String> condtionMap = new LinkedHashMap<String, String>();
        condtionMap.put("tmId", tmId);
        condtionMap.put("contType", contType);
        condtionMap.put("tmpType", tmpType);
        //排序条件
        LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
        orderMap.put("id", "ASC");
        try {
            List<LfTempContent> list = empDAO.findListByCondition(LfTempContent.class, condtionMap, orderMap);
            for (LfTempContent lfTempContent : list) {
                tempContents.add(lfTempContent.getTmpContent());
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询LfTempContent异常");
        }
        return tempContents;
    }

    @Override
    public List<LfTmplRela> findSharTmp(Long tmId, LfSysuser lfSysuser) {

        String sql = "SELECT * FROM lf_tmplrela " +
                "WHERE " +
                " TEMPL_ID=" + tmId + " AND " +
                "TEMPL_TYPE=4 AND (" +
                "(LF_TMPLRELA.TOUSER_TYPE = 1 AND LF_TMPLRELA.TOUSER IN ( SELECT DEP_ID FROM LF_SYSUSER WHERE USER_ID = " + lfSysuser.getUserId() + " )) " +
                "OR " +
                "(LF_TMPLRELA.TOUSER_TYPE = 2 AND LF_TMPLRELA.TOUSER = " + lfSysuser.getUserId() + "))";
        PageInfo pageInfo = new PageInfo();
        List<LfTmplRela> lfTmplRelas = null;
        try {
            lfTmplRelas = genericDAO.findPageEntityListBySQLNoCount(LfTmplRela.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询同步表异常");
        }
        return lfTmplRelas;
    }

    private String getTempsGroupBySql() {
        StringBuffer sql = new StringBuffer();
        sql.append(TableLfSubTemplate.TMP_TYPE).append(",")
                .append(TableLfTemplate.TM_NAME).append(",")
                .append(TableLfTemplate.TM_ID).append(",")
                .append(TableLfTemplate.USECOUNT).append(",")
                .append(TableLfSubTemplate.DEGREE_SIZE).append(",")
                .append(TableLfSubTemplate.DEGREE).append(",")
                .append(TableLfTemplate.DS_FLAG).append(",")
                .append(TableLfTemplate.USER_ID).append(",")
                .append(TableLfTemplate.ADD_TIME).append(",")
                .append(TableLfTemplate.TM_STATE).append(",")
                .append(TableLfTemplate.AUDITSTATUS).append(",")
                .append(TableLfTemplate.ISSHORTTEMP).append(",")
                .append(TableLfTemplate.SP_TEMPLID).append(",")
                .append(TableLfSubTemplate.CONTENT).append(",")
                .append(TableLfTemplate.USEID).append(",")
                .append(TableLfTemplate.INDUSTRYID).append(",")
                .append(TableLfSubTemplate.CARD_HTML).append(",")
                .append(TableLfSubTemplate.FILEURL).append(",")
                .append(TableLfSubTemplate.H5TYPE).append(",")
                .append(TableLfSubTemplate.H5URL).append(",")
                .append(TableLfTemplate.TM_MSG).append(",")
                .append(TableLfTemplate.VER).append(",")
                .append(TableLfTemplate.SOURCE);
        return sql.toString();
    }
}
