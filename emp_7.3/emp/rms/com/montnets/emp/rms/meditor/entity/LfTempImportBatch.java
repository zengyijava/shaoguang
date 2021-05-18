package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Description: 模板导入批次
 * @author hsh
 * @date 2018-11-07 上午10:33:01
 */
public class LfTempImportBatch implements Serializable {

    private static final long serialVersionUID = -6891829146186524634L;

    /**
     * 主键id
     */
    private Long  id;

    /**
     * 批次
     */
    private Long batch;

    /**
     * 企业编码
     */
    private String corpCode;

    /**
     * 企业名称
     */
    private String corpName;

    /**
     * 总数量
     */
    private Long amount;

    /**
     * 成功总数
     */
    //private Long successAccount = 0L;
    private Long successAmount;

    /**
     * 失败总数
     */
    private Long failAmount;

    /**
     * 处理状态 1处理完成 0处理中
     */
    private Integer processStatus;

    /**
     * 富信名称
     */
    private String tmName;

    /**
     * 添加时间
     */
    private Timestamp addtime;

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName;
    }

    public LfTempImportBatch() {
        addtime = new Timestamp(System.currentTimeMillis());
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public Timestamp getAddtime() {
        return addtime;
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

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }


    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public Long getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(Long successAmount) {
        this.successAmount = successAmount;
    }

    public Long getFailAmount() {
        return failAmount;
    }

    public void setFailAmount(Long failAmount) {
        this.failAmount = failAmount;
    }

    @Override
    public String toString() {
        return "LfTempImportBatch{" +
                "id=" + id +
                ", batch=" + batch +
                ", corpCode='" + corpCode + '\'' +
                ", corpName='" + corpName + '\'' +
                ", amount=" + amount +
                ", successAmount=" + successAmount +
                ", failAmount=" + failAmount +
                ", processStatus=" + processStatus +
                ", addtime=" + addtime +
                '}';
    }
}
