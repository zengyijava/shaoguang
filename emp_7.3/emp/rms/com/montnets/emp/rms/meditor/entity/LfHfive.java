package com.montnets.emp.rms.meditor.entity;

import java.sql.Timestamp;

public class LfHfive {
	//id
	private Integer hId;
	//h5标题
	private String title;
	//创建人
	private String author;
	//创建时间
	private Timestamp createTime;
	//提交时间
	private Timestamp commitTime;
	//存放路径
	private String url;
	//修改时间
	private Timestamp updateTime;
	//最后一次的使用时间
	private Timestamp useTime;
	//状态
	private Integer staus;
	
	
	
	public Integer getHId() {
		return hId;
	}
	public void setHId(Integer hId) {
		this.hId = hId;
	}
	public Integer getStaus() {
		return staus;
	}
	public void setStaus(Integer staus) {
		this.staus = staus;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(Timestamp commitTime) {
		this.commitTime = commitTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Timestamp getUseTime() {
		return useTime;
	}
	public void setUseTime(Timestamp useTime) {
		this.useTime = useTime;
	}
	
}
