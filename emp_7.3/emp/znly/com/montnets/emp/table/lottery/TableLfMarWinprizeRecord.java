package com.montnets.emp.table.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfMarWinprizeRecord
 *
 * @project p_mar
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfMarWinprizeRecord
{
    /**
     * 表名
      */
	public static final String TABLE_NAME = "LF_MAR_WINPRIZE_RECORD";


    public static final String			W_ID	= "W_ID";

    public static final String			S_ID	= "S_ID";

    public static final String			A_ID	= "A_ID";

    public static final String			R_ID	= "R_ID";
    
    public static final String          PID    = "PID";

    public static final String			OPEN_ID	= "OPEN_ID";

    public static final String			SERIAL_NUM	= "SERIAL_NUM";

    public static final String			PHONE	= "PHONE";

    public static final String			CORP_CODE	= "CORP_CODE";

    public static final String			CREATETIME	= "CREATETIME";

    /**
     * 序列
     */
	public static final String			SEQUENCE	= "S_OT_MAR_WINPRIZE_RECORD";

    /**
     * 映射集合
     */
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfMarWinprizeRecord", TABLE_NAME);
		columns.put("tableId", W_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("wid", W_ID);
        columns.put("sid", S_ID);
        columns.put("aid", A_ID);
        columns.put("rid", R_ID);
        columns.put("pid", PID);
        columns.put("openId", OPEN_ID);
        columns.put("serialNum", SERIAL_NUM);
        columns.put("phone", PHONE);
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