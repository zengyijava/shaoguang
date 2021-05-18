/**
 * 
 */
package com.montnets.emp.entity.client;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-30 上午11:20:32
 * @description 操作员与客户机构权限表
 */

public class LfCliDepConn implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7274438363622506238L;
	//标识列ID
	private Long connId;
	//操作员ID
	private Long userId;
	//机构ID（之前是用客户机构编码【DEP_CODE_THIRD】关联）
	private Long  depId;
	private String depCodeThird;
	
	
	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}
	public LfCliDepConn(){}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Long getConnId()
	{
		return connId;
	}

	public void setConnId(Long connId)
	{
		this.connId = connId;
	}

	public String getDepCodeThird()
	{
		return depCodeThird;
	}

	public void setDepCodeThird(String depCodeThird)
	{
		this.depCodeThird = depCodeThird;
	}
	
	
}
