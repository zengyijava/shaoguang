package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:19
 * @description
 */
public class TableLfOpratelog
{

	public static final String TABLE_NAME = "LF_OPRATELOG";

	public static final String LOG_ID = "LOG_ID";

	public static final String OP_TIME = "OP_TIME";

	public static final String OP_USER = "OP_USER";

	public static final String OP_MODULE = "OP_MODULE";

	public static final String OP_ACTION = "OP_ACTION";

	public static final String OP_RESULT = "OP_RESULT";

	public static final String OP_CONTENT = "OP_CONTENT";
	
	public static final String CORP_CODE="CORP_CODE";

	public static final String SEQUENCE = "S_LF_OPRATELOG";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfOpratelog", TABLE_NAME);
		columns.put("tableId", LOG_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("logId", LOG_ID);
		columns.put("opTime", OP_TIME);
		columns.put("opUser", OP_USER);
		columns.put("opModule", OP_MODULE);
		columns.put("opAction", OP_ACTION);
		columns.put("opResult", OP_RESULT);
		columns.put("opContent", OP_CONTENT);
		columns.put("corpCode",CORP_CODE);

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
