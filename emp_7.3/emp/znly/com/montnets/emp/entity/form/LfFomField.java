package com.montnets.emp.entity.form;



/**
 * 实体类： LfFomField
 *
 * @project p_fom
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfFomField implements java.io.Serializable
{
   
    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-23 下午04:14:23
     */
    private static final long serialVersionUID = -3120933559442156222L;

    // 自增ID
    private Long	fieldId;
   
    //问题类型（单选radio，多选checkbox，输入框input，文本框textarea）
    private String	filedType;
   
    //选项值
    private String	fieldValue;
    
    private  Long   qid;
    
    public Long getQid()
    {
        return qid;
    }

    public void setQid(Long qid)
    {
        this.qid = qid;
    }

    public Long getFieldId()
	{
		return fieldId;
	}

	public void setFieldId(Long fieldId)
	{
		this.fieldId = fieldId;
	}
    
	public String getFiledType()
	{
		return filedType;
	}

	public void setFiledType(String filedType)
	{
		this.filedType = filedType;
	}
    
	public String getFieldValue()
	{
		return fieldValue;
	}

	public void setFieldValue(String fieldValue)
	{
		this.fieldValue = fieldValue;
	}

}