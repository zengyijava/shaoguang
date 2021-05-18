					
package com.montnets.emp.table.engine;

import java.util.HashMap;
import java.util.Map;

public class TableLfAutoreply
{
	public final static String TABLE_NAME	= "LF_AUTOREPLY";

   public static final String STATE= "STATE";

   public static final String ID= "ID";

   public static final String REPLY_CONTENT= "REPLY_CONTENT";

   public static final String CORP_CODE= "CORP_CODE";

   public static final String TYPE= "TYPE";

   public static final String SEQUENCE	= "S_LF_AUTOREPLY";
   protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfAutoreply", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
       
			columns.put("state", STATE);
        
			columns.put("id", ID);
        
			columns.put("replycontent", REPLY_CONTENT);
        
			columns.put("corpcode", CORP_CODE);
        
			columns.put("type", TYPE);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					