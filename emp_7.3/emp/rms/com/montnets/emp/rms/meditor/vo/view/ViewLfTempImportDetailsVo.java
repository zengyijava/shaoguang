package com.montnets.emp.rms.meditor.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewLfTempImportDetailsVo {
    //实体类字段与数据库字段实体类映射的map集合
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    static
    {
        columns.put("LfTempImportDetails","LF_TEMP_IMPORT_DETAILS");
        columns.put("tableId","ID");
        columns.put("id", "ID");
        columns.put("batch", "BATCH");
        columns.put("tmName", "TM_NAME");
        columns.put("name", "NAME");
        columns.put("phoneNum", "PHONE_NUM");
        columns.put("score", "SCORE");
        columns.put("sptemplid", "SPTEMPLID");
        columns.put("sendStatus", "SEND_STATUS");
        columns.put("importStatus", "IMPORT_STATUS");
        columns.put("cause", "CAUSE");
        columns.put("imageSrc", "IMAGE_SRC");
        columns.put("videoSrc", "VIDEO_SRC");
        columns.put("addtime", "ADDTIME");
        columns.put("auditstatus", "AUDITSTATUS");
        columns.put("corpCode", "CORP_CODE");

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
