package com.montnets.emp.common.security.entity;

/**
 * @author liangHuaGeng
 * @Title: FilterValue
 * @ProjectName emp_7.3
 * @Description: TODO
 * @date 2019/1/1610:04
 */
public class FilterValue {

    /**
     * 需要过滤的内容
     */
    private String value;

    public FilterValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
