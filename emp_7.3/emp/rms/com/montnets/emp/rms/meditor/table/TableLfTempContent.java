package com.montnets.emp.rms.meditor.table;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 存储模板结构的JSON串Table 类
 * @Auther:xuty
 * @Date: 2018/9/10 14:36
 */
public class TableLfTempContent {

    public static final String TABLE_NAME = "LF_TEMPCONTENT";
    public static final String ID = "ID";
    public static final String TM_ID = "TMID";
    public static final String CONTTYPE = "CONTTYPE";
    public static final String TMPTYPE = "TMPTYPE";
    public static final String TMPCONTENT = "TMPCONTENT";


    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
    static{
        columns.put("LfTempContent",TABLE_NAME);
        columns.put("tableId",ID);
        columns.put("id",ID);
        columns.put("tmId",TM_ID);
        columns.put("contType",CONTTYPE);
        columns.put("tmpType",TMPTYPE);
        columns.put("tmpContent",TMPCONTENT);

    }
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
