package com.montnets.emp.rms.meditor.biz.imp;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.meditor.biz.SynTemplateBiz;
import com.montnets.emp.rms.meditor.dao.SynRcosTemplateDao;
import com.montnets.emp.rms.meditor.dao.imp.SynRcosTemplateDaoImpl;
import com.montnets.emp.rms.meditor.entity.LfSubTemplate;
import com.montnets.emp.rms.meditor.entity.LfTempContent;
import com.montnets.emp.rms.meditor.entity.LfTempParam;
import com.montnets.emp.rms.vo.CorpUserVo;
import com.montnets.emp.util.StringUtils;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @ProjectName: (TG)EMP7.2$
 * @Package: com.montnets.emp.rms.meditor.biz.imp$
 * @ClassName: SynTemplateBizImp$
 * @Description: 查询来自RCOS的模板
 * @Author: xuty
 * @CreateDate: 2018/11/1$ 10:30$
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/11/1$ 10:30$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class SynTemplateBizImp extends SuperBiz implements SynTemplateBiz {

    @Override
    public LfTemplate getTemplateFromRcos(String corpCode) {
        LinkedHashMap contionMap = new LinkedHashMap();
        LinkedHashMap orderMap = new LinkedHashMap();
        LfTemplate lfTemplate = null;
        try {
            //1-托管版EMP;2-标准版EMP,3-RCOS,4-富信模板接口,5-其它
            contionMap.put("source","3");
            //isMaterial = 1 - 公共素材，0 -非公共素材
            contionMap.put("isMaterial","1");
            //公共素材是放在100000号下
            contionMap.put("corpCode",corpCode);
            orderMap.put("sptemplid","desc");
            List<LfTemplate> lfTemplates = empDao.findListByCondition(LfTemplate.class,contionMap,orderMap);

            if(lfTemplates != null && lfTemplates.size() > 0){
                //获取同步来最新的一条记录进行下一次的同步操作
                lfTemplate = lfTemplates.get(0);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"LF_TEMPLATE 表查询出现异常!");
        }
        return  lfTemplate;
    }

    @Override
    public LfTemplate getEcTemplateFromRcos(String corpCode) {
        LinkedHashMap contionMap = new LinkedHashMap();
        LinkedHashMap orderMap = new LinkedHashMap();
        LfTemplate lfTemplate = null;
        try {
            //1-托管版EMP;2-标准版EMP,3-RCOS,4-富信模板接口,5-其它
            contionMap.put("source","3");
            //isMaterial = 1 - 公共素材，0 -非公共素材
            contionMap.put("isMaterial","0");
            //公共素材是放在100000号下
            contionMap.put("corpCode",corpCode);
            orderMap.put("sptemplid","desc");
            List<LfTemplate> lfTemplates = empDao.findListByCondition(LfTemplate.class,contionMap,orderMap);

            if(lfTemplates != null && lfTemplates.size() > 0){
                //获取同步来最新的一条记录进行下一次的同步操作
                lfTemplate = lfTemplates.get(0);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"LF_TEMPLATE 表查询出现异常!");
        }
        return  lfTemplate;
    }

    @Override
    public long addTemplate(LfTemplate lfTemplate) {
        long tmId = 0;
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            tmId = empTransDao.saveObjectReturnID(conn,lfTemplate);
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"LF_TEMPLATE 表添加记录出现异常!");
            empTransDao.rollBackTransaction(conn);
        }finally {
            empTransDao.closeConnection(conn);
        }
        return tmId;
    }

    @Override
    public boolean addLfSubTemplate(LfSubTemplate lfSubTemplate) {
        boolean flag = false;
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            if(empTransDao.save(conn,lfSubTemplate)){
                flag = true;
            }
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"LF_SUB_TEMPLATE 表添加记录出现异常!");
            empTransDao.rollBackTransaction(conn);
        }finally {
            empTransDao.closeConnection(conn);
        }
        return  flag;
    }

    @Override
    public boolean addLfTempContent(LfTempContent lfTempContent) {
        boolean flag = false;
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            if(empTransDao.save(conn,lfTempContent)){
                flag = true;
            }
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"LF_TEMPCONTENT 表添加记录出现异常!");
            empTransDao.rollBackTransaction(conn);
        }finally {
            empTransDao.closeConnection(conn);
        }
        return  flag;
    }

    @Override
    public boolean addLfTempParam(LfTempParam lfTempParam) {
        boolean flag = false;
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            if(empTransDao.save(conn,lfTempParam)){
                flag = true;
            }
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"LF_TEMPPARAM 表添加记录出现异常!");
            empTransDao.rollBackTransaction(conn);
        }finally {
            empTransDao.closeConnection(conn);
        }
        return  flag;
    }


    @Override
    public LfTemplate getTemplate(String spTemplateId,String source) {
        LinkedHashMap<String,String> contitionMap = new LinkedHashMap<String, String>();
        if(StringUtils.isNotEmpty(spTemplateId)){
            contitionMap.put("sptemplid",spTemplateId);
        }
        if(StringUtils.isNotEmpty(source)){
            contitionMap.put("source",source);
        }
        try {
            List<LfTemplate> temps = empDao.findListByCondition(LfTemplate.class, contitionMap, null);
            if(null != temps && temps.size() > 0 ){
               return temps.get(0);
            }
        } catch (Exception e){
            EmpExecutionContext.error(e,"RCOS同步公共模板，查询 LF_TEMPLATE 出现异常。");
        }
        return  null;
    }

    @Override
    public LfTemplate getTemplateByTmid(String tmId) {
        LfTemplate lfTemplate = null;
        LinkedHashMap<String, String> contitionMap = new LinkedHashMap<String, String>();
        contitionMap.put("tmid", tmId);
        try {
            List<LfTemplate> temps = empDao.findListByCondition(LfTemplate.class, contitionMap, null);
            if (null != temps && temps.size() > 0) {
                lfTemplate = temps.get(0);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"RCOS同步公共模板，查询 LF_TEMPLATE 出现异常。");
        }
        return lfTemplate;
    }

    @Override
    public boolean updateTemplate(LfTemplate lfTemplate) {
        boolean flag = false;
        try {
            empDao.update(lfTemplate);
            flag = true;
        } catch (Exception e) {
            EmpExecutionContext.error(e,"RCOS同步公共模板，更新模板表 LF_TEMPLATE 出现异常。");
        }
        return flag;
    }

    @Override
    public List<CorpUserVo> getCorps() {
        SynRcosTemplateDao synRcosTemplateDao = new SynRcosTemplateDaoImpl();
        return synRcosTemplateDao.getCorps();
    }

    @Override
    public void deleteTemplate(String sptempId,long rcosTmpState,long tmState) {
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            //逻辑删除LF_TEMPLATE 表中的记录
            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("sptemplid",sptempId);
            List<LfTemplate> lfTemplates = empDao.findListByCondition(LfTemplate.class, conditionMap, null);
            LfTemplate lfTemplate = null;
            if(null != lfTemplates && lfTemplates.size() > 0){
                lfTemplate= lfTemplates.get(0);
                if(null == lfTemplate.getSptemplid() && lfTemplate.getSptemplid() == 0){
                    //设置为null的目的是为了避免入库默认为0，导致模板ID一样
                    lfTemplate.setSptemplid(null);
                }
                //RCOS 平台模板状态
                lfTemplate.setRcosTmpState(Integer.parseInt(String.valueOf(rcosTmpState)));
                empTransDao.update(conn,lfTemplate);
            }
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
           EmpExecutionContext.error(e,"RCOS同步企业侧模板，更新本地模板 TM_STATE[模板状态]记录出现异常。");
           empTransDao.rollBackTransaction(conn);
        } finally {
            empTransDao.closeConnection(conn);
        }
    }

    @Override
    public List<LfTemplate> getCommonTemplateFromRcos(String corpCode) {
        List<LfTemplate> lfTemplates = null;
        LinkedHashMap contionMap = new LinkedHashMap();
        LinkedHashMap orderMap = new LinkedHashMap();
        try {
            //1-托管版EMP;2-标准版EMP,3-RCOS,4-富信模板接口,5-其它
            contionMap.put("source","3");
            //isMaterial = 1 - 公共素材，0 -非公共素材
            contionMap.put("isMaterial","1");
            contionMap.put("corpCode",corpCode);
            orderMap.put("sptemplid","desc");
            lfTemplates = empDao.findListByCondition(LfTemplate.class,contionMap,orderMap);

        } catch (Exception e) {
            EmpExecutionContext.error(e,"LF_TEMPLATE 表查询出现异常!");
        }
        return  lfTemplates;
    }
}
