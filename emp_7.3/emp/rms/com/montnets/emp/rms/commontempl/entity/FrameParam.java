package com.montnets.emp.rms.commontempl.entity;

import java.io.Serializable;

/**
* @ClassName: FrameParam 
* @Description: 每帧参数实体类
* @author xuty  
* @date 2018-4-10 上午11:14:33 
*  
*/
public class FrameParam implements Serializable {
	private static final long serialVersionUID = 4038120491606124448L;
	//帧数索引
	private Integer frameIndex;
	
	// 类型 0-图文，1-报表
	private Integer type ;

	// 行 ：图文 类型 默认为0
	private Integer rowNum = 0;
	
	// 列 ：图文 类型 默认为0
	private Integer colNum = 0;
	
	//一帧中所有参数
	private String paramValue;
	
	//一帧参数个数
	private Integer paramCount;
	
	//报表参数类型  2-数值动态，3-全值动态
	private Integer dynamicType;
	
	//报表类型  3：饼图，4：柱状图，5：折线图，6：工资条，7：表格
	private Integer charType;
	
	//报表元素颜色
	private String color;
	
	public Integer getFrameIndex() {
		return frameIndex;
	}

	public void setFrameIndex(Integer frameIndex) {
		this.frameIndex = frameIndex;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getColNum() {
		return colNum;
	}

	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public Integer getParamCount() {
		return paramCount;
	}

	public void setParamCount(Integer paramCount) {
		this.paramCount = paramCount;
	}

	public Integer getDynamicType() {
		return dynamicType;
	}

	public void setDynamicType(Integer dynamicType) {
		this.dynamicType = dynamicType;
	}
	
	public void setCharType(Integer charType) {
		this.charType = charType;
	}
	public Integer getCharType() {
		return charType;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getColor() {
		return color;
	}
	
}
