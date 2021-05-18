package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * 通道账号动态信息table
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 下午01:46:34
 */
public class TableLfMonDgtacinfo
{
	public static final String				TABLE_NAME	= "LF_MON_DGTACINFO";

	public static final String		ID			= "ID";
	public static final String		GATEACCOUNT			= "GATEACCOUNT";
	public static final String		EVTTYPE			= "EVTTYPE";
	public static final String		GATEWAYID			= "GATEWAYID";
	public static final String		LINKNUM			= "LINKNUM";
	public static final String		LOGINIP			= "LOGINIP";
	public static final String		ONLINESTATUS			= "ONLINESTATUS";
	public static final String		USERFEE			= "USERFEE";
	public static final String		FEEFLAG			= "FEEFLAG";
	public static final String		MTTOTALSND			= "MTTOTALSND";
	public static final String		MTHAVESND			= "MTHAVESND";
	public static final String		MTREMAINED			= "MTREMAINED";
	public static final String		MTRECVSPD			= "MTRECVSPD";
	public static final String		MOTOTALRECV			= "MOTOTALRECV";
	public static final String		MOHAVESND			= "MOHAVESND";
	public static final String		MOREMAINED			= "MOREMAINED";
	public static final String		MOSNDSPD			= "MOSNDSPD";
	public static final String		RPTHAVESND			= "RPTHAVESND";
	public static final String		RPTREMAINED			= "RPTREMAINED";
	public static final String		RPTSNDSPD			= "RPTSNDSPD";
	public static final String		RPTTOTALRECV			= "RPTTOTALRECV";
	public static final String		LOGININTM			= "LOGININTM";
	public static final String		LOGINOUTTM			= "LOGINOUTTM";
	public static final String		MODIFYTIME			= "MODIFYTIME";
	public static final String		HOSTID			= "HOSTID";
	public static final String		GATENAME			= "GATENAME";
	public static final String		THRESHOLDFLAG1			= "THRESHOLDFLAG1";
	public static final String		THRESHOLDFLAG2			= "THRESHOLDFLAG2";
	public static final String		THRESHOLDFLAG3			= "THRESHOLDFLAG3";
	public static final String		THRESHOLDFLAG4			= "THRESHOLDFLAG4";
	public static final String		THRESHOLDFLAG5			= "THRESHOLDFLAG5";
	public static final String		THRESHOLDFLAG6			= "THRESHOLDFLAG6";
	public static final String		THRESHOLDFLAG7			= "THRESHOLDFLAG7";
	public static final String		THRESHOLDFLAG8			= "THRESHOLDFLAG8";
	public static final String		THRESHOLDFLAG9			= "THRESHOLDFLAG9";
	public static final String		THRESHOLDFLAG10			= "THRESHOLDFLAG10";
	public static final String		THRESHOLDFLAG11			= "THRESHOLDFLAG11";
	public static final String		THRESHOLDFLAG12			= "THRESHOLDFLAG12";
	public static final String		THRESHOLDFLAG13			= "THRESHOLDFLAG13";
	
	public static final String			SENDMAILFLAG1 			= "SENDMAILFLAG1";
	public static final String			SENDMAILFLAG2 			= "SENDMAILFLAG2";
	public static final String			SENDMAILFLAG3 			= "SENDMAILFLAG3";
	public static final String			SENDMAILFLAG4 			= "SENDMAILFLAG4";
	public static final String			SENDMAILFLAG5 			= "SENDMAILFLAG5";
	public static final String			SENDMAILFLAG6 			= "SENDMAILFLAG6";
	public static final String			SENDMAILFLAG7 			= "SENDMAILFLAG7";
	public static final String			SENDMAILFLAG8 			= "SENDMAILFLAG8";
	public static final String			SENDMAILFLAG9 			= "SENDMAILFLAG9";
	public static final String			SENDMAILFLAG10 			= "SENDMAILFLAG10";
	public static final String			SENDMAILFLAG11 			= "SENDMAILFLAG11";
	public static final String			SENDMAILFLAG12 			= "SENDMAILFLAG12";
	public static final String			SENDMAILFLAG13 			= "SENDMAILFLAG13";
	public static final String 				SERVERNUM               = "SERVERNUM";
	public static final String				GWNO					= "GWNO";
    public static final String 			DBSERVTIME 				= "DBSERVTIME";

	public static final String			SEQUENCE	= "S_LF_MON_DGTACINFO";

	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfMonDgtacinfo", TABLE_NAME);
		
		columns.put("tableId", ID);
		
		columns.put("sequence", SEQUENCE);
		
		columns.put("id",ID );
		columns.put("gateaccount",GATEACCOUNT );
		columns.put("evtType", EVTTYPE);
		columns.put("gatewayid", GATEWAYID);
		columns.put("linknum", LINKNUM);
		columns.put("loginip", LOGINIP);
		columns.put("onlinestatus", ONLINESTATUS);
		columns.put("userfee", USERFEE);
		columns.put("feeflag", FEEFLAG);
		columns.put("mttotalsnd", MTTOTALSND);
		columns.put("mthavesnd", MTHAVESND);
		columns.put("mtremained", MTREMAINED);
		columns.put("mtrecvspd", MTRECVSPD);
		columns.put("mototalrecv", MOTOTALRECV);
		columns.put("mohavesnd",MOHAVESND );
		columns.put("moremained", MOREMAINED);
		columns.put("mosndspd", MOSNDSPD);
		columns.put("rpthavesnd",RPTHAVESND );
		columns.put("rptremained", RPTREMAINED);
		columns.put("rptsndspd", RPTSNDSPD);
		columns.put("rpttotalrecv", RPTTOTALRECV);
		columns.put("loginintm",LOGININTM );
		columns.put("loginouttm", LOGINOUTTM);
		columns.put("modifytime", MODIFYTIME);
		columns.put("hostId",HOSTID);
		columns.put("gateName", GATENAME);
		columns.put("thresholdflag1", THRESHOLDFLAG1);
		columns.put("thresholdflag2", THRESHOLDFLAG2);
		columns.put("thresholdflag3", THRESHOLDFLAG3);
		columns.put("thresholdflag4", THRESHOLDFLAG4);
		columns.put("thresholdflag5", THRESHOLDFLAG5);
		columns.put("thresholdflag6", THRESHOLDFLAG6);
		columns.put("thresholdflag7", THRESHOLDFLAG7);
		columns.put("thresholdflag8", THRESHOLDFLAG8);
		columns.put("thresholdflag9", THRESHOLDFLAG9);
		columns.put("thresholdflag10", THRESHOLDFLAG10);
		columns.put("thresholdflag11", THRESHOLDFLAG11);
		columns.put("thresholdflag12", THRESHOLDFLAG12);
		columns.put("thresholdflag13", THRESHOLDFLAG13);
		columns.put("sendmailflag1", SENDMAILFLAG1);
		columns.put("sendmailflag2", SENDMAILFLAG2);
		columns.put("sendmailflag3", SENDMAILFLAG3);
		columns.put("sendmailflag4", SENDMAILFLAG4);
		columns.put("sendmailflag5", SENDMAILFLAG5);
		columns.put("sendmailflag6", SENDMAILFLAG6);
		columns.put("sendmailflag7", SENDMAILFLAG7);
		columns.put("sendmailflag8", SENDMAILFLAG8);
		columns.put("sendmailflag9", SENDMAILFLAG9);
		columns.put("sendmailflag10", SENDMAILFLAG10);
		columns.put("sendmailflag11", SENDMAILFLAG11);
		columns.put("sendmailflag12", SENDMAILFLAG12);
		columns.put("sendmailflag13", SENDMAILFLAG13);
        columns.put("serverNum", SERVERNUM);
        columns.put("gwno", GWNO);
        columns.put("dbservtime", DBSERVTIME);
	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
