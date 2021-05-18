package com.montnets.emp.table.wy;
/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-3-25 上午11:14:09
 */	

import java.util.HashMap;
import java.util.Map;
/**
 * @description 
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-3-25 上午11:14:09
 */
public class TableAAreacode
{
	public static final String TABLE_NAME	= "A_AREACODE";

   public static final String AREANAME= "AREANAME";

   public static final String ID= "ID";

   public static final String CREATETIME= "CREATETIME";

   public static final String AREACODE= "AREACODE";

   public static final String SEQUENCE	= "SEQ_A_AREACODE";
   protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("AAreacode", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
       
			columns.put("areaname", AREANAME);
        
			columns.put("id", ID);
        
			columns.put("createtime", CREATETIME);
        
			columns.put("areacode", AREACODE);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					