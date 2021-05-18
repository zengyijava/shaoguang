package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfSpFeeLog {

	//表名   SP充值/回收日志表
	public static final String TABLE_NAME = "LF_SPFEE_LOG";
	//自增ID
	private static final String ID = "ID";
	// SP账号
	private static final String SPUSER = "SPUSER";
	// 充值/回收数量
	private static final String ICOUNT = "ICOUNT";
	// 执行结果（0成功，1失败）
	private static final String RESULT  = "RESULT";
	// 操作员ID
	private static final String USERID = "USERID";
	// 企业编号
	private static final String CORP_CODE  = "CORP_CODE";
	// 操作时间：执行充值/回收的时间
	private static final String OPR_TIME  = "OPR_TIME";
	// 备注
	private static final String MEMO  = "MEMO";
	//序列名
	private static final String SEQUENCE="S_LF_SPFEE_LOG";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfSpFeeLog", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("spuser", SPUSER);
		columns.put("icount", ICOUNT);
		columns.put("result", RESULT);
		columns.put("userid", USERID);
		columns.put("corpcode", CORP_CODE);
		columns.put("oprtime", OPR_TIME);
		columns.put("memo", MEMO);
	}

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}
}
