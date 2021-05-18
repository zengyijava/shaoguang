package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * 通道账号动态信息实体类
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 下午01:47:14
 */
public class LfMonDgtacinfo
{
	// Rpt转发量
	private Integer		rpthavesnd;

	// MT已转发量
	private Integer		mthavesnd;

	// 帐号的接入IP地址 IP="登录使用的IP地址信息
	private String		loginip;

	// 帐号费用
	private Integer		userfee;

	// MO接收总量
	private Integer		mototalrecv;

	// Rpt转发速度
	private Integer		rptsndspd;

	// Rpt接收总量
	private Integer		rpttotalrecv;

	// Rpt滞留量
	private Integer		rptremained;

	// MT接收速度
	private Integer		mtrecvspd;

	// 最后一次登录时间
	private Timestamp	loginintm;

	// 更新时间
	private Timestamp	modifytime;

	// MO滞留量
	private Integer		moremained;

	// MO转发速度
	private Integer		mosndspd;

	// 付费类型 1：预付 2：后付
	private Integer		feeflag;

	// MT滞留量
	private Integer		mtremained;

	// 最后一次离线时间
	private Timestamp	loginouttm;

	// 通道帐号，主键。与LF_MON_SGTACINFO表外键关联
	private String		gateaccount;

	// MO转发量
	private Integer		mohavesnd;

	// 在线状态0：在线 1：离线
	private Integer		onlinestatus;

	// 帐号的连接数
	private Integer		linknum;

	private Long		id;

	// 主机编号
	private Long		hostId;

	// MT提交总量
	private Integer		mttotalsnd;

	// 网关编号
	private Long		gatewayid;

	// 告警级别
	private Integer		evtType;

	// 通道名称
	private String		gateName;
	
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
    
	//网关编号，与gatewayid一致
	private Long gwno;
	
    //数据库当前入库时间
    private Timestamp dbservtime;
    
	public Integer getRpthavesnd() {
		return rpthavesnd;
	}

	public void setRpthavesnd(Integer rpthavesnd) {
		this.rpthavesnd = rpthavesnd;
	}

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

	public Integer getMtrecvspd() {
		return mtrecvspd;
	}

	public void setMtrecvspd(Integer mtrecvspd) {
		this.mtrecvspd = mtrecvspd;
	}

	public Timestamp getLoginintm() {
		return loginintm;
	}

	public void setLoginintm(Timestamp loginintm) {
		this.loginintm = loginintm;
	}

	public Timestamp getModifytime() {
		return modifytime;
	}

	public void setModifytime(Timestamp modifytime) {
		this.modifytime = modifytime;
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

	public String getGateaccount() {
		return gateaccount;
	}

	public void setGateaccount(String gateaccount) {
		this.gateaccount = gateaccount;
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

	public Integer getLinknum() {
		return linknum;
	}

	public void setLinknum(Integer linknum) {
		this.linknum = linknum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHostId() {
		return hostId;
	}

	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}

	public Integer getMttotalsnd() {
		return mttotalsnd;
	}

	public void setMttotalsnd(Integer mttotalsnd) {
		this.mttotalsnd = mttotalsnd;
	}



	public Long getGatewayid()
	{
		return gatewayid;
	}

	public void setGatewayid(Long gatewayid)
	{
		this.gatewayid = gatewayid;
	}

	public Integer getEvtType() {
		return evtType;
	}

	public void setEvtType(Integer evtType) {
		this.evtType = evtType;
	}

	public String getGateName() {
		return gateName;
	}

	public void setGateName(String gateName) {
		this.gateName = gateName;
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

	public Long getThresholdflag13()
	{
		return thresholdflag13;
	}

	public void setThresholdflag13(Long thresholdflag13)
	{
		this.thresholdflag13 = thresholdflag13;
	}

	public String getServerNum()
	{
		return serverNum;
	}

	public void setServerNum(String serverNum)
	{
		this.serverNum = serverNum;
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

	public Long getGwno()
	{
		return gwno;
	}

	public void setGwno(Long gwno)
	{
		this.gwno = gwno;
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
