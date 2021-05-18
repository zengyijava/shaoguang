package com.montnets.emp.common.entity;

/**
 * @功能概要：
 * @项目名称： emp_std_192.169.1.81
 * @初创作者： Administrator
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016/4/20 9:39
 * <p>修改记录1：</p>
 * <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class LfChangeLog {

    //主要版本号
    private String majorversion;

    //发行时间
    private String releasetime = "";

    //是否为补丁版本 1：是 0：否
    private String issp = "0";

    //补丁版本信息
    private String servicepack;

    //版本功能描述
    private String releaseNote = "";

    public String getMajorversion() {
        return majorversion;
    }

    public void setMajorversion(String majorversion) {
        this.majorversion = majorversion;
    }

    public String getReleasetime() {
        return releasetime;
    }

    public void setReleasetime(String releasetime) {
        this.releasetime = releasetime;
    }

    public String getIssp() {
        return issp;
    }

    public void setIssp(String issp) {
        this.issp = issp;
    }

    public String getServicepack() {
        return servicepack;
    }

    public void setServicepack(String servicepack) {
        this.servicepack = servicepack;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }
}
