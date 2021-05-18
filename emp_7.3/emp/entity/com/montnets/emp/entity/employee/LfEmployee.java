/**
 * 
 */
package com.montnets.emp.entity.employee;

import java.sql.Timestamp;

/**
 * TableLfEmployee员工信息表
 * 
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 10:12:12
 * @description
 */

public class LfEmployee implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8380007933358132686L;

	/**
	 * 
	 */
	// private static final long serialVersionUID = -1135566612717853762L;
	//员工ID
	private Long employeeId;
	//职位ID
	private Integer pid;
	//机构ID
	private Long depId;
	//工号
	private String employeeNo;
	//员工状态（0无效，1有效）
	private Integer estate;
	//备注
	private String commnets;
	//是否（0否，1是）接收短信
	private Integer recState;
	//隐藏手机号
	private Integer hidephState;
	//E-MAIL地址
	private String email;
	//MSN号码
	private String msn;
	//QQ号码
	private String qq;
	//姓名
	private String name;
	//性别
	private Integer sex;
	//手机
	private String mobile;
	//生日
	private Timestamp birthday;
	//办公电话
	private String oph;
	//通讯录唯一性标识
	private Long guId;
	//业务类型（业务类型编码）
	private String bizId;
	//员工编码
	private String employeeCode;
	//部门编码
	// private String depCode;
	// 更新时间
	private Timestamp lastUpddttm;
	// 在职状态
	private String hrStatus;
	// 1-表示操作员；0-表示非操作员
	private Integer isOperator;
	// 企业编码
	private String corpCode;
	//职务
	private String duties;
	//传真号码
	private String fax;
	
	//统一平台操作员标识ID
	private Long upGuId;
	
	public Long getUpGuId() {
		return upGuId;
	}

	public void setUpGuId(Long upGuId) {
		this.upGuId = upGuId;
	}

	public LfEmployee() {

	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getEstate() {
		return estate;
	}

	public void setEstate(Integer estate) {
		this.estate = estate;
	}

	public String getCommnets() {
		return commnets;
	}

	public void setCommnets(String commnets) {
		this.commnets = commnets;
	}

	public Integer getRecState() {
		return recState;
	}

	public void setRecState(Integer recState) {
		this.recState = recState;
	}

	public Integer getHidephState() {
		return hidephState;
	}

	public void setHidephState(Integer hidephState) {
		this.hidephState = hidephState;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getMobile() {
		if (mobile == null) {
			return "";
		}
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Timestamp getBirthday() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	public String getOph() {
		return oph;
	}

	public void setOph(String oph) {
		this.oph = oph;
	}

	public Long getGuId() {
		return guId;
	}

	public void setGuId(Long guId) {
		this.guId = guId;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	/*
	 * public String getDepCode() { return depCode; }
	 * 
	 * public void setDepCode(String depCode) { this.depCode = depCode; }
	 */

	public Timestamp getLastUpddttm() {
		return lastUpddttm;
	}

	public void setLastUpddttm(Timestamp lastUpddttm) {
		this.lastUpddttm = lastUpddttm;
	}

	public String getHrStatus() {
		return hrStatus;
	}

	public void setHrStatus(String hrStatus) {
		this.hrStatus = hrStatus;
	}

	public Integer getIsOperator() {
		return isOperator;
	}

	public void setIsOperator(Integer isOperator) {
		this.isOperator = isOperator;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getDuties() {
		return duties;
	}

	public void setDuties(String duties) {
		this.duties = duties;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

}
