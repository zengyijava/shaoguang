package com.montnets.emp.entity.sms;

import java.sql.Timestamp;

public class LfSubDrafts {

    private Long id;
    private Long draftId; //主草稿箱记录ID
    private Long domainId;
    private Long netUrlId;
    private Integer type; //有效天数值
    private Timestamp createtime;
    private Timestamp updatetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDraftId() {
        return draftId;
    }

    public void setDraftId(Long draftId) {
        this.draftId = draftId;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public Long getNetUrlId() {
        return netUrlId;
    }

    public void setNetUrlId(Long netUrlId) {
        this.netUrlId = netUrlId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }
}
