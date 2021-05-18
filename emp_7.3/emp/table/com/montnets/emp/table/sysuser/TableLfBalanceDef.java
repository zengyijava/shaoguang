
package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;

public class TableLfBalanceDef
{

	public static final String TABLE_NAME	= "LF_BALANCE_DEF";

	public static final String MODI_TIME = "MODI_TIME";

	public static final String SMS_COUNT = "SMS_COUNT";

	public static final String MMS_COUNT = "MMS_COUNT";

	public static final String DEP_ID = "DEP_ID";

	public static final String CREATE_TIME = "CREATE_TIME";

	public static final String MODI_USERID = "MODI_USERID";

	public static final String ID = "ID";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String SEQUENCE	= "S_LF_BL_DEF";

	protected static  final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfBalanceDef", TABLE_NAME);
		columns.put("tableId",ID );
		columns.put("sequence", SEQUENCE);
		columns.put("moditime", MODI_TIME);
		columns.put("smscount", SMS_COUNT);
		columns.put("mmscount", MMS_COUNT);
		columns.put("depid", DEP_ID);
		columns.put("createtime", CREATE_TIME);
		columns.put("modiuserid", MODI_USERID);
		columns.put("id", ID);
		columns.put("corpcode", CORP_CODE);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
