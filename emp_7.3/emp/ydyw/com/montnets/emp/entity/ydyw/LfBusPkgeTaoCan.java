package com.montnets.emp.entity.ydyw;

import java.sql.Timestamp;

/***
 * 业务、业务包与套餐关系
 * @author Administrator
 *
 */
public class LfBusPkgeTaoCan {

	//自增ID
	private Integer id;
	//套餐编码
	private String taocanCode;
	//业务包编码
	private String packageCode;
	//业务套编码
	private String busCode;
	//关联类型（0：套餐对应业务包；1：业务包对应业务）
	private Integer associateType;
	//企业编码
	private String corpCode;
	//创建时间
	private Timestamp createTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTaocanCode() {
		return taocanCode;
	}
	public void setTaocanCode(String taocanCode) {
		this.taocanCode = taocanCode;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	public Integer getAssociateType() {
		return associateType;
	}
	public void setAssociateType(Integer associateType) {
		this.associateType = associateType;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
	
}
