package com.montnets.emp.charging.vo;

import java.sql.Timestamp;

public class LfDepRechargeLogVo implements java.io.Serializable{

	private static final long serialVersionUID = 5876047657056874306L;
	//主键
	private Long logId;
	//充值类型（100为总公司充值/回收；101机构为机构充值/回收； 102机构为操作员充值/回收）
	private Integer optType;		
	// 充值源id   上级机构
	private Long srcTargetId;			
	//充值目的id   充值机构
	private Long dstTargetId;
	//充值源名称/描述      充值目的名称/描述
	private String optInfo;     
	//信息类型：1为短信，2为彩信
	private Integer msgType;		
	//数量：充值/回收数量
	private Long count;		
	//操作员id：执行充值/回收的操作员Guid
	private Long optId;			
	//操作时间：执行充值/回收的时间
	private Timestamp optDate;	
	//执行结果（0成功，其它失败）
	private Integer result;			
	//备注
	private String memo;			
	//上机机构名称
	private String srcName;		
	//充值机构名称
	private String dstName;		
	//操作员状态
	private Integer userState;			
	//操作员名称
	private String userName;			
	//开始时间
	private String beginTime;
	//结束时间
	private String endTime;
	
	public LfDepRechargeLogVo(){}
	public String getOptInfo() {
		return optInfo;
	}
	public void setOptInfo(String optInfo) {
		this.optInfo = optInfo;
	}
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public Integer getOptType() {
		return optType;
	}
	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Long getOptId() {
		return optId;
	}
	public void setOptId(Long optId) {
		this.optId = optId;
	}
	public Timestamp getOptDate() {
		return optDate;
	}
	public void setOptDate(Timestamp optDate) {
		this.optDate = optDate;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getSrcName() {
		return srcName;
	}
	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}
	public String getDstName() {
		return dstName;
	}
	public void setDstName(String dstName) {
		this.dstName = dstName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getSrcTargetId() {
		return srcTargetId;
	}

	public void setSrcTargetId(Long srcTargetId) {
		this.srcTargetId = srcTargetId;
	}

	public Long getDstTargetId() {
		return dstTargetId;
	}

	public void setDstTargetId(Long dstTargetId) {
		this.dstTargetId = dstTargetId;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getUserState() {
		return userState;
	}
	public void setUserState(Integer userState) {
		this.userState = userState;
	}
	
}
