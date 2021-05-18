package com.montnets.emp.common.security.service.impl;


import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.security.cache.SecurityCache;
import com.montnets.emp.common.security.constant.SecurityConst;
import com.montnets.emp.common.security.entity.FilterValue;
import com.montnets.emp.common.security.service.EmpSecurityService;
import com.montnets.emp.common.security.util.PatternUtils;
import com.montnets.emp.common.security.util.PropertiesUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author liangHuaGeng
 * @Title: EmpSecurityServiceImpl
 * @ProjectName emp_7.3
 * @Description: TODO
 * @date 2019/1/169:30
 */
public class EmpSecurityServiceImpl implements EmpSecurityService {


    @Override
    public String xssEncode(String target, boolean charFlag, boolean sqlFlag) {

        if (StringUtils.isBlank(target)) {
            return target;
        }
        // 过滤 % 和 _
        boolean specialFilter = (target.contains("%") || target.contains("_")) && target.trim().length() == 1;
        if (specialFilter) {
            return SecurityConst.EMPTY;
        }
        try {
            //替换Script语句和Sql关键字为空
            String str = scriptXSSAndSql(target, sqlFlag);
            if (charFlag && StringUtils.isNotBlank(str)) {
                List<String> escapeChar = SecurityCache.INSTANCE.getEscapeChar();
                StringBuilder sb = new StringBuilder(str.length() + 16);
                for (int i = 0; i < str.length(); i++) {
                    String oldStr = String.valueOf(str.charAt(i));
                    String result = oldStr;
                    if (!escapeChar.contains(oldStr)) {
                        sb.append(result);
                        continue;
                    }
                    for (String tmpChar : escapeChar) {
                        if (tmpChar.equals(oldStr)) {
                            /*if ("'".equals(oldStr)) {
                                result = "&#39";
                            } else if ("+".equals(oldStr)) {
                                result = "&#43;";
                            } else if ("%".equals(oldStr)) {
                                result = "&#37;";
                            } else if (";".equals(oldStr)) {
                                result = "&#59;";
                            } else if ("&".equals(oldStr)) {
                                result = "&amp;";
                            } else {*/
                            // 如果发现和我们配置文件中配置的转义符相同, 就进行转义
                            result = StringEscapeUtils.escapeHtml(oldStr);
                            /* }*/
                            EmpExecutionContext.info("EMP安全检测发现有特殊字符, char = " + oldStr
                                    + ", 已经在 " + Calendar.getInstance(Locale.CHINA).getTime() + " 对其进行转义! value = :" + result);
                            // 转义结束, 就结束当前循环
                            break;
                        }
                    }
                    sb.append(result);
                }
                str = sb.toString();
            }
            return str;
        } catch (
                Exception e) {
            EmpExecutionContext.error(e, "EmpSecurityServiceImpl.xssEncode() 方法发现异常!");
        }
        return SecurityConst.EMPTY;

    }


    @Override
    public String xssEncodeForSend(String target, boolean charFlag, boolean sqlFlag) {

        if (StringUtils.isBlank(target)) {
            return target;
        }
//        // 过滤 % 和 _
//        boolean specialFilter = (target.contains("%") || target.contains("_")) && target.trim().length() == 1;
//        if (specialFilter) {
//            return SecurityConst.EMPTY;
//        }
        try {
            //替换Script语句和Sql关键字为空
            String str = scriptXSSAndSql(target, sqlFlag);
            if (charFlag && StringUtils.isNotBlank(str)) {
                List<String> escapeChar = SecurityCache.INSTANCE.getEscapeChar();
                StringBuilder sb = new StringBuilder(str.length() + 16);
                for (int i = 0; i < str.length(); i++) {
                    String oldStr = String.valueOf(str.charAt(i));
                    String result = oldStr;
                    if (!escapeChar.contains(oldStr)) {
                        sb.append(result);
                        continue;
                    }
                    for (String tmpChar : escapeChar) {
                        if (tmpChar.equals(oldStr)) {
                           /* if ("'".equals(oldStr)) {
                                result = "&#39";
                            } else if ("+".equals(oldStr)) {
                                result = "&#43;";
                            } else if ("%".equals(oldStr)) {
                                result = "&#37;";
                            } else if (";".equals(oldStr)) {
                                result = "&#59;";
                            } else if ("&".equals(oldStr)) {
                                result = "&amp;";
                            } else {*/
                            // 如果发现和我们配置文件中配置的转义符相同, 就进行转义
                            result = StringEscapeUtils.escapeHtml(oldStr);
                            //}
                            EmpExecutionContext.info("EMP安全检测发现有特殊字符, char = " + oldStr
                                    + ", 已经在 " + Calendar.getInstance(Locale.CHINA).getTime() + " 对其进行转义! value = :" + result);
                            // 转义结束, 就结束当前循环
                            break;
                        }
                    }
                    sb.append(result);
                }
                str = sb.toString();
            }
            return str;
        } catch (
                Exception e)

        {
            EmpExecutionContext.error(e, "EmpSecurityServiceImpl.xssEncodeForSend() 方法发现异常!");
        }
        return SecurityConst.EMPTY;
     }

    @Override
    public String scriptXSSAndSql(String value, boolean sqlFlag) {
        try {
            FilterValue val = new FilterValue(value);
            if (null != value) {
                //匹配script语句替换为空
                PatternUtils.scriptFilter(val);
                if (sqlFlag) {
                    //需要进行匹配sql关键字替换为空
                    PatternUtils.sqlFilter(val);
                }
            }
            return val.getValue();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "EmpSecurityServiceImpl.scriptXSSAndSql() 方法发现异常!");
        }
        return SecurityConst.EMPTY;
    }

    @Override
    public Map<String, Map<String, List<String>>> getUrlMethodMapOfChar(String fileName) {
        try {
            Map<String, Map<String, List<String>>> tmpMap = new HashMap<String, Map<String, List<String>>>();
            Properties properties = PropertiesUtils.getProperties(fileName);
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                String[] methods = value.split(";");
                Map<String, List<String>> map = new HashMap<String, List<String>>();
                for (String m : methods) {
                    String method = m.split("&")[0];
                    String params = m.split("&").length >= 2 ? m.split("&")[1] : "";
                    String[] paramArr = params.split(",");
                    List<String> list = new ArrayList<String>();
                    Collections.addAll(list, paramArr);
                    map.put(method, list);
                }
                tmpMap.put(key, map);
            }
            return tmpMap;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "EmpSecurityServiceImpl.getUrlMethodMapOfChar() 方法发现异常!");
        }
        return null;
    }

    @Override
    public Map<String, Map<String, String>> getUrlMethod(String fileName) {
        try {
            Map<String, Map<String, String>> tmpMap = new HashMap<String, Map<String, String>>();
            Properties properties = PropertiesUtils.getProperties(fileName);
            //sql注入放权配置文件
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                String[] methods = value.split(",");
                Map<String, String> map = new HashMap<String, String>();
                for (String m : methods) {
                    map.put(m, SecurityConst.NUM_ONE);
                }
                tmpMap.put(key, map);
            }
            return tmpMap;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "EmpSecurityServiceImpl.getUrlMethod() 方法发现异常!");
        }
        return null;
    }

    @Override
    public List<String> getEscapeChar(String fileName) {
        List<String> escapeList = new ArrayList<String>();
        try {
            Properties properties = PropertiesUtils.getProperties(fileName);
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = (String) entry.getKey();
                if (!"Escape_character".equals(key)) {
                    throw new IllegalArgumentException("配置文件中的参数异常, key != " + key);
                }
                String value = (String) entry.getValue();
                String[] escapeArr = value.split("and");
                CollectionUtils.addAll(escapeList, escapeArr);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "EmpSecurityServiceImpl.getEscapeChar() 方法发现异常!");
        }
        return escapeList;
    }

    @Override
    public Map<String, List<String>> getEscapeParam() {
        String escapeParamStr="";
        Map<String, List<String>> escapeParam=new HashMap<String, List<String>>();
        try {
            escapeParamStr = SystemGlobals.getValue("montnets.emp.char.notdecode", "ssm_ssendBatchSMS.htm&msg,taskname|dsm_sendDiffSMS.htm&msg,taskname|tem_smsTemplate.htm&tmName,tmMsg|ssm_sendBatchSMS.htm&msg,taskname|dsm_ssendDiffSMS.htm&msg,taskname|kfs_sendClientSMS.htm&msg,taskname,tmMsg|per_sendNoticeSMS.htm&content|bir_birthdaySendEMP.htm&tmMsg,msg,signName,title|bir_birthdaySendClient.htm&tmMsg,msg,signName,title");
            if(escapeParamStr!=null&&!"".equals(escapeParamStr)){
                String[] escapeParamArr=escapeParamStr.split("\\|");
                for (int i=0;i<escapeParamArr.length;i++){
                    String[] sonEscapeParamArr=escapeParamArr[i].split("&");
                    String[] grandsonEscapeParamArr=sonEscapeParamArr[1].split(",");
                    List<String> escapeParamList=Arrays.asList(grandsonEscapeParamArr);
                    escapeParam.put(sonEscapeParamArr[0],escapeParamList);
                }
            }

        }catch (Exception e){
            escapeParam=null;
            EmpExecutionContext.error(e, "EmpSecurityServiceImpl.getEscapeParam() 方法发现异常!");
        }
        return escapeParam;
    }


}
