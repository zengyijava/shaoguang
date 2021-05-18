package com.montnets.emp.entity.form;

import java.sql.Timestamp;


/**
 * 实体类： LfFomType
 *
 * @project p_fom
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfFomType implements java.io.Serializable
{
   
    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-23 下午04:18:09
     */
    private static final long serialVersionUID = -3748504775658082901L;

    // 程序自增ID
    private Long	typeId;
   
    // 名称
    private String	name;
   
    // 父ID
    private Long	parentId;
   
    // 排序
    private Integer	seqNum;
   
    // 是否属于系统（1是，0否）
    private Integer	isSystem;
   
    // 企业编码
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
    
	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}
    
	public Integer getSeqNum()
	{
		return seqNum;
	}

	public void setSeqNum(Integer seqNum)
	{
		this.seqNum = seqNum;
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