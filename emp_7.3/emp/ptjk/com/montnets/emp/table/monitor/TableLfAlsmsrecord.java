/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-4-3 下午05:55:00
 */
package com.montnets.emp.table.monitor;

/**
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-4-3 下午05:55:00
 */

import java.util.HashMap;
import java.util.Map;

public class TableLfAlsmsrecord
{
	public static final String TABLE_NAME	= "LF_ALSMSRECORD";

	public static final String PHONE= "PHONE";

	public static final String MESSAGE= "MESSAGE";

	public static final String ERRORCODE= "ERRORCODE";

	public static final String SPGATE= "SPGATE";

	public static final String RECVTIME= "RECVTIME";

	public static final String SENDTIME= "SENDTIME";

	public static final String ID= "ID";

	public static final String SENDFLAG= "SENDFLAG";

	public static final String SENDSTATUS= "SENDSTATUS";

	public static final String MSGID= "MSGID";

	public static final String UNICOM= "UNICOM";

	public static final String USERID= "USERID";

	public static final String SEQUENCE	= "S_LF_ALSMSRECORD";
	protected static final Map<String , String> columns = new HashMap<String, String>();
   
	static
	{
		columns.put("LfAlsmsrecord", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
       
			columns.put("phone", PHONE);
        
			columns.put("message", MESSAGE);
        
			columns.put("errorcode", ERRORCODE);
        
			columns.put("spgate", SPGATE);
        
			columns.put("recvtime", RECVTIME);
        
			columns.put("sendtime", SENDTIME);
        
			columns.put("id", ID);
        
			columns.put("sendflag", SENDFLAG);
        
			columns.put("sendstatus", SENDSTATUS);
        
			columns.put("msgid", MSGID);
        
			columns.put("unicom", UNICOM);
        
			columns.put("userid", USERID);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					
