package com.montnets.emp.entity.form;

import java.sql.Timestamp;


/**
 * 实体类： LfFomQuestion
 *
 * @project p_fom
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfFomQuestion implements java.io.Serializable
{
   
    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-23 下午04:17:44
     */
    private static final long serialVersionUID = 2244336946240714592L;

    // 程序自增ID
    private Long	qId;
   
    // 所属表单ID
    private Long	fId;
   
    // 问题类型（单选radio，多选checkbox，输入框input，文本框textarea）
    private String	filedType;
   
    // 问题题目
    private String	title;
   
    // 问题统计（json格式）
    private String	formData;
   
    // 排序
    private Integer	seqNum;
   
    //企业编码
    private String	corpCode;
   
    //创建时间
    private Timestamp	createtime;
   
    // 更新时间
    private Timestamp	moditytime;
   
    
	public Long getQId()
	{
		return qId;
	}

	public void setQId(Long qId)
	{
		this.qId = qId;
	}
    
	public Long getFId()
	{
		return fId;
	}

	public void setFId(Long fId)
	{
		this.fId = fId;
	}
    
	public String getFiledType()
	{
		return filedType;
	}

	public void setFiledType(String filedType)
	{
		this.filedType = filedType;
	}
    
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
    
	public String getFormData()
	{
		return formData;
	}

	public void setFormData(String formData)
	{
		this.formData = formData;
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