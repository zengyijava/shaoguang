package com.montnets.emp.netnews.table;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuxiaotao <819475589@qq.com>
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:22
 * @description
 */
public class TableLfWXData {
    //表名：
    public static final String TABLE_NAME = "LF_WX_DATA";
    public static final String DID = "DID";
    public static final String CODE = "CODE";
    public static final String NAME = "NAME";
    public static final String DATATYPEID = "DATATYPEID";
    public static final String REPLYSETTYPE = "REPLYSETTYPE";
    public static final String QUESTYPE = "QUESTYPE";
    public static final String QUESCONTENT = "QUESCONTENT";
    public static final String COLNAME = "COLNAME";
    public static final String COLTYPE = "COLTYPE";
    public static final String COLSIZE = "COLSIZE";
    public static final String STATUS = "STATUS";
    public static final String MODIFYDATE = "MODIFYDATE";
    public static final String CREATDATE = "CREATDATE";
    public static final String USERID = "USERID";
    public static final String CORPCODE = "CORP_CODE";
    //序列
    public static final String SEQUENCE = "8";
    //映射集合
    protected static final Map<String, String> columns = new HashMap<String, String>();

    static {
        columns.put("LfWXData", TABLE_NAME);
        columns.put("tableId", DID);
        columns.put("dId", DID);
        columns.put("sequence", SEQUENCE);
        columns.put("code", CODE);
        columns.put("name", NAME);
        columns.put("dataTypeId", DATATYPEID);
        columns.put("replySetType", REPLYSETTYPE);
        columns.put("quesType", QUESTYPE);
        columns.put("quesContent", QUESCONTENT);
        columns.put("colName", COLNAME);
        columns.put("colType", COLTYPE);
        columns.put("colSize", COLSIZE);
        columns.put("status", STATUS);

        columns.put("modifyDate", MODIFYDATE);
        columns.put("creatDate", CREATDATE);
        columns.put("userId", USERID);
        columns.put("corpCode", CORPCODE);
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
