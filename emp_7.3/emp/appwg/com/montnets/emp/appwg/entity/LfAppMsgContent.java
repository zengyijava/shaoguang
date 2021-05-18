							
package com.montnets.emp.appwg.entity;


public class LfAppMsgContent
{

	private Long id;

	private Long cacheId;
	
	//消息类型。1-mt消息；2-mo消息；3-rpt消息
	private Integer msgType;
	
	private String content;
	
	//排序数字，从1开始
	private Integer sortNum;

	public LfAppMsgContent(){
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getCacheId()
	{
		return cacheId;
	}

	public void setCacheId(Long cacheId)
	{
		this.cacheId = cacheId;
	}

	public Integer getMsgType()
	{
		return msgType;
	}

	public void setMsgType(Integer msgType)
	{
		this.msgType = msgType;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	} 

	

}

							