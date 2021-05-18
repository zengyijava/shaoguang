package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Description: 模板导入详情
 * @author hsh
 * @date 2018-11-07 上午10:47:01
 */
public class LfTempImportDetails implements Serializable  {

    private static final long serialVersionUID = 4552344638651076564L;

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
     * 企业编码
     */
    private String corpCode;

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public LfTempImportDetails() {
        addtime = new Timestamp(System.currentTimeMillis());
    }

    public Integer getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(Integer importStatus) {
        this.importStatus = importStatus;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Timestamp getAddtime() {
        return addtime;
    }

    public String getTmName() {
        return tmName;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName;
    }

    public void setAddtime(Timestamp addtime) {
        this.addtime = addtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getSptemplid() {
        return sptemplid;
    }

    public void setSptemplid(Long sptemplid) {
        this.sptemplid = sptemplid;
    }


    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Override
    public String toString() {
        return "LfTempImportDetails{" +
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
                ", corpCode='" + corpCode + '\'' +
                '}';
    }
}
