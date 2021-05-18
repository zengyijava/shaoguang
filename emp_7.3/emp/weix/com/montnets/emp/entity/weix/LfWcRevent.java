package com.montnets.emp.entity.weix;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 微信事件回复消息实体类
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWcRevent implements Serializable
{

	private static final long	serialVersionUID	= 3112673705920548111L;

	// 自增id
	private Long				EvtId;

	// 消息的类型（0：无关键字默认回复；1：关注事件；2：点阅；3：取消点阅；4：CLICK(自定义菜单点击事件)；）
	private Integer				evtType;

	// 模板Id
	private Long				TId;

	// 公众帐号的ID（如果为空，为该企业所有公众帐号共有）
	private Long				AId;

	// 企业编码
	private String				corpCode;

	// 创建时间
	private Timestamp			createtime;

	// 内容摘要，界面显示用
	private String				msgText;

	// 待返回的xml格式数据
	private String				msgXml;

	// 标题
	private String				title;

	// 更新时间
	private Timestamp			modifytime;

	/**
	 * 
	 */
	public LfWcRevent()
	{
		super();
	}

	public Long getEvtId()
	{
		return EvtId;
	}

	public void setEvtId(Long evtId)
	{
		EvtId = evtId;
	}

	public Integer getEvtType()
	{
		return evtType;
	}

	public void setEvtType(Integer evtType)
	{
		this.evtType = evtType;
	}

	public Long getTId()
	{
		return TId;
	}

	public void setTId(Long tId)
	{
		TId = tId;
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

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public String getMsgText()
	{
		return msgText;
	}

	public void setMsgText(String msgText)
	{
		this.msgText = msgText;
	}

	public String getMsgXml()
	{
		return msgXml;
	}

	public void setMsgXml(String msgXml)
	{
		this.msgXml = msgXml;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

}
