package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * 通道账号信息实体类
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 下午01:47:46
 */
public class LfMonSgtacinfo
{

	// MT接收速度告警阀值
	private Integer		mtrecvspd	= 0;

	// 监控状态
	private Integer		monstatus	= 1;

	// 记录更新时间
	private Timestamp	modifytime;

	// RPT最低转发率告警阀值
	private Integer		rptsndratio	= 0;

	// MO滞留量告警阀值
	private Integer		moremained	= 0;

	// 告警短信发送手机号
	private String		monphone	= "";
	
	//告警邮件发送邮箱
	private String		monemail = " ";

	// MO转发速度告警阀值
	private Integer		mosndspd	= 0;

	// MT滞留量告警阀值
	private Integer		mtremained	= 0;

	// 帐号费用告警阀值
	private Integer		userfee		= 0;

	// 通道账号
	private String		gateaccount;

	// 创建时间
	private Timestamp	createtime;

	// Rpt转发速度告警阀值
	private Integer		rptsndspd	= 0;

	// MO最低转发率告警阀值
	private Integer		mosndratio	= 0;

	// 帐号的连接数告警阀值
	private Integer		linknum		= 0;

	// 采集被监控程序的频率，单位秒
	private Integer		monfreq		= 30;

	// 通道账号名称
	private String		gatename;

	// Rpt滞留量告警阀值
	private Integer		rptremained	= 0;

	// 网关编号
	private Long		gatewayid;

	// 是否欠费告警 0否 1是
	private Integer		isarrearage	= 0;

	// 主机编号
	private Long		hostid;

	public Integer getIsarrearage()
	{
		return isarrearage;
	}

	public void setIsarrearage(Integer isarrearage)
	{
		this.isarrearage = isarrearage;
	}

	public LfMonSgtacinfo()
	{
	}

	public Integer getMtrecvspd()
	{

		return mtrecvspd;
	}

	public void setMtrecvspd(Integer mtrecvspd)
	{

		this.mtrecvspd = mtrecvspd;

	}

	public Integer getMonstatus()
	{

		return monstatus;
	}

	public void setMonstatus(Integer monstatus)
	{

		this.monstatus = monstatus;

	}

	public Timestamp getModifytime()
	{

		return modifytime;
	}

	public void setModifytime(Timestamp modifytime)
	{

		this.modifytime = modifytime;

	}

	public Integer getRptsndratio()
	{

		return rptsndratio;
	}

	public void setRptsndratio(Integer rptsndratio)
	{

		this.rptsndratio = rptsndratio;

	}

	public Integer getMoremained()
	{

		return moremained;
	}

	public void setMoremained(Integer moremained)
	{

		this.moremained = moremained;

	}

	public String getMonphone()
	{

		return monphone;
	}

	public void setMonphone(String monphone)
	{

		this.monphone = monphone;

	}

	public Integer getMosndspd()
	{

		return mosndspd;
	}

	public void setMosndspd(Integer mosndspd)
	{

		this.mosndspd = mosndspd;

	}

	public Integer getMtremained()
	{

		return mtremained;
	}

	public void setMtremained(Integer mtremained)
	{

		this.mtremained = mtremained;

	}

	public Integer getUserfee()
	{

		return userfee;
	}

	public void setUserfee(Integer userfee)
	{

		this.userfee = userfee;

	}

	public String getGateaccount()
	{

		return gateaccount;
	}

	public void setGateaccount(String gateaccount)
	{

		this.gateaccount = gateaccount;

	}

	public Timestamp getCreatetime()
	{

		return createtime;
	}

	public void setCreatetime(Timestamp createtime)
	{

		this.createtime = createtime;

	}

	public Integer getRptsndspd()
	{

		return rptsndspd;
	}

	public void setRptsndspd(Integer rptsndspd)
	{

		this.rptsndspd = rptsndspd;

	}

	public Integer getMosndratio()
	{

		return mosndratio;
	}

	public void setMosndratio(Integer mosndratio)
	{

		this.mosndratio = mosndratio;

	}

	public Integer getLinknum()
	{

		return linknum;
	}

	public void setLinknum(Integer linknum)
	{

		this.linknum = linknum;

	}

	public Integer getMonfreq()
	{

		return monfreq;
	}

	public void setMonfreq(Integer monfreq)
	{

		this.monfreq = monfreq;

	}

	public String getGatename()
	{

		return gatename;
	}

	public void setGatename(String gatename)
	{

		this.gatename = gatename;

	}

	public Integer getRptremained()
	{

		return rptremained;
	}

	public void setRptremained(Integer rptremained)
	{

		this.rptremained = rptremained;

	}

	public Long getGatewayid()
	{

		return gatewayid;
	}

	public void setGatewayid(Long gatewayid)
	{

		this.gatewayid = gatewayid;
	}

	public Long getHostid()
	{
		return hostid;
	}

	public void setHostid(Long hostid)
	{
		this.hostid = hostid;
	}

	public String getMonemail() {
		return monemail;
	}

	public void setMonemail(String monemail) {
		this.monemail = monemail;
	}

}
