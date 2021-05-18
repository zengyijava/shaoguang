package com.montnets.emp.shorturl.surlmanage.entity;

import java.sql.Timestamp;

/**
 * 短域名维护
 * @ClassName:LfDomain.java
 * @Description:TODO
 * @author ouyangyu
 * @date:2018-3-5下午02:46:54
 */
public class LfDomain {
	//短域名id 
	private Long id;
	//短域名
	private String domain;
	//总长度
	private Integer lenAll;
	//全局扩展位数
	private Long lenExten;
	//域名类别  0 公用  1专用
	private Integer dtype;
	//域名状态  0  有效    -1 无效
	private Integer flag;
	//有效时间，单位天 , 30  ,值不能为0 
	private Long validDays;
	//创建人员
	private Long createUid;
	//创建用户CREATE_USER
	private String  createUser;
	//创建时间
	private Timestamp createTm;
	//最后修改人员ID 
	private Long updateUid;
	//最后修改时间
	private Timestamp updateTm;
	private String remark;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public Integer getLenAll() {
		return lenAll;
	}
	public void setLenAll(Integer lenAll) {
		this.lenAll = lenAll;
	}
	public Long getLenExten() {
		return lenExten;
	}
	public void setLenExten(Long lenExten) {
		this.lenExten = lenExten;
	}
	public Integer getDtype() {
		return dtype;
	}
	public void setDtype(Integer dtype) {
		this.dtype = dtype;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Long getValidDays() {
		return validDays;
	}
	public void setValidDays(Long validDays) {
		this.validDays = validDays;
	}
	public Long getCreateUid() {
		return createUid;
	}
	public void setCreateUid(Long createUid) {
		this.createUid = createUid;
	}
	public Timestamp getCreateTm() {
		return createTm;
	}
	public void setCreateTm(Timestamp createTm) {
		this.createTm = createTm;
	}
	public Long getUpdateUid() {
		return updateUid;
	}
	public void setUpdateUid(Long updateUid) {
		this.updateUid = updateUid;
	}
	public Timestamp getUpdateTm() {
		return updateTm;
	}
	public void setUpdateTm(Timestamp updateTm) {
		this.updateTm = updateTm;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		if("".equals(remark)){
			this.remark = " ";
		}else{
			this.remark = remark;
		}
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
}
