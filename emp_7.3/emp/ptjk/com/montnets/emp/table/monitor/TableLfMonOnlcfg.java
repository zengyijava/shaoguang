/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-31 上午10:20:55
 */
package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @project p_comm
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-31 上午10:20:55
 */

public class TableLfMonOnlcfg
{
	public static final String				TABLE_NAME	= "LF_MON_ONLCFG";

	public static final String			MAX_ONLINE	= "MAX_ONLINE";

	public static final String			ID			= "ID";

	public static final String			MONFREQ		= "MONFREQ";

	public static final String			ONLINENUM	= "ONLINENUM";

	public static final String			MONPHONE	= "MONPHONE";
	
	public static final String 			MONEMAIL	= "MONEMAIL";

	public static final String			MODIFYTIME	= "MODIFYTIME";

	public static final String			EVTTYPE		= "EVTTYPE";

	public static final String			MONSTATUS	= "MONSTATUS";
	
	public static final String          MONTHRESHOLDFLAG = "THRESHOLDFLAG";
	
	public static final String          SENDMAILFLAG  = "SENDMAILFLAG ";

	public static final String			SEQUENCE	= "S_LF_MON_ONLCFG";

	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfMonOnlcfg", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("maxonline", MAX_ONLINE);
		columns.put("id", ID);
		columns.put("monfreq", MONFREQ);
		columns.put("onlinenum", ONLINENUM);
		columns.put("monphone", MONPHONE);
		columns.put("monemail", MONEMAIL);
		columns.put("evttype", EVTTYPE);
		columns.put("modifytime", MODIFYTIME);
		columns.put("monstatus", MONSTATUS);
		columns.put("monThresholdFlag", MONTHRESHOLDFLAG);
		columns.put("sendmailFlag", SENDMAILFLAG);
	};

	public static Map<String, String> getORM()
	{
		return columns;
	}

}
