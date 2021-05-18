/**
 * 
 */
package com.montnets.emp.entity.engine;

import java.sql.Timestamp;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-28 上午09:08:27
 * @description 
 */

public class LfServicelog implements java.io.Serializable
{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -6779070699601387568L;
	private Long slId;
	private Long serId;
	private Timestamp runTime;
	private Integer slState;
	private String url;
	
	public LfServicelog(){}

	public Long getSlId()
	{
		return slId;
	}

	public void setSlId(Long slId)
	{
		this.slId = slId;
	}

	public Long getSerId()
	{
		return serId;
	}

	public void setSerId(Long serId)
	{
		this.serId = serId;
	}
 

	public Timestamp getRunTime()
	{
		return runTime;
	}

	public void setRunTime(Timestamp runTime)
	{
		this.runTime = runTime;
	}

	public Integer getSlState()
	{
		return slState;
	}

	public void setSlState(Integer slState)
	{
		this.slState = slState;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
	
	
}
