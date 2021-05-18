package com.montnets.emp.table.online;

import java.util.HashMap;
import java.util.Map;

public class TableLfOnlRemark
{
 // 表名
    public static final String                TABLE_NAME  = "LF_ONL_REMARK";

    public static final String          USER_ID = "USER_ID";

    public static final String          MARK_NAME = "MARK_NAME";


    public static final String          MARK_ID = "MARK_ID";
    
    // 序列
    public static final String          SEQUENCE    = "";

    // 映射集合
    protected static final Map<String, String>   columns     = new HashMap<String, String>();

    static
    {
        columns.put("LfOnlRemark", TABLE_NAME);
        
        columns.put("sequence", SEQUENCE);
        columns.put("userId", USER_ID);
        columns.put("markName", MARK_NAME);
        columns.put("markId", MARK_ID );
        
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
