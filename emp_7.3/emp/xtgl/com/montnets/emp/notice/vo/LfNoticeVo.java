package com.montnets.emp.notice.vo;

import java.sql.Timestamp;

/**
 * 
 * @project montnets_entity
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-9-27 上午11:12:49
 * @description
 */
public class LfNoticeVo implements java.io.Serializable
{
	/**
	 * 公告vo
	 */
	private static final long serialVersionUID = 4201883398907087563L;
	// Lf_Notice主键
	private Long noticeID;
    //用户id
	private Long userID;
    //标题
	private String title;
    //内容 
	private String context;
    //发布时间
	private Timestamp publishTime;
    //用户名称
	private String name;
	//企业编码
	private String corpCode;

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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}	
}
