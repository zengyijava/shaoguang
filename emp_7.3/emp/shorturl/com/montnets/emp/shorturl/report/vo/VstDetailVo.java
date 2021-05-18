package com.montnets.emp.shorturl.report.vo;

import java.sql.Timestamp;

public class VstDetailVo {
    private String phone;
    //1表示已访问 0表示未访问
    private String visitStatus;
    private String mobileArea;
    private String visitIP;
    private String vsttm;
    private String endTime;
    private String cropCode;
    private String taskId;
    private String srcAddress;
    //区域名字
    private String areaName;
    //批次发送时间
    private Timestamp sendTime;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        this.visitStatus = visitStatus;
    }

    public String getMobileArea() {
        return mobileArea;
    }

    public void setMobileArea(String mobileArea) {
        this.mobileArea = mobileArea;
    }

    public String getVisitIP() {
        return visitIP;
    }

    public void setVisitIP(String visitIP) {
        this.visitIP = visitIP;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCropCode() {
        return cropCode;
    }

    public void setCropCode(String cropCode) {
        this.cropCode = cropCode;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        this.srcAddress = srcAddress;
    }

    public String getVsttm() {
        return vsttm;
    }

    public void setVsttm(String vsttm) {
        this.vsttm = vsttm;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
