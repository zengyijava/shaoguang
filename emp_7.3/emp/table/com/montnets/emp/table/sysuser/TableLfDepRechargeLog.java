package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;

public class TableLfDepRechargeLog {

	//表名   机构充值/回收日志表
	public static final String TABLE_NAME = "LF_DEP_RECHARGE_LOG";
	//自增ID
	public static final String LOG_ID = "LOG_ID";
	// 充值类型（100为总公司充值/回收；101机构为机构充值/回收； 102机构为操作员充值/回收）
	public static final String OPT_TYPE = "OPT_TYPE";
	// 充值源id 上级机构
	public static final String SRC_TARGETID = "SRC_TARGETID";
	// 充值目的id 充值机构
	public static final String DST_TARGETID = "DST_TARGETID";
	// 充值源名称/描述 充值目的名称/描述
	public static final String OPT_INFO = "OPT_INFO";
	// 信息类型：1为短信，2为彩信
	public static final String MSG_TYPE = "MSG_TYPE";
	// 数量：充值/回收数量
	public static final String COUNT = "COUNT";
	// 操作员id：执行充值/回收的操作员id
	public static final String OPT_ID  = "OPT_ID";
	// 操作时间：执行充值/回收的时间
	public static final String OPT_DATE  = "OPT_DATE";
	// 执行结果（0成功，其它失败）
	public static final String RESULT  = "RESULT";
	// 备注
	public static final String MEMO  = "MEMO";
	// 企业编号
	public static final String CORP_CODE  = "CORP_CODE";
	//序列名
	public static final String SEQUENCE="S_LF_DEP_RECHARGE_LOG";

	protected static  final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfDepRechargeLog", TABLE_NAME);
		columns.put("tableId", LOG_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("logId", LOG_ID);
		columns.put("optType", OPT_TYPE);
		columns.put("srcTargetId", SRC_TARGETID);
		columns.put("dstTargetId", DST_TARGETID);
		columns.put("optInfo", OPT_INFO);
		columns.put("msgType", MSG_TYPE);
		columns.put("count", COUNT);
		columns.put("optId", OPT_ID);
		columns.put("optDate", OPT_DATE);
		columns.put("result", RESULT);
		columns.put("memo", MEMO);
		columns.put("corpCode", CORP_CODE);

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
