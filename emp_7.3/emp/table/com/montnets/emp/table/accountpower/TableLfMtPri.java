package com.montnets.emp.table.accountpower;


import java.util.HashMap;

import java.util.Map;
public class TableLfMtPri {



	public static final String TABLE_NAME	= "LF_MT_PRI";

	public static final String USER_ID = "USER_ID";

	public static final String ID = "ID";

	public static final String CREATETIME = "CREATETIME";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String CREATE_USERID = "CREATE_USERID";

	public static final String SPUSERID = "SPUSERID";

	public static final String SEQUENCE	= "S_LF_MT_PRI";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfMtPri", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("userid", USER_ID);
		columns.put("id", ID);
		columns.put("createtime", CREATETIME);
		columns.put("corpcode", CORP_CODE);
		columns.put("createuserid", CREATE_USERID);
		columns.put("spuserid", SPUSERID);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
