					
package com.montnets.emp.table.appmage;

import java.util.HashMap;
import java.util.Map;

public class TableLfAppMtmsg
{
	public static final String TABLE_NAME	= "LF_APP_MTMSG";

   public static final String MSG_ID= "MSG_ID";

   public static final String MSG_TYPE= "MSG_TYPE";

   public static final String CREATETIME= "CREATETIME";

   public static final String USERID= "USERID";

   public static final String CORP_CODE= "CORP_CODE";

   public static final String TITLE= "TITLE";

   public static final String MSG_TEXT= "MSG_TEXT";

   public static final String CONTENT= "CONTENT";

   public static final String TASKID= "TASKID";

   public static final String TOUSERNAME= "TOUSERNAME";

   public static final String MSG_XML= "MSG_XML";

   public static final String ID= "ID";

   public static final String APPACOUNT= "APPACOUNT";

   public static final String PARENT_ID= "PARENT_ID";

   public static final String SENDSTATE= "SENDSTATE";
   
   public static final String APP_MSG_ID= "APP_MSG_ID";
   
   public static final String APPUSERACCOUNT= "APPUSERACCOUNT";
   
   public static final String RPT_STATE= "RPT_STATE";
   
   public static final String SendMsgType= "SendMsgType";
   
   public static final String RecRPTTime= "RecRPTTime";

   public static final String SEQUENCE	= "S_LF_APP_MTMSG";
   protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfAppMtmsg", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
       
			columns.put("msgid", MSG_ID);
        
			columns.put("msgtype", MSG_TYPE);
        
			columns.put("createtime", CREATETIME);
        
			columns.put("userid", USERID);
        
			columns.put("corpcode", CORP_CODE);
        
			columns.put("title", TITLE);
        
			columns.put("msgtext", MSG_TEXT);
        
			columns.put("content", CONTENT);
        
			columns.put("taskid", TASKID);
        
			columns.put("tousername", TOUSERNAME);
        
			columns.put("msgxml", MSG_XML);
        
			columns.put("id", ID);
        
			columns.put("appacount", APPACOUNT);
        
			columns.put("parentid", PARENT_ID);
        
			columns.put("sendstate", SENDSTATE);
			
			columns.put("appMsgId", APP_MSG_ID);
			
			columns.put("appUserAccount", APPUSERACCOUNT);
			
			columns.put("rptState", RPT_STATE);
			
			columns.put("sendMsgType", SendMsgType);
			
			columns.put("recRPTTime", RecRPTTime);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					