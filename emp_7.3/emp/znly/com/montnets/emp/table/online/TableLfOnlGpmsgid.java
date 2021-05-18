package com.montnets.emp.table.online;

import java.util.HashMap;
import java.util.Map;

public class TableLfOnlGpmsgid
{
 // 表名
    public static final String                TABLE_NAME  = "LF_ONL_GPMSGID";

    // 群组人员ID
    public static final String          GM_USER = "GM_USER";

    // 已读消息最大ID
    public static final String          MSG_ID = "MSG_ID";


    public static final String          UPDATE_TIME = "UPDATE_TIME";
    
    // 序列
    public static final String          SEQUENCE    = "";

    // 映射集合
    protected static final Map<String, String>   columns     = new HashMap<String, String>();

    static
    {
        columns.put("LfOnlGpmsgid", TABLE_NAME);
        
        columns.put("sequence", SEQUENCE);
        columns.put("gmUser", GM_USER);
        columns.put("msgId", MSG_ID);
        columns.put("updateTime", UPDATE_TIME );
        
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
