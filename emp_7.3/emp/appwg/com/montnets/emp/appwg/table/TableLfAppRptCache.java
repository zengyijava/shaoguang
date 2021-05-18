							
package com.montnets.emp.appwg.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfAppRptCache
{

	public static final String TABLE_NAME	= "LF_APP_RPTCACHE";

	public static final String PACKETID = "PACKETID";

	public static final String READ_STATE = "READ_STATE";

	public static final String MSG_TYPE = "MSG_TYPE";

	public static final String ECODE = "ECODE";

	public static final String APPID = "APPID";

	public static final String CREATETIME = "CREATETIME";

	public static final String MSGID = "MSGID";

	public static final String EMPTYPE = "EMTYPE";

	public static final String SEND_STATE = "SEND_STATE";

	public static final String VALIDITY = "VALIDITY";

	public static final String BODY = "BODY";

	public static final String SENDED_COUNT = "SENDED_COUNT";

	public static final String ID = "ID";

	public static final String SERIAL = "SERIAL";
	
	public static final String SEND_RESULT = "SEND_RESULT";
	public static final String icode = "icode";
	public static final String ERRCODE = "ERRCODE";
	public static final String USERNAME = "USERNAME";


	public static final String SEQUENCE	= "S_LF_APP_RPTCACHE";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfAppRptCache", TABLE_NAME);
		//columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("packetid", PACKETID);
		columns.put("readstate", READ_STATE);
		columns.put("msgtype", MSG_TYPE);
		columns.put("ecode", ECODE);
		columns.put("appid", APPID);
		columns.put("createtime", CREATETIME);
		columns.put("msgid", MSGID);
		columns.put("emptype", EMPTYPE);
		columns.put("sendstate", SEND_STATE);
		columns.put("validity", VALIDITY);
		columns.put("body", BODY);
		columns.put("sendedcount", SENDED_COUNT);
		columns.put("id", ID);
		columns.put("serial", SERIAL);
		
		columns.put("sendResult", SEND_RESULT);
		columns.put("icode", icode);
		columns.put("errCode", ERRCODE);
		columns.put("userName", USERNAME);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

						