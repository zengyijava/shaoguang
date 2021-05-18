package com.montnets.emp.qyll.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;


public class LldgSqlDao {
	
//	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
	
	public static String getInsertLlTaskSql(){
		String sql = "";
		if(StaticValue.DBTYPE == 1){
			sql = "Insert into ll_order_task" +
					"(id, taskid, ecid, user_id, org_id, topic, pro_ids, msgtype, temp_id, sp_user, sp_pwd, subcount, effcount, succount,"+
					"faicount, timer_status, timer_time, re_status, orderstatus, smsstatus, isretry, " +
					"submittm, ordertm, updatetm, createtm,orderno,summoney,reportdate,msg) "+
					"values(seq_ll_order_task.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		}else if(StaticValue.DBTYPE == 2 || StaticValue.DBTYPE == 3 || StaticValue.DBTYPE == 4){
			sql = "Insert into ll_order_task" +
					"(taskid, ecid, user_id, org_id, topic, pro_ids, msgtype, temp_id, sp_user, sp_pwd, subcount, effcount, succount,"+
					"faicount, timer_status, timer_time, re_status, orderstatus, smsstatus, isretry, " +
					"submittm, ordertm, updatetm, createtm,orderno,summoney,reportdate,msg) "+
					"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		}else{
			EmpExecutionContext.error("获取插入订单任务表SQL失败:未知的数据库类型,DBTYPE["+StaticValue.DBTYPE+"]");
		}
		return sql;
	}
	
	
	public static String getInsertLlOrderDetailsql(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(new Date());
		String sql = "";
		if(StaticValue.DBTYPE == 1){
			sql = "insert into ll_order_detail"+year+"("+
					  "id, mobile, msg, batchId, orderno, llrpt, user_id, org_id, pro_id, ordertm, rpttm, updatetm, createtm,productId,status,reportdate) "+
					  "values(seq_ll_order_detail.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		}else if(StaticValue.DBTYPE == 2 || StaticValue.DBTYPE == 3 || StaticValue.DBTYPE == 4){
			sql = "insert into ll_order_detail"+year+"("+
					  "mobile, msg, batchId, orderno, llrpt, user_id, org_id, pro_id, ordertm, rpttm, updatetm, createtm,productId,status,reportdate) "+
					  "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		}else{
			EmpExecutionContext.error("获取插入订单详情表SQL失败:未知的数据库类型,DBTYPE["+StaticValue.DBTYPE+"]");
		}
		return sql;
	}
	
	public static String getFindOrderDetailSql(int row ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(new Date());
		String sql = "";
		
		//1 oracle ; 2 SQL Server2005 ; 3 MySQL ; 4 DB2    
		if(StaticValue.DBTYPE == 1){
			sql = "select a.id,a.mobile,a.orderno,a.productid, " +
					"   a.msg, b.sp_user , b.sp_pwd  " +
					"   from ll_order_detail"+ year +
					"	a left join  ll_order_task b on a.orderno = b.orderno " +
					"	where a.ordertm < ? and b.RE_STATUS in ('0','2') and a.Status = 0 and Rownum < " + row ;
		}else if(StaticValue.DBTYPE == 2 ){
			sql = "select top "+row+" a.id,a.mobile,a.orderno,a.productid, " +
					"   a.msg, b.sp_user , b.sp_pwd  " +
					"   from ll_order_detail"+ year +
					"	a left join  ll_order_task b on a.orderno = b.orderno " +
					"	where a.ordertm < ? and b.RE_STATUS in ('0','2') and a.Status = 0" ;
		}else if(StaticValue.DBTYPE == 3 ){
			sql = "select a.id,a.mobile,a.orderno,a.productid, " +
					"   a.msg, b.sp_user , b.sp_pwd  " +
					"   from ll_order_detail"+ year +
					"	a left join  ll_order_task b on a.orderno = b.orderno " +
					"	where a.ordertm < ? and b.RE_STATUS in ('0','2') and a.Status = 0  limit 0,"+row;
		}else if(StaticValue.DBTYPE == 4 ){
			sql = " SELECT * FROM ( "+
		                " SELECT C.*,ROWNUMBER() OVER() AS ROWID FROM( "+
		                       " SELECT A.ID,A.MOBILE,A.ORDERNO,A.PRODUCTID, A.MSG, B.SP_USER , B.SP_PWD FROM LL_ORDER_DETAIL"+year+"  A "+
		                       " LEFT JOIN LL_ORDER_TASK B ON  A.ORDERNO = B.ORDERNO WHERE A.ORDERTM < ?  AND     B.RE_STATUS IN ('0','2') AND A.STATUS = 0 "+                
		                 " )  C "+
		            " ) TMP WHERE  TMP.ROWID < 500 ";
		}else{
			EmpExecutionContext.error("获取插入订单详情表SQL失败:未知的数据库类型,DBTYPE["+StaticValue.DBTYPE+"]");
		}
		return sql;
	}
	
	
	public static String getInsertLlStatusrptSql(){
		String sql = "";
		if(StaticValue.DBTYPE == 1){
			sql = "INSERT INTO LL_STATUSRPT(ID, MOBILE, PRODUCTID, LLRPT, ORDERTM, ERRCODE, ORDERNO, CREATETM) " +
					" VALUES(SEQ_LL_STATUSRPT.NEXTVAL,?,?,?,?,?,?,?)";
		}else if(StaticValue.DBTYPE == 2 || StaticValue.DBTYPE == 3 || StaticValue.DBTYPE == 4){
			sql = "INSERT INTO LL_STATUSRPT( MOBILE, PRODUCTID, LLRPT, ORDERTM, ERRCODE, ORDERNO, CREATETM) " +
					" VALUES(?,?,?,?,?,?,?)";
		}else{
			EmpExecutionContext.error("获取插入状态报告表SQL失败:未知的数据库类型,DBTYPE["+StaticValue.DBTYPE+"]");
		}
		return sql;
	}
	
}









