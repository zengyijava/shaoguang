package com.montnets.emp.shorturl.report.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class BatchVisitVo implements Serializable {
    //private Long mtId;
    private Long userId;
    private String title;
    //private Integer msgType;
    //private Timestamp submitTime;
    //private Integer subState;
    //private Integer reState;
    //private Integer sendstate;
    //private Integer sendLevel;
    //private Integer bmtType;
    //private String spUser;
    //private String mobileUrl;
    //private String subCount;
    private String effCount;
    //private String sucCount;
    //private String faiCount;
    private String msg;
    //private String spPwd;
    //private String comments;
    //private String icount;
    //private String name;
    private String userName;
    private String depName;
    private Long depId;
    //private String staffName;
    //private String startSubmitTime;
    private String endSubmitTime;
    private String startSendTime;
    private String endSendTime;
    //private Timestamp timerTime;
    //private String busCode;
    //private String busName;
    //private Integer timerStatus;
    private Integer msType;
    //private String reStates;
    private String userIds;
    private String depIds;
    //private String overSendstate;
    //private String errorCodes;
    //private String msTypes;
    //private Integer isReply;
    //private Integer isRetry;
    //private Long rFail2;
    //private String icount2;
    private Integer userState;
    private String corpCode;
    //private String taskState;
    private Long taskId;
    //private Integer taskType;
    //private Long batchID;
    //private Long flowID;
    private String isContainsSun;
    //private String mtIdCipher;
    //private Integer validtm;
    //private String spNumber;
    //private String subNo;
    //访问人数
    private Integer visitorCount;
    //访问次数
    private Integer visitCount;
    //长地址
    private String netUrl;
    //短地址
    private String domainUrl;
    //有效时间
    private Double validDays;
    //发送时间
    private Timestamp planTime;
    //短链生成时间
    private Timestamp createTm;
    //短链失效时间
    private Timestamp invalidTm;
    //当前企业编码
    private String currCorpCode;
    //当前操作员编码
    private String currUserId;
    //当前操作员名字
    private String currUserName;
    //当前操作员数据权限
    private int currUserDataPri;

    public String getCurrCorpCode() {
        return currCorpCode;
    }

    public void setCurrCorpCode(String currCorpCode) {
        this.currCorpCode = currCorpCode;
    }

    public String getCurrUserId() {
        return currUserId;
    }

    public void setCurrUserId(String currUserId) {
        this.currUserId = currUserId;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public Integer getMsType() {
        return msType;
    }

    public void setMsType(Integer msType) {
        this.msType = msType;
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

    public String getIsContainsSun() {
        return isContainsSun;
    }

    public void setIsContainsSun(String isContainsSun) {
        this.isContainsSun = isContainsSun;
    }

    public Integer getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(Integer visitorCount) {
        this.visitorCount = visitorCount;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
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

    public String getNetUrl() {
        return netUrl;
    }

    public void setNetUrl(String netUrl) {
        this.netUrl = netUrl;
    }

    public Double getValidDays() {
        return validDays;
    }

    public void setValidDays(Double validDays) {
        this.validDays = validDays;
    }

    public Timestamp getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Timestamp createTm) {
        this.createTm = createTm;
    }

    public Timestamp getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Timestamp planTime) {
        this.planTime = planTime;
    }

    public String getEffCount() {
        return effCount;
    }

    public void setEffCount(String effCount) {
        this.effCount = effCount;
    }

    public Timestamp getInvalidTm() {
        return invalidTm;
    }

    public void setInvalidTm(Timestamp invalidTm) {
        this.invalidTm = invalidTm;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getCurrUserName() {
        return currUserName;
    }

    public void setCurrUserName(String currUserName) {
        this.currUserName = currUserName;
    }

    public int getCurrUserDataPri() {
        return currUserDataPri;
    }

    public void setCurrUserDataPri(int currUserDataPri) {
        this.currUserDataPri = currUserDataPri;
    }
}
