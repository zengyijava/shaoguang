					
package com.montnets.emp.entity.appmage;

import java.sql.Timestamp;

public class LfAppMtmsg
{

	private String msgid;

	private Integer msgtype;

	private Timestamp createtime = new Timestamp(System.currentTimeMillis());

	private Long userid;

	private String corpcode;

	private String title;

	private String msgtext;

	private String content;

	private Long taskid;

	private String tousername;

	private String msgxml;

	private Long id;

	private String appacount;

	private Long parentid;

	private Integer sendstate;
	
	private String appMsgId;
	
	private String appUserAccount;
	
	private String rptState;
	
	private Integer sendMsgType;
	
	private Timestamp recRPTTime;

	public LfAppMtmsg(){
	} 

	
	public Timestamp getRecRPTTime()
	{
		return recRPTTime;
	}


	public void setRecRPTTime(Timestamp recRPTTime)
	{
		this.recRPTTime = recRPTTime;
	}


	public String getAppUserAccount()
	{
		return appUserAccount;
	}


	public void setAppUserAccount(String appUserAccount)
	{
		this.appUserAccount = appUserAccount;
	}


	public Integer getSendMsgType()
	{
		return sendMsgType;
	}


	public void setSendMsgType(Integer sendMsgType)
	{
		this.sendMsgType = sendMsgType;
	}


	public String getRptState()
	{
		return rptState;
	}


	public void setRptState(String rptState)
	{
		this.rptState = rptState;
	}


	public String getAppMsgId()
	{
		return appMsgId;
	}


	public void setAppMsgId(String appMsgId)
	{
		this.appMsgId = appMsgId;
	}


	public String getMsgid(){

		return msgid;
	}

	public void setMsgid(String msgid){

		this.msgid= msgid;

	}

	public Integer getMsgtype(){

		return msgtype;
	}

	public void setMsgtype(Integer msgtype){

		this.msgtype= msgtype;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public Long getUserid(){

		return userid;
	}

	public void setUserid(Long userid){

		this.userid= userid;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

	public String getTitle(){

		return title;
	}

	public void setTitle(String title){

		this.title= title;

	}

	public String getMsgtext(){

		return msgtext;
	}

	public void setMsgtext(String msgtext){

		this.msgtext= msgtext;

	}

	public String getContent(){

		return content;
	}

	public void setContent(String content){

		this.content= content;

	}

	public Long getTaskid(){

		return taskid;
	}

	public void setTaskid(Long taskid){

		this.taskid= taskid;

	}

	public String getTousername(){

		return tousername;
	}

	public void setTousername(String tousername){

		this.tousername= tousername;

	}

	public String getMsgxml(){

		return msgxml;
	}

	public void setMsgxml(String msgxml){

		this.msgxml= msgxml;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public String getAppacount(){

		return appacount;
	}

	public void setAppacount(String appacount){

		this.appacount= appacount;

	}

	public Long getParentid(){

		return parentid;
	}

	public void setParentid(Long parentid){

		this.parentid= parentid;

	}

	public Integer getSendstate(){

		return sendstate;
	}

	public void setSendstate(Integer sendstate){

		this.sendstate= sendstate;

	}

}

					