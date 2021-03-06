package com.montnets.emp.rms.meditor.thread;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.dao.UserDataDAO;
import com.montnets.emp.rms.meditor.biz.ILfTempSynchBiz;
import com.montnets.emp.rms.meditor.biz.SynTemplateBiz;
import com.montnets.emp.rms.meditor.biz.imp.LfTempSynchBiz;
import com.montnets.emp.rms.meditor.biz.imp.SynTemplateBizImp;
import com.montnets.emp.rms.meditor.config.LfTemplateConfig;
import com.montnets.emp.rms.meditor.entity.LfSubTemplate;
import com.montnets.emp.rms.meditor.entity.LfTempContent;
import com.montnets.emp.rms.meditor.entity.LfTempParam;
import com.montnets.emp.rms.meditor.entity.LfTempSynch;
import com.montnets.emp.rms.meditor.entity.MainTemplate;
import com.montnets.emp.rms.meditor.entity.SubTempData;
import com.montnets.emp.rms.meditor.entity.TempContent;
import com.montnets.emp.rms.meditor.entity.TempData;
import com.montnets.emp.rms.meditor.entity.TempDataElement;
import com.montnets.emp.rms.meditor.entity.TempDataParam;
import com.montnets.emp.rms.meditor.entity.TempElement;
import com.montnets.emp.rms.meditor.tools.FileOperatorTool;
import com.montnets.emp.rms.meditor.tools.FileTypeTool;
import com.montnets.emp.rms.meditor.tools.JsonParseResourceTool;
import com.montnets.emp.rms.meditor.tools.OttFileBytesTool;
import com.montnets.emp.rms.meditor.tools.ParamTool;
import com.montnets.emp.rms.meditor.tools.RegParamTool;
import com.montnets.emp.rms.meditor.tools.RmsFileBytesTool;
import com.montnets.emp.rms.meditor.tools.String2FileUtil;
import com.montnets.emp.rms.meditor.tools.TemplateUtil;
import com.montnets.emp.rms.rmsapi.biz.impl.IRMSApiBiz;
import com.montnets.emp.rms.rmsapi.model.TempParams;
import com.montnets.emp.rms.tools.ZipTool;
import com.montnets.emp.rms.vo.UserDataVO;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;

/**
 * RCOS????????????????????????
 */
public class SyncRcosThread implements Runnable {
    private static final String TEMPLATEPATH = "file/templates/";
    private static final String TMPPATH = "ueditor/jsp/upload/";
    /**
     * ??????????????????IP??????
     */

    private static final String uploadPath = SystemGlobals.getValue("montnet.rms.uploadPath").trim();
    private static final String rootPath = SystemGlobals.getValue("montnet.rms.nginx.rootPath").trim();
    private final SimpleDateFormat SMF = new SimpleDateFormat("yyyyMMdd");
    private static final int INTEENAL = SystemGlobals.getIntValue("montnets.rcos.template.syn.internal", 60);
    private static final int SYNCOUNTS = SystemGlobals.getIntValue("montnets.rcos.template.syn.counts", 3);
    private IRMSApiBiz irmsApiBiz = new IRMSApiBiz();
    private ILfTempSynchBiz synchBiz = new LfTempSynchBiz();
    private SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
    private BaseBiz baseBiz = new BaseBiz();
    /**
     * Map<String,Long> String-???????????????Long -????????????????????????????????????????????????
     */
    private Map<String, Long> synCorpMap = new HashMap<String, Long>();

    @Override
    public void run() {

        long preTemplateId = 0L;
        while (true) {
            try {
                //??????????????????ID
                String spTemplateId = "";
                UserDataDAO userDataDAO = new UserDataDAO();
                UserDataVO userData = userDataDAO.getCommonSPUser();
                String userId = "FXADMIN";
                String pass = "abc!@#$%^&*123";
                if (userData != null) {
                    userId = userData.getUserId();
                    pass = userData.getPassWord();
                    // ?????????????????????????????? 100000
                    String corpCode = "100000";
                    //?????????
                    if (StaticValue.getCORPTYPE() == 0) {
                        corpCode = "100001";
                    }
                    if (synCorpMap.get(corpCode) != null && !getInternal(synCorpMap.get(corpCode))) {
                        continue;
                    }
                    spTemplateId = String.valueOf(getTemplateFromRcos(corpCode));
                    //?????????????????? 0-??????1-???
                    int isMaterial = 1;
                    List<LfTempSynch> synFailTemps = synchBiz.findSynFailTemp(isMaterial);
                    if (synFailTemps != null && synFailTemps.size() > 0) {
                        for (LfTempSynch lfTempSynch : synFailTemps) {
                            long tmpid = lfTempSynch.getSpTemplateid().longValue();
                            synChronizeTemp(userId, pass, String.valueOf(tmpid), corpCode);
                        }
                    }
                    if (spTemplateId.equals(String.valueOf(preTemplateId))) {
                        spTemplateId = String.valueOf(preTemplateId + 1);
                    }
                    preTemplateId = Long.parseLong(spTemplateId);
                    synChronizeTemp(userId, pass, String.valueOf(spTemplateId), corpCode);
                } else {
                    EmpExecutionContext.error("??????RCOS??????????????????????????????????????????SP???????????????");
                    //?????????????????????????????????
                    Thread.sleep(60L * 1000);
                }
                boolean synFlag = false;
                //???????????? ?????? ???????????????????????????????????? ??????????????????????????????????????????????????? INTEENAL ??????
                for (Map.Entry<String, Long> entry : synCorpMap.entrySet()) {
                    synFlag = getInternal(synCorpMap.get(entry.getKey()));
                    //?????????????????????????????????????????????????????????
                    if (synFlag) {
                        break;
                    }
                }
                //?????????????????????????????????????????????
                if (!synFlag && synCorpMap.size() > 0) {
                    Thread.sleep(INTEENAL * 1000L);
                    synCorpMap = null;
                    synCorpMap = new HashMap<String, Long>();
                }

                Thread.sleep(3000);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "RCOS?????????????????????????????????????????????5s");
                try {
                    Thread.sleep(5000);
                }catch (Exception e1){
                    EmpExecutionContext.error(e1, "RCOS???????????????????????????????????????????????????");
                }
            }
        }

    }


    /**
     * ??????????????????????????????????????? + ???????????????????????? ?????????????????????
     *
     * @param noTemplateTm
     * @return
     */
    public boolean getInternal(long noTemplateTm) {
        long currentTm = System.currentTimeMillis();
        noTemplateTm = noTemplateTm + INTEENAL * 1000;
        if (currentTm >= noTemplateTm) {
            //?????????
            return true;
        } else {
            //?????????
            return false;
        }
    }


    /**
     * ???RSC ??????RCOS?????????????????????
     *
     * @param userId
     * @param pwd
     * @param spTemplateId
     */
    public void synChronizeTemp(String userId, String pwd, String spTemplateId, String corpCode) {
        try {
            //isMaterial = 1-????????????
            synchBiz.syncTempInfo(Long.parseLong(spTemplateId), 1);
            Map<String, Object> templateMap = irmsApiBiz.getTemplateFromRCOS(userId, pwd, spTemplateId);
            Map<String, String> resultMap = getContent(templateMap, spTemplateId, corpCode);
            String result = resultMap.get("result");
            spTemplateId = resultMap.get("tmplid");
            if (StringUtils.isNotEmpty(spTemplateId)) {
                boolean isExistId = false;
                LfTemplate lfTemplate = synTemplateBiz.getTemplate(spTemplateId, null);
                if (null != lfTemplate) {
                    isExistId = true;
                }
                if (StringUtils.isNotEmpty(result) && !isExistId) {
                    boolean flag = analysisTemplateData(result, spTemplateId);
                    if (flag) {
                        synchBiz.changeSynStatus(Long.parseLong(spTemplateId), 1);
                    }
                }
            }
            //???????????????????????????
            Thread.sleep(500);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????RCOS??????????????????");
        }
    }

    /**
     * ????????????RCOS ???????????????
     *
     * @return
     */
    public Map<String, String> getContent(Map<String, Object> templateMap, String spTemplateId, String corpCode) {
        String result = "";
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            if (null != templateMap && templateMap.size() > 0) {
                // result 0 ??????????????????
                if (null != templateMap.get("result")) {
                    if (!"0".equals(templateMap.get("result").toString())) {
                        //???????????????????????????????????????Map???
                        synCorpMap.put(corpCode, System.currentTimeMillis());
                        resultMap.put("result", result);
                        resultMap.put("tmplid", spTemplateId);
                        //result = 100504 ?????????????????????????????????
                        if ("100504".equals(templateMap.get("result").toString())) {
                            Thread.sleep(INTEENAL * 1000L);

                        }
                    } else {
                        if (StringUtils.isNotBlank(templateMap.get("content").toString()) && StringUtils.isNotBlank(templateMap.get("tmplid").toString())) {
                            JSONArray jsonArray = JSON.parseArray(templateMap.get("content").toString());
                            JSONObject object = (JSONObject) jsonArray.get(0);
                            result = new String(Base64.decodeBase64(object.get("content").toString().getBytes("UTF-8")), "UTF-8");
                            resultMap.put("result", result);
                            resultMap.put("tmplid", templateMap.get("tmplid").toString());
                        } else {
                            resultMap.put("result", result);
                            resultMap.put("tmplid", spTemplateId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????RCOS?????????????????????????????????");
        }
        return resultMap;
    }

    /**
     * ?????????RCOS?????????EMP?????????????????????
     *
     * @return ???????????????????????????ID
     */
    public long getTemplateFromRcos(String corpCode) {
        long spTemplateId = 0;
        SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
        LfTemplate template = synTemplateBiz.getTemplateFromRcos(corpCode);
        if (null != template) {
            spTemplateId = template.getSptemplid() + 1;
//            long failId = synchBiz.findMaxFailTempId("1", SYNCOUNTS);//1-????????????
//            if (spTemplateId < failId) {
//                spTemplateId = failId;
//            }
        }
        return spTemplateId;
    }

    /**
     * ??????RCOS??????????????????????????????LF_TEMPLATE
     */
    public void addTemplate(TempData tmpData, String frontJson, HashMap<String, String> srcMap, long tmId, LfSysuser sysuser) {
        SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
        frontJson = replaceSrc(frontJson, srcMap);
        //????????????????????????JSON?????????????????????????????? ???tmid ????????????????????????
        JSONObject transObject = JSONObject.parseObject(frontJson);
        TempData transTmpData = JSONObject.toJavaObject(transObject, TempData.class);
        List<SubTempData> transSubData = transTmpData.getTempArr();

        // ???????????????
        List<LfSubTemplate> subTempList = new ArrayList<LfSubTemplate>();
        //???????????????
        List<LfTempParam> paramList = new ArrayList<LfTempParam>();

        //????????????????????????
        combineParam(tmId, transTmpData, paramList);
        //????????????
        int priority = 0;
        //H5?????????appContent
        String appContent = "";
        //??????
        int degree = 0;
        //??????
        int degreeSize = 0;
        for (SubTempData subData : transSubData) {
            priority++;
            //??????????????????byte[]
            if (null != subData) {
                Integer type = subData.getTmpType();
                switch (type) {
                    case 11:
                        //?????????
                        Map<String, Object> richMap = analysisRichMedia(transTmpData, subData, tmId, sysuser, priority, "zh_CN", paramList, subTempList);
                        if (11 == transTmpData.getTmpType().intValue()) {//?????????????????????????????????
                            degree = (Integer) richMap.get("degree");
                            degreeSize = (Integer) richMap.get("degreeSize");
                        }
                        break;
                    case 12:
                        // ???????????????????????????
                        Map<String, Object> cardMap = analysisCard(transTmpData, subData, tmId, sysuser, priority, paramList, subTempList);
                        if (12 == tmpData.getTmpType().intValue()) {//?????????????????????????????????
                            degree = (Integer) cardMap.get("degree");
                            degreeSize = (Integer) cardMap.get("degreeSize");
                        }
                        break;
                    case 13:
                        // ?????????
                        Map<String, Object> richTextMap = analysisRichText(transTmpData, subData, tmId, sysuser, priority, "zh_CN", paramList, subTempList);
                        degree = (Integer) richTextMap.get("degree");
                        degreeSize = (Integer) richTextMap.get("degreeSize");
                        break;
                    case 14:
                        // ??????
                        analysisSms(transTmpData, subTempList, subData, priority, paramList);
                        break;
                    case 15:
                        //H5-RCOS????????????
                        break;
                    default:
                        break;
                }

            }

        }
        //??????LF_SUB_TEMPLATE,LF_TEMPCONTENT,LF_TEMPPARAM,
        if (tmId > 0) {
            int subCount = 0;
            for (com.montnets.emp.rms.meditor.entity.LfSubTemplate LfSubTemplate : subTempList) {
                LfSubTemplate.setTmId(tmId);
                LfSubTemplate.setCardHtml(" ");
                String frontCont = LfSubTemplate.getFrontJson();
                String endCont = LfSubTemplate.getEndJson();
                // ??????????????????
                //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????? LF_TEMPCONTENT ????????????
                LfSubTemplate.setFrontJson(" ");
                LfSubTemplate.setContent(" ");
                LfSubTemplate.setEndJson(" ");
                LfSubTemplate.setH5Url(" ");
                boolean save = synTemplateBiz.addLfSubTemplate(LfSubTemplate);
                // ????????????JSON????????????????????? LF_TEMPCONTENT
                if (save) {
                    subCount++;
                    saveLftempContent(frontCont, endCont, LfSubTemplate, tmId, appContent);
                }
            }
            //???????????? LF_TEMPPARAM
            if (tmId > 0 && subCount == subTempList.size()) {
                if (null != paramList && paramList.size() > 0) {
                    for (LfTempParam lfTempParam : paramList) {
                        synTemplateBiz.addLfTempParam(lfTempParam);
                    }
                }
            }
        }

        //??????EXTJSON
        Locale locale = new Locale("zh", "CN");
        transTmpData.setTmid(tmId);
        ParamTool.saveExcleJson(transTmpData, locale);

        //??????????????????
        updateLfTemplate(String.valueOf(tmId), degree, degreeSize);

    }


    /**
     * ???????????????LF_TEMPLATE TM_MSG ??????
     *
     * @param tmMsg
     */
    public void updateLfTemplate(String tmId, String tmMsg) {
        SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
        LfTemplate lfTemplate = synTemplateBiz.getTemplateByTmid(tmId);
        lfTemplate.setTmMsg(tmMsg);
        synTemplateBiz.updateTemplate(lfTemplate);

    }

    /**
     * ?????????????????????JSON,?????????????????????????????????
     *
     * @param frontJson ??????JSON
     */
    public boolean analysisTemplateData(String frontJson, String spTemplateId) {
        boolean flag = false;
        long tmId = 0L;
        try {
            SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
            //??????JSON
            JSONObject object = JSONObject.parseObject(frontJson);
            TempData tmpData = JSONObject.toJavaObject(object, TempData.class);
            List<SubTempData> subDatas = tmpData.getTempArr();
            LfSysuser sysuser = new LfSysuser();
            //?????????
            if (StaticValue.getCORPTYPE() == 0) {
                sysuser.setUserId(2L);
                sysuser.setCorpCode("100001");
            } else {
                sysuser.setUserId(2L);
                sysuser.setCorpCode("100000");
            }
            //????????????????????????ID
            LfTemplate lfTemplate = combineLFtemplate(sysuser, tmpData, spTemplateId);
            tmId = synTemplateBiz.addTemplate(lfTemplate);
            //???????????????srcMap
            HashMap<String, String> srcMap = new HashMap<String, String>();
            //?????????????????????????????????????????????????????????????????????--TMPPATH:  ueditor/jsp/upload/
            for (SubTempData subData : subDatas) {
                //??????????????????byte[]
                if (null != subData) {
                    Integer type = subData.getTmpType();
                    switch (type) {
                        case 11:
                            //?????????
                            anaylysisRichMediaToTmp(tmpData, subData, srcMap);
                            break;
                        case 12:
                            // ???????????????????????????
                            anaylysisCardToTmp(subData, srcMap);
                            break;
                        case 13:
                            // ?????????
                            break;
                        case 14:
                            // ??????
                            break;
                        case 15:
                            //H5
                            break;
                        default:
                            break;
                    }
                }
            }
            //??? LF_TEMPLATE ,LF_SUB_TEMPLATE,LF_TEMPCONTENT,LF_TEMPPARAM ???
            addTemplate(tmpData, frontJson, srcMap, tmId, sysuser);
            //??????????????????
            String sourcePath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmId;
            String zipPath = new File(sourcePath + ".zip").getPath();
            //?????? ????????????
            ZipTool.createZip(sourcePath, zipPath);
            String fileCenterPath = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmId + ".zip";
            //????????????????????????????????????
            FileOperatorTool.uploadFileCenter(fileCenterPath);
            updateLfTemplate(String.valueOf(tmId), fileCenterPath);
            flag = true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????RCOS??????????????????????????????????????????");
            //???????????????????????????
            String ids = String.valueOf(tmId);
            try {
                baseBiz.deleteByIds(LfTemplate.class, ids);
            } catch (Exception e1) {
                EmpExecutionContext.error(e1, "???????????????????????????????????????????????????????????????");
            }
        }
        return flag;
    }

    /**
     * ????????????JSON???????????????
     *
     * @param frontCont
     * @param endCont
     * @param lfSubTemplate
     */
    public void saveLftempContent(String frontCont, String endCont, LfSubTemplate lfSubTemplate, Long tmid, String appContent) {
        List<String> frontContList = StringUtils.strToList(frontCont);
        List<String> endContList = StringUtils.strToList(endCont);
        SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
        //??????JSON??????LF_TempContent ???
        try {
            if (frontContList != null) {
                for (String content : frontContList) {
                    LfTempContent LftempContent = new LfTempContent();
                    LftempContent.setTmId(tmid);
                    LftempContent.setTmpType(lfSubTemplate.getTmpType());
                    LftempContent.setTmpContent(content);
                    LftempContent.setContType(1);//1-??????JSON
                    synTemplateBiz.addLfTempContent(LftempContent);
                }
            }
            if (endContList != null) {
                for (String content : endContList) {
                    LfTempContent LftempContent = new LfTempContent();
                    LftempContent.setTmId(tmid);
                    LftempContent.setTmpType(lfSubTemplate.getTmpType());
                    LftempContent.setTmpContent(content);
                    LftempContent.setContType(2);//2-??????JSON
                    synTemplateBiz.addLfTempContent(LftempContent);
                }
            }
            if ("15".equals(lfSubTemplate.getTmpType().toString())) {
                List<String> appContList = StringUtils.strToList(appContent);
                if (appContList != null) {
                    for (String content : appContList) {
                        LfTempContent LftempContent = new LfTempContent();
                        LftempContent.setTmId(tmid);
                        LftempContent.setTmpType(lfSubTemplate.getTmpType());
                        LftempContent.setTmpContent(content);
                        LftempContent.setContType(3);//3-appContent H5 ??????JSON
                        synTemplateBiz.addLfTempContent(LftempContent);
                    }
                }

            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????LF_TEMPCONTENT ???????????????.");
        }
    }


    /**
     * ???????????????????????????
     *
     * @param tmpData
     * @param paramList
     */
    private void combineParam(long tmid, TempData tmpData, List<LfTempParam> paramList) {
        if (null != tmpData && null != tmpData.getParamArr()) {
            List<TempDataParam> paramArr = tmpData.getParamArr();
            for (TempDataParam param : paramArr) {
                LfTempParam lfTempParam = new LfTempParam();
                //????????????
                int type = 1;//??????1??????????????????
                if (null != param.getType()) {
                    type = param.getType();
                }
                //?????????
                String name = "";
                if (null != param.getName()) {
                    name = param.getName();
                }
                //???????????? 0?????? 1??????
                int lengthRestrict = 0;
                if (null != param.getLengthRestrict()) {
                    lengthRestrict = param.getLengthRestrict();
                }
                //????????????
                int fixLength = 0;
                if (null != param.getFixLength()) {
                    fixLength = param.getFixLength();
                }
                //????????????
                int minLength = 0;
                if (null != param.getMinLength()) {
                    minLength = param.getMinLength();
                }
                //????????????
                int maxLength = 0;
                if (null != param.getMaxLength()) {
                    maxLength = param.getMaxLength();
                }
                // ??????????????????????????? 0-false ,1-true
                int hasLength = 0;
                if (null != param.getHasLength()) {
                    hasLength = param.getHasLength();
                }
                lfTempParam.setTmId(tmid);
                lfTempParam.setType(type);
                lfTempParam.setName(name);
                lfTempParam.setFixLength(fixLength);
                lfTempParam.setLengthRestrict(lengthRestrict);
                lfTempParam.setMinLength(minLength);
                lfTempParam.setMaxLength(maxLength);
                lfTempParam.setRegContent(RegParamTool.parseParam2Reg(lfTempParam.getType(), lfTempParam.getLengthRestrict(), lfTempParam.getMinLength(), lfTempParam.getMaxLength(), lfTempParam.getFixLength()));
                //?????????????????????
                lfTempParam.setHasLength(hasLength);
                paramList.add(lfTempParam);
            }
        }
    }

    /**
     * ?????? ??????src????????????????????? ??????????????????????????????
     *
     * @param frontJson RCOS ??????????????????JSON
     * @param srcMap    ???????????????src ??????
     * @return
     */
    public String replaceSrc(String frontJson, HashMap<String, String> srcMap) {
        //
        for (Map.Entry<String, String> en : srcMap.entrySet()) {
            frontJson = frontJson.replace(en.getKey(), en.getValue());
        }
        return frontJson;
    }

    /**
     * ????????? ??????src????????????????????? ??????????????????????????????
     *
     * @param tmpData
     * @param subData
     * @return
     */
    private void anaylysisRichMediaToTmp(TempData tmpData, SubTempData subData, HashMap<String, String> srcMap) {
        if (null != subData) {
            JSONArray contentArr = (JSONArray) subData.getContent();
            if (null != contentArr && contentArr.size() > 0) {
                for (int i = 0; i < contentArr.size(); i++) {
                    JSONObject jsonObject = contentArr.getJSONObject(i);
                    //???????????????
                    if (jsonObject.containsKey("type") && jsonObject.getString("type").equals("text")) {
                        if (!StringUtils.isBlank(jsonObject.getJSONObject("image").getString("src"))) {
                            //??????
                            String folderType = "image";
                            JSONObject imgObject = jsonObject.getJSONObject("image");
                            storeRichMediaTmpFile(imgObject, folderType, srcMap);
                            if (null != imgObject.getJSONArray("textEditable")) {//????????????????????????
                                JSONArray imgArr = imgObject.getJSONArray("textEditable");
                                for (int j = 0; j < imgArr.size(); j++) {
                                    JSONObject imgObj = imgArr.getJSONObject(j);
                                    if (!StringUtils.isBlank(imgObj.getString("src"))) {
                                        storeRichMediaTmpFile(imgObj, folderType, srcMap);
                                    }
                                }
                            }
                        }
                    } else if (jsonObject.containsKey("type") &&
                            jsonObject.getString("type").equals("image") && StringUtils.isNotBlank(jsonObject.getString("src"))) {
                        //??????
                        String folderType = "image";
                        storeRichMediaTmpFile(jsonObject, folderType, srcMap);
                        //????????????????????????
                        if (null != jsonObject.getJSONArray("textEditable")) {
                            JSONArray imgArr = jsonObject.getJSONArray("textEditable");
                            for (int j = 0; j < imgArr.size(); j++) {
                                JSONObject imgObj = imgArr.getJSONObject(j);
                                if (imgObj.containsKey("src")) {//????????????????????????
                                    if (!StringUtils.isBlank(imgObj.getString("src"))) {
                                        storeRichMediaTmpFile(imgObj, folderType, srcMap);
                                    }
                                }
                            }
                        }
                    } else if (jsonObject.containsKey("type") && jsonObject.getString("type").equals("audio")) {
                        //??????
                        String folderType = "audio";
                        storeRichMediaTmpFile(jsonObject, folderType, srcMap);

                    } else if (jsonObject.containsKey("type") && jsonObject.getString("type").equals("video")) {
                        //??????
                        String folderType = "video";
                        storeRichMediaTmpFile(jsonObject, folderType, srcMap);
                    }
                }
            }
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     */
    public void anaylysisCardToTmp(SubTempData subData, HashMap<String, String> srcMap) {
        //??????????????????
        if (null != subData) {
            TempContent tempContent = JSONObject.parseObject(subData.getContent().toString(), TempContent.class);
            //??????????????????????????????
            TempDataElement elements = tempContent.getElements();
            List<TempElement> images = elements.getImages();
            List<TempElement> audios = elements.getAudios();
            List<TempElement> videos = elements.getVideos();
            List<TempElement> qrcodes = elements.getQrcodes();

            if (null != images && images.size() > 0) {
                for (TempElement image : images) {
                    String folderType = "image";
                    storeCardTmpFile(image, folderType, srcMap);
                }
            }
            if (null != qrcodes && qrcodes.size() > 0) {
                for (TempElement qrcode : qrcodes) {
                    String folderType = "image";
                    storeCardTmpFile(qrcode, folderType, srcMap);
                }
            }
            if (null != videos && videos.size() > 0) {
                for (TempElement video : videos) {
                    String folderType = "video";
                    storeCardTmpFile(video, folderType, srcMap);
                }
            }
            if (null != audios && audios.size() > 0) {
                for (TempElement audio : audios) {
                    String folderType = "audio";
                    storeCardTmpFile(audio, folderType, srcMap);
                }
            }

        }
    }

    private void deleteFile(String filePath) {
        filePath = new TxtFileUtil().getWebRoot() + filePath;
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            boolean flag = file.delete();
            if (!flag) {
                EmpExecutionContext.error("?????????????????????");
            }
        }
    }

    /**
     * ???RCOS??????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param jsonObject
     * @param folderType
     */
    private void storeRichMediaTmpFile(JSONObject jsonObject, String folderType, HashMap<String, String> srcMap) {
        byte[] imgByte = null;
        try {
            String imageName = new TxtFileUtil().getWebRoot() + TMPPATH + folderType + "/" + SMF.format(new Date()) + "/" + UUID.randomUUID().toString().replace("-", "") + ".";
            imgByte = Base64.decodeBase64(jsonObject.getString("src").getBytes());
            ByteArrayInputStream buf = new ByteArrayInputStream(imgByte);
            byte[] fileHeader = new byte[4];
            System.arraycopy(imgByte, 0, fileHeader, 0, 4);
            String fileType = FileTypeTool.getFileType(fileHeader);
            imageName = imageName + fileType;
            writeInFileByfi(imageName, imgByte);
            //src??????????????????
            String source = jsonObject.getString("src");
            //src?????????????????????
            String dest = imageName.substring(imageName.indexOf(TMPPATH), imageName.length());
            srcMap.put(source, dest);
        } catch (Exception e) {
            if (imgByte == null || imgByte.length == 0) {
                EmpExecutionContext.error(e, "RCOS????????????-???????????????--??????????????????src??????????????????");
            }
            EmpExecutionContext.error(e, "????????????????????????src base64??????????????????");
        }
    }

    /**
     * ???RCOS??????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param element
     * @param folderType
     */
    private void storeCardTmpFile(TempElement element, String folderType, HashMap<String, String> srcMap) {
        byte[] imgByte = null;
        try {
            String imageName = new TxtFileUtil().getWebRoot() + TMPPATH + folderType + "/" + SMF.format(new Date()) + "/" + UUID.randomUUID().toString().replace("-", "") + ".";
            imgByte = Base64.decodeBase64(element.getSrc().getBytes());
            byte[] fileHeader = new byte[4];
            System.arraycopy(imgByte, 0, fileHeader, 0, 4);
            String fileType = FileTypeTool.getFileType(fileHeader);
            imageName = imageName + fileType;
            writeInFileByfi(imageName, imgByte);
            //src??????????????????
            String source = element.getSrc();
            //src?????????????????????
            String dest = imageName.substring(imageName.indexOf(TMPPATH), imageName.length());
            srcMap.put(source, dest);
        } catch (Exception e) {
            if (imgByte == null || imgByte.length == 0) {
                EmpExecutionContext.error(e, "RCOS????????????-????????????-- ??????????????????src??????????????????");
            }
            EmpExecutionContext.error(e, "????????????????????????src base64??????????????????");
        }
    }


    /**
     * ???????????????????????????
     *
     * @param sysuser
     * @param tmpData
     * @return
     */
    private LfTemplate combineLFtemplate(LfSysuser sysuser, TempData tmpData, String spTemplateId) {
        LfTemplate template = new LfTemplate();
        // ?????????????????????
        template.setTmCode(" ");
        // ????????????(????????????-0????????????-1?????????1?????????2)
        template.setIsPass(-1);
        // ???????????????0?????????1?????????2?????????
        if (1 == tmpData.getSubType()) {//??????
            template.setTmState(1L);
        } else {//????????????
            template.setTmState(2L);
        }
        // ????????????
        template.setAddtime(Timestamp.valueOf(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date())));
        // ????????????
        template.setTmMsg(" ");// ???????????????????????????????????????????????????
        // ?????????ID
        template.setUserId(sysuser.getUserId());
        template.setCorpCode(sysuser.getCorpCode());
        // ?????????3-????????????;4-???????????????11-???????????????
        template.setTmpType(11);//?????????????????????????????????H5????????????????????????11
        if (null != tmpData.getParamArr() && tmpData.getParamArr().size() > 0) {// ????????????
            template.setParamcnt(tmpData.getParamArr().size());
            template.setDsflag(1L);
        } else {// ????????????
            template.setParamcnt(0);
            // ????????????0???????????????1
            template.setDsflag(0L);
        }
        //?????????????????? 0-???????????????1-????????????

        template.setIsPublic(1);
        //??????????????????
        template.setUsecount(0L);
        //V2.0 ?????????????????????????????????????????? JSON???
        template.setExlJson(" ");
        // ?????????????????? -1???????????????0??????????????????1????????????2????????????3????????????
        template.setAuditstatus(1);
        ////???????????????????????????0??????????????????	1???????????????????????????	2????????????????????????????????????
        template.setTmplstatus(0);

        //??????ID
        int useID = -2; //??????-2
        if (null != tmpData.getUseId()) {
            useID = tmpData.getUseId();
        }
        template.setUseid(useID);

        //??????ID
        int industryID = -1;//??????-1
        if (null != tmpData.getIndustryId()) {
            industryID = tmpData.getIndustryId();
        }
        template.setIndustryid(industryID);

        //?????????
        template.setVer("V3.0");
        //???????????? 	0????????????	1???????????????	2???????????????	3???????????? --??????????????????????????????????????????????????????
        template.setSubmitstatus(0);
        //?????????????????? 0-???
        template.setIsShortTemp(0);
        //???????????????
        String tmName = StringUtils.isBlank(tmpData.getTmName()) ? " " : tmpData.getTmName();
        template.setTmName(tmName);

        template.setSptemplid(Long.parseLong(spTemplateId));
        template.setSource(3);//??????????????????????????????RCOS - 3
        template.setIsMaterial(1);//?????????????????????1
        return template;

    }

    /**
     * H5????????????????????????
     *
     * @param tmpData
     * @param subData
     * @param tmid
     * @param sysuser
     * @param priority
     * @param paramList
     * @param subTempList
     * @param subGwlist
     * @param version
     * @param createOtt
     */
    private Map<String, Object> analysisH5(TempData tmpData, SubTempData subData, Long tmid, LfSysuser sysuser, int priority, List<LfTempParam> paramList, List<LfSubTemplate> subTempList, List<TempParams> subGwlist, String version, String createOtt) {
        byte[] ottBytes = null;
        int degree = 0;
        int degreeSize = 0;
        int pnum = 0;
        int subType = null == tmpData.getSubType() ? 1 : tmpData.getSubType();
        String h5Root = SystemGlobals.getValue("montnet.rms.nginx.rootPath", "web/");
        Map<String, Object> h5Map = new HashMap<String, Object>();
        try {
            //H5????????????tempArr ???????????????????????????????????????????????? ??????+ ?????? ????????????
            String corpCode = sysuser.getCorpCode();
            JSONObject object = (JSONObject) tmpData.getTempArr().get(0).getContent();
            String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/";
            String h5path = new TxtFileUtil().getWebRoot() + "templates/" + corpCode;
//                            //HTML????????????
//                            new FileOperatorTool().writeFile(srcPath + "/" + tmid + ".html", h5Html);
            new JsonParseResourceTool().parseH5Html(object, h5path + "/" + tmid + ".html", new StringBuilder());
            // ??????????????????????????????url
            File h5File = new File(h5path + "/" + tmid + ".html");
            String btype = "2";//1-????????????
            String ftype = "1";//????????????????????????1-html
            String h5URL = String2FileUtil.uploadFile(h5File, "templates/" + corpCode, btype, ftype);
            h5URL = uploadPath + rootPath + h5URL;
            //??????H5?????? ?????????????????????????????????
            MainTemplate dataParsToTemp = TemplateUtil.hFiveToTerminal(tmpData, h5URL, new StringBuilder());
            dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
            String endJson = JSONObject.toJSONString(dataParsToTemp);
            //???ott????????????????????????
            String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/src/ott.mrcsl";
            new FileOperatorTool().writeFile(mcrslUrl, endJson);

            // ???????????????
            String frontJson = JSONObject.toJSONString(subData.getContent());
            //??????????????????
            String appContent = JSONObject.toJSONString(tmpData.getApp());
            //H5 ????????????appContent ????????????src
            Map<String, Object> resultMap = new JsonParseResourceTool().storeH5ResourceFile(corpCode, tmid, tmpData, appContent, subType);
            appContent = (String) resultMap.get("frontJson");
            String fileUrl = TEMPLATEPATH + corpCode + "/" + tmid + "/ott/fuxin.mrcsl";
            //???????????????

            combineSubLftemplate(tmpData, subData, subTempList, priority, frontJson, fileUrl, endJson);
            // ??????????????????????????????
            srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/";
            Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath + "src/");
            if (tmpData.getSubType() == 1) {//???????????? ???????????????????????????
                ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
            } else {
                ottBytes = " ".getBytes();

            }
            //????????????????????????
            String fuxinMrcsl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/fuxin.mrcsl";
            new FileOperatorTool().writeFile(fuxinMrcsl, ottBytes);
            //?????? ?????? ????????? ????????????---- ????????????????????????+??????
            String titleTxt = tmpData.getApp().getTitle().getText();
            String smContent = titleTxt + "?????????????????? " + h5URL;
            h5Map.put("smContent", smContent);
            h5Map.put("h5URL", h5URL);
            h5Map.put("appContent", appContent);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "H5????????????????????????-??????H5????????????????????????");
        }
        return h5Map;
    }


    /**
     * ??????????????????????????????
     *
     * @param tmpData
     * @param subTempList
     * @param subData
     * @param priority
     * @param paramList
     * @return
     */
    private Map<String, Object> analysisSms(TempData tmpData, List<LfSubTemplate> subTempList, SubTempData subData, int priority, List<LfTempParam> paramList) {
        String smContent = "";
        String smParamReg = "";
        Map<String, Object> smsMap = new HashMap<String, Object>();
        String frontJson = JSONObject.toJSONString(subData.getContent());
        combineSubLftemplate(tmpData, subData, subTempList, priority, frontJson, " ", " ");
        Map<String, String> textMap = JSONObject.parseObject(subData.getContent().toString(), Map.class);
        smContent = RegParamTool.replaceParam(textMap.get("template"));
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(smContent);
        smContent = m.replaceAll("");
        StringBuilder paramBuilder = new StringBuilder("{");
        for (int i = 0; i < paramList.size(); i++) {
            LfTempParam param = paramList.get(i);
            String enCodeRegParam = new String(Base64.encodeBase64(param.getRegContent().getBytes()));
            paramBuilder.append("\\\"").append("P" + (i + 1) + "\\\"").append(":\\\"").append(enCodeRegParam).append("\\\",");
        }
        if (paramBuilder.toString().endsWith(",")) {
            smParamReg = paramBuilder.toString().substring(0, paramBuilder.length() - 1) + "}";
        }
        smsMap.put("smContent", smContent);
        smsMap.put("smParamReg", smParamReg);
        return smsMap;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param tmpData
     * @param subData
     * @param tmid
     * @param sysuser
     * @param priority
     * @param langName
     * @param paramList
     * @param subTempList
     */
    private Map<String, Object> analysisRichText(TempData tmpData, SubTempData subData, Long tmid, LfSysuser sysuser, int priority, String langName, List<LfTempParam> paramList, List<LfSubTemplate> subTempList) {
        byte[] ottBytes = null;
        int degree = 0;
        int degreeSize = 0;
        int pnum = 0;
        Map<String, Object> richTextMap = new HashMap<String, Object>();
        //????????????JSON
        String frontJson = JSONObject.toJSONString(subData.getContent());
        String fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        combineSubLftemplate(tmpData, subData, subTempList, priority, frontJson, fileUrl, " ");
        MainTemplate dataParsToTemp = TemplateUtil.dataParsToTemp(tmpData, new StringBuilder());
        dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
        String endJson = JSONObject.toJSONString(dataParsToTemp);
        endJson = new JsonParseResourceTool().replaceDiv(endJson);
        //???ott????????????????????????
        String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/ott.mrcsl";
        new FileOperatorTool().writeFile(mcrslUrl, endJson);
        //??????????????????????????????
        String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/";
        Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath);
        if (tmpData.getSubType() == 1) {//???????????? ???????????????????????????
            ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
        } else {
            ottBytes = " ".getBytes();

        }
        //????????????
        degree = null == subData.getDegree() ? 0 : subData.getDegree();
        degreeSize = ottBytes.length;
        pnum = paramList.size();
        String fuxinMrcsl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        new FileOperatorTool().writeFile(fuxinMrcsl, ottBytes);
        richTextMap.put("degree", degree);
        richTextMap.put("degreeSize", degreeSize);
        return richTextMap;
    }


    /**
     * ????????????????????????
     *
     * @param tmpData
     * @param subData
     * @param tmid
     * @param sysuser
     * @param priority
     * @param paramList
     * @param subTempList
     */
    private Map<String, Object> analysisCard(TempData tmpData, SubTempData subData, Long tmid, LfSysuser sysuser, int priority, List<LfTempParam> paramList, List<LfSubTemplate> subTempList) {
        //??????JSON
        byte[] ottBytes = null;
        int degree = 0;
        int degreeSize = 0;
        int pnum = 0;
        boolean subGwFlag = false;
        Map<String, Object> cardMap = new HashMap<String, Object>();
        int subType = null == tmpData.getSubType() ? 1 : tmpData.getSubType();
        MainTemplate dataParsToTemp = TemplateUtil.dataParsToTemp(tmpData, new StringBuilder());
        dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
        String endJson = JSONObject.toJSONString(dataParsToTemp);
        endJson = new JsonParseResourceTool().replaceDiv(endJson);
        //???ott????????????????????????
        String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/ott.mrcsl";
        new FileOperatorTool().writeFile(mcrslUrl, endJson);

        // ???????????????
        TempContent tempContent = JSONObject.parseObject(subData.getContent().toString(), TempContent.class);
        String frontJson = JSONObject.toJSONString(subData.getContent());
        Map<String, Object> resultMap = new JsonParseResourceTool().storeOttResourceFile(sysuser.getCorpCode(), tmid, tempContent, frontJson, subType);
        String json = (String) resultMap.get("frontJson");
        String fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        combineSubLftemplate(tmpData, subData, subTempList, priority, json, fileUrl, endJson);
        // ??????????????????????????????
        String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/";
        Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath + "src/");
        if (tmpData.getSubType() == 1) {//???????????? ???????????????????????????
            ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
        } else {
            ottBytes = " ".getBytes();

        }
        //????????????
        degree = null == subData.getDegree() ? 0 : subData.getDegree();
        degreeSize = ottBytes.length;
        pnum = paramList.size();
        String fuxinMrcsl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        new FileOperatorTool().writeFile(fuxinMrcsl, ottBytes);
        cardMap.put("degree", degree);
        cardMap.put("degreeSize", degreeSize);
        return cardMap;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param tmpData
     * @param subData
     * @param tmid
     * @param sysuser
     * @param priority
     * @param langName
     * @param paramList
     * @param subTempList
     */
    private Map<String, Object> analysisRichMedia(TempData tmpData, SubTempData subData, long tmid, LfSysuser sysuser, int priority, String langName, List<LfTempParam> paramList, List<LfSubTemplate> subTempList) {
        //??????JSON
        String frontJson = "";
        byte[] rmsBytes = null;
        byte[] ottBytes = null;
        int degree = 0;
        int degreeSize = 0;
        int pnum = 0;
        boolean subGwFlag = false;
        String mcrslUrl = "";
        //????????????-0????????????1-????????????
        int subType = tmpData.getSubType();
        Map<String, Object> richMap = new HashMap<String, Object>();
        //??????????????????
        JSONArray jsonArray = (JSONArray) subData.getContent();
        String projectPath = String2FileUtil.getWebRoot();
        String targetPath = projectPath + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms";
        frontJson = JSONObject.toJSONString(subData.getContent());
        Map<String, Object> resultMap = new JsonParseResourceTool().storeRmsResourceFile(targetPath, projectPath, jsonArray, frontJson, langName, subType, new StringBuilder());
        String json = (String) resultMap.get("fontJson");
        String fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms/fuxin.rms";
        combineSubLftemplate(tmpData, subData, subTempList, priority, json, fileUrl, " ");
        List<Map<String, String>> list = (List<Map<String, String>>) resultMap.get("list");
        String title = (null == tmpData.getTmName()) ? "" : tmpData.getTmName();
        if (tmpData.getSubType() == 1) {//???????????? ???????????????????????????
            rmsBytes = new RmsFileBytesTool().getRmsFileBytes(targetPath + "/src", title, list);
        } else {
            rmsBytes = " ".getBytes();
        }
        //????????????
        degree = null == subData.getDegree() ? 0 : subData.getDegree();
        degreeSize = rmsBytes.length;
        pnum = paramList.size();
        String fuxinRms = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms/fuxin.rms";
        new FileOperatorTool().writeFile(fuxinRms, rmsBytes);
        //??????H5???????????????
        mcrslUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms/src/fuxin.rms";
        richMap.put("degree", degree);
        richMap.put("degreeSize", degreeSize);
        return richMap;
    }


    /**
     * ???????????????????????????
     *
     * @param tmpData
     * @param subData
     * @param subTempList
     */
    private void combineSubLftemplate(TempData tmpData, SubTempData subData,
                                      List<LfSubTemplate> subTempList, int priority, String frontJson, String filrUrl, String endJson) {
        // ?????????????????????
        LfSubTemplate subTemplate = new LfSubTemplate();
        subTemplate.setTmpType(subData.getTmpType());
        subTemplate.setAddTime(Timestamp.valueOf(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date())));
        String content = JSONObject.toJSONString(subData.getContent());
        subTemplate.setContent(frontJson);
//        MainTemplate dataParsToTemp = TemplateUtil.dataParsToTemp(tmpData, new StringBuilder());
//        String endJson = JSONObject.toJSONString(dataParsToTemp);
        subTemplate.setEndJson(endJson);
        subTemplate.setFileUrl(filrUrl);
        subTemplate.setFrontJson(frontJson);
        subTemplate.setIndustryId(null == tmpData.getIndustryId() ? -1 : tmpData.getIndustryId());
        subTemplate.setUseId(null == tmpData.getUseId() ? -2 : tmpData.getUseId());
        if (tmpData.getTmpType() == subData.getTmpType()) {
            subTemplate.setPriority(LfTemplateConfig.SUB_TEMP_PRIORITY);
        } else {
            subTemplate.setPriority(priority);
        }
        subTemplate.setStatus(1);
        String cardHtml = StringUtils.isBlank(subData.getCardHtml()) ? " " : subData.getCardHtml();
        subTemplate.setCardHtml(cardHtml);
        subTemplate.setDegree(null == subData.getDegree() ? 0 : subData.getDegree());
        subTemplate.setDegreeSize(null == subData.getDegreeSize() ? 1 : subData.getDegreeSize() + 1);
        subTemplate.setH5Type(null == subData.getH5Type() ? 0 : subData.getH5Type());
        subTempList.add(subTemplate);

    }

    /**
     * ????????????RCOS????????????????????????????????????
     *
     * @param path    ????????????????????????
     * @param imgByte ???????????????????????????
     */
    public static void writeInFileByfi(String path, byte[] imgByte) {
        File f = new File(path);
        FileOutputStream fos = null;
        try {
            File folder = new File(f.getParent());
            if (!folder.exists()) {//????????????
                folder.mkdirs();
            }
            if (!f.exists()) {
                boolean flag = f.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("?????????????????????");
                }
            }
            fos = new FileOutputStream(f);
            fos.write(imgByte);
        } catch (IOException e) {
            EmpExecutionContext.error(e, "?????????????????????...");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "?????????????????????");
                }
            }
        }

    }

    /**
     * ?????? ???????????????
     *
     * @param tmId
     * @param degree
     * @param degreeSize
     */
    public void updateLfTemplate(String tmId, int degree, long degreeSize) {
        SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
        LfTemplate lfTemplate = synTemplateBiz.getTemplateByTmid(tmId);
        if (lfTemplate != null) {
            lfTemplate.setDegree(degree);
            lfTemplate.setDegreeSize(degreeSize);
            if (lfTemplate.getSptemplid() == null && lfTemplate.getSptemplid().longValue() == 0) {
                lfTemplate.setSptemplid(null);
            }
            synTemplateBiz.updateTemplate(lfTemplate);
        }
    }
}
