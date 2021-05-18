package com.montnets.emp.entity.sysuser;

import java.util.Date;

public class LfUpUserInfo   implements java.io.Serializable
{
	/* 用户ID */
	private String guid;
	/* 部门id */
	private Long depCode;
	/* 用户类型 */
	private String userType;
	/* 用户角色 */
	private String userRole;
	/* 用户名 */
	private String userName;
	/* 姓名 */
	private String name;
	/* 性别 */
	private String sex;
	/* 生日 */
	private String birthday;
	/* 手机 */
	private String mobile;
	/* 办公电话 */
	private String telephone;
	/* qq */
	private String qq;
	/* 邮箱 */
	private String email;
	/* msn */
	private String msn;
	/* 密码 */
	private String password;
	

	private String userState;
	/* 创建时间 */
	private Date regtime;
	/* 对应外部用户编码 */
	private String userCodeThird;
	/* 企业代码 */
	private String corpCode;
	/* 是否有效 */
	private String status;
	/* 更新时间 */
	private Date updateTime;
	/* 操作类型 */
	private String  optType;
	/* 是否修改 */
	private String  isUpdate;
	/*自增ID */
	private Long  upId;
	
	public String getUserState() {
		return userState;
	}
	public void setUserState(String userState) {
		this.userState = userState;
	}
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
	public Long getUpId() {
		return upId;
	}
	public void setUpId(Long upId) {
		this.upId = upId;
	}

	
	public String getGuid()
	{
		return guid;
	}
	public void setGuid(String guid)
	{
		this.guid = guid;
	}
	public Long getDepCode()
	{
		return depCode;
	}
	public void setDepCode(Long depCode)
	{
		this.depCode = depCode;
	}
	public String getUserType()
	{
		return userType;
	}
	public void setUserType(String userType)
	{
		this.userType = userType;
	}
	public String getUserRole()
	{
		return userRole;
	}
	public void setUserRole(String userRole)
	{
		this.userRole = userRole;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getSex()
	{
		return sex;
	}
	public void setSex(String sex)
	{
		this.sex = sex;
	}
	public String getBirthday()
	{
		return birthday;
	}
	public void setBirthday(String birthday)
	{
		this.birthday = birthday;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getTelephone()
	{
		return telephone;
	}
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}
	public String getQq()
	{
		return qq;
	}
	public void setQq(String qq)
	{
		this.qq = qq;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getMsn()
	{
		return msn;
	}
	public void setMsn(String msn)
	{
		this.msn = msn;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public Date getRegtime()
	{
		return regtime;
	}
	public void setRegtime(Date regtime)
	{
		this.regtime = regtime;
	}
	public String getUserCodeThird()
	{
		return userCodeThird;
	}
	public void setUserCodeThird(String userCodeThird)
	{
		this.userCodeThird = userCodeThird;
	}
	public String getCorpCode()
	{
		return corpCode;
	}
	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	
}
