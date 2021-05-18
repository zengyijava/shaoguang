package com.montnets.emp.engine.vo;

import java.sql.Timestamp;

public class LfMttaskVo implements java.io.Serializable
{
	/**
	 * 发送任务vo
	 */
	private static final long serialVersionUID = 8034040440056368626L;
    //主键
	private Long mtId;
    //用户id
	private Long userId;
    //标题
	private String title;
    //短信类型
	private Integer msgType;
    //提交时间
	private Timestamp submitTime;

	private Integer subState;

	private Integer reState;

	private Integer sendstate;

	private Integer sendLevel;

	private Integer bmtType;
    //发送账号
	private String spUser;
    //发送对象
	private String mobileUrl;
    //提交总数
	private String subCount;
	//提交总数
	private String effCount;
    //成功总数
	private String sucCount;
    //失败总数
	private String faiCount;
    //发送内容
	private String msg;
	//发送账号密码
	private String spPwd;
	
	private String comments;
	//提交总数
	private String icount;	
    //用户名称
	private String name;
	//用户名称
	private String userName;
    //机构名称
	private String depName;
	//机构id
	private Long depId;
	
	private String staffName;
	
	private String startSubmitTime;

	private String endSubmitTime;
	
	private String startSendTime;

	private String endSendTime;
	//短信定时发送时间
	private Timestamp timerTime;
	//业务编码 
	private String busCode;
	//业务名称
	private String busName;
	//是否定时发送  1-是 0-否
	private Integer timerStatus;
	
	private Integer msType;
	
	private String reStates;
	//用户ids
	private String userIds;
	//机构ids
	private String depIds;
	//时间查询用,查询条件为sendstete>这个值
	private String overSendstate;
	//错误编码
	private String errorCodes;
	//in查询用短信
	private String msTypes;
	//是否回复
	private Integer isReply;
	//是否重发  1-已重发 0-未重发
	private Integer isRetry ;
	
	private Long rFail2;
	//提交总数
	private String icount2;
	//操作员状态
	private Integer userState;
	
	private String corpCode;
	
	//任务状态，群发任务查询用
	private String taskState;
	
	private Long serId;
	private String serName;
	
	//任务ID
	private Long taskId;
	
	//mtId密文
	private String mtIdCipher;
	
	/**
	 * 
	 * @description 获取mtId密文
	 * @return mtId密文
	 */
	public String getMtIdCipher()
	{
		return mtIdCipher;
	}

	public void setMtIdCipher(String mtIdCipher)
	{
		this.mtIdCipher = mtIdCipher;
	}

	public String getIcount2() {
		return icount2;
	}

	public void setIcount2(String icount2) {
		this.icount2 = icount2;
	}

	public Long getRFail2() {
		return rFail2;
	}

	public void setRFail2(Long rFail2) {
		this.rFail2 = rFail2;
	}

	public Integer getIsRetry() {
		return isRetry;
	}

	public void setIsRetry(Integer isRetry) {
		this.isRetry = isRetry;
	}
	
	public Integer getIsReply() {
		return isReply;
	}

	public void setIsReply(Integer isReply) {
		this.isReply = isReply;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

	public String getSubNo() {
		return subNo;
	}

	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}

	private String spNumber;//通道号
	
	private String subNo;
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

	public Long getMtId()
	{
		return mtId;
	}

	public void setMtId(Long mtId)
	{
		this.mtId = mtId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Integer getMsgType()
	{
		return msgType;
	}

	public void setMsgType(Integer msgType)
	{
		this.msgType = msgType;
	}

	public Timestamp getSubmitTime()
	{
		return submitTime;
	}

	public void setSubmitTime(Timestamp submitTime)
	{
		this.submitTime = submitTime;
	}

	public Integer getSubState()
	{
		return subState;
	}

	public void setSubState(Integer subState)
	{
		this.subState = subState;
	}

	public Integer getReState()
	{
		return reState;
	}

	public void setReState(Integer reState)
	{
		this.reState = reState;
	}

	public Integer getSendstate()
	{
		return sendstate;
	}

	public void setSendstate(Integer sendstate)
	{
		this.sendstate = sendstate;
	}

	public Integer getSendLevel()
	{
		return sendLevel;
	}

	public void setSendLevel(Integer sendLevel)
	{
		this.sendLevel = sendLevel;
	}

	public Integer getBmtType()
	{
		return bmtType;
	}

	public void setBmtType(Integer bmtType)
	{
		this.bmtType = bmtType;
	}

	public String getSpUser()
	{
		return spUser;
	}

	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}

	public String getStaffName()
	{
		return staffName;
	}

	public void setStaffName(String staffName)
	{
		this.staffName = staffName;
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

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getMobileUrl()
	{
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl)
	{
		this.mobileUrl = mobileUrl;
	}

	public String getSubCount()
	{
		return subCount;
	}

	public void setSubCount(String subCount)
	{
		this.subCount = subCount;
	}

	public String getEffCount()
	{
		return effCount;
	}

	public void setEffCount(String effCount)
	{
		this.effCount = effCount;
	}

	public String getSucCount()
	{
		return sucCount;
	}

	public void setSucCount(String sucCount)
	{
		this.sucCount = sucCount;
	}

	public String getFaiCount()
	{
		return faiCount;
	}

	public void setFaiCount(String faiCount)
	{
		this.faiCount = faiCount;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public String getSpPwd()
	{
		return spPwd;
	}

	public void setSpPwd(String spPwd)
	{
		this.spPwd = spPwd;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getIcount()
	{
		return icount;
	}

	public void setIcount(String icount)
	{
		this.icount = icount;
	}

	public Long getDepId()
	{
		return depId;
	}

	public void setDepId(Long depId)
	{
		this.depId = depId;
	}

	public Timestamp getTimerTime() {
		return timerTime;
	}

	public void setTimerTime(Timestamp timerTime) {
		this.timerTime = timerTime;
	}

	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public Integer getTimerStatus() {
		return timerStatus;
	}

	public void setTimerStatus(Integer timerStatus) {
		this.timerStatus = timerStatus;
	}

	public String getReStates()
	{
		return reStates;
	}

	public void setReStates(String reStates)
	{
		this.reStates = reStates;
	}

	public String getUserIds()
	{
		return userIds;
	}

	public void setUserIds(String userIds)
	{
		this.userIds = userIds;
	}

	public String getDepIds()
	{
		return depIds;
	}

	public void setDepIds(String depIds)
	{
		this.depIds = depIds;
	}

	public Integer getMsType()
	{
		return msType;
	}

	public void setMsType(Integer msType)
	{
		this.msType = msType;
	}

	public String getOverSendstate() {
		return overSendstate;
	}

	public void setOverSendstate(String overSendstate) {
		this.overSendstate = overSendstate;
	}

	public String getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(String errorCodes) {
		this.errorCodes = errorCodes;
	}

	public String getStartSendTime() {
		return startSendTime;
	}

	public void setStartSendTime(String startSendTime) {
		this.startSendTime = startSendTime;
	}

	public String getEndSendTime() {
		return endSendTime;
	}

	public void setEndSendTime(String endSendTime) {
		this.endSendTime = endSendTime;
	}

	public String getMsTypes() {
		return msTypes;
	}

	public void setMsTypes(String msTypes) {
		this.msTypes = msTypes;
	}

	public Integer getUserState() {
		return userState;
	}
	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public Long getTaskId()
	{
		return taskId;
	}

	public void setTaskId(Long taskId)
	{
		this.taskId = taskId;
	}

	public Long getSerId()
	{
		return serId;
	}

	public void setSerId(Long serId)
	{
		this.serId = serId;
	}

	public String getSerName()
	{
		return serName;
	}

	public void setSerName(String serName)
	{
		this.serName = serName;
	}
	
	
}
