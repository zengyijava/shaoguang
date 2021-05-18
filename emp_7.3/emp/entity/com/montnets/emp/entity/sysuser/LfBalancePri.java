/**
 * 
 */
package com.montnets.emp.entity.sysuser;

import java.sql.Timestamp;

/**
 * @project emp
 * @author pengj
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-10-20 上午10:20:00
 * @description 回收充值机构权限表
 */

public class LfBalancePri implements java.io.Serializable
{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8457008887165687753L;
	//标识列ID
	private Long id;
	//操作员ID
	private Long userId;
	//机构ID
	private Long  depId;
	//充值权限类型 默认0 
	private Integer type;
	//创建记录的操作员ID
	private Long createUserId;
	//企业编码
	private String corpCode;
	//创建时间
	private Timestamp createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getDepId() {
		return depId;
	}
	public void setDepId(Long depId) {
		this.depId = depId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
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
