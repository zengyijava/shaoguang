package com.montnets.emp.entity.gateway;

import java.sql.Timestamp;

/**
 * @description  运营商余额表
 * @author linzhihan <zhihanking@163.com>
 * @datetime 2013-10-30 上午10:02:57
 */
public class LfSpFee  implements java.io.Serializable
{

	private static final long	serialVersionUID	= -2685671673710792506L;
	// 自增ID
	private Long sfId;
	// SP账号/后端账号
	private String spUser;
	// 账户密码
	private String spUserpassword;
	// 账号类型(1代表后端账号，2代表SP账号)
	private Integer spType;
	// SP账号类型	(1短信SP账号，2彩信SP账号)
	private Integer accountType;
	// 运营商余额查询URL
	private String spFeeUrl;
	// 运营商余额
	private Integer balance;
	// 余额查询阀值
	private Integer balanceth;
	// 更新时间
	private Timestamp updateTime;
	// 扣费类型(1代表预付费，2代表后付费)
	private Integer spFeeFlag;
	//请求余额结果
	private String spResult;
	//查询时间
	private Timestamp queryTime;
	//富信运营商余额条数
	private Long rmsBalance;
	
	public Long getSfId()
	{
		return sfId;
	}
	public void setSfId(Long sfId)
	{
		this.sfId = sfId;
	}
	public String getSpUser()
	{
		return spUser;
	}
	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}
	public String getSpUserpassword()
	{
		return spUserpassword;
	}
	public void setSpUserpassword(String spUserpassword)
	{
		this.spUserpassword = spUserpassword;
	}
	public Integer getSpType()
	{
		return spType;
	}
	public void setSpType(Integer spType)
	{
		this.spType = spType;
	}
	public Integer getAccountType()
	{
		return accountType;
	}
	public void setAccountType(Integer accountType)
	{
		this.accountType = accountType;
	}
	public String getSpFeeUrl()
	{
		return spFeeUrl;
	}
	public void setSpFeeUrl(String spFeeUrl)
	{
		this.spFeeUrl = spFeeUrl;
	}
	public Integer getBalance()
	{
		return balance;
	}
	public void setBalance(Integer balance)
	{
		this.balance = balance;
	}
	public Integer getBalanceth()
	{
		return balanceth;
	}
	public void setBalanceth(Integer balanceth)
	{
		this.balanceth = balanceth;
	}
	public Timestamp getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}
	public Integer getSpFeeFlag()
	{
		return spFeeFlag;
	}
	public void setSpFeeFlag(Integer spFeeFlag)
	{
		this.spFeeFlag = spFeeFlag;
	}
	public String getSpResult()
	{
		return spResult;
	}
	public void setSpResult(String spResult)
	{
		this.spResult = spResult;
	}
	public Timestamp getQueryTime()
	{
		return queryTime;
	}
	public void setQueryTime(Timestamp queryTime)
	{
		this.queryTime = queryTime;
	}
	public Long getRmsBalance() {
		return rmsBalance;
	}
	public void setRmsBalance(Long rmsBalance) {
		this.rmsBalance = rmsBalance;
	}
	
}
