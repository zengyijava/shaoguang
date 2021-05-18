package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Description:
 * @Auther:xuty
 * @Date: 2018/8/2 15:55
 */
public class LfSubTemplate implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = -7957710953357884464L;
	private Long id;
    private Integer tmpType;
    private String frontJson = "{}";
    private Integer industryId;
    private Integer useId;
    private Integer status;
    private Timestamp addTime;
    private String endJson = "{}";
    private Integer priority;
    private String fileUrl = " ";
    private String content;
    private String cardHtml = " ";
    private Long tmId;
    private Integer degree;
    private Integer degreeSize;
    //H5编辑类型 - 0：长页，1-短页
    private Integer h5Type;

    private String app =" ";

    private String h5Url = "";

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTmpType() {
        return tmpType;
    }

    public void setTmpType(Integer tmpType) {
        this.tmpType = tmpType;
    }
    public String getFrontJson() {
        return frontJson;
    }

    public void setFrontJson(String frontJson) {
        this.frontJson = frontJson;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public Integer getUseId() {
        return useId;
    }

    public void setUseId(Integer useId) {
        this.useId = useId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public String getEndJson() {
        return endJson;
    }

    public void setEndJson(String endJson) {
        this.endJson = endJson;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCardHtml() {
        return cardHtml;
    }

    public void setCardHtml(String cardHtml) {
        this.cardHtml = cardHtml;
    }

    public Long getTmId() {
        return tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
    }

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public Integer getDegreeSize() {
		return degreeSize;
	}

	public void setDegreeSize(Integer degreeSize) {
		this.degreeSize = degreeSize;
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


}
