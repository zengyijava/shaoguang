package com.montnets.emp.rms.meditor.biz.imp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.rms.commontempl.dao.CommonTemplateDao;
import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.commontempl.entity.LfTemplate;
import com.montnets.emp.rms.meditor.dao.imp.MeditorDaoImp;
import com.montnets.emp.rms.meditor.entity.LfTempParam;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author:yangdl
 * @Data:Created in 20:09 2018.8.8 008
 */
public class CommonTemplateBiz extends BaseBiz {


    public List<LfTemplateVo> getCommonTempalateList(LfTemplateVo lfTemplate, PageInfo pageInfo, String key) {
        List<LfTemplateVo> templateList = new CommonTemplateDao().getCommonTempalateList(lfTemplate, pageInfo, key);
        return templateList;
    }

    public List<LfIndustryUse> getIndustryUseList(LfIndustryUse lfIndustryUse,
                                                  PageInfo pageInfo) {
        return new CommonTemplateDao().getIndustryUseList(lfIndustryUse, pageInfo);
    }

    public boolean addIndustryOrUse(LfIndustryUse lfIndustryUse) {
        return new CommonTemplateDao().addIndustryOrUse(lfIndustryUse);
    }

    public boolean updateIndustryOrUse(LfIndustryUse lfIndustryUse) {
        return new CommonTemplateDao().updateIndustryOrUse(lfIndustryUse);
    }

    public boolean updateUseCount(LfTemplate lfTemplate) {
        return new CommonTemplateDao().updateUseCount(lfTemplate);
    }

    public String getContentbyTmId(String tmId, String contType, String tmpType) throws Exception {
        MeditorDaoImp meditorDaoImp = new MeditorDaoImp();
        List<String> tempContents = meditorDaoImp.getTempContents(tmId, contType, tmpType);
        return StringUtils.listToStr(tempContents);
    }

    public String getContentbyTempid(String tempid, String contType, String tmpType) throws Exception {
        LinkedHashMap map = new LinkedHashMap<String, String>();
        map.put("sptemplid", tempid);
        Long tmid = null;
        List<LfTemplate> lfTemplateList = empDao.findListByCondition(LfTemplate.class, map, null);
        if (lfTemplateList.size() != 0) {
            LfTemplate lfTemplate1 = lfTemplateList.get(0);
            tmid = lfTemplate1.getTmid();
        }
        return getContentbyTmId(String.valueOf(tmid), contType, tmpType);
    }

    public List<LfTempParam> getParamByTmId(String tmId) throws Exception {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("tmId", tmId);
        List<LfTempParam> paramList = empDao.findListByCondition(LfTempParam.class, map, null);
        return paramList;
    }

    public List<LfTempParam> getParamByTempid(String tempid) throws Exception {
        LinkedHashMap map = new LinkedHashMap<String, String>();
        LinkedHashMap subMap = new LinkedHashMap<String, String>();
        map.put("sptemplid", tempid);
        Long tmid = null;
        List<LfTemplate> lfTemplateList = empDao.findListByCondition(LfTemplate.class, map, null);
        if (lfTemplateList.size() != 0) {
            LfTemplate lfTemplate1 = lfTemplateList.get(0);
            tmid = lfTemplate1.getTmid();
        }
        subMap.put("tmId", String.valueOf(tmid));
        List<LfTempParam> paramList = empDao.findListByCondition(LfTempParam.class, subMap, null);
        return paramList;
    }

    /**
     * 只取文本内容进行解析
     * @param tempId 模板Id
     * @param tempType 模板类型
     * @return 结果字符串
     * @throws Exception 抛出异常
     */
    public String getTextContentbyTempid(String tempId, String tempType) throws Exception{
        StringBuilder str = new StringBuilder();
        String contentbyTempid = getContentbyTempid(tempId, "1", tempType);
        if("11".equals(tempType)){
            //富媒体消息
            JSONArray objects = JSONObject.parseArray(contentbyTempid);
            for (Object jsonObject : objects){
                JSONObject obj = (JSONObject) jsonObject;
                String text = (String) obj.get("text");
                str.append(org.apache.commons.lang.StringUtils.isBlank(text) ? "" : text + ",");
                //有图配文
                if(obj.get("textEditable") != null){
                    JSONArray textEditableArr = (JSONArray) obj.get("textEditable");
                    for(Object textObj:textEditableArr){
                        JSONObject object = (JSONObject) textObj;
                        String textConent = (String) object.get("text");
                        str.append(textConent).append(",");
                    }
                }
            }
        }
        if("13".equals(tempType)){
            //富文本消息
            JSONObject parse = (JSONObject) JSONObject.parse(contentbyTempid);
            String tempStr = (String)parse.get("template");
            //替换掉html相关内容
            str.append(tempStr.replaceAll("<.*?>",""));
        }
        if("12".equals(tempType)){
            //场景消息
            JSONObject obj = (JSONObject) JSONObject.parse(contentbyTempid);
            JSONObject ele = (JSONObject)obj.get("elements");
            //文本
            JSONArray textArr = (JSONArray) ele.get("texts");
            for(Object textObj : textArr){
                JSONObject object = (JSONObject) textObj;
                String textConent = (String) object.get("text");
                str.append(textConent).append(",");
            }
            //按钮
            JSONArray butArr = (JSONArray) ele.get("buttons");
            for(Object textObj : butArr){
                JSONObject object = (JSONObject) textObj;
                String textConent = (String) object.get("text");
                String url = (String) object.get("url");
                String pkg = (String) object.get("pkg");
                String param = (String) object.get("param");
                String path = (String) object.get("path");
                str.append(textConent).append(",").append(url == null ? "":url+",").append(path == null ? "":path+",").append(pkg == null ? "":pkg+",").append(param == null ? "":param+",");
            }
        }
        //判断最后是否包含括号
        String result = str.toString();
        if(str.toString().endsWith(",")){
            result = str.deleteCharAt(str.lastIndexOf(",")).toString();
        }
        return result.replaceAll("&nbsp;"," ").
                replaceAll("&amp;","&").
                replaceAll("&gt;",">").
                replaceAll("&lt;","<").
                replaceAll("&apos;","'").
                replaceAll("&quot;","\"");
    }
}
