package com.montnets.emp.entity.wxgl;

import java.sql.Timestamp;

/**
 * 微信上行/下行记录实体类
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWeiMsg implements java.io.Serializable
{
	private static final long	serialVersionUID	= -4923255341568942898L;

	// 消息记录自动编号
	private Long					msgId;

	// 消息的类型（0：文本消息；1：图片消息；2：地理位置消息；3：链接消息；4：事件推送；5：回复文本；6：回复图文；7：回复语音消息）
	private Integer				msgType;

	// 微信Id
	private Long					wcId;

	// 公众帐号Id
	private Long					AId;

	// 上行/下行
	private Integer				type;

	// 消息的XML数据
	private String				msgXml;

	// 消息的预览-在展示消息历史记录的时候不需要解析msgXML格式数据
	private String				msgText;

	// 上下行关联
	private Long					parentId;

	// 企业编号
	private String				corpCode;

	// 创建时间
	private Timestamp			createTime;

	public Long getMsgId()
	{
		return msgId;
	}

	public void setMsgId(Long msgId)
	{
		this.msgId = msgId;
	}

	public Integer getMsgType()
	{
		return msgType;
	}

	public void setMsgType(Integer msgType)
	{
		this.msgType = msgType;
	}

	public Long getWcId()
	{
		return wcId;
	}

	public void setWcId(Long wcId)
	{
		this.wcId = wcId;
	}

	public Long getAId()
	{
		return AId;
	}

	public void setAId(Long aId)
	{
		AId = aId;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
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

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}

	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}
}
