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

public class LfCustField implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5255336401030549819L;
	//自增ID
	private Long id;					
	//引用字段
	private String field_Ref;			
	//显示名称
	private String field_Name;			
	//值类型。0：单选；1：多选
	private String v_type;				
	//企业代码
	private String corp_code;			
	//用户ID
	private String userid;				

	public LfCustField(){
		this.v_type = "0";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getField_Ref() {
		return field_Ref;
	}

	public void setField_Ref(String field_Ref) {
		this.field_Ref = field_Ref;
	}

	public String getField_Name() {
		return field_Name;
	}

	public void setField_Name(String field_Name) {
		this.field_Name = field_Name;
	}

	public String getV_type() {
		return v_type;
	}

	public void setV_type(String v_type) {
		this.v_type = v_type;
	}

	public String getCorp_code() {
		return corp_code;
	}

	public void setCorp_code(String corp_code) {
		this.corp_code = corp_code;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
