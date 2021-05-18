package com.montnets.emp.table.tailmanage;

import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;
public class TableGwTdcmd {

	public static final String TABLE_NAME	= "GW_TDCMD";

	public static final String PB_SVRTYPE = "PB_SVRTYPE";

	public static final String TD_ECID = "TD_ECID";

	public static final String MATCHTYPE = "MATCHTYPE";

	public static final String OPTYPE = "OPTYPE";

	public static final String TD_SPNUMBER = "TD_SPNUMBER";

	public static final String PB_SPNUMBER = "PB_SPNUMBER";

	public static final String PB_CROPCODE = "PB_CROPCODE";

	public static final String STATUS = "STATUS";

	public static final String TD_CMD = "TD_CMD";

	public static final String PB_USERID = "PB_USERID";

	public static final String ID = "ID";

	public static final String TD_USERID = "TD_USERID";

	public static final String TD_TIMES = "TD_TIMES";

	public static final String SEQUENCE	= "S_GW_TDCMD";
	protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwTdcmd", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("pbsvrtype", PB_SVRTYPE);
		columns.put("tdecid", TD_ECID);
		columns.put("matchtype", MATCHTYPE);
		columns.put("optype", OPTYPE);
		columns.put("tdspnumber", TD_SPNUMBER);
		columns.put("pbspnumber", PB_SPNUMBER);
		columns.put("pbcropcode", PB_CROPCODE);
		columns.put("status", STATUS);
		columns.put("tdcmd", TD_CMD);
		columns.put("pbuserid", PB_USERID);
		columns.put("id", ID);
		columns.put("tduserid", TD_USERID);
		columns.put("tdtimes", TD_TIMES);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
