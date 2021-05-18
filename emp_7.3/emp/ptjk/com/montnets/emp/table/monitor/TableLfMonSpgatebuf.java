package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

public class TableLfMonSpgatebuf
{

	public static final String				TABLE_NAME		= "LF_MON_SPGATEBUF";

	public static final String			RPTSDWAITBUF	= "RPTSDWAITBUF";

	public static final String			MOSDBUF			= "MOSDBUF";

	public static final String			RPTSDBUF		= "RPTSDBUF";

	public static final String			MTUPDBUF		= "MTUPDBUF";

	public static final String			MTSDBUF			= "MTSDBUF";

	public static final String			UPDATETIME		= "UPDATETIME";

	public static final String			PROCEID			= "PROCEID";

	public static final String			MOSDWAITBUF		= "MOSDWAITBUF";

	public static final String			RPTRVCNT		= "RPTRVCNT";

	public static final String			ID				= "ID";

	public static final String			MTSDCNT			= "MTSDCNT";

	public static final String			MTSPD1			= "MTSPD1";

	public static final String			MORVCNT			= "MORVCNT";

	public static final String			MTSPD2			= "MTSPD2";

	public static final String			MORPTSPD		= "MORPTSPD";

	public static final String			RPTRVBUF		= "RPTRVBUF";

	public static final String			MORVBUF			= "MORVBUF";

	public static final String			GATEWAYID		= "GATEWAYID";

	public static final String			MTRVCNT			= "MTRVCNT";

	public static final String			MTSDWAITBUF		= "MTSDWAITBUF";

	public static final String			SEQUENCE		= "S_LF_MON_SPGATEBUF";

	protected static final Map<String, String>	columns			= new HashMap<String, String>();

	static
	{
		columns.put("LfMonSpgatebuf", TABLE_NAME);

		columns.put("tableId", ID);

		columns.put("sequence", SEQUENCE);

		columns.put("rptsdwaitbuf", RPTSDWAITBUF);

		columns.put("mosdbuf", MOSDBUF);

		columns.put("rptsdbuf", RPTSDBUF);

		columns.put("mtupdbuf", MTUPDBUF);

		columns.put("mtsdbuf", MTSDBUF);

		columns.put("updatetime", UPDATETIME);

		columns.put("proceid", PROCEID);

		columns.put("mosdwaitbuf", MOSDWAITBUF);

		columns.put("rptrvcnt", RPTRVCNT);

		columns.put("id", ID);

		columns.put("mtsdcnt", MTSDCNT);

		columns.put("mtspd1", MTSPD1);

		columns.put("morvcnt", MORVCNT);

		columns.put("mtspd2", MTSPD2);

		columns.put("morptspd", MORPTSPD);

		columns.put("rptrvbuf", RPTRVBUF);

		columns.put("morvbuf", MORVBUF);

		columns.put("gatewayid", GATEWAYID);

		columns.put("mtrvcnt", MTRVCNT);

		columns.put("mtsdwaitbuf", MTSDWAITBUF);

	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
