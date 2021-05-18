package com.montnets.emp.common.vo;
/**
 * 审核流程VO类
 * @project emp_std_201508
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-11-23 上午11:22:56
 * @description
 */
public class ReviewFlowVo
{
	// 当前审批级别
	private Integer rLevel;
	
	// 审批总级别
	private Integer rLevelAmount;
	
	// 审批人姓名
	private String reviewerName;
	
	// 审核状态
	private String reviewState;
	
	// 审核意见
	private String reviewComments;
	
	// 审核时间
	private String reviewTime;
	
	//催办时间
	private String remindTime;

	public Integer getrLevel()
	{
		return rLevel;
	}

	public void setrLevel(Integer rLevel)
	{
		this.rLevel = rLevel;
	}

	public Integer getrLevelAmount()
	{
		return rLevelAmount;
	}

	public void setrLevelAmount(Integer rLevelAmount)
	{
		this.rLevelAmount = rLevelAmount;
	}

	public String getReviewerName()
	{
		return reviewerName;
	}

	public void setReviewerName(String reviewerName)
	{
		this.reviewerName = reviewerName;
	}

	public String getReviewState()
	{
		return reviewState;
	}

	public void setReviewState(String reviewState)
	{
		this.reviewState = reviewState;
	}

	public String getReviewComments()
	{
		return reviewComments;
	}

	public void setReviewComments(String reviewComments)
	{
		this.reviewComments = reviewComments;
	}

	public String getReviewTime()
	{
		return reviewTime;
	}

	public void setReviewTime(String reviewTime)
	{
		this.reviewTime = reviewTime;
	}

	public String getRemindTime()
	{
		return remindTime;
	}

	public void setRemindTime(String remindTime)
	{
		this.remindTime = remindTime;
	}
	
	
}
