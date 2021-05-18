package com.montnets.emp.netnews.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfWXBASEINFO {
    public static final String TABLE_NAME = "LF_WX_BASEINFO";
    public static final String ID = "ID";                    //自动编号
    public static final String NAME = "NAME";            //业务数据编号
    public static final String NETID = "NETID";            //显示名称
    public static final String SORT = "SORT";        //字段列名
    public static final String TIMETYPE = "TIMETYPE";            //数据类型。0：字符串；1：数字；2：日期。
    public static final String TIMEOUT = "TIMEOUT";            //数据长度
    public static final String WXRIGHT = "WXRIGHT";            //其他长度，如：小数位。
    public static final String TIMEEND = "TIMEEND";            //设为参数。0：否；1：是
    public static final String URL = "URL";            //素材管理文件类型
    public static final String STATUS = "STATUS";
    public static final String AUDITUSERID = "AUDITUSERID";
    public static final String AUDITDATE = "AUDITDATE";
    public static final String WXTYPE = "WXTYPE";
    public static final String IMAGE = "IMAGE";
    public static final String MODIFYID = "MODIFYID";
    public static final String MODIFYDATE = "MODIFYDATE";
    public static final String CREATID = "CREATID";
    public static final String CREATDATE = "CREATDATE";
    public static final String SMS = "SMS";
    public static final String WXSHARE = "WXSHARE";
    public static final String CORP_CODE = "CORP_CODE";
    public static final String DATA_TABLENAME = "DATA_TABLENAME";
    public static final String DYN_TABLENAME = "DYN_TABLENAME";
    public static final String TEMPTYPE = "TEMPTYPE";
    public static final String PARAMS = "PARAMS";
    public static final String HASPARAMS = "HASPARAMS";
    //运营商审批状态 默认值为0 字段类型为int类型  1.表示审核通过，2表示审核未通过
    public static final String OPERAPPSTATUS = "OPERAPPSTATUS";
    //运营商审批意见 OPERAPPNOTE
    public static final String OPERAPPNOTE = "OPERAPPNOTE";
    //序列
    public static final String SEQUENCE = "S_LF_WX_BASEINFO";

    protected static final Map<String, String> columns = new HashMap<String, String>();

    static {
        columns.put("LfWXBASEINFO", TABLE_NAME);
        columns.put("tableId", ID);
        columns.put("ID", ID);
        columns.put("NAME", NAME);
        columns.put("NETID", NETID);
        columns.put("SORT", SORT);
        columns.put("TIMETYPE", TIMETYPE);
        columns.put("TIMEOUT", TIMEOUT);
        columns.put("wxRIGHT", WXRIGHT);
        columns.put("TIMEEND", TIMEEND);
        columns.put("URL", URL);
        columns.put("STATUS", STATUS);
        columns.put("AUDITUSERID", AUDITUSERID);
        columns.put("AUDITDATE", AUDITDATE);
        columns.put("wxTYPE", WXTYPE);
        columns.put("IMAGE", IMAGE);
        columns.put("MODIFYID", MODIFYID);
        columns.put("MODIFYDATE", MODIFYDATE);
        columns.put("CREATID", CREATID);
        columns.put("CREATDATE", CREATDATE);
        columns.put("SMS", SMS);
        columns.put("wxSHARE", WXSHARE);
        columns.put("CORPCODE", CORP_CODE);
        columns.put("dataTableName", DATA_TABLENAME);
        columns.put("dynTableName", DYN_TABLENAME);
        columns.put("tempType", TEMPTYPE);
        columns.put("params", PARAMS);
        columns.put("hasParams", HASPARAMS);
        columns.put("operAppStatus", OPERAPPSTATUS);
        columns.put("operAppNote", OPERAPPNOTE);
        columns.put("sequence", SEQUENCE);
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
