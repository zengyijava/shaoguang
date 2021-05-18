package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * 程序动态信息table
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 上午10:17:14
 */
public class TableLfMonSproce
{

	public static final String				TABLE_NAME		= "LF_MON_SPROCE";

	public static final String			CURCONUSE		= "CURCONUSE";

	public static final String			VMEMUSE			= "VMEMUSE";

	public static final String			PROCETYPE		= "PROCETYPE";

	public static final String			PROCEID			= "PROCEID";

	public static final String			MONSTATUS		= "MONSTATUS";

	public static final String			PROCENAME		= "PROCENAME";

	public static final String			CURSEESIONUSE	= "CURSEESIONUSE";

	public static final String			MODIFYTIME		= "MODIFYTIME";

	public static final String			DESCR			= "DESCR";

	public static final String			MONPHONE		= "MONPHONE";
	
	public static final String			MONEMAIL	    = "MONEMAIL";

	public static final String			HEAPUSE			= "HEAPUSE";

	public static final String			DBSPACEUSE		= "DBSPACEUSE";

	public static final String			HOSTID			= "HOSTID";

	public static final String			CREATETIME		= "CREATETIME";

	public static final String			MEMUSE			= "MEMUSE";

	public static final String			HARDDISKSPACE	= "HARDDISKSPACE";

	public static final String			MONFREQ			= "MONFREQ";

	public static final String			CPUBL			= "CPUBL";

	public static final String			DBCONNUM		= "DBCONNUM";

	public static final String			NONHEAPUSE		= "NONHEAPUSE";

	public static final String			VERSION			= "VERSION";

	public static final String			CURTHREADUSE	= "CURTHREADUSE";

	public static final String			GATEWAYID		= "GATEWAYID";

	public static final String			PROCEUSESTATUS	= "PROCEUSESTATUS";
	
	public static final String			ISDBCONNECT	= "ISDBCONNECT";
	
	public static final String			SERVERNUM	= "SERVERNUM";

	public static final String			SEQUENCE		= "S_LF_MON_SPROCE";

	protected static final Map<String, String>	columns			= new HashMap<String, String>();

	static
	{
		columns.put("LfMonSproce", TABLE_NAME);
		columns.put("tableId", PROCEID);
		columns.put("sequence", SEQUENCE);

		columns.put("curconuse", CURCONUSE);

		columns.put("vmemuse", VMEMUSE);

		columns.put("procetype", PROCETYPE);

		columns.put("proceid", PROCEID);

		columns.put("monstatus", MONSTATUS);

		columns.put("procename", PROCENAME);

		columns.put("curseesionuse", CURSEESIONUSE);

		columns.put("modifytime", MODIFYTIME);

		columns.put("descr", DESCR);

		columns.put("monphone", MONPHONE);

		columns.put("monemail", MONEMAIL);
		
		columns.put("heapuse", HEAPUSE);

		columns.put("dbspaceuse", DBSPACEUSE);

		columns.put("hostid", HOSTID);

		columns.put("createtime", CREATETIME);

		columns.put("memuse", MEMUSE);

		columns.put("harddiskspace", HARDDISKSPACE);

		columns.put("monfreq", MONFREQ);

		columns.put("cpubl", CPUBL);

		columns.put("dbconnum", DBCONNUM);

		columns.put("nonheapuse", NONHEAPUSE);

		columns.put("version", VERSION);

		columns.put("curthreaduse", CURTHREADUSE);

		columns.put("gatewayid", GATEWAYID);

		columns.put("proceusestatus", PROCEUSESTATUS);
		
		columns.put("isDbconnect", ISDBCONNECT);
		
		columns.put("servernum", SERVERNUM);
	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
