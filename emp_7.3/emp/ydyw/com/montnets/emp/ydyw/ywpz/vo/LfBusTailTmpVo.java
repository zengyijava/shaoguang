package com.montnets.emp.ydyw.ywpz.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class LfBusTailTmpVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8583697931158348985L;

	//业务ID
	private Long busId;
	
	//业务名称
	private String busName;
	
	//业务编码 
	private String busCode;
	
	//状态：0、启用        1、禁用
	private Integer state;
	
	//配置短信模板个数
	private Integer icount;
	
	//创建时间
	//private String createTime;
	private Timestamp createTime;
	
	//最后更新时间
	//private String updateTime;
	private Timestamp updateTime;
	
	//创建人
	private String name;
	
	//创建人帐号
	private String userName;
	
	//创建人机构
	private String depName;

	public Long getBusId() {
		return busId;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIcount() {
		return icount;
	}

	public void setIcount(Integer icount) {
		this.icount = icount;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

}
