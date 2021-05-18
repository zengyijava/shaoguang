							
package com.montnets.emp.appwg.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfAppMsgcache
{

	public static final String TABLE_NAME	= "LF_APP_MSGCACHE";

	public static final String PACKETID = "PACKETID";

	public static final String TOJID = "TOJID";

	public static final String TOTYPE = "TOTYPE";

	public static final String READ_STATE = "READ_STATE";

	public static final String FNAME = "FNAME";

	public static final String MSG_TYPE = "MSG_TYPE";

	public static final String ECODE = "ECODE";

	public static final String APPID = "APPID";

	public static final String CREATETIME = "CREATETIME";

	public static final String MSGID = "MSGID";

	public static final String EMPTYPE = "EMPTYPE";

	public static final String SEND_STATE = "SEND_STATE";

	public static final String FROMJID = "FROMJID";

	public static final String VALIDITY = "VALIDITY";

	public static final String BODY = "BODY";

	public static final String TNAME = "TNAME";

	public static final String SENDED_COUNT = "SENDED_COUNT";

	public static final String ID = "ID";

	public static final String SERIAL = "SERIAL";

	public static final String MSG_SRC = "MSG_SRC";
	
	public static final String HAS_CONTENT = "HAS_CONTENT";
	
	public static final String SEQUENCE	= "S_LF_APP_MSGCACHE";
	protected  static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfAppMsgcache", TABLE_NAME);
		//columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("packetid", PACKETID);
		columns.put("tojid", TOJID);
		columns.put("totype", TOTYPE);
		columns.put("readstate", READ_STATE);
		columns.put("fname", FNAME);
		columns.put("msgtype", MSG_TYPE);
		columns.put("ecode", ECODE);
		columns.put("appid", APPID);
		columns.put("createtime", CREATETIME);
		columns.put("msgid", MSGID);
		columns.put("emptype", EMPTYPE);
		columns.put("sendstate", SEND_STATE);
		columns.put("fromjid", FROMJID);
		columns.put("validity", VALIDITY);
		columns.put("body", BODY);
		columns.put("tname", TNAME);
		columns.put("sendedcount", SENDED_COUNT);
		columns.put("id", ID);
		columns.put("serial", SERIAL);
		columns.put("msgsrc", MSG_SRC);
		
		columns.put("hasContent", HAS_CONTENT);
		
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

						