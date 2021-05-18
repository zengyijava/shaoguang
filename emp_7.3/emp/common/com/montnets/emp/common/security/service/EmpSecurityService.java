package com.montnets.emp.common.security.service;

import java.util.List;
import java.util.Map;

/**
 * @author liangHuaGeng
 * @Title: EmpSecurityService
 * @ProjectName emp_7.3
 * @Description: TODO
 * @date 2019/1/169:33
 */
@SuppressWarnings("ALL")
public interface EmpSecurityService {

    /**
     * 将容易引起xss & sql漏洞的半角字符直接替换成全角字符
     *
     * @param target
     * @param charFlag
     * @param sqlFlag
     * @return
     */
    String xssEncode(String target, boolean charFlag, boolean sqlFlag);

    /**
     * 解决不能单独发送%或者_的问题 此方法不过滤参数中的%号或者_下划线
     * 将容易引起xss & sql漏洞的半角字符直接替换成全角字符
     *
     * @param target
     * @param charFlag
     * @param sqlFlag
     * @return
     */
    String xssEncodeForSend(String target, boolean charFlag, boolean sqlFlag);


    /**
     * 防止xss跨脚本攻击（替换，根据实际情况调整）
     *
     * @param value
     * @param sqlFlag
     * @return
     */
    String scriptXSSAndSql(String value, boolean sqlFlag);

    /**
     * 获取特殊字符过滤器 放权的url和method 的Map
     *
     * @param fileName
     * @return
     */
    Map<String, Map<String, List<String>>> getUrlMethodMapOfChar(String fileName);

    /**
     * 获取sql注入过滤器关键字 放权的url和method
     *
     * @param fileName
     * @return
     */
    Map<String, Map<String, String>> getUrlMethod(String fileName);

    /**
     * 获取需要转义的字符
     *
     * @param fileName
     * @return
     */
    List<String> getEscapeChar(String fileName);


    Map<String,List<String>> getEscapeParam();
}
