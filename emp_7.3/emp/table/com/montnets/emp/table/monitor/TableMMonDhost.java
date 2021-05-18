package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

public class TableMMonDhost
{
	public static final String TABLE_NAME = "M_MON_DHOST";

	public static final String VMEMUSE = "VMEMUSE";

	public static final String BOARDTEMP = "BOARDTEMP";

	public static final String ACTTIME = "ACTTIME";

	public static final String VMEMUSAGE = "VMEMUSAGE";

	public static final String UPDATETIME = "UPDATETIME";

	public static final String CPUSJ = "CPUSJ";

	public static final String CPUTEMP = "CPUTEMP";

	public static final String DISKFREESPACE = "DISKFREESPACE";

	public static final String HOSTSTATUS = "HOSTSTATUS";

	public static final String MEMUSAGE = "MEMUSAGE";

	public static final String HOSTID = "HOSTID";

	public static final String CPUUSAGE = "CPUUSAGE";

	public static final String PROCESSCNT = "PROCESSCNT";

	public static final String MEMUSE = "MEMUSE";

	public static final String CPUBL = "CPUBL";

	public static final String DISKTEMP = "DISKTEMP";

	public static final String DISKFREE = "DISKFREE";

	public static final String SEQUENCE = "S_M_MON_DHOST";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("MMonDhost", TABLE_NAME);
		//columns.put("tableId", HOSTID);
		columns.put("sequence", SEQUENCE);

		columns.put("vmemuse", VMEMUSE);

		columns.put("boardtemp", BOARDTEMP);

		columns.put("acttime", ACTTIME);

		columns.put("vmemusage", VMEMUSAGE);

		columns.put("updatetime", UPDATETIME);

		columns.put("cpusj", CPUSJ);

		columns.put("cputemp", CPUTEMP);

		columns.put("diskfreespace", DISKFREESPACE);

		columns.put("hoststatus", HOSTSTATUS);

		columns.put("memusage", MEMUSAGE);

		columns.put("hostid", HOSTID);

		columns.put("cpuusage", CPUUSAGE);

		columns.put("processcnt", PROCESSCNT);

		columns.put("memuse", MEMUSE);

		columns.put("cpubl", CPUBL);

		columns.put("disktemp", DISKTEMP);

		columns.put("diskfree", DISKFREE);

	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
