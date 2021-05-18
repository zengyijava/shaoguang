package com.montnets.emp.rms.meditor.entity;

import java.sql.Timestamp;

public class LfTempSynch {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 模板id
     */
    private Long spTemplateid;
    /**
     * 同步次数
     */
    private Integer count;
    /**
     * 同步状态
     */
    private Integer synstatus;
    /**
     * 备注
     */
    private String cause;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 素材还是企业模板 0 -素材，1-企业模板
     */

    private Integer isMaterial;

    public LfTempSynch() {
        updateTime = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSpTemplateid() {
        return spTemplateid;
    }

    public void setSpTemplateid(Long spTemplateid) {
        this.spTemplateid = spTemplateid;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSynstatus() {
        return synstatus;
    }

    public void setSynstatus(Integer synstatus) {
        this.synstatus = synstatus;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Integer getIsMaterial() {
        return isMaterial;
    }

    public void setIsMaterial(Integer isMaterial) {
        this.isMaterial = isMaterial;
    }

    @Override
    public String toString() {
        return "LfTempSynch{" +
                "id=" + id +
                ", spTemplateid=" + spTemplateid +
                ", count=" + count +
                ", synstatus=" + synstatus +
                ", cause='" + cause + '\'' +
                ", updateTime=" + updateTime +
                ", isMaterial=" + isMaterial +
                '}';
    }
}

