package com.montnets.emp.qyll.entity;

import java.util.List;

/**
 * 订购流量包报文实体
 * @author Administrator
 *
 */
public class EMI1001 {
	
	private String ECOrderId;
	
	private List<OrderList> OrderList;

	public String getECOrderId() {
		return ECOrderId;
	}

	public void setECOrderId(String eCOrderId) {
		ECOrderId = eCOrderId;
	}

	public List<OrderList> getOrderList() {
		return OrderList;
	}

	public void setOrderList(List<OrderList> orderList) {
		OrderList = orderList;
	}
	
	/*//暂时不用
	private  String  OrderTime;
	
	private String Activity;
	
	private String Operator;*/
	
	
}
