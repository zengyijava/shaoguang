/**
 * Program  : LfAppTcMomsg.java
 * Author   : zousy
 * Create   : 2014-6-13 下午01:57:45
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.entity.appmage;

import java.sql.Timestamp;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-13 下午01:57:45
 */
public class LfAppTcMomsg
{

	private Long msgid;

	private Long aid;

	private Long wcid;

	private String msgxml;

	private Integer msgtype;

	private Timestamp createtime;

	private String corpcode;

	private Long parentid;
	// 0文本  	1图片   2语音  3视频
	private Integer type;

	private String msgtext;
	
	private String msgJson;
	private String fromUser;
	private String toName;
	private String toUser;
	private Integer msgSrc;
	private Integer status;
	private String serial;
	private String fromName;

	public LfAppTcMomsg(){
	} 

	public String getSerial()
	{
		return serial;
	}

	public void setSerial(String serial)
	{
		this.serial = serial;
	}

	public String getFromName()
	{
		return fromName;
	}

	public void setFromName(String fromName)
	{
		this.fromName = fromName;
	}

	public String getMsgJson()
	{
		return msgJson;
	}

	public void setMsgJson(String msgJson)
	{
		this.msgJson = msgJson;
	}

	public String getFromUser()
	{
		return fromUser;
	}

	public void setFromUser(String fromUser)
	{
		this.fromUser = fromUser;
	}

	public String getToName()
	{
		return toName;
	}

	public void setToName(String toName)
	{
		this.toName = toName;
	}

	public String getToUser()
	{
		return toUser;
	}

	public void setToUser(String toUser)
	{
		this.toUser = toUser;
	}

	public Integer getMsgSrc()
	{
		return msgSrc;
	}

	public void setMsgSrc(Integer msgSrc)
	{
		this.msgSrc = msgSrc;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Long getMsgid(){

		return msgid;
	}

	public void setMsgid(Long msgid){

		this.msgid= msgid;

	}

	public Long getAid(){

		return aid;
	}

	public void setAid(Long aid){

		this.aid= aid;

	}

	public Long getWcid(){

		return wcid;
	}

	public void setWcid(Long wcid){

		this.wcid= wcid;

	}

	public String getMsgxml(){

		return msgxml;
	}

	public void setMsgxml(String msgxml){

		this.msgxml= msgxml;

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

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

	public Long getParentid(){

		return parentid;
	}

	public void setParentid(Long parentid){

		this.parentid= parentid;

	}

	public Integer getType(){

		return type;
	}

	public void setType(Integer type){

		this.type= type;

	}

	public String getMsgtext(){

		return msgtext;
	}

	public void setMsgtext(String msgtext){

		this.msgtext= msgtext;

	}

}

