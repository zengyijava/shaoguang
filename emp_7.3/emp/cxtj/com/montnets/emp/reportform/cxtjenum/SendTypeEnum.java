package com.montnets.emp.reportform.cxtjenum;

/**
 * 查询统计模板下拉框内容枚举类
 * @author Chenguang
 * @date 2018-12-18 08:37:00
 */
public enum SendTypeEnum {
    /**
     * 发送类型 -- 全部
     */
    WHOLE_SEND("全部", 0),
    /**
     * 发送类型 -- EMP发送
     */
    EMP_SEND("EMP发送", 1),
    /**
     * 发送类型 -- HTTP接入
     */
    HTTP_SEND("HTTP接入", 2),
    /**
     * 发送类型 -- DB接入
     */
    DB_SEND("DB接入", 3),
    /**
     * 发送类型 -- 直连接入
     */
    DIRECT_SEND("直连接入", 4);

    private String name;
    private int code;

    SendTypeEnum(String name, int code) {
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
