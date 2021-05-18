					
package com.montnets.emp.table.appmage;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class TableLfAppMwGpmem
{
	public static final String TABLE_NAME	= "LF_APP_MW_GPMEM";

   public static final String GM_STATU= "GM_STATU";

   public static final String GM_USER= "GM_USER";

   public static final String G_ID= "G_ID";

   public static final String APP_TYPE= "APP_TYPE";

   public static final String SEQUENCE	= "26";
   protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfAppMwGpmem", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
       
			columns.put("gmstatu", GM_STATU);
        
			columns.put("gmuser", GM_USER);
        
			columns.put("gid", G_ID);
        
			columns.put("apptype", APP_TYPE);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

		

					