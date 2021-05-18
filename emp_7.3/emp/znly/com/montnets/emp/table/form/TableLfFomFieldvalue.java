package com.montnets.emp.table.form;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfFomFieldvalue
 *
 * @author fangyt <foyoto@gmail.com>
 * @project p_fom
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfFomFieldvalue {
    // 表名
    public static final String TABLE_NAME = "LF_FOM_FIELDVALUE";

    public static final String V_ID = "V_ID";

    public static final String F_ID = "F_ID";

    public static final String Q_ID = "Q_ID";

    public static final String FIELD_ID = "FIELD_ID";

    public static final String FIELD_VALUE = "FIELD_VALUE";

    public static final String FIELD_TYPE = "FIELD_TYPE";

    public static final String WC_ID = "WC_ID";

    public static final String CORP_CODE = "CORP_CODE";

    public static final String CREATETIME = "CREATETIME";

    public static final String AID = "AID";

    // 序列
    public static final String SEQUENCE = "";

    // 映射集合
    protected static final Map<String, String> columns = new HashMap<String, String>();

    static {
        columns.put("LfFomFieldvalue", TABLE_NAME);
        columns.put("tableId", V_ID);
        columns.put("sequence", SEQUENCE);
        columns.put("vid", V_ID);
        columns.put("fid", F_ID);
        columns.put("qid", Q_ID);
        columns.put("fieldId", FIELD_ID);
        columns.put("fieldValue", FIELD_VALUE);
        columns.put("fieldType", FIELD_TYPE);
        columns.put("wcId", WC_ID);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("aid", AID);
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