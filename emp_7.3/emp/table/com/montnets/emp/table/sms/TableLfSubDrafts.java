package com.montnets.emp.table.sms;

import java.util.HashMap;
import java.util.Map;

public class TableLfSubDrafts {

    public static final String TABLE_NAME	= "LF_SUB_DRAFTS";

    public static final String ID = "ID";

    public static final String DRAFTID = "DRAFTID";

    public static final String DOMAINID = "DOMAINID";

    public static final String NETURLID = "NETURLID";

    public static final String TYPE = "TYPE";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String UPDATE_TIME = "UPDATE_TIME";

    protected static final Map<String , String> columns = new HashMap<String, String>();

    static
    {

        columns.put("LfSubDrafts", TABLE_NAME);
        columns.put("tableId", ID);
        columns.put("id", ID);
        columns.put("draftId", DRAFTID);
        columns.put("domainId", DOMAINID);
        columns.put("netUrlId", NETURLID);
        columns.put("type", TYPE);
        columns.put("createtime", CREATE_TIME);
        columns.put("updatetime", UPDATE_TIME);
    }


    public static Map<String , String> getORM(){

        return columns;

    }

}
