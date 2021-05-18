package com.montnets.emp.spbalance.vo;


public class UserfeeVo implements java.io.Serializable{
	
	/**
	 * @description  
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-17 上午09:00:35
	 */
	private static final long	serialVersionUID	= 1L;
	//sp账号
	private String spuser;
	//信息类型 0 短信 1 彩信
	private String accounttype;
	//企业编码
	private String sptype;
	//企业编码
	private String corpcode;
	
	public String getSpuser()
	{
		return spuser;
	}
	public void setSpuser(String spuser)
	{
		this.spuser = spuser;
	}
	public String getAccounttype()
	{
		return accounttype;
	}
	public void setAccounttype(String accounttype)
	{
		this.accounttype = accounttype;
	}
	public String getSptype()
	{
		return sptype;
	}
	public void setSptype(String sptype)
	{
		this.sptype = sptype;
	}
	public String getCorpcode()
	{
		return corpcode;
	}
	public void setCorpcode(String corpcode)
	{
		this.corpcode = corpcode;
	}


}
