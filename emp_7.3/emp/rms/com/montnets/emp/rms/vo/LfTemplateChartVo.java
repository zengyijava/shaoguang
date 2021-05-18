package com.montnets.emp.rms.vo;

public class LfTemplateChartVo {
	//图形类型 （1：饼状图，2：柱状图，3：折线图，4：工资条，5：表格，默认是1）
	private String  chartType;
	//图形标题
	private String  chartTitle;
	//数据类型（1：静态，2：数值动态,3：全动态，默认是1）
	private String  ptType;
	//饼状图颜色
	private String  color;
	//饼状图第二列数值
	private String  rowValue;
	//图片保存的位置
	private String  pictureUrl;
	//图片大小
	private  Long pictureSize;
	//柱状图折线图行名
	private String barRowName;
	//饼状图柱状图折线图列名
	private String barColName;
	//柱状图折线图数值(以行为单位，用“,”隔开，换行则用“@”隔开)
	private String barValue;
	//柱状图折线图表所有数值，用于回显(以行为单位，用“,”隔开，换行则用“@”隔开)
	private String barTableVal;
	//动态参数的值
	private String parmValue;
	//行数
	private String rowNum;
	//列数
	private String colNum;
	
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
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getRowValue() {
		return rowValue;
	}
	public void setRowValue(String rowValue) {
		this.rowValue = rowValue;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public Long getPictureSize() {
		return pictureSize;
	}
	public void setPictureSize(Long pictureSize) {
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
	
}
