package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * 主机动态信息table
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 上午10:16:55
 */
public class TableLfMonDhost
{

    public static final String TABLE_NAME	= "LF_MON_DHOST";

    public static final String MEMUSE = "MEMUSE";

    public static final String CPUUSAGE = "CPUUSAGE";

    public static final String THRESHOLDFLAG1 = "THRESHOLDFLAG1";

    public static final String THRESHOLDFLAG2 = "THRESHOLDFLAG2";

    public static final String THRESHOLDFLAG3 = "THRESHOLDFLAG3";

    public static final String THRESHOLDFLAG4 = "THRESHOLDFLAG4";

    public static final String THRESHOLDFLAG5 = "THRESHOLDFLAG5";

    public static final String THRESHOLDFLAG6 = "THRESHOLDFLAG6";
    
	public static final String			SENDMAILFLAG1 			= "SENDMAILFLAG1";
	public static final String			SENDMAILFLAG2 			= "SENDMAILFLAG2";
	public static final String			SENDMAILFLAG3 			= "SENDMAILFLAG3";
	public static final String			SENDMAILFLAG4 			= "SENDMAILFLAG4";
	public static final String			SENDMAILFLAG5 			= "SENDMAILFLAG5";
	public static final String			SENDMAILFLAG6 			= "SENDMAILFLAG6";

    public static final String VMEMUSAGE = "VMEMUSAGE";

    public static final String VMEMUSE = "VMEMUSE";

    public static final String DISKFREESPACE = "DISKFREESPACE";

    public static final String HOSTNAME = "HOSTNAME";

    public static final String MEMUSAGE = "MEMUSAGE";

    public static final String HOSTSTATUS = "HOSTSTATUS";

    public static final String DISKSPACE = "DISKSPACE";

    public static final String HOSTID = "HOSTID";

    public static final String ID = "ID";

    public static final String PROCESSCNT = "PROCESSCNT";

    public static final String ADAPTER1 = "ADAPTER1";

    public static final String UPDATETIME = "UPDATETIME";

    public static final String EVTTYPE = "EVTTYPE";
    
    public static final String SERVERNUM = "SERVERNUM";
    
    public static final String IPADDR = "IPADDR";
    
    public static final String DBSERVTIME = "DBSERVTIME";

    public static final String SEQUENCE	= "S_LF_MON_DHOST";
    protected static final Map<String , String> columns = new HashMap<String, String>();

    static
    {

        columns.put("LfMonDhost", TABLE_NAME);
        columns.put("tableId", ID);
        columns.put("sequence", SEQUENCE);
        columns.put("memuse", MEMUSE);
        columns.put("cpuusage", CPUUSAGE);
        columns.put("thresholdFlag1", THRESHOLDFLAG1);
        columns.put("thresholdFlag2", THRESHOLDFLAG2);
        columns.put("thresholdFlag3", THRESHOLDFLAG3);
        columns.put("thresholdFlag4", THRESHOLDFLAG4);
        columns.put("thresholdFlag5", THRESHOLDFLAG5);
        columns.put("thresholdFlag6", THRESHOLDFLAG6);
		columns.put("sendmailflag1", SENDMAILFLAG1);
		columns.put("sendmailflag2", SENDMAILFLAG2);
		columns.put("sendmailflag3", SENDMAILFLAG3);
		columns.put("sendmailflag4", SENDMAILFLAG4);
		columns.put("sendmailflag5", SENDMAILFLAG5);
		columns.put("sendmailflag6", SENDMAILFLAG6);
        columns.put("vmemusage", VMEMUSAGE);
        columns.put("vmemuse", VMEMUSE);
        columns.put("diskfreespace", DISKFREESPACE);
        columns.put("hostName", HOSTNAME);
        columns.put("memusage", MEMUSAGE);
        columns.put("hoststatus", HOSTSTATUS);
        columns.put("diskspace", DISKSPACE);
        columns.put("hostid", HOSTID);
        columns.put("id", ID);
        columns.put("processcnt", PROCESSCNT);
        columns.put("adapter1", ADAPTER1);
        columns.put("updatetime", UPDATETIME);
        columns.put("evtType", EVTTYPE);
        columns.put("serverNum", SERVERNUM);
        columns.put("ipAddr", IPADDR);
        columns.put("dbservtime", DBSERVTIME);
    };


    public static Map<String , String> getORM(){

        return columns;

    }
}

