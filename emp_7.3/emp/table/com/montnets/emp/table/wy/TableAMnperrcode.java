/**
 * Program  : TableAMnperrcode.java
 * Author   : zousy
 * Create   : 2014-3-26 上午10:34:51
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.table.wy;

import java.util.HashMap;
import java.util.Map;

/**
 * 携号转网错误码表
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-3-26 上午10:34:51
 */
public class TableAMnperrcode
{
	public static final String TABLE_NAME	= "A_MNPERRCODE";

   public static final String ID= "ID";

   public static final String ERRORCODE= "ERRORCODE";
   
   public static final String CREATETIME= "CREATETIME";

   public static final String STATUS= "STATUS";

   public static final String TYPE= "TYPE";
   
   public static final String MNPTYPE= "MNPTYPE";

   public static final String SEQUENCE	= "SEQ_A_MNPERRCODE";
   protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("AMnperrcode", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
       
		columns.put("errorcode", ERRORCODE);
    
		columns.put("createtime", CREATETIME);
    
		columns.put("status", STATUS);
    
		columns.put("type", TYPE);
		
		columns.put("mnptype", MNPTYPE);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}

}

