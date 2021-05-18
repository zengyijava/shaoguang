package com.montnets.emp.reportform.bean;

/**
 * 用于报表vo 自定义注解的类
 * @author Chenguang
 * @date 2018-12-21 09:22:36
 */
public class RptExportBean {
    /**
    * 表头顺序 一定要与module一一对应
     * 0基
     */
    private Integer index;
    /**
     * 获取对应字段的getter的方法名字
     */
    private String methodName;
    /**
     * 表头名称
     */
    private String thName;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getThName() {
        return thName;
    }

    public void setThName(String thName) {
        this.thName = thName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
