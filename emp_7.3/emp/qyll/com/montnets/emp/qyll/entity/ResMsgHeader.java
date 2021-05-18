package com.montnets.emp.qyll.entity;

public class ResMsgHeader {
	//消息标志
	private String BCode;
	//动作码
	private String Ack;
	//流水号
	private String SqId;
	//返回状态
	private String RtState;
	//返回错误编码
	private String RtErrCode;
	//返回结果消息描述
	private String RtMsg;
	//应答报文体内容
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
	public String getRtState() {
		return RtState;
	}
	public void setRtState(String rtState) {
		RtState = rtState;
	}
	public String getRtErrCode() {
		return RtErrCode;
	}
	public void setRtErrCode(String rtErrCode) {
		RtErrCode = rtErrCode;
	}
	public String getRtMsg() {
		return RtMsg;
	}
	public void setRtMsg(String rtMsg) {
		RtMsg = rtMsg;
	}
	public String getCnxt() {
		return Cnxt;
	}
	public void setCnxt(String cnxt) {
		Cnxt = cnxt;
	}
	@Override
	public String toString() {
		return "ResMsgHeader [BCode=" + BCode + ", Ack=" + Ack + ", SqId="
				+ SqId + ", RtState=" + RtState + ", RtErrCode=" + RtErrCode
				+ ", RtMsg=" + RtMsg + ", Cnxt=" + Cnxt + "]";
	}

}
