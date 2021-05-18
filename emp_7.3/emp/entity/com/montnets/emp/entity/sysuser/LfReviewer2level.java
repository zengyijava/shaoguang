/**
 * 
 */
package com.montnets.emp.entity.sysuser;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-28 上午10:24:27
 * @description 
 */

public class LfReviewer2level implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2358410128611379868L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = -2358410128611379868L;
	//主键
	private Long frlId;   
	//外键(LF_FLOW.Fid)  审批流程表
	private Long FId;   
	//外键(lf_sysuser.user_id)  用户表
	private Long userId;   
	//等级 
	private Integer RLevel;   
	//该级审核人是否需要审核提醒)  1 ：是 ，2：否
	private Integer isRevRemind;
	//审核类型，1-操作员，2-群组，3-上级，4-机构审核员,5-逐级审核
	private Integer rType;
	//唯一编码，R_TYPE类型决定值类型
	private String rCode;
	//1全部通过生效;2第一人审核生效
	private Integer rCondition;
	
	
	
	
	public Integer getIsRevRemind() {
		return isRevRemind;
	}

	public void setIsRevRemind(Integer isRevRemind) {
		this.isRevRemind = isRevRemind;
	}

	public LfReviewer2level(){}

	public Long getFrlId()
	{
		return frlId;
	}

	public void setFrlId(Long frlId)
	{
		this.frlId = frlId;
	}


	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Long getFId()
	{
		return FId;
	}

	public void setFId(Long fId)
	{
		FId = fId;
	}

	public Integer getRLevel()
	{
		return RLevel;
	}

	public void setRLevel(Integer rLevel)
	{
		RLevel = rLevel;
	}

	public Integer getRType()
	{
		return rType;
	}

	public void setRType(Integer rType)
	{
		this.rType = rType;
	}

	public String getRCode()
	{
		return rCode;
	}

	public void setRCode(String rCode)
	{
		this.rCode = rCode;
	}

	public Integer getRCondition()
	{
		return rCondition;
	}

	public void setRCondition(Integer rCondition)
	{
		this.rCondition = rCondition;
	}

	
	
}
