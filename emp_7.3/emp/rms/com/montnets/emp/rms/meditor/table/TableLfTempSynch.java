package com.montnets.emp.rms.meditor.table;

import java.util.LinkedHashMap;
import java.util.Map;

public class TableLfTempSynch {
    //实体类字段与数据库字段实体类映射的map集合
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    public static final String TABLE_NAME = "LF_TEMP_SYNCH";
    //序列 用于oracle主键自增
    public static final String SEQUENCE = "LF_TEMP_SYNCH_S";
    static
    {

        columns.put("LfTempSynch","LF_TEMP_SYNCH");
        columns.put("tableId","ID");
        columns.put("id", "ID");
        columns.put("spTemplateid", "SP_TEMPLID");
        columns.put("count", "COUNT");
        columns.put("synstatus", "SYNSTATUS");
        columns.put("cause", "CAUSE");
        columns.put("updateTime", "UPDATE_TIME");
        columns.put("isMaterial", "ISMATERIAL");
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
