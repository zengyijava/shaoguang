package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.util.List;
/***
 * Rms的报表类
 * @author dell
 *
 */
public class TempRmsChart implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5420164672068532329L;
	//图形类型 （1：饼状图，2：柱状图，3：折线图 默认是1）
	private String chartType;
	//图形标题
	private String chartTitle;
	//数据类型（1：静态，2：数值动态,3：全动态，默认是1）
	private String ptType;
	//图片路径
	private String pictureUrl;
	//图片大小
	private String pictureSize;
	//饼状图第二列数值
	private String barRowName;
	//饼状图、柱状图、折线图列名
	private String barColName;
	//柱状图折线图数值(以行为单位，用“,”隔开，换行则用“@”隔开)
	private String barValue;
	//柱状图折线图表格所有数据，用于回显(以行为单位，用“,”隔开，换行则用“@”隔开)
	private String barTableVal;
	//动态参数的值
	private String parmValue;
	//行数
	private String rowNum;
	//列数
	private String colNum;
	//图表颜色值
	private String color;
	
	private String text;

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public String getPtType() {
		return ptType;
	}

	public void setPtType(String ptType) {
		this.ptType = ptType;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getPictureSize() {
		return pictureSize;
	}

	public void setPictureSize(String pictureSize) {
		this.pictureSize = pictureSize;
	}

	public String getBarRowName() {
		return barRowName;
	}

	public void setBarRowName(String barRowName) {
		this.barRowName = barRowName;
	}

	public String getBarColName() {
		return barColName;
	}

	public void setBarColName(String barColName) {
		this.barColName = barColName;
	}

	public String getBarValue() {
		return barValue;
	}

	public void setBarValue(String barValue) {
		this.barValue = barValue;
	}

	public String getBarTableVal() {
		return barTableVal;
	}

	public void setBarTableVal(String barTableVal) {
		this.barTableVal = barTableVal;
	}

	public String getParmValue() {
		return parmValue;
	}

	public void setParmValue(String parmValue) {
		this.parmValue = parmValue;
	}

	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}

	public String getColNum() {
		return colNum;
	}

	public void setColNum(String colNum) {
		this.colNum = colNum;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}	
