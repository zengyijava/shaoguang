/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-14 下午03:19:57
 */
package com.montnets.emp.table.apptask;

import java.util.HashMap;
import java.util.Map;

/**
 * @description AAP信息发送
 * @project emp_std_183
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-14 下午03:19:57
 */

public class TableLfAppMttask
{
	   public static final String TABLE_NAME	= "LF_APP_MTTASK";

	   public static final String USER_ID= "USER_ID";

	   public static final String MSG_TYPE= "MSG_TYPE";

	   public static final String CORP_CODE= "CORP_CODE";

	   public static final String FAI_COUNT= "FAI_COUNT";

	   public static final String SUB_COUNT= "SUB_COUNT";

	   public static final String MSG= "MSG";

	   public static final String BIGINTIME= "BIGINTIME";

	   public static final String SENDSTATE= "SENDSTATE";

	   public static final String MSG_URL= "MSG_URL";
	   
	   public static final String MSG_LOCALURL= "MSG_LOCALURL";

	   public static final String READ_COUNT= "READ_COUNT";

	   public static final String ID= "ID";

	   public static final String TASKID= "TASKID";

	   public static final String ENDTIME= "ENDTIME";

	   public static final String SUC_COUNT= "SUC_COUNT";

	   public static final String UNREAD_COUNT= "UNREAD_COUNT";

	   public static final String TITLE= "TITLE";

	   public static final String APPACOUNT= "APPACOUNT";

	   public static final String SEQUENCE	= "S_LF_APP_MTTASK";
	   protected static final Map<String , String> columns = new HashMap<String, String>();

		static
		{
			columns.put("LfAppMttask", TABLE_NAME);
			columns.put("tableId", ID);
			columns.put("sequence", SEQUENCE);
			columns.put("userid", USER_ID);
			columns.put("msgtype", MSG_TYPE);
			columns.put("corpcode", CORP_CODE);
			columns.put("faicount", FAI_COUNT);
			columns.put("subcount", SUB_COUNT);
			columns.put("msg", MSG);
			columns.put("bigintime", BIGINTIME);
			columns.put("sendstate", SENDSTATE);
			columns.put("msgurl", MSG_URL);
			columns.put("msglocalurl", MSG_LOCALURL);
			columns.put("readcount", READ_COUNT);
			columns.put("id", ID);
			columns.put("taskid", TASKID);
			columns.put("endtime", ENDTIME);
			columns.put("succount", SUC_COUNT);
			columns.put("unreadcount", UNREAD_COUNT);
			columns.put("title", TITLE);
			columns.put("appacount", APPACOUNT);
		};


		public static Map<String , String> getORM()
		{
			return columns;
		}
}
