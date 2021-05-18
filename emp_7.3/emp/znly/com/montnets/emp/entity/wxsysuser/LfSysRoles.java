/**
 * 
 */
package com.montnets.emp.entity.wxsysuser;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-10 下午01:40:20
 * @description 
 */

public class LfSysRoles implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7573393314887186904L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = -3055354886380351920L;
	//角色id
	private Long roleId;
	//角色名称
	private String roleName;
	//角色描述
	private String comments;
	//企业编码
	private String corpCode;
	//创建该角色者的guid
	private Long guId;	
	
	public LfSysRoles(){}

	public Long getGuId() {
		return guId;
	}
	public void setGuId(Long guId) {
		this.guId = guId;
	}

	public Long getRoleId()
	{
		return roleId;
	}



	public void setRoleId(Long roleId)
	{
		this.roleId = roleId;
	}



	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}



	public String getCorpCode()
	{
		return corpCode;
	}



	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	
	
}
