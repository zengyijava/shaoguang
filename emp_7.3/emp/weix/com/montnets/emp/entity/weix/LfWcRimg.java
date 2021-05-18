package com.montnets.emp.entity.weix;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 微信图文回复实体类
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWcRimg implements Serializable
{

	/**
	 * 图文表
	 */
	private static final long	serialVersionUID	= 101023075034964670L;

	// 图文编号ID
	private Long				rimgId;

	// 图文标题
	private String				title;

	// 图文描述
	private String				description;

	// 封面地址
	private String				picurl;

	// 外网链接地址
	private String				link;

	// 公众帐号的ID
	private Long				AId;

	private String				corpCode;

	private Timestamp			createtime;

	private Timestamp			modifytime;

	/**
	 * 
	 */
	public LfWcRimg()
	{
		super();
	}

	public Long getRimgId()
	{
		return rimgId;
	}

	public void setRimgId(Long rimgId)
	{
		this.rimgId = rimgId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getPicurl()
	{
		return picurl;
	}

	public void setPicurl(String picurl)
	{
		this.picurl = picurl;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public Long getAId()
	{
		return AId;
	}

	public void setAId(Long aId)
	{
		AId = aId;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}

	public Timestamp getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(Timestamp createtime)
	{
		this.createtime = createtime;
	}

	public Timestamp getModifytime()
	{
		return modifytime;
	}

	public void setModifytime(Timestamp modifytime)
	{
		this.modifytime = modifytime;
	}

}
