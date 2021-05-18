package com.montnets.emp.entity.selfparam;

public class LfWgParamConfig implements java.io.Serializable{

	/**
	 * 动态参数配置
	 */
	private static final long serialVersionUID = -8881171543640265999L;
	//自增ID
	private Long pid;
	//Param2，Param3，Param4
	private String param;
	//参数值
	private String paramValue;
	//该动态参数所代表的含义
	private String paramName;
	//所属企业编码
	private String corpCode;
	//含义说明
	private String memo;
	//分段数
	private Integer paramSubNum;
	
	
	public LfWgParamConfig(){}
 
	//分段数
	public Integer getParamSubNum() {
		return paramSubNum;
	}

	public void setParamSubNum(Integer paramSubNum) {
		this.paramSubNum = paramSubNum;
	}

	//自增ID
	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	//Param2，Param3，Param4
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	//参数值
	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	//该动态参数所代表的含义
	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	//所属企业编码
	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	
	//含义说明
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	
}
