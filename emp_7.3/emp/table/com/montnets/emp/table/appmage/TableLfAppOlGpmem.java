					
package com.montnets.emp.table.appmage;

import java.util.HashMap;
import java.util.Map;

public class TableLfAppOlGpmem
{
	public static final String TABLE_NAME	= "LF_APP_OL_GPMEM";

   public static final String GM_STATE= "GM_STATE";

   public static final String GM_USER= "GM_USER";

   public static final String APP_TYPE= "APP_TYPE";

   public static final String G_ID= "G_ID";

   public static final String SEQUENCE	= "26";
   protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfAppOlGpmem", TABLE_NAME);
		
		columns.put("sequence", SEQUENCE);
       
			columns.put("gmstate", GM_STATE);
        
			columns.put("gmuser", GM_USER);
        
			columns.put("apptype", APP_TYPE);
        
			columns.put("gid", G_ID);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					