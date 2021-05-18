package com.montnets.emp.reportform.cxtjenum;

/**
 * 跳转路径的枚举类
 * @author Chenguang
 * @date 2018-12-11 08:47:55
 */
public enum JumpPathEnum {
    /**
     * 运营商统计报表
     */
    spisuncmMtReport("运营商统计报表", "spisuncmMtReport",1, "icount,rsucc,rfail1,rfail2,rnret,iymd,spId,spisuncm"),
    /**
     * SP账号统计报表
     */
    spMtReport("SP账号统计报表", "spMtReport", 2, "icount,rsucc,rfail1,rfail2,rnret,iymd,userId,sptype,sendtype,staffname"),
    /**
     * 机构统计报表
     */
    sysDepReport("机构统计报表", "sysDepReport", 3, "icount,rsucc,rfail1,rfail2,rnret,iymd,depId,depName,idtype,userState"),
    /**
     * 操作员统计报表
     */
    sysUserReport("操作员统计报表", "sysUserReport", 4, "icount,rsucc,iymd,rfail1,rfail2,rnret,userState,name,depName"),
    /**
     * 业务类型统计报表
     */
    busReport("业务类型统计报表", "busReport", 5, "icount,rsucc,rfail1,rfail2,rnret,iymd,svrType,busName"),
    /**
     * 区域统计报表
     */
    areaReport("区域统计报表", "areaReport", 6, "icount,rsucc,iymd,rfail1,rfail2,rnret,province,city"),
    /**
     * 自定参数统计报表
     */
    dynParamReport("自定参数统计报表", "dynParamReport", 7, "icount,rsucc,rfail1,rfail2,rnret,paramValue,paramName");

    private String name;
    private String url;
    private int index;
    private String colName;
    /**
     * 构造方法
     * @param name 名字
     * @param url 路径
     * @param index 顺序
     * @param colName 报表展示字段
     */
    JumpPathEnum(String name, String url, int index, String colName) {
        this.name = name;
        this.url = url;
        this.index = index;
        this.colName = colName;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public String getUrl() {
        return url;
    }

    public String getColName() {
        return colName;
    }

}
