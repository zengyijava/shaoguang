/**
 * 
 */
package com.montnets.emp.entity.sysuser;


 

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午02:21:56
 * @description 
 */

public class LfDomination implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4002648069995133358L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = 4002648069995133358L;
	//操作员ID
	private Long depId;
	//机构ID
	private Long userId;
	
	public LfDomination(){}
	

	public Long getDepId()
	{
		return depId;
	}


	public void setDepId(Long depId)
	{
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
 	
 	
}
