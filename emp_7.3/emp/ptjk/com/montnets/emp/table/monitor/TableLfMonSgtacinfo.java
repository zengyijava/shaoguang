package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * 通道账号信息table
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 下午01:48:05
 */
public class TableLfMonSgtacinfo
{

	public static final String				TABLE_NAME	= "LF_MON_SGTACINFO";

	public static final String			MTRECVSPD	= "MTRECVSPD";

	public static final String			MONSTATUS	= "MONSTATUS";

	public static final String			MODIFYTIME	= "MODIFYTIME";

	public static final String			RPTSNDRATIO	= "RPTSNDRATIO";

	public static final String			MOREMAINED	= "MOREMAINED";

	public static final String			MONPHONE	= "MONPHONE";

	public static final String			MONEMAIL	= "MONEMAIL";
	
	public static final String			MOSNDSPD	= "MOSNDSPD";

	public static final String			MTREMAINED	= "MTREMAINED";

	public static final String			USERFEE		= "USERFEE";

	public static final String			GATEACCOUNT	= "GATEACCOUNT";

	public static final String			CREATETIME	= "CREATETIME";

	public static final String			RPTSNDSPD	= "RPTSNDSPD";

	public static final String			HOSTID		= "HOSTID";

	public static final String			MOSNDRATIO	= "MOSNDRATIO";

	public static final String			LINKNUM		= "LINKNUM";

	public static final String			MONFREQ		= "MONFREQ";

	public static final String			GATENAME	= "GATENAME";

	public static final String			RPTREMAINED	= "RPTREMAINED";

	public static final String			ISARREARAGE	= "ISARREARAGE";

	public static final String			GATEWAYID	= "GATEWAYID";

	public static final String			SEQUENCE	= "S_LF_MON_SGTACINFO";

	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfMonSgtacinfo", TABLE_NAME);
		
		//columns.put("tableId", GATEACCOUNT);
		
		columns.put("sequence", SEQUENCE);

		columns.put("mtrecvspd", MTRECVSPD);

		columns.put("monstatus", MONSTATUS);

		columns.put("modifytime", MODIFYTIME);

		columns.put("rptsndratio", RPTSNDRATIO);

		columns.put("moremained", MOREMAINED);

		columns.put("monphone", MONPHONE);

		columns.put("monemail", MONEMAIL);
		
		columns.put("mosndspd", MOSNDSPD);

		columns.put("mtremained", MTREMAINED);

		columns.put("userfee", USERFEE);

		columns.put("gateaccount", GATEACCOUNT);

		columns.put("createtime", CREATETIME);

		columns.put("rptsndspd", RPTSNDSPD);

		columns.put("hostid", HOSTID);

		columns.put("mosndratio", MOSNDRATIO);

		columns.put("linknum", LINKNUM);

		columns.put("monfreq", MONFREQ);

		columns.put("gatename", GATENAME);

		columns.put("rptremained", RPTREMAINED);

		columns.put("isarrearage", ISARREARAGE);

		columns.put("gatewayid", GATEWAYID);

	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
