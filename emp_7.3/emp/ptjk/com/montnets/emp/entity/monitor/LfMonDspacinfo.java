package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * SP账号动态信息表实体类
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 下午01:54:51
 */
public class LfMonDspacinfo
{

	// MT已转发量
	private Integer		mthavesnd;

	// 帐号登录IP IP="登录使用的IP地址信息(字符串),如：192.169.1.130"
	private String		loginip;

	// SP帐号，主键。与LF_MON_SSPACINFO表外键关联
	private String		spaccountid;

	// 账号余额
	private Integer		userfee;

	// MO接收总量
	private Integer		mototalrecv;

	// RPT接收速度(整型)
	private Integer		rptsndspd;

	private Long		id;

	// Rpt接收总量
	private Integer		rpttotalrecv;

	// Rpt滞留量
	private Integer		rptremained;

	// 最后一次登录时间
	private Timestamp	loginintm;

	// 更新时间
	private Timestamp	updatetime;

	// MO滞留量
	private Integer		moremained;

	// MO转发速度
	private Integer		mosndspd;

	// 付费类型 1：预付 2：后付
	private Integer		feeflag;

	// MT下发速度
	private Integer		mtissuedspd;

	// MT滞留量
	private Integer		mtremained;

	// 最后一次离线时间
	private Timestamp	loginouttm;

	// MO转发量
	private Integer		mohavesnd;

	// 0：在线 1：离线STATUS="用户的状态(整型 0:在线 1:离线 其他暂视为未知)"
	private Integer		onlinestatus;

	// 发送级别
	private Integer		sendlevel;

	// 帐号连接数 LINKNUM="连接数（整型）"
	private Integer		linknum;

	// MT提交速度
	private Integer		mtsndspd;

	// 主机编号
	private Long		hostId;

	// Rpt转发量
	private Integer		rptHaveSnd;

	// MT提交总量
	private Integer		mtTotalSnd;

	// 告警级别
	private Integer		evtType;
	
	// 账号类型
	private Integer		spAccountType;
	
	// 账号名称
	private String		accountName;
	
	//新增字段
	
	private Long thresholdflag1;
	
	private Long thresholdflag2;
	
	private Long thresholdflag3;
	
	private Long thresholdflag4;
	
	private Long thresholdflag5;
	
	private Long thresholdflag6;
	
	private Long thresholdflag7;
	
	private Long thresholdflag8;
	
	private Long thresholdflag9;
	
	private Long thresholdflag10;
	
	private Long thresholdflag11;
	
	private Long thresholdflag12;
	
	private Long thresholdflag13;
	
	private Long sendmailflag1;
	private Long sendmailflag2;
	private Long sendmailflag3;
	private Long sendmailflag4;
	private Long sendmailflag5;
	private Long sendmailflag6;
	private Long sendmailflag7;
	private Long sendmailflag8;
	private Long sendmailflag9;
	private Long sendmailflag10;
	private Long sendmailflag11;
	private Long sendmailflag12;
	private Long sendmailflag13;
	

    private String serverNum;

	// 离线时间
	private Long	offlineDuration;

	// 未提交数据时间
	private Long	noMtHaveSnd;
	
	// 登录类型，0-未知；1－WEB登录；2－直接登录
	private Integer loginType;
	
	//在线状态
	private String onlinestatusStr;
	
	//网关编号
	private Long gatewayid;
	
	//提交状态
	private String submitStatusStr;
	
	//数据库当前入库时间
	private Timestamp dbservtime;
	
	public Integer getMthavesnd() {
		return mthavesnd;
	}

	public void setMthavesnd(Integer mthavesnd) {
		this.mthavesnd = mthavesnd;
	}

	public String getLoginip() {
		return loginip;
	}

	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}

	public String getSpaccountid() {
		return spaccountid;
	}

	public void setSpaccountid(String spaccountid) {
		this.spaccountid = spaccountid;
	}

	public Integer getUserfee() {
		return userfee;
	}

	public void setUserfee(Integer userfee) {
		this.userfee = userfee;
	}

	public Integer getMototalrecv() {
		return mototalrecv;
	}

	public void setMototalrecv(Integer mototalrecv) {
		this.mototalrecv = mototalrecv;
	}

	public Integer getRptsndspd() {
		return rptsndspd;
	}

	public void setRptsndspd(Integer rptsndspd) {
		this.rptsndspd = rptsndspd;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRpttotalrecv() {
		return rpttotalrecv;
	}

	public void setRpttotalrecv(Integer rpttotalrecv) {
		this.rpttotalrecv = rpttotalrecv;
	}

	public Integer getRptremained() {
		return rptremained;
	}

	public void setRptremained(Integer rptremained) {
		this.rptremained = rptremained;
	}

	public Timestamp getLoginintm() {
		return loginintm;
	}

	public void setLoginintm(Timestamp loginintm) {
		this.loginintm = loginintm;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getMoremained() {
		return moremained;
	}

	public void setMoremained(Integer moremained) {
		this.moremained = moremained;
	}

	public Integer getMosndspd() {
		return mosndspd;
	}

	public void setMosndspd(Integer mosndspd) {
		this.mosndspd = mosndspd;
	}

	public Integer getFeeflag() {
		return feeflag;
	}

	public void setFeeflag(Integer feeflag) {
		this.feeflag = feeflag;
	}

	public Integer getMtissuedspd() {
		return mtissuedspd;
	}

	public void setMtissuedspd(Integer mtissuedspd) {
		this.mtissuedspd = mtissuedspd;
	}

	public Integer getMtremained() {
		return mtremained;
	}

	public void setMtremained(Integer mtremained) {
		this.mtremained = mtremained;
	}

	public Timestamp getLoginouttm() {
		return loginouttm;
	}

	public void setLoginouttm(Timestamp loginouttm) {
		this.loginouttm = loginouttm;
	}

	public Integer getMohavesnd() {
		return mohavesnd;
	}

	public void setMohavesnd(Integer mohavesnd) {
		this.mohavesnd = mohavesnd;
	}

	public Integer getOnlinestatus() {
		return onlinestatus;
	}

	public void setOnlinestatus(Integer onlinestatus) {
		this.onlinestatus = onlinestatus;
	}

	public Integer getSendlevel() {
		return sendlevel;
	}

	public void setSendlevel(Integer sendlevel) {
		this.sendlevel = sendlevel;
	}

	public Integer getLinknum() {
		return linknum;
	}

	public void setLinknum(Integer linknum) {
		this.linknum = linknum;
	}

	public Integer getMtsndspd() {
		return mtsndspd;
	}

	public void setMtsndspd(Integer mtsndspd) {
		this.mtsndspd = mtsndspd;
	}

	public Long getHostId() {
		return hostId;
	}

	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}

	public Integer getRptHaveSnd() {
		return rptHaveSnd;
	}

	public void setRptHaveSnd(Integer rptHaveSnd) {
		this.rptHaveSnd = rptHaveSnd;
	}

	public Integer getMtTotalSnd() {
		return mtTotalSnd;
	}

	public void setMtTotalSnd(Integer mtTotalSnd) {
		this.mtTotalSnd = mtTotalSnd;
	}

	public Integer getEvtType() {
		return evtType;
	}

	public void setEvtType(Integer evtType) {
		this.evtType = evtType;
	}

	public Integer getSpAccountType() {
		return spAccountType;
	}

	public void setSpAccountType(Integer spAccountType) {
		this.spAccountType = spAccountType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}


	public Long getThresholdflag1()
	{
		return thresholdflag1;
	}

	public void setThresholdflag1(Long thresholdflag1)
	{
		this.thresholdflag1 = thresholdflag1;
	}

	public Long getThresholdflag2()
	{
		return thresholdflag2;
	}

	public void setThresholdflag2(Long thresholdflag2)
	{
		this.thresholdflag2 = thresholdflag2;
	}

	public Long getThresholdflag3()
	{
		return thresholdflag3;
	}

	public void setThresholdflag3(Long thresholdflag3)
	{
		this.thresholdflag3 = thresholdflag3;
	}

	public Long getThresholdflag4()
	{
		return thresholdflag4;
	}

	public void setThresholdflag4(Long thresholdflag4)
	{
		this.thresholdflag4 = thresholdflag4;
	}

	public Long getThresholdflag5()
	{
		return thresholdflag5;
	}

	public void setThresholdflag5(Long thresholdflag5)
	{
		this.thresholdflag5 = thresholdflag5;
	}

	public Long getThresholdflag6()
	{
		return thresholdflag6;
	}

	public void setThresholdflag6(Long thresholdflag6)
	{
		this.thresholdflag6 = thresholdflag6;
	}

	public Long getThresholdflag7()
	{
		return thresholdflag7;
	}

	public void setThresholdflag7(Long thresholdflag7)
	{
		this.thresholdflag7 = thresholdflag7;
	}

	public Long getThresholdflag8()
	{
		return thresholdflag8;
	}

	public void setThresholdflag8(Long thresholdflag8)
	{
		this.thresholdflag8 = thresholdflag8;
	}

	public Long getThresholdflag9()
	{
		return thresholdflag9;
	}

	public void setThresholdflag9(Long thresholdflag9)
	{
		this.thresholdflag9 = thresholdflag9;
	}

	public Long getThresholdflag10()
	{
		return thresholdflag10;
	}

	public void setThresholdflag10(Long thresholdflag10)
	{
		this.thresholdflag10 = thresholdflag10;
	}

	public Long getThresholdflag11()
	{
		return thresholdflag11;
	}

	public void setThresholdflag11(Long thresholdflag11)
	{
		this.thresholdflag11 = thresholdflag11;
	}

	public Long getThresholdflag12()
	{
		return thresholdflag12;
	}

	public void setThresholdflag12(Long thresholdflag12)
	{
		this.thresholdflag12 = thresholdflag12;
	}

	public String getServerNum()
	{
		return serverNum;
	}

	public void setServerNum(String serverNum)
	{
		this.serverNum = serverNum;
	}

	public Long getOfflineDuration()
	{
		return offlineDuration;
	}

	public void setOfflineDuration(Long offlineDuration)
	{
		this.offlineDuration = offlineDuration;
	}


	public Long getNoMtHaveSnd()
	{
		return noMtHaveSnd;
	}

	public void setNoMtHaveSnd(Long noMtHaveSnd)
	{
		this.noMtHaveSnd = noMtHaveSnd;
	}

	public Integer getLoginType()
	{
		return loginType;
	}

	public void setLoginType(Integer loginType)
	{
		this.loginType = loginType;
	}

	public String getOnlinestatusStr()
	{
		return onlinestatusStr;
	}

	public void setOnlinestatusStr(String onlinestatusStr)
	{
		this.onlinestatusStr = onlinestatusStr;
	}

	public String getSubmitStatusStr()
	{
		return submitStatusStr;
	}

	public void setSubmitStatusStr(String submitStatusStr)
	{
		this.submitStatusStr = submitStatusStr;
	}

	public Long getThresholdflag13()
	{
		return thresholdflag13;
	}

	public void setThresholdflag13(Long thresholdflag13)
	{
		this.thresholdflag13 = thresholdflag13;
	}

	public Long getSendmailflag1() {
		return sendmailflag1;
	}

	public void setSendmailflag1(Long sendmailflag1) {
		this.sendmailflag1 = sendmailflag1;
	}

	public Long getSendmailflag2() {
		return sendmailflag2;
	}

	public void setSendmailflag2(Long sendmailflag2) {
		this.sendmailflag2 = sendmailflag2;
	}

	public Long getSendmailflag3() {
		return sendmailflag3;
	}

	public void setSendmailflag3(Long sendmailflag3) {
		this.sendmailflag3 = sendmailflag3;
	}

	public Long getSendmailflag4() {
		return sendmailflag4;
	}

	public void setSendmailflag4(Long sendmailflag4) {
		this.sendmailflag4 = sendmailflag4;
	}

	public Long getSendmailflag5() {
		return sendmailflag5;
	}

	public void setSendmailflag5(Long sendmailflag5) {
		this.sendmailflag5 = sendmailflag5;
	}

	public Long getSendmailflag6() {
		return sendmailflag6;
	}

	public void setSendmailflag6(Long sendmailflag6) {
		this.sendmailflag6 = sendmailflag6;
	}

	public Long getSendmailflag7() {
		return sendmailflag7;
	}

	public void setSendmailflag7(Long sendmailflag7) {
		this.sendmailflag7 = sendmailflag7;
	}

	public Long getSendmailflag8() {
		return sendmailflag8;
	}

	public void setSendmailflag8(Long sendmailflag8) {
		this.sendmailflag8 = sendmailflag8;
	}

	public Long getSendmailflag9() {
		return sendmailflag9;
	}

	public void setSendmailflag9(Long sendmailflag9) {
		this.sendmailflag9 = sendmailflag9;
	}

	public Long getSendmailflag10() {
		return sendmailflag10;
	}

	public void setSendmailflag10(Long sendmailflag10) {
		this.sendmailflag10 = sendmailflag10;
	}

	public Long getSendmailflag11() {
		return sendmailflag11;
	}

	public void setSendmailflag11(Long sendmailflag11) {
		this.sendmailflag11 = sendmailflag11;
	}

	public Long getSendmailflag12() {
		return sendmailflag12;
	}

	public void setSendmailflag12(Long sendmailflag12) {
		this.sendmailflag12 = sendmailflag12;
	}

	public Long getSendmailflag13() {
		return sendmailflag13;
	}

	public void setSendmailflag13(Long sendmailflag13) {
		this.sendmailflag13 = sendmailflag13;
	}

	public Long getGatewayid()
	{
		return gatewayid;
	}

	public void setGatewayid(Long gatewayid)
	{
		this.gatewayid = gatewayid;
	}

	public Timestamp getDbservtime()
	{
		return dbservtime;
	}

	public void setDbservtime(Timestamp dbservtime)
	{
		this.dbservtime = dbservtime;
	}

}
