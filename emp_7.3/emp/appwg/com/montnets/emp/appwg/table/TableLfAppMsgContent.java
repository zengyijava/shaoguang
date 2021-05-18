							
package com.montnets.emp.appwg.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfAppMsgContent
{

	public static final String TABLE_NAME	= "LF_APP_MSGCONTENT";

	

	public static final String ID = "ID";

	public static final String CACHE_ID = "CACHE_ID";

	public static final String MSG_TYPE = "MSG_TYPE";
	
	public static final String CONTENT = "CONTENT";
	
	public static final String SORT_NUM = "SORT_NUM";

	public static final String SEQUENCE	= "S_LF_APP_MSGCONTENT";
	
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfAppMsgContent", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("cacheId", CACHE_ID);
		columns.put("msgType", MSG_TYPE);
		columns.put("content", CONTENT);
		columns.put("sortNum", SORT_NUM);
		
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

						