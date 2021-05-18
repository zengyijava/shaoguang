package com.montnets.emp.entity.query;

import java.sql.Timestamp;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-3 下午03:22:16
 * @description
 */

public class MoTask01_12 implements java.io.Serializable{

	/**
	 * 上行记录历史表
	 */
	private static final long serialVersionUID = -9113224268269245105L;
    //主键
	private Long id;
    
	private Long uid;
    //用户id
	private String userId;

	private String spnumber;

	private String serviceId;
    //发送状态
	private Integer sendStatus;

	private Timestamp deliverTime;
    //手机号
	private String phone;
    //发送内容
	private String msgContent;

	private Long ecid;

	private Long orgUid;

	private Long ptmsgId;
	
	private Long tpPid;
	
	private Long tpUdhi;
	
	private Long msgFmt;
	//运行商（0移动，1联通，21电信）
	private Long unicom; 

	public MoTask01_12() {		
		this.deliverTime = new Timestamp(System.currentTimeMillis());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
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

	public Integer getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
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

	public Long getEcid() {
		return ecid;
	}

	public void setEcid(Long ecid) {
		this.ecid = ecid;
	}

	public Long getOrgUid() {
		return orgUid;
	}

	public void setOrgUid(Long orgUid) {
		this.orgUid = orgUid;
	}

	public Long getPtmsgId() {
		return ptmsgId;
	}

	public void setPtmsgId(Long ptmsgId) {
		this.ptmsgId = ptmsgId;
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

	public Long getMsgFmt() {
		return msgFmt;
	}

	public void setMsgFmt(Long msgFmt) {
		this.msgFmt = msgFmt;
	}

	public Long getUnicom()
	{
		return unicom;
	}

	public void setUnicom(Long unicom)
	{
		this.unicom = unicom;
	}	
}
