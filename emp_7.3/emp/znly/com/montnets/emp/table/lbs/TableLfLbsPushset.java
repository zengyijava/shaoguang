package com.montnets.emp.table.lbs;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfLbsPushset
 *
 * @project p_lbs
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfLbsPushset
{
	// 表名
	public static final String				TABLE_NAME	= "LF_LBS_PUSHSET";

    public static final String			PUSH_ID	= "PUSH_ID";

    public static final String			PUSHTYPE	= "PUSHTYPE";

    public static final String			IMGURL	= "IMGURL";

    public static final String			NOTE	= "NOTE";

    public static final String			RADIUS	= "RADIUS";

    public static final String			AUTORADIUS	= "AUTORADIUS";

    public static final String			PUSHCOUNT	= "PUSHCOUNT";

    public static final String			AUTOMORE	= "AUTOMORE";

    public static final String			CORP_CODE	= "CORP_CODE";

    public static final String			CREATETIME	= "CREATETIME";

    public static final String			MODITYTIME	= "MODITYTIME";

	// 序列
	public static final String			SEQUENCE	= "";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfLbsPushset", TABLE_NAME);
		columns.put("tableId", PUSH_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("pushId", PUSH_ID);
        columns.put("pushtype", PUSHTYPE);
        columns.put("imgurl", IMGURL);
        columns.put("note", NOTE);
        columns.put("radius", RADIUS);
        columns.put("autoradius", AUTORADIUS);
        columns.put("pushcount", PUSHCOUNT);
        columns.put("automore", AUTOMORE);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("moditytime", MODITYTIME);
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