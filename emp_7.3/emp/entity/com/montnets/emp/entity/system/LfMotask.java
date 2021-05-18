/**
 * 
 */
package com.montnets.emp.entity.system;

import java.sql.Timestamp;

/**
 * 上行短信任务
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-4 ����11:20:34
 * @description
 */

public class LfMotask implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -814440704722340956L;
	//标识ID
	private Long moId;
	//梦网短信平台自编的流水号
	private String ptMsgId;
	//该上行所属用户的UID
	private Long uids;
	//递送该条上行信息的通道的UID
	private Long orgUid;
	//该上行所属用户的企业的ID
	private Long ecid;
	// 该上行所属用户的账号
	private String spUser;
	// 上行通道
	private String spnumber;
	// 服务类型
	private String serviceId;
	// 服务代码
	private String spsc;
	//上行的发送状态(0：MO送往SP成功，1：网关接收MO成功)
	private Long sendStatus;
	// 消息编码格式
	private Long msgFmt;
	//
	private Long tpPid;
	//
	private Long tpUdhi;
	// 接收该上行的时间
	private Timestamp deliverTime;
	// 上行手机号
	private String phone;
	// 上行内容
	private String msgContent;
	//上行发至应用系统的时间
	private Timestamp doneTime;
	//操作员GUID
	private Long userGuid;
	//模块编号
	private String menuCode;
	//机构ID
	private Long depId;
	//任务ID
	private Long taskId;
	//业务编码
	private String busCode;
	//所属企业编码
	private String corpCode;
	//0移动，1联通，21电信
	private Integer spisuncm;
	//尾号
	private String subno;
	//指令，目前存放A_CMD_ROUTE中的ID字段
	private String moOrder;

	
	
	public String getSubno() {
		return subno;
	}

	public void setSubno(String subno) {
		this.subno = subno;
	}

	public Integer getSpisuncm() {
		return spisuncm;
	}

	public void setSpisuncm(Integer spisuncm) {
		this.spisuncm = spisuncm;
	}

	public Long getUserGuid() {
		return userGuid;
	}

	public void setUserGuid(Long userGuid) {
		this.userGuid = userGuid;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public LfMotask()
	{
		this.ptMsgId = "0";
		this.uids = new Long(0);
		this.orgUid = new Long(0);
		this.ecid = new Long(0);
		this.spUser =" ";
		this.spnumber=" ";
		this.serviceId =" ";
		this.sendStatus = new Long(2);
		this.msgFmt = new Long(15);
		this.tpPid = new Long(0);
		this.tpUdhi = new Long(0);
		this.deliverTime = new Timestamp(System.currentTimeMillis());
		this.phone=" ";
		this.msgContent = " ";
		this.doneTime = new Timestamp(System.currentTimeMillis());
		
	}

	public Long getMoId()
	{
		return moId;
	}

	public void setMoId(Long moId)
	{
		this.moId = moId;
	}

	public String getPtMsgId()
	{
		return ptMsgId;
	}

	public void setPtMsgId(String ptMsgId)
	{
		this.ptMsgId = ptMsgId;
	}

	public Long getUids()
	{
		return uids;
	}

	public void setUids(Long uids)
	{
		this.uids = uids;
	}

	public Long getOrgUid()
	{
		return orgUid;
	}

	public void setOrgUid(Long orgUid)
	{
		this.orgUid = orgUid;
	}

	public Long getEcid()
	{
		return ecid;
	}

	public void setEcid(Long ecid)
	{
		this.ecid = ecid;
	}

	public String getSpUser()
	{
		return spUser;
	}

	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}

	public String getSpnumber()
	{
		return spnumber;
	}

	public void setSpnumber(String spnumber)
	{
		this.spnumber = spnumber;
	}

	public String getServiceId()
	{
		return serviceId;
	}

	public void setServiceId(String serviceId)
	{
		this.serviceId = serviceId;
	}

	public String getSpsc()
	{
		return spsc;
	}

	public void setSpsc(String spsc)
	{
		this.spsc = spsc;
	}

	public Long getSendStatus()
	{
		return sendStatus;
	}

	public void setSendStatus(Long sendStatus)
	{
		this.sendStatus = sendStatus;
	}

	public Long getMsgFmt()
	{
		return msgFmt;
	}

	public void setMsgFmt(Long msgFmt)
	{
		this.msgFmt = msgFmt;
	}

	public Long getTpPid()
	{
		return tpPid;
	}

	public void setTpPid(Long tpPid)
	{
		this.tpPid = tpPid;
	}

	public Long getTpUdhi()
	{
		return tpUdhi;
	}

	public void setTpUdhi(Long tpUdhi)
	{
		this.tpUdhi = tpUdhi;
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

	public Timestamp getDoneTime()
	{
		return doneTime;
	}

	public void setDoneTime(Timestamp doneTime)
	{
		this.doneTime = doneTime;
	}

	public String getMoOrder()
	{
		return moOrder;
	}

	public void setMoOrder(String moOrder)
	{
		this.moOrder = moOrder;
	}

	@Override
	public String toString() {
		return "LfMotask [deliverTime=" + deliverTime + ", doneTime="
				+ doneTime + ", ecid=" + ecid + ", moId=" + moId
				+ ", msgContent=" + msgContent + ", msgFmt=" + msgFmt
				+ ", orgUid=" + orgUid + ", phone=" + phone + ", ptMsgId="
				+ ptMsgId + ", sendStatus=" + sendStatus + ", serviceId="
				+ serviceId + ", spUser=" + spUser + ", spnumber=" + spnumber
				+ ", spsc=" + spsc + ", tpPid=" + tpPid + ", tpUdhi=" + tpUdhi
				+ ", uids=" + uids + "]";
	}

}
