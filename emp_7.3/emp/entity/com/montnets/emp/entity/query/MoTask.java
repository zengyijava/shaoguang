package com.montnets.emp.entity.query;

import java.sql.Timestamp;

/**
 * TableMoTask对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:26:51
 * @description 
 */
public class MoTask implements java.io.Serializable {
 

	/**
	 * TableMoTask对应的实体类
	 */
	private static final long serialVersionUID = 4564045909502940798L;
    //主键
	private Long id;
	
	private String ptMsgId;
	//uid
	private Long uid;
	
	private Long orgUid;
	
	private Long ecid;
	//用户id
	private String userId;

	private String spnumber;

	private String serviceId;
    //发送状态
	private Long sendStatus;

	private Long msgFmt;
	
	private Long tpPid;
	
	private Long tpUdhi;
	
	private Timestamp deliverTime;
    //手机号
	private String phone;
    //短信内容
	private String msgContent;

	private Timestamp doneTime;
	//运行商（0移动，1联通，21电信）
	private Long unicom;
	//分条
	private Long pknumber;
	//分条总数
	private Long pktotal;
	
	public MoTask() {}   

	public Long getUnicom() {
		return unicom;
	}

	public void setUnicom(Long unicom) {
		this.unicom = unicom;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPtMsgId() {
		return ptMsgId;
	}

	public void setPtMsgId(String ptMsgId) {
		this.ptMsgId = ptMsgId;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getOrgUid() {
		return orgUid;
	}

	public void setOrgUid(Long orgUid) {
		this.orgUid = orgUid;
	}

	public Long getEcid() {
		return ecid;
	}

	public void setEcid(Long ecid) {
		this.ecid = ecid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSpnumber() {
		return spnumber;
	}

	public void setSpnumber(String spnumber) {
		this.spnumber = spnumber;
	} 

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Long getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Long sendStatus) {
		this.sendStatus = sendStatus;
	}

	public Long getMsgFmt() {
		return msgFmt;
	}

	public void setMsgFmt(Long msgFmt) {
		this.msgFmt = msgFmt;
	}

	public Long getTpPid() {
		return tpPid;
	}

	public void setTpPid(Long tpPid) {
		this.tpPid = tpPid;
	}

	public Long getTpUdhi() {
		return tpUdhi;
	}

	public void setTpUdhi(Long tpUdhi) {
		this.tpUdhi = tpUdhi;
	}

	public Timestamp getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Timestamp deliverTime) {
		this.deliverTime = deliverTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Timestamp getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(Timestamp doneTime) {
		this.doneTime = doneTime;
	}

	public Long getPknumber()
	{
		return pknumber;
	}

	public void setPknumber(Long pknumber)
	{
		this.pknumber = pknumber;
	}

	public Long getPktotal()
	{
		return pktotal;
	}

	public void setPktotal(Long pktotal)
	{
		this.pktotal = pktotal;
	}    
}