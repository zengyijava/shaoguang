package com.montnets.emp.servmodule.txgl.table;

import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;

public class TableGwBaseprotocol
{

	public static final String TABLE_NAME	= "GW_BASEPROTOCOL";

	public static final String FUNTYPE = "FUNTYPE";

	public static final String CFUNNAME = "CFUNNAME";

	public static final String MODIYTM = "MODIYTM";

	public static final String FUNNAME = "FUNNAME";

	public static final String FMTMSG = "FMTMSG";

	public static final String CMDTYPE = "CMDTYPE";

	public static final String ID = "ID";

	public static final String CUST_INTFNAME = "CUST_INTFNAME";

	public static final String STATUS = "STATUS";

	public static final String CREATETM = "CREATETM";

	public static final String RETTYPE = "RETTYPE";

	public static final String ECID = "ECID";

	public static final String SEQUENCE	= "SEQ_GW_BASEPROTOCOL";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwBaseprotocol", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("funtype", FUNTYPE);
		columns.put("cfunname", CFUNNAME);
		columns.put("modiytm", MODIYTM);
		columns.put("funname", FUNNAME);
		columns.put("fmtmsg", FMTMSG);
		columns.put("cmdtype", CMDTYPE);
		columns.put("id", ID);
		columns.put("custintfname", CUST_INTFNAME);
		columns.put("status", STATUS);
		columns.put("createtm", CREATETM);
		columns.put("rettype", RETTYPE);
		columns.put("ecid", ECID);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

						
