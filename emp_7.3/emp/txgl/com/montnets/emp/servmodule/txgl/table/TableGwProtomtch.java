package com.montnets.emp.servmodule.txgl.table;

import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;
public class TableGwProtomtch {

	public static final String TABLE_NAME	= "GW_PROTOMTCH";

	public static final String CARGTYPE = "CARGTYPE";

	public static final String CARGVALUE = "CARGVALUE";

	public static final String BELONG = "BELONG";

	public static final String FUNNAME = "FUNNAME";

	public static final String CMDTYPE = "CMDTYPE";

	public static final String MARGNAME = "MARGNAME";

	public static final String CREATETIME = "CREATETIME";

	public static final String BELONGTYPE = "BELONGTYPE";

	public static final String CARGNAME = "CARGNAME";

	public static final String MODIFTIME = "MODIFTIME";

	public static final String FUNTYPE = "FUNTYPE";

	public static final String RESERVE = "RESERVE";

	public static final String ID = "ID";

	public static final String ECID = "ECID";

	public static final String SEQUENCE	= "SEQ_GW_PROTOMTCH";
	protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwProtomtch", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("cargtype", CARGTYPE);
		columns.put("cargvalue", CARGVALUE);
		columns.put("belong", BELONG);
		columns.put("funname", FUNNAME);
		columns.put("cmdtype", CMDTYPE);
		columns.put("margname", MARGNAME);
		columns.put("createtime", CREATETIME);
		columns.put("belongtype", BELONGTYPE);
		columns.put("cargname", CARGNAME);
		columns.put("modiftime", MODIFTIME);
		columns.put("funtype", FUNTYPE);
		columns.put("reserve", RESERVE);
		columns.put("id", ID);
		columns.put("ecid", ECID);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
