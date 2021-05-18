package com.montnets.emp.entity.weix;

/**
 * 微信公众帐号关系实体类
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWcAlink implements java.io.Serializable
{
	private static final long	serialVersionUID	= -4923255341568942878L;

	// EMP微信用户ID
	private Long				wcId;

	// EMP用户ID
	private Long				AId;

	// EMP微信用户openid
	private String				openId;

	public LfWcAlink()
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

	public Long getAId()
	{
		return AId;
	}

	public void setAId(Long AId)
	{
		this.AId = AId;
	}

	public String getOpenId()
	{
		return openId;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}
}
