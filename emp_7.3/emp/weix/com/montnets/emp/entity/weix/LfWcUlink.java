package com.montnets.emp.entity.weix;

/**
 * 微信用户与客户关联实体表
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWcUlink implements java.io.Serializable
{
	private static final long	serialVersionUID	= -4923255341568942868L;

	// 微信帐号ID
	private Long				wcId;

	// EMP用户ID
	private Long				UId;

	public LfWcUlink()
	{
	}

	public Long getWcId()
	{
		return wcId;
	}

	public void setWcId(Long wcId)
	{
		this.wcId = wcId;
	}

	public Long getUId()
	{
		return UId;
	}

	public void setUId(Long UId)
	{
		this.UId = UId;
	}
}
