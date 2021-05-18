package com.montnets.emp.entity.ydyw;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 业务与短信贴尾及模板关系
 * @author Administrator
 *
 */
public class LfBusTailTmp implements Serializable {
	  //自增
	  private Integer id;
	  //业务ID
	  private Integer bus_id;
	  //贴尾模板ID
	  private Integer smstail_id;
	  //短信模板ID
	  private Integer tm_id;
	  //企业编码
	  private String corp_code;
	  //创建时间
	  private Timestamp create_time;
	  //修改时间
	  private Timestamp update_time;
	  //操作员ID
	  private Integer user_id;
	  //操作员机构ID
	  private Integer dep_id;
	  //关联类型（0：业务对应贴尾；1：业务对应短信模板）
	  private Integer associate_type;
	  
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBus_id() {
		return bus_id;
	}
	public void setBus_id(Integer busId) {
		bus_id = busId;
	}
	public Integer getSmstail_id() {
		return smstail_id;
	}
	public void setSmstail_id(Integer smstailId) {
		smstail_id = smstailId;
	}
	public Integer getTm_id() {
		return tm_id;
	}
	public void setTm_id(Integer tmId) {
		tm_id = tmId;
	}
	public String getCorp_code() {
		return corp_code;
	}
	public void setCorp_code(String corpCode) {
		corp_code = corpCode;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp createTime) {
		create_time = createTime;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp updateTime) {
		update_time = updateTime;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer userId) {
		user_id = userId;
	}
	public Integer getDep_id() {
		return dep_id;
	}
	public void setDep_id(Integer depId) {
		dep_id = depId;
	}
	public Integer getAssociate_type() {
		return associate_type;
	}
	public void setAssociate_type(Integer associateType) {
		associate_type = associateType;
	}
	  
	  
}
