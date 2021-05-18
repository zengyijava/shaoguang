
package com.montnets.emp.table.monitor;

import java.util.HashMap;

import java.util.Map;

public class TableLfMonDbstate
{

	public static final String TABLE_NAME	= "LF_MON_DBSTATE";

	public static final String MODIOPR = "MODIOPR";

	public static final String MONSTATUS = "MONSTATUS";

	public static final String PROCETYPE = "PROCETYPE";

	public static final String CREATETIME = "CREATETIME";

	public static final String DELOPR = "DELOPR";

	public static final String DBCONNECTSTATE = "DBCONNECTSTATE";

	public static final String DBID = "DBID";

	public static final String MAILALFLAG3 = "MAILALFLAG3";

	public static final String MAILALFLAG2 = "MAILALFLAG2";

	public static final String PROCENAME = "PROCENAME";

	public static final String MAILALFLAG5 = "MAILALFLAG5";

	public static final String MAILALFLAG4 = "MAILALFLAG4";

	public static final String MAILALFLAG1 = "MAILALFLAG1";

	public static final String ID = "ID";

	public static final String DISPOPR = "DISPOPR";

	public static final String EVTTYPE = "EVTTYPE";

	public static final String PROCENODE = "PROCENODE";

	public static final String SMSALFLAG4 = "SMSALFLAG4";

	public static final String SMSALFLAG5 = "SMSALFLAG5";

	public static final String SMSALFLAG1 = "SMSALFLAG1";

	public static final String SMSALFLAG2 = "SMSALFLAG2";

	public static final String SMSALFLAG3 = "SMSALFLAG3";

	public static final String ADDOPR = "ADDOPR";

	public static final String UPDATETIME = "UPDATETIME";

	public static final String DBNAME = "DBNAME";
	
	public static final String SERVERNUM = "SERVERNUM";

	public static final String DBADDOPRDES = "DBADDOPRDES";

	public static final String DBDELOPRDES = "DBDELOPRDES";

	public static final String DBMODIOPRDES = "DBMODIOPRDES";
	
	public static final String DBDISPOPRDES = "DBDISPOPRDES";
	
	public static final String DBSERVTIME = "DBSERVTIME";

	public static final String SEQUENCE	= "S_LF_M_DBSTATE";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfMonDbstate", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("modiopr", MODIOPR);
		columns.put("monstatus", MONSTATUS);
		columns.put("procetype", PROCETYPE);
		columns.put("createtime", CREATETIME);
		columns.put("delopr", DELOPR);
		columns.put("dbconnectstate", DBCONNECTSTATE);
		columns.put("dbid", DBID);
		columns.put("mailalflag3", MAILALFLAG3);
		columns.put("mailalflag2", MAILALFLAG2);
		columns.put("procename", PROCENAME);
		columns.put("mailalflag5", MAILALFLAG5);
		columns.put("mailalflag4", MAILALFLAG4);
		columns.put("mailalflag1", MAILALFLAG1);
		columns.put("id", ID);
		columns.put("dispopr", DISPOPR);
		columns.put("evttype", EVTTYPE);
		columns.put("procenode", PROCENODE);
		columns.put("smsalflag4", SMSALFLAG4);
		columns.put("smsalflag5", SMSALFLAG5);
		columns.put("smsalflag1", SMSALFLAG1);
		columns.put("smsalflag2", SMSALFLAG2);
		columns.put("smsalflag3", SMSALFLAG3);
		columns.put("addopr", ADDOPR);
		columns.put("updatetime", UPDATETIME);
		columns.put("dbname", DBNAME);
		columns.put("serverNum", SERVERNUM);
		columns.put("dbaddoprdes", DBADDOPRDES);
		columns.put("dbdeloprdes", DBDELOPRDES);
		columns.put("dbmodioprdes", DBMODIOPRDES);
		columns.put("dbdispoprdes", DBDISPOPRDES);
	    columns.put("dbservtime", DBSERVTIME);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
