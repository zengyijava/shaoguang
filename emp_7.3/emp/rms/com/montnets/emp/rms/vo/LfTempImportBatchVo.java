package com.montnets.emp.rms.vo;

import java.sql.Timestamp;

public class LfTempImportBatchVo {
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
    /**
     * 发送状态
     */
    private Integer sendState;


    public Integer getSendState() {
        return sendState;
    }

    public void setSendState(Integer sendState) {
        this.sendState = sendState;
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

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName;
    }

    public Timestamp getAddtime() {
        return addtime;
    }

    public void setAddtime(Timestamp addtime) {
        this.addtime = addtime;
    }
}
