package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * SP账号信息表实体table类
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 下午01:57:36
 */
public class TableLfMonSspacinfo
{

	public static final String				TABLE_NAME		= "LF_MON_SSPACINFO";

	public static final String			SPACCOUNTTYPE	= "SPACCOUNTTYPE";

	public static final String			MONSTATUS		= "MONSTATUS";

	public static final String			MODIFYTIME		= "MODIFYTIME";

	public static final String			MOREMAINED		= "MOREMAINED";

	public static final String			RPTRECVRATIO	= "RPTRECVRATIO";

	public static final String			MONPHONE		= "MONPHONE";

	public static final String			MONEMAIL		= "MONEMAIL";
	
	public static final String			MTISSUEDSPD		= "MTISSUEDSPD";

	public static final String			MTREMAINED		= "MTREMAINED";

	public static final String			MAXLINKNUM		= "MAXLINKNUM";

	public static final String			MTHAVESND		= "MTHAVESND";

	public static final String			MORECVRATIO		= "MORECVRATIO";

	public static final String			SPACCOUNTID		= "SPACCOUNTID";

	public static final String			USERFEE			= "USERFEE";

	public static final String			CREATETIME		= "CREATETIME";

	public static final String			RPTSNDSPD		= "RPTSNDSPD";

	public static final String			HOSTID			= "HOSTID";

	public static final String			SENDLEVEL		= "SENDLEVEL";

	public static final String			ACCOUNTNAME		= "ACCOUNTNAME";

	public static final String			MONFREQ			= "MONFREQ";

	public static final String			RPTREMAINED		= "RPTREMAINED";

	public static final String			MTSNDSPD		= "MTSNDSPD";
	
	public static final String			LOGINTYPE		= "LOGIN_TYPE";

	public static final String			OFFLINETHRESHD		= "OFFLINETHRESHD";

	public static final String			SEQUENCE		= "S_LF_MON_SSPACINFO";

	protected static final Map<String, String>	columns			= new HashMap<String, String>();

	static
	{
		columns.put("LfMonSspacinfo", TABLE_NAME);
		
		//columns.put("tableId", SPACCOUNTID);
		
		columns.put("sequence", SEQUENCE);

		columns.put("spaccounttype", SPACCOUNTTYPE);

		columns.put("monstatus", MONSTATUS);

		columns.put("modifytime", MODIFYTIME);

		columns.put("moremained", MOREMAINED);

		columns.put("rptrecvratio", RPTRECVRATIO);

		columns.put("monphone", MONPHONE);

		columns.put("monemail", MONEMAIL);
		
		columns.put("mtissuedspd", MTISSUEDSPD);

		columns.put("mtremained", MTREMAINED);

		columns.put("maxlinknum", MAXLINKNUM);

		columns.put("mthavesnd", MTHAVESND);

		columns.put("morecvratio", MORECVRATIO);

		columns.put("spaccountid", SPACCOUNTID);

		columns.put("userfee", USERFEE);

		columns.put("createtime", CREATETIME);

		columns.put("rptsndspd", RPTSNDSPD);

		columns.put("hostid", HOSTID);

		columns.put("sendlevel", SENDLEVEL);

		columns.put("accountname", ACCOUNTNAME);

		columns.put("monfreq", MONFREQ);

		columns.put("rptremained", RPTREMAINED);

		columns.put("mtsndspd", MTSNDSPD);
		
		columns.put("loginType", LOGINTYPE);

		columns.put("offlineThreshd", OFFLINETHRESHD);

	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
