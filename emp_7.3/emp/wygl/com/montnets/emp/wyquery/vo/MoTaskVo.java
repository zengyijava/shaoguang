package com.montnets.emp.wyquery.vo;

import java.sql.Timestamp;

/**
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-1 上午11:14:24
 * @description
 */

public class MoTaskVo implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7766060299921996177L;

	private String spnumber;//通道号码

	private Timestamp deliverTime;//接收时间

	private String phone; //手机号码
	
	private String name; //员工姓名

	private String msgContent;//短信内容

	private String startSubmitTime;//

	private String endSubmitTime;//
	
    private String spgate;//通道
    
    private String spUser;//历史记录用发送账号
    //新增字段------------------------
    private Long pknumber;//长短信第一条
    
	private Long pktotal; //长短信总条数

	private Long msgFmt;//信息编码格式编码
	
	private String userId;//发送账号
	
	private Integer unicom;//运行商（0移动，1联通，21电信）
	//--------------------------------
	
	
	
	public String getSpUser() {
		return spUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getMsgFmt() {
		return msgFmt;
	}

	public void setMsgFmt(Long msgFmt) {
		this.msgFmt = msgFmt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getUnicom() {
		return unicom;
	}

	public void setUnicom(Integer unicom) {
		this.unicom = unicom;
	}

	public void setSpUser(String spUser) {
		this.spUser = spUser;
	}

	public String getSpgate() {
		return spgate;
	}

	public void setSpgate(String spgate) {
		this.spgate = spgate;
	}

	public String getSpnumber()
	{
		return spnumber;
	}

	public void setSpnumber(String spnumber)
	{
		this.spnumber = spnumber;
	}

	public Timestamp getDeliverTime()
	{
		return deliverTime;
	}

	public void setDeliverTime(Timestamp deliverTime)
	{
		this.deliverTime = deliverTime;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getMsgContent()
	{
		return msgContent;
	}

	public void setMsgContent(String msgContent)
	{
		this.msgContent = msgContent;
	}

	public String getStartSubmitTime()
	{
		return startSubmitTime;
	}

	public void setStartSubmitTime(String startSubmitTime)
	{
		this.startSubmitTime = startSubmitTime;
	}

	public String getEndSubmitTime()
	{
		return endSubmitTime;
	}

	public void setEndSubmitTime(String endSubmitTime)
	{
		this.endSubmitTime = endSubmitTime;
	}
}
