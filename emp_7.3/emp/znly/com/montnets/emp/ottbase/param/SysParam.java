package com.montnets.emp.ottbase.param;

public class SysParam implements java.io.Serializable{

	
	private static final long serialVersionUID = -8881171543640265999L;
	//自增ID
	private Long paramID;
	//参数组
	private String paramGroup;
	//参数项
	private String paramItem;
	//参数值
	private String paramValue;
	//含义说明
	private String memo;
	
	public SysParam(){}

	
	//自增ID
	public Long getParamID() {
		return paramID;
	}

	public void setParamID(Long paramID) {
		this.paramID = paramID;
	}

	//参数组
	public String getParamGroup() {
		return paramGroup;
	}

	public void setParamGroup(String paramGroup) {
		this.paramGroup = paramGroup;
	}

	//参数项
	public String getParamItem() {
		return paramItem;
	}

	public void setParamItem(String paramItem) {
		this.paramItem = paramItem;
	}

	//参数值
	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
	//含义说明
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
 

	
}
