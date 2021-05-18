/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-31 上午10:23:52
 */
package com.montnets.emp.entity.monitoronline;

import java.sql.Timestamp;

/**
 * @description
 * @project p_comm
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-31 上午10:23:52
 */

public class LfMonOnluser
{
	// 登录IP地址
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
	private String		servernum;

	// 服务器编号
	private String		corpcode;
	
	// 机构名称
	private String depName;

	public LfMonOnluser()
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

	public String getServernum()
	{

		return servernum;
	}

	public void setServernum(String servernum)
	{

		this.servernum = servernum;

	}

	public String getCorpcode()
	{

		return corpcode;
	}

	public void setCorpcode(String corpcode)
	{

		this.corpcode = corpcode;

	}

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

}
