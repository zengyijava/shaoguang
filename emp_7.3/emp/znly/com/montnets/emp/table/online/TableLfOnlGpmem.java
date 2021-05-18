package com.montnets.emp.table.online;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfOnlGpmem
 *
 * @project p_onl
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfOnlGpmem
{
	// 表名
	public static final String				TABLE_NAME	= "LF_ONL_GPMEM";


    // 关联群组id
    public static final String			GP_ID	= "GP_ID";

    // 群组人员ID
    public static final String			GM_USER	= "GM_USER";

    // 状态，1-有效，0-无效（当群组人员推出该群组时置为状态无效
    public static final String			GM_STATE	= "GM_STATE";

	// 序列
	public static final String			SEQUENCE	= "";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfOnlGpmem", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
        columns.put("gpId", GP_ID);
        columns.put("gmUser", GM_USER);
        columns.put("gmState", GM_STATE);
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