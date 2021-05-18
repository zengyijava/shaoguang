package com.montnets.emp.rms.meditor.vo;

import com.montnets.emp.rms.meditor.entity.TempDataParam;

import java.sql.Timestamp;

/**
 * 用于前端-后端-数据表的多表数据载体,用于查询模板列表接口，连接表有：LF_TEMPALTE、lf_SUB_TEMPLATE、LF_PARAM
 */
public class TempsVo {

    private Long tmId;
    private Integer tmpType;//模板类型（全部、11富媒体、12卡片和13富文本 14短信
    private Integer corpCode;//企业号
    private String tmName;//模板名称
    private Integer auditStatus;//网关审核状态，（-2未审批、-1审批中、0已禁用、1审批通过、2审批未通过）
    private Long sptemplid;//审核id
    private Timestamp addTime;//创建时间
    private String content;
    private Integer useId;//用途id
    private Integer industryId;//行业id
    private Integer isShortTemp;
    private Integer isPublic;//是否是公共模板 0不是 1是
    private Integer dsFlag;//模板静动态类型 1.通用动态模块;0.通用静态模块
    private Long tmState;//模板启用禁用状态1.启用，0.禁用
    private Long usecount;//使用次数
    private Long degreeSize;//容量
    private Integer degree;//档位
    private Long userId;//创建人
    private String cardHtml;
    private String fileUrl;
    private Integer previewType;//0只给主数据  1,给所有数据
    private String language;//语言
    private Integer currentPage;//当前页
    private Integer pageSize;//每页数据数
    private String addtimeBeg;
    private String addtimeEnd;
    private String tmMsg; //模板内容
    // 富信模板版本
    private String ver = "";
    private Integer h5Type;
    private String h5Url;
    //模板来源：0 emp web ； 1 托管版emp； 2 标准版emp； 3 rcos（审核平台）。 前端只取0、3即可，1、2由后端自己判断
    private Integer source;

    //是否包含共享模板
    private Integer containShare;

    //是否是素材  0 不是  1是
    private Integer isMaterial;

    @Override
    public String toString() {
        return "TempsVo{" +
                "tmId=" + tmId +
                ", tmpType=" + tmpType +
                ", corpCode=" + corpCode +
                ", tmName='" + tmName + '\'' +
                ", auditStatus=" + auditStatus +
                ", sptemplid=" + sptemplid +
                ", addTime=" + addTime +
                ", content='" + content + '\'' +
                ", useId=" + useId +
                ", industryId=" + industryId +
                ", isShortTemp=" + isShortTemp +
                ", isPublic=" + isPublic +
                ", dsFlag=" + dsFlag +
                ", tmState=" + tmState +
                ", usecount=" + usecount +
                ", degreeSize=" + degreeSize +
                ", degree=" + degree +
                ", userId=" + userId +
                ", cardHtml='" + cardHtml + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", previewType=" + previewType +
                ", language='" + language + '\'' +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", addtimeBeg='" + addtimeBeg + '\'' +
                ", addtimeEnd='" + addtimeEnd + '\'' +
                ", tmMsg='" + tmMsg + '\'' +
                ", ver='" + ver + '\'' +
                ", h5Type=" + h5Type +
                ", h5Url='" + h5Url + '\'' +
                ", source=" + source +
                ", containShare=" + containShare +
                ", isMaterial=" + isMaterial +
                ", tempDataParam=" + tempDataParam +
                '}';
    }

    public Integer getIsMaterial() {
        return isMaterial;
    }

    public void setIsMaterial(Integer isMaterial) {
        this.isMaterial = isMaterial;
    }

    public Integer getContainShare() {
        return containShare;
    }

    public void setContainShare(Integer containShare) {
        this.containShare = containShare;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getTmMsg() {
        return tmMsg;
    }

    public void setTmMsg(String tmMsg) {
        this.tmMsg = tmMsg;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getCardHtml() {
        return cardHtml;
    }

    public void setCardHtml(String cardHtml) {
        this.cardHtml = cardHtml;
    }

    public Integer getIsShortTemp() {
        return isShortTemp;
    }

    public void setIsShortTemp(Integer isShortTemp) {
        this.isShortTemp = isShortTemp;
    }

    public Long getUsecount() {
        return usecount;
    }

    public void setUsecount(Long usecount) {
        this.usecount = usecount;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private TempDataParam tempDataParam;//参数

    public Integer getTmpType() {
        return tmpType;
    }

    public void setTmpType(Integer tmpType) {
        this.tmpType = tmpType;
    }

    public Integer getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(Integer corpCode) {
        this.corpCode = corpCode;
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

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
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

    public String getAddtimeBeg() {
        return addtimeBeg;
    }

    public void setAddtimeBeg(String addtimeBeg) {
        this.addtimeBeg = addtimeBeg;
    }

    public String getAddtimeEnd() {
        return addtimeEnd;
    }

    public void setAddtimeEnd(String addtimeEnd) {
        this.addtimeEnd = addtimeEnd;
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

    public TempDataParam getTempDataParam() {
        return tempDataParam;
    }

    public void setTempDataParam(TempDataParam tempDataParam) {
        this.tempDataParam = tempDataParam;
    }

    public Long getTmId() {
        return tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
    }

    public Integer getPreviewType() {
        return previewType;
    }

    public void setPreviewType(Integer previewType) {
        this.previewType = previewType;
    }

    public Integer getH5Type() {
        return h5Type;
    }

    public void setH5Type(Integer h5Type) {
        this.h5Type = h5Type;
    }
}
