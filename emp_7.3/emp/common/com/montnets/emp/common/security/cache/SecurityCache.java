package com.montnets.emp.common.security.cache;

import com.montnets.emp.common.context.EmpExecutionContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liangHuaGeng
 * @Title: SecurityCache
 * @ProjectName emp_7.3
 * @Description: 缓存类, 用来缓存配置文件中取回来的数据, 具体对应security包下的配置文件
 * @date 2019/1/1614:38
 */
public enum SecurityCache {
    INSTANCE;

    private static final ConcurrentHashMap<String, Map<String, List<String>>> URL_METHOD_OF_CHAR = new ConcurrentHashMap<String, Map<String, List<String>>>();
    private static final ConcurrentHashMap<String, Map<String, String>> URL_METHOD = new ConcurrentHashMap<String, Map<String, String>>();
    private static final List<String> ESCAPE_CHAR = Collections.synchronizedList(new ArrayList<String>());

    //解决%和_不能单独发送的问题  读取配置文件的montnets.emp.char.notdecode配置项
    private static final ConcurrentHashMap<String, List<String>> ESCAPE_PARAM=new ConcurrentHashMap<String, List<String>>();

    public  ConcurrentHashMap<String, List<String>> getEscapeParam() {
        return ESCAPE_PARAM;
    }

    public void setEscapeParam(Map<String, List<String>> escapeParam){
        ESCAPE_PARAM.putAll(escapeParam);
    }


    public List<String> getEscapeChar() {
        return ESCAPE_CHAR;
    }

    public void setEscapeChar(List<String> list) {
        if (null != list) {
            ESCAPE_CHAR.addAll(list);
        } else {
            EmpExecutionContext.error(new NullPointerException(), "SecurityCache.setEscapeChar() 发现异常!");
        }
    }

    public void clearEscapeChar() {
        ESCAPE_CHAR.clear();
    }

    public ConcurrentHashMap<String, Map<String, String>> getUrlMethodMap() {
        return URL_METHOD;
    }

    public void setUrlMethodCache(Map<String, Map<String, String>> map) {
        if (null != map) {
            URL_METHOD.putAll(map);
        } else {
            EmpExecutionContext.error(new NullPointerException(), "SecurityCache.setUrlMethodCache() 发现异常!");
        }
    }

    public void clearUrlMethodCache() {
        URL_METHOD.clear();
    }

    public ConcurrentHashMap<String, Map<String, List<String>>> getUrlMethodMapOfChar() {
        return URL_METHOD_OF_CHAR;
    }

    public void setUrlMethodOfCharCache(Map<String, Map<String, List<String>>> map) {
        if (null != map) {
            URL_METHOD_OF_CHAR.putAll(map);
        } else {
            EmpExecutionContext.error(new NullPointerException(), "SecurityCache.setUrlMethodOfCharCache() 发现异常!");
        }
    }

    public void clearUrlMethodOfCharCache() {
        URL_METHOD_OF_CHAR.clear();
    }
}
