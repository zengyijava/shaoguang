package com.montnets.emp.entity.site;

import java.sql.Timestamp;


/**
 * 实体类： LfSitInfo
 *
 * @project p_sit
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfSitInfo implements java.io.Serializable
{
   
    // 程序自增ID
    private Long	sId;
   
    // 微站风格
    private Long	typeId;
   
    // 微站名称
    private String	name;
   
    // 访问地址
    private String	url;
   
    // 是否属于系统（1是，0否）
    private Integer	isSystem;
   
    // 企业编码（0表示系统默认分类）
    private String	corpCode;
   
    // 创建时间
    private Timestamp	createtime;
   
    // 更新时间
    private Timestamp	moditytime;
   
    
	public Long getSId()
	{
		return sId;
	}

	public void setSId(Long sId)
	{
		this.sId = sId;
	}
    
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
    
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
    
	public Integer getIsSystem()
	{
		return isSystem;
	}

	public void setIsSystem(Integer isSystem)
	{
		this.isSystem = isSystem;
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