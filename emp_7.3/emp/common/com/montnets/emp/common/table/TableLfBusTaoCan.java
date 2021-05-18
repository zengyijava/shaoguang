package com.montnets.emp.common.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfBusTaoCan {
    public static final String TABLE_NAME = "LF_BUS_TAOCAN";
    public static final String TAOCAN_ID = "taocan_id";
    public static final String TAOCAN_CODE = "taocan_code";
    public static final String TAOCAN_NAME = "taocan_name";
    public static final String TAOCAN_DES = "taocan_des";
    public static final String STATE = "state";
    public static final String CORP_CODE = "corp_code";
    public static final String CREATE_TIME = "create_time";
    public static final String UPDATE_TIME = "update_time";
    public static final String DEP_ID = "dep_id";
    public static final String USER_ID = "user_id";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String TAOCAN_TYPE = "taocan_type";
    public static final String TAOCAN_MONEY = "taocan_money";

    protected static final Map<String, String> columns = new HashMap<String, String>();

    static {
        columns.put("LfBusTaoCan", TABLE_NAME);
        columns.put("taocan_id", TAOCAN_ID);
        columns.put("taocan_code", TAOCAN_CODE);
        columns.put("taocan_name", TAOCAN_NAME);
        columns.put("taocan_des", TAOCAN_DES);
        columns.put("state", STATE);
        columns.put("corp_code", CORP_CODE);
        columns.put("create_time", CREATE_TIME);
        columns.put("update_time", UPDATE_TIME);
        columns.put("dep_id", DEP_ID);
        columns.put("user_id", USER_ID);
        columns.put("start_date", START_DATE);
        columns.put("end_date", END_DATE);
        columns.put("taocan_type", TAOCAN_TYPE);
        columns.put("taocan_money", TAOCAN_MONEY);
    }

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getORM() {
        return columns;
    }
}
