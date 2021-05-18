/**
 * 
 */
package com.montnets.emp.entity.gateway;

/**
 * @project montnets_gateway
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-3 下午07:01:40
 * @description 
 */

public class AgwSpBind implements java.io.Serializable
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7564363225520198140L;

	private Integer ptAccUid;
	
	private Integer gateId;
	
	public AgwSpBind(){}

	public Integer getPtAccUid()
	{
		return ptAccUid;
	}

	public void setPtAccUid(Integer ptAccUid)
	{
		this.ptAccUid = ptAccUid;
	}

	public Integer getGateId()
	{
		return gateId;
	}

	public void setGateId(Integer gateId)
	{
		this.gateId = gateId;
	}
	
	
}
