					
package com.montnets.emp.table.appmage;

import java.util.HashMap;
import java.util.Map;

public class TableLfAppOlGroup
{
	public static final String TABLE_NAME	= "LF_APP_OL_GROUP";

   public static final String GP_NAME= "GP_NAME";

   public static final String MOUNTCOUNT= "MOUNTCOUNT";

   public static final String CREATE_TIME= "CREATE_TIME";

   public static final String CREATE_USER= "CREATE_USER";

   public static final String APP_TYPE= "APP_TYPE";

   public static final String GP_ID= "GP_ID";

   public static final String SEQUENCE	= "521";
   protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfAppOlGroup", TABLE_NAME);
		columns.put("tableId", GP_ID);
		columns.put("sequence", SEQUENCE);
       
			columns.put("gpname", GP_NAME);
        
			columns.put("mountcount", MOUNTCOUNT);
        
			columns.put("createtime", CREATE_TIME);
        
			columns.put("createuser", CREATE_USER);
        
			columns.put("apptype", APP_TYPE);
        
			columns.put("gpid", GP_ID);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					