package com.montnets.emp.netnews.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Vincent
 * @version 1.0
 * describe 页面存储表(LF_WX_PAGE)
 * date 2011.11.5
 * **/

public class LfWXPAGE implements Serializable{
	
	private Long ID = 0L;					//网讯编号
	private String NAME ="";
	private Long NETID = 0L;				//关联网讯编号
	private Long PARENTID = 0L;			//页面父节点
	private Long CHILDID = 0L;			//页面子节点
	private String CONTENT = "";		//页面内容
	private Integer PAGESIZE = 0;			//页面大小
	private Long MODIFYID = 0L;			//最后修改用户ID
	private Timestamp MODIFYDATE;		//最后修改时间
	private Long CREATID = 0L;			//创建用户ID
	private Timestamp CREATDATE;		//创建时间
	private String link = "";			//超链接替换标识
	
	
	public LfWXPAGE(Long id, String name, Long netid, Long parentid, Long childid,
			String content, Integer pagesize, Long modifyid, Timestamp modifydate,
			Long creatid, Timestamp creatdate,String link) {
		super();
		ID = id;
		NAME = name;
		NETID = netid;
		PARENTID = parentid;
		CHILDID = childid;
		CONTENT = content;
		PAGESIZE = pagesize;
		MODIFYID = modifyid;
		MODIFYDATE = modifydate;
		CREATID = creatid;
		CREATDATE = creatdate;
		this.link = link; 
	}

	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}
	public Integer getPAGESIZE() {
		return PAGESIZE;
	}
	public void setPAGESIZE(Integer pAGESIZE) {
		PAGESIZE = pAGESIZE;
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
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String name) {
		NAME = name;
	}
	
	  
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

	public Long getPARENTID() {
		return PARENTID;
	}

	public void setPARENTID(Long pARENTID) {
		PARENTID = pARENTID;
	}

	public Long getCHILDID() {
		return CHILDID;
	}

	public void setCHILDID(Long cHILDID) {
		CHILDID = cHILDID;
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

	@Override
  public String toString() {
		//增加content字段，用于页面处理
  	String json="{id:%d,netid:%d,name:'%s',parentid:'%d',cteateid:'%d',content:'%s'}";
  	
  	return String.format(json,this.getID(), this.getNETID(),this.getNAME(),this.getPARENTID(),this.getCREATID(),this.getCONTENT());
  }
    public LfWXPAGE(){
    	super();
    }
    


}
