package com.montnets.emp.client.vo;

/**
 * 
 * @project emp
 * @author wuqw <158030621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2012-1-11
 * @description 客户属性值vo
 */
public class CustFieldValueVo implements java.io.Serializable
{
	private static final long serialVersionUID = -8870026520894025958L;
    //主键
	private Long id;
	//属性名称
	private Long field_ID;
	//属性值
	private String field_Value;	
	//企业编码
	private String corp_code;
	//获取id
	public Long getId() {
		return id;
	}
	//设置id
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

	public String getCorp_code() {
		return corp_code;
	}

	public void setCorp_code(String corp_code) {
		this.corp_code = corp_code;
	}

}
