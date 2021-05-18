package com.montnets.emp.rms.report.vo;

import java.io.Serializable;

/**
 * 档位统计报表VO对象
 *
 * @date 2018-6-13 14:40:14
 * @author Cheng
 */
public class MtDataReportVo implements Serializable {
    private static final long serialVersionUID = 6350028530418409007L;
    /**
     * SP账号
     */
    private String userId;
    /**
     * 日期(年月日)
     */
    private Integer iymd;
    /**
     * 月份
     */
    private Integer imonth;
    /**
     * 年
     */
    private Integer y;
    /**
     * 发送总数
     */
    private Integer icount;
    /**
     * R失败1数量
     */
    private Integer rfail1;
    /**
     * 接收失败数
     */
    private Integer recfail;
    /**
     * 接收成功数
     */
    private Integer rsucc;
    /**
     * 未返数
     */
    private Long rnret;
    /**
     * 下载成功数
     */
    //private Integer dwsucc;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 运营商 0 ：移动 1 ：联通 21：电信
     */
    private Integer spisuncm;
    /**
     * 档位信息
     */
    private Integer chgrade;
    /**
     * 企业编码
     */
    private String corpCode;
    /**
     * 企业名称
     */
    private String corpName;
    /**
     * 报表类型
     */
    private Integer reportType;
    /**
     * 是否详情
     */
    private Boolean isDes = false;
    /**
     * 登录企业编码
     */
    private String loginCorpCode;
    /**
     * 页面上显示的时间
     */
    private String showTime;
    /**
     * 运营商名字
     */
    private String spisuncmName;
    /**
     * 点击详情时需要带过去的信息
     */
    private String detailInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getIymd() {
        return iymd;
    }

    public void setIymd(Integer iymd) {
        this.iymd = iymd;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getIcount() {
        return icount;
    }

    public void setIcount(Integer icount) {
        this.icount = icount;
    }

    public Integer getRfail1() {
        return rfail1;
    }

    public void setRfail1(Integer rfail1) {
        this.rfail1 = rfail1;
    }

    public Integer getRsucc() {
        return rsucc;
    }

    public void setRsucc(Integer rsucc) {
        this.rsucc = rsucc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getSpisuncm() {
        return spisuncm;
    }

    public void setSpisuncm(Integer spisuncm) {
        this.spisuncm = spisuncm;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Boolean getDes() {
        return isDes;
    }

    public void setDes(Boolean des) {
        isDes = des;
    }

    public String getLoginCorpCode() {
        return loginCorpCode;
    }

    public void setLoginCorpCode(String loginCorpCode) {
        this.loginCorpCode = loginCorpCode;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getSpisuncmName() {
        return spisuncmName;
    }

    public void setSpisuncmName(String spisuncmName) {
        this.spisuncmName = spisuncmName;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public Integer getImonth() {
        return imonth;
    }

    public void setImonth(Integer imonth) {
        this.imonth = imonth;
    }

    public Integer getChgrade() {
        return chgrade;
    }

    public void setChgrade(Integer chgrade) {
        this.chgrade = chgrade;
    }

	public Integer getRecfail() {
		return recfail;
	}

	public void setRecfail(Integer recfail) {
		this.recfail = recfail;
	}

	public Long getRnret() {
		return rnret;
	}

	public void setRnret(Long rnret) {
		this.rnret = rnret;
	}
	
}
