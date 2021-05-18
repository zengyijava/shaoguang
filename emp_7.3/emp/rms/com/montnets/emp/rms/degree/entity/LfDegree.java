package com.montnets.emp.rms.degree.entity;

import java.sql.Timestamp;

/**
 * 档位表实体类对象
 * @author EMP
 * @date 2018-7-31
 */
public class LfDegree implements java.io.Serializable {
	private static final long serialVersionUID = -8364769593714598614L;
	/**
	 * 主键ID
	 */
	private Long id;
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
	/**
	 * 有效开始时间
	 */
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
	 * 记录创建时间
	 */
	private Timestamp createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
