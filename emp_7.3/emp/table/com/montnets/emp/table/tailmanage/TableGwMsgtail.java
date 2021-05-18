
package com.montnets.emp.table.tailmanage;

import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;

public class TableGwMsgtail
{

	public static final String TABLE_NAME	= "GW_MSGTAIL";

	public static final String TAIL_ID = "TAIL_ID";

	public static final String USER_ID = "USER_ID";

	public static final String CREATE_TIME = "CREATE_TIME";

	public static final String CONTENT = "CONTENT";

	public static final String TAIL_NAME = "TAIL_NAME";

	public static final String UPDATE_TIME = "UPDATE_TIME";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String SEQUENCE	= "SEQ_GW_MSGTAIL";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwMsgtail", TABLE_NAME);
		columns.put("tableId", TAIL_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("tailid", TAIL_ID);
		columns.put("userid", USER_ID);
		columns.put("createtime", CREATE_TIME);
		columns.put("content", CONTENT);
		columns.put("tailname", TAIL_NAME);
		columns.put("updatetime", UPDATE_TIME);
		columns.put("corpcode", CORP_CODE);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
