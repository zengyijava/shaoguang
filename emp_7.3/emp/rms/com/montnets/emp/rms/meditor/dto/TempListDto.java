package com.montnets.emp.rms.meditor.dto;

import java.sql.Timestamp;

public class TempListDto {
    private Long tmId;
    private Integer tmpType;//模板类型（全部、11富媒体、12卡片和13富文本 14短信
    private String tmName;//模板名称
    private Integer auditStatus;//网关审核状态，（-2未审批、-1审批中、0已禁用、1审批通过、2审批未通过）
    private Long sptemplid;//审核id
    private Long addTime;//创建时间
    private String content;
    private Integer useId;//用途id
    private Integer industryId;//行业id
    private Integer isShortTmp;

    private String language;//语言
    private Integer currentPage;//当前页
    private Integer pageSize;//每页数据数
    private Integer isPublic;//是否是公共模板 0不是 1是
    private Integer dsFlag;//模板静动态类型 1.通用动态模块;0.通用静态模块
    private Long tmState;//模板启用禁用状态1.启用，0.禁用
    private String cardHtml;
    private String fileUrl;

    private DetailInfo detailInfo;
    private Integer h5Type;
    private String app;
    private String h5Url;
    private Integer isShare;

    public Integer getIsShare() {
        return isShare;
    }

    public void setIsShare(Integer isShare) {
        this.isShare = isShare;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public Integer getH5Type() {
        return h5Type;
    }

    public void setH5Type(Integer h5Type) {
        this.h5Type = h5Type;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getTmId() {
        return tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
    }

    public Integer getTmpType() {
        return tmpType;
    }

    public void setTmpType(Integer tmpType) {
        this.tmpType = tmpType;
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Long getSptemplid() {
        return sptemplid;
    }

    public void setSptemplid(Long sptemplid) {
        this.sptemplid = sptemplid;
    }

    public Long getAddTime() {
		return addTime;
	}

	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}

	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUseId() {
        return useId;
    }

    public void setUseId(Integer useId) {
        this.useId = useId;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public Integer getIsShortTmp() {
        return isShortTmp;
    }

    public void setIsShortTmp(Integer isShortTmp) {
        this.isShortTmp = isShortTmp;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getDsFlag() {
        return dsFlag;
    }

    public void setDsFlag(Integer dsFlag) {
        this.dsFlag = dsFlag;
    }

    public Long getTmState() {
        return tmState;
    }

    public void setTmState(Long tmState) {
        this.tmState = tmState;
    }

    public String getCardHtml() {
        return cardHtml;
    }

    public void setCardHtml(String cardHtml) {
        this.cardHtml = cardHtml;
    }

    public DetailInfo getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(DetailInfo detailInfo) {
        this.detailInfo = detailInfo;
    }

    @Override
    public String toString() {
        return "TempListDto{" +
                "tmId=" + tmId +
                ", tmpType=" + tmpType +
                ", tmName='" + tmName + '\'' +
                ", auditStatus=" + auditStatus +
                ", sptemplid=" + sptemplid +
                ", addTime=" + addTime +
                ", content='" + content + '\'' +
                ", useId=" + useId +
                ", industryId=" + industryId +
                ", isShortTmp=" + isShortTmp +
                ", language='" + language + '\'' +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", isPublic=" + isPublic +
                ", dsFlag=" + dsFlag +
                ", tmState=" + tmState +
                ", cardHtml='" + cardHtml + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", detailInfo=" + detailInfo +
                ", h5Type=" + h5Type +
                ", app='" + app + '\'' +
                '}';
    }
}
