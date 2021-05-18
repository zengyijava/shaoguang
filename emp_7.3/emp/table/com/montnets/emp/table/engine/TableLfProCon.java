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
 * @datetime 2011-1-26 下午02:17:37
 * @description
 */

public class TableLfProCon
{
	public static final String TABLE_NAME = "LF_PRO_CON";

	public static final String DBCON_ID = "DBCON_ID";

	public static final String PR_ID = "PR_ID";

	public static final String CON_EXPRESS = "CON_EXPRESS";

	public static final String CON_OPERATE = "CON_OPERATE";

	public static final String CON_VALUE = "CON_VALUE";

	public static final String COMMENTS = "COMMENTS";

	public static final String USED_PRID = "USEDPRID";

	public static final String SEQUENCE = "S_LF_PRO_CON";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfProCon", TABLE_NAME);
		columns.put("tableId", DBCON_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("dbconId", DBCON_ID);
		columns.put("prId", PR_ID);
		columns.put("conExpress", CON_EXPRESS);
		columns.put("conOperate", CON_OPERATE);
		columns.put("conValue", CON_VALUE);
		columns.put("comments", COMMENTS);
		columns.put("UsedPrId", USED_PRID);
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
