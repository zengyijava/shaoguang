package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * 告警历史表实体类
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 下午02:20:51
 */
public class LfMonErrhis
{

	//入库时间(处理时间)
	private Timestamp crttime;

	//程序ID
	private Long proceid;

	//监控状态
	private Integer monstatus;

	//主机ID
	private Long hostid;

	//接收时间
	private Timestamp rcvtime;

	//事件ID
	private Integer evtid;

	//事件类型
	private Integer evttype;

	//事件处理状态
	private Integer dealflag;

	//事件抛出者
	private String who;

	//自增ID
	private Long id;

	//事件时间
	private Timestamp evttime;

	//事件内容
	private String msg;

	//应用类型
	private Integer apptype;
	
	//sp账号
	private String spaccountid;
	
	//通道账号
	private String gateaccount;
	
	//处理描述
	private String dealdesc;
	
	//处理人
	private String dealpeople;

	//告警次数
	private Integer montimer;
	
	//SP未提交告警时间段和时长
	private String spOfflinePrd = " ";
	
	//程序节点
	private Long procenode;
	
	//WEB服务器节点
	private Long webnode;
	
	public String getSpaccountid()
	{
		return spaccountid;
	}

	public void setSpaccountid(String spaccountid)
	{
		this.spaccountid = spaccountid;
	}

	public String getGateaccount()
	{
		return gateaccount;
	}

	public void setGateaccount(String gateaccount)
	{
		this.gateaccount = gateaccount;
	}

	public String getDealdesc()
	{
		return dealdesc;
	}

	public void setDealdesc(String dealdesc)
	{
		this.dealdesc = dealdesc;
	}

	public String getDealpeople()
	{
		return dealpeople;
	}

	public void setDealpeople(String dealpeople)
	{
		this.dealpeople = dealpeople;
	}

	public LfMonErrhis(){
	} 

	public Timestamp getCrttime(){

		return crttime;
	}

	public void setCrttime(Timestamp crttime){

		this.crttime= crttime;

	}

	public Long getProceid(){

		return proceid;
	}

	public void setProceid(Long proceid){

		this.proceid= proceid;

	}

	public Integer getMonstatus(){

		return monstatus;
	}

	public void setMonstatus(Integer monstatus){

		this.monstatus= monstatus;

	}

	public Long getHostid(){

		return hostid;
	}

	public void setHostid(Long hostid){

		this.hostid= hostid;

	}

	public Timestamp getRcvtime(){

		return rcvtime;
	}

	public void setRcvtime(Timestamp rcvtime){

		this.rcvtime= rcvtime;

	}

	public Integer getEvtid(){

		return evtid;
	}

	public void setEvtid(Integer evtid){

		this.evtid= evtid;

	}

	public Integer getEvttype(){

		return evttype;
	}

	public void setEvttype(Integer evttype){

		this.evttype= evttype;

	}

	public Integer getDealflag(){

		return dealflag;
	}

	public void setDealflag(Integer dealflag){

		this.dealflag= dealflag;

	}

	public String getWho(){

		return who;
	}

	public void setWho(String who){

		this.who= who;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Timestamp getEvttime(){

		return evttime;
	}

	public void setEvttime(Timestamp evttime){

		this.evttime= evttime;

	}

	public String getMsg(){

		return msg;
	}

	public void setMsg(String msg){

		this.msg= msg;

	}

	public Integer getApptype()
	{
		return apptype;
	}

	public void setApptype(Integer apptype)
	{
		this.apptype = apptype;
	}

	public Integer getMontimer()
	{
		return montimer;
	}

	public void setMontimer(Integer montimer)
	{
		this.montimer = montimer;
	}

	public String getSpOfflinePrd()
	{
		return spOfflinePrd;
	}

	public void setSpOfflinePrd(String spOfflinePrd)
	{
		this.spOfflinePrd = spOfflinePrd;
	}

	public Long getProcenode()
	{
		return procenode;
	}

	public void setProcenode(Long procenode)
	{
		this.procenode = procenode;
	}

	public Long getWebnode()
	{
		return webnode;
	}

	public void setWebnode(Long webnode)
	{
		this.webnode = webnode;
	}




}
