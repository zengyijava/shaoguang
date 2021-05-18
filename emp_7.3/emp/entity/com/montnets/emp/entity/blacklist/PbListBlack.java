package com.montnets.emp.entity.blacklist;

import java.sql.Timestamp;


/**
 * TablePbListBlack对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:29:22
 * @description 
 */
public class PbListBlack implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6393363309449428123L;
	private Long id;
	private String userId;
	private String spgate;
	private String spnumber;
	private Long phone;
	private Integer optype;
	private Timestamp optTime;
	private String corpCode;
	private String svrType;
	private String msg=" ";
	//黑名单类型。1：短信；2：彩信
	private Integer blType;
	
	public PbListBlack() {}

	 
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getSpgate()
	{
		return spgate;
	}

	public void setSpgate(String spgate)
	{
		this.spgate = spgate;
	}

	public String getSpnumber()
	{
		return spnumber;
	}

	public void setSpnumber(String spnumber)
	{
		this.spnumber = spnumber;
	}

	public Long getPhone() {
		return phone;
	}


	public void setPhone(Long phone) {
		this.phone = phone;
	}


	public Timestamp getOptTime() {
		return optTime;
	}


	public void setOptTime(Timestamp optTime) {
		this.optTime = optTime;
	}


	public Integer getOptype()
	{
		return optype;
	}

	public void setOptype(Integer optype)
	{
		this.optype = optype;
	}


	public String getCorpCode() {
		return corpCode;
	}


	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}


	public String getSvrType() {
		return svrType;
	}


	public void setSvrType(String svrType) {
		this.svrType = svrType;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public Integer getBlType()
	{
		return blType;
	}


	public void setBlType(Integer blType)
	{
		this.blType = blType;
	}
	
	
  

}