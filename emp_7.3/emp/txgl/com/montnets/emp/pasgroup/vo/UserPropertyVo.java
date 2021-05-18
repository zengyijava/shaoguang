package com.montnets.emp.pasgroup.vo;

public class UserPropertyVo implements java.io.Serializable
{

	/**
	 * htts设置
	 */
	private static final long	serialVersionUID	= -4170642395356731527L;

	private String				userid;										// SPUSER

	private String				cacertname;									// 指令名称

	private String				verifypeer;									// 指令编码

	private String				verifyhost;

	public UserPropertyVo()
	{
		userid = "";
		cacertname = "";
		verifypeer = "";
		verifyhost = "";
	}

	public String getUserid()
	{
		return userid;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	public String getCacertname()
	{
		return cacertname;
	}

	public void setCacertname(String cacertname)
	{
		this.cacertname = cacertname;
	}

	public String getVerifypeer()
	{
		return verifypeer;
	}

	public void setVerifypeer(String verifypeer)
	{
		this.verifypeer = verifypeer;
	}

	public String getVerifyhost()
	{
		return verifyhost;
	}

	public void setVerifyhost(String verifyhost)
	{
		this.verifyhost = verifyhost;
	}

}
