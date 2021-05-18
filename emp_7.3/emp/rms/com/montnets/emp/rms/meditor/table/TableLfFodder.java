package com.montnets.emp.rms.meditor.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfFodder {
    public static final String TABLE_NAME = "LF_FODDER";
    public static final String ID = "ID";
    public static final String USER_ID = "USER_ID";
    public static final String URL = "URL";
    public static final String FOTYPE = "FO_TYPE";
    public static final String FOSIZE = "FO_SIZE";
    public static final String WIDTH = "WIDTH";
    public static final String HEIGHT = "HEIGHT";
    public static final String RADIO = "RADIO";
    public static final String DURATION = "DURATION";
    public static final String ORIGINAL = "ORIGINAL";
    public static final String FO_STATUS = "FO_STATUS";
    public static final String FIRSTFRAMEPATH ="FIRSTFRAMEPATH";

    protected final static Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
    static{
        columns.put("LfFodder",TABLE_NAME);
        columns.put("tableId",ID);
        columns.put("id",ID);
        columns.put("userId",USER_ID);
        columns.put("url",URL);
        columns.put("foType",FOTYPE);
        columns.put("foSize",FOSIZE);
        columns.put("width",WIDTH);
        columns.put("height",HEIGHT);
        columns.put("radio",RADIO);
        columns.put("duration",DURATION);
        columns.put("original",ORIGINAL);
        columns.put("status",FO_STATUS);
        columns.put("fistFramePath",FIRSTFRAMEPATH);
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
