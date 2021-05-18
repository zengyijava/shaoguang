package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;

import java.util.Map;

public class TableGwUserproperty
{

	public static final String TABLE_NAME	= "GW_USERPROPERTY";

	public static final String PUSHRPTFMT = "PUSHRPTFMT";

	public static final String PUSHMSGCODE = "PUSHMSGCODE";

	public static final String PUSHRPTMAXCNT = "PUSHRPTMAXCNT";

	public static final String PUSHSLIDEWND = "PUSHSLIDEWND";

	public static final String PUSHMOMAXCNT = "PUSHMOMAXCNT";

	public static final String GETMOMAXCNT = "GETMOMAXCNT";

	public static final String PUSHMSGENCODE = "PUSHMSGENCODE";

	public static final String PUSHMOFMT = "PUSHMOFMT";

	public static final String RESERVE = "RESERVE";

	public static final String PASSENCODE = "PWDENCODE";

	public static final String MSGENCODE = "MSGENCODE";

	public static final String ID = "ID";

	public static final String PUSHPASSENCODE = "PUSHPWDENCODE";

	public static final String PUSHFAILCNT = "PUSHFAILCNT";

	public static final String MSGCODE = "MSGCODE";

	public static final String GETRPTMAXCNT = "GETRPTMAXCNT";

	public static final String PASSENCODESTR = "PWDENCODESTR";

	public static final String ECID = "ECID";

	public static final String PUSHPASSENCODESTR = "PUSHPWDENCODESTR";

	public static final String USERID = "USERID";

	public static final String SEQUENCE	= "SEQ_GW_USERPROPERTY";
	protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwUserproperty", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("pushrptfmt", PUSHRPTFMT);
		columns.put("pushmsgcode", PUSHMSGCODE);
		columns.put("pushrptmaxcnt", PUSHRPTMAXCNT);
		columns.put("pushslidewnd", PUSHSLIDEWND);
		columns.put("pushmomaxcnt", PUSHMOMAXCNT);
		columns.put("getmomaxcnt", GETMOMAXCNT);
		columns.put("pushmsgencode", PUSHMSGENCODE);
		columns.put("pushmofmt", PUSHMOFMT);
		columns.put("reserve", RESERVE);
		columns.put("pwdencode", PASSENCODE);
		columns.put("msgencode", MSGENCODE);
		columns.put("id", ID);
		columns.put("pushpwdencode", PUSHPASSENCODE);
		columns.put("pushfailcnt", PUSHFAILCNT);
		columns.put("msgcode", MSGCODE);
		columns.put("getrptmaxcnt", GETRPTMAXCNT);
		columns.put("pwdencodestr", PASSENCODESTR);
		columns.put("ecid", ECID);
		columns.put("pushpwdencodestr", PUSHPASSENCODESTR);
		columns.put("userid", USERID);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}