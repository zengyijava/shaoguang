package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

public class TableLfMonDproce
{

	public static final String				TABLE_NAME		= "LF_MON_DPROCE";

	public static final String			ID			= "ID";
	public static final String			PROCEID			= "PROCEID";
	public static final String			GATEWAYID			= "GATEWAYID";
	public static final String			EVTTYPE			= "EVTTYPE";
	public static final String			CPUUSAGE			= "CPUUSAGE";
	public static final String			MEMUSAGE			= "MEMUSAGE";
	public static final String			VMEMUSAGE			= "VMEMUSAGE";
	public static final String			DISPFREE			= "DISPFREE";
	public static final String			PROCESTATUS			= "PROCESTATUS";
	public static final String			UPDATETIME			= "UPDATETIME";
	public static final String			STARTTIME			= "STARTTIME";
	public static final String			MAXTHREADNUM			= "MAXTHREADNUM";
	public static final String			CURTHREADNUM			= "CURTHREADNUM";
	public static final String			BUSYTHREADNUM			= "BUSYTHREADNUM";
	public static final String			CURSEESIONNUM			= "CURSEESIONNUM";
	public static final String			MAXACTIVENUM			= "MAXACTIVENUM";
	public static final String			CURCONNUM			= "CURCONNUM";
	public static final String			HEAPUSE			= "HEAPUSE";
	public static final String			NONHEAPUSE			= "NONHEAPUSE";
	public static final String			HOSTNAME			= "HOSTNAME";
	public static final String			PROCENAME			= "PROCENAME";
	public static final String			HOSTID			= "HOSTID";
	public static final String			VERSION			= "VERSION";
	public static final String			THRESHOLDFLAG1			= "THRESHOLDFLAG1";
	public static final String			THRESHOLDFLAG2			= "THRESHOLDFLAG2";
	public static final String			THRESHOLDFLAG3			= "THRESHOLDFLAG3";
	public static final String			THRESHOLDFLAG4			= "THRESHOLDFLAG4";
	public static final String			THRESHOLDFLAG5			= "THRESHOLDFLAG5";
	public static final String			THRESHOLDFLAG6			= "THRESHOLDFLAG6";
	
	public static final String			SENDMAILFLAG1 			= "SENDMAILFLAG1";
	public static final String			SENDMAILFLAG2 			= "SENDMAILFLAG2";
	public static final String			SENDMAILFLAG3 			= "SENDMAILFLAG3";
	public static final String			SENDMAILFLAG4 			= "SENDMAILFLAG4";
	public static final String			SENDMAILFLAG5 			= "SENDMAILFLAG5";
	public static final String			SENDMAILFLAG6 			= "SENDMAILFLAG6";
	public static final String 			SERVERNUM               = "SERVERNUM";
	public static final String 			PROCETYPE               = "PROCETYPE";
	public static final String 			DBCONNECTSTATE          = "DBCONNECTSTATE";
    public static final String 			DBSERVTIME 				= "DBSERVTIME";

	public static final String			SEQUENCE		= "S_LF_MON_DPROCE";

	protected static final Map<String, String>	columns			= new HashMap<String, String>();

	static
	{
		columns.put("LfMonDproce", TABLE_NAME);
		
		columns.put("tableId", ID);
		
		columns.put("id",ID);
		columns.put("proceid",PROCEID );
		columns.put("gatewayId",GATEWAYID );
		columns.put("evttype", EVTTYPE);
		columns.put("cpuusage", CPUUSAGE);
		columns.put("memusage", MEMUSAGE);
		columns.put("vmemusage", VMEMUSAGE);
		columns.put("diskFree", DISPFREE);
		columns.put("procestatus", PROCESTATUS);
		columns.put("updatetime", UPDATETIME);
		columns.put("starttime",STARTTIME );
		columns.put("maxthreadnum", MAXTHREADNUM);
		columns.put("curthreadnum", CURTHREADNUM);
		columns.put("busythreadnum", BUSYTHREADNUM);
		columns.put("curseesionnum", CURSEESIONNUM);
		columns.put("maxactivenum", MAXACTIVENUM);
		columns.put("curconnum", CURCONNUM);
		columns.put("heapuse", HEAPUSE);
		columns.put("nonheapuse", NONHEAPUSE);
		columns.put("hostname", HOSTNAME);
		columns.put("procename", PROCENAME);
		columns.put("hostid", HOSTID);
		columns.put("version", VERSION);
		columns.put("thresholdflag1", THRESHOLDFLAG1);
		columns.put("thresholdflag2", THRESHOLDFLAG2);
		columns.put("thresholdflag3", THRESHOLDFLAG3);
		columns.put("thresholdflag4", THRESHOLDFLAG4);
		columns.put("thresholdflag5", THRESHOLDFLAG5);
		columns.put("thresholdflag6", THRESHOLDFLAG6);
		columns.put("sendmailflag1", SENDMAILFLAG1);
		columns.put("sendmailflag2", SENDMAILFLAG2);
		columns.put("sendmailflag3", SENDMAILFLAG3);
		columns.put("sendmailflag4", SENDMAILFLAG4);
		columns.put("sendmailflag5", SENDMAILFLAG5);
		columns.put("sendmailflag6", SENDMAILFLAG6);
        columns.put("serverNum", SERVERNUM);
        columns.put("dbconnectstate", DBCONNECTSTATE);
        columns.put("procetype", PROCETYPE);
        columns.put("dbservtime", DBSERVTIME);
		columns.put("sequence", SEQUENCE);

		

	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
