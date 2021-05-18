					
package com.montnets.emp.table.appmage;

import java.util.HashMap;
import java.util.Map;

public class TableLfAppmainpagehis
{
	public static final String TABLE_NAME	= "LF_APPMAINPAGEHIS";

   public static final String SENDSTATE= "SENDSTATE";

   public static final String TOTYPE= "TOTYPE";

   public static final String USER_ID= "USER_ID";

   public static final String SENDTIME= "SENDTIME";

   public static final String TOUSER= "TOUSER";

   public static final String ID= "ID";

   public static final String TASKID= "TASKID";

   public static final String FROMUSER= "FROMUSER";

   public static final String SEQUENCE	= "S_LF_APPMAINPAGEHIS";
   protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfAppmainpagehis", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
       
			columns.put("sendstate", SENDSTATE);
        
			columns.put("totype", TOTYPE);
        
			columns.put("userid", USER_ID);
        
			columns.put("sendtime", SENDTIME);
        
			columns.put("touser", TOUSER);
        
			columns.put("id", ID);
        
			columns.put("taskid", TASKID);
        
			columns.put("fromuser", FROMUSER);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					