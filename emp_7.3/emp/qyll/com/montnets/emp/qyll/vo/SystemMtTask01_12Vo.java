package com.montnets.emp.qyll.vo;

import java.sql.Timestamp;

/**
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-5 下午03:27:03
 * @description 
 */

public class SystemMtTask01_12Vo implements java.io.Serializable
{
	private static final long serialVersionUID = 3042524334486917600L;
    //主键
	private Long id;
    //用户id
	private String userid;
    //通道号
	private String spgate;

	private String cpno;
    //手机号
	private String phone;
    //发送状态
	private Long sendstatus;
    //错误编码
	private String errorcode;
    //发送时间
	private Timestamp sendtime;
    //短信内容
	private String message;
	//运营商
	private Long unicom;
    //开始时间
	private String startTime;
    //结束时间
	private String endTime;
	//发送账号
    private String spusers;    
    //分条 2012.05.05
	private Long pknumber; 
	//分条总数
	private Long pktotal;
	//接收时间
	private Timestamp recvTime;
	//业务类型
    private String svrtype;
    //操作员code
    private String p1;
    //企业编码
    private String corpcode;
    //任务批次
    private Long taskid;
    
    public Long getTaskid()
	{
		return taskid;
	}
	public void setTaskid(Long taskid)
	{
		this.taskid = taskid;
	}
	public String getCorpcode() {
		return corpcode;
	}
	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}
	public String getP1()
    {
    	return p1;
    }
    public void setP1(String p1)
    {
    	this.p1 = p1;
    }
	
	public Long getPknumber() {
		return pknumber;
	}

	public void setPknumber(Long pknumber) {
		this.pknumber = pknumber;
	}

	public Long getPktotal() {
		return pktotal;
	}

	public void setPktotal(Long pktotal) {
		this.pktotal = pktotal;
	}

	public Timestamp getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(Timestamp recvTime) {
		this.recvTime = recvTime;
	}

	public String getSvrtype() {
		return svrtype;
	}

	public void setSvrtype(String svrtype) {
		this.svrtype = svrtype;
	}


	public String getSpusers() {
		return spusers;
	}

	public void setSpusers(String spusers) {
		this.spusers = spusers;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getUserid()
	{
		return userid;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	public String getSpgate()
	{
		return spgate;
	}

	public void setSpgate(String spgate)
	{
		this.spgate = spgate;
	}

	public String getCpno()
	{
		return cpno;
	}

	public void setCpno(String cpno)
	{
		this.cpno = cpno;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public Long getSendstatus()
	{
		return sendstatus;
	}

	public void setSendstatus(Long sendstatus)
	{
		this.sendstatus = sendstatus;
	}

	public String getErrorcode()
	{
		return errorcode;
	}

	public void setErrorcode(String errorcode)
	{
		this.errorcode = errorcode;
	}

	public Timestamp getSendtime()
	{
		return sendtime;
	}

	public void setSendtime(Timestamp sendtime)
	{
		this.sendtime = sendtime;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public Long getUnicom()
	{
		return unicom;
	}

	public void setUnicom(Long unicom)
	{
		this.unicom = unicom;
	}	
}
