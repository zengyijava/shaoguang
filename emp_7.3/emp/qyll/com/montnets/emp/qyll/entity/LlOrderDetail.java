package com.montnets.emp.qyll.entity;

import java.sql.Timestamp;
import java.util.Arrays;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.Logger;

public class LlOrderDetail extends LLOrderTask implements Cloneable{
	private int id; 
	private String mobile; 
	private String msg; 
	private int batchId; 
	private String orderno; 
	private String llrpt; 
	private int user_id; 
	private long org_id; 
	private int pro_id;
	private String errCode;
	private String productId;
	private Timestamp ordertm; 
	private Timestamp rpttm; 
	private Timestamp updatetm; 
	private Timestamp createtm;
	private int status;
	//产品编号 移动-联通-电信
	private String proIds [] ={"-1","-1","-1"};
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getLlrpt() {
		return llrpt;
	}
	public void setLlrpt(String llrpt) {
		this.llrpt = llrpt;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
	public int getPro_id() {
		return pro_id;
	}
	public void setPro_id(int pro_id) {
		this.pro_id = pro_id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Timestamp getOrdertm() {
		return ordertm;
	}
	public void setOrdertm(Timestamp ordertm) {
		this.ordertm = ordertm;
	}
	public Timestamp getRpttm() {
		return rpttm;
	}
	public void setRpttm(Timestamp rpttm) {
		this.rpttm = rpttm;
	}
	public Timestamp getUpdatetm() {
		return updatetm;
	}
	public void setUpdatetm(Timestamp updatetm) {
		this.updatetm = updatetm;
	}
	public Timestamp getCreatetm() {
		return createtm;
	}
	public void setCreatetm(Timestamp createtm) {
		this.createtm = createtm;
	}
	public String[] getProIds() {
		return proIds;
	}
	public void setProIds(String[] proIds) {
		this.proIds = proIds;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		LlOrderDetail lod = null;
		try {	
			lod =(LlOrderDetail) super.clone();
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "对象LlOrderDetail clone失败",Logger.LEVEL_ERROR );
		}
		return lod;
	}
	@Override
	public String toString() {
		return "LlOrderDetail [id=" + id + ", mobile=" + mobile + ", msg="
				+ msg + ", batchId=" + batchId + ", orderno=" + orderno
				+ ", llrpt=" + llrpt + ", user_id=" + user_id + ", org_id="
				+ org_id + ", pro_id=" + pro_id + ", productId=" + productId
				+ ", ordertm=" + ordertm + ", rpttm=" + rpttm + ", updatetm="
				+ updatetm + ", createtm=" + createtm + ", proIds="
				+ Arrays.toString(proIds) + "]"+",status="+status;
	}
	
}
