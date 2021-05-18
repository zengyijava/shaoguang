package com.montnets.emp.table.pasgroup;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:30:52
 * @description
 */
public class TablePbWebzzcmdQueue
{

	public static final String TABLE_NAME = "PB_WEBZZCMD_QUEUE";

	public static final String ID = "ID";

	public static final String ST_IME = "STIME";

	public static final String SID = "SID";

	public static final String CMD_TYPE = "CMDTYPE";

	public static final String USER_ID = "USERID";

	public static final String CMD_PARAM = "CMDPARAM";

	public static final String EXEC_TIME = "EXECTIME";

	public static final String USE_IP = "USEIP";

	public static final String EXEC_FLAG = "EXECFLAG";

	public static final String MEMO = "MEMO";

	public static final String PROD_ID = "PRODID";

	public static final String OP_ID = "OPID";

	public static final String FEE_TYPE = "FEETYPE";

	public static final String FEE = "FEE";

	public static final String SEQUENCE = "PB_WEBZZCMD_QUEUE_MPSN";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("PbWebzzcmdQueue", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("stime", ST_IME);
		columns.put("sid", SID);
		columns.put("cmdType", CMD_TYPE);
		columns.put("userId", USER_ID);
		columns.put("cmdParam", CMD_PARAM);
		columns.put("exectime", EXEC_TIME);
		columns.put("useIp", USE_IP);
		columns.put("execflag", EXEC_FLAG);
		columns.put("memo", MEMO);
		columns.put("prodId", PROD_ID);
		columns.put("opid", OP_ID);
		columns.put("feeType", FEE_TYPE);
		columns.put("fee", FEE);
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
