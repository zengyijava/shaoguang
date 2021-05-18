package com.montnets.emp.qyll.dao.sql;

import java.util.Calendar;
import com.montnets.emp.qyll.vo.OrderDetailVO;
import com.montnets.emp.qyll.vo.OrderTaskVO;

/**
 * @author Jason Huang
 * @date 2017年12月28日 下午4:08:38
 */

/**
 * SQL父类基于Oracle编写
 */
public abstract class DataQuerySQL {
	// 获取当前系统年份作为详情表表名后缀(**************感觉这样会有BUG**************)
	protected static String detailTable = "LL_ORDER_DETAIL" + String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

	public String getOrderDetailFiledSQL() {
		return "SELECT LL_ORDER_DETAIL.MOBILE,LL_ORDER_DETAIL.ERRCODE,LL_ORDER_DETAIL.ORDERTM,LL_ORDER_DETAIL.LLRPT,LL_ORDER_DETAIL.RPTTM,LL_ORDER_DETAIL.ORDERNO,LL_PRODUCT.PRODUCTNAME,LL_PRODUCT.ISP,LF_DEP.DEP_NAME,LF_SYSUSER.NAME";
	}

	public String getOrderTaskFiledSQL() {
		return "SELECT LL_ORDER_TASK.ID,LF_SYSUSER.NAME,(SELECT LL_ORDER_DETAIL.MSG FROM "
				+ detailTable
				+ " LL_ORDER_DETAIL WHERE LL_ORDER_TASK.ORDERNO = LL_ORDER_DETAIL.ORDERNO AND ROWNUM = 1)AS MSG,LF_DEP.DEP_NAME,LL_ORDER_TASK.ORDERNO,LL_ORDER_TASK.TOPIC,LL_ORDER_TASK.PRO_IDS,LL_ORDER_TASK.ORDERSTATUS,LL_ORDER_TASK.SMSSTATUS,LL_ORDER_TASK.CREATETM,LL_ORDER_TASK.ORDERTM,LL_ORDER_TASK.SUBCOUNT,"
				+ " (SELECT COUNT(*) FROM " + detailTable
				+ " LL_ORDER_DETAIL WHERE LL_ORDER_DETAIL.ORDERNO = LL_ORDER_TASK.ORDERNO AND LLRPT = '1') AS SUCCOUNT";
	}

	public String getProductFiledSQL() {
		return "SELECT MAX(LL_PRODUCT.PRODUCTNAME) AS PRODUCTNAME,MAX(LL_PRODUCT.ISP) AS ISP,COUNT(*) AS COUNT,MAX(LL_PRODUCT.PRODUCTID) AS PRODUCTID";
	}

	public String getOrderDetailTableSQL() {
		StringBuilder tableSQL = new StringBuilder(" FROM ").append(detailTable).append(" LL_ORDER_DETAIL")
				.append(" LEFT JOIN LL_PRODUCT ON (LL_ORDER_DETAIL.PRODUCTID = LL_PRODUCT.PRODUCTID AND LL_PRODUCT.STATUS = 1)")
				.append(" LEFT JOIN LF_DEP ON (LL_ORDER_DETAIL.ORG_ID = LF_DEP.DEP_ID)")
				.append(" LEFT JOIN LF_SYSUSER ON (LL_ORDER_DETAIL.USER_ID = LF_SYSUSER.USER_ID)").append(" WHERE 1=1");
		return tableSQL.toString();
	}
	
	public String getOrderDetailTableSQL(String ecid) {
		StringBuilder tableSQL = new StringBuilder(" FROM ").append(detailTable).append(" LL_ORDER_DETAIL")
				.append(" INNER JOIN LL_PRODUCT ON (LL_ORDER_DETAIL.PRODUCTID = LL_PRODUCT.PRODUCTID AND LL_PRODUCT.ECID = "+ecid+" AND LL_PRODUCT.STATUS = 1)")
				.append(" LEFT JOIN LF_DEP ON (LL_ORDER_DETAIL.ORG_ID = LF_DEP.DEP_ID)")
				.append(" LEFT JOIN LF_SYSUSER ON (LL_ORDER_DETAIL.USER_ID = LF_SYSUSER.USER_ID)").append(" WHERE 1=1");
		return tableSQL.toString();
	}

	public String getOrderTaskTableSQL() {
		StringBuilder tableSQL = new StringBuilder(" FROM LL_ORDER_TASK")
				.append(" LEFT JOIN LF_DEP ON (LL_ORDER_TASK.ORG_ID = LF_DEP.DEP_ID)")
				.append(" LEFT JOIN LF_SYSUSER ON (LL_ORDER_TASK.USER_ID = LF_SYSUSER.USER_ID)").append(" WHERE 1=1");
		return tableSQL.toString();
	}

	public String getProductTableSQL() {
		StringBuilder tableSQL = new StringBuilder(" FROM ").append(detailTable).append(" LL_ORDER_DETAIL")
				.append(" LEFT JOIN LL_PRODUCT ON (LL_PRODUCT.PRODUCTID = LL_ORDER_DETAIL.PRODUCTID AND LL_PRODUCT.STATUS = 1)")
				.append(" WHERE 1=1");
		return tableSQL.toString();
	}

	/**
	 * 根据对象存在属性拼接SQL,空字符串已在SVT层过滤
	 * @param orderDetail
	 * @return
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
				conditionSQL.append(" AND LL_PRODUCT.PRODUCTID = '" + orderDetail.getTheme() + "'");
			}
			if (orderDetail.getOrderno() != null) {
				conditionSQL.append(" AND LL_ORDER_DETAIL.ORDERNO ='" + orderDetail.getOrderno() + "'");
			}
			if (orderDetail.getIsp() != null) {
				conditionSQL.append(" AND LL_PRODUCT.ISP ='" + orderDetail.getIsp() + "'");
			}
			if (orderDetail.getState() != null && orderDetail.getState().equals("detail")) {
				conditionSQL.append(" AND  LL_ORDER_DETAIL.LLRPT IN('0','1','2','4')");
			} else if (orderDetail.getState() != null) {
				conditionSQL.append(" AND LL_ORDER_DETAIL.LLRPT ='" + orderDetail.getState() + "'");
			}
			if (orderDetail.getStartTime() != null) {
				conditionSQL.append(" AND LL_ORDER_DETAIL.ORDERTM >= '" + orderDetail.getStartTime() + "'");
			}
			if (orderDetail.getEndTime() != null) {
				conditionSQL.append(" AND LL_ORDER_DETAIL.ORDERTM <= '" + orderDetail.getEndTime() + "'");
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
				conditionSQL.append(" AND LL_ORDER_TASK.ORDERTM >= '" + orderTask.getStartTime() + "'");
			}
			if (orderTask.getEndTime() != null) {
				conditionSQL.append(" AND LL_ORDER_TASK.ORDERTM <= '" + orderTask.getEndTime() + "'");
			}
		}
		return conditionSQL.toString();
	}

	public String getProductConditionSQL(String ids, String orderNo) {
		StringBuilder conditionSQL = new StringBuilder();
		if (orderNo != null && orderNo.trim().length() > 0) {
			conditionSQL.append(" AND LL_ORDER_DETAIL.ORDERNO = '" + orderNo + "'");
		}
		if (ids != null && ids.trim().length() > 0) {
			conditionSQL.append(" AND LL_PRODUCT.PRODUCTID IN (" + ids + ")");
		}
		conditionSQL.append(" GROUP BY LL_ORDER_DETAIL.PRODUCTID");
		return conditionSQL.toString();
	}

	public String getOrderDetailOrderBySQL() {
		// 根据流量返回状态报告时间降序
		return " ORDER BY RPTTM DESC";
	}
	
	public String getOrderDetailOrderByMobileSQL() {
		// 根据手机号码升序
		return " ORDER BY MOBILE";
	}

	public String getOrderTaskOrderBySQL() {
		// 根据订购时间降序
		return " ORDER BY ORDERTM DESC";
	}

}