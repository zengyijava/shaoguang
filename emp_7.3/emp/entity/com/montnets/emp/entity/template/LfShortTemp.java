package com.montnets.emp.entity.template;

import java.sql.Timestamp;

public class LfShortTemp {
    //ID
    private Integer id;
    //模板ID
    private Long tempId;
    //模板名称
    private String tempName;
    //用户id
    private Long userId;
    //企业编号
    private String corpCode;

    private Timestamp addTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTempId() {
        return tempId;
    }

    public void setTempId(Long tempId) {
        this.tempId = tempId;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "LfShortTemp{" +
                "id=" + id +
                ", tempId=" + tempId +
                ", tempName='" + tempName + '\'' +
                ", userId=" + userId +
                ", corpCode='" + corpCode + '\'' +
                ", addTime='" + addTime + '\'' +
                '}';
    }
}
