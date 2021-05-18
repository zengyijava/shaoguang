/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-31 上午10:19:27
 */
package com.montnets.emp.entity.monitoronline;

import java.sql.Timestamp;

/**
 * @description
 * @project p_comm
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-31 上午10:19:27
 */

public class LfMonOnlcfg
{
	// 最大在线用户数
	private Long		maxonline;

	// 自增ID
	private Long		id;

	// 刷新时间
	private Long		monfreq;

	// 在线用户数
	private Long		onlinenum;

	// 告警手机号码
	private String		monphone;

	// 设置时间
	private Timestamp	modifytime;

	// 告警级别
	private Integer		evttype;
	
	//监控状态：0：未监控1：监控
	private Integer monstatus;
	
	//服务器节点
	private String servernum;
	
	//邮箱地址
	private String monemail;
	
	//监控标识
	private String monThresholdFlag;
	
	//邮件告警标识
	private String sendmailFlag;

	
		public Integer getMonstatus(){

		return monstatus;
	}

	public void setMonstatus(Integer monstatus){

		this.monstatus= monstatus;

	}


	public LfMonOnlcfg()
	{
	}

	public Long getMaxonline()
	{

		return maxonline;
	}

	public void setMaxonline(Long maxonline)
	{

		this.maxonline = maxonline;

	}

	public Long getId()
	{

		return id;
	}

	public void setId(Long id)
	{

		this.id = id;

	}

	public Long getMonfreq()
	{

		return monfreq;
	}

	public void setMonfreq(Long monfreq)
	{

		this.monfreq = monfreq;

	}

	public Long getOnlinenum()
	{

		return onlinenum;
	}

	public void setOnlinenum(Long onlinenum)
	{

		this.onlinenum = onlinenum;

	}

	public String getMonphone()
	{

		return monphone;
	}

	public void setMonphone(String monphone)
	{

		this.monphone = monphone;
	}

	public Timestamp getModifytime()
	{
		return modifytime;
	}

	public void setModifytime(Timestamp modifytime)
	{
		this.modifytime = modifytime;
	}

	public Integer getEvttype()
	{
		return evttype;
	}

	public void setEvttype(Integer evttype)
	{
		this.evttype = evttype;
	}

	public String getMonThresholdFlag()
	{
		return monThresholdFlag;
	}

	public void setMonThresholdFlag(String monThresholdFlag)
	{
		this.monThresholdFlag = monThresholdFlag;
	}

	public String getServernum()
	{
		return servernum;
	}

	public void setServernum(String servernum)
	{
		this.servernum = servernum;
	}

	public String getMonemail()
	{
		return monemail;
	}

	public void setMonemail(String monemail)
	{
		this.monemail = monemail;
	}

	public String getSendmailFlag()
	{
		return sendmailFlag;
	}

	public void setSendmailFlag(String sendmailFlag)
	{
		this.sendmailFlag = sendmailFlag;
	}
	
}
