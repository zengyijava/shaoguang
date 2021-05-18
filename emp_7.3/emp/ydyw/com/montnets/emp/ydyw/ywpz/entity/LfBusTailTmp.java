package com.montnets.emp.ydyw.ywpz.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class LfBusTailTmp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6369799329019659838L;
	
	//主键ID
	private Long id;
	
	//业务ID
	private Long busId;
	
	//模板ID
	private Long tmId;
	
	//关联类型ASSOCIATE_TYPE：1、短信模板，0、贴尾
	private Integer associateType;
	
	//企业编码
	private String corpCode;
	
	//创建时间
	private Timestamp createTime;
	
	//修改时间
	private Timestamp updateTime;
	
	//创建人机构ID
	private Long depId;
	
	//创建人ID
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBusId() {
		return busId;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}

	public Long getTmId() {
		return tmId;
	}

	public void setTmId(Long tmId) {
		this.tmId = tmId;
	}

	public Integer getAssociateType() {
		return associateType;
	}

	public void setAssociateType(Integer associateType) {
		this.associateType = associateType;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
