package com.montnets.emp.rms.meditor.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfSubTemplate {
    public static final String TABLE_NAME = "LF_SUB_TEMPLATE";
    public static final String TMP_TYPE = "TMP_TYPE";
    public static final String FRONTJSON = "FRONTJSON";
    public static final String INDUSTRY_ID = "INDUSTRY_ID";
    public static final String USE_ID = "USE_ID";
    public static final String STATUS = "STATUS";
    public static final String ADD_TIME = "ADD_TIME";
    public static final String END_JSON = "ENDJSON";
    public static final String PRIORITY = "PRIORITY";
    public static final String FILEURL = "FILEURL";
    public static final String CONTENT = "CONTENT";
    public static final String CARD_HTML = "CARD_HTML";
    public static final String TM_ID = "TM_ID";
    public static final String ID = "ID";
    public static final String DEGREE = "DEGREE";
    public static final String DEGREE_SIZE = "DEGREE_SIZE";

    public static final String PRIORITY_ENTITY = "priority";

    public static final String TM_ID_ENTITY = "tmId";
	//序列
	public static final String SEQUENCE = "S_LF_SUB_TEMPLATE";
    //H5类型 0-长页，1-短页
    public static final String H5TYPE = "H5TYPE";
    public static final String APP = "APP";
    public static final String H5URL = "H5URL";

    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
    static{
        columns.put("LfSubTemplate",TABLE_NAME);
        columns.put("id",ID);
        columns.put("tableId",ID);
        columns.put("sequence",SEQUENCE);
        columns.put("tmpType",TMP_TYPE);
        columns.put("frontJson",FRONTJSON);
        columns.put("industryId",INDUSTRY_ID);
        columns.put("useId",USE_ID);
        columns.put("status",STATUS);
        columns.put("addTime",ADD_TIME);
        columns.put("endJson",END_JSON);
        columns.put("priority",PRIORITY);
        columns.put("fileUrl",FILEURL);
        columns.put("content",CONTENT);
        columns.put("cardHtml",CARD_HTML);
        columns.put("tmId",TM_ID);
        columns.put("degree",DEGREE);
        columns.put("degreeSize",DEGREE_SIZE);
        columns.put("h5Type",H5TYPE);
        columns.put("app",APP);
        columns.put("h5Url",H5URL);
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
