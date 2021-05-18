/**
 * 
 */
package com.montnets.emp.group.vo;

public class LfList2groVo implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2728094457736699874L;
	//对应通讯录的GUID
	private Long guid;
	//姓名
	private String name;
	//手机
	private String mobile;
	//成员类型
	//为0时，插入的是员工通讯录的GUID；
	//为1时,插入的是客户通讯录的GUID；
	//为2时，插入的是自定义通讯录的GUID;
	//为3时，插入的是操作员的GUID)
	private Integer l2gtype;
	//共享类型
	private Integer sharetype;
	
	public Long getGuid() {
		return guid;
	}
	public void setGuid(Long guid) {
		this.guid = guid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getL2gtype() {
		return l2gtype;
	}
	public void setL2gtype(Integer l2gtype) {
		this.l2gtype = l2gtype;
	}
	public Integer getSharetype() {
		return sharetype;
	}
	public void setSharetype(Integer sharetype) {
		this.sharetype = sharetype;
	}
	
	
}
