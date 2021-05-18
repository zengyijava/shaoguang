package com.montnets.emp.entity.selfparam;

public class LfWgParmDefinition implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8486489790094338107L;
	//参数ID(自增ID)
	private Long pid;
	//Param2，Param3，Param4
	private String param;
	//分段数
	private Integer paramSubNum;
	//分段符
	private String paramSubSign;
	//参数名称
	private String paramSubName;
	//备注
	private String memo;
	//企业编码
	private String corpCode;
	
	//参数ID(自增ID)
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
	
	//分段数
	public Integer getParamSubNum() {
		return paramSubNum;
	}
	public void setParamSubNum(Integer paramSubNum) {
		this.paramSubNum = paramSubNum;
	}
	
	//分段符
	public String getParamSubSign() {
		return paramSubSign;
	}
	public void setParamSubSign(String paramSubSign) {
		this.paramSubSign = paramSubSign;
	}
	
	//参数名称
	public String getParamSubName() {
		return paramSubName;
	}
	public void setParamSubName(String paramSubName) {
		this.paramSubName = paramSubName;
	}
	
	//企业编码
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	//备注
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
		
}
