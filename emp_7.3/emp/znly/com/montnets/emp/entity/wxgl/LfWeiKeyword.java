package com.montnets.emp.entity.wxgl;

import java.sql.Timestamp;

/**
 * 微信关键字实体类
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWeiKeyword implements java.io.Serializable
{
	/**
	 * 微信关键字
	 */
	private static final long	serialVersionUID	= -3620693205772335536L;

	// 关键字ID
	private Long				KId;

	// 关键字名称
	private String				name;

	// 关键字类型(0:模糊匹配；1:全批匹配)
	private Integer				type;

	// 公众帐号ID
	private Long				AId;

	// 公司编号
	private String				corpCode;

	// 创建时间
	private Timestamp			createtime;

	// 更新时间
	private Timestamp			modifytime;

	/**
	 * 
	 */
	public LfWeiKeyword()
	{
		super();
	}

	public Long getKId()
	{
		return KId;
	}

	public void setKId(Long kId)
	{
		KId = kId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
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
}
