/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-14 下午03:02:55
 */
package com.montnets.emp.entity.apptask;

import java.sql.Timestamp;

/**
 * @description app发送任务
 * @project emp183
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-14 下午03:02:55
 */

public class LfAppMttask
{
	//操作员ID
	private Long userid;
	//消息类型（0：文本消息；1：图片消息；2：视频；3：语音；4：事件推送； ）
	private Integer msgtype;
	//企业编码
	private String corpcode;
	//发送失败数
	private Long faicount;
	//发送总数
	private Long subcount;
	//发送内容
	private String msg;
	//发送开始时间
	private Timestamp bigintime;
	//发送状态(0-代表新消息（未发送）；1-已发送到APP网关成功;2-失败;3-APP网关处理完成;4-发送中; 5超时未发送)
	private Integer sendstate;
	//多媒体文件服务器URL
	private String msgurl;
	//多媒体文件本地服务器URL
	private String msglocalurl;
	//已读用户数
	private Long readcount;
	//自增ID
	private Long id;
	//任务ID
	private Long taskid;
	//发送结束时间
	private Timestamp endtime;
	//发送成功数
	private Long succount;
	//未读用户数
	private Long unreadcount;
	//发送主题
	private String title;
	//APP企业账号
	private String appacount;

	public LfAppMttask(){
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

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

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

	public String getMsglocalurl()
	{
		return msglocalurl;
	}

	public void setMsglocalurl(String msglocalurl)
	{
		this.msglocalurl = msglocalurl;
	}

}
