
package com.montnets.emp.table.tailmanage;

import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;

public class TableGwTailbind
{

	public static final String TABLE_NAME	= "GW_TAILBIND";

	public static final String TAIL_ID = "TAIL_ID";

	public static final String BUS_CODE = "BUS_CODE";

	public static final String USER_ID = "USER_ID";

	public static final String CREATE_TIME = "CREATE_TIME";

	public static final String ID = "ID";

	public static final String TAIL_TYPE = "TAIL_TYPE";

	public static final String UPDATE_TIME = "UPDATE_TIME";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String SPUSERID = "SPUSERID";

	public static final String SEQUENCE	= "SEQ_GW_TAILBIND";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwTailbind", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("tailid", TAIL_ID);
		columns.put("buscode", BUS_CODE);
		columns.put("userid", USER_ID);
		columns.put("createtime", CREATE_TIME);
		columns.put("id", ID);
		columns.put("tailtype", TAIL_TYPE);
		columns.put("updatetime", UPDATE_TIME);
		columns.put("corpcode", CORP_CODE);
		columns.put("spuserid", SPUSERID);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
