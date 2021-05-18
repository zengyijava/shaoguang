package com.montnets.emp.reportform.cxtjenum;

/**
 * 查询统计模板下拉框内容枚举类
 * @author Chenguang
 * @date 2018-12-18 08:37:00
 */
public enum OperatorEnum {
    /**
     * 运营商 -- 全部
     */
    OPERATOR_QB("全部", -1),
    /**
     * 运营商 -- 移动
     */
    OPERATOR_YD("移动", 0),
    /**
     * 运营商 -- 联通
     */
    OPERATOR_LT("联通", 1),
    /**
     * 运营商 -- 电信
     */
    OPERATOR_DX("电信", 21),
    /**
     * 运营商 -- 国外
     */
    OPERATOR_FOREIGN("国外", 5);

    private String name;
    private int code;

    OperatorEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code;
    }


    public String getName() {
        return name;
    }
}
