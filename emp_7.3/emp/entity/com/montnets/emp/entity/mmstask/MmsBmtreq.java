package com.montnets.emp.entity.mmstask;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * TableMmsDatareport对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:28:15
 * @description 
 */
public class MmsBmtreq implements Serializable{

 	/**
	 * 
	 */
	private static final long serialVersionUID = 4780180968578312984L;
	//自增ID
	private Long id;
	//平台唯一流水号	
	private Long msgid=0L;
	//批量文件处理流水号
	private Long bmtmsgid=0L;
	//用户账号
	private String userid=" ";
	//代理账号
	private String loginid=" ";
	//用户UID
	private Integer useruid=0;
	//业务类型
	private String servicetype=" ";
	//信息类型 0：短信 	10：普通彩信（通过直接发送接口发送的彩信） 	11：静态模板彩信（彩信资源是固定的）12：动态模板彩信（模板中有参数）
	private Integer msgtype;
	//任务ID
	private Integer taskid;
	//标题
	private String title=" ";
	//短信内容
	private String msg=" ";	
	//消息编码格式
	private Integer msgfmt=15;
	//文件远程地址
	private String remoteurl=" ";
	//文件本地地址
	private String localpath=" ";
	//发送级别
	private Integer sendlevel=5;
	//定时发送(由时间转换为大整型数如：20120806185600)
	private String attime=" ";
	//短信有效期(由时间转换为大整型数如：20120806185600)
	private String validtime=" ";
	//发送状态
	private Integer sendstatus;
	//错误代码(状态报告)
	private String errorcode=" ";
	//接收时间
	private Timestamp recvtime=new Timestamp(System.currentTimeMillis());
	//发送时间
	private Timestamp sendtime=new Timestamp(System.currentTimeMillis());
	//发送类型
	private Integer sendtype=1;
	//定时是否处理标志
	private Integer attimeflag=0;
	//下行源地址，也就通道号或扩展子号
	private String sa=" ";
	//服务类型
	private String svrtype=" ";
	//自定义参数1
	private String p1=" ";
	//自定义参数2
	private String p2=" ";
	//自定义参数3
	private String p3=" ";
	//自定义参数4
	private String p4=" ";
	//用户消息ID
	private Long usermsgid=0L;
	//模块ID
	private Integer moduleid;
	//是否需要状态报告标识
	private Integer retflag = 0;
	//模板id
	private Long templid;
	
  
	public MmsBmtreq(){
	}


	public Long getTemplid() {
		return templid;
	}


	public void setTemplid(Long templid) {
		this.templid = templid;
	}


	public Integer getMsgtype() {
		return msgtype;
	}


	public void setMsgtype(Integer msgtype) {
		this.msgtype = msgtype;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getMsgid() {
		return msgid;
	}


	public void setMsgid(Long msgid) {
		this.msgid = msgid;
	}


	public Long getBmtmsgid() {
		return bmtmsgid;
	}


	public void setBmtmsgid(Long bmtmsgid) {
		this.bmtmsgid = bmtmsgid;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	public String getLoginid() {
		return loginid;
	}


	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}


	public Integer getUseruid() {
		return useruid;
	}


	public void setUseruid(Integer useruid) {
		this.useruid = useruid;
	}


	public String getServicetype() {
		return servicetype;
	}


	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}


	public Integer getTaskid() {
		return taskid;
	}


	public void setTaskid(Integer taskid) {
		this.taskid = taskid;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public Integer getMsgfmt() {
		return msgfmt;
	}


	public void setMsgfmt(Integer msgfmt) {
		this.msgfmt = msgfmt;
	}


	public String getRemoteurl() {
		return remoteurl;
	}


	public void setRemoteurl(String remoteurl) {
		this.remoteurl = remoteurl;
	}


	public String getLocalpath() {
		return localpath;
	}


	public void setLocalpath(String localpath) {
		this.localpath = localpath;
	}


	public Integer getSendlevel() {
		return sendlevel;
	}


	public void setSendlevel(Integer sendlevel) {
		this.sendlevel = sendlevel;
	}


	public String getAttime() {
		return attime;
	}


	public void setAttime(String attime) {
		this.attime = attime;
	}


	public String getValidtime() {
		return validtime;
	}


	public void setValidtime(String validtime) {
		this.validtime = validtime;
	}


	public Integer getSendstatus() {
		return sendstatus;
	}


	public void setSendstatus(Integer sendstatus) {
		this.sendstatus = sendstatus;
	}


	public String getErrorcode() {
		return errorcode;
	}


	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}


	public Timestamp getRecvtime() {
		return recvtime;
	}


	public void setRecvtime(Timestamp recvtime) {
		this.recvtime = recvtime;
	}


	public Timestamp getSendtime() {
		return sendtime;
	}


	public void setSendtime(Timestamp sendtime) {
		this.sendtime = sendtime;
	}


	public Integer getSendtype() {
		return sendtype;
	}


	public void setSendtype(Integer sendtype) {
		this.sendtype = sendtype;
	}


	public Integer getAttimeflag() {
		return attimeflag;
	}


	public void setAttimeflag(Integer attimeflag) {
		this.attimeflag = attimeflag;
	}


	public String getSa() {
		return sa;
	}


	public void setSa(String sa) {
		this.sa = sa;
	}


	public String getSvrtype() {
		return svrtype;
	}


	public void setSvrtype(String svrtype) {
		this.svrtype = svrtype;
	}


	public String getP1() {
		return p1;
	}


	public void setP1(String p1) {
		this.p1 = p1;
	}


	public String getP2() {
		return p2;
	}


	public void setP2(String p2) {
		this.p2 = p2;
	}


	public String getP3() {
		return p3;
	}


	public void setP3(String p3) {
		this.p3 = p3;
	}


	public String getP4() {
		return p4;
	}


	public void setP4(String p4) {
		this.p4 = p4;
	}


	

	

	public Long getUsermsgid()
	{
		return usermsgid;
	}


	public void setUsermsgid(Long usermsgid)
	{
		this.usermsgid = usermsgid;
	}


	public Integer getModuleid()
	{
		return moduleid;
	}


	public void setModuleid(Integer moduleid)
	{
		this.moduleid = moduleid;
	}


	public Integer getRetflag() {
		return retflag;
	}


	public void setRetflag(Integer retflag) {
		this.retflag = retflag;
	}

	
	
}
