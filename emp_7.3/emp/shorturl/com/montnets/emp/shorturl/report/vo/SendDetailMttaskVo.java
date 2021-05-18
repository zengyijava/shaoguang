package com.montnets.emp.shorturl.report.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class SendDetailMttaskVo implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long unicom;
    private String phone;
    private String message;
    private String errorcode;
    private Long pknumber;
    private Long pktotal;
    private String taskId;
    private Timestamp sendTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUnicom() {
        return unicom;
    }

    public void setUnicom(Long unicom) {
        this.unicom = unicom;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public Long getPknumber() {
        return pknumber;
    }

    public void setPknumber(Long pknumber) {
        this.pknumber = pknumber;
    }

    public Long getPktotal() {
        return pktotal;
    }

    public void setPktotal(Long pktotal) {
        this.pktotal = pktotal;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }
}
