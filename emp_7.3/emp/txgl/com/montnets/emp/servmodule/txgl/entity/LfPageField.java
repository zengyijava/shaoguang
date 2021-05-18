package com.montnets.emp.servmodule.txgl.entity;

/**
 * 
 * @project montnets_entity
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-24 下午02:31:18
 * @description 状态报告类
 */
public class LfPageField implements java.io.Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5194544161829819670L;
	
	private String modleId;
	private String pageId;
	private String fieldId;
	private String fieldName;
	private String field;
	private Integer fieldType;
	private String subFieldValue;
	private String subFieldName;
	
	//控件是否显示.0：是;1：否
	private Integer filedShow;
	
	//是否是子项。0：是；1：否
	private Integer subField;
	private String defaultValue;
	private Integer sortValue;
	//是否为控件。0：是;1：否，即为控件子项
	private Integer isField;
	
	
	public String getModleId()
	{
		return modleId;
	}
	public void setModleId(String modleId)
	{
		this.modleId = modleId;
	}
	public String getPageId()
	{
		return pageId;
	}
	public void setPageId(String pageId)
	{
		this.pageId = pageId;
	}
	public String getFieldId()
	{
		return fieldId;
	}
	public void setFieldId(String fieldId)
	{
		this.fieldId = fieldId;
	}
	public String getFieldName()
	{
		return fieldName;
	}
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}
	public String getField()
	{
		return field;
	}
	public void setField(String field)
	{
		this.field = field;
	}
	public Integer getFieldType()
	{
		return fieldType;
	}
	public void setFieldType(Integer fieldType)
	{
		this.fieldType = fieldType;
	}
	public String getSubFieldValue()
	{
		return subFieldValue;
	}
	public void setSubFieldValue(String subFieldValue)
	{
		this.subFieldValue = subFieldValue;
	}
	public String getSubFieldName()
	{
		return subFieldName;
	}
	public void setSubFieldName(String subFieldName)
	{
		this.subFieldName = subFieldName;
	}
	public Integer getFiledShow()
	{
		return filedShow;
	}
	public void setFiledShow(Integer filedShow)
	{
		this.filedShow = filedShow;
	}
	public Integer getSubField()
	{
		return subField;
	}
	public void setSubField(Integer subField)
	{
		this.subField = subField;
	}
	public String getDefaultValue()
	{
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	public Integer getSortValue()
	{
		return sortValue;
	}
	public void setSortValue(Integer sortValue)
	{
		this.sortValue = sortValue;
	}
	public Integer getIsField()
	{
		return isField;
	}
	public void setIsField(Integer isField)
	{
		this.isField = isField;
	}
	
	
	
}
