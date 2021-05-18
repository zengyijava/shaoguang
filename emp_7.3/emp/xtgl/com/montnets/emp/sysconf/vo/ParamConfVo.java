package com.montnets.emp.sysconf.vo;

/**
 * Web运行参数配置
 *
 * @author :  tanjy
 * @version :  V1.0
 * @date :  2021-02-02 09:33
 */
public class ParamConfVo {
    /**
     * 参数数据类型： 0为数字  1为布尔值  2为字符串
     */
    private int dataType;

    /**
     * 参数键
     */
    private String paramKey;

    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 参数详细描述
     */
    private String paramMemo;
    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 参数配置种类 0 企业配置  1系统配置  2位全局配置
     */
    private int paramType;

    public ParamConfVo(int dataType, String paramKey, String paramName, String paramMemo, String paramValue, int paramType) {
        this.paramType = paramType;
        this.paramKey = paramKey;
        this.paramName = paramName;
        this.paramMemo = paramMemo;
        this.paramValue = paramValue;
        this.dataType = dataType;
    }

    public ParamConfVo() {
    }

    public int getParamType() {
        return paramType;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public void setParamType(int paramType) {
        this.paramType = paramType;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamMemo() {
        return paramMemo;
    }

    public void setParamMemo(String paramMemo) {
        this.paramMemo = paramMemo;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
