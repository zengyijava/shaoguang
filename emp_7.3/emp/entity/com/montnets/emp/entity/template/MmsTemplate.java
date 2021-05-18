package com.montnets.emp.entity.template;

import java.io.Serializable;
import java.sql.Timestamp;

public class MmsTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1368188826486433369L;
	
	//自增ID
	private Long id;
	//模板ID，唯一索引
	private Long tmplId;
	//用户帐号，用来标识该模板归属的用户
	private String userId;
	//审核状态 
	//0：未审核
	//1：审核通过
	//2：审核不通过
	private Integer auditStatus;
	//模板状态
	//0：正常，可用
	//1：锁定，暂时不可用
	//2：永久锁定，永远不可用
	private Integer tmplStatus;
	//模板中参数的个数
	private Integer paramCnt;
	//模板文件存储过的路径
	private String tmplPath;
	//模板接收时间
	private Timestamp recvTime;
	//审核时间
	private Timestamp auditTime;
	//审核员
	private String auditor;
	//备注（审核通过或不通过的理由等）
	private String remarks;
	//预留字段
	private String reServe1;
	//预留字段
	private String reServe2;
	//预留字段
	private String reServe3;
	//预留字段
	private String reServe4;
	//预留字段
	private String reServe5;
	//提交状态 
	//	0：未提交
	//	1：提交成功
	//	2：提交失败
	private Integer submitstatus;
	private String emptemplid;
	
	
	public String getEmptemplid() {
		return emptemplid;
	}
	public void setEmptemplid(String emptemplid) {
		this.emptemplid = emptemplid;
	}
	public Integer getSubmitstatus() {
		return submitstatus;
	}
	public void setSubmitstatus(Integer submitstatus) {
		this.submitstatus = submitstatus;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTmplId() {
		return tmplId;
	}
	public void setTmplId(Long tmplId) {
		this.tmplId = tmplId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Integer getTmplStatus() {
		return tmplStatus;
	}
	public void setTmplStatus(Integer tmplStatus) {
		this.tmplStatus = tmplStatus;
	}
	public Integer getParamCnt() {
		return paramCnt;
	}
	public void setParamCnt(Integer paramCnt) {
		this.paramCnt = paramCnt;
	}
	public String getTmplPath() {
		return tmplPath;
	}
	public void setTmplPath(String tmplPath) {
		this.tmplPath = tmplPath;
	}
	public Timestamp getRecvTime() {
		return recvTime;
	}
	public void setRecvTime(Timestamp recvTime) {
		this.recvTime = recvTime;
	}
	public Timestamp getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getReServe1() {
		return reServe1;
	}
	public void setReServe1(String reServe1) {
		this.reServe1 = reServe1;
	}
	public String getReServe2() {
		return reServe2;
	}
	public void setReServe2(String reServe2) {
		this.reServe2 = reServe2;
	}
	public String getReServe3() {
		return reServe3;
	}
	public void setReServe3(String reServe3) {
		this.reServe3 = reServe3;
	}
	public String getReServe4() {
		return reServe4;
	}
	public void setReServe4(String reServe4) {
		this.reServe4 = reServe4;
	}
	public String getReServe5() {
		return reServe5;
	}
	public void setReServe5(String reServe5) {
		this.reServe5 = reServe5;
	}
}
