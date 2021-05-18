package com.montnets.emp.rms.meditor.table;

import java.util.LinkedHashMap;
import java.util.Map;

public class TableLfTempImportBatch {
    //实体类字段与数据库字段实体类映射的map集合
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    public static final String TABLE_NAME = "LF_TEMP_IMPORT_BATCH";
    public static final String BATCH = "BATCH";
    public static final String TM_NAME = "TM_NAME";
    static
    {
        columns.put("LfTempImportBatch","LF_TEMP_IMPORT_BATCH");
        columns.put("tableId","ID");
        columns.put("id", "ID");
        columns.put("batch", "BATCH");
        columns.put("corpCode", "CORP_CODE");
        columns.put("corpName", "CORP_NAME");
        columns.put("amount", "AMOUNT");
        columns.put("successAmount", "SUCCESS_AMOUNT");
        columns.put("failAmount", "FAIL_AMOUNT");
        columns.put("processStatus", "PROCESS_STATUS");
        columns.put("tmName", "TM_NAME");
        columns.put("addtime", "ADDTIME");
        columns.put("sendState", "SENDSTATE");
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
