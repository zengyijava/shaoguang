package com.montnets.emp.employee.vo;

public class LfEmpDepConnVo implements java.io.Serializable{
	/**
	 * 员工机构vo
	 */
	private static final long serialVersionUID = -1802327144507016701L;
	//员工id
	private Long user_id;
	//private String dep_code_third;
	//机构id
	private Long dep_id;
	//关联id
	private Long conn_id;
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	/*public String getDep_code_third() {
		return dep_code_third;
	}
	public void setDep_code_third(String dep_code_third) {
		this.dep_code_third = dep_code_third;
	}*/
	public Long getDep_id()
	{
		return dep_id ;
	}
	public void setDep_id(Long dep_id)
	{
		this.dep_id = dep_id;
	}
	public Long getConn_id() {
		return conn_id;
	}
	public void setConn_id(Long conn_id) {
		this.conn_id = conn_id;
	}	
}