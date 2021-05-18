package com.montnets.emp.rms.meditor.dto;

import java.sql.Timestamp;

public class DetailInfo {

    private String tmName;
    private Long tmId;
    private Long usecounts;
    private Long degreeSize;
    private Integer degree;
    private Integer dsFlag;
    private String createUser;
    private String depName;
    private String corpCode;
    private Long addTime;
    private Long tmState;
    private Integer auditStatus;
    private Long sptemplid;

    @Override
    public String toString() {
        return "DetailInfo{" +
                "tmName='" + tmName + '\'' +
                ", tmId=" + tmId +
                ", usecounts=" + usecounts +
                ", degreeSize=" + degreeSize +
                ", degree=" + degree +
                ", dsFlag=" + dsFlag +
                ", createUser='" + createUser + '\'' +
                ", depName='" + depName + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", addTime=" + addTime +
                ", tmState=" + tmState +
                ", auditStatus=" + auditStatus +
                ", sptemplid=" + sptemplid +
                '}';
    }

    public Long getSptemplid() {
        return sptemplid;
    }

    public void setSptemplid(Long sptemplid) {
        this.sptemplid = sptemplid;
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName;
    }

    public Long getTmId() {
        return tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
    }

    public Long getUsecounts() {
        return usecounts;
    }

    public void setUsecounts(Long usecounts) {
        this.usecounts = usecounts;
    }

    public Long getDegreeSize() {
        return degreeSize;
    }

    public void setDegreeSize(Long degreeSize) {
        this.degreeSize = degreeSize;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Integer getDsFlag() {
        return dsFlag;
    }

    public void setDsFlag(Integer dsFlag) {
        this.dsFlag = dsFlag;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public Long getAddTime() {
		return addTime;
	}

	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}

	public Long getTmState() {
        return tmState;
    }

    public void setTmState(Long tmState) {
        this.tmState = tmState;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
}
