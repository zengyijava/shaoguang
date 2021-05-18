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
 * @datetime 2011-1-28 上午10:11:27
 * @description
 */

public class TableLfReply
{

	public static final String TABLE_NAME = "LF_REPLY";

	public static final String PR_ID = "PR_ID";

	public static final String MSG_HEADER = "MSG_HEADER";

	public static final String MSG_MAIN = "MSG_MAIN";

	public static final String MSG_TAIL = "MSG_TAIL";

	public static final String MSG_LOOP_ID = "MSG_LOOP_ID";

	public static final String RE_ID = "RE_ID";

	public static final String SEQUENCE = "S_LF_REPLY";

	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfReply", TABLE_NAME);
		columns.put("tableId", RE_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("prId", PR_ID);
		columns.put("msgHeader", MSG_HEADER);
		columns.put("msgMain", MSG_MAIN);
		columns.put("msgTail", MSG_TAIL);
		columns.put("msgLoopId", MSG_LOOP_ID);
		columns.put("reId", RE_ID);
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
