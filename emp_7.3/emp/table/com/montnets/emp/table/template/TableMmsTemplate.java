package com.montnets.emp.table.template;

import java.util.HashMap;
import java.util.Map;

public class TableMmsTemplate {
	// 表名
	public static final String TABLE_NAME = "MMS_TEMPLATE";
	// 自增ID
	public static final String ID = "ID";
	public static final String TABLE_ID = "ID";
	//用户帐号，用来标识该模板归属的用户
	public static final String USERID = "USERID";
	// 模板ID，唯一索引
	public static final String TMPLID = "TMPLID";
	// 审核状态 
	public static final String AUDITSTATUS = "AUDITSTATUS";
	// 模板状态
	public static final String TMPLSTATUS = "TMPLSTATUS";
	//模板中参数的个数
	public static final String PARAMCNT = "PARAMCNT";
	// 模板文件存储过的路径
	public static final String TMPLPATH = "TMPLPATH";
	// 模板接收时间
	public static final String RECVTIME = "RECVTIME";
	//审核时间
	public static final String AUDITTIME = "AUDITTIME";
	//审核员
	public static final String AUDITOR = "AUDITOR";
	// 备注（审核通过或不通过的理由等）
	public static final String REMARKS = "REMARKS";
	// 预留字段
	public static final String RESERVE1 = "RESERVE1";
	// 预留字段
	public static final String RESERVE2 = "RESERVE2";
	// 预留字段
	public static final String RESERVE3 = "RESERVE3";
	// 预留字段
	public static final String RESERVE4 = "RESERVE4";
	// 预留字段
	public static final String RESERVE5 = "RESERVE5";
	//提交状态 
	//	0：未提交
	//	1：提交成功
	//	2：提交失败
	public static final String SUBMITSTATUS = "SUBMITSTATUS";
	
	public static final String EMP_TMPLID = "EMP_TMPLID";
	
	
	public static final String SEQUENCE = "SEQ_MMS_TEMPLATE";

    protected static final Map<String, String> columns = new HashMap<String, String>();
	static {
		columns.put("MmsTemplate", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
		columns.put("tableId", TABLE_ID);
		columns.put("id", ID);
		columns.put("userId", USERID);
		columns.put("tmplId", TMPLID);
		columns.put("auditStatus", AUDITSTATUS);
		columns.put("tmplStatus", TMPLSTATUS);
		columns.put("paramCnt", PARAMCNT);
		columns.put("tmplPath", TMPLPATH);
		columns.put("recvTime", RECVTIME);
		columns.put("auditTime", AUDITTIME);
		columns.put("auditor", AUDITOR);
		columns.put("remarks", REMARKS);
		columns.put("reServe1", RESERVE1);
		columns.put("reServe2", RESERVE2);
		columns.put("reServe3", RESERVE3);
		columns.put("reServe4", RESERVE4);
		columns.put("reServe5", RESERVE5);
		columns.put("submitstatus", SUBMITSTATUS);
		columns.put("emptemplid", EMP_TMPLID);
		
	}

	public static Map<String, String> getORM() {
		return columns;
	}
}
