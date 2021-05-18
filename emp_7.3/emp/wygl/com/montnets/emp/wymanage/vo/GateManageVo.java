package com.montnets.emp.wymanage.vo;

public class GateManageVo
{
	// 通道名称
	private String	gatename;

	// IP地址
	private String	ip;

	// 端口号
	private String	port;

	// sim卡号
	private String	phoneno;

	// 运营商
	private String	unicom;

	// 通道id字符串
	private String	gateids;
	
	//通道ID
	private String gateid;

	public GateManageVo()
	{
	}

	public String getGateid()
	{
		return gateid;
	}

	public void setGateid(String gateid)
	{
		this.gateid = gateid;
	}

	public String getGateids()
	{
		return gateids;
	}

	public void setGateids(String gateids)
	{
		this.gateids = gateids;
	}

	public String getGatename()
	{
		return gatename;
	}

	public void setGatename(String gatename)
	{
		this.gatename = gatename;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getPhoneno()
	{
		return phoneno;
	}

	public void setPhoneno(String phoneno)
	{
		this.phoneno = phoneno;
	}

	public String getUnicom()
	{
		return unicom;
	}

	public void setUnicom(String unicom)
	{
		this.unicom = unicom;
	}

}
