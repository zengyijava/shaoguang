package com.montnets.emp.rms.meditor.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
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
import com.montnets.emp.rms.meditor.entity.*;
import com.montnets.emp.rms.meditor.tools.*;
import com.montnets.emp.rms.rmsapi.biz.impl.IRMSApiBiz;
import com.montnets.emp.rms.rmsapi.model.TempParams;
import com.montnets.emp.rms.tools.ZipTool;
import com.montnets.emp.rms.vo.CorpUserVo;
import com.montnets.emp.rms.vo.UserDataVO;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RCOS平台企业侧模板同步线程
 */
public class SyncEcTemplateThread implements Runnable {
    private static final String TEMPLATEPATH = "file/templates/";
    private static final String TMPPATH = "ueditor/jsp/upload/";
    /**
     * 上传的服务器IP地址
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
     * Map<String,Long> String-企业编码，Long -该企业返回没有模板可同步的时间戳
     */
    private Map<String, Long> synCorpMap = new HashMap<String, Long>();

    @Override
    public void run() {

        while (true) {
            try {
                //记录上次同步ID
                long preTemplateId = 0L;
                SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
                //1.获取需要同步模板的企业
                List<CorpUserVo> corps = synTemplateBiz.getCorps();
                if (corps != null && corps.size() > 0) {
                    String userId = "CSD008";
                    String pass = "123456";
                    for (CorpUserVo corp : corps) {
                        //该企业没有可同步的模板并且在重试时间间隔外，就不进行模板同步；synCorpMap 存储的是满足没有模板可同步的企业
                        if (synCorpMap.get(corp.getCorpCode()) != null && !getInternal(synCorpMap.get(corp.getCorpCode()))) {
                            continue;
                        }
                        //2.根据企业获取SP账号
                        UserDataVO spUser = new UserDataDAO().getSPUser(corp.getCorpCode());
                        if (spUser != null) {
                            userId = spUser.getUserId();
                            pass = spUser.getPassWord();
                            String spTemplateId = String.valueOf(getEcTemplateFromRcos(corp.getCorpCode()));
                            //是否公共模板 0-否，1-是
                            int isMaterial = 0;
                            //下次同步时找模板表和同步模板表中模板ID最大的
                            spTemplateId = String.valueOf(synchBiz.syncTempBeforeDeal(Long.parseLong(spTemplateId)));
                            List<LfTempSynch> synFailTemps = synchBiz.findSynFailTemp(isMaterial);
                            if (synFailTemps != null && synFailTemps.size() > 0) {
                                for (LfTempSynch lfTempSynch : synFailTemps) {
                                    //入库模板同步表[LF_TEMP_SYNCH]的模板ID[SP_TEMPLID]
                                    Long syncId = lfTempSynch.getSpTemplateid();
                                    /**
                                     *  企业定制模板 同步失败的模板，需要进行减1进行同步
                                     *  同步流程：
                                     *  如使用800013701 这个模板ID去同步，假使RSC平台有800013702，800013703，800013704....等模板需要同步，
                                     *  本次请求会返回800013702这个模板ID的数据，当800013702返回的数据出现异常，导致同步失败的是否，下次查询出来的
                                     *  异常模板就是800013702，需要再次进行同步，故需要减1
                                     */

                                    long tmpid = lfTempSynch.getSpTemplateid().longValue() - 1;
                                    synEcChronizeTemp(userId, pass, String.valueOf(tmpid), corp.getCorpCode(), corp.getUserId(), syncId);
                                }
                            }
                            if (spTemplateId.equals(String.valueOf(preTemplateId))) {
                                spTemplateId = String.valueOf(preTemplateId + 1);
                            }
                            preTemplateId = Long.parseLong(spTemplateId);
                            synEcChronizeTemp(userId, pass, spTemplateId, corp.getCorpCode(), corp.getUserId(), preTemplateId);
                        } else {
                            EmpExecutionContext.error("企业编码：" + corp.getCorpCode() + "，同步RCOS平台企业侧模板，没有可从同步的SP账号！！！");
                            //没有账号时，休息一分钟
                            Thread.sleep(60 * 1000L);
                        }
                    }
                }
                boolean synFlag = false;
                //循环所有 企业 和未有可同步模板的企业， 当所有企业都没有模板可同步，就休息 INTEENAL 时间
                for (Map.Entry<String, Long> entry : synCorpMap.entrySet()) {
                    synFlag = getInternal(synCorpMap.get(entry.getKey()));
                    //如果有要同步的，直接终止循环，继续同步
                    if (synFlag) {
                        break;
                    }
                }
                //如果没有要同步的，睡眠一段时间
                if (!synFlag && synCorpMap.size() > 0) {
                    Thread.sleep(INTEENAL * 1000L);
                    synCorpMap = null;
                    synCorpMap = new HashMap<String, Long>();
                }

                Thread.sleep(3000);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "RCOS平台企业侧模板同步线程处理异常");
                try {
                    Thread.sleep(5000);
                }catch (Exception e1){
                    EmpExecutionContext.error(e1, "RCOS平台企业侧模板同步线程处理异常中休眠异常");
                }
            }
        }

    }

    /**
     * 判断没有可同步的模板时间点 + 模板重试时间间隔 是到达当前时间
     *
     * @param noTemplateTm
     * @return
     */
    public boolean getInternal(long noTemplateTm) {
        long currentTm = System.currentTimeMillis();
        noTemplateTm = noTemplateTm + INTEENAL * 1000;
        if (currentTm >= noTemplateTm) {
            //要同步
            return true;
        } else {
            //不同步
            return false;
        }
    }


    /**
     * 从RSC 下载RCOS创建的模板
     *
     * @param userId
     * @param pwd
     * @param spTemplateId
     */
    public void synEcChronizeTemp(String userId, String pwd, String spTemplateId, String corpCode, long sysUserId, Long syncId) {
        try {
            //isMaterial = 0-企业定制模板
            synchBiz.syncTempInfo(syncId, 0);
            Map<String, Object> templateMap = irmsApiBiz.syncEcTemplateFromRCOS(userId, pwd, spTemplateId);
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
                    boolean falg = analysisTemplateData(result, spTemplateId, sysUserId, corpCode);
                    if (falg) {
                        LinkedHashMap contionMap = new LinkedHashMap();
                        contionMap.put("sptemplid", spTemplateId);
                        LfTemplate lfTemplates = synTemplateBiz.getTemplate(spTemplateId, null);
                        if (null != lfTemplates) {
                            synchBiz.syncTempSucceedDeal(spTemplateId);
                            //synchBiz.changeSynStatus(Long.parseLong(spTemplateId), 1);
                        }
                        //每个模板休息半秒中
                        Thread.sleep(500);
                        //synEcChronizeTemp(userId,pwd,spTemplateId,corpCode,sysUserId,syncId);
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "同步企业侧RCOS模板出现异常");
        }
    }

    /**
     * 存储来自RCOS的模板
     *
     * @return
     */
    public Map<String, String> getContent(Map<String, Object> templateMap, String spTemplateId, String corpCode) {
        String result = "";
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            if (null != templateMap && templateMap.size() > 0) {
                // result = 0 成功同步模板
                if (null != templateMap.get("result") && !"0".equals(templateMap.get("result").toString())) {
                    //没同步成功的情况都放到休息Map中
                    synCorpMap.put(corpCode, System.currentTimeMillis());
                }
                Map<String, Object> contentMap = (Map<String, Object>) templateMap.get("template");
                if (contentMap != null && StringUtils.isNotBlank(contentMap.get("content").toString()) && StringUtils.isNotBlank(contentMap.get("tmplid").toString())) {
                    JSONArray jsonArray = JSON.parseArray(contentMap.get("content").toString());
                    JSONObject object = (JSONObject) jsonArray.get(0);
                    result = new String(Base64.decodeBase64(object.get("content").toString().getBytes("UTF-8")), "UTF-8");
                    resultMap.put("result", result);
                    resultMap.put("tmplid", contentMap.get("tmplid").toString());
                }

            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取RCOS企业侧模板内容出现异常！");
        }
        return resultMap;
    }

    /**
     * 获取从RCOS同步到EMP模板表中的记录
     *
     * @return 每次需要同步的模板ID
     */
    public long getEcTemplateFromRcos(String corpCode) {
        long spTemplateId = 0;
        SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
        LfTemplate template = synTemplateBiz.getEcTemplateFromRcos(corpCode);
        if (null != template) {
            spTemplateId = template.getSptemplid();
            //0-企业侧定制模板
//            long failId =  synchBiz.findMaxFailTempId("0",SYNCOUNTS);
//            if(spTemplateId < failId){
//                spTemplateId = failId;
//            }
        }
        return spTemplateId;
    }

    /**
     * 入库RCOS平台同步过来的模板到LF_TEMPLATE
     */
    public void addTemplate(TempData tmpData, String frontJson, HashMap<String, String> srcMap, long tmId, LfSysuser sysuser) {
        SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
        frontJson = replaceSrc(frontJson, srcMap);
        //解析替换后的前端JSON，进行资源文件存储到 以tmid 为基础的文件路径
        JSONObject transObject = JSONObject.parseObject(frontJson);
        TempData transTmpData = JSONObject.toJavaObject(transObject, TempData.class);
        List<SubTempData> transSubData = transTmpData.getTempArr();

        // 子模板集合
        List<LfSubTemplate> subTempList = new ArrayList<LfSubTemplate>();
        //参数实体类
        List<LfTempParam> paramList = new ArrayList<LfTempParam>();

        //参数表实体类赋值
        combineParam(tmId, tmpData, paramList);
        //子表权重
        int priority = 0;
        //H5相关的appContent
        String appContent = "";
        //档位
        int degree = 0;
        //容量
        int degreeSize = 0;
        for (SubTempData subData : transSubData) {
            priority++;
            //组网关所需的byte[]
            if (null != subData) {
                Integer type = subData.getTmpType();
                switch (type) {
                    case 11:
                        //富媒体
                        Map<String, Object> richMap = analysisRichMedia(transTmpData, subData, tmId, sysuser, priority, "zh_CN", paramList, subTempList);
                        if (11 == transTmpData.getTmpType().intValue()) {//获取主方式的档位和容量
                            degree = (Integer) richMap.get("degree");
                            degreeSize = (Integer) richMap.get("degreeSize");
                        }
                        break;
                    case 12:
                        // 解析存储到临时目录
                        Map<String, Object> cardMap = analysisCard(transTmpData, subData, tmId, sysuser, priority, paramList, subTempList);
                        if (12 == tmpData.getTmpType().intValue()) {//获取主方式的档位和容量
                            degree = (Integer) cardMap.get("degree");
                            degreeSize = (Integer) cardMap.get("degreeSize");
                        }
                        break;
                    case 13:
                        // 富文本
                        Map<String, Object> richTextMap = analysisRichText(transTmpData, subData, tmId, sysuser, priority, "zh_CN", paramList, subTempList);
                        degree = (Integer) richTextMap.get("degree");
                        degreeSize = (Integer) richTextMap.get("degreeSize");
                        break;
                    case 14:
                        // 短信
                        analysisSms(transTmpData, subTempList, subData, priority, paramList);
                        break;
                    case 15:
                        //H5
                        break;
                    default:
                        break;
                }

            }

        }
        //入库LF_SUB_TEMPLATE,LF_TEMPCONTENT,LF_TEMPPARAM,
        if (tmId > 0) {
            int subCount = 0;
            for (com.montnets.emp.rms.meditor.entity.LfSubTemplate LfSubTemplate : subTempList) {
                LfSubTemplate.setTmId(tmId);
                LfSubTemplate.setCardHtml(" ");
                String frontCont = LfSubTemplate.getFrontJson();
                String endCont = LfSubTemplate.getEndJson();
                // 模板子表入库
                //子表的这三个字段因为模板结构字符串超过数据库该字段的实际存储结构，改为存储到 LF_TEMPCONTENT 拆分表中
                LfSubTemplate.setFrontJson(" ");
                LfSubTemplate.setContent(" ");
                LfSubTemplate.setEndJson(" ");
                LfSubTemplate.setH5Url(" ");
                boolean save = synTemplateBiz.addLfSubTemplate(LfSubTemplate);
                // 模板结构JSON拆分存储表入表 LF_TEMPCONTENT
                if (save) {
                    subCount++;
                    saveLftempContent(frontCont, endCont, LfSubTemplate, tmId, appContent);
                }
            }
            //入参数表 LF_TEMPPARAM
            if (tmId > 0 && subCount == subTempList.size()) {
                if (null != paramList && paramList.size() > 0) {
                    for (LfTempParam lfTempParam : paramList) {
                        synTemplateBiz.addLfTempParam(lfTempParam);
                    }
                }
            }
        }

        //入库EXTJSON
        Locale locale = new Locale("zh", "CN");
        transTmpData.setTmid(tmId);
        ParamTool.saveExcleJson(transTmpData, locale);

        //更新主模板表
        updateLfTemplate(String.valueOf(tmId), degree, degreeSize);
    }

    /**
     * 更新模板表LF_TEMPLATE TM_MSG 字段
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
     * 解析转换的前端JSON,存储资源文件到临时目录
     *
     * @param frontJson 前端JSON
     */
    public boolean analysisTemplateData(String frontJson, String spTemplateId, long sysUserId, String corpCode) {
        boolean flag = false;
        long tmId = 0L;
        try {
            SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
            //前端JSON
            JSONObject object = JSONObject.parseObject(frontJson);
            TempData tmpData = JSONObject.toJavaObject(object, TempData.class);
            List<SubTempData> subDatas = tmpData.getTempArr();
            LfSysuser sysuser = new LfSysuser();
            sysuser.setUserId(sysUserId);
            sysuser.setCorpCode(corpCode);
            //入库主表返回自增ID
            LfTemplate lfTemplate = combineLFtemplate(sysuser, tmpData, spTemplateId);
            tmId = synTemplateBiz.addTemplate(lfTemplate);
            //需要替换的srcMap
            HashMap<String, String> srcMap = new HashMap<String, String>();
            //此循环用于解析资源文件的二进制流存储到临时目录--TMPPATH:  ueditor/jsp/upload/
            for (SubTempData subData : subDatas) {
                //组网关所需的byte[]
                if (null != subData) {
                    Integer type = subData.getTmpType();
                    switch (type) {
                        case 11:
                            //富媒体
                            anaylysisRichMediaToTmp(tmpData, subData, srcMap);
                            break;
                        case 12:
                            // 解析存储到临时目录
                            anaylysisCardToTmp(subData, srcMap);
                            break;
                        case 13:
                            // 富文本
                            break;
                        case 14:
                            // 短信
                            break;
                        case 15:
                            //H5
                            break;
                        default:
                            break;
                    }
                }
            }
            //入 LF_TEMPLATE ,LF_SUB_TEMPLATE,LF_TEMPCONTENT,LF_TEMPPARAM 表
            addTemplate(tmpData, frontJson, srcMap, tmId, sysuser);

            //压缩资源文件
            String sourcePath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmId;
            String zipPath = new File(sourcePath + ".zip").getPath();
            //压缩 绝对路径
            ZipTool.createZip(sourcePath, zipPath);
            String fileCenterPath = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmId + ".zip";
            //上传本地资源到文件服务器
            FileOperatorTool.uploadFileCenter(fileCenterPath);
            // 更新模板表LF_TEMPLATE TM_MSG 字段
            updateLfTemplate(String.valueOf(tmId), fileCenterPath);
            flag = true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "解析RCOS同步过来的企业定制场景模板数据出现异常");
            //解析模板资源出错处理
            synchBiz.analysisTempError(Long.parseLong(spTemplateId));
            //删除入库的异常数据
            String ids = String.valueOf(tmId);
            try {
                baseBiz.deleteByIds(LfTemplate.class, ids);
            } catch (Exception e1) {
                EmpExecutionContext.error(e1, "企业定制场景：删除解析异常的模板记录出现异常");
            }
        }
        return flag;
    }

    /**
     * 入库报文JSON结构拆分表
     *
     * @param frontCont
     * @param endCont
     * @param lfSubTemplate
     */
    public void saveLftempContent(String frontCont, String endCont, LfSubTemplate lfSubTemplate, Long tmid, String appContent) {
        List<String> frontContList = StringUtils.strToList(frontCont);
        List<String> endContList = StringUtils.strToList(endCont);
        SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
        //前端JSON入库LF_TempContent 表
        try {
            if (frontContList != null) {
                for (String content : frontContList) {
                    LfTempContent LftempContent = new LfTempContent();
                    LftempContent.setTmId(tmid);
                    LftempContent.setTmpType(lfSubTemplate.getTmpType());
                    LftempContent.setTmpContent(content);
                    LftempContent.setContType(1);//1-前端JSON
                    synTemplateBiz.addLfTempContent(LftempContent);
                }
            }
            if (endContList != null) {
                for (String content : endContList) {
                    LfTempContent LftempContent = new LfTempContent();
                    LftempContent.setTmId(tmid);
                    LftempContent.setTmpType(lfSubTemplate.getTmpType());
                    LftempContent.setTmpContent(content);
                    LftempContent.setContType(2);//2-终端JSON
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
                        LftempContent.setContType(3);//3-appContent H5 封面JSON
                        synTemplateBiz.addLfTempContent(LftempContent);
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "入库LF_TEMPCONTENT 表出现异常.");
        }
    }


    /**
     * 组合参数表字段参数
     *
     * @param tmpData
     * @param paramList
     */
    private void combineParam(long tmid, TempData tmpData, List<LfTempParam> paramList) {
        if (null != tmpData && null != tmpData.getParamArr()) {
            List<TempDataParam> paramArr = tmpData.getParamArr();
            for (TempDataParam param : paramArr) {
                LfTempParam lfTempParam = new LfTempParam();
                //参数类型
                int type = 1;//默认1：纯数字类型
                if (null != param.getType()) {
                    type = param.getType();
                }
                //参数名
                String name = "";
                if (null != param.getName()) {
                    name = param.getName();
                }
                //是否定长 0可变 1固定
                int lengthRestrict = 0;
                if (null != param.getLengthRestrict()) {
                    lengthRestrict = param.getLengthRestrict();
                }
                //固定长度
                int fixLength = 0;
                if (null != param.getFixLength()) {
                    fixLength = param.getFixLength();
                }
                //最小长度
                int minLength = 0;
                if (null != param.getMinLength()) {
                    minLength = param.getMinLength();
                }
                //最大长度
                int maxLength = 0;
                if (null != param.getMaxLength()) {
                    maxLength = param.getMaxLength();
                }
                // 是否有参数长度限制 0-false ,1-true
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
                //是否有参数限制
                lfTempParam.setHasLength(hasLength);
                paramList.add(lfTempParam);
            }
        }
    }

    /**
     * 场景 替换src的文件流二进制 为实际的临时存储目录
     *
     * @param frontJson RCOS 同步来的前端JSON
     * @param srcMap    需要替换的src 集合
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
     * 富媒体 替换src的文件流二进制 为实际的临时存储目录
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
                    //文字配图片
                    if (jsonObject.containsKey("type") && jsonObject.getString("type").equals("text")) {
                        if (!StringUtils.isBlank(jsonObject.getJSONObject("image").getString("src"))) {
                            //图片
                            String folderType = "image";
                            JSONObject imgObject = jsonObject.getJSONObject("image");
                            storeRichMediaTmpFile(imgObject, folderType, srcMap);
                            if (null != imgObject.getJSONArray("textEditable")) {//图片上还配有图片
                                JSONArray imgArr = imgObject.getJSONArray("textEditable");
                                for (int j = 0; j < imgArr.size(); j++) {
                                    JSONObject imgObj = imgArr.getJSONObject(j);
                                    if (!StringUtils.isBlank(imgObj.getString("src"))) {
                                        storeRichMediaTmpFile(imgObj, folderType, srcMap);
                                    }
                                }
                            }
                        }
                    } else if (jsonObject.containsKey("type") && jsonObject.getString("type").equals("image")) {
                        //图片
                        String folderType = "image";
                        storeRichMediaTmpFile(jsonObject, folderType, srcMap);
                        if (null != jsonObject.getJSONArray("textEditable")) {//图片上还配有图片
                            JSONArray imgArr = jsonObject.getJSONArray("textEditable");
                            for (int j = 0; j < imgArr.size(); j++) {
                                JSONObject imgObj = imgArr.getJSONObject(j);
                                if (!StringUtils.isBlank(imgObj.getString("src"))) {
                                    storeRichMediaTmpFile(imgObj, folderType, srcMap);
                                }
                            }
                        }
                    } else if (jsonObject.containsKey("type") && jsonObject.getString("type").equals("audio")) {
                        //音频
                        String folderType = "audio";
                        storeRichMediaTmpFile(jsonObject, folderType, srcMap);

                    } else if (jsonObject.containsKey("type") && jsonObject.getString("type").equals("video")) {
                        //视屏
                        String folderType = "video";
                        storeRichMediaTmpFile(jsonObject, folderType, srcMap);
                    }
                }
            }
        }
    }

    /**
     * 解析场景编辑器的模板资源存储到临时目录
     */
    public void anaylysisCardToTmp(SubTempData subData, HashMap<String, String> srcMap) {
        //前端对象实体
        if (null != subData) {
            TempContent tempContent = JSONObject.parseObject(subData.getContent().toString(), TempContent.class);
            //复制组件中的所有资源
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
                EmpExecutionContext.error("删除文件失败！");
            }
        }
    }

    /**
     * 将RCOS平台同步过来的模板中的资源文件的二进制流写入为文件存储到本地临时目录
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

            //src中二进制原值
            String source = jsonObject.getString("src");
            //src中替换后的路径
            String dest = imageName.substring(imageName.indexOf(TMPPATH), imageName.length());
            srcMap.put(source, dest);
        } catch (Exception e) {
            if (imgByte == null || imgByte.length == 0) {
                EmpExecutionContext.error(e, "RCOS企业侧模板-富媒体消息--资源文件中的src为空值！！！");
            }
            EmpExecutionContext.error(e, "解析资源文件中的src base64字节出现异常");
        }
    }

    /**
     * 将RCOS平台同步过来的模板中的资源文件的二进制流写入为文件存储到本地临时目录
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

            //src中二进制原值
            String source = element.getSrc();
            //src中替换后的路径
            String dest = imageName.substring(imageName.indexOf(TMPPATH), imageName.length());
            srcMap.put(source, dest);
        } catch (Exception e) {
            if (imgByte == null || imgByte.length == 0) {
                EmpExecutionContext.error(e, "RCOS企业侧模板-场景消息-资源文件中的src为空值！！！");
            }
            EmpExecutionContext.error(e, "解析资源文件中的src base64字节出现异常");
        }
    }


    /**
     * 组装模板表字段参数
     *
     * @param sysuser
     * @param tmpData
     * @return
     */
    private LfTemplate combineLFtemplate(LfSysuser sysuser, TempData tmpData, String spTemplateId) {
        LfTemplate template = new LfTemplate();
        // 模板实体类赋值
        template.setTmCode(" ");
        // 是否审核(无需审核-0，未审核-1，同意1，拒绝2)
        template.setIsPass(-1);
        // 模板状态（0无效，1有效，2草稿）
        if (1 == tmpData.getSubType()) {
            template.setTmState(1L);
        } else {//暂存草稿
            template.setTmState(2L);
        }
        // 添加时间
        template.setAddtime(Timestamp.valueOf(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date())));
        // 模板内容,添加的时候还未生成，直接填空字符串
        template.setTmMsg(" ");
        // 操作员ID
        template.setUserId(sysuser.getUserId());
        template.setCorpCode(sysuser.getCorpCode());
        // 模板（3-短信模板;4-彩信模板；11-富信模板）
        //富文本、富媒体、卡片、H5的类型主表都改为11，真正的类型放到LF_SUB_TEMPLATE 表中根据 TMP_TYPE 来判断，PRIORITY =0 为编辑器的主方式
        template.setTmpType(11);
        // 动态模板
        //参数实际数量
        int paramCnt = 0;
        //模板类型：0-静态，1-动态
        long dsFlag = 0L;
        if (null != tmpData.getParamArr() && tmpData.getParamArr().size() > 0) {
            paramCnt = tmpData.getParamArr().size();
            dsFlag = 1L;
        } else {// 静态模板
            paramCnt = 0;
            dsFlag = 0L;
        }
        template.setParamcnt(paramCnt);
        template.setDsflag(dsFlag);
        //是否公共场景 0-我的场景，1-公共场景
        //为企业定制的为我的场景
        //20190107又改为了公共场景，不知什么时候又会改回来
        template.setIsPublic(1);
        //模板使用次数
        template.setUsecount(0L);
        //V2.0 时给模板发送时使用的校验参数 JSON串
        template.setExlJson(" ");
        // 网关审核状态 -1：未审批，0：无需审批，1：同意，2：拒绝，3：审核中
        template.setAuditstatus(3);
        ////网关彩信模板状态（0：正常，可用	1：锁定，暂时不可用	2：永久锁定，永远不可用）
        template.setTmplstatus(0);

        //用途ID,默认-2
        int useID = -2;
        if (null != tmpData.getUseId()) {
            useID = tmpData.getUseId();
        }
        template.setUseid(useID);
        //行业ID 默认-1
        int industryID = -1;
        if (null != tmpData.getIndustryId()) {
            industryID = tmpData.getIndustryId();
        }
        template.setIndustryid(industryID);
        //版本号
        template.setVer("V3.0");
        //提交状态 	0：未提交	1：提交成功	2：提交失败	3；提交中 --彩信时就有的字段，我也不知道干啥用的
        template.setSubmitstatus(0);
        //是否快捷场景 0-否
        template.setIsShortTemp(0);
        //模板主题名
        String tmName = StringUtils.isBlank(tmpData.getTmName()) ? " " : tmpData.getTmName();
        template.setTmName(tmName);
        template.setSptemplid(Long.parseLong(spTemplateId));
        //同步过来的模板来源为RCOS - 3
        template.setSource(3);
        //是否RCOS同步来的公共素材 0 -否，1-是
        template.setIsMaterial(0);
        // RCOS 为企业定制的模板默认是禁用的 ： 0-禁用，1-启用
        template.setRcosTmpState(0);
        return template;

    }

    /**
     * H5编辑前端报文解析
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
            //H5编辑器的tempArr 只有一种编辑方式，短信补充方式由 标题+ 链接 自动生成
            String corpCode = sysuser.getCorpCode();
            JSONObject object = (JSONObject) tmpData.getTempArr().get(0).getContent();
            String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/";
            String h5path = new TxtFileUtil().getWebRoot() + "templates/" + corpCode;
//                            //HTML存储本地
//                            new FileOperatorTool().writeFile(srcPath + "/" + tmid + ".html", h5Html);
            new JsonParseResourceTool().parseH5Html(object, h5path + "/" + tmid + ".html", new StringBuilder());
            // 上传资源服务器，返回url
            File h5File = new File(h5path + "/" + tmid + ".html");
            String btype = "2";//1-上传模板
            String ftype = "1";//上传的文件格式：1-html
            String h5URL = String2FileUtil.uploadFile(h5File, "templates/" + corpCode, btype, ftype);
            h5URL = uploadPath + rootPath + h5URL;
            //前端H5报文 转为手机终端需要的对象
            MainTemplate dataParsToTemp = TemplateUtil.hFiveToTerminal(tmpData, h5URL, new StringBuilder());
            dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
            String endJson = JSONObject.toJSONString(dataParsToTemp);
            //写ott卡片模板结构文件
            String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/src/ott.mrcsl";
            new FileOperatorTool().writeFile(mcrslUrl, endJson);

            // 存储源文件
            String frontJson = JSONObject.toJSONString(subData.getContent());
            //存储资源文件
            String appContent = JSONObject.toJSONString(tmpData.getApp());
            //H5 替换的是appContent 中封面的src
            Map<String, Object> resultMap = new JsonParseResourceTool().storeH5ResourceFile(corpCode, tmid, tmpData, appContent, subType);
            appContent = (String) resultMap.get("frontJson");
            String fileUrl = TEMPLATEPATH + corpCode + "/" + tmid + "/ott/fuxin.mrcsl";
            //组子表数据

            combineSubLftemplate(tmpData, subData, subTempList, priority, frontJson, fileUrl, endJson);
            // 读取模板源文件字节流
            srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/";
            Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath + "src/");
            if (tmpData.getSubType() == 1) {//提交审核 才进行组流文件操作
                ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
            } else {
                ottBytes = " ".getBytes();

            }
            //在本地生成流文件
            String fuxinMrcsl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/fuxin.mrcsl";
            new FileOperatorTool().writeFile(fuxinMrcsl, ottBytes);
            //根据 标题 、链接 生成短信---- 标题，详情请点击+链接
            String titleTxt = tmpData.getApp().getTitle().getText();
            String smContent = titleTxt + "，详情请点击 " + h5URL;
            h5Map.put("smContent", smContent);
            h5Map.put("h5URL", h5URL);
            h5Map.put("appContent", appContent);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "H5编辑前端报文解析-上传H5资源中心出现异常");
        }
        return h5Map;
    }


    /**
     * 短信补充方式报文分析
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
     * 富文本补充方式前端报文解析
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
        //转换终端JSON
        String frontJson = JSONObject.toJSONString(subData.getContent());
        String fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        combineSubLftemplate(tmpData, subData, subTempList, priority, frontJson, fileUrl, " ");
        MainTemplate dataParsToTemp = TemplateUtil.dataParsToTemp(tmpData, new StringBuilder());
        dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
        String endJson = JSONObject.toJSONString(dataParsToTemp);
        endJson = new JsonParseResourceTool().replaceDiv(endJson);
        //写ott卡片模板结构文件
        String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/ott.mrcsl";
        new FileOperatorTool().writeFile(mcrslUrl, endJson);
        //读取模板源文件字节流
        String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/";
        Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath);
        if (tmpData.getSubType() == 1) {//提交审核 才进行组流文件操作
            ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
        } else {
            ottBytes = " ".getBytes();

        }
        //组装网关
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
     * 卡片前端报文解析
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
        //前端JSON
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
        //写ott卡片模板结构文件
        String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/ott.mrcsl";
        new FileOperatorTool().writeFile(mcrslUrl, endJson);

        // 存储源文件
        TempContent tempContent = JSONObject.parseObject(subData.getContent().toString(), TempContent.class);
        String frontJson = JSONObject.toJSONString(subData.getContent());
        Map<String, Object> resultMap = new JsonParseResourceTool().storeOttResourceFile(sysuser.getCorpCode(), tmid, tempContent, frontJson, subType);
        String json = (String) resultMap.get("frontJson");
        String fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        combineSubLftemplate(tmpData, subData, subTempList, priority, json, fileUrl, " ");
        // 读取模板源文件字节流
        String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/";
        Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath + "src/");
        if (tmpData.getSubType() == 1) {//提交审核 才进行组流文件操作
            ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
        } else {
            ottBytes = " ".getBytes();

        }
        //组装网关
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
     * 富媒体补充方式前端报文解析
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
        //前端JSON
        String frontJson = "";
        byte[] rmsBytes = null;
        byte[] ottBytes = null;
        int degree = 0;
        int degreeSize = 0;
        int pnum = 0;
        boolean subGwFlag = false;
        String mcrslUrl = "";
        //操作类型-0：保存，1-提交审核
        int subType = tmpData.getSubType();
        Map<String, Object> richMap = new HashMap<String, Object>();
        //存储资源文件
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
        if (tmpData.getSubType() == 1) {//提交审核 才进行组流文件操作
            rmsBytes = new RmsFileBytesTool().getRmsFileBytes(targetPath + "/src", title, list);
        } else {
            rmsBytes = " ".getBytes();
        }
        //组装网关
        degree = null == subData.getDegree() ? 0 : subData.getDegree();
        degreeSize = rmsBytes.length;
        pnum = paramList.size();
        String fuxinRms = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms/fuxin.rms";
        new FileOperatorTool().writeFile(fuxinRms, rmsBytes);
        //上传H5资源服务器
        mcrslUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms/src/fuxin.rms";
        richMap.put("degree", degree);
        richMap.put("degreeSize", degreeSize);
        return richMap;
    }


    /**
     * 组装子模板表参数值
     *
     * @param tmpData
     * @param subData
     * @param subTempList
     */
    private void combineSubLftemplate(TempData tmpData, SubTempData subData,
                                      List<LfSubTemplate> subTempList, int priority, String frontJson, String filrUrl, String endJson) {
        // 子表实体类赋值
        LfSubTemplate subTemplate = new LfSubTemplate();
        subTemplate.setTmpType(subData.getTmpType());
        subTemplate.setAddTime(Timestamp.valueOf(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date())));
        subTemplate.setContent(frontJson);
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
     * 将收到的RCOS资源文件进行本地临时存储
     *
     * @param path    资源文件存储路径
     * @param imgByte 资源文件二进制内容
     */
    public static void writeInFileByfi(String path, byte[] imgByte) {
        File f = new File(path);
        FileOutputStream fos = null;
        try {
            File folder = new File(f.getParent());
            if (!folder.exists()) {//新文件夹
                folder.mkdirs();
            }
            if (!f.exists()) {
                boolean flag = f.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
            }
            fos = new FileOutputStream(f);
            fos.write(imgByte);
        } catch (IOException e) {
            EmpExecutionContext.error(e, "写文件出现异常...");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "关闭流出现异常");
                }
            }
        }

    }

    /**
     * 更新 档位和容量
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
