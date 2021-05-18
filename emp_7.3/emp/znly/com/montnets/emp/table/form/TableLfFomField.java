package com.montnets.emp.table.form;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfFomField
 *
 * @author fangyt <foyoto@gmail.com>
 * @project p_fom
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfFomField {
    // 表名
    public static final String TABLE_NAME = "LF_FOM_FIELD";

    public static final String FIELD_ID = "FIELD_ID";

    public static final String FILED_TYPE = "FILED_TYPE";

    public static final String FIELD_VALUE = "FIELD_VALUE";

    public static final String Q_ID = "Q_ID";

    // 序列
    public static final String SEQUENCE = "";

    // 映射集合
    protected static final Map<String, String> columns = new HashMap<String, String>();

    static {
        columns.put("LfFomField", TABLE_NAME);
        columns.put("tableId", FIELD_ID);
        columns.put("sequence", SEQUENCE);
        columns.put("fieldId", FIELD_ID);
        columns.put("filedType", FILED_TYPE);
        columns.put("fieldValue", FIELD_VALUE);
        columns.put("qid", Q_ID);
    }

    ;

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getORM() {
        return columns;
    }
}