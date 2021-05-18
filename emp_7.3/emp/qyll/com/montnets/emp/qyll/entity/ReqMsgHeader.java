package com.montnets.emp.qyll.entity;

/**
 * 公共的请求报文头参数
 */
public class ReqMsgHeader {
	//消息标志
	private String BCode;
	//动作码
	private String Ack;
	//流水号
	private String SqId;
	//Ec编码
	private String ECID;
	//报文体
	private String Cnxt;
	public String getBCode() {
		return BCode;
	}
	public void setBCode(String bCode) {
		BCode = bCode;
	}
	public String getAck() {
		return Ack;
	}
	public void setAck(String ack) {
		Ack = ack;
	}
	public String getSqId() {
		return SqId;
	}
	public void setSqId(String sqId) {
		SqId = sqId;
	}
	public String getECID() {
		return ECID;
	}
	public void setECID(String eCID) {
		ECID = eCID;
	}
	public String getCnxt() {
		return Cnxt;
	}
	public void setCnxt(String cnxt) {
		Cnxt = cnxt;
	}
	@Override
	public String toString() {
		return "MsgHeader [BCode=" + BCode + ", Ack=" + Ack + ", SqId=" + SqId
				+ ", ECID=" + ECID + ", Cnxt=" + Cnxt + "]";
	}
	
}
