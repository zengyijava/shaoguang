package com.montnets.emp.table.template;

import java.util.LinkedHashMap;
import java.util.Map;

public class TableLfShortTemp {

    public static final String TABLE_NAME = "LF_SHORTTEMP";
    public static final String ID = "ID";
    public static final String USERID = "USERID";
    public static final String CORPCODE = "CORPCODE";
    public static final String TEMPID = "TEMPID";
    public static final String TEMPNAME = "TEMPNAME";
    public static final String ADDTIME = "ADDTIME";

    public static final String ID_ENTITY = "id";
    public static final String USERID_ENTITY = "userId";
    public static final String CORPCODE_ENTITY = "corpCode";
    public static final String TEMPID_ENTITY = "tempId";
    public static final String TEMPNAME_ENTITY = "tempName";
    public static final String ADDTIME_ENTITY = "addTime";

    //实体类字段与数据库字段实体类映射的map集合
    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
    static
    {
        columns.put("LfShortTemp",TABLE_NAME);
        columns.put("tableId",ID);
        columns.put(ID_ENTITY,ID);
        columns.put(TEMPNAME_ENTITY, TEMPNAME);
        columns.put(TEMPID_ENTITY,TEMPID);
        columns.put(USERID_ENTITY, USERID);
        columns.put(CORPCODE_ENTITY, CORPCODE);
        columns.put(ADDTIME_ENTITY, ADDTIME);
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
