package com.montnets.emp.servmodule.xtgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableGwCluStatus {
    public static final String TABLE_NAME = "GW_CLUSTATUS";
    public static final String ID = "ID";
    public static final String GWTYPE = "GWTYPE";
    public static final String GWNO = "GWNO";
    public static final String PRIGWNO = "PRIGWNO";
    public static final String RUNSTATUS = "RUNSTATUS";
    public static final String GWEIGHT = "GWEIGHT";
    public static final String RUNWEIGHT = "RUNWEIGHT";
    public static final String UPDTIME = "UPDTIME";

    public static final String SEQUENCE = "SEQ_GW_CLUSTATUS";


    protected static final Map<String, String> columns = new HashMap<String, String>();

    static {
        columns.put("GwCluStatus", TABLE_NAME);
        columns.put("id", ID);
        columns.put("tableId", ID);
        columns.put("gwType", GWTYPE);
        columns.put("gwNo", GWNO);
        columns.put("priGwNo", PRIGWNO);
        columns.put("runstatus", RUNSTATUS);
        columns.put("gweight", GWEIGHT);
        columns.put("runweight", RUNWEIGHT);
        columns.put("updtime", UPDTIME);
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
