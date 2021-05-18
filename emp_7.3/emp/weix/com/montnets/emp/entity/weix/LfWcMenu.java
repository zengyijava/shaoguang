package com.montnets.emp.entity.weix;

import java.sql.Timestamp;

/**
 * 微信自定义菜单实体类
 * 
 * @project p_weix
 * @author linzhihan <zhihanking@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-13 上午11:59:45
 * @description
 */
public class LfWcMenu implements java.io.Serializable
{
	private static final long	serialVersionUID	= 1L;

	// 菜单ID（程序自增）
	private Long				MId;

	// 菜单的名称(8个汉字，16个字母)
	private String				mname;

	// 类型(创建时默认赋值为0;1:发送消息click；2：转到连接view)
	private Integer				mtype;

	// 菜单标示 ，自定义菜单唯一标识
	private String				mkey;

	// 自动回复的URL
	private String				murl;

	// 是否隐藏
	private Integer				mhidden;

	// 菜单的顺序编号
	private Integer				morder;

	// 父ID
	private Long				PId;

	// 模板Id
	private Long				TId;

	// 内容摘要，界面显示用
	private String				msgText;

	// 返回数据需要生成的XML格式
	private String				msgXml;

	// 公众帐号的ID
	private Long				AId;

	// 企业编码
	private String				corpCode;

	// 创建时间
	private Timestamp			createTime;

	public LfWcMenu()
	{
		createTime = new Timestamp(System.currentTimeMillis());
		mhidden = 0;
		msgXml = "";
		mtype = 0;
	}

	public Long getMId()
	{
		return MId;
	}

	public void setMId(Long mId)
	{
		MId = mId;
	}

	public String getMname()
	{
		return mname;
	}

	public void setMname(String mname)
	{
		this.mname = mname;
	}

	public Integer getMtype()
	{
		return mtype;
	}

	public void setMtype(Integer mtype)
	{
		this.mtype = mtype;
	}

	public String getMkey()
	{
		return mkey;
	}

	public void setMkey(String mkey)
	{
		this.mkey = mkey;
	}

	public String getMurl()
	{
		return murl;
	}

	public void setMurl(String murl)
	{
		this.murl = murl;
	}

	public Integer getMhidden()
	{
		return mhidden;
	}

	public void setMhidden(Integer mhidden)
	{
		this.mhidden = mhidden;
	}

	public Integer getMorder()
	{
		return morder;
	}

	public void setMorder(Integer morder)
	{
		this.morder = morder;
	}

	public Long getPId()
	{
		return PId;
	}

	public void setPId(Long pId)
	{
		PId = pId;
	}

	public Long getTId()
	{
		return TId;
	}

	public void setTId(Long tId)
	{
		TId = tId;
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

	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

}
