							
package com.montnets.emp.table.ydyw;

import java.util.HashMap;
import java.util.Map;

public class TableLfDepTaocan
{

	public static final String TABLE_NAME	= "LF_DEP_TAOCAN";

	public static final String DEP_ID = "DEP_ID";

	public static final String USER_ID = "USER_ID";

	public static final String TAOCAN_CODE = "TAOCAN_CODE";

	public static final String CONTRACT_USER = "CONTRACT_USER";

	public static final String ID = "ID";

	public static final String CONTRACT_DEP = "CONTRACT_DEP";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String SEQUENCE	= "S_LF_DEP_TAOCAN";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfDepTaocan", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("depid", DEP_ID);
		columns.put("userid", USER_ID);
		columns.put("taocancode", TAOCAN_CODE);
		columns.put("contractuser", CONTRACT_USER);
		columns.put("id", ID);
		columns.put("contractdep", CONTRACT_DEP);
		columns.put("corpcode", CORP_CODE);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

						