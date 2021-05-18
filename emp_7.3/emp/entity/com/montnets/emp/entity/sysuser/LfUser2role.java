/**
 * 
 */
package com.montnets.emp.entity.sysuser;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-28 上午09:52:19
 * @description 
 */

public class LfUser2role implements java.io.Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8943910867095987393L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = -8943910867095987393L;
	//角色ID
	private Long roleId;
	//操作员ID
    private Long userId;
	
	public LfUser2role(){}

	
	
	//角色ID
	public Long getRoleId()
	{
		return roleId;
	}

	public void setRoleId(Long roleId)
	{
		this.roleId = roleId;
	}

	
	//操作员ID
	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	
}
