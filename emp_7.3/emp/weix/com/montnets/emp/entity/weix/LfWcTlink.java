package com.montnets.emp.entity.weix;

import java.io.Serializable;

/**
 * 微信关键字与回复模板关联实体类
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWcTlink implements Serializable
{

	private static final long	serialVersionUID	= 5961760955435322743L;

	// 关键字编号
	private Long				KId;

	// 模板编号
	private Long				TId;

	public LfWcTlink()
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

	public Long getTId()
	{
		return TId;
	}

	public void setTId(Long tId)
	{
		TId = tId;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

}
