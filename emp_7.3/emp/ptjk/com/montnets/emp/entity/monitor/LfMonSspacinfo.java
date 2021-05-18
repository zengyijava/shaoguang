package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * SP账号信息表实体类
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 下午01:57:22
 */
public class LfMonSspacinfo
{

	// 账号类型
	private Integer		spaccounttype;

	// 监控状态
	private Integer		monstatus		= 1;

	// 记录更新时间
	private Timestamp	modifytime;

	// MO滞留量告警阀值
	private Integer		moremained		= 0;

	// Rpt最低接收率告警阀值
	private Integer		rptrecvratio	= 0;

	// 告警短信发送手机号
	private String		monphone		= "";
	
	// 告警邮件发送邮箱
	private String		monemail		= " ";

	// MT下发速度告警阀值
	private Integer		mtissuedspd		= 0;

	// MT滞留量
	private Integer		mtremained		= 0;

	// 帐号最大连接数
	private Integer		maxlinknum		= 1;

	// MT已转发量
	private Integer		mthavesnd		= 0;

	// 用户帐号
	private String		spaccountid;

	// Mo最低接收率告警阀值
	private Integer		morecvratio		= 0;

	// 帐号费用
	private Integer		userfee			= 0;

	// 创建时间
	private Timestamp	createtime;

	// RPT接收速度告警阀值
	private Integer		rptsndspd		= 0;

	// 发送级别
	private Integer		sendlevel;

	// 账号名称
	private String		accountname;

	// 采集被监控程序的频率，单位秒
	private Integer		monfreq			= 30;

	// Rpt滞留量告警阀值
	private Integer		rptremained		= 0;

	// MT提交速度
	private Integer		mtsndspd		= 0;
	
	// 主机编号
	private Long hostid;
	
	// 登录类型，0-未知；1－WEB登录；2－直接登录
	private Integer loginType = 0;
	
	// 账号离线阀值
	private Integer offlineThreshd = 0;

	public LfMonSspacinfo()
	{
	}

	public Integer getSpaccounttype()
	{

		return spaccounttype;
	}

	public void setSpaccounttype(Integer spaccounttype)
	{

		this.spaccounttype = spaccounttype;

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

	public Integer getMoremained()
	{

		return moremained;
	}

	public void setMoremained(Integer moremained)
	{

		this.moremained = moremained;

	}

	public Integer getRptrecvratio()
	{
		return rptrecvratio;
	}

	public void setRptrecvratio(Integer rptrecvratio)
	{
		this.rptrecvratio = rptrecvratio;
	}

	public String getMonphone()
	{

		return monphone;
	}

	public void setMonphone(String monphone)
	{

		this.monphone = monphone;

	}

	public Integer getMtissuedspd()
	{

		return mtissuedspd;
	}

	public void setMtissuedspd(Integer mtissuedspd)
	{

		this.mtissuedspd = mtissuedspd;

	}

	public Integer getMtremained()
	{

		return mtremained;
	}

	public void setMtremained(Integer mtremained)
	{

		this.mtremained = mtremained;

	}

	public Integer getMaxlinknum()
	{

		return maxlinknum;
	}

	public void setMaxlinknum(Integer maxlinknum)
	{

		this.maxlinknum = maxlinknum;

	}

	public Integer getMthavesnd()
	{

		return mthavesnd;
	}

	public void setMthavesnd(Integer mthavesnd)
	{

		this.mthavesnd = mthavesnd;

	}

	public String getSpaccountid()
	{

		return spaccountid;
	}

	public void setSpaccountid(String spaccountid)
	{

		this.spaccountid = spaccountid;

	}

	public Integer getMorecvratio()
	{

		return morecvratio;
	}

	public void setMorecvratio(Integer morecvratio)
	{

		this.morecvratio = morecvratio;

	}

	public Integer getUserfee()
	{

		return userfee;
	}

	public void setUserfee(Integer userfee)
	{

		this.userfee = userfee;

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

	public Integer getSendlevel()
	{

		return sendlevel;
	}

	public void setSendlevel(Integer sendlevel)
	{

		this.sendlevel = sendlevel;

	}

	public String getAccountname()
	{

		return accountname;
	}

	public void setAccountname(String accountname)
	{

		this.accountname = accountname;

	}

	public Integer getMonfreq()
	{

		return monfreq;
	}

	public void setMonfreq(Integer monfreq)
	{

		this.monfreq = monfreq;

	}

	public Integer getRptremained()
	{

		return rptremained;
	}

	public void setRptremained(Integer rptremained)
	{

		this.rptremained = rptremained;
	}

	public Integer getMtsndspd()
	{

		return mtsndspd;
	}

	public void setMtsndspd(Integer mtsndspd)
	{

		this.mtsndspd = mtsndspd;
	}

	public Long getHostid()
	{
		return hostid;
	}

	public void setHostid(Long hostid)
	{
		this.hostid = hostid;
	}

	public Integer getLoginType()
	{
		return loginType;
	}

	public void setLoginType(Integer loginType)
	{
		this.loginType = loginType;
	}

	public Integer getOfflineThreshd()
	{
		return offlineThreshd;
	}

	public void setOfflineThreshd(Integer offlineThreshd)
	{
		this.offlineThreshd = offlineThreshd;
	}

	public String getMonemail() {
		return monemail;
	}

	public void setMonemail(String monemail) {
		this.monemail = monemail;
	}
	
}
