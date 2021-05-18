package com.montnets.emp.netnews.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Vincent
 * @version 1.0
 * describe 类型存储表(LF_WX_SORT)
 * date 2011.11.5
 * **/

public class LfWXSORT implements Serializable{
	
	private Long ID ;					//类型编号
	private String NAME = "";			//类型名称
	private String DESCRIPTION = "";	//类型描述
	private Integer TYPE = 0;				//类型。0：网讯；1：素材
	private Long MODIFYID =new Long(0);			//最后修改用户ID
	private Timestamp MODIFYDATE =new Timestamp(new Date().getTime());		//最后修改时间
	private Long CREATID ;			//创建用户ID
	private Timestamp CREATDATE=new Timestamp(new Date().getTime());		//创建时间
	private Integer FILETYPE=0;             //素材管理文件类型
	private String corpCode;
	private Long parentId;
	private String sort_path=""; 
	
	public Integer getTYPE() {
		return TYPE;
	}
	public void setTYPE(Integer tYPE) {
		TYPE = tYPE;
	}
	
	public Integer getFILETYPE() {
		return FILETYPE;
	}
	public void setFILETYPE(Integer fILETYPE) {
		FILETYPE = fILETYPE;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public Timestamp getMODIFYDATE() {
		return MODIFYDATE;
	}
	public void setMODIFYDATE(Timestamp mODIFYDATE) {
		MODIFYDATE = mODIFYDATE;
	}
	public Timestamp getCREATDATE() {
		return CREATDATE;
	}
	public void setCREATDATE(Timestamp cREATDATE) {
		CREATDATE = cREATDATE;
	}
	
	
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public Long getMODIFYID() {
		return MODIFYID;
	}
	public void setMODIFYID(Long mODIFYID) {
		MODIFYID = mODIFYID;
	}
	public Long getCREATID() {
		return CREATID;
	}
	public void setCREATID(Long cREATID) {
		CREATID = cREATID;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getSort_path() {
		return sort_path;
	}
	public void setSort_path(String sortPath) {
		sort_path = sortPath;
	}
	
}
