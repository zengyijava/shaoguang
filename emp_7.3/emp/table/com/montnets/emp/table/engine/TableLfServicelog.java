/**
 * 
 */
package com.montnets.emp.table.engine;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-28 上午09:08:17
 * @description
 */

public class TableLfServicelog
{

	public static final  String TABLE_NAME = "LF_SERVICELOG";

	public static final String SL_ID = "SL_ID";

	public static final String SER_ID = "SER_ID";

	public static final String RUNTIME = "RUNTIME";

	public static final String SL_STATE = "SL_STATE";

	public static final String URL = "URL";

	public static final String SEQUENCE = "S_LF_SERVICELOG";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfServicelog", TABLE_NAME);
		columns.put("tableId", SL_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("slId", SL_ID);
		columns.put("serId", SER_ID);
		columns.put("runTime", RUNTIME);
		columns.put("slState", SL_STATE);
		columns.put("url", URL);
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
