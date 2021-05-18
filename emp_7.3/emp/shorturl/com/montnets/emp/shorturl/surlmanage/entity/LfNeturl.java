package com.montnets.emp.shorturl.surlmanage.entity;

import java.sql.Timestamp;

/**
 * url地址维护
 * @ClassName:LfNeturl.java
 * @Description:TODO
 * @author Administrator
 * @date:2018-1-11下午02:15:54
 */
public class LfNeturl {
	//id主键 
	private Long id;
	
	//源地址 
	private String srcurl;
	
	//连接名称
	private String urlname; 
	
	//内容描述
	private String urlmsg; 
	
	//企业编码 
	private String corpcode;
	
	//审批状态 -2 禁用  -1:删除   0：未审批, 1：无需审批 ,2：审批通过 （运营商侧），3审批不通过
	private Integer ispass;
	
	//-1：禁用 0：启用 （用户侧）  
	private Integer urlstate;
	
	//创建人
	private Integer createuid ;
	
	//创建时间
	private Timestamp createtm;
	
	//修改人员（用户侧）
	private Integer updateuid;
	
	//修改时间
	private Timestamp updatetm;
	
	//审核人id
	private Integer audituid; 
	
	//审核时间
	private Timestamp audittm;
	
	//禁用人员id（运营商侧）
	private Integer stopuid;
	
	//禁用时间
	private Timestamp stoptm;
	
	//审核意见 
	private String remarks;
	
	//预留字段1（禁用意见）
	private String remarks1 ;
	
	//预留字段2
	private String remarks2 ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSrcurl() {
		return srcurl;
	}

	public void setSrcurl(String srcurl) {
		this.srcurl = srcurl;
	}

	public String getUrlname() {
		return urlname;
	}

	public void setUrlname(String urlname) {
		this.urlname = urlname;
	}

	public String getUrlmsg() {
		return urlmsg;
	}

	public void setUrlmsg(String urlmsg) {
		this.urlmsg = urlmsg;
	}

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}

	public Integer getIspass() {
		return ispass;
	}

	public void setIspass(Integer ispass) {
		this.ispass = ispass;
	}

	public Integer getUrlstate() {
		return urlstate;
	}

	public void setUrlstate(Integer urlstate) {
		this.urlstate = urlstate;
	}

	public Integer getCreateuid() {
		return createuid;
	}

	public void setCreateuid(Integer createuid) {
		this.createuid = createuid;
	}

	public Timestamp getCreatetm() {
		return createtm;
	}

	public void setCreatetm(Timestamp createtm) {
		this.createtm = createtm;
	}
	
	public Integer getUpdateuid() {
		return updateuid;
	}

	public void setUpdateuid(Integer updateuid) {
		this.updateuid = updateuid;
	}

	public Timestamp getUpdatetm() {
		return updatetm;
	}

	public void setUpdatetm(Timestamp updatetm) {
		this.updatetm = updatetm;
	}

	public Integer getAudituid() {
		return audituid;
	}

	public void setAudituid(Integer audituid) {
		this.audituid = audituid;
	}

	public Timestamp getAudittm() {
		return audittm;
	}

	public void setAudittm(Timestamp audittm) {
		this.audittm = audittm;
	}

	public Integer getStopuid() {
		return stopuid;
	}

	public void setStopuid(Integer stopuid) {
		this.stopuid = stopuid;
	}

	public Timestamp getStoptm() {
		return stoptm;
	}

	public void setStoptm(Timestamp stoptm) {
		this.stoptm = stoptm;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		if("".equals(remarks)){
			this.remarks = " ";
		}else{
			this.remarks = remarks;
		}
	}

	public String getRemarks1() {
		return remarks1;
	}

	public void setRemarks1(String remarks1) {
		if("".equals(remarks1)){
			this.remarks1 = " ";
		}else{
			this.remarks1 = remarks1;
		}
	}

	public String getRemarks2() {
		return remarks2;
	}

	public void setRemarks2(String remarks2) {
		if("".equals(remarks2)){
			this.remarks2 = " ";
		}else{
			this.remarks2 = remarks2;
		}
	}

	
}
