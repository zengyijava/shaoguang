package com.montnets.emp.entity.ydyw;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 业务包管理实体类
 * @todo TODO
 * @project	emp
 * @author WANGRUBIN
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-12 上午10:50:58
 * @description
 */
public class LfBusPackage  implements Serializable {
	
	//业务包管理 ID
	private Integer packageId;
	
	//业务包编码
	private String packageCode;
	
	//业务包名称
	private String packageName;

	//描述
	private String packageDes;
	
	//状态(0已启用，1已禁用)
	private Integer packageState;
	
	//企业编码
	private String corpCode;
	
	//创建时间
	private Timestamp createTime;
	
	//更新时间
	private Timestamp updateTime;
	
	//机构ID
	private Integer depId;
	
	//操作员ID
	private Integer userId;

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageDes() {
		return packageDes;
	}

	public void setPackageDes(String packageDes) {
		this.packageDes = packageDes;
	}

	public Integer getPackageState() {
		return packageState;
	}

	public void setPackageState(Integer packageState) {
		this.packageState = packageState;
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

	public Integer getDepId() {
		return depId;
	}

	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	
}
