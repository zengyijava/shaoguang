package com.montnets.emp.shorturl.surlmanage.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfDfadvanced
{

	public static final String TABLE_NAME	= "LF_DFADVANCED";

	public static final String REPLY_SET = "REPLY_SET";

	public static final String BUS_CODE = "BUS_CODE";

	public static final String REPEAT_FILTER = "REPEAT_FILTER";

	public static final String USER_ID = "USER_ID";

	public static final String CREATE_TIME = "CREATE_TIME";

	public static final String SPUSER_ID = "SPUSER_ID";

	public static final String PRIORITY = "PRIORITY";

	public static final String ID = "ID";

	public static final String FLAG = "FLAG";

	public static final String SEQUENCE	= "S_LF_DFADVANCED";
	protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfDfadvanced", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("replyset", REPLY_SET);
		columns.put("buscode", BUS_CODE);
		columns.put("repeatfilter", REPEAT_FILTER);
		columns.put("userid", USER_ID);
		columns.put("createtime", CREATE_TIME);
		columns.put("spuserid", SPUSER_ID);
		columns.put("priority", PRIORITY);
		columns.put("id", ID);
		columns.put("flag", FLAG);
	};


	public static Map<String , String> getORM(){
		return columns;
	}
}