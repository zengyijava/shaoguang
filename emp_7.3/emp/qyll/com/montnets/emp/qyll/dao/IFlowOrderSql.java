package com.montnets.emp.qyll.dao;

import java.text.SimpleDateFormat;

/**
 * 流量订购模块SQL接口
 * @author xiebk
 */
public interface IFlowOrderSql {
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
	/**
	 * 插入ll_order_task表的sql
	 * @return
	 */
	public  String getInsertLlTaskSql();
	
	/**
	 * 插入ll_order_detail表的SQL
	 * @return
	 */
	public  String getInsertLlOrderDetailsql();
	
	/**
	 * 查询ll_order_detail表的SQL
	 * @param row
	 * @return
	 */
	public  String getFindOrderDetailSql(int row);
	
	/**
	 * 插入ll_status_rpt的sql
	 * @return
	 */
	public  String getInsertLlStatusrptSql();
	
	/**
	 * 查询未处理的订单编号的sql
	 * @return
	 */
	public String findOrderNumUnsentSql();

}
