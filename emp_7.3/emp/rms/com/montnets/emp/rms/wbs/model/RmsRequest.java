package com.montnets.emp.rms.wbs.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * 
 * @ClassName RmsReq     
 * @Description TODO   请求报文body,用户封装body部分的报文数据
 * @author zhouxiangxian 203492752@qq.com
 * @date 2018年1月13日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SiiRequest", propOrder = { "bizCode", "actionCode",
		"encryFlag", "svcCont" })
public class RmsRequest {
    
	@XmlElement(name = "BizCode")
	protected String bizCode;
	@XmlElement(name = "ActionCode")
	protected int actionCode;
	@XmlElement(name = "EncryFlag")
	protected int encryFlag;
	@XmlElement(name = "SvcCont")
	protected String svcCont;

	/**
	 * Gets the value of the bizCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBizCode() {
		return bizCode;
	}

	/**
	 * Sets the value of the bizCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBizCode(String value) {
		this.bizCode = value;
	}

	/**
	 * Gets the value of the actionCode property.
	 * 
	 */
	public int getActionCode() {
		return actionCode;
	}

	/**
	 * Sets the value of the actionCode property.
	 * 
	 */
	public void setActionCode(int value) {
		this.actionCode = value;
	}

	/**
	 * Gets the value of the encryFlag property.
	 * 
	 */
	public int getEncryFlag() {
		return encryFlag;
	}

	/**
	 * Sets the value of the encryFlag property.
	 * 
	 */
	public void setEncryFlag(int value) {
		this.encryFlag = value;
	}

	/**
	 * Gets the value of the svcCont property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSvcCont() {
		return svcCont;
	}

	/**
	 * Sets the value of the svcCont property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSvcCont(String value) {
		this.svcCont = value;
	}

}
