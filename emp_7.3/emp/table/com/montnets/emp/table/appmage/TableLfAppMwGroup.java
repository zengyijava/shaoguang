package com.montnets.emp.table.appmage;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 梦网APP用户群组
 * @author Administrator
 *
 */
public class TableLfAppMwGroup
{
	public static final String TABLE_NAME	= "LF_APP_MW_GROUP";

   public static final String WG_ID= "WG_ID";

   public static final String NAME= "NAME";

   public static final String MODIFYTIME= "MODIFYTIME";

   public static final String A_ID= "A_ID";

   public static final String COUNT= "COUNT";

   public static final String G_ID= "G_ID";

   public static final String CREATETIME= "CREATETIME";

   public static final String CORP_CODE= "CORP_CODE";

   public static final String SEQUENCE	= "521";
   protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfAppMwGroup", TABLE_NAME);
		columns.put("tableId", G_ID);
		columns.put("sequence", SEQUENCE);
       
			columns.put("wgid", WG_ID);
        
			columns.put("name", NAME);
        
			columns.put("modifytime", MODIFYTIME);
        
			columns.put("aid", A_ID);
        
			columns.put("count", COUNT);
        
			columns.put("gid", G_ID);
        
			columns.put("createtime", CREATETIME);
        
			columns.put("corpcode", CORP_CODE);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					