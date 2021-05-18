
package com.montnets.emp.table.tailmanage;


import java.util.HashMap;

import java.util.Map;

public class TableGwTailctrl
{

	public static final String TABLE_NAME	= "GW_TAILCTRL";

	public static final String USER_ID = "USER_ID";

	public static final String CREATE_TIME = "CREATE_TIME";

	public static final String ID = "ID";

	public static final String UPDATE_TIME = "UPDATE_TIME";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String OVERTAILFLAG = "OVERTAILFLAG";

	public static final String OTHERTAILFLAG = "OTHERTAILFLAG";

	public static final String SEQUENCE	= "SEQ_GW_TAILCTRL";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwTailctrl", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("userid", USER_ID);
		columns.put("createtime", CREATE_TIME);
		columns.put("id", ID);
		columns.put("updatetime", UPDATE_TIME);
		columns.put("corpcode", CORP_CODE);
		columns.put("overtailflag", OVERTAILFLAG);
		columns.put("othertailflag", OTHERTAILFLAG);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
