package com.montnets.emp.appmage.vo;

import java.sql.Timestamp;

public class MttaskSelectVo implements java.io.Serializable
{
	/**
	 * 发送任务vo
	 */
	private static final long serialVersionUID = 8034040440056368626L;
	//操作员ID
	private Long userid;
	//操作员名称
	private String username;
	//操作员状态（0为禁用，1为启用，2为注销）
	private Integer userstate;
	//用户ids  查询条件
	private String userids;
	
	//内容类型   消息的类型（0：文本消息；1：图片消息；2：视频；3：语音；4：事件推送； 
	private Integer msgtype;

	private Long faicount;

	private Long subcount;

	private String msg;
	//开始发送时间
	private Timestamp bigintime;
	//结束发送时间
	private Timestamp endtime;
	
	//开始发送时间 查询条件
	private String bigintimestr;
	//结束发送时间 查询条件
	private String endtimestr;

	private Integer sendstate;

	private String msgurl;

	private Long readcount;

	private Long id;

	private Long taskid;

	private Long succount;

	private Long unreadcount;

	private String title;

	private String appacount;
	
	private String corpcode;

	public MttaskSelectVo(){
	} 
	
	public String getCorpcode()
	{
		return corpcode;
	}

	public void setCorpcode(String corpcode)
	{
		this.corpcode = corpcode;
	}

	public String getBigintimestr()
	{
		return bigintimestr;
	}

	public void setBigintimestr(String bigintimestr)
	{
		this.bigintimestr = bigintimestr;
	}


	public String getEndtimestr()
	{
		return endtimestr;
	}


	public void setEndtimestr(String endtimestr)
	{
		this.endtimestr = endtimestr;
	}


	public String getUserids()
	{
		return userids;
	}

	public void setUserids(String userids)
	{
		this.userids = userids;
	}

	public Integer getUserstate()
	{
		return userstate;
	}

	public void setUserstate(Integer userstate)
	{
		this.userstate = userstate;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public Long getUserid(){

		return userid;
	}

	public void setUserid(Long userid){

		this.userid= userid;

	}

	public Integer getMsgtype(){

		return msgtype;
	}

	public void setMsgtype(Integer msgtype){

		this.msgtype= msgtype;

	}

	public Long getFaicount(){

		return faicount;
	}

	public void setFaicount(Long faicount){

		this.faicount= faicount;

	}

	public Long getSubcount(){

		return subcount;
	}

	public void setSubcount(Long subcount){

		this.subcount= subcount;

	}

	public String getMsg(){

		return msg;
	}

	public void setMsg(String msg){

		this.msg= msg;

	}

	public Timestamp getBigintime(){

		return bigintime;
	}

	public void setBigintime(Timestamp bigintime){

		this.bigintime= bigintime;

	}

	public Integer getSendstate(){

		return sendstate;
	}

	public void setSendstate(Integer sendstate){

		this.sendstate= sendstate;

	}

	public String getMsgurl(){

		return msgurl;
	}

	public void setMsgurl(String msgurl){

		this.msgurl= msgurl;

	}

	public Long getReadcount(){

		return readcount;
	}

	public void setReadcount(Long readcount){

		this.readcount= readcount;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Long getTaskid(){

		return taskid;
	}

	public void setTaskid(Long taskid){

		this.taskid= taskid;

	}

	public Timestamp getEndtime(){

		return endtime;
	}

	public void setEndtime(Timestamp endtime){

		this.endtime= endtime;

	}

	public Long getSuccount(){

		return succount;
	}

	public void setSuccount(Long succount){

		this.succount= succount;

	}

	public Long getUnreadcount(){

		return unreadcount;
	}

	public void setUnreadcount(Long unreadcount){

		this.unreadcount= unreadcount;

	}

	public String getTitle(){

		return title;
	}

	public void setTitle(String title){

		this.title= title;

	}

	public String getAppacount(){

		return appacount;
	}

	public void setAppacount(String appacount){

		this.appacount= appacount;

	}


		
}
