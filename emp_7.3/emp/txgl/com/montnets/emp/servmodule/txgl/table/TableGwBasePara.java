package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableGwBasePara {

	public static final String TABLE_NAME	= "GW_BASEPARA";

	public static final String ID = "ID";

	public static final String FUNNAME = "FUNNAME";

	public static final String CMDTYPE = "CMDTYPE";

	public static final String ARGNAME = "ARGNAME";

	public static final String ARGVALUELEN = "ARGVALUELEN";

	public static final String ARGDES = "ARGDES";

	public static final String ARGTYPE = "ARGTYPE";
//
	public static final String CREATETIME = "CREATETIME";
//
	public static final String MODIFTIME = "MODIFTIME";

	public static final String RESERVE = "RESERVE";

	public static final String SEQUENCE	= "S_GW_BASEPARA";

	protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwBasePara", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		
		columns.put("id", ID);
		columns.put("funName", FUNNAME);
		columns.put("cmdType", CMDTYPE);
		columns.put("argName", ARGNAME);
		columns.put("argValueLen", ARGVALUELEN);
		columns.put("argDes", ARGDES);
		columns.put("argType", ARGTYPE);
		columns.put("createTime", CREATETIME);
		columns.put("modifTime", MODIFTIME);
		columns.put("reserve", RESERVE);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
