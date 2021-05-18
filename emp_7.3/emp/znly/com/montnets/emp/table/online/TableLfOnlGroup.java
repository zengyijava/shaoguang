package com.montnets.emp.table.online;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfOnlGroup
 *
 * @project p_onl
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfOnlGroup
{
	// 表名
	public static final String				TABLE_NAME	= "LF_ONL_GROUP";


    // 标识列
    public static final String			GP_ID	= "GP_ID";

    // 群组名称
    public static final String			GP_NAME	= "GP_NAME";

    // 创建者
    public static final String			CREATE_USER	= "CREATE_USER";

    // 创建时间
    public static final String			CREATE_TIME	= "CREATE_TIME";
    
    // 人数
    public static final String			MEM_COUNT	= "MEM_COUNT";

	// 序列
	public static final String			SEQUENCE	= "550";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfOnlGroup", TABLE_NAME);
		columns.put("tableId", GP_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("gpId", GP_ID);
        columns.put("gpName", GP_NAME);
        columns.put("createUser", CREATE_USER);
        columns.put("createTime", CREATE_TIME);
        columns.put("memCount", MEM_COUNT);
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