package com.montnets.emp.rms.meditor.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.rms.commontempl.entity.FrameParam;
import com.montnets.emp.rms.commontempl.entity.LfTemplate;
import com.montnets.emp.rms.meditor.entity.*;
import com.montnets.emp.util.StringUtils;

import java.util.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:yangdl
 * @Data:Created in 16:04 2018.8.9 009
 */
public class ParamTool {
    private static final BaseBiz baseBiz = new BaseBiz();

    /**
     * 根据rms模板内容解析出所含参数
     *
     * @param ottJson
     * @param rmsJson
     * @return
     */
    public static LinkedHashMap<String, ArrayList<String>> convertParamRms(String ottJson, String rmsJson, Locale locale) {
        //map：String为每一帧的参数类型拼上当前帧数，arraylist中是本帧所含参数，无参时为空
        LinkedHashMap<String, ArrayList<String>> map = new LinkedHashMap<String, ArrayList<String>>();
        if (StringUtils.isNotEmpty(rmsJson)) {
            //将模板内容转为map类型的list集合
            List<LinkedHashMap> jsonArray = JSONArray.parseArray(rmsJson, LinkedHashMap.class);
            //类型
            String type = "";
            ArrayList<String> list;
            String mapType;
            String text;
            String s;
            //遍历每一个map
            for (int i = 1; i <= jsonArray.size(); i++) {
                list = new ArrayList<String>();
                LinkedHashMap<String, String> jsonMap = jsonArray.get(i - 1);
                //获取类型与文本内容
                mapType = jsonMap.get("type");
                text = jsonMap.get("text");
                if (StringUtils.isNotEmpty(text)) {
                    //当模板类型为文本图文与报表时拼接相应的帧数，从1开始
                    if ("text".equals(mapType)) {
                        type = MessageUtils.getWord("common", locale, "common_send_13") + i;
                    } else if ("image".equals(mapType)) {
                        type = MessageUtils.getWord("common", locale, "common_send_14") + i;
                    } else if ("chart".equals(mapType)) {
                        type = MessageUtils.getWord("common", locale, "common_send_15") + i;
                    }
                    //String regex = "\\{#参数\\d+#\\}";
                    Pattern pattern = Pattern.compile(MessageUtils.getWord("common", locale, "common_send_16"));
                    Matcher matcher = pattern.matcher(text);
                    //正则匹配发现文本含有参数时取其加入到参数集合
                    while (matcher.find()) {
                        s = matcher.group();
                        list.add(s);
                    }
                    //参数集合不为空时将本帧参数集合加入map
                    if (list.size() != 0) {
                        map.put(type, list);
                    }
                }
            }
        }
        //动态二维码
        HashMap<String, Object> ottmap = new HashMap<String, Object>();
        ottmap.put("content", ottJson);
        ottmap.put("type", 12);
        Object[] ottarr = {ottmap};
        String ottJsonStr = JSONObject.toJSONString(ottarr);
        List<TempData> tempData = TemplateUtil.parseToData(ottJsonStr);
        if (tempData.size() != 0) {
            TempCardData cardData = (TempCardData) tempData.get(0);
            if (cardData != null) {
                TempContent content = cardData.getContent();
                if (content != null) {
                    TempDataElement elements = content.getElements();
                    if (elements != null) {
                        List<TempElement> qrcodes = elements.getQrcodes();
                        ArrayList<String> list = new ArrayList<String>();
                        String codeType;
                        String qr;
                        for (int i = 0; i < qrcodes.size(); i++) {
                            TempElement tempElement = qrcodes.get(i);
                            codeType = tempElement.getCodeType();
                            if ("1".equals(codeType)) {
                                qr = tempElement.getName();
                                list.add(qr);
                            }
                        }
                        map.put("二维码", list);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 获取rms参数信息
     *
     * @param ottJson
     * @param rmsJson
     * @return
     */
    public static String getFrameParamRms(String ottJson, String rmsJson, Locale locale) {
        //参数对象集合
        List<FrameParam> frameList = new ArrayList<FrameParam>();
        //参数总个数
        int allParamCount = 0;
        if (StringUtils.isNotEmpty(rmsJson)) {
            List<LinkedHashMap> jsonArray = JSONArray.parseArray(rmsJson, LinkedHashMap.class);
            //遍历每一帧内容
            for (int i = 1; i <= jsonArray.size(); i++) {
                LinkedHashMap<String, String> jsonMap = jsonArray.get(i - 1);
                //每一个参数对象
                FrameParam frameParam = new FrameParam();
                String mapType = jsonMap.get("type");
                String text = jsonMap.get("text");
                //2、文本参数3、图片配文参数,5、报表配文参数
                Integer paramType = 0;
                if (StringUtils.isNotEmpty(text)) {
                    if ("text".equals(mapType)) {
                        paramType = 2;
                    } else if ("image".equals(mapType)) {
                        paramType = 3;
                    } else if ("chart".equals(mapType)) {
                        paramType = 5;
                    }
                    //String regex = "\\{#参数\\d+#\\}";
                    Pattern pattern = Pattern.compile(MessageUtils.getWord("common", locale, "common_send_16"));
                    Matcher matcher = pattern.matcher(text);
                    //参数
                    String paramValue = "";
                    //参数个数
                    Integer paramCount = 0;
                    while (matcher.find()) {
                        String s = matcher.group();
                        paramValue += s + ",";
                        paramCount += 1;
                    }
                    if (!"".equals(paramValue)) {
                        int j = paramValue.lastIndexOf(",");
                        paramValue = paramValue.substring(0, j);
                    }
                    frameParam.setParamCount(paramCount);
                    frameParam.setParamValue(paramValue);
                    //i为当前帧数
                    frameParam.setFrameIndex(i);
                    frameParam.setType(paramType);
                    if (paramCount != 0) {
                        frameList.add(frameParam);
                        allParamCount += paramCount;
                    }
                }
            }
        }
        //动态二维码
        HashMap<String, Object> ottmap = new HashMap<String, Object>();
        ottmap.put("content", ottJson);
        ottmap.put("type", 12);
        Object[] ottarr = {ottmap};
        String ottJsonStr = JSONObject.toJSONString(ottarr);
        List<TempData> tempData = TemplateUtil.parseToData(ottJsonStr);
        if (tempData.size() != 0) {
            TempCardData cardData = (TempCardData) tempData.get(0);
            if (cardData != null) {
                TempContent content = cardData.getContent();
                if (content != null) {
                    TempDataElement elements = content.getElements();
                    if (elements != null) {
                        List<TempElement> qrcodes = elements.getQrcodes();
                        for (int i = 0; i < qrcodes.size(); i++) {
                            TempElement tempElement = qrcodes.get(i);
                            String codeType = tempElement.getCodeType();
                            if ("1".equals(codeType)) {
                                String qr = tempElement.getName();
                                FrameParam frameParam = new FrameParam();
                                frameParam.setParamValue(qr);
                                frameParam.setParamCount(1);
                                frameList.add(frameParam);
                                allParamCount += 1;
                            }
                        }
                    }
                }
            }
        }
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("allParamCount", String.valueOf(allParamCount));
        hashMap.put("totalFrame", frameList.size());
        hashMap.put("frameList", frameList);
        return JSON.toJSONString(hashMap);
    }

    /**
     * 获取ottfrontJson所含参数
     *
     * @param ottfrontJson
     * @return
     */
    public static LinkedHashMap<String, ArrayList<String>> convertParamOtt(String ottfrontJson, Locale locale) {
        LinkedHashMap<String, ArrayList<String>> map = new LinkedHashMap<String, ArrayList<String>>();
        if (StringUtils.isNotEmpty(ottfrontJson)) {
            HashMap<String, Object> ottmap = new HashMap<String, Object>();
            ottmap.put("content", ottfrontJson);
            ottmap.put("type", 12);
            Object[] ottarr = {ottmap};
            String ottJson = JSONObject.toJSONString(ottarr);
            List<TempData> tempData = TemplateUtil.parseToData(ottJson);
            if (tempData.size() != 0) {
                TempCardData cardData = (TempCardData) tempData.get(0);
                if (cardData != null) {
                    TempContent content = cardData.getContent();
                    if (content != null) {
                        TempDataElement elements = content.getElements();
                        if (elements != null) {
                            //文本参数
                            List<TempElement> elementsTexts = elements.getTexts();
                            ArrayList<String> list = new ArrayList<String>();
                            for (int i = 0; i < elementsTexts.size(); i++) {
                                TempElement tempElement = elementsTexts.get(i);
                                String text = tempElement.getText();
                                if (StringUtils.isNotEmpty(text)) {
                                    //String regex = "\\{#参数\\d+#\\}";
                                    Pattern pattern = Pattern.compile(MessageUtils.getWord("common", locale, "common_send_16"));
                                    Matcher matcher = pattern.matcher(text);
                                    while (matcher.find()) {
                                        String s = matcher.group();
                                        list.add(s);
                                    }
                                }
                            }
                            map.put(MessageUtils.getWord("common", locale, "common_send_13"), list);
                            //二维码参数
                            List<TempElement> qrcodes = elements.getQrcodes();
                            ArrayList<String> arrayList = new ArrayList<String>();
                            for (int i = 0; i < qrcodes.size(); i++) {
                                TempElement tempElement = qrcodes.get(i);
                                String codeType = tempElement.getCodeType();
                                if ("1".equals(codeType)) {
                                    String qr = tempElement.getName();
                                    arrayList.add(qr);
                                }
                            }
                            map.put(MessageUtils.getWord("common", locale, "common_send_17"), arrayList);
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 获取ott参数信息
     *
     * @param ottfrontJson
     * @return
     */
    public static String getFrameParamOtt(String ottfrontJson, Locale locale) {
        List<FrameParam> frameList = new ArrayList<FrameParam>();
        int allParamCount = 0;
        //获取卡片json的参数
        if (StringUtils.isNotEmpty(ottfrontJson)) {
            HashMap<String, Object> ottmap = new HashMap<String, Object>();
            ottmap.put("content", ottfrontJson);
            ottmap.put("type", 12);
            Object[] ottarr = {ottmap};
            String ottJson = JSONObject.toJSONString(ottarr);
            List<TempData> tempData = TemplateUtil.parseToData(ottJson);
            if (tempData.size() != 0) {
                TempCardData cardData = (TempCardData) tempData.get(0);
                if (cardData != null) {
                    TempContent content = cardData.getContent();
                    if (content != null) {
                        TempDataElement elements = content.getElements();
                        if (elements != null) {
                            //文本
                            List<TempElement> elementsTexts = elements.getTexts();
                            FrameParam frameParam = new FrameParam();
                            String paramValue = "";
                            Integer paramCount = 0;
                            for (int i = 0; i < elementsTexts.size(); i++) {
                                TempElement tempElement = elementsTexts.get(i);
                                String text = tempElement.getText();
                                if (StringUtils.isNotEmpty(text)) {
                                    // String regex = "\\{#参数\\d+#\\}";
                                    Pattern pattern = Pattern.compile(MessageUtils.getWord("common", locale, "common_send_16"));
                                    Matcher matcher = pattern.matcher(text);
                                    while (matcher.find()) {
                                        String s = matcher.group();
                                        paramValue += s + ",";
                                        paramCount += 1;
                                    }
                                }
                            }
                            if (!"".equals(paramValue)) {
                                int i = paramValue.lastIndexOf(",");
                                paramValue = paramValue.substring(0, i);
                            }
                            frameParam.setType(2);
                            frameParam.setParamCount(paramCount);
                            frameParam.setParamValue(paramValue);
                            if (paramCount != 0) {
                                frameList.add(frameParam);
                                allParamCount += paramCount;
                            }
                            //二维码
                            List<TempElement> qrcodes = elements.getQrcodes();
                            for (int i = 0; i < qrcodes.size(); i++) {
                                TempElement tempElement = qrcodes.get(i);
                                String codeType = tempElement.getCodeType();
                                if ("1".equals(codeType)) {
                                    String qr = tempElement.getName();
                                    FrameParam fp = new FrameParam();
                                    fp.setParamValue(qr);
                                    fp.setParamCount(1);
                                    frameList.add(fp);
                                    allParamCount += 1;
                                }
                            }
                        }
                    }
                }
            }
        }
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("allParamCount", String.valueOf(allParamCount));
        hashMap.put("totalFrame", frameList.size());
        hashMap.put("frameList", frameList);
        return JSON.toJSONString(hashMap);
    }

    /**
     * 获取富文本所含参数
     *
     * @param text
     * @return
     */
    public static LinkedHashMap<String, ArrayList<String>> convertParamRichText(String text, Locale locale) {
        LinkedHashMap<String, ArrayList<String>> map = new LinkedHashMap<String, ArrayList<String>>();
        ArrayList<String> list = new ArrayList<String>();
        if (!checkParam(text, locale)) {
            return map;
        }
        //String regex = "\\{#参数\\d+#\\}";
        Pattern pattern = Pattern.compile(MessageUtils.getWord("common", locale, "common_send_16"));
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String s = matcher.group();
            list.add(s);
        }
        map.put(MessageUtils.getWord("common", locale, "common_send_13"), list);
        return map;
    }

    /**
     * 获取富文本参数信息
     *
     * @param text
     * @return
     */
    public static String getFrameParamRichText(String text, Locale locale) {
        List<FrameParam> frameList = new ArrayList<FrameParam>();
        int allParamCount = 0;
        //每一个参数对象
        FrameParam frameParam = new FrameParam();
        if (StringUtils.isNotEmpty(text)) {
            //String regex = "\\{#参数\\d+#\\}";
            Pattern pattern = Pattern.compile(MessageUtils.getWord("common", locale, "common_send_16"));
            Matcher matcher = pattern.matcher(text);
            String paramValue = "";
            Integer paramCount = 0;
            while (matcher.find()) {
                String s = matcher.group();
                paramValue += s + ",";
                paramCount += 1;
            }
            if (!"".equals(paramValue)) {
                int i = paramValue.lastIndexOf(",");
                paramValue = paramValue.substring(0, i);
            }
            frameParam.setParamValue(paramValue);
            frameParam.setParamCount(paramCount);
            frameParam.setFrameIndex(1);
            frameParam.setType(7);
            if (paramCount != 0) {
                frameList.add(frameParam);
                allParamCount += paramCount;
            }
        }
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("allParamCount", String.valueOf(allParamCount));
        hashMap.put("totalFrame", frameList.size());
        hashMap.put("frameList", frameList);
        return JSON.toJSONString(hashMap);
    }

    /**
     * 提交模板至审核平台时生成exceljson参数信息
     *
     * @param tempData
     * @return
     */
    public static void saveExcleJson(TempData tempData, Locale locale) {
        boolean b = false;
        String excelJson = "";
        String tempStr = JSONObject.toJSONString(tempData);
        if (tempData == null || !checkParam(tempStr, locale)) {
            return;
        }
        try {
            Long tmid = tempData.getTmid();
            Integer tempType = tempData.getTmpType();
            List<SubTempData> tempArr = tempData.getTempArr();
            if (11 == tempType) {
                for (SubTempData subTempData : tempArr) {
                    Integer tmpType = subTempData.getTmpType();
                    String rmsFrontJson = "";
                    String ottFrontJson = "";
                    if (11 == tmpType) {
                        rmsFrontJson = JSONObject.toJSONString(subTempData.getContent());
                    } else if (12 == tmpType) {
                        ottFrontJson = JSONObject.toJSONString(subTempData.getContent());
                    }
                    if (11 == tmpType) {
                        excelJson = getFrameParamRms(ottFrontJson, rmsFrontJson, locale);
                    }
                }
            } else if (12 == tempType) {
                //卡片以富媒体的补充方式生成,如富媒体无参则使用卡片
                boolean isFlag = true;
                String ottFrontJson = "";
                String rmsFrontJson = "";
                for (SubTempData subTempData : tempArr) {
                    Integer tmpType = subTempData.getTmpType();
                    if (11 == tmpType) {
                        rmsFrontJson = JSONObject.toJSONString(subTempData.getContent());
                        if (checkParam(rmsFrontJson, locale)) {
                            isFlag = false;
                            break;
                        }
                    }
                    if (12 == tmpType) {
                        ottFrontJson = JSONObject.toJSONString(subTempData.getContent());
                    }
                }
                if (isFlag) {
                    excelJson = getFrameParamOtt(ottFrontJson, locale);
                } else {
                    excelJson = getFrameParamRms(ottFrontJson, rmsFrontJson, locale);
                }

            } else if (13 == tempType || 15 == tempType) {
                for (SubTempData subTempData : tempArr) {
                    Integer tmpType = subTempData.getTmpType();
                    Object content = subTempData.getContent();
                    if (13 == tmpType || 15 == tempType) {
                        String frontJson = JSONObject.toJSONString(content);
                        excelJson = getFrameParamRichText(frontJson, locale);
                    }
                }
            }

            if (!checkParam(excelJson, locale)) {
                return;
            }
            // 更新数据库LF_TEMPLATE  phoneParam 字段
            //更新表字段参数JSONss
            LfTemplate lfTemplate = new LfTemplate();
            lfTemplate.setTmid(tmid);
            lfTemplate.setExljson(excelJson);
            baseBiz.updateObj(lfTemplate);
        } catch (Exception e) {
            EmpExecutionContext.info("更新数据库LF_TEMPLATE表excelJson字段异常");
        }
    }

    /**
     * V1.0 - V3.0 模板生成EXTJSON
     * @param tempData
     * @param locale
     * @return
     */
    public static String saveExcleJsonV1TOV3(TempData tempData, Locale locale) {
        String excelJson = "";
        String tempStr = JSONObject.toJSONString(tempData);
        if (tempData == null || !checkParam(tempStr, locale)) {
            return "";
        }
        try {
            Long tmid = tempData.getTmid();
            Integer tempType = tempData.getTmpType();
            List<SubTempData> tempArr = tempData.getTempArr();
            if (11 == tempType) {
                for (SubTempData subTempData : tempArr) {
                    Integer tmpType = subTempData.getTmpType();
                    if (11 == tmpType) {
                        String ottFrontJson = "";
                        String  rmsFrontJson = JSONObject.toJSONString(subTempData.getContent());
                        excelJson = getFrameParamRms(ottFrontJson, rmsFrontJson, locale);
                    }
                }
            }
            if (!checkParam(excelJson, locale)) {
                return "";
            }

        } catch (Exception e) {
            EmpExecutionContext.error("更新数据库LF_TEMPLATE表excelJson字段异常");
        }
        return excelJson;
    }

    /**
     * 检查字符串中是否含有参数
     *
     * @param str
     * @return
     */
    public static boolean checkParam(String str, Locale locale) {
        boolean isFlag = false;
        String regx = MessageUtils.getWord("common", locale, "common_send_16");
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            isFlag = true;
        }
        return isFlag;
    }

    public static String[] resolveString(String content) {
        String[] str = new String[2];
        Pattern nPattern = Pattern.compile("\\d+");
        Matcher nMatcher = nPattern.matcher(content);
        while (nMatcher.find()) {
            str[0] = nMatcher.group(0);
        }
        Pattern wPattern = Pattern.compile("\\D+");
        Matcher wMatcher = wPattern.matcher(content);
        while (wMatcher.find()) {
            str[1] = wMatcher.group(0);
        }
        return str;

    }
}