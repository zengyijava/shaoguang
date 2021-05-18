/**
 * 
 */
package com.montnets.emp.entity.client;

import java.io.Serializable;

/**
 * @project emp
 * @author wuqw <158030621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2012-1-10
 * @description 
 */

public class LfCustFieldValue implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -509542783551714782L;
	//自增ID
	private Long id;					
	//引用字段，关联LF_CUSTFIELD表的ID
	private Long field_ID;			
	//值
	private String field_Value;			

	public LfCustFieldValue(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getField_ID() {
		return field_ID;
	}

	public void setField_ID(Long field_ID) {
		this.field_ID = field_ID;
	}

	public String getField_Value() {
		return field_Value;
	}

	public void setField_Value(String field_Value) {
		this.field_Value = field_Value;
	}

}
