							
package com.montnets.emp.appwg.entity;

import java.sql.Timestamp;

public class LfAppMoCache
{

	private String packetid;

	private String tojid;

	private Integer totype;

	private Integer readstate;

	private String fname;

	private Integer msgtype;

	private String ecode;

	private Long appid;

	private Timestamp createtime;

	private Long msgid;

	private Integer emptype;

	private Integer sendstate;

	private String fromjid;

	private Long validity;

	private String body;

	private String tname;

	private Integer sendedcount;

	private Long id;

	private String serial;

	private Integer msgsrc;
	
	//标记为离线消息。0在线消息；1离线消息
	private Integer outlinemsg;

	public LfAppMoCache(){
	} 

	public Integer getOutlinemsg()
	{
		return outlinemsg;
	}

	public void setOutlinemsg(Integer outlinemsg)
	{
		this.outlinemsg = outlinemsg;
	}

	public String getPacketid(){

		return packetid;
	}

	public void setPacketid(String packetid){

		this.packetid= packetid;

	}

	public String getTojid(){

		return tojid;
	}

	public void setTojid(String tojid){

		this.tojid= tojid;

	}

	public Integer getTotype(){

		return totype;
	}

	public void setTotype(Integer totype){

		this.totype= totype;

	}

	public Integer getReadstate(){

		return readstate;
	}

	public void setReadstate(Integer readstate){

		this.readstate= readstate;

	}

	public String getFname(){

		return fname;
	}

	public void setFname(String fname){

		this.fname= fname;

	}

	public Integer getMsgtype(){

		return msgtype;
	}

	public void setMsgtype(Integer msgtype){

		this.msgtype= msgtype;

	}

	public String getEcode(){

		return ecode;
	}

	public void setEcode(String ecode){

		this.ecode= ecode;

	}

	public Long getAppid(){

		return appid;
	}

	public void setAppid(Long appid){

		this.appid= appid;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public Long getMsgid(){

		return msgid;
	}

	public void setMsgid(Long msgid){

		this.msgid= msgid;

	}

	public Integer getEmptype(){

		return emptype;
	}

	public void setEmptype(Integer emptype){

		this.emptype= emptype;

	}

	public Integer getSendstate(){

		return sendstate;
	}

	public void setSendstate(Integer sendstate){

		this.sendstate= sendstate;

	}

	public String getFromjid(){

		return fromjid;
	}

	public void setFromjid(String fromjid){

		this.fromjid= fromjid;

	}

	public Long getValidity(){

		return validity;
	}

	public void setValidity(Long validity){

		this.validity= validity;

	}

	public String getBody(){

		return body;
	}

	public void setBody(String body){

		this.body= body;

	}

	public String getTname(){

		return tname;
	}

	public void setTname(String tname){

		this.tname= tname;

	}

	public Integer getSendedcount(){

		return sendedcount;
	}

	public void setSendedcount(Integer sendedcount){

		this.sendedcount= sendedcount;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public String getSerial(){

		return serial;
	}

	public void setSerial(String serial){

		this.serial= serial;

	}

	public Integer getMsgsrc(){

		return msgsrc;
	}

	public void setMsgsrc(Integer msgsrc){

		this.msgsrc= msgsrc;

	}

}

							