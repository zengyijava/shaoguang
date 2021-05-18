package com.montnets.emp.servmodule.txgl.entity;

import java.sql.Timestamp;


public class AcmdRoute implements java.io.Serializable{

	/**
	 * 上行业务指令绑定
	 */
	private static final long serialVersionUID = -4170642395356731527L;
	
	private Long id;  // 主键ID 自动增长
	private String name;    // 指令名称
	private String structcode; //指令编码
	private String bussysname;
	private Long spid;   //绑定SP	帐号ID
	private String creater;  // 创建者
	private Timestamp creattime; // 创建时间
	private String tructtype;// 指令类型
	private String status;  // 指令使用状态  01启用 02绑定 -->用于上行智能引擎业务绑定
	private Integer matchmode;
	
	public AcmdRoute(){
		matchmode=1;
	}
	
	
	public Integer getMatchmode() {
		return matchmode;
	}
	public void setMatchmode(Integer matchmode) {
		this.matchmode = matchmode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStructcode() {
		return structcode;
	}
	public void setStructcode(String structcode) {
		this.structcode = structcode;
	}
	
	public Long getSpid() {
		return spid;
	}
	public void setSpid(Long spid) {
		this.spid = spid;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBussysname() {
		return bussysname;
	}
	public void setBussysname(String bussysname) {
		this.bussysname = bussysname;
	}
	public String getTructtype() {
		return tructtype;
	}
	public void setTructtype(String tructtype) {
		this.tructtype = tructtype;
	}
	public Timestamp getCreattime() {
		return creattime;
	}
	public void setCreattime(Timestamp creattime) {
		this.creattime = creattime;
	}
	
}
