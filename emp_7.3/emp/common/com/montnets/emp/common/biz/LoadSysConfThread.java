package com.montnets.emp.common.biz;

import com.montnets.emp.common.cache.SysConf;
import com.montnets.emp.common.context.EmpExecutionContext;

import java.util.Map;

/**
 * 加载系统配置线程
 */
public class LoadSysConfThread implements Runnable {

    private static String CORP_CODE="100001";

    private LoadSysConfBiz loadSysConfBiz=new LoadSysConfBiz();

    @Override
    public void run() {
        try
        {
            EmpExecutionContext.info("对LF_CORP_CONF、LF_SYS_PARAM、LF_GLOBAL_VARIABLE表信息加载开始！");

            //查询LF_CORP_CONF表
            Map<String, String> corpConfMap = loadSysConfBiz.getCorpConfMap(CORP_CODE);
            if (corpConfMap != null && corpConfMap.size() > 0) {
                SysConf.CORP_CONF_MAP = corpConfMap;
            }

            //查询LF_SYS_PARAM表
            Map<String, String> sysParamMap = loadSysConfBiz.getSysParamMap();
            if (sysParamMap != null && sysParamMap.size() > 0) {
                SysConf.SYS_PARAM_MAP = sysParamMap;
            }

            //查询LF_GLOBAL_VARIABLE表
            Map<String, Object> globalVariableMap = loadSysConfBiz.getGlobalVariableMap();
            if (globalVariableMap != null && globalVariableMap.size() > 0) {
                SysConf.GLOBAL_VARIABLE_MAP = globalVariableMap;
            }

            //更新LF_CORP_CONF表对应的缓存
            loadSysConfBiz.setCorpConfValue();

            //更新LF_SYS_PARAM表对应的缓存
            loadSysConfBiz.setSysParamValue();

            loadSysConfBiz.setGlobalVariableValue();

            EmpExecutionContext.info("对LF_CORP_CONF、LF_SYS_PARAM、LF_GLOBAL_VARIABLE表信息加载结束！");

        }catch (Exception e)
        {
            EmpExecutionContext.error(e,"对LF_CORP_CONF、LF_SYS_PARAM、LF_GLOBAL_VARIABLE表信息加载出现异常！");
        }

    }
}
