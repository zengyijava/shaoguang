package com.montnets.emp.rms.wbs.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * webservices响应类型
 * @ClassName RmsResponse
 * @Description TODO
 * @author zhouxiangxian 203492752@qq.com
 * @date 2018年1月9日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SiiResponse", propOrder = { "bizCode", "actionCode",
		"resultCode", "resultMsg", "encryFlag", "svcCont" })
public class RmsResponse {

	@XmlElement(name = "BizCode")
	private String bizCode;
	@XmlElement(name = "ActionCode")
	private int actionCode;
	@XmlElement(name = "ResultCode")
	private String resultCode;
	@XmlElement(name = "ResultMsg")
	private String resultMsg;
	@XmlElement(name = "EncryFlag")
	private int encryFlag;
	@XmlElement(name = "SvcCont")
	private String svcCont;
	public String getBizCode() {
		return bizCode;
	}
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}
	public int getActionCode() {
		return actionCode;
	}
	public void setActionCode(int actionCode) {
		this.actionCode = actionCode;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public int getEncryFlag() {
		return encryFlag;
	}
	public void setEncryFlag(int encryFlag) {
		this.encryFlag = encryFlag;
	}
	public String getSvcCont() {
		return svcCont;
	}
	public void setSvcCont(String svcCont) {
		this.svcCont = svcCont;
	}
	
	
	
	
}
