package com.montnets.emp.entity.form;

import java.sql.Timestamp;


/**
 * 实体类： LfFomInfo
 *
 * @project p_fom
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfFomInfo implements java.io.Serializable
{
   
    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-23 下午04:15:06
     */
    private static final long serialVersionUID = 4914231916756638418L;

    // 程序自增ID
    private Long	fId;
   
    // 分类ID   表单类型 ID 对应LfFomType
    //自定义系统表单为 0
    private Long	typeId;
    
    //父ID   当为系统表单时 ,则为0,当自定义/套用 表单时 则设置为是系统表单ID
    //自定义系统表单为-1
    private Long parentId;
   
    // 表单标题
    private String	title;
   
    // 表单说明
    private String	note;
   
    // 发布状态  1发布   2草稿 3 删除   4表单已经被用户所提交,只做参数传递,不做数据存储
    private Integer	publishState;
   
    // 访问地址
    private String	url;
   
    // 提交次数//   如果是系统表单则代表的模板使用量 ,如果是非系统模板,则代表表单提交量
    private Integer	submitCount;
   
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
    
    public  LfFomInfo(){
        this.isSystem = 0;
        this.publishState = 1;
        this.createtime = new Timestamp(System.currentTimeMillis());
        this.submitCount = 0;
    }
   
    
	public Long getFId()
	{
		return fId;
	}

	public void setFId(Long fId)
	{
		this.fId = fId;
	}
    
	public Long getTypeId()
	{
		return typeId;
	}

	public void setTypeId(Long typeId)
	{
		this.typeId = typeId;
	}
	
    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }
    
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
    
	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}
    
	public Integer getPublishState()
	{
		return publishState;
	}

	public void setPublishState(Integer publishState)
	{
		this.publishState = publishState;
	}
    
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
    
	public Integer getSubmitCount()
	{
		return submitCount;
	}

	public void setSubmitCount(Integer submitCount)
	{
		this.submitCount = submitCount;
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