/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-10 下午05:54:58
 */
package com.montnets.emp.monitor.constant;

import java.sql.Timestamp;

/**
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-10 下午05:54:58
 */

public class MonErrorParams
{


	//事件处理状态
	private Integer dealflag;

	//事件类型
	private Integer evttype=0;

	//事件抛出者
	private String who;

	//事件处理状态
	private Timestamp evttime;

	//事件类型 0：不监 1：监控
	private Integer monstatus;

	//程序ID
	private Long proceid;

	//事件内容
	private String msg;

	//接收时间
	private Timestamp rcvtime;

	//产生事件的主机ID
	private Long hostid;

	//自增ID
	private Long id;

	//事件ID
	private Integer evtid;

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

	//主机名称
	private String hostname;
	
	//程序名称
	private String procename;


	public String getHostname()
	{
		return hostname;
	}

	public void setHostname(String hostname)
	{
		this.hostname = hostname;
	}

	public String getProcename()
	{
		return procename;
	}

	public void setProcename(String procename)
	{
		this.procename = procename;
	}

	public MonErrorParams(){
	} 

	public Integer getDealflag(){

		return dealflag;
	}

	public void setDealflag(Integer dealflag){

		this.dealflag= dealflag;

	}

	public Integer getEvttype(){

		return evttype;
	}

	public void setEvttype(Integer evttype){

		this.evttype= evttype;

	}

	public String getWho(){

		return who;
	}

	public void setWho(String who){

		this.who= who;

	}

	public Timestamp getEvttime(){

		return evttime;
	}

	public void setEvttime(Timestamp evttime){

		this.evttime= evttime;

	}

	public Integer getMonstatus(){

		return monstatus;
	}

	public void setMonstatus(Integer monstatus){

		this.monstatus= monstatus;

	}

	public Long getProceid(){

		return proceid;
	}

	public void setProceid(Long proceid){

		this.proceid= proceid;

	}

	public String getMsg(){

		return msg;
	}

	public void setMsg(String msg){

		this.msg= msg;

	}

	public Timestamp getRcvtime(){

		return rcvtime;
	}

	public void setRcvtime(Timestamp rcvtime){

		this.rcvtime= rcvtime;

	}

	public Long getHostid(){

		return hostid;
	}

	public void setHostid(Long hostid){

		this.hostid= hostid;

	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Integer getEvtid(){

		return evtid;
	}

	public void setEvtid(Integer evtid){

		this.evtid= evtid;

	}

	public Integer getApptype(){

		return apptype;
	}

	public void setApptype(Integer apptype){

		this.apptype= apptype;

	}

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

}
