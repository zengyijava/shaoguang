package com.montnets.emp.entity.gateway;

public class AProInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8486489790094338107L;

	private String serialNum;
	private Integer proType;
	private Integer proStatus;
	private String statusInfo;
	private Integer validDays;
	private String corpName;
	private Integer sendSpeed; 
	
	public Integer getSendSpeed()
	{
		return sendSpeed;
	}
	public void setSendSpeed(Integer sendspeed)
	{
		this.sendSpeed = sendspeed;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public Integer getProType() {
		return proType;
	}
	public void setProType(Integer proType) {
		this.proType = proType;
	}
	public Integer getProStatus() {
		return proStatus;
	}
	public void setProStatus(Integer proStatus) {
		this.proStatus = proStatus;
	}
	public String getStatusInfo() {
		return statusInfo;
	}
	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}
	public Integer getValidDays() {
		return validDays;
	}
	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	
	public AProInfo()
	{
		this.validDays = 0;
	}
	
}
