package com.montnets.emp.rms.vo;

/**
 * 企业富信-数据查询-发送明细查询的VO对象
 */
public class RmsMtRecordVo {
    /**
     * sp账号
     */
    private String spUser;
    /**
     * 通道号
     */
    private String spGate;
    /**
     * 发送主题
     */
    private String sendSubject;
    /**
     * 业务类型
     */
    private String svrtype;
    /**
     * 业务类型名称
     */
    private String busTypeName;

    /**
     * 富信主题
     */
    private String rmsSubject;
    /**
     * 场景ID
     */
    private Long tmplId;
    /**
     * 档位
     */
    private Integer degree;
    /**
     * 运营商
     */
    private Integer unicom;
    /**
     * 运营商名字
     */
    private String unicomName;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 任务批次
     */
    private Integer taskId;
    /**
     * 接收状态
     */
    private String receStatus;
    /**
     * 下载状态
     */
    private String downStatus;
    /**
     * 发送时间
     */
    private String sendTime;
    /**
     * 接收时间
     */
    private String recvTime;
    /**
     * 下载时间
     */
    private String downTime;
    /**
     * 自定义流水号(旧版)
     */
    private Long userMsgId;
    /**
     * 运营商流水号
     */
    private Long ptmsgid;
    /**
     * 富信下载状态报告错误码
     */
    private String errorCode2;
    /**
     * 接收状态报告错误码
     */
    private String errorCode;
    /**
     * 自定义流水号(新版)
     */
    private String custId;

    /**
     * 模板自增id
     */
    private Long tmId;

    public Long getTmId() {
        return tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getSpUser() {
        return spUser;
    }

    public void setSpUser(String spUser) {
        this.spUser = spUser;
    }

    public String getSpGate() {
        return spGate;
    }

    public void setSpGate(String spGate) {
        this.spGate = spGate;
    }

    public String getSendSubject() {
        return sendSubject;
    }

    public void setSendSubject(String sendSubject) {
        this.sendSubject = sendSubject;
    }

    public String getRmsSubject() {
        return rmsSubject;
    }

    public void setRmsSubject(String rmsSubject) {
        this.rmsSubject = rmsSubject;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Integer getUnicom() {
        return unicom;
    }

    public void setUnicom(Integer unicom) {
        this.unicom = unicom;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getReceStatus() {
        return receStatus;
    }

    public void setReceStatus(String receStatus) {
        this.receStatus = receStatus;
    }

    public String getDownStatus() {
        return downStatus;
    }

    public void setDownStatus(String downStatus) {
        this.downStatus = downStatus;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getDownTime() {
        return downTime;
    }

    public void setDownTime(String downTime) {
        this.downTime = downTime;
    }

    public Long getUserMsgId() {
        return userMsgId;
    }

    public void setUserMsgId(Long userMsgId) {
        this.userMsgId = userMsgId;
    }

    public Long getOperatorMsgId() {
        return ptmsgid;
    }

    public void setOperatorMsgId(Long operatorMsgId) {
        this.ptmsgid = operatorMsgId;
    }

    public Long getTmplId() {
        return tmplId;
    }

    public void setTmplId(Long tmplId) {
        this.tmplId = tmplId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(String recvTime) {
        this.recvTime = recvTime;
    }

    public String getErrorCode2() {
        return errorCode2;
    }

    public void setErrorCode2(String errorCode2) {
        this.errorCode2 = errorCode2;
    }

    public String getUnicomName() {
        return unicomName;
    }

    public void setUnicomName(String unicomName) {
        this.unicomName = unicomName;
    }

    public Long getPtmsgid() {
        return ptmsgid;
    }

    public void setPtmsgid(Long ptmsgid) {
        this.ptmsgid = ptmsgid;
    }


    public String getBusTypeName() {
        return busTypeName;
    }

    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }

    public String getSvrtype() {
        return svrtype;
    }

    public void setSvrtype(String svrtype) {
        this.svrtype = svrtype;
    }
}
