package com.montnets.emp.common.biz;

import com.montnets.emp.common.cache.SysConf;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SysConfValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.entity.system.LfSysParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载系统配置业务层
 */
public class LoadSysConfBiz extends SuperBiz {

    private static String DEP_MAXLEVEL="dep.maxlevel";

    private static String DEP_MAXCHILD="dep.maxchild";

    private static String DEP_MAXDEP="dep.maxdep";

    private static String MAX_CHARGE_DEP_LEVEL	= "dep.maxchargelevel";

    private static String  IS_REMIND="isRemind";

    private static String  IS_HDATA="isHdata";

    private static String  IS_SUMM="isSumm";

    private static String  SUMM_TIME_INTERVAL="SummTimeInterval";

    private static String  ENDHOUR="ENDHOUR";

    private static String  SPUSER_ISLWR="SPUSER_ISLWR";

    private static String  LOGIN_YAN_ZHENG_MA="loginYanZhengMa";

    private static String  CXTJ_MT_EXPORT_LIMIT="cxtjMtExportLimit";

    private static String HTTP_REQUEST_TIMEOUT ="HTTP_REQUEST_TIMEOUT";

    private static String HTTP_RESPONSE_TIMEOUT ="HTTP_RESPONSE_TIMEOUT";

    private static String BALANCE_REQ_INTERVAL ="BALANCE_REQ_INTERVAL";

    private static String  LOG_PRINT_INTERVAL="LOG_PRINT_INTERVAL";

    private static String  BLACK_MAXCOUNT="BLACK_MAXCOUNT";

    private static String  ADDBLABYMOORDERFLAG="ADDBLABYMOORDERFLAG";

    private static String  ADDBLAMOORDER="ADDBLAMOORDER";

    private static String  ADDBLAMOMAXID="ADDBLAMOMAXID";

    private static String DELBLAMOORDER ="DELBLAMOORDER";

    private static String  BLACKCORPCODE="BLACKCORPCODE";

    private static String  GWFEE_CHECK="GWFEE_CHECK";

    /**
     * 根据企业编码和key获得对应企业配置的机构限制信息,返回map
     * @param
     * @param corpCode
     * @return map
     */
    public  Map<String, String> getCorpConfMap(String corpCode){
        LinkedHashMap<String, String> corpConfMap = new LinkedHashMap<String, String>();

        //获取企业编码
        corpConfMap.put("corpCode", corpCode);

        try {
            List<LfCorpConf> lfCorpConfList = empDao.findListByCondition(LfCorpConf.class, corpConfMap, null);

            if(lfCorpConfList !=  null && lfCorpConfList.size() > 0){
                corpConfMap.clear();
                for(LfCorpConf lfCorpCon: lfCorpConfList)
                {
                    // 机构级别(系统配置)
                    if(DEP_MAXLEVEL.equals(lfCorpCon.getParamKey()))
                    {
                        corpConfMap.put(DEP_MAXLEVEL, lfCorpCon.getParamValue());
                    }
                    //单个机构子机构数(系统配置)
                    else if(DEP_MAXCHILD.equals(lfCorpCon.getParamKey()))
                    {
                        corpConfMap.put(DEP_MAXCHILD, lfCorpCon.getParamValue());
                    }
                    // 机构总数(系统配置)
                    else if(DEP_MAXDEP.equals(lfCorpCon.getParamKey()))
                    {
                        corpConfMap.put(DEP_MAXDEP, lfCorpCon.getParamValue());
                    }
                    //企业机构最大级别数
                    else if(MAX_CHARGE_DEP_LEVEL.equals(lfCorpCon.getParamKey())){
                        corpConfMap.put(MAX_CHARGE_DEP_LEVEL, lfCorpCon.getParamValue());
                    }

                }
                return corpConfMap;
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据企业编码和key获得对应企业配置的机构限制信息异常。");
        }
        return null;
    }

    /**
     * 查询LF_SYS_PARAM表
     * @return 返回MAP集合
     */
    public  Map<String, String> getSysParamMap(){
        LinkedHashMap<String, String> sysParamMap = new LinkedHashMap<String, String>();

        try {
            List<LfSysParam> lfSysParamList = empDao.findListByCondition(LfSysParam.class, null, null);

            if(lfSysParamList !=  null && lfSysParamList.size() > 0){
                for(LfSysParam lfSysParam: lfSysParamList)
                {
                    // 短信审批使用   0：开启审批提醒  1：关闭审批提醒
                    if(IS_REMIND.equals(lfSysParam.getParamItem()))
                    {
                        sysParamMap.put(IS_REMIND, lfSysParam.getParamValue());
                    }
                    // 调度汇总是否执行使用  0：执行  1：不执行
                    else if(IS_HDATA.equals(lfSysParam.getParamItem()))
                    {
                        sysParamMap.put(IS_HDATA, lfSysParam.getParamValue());
                    }
                    // EMP调度汇总是否执行使用  0：执行  1：不执行
                    else if(IS_SUMM.equals(lfSysParam.getParamItem()))
                    {
                        sysParamMap.put(IS_SUMM, lfSysParam.getParamValue());
                    }
                    // 白天汇总时间间隔  单位小时
                    else if(SUMM_TIME_INTERVAL.equals(lfSysParam.getParamItem()))
                    {
                        sysParamMap.put(SUMM_TIME_INTERVAL, lfSysParam.getParamValue());
                     //汇总结束扫描网关执行状态时间点
                    }else if(ENDHOUR.equals(lfSysParam.getParamItem()))
                    {
                        sysParamMap.put(ENDHOUR, lfSysParam.getParamValue());
                     //是否支持小写账号 0 不支持  1支持
                    }else if(SPUSER_ISLWR.equals(lfSysParam.getParamItem()))
                    {
                        sysParamMap.put(SPUSER_ISLWR, lfSysParam.getParamValue());
                    }else if(LOGIN_YAN_ZHENG_MA.equals(lfSysParam.getParamItem()))
                    {
                        sysParamMap.put(LOGIN_YAN_ZHENG_MA, lfSysParam.getParamValue());
                    }else if(CXTJ_MT_EXPORT_LIMIT.equals(lfSysParam.getParamItem()))
                    {
                        sysParamMap.put(CXTJ_MT_EXPORT_LIMIT, lfSysParam.getParamValue());
                    }

                }
                return sysParamMap;
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据企业编码和key获得对应企业配置的机构限制信息异常。");
        }
        return null;
    }

    /**
     * 查询LF_GLOBAL_VARIABLE表
     * @return 返回MAP集合
     */
    public  Map<String, Object> getGlobalVariableMap(){
        LinkedHashMap<String, Object> globalVariableMap = new LinkedHashMap<String, Object>();

        try {
            List<LfGlobalVariable> lfGlobalVariableList = empDao.findListByCondition(LfGlobalVariable.class, null, null);

            if(lfGlobalVariableList !=  null && lfGlobalVariableList.size() > 0){
                for(LfGlobalVariable lfGlobalVariable: lfGlobalVariableList)
                {
                    // 短信审批使用   0：开启审批提醒  1：关闭审批提醒
                    if(HTTP_REQUEST_TIMEOUT.equals(lfGlobalVariable.getGlobalKey()))
                    {
                        globalVariableMap.put(HTTP_REQUEST_TIMEOUT, lfGlobalVariable.getGlobalValue());
                    }
                    // 调度汇总是否执行使用  0：执行  1：不执行
                    else if(HTTP_RESPONSE_TIMEOUT.equals(lfGlobalVariable.getGlobalKey()))
                    {
                        globalVariableMap.put(HTTP_RESPONSE_TIMEOUT, lfGlobalVariable.getGlobalValue());
                    }
                    // EMP调度汇总是否执行使用  0：执行  1：不执行
                    else if(BALANCE_REQ_INTERVAL.equals(lfGlobalVariable.getGlobalKey()))
                    {
                        globalVariableMap.put(BALANCE_REQ_INTERVAL, lfGlobalVariable.getGlobalValue());
                    }
                    // 白天汇总时间间隔  单位小时
                    else if(LOG_PRINT_INTERVAL.equals(lfGlobalVariable.getGlobalKey()))
                    {
                        globalVariableMap.put(LOG_PRINT_INTERVAL, lfGlobalVariable.getGlobalValue());
                        //汇总结束扫描网关执行状态时间点
                    }else if(BLACK_MAXCOUNT.equals(lfGlobalVariable.getGlobalKey()))
                    {
                        globalVariableMap.put(BLACK_MAXCOUNT, lfGlobalVariable.getGlobalValue());
                        //是否支持小写账号 0 不支持  1支持
                    }else if(ADDBLABYMOORDERFLAG.equals(lfGlobalVariable.getGlobalKey()))
                    {
                        globalVariableMap.put(ADDBLABYMOORDERFLAG, lfGlobalVariable.getGlobalValue());
                    }else if(ADDBLAMOORDER.equals(lfGlobalVariable.getGlobalKey()))
                    {
                        globalVariableMap.put(ADDBLAMOORDER, lfGlobalVariable.getGlobalStrValue());
                    }else if(ADDBLAMOMAXID.equals(lfGlobalVariable.getGlobalKey()))
                    {
                        globalVariableMap.put(ADDBLAMOMAXID, lfGlobalVariable.getGlobalValue());
                    }else if(DELBLAMOORDER.equals(lfGlobalVariable.getGlobalKey()))
                    {
                        globalVariableMap.put(DELBLAMOORDER, lfGlobalVariable.getGlobalStrValue());
                    }else if(BLACKCORPCODE.equals(lfGlobalVariable.getGlobalKey()))
                    {
                        globalVariableMap.put(BLACKCORPCODE, lfGlobalVariable.getGlobalStrValue());
                    }else if(GWFEE_CHECK.equals(lfGlobalVariable.getGlobalKey()))
                    {
                        globalVariableMap.put(GWFEE_CHECK, lfGlobalVariable.getGlobalValue());
                    }
                }
                return globalVariableMap;
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据企业编码和key获得对应企业配置的机构限制信息异常。");
        }
        return null;
    }

    public void setCorpConfValue(){
        String depMaxLevel=SysConf.CORP_CONF_MAP.get(DEP_MAXLEVEL);
        if(depMaxLevel!=null){
            SysConfValue.setDepMaxLevel(Integer.parseInt(depMaxLevel));
        }

        String depMaxChild=SysConf.CORP_CONF_MAP.get(DEP_MAXCHILD);
        if(depMaxChild!=null){
            SysConfValue.setDepMaxChild(Integer.parseInt(depMaxChild));
        }

        String depMaxDep=SysConf.CORP_CONF_MAP.get(DEP_MAXDEP);
        if(depMaxDep!=null){
            SysConfValue.setDepMaxDep(Integer.parseInt(depMaxDep));
        }

        String depMaxChargeLevel=SysConf.CORP_CONF_MAP.get(MAX_CHARGE_DEP_LEVEL);
        if(depMaxChargeLevel!=null){
            SysConfValue.setDepMaxChargeLevel(Integer.parseInt(depMaxChargeLevel));
        }
    }

    public void setSysParamValue(){
        String isRemind=SysConf.SYS_PARAM_MAP.get(IS_REMIND);
        if(isRemind!=null){
            SysConfValue.setIsRemind(isRemind);
        }

        String isHdata=SysConf.SYS_PARAM_MAP.get(IS_HDATA);
        if(isHdata!=null){
            SysConfValue.setIsHdata(isHdata);
        }

        String isSumm=SysConf.SYS_PARAM_MAP.get(IS_SUMM);
        if(isSumm!=null){
            SysConfValue.setIsSumm(isSumm);
        }

        String summTimeInterval=SysConf.SYS_PARAM_MAP.get(SUMM_TIME_INTERVAL);
        if(summTimeInterval!=null){
            SysConfValue.setSummTimeInterval(summTimeInterval);
        }

        String endHour=SysConf.SYS_PARAM_MAP.get(LoadSysConfBiz.ENDHOUR);
        if(endHour!=null){
            SysConfValue.setENDHOUR(endHour);
        }

        String spUserIsLwr=SysConf.SYS_PARAM_MAP.get(LoadSysConfBiz.SPUSER_ISLWR);
        if(spUserIsLwr!=null){
            SysConfValue.setSpuserIslwr(spUserIsLwr);
        }

        String loginYanZhengMa=SysConf.SYS_PARAM_MAP.get(LOGIN_YAN_ZHENG_MA);
        if(loginYanZhengMa!=null){
            SysConfValue.setLoginYanZhengMa(loginYanZhengMa);
        }

        String cxtjMtExportLimit=SysConf.SYS_PARAM_MAP.get(CXTJ_MT_EXPORT_LIMIT);
        if(cxtjMtExportLimit!=null){
            SysConfValue.setCxtjMtExportLimit(cxtjMtExportLimit);
        }
    }

    public void setGlobalVariableValue(){
        Object httpRequestTimeout=SysConf.GLOBAL_VARIABLE_MAP.get(LoadSysConfBiz.HTTP_REQUEST_TIMEOUT);
        if(httpRequestTimeout!=null){
            SysConfValue.setHttpRequestTimeout(Long.parseLong(httpRequestTimeout.toString()));
        }
        //刷新缓存
        StaticValue.HTTP_REQUEST_TIMEOUT=Integer.parseInt(String.valueOf(SysConfValue.getHttpRequestTimeout()));

        Object httpResponseTimeout=SysConf.GLOBAL_VARIABLE_MAP.get(LoadSysConfBiz.HTTP_RESPONSE_TIMEOUT);
        if(httpResponseTimeout!=null){
            SysConfValue.setHttpResponseTimeout(Long.parseLong(httpResponseTimeout.toString()));
        }
        //刷新缓存
        StaticValue.HTTP_RESPONSE_TIMEOUT=Integer.parseInt(String.valueOf(SysConfValue.getHttpResponseTimeout()));

        Object balanceReqInterval=SysConf.GLOBAL_VARIABLE_MAP.get(LoadSysConfBiz.BALANCE_REQ_INTERVAL);
        if(balanceReqInterval!=null){
            SysConfValue.setBalanceReqInterval(Long.parseLong(balanceReqInterval.toString()));
        }
        //刷新缓存
        StaticValue.setBalanceRequestInterval(SysConfValue.getBalanceReqInterval());

        Object logPrintInterval=SysConf.GLOBAL_VARIABLE_MAP.get(LoadSysConfBiz.LOG_PRINT_INTERVAL);
        if(logPrintInterval!=null){
            SysConfValue.setLogPrintInterval(Long.parseLong(logPrintInterval.toString()));
        }
        //刷新缓存
        StaticValue.setLogPrintInterval(SysConfValue.getLogPrintInterval());

        Object blackMaxCount=SysConf.GLOBAL_VARIABLE_MAP.get(LoadSysConfBiz.BLACK_MAXCOUNT);
        if(blackMaxCount!=null){
            SysConfValue.setBlackMaxcount(Long.parseLong(blackMaxCount.toString()));
        }
        //刷新缓存
        StaticValue.setBlackMaxcount(SysConfValue.getBlackMaxcount());

        Object addBlaByMoOrderFlag=SysConf.GLOBAL_VARIABLE_MAP.get(LoadSysConfBiz.ADDBLABYMOORDERFLAG);
        if(addBlaByMoOrderFlag!=null){
            SysConfValue.setADDBLABYMOORDERFLAG(Long.parseLong(addBlaByMoOrderFlag.toString()));
        }

        Object addBlaMoOrder=SysConf.GLOBAL_VARIABLE_MAP.get(LoadSysConfBiz.ADDBLAMOORDER);
        if(addBlaMoOrder!=null){
            SysConfValue.setADDBLAMOORDER(addBlaMoOrder.toString());
        }

        Object addBlaMoMaxId=SysConf.GLOBAL_VARIABLE_MAP.get(LoadSysConfBiz.ADDBLAMOMAXID);
        if(addBlaMoMaxId!=null){
            SysConfValue.setADDBLAMOMAXID(Long.parseLong(addBlaMoMaxId.toString()));
        }

        Object delBlaMoOrder=SysConf.GLOBAL_VARIABLE_MAP.get(LoadSysConfBiz.DELBLAMOORDER);
        if(delBlaMoOrder!=null){
            SysConfValue.setDELBLAMOORDER(delBlaMoOrder.toString());
        }

        Object blackCorpCode=SysConf.GLOBAL_VARIABLE_MAP.get(LoadSysConfBiz.BLACKCORPCODE);
        if(blackCorpCode!=null){
            SysConfValue.setBLACKCORPCODE(blackCorpCode.toString());
        }

        Object gwFeeCheck=SysConf.GLOBAL_VARIABLE_MAP.get(LoadSysConfBiz.GWFEE_CHECK);
        if(gwFeeCheck!=null){
            SysConfValue.setGwfeeCheck(Long.parseLong(gwFeeCheck.toString()));
        }
    }
}
