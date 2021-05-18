package com.montnets.emp.charging.vo;

import java.io.Serializable;

public class LfDepBalanceDefVo implements Serializable{
	
	//机构id
	private Long depId;
	//机构名称
	private String depName;
	//企业编码
	private String corpCode;
	//短信默认值
	private Integer smsCount;
	//彩信默认值	
	private Integer mmsCount;
	
	public Long getDepId() {
		return depId;
	}
	public void setDepId(Long depId) {
		this.depId = depId;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public Integer getSmsCount() {
		return smsCount;
	}
	public void setSmsCount(Integer smsCount) {
		this.smsCount = smsCount;
	}
	public Integer getMmsCount() {
		return mmsCount;
	}
	public void setMmsCount(Integer mmsCount) {
		this.mmsCount = mmsCount;
	}

}
