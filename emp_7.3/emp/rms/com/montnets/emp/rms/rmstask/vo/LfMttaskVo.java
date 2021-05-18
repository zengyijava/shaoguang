package com.montnets.emp.rms.rmstask.vo;

import java.sql.Timestamp;

/**
 * 群发任务、群发历史VO对象
 * @author Cheng
 * @date 2018-08-03 16:44:50
 */
public class LfMttaskVo implements java.io.Serializable {

	private static final long serialVersionUID = 8034040440056368626L;

	/**
	 * 加密MtId
	 */
	private String mtIdCipher;
	/**
	 * 自增Id
	 */
	private Long mtId;
	/**
	 * sp账号
	 */
	private String spUser;
	/**
	 * 操作员Id
	 */
	private String userId;
	/**
	 * 号码文件url
	 */
	private String mobileUrl;
	/**
	 * 发送主题
	 */
	private String title;
	/**
	 * 业务类型编码
	 */
	private String busCode;
	/**
	 * 业务类型名字
	 */
	private String busName;
	/**
	 * 模板Id（非自增）
	 */
	private Long tempId;
	/**
	 * 号码个数
	 */
	private Long effCount;
	/**
	 * 发送成功数
	 */
	private Long icount2;
	/**
	 * 提交总数
	 */
	private Long icount;
	/**
	 * 接受失败数
	 */
	private Long rfail2;
	/**
	 * 提交失败数
	 */
	private Long faiCount;
	/**
	 * 滞留数
	 */
	private Long delayNum;
	/**
	 * 具体的发送状态 提交成功、部分提交失败、...
	 */
	private String sendStatus;
	/**
	 * 发送时间
	 */
	private Timestamp timerTime;
	/**
	 * 提交时间
	 */
	private Timestamp submitTime;
	/**
	 * 发送类型
	 */
	private Integer taskType;
	/**
	 * 批次Id
	 */
	private Long taskId;
	/**
	 * 是否为定时任务
	 */
	private Integer timerStatus;
	/**
	 * 提交状态 1创建中，2提交，3撤销, 4冻结
	 */
	private Integer subState;
	/**
	 * 发送状态 0是未发送，1是已发送到网关,2发送失败,3网关处理完成,4发送中,5超时未发送
	 */
	private Integer sendState;
	/**
	 * 是否重发 1-已重发 0-未重发
	 */
	private Integer isRetry;
	/**
	 * rms文件相对路径
	 */
	private String tmplPath;
	/**
	 * 操作员名字
	 */
	private String name;
	/**
	 * 操作员状态
	 */
	private Integer userState;
	/**
	 * 机构名字
	 */
	private String depName;
	/**
	 * 控制表记录读取到号码文件哪一行
	 */
	private Long currentCount;
	/**
	 * 档位
	 */
	private Integer degree;
	/**
	 * 富信主题
	 */
	private String tmName;
	/**
	 * 当前操作员Id
	 */
	private Long currUserId;
	/**
	 * 当前企业编码
	 */
	private String currCorpCode;
	/**
	 * 企业编码
	 */
	private String corpCode;
	/**
	 * 是包含子机构
	 */
	private Boolean iscontainsSun;
	/**
	 * 页面选择的机构Id
	 */
	private String depIds;
	/**
	 * 页面选择的操作员Id
	 */
	private String userIds;
	/**
	 * 页面选择的任务状态
	 */
	private Integer taskState;
	/**
	 * 页面选择的操作员名字
	 */
	private String userName;
	/**
	 * 发送开始时间(页面查询)
	 */
	private String startSendTime;
	/**
	 * 发送结束时间(页面查询)
	 */
	private String endSendTime;
	/**
	 * 创建开始时间(页面查询)
	 */
	private String startSubTime;
	/**
	 * 创建结束时间(页面查询)
	 */
	private String endSubTime;

	/**
	 * 当前模块名字
	 */
	private String menuName;
	/**
	 *是否需要状态报告
	 * 0:表示不需要;
	 * 1:需要通知状态报告;
	 * 2:需要下载状态报告;
	 * 3:通知、下载状态报告都要;(默认通知状态报告必须)
	 */
	private Integer rptFlag;
	/**
	 * 模板自增id
	 */
	private Long tmId;

	private Long msgType;

	public Long getTmId() {
		return tmId;
	}

	public void setTmId(Long tmId) {
		this.tmId = tmId;
	}

	public String getSpUser() {
		return spUser;
	}

	public void setSpUser(String spUser) {
		this.spUser = spUser;
	}

	public String getMobileUrl() {
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Long getTempId() {
		return tempId;
	}

	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}

	public Long getEffCount() {
		return effCount;
	}

	public void setEffCount(Long effCount) {
		this.effCount = effCount;
	}

	public Long getIcount2() {
		return icount2;
	}

	public void setIcount2(Long icount2) {
		this.icount2 = icount2;
	}

	public Long getRfail2() {
		return rfail2;
	}

	public void setRfail2(Long rfail2) {
		this.rfail2 = rfail2;
	}

	public Long getFaiCount() {
		return faiCount;
	}

	public void setFaiCount(Long faiCount) {
		this.faiCount = faiCount;
	}

	public Timestamp getTimerTime() {
		return timerTime;
	}

	public void setTimerTime(Timestamp timerTime) {
		this.timerTime = timerTime;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Integer getTimerStatus() {
		return timerStatus;
	}

	public void setTimerStatus(Integer timerStatus) {
		this.timerStatus = timerStatus;
	}

	public Integer getSubState() {
		return subState;
	}

	public void setSubState(Integer subState) {
		this.subState = subState;
	}

	public Integer getSendState() {
		return sendState;
	}

	public void setSendState(Integer sendState) {
		this.sendState = sendState;
	}

	public Integer getIsRetry() {
		return isRetry;
	}

	public void setIsRetry(Integer isRetry) {
		this.isRetry = isRetry;
	}

	public String getTmplPath() {
		return tmplPath;
	}

	public void setTmplPath(String tmplPath) {
		this.tmplPath = tmplPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public Long getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(Long currentCount) {
		this.currentCount = currentCount;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public String getTmName() {
		return tmName;
	}

	public void setTmName(String tmName) {
		this.tmName = tmName;
	}

	public Long getCurrUserId() {
		return currUserId;
	}

	public void setCurrUserId(Long currUserId) {
		this.currUserId = currUserId;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getDepIds() {
		return depIds;
	}

	public void setDepIds(String depIds) {
		this.depIds = depIds;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Integer getTaskState() {
		return taskState;
	}

	public void setTaskState(Integer taskState) {
		this.taskState = taskState;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getStartSubTime() {
		return startSubTime;
	}

	public void setStartSubTime(String startSubTime) {
		this.startSubTime = startSubTime;
	}

	public String getEndSubTime() {
		return endSubTime;
	}

	public void setEndSubTime(String endSubTime) {
		this.endSubTime = endSubTime;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getCurrCorpCode() {
		return currCorpCode;
	}

	public void setCurrCorpCode(String currCorpCode) {
		this.currCorpCode = currCorpCode;
	}

	public Boolean getIscontainsSun() {
		return iscontainsSun;
	}

	public void setIscontainsSun(Boolean iscontainsSun) {
		this.iscontainsSun = iscontainsSun;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getIcount() {
		return icount;
	}

	public void setIcount(Long icount) {
		this.icount = icount;
	}

	public Integer getRptFlag() {
		return rptFlag;
	}

	public void setRptFlag(Integer rptFlag) {
		this.rptFlag = rptFlag;
	}

	public Long getMtId() {
		return mtId;
	}

	public void setMtId(Long mtId) {
		this.mtId = mtId;
	}

	public String getMtIdCipher() {
		return mtIdCipher;
	}

	public void setMtIdCipher(String mtIdCipher) {
		this.mtIdCipher = mtIdCipher;
	}

	public Timestamp getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime;
	}

	public Long getDelayNum() {
		return delayNum;
	}

	public void setDelayNum(Long delayNum) {
		this.delayNum = delayNum;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public Long getMsgType() {
		return msgType;
	}

	public void setMsgType(Long msgType) {
		this.msgType = msgType;
	}
}
