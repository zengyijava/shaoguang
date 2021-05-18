package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

/**
 * 前端参数类
 * @author moll
 *
 */
public class TempDataParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8961614436843712076L;
	//参数名字
	private String name;
	//参数类型
	private Integer type;
	//参数长度约束 0可变 1固定
	private Integer lengthRestrict;
	//最大长度
	private Integer maxLength;
	//最小长度
	private Integer minLength;
	//固定长度
	private Integer fixLength;

	//参数是否含有长度限制
	private Integer hasLength;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLengthRestrict() {
		return lengthRestrict;
	}

	public void setLengthRestrict(Integer lengthRestrict) {
		this.lengthRestrict = lengthRestrict;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Integer getFixLength() {
		return fixLength;
	}

	public void setFixLength(Integer fixLength) {
		this.fixLength = fixLength;
	}

	public Integer getHasLength() {
		return hasLength;
	}

	public void setHasLength(Integer hasLength) {
		this.hasLength = hasLength;
	}

	@Override
	public String toString() {
		return "TempDataParam{" +
				"name='" + name + '\'' +
				", type=" + type +
				", lengthRestrict=" + lengthRestrict +
				", maxLength=" + maxLength +
				", minLength=" + minLength +
				", fixLength=" + fixLength +
				", hasLength=" + hasLength +
				'}';
	}
}
