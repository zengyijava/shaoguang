package com.montnets.emp.rms.vo;

/**
 * @author Jason Huang
 * @date 2018年4月3日 上午9:39:40
 */

public class Param {
	private Integer type;// 参数类型,1:文本,2:图文,3:饼状图4:柱状图,5:折线图
	private Integer dynamicType; // 动态类型,2:数值动态,3:全动态
	private Integer colNum; // 总列数
	private Integer rowNum; // 总行数
	private String value;// 参数值
	private String color; // 颜色值
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getDynamicType() {
		return dynamicType;
	}
	public void setDynamicType(Integer dynamicType) {
		this.dynamicType = dynamicType;
	}
	public Integer getColNum() {
		return colNum;
	}
	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}
	public Integer getRowNum() {
		return rowNum;
	}
	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

}
