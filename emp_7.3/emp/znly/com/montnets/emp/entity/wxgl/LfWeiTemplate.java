package com.montnets.emp.entity.wxgl;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 微信回复模板实体类
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWeiTemplate implements Serializable
{

	private static final long	serialVersionUID	= 5255181422841461976L;

	// 模板ID
	private Long				TId;

	// 模板名称
	private String				name;

	// 消息的类型（0：文本回复；1：单图文回复；2：多图文回复；3：语音回复；）
	private Integer				msgType;

	// 返回数据需要生成的XML格式
	private String				msgXml;

	// 内容摘要，界面显示用
	private String				msgText;

	// 是否共用
	private Integer				isPublic;

	// 是否草稿
	private Integer				isDraft;

	// 0：静态；1：动态
	private Integer				isDynamic;

	// 公众帐号的ID（如果为空，为该企业所有公众账号共有）
	private Long				AId;

	// 企业编号
	private String				corpCode;

	// 创建时间
	private Timestamp			createtime;

	// 更新时间
	private Timestamp			modifytime;

	// 关联图文id（多个id以“，”隔开）
	private String				rimgids;

	// 关联关键字
	private String				keywordsvo;

	public String getKeywordsvo()
	{
		return keywordsvo;
	}

	public void setKeywordsvo(String keywordsvo)
	{
		this.keywordsvo = keywordsvo;
	}

	public String getRimgids()
	{
		return rimgids;
	}

	public void setRimgids(String rimgids)
	{
		this.rimgids = rimgids;
	}

	/**
	 * 
	 */
	public LfWeiTemplate()
	{
		super();
	}

	public Long getTId()
	{
		return TId;
	}

	public void setTId(Long tId)
	{
		TId = tId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getMsgType()
	{
		return msgType;
	}

	public void setMsgType(Integer msgType)
	{
		this.msgType = msgType;
	}

	public String getMsgXml()
	{
		return msgXml;
	}

	public void setMsgXml(String msgXml)
	{
		this.msgXml = msgXml;
	}

	public String getMsgText()
	{
		return msgText;
	}

	public void setMsgText(String msgText)
	{
		this.msgText = msgText;
	}

	public Integer getIsPublic()
	{
		return isPublic;
	}

	public void setIsPublic(Integer isPublic)
	{
		this.isPublic = isPublic;
	}

	public Integer getIsDraft()
	{
		return isDraft;
	}

	public void setIsDraft(Integer isDraft)
	{
		this.isDraft = isDraft;
	}

	public Integer getIsDynamic()
	{
		return isDynamic;
	}

	public void setIsDynamic(Integer isDynamic)
	{
		this.isDynamic = isDynamic;
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
