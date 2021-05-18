package com.montnets.emp.client.vo;
/**
 * 通讯录权限vo
 * @project emp
 * @author linzhihan <zhihanking@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-6-9 上午10:14:32
 * @description
 */
public class AddrBookPermissionsVo implements java.io.Serializable
{
	private static final long serialVersionUID = -1934400900243453206L;
	//机构code
	private String depCode;
	//用户id
	private String userId;
	private String connId;
    //机构名称
	private String depName;
	//用户名称
	private String name;
	//用户名称
	private String userName;	
	//用于查询条件的机构ID字符串
	private String depIds;
	//机构id
	private Long depId;
	//获取机构编码
	public String getDepCode()
	{
		return depCode;
	}
	//设置机构编码
	public void setDepCode(String depCode)
	{
		this.depCode = depCode;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getConnId()
	{
		return connId;
	}

	public void setConnId(String connId)
	{
		this.connId = connId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "AddrBookPermissionsVo [depCode=" + depCode + ", userId="
				+ userId + ", connId=" + connId + ", depName=" + depName
				+ ", name=" + name + ", userName=" + userName + "]";
	}

	public String getDepIds() {
		return depIds;
	}

	public void setDepIds(String depIds) {
		this.depIds = depIds;
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
