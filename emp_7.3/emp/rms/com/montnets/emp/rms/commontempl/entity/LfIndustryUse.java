package com.montnets.emp.rms.commontempl.entity;

import java.sql.Timestamp;

/**
 * @ClassName: LfIndustryManage
 * @Description: 行业-用途管理表
 * @author xuty
 * @date 2018-3-20 上午10:44:27
 * 
 */
public class LfIndustryUse {
	// 自增ID,用于与LF_TEMPLATE
	private Long id;
	private String name;
	private Long operator;
	private Integer type;
	private Timestamp createtm;
	private Timestamp updatetm;
	private Integer tmpType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public void setCreatetm(Timestamp createtm) {
		this.createtm = createtm;
	}
	public Timestamp getCreatetm() {
		return createtm;
	}
	public Timestamp getUpdatetm() {
		return updatetm;
	}

	public void setUpdatetm(Timestamp updatetm) {
		this.updatetm = updatetm;
	}

	public Integer getTmpType() {
		return tmpType;
	}

	public void setTmpType(Integer tmpType) {
		this.tmpType = tmpType;
	}
}
