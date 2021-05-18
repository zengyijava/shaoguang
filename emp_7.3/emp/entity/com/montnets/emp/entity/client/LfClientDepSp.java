/**
 * 
 */
package com.montnets.emp.entity.client;

/**
 * @project sinolife
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-16 下午03:16:00
 * @description 
 */

public class LfClientDepSp implements java.io.Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9019418545548916570L;

	//客户ID
	private Long clientId;
	//标识列ID
	private Long depId;
	
	
	
	public Long getClientId()
	{
		return clientId;
	}
	public void setClientId(Long clientId)
	{
		this.clientId = clientId;
	}
	public Long getDepId()
	{
		return depId;
	}
	public void setDepId(Long depId)
	{
		this.depId = depId;
	}
	
	

}
