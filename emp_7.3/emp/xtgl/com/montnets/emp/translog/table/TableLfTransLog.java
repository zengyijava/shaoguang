package com.montnets.emp.translog.table;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lianggp
 * @datetime 2021-1-27
 * @description LfTransLog映射类
 */
public class TableLfTransLog {

    public static final String TABLE_NAME = "TRANS_LOG";
    public static final String ID = "ID";
    public static final String USETYPE = "USETYPE";
    public static final String TRANSNAME = "TRANSNAME";
    public static final String TSTATUS = "TSTATUS";
    public static final String CREATETIME = "CREATETIME";
    public static final String RUNFLAG = "RUNFLAG";

    protected static final Map<String, String> columns = new HashMap<String, String>();
    static
    {
        columns.put("LfTransLog", TABLE_NAME);
        columns.put("tableId", ID);
        columns.put("id", ID);
        columns.put("usetype", USETYPE);
        columns.put("transname", TRANSNAME);
        columns.put("tstatus", TSTATUS);
        columns.put("createtime", CREATETIME);
        columns.put("runflag", RUNFLAG);
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
