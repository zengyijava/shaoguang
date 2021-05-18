/**
 * Program  : TableLfAppSitPlant.java
 * Author   : zousy
 * Create   : 2014-6-16 下午03:59:05
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.table.appmage;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-16 下午03:59:05
 */
public class TableLfAppSitPlant
{
	public static final String TABLE_NAME	= "LF_APP_SIT_PLANT";

	   public static final String PLANT_TYPE= "PLANT_TYPE";

	   public static final String PAGE_ID= "PAGE_ID";

	   public static final String NAME= "NAME";

	   public static final String FEILD_VALUES= "FEILD_VALUES";

	   public static final String MODITYTIME= "MODITYTIME";

	   public static final String CREATETIME= "CREATETIME";

	   public static final String CORP_CODE= "CORP_CODE";

	   public static final String S_ID= "S_ID";

	   public static final String FEILD_NAMES= "FEILD_NAMES";

	   public static final String PLANT_ID= "PLANT_ID";

	   public static final String SEQUENCE	= "S_LF_APP_SIT_PLANT";
	   protected static final Map<String , String> columns = new HashMap<String, String>();

		static
		{
			columns.put("LfAppSitPlant", TABLE_NAME);
			columns.put("tableId", PLANT_ID);
			columns.put("sequence", SEQUENCE);
	       
				columns.put("planttype", PLANT_TYPE);
	        
				columns.put("pageid", PAGE_ID);
	        
				columns.put("name", NAME);
	        
				columns.put("feildvalues", FEILD_VALUES);
	        
				columns.put("moditytime", MODITYTIME);
	        
				columns.put("createtime", CREATETIME);
	        
				columns.put("corpcode", CORP_CODE);
	        
				columns.put("sid", S_ID);
	        
				columns.put("feildnames", FEILD_NAMES);
	        
				columns.put("plantid", PLANT_ID);
	        
		};


		public static Map<String , String> getORM()
		{
			return columns;
		}
}

