/**
 * 
 */
package com.montnets.emp.entity.sysuser;


 

/**
 * TableLfImpower对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 上午10:43:44
 * @description 
 */

public class LfImpower implements java.io.Serializable
{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8444906700746321642L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = 8444906700746321642L;
	private Long roleId;
	private Long privilegeId;
	
	public LfImpower(){}
	
	
	public Long getRoleId()
	{
		return roleId;
	}

	public void setRoleId(Long roleId)
	{
		this.roleId = roleId;
	}

	public Long getPrivilegeId()
	{
		return privilegeId;
	}

	public void setPrivilegeId(Long privilegeId)
	{
		this.privilegeId = privilegeId;
	}

 
	
	
}
