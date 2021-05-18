package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误告警表table
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 下午02:15:43
 */
public class TableLfMonErr
{

	public static final String				TABLE_NAME		= "LF_MON_ERR";

	public static final String			PROCEID			= "PROCEID";

	public static final String			CRTTIME		= "CRTTIME";

	public static final String			MONSTATUS		= "MONSTATUS";

	public static final String			HOSTID			= "HOSTID";

	public static final String			RCVTIME			= "RCVTIME";

	public static final String			EVTID			= "EVTID";

	public static final String			EVTTYPE			= "EVTTYPE";

	public static final String			DEALFLAG		= "DEALFLAG";

	public static final String			SPACCOUNTID		= "SPACCOUNTID";

	public static final String			WHO				= "WHO";

	public static final String			GATEACCOUNT		= "GATEACCOUNT";

	public static final String			EVTTIME			= "EVTTIME";

	public static final String			ID				= "ID";

	public static final String			MSG				= "MSG";

	public static final String			APPTYPE			= "APPTYPE";

	public static final String			DEALPEOPLE		= "DEALPEOPLE";

	public static final String			DEALDESC		= "DEALDESC";

	public static final String			MONTHRESHOLD	= "MONTHRESHOLD";
	
	public static final String 			MONTIMER        = "MONTIMER";
	
	public static final String 			SPOFFLINEPRD        = "SPOFFLINEPRD";
	
	public static final String 			PROCENODE        = "PROCENODE";
	
	public static final String 			WEBNODE        = "WEBNODE";

	public static final String			SEQUENCE		= "S_LF_MON_ERR";

	protected static final Map<String, String>	columns			= new HashMap<String, String>();

	static
	{
		columns.put("LfMonErr", TABLE_NAME);

		columns.put("tableId", ID);

		columns.put("sequence", SEQUENCE);
		
		columns.put("crttime", CRTTIME);

		columns.put("proceid", PROCEID);

		columns.put("monstatus", MONSTATUS);

		columns.put("hostid", HOSTID);

		columns.put("rcvtime", RCVTIME);

		columns.put("evtid", EVTID);

		columns.put("evttype", EVTTYPE);

		columns.put("dealflag", DEALFLAG);

		columns.put("spaccountid", SPACCOUNTID);

		columns.put("who", WHO);

		columns.put("gateaccount", GATEACCOUNT);

		columns.put("evttime", EVTTIME);

		columns.put("id", ID);

		columns.put("msg", MSG);

		columns.put("apptype", APPTYPE);

		columns.put("dealpeople", DEALPEOPLE);

		columns.put("dealdesc", DEALDESC);
		
		columns.put("monthreshold", MONTHRESHOLD);
		
		columns.put("montimer", MONTIMER);

		columns.put("spOfflinePrd", SPOFFLINEPRD);
		
		columns.put("procenode", PROCENODE);

		columns.put("webnode", WEBNODE);
	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
