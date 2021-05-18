package com.montnets.emp.employee.vo;

public class LfEmployeeTypeVo implements java.io.Serializable{
	/**
	 * 员工类型表vo
	 */
	private static final long serialVersionUID = 983371534511786956L;
	//员工id
	private Long user_id;
	//员工名称
	private String name;
	//企业编码
	private String corp_code;
	//主键
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCorp_code() {
		return corp_code;
	}
	public void setCorp_code(String corp_code) {
		this.corp_code = corp_code;
	}
}
