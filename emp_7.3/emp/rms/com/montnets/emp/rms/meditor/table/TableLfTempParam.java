package com.montnets.emp.rms.meditor.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfTempParam {

    public static final String TABLE_NAME = "LF_TEMP_PARAM";
    public static final String ID = "ID";
    public static final String TM_ID = "TM_ID";
    public static final String NAME = "NAME";
    public static final String MAX_LENGTH = "MAX_LENGTH";
    public static final String MIN_LENGTH = "MIN_LENGTH";
    public static final String TYPE = "TYPE";
    public static final String LENGTH_RESTRICT = "LENGTH_RESTRICT";
    public static final String FIX_LENGTH = "FIX_LENGTH";
    public static final String REGCONTENT = "REGCONTENT";
    public static final String HASLENGTH = "HASLENGTH";
    /**
     * 序列 用于oracle主键自增
     */
    public static final String SEQUENCE = "LF_TEMP_PARAM_S";

    protected final static Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
    static{
        columns.put("LfTempParam",TABLE_NAME);
        columns.put("tableId",ID);
        columns.put("id",ID);
        columns.put("tmId",TM_ID);
        columns.put("name",NAME);
        columns.put("maxLength",MAX_LENGTH);
        columns.put("minLength",MIN_LENGTH);
        columns.put("type",TYPE);
        columns.put("lengthRestrict",LENGTH_RESTRICT);
        columns.put("fixLength",FIX_LENGTH);
        columns.put("regContent",REGCONTENT);
        columns.put("hasLength",HASLENGTH);
        columns.put("sequence",SEQUENCE);
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
