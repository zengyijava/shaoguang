package com.montnets.emp.netnews.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Vincent
 * @version 1.0
 * describe 网讯下载文件管理（LF_WX_UPLOADFILE）
 * date 2011.11.5
 * **/

public class LfWXUploadFile implements Serializable{
	
	private static final long serialVersionUID = 1L;  

	private Long ID = 0L;					//文件编号
	private Long NETID = 0L;				//关联网讯编号
	private String FILENAME = "";		//文件名称
	private String FILEVER = "";		//文件版本
	private String FILEDESC	= "";		//文件描述
	private Timestamp UPLOADDATE;		//上传日期
	private Long UPLOADUSERID = 0L;		//上传人ID
	private String WEBURL = "";			//文件URL地址
	private Integer STATUS = 0;				//审核状态。0：未审核；1：已审核
	private Long MODIFYID = 0L;			//最后修改用户ID
	private Timestamp MODIFYDATE;		//最后修改时间
	private Long CREATID = 0L;			//创建用户ID
	private Timestamp CREATDATE;		//创建时间
	private String NAMETEMP;            //
	private String SORTID;            //文件类型id
	private String type;
	//此处增加公司的判断，区分不同公司素材
	private String CORP_CODE;  //公司编码
	/*
	//附加属性
	private String DATE = "";			//日期时间
	private String TYPENAME="";      //类型名称
	private int FILETYPE=0;      //文件类型
	private int USERTYPE=0;      //用户类型
	
	*/
		private Long FILESIZE;
	
	public Long getFILESIZE() {
		return FILESIZE;
	}

	public void setFILESIZE(Long fILESIZE) {
		FILESIZE = fILESIZE;
	}
	public String getNAMETEMP() {
		return NAMETEMP;
	}
	
	public String getCORP_CODE()
	{
		return CORP_CODE;
	}

	public void setCORP_CODE(String cORPCODE)
	{
		CORP_CODE = cORPCODE;
	}

	public Integer getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(Integer sTATUS) {
		STATUS = sTATUS;
	}



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setNAMETEMP(String nametemp) {
		NAMETEMP = nametemp;
	}
	public String getFILENAME() {
		return FILENAME;
	}
	public void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}
	public String getFILEVER() {
		return FILEVER;
	}
	public void setFILEVER(String fILEVER) {
		FILEVER = fILEVER;
	}
	public String getFILEDESC() {
		return FILEDESC;
	}
	public void setFILEDESC(String fILEDESC) {
		FILEDESC = fILEDESC;
	}
	public Timestamp getUPLOADDATE() {
		return UPLOADDATE;
	}
	public void setUPLOADDATE(Timestamp uPLOADDATE) {
		UPLOADDATE = uPLOADDATE;
	}
	public String getWEBURL() {
		return WEBURL;
	}
	public void setWEBURL(String wEBURL) {
		WEBURL = wEBURL;
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
	/*
	public String getTYPENAME() {
		return TYPENAME;
	}
	public void setTYPENAME(String typename) {
		TYPENAME = typename;
	}
	public String getDATE() {
		return DATE;
	}
	public void setDATE(String date) {
		DATE = date;
	}
	public int getFILETYPE() {
		return FILETYPE;
	}
	public void setFILETYPE(int filetype) {
		FILETYPE = filetype;
	}
	public int getUSERTYPE() {
		return USERTYPE;
	}
	public void setUSERTYPE(int usertype) {
		USERTYPE = usertype;
	}
	*/
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public Long getNETID() {
		return NETID;
	}
	public void setNETID(Long nETID) {
		NETID = nETID;
	}
	public Long getUPLOADUSERID() {
		return UPLOADUSERID;
	}
	public void setUPLOADUSERID(Long uPLOADUSERID) {
		UPLOADUSERID = uPLOADUSERID;
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
	public String getSORTID() {
		return SORTID;
	}
	public void setSORTID(String sORTID) {
		SORTID = sORTID;
	}
	

}
