/**
 * Program  : LfAppSitInfo.java
 * Author   : zousy
 * Create   : 2014-6-17 上午09:25:31
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.entity.appmage;

import java.sql.Timestamp;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-17 上午09:25:31
 */
public class LfAppSitInfo
{
	//首页名称
	private String name;

	private Timestamp moditytime;

	private Integer issystem;

	private Long typeid;

	private Timestamp createtime;

	private String corpcode;

	private String url;

	private Long sid;

	private Long userid;
	
	//发布时间
	private Timestamp publishtime;
	
	//0 草稿 1未发布 2 已发布
	private Integer status;
	
	//有效期，毫秒数
	private Long validity;
	
	private String serial;
	private String msgId;
	private String fromUser;
	private String toUser;
	private Timestamp sendTime;
	//空：未返；0:APP网关推送消息中心成功；1:APP网关推送消息中心失败；00:客户端接收成功；10:客户端接收失败；110:已接收,未显示；120:成功显示
	private Integer sendState;
	private Timestamp recTime;
	//下架时间
	private Timestamp cancelTime;

	public LfAppSitInfo(){
	} 

	public Timestamp getCancelTime()
	{
		return cancelTime;
	}

	public void setCancelTime(Timestamp cancelTime)
	{
		this.cancelTime = cancelTime;
	}

	public String getSerial()
	{
		return serial;
	}

	public void setSerial(String serial)
	{
		this.serial = serial;
	}

	public String getMsgId()
	{
		return msgId;
	}

	public void setMsgId(String msgId)
	{
		this.msgId = msgId;
	}

	public String getFromUser()
	{
		return fromUser;
	}

	public void setFromUser(String fromUser)
	{
		this.fromUser = fromUser;
	}

	public String getToUser()
	{
		return toUser;
	}

	public void setToUser(String toUser)
	{
		this.toUser = toUser;
	}

	public Timestamp getSendTime()
	{
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime)
	{
		this.sendTime = sendTime;
	}

	public Integer getSendState()
	{
		return sendState;
	}

	public void setSendState(Integer sendState)
	{
		this.sendState = sendState;
	}

	public Timestamp getRecTime()
	{
		return recTime;
	}

	public void setRecTime(Timestamp recTime)
	{
		this.recTime = recTime;
	}

	public Long getValidity()
	{
		return validity;
	}

	public void setValidity(Long validity)
	{
		this.validity = validity;
	}

	public String getName(){

		return name;
	}

	public void setName(String name){

		this.name= name;

	}

	public Timestamp getModitytime(){

		return moditytime;
	}

	public void setModitytime(Timestamp moditytime){

		this.moditytime= moditytime;

	}

	public Integer getIssystem(){

		return issystem;
	}

	public void setIssystem(Integer issystem){

		this.issystem= issystem;

	}

	public Long getTypeid(){

		return typeid;
	}

	public void setTypeid(Long typeid){

		this.typeid= typeid;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

	public String getUrl(){

		return url;
	}

	public void setUrl(String url){

		this.url= url;

	}

	public Long getSid(){

		return sid;
	}

	public void setSid(Long sid){

		this.sid= sid;

	}

	public Long getUserid(){

		return userid;
	}

	public void setUserid(Long userid){

		this.userid= userid;

	}

	public Timestamp getPublishtime()
	{
		return publishtime;
	}

	public void setPublishtime(Timestamp publishtime)
	{
		this.publishtime = publishtime;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}
	
}

