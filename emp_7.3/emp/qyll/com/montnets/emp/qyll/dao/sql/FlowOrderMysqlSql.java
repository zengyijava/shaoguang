package com.montnets.emp.qyll.dao.sql;

import java.util.Date;

import com.montnets.emp.qyll.dao.IFlowOrderSql;

public class FlowOrderMysqlSql implements IFlowOrderSql{

	public String getInsertLlTaskSql() {
		return "Insert into ll_order_task" +
				"(taskid, ecid, user_id, org_id, topic, pro_ids, msgtype, temp_id, sp_user, sp_pwd, subcount, effcount, succount,"+
				"faicount, timer_status, timer_time, re_status, orderstatus, smsstatus, isretry, " +
				"submittm, ordertm, updatetm, createtm,orderno,summoney,reportdate,msg,p_ids) "+
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}

	public String getInsertLlOrderDetailsql() {
		String year = sdf.format(new Date());
		return "insert into ll_order_detail"+year+"("+
				  "mobile, msg, batchId, orderno, llrpt, user_id, org_id, pro_id, ordertm, rpttm, updatetm, createtm,productId,status,reportdate) "+
				  "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}

	public String getFindOrderDetailSql(int row) {
		String year = sdf.format(new Date());
		return "select a.id,a.mobile,a.orderno,a.productid, " +
				"   a.msg, b.sp_user , b.sp_pwd  " +
				"   from ll_order_detail"+ year +
				"	a left join  ll_order_task b on a.orderno = b.orderno " +
				"	where a.Status = 0 and a.orderno = ? and b.servernum = ?  limit 0,"+row;
	}

	public String getInsertLlStatusrptSql() {
		return "INSERT INTO LL_STATUSRPT( MOBILE, PRODUCTID, LLRPT, ORDERTM, ERRCODE, ORDERNO, CREATETM) " +
				" VALUES(?,?,?,?,?,?,?)";
	}
	
	public String findOrderNumUnsentSql() {
		return "SELECT ORDERNO,USER_ID,TIMER_TIME,RE_STATUS,TIMER_STATUS,ISRETRY FROM LL_ORDER_TASK WHERE TIMER_TIME < ? AND RE_STATUS IN ('0','2') AND SERVERNUM = '0' AND ORDERSTATUS NOT IN ('3','4','5')  LIMIT  500" ;
	}
	
}


