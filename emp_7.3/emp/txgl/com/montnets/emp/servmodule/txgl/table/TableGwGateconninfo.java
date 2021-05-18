package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableGwGateconninfo
{

	private static final String TABLE_NAME	= "GW_GATECONNINFO";

	private static final String SPEEDLIMIT = "SPEEDLIMIT";

	private static final String UPDATETM = "UPDATETM";

	private static final String PTID = "PTID";

	private static final String PING = "PING";

	private static final String TESTMETHOD = "TESTMETHOD";

	private static final String IP = "IP";

	private static final String ABNORMALTMS = "ABNORMALTMS";

	private static final String RECONNCNT = "RECONNCNT";

	private static final String GATEID = "GATEID";

	private static final String SWITCHMAINIP = "SWITCHMAINIP";

	private static final String MINLINKS = "MINLINKS";

	private static final String TESTTIMES = "TESTTIMES";

	private static final String KEEPCONN = "KEEPCONN";

	private static final String PORT = "PORT";

	private static final String PTACCID = "PTACCID";

	private static final String ABNORMALONG = "ABNORMALONG";

	private static final String LINKCNT = "LINKCNT";

	private static final String ID = "ID";

	private static final String CONNTYPE = "CONNTYPE";

	private static final String LINKLEVEL = "LINKLEVEL";

	private static final String LINKSTATUS = "LINKSTATUS";

	private static final String RELOGINCNT = "RELOGINCNT";

	private static final String CREATETM = "CREATETM";

	private static final String SEQUENCE	= "SEQ_GW_GATECONNINFO";
    protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("GwGateconninfo", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("speedlimit", SPEEDLIMIT);
		columns.put("updatetm", UPDATETM);
		columns.put("ptid", PTID);
		columns.put("ping", PING);
		columns.put("testmethod", TESTMETHOD);
		columns.put("ip", IP);
		columns.put("abnormaltms", ABNORMALTMS);
		columns.put("reconncnt", RECONNCNT);
		columns.put("gateid", GATEID);
		columns.put("switchmainip", SWITCHMAINIP);
		columns.put("minlinks", MINLINKS);
		columns.put("testtimes", TESTTIMES);
		columns.put("keepconn", KEEPCONN);
		columns.put("port", PORT);
		columns.put("ptaccid", PTACCID);
		columns.put("abnormalong", ABNORMALONG);
		columns.put("linkcnt", LINKCNT);
		columns.put("id", ID);
		columns.put("conntype", CONNTYPE);
		columns.put("linklevel", LINKLEVEL);
		columns.put("linkstatus", LINKSTATUS);
		columns.put("relogincnt", RELOGINCNT);
		columns.put("createtm", CREATETM);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
