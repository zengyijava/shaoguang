package com.montnets.emp.qyll.dao.sql;

import com.montnets.emp.qyll.vo.OrderDetailVO;
import com.montnets.emp.qyll.vo.OrderTaskVO;

/**
 * @author Jason Huang
 * @date 2017年12月28日 下午4:08:57
 */

public class DataQueryOracleSQL extends DataQuerySQL {
	
	/**
	 * 根据对象存在属性拼接SQL,空字符串已在SVT层过滤,Oracle中的时间函数进行特殊处理
	 */
	public String getOrderDetailConditionSQL(OrderDetailVO orderDetail) {
		StringBuilder conditionSQL = new StringBuilder();
		if (orderDetail != null) {
			if (orderDetail.getOperator() != null) {
				String[] names = orderDetail.getOperator().split(";");
				conditionSQL.append(" AND LF_SYSUSER.NAME IN(");
				for (int i = 0; i < names.length; i++) {
					if (!names[i].trim().equals("")) {
						if (i == 0) {
							conditionSQL.append("'" + names[i].trim() + "'");
						} else {
							conditionSQL.append(",'" + names[i].trim() + "'");
						}
					}
				}
				conditionSQL.append(")");
			}
			if (orderDetail.getOrganization() != null) {
				String[] names = orderDetail.getOrganization().split(";");
				conditionSQL.append(" AND LF_DEP.DEP_NAME IN(");
				for (int i = 0; i < names.length; i++) {
					if (!names[i].trim().equals("")) {
						if (i == 0) {
							conditionSQL.append("'" + names[i].trim() + "'");
						} else {
							conditionSQL.append(",'" + names[i].trim() + "'");
						}
					}
				}
				conditionSQL.append(")");
			}
			if (orderDetail.getMobile() != null) {
				conditionSQL.append(" AND LL_ORDER_DETAIL.MOBILE ='" + orderDetail.getMobile() + "'");
			}
			if (orderDetail.getTheme() != null) {
				conditionSQL.append(" AND LL_PRODUCT.PRODUCTID LIKE'" + orderDetail.getTheme() + "'");
			}
			if (orderDetail.getOrderno() != null) {
				conditionSQL.append(" AND LL_ORDER_DETAIL.ORDERNO ='" + orderDetail.getOrderno() + "'");
			}
			if (orderDetail.getIsp() != null) {
				conditionSQL.append(" AND LL_PRODUCT.ISP ='" + orderDetail.getIsp() + "'");
			}
			if (orderDetail.getState() != null && orderDetail.getState().equals("detail")) {
				conditionSQL.append(" AND  LL_ORDER_DETAIL.LLRPT IN('0','1','2')");
			} else if (orderDetail.getState() != null) {
				conditionSQL.append(" AND LL_ORDER_DETAIL.LLRPT ='" + orderDetail.getState() + "'");
			}
			if (orderDetail.getStartTime() != null) {
				conditionSQL.append(" AND LL_ORDER_DETAIL.ORDERTM >=TO_DATE('" + orderDetail.getStartTime()
						+ "','yyyy-mm-dd hh24:mi:ss')");
			}
			if (orderDetail.getEndTime() != null) {
				conditionSQL.append(" AND LL_ORDER_DETAIL.ORDERTM <=TO_DATE('" + orderDetail.getEndTime()
						+ "','yyyy-mm-dd hh24:mi:ss')");
			}
			if (orderDetail.getIds() != null) {
				conditionSQL.append(" AND LL_ORDER_DETAIL.PRODUCTID IN(" + orderDetail.getIds() + ")");
			}
		}
		return conditionSQL.toString();
	}
	
	public String getOrderTaskConditionSQL(OrderTaskVO orderTask) {
		StringBuilder conditionSQL = new StringBuilder();
		if (orderTask != null) {
			if (orderTask.getOperator() != null) {
				String[] names = orderTask.getOperator().split(";");
				conditionSQL.append(" AND LF_SYSUSER.NAME IN(");
				for (int i = 0; i < names.length; i++) {
					if (!names[i].trim().equals("")) {
						if (i == 0) {
							conditionSQL.append("'" + names[i].trim() + "'");
						} else {
							conditionSQL.append(",'" + names[i].trim() + "'");
						}
					}
				}
				conditionSQL.append(")");
			}
			if (orderTask.getOrganization() != null) {
				String[] names = orderTask.getOrganization().split(";");
				conditionSQL.append(" AND LF_DEP.DEP_NAME IN(");
				for (int i = 0; i < names.length; i++) {
					if (!names[i].trim().equals("")) {
						if (i == 0) {
							conditionSQL.append("'" + names[i].trim() + "'");
						} else {
							conditionSQL.append(",'" + names[i].trim() + "'");
						}
					}
				}
				conditionSQL.append(")");
			}
			if (orderTask.getOrderNo() != null) {
				conditionSQL.append(" AND LL_ORDER_TASK.ORDERNO ='" + orderTask.getOrderNo() + "'");
			}
			if (orderTask.getEcid() != null) {
				conditionSQL.append(" AND LL_ORDER_TASK.ECID =" + orderTask.getEcid());
			}
			if (orderTask.getTopic() != null) {
				conditionSQL.append(" AND LL_ORDER_TASK.TOPIC LIKE'" + orderTask.getTopic() + "%'");
			}
			if (orderTask.getTheme() != null) {
				conditionSQL.append(" AND LL_ORDER_TASK.PRO_IDS LIKE '%" + orderTask.getTheme() + "%'");
			}
			if (orderTask.getOrderState() != null) {
				conditionSQL.append(" AND LL_ORDER_TASK.ORDERSTATUS ='" + orderTask.getOrderState() + "'");
				// 查询条件没有指定状态时,查询已订购的记录
			} else if (orderTask.isOrdered() == true) {
				conditionSQL.append(" AND LL_ORDER_TASK.ORDERSTATUS = '0'");
				// //查询条件没有指定状态时,查询非已订购的记录
			} else if (orderTask.isOrdered() == false) {
				conditionSQL.append(" AND LL_ORDER_TASK.ORDERSTATUS <> '0'");
			}
			if (orderTask.getStartTime() != null) {
				conditionSQL.append(" AND LL_ORDER_TASK.ORDERTM >=TO_DATE('" + orderTask.getStartTime()
						+ "','yyyy-mm-dd hh24:mi:ss')");
			}
			if (orderTask.getEndTime() != null) {
				conditionSQL.append(" AND LL_ORDER_TASK.ORDERTM <=TO_DATE('" + orderTask.getEndTime()
						+ "','yyyy-mm-dd hh24:mi:ss')");
			}
		}
		return conditionSQL.toString();
	}

}
