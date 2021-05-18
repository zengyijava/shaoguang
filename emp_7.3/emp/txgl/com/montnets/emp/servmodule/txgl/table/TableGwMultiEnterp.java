package com.montnets.emp.servmodule.txgl.table;


import java.util.HashMap;
import java.util.Map;

public class TableGwMultiEnterp
{

	public static final String TABLE_NAME	= "GW_MULTI_ENTERP";

	public static final String MATCHCNT = "MATCHCNT";

	public static final String FUNTYPE = "FUNTYPE";

	public static final String BOOKCNT = "BOOKCNT";

	public static final String MODIYTM = "MODIYTM";

	public static final String RESPFMT = "RESPFMT";

	public static final String STATUS = "STATUS";

	public static final String CREATETM = "CREATETM";

	public static final String ECID = "ECID";
	public static final String REQFMT = "REQFMT";

	public static final String SEQUENCE	= "S_GW_MULTI_ENTERP";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwMultiEnterp", TABLE_NAME);
		columns.put("tableId", "");
		columns.put("sequence", SEQUENCE);
		columns.put("matchcnt", MATCHCNT);
		columns.put("funtype", FUNTYPE);
		columns.put("bookcnt", BOOKCNT);
		columns.put("modiytm", MODIYTM);
		columns.put("respfmt", RESPFMT);
		columns.put("status", STATUS);
		columns.put("createtm", CREATETM);
		columns.put("reqfmt", REQFMT);
		columns.put("ecid", ECID);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
