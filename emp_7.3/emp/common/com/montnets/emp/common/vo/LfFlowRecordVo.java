package com.montnets.emp.common.vo;

import java.sql.Timestamp;

public class LfFlowRecordVo implements java.io.Serializable
{
	/**
	 * 审批流程vo
	 */
	private static final long serialVersionUID = -2533192656814914597L;
    //主键
	private Long frId;
    //任务mtid
	private Long mtId;

	private Long FId;

	private Timestamp RTime;
    //审批级次
	private Integer RLevel;
    //审批总级次
	private Integer RLevelAmount;

	private String RContent;
    //审批状态
	private Integer RState;

	private String comments;

	private String preReviName;

	private String reviName;
    //用户名称
	private String name;
	//用户名称
	private String userName;
    //机构名称
	private String depName;
    //任务标题
	private String title;
    //任务名称
	private String taskName;

	private Integer bmtType;
    //发送账号
	private String spUser;

	private String staffName;
    //提交时间
	private Timestamp submitTime;
    //发送对象 
	private String mobileUrl;
    //提交总数
	private Integer effCount;
    //短信内容
	private String msg;

	private Integer msgType;
	
	private String startSubmitTime;

	private String endSubmitTime;

	private Integer reviewType;	
	//短信定时发送时间
    private Timestamp timerTime;    
  //是否定时发送  1-是 0-否
    private Integer timerStatus;
    //用户状态
    private Integer userState;
    
	//模板路径
    private String tmplPath;
	
	  public String getTmplPath() {
		return tmplPath;
	}

	public void setTmplPath(String tmplPath) {
		this.tmplPath = tmplPath;
	}
    
	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}    
	
	public Timestamp getTimerTime() {
		return timerTime;
	}

	public void setTimerTime(Timestamp timerTime) {
		this.timerTime = timerTime;
	}

	public Integer getTimerStatus() {
		return timerStatus;
	}

	public void setTimerStatus(Integer timerStatus) {
		this.timerStatus = timerStatus;
	}

	public Long getFrId()
	{
		return frId;
	}

	public void setFrId(Long frId)
	{
		this.frId = frId;
	}

	public Long getMtId()
	{
		return mtId;
	}

	public void setMtId(Long mtId)
	{
		this.mtId = mtId;
	}

	public Long getFId()
	{
		return FId;
	}

	public void setFId(Long FId)
	{
		this.FId = FId;
	}

	public Timestamp getRTime()
	{
		return RTime;
	}

	public void setRTime(Timestamp rTime)
	{
		this.RTime = rTime;
	}

	public Integer getRLevel()
	{
		return RLevel;
	}

	public void setRLevel(Integer rLevel)
	{
		RLevel = rLevel;
	}

	public Integer getRLevelAmount()
	{
		return RLevelAmount;
	}

	public void setRLevelAmount(Integer rLevelAmount)
	{
		RLevelAmount = rLevelAmount;
	}

	public String getRContent()
	{
		return RContent;
	}

	public void setRContent(String rContent)
	{
		RContent = rContent;
	}

	public Integer getRState()
	{
		return RState;
	}

	public void setRState(Integer rState)
	{
		RState = rState;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public String getPreReviName()
	{
		return preReviName;
	}

	public void setPreReviName(String preReviName)
	{
		this.preReviName = preReviName;
	}

	public String getReviName()
	{
		return reviName;
	}

	public void setReviName(String reviName)
	{
		this.reviName = reviName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
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

	public Timestamp getSubmitTime()
	{
		return submitTime;
	}

	public void setSubmitTime(Timestamp submitTime)
	{
		this.submitTime = submitTime;
	}

	public String getMobileUrl()
	{
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl)
	{
		this.mobileUrl = mobileUrl;
	}

	public Integer getEffCount()
	{
		return effCount;
	}

	public void setEffCount(Integer effCount)
	{
		this.effCount = effCount;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public Integer getReviewType()
	{
		return reviewType;
	}

	public void setReviewType(Integer reviewType)
	{
		this.reviewType = reviewType;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public Integer getMsgType()
	{
		return msgType;
	}

	public void setMsgType(Integer msgType)
	{
		this.msgType = msgType;
	}
}
