package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

public class TableLfMonWbsbuf
{

	public static final String				TABLE_NAME		= "LF_MON_WBSBUF";

	public static final String			RPTNOSD			= "RPTNOSD";

	public static final String			MTNOSD			= "MTNOSD";

	public static final String			RPTSDBUF		= "RPTSDBUF";

	public static final String			MORPTSDSPD1		= "MORPTSDSPD1";

	public static final String			WRMTBUF			= "WRMTBUF";

	public static final String			LOGFILENUM		= "LOGFILENUM";

	public static final String			MORPTSDBUF		= "MORPTSDBUF";

	public static final String			RECVBUF			= "RECVBUF";

	public static final String			PROCEID			= "PROCEID";

	public static final String			MORPTRVSPD1		= "MORPTRVSPD1";

	public static final String			MONOSD			= "MONOSD";

	public static final String			MORPTRVSPD2		= "MORPTRVSPD2";

	public static final String			PRERSPBUF		= "PRERSPBUF";

	public static final String			MORPTSDSPD2		= "MORPTSDSPD2";

	public static final String			ID				= "ID";

	public static final String			SUPPSDBUF		= "SUPPSDBUF";

	public static final String			PRECNT			= "PRECNT";

	public static final String			ENDCNT			= "ENDCNT";

	public static final String			MTSDSPD1		= "MTSDSPD1";

	public static final String			MTSDSPD2		= "MTSDSPD2";

	public static final String			LOGBUF			= "LOGBUF";

	public static final String			WRRPTBUF		= "WRRPTBUF";

	public static final String			UPDMOBUF		= "UPDMOBUF";

	public static final String			MOSDCNT			= "MOSDCNT";

	public static final String			WRMTLVLBUF		= "WRMTLVLBUF";

	public static final String			MTSDBUF			= "MTSDBUF";

	public static final String			WRMTVFYBUF		= "WRMTVFYBUF";

	public static final String			UPDATETIME		= "UPDATETIME";

	public static final String			MTRVSPD2		= "MTRVSPD2";

	public static final String			MTWAITBUF		= "MTWAITBUF";

	public static final String			MTRVSPD1		= "MTRVSPD1";

	public static final String			WRMOBUF			= "WRMOBUF";

	public static final String			RESNDBUF		= "RESNDBUF";

	public static final String			MTSDCNT			= "MTSDCNT";

	public static final String			MORVCNT			= "MORVCNT";

	public static final String			MORPTWAITBUF	= "MORPTWAITBUF";

	public static final String			ENDRSPBUF		= "ENDRSPBUF";

	public static final String			GATEWAYID		= "GATEWAYID";

	public static final String			MTRVCNT			= "MTRVCNT";

	public static final String			PRERSPTMPBUF	= "PRERSPTMPBUF";

	public static final String			SEQUENCE		= "S_LF_MON_WBSBUF";

	protected static final Map<String, String>	columns			= new HashMap<String, String>();

	static
	{
		columns.put("LfMonWbsbuf", TABLE_NAME);

		columns.put("tableId", ID);

		columns.put("sequence", SEQUENCE);

		columns.put("rptnosd", RPTNOSD);

		columns.put("mtnosd", MTNOSD);

		columns.put("rptsdbuf", RPTSDBUF);

		columns.put("morptsdspd1", MORPTSDSPD1);

		columns.put("wrmtbuf", WRMTBUF);

		columns.put("logfilenum", LOGFILENUM);

		columns.put("morptsdbuf", MORPTSDBUF);

		columns.put("recvbuf", RECVBUF);

		columns.put("proceid", PROCEID);

		columns.put("morptrvspd1", MORPTRVSPD1);

		columns.put("monosd", MONOSD);

		columns.put("morptrvspd2", MORPTRVSPD2);

		columns.put("prerspbuf", PRERSPBUF);

		columns.put("morptsdspd2", MORPTSDSPD2);

		columns.put("id", ID);

		columns.put("suppsdbuf", SUPPSDBUF);

		columns.put("precnt", PRECNT);

		columns.put("endcnt", ENDCNT);

		columns.put("mtsdspd1", MTSDSPD1);

		columns.put("mtsdspd2", MTSDSPD2);

		columns.put("logbuf", LOGBUF);

		columns.put("wrrptbuf", WRRPTBUF);

		columns.put("updmobuf", UPDMOBUF);

		columns.put("mosdcnt", MOSDCNT);

		columns.put("wrmtlvlbuf", WRMTLVLBUF);

		columns.put("mtsdbuf", MTSDBUF);

		columns.put("wrmtvfybuf", WRMTVFYBUF);

		columns.put("updatetime", UPDATETIME);

		columns.put("mtrvspd2", MTRVSPD2);

		columns.put("mtwaitbuf", MTWAITBUF);

		columns.put("mtrvspd1", MTRVSPD1);

		columns.put("wrmobuf", WRMOBUF);

		columns.put("resndbuf", RESNDBUF);

		columns.put("mtsdcnt", MTSDCNT);

		columns.put("morvcnt", MORVCNT);

		columns.put("morptwaitbuf", MORPTWAITBUF);

		columns.put("endrspbuf", ENDRSPBUF);

		columns.put("gatewayid", GATEWAYID);

		columns.put("mtrvcnt", MTRVCNT);

		columns.put("prersptmpbuf", PRERSPTMPBUF);

	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
