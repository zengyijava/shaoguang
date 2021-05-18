package com.montnets.emp.degree.vo;

import java.sql.Timestamp;

public class LfDegreeManageVo implements java.io.Serializable {
	private static final long serialVersionUID = -7412464189898873992L;

	/**
	 * 主键ID
	 */
	private Long id;
	private Long tableId;
	/**
	 * 档位 1-5档
	 */
	private Integer degree;
	/**
	 * 档位容量开始
	 */
	private String degreeBegin;
	/**
	 * 档位容量结束
	 */
	private String degreeEnd;
	/**
	 * 操作员ID
	 */
	private Long userId;
	//有效开始时间
	private Timestamp validDateBegin;
	/**
	 * 有效截止时间
	 */
	private Timestamp validDateEnd;
	/**
	 * 状态，0-启用，1-禁用，2-过期，默认为0
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 是否修改
	 */
	private Boolean isUpdate;
	/**
	 * 操作员名称
	 */
	private String userName;
	/**
	 * 企业编码
	 */
	private String corpCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public String getDegreeBegin() {
		return degreeBegin;
	}

	public void setDegreeBegin(String degreeBegin) {
		this.degreeBegin = degreeBegin;
	}

	public String getDegreeEnd() {
		return degreeEnd;
	}

	public void setDegreeEnd(String degreeEnd) {
		this.degreeEnd = degreeEnd;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Timestamp getValidDateBegin() {
		return validDateBegin;
	}

	public void setValidDateBegin(Timestamp validDateBegin) {
		this.validDateBegin = validDateBegin;
	}

	public Timestamp getValidDateEnd() {
		return validDateEnd;
	}

	public void setValidDateEnd(Timestamp validDateEnd) {
		this.validDateEnd = validDateEnd;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Boolean getUpdate() {
		return isUpdate;
	}

	public void setUpdate(Boolean update) {
		isUpdate = update;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
}
