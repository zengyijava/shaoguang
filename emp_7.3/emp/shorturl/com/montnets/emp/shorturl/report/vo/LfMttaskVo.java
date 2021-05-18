package com.montnets.emp.shorturl.report.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class LfMttaskVo implements Serializable {
    private Long userId;
    private String title;
    private Integer sendstate;
    private String spUser;
    private String subCount;
    private String effCount;
    private String sucCount;
    private String faiCount;
    private String msg;
    private String icount;
    private String userName;
    private String depName;
    private Long depId;
    private String startSendTime;
    private String endSendTime;
    private Timestamp timerTime;
    private Integer isReply;
    private Integer isRetry;
    private Long rFail2;
    private String icount2;
    private Integer userState;
    private String corpCode;
    private Long taskId;
    private Integer taskType;
    private String isContainsSun;
    private String netUrl;
    private String domainUrl;
    private String msType;
    private String overSendstate;
    private String name;
    private String errorCodes;
    private String mobileUrl;

    public String getNetUrl() {
        return netUrl;
    }

    public void setNetUrl(String netUrl) {
        this.netUrl = netUrl;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getDepIds() {
        return depIds;
    }

    public void setDepIds(String depIds) {
        this.depIds = depIds;
    }

    private String userIds;
    private String depIds;

    public LfMttaskVo() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSendstate() {
        return sendstate;
    }

    public void setSendstate(Integer sendstate) {
        this.sendstate = sendstate;
    }

    public String getSpUser() {
        return spUser;
    }

    public void setSpUser(String spUser) {
        this.spUser = spUser;
    }

    public String getSubCount() {
        return subCount;
    }

    public void setSubCount(String subCount) {
        this.subCount = subCount;
    }

    public String getEffCount() {
        return effCount;
    }

    public void setEffCount(String effCount) {
        this.effCount = effCount;
    }

    public String getSucCount() {
        return sucCount;
    }

    public void setSucCount(String sucCount) {
        this.sucCount = sucCount;
    }

    public String getFaiCount() {
        return faiCount;
    }

    public void setFaiCount(String faiCount) {
        this.faiCount = faiCount;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIcount() {
        return icount;
    }

    public void setIcount(String icount) {
        this.icount = icount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public Long getDepId() {
        return depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    public String getStartSendTime() {
        return startSendTime;
    }

    public void setStartSendTime(String startSendTime) {
        this.startSendTime = startSendTime;
    }

    public String getEndSendTime() {
        return endSendTime;
    }

    public void setEndSendTime(String endSendTime) {
        this.endSendTime = endSendTime;
    }

    public Timestamp getTimerTime() {
        return timerTime;
    }

    public void setTimerTime(Timestamp timerTime) {
        this.timerTime = timerTime;
    }

    public Integer getIsReply() {
        return isReply;
    }

    public void setIsReply(Integer isReply) {
        this.isReply = isReply;
    }

    public Integer getIsRetry() {
        return isRetry;
    }

    public void setIsRetry(Integer isRetry) {
        this.isRetry = isRetry;
    }

    public String getIcount2() {
        return icount2;
    }

    public void setIcount2(String icount2) {
        this.icount2 = icount2;
    }

    public Integer getUserState() {
        return userState;
    }

    public void setUserState(Integer userState) {
        this.userState = userState;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getIsContainsSun() {
        return isContainsSun;
    }

    public void setIsContainsSun(String isContainsSun) {
        this.isContainsSun = isContainsSun;
    }

    public String getMsType() {
        return msType;
    }

    public void setMsType(String msType) {
        this.msType = msType;
    }

    public String getOverSendstate() {
        return overSendstate;
    }

    public void setOverSendstate(String overSendstate) {
        this.overSendstate = overSendstate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(String errorCodes) {
        this.errorCodes = errorCodes;
    }

    public Long getrFail2() {
        return rFail2;
    }

    public void setRFail2(Long rFail2) {
        this.rFail2 = rFail2;
    }

    public String getMobileUrl() {
        return mobileUrl;
    }

    public void setMobileUrl(String mobileUrl) {
        this.mobileUrl = mobileUrl;
    }
}
