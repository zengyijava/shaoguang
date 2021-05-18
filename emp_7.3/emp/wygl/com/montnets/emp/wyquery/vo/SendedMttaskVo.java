package com.montnets.emp.wyquery.vo;

import java.sql.Timestamp;

public class SendedMttaskVo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	//运营商
	private Long unicom;
	//手机号
	private String phone;
	//发送内容
	private String message;
	//状态报告
	private String errorcode;
	//分条数
	private Long pknumber;
	//分条总数
	private Long pktotal;
	//发送时间
	private Timestamp sendtime;
	//接收时间
	private Timestamp recvtime;
	//通道号
	private String spgate;
	//通道名称
	private String gateName;
	
	
	public String getSpgate()
	{
		return spgate;
	}
	public void setSpgate(String spgate)
	{
		this.spgate = spgate;
	}
	public Timestamp getSendtime()
	{
		return sendtime;
	}
	public void setSendtime(Timestamp sendtime)
	{
		this.sendtime = sendtime;
	}
	public Timestamp getRecvtime()
	{
		return recvtime;
	}
	public void setRecvtime(Timestamp recvtime)
	{
		this.recvtime = recvtime;
	}
	public String getGateName()
	{
		return gateName;
	}
	public void setGateName(String gateName)
	{
		this.gateName = gateName;
	}
	public Long getUnicom()
	{
		return unicom;
	}
	public void setUnicom(Long unicom)
	{
		this.unicom = unicom;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public Long getPknumber() {
		return pknumber;
	}

	public void setPknumber(Long pknumber) {
		this.pknumber = pknumber;
	}

	public Long getPktotal() {
		return pktotal;
	}

	public void setPktotal(Long pktotal) {
		this.pktotal = pktotal;
	}
}
