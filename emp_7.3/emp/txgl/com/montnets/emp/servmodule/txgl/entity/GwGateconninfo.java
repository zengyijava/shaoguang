package com.montnets.emp.servmodule.txgl.entity;

import java.sql.Timestamp;

public class GwGateconninfo
{
	// 自增
	private Integer		id;

	/*
	 * 下级网关对应的平台的平台编号，默认0.CONNTYPE=1时，该字段有意义。
	 */
	private Integer		ptid;

	/*
	 * 通道ID, 默认0.CONNTYPE=0时,该字段有意义
	 */
	private Integer		gateid;

	/*
	 * 连接类型:直连0中转1EMP网关定义为供应商类型：其他：0 单链路连接梦网：1 主备多链路同时连接
	 */
	private Integer		conntype;

	/*
	 * 通道帐号
	 */
	private String		ptaccid;

	/*
	 * 目的地址
	 */
	private String		ip;

	/*
	 * 端口
	 */
	private Integer		port;

	/*
	 * 连接数
	 */
	private Integer		linkcnt;

	/*
	 * 总限速，暂不启用，使用运行参数控制。
	 */
	private Integer		speedlimit;

	/*
	 * 链路顺序，0为主用，1为备用
	 */
	private Integer		linklevel;

	/*
	 * 链路状态: 0,正常 1,暂不发送 2,停用
	 */
	private Integer		linkstatus;

	/*
	 * 是否可ping: 0否, 1是
	 */
	private Integer		ping;

	/*
	 * 最小连接数(当链路当前可用总连接数小于该值时,监控平台会报警).
	 */
	private Integer		minlinks;

	/*
	 * 主备链路是否同时连接并登录：0 只主用连接 1 主备同时连接CONNTYPE为0时，默认为 0
	 */
	private Integer		keepconn;

	/*
	 * 连续重连失败多少次后切换，默认5次。KEEPCONN=0时，该字段有意义
	 */
	private Integer		reconncnt;

	/*
	 * 连续登录失败多少次后切换，默认5次。KEEPCONN=0时，该字段有意义
	 */
	private Integer		relogincnt;

	/*
	 * 备用IP使用时，主用恢复正常后是否切回主用：
	 * 0 不切换回主用，直至备用IP不可用；
	 * 1 主用恢复正常即可切换
	 * KEEPCONN=0时，该字段有意义
	 */
	private Integer		switchmainip;

	/*
	 * 检测IP正常机制：
	 * 1 登录正常
	 * 2 连接正常
	 * KEEPCONN=0、 SWITCHMAINIP=1时，该字段有意义
	 */
	private Integer		testmethod;

	/*
	 * 主用恢复正常，备用链路多久无数据发送可以切换主用。
	 * KEEPCONN=0、 SWITCHMAINIP=1时，该字段有意义。
	 * 存在链路持续有数据发送，在每天的0点进行强制切换。
	 */
	private Integer		testtimes;

	/*
	 * 连续时间内连接异常次数超过所配置次数后，视为链路异常。
	 * 配合ABNORMALTMS参数一起生效
	 */
	private Integer		abnormalong;

	/*
	 * 连续异常次数。为0时表示该功能禁用。
	 */
	private Integer		abnormaltms;

	/*
	 * 创建时间
	 */
	private Timestamp	createtm;

	/*
	 * 修改时间
	 */
	private Timestamp	updatetm;

	public GwGateconninfo()
	{
		/*
		 * 下级网关对应的平台的平台编号，默认0.CONNTYPE=1时，该字段有意义。
		 */
		this.ptid = 0;
		/*
		 * 通道ID, 默认0.CONNTYPE=0时,该字段有意义
		 */
		this.gateid = 0;
		/*
		 * 连接类型:直连0中转1EMP网关定义为供应商类型：其他：0 单链路连接梦网：1 主备多链路同时连接
		 */
		this.conntype = 0;
		/*
		 * 通道帐号
		 */
		this.ptaccid = "";
		/*
		 * 目的地址
		 */
		this.ip = "";
		/*
		 * 端口
		 */
		this.port = 0;
		/*
		 * 连接数
		 */
		this.linkcnt = 3;
		/*
		 * 总限速，暂不启用，使用运行参数控制。
		 */
		this.speedlimit = 1000;
		/*
		 * 链路顺序，0为主用，1为备用
		 */
		this.linklevel = 0;
		/*
		 * 链路状态: 0,正常 1,暂不发送 2,停用
		 */
		this.linkstatus = 0;
		/*
		 * 是否可ping: 0否, 1是
		 */
		this.ping = 0;
		/*
		 * 最小连接数(当链路当前可用总连接数小于该值时,监控平台会报警).
		 */
		this.minlinks = 1;
		/*
		 * 主备链路是否同时连接并登录：0 只主用连接 1 主备同时连接CONNTYPE为0时，默认为 0
		 */
		this.keepconn = 1;
		/*
		 * 连续重连失败多少次后切换，默认5次。KEEPCONN=0时，该字段有意义
		 */
		this.reconncnt = 5;
		/*
		 * 连续登录失败多少次后切换，默认5次。KEEPCONN=0时，该字段有意义
		 */
		this.relogincnt = 5;
		/*
		 * 备用IP使用时，主用恢复正常后是否切回主用：
		 * 0 不切换回主用，直至备用IP不可用；
		 * 1 主用恢复正常即可切换
		 * KEEPCONN=0时，该字段有意义
		 */
		this.switchmainip = 1;
		/*
		 * 检测IP正常机制：
		 * 1 登录正常
		 * 2 连接正常
		 * KEEPCONN=0、 SWITCHMAINIP=1时，该字段有意义
		 */
		this.testmethod = 2;
		/*
		 * 主用恢复正常，备用链路多久无数据发送可以切换主用。
		 * KEEPCONN=0、 SWITCHMAINIP=1时，该字段有意义。
		 * 存在链路持续有数据发送，在每天的0点进行强制切换。
		 */
		this.testtimes = 60;
		/*
		 * 连续时间内连接异常次数超过所配置次数后，视为链路异常。
		 * 配合ABNORMALTMS参数一起生效
		 */
		this.abnormalong = 300;
		/*
		 * 连续异常次数。为0时表示该功能禁用。
		 */
		this.abnormaltms = 10;
		/*
		 * 创建时间
		 */
		this.createtm = new Timestamp(System.currentTimeMillis());
		/*
		 * 修改时间
		 */
		this.updatetm = new Timestamp(System.currentTimeMillis());
	}

	public Integer getSpeedlimit()
	{
		return speedlimit;
	}

	public void setSpeedlimit(Integer speedlimit)
	{
		this.speedlimit = speedlimit;
	}

	public Timestamp getUpdatetm()
	{
		return updatetm;
	}

	public void setUpdatetm(Timestamp updatetm)
	{
		this.updatetm = updatetm;
	}

	public Integer getPtid()
	{
		return ptid;
	}

	public void setPtid(Integer ptid)
	{
		this.ptid = ptid;
	}

	public Integer getPing()
	{
		return ping;
	}

	public void setPing(Integer ping)
	{
		this.ping = ping;
	}

	public Integer getTestmethod()
	{

		return testmethod;
	}

	public void setTestmethod(Integer testmethod)
	{
		this.testmethod = testmethod;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public Integer getAbnormaltms()
	{
		return abnormaltms;
	}

	public void setAbnormaltms(Integer abnormaltms)
	{
		this.abnormaltms = abnormaltms;
	}

	public Integer getReconncnt()
	{
		return reconncnt;
	}

	public void setReconncnt(Integer reconncnt)
	{
		this.reconncnt = reconncnt;
	}

	public Integer getGateid()
	{
		return gateid;
	}

	public void setGateid(Integer gateid)
	{
		this.gateid = gateid;
	}

	public Integer getSwitchmainip()
	{
		return switchmainip;
	}

	public void setSwitchmainip(Integer switchmainip)
	{
		this.switchmainip = switchmainip;
	}

	public Integer getMinlinks()
	{
		return minlinks;
	}

	public void setMinlinks(Integer minlinks)
	{
		this.minlinks = minlinks;
	}

	public Integer getTesttimes()
	{
		return testtimes;
	}

	public void setTesttimes(Integer testtimes)
	{
		this.testtimes = testtimes;
	}

	public Integer getKeepconn()
	{
		return keepconn;
	}

	public void setKeepconn(Integer keepconn)
	{
		this.keepconn = keepconn;
	}

	public Integer getPort()
	{
		return port;
	}

	public void setPort(Integer port)
	{
		this.port = port;
	}

	public String getPtaccid()
	{
		return ptaccid;
	}

	public void setPtaccid(String ptaccid)
	{
		this.ptaccid = ptaccid;
	}

	public Integer getAbnormalong()
	{
		return abnormalong;
	}

	public void setAbnormalong(Integer abnormalong)
	{
		this.abnormalong = abnormalong;
	}

	public Integer getLinkcnt()
	{
		return linkcnt;
	}

	public void setLinkcnt(Integer linkcnt)
	{
		this.linkcnt = linkcnt;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getConntype()
	{
		return conntype;
	}

	public void setConntype(Integer conntype)
	{
		this.conntype = conntype;
	}

	public Integer getLinklevel()
	{
		return linklevel;
	}

	public void setLinklevel(Integer linklevel)
	{
		this.linklevel = linklevel;
	}

	public Integer getLinkstatus()
	{
		return linkstatus;
	}

	public void setLinkstatus(Integer linkstatus)
	{
		this.linkstatus = linkstatus;
	}

	public Integer getRelogincnt()
	{
		return relogincnt;
	}

	public void setRelogincnt(Integer relogincnt)
	{
		this.relogincnt = relogincnt;
	}

	public Timestamp getCreatetm()
	{
		return createtm;
	}

	public void setCreatetm(Timestamp createtm)
	{
		this.createtm = createtm;
	}

}
