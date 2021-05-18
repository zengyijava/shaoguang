package com.montnets.emp.qyll.vo;

import java.sql.Timestamp;

/**
 * @author Jason Huang
 * @date 2017年10月31日 下午2:01:55
 */

public class OrderTaskVO {
	private String id;
	private String operator; // 操作员
	private String organization; // 机构
	private String topic;// 主题
	private String theme; // 流量套餐
	private String isp;// 运营商
	private String orderNo;// 订购编号
	private String ecid;// 企业ID
	private String msg;// 短信内容
	private String productIds;// 产品ID(多个)
	private String orderState;// 订购状态
	private boolean isOrdered; // 是否已订购
	private String smsState;// 短信状态
	private Timestamp createtm;// 创建时间
	private Timestamp ordertm;// 订购时间
	private String subCount;// 提交号码数
	// private String effCount;// 有效号码数
	private String sucCount;// 成功号码数
	// private String faiCount;// 失败号码数
	private String startTime;// 查询时使用的起始时间
	private String endTime;// 查询时使用的结束时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isOrdered() {
		return isOrdered;
	}

	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

	public String getEcid() {
		return ecid;
	}

	public void setEcid(String ecid) {
		this.ecid = ecid;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	public String getSucCount() {
		return sucCount;
	}

	public void setSucCount(String sucCount) {
		this.sucCount = sucCount;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getSmsState() {
		return smsState;
	}

	public void setSmsState(String smsState) {
		this.smsState = smsState;
	}

	public Timestamp getCreatetm() {
		return createtm;
	}

	public void setCreatetm(Timestamp createtm) {
		this.createtm = createtm;
	}

	public Timestamp getOrdertm() {
		return ordertm;
	}

	public void setOrdertm(Timestamp ordertm) {
		this.ordertm = ordertm;
	}

	public String getSubCount() {
		return subCount;
	}

	public void setSubCount(String subCount) {
		this.subCount = subCount;
	}

}
