/**
 * 
 */
package com.montnets.emp.entity.sysuser;

import java.sql.Timestamp;


/**
 * 审核流程表
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午03:00:47
 * @description 
 */

public class LfFlow implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6641697173205680300L;
	//审核流ID 标识列
	private Long FId; 
	//审核任务名
	private String FTask;
	//审核类型（手工业务账户0、关键字过滤1）
	private Integer FType;
	//审批级数(1-5级)从一级开始审批，先审核一级才能审核二级，然后才能审核三级，如此类推，一直审核到五级
	private Integer RLevelAmount;
	//发送账号(SP账号)
	private String userId;
	//条数
	private Long msgAccount;
	//备注
	private String comments;
	//企业编号
	private String corpCode;   
	//审核流创建人id
	private Long createUserId;
	//审批流程页面显示用的字符串
	private String flowshow;
	//启用状态  1.启用  2.禁用
	private Integer remindState;
	//修改时间
	private Timestamp updateTime;
	//新增时间
	private Timestamp createTime;
	//1：启用，2：禁用
	private Integer flowState;
	
	
	public Long getCreateUserId() {
		return createUserId;
	}




	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}




	public LfFlow(){}

	
	//审批流程暂时用的字符串
	public String getFlowshow() {
		return flowshow;
	}




	public void setFlowshow(String flowshow) {
		this.flowshow = flowshow;
	}




	public Long getFId()
	{
		return FId;
	}



	public void setFId(Long fId)
	{
		FId = fId;
	}



	public String getFTask()
	{
		return FTask;
	}



	public void setFTask(String fTask)
	{
		FTask = fTask;
	}



	public Integer getFType()
	{
		return FType;
	}



	public void setFType(Integer fType)
	{
		FType = fType;
	}



	public Integer getRLevelAmount()
	{
		return RLevelAmount;
	}



	public void setRLevelAmount(Integer rLevelAmount)
	{
		RLevelAmount = rLevelAmount;
	}



	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Long getMsgAccount()
	{
		return msgAccount;
	}

	public void setMsgAccount(Long msgAccount)
	{
		this.msgAccount = msgAccount;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}



	public String getCorpCode() {
		return corpCode;
	}



	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}




	public Integer getRemindState() {
		return remindState;
	}




	public void setRemindState(Integer remindState) {
		this.remindState = remindState;
	}




	public Timestamp getUpdateTime()
	{
		return updateTime;
	}




	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}




	public Timestamp getCreateTime()
	{
		return createTime;
	}




	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}




	public Integer getFlowState()
	{
		return flowState;
	}




	public void setFlowState(Integer flowState)
	{
		this.flowState = flowState;
	}
 

	
}
