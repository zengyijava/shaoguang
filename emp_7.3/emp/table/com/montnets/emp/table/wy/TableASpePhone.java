package com.montnets.emp.table.wy;

import java.util.HashMap;
import java.util.Map;

public class TableASpePhone
{
	public static final String TABLE_NAME	= "A_SPE_PHONE";

   public static final String PHONE= "PHONE";

   public static final String ID= "ID";

   public static final String CUSTID= "CUSTID";

   public static final String CREATETIME= "CREATETIME";

   public static final String SPECTYPE= "SPECTYPE";

   public static final String OPTTYPE= "OPTTYPE";

   public static final String UNICOM= "UNICOM";

   public static final String USERID= "USERID";

   public static final String SEQUENCE	= "SEQ_A_SPE_PHONE";
   protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("ASpePhone", TABLE_NAME);
		
		columns.put("tableId", ID);
		
		columns.put("sequence", SEQUENCE);
       
		columns.put("phone", PHONE);
    
		columns.put("id", ID);
    
		columns.put("custid", CUSTID);
    
		columns.put("createtime", CREATETIME);
    
		columns.put("spectype", SPECTYPE);
    
		columns.put("opttype", OPTTYPE);
    
		columns.put("unicom", UNICOM);
    
		columns.put("userid", USERID);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

