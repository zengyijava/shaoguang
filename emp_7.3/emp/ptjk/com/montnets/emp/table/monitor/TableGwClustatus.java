
package com.montnets.emp.table.monitor;

import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;

public class TableGwClustatus
{

	public static final String  TABLE_NAME	= "GW_CLUSTATUS";

	public static final String UPDTIME = "UPDTIME";

	public static final String ID = "ID";

	public static final String GWNO = "GWNO";

	public static final String RUNWEIGHT = "RUNWEIGHT";

	public static final String GWTYPE = "GWTYPE";

	public static final String GWEIGHT = "GWEIGHT";

	public static final String RUNSTATUS = "RUNSTATUS";

	public static final String PRIGWNO = "PRIGWNO";

	public static final String SEQUENCE	= "SEQ_GW_CLUSTATUS";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwClustatus", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("updtime", UPDTIME);
		columns.put("id", ID);
		columns.put("gwno", GWNO);
		columns.put("runweight", RUNWEIGHT);
		columns.put("gwtype", GWTYPE);
		columns.put("gweight", GWEIGHT);
		columns.put("runstatus", RUNSTATUS);
		columns.put("prigwno", PRIGWNO);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
