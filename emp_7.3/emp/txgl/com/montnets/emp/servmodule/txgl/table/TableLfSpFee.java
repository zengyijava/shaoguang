package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfSpFee
{
	/*SF_ID	自增ID
	SP_USER	SP账号/后端账号
	SP_USERPASSWORD	账户密码
	SP_TYPE	账号类型(1代表后端账号，2代表SP账号)
	ACCOUNTTYPE	账号类型(1代表短信，2代表彩信)
	FEE_URL	运营商余额查询URL
	BALANCE	运营商余额
	BALANCETH	余额查询阀值
	UPDATETIME	更新时间
	SPFEE_FLAG	扣费类型(1代表后付费，2代表预付费)
	QUERY_TIME  查询时间*/
	private final static String SF_ID ="SF_ID";
	private final static String SP_USER ="SP_USER";
	private final static String SP_USERPASSW ="SP_USERPASSWORD";
	private final static String SP_TYPE ="SP_TYPE";
	private final static String ACCOUNTTYPE ="ACCOUNTTYPE";
	private final static String SPFEE_URL ="SPFEE_URL";
	private final static String BALANCE ="BALANCE";
	private final static String BALANCETH ="BALANCETH";
	private final static String UPDATETIME ="UPDATETIME";
	private final static String SPFEE_FLAG ="SPFEE_FLAG";
	private final static String SP_RESULT ="SP_RESULT";
	private final static String QUERY_TIME ="QUERY_TIME";
	
	public static final String TABLE_NAME = "LF_SPFEE";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();
	static
	{
		columns.put("LfSpFee", TABLE_NAME);
		columns.put("tableId", SF_ID);
		columns.put("sfId", SF_ID);
		columns.put("spUser", SP_USER);
		columns.put("spUserpassword", SP_USERPASSW);
		columns.put("spType", SP_TYPE);
		columns.put("accountType", ACCOUNTTYPE);
 		columns.put("spFeeFlag", SPFEE_FLAG);
 		columns.put("spFeeUrl", SPFEE_URL);
 		columns.put("balance", BALANCE);
 		columns.put("balanceth", BALANCETH);
 		columns.put("updateTime", UPDATETIME);
		columns.put("spResult", SP_RESULT);
		columns.put("queryTime", QUERY_TIME);
	 
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM()
	{
		return columns;
	}
	
}
