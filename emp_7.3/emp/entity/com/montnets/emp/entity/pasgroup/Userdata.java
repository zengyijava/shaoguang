package com.montnets.emp.entity.pasgroup;

import java.sql.Timestamp;

 
/**
 * TableUserdata对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:20:44
 * @description 
 */
public class Userdata implements java.io.Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1044168011788767143L;
	//SP账号(主键)
	private Long uid;
	//自增(费率表中通过UID关联)
	private String userId;
	//代理登录账号
	private String loginId;
	//账号名称
	private String staffName;
	//集团账号(默认为’’)
	private String corpAccount;
	//账号类型 (0企业用户，1spgate用户)默认0
	private Long userType;
	//状态(0已激活,1已失效,2已锁定)默认0
	private Integer status;
	//账号密码
	private String userPassword;
	//扣费类型(1:预付费,2:后付费)
	private Long feeFlag;
	//开户日期
	//private String khdate;
	private Timestamp orderTime;
	
	//private String lxr;
	
	//private String lxrph;
	//账号优先级
	private Long riseLevel;
	//上行URL
	private String moUrl;
	//状态报告URL
	private String rptUrl;
	//SP账号类型	(1短信SP账号，2彩信SP账号)
	private Integer accouttype;
	
	private Integer sptype;
	
	public String getMoUrl() {
		return moUrl;
	}

	public void setMoUrl(String moUrl) {
		this.moUrl = moUrl;
	}

	public Userdata(){
		//初始化数据
		this.corpAccount ="200001";
		//this.khdate=DateFormat.getDateInstance().format(new Date());
		this.orderTime = new Timestamp(System.currentTimeMillis());
	}
	 
	public Long getUid()
	{
		return uid;
	}

	public void setUid(Long uid)
	{
		this.uid = uid;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getLoginId()
	{
		return loginId;
	}

	public void setLoginId(String loginId)
	{
		this.loginId = loginId;
	}

	public String getStaffName()
	{
		return staffName;
	}

	public void setStaffName(String staffName)
	{
		this.staffName = staffName;
	}

	public String getCorpAccount()
	{
		return corpAccount;
	}

	public void setCorpAccount(String corpAccount)
	{
		this.corpAccount = corpAccount;
	}

	public Long getUserType()
	{
		return userType;
	}

	public void setUserType(Long userType)
	{
		this.userType = userType;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getUserPassword()
	{
		return userPassword;
	}

	public void setUserPassword(String userPassword)
	{
		this.userPassword = userPassword;
	}

	public Long getFeeFlag()
	{
		return feeFlag;
	}

	public void setFeeFlag(Long feeFlag)
	{
		this.feeFlag = feeFlag;
	}
 
	/*public String getLxr()
	{
		return lxr;
	}

	public void setLxr(String lxr)
	{
		this.lxr = lxr;
	}
 */

	public Long getRiseLevel()
	{
		return riseLevel;
	}

	public void setRiseLevel(Long riseLevel)
	{
		this.riseLevel = riseLevel;
	}

	public String getRptUrl()
	{
		return rptUrl;
	}

	public void setRptUrl(String rptUrl)
	{
		this.rptUrl = rptUrl;
	}

	public Timestamp getOrderTime()
	{
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime)
	{
		this.orderTime = orderTime;
	}

	public Integer getAccouttype() {
		return accouttype;
	}

	public void setAccouttype(Integer accouttype) {
		this.accouttype = accouttype;
	}

	public Integer getSptype()
	{
		return sptype;
	}

	public void setSptype(Integer sptype)
	{
		this.sptype = sptype;
	}
	
	
}
