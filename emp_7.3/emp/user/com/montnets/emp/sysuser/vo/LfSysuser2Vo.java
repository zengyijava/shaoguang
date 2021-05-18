package com.montnets.emp.sysuser.vo;

import java.sql.Timestamp;
import java.util.List;

public class LfSysuser2Vo implements java.io.Serializable
{
	/**
	 * 操作用户vo
	 */
	private static final long serialVersionUID = -3407233205092228376L;
	//操作员userid
	private Long userId;
	//操作员名称
	private String name;
	
	private Integer userType;
	//用户状态
	private Integer userState;

	private String holder;

	private Timestamp regTime;
	//用户手机号
	private String mobile;
    //用户座机号
	private String oph;
	//用户QQ号
	private String qq;
	 //用户 email
	private String EMail;
	//用户性别
	private Integer sex;
	//用户密码
	private String password;
	//用户名称
	private String userName;
	//机构id
	private String depId;
	

	@SuppressWarnings("unchecked")
	private List domDepList;

	@SuppressWarnings("unchecked")
	private List roleList;
	
	

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getUserState()
	{
		return userState;
	}

	public void setUserState(Integer userState)
	{
		this.userState = userState;
	}

	public String getHolder()
	{
		return holder;
	}

	public void setHolder(String holder)
	{
		this.holder = holder;
	}

	public Timestamp getRegTime()
	{
		return regTime;
	}

	public void setRegTime(Timestamp regTime)
	{
		this.regTime = regTime;
	}


	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getOph()
	{
		return oph;
	}

	public void setOph(String oph)
	{
		this.oph = oph;
	}

	public String getQq()
	{
		return qq;
	}

	public void setQq(String qq)
	{
		this.qq = qq;
	}

	public String getEMail()
	{
		return EMail;
	}

	public void setEMail(String eMail)
	{
		EMail = eMail;
	}

	public Integer getSex()
	{
		return sex;
	}

	public void setSex(Integer sex)
	{
		this.sex = sex;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@SuppressWarnings("unchecked")
	public List getDomDepList()
	{
		return domDepList;
	}

	@SuppressWarnings("unchecked")
	public void setDomDepList(List domDepList)
	{
		this.domDepList = domDepList;
	}

	@SuppressWarnings("unchecked")
	public List getRoleList()
	{
		return roleList;
	}

	@SuppressWarnings("unchecked")
	public void setRoleList(List roleList)
	{
		this.roleList = roleList;
	}

	public String getDepId()
	{
		return depId;
	}

	public void setDepId(String depId)
	{
		this.depId = depId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

}
