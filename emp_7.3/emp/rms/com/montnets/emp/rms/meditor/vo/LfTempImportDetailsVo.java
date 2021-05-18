package com.montnets.emp.rms.meditor.vo;

import java.sql.Timestamp;

public class LfTempImportDetailsVo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 所属批次
     */
    private Long batch;

    /**
     * 富信主题(模板名称)
     */
    private String tmName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String phoneNum;

    /**
     * 成绩
     */
    private String score;

    /**
     * 审核id
     */
    private Long sptemplid;


    /**
     * 导入状态 (1.成功  0失败)
     */
    private Integer importStatus;

    /**
     * 发送状态 (1.成功  0失败)
     */
    private Integer sendStatus;

    /**
     * 备注
     */
    private String cause;

    /**
     * 图片地址
     */
    private String imageSrc;

    /**
     * 视频地址
     */
    private String videoSrc;

    /**
     * 添加时间
     */
    private Timestamp addtime;
    /**
     * 网关审核状态，（－1.未审批；0.无需审批；1.审批通过；2.审批未通过；3审核中；4禁用）
     */
    private Integer auditstatus;

    private String corpCode;

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Long getSptemplid() {
        return sptemplid;
    }

    public void setSptemplid(Long sptemplid) {
        this.sptemplid = sptemplid;
    }

    public Integer getImportStatus() {
        return importStatus;
    }
    public void setImportStatus(Integer importStatus) {
        this.importStatus = importStatus;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getVideoSrc() {
        return videoSrc;
    }

    public void setVideoSrc(String videoSrc) {
        this.videoSrc = videoSrc;
    }

    public Timestamp getAddtime() {
        return addtime;
    }

    public void setAddtime(Timestamp addtime) {
        this.addtime = addtime;
    }

    public Integer getAuditstatus() {
        return auditstatus;
    }

    public void setAuditstatus(Integer auditstatus) {
        this.auditstatus = auditstatus;
    }

    @Override
    public String toString() {
        return "LfTempImportDetailsVo{" +
                "id=" + id +
                ", batch=" + batch +
                ", tmName='" + tmName + '\'' +
                ", name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", score='" + score + '\'' +
                ", sptemplid=" + sptemplid +
                ", importStatus=" + importStatus +
                ", sendStatus=" + sendStatus +
                ", cause='" + cause + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", videoSrc='" + videoSrc + '\'' +
                ", addtime=" + addtime +
                ", auditstatus=" + auditstatus +
                ", corpCode='" + corpCode + '\'' +
                '}';
    }
}
