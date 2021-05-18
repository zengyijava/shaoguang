package com.montnets.emp.entity.pasgrpbind;

import java.sql.Timestamp;

/**
 * 
 * @author Administrator
 *
 */
public class LfAccountBind implements java.io.Serializable
{
	private static final long serialVersionUID = -453720090586239004L;
	//标识列ID
	private Long id;
	//发送账号 
	private String spuserId;
	//业务编码
	private String busCode;
	//操作员GUID
	private Long sysGuid;
	//机构编码
	private String depCode;
	//模块编码
	private String menuCode;
	//绑定类型（0-机构；1-操作员；2-业务类型）
	private Integer bindType;
	//创建时间
	private Timestamp createtime;
	//创建者GUID
	private Long createrGuid;
	//备注
	private String description;
	//企业编码
	private String corpCode;
	

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public LfAccountBind()
	{
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSpuserId() {
		return spuserId;
	}

	public void setSpuserId(String spuserId) {
		this.spuserId = spuserId;
	}

	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

	public Long getSysGuid() {
		return sysGuid;
	}

	public void setSysGuid(Long sysGuid) {
		this.sysGuid = sysGuid;
	}

	public String getDepCode() {
		return depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}



	public Integer getBindType() {
		return bindType;
	}

	public void setBindType(Integer bindType) {
		this.bindType = bindType;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public Long getCreaterGuid() {
		return createrGuid;
	}

	public void setCreaterGuid(Long createrGuid) {
		this.createrGuid = createrGuid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	
}