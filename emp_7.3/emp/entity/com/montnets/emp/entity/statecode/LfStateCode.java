/**
 * 
 */
package com.montnets.emp.entity.statecode;

import java.sql.Timestamp;

/**
 * @project emp
 * @author pengj
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2016-1-15 上午09:05:00
 * @description
 */

public class LfStateCode implements java.io.Serializable
{


	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5288988593319137450L;
	//编号id
	private Integer stateId;
	//状态码
	private String stateCode;
	//映射码
	private String mappingCode;
	public String getMappingCode() {
		return mappingCode;
	}
	public void setMappingCode(String mappingCode) {
		this.mappingCode = mappingCode;
	}
	//状态码描述
	private String stateDes;
	//企业编码
	private String corpCode;
	//创建时间
	private Timestamp createTime;
	//修改时间
	private Timestamp updateTime;
	//最后更新的操作员ID
	private Long userId;
	public Integer getStateId() {
		return stateId;
	}
	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getStateDes() {
		return stateDes;
	}
	public void setStateDes(String stateDes) {
		this.stateDes = stateDes;
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
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
}
