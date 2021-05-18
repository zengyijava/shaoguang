package com.montnets.emp.tempflow.vo;

import java.sql.Timestamp;

/**
 * 
 * @project montnets_entity
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-16 上午11:24:00
 * @description
 */
public class LfFlowRecordTemplateVo implements java.io.Serializable
{
	/**
	 * 模板审批流vo
	 */
	private static final long serialVersionUID = 580242856773191216L;
    //主键
	private Long frId;

	private Long mtId;

	private Long FId;

	private Timestamp RTime;
    //审批级次
	private Integer RLevel;
    //审批总级次
	private Integer RLevelAmount;
    //审批意见
	private String RContent;
    //审批状态
	private Integer RState;

	private String comments;

	private String preReviName;

	private String reviName;
    //用户id
	private Long userId;
	//用户名称
	private String name;
	//用户名称
	private String userName;
    //机构名称
	private String depName;

	private String tmName;

	private Long dsflag;
	
	private String startAddTime;

	private Timestamp addtime;

	private String endAddTime; 
	//模板内容
	private String tmMsg;
    //模板状态
	private Long tmState;
	//模板参数个数
	private Integer paramcnt;

	private Integer reviewType;	
	
	public Integer getParamcnt() {
		return paramcnt;
	}

	public void setParamcnt(Integer paramcnt) {
		this.paramcnt = paramcnt;
	}

	public String getStartAddTime() {
		return startAddTime;
	}

	public void setStartAddTime(String startAddTime) {
		this.startAddTime = startAddTime;
	}

	public String getEndAddTime() {
		return endAddTime;
	}

	public void setEndAddTime(String endAddTime) {
		this.endAddTime = endAddTime;
	}

	public Long getFrId()
	{
		return frId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Timestamp getAddtime()
	{
		return addtime;
	}

	public void setAddtime(Timestamp addtime)
	{
		this.addtime = addtime;
	}

	public String getTmMsg()
	{
		return tmMsg;
	}

	public void setTmMsg(String tmMsg)
	{
		this.tmMsg = tmMsg;
	}

	public Integer getReviewType()
	{
		return reviewType;
	}

	public void setReviewType(Integer reviewType)
	{
		this.reviewType = reviewType;
	}

	public String getTmName()
	{
		return tmName;
	}

	public void setTmName(String tmName)
	{
		this.tmName = tmName;
	}

	public Long getDsflag()
	{
		return dsflag;
	}

	public void setDsflag(Long dsflag)
	{
		this.dsflag = dsflag;
	}

	public Long getTmState()
	{
		return tmState;
	}

	public void setTmState(Long tmState)
	{
		this.tmState = tmState;
	}
}
