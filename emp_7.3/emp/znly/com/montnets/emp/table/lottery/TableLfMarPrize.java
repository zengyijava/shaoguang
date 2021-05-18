package com.montnets.emp.table.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfMarPrize
 *
 * @project p_mar
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfMarPrize
{
	// 表名
	public static final String				TABLE_NAME	= "LF_MAR_PRIZE";


    public static final String			P_ID	= "P_ID";

    public static final String			S_ID	= "S_ID";

    public static final String			TITLE	= "TITLE";

    public static final String			LIMIT_COUNT	= "LIMIT_COUNT";

    public static final String			USING_ATTR	= "USING_ATTR";

    public static final String			HEAD_RANGE	= "HEAD_RANGE";

    public static final String			END_RANGE	= "END_RANGE";

    public static final String			RANGE_PART	= "RANGE_PART";
    
    public static final String          BEGIN_PRIZETIME  = "BEGIN_PRIZETIME";
    
    public static final String          END_PRIZETIME  = "END_PRIZETIME";

	// 序列
	public static final String			SEQUENCE	= "S_OT_MAR_PRIZE";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfMarPrize", TABLE_NAME);
		columns.put("tableId", P_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("pid", P_ID);
        columns.put("sid", S_ID);
        columns.put("title", TITLE);
        columns.put("limitCount", LIMIT_COUNT);
        columns.put("usingAttr", USING_ATTR);
        columns.put("headRange", HEAD_RANGE);
        columns.put("endRange", END_RANGE);
        columns.put("rangePart", RANGE_PART);
        columns.put("beginPrizeTime", BEGIN_PRIZETIME);
        columns.put("endPrizeTime", END_PRIZETIME);
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