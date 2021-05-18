package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableGwPushRsProtocol 
{

	public static final String TABLE_NAME	= "GW_PUSHRSPROTOCOL";

	public static final String ID = "ID";

	public static final String ECID = "ECID";

	public static final String USERID = "USERID";

	public static final String RSPCMD = "RSPCMD";

	public static final String CARGNAME = "CARGNAME";

	public static final String RSPSTATUS = "RSPSTATUS";

	public static final String CRSPFMT = "CRSPFMT";

	public static final String CARGVALUE = "CARGVALUE";

	public static final String SEQUENCE	= "S_GW_PUSHRSPROTOCOL";
	
	protected static final  Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwPushRsProtocol", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		
		columns.put("id", ID);
		columns.put("ecid", ECID);
		columns.put("userid", USERID);
		columns.put("rspCmd", RSPCMD);
		columns.put("cargName", CARGNAME);
		columns.put("rspStatus", RSPSTATUS);
		columns.put("crspfmt", CRSPFMT);
		columns.put("cargValue", CARGVALUE);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
