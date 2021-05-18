package com.montnets.emp.common.constant;

public class SysConfValue {

    //LF_CORP_CONF表的配置
    private static int DEP_MAX_LEVEL=10;

    private static int DEP_MAX_CHILD=100;

    private static int DEP_MAX_DEP=10000;

    private static int DEP_MAX_CHARGE_LEVEL=20;

    //LF_SYS_PARAM表的配置
    private static String IS_REMIND="0";

    private static String IS_HDATA="0";

    private static String IS_SUMM="0";

    private static String SUMM_TIME_INTERVAL="6";

    private static String ENDHOUR="6:00";

    private static String SPUSER_ISLWR="0";

    private static String LOGIN_YAN_ZHENG_MA="true";

    private static String CXTJ_MT_EXPORT_LIMIT="1000000";

    //LF_GLOBAL_VARIABLE表的配置
    private static long HTTP_REQUEST_TIMEOUT=120000L;

    private static long HTTP_RESPONSE_TIMEOUT=120000L;

    private static long BALANCE_REQ_INTERVAL=300000L;

    private static long LOG_PRINT_INTERVAL=20000L;

    private static long BLACK_MAXCOUNT=10000000L;

    private static long ADDBLABYMOORDERFLAG=1L;

    private static  String ADDBLAMOORDER="TD";

    private static long ADDBLAMOMAXID=0L;

    private static String DELBLAMOORDER="";

    private static String BLACKCORPCODE="100001";

    private static long  GWFEE_CHECK=1L;


    public static int getDepMaxLevel() {
        return DEP_MAX_LEVEL;
    }

    public static void setDepMaxLevel(int depMaxLevel) {
        DEP_MAX_LEVEL = depMaxLevel;
    }

    public static int getDepMaxChild() {
        return DEP_MAX_CHILD;
    }

    public static void setDepMaxChild(int depMaxChild) {
        DEP_MAX_CHILD = depMaxChild;
    }

    public static int getDepMaxDep() {
        return DEP_MAX_DEP;
    }

    public static void setDepMaxDep(int depMaxDep) {
        DEP_MAX_DEP = depMaxDep;
    }

    public static int getDepMaxChargeLevel() {
        return DEP_MAX_CHARGE_LEVEL;
    }

    public static void setDepMaxChargeLevel(int depMaxChargeLevel) {
        DEP_MAX_CHARGE_LEVEL = depMaxChargeLevel;
    }

    public static String getIsRemind() {
        return IS_REMIND;
    }

    public static void setIsRemind(String isRemind) {
        IS_REMIND = isRemind;
    }

    public static String getIsHdata() {
        return IS_HDATA;
    }

    public static void setIsHdata(String isHdata) {
        IS_HDATA = isHdata;
    }

    public static String getIsSumm() {
        return IS_SUMM;
    }

    public static void setIsSumm(String isSumm) {
        IS_SUMM = isSumm;
    }

    public static String getSummTimeInterval() {
        return SUMM_TIME_INTERVAL;
    }

    public static void setSummTimeInterval(String summTimeInterval) {
        SUMM_TIME_INTERVAL = summTimeInterval;
    }

    public static String getENDHOUR() {
        return ENDHOUR;
    }

    public static void setENDHOUR(String ENDHOUR) {
        SysConfValue.ENDHOUR = ENDHOUR;
    }

    public static String getSpuserIslwr() {
        return SPUSER_ISLWR;
    }

    public static void setSpuserIslwr(String spuserIslwr) {
        SPUSER_ISLWR = spuserIslwr;
    }

    public static String getLoginYanZhengMa() {
        return LOGIN_YAN_ZHENG_MA;
    }

    public static void setLoginYanZhengMa(String loginYanZhengMa) {
        LOGIN_YAN_ZHENG_MA = loginYanZhengMa;
    }

    public static String getCxtjMtExportLimit() {
        return CXTJ_MT_EXPORT_LIMIT;
    }

    public static void setCxtjMtExportLimit(String cxtjMtExportLimit) {
        CXTJ_MT_EXPORT_LIMIT = cxtjMtExportLimit;
    }

    public static long getHttpRequestTimeout() {
        return HTTP_REQUEST_TIMEOUT;
    }

    public static void setHttpRequestTimeout(long httpRequestTimeout) {
        HTTP_REQUEST_TIMEOUT = httpRequestTimeout;
    }

    public static long getHttpResponseTimeout() {
        return HTTP_RESPONSE_TIMEOUT;
    }

    public static void setHttpResponseTimeout(long httpResponseTimeout) {
        HTTP_RESPONSE_TIMEOUT = httpResponseTimeout;
    }

    public static long getBalanceReqInterval() {
        return BALANCE_REQ_INTERVAL;
    }

    public static void setBalanceReqInterval(long balanceReqInterval) {
        BALANCE_REQ_INTERVAL = balanceReqInterval;
    }

    public static long getLogPrintInterval() {
        return LOG_PRINT_INTERVAL;
    }

    public static void setLogPrintInterval(long logPrintInterval) {
        LOG_PRINT_INTERVAL = logPrintInterval;
    }

    public static long getBlackMaxcount() {
        return BLACK_MAXCOUNT;
    }

    public static void setBlackMaxcount(long blackMaxcount) {
        BLACK_MAXCOUNT = blackMaxcount;
    }

    public static long getADDBLABYMOORDERFLAG() {
        return ADDBLABYMOORDERFLAG;
    }

    public static void setADDBLABYMOORDERFLAG(long ADDBLABYMOORDERFLAG) {
        SysConfValue.ADDBLABYMOORDERFLAG = ADDBLABYMOORDERFLAG;
    }

    public static String getADDBLAMOORDER() {
        return ADDBLAMOORDER;
    }

    public static void setADDBLAMOORDER(String ADDBLAMOORDER) {
        SysConfValue.ADDBLAMOORDER = ADDBLAMOORDER;
    }

    public static long getADDBLAMOMAXID() {
        return ADDBLAMOMAXID;
    }

    public static void setADDBLAMOMAXID(long ADDBLAMOMAXID) {
        SysConfValue.ADDBLAMOMAXID = ADDBLAMOMAXID;
    }

    public static String getDELBLAMOORDER() {
        return DELBLAMOORDER;
    }

    public static void setDELBLAMOORDER(String DELBLAMOORDER) {
        SysConfValue.DELBLAMOORDER = DELBLAMOORDER;
    }

    public static String getBLACKCORPCODE() {
        return BLACKCORPCODE;
    }

    public static void setBLACKCORPCODE(String BLACKCORPCODE) {
        SysConfValue.BLACKCORPCODE = BLACKCORPCODE;
    }

    public static long getGwfeeCheck() {
        return GWFEE_CHECK;
    }

    public static void setGwfeeCheck(long gwfeeCheck) {
        GWFEE_CHECK = gwfeeCheck;
    }
}
