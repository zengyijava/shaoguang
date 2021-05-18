package com.montnets.emp.rms.meditor.tools;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.meditor.entity.LfSubTemplate;
import com.montnets.emp.rms.meditor.entity.LfTempContent;
import com.montnets.emp.rms.meditor.entity.LfTempParam;
import com.montnets.emp.rms.meditor.entity.SubTempData;
import com.montnets.emp.rms.meditor.entity.TempData;
import com.montnets.emp.util.StringUtils;

/**
 * @Description: 将V1.0 的模板记录转换为V3.0 编辑器识别的前端JSON 入库
 * LF_TEMPCONTENT
 * lf_sub_template
 * lf_tempcontent
 * @Auther:xuty
 * @Date: 2018/10/25 15:24
 */
public class ParseV1ToV3Tool extends SuperBiz {

    /**
     * 获取1.0版本的模板记录
     * @return
     */
    public List<LfTemplate> getV1Template(){
        List<LfTemplate> list = null;
        try{
            //查询条件
            LinkedHashMap<String, String> condtionMap = new LinkedHashMap<String, String>();
            condtionMap.put("ver", "V1.0");
            condtionMap.put("tmpType","11");
            //排序条件
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("tmid", "ASC");
            list = empDao.findListByCondition(LfTemplate.class,condtionMap,orderMap);
            condtionMap.put("tmpType","5");
            list.addAll(empDao.findListByCondition(LfTemplate.class,condtionMap,orderMap));
        }catch(Exception e){
            EmpExecutionContext.error(e, "查询LF_Template表出现异常.");
        }
        return list;
    }

    /**
     *
     * @param content     前端JSON
     * @param paramList   参数对象集合
     * @param lfTemplate  模板主表对象
     */
    public void transTemplate(String content, List<LfTempParam> paramList, LfTemplate lfTemplate) {
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            //入lf_tempcontent表
            saveLftempContent(conn, content, lfTemplate);
            //入lf_sub_template表
            saveLfSubTemplate(conn, lfTemplate);
            //入LF_TEMP_PARAM表
            saveLfTempParam(conn,paramList);
            //更新lf_template表 VER 字段为3.0
            updateLftemplate(conn, lfTemplate,content);

            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
        } finally {
            empTransDao.closeConnection(conn);
        }

    }

    public TempData getTempData(String content){
        TempData tempData = new TempData();
        try {
            tempData.setTmpType(11);//设置默认类型
            List<SubTempData> tempArr = new ArrayList<SubTempData>();
            SubTempData subTempData = new SubTempData();
            subTempData.setTmpType(11);
            subTempData.setContent(JSONObject.parse(content));
            tempArr.add(subTempData);
            tempData.setTempArr(tempArr);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"获取TempData有误");
        }
        return tempData;
    }

    /**
     * 入LF_TEMP_PARAM  表
     */
    public void saveLfTempParam(Connection conn, List<LfTempParam> params){
        try {
            if(null != params && params.size() > 0) {
                for (LfTempParam lfTempParam : params) {
                    empTransDao.save(conn, lfTempParam);
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "入LF_TEMP_PARAM 表出现异常.");
        }

    }
    /**
     * 入lf_tempcontent表
     *
     * @param conn
     * @param frontCont
     * @param lfTemplate
     */
    public void saveLftempContent(Connection conn, String frontCont, LfTemplate lfTemplate) {

        List<String> frontContList = StringUtils.strToList(frontCont);
        //前端JSON入库LF_TempContent 表
        try {
            for (String content : frontContList) {
                LfTempContent LftempContent = new LfTempContent();
                LftempContent.setTmId(lfTemplate.getTmid());
                LftempContent.setTmpType(11);
                LftempContent.setTmpContent(content);
                LftempContent.setContType(1);//1-前端JSON
                empTransDao.save(conn, LftempContent);
            }

            //富媒体的入库一条默认的短信补充方式 - 目的是兼容前端编辑器
            LfTempContent LftempContent = new LfTempContent();
            LftempContent.setTmId(lfTemplate.getTmid());
            LftempContent.setTmpType(14);
            LftempContent.setTmpContent("{\"template\":\"\"}");
            LftempContent.setContType(1);//1-前端JSON
            empTransDao.save(conn, LftempContent);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "入库LF_TEMPCONTENT 表出现异常.");
        }
    }

    /**
     * 入LF_SUB_TEMPLATE 表
     */
    public void saveLfSubTemplate(Connection conn, LfTemplate lfTemplate) {
        try {
            LfSubTemplate lfSubTemplate = new LfSubTemplate();
            lfSubTemplate.setH5Url(" ");
            lfSubTemplate.setH5Type(0);
            lfSubTemplate.setAddTime(new Timestamp(new Date().getTime()));
            lfSubTemplate.setApp(" ");
            lfSubTemplate.setCardHtml(" ");
            lfSubTemplate.setContent(" ");
            lfSubTemplate.setDegree(lfTemplate.getDegree());
            lfSubTemplate.setDegreeSize(lfTemplate.getDegreeSize().intValue());
            lfSubTemplate.setFileUrl(lfTemplate.getTmMsg());
            lfSubTemplate.setPriority(0);
            lfSubTemplate.setTmpType(11);
            lfSubTemplate.setStatus(0);
            lfSubTemplate.setIndustryId(lfTemplate.getIndustryid());
            lfSubTemplate.setUseId(lfTemplate.getUseid());
            lfSubTemplate.setStatus(0);
            lfSubTemplate.setTmId(lfTemplate.getTmid());
            empTransDao.save(conn, lfSubTemplate);

            //增加富媒体补充方式
            lfSubTemplate = new LfSubTemplate();
            lfSubTemplate.setH5Url(" ");
            lfSubTemplate.setH5Type(0);
            lfSubTemplate.setAddTime(new Timestamp(new Date().getTime()));
            lfSubTemplate.setApp(" ");
            lfSubTemplate.setCardHtml(" ");
            lfSubTemplate.setContent(" ");
            lfSubTemplate.setDegree(lfTemplate.getDegree());
            lfSubTemplate.setDegreeSize(lfTemplate.getDegreeSize().intValue());
            lfSubTemplate.setFileUrl(lfTemplate.getTmMsg());
            lfSubTemplate.setPriority(1);
            lfSubTemplate.setTmpType(14);
            lfSubTemplate.setStatus(0);
            lfSubTemplate.setIndustryId(lfTemplate.getIndustryid());
            lfSubTemplate.setUseId(lfTemplate.getUseid());
            lfSubTemplate.setTmId(lfTemplate.getTmid());
            empTransDao.save(conn, lfSubTemplate);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "入库LF_SUB_TEMPLATE表出现异常.");
        }
    }

    /**
     * 更新lf_template 表VER 字段为V3.0 ，参数个数Paramsnum
     */
    public void updateLftemplate(Connection conn, LfTemplate lfTemplate,String content) {
        try {

            TempData tempData = getTempData(content);
            tempData.setTmid(lfTemplate.getTmid());
            Locale locale = new Locale("zh","CN");
            String extJSON = ParamTool.saveExcleJsonV1TOV3(tempData,locale);

            int paramsnum = lfTemplate.getDsflag().intValue();
            lfTemplate.setVer("V3.0");
            lfTemplate.setAuditstatus(1);
            lfTemplate.setParamsnum(paramsnum);
            lfTemplate.setTmpType(11);
            lfTemplate.setExlJson(extJSON);
            empTransDao.update(conn, lfTemplate);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "更新lf_template 表 表出现异常.");
        }
    }
}
