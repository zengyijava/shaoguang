package com.montnets.emp.qyll.dao.sql;

import java.util.Date;

import com.montnets.emp.qyll.dao.IFlowOrderSql;

public class FlowOrderOracleSql implements IFlowOrderSql{

	public String getInsertLlTaskSql() {
		return "Insert into ll_order_task" +
				"(id, taskid, ecid, user_id, org_id, topic, pro_ids, msgtype, temp_id, sp_user, sp_pwd, subcount, effcount, succount,"+
				"faicount, timer_status, timer_time, re_status, orderstatus, smsstatus, isretry, " +
				"submittm, ordertm, updatetm, createtm,orderno,summoney,reportdate,msg,p_ids) "+
				"values(seq_ll_order_task.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}

	public String getInsertLlOrderDetailsql() {
		String year = sdf.format(new Date());
		return 	  "insert into ll_order_detail"+year+"("+
				  "id, mobile, msg, batchId, orderno, llrpt, user_id, org_id, pro_id, ordertm, rpttm, updatetm, createtm,productId,status,reportdate) "+
				  "values(seq_ll_order_detail.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}

	public String getFindOrderDetailSql(int row) {
		String year = sdf.format(new Date());
		return "select a.id,a.mobile,a.orderno,a.productid, " +
				"   a.msg, b.sp_user , b.sp_pwd  " +
				"   from ll_order_detail"+ year +
				"	a left join  ll_order_task b on a.orderno = b.orderno " +
				"	where a.Status = 0 and a.orderno = ? and b.servernum = ? and  Rownum < " + row ;
	}

	public String getInsertLlStatusrptSql() {
		return "INSERT INTO LL_STATUSRPT(ID, MOBILE, PRODUCTID, LLRPT, ORDERTM, ERRCODE, ORDERNO, CREATETM) " +
		" VALUES(SEQ_LL_STATUSRPT.NEXTVAL,?,?,?,?,?,?,?)";
	}
	
	public String findOrderNumUnsentSql() {
		return "SELECT ORDERNO,USER_ID,TIMER_TIME,RE_STATUS,TIMER_STATUS,ISRETRY FROM LL_ORDER_TASK WHERE TIMER_TIME < ? AND RE_STATUS IN ('0','2') AND SERVERNUM = '0' AND ORDERSTATUS NOT IN ('3','4','5') AND ROWNUM < 500" ;
	}
}
