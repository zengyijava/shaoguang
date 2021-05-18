package com.montnets.emp.table.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfMarRecord
 *
 * @project p_mar
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfMarRecord
{
	// 表名
	public static final String				TABLE_NAME	= "LF_MAR_RECORD";


    public static final String			R_ID	= "R_ID";

    public static final String			S_ID	= "S_ID";

    public static final String			A_ID	= "A_ID";

    public static final String			OPEN_ID	= "OPEN_ID";

    public static final String			RANDOM_NUM	= "RANDOM_NUM";

    public static final String			CORP_CODE	= "CORP_CODE";

    public static final String			CREATETIME	= "CREATETIME";

	// 序列
	public static final String			SEQUENCE	= "41";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfMarRecord", TABLE_NAME);
		columns.put("tableId", R_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("rid", R_ID);
        columns.put("sid", S_ID);
        columns.put("aid", A_ID);
        columns.put("openId", OPEN_ID);
        columns.put("randomNum", RANDOM_NUM);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
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