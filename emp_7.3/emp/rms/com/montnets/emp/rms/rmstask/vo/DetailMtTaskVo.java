package com.montnets.emp.rms.rmstask.vo;

/**
 * 详情查看Vo对象
 * @author Cheng
 * @date 2018-8-23 11:03:25
 */
public class DetailMtTaskVo {
    /**
     * 运营商 0-移动 1联通 21电信
     */
    private Integer unicom;
    /**
     * 运营商名字
     */
    private String unicomName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 下载状态 DELIVRD 成功  空为未返
     */
    private String errorCode2;
    /**
     * 回复状态 DELIVRD 成功  空为未返 失败：E1:0042
     */
    private String errorCode;
    /**
     * 下载状态报告 下载成功 下载失败 未返
     */
    private String downStatus;
    /**
     * 发送状态报告 发送成功 发送失败 未返
     */
    private String sendStatus;

    public Integer getUnicom() {
        return unicom;
    }

    public void setUnicom(Integer unicom) {
        this.unicom = unicom;
    }

    public String getUnicomName() {
        return unicomName;
    }

    public void setUnicomName(String unicomName) {
        this.unicomName = unicomName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getErrorCode2() {
        return errorCode2;
    }

    public void setErrorCode2(String errorCode2) {
        this.errorCode2 = errorCode2;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDownStatus() {
        return downStatus;
    }

    public void setDownStatus(String downStatus) {
        this.downStatus = downStatus;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }
}
