package com.montnets.emp.entity.ydyw;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 短信贴尾实体类
 * @author Administrator
 *
 */
public class LfBusTail implements Serializable
{
	  //贴尾模板ID，自增
	  private Integer bustail_id;
	  //名称
	  private String bustail_name;
	  //企业编码
	  private String corp_code;
	  //内容
	  private String content;
	  //创建时间
	  private Timestamp create_time;
	  //修改时间
	  private Timestamp update_time;
	  //操作员ID
	  private Integer user_id;
	  //操作员机构ID
	  private Integer dep_id;

	  
	  
	public Integer getBustail_id() {
		return bustail_id;
	}
	public void setBustail_id(Integer bustailId) {
		bustail_id = bustailId;
	}
	public String getBustail_name() {
		return bustail_name;
	}
	public void setBustail_name(String bustailName) {
		bustail_name = bustailName;
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
	public String getCorp_code() {
		return corp_code;
	}
	public void setCorp_code(String corpCode) {
		corp_code = corpCode;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	  
}
