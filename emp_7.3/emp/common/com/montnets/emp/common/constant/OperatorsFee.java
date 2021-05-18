package com.montnets.emp.common.constant;
/**
 * 运营商余额信息
 * @author LinZhiHan
 *
 */
public class OperatorsFee {
	//账号
	private String userId;
	//密码
	private String password;
	//1 短信 2彩信
	private Integer msType;
	//余额数量
	private String count;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public Integer getMsType() {
		return msType;
	}
	public void setMsType(Integer msType) {
		this.msType = msType;
	}
}
