package com.montnets.emp.entity.site;

import java.sql.Timestamp;


/**
 * 实体类： LfSitType
 *
 * @project p_sit
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfSitType implements java.io.Serializable
{
   
    // 程序自增ID
    private Long	typeId;
   
    // 名称
    private String	name;
   
    // 是否默认选择
    private Integer	isDefault;
   
    // 模式（0标准，1高级）
    private Integer	pattern;
   
    // 封面地址
    private String	imgUrl;
   
    // 图片预览0
    private String	imgUrl0;
   
    // 图片预览1
    private String	imgUrl1;
   
    // 图片预览2
    private String	imgUrl2;
   
    // 图片预览3
    private String	imgUrl3;
   
    // 是否属于系统（1是，0否）
    private Integer	isSystem;
   
    // 排序
    private Integer	seqNum;
   
    // 企业编码（0表示系统默认分类）
    private String	corpCode;
   
    // 创建时间
    private Timestamp	createtime;
   
    // 更新时间
    private Timestamp	moditytime;
   
    
	public Long getTypeId()
	{
		return typeId;
	}

	public void setTypeId(Long typeId)
	{
		this.typeId = typeId;
	}
    
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
    
	public Integer getIsDefault()
	{
		return isDefault;
	}

	public void setIsDefault(Integer isDefault)
	{
		this.isDefault = isDefault;
	}
    
	public Integer getPattern()
	{
		return pattern;
	}

	public void setPattern(Integer pattern)
	{
		this.pattern = pattern;
	}
    
	public String getImgUrl()
	{
		return imgUrl;
	}

	public void setImgUrl(String imgUrl)
	{
		this.imgUrl = imgUrl;
	}
    
	public String getImgUrl0()
	{
		return imgUrl0;
	}

	public void setImgUrl0(String imgUrl0)
	{
		this.imgUrl0 = imgUrl0;
	}
    
	public String getImgUrl1()
	{
		return imgUrl1;
	}

	public void setImgUrl1(String imgUrl1)
	{
		this.imgUrl1 = imgUrl1;
	}
    
	public String getImgUrl2()
	{
		return imgUrl2;
	}

	public void setImgUrl2(String imgUrl2)
	{
		this.imgUrl2 = imgUrl2;
	}
    
	public String getImgUrl3()
	{
		return imgUrl3;
	}

	public void setImgUrl3(String imgUrl3)
	{
		this.imgUrl3 = imgUrl3;
	}
    
	public Integer getIsSystem()
	{
		return isSystem;
	}

	public void setIsSystem(Integer isSystem)
	{
		this.isSystem = isSystem;
	}
    
	public Integer getSeqNum()
	{
		return seqNum;
	}

	public void setSeqNum(Integer seqNum)
	{
		this.seqNum = seqNum;
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
    
	public Timestamp getModitytime()
	{
		return moditytime;
	}

	public void setModitytime(Timestamp moditytime)
	{
		this.moditytime = moditytime;
	}

}