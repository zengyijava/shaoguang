package com.montnets.emp.table.online;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfOnlMsgHis
 *
 * @project p_onl
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfOnlServer
{
	// 表名
	public static final String				TABLE_NAME	= "LF_ONL_SERVER";

    public static final String			SER_ID	= "SER_ID";

    public static final String			SER_NUM	= "SER_NUM";

    public static final String			CUSTOME_ID	= "CUSTOME_ID";

    public static final String			A_ID	= "A_ID";

    public static final String			CREATE_TIME	= "CREATE_TIME";

    public static final String			FROM_USER	= "FROM_USER";

    public static final String			SCORE	= "SCORE";

    public static final String			EVALUATE	= "EVALUATE";
    
    public static final String			END_TIME	= "END_TIME";
    
    public static final String			DURATION	= "DURATION";
    
    public static final String			SERTYPE	= "SERTYPE";


	// 序列
	public static final String			SEQUENCE	= "S_LF_ONL_SERVER";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfOnlServer", TABLE_NAME);
		columns.put("tableId", SER_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("serId", SER_ID);
        columns.put("AId", A_ID);
        columns.put("fromUser", FROM_USER);
        columns.put("serNum", SER_NUM);
        columns.put("createTime", CREATE_TIME);
        columns.put("score", SCORE);
        columns.put("evaluate", EVALUATE);
        columns.put("customeId", CUSTOME_ID);
        columns.put("endTime", END_TIME);
        columns.put("duration", DURATION);
        columns.put("serType", SERTYPE);
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