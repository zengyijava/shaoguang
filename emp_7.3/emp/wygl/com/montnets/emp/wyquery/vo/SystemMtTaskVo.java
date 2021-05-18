package com.montnets.emp.wyquery.vo;

import java.sql.Timestamp;

/**
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-5 下午03:35:31
 * @description
 */

public class SystemMtTaskVo implements java.io.Serializable
{
	private static final long serialVersionUID = -1205801864795338598L;
    //主键
	private Long id;
    //用户id
	private String userid;
    //通道 号
	private String spgate;

	private String cpno;
    //手机事情
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
    //分条
	private Long pknumber; 
	//分条总数
	private Long pktotal;    
    //接收时间 2012.05.05
	private Timestamp recvTime;
	//业务类型
    private String svrtype;
    //编码
    private Integer msgfmt;
    //操作员code
    private String p1;
    //企业编码
    private String corpcode;
    //任务批次
    private Long taskid;
    //通道名称
    private String gateName;
    //状态
    private String reportStatus;
    //号码类型
    private String numberStyle;
    
    //登录用户ID
    private String lguserid;
    
    
    public String getLguserid()
	{
		return lguserid;
	}
	public void setLguserid(String lguserid)
	{
		this.lguserid = lguserid;
	}
	public String getGateName()
	{
		return gateName;
	}
	public void setGateName(String gateName)
	{
		this.gateName = gateName;
	}
	public String getReportStatus()
	{
		return reportStatus;
	}
	public void setReportStatus(String reportStatus)
	{
		this.reportStatus = reportStatus;
	}
	public String getNumberStyle()
	{
		return numberStyle;
	}
	public void setNumberStyle(String numberStyle)
	{
		this.numberStyle = numberStyle;
	}
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
	
	public Integer getMsgfmt() {
		return msgfmt;
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
	public void setSvrtype(String svrtype) {
		this.svrtype = svrtype;
	}
	public void setMsgfmt(Integer msgfmt) {
		this.msgfmt = msgfmt;
	}
	public String getSvrtype(){
		return svrtype;
	}
	//add end    
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
