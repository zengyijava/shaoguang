package com.montnets.emp.entity.weix;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 微信默认回复消息实体类
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWcRtext implements Serializable
{

	private static final long	serialVersionUID	= 3108769146114069567L;

	// 默认回复ID
	private Long				TetId;

	// 消息的类型（0：文本回复；1：单图文回复；2：多图文回复；3：语音回复；）
	private Integer				tetType;

	// 内容摘要，界面显示用
	private String				msgText;

	// 返回数据需要生成的XML格式
	private String				msgXML;

	// 模板Id
	private Long				TId;

	// 公众帐号的ID
	private Long				AId;

	// 企业编号
	private String				corpCode;

	// 创建时间
	private Timestamp			createtime;

	// 更新时间
	private Timestamp			modifytime;

	// 标题
	private String				title;

	/**
	 * 
	 */

	public String getMsgText()
	{
		return msgText;
	}

	public void setMsgText(String msgText)
	{
		this.msgText = msgText;
	}

	public String getMsgXML()
	{
		return msgXML;
	}

	public void setMsgXML(String msgXML)
	{
		this.msgXML = msgXML;
	}

	public LfWcRtext()
	{
		super();
	}

	public Long getTetId()
	{
		return TetId;
	}

	public void setTetId(Long tetId)
	{
		TetId = tetId;
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

	public Integer getTetType()
	{
		return tetType;
	}

	public void setTetType(Integer tetType)
	{
		this.tetType = tetType;
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
