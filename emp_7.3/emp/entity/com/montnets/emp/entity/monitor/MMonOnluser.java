package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-2 下午03:30:19
 */

public class MMonOnluser
{
	// 登录IP
	private String		loginaddr;

	// 所属机构
	private Long		depid;

	// 操作员名称
	private String		name;

	// 用户编号
	private Long		userid;

	// 登录账号
	private String		username;

	// 会话ID
	private String		sesseionid;

	// 登录时间
	private Timestamp	logintime;

	// 企业编号
	private String		corpcode;

	// 服务器编号
	private String		serverNum;

	public MMonOnluser()
	{
	}

	public String getLoginaddr()
	{

		return loginaddr;
	}

	public void setLoginaddr(String loginaddr)
	{

		this.loginaddr = loginaddr;

	}

	public Long getDepid()
	{

		return depid;
	}

	public void setDepid(Long depid)
	{

		this.depid = depid;

	}

	public String getName()
	{

		return name;
	}

	public void setName(String name)
	{

		this.name = name;

	}

	public Long getUserid()
	{

		return userid;
	}

	public void setUserid(Long userid)
	{

		this.userid = userid;

	}

	public String getUsername()
	{

		return username;
	}

	public void setUsername(String username)
	{

		this.username = username;

	}

	public String getSesseionid()
	{

		return sesseionid;
	}

	public void setSesseionid(String sesseionid)
	{

		this.sesseionid = sesseionid;

	}

	public Timestamp getLogintime()
	{

		return logintime;
	}

	public void setLogintime(Timestamp logintime)
	{

		this.logintime = logintime;

	}

	public String getCorpcode()
	{

		return corpcode;
	}

	public void setCorpcode(String corpcode)
	{

		this.corpcode = corpcode;

	}

	public String getServerNum()
	{
		return serverNum;
	}

	public void setServerNum(String serverNum)
	{
		this.serverNum = serverNum;
	}
}
