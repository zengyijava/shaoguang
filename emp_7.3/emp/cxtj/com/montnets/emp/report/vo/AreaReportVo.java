package com.montnets.emp.report.vo;

import java.io.Serializable;

/**
 * 区域报表实体类
 *
 * @author liaojirong <ljr0300@163.com>
 * @project p_cxtj
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 下午07:27:19
 * @description
 */
public class AreaReportVo implements Serializable {
    private static final long serialVersionUID = 7857327174754855656L;
    //年
    private String y;
    //月
    private String imonth;
    //区域编码
    private Integer mobilearea;
    //区域
    private String province;

    //接收失败数
    private Long rfail2;
    //提交总数
    private Long icount;
    //接受成功数
    private Long rsucc;
    //发送失败数
    private Long rfail1;
    //未返数
    private Long rnret;
    //报表类型
    private Integer reporttype;

    //账户类型 0短信 1彩信
    private Integer mstype;
    //企业编码
    private String corpcode;
    //处理日报表 起始日期
    private String startdate;
    //处理日报表 结束日期
    private String enddate;
    //是否详情查看
    private boolean isDes;
    //时间
    private String iymd;

    private String userCode;

    public Integer getMobilearea() {
        return mobilearea;
    }

    public void setMobilearea(Integer mobilearea) {
        this.mobilearea = mobilearea;
    }

    public String getCorpcode() {
        return corpcode;
    }

    public void setCorpcode(String corpcode) {
        this.corpcode = corpcode;
    }

    public Integer getReporttype() {
        return reporttype;
    }

    public void setReporttype(Integer reporttype) {
        this.reporttype = reporttype;
    }

    public Long getIcount() {
        return icount;
    }

    public void setIcount(Long icount) {
        this.icount = icount;
    }

    public Long getRsucc() {
        return rsucc;
    }

    public void setRsucc(Long rsucc) {
        this.rsucc = rsucc;
    }

    public Long getRfail1() {
        return rfail1;
    }

    public void setRfail1(Long rfail1) {
        this.rfail1 = rfail1;
    }

    public Long getRnret() {
        return rnret;
    }

    public void setRnret(Long rnret) {
        this.rnret = rnret;
    }

    public Long getRfail2() {
        return rfail2;
    }

    public void setRfail2(Long rfail2) {
        this.rfail2 = rfail2;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getMstype() {
        return mstype;
    }

    public void setMstype(Integer mstype) {
        this.mstype = mstype;
    }


    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getImonth() {
        return imonth;
    }

    public void setImonth(String imonth) {
        this.imonth = imonth;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public boolean isDes() {
        return isDes;
    }

    public void setDes(boolean isDes) {
        this.isDes = isDes;
    }

    public String getIymd() {
        return iymd;
    }

    public void setIymd(String iymd) {
        this.iymd = iymd;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
