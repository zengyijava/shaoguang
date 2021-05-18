/**
 * Program  : TableLfAppSitInfo.java
 * Author   : zousy
 * Create   : 2014-6-17 上午09:26:22
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
 * @2014-6-17 上午09:26:22
 */
public class TableLfAppSitInfo
{
	public static final String TABLE_NAME	= "LF_APP_SIT_INFO";

	   public static final String PUBLISHTIME= "PUBLISHTIME";

	   public static final String NAME= "NAME";

	   public static final String MODITYTIME= "MODITYTIME";

	   public static final String IS_SYSTEM= "IS_SYSTEM";

	   public static final String TYPE_ID= "TYPE_ID";

	   public static final String CREATETIME= "CREATETIME";

	   public static final String STATUS= "STATUS";

	   public static final String CORP_CODE= "CORP_CODE";

	   public static final String URL= "URL";

	   public static final String S_ID= "S_ID";

	   public static final String USERID= "USERID";
	   
	   public static final String validity= "validity";
	   
	   public static final String SERIAL= "SERIAL";
	   public static final String MSG_ID= "MSG_ID";
	   public static final String FROMUSER= "FROMUSER";
	   public static final String TOUSER= "TOUSER";
	   public static final String SENDTIME= "SENDTIME";
	   public static final String SENDSTATE= "SENDSTATE";
	   public static final String RECTIME= "RECTIME";
	   public static final String CANCELTIME= "CANCELTIME";

	   public static final String SEQUENCE	= "532";
	   protected static final Map<String , String> columns = new HashMap<String, String>();

		static
		{
			columns.put("LfAppSitInfo", TABLE_NAME);
//			columns.put("tableId", S_ID);
			columns.put("sequence", SEQUENCE);
	       
				columns.put("publishtime", PUBLISHTIME);
	        
				columns.put("name", NAME);
	        
				columns.put("moditytime", MODITYTIME);
	        
				columns.put("issystem", IS_SYSTEM);
	        
				columns.put("typeid", TYPE_ID);
	        
				columns.put("createtime", CREATETIME);
	        
				columns.put("status", STATUS);
	        
				columns.put("corpcode", CORP_CODE);
	        
				columns.put("url", URL);
	        
				columns.put("sid", S_ID);
	        
				columns.put("userid", USERID);
				
				columns.put("validity", validity);
				
				columns.put("serial", SERIAL);
				columns.put("msgId", MSG_ID);
				columns.put("fromUser", FROMUSER);
				columns.put("toUser", TOUSER);
				columns.put("sendTime", SENDTIME);
				columns.put("sendState", SENDSTATE);
				columns.put("recTime", RECTIME);
				columns.put("cancelTime", CANCELTIME);
	        
		};


		public static Map<String , String> getORM()
		{
			return columns;
		}
}

