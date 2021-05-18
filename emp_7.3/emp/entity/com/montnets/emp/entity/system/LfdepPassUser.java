package com.montnets.emp.entity.system;
/**
 *  用户接收密码人列表
 * @author Administrator
 *
 */
public class LfdepPassUser  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -703116432094336278L;
	
	//机构ID
	private Long depId;	
	//机构名称
	private String depName;	
	//用户userid
	private Long userId;
	//姓名
	private String name;
	//手机号码
	private String mobile;
	//工号
	private String worknumber;
	
	public Long getDepId() {
		return depId;
	}
	public void setDepId(Long depId) {
		this.depId = depId;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public String getWorknumber() {
		return worknumber;
	}
	public void setWorknumber(String worknumber) {
		this.worknumber = worknumber;
	}

	
	
	

}
