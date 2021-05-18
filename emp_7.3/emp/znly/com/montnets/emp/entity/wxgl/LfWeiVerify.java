package com.montnets.emp.entity.wxgl;

import java.sql.Timestamp;

/**
 * 手机号对应验证码实体类
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWeiVerify implements java.io.Serializable
{

	private static final long	serialVersionUID	= -6298313707205249070L;

	private Long				vfId;

	// 微信帐号ID
	private Long				wcId;

	// 手机号
	private String				phone;

	// 验证码
	private String				verifyCode;

	// 最后一次获取验证码成功的有效时间
	private Timestamp			codeTime;

	// verifyTime 验证码的获取时间
	private Timestamp			limitTime;

	// 验证次数
	private Integer				verifyCount;

	// 企业编码
	private String				corpCode;

	public Long getWcId()
	{
		return wcId;
	}

	public void setWcId(Long wcId)
	{
		this.wcId = wcId;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getVerifyCode()
	{
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode)
	{
		this.verifyCode = verifyCode;
	}

	public Timestamp getCodeTime()
	{
		return codeTime;
	}

	public void setCodeTime(Timestamp codeTime)
	{
		this.codeTime = codeTime;
	}

	public Long getVfId()
	{
		return vfId;
	}

	public void setVfId(Long vfId)
	{
		this.vfId = vfId;
	}

	public Timestamp getLimitTime()
	{
		return limitTime;
	}

	public void setLimitTime(Timestamp limitTime)
	{
		this.limitTime = limitTime;
	}

	public Integer getVerifyCount()
	{
		return verifyCount;
	}

	public void setVerifyCount(Integer verifyCount)
	{
		this.verifyCount = verifyCount;
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
