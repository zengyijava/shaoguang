package com.montnets.emp.table.blacklist;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:49
 * @description
 */
public class TablePbListBlackDelrecord
{

	public static final String TABLE_NAME = "PB_BLACK_DEL";

	public static final String ID = "ID";

	public static final String OPERATE_ID = "OPERATEID";

	public static final String PHONE = "PHONE";

	public static final String OP_TTIME = "OPTTIME";

	public static final String TASK_ID = "TASKID";


	public static final String SEQUENCE = "PB_BLACK_DEL_S";

	protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("PbListBlackDelrecord", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("operateId", OPERATE_ID);
		columns.put("phone", PHONE);
		columns.put("optTime", OP_TTIME);
		columns.put("taskId", TASK_ID);
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
