package com.montnets.emp.servmodule.txgl.table;


import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;

public class TableGwPushprotomtch {



	public static final String TABLE_NAME	= "GW_PUSHPROTOMTCH";

	private static final String CARGTYPE = "CARGTYPE";

	private static final String RESERVE = "RESERVE";

	private static final String BELONG = "BELONG";

	private static final String CARGVALUE = "CARGVALUE";

	private static final String CMDTYPE = "CMDTYPE";

	private static final String ID = "ID";

	private static final String MARGNAME = "MARGNAME";

	private static final String BELONGTYPE = "BELONGTYPE";

	private static final String CARGNAME = "CARGNAME";

	private static final String PUSHFLAG = "PUSHFLAG";

	private static final String ECID = "ECID";

	private static final String USERID = "USERID";

	private static final String SEQUENCE	= "SEQ_GW_PUSHPROTOMTCH";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwPushprotomtch", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("cargtype", CARGTYPE);
		columns.put("reserve", RESERVE);
		columns.put("belong", BELONG);
		columns.put("cargvalue", CARGVALUE);
		columns.put("cmdtype", CMDTYPE);
		columns.put("id", ID);
		columns.put("margname", MARGNAME);
		columns.put("belongtype", BELONGTYPE);
		columns.put("cargname", CARGNAME);
		columns.put("pushflag", PUSHFLAG);
		columns.put("ecid", ECID);
		columns.put("userid", USERID);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
