package com.montnets.emp.common.constant;

import java.util.Date;

/**
 * 数据传输对象，
 * 
 * @project emp
 * @author luomingming <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-19 上午01:56:42
 * @description
 */
public class Message implements java.io.Serializable {

	/**
	 *  数据传输对象
	 */
	private static final long serialVersionUID = 3806621925095246741L;
	private String fromId;
	private String toId;
	private String targetId;
	private String targetPath;
	private String targetName;	
	private Date sendDate;
	private String strDate;
	private String content;
	private String name;
	private String messageId;
	private boolean isSingle;
	private String telPhoneNo;
	private String dialogId;
	private int type;
	private int state;
	private Long depId;

	private int recordCount;
	
	private String menuCode;
	
	private String mtmsgid;
	
	private Long taskId;
	
	private String sendType;

	/*private String fName;
	private String fLoginName;
	private String depName;
	private Integer fType;
	private String tName;
	private Integer tType;
	private String tmobileNum;
	private Long depId;
	private String msg;
	private Date date;
	private String time;
	private int state;
	private int flag;
*/
	public Message() {

	}

	public String getMenuCode()
	{
		return menuCode;
	}

	public void setMenuCode(String menuCode)
	{
		this.menuCode = menuCode;
	}
	
	public String getDialogId()
	{
		return dialogId;
	}

	public void setDialogId(String dialogId)
	{
		this.dialogId = dialogId;
	}
	
	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public boolean getIsSingle() {
		return isSingle;
	}

	public void setIsSingle(boolean isSingle) {
		this.isSingle = isSingle;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public void setSingle(boolean isSingle) {
		this.isSingle = isSingle;
	}

	
	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTelPhoneNo() {
		return telPhoneNo;
	}

	public void setTelPhoneNo(String telPhoneNo) {
		this.telPhoneNo = telPhoneNo;
	}

	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}
	
	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	
	public String getMtmsgid()
	{
		return mtmsgid;
	}

	public void setMtmsgid(String mtmsgid)
	{
		this.mtmsgid = mtmsgid;
	}
	
	public Long getTaskId()
	{
		return taskId;
	}

	public void setTaskId(Long taskId)
	{
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		return "LfImMessage [content=" + content + ", depId=" + depId
				+ ", dialogId=" + dialogId + ", fromId=" + fromId
				+ ", isSingle=" + isSingle + ", menuCode=" + menuCode
				+ ", messageId=" + messageId + ", name=" + name
				+ ", recordCount=" + recordCount + ", sendDate=" + sendDate
				+ ", state=" + state + ", strDate=" + strDate + ", targetId="
				+ targetId + ", targetName=" + targetName + ", targetPath="
				+ targetPath + ", telPhoneNo=" + telPhoneNo + ", toId=" + toId
				+ ", type=" + type + "]";
	}

	public String getSendType()
	{
		return sendType;
	}

	public void setSendType(String sendType)
	{
		this.sendType = sendType;
	}	
}
