package com.montnets.emp.qyll.vo;

import java.sql.Timestamp;

/**
 * @author Jason Huang
 * @date 2017年10月30日 下午4:22:39
 */

public class OrderDetailVO {
	private String mobile;// 手机
	private String errcode;// 错误码
	private String theme;// 主题
	private String isp;// 运营商
	private String state;// 状态
	private String orderno;// 订购编号
	private String organization; // 机构
	private String operator; // 操作员
	private Timestamp ordertm;// 订购时间
	private Timestamp rpttm;// 返回时间
	private String startTime;// 查询时使用的起始时间
	private String endTime;// 查询时使用的结束时间
	private String ids;// 产品ID(多个)
	private String ecid;// 企业ID

	public String getEcid() {
		return ecid;
	}

	public void setEcid(String ecid) {
		this.ecid = ecid;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
