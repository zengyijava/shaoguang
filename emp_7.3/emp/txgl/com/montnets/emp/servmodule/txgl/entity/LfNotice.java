package com.montnets.emp.servmodule.txgl.entity;

import java.sql.Timestamp;
/**
 * 
 * @project montnets_entity
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-30 下午03:12:30
 * @description  公告
 */
public class LfNotice implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6886884383731363773L;
	private Long noticeID;
	private Long userID;
	private String title;
	private String context;
	private Timestamp publishTime;
	private String corpcode;
	//注尾
	private String noteTail;
	//增加公告状态，1开启，2关闭
	private Integer noteState;
	//有效期，单位天
	private Integer noteValid;
	
	
	public String getNoteTail()
	{
		return noteTail;
	}
	public void setNoteTail(String noteTail)
	{
		this.noteTail = noteTail;
	}
	public Integer getNoteState()
	{
		return noteState;
	}
	public void setNoteState(Integer noteState)
	{
		this.noteState = noteState;
	}
	public Integer getNoteValid()
	{
		return noteValid;
	}
	public void setNoteValid(Integer noteValid)
	{
		this.noteValid = noteValid;
	}
	public String getCorpcode()
	{
		return corpcode;
	}
	public void setCorpcode(String corpcode)
	{
		this.corpcode = corpcode;
	}
	public Long getNoticeID()
	{
		return noticeID;
	}
	public void setNoticeID(Long noticeID)
	{
		this.noticeID = noticeID;
	}
	public Long getUserID()
	{
		return userID;
	}
	public void setUserID(Long userID)
	{
		this.userID = userID;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getContext()
	{
		return context;
	}
	public void setContext(String context)
	{
		this.context = context;
	}
	public Timestamp getPublishTime()
	{
		return publishTime;
	}
	public void setPublishTime(Timestamp publishTime)
	{
		this.publishTime = publishTime;
	}
	
}
