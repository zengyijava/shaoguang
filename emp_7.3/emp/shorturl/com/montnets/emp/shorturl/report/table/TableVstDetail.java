package com.montnets.emp.shorturl.report.table;

import java.util.HashMap;
import java.util.Map;

public class TableVstDetail {
    public static final String TABLE_NAME	= "VST_DETAIL";

    public static final String ID = "ID";
    public static final String PTMSGID = "PTMSGID";
    public static final String VSTMSGID = "VSTMSGID";
    public static final String USERID = "USERID";
    public static final String ECID = "ECID";
    public static final String PHONE = "PHONE";
    public static final String MOBILEAREA = "MOBILEAREA";
    public static final String CTTM = "CTTM";
    public static final String VSTTM = "VSTTM";
    public static final String PTCODE = "PTCODE";
    public static final String WGNO = "WGNO";
    public static final String SRCADDRESS = "SRCADDRESS";
    public static final String XWAPPROF = "XWAPPROF";
    public static final String XBROTYPE = "XBROTYPE";
    public static final String DRS = "DRS";
    public static final String TIMES = "TIMES";
    public static final String CUTID = "CUSTID";
    public static final String SURL = "SURL";
    public static final String LURL = "LURL";
    public static final String HTTPHEADER = "HTTPHEADER";
    public static final String EXDATA = "EXDATA";
    public static final String SRCPT = "SRCPT";
    public static final String SID = "SID";

    protected static final Map<String , String> columns = new HashMap<String, String>();

    static {
        columns.put("VstDetail", TABLE_NAME);
        columns.put("id", ID);
        columns.put("ptmsgid", PTMSGID);
        columns.put("vstmsgid", VSTMSGID);
        columns.put("userid", USERID);
        columns.put("ecid", ECID);
        columns.put("phone", PHONE);
        columns.put("mobilearea", MOBILEAREA);
        columns.put("cttm", CTTM);
        columns.put("vsttm", VSTTM);
        columns.put("ptcode", PTCODE);
        columns.put("wgno", WGNO);
        columns.put("srcaddress",SRCADDRESS );
        columns.put("xwapprof", XWAPPROF);
        columns.put("xbrotype", XBROTYPE);
        columns.put("drs", DRS);
        columns.put("times", TIMES);
        columns.put("cutid",CUTID );
        columns.put("surl",SURL );
        columns.put("lurl", LURL);
        columns.put("http_header",HTTPHEADER );
        columns.put("exdata",EXDATA );
        columns.put("srcpt",SRCPT );
        columns.put("sid",SID );
    }

    public static Map<String , String> getORM(){
        return columns;
    }
}
