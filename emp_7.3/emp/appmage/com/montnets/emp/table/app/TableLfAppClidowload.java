
package com.montnets.emp.table.app;

import java.util.HashMap;
import java.util.Map;

public class TableLfAppClidowload
{

	public static final String TABLE_NAME	= "LF_APP_CLIDOWLOAD";

	public static final String ADUPDATEUSER = "ADUPDATEUSER";

	public static final String CLID = "CLID";

	public static final String A_ID = "A_ID";

	public static final String ADFILEVERSION = "ADFILEVERSION";

	public static final String ADFILEDESCR = "ADFILEDESCR";

	public static final String IOSFILEURL = "IOSFILEURL";

	public static final String ADFILEURL = "ADFILEURL";

	public static final String IOSFILEVERSION = "IOSFILEVERSION";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String ADUPDATEDATE = "ADUPDATEDATE";

	public static final String IOSFILEDESCR = "IOSFILEDESCR";

	public static final String AZFILESIZE = "AZFILESIZE";

	public static final String ADPUSHDATE = "ADPUSHDATE";

	public static final String IOSFILESIZE = "IOSFILESIZE";

	public static final String IOSUPDATEUSER = "IOSUPDATEUSER";

	public static final String IOSUPDATEDATE = "IOSUPDATEDATE";

	public static final String IOSPUSHDATE = "IOSPUSHDATE";

	public static final String SEQUENCE	= "S_LF_APP_CLIDOWLOAD";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfAppClidowload", TABLE_NAME);
		columns.put("tableId",CLID );
		columns.put("sequence", SEQUENCE);
		columns.put("adupdateuser", ADUPDATEUSER);
		columns.put("clid", CLID);
		columns.put("aid", A_ID);
		columns.put("adfileversion", ADFILEVERSION);
		columns.put("adfiledescr", ADFILEDESCR);
		columns.put("iosfileurl", IOSFILEURL);
		columns.put("adfileurl", ADFILEURL);
		columns.put("iosfileversion", IOSFILEVERSION);
		columns.put("corpcode", CORP_CODE);
		columns.put("adupdatedate", ADUPDATEDATE);
		columns.put("iosfiledescr", IOSFILEDESCR);
		columns.put("azfilesize", AZFILESIZE);
		columns.put("adpushdate", ADPUSHDATE);
		columns.put("iosfilesize", IOSFILESIZE);
		columns.put("iosupdateuser", IOSUPDATEUSER);
		columns.put("iosupdatedate", IOSUPDATEDATE);
		columns.put("iospushdate", IOSPUSHDATE);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
