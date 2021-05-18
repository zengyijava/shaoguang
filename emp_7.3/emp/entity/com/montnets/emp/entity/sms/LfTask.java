package com.montnets.emp.entity.sms;

import java.sql.Timestamp;

/**
 * TableLfTask对应的实体类
 * 
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:25:31
 * @description
 */
public class LfTask implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3926790175963367245L;

	/**
	 * 
	 */
	// private static final long serialVersionUID = -4643663598574144942L;
	// 标识ID
	private Long taId;

	//任务ID
	private Long taskId;

	//服务任务ID
	private Long slId;

	//发送任务ID
	private Long mtId;
	
	// 业务类型（服务0，手工1，接入2,Im3）
	private Integer taskType;

	//SP账号
	private String spUser;

	//操作员登录ID(指提交任务的操作员帐号)
	private String userName;

	//操作员所属机构ID（指提交任务的操作员所在部门）
	private Long depId;

	//操作员ID
	private Long userId;
	
	//提交时间
	private Timestamp submitTime;
	
	//企业编码
	private String corpCode;
	
	public LfTask()
	{
		submitTime = new Timestamp(System.currentTimeMillis());
	}
	 
	public Long getTaId()
	{
		return taId;
	}

	public Timestamp getSubmitTime()
	{
		return submitTime;
	}

	public void setSubmitTime(Timestamp submitTime)
	{
		this.submitTime = submitTime;
	}

	public void setTaId(Long taId)
	{
		this.taId = taId;
	}

	public Long getTaskId()
	{
		return taskId;
	}

	public void setTaskId(Long taskId)
	{
		this.taskId = taskId;
	}

	public Long getSlId()
	{
		return slId;
	}

	public void setSlId(Long slId)
	{
		this.slId = slId;
	}

	public Long getMtId()
	{
		return mtId;
	}

	public void setMtId(Long mtId)
	{
		this.mtId = mtId;
	}

	public Integer getTaskType()
	{
		return taskType;
	}

	public void setTaskType(Integer taskType)
	{
		this.taskType = taskType;
	}

	public String getSpUser()
	{
		return spUser;
	}

	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
    
 

}