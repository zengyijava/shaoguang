package com.montnets.emp.rms.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewLfTempImportBatchVo {
    //实体类字段与数据库字段实体类映射的map集合
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    static
    {
        columns.put("id", "ID");
        columns.put("batch", "BATCH");
        columns.put("corpName", "CORP_NAME");
        columns.put("corpCode", "CORP_CODE");
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
