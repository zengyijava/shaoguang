package com.montnets.emp.netnews.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Vincent
 * @version 1.0
 * describe 业务数据结构表（LF_WX_TRUSTDATA_COLS）
 * date 2011.12.1
 * **/

@SuppressWarnings("serial")
public class LfWXTrustCols implements Serializable{
	
	private Long id = 0L;					//自动编号
	private Long trustId = 0L;			//业务数据编号
	private String name = "";			//显示名称
	private String colName = "";		//字段列名
	private Integer colType = 0;			//数据类型。0：字符串；1：数字；2：日期。
	private Integer colSize = 0;			//数据长度
	private Integer otherSize = 0;			//其他长度，如：小数位。
	private Integer isParam = 0;			//设为参数。0：否；1：是
	//private String ph ="未知";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public int getColType() {
		return colType;
	}
	public void setColType(int colType) {
		this.colType = colType;
	}
	public int getColSize() {
		return colSize;
	}
	public void setColSize(int colSize) {
		this.colSize = colSize;
	}
	public int getOtherSize() {
		return otherSize;
	}
	public void setOtherSize(int otherSize) {
		this.otherSize = otherSize;
	}
	public int getIsParam() {
		return isParam;
	}
	public void setIsParam(int isParam) {
		this.isParam = isParam;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTrustId() {
		return trustId;
	}
	public void setTrustId(Long trustId) {
		this.trustId = trustId;
	}
	public void setColType(Integer colType) {
		this.colType = colType;
	}
	public void setColSize(Integer colSize) {
		this.colSize = colSize;
	}
	public void setOtherSize(Integer otherSize) {
		this.otherSize = otherSize;
	}
	public void setIsParam(Integer isParam) {
		this.isParam = isParam;
	}
	
	
	/*public String getPh() {
		return ph;
	}

	public void setPh(String ph) {
		this.ph = ph;
	}*/

	
	
}
