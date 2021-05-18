package com.montnets.emp.table.online;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfOnlMsg
 *
 * @project p_onl
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfOnlMsg
{
	// 表名
	public static final String				TABLE_NAME	= "LF_ONL_MSG";


    // 注释-TODO
    public static final String			M_ID	= "M_ID";

    // 注释-TODO
    public static final String			A_ID	= "A_ID";

    // 注释-TODO
    public static final String			FROM_USER	= "FROM_USER";

    // 注释-TODO
    public static final String			TO_USER	= "TO_USER";

    // 注释-TODO
    public static final String			SERVER_NUM	= "SERVER_NUM";

    // 注释-TODO
    public static final String			MESSAGE	= "MESSAGE";

    // 注释-TODO
    public static final String			SEND_TIME	= "SEND_TIME";

    // 注释-TODO
    public static final String			MSG_TYPE	= "MSG_TYPE";

    // 注释-TODO
    public static final String			PUSH_TYPE	= "PUSH_TYPE";

	// 序列
	public static final String			SEQUENCE	= "";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfOnlMsg", TABLE_NAME);
		//columns.put("tableId", M_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("MId", M_ID);
        columns.put("AId", A_ID);
        columns.put("fromUser", FROM_USER);
        columns.put("toUser", TO_USER);
        columns.put("serverNum", SERVER_NUM);
        columns.put("message", MESSAGE);
        columns.put("sendTime", SEND_TIME);
        columns.put("msgType", MSG_TYPE);
        columns.put("pushType", PUSH_TYPE);
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