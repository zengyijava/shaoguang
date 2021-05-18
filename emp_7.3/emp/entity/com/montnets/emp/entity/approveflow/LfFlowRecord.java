/**
 * 
 */
package com.montnets.emp.entity.approveflow;

import java.sql.Timestamp;

/**
 * TableLfFlowRecord对应的实体类 审核流程记录
 * 
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-19 下午01:42:01
 * @description
 */

public class LfFlowRecord implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3854046162816832012L;
	/**
	 * 
	 */
	// private static final long serialVersionUID = 8466944967250489155L;
	// 标示ID
	private Long frId;
	// 对应的审批任务ID
	private Long mtId;
	// 逐级审批的记录时，该值记录逐级审核层级的下标。非逐级审核时，则为0
	private Long preRv;
	// 当前审批人ID
	private Long reviewer;
	// 当前审批级别
	private Integer RLevel;
	// 审批总级别
	private Integer RLevelAmount;
	// 审批意见
	private String RContent;
	// 审批状态
	private Integer RState;
	// 备注
	private String comments;
	// 审批时间
	private Timestamp RTime;
	// 审核流程ID
	private Long FId;
	// 模板审核时，插入模板类型。1.通用动态模块;0.通用静态模块;2.智能抓取模块;3.移动财务模块
	private Integer reviewType;
	// 审批指令(同意)
	private String batchNumber;
	// 审批指令(不同意)
	private String disagreeNumber;
	
	//信息类型。1：短信发送；2：彩信发送；3：短信模板；4：彩信模板；6:网讯模板
	private Integer infoType;
	//发起流程的操作员唯一编码
	private Long ProUserCode;
	//审核类型，1-操作员，2-群组，3-上级，4-机构审核员,5-逐级审核
	private Integer rType;
	//审核人操作员编码
	private Long userCode;
	//1全部通过生效;2第一人审核生效
	private Integer rCondition;
	//1：流程结束。2：流程未结束
	private Integer isComplete;
	//任务提交时间
	private Timestamp submitTime;
	
	//是否已催办 1已催办 0未催办
	private Integer isremind;
	
	//催办时间  未催办时，此字段为入库时间
	private Timestamp remindTime;
	
	public String getDisagreeNumber() {
		return disagreeNumber;
	}

	public void setDisagreeNumber(String disagreeNumber) {
		this.disagreeNumber = disagreeNumber;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public LfFlowRecord() {
		RTime = new Timestamp(System.currentTimeMillis());
		isremind=0;
		remindTime=new Timestamp(System.currentTimeMillis());
	}

	public Long getFrId() {
		return frId;
	}

	public void setFrId(Long frId) {
		this.frId = frId;
	}

	public Long getMtId() {
		return mtId;
	}

	public void setMtId(Long mtId) {
		this.mtId = mtId;
	}

	public Long getPreRv() {
		return preRv;
	}

	public void setPreRv(Long preRv) {
		this.preRv = preRv;
	}

	public Long getReviewer() {
		return reviewer;
	}

	public void setReviewer(Long reviewer) {
		this.reviewer = reviewer;
	}

	public Integer getRLevel() {
		return RLevel;
	}

	public void setRLevel(Integer rLevel) {
		RLevel = rLevel;
	}

	public Integer getRLevelAmount() {
		return RLevelAmount;
	}

	public void setRLevelAmount(Integer rLevelAmount) {
		RLevelAmount = rLevelAmount;
	}

	public String getRContent() {
		return RContent;
	}

	public void setRContent(String rContent) {
		RContent = rContent;
	}

	public Integer getRState() {
		return RState;
	}

	public void setRState(Integer rState) {
		RState = rState;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Timestamp getRTime() {
		return RTime;
	}

	public void setRTime(Timestamp rTime) {
		RTime = rTime;
	}

	public Long getFId() {
		return FId;
	}

	public void setFId(Long fId) {
		FId = fId;
	}

	public Integer getReviewType() {
		return reviewType;
	}

	public void setReviewType(Integer reviewType) {
		this.reviewType = reviewType;
	}

	public Integer getInfoType()
	{
		return infoType;
	}

	public void setInfoType(Integer infoType)
	{
		this.infoType = infoType;
	}

	public Long getProUserCode()
	{
		return ProUserCode;
	}

	public void setProUserCode(Long proUserCode)
	{
		ProUserCode = proUserCode;
	}

	public Integer getRType()
	{
		return rType;
	}

	public void setRType(Integer rType)
	{
		this.rType = rType;
	}

	public Long getUserCode()
	{
		return userCode;
	}

	public void setUserCode(Long userCode)
	{
		this.userCode = userCode;
	}

	public Integer getRCondition()
	{
		return rCondition;
	}

	public void setRCondition(Integer rCondition)
	{
		this.rCondition = rCondition;
	}

	public Integer getIsComplete()
	{
		return isComplete;
	}

	public void setIsComplete(Integer isComplete)
	{
		this.isComplete = isComplete;
	}

	public Timestamp getSubmitTime()
	{
		return submitTime;
	}

	public void setSubmitTime(Timestamp submitTime)
	{
		this.submitTime = submitTime;
	}

	public Integer getIsremind()
	{
		return isremind;
	}

	public void setIsremind(Integer isremind)
	{
		this.isremind = isremind;
	}

	public Timestamp getRemindTime()
	{
		return remindTime;
	}

	public void setRemindTime(Timestamp remindTime)
	{
		this.remindTime = remindTime;
	}
	
	

}
