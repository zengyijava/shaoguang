package com.montnets.emp.sysuser.vo;

import java.io.Serializable;

public class LfRolesVo implements Serializable {

	/**
	 * 角色vo
	 */
	private static final long serialVersionUID = -4864598112847295609L;
	//角色ID
	private Long roleId;			
	//角色名称
	private String roleName;	
	//备注
	private String comments;	
	//创建该角色者的guid
	private Long guId;			
	//该创建者的名字
	private String userName;			

	public LfRolesVo()
	{
	}
	
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getGuId() {
		return guId;
	}

	public void setGuId(Long guId) {
		this.guId = guId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
