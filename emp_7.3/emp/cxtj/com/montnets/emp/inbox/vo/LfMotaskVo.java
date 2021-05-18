package com.montnets.emp.inbox.vo;

import java.sql.Timestamp;

public class LfMotaskVo {
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
	//用户userid
	private Long userId;
    //用户名称
	private String name;
	//用户名称
	private String userName;
	//操作员状态
	private Integer userState;
	//机构id
	private Long sysdepId;
	
	private String employeeName;
	
	
	

	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Long getSysdepId()
	{
		return sysdepId;
	}
	public void setSysdepId(Long sysdepId)
	{
		this.sysdepId = sysdepId;
	}
	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getUserName()
	{
		return userName;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	

	public Integer getUserState() {
		return userState;
	}
	public void setUserState(Integer userState) {
		this.userState = userState;
	}	
	
	
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

}
