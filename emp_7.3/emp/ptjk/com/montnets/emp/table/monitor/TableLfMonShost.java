package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * 主机信息表table类
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 上午09:39:41
 */
public class TableLfMonShost
{

	public static final String				TABLE_NAME		= "LF_MON_SHOST";

	public static final String			VMEMUSAGE		= "VMEMUSAGE";

	public static final String			DISKFREESPACE	= "DISKFREESPACE";

	public static final String			HOSTHD			= "HOSTHD";

	public static final String			HOSTMEM			= "HOSTMEM";

	public static final String			HOSTID			= "HOSTID";

	public static final String			CPUUSAGE		= "CPUUSAGE";

	public static final String			HOSTTYPE		= "HOSTTYPE";

	public static final String			OUPIP			= "OUPIP";

	public static final String			MONSTATUS		= "MONSTATUS";

	public static final String			MODIFYTIME		= "MODIFYTIME";

	public static final String			DESCR			= "DESCR";

	public static final String			CPUSJ			= "CPUSJ";

	public static final String			HOSTCPU			= "HOSTCPU";

	public static final String			MONPHONE		= "MONPHONE";
	
	public static final String 			MONEMAIL		= "MONEMAIL";

	public static final String			HOSTNAME		= "HOSTNAME";

	public static final String			MONFREQ			= "MONFREQ";

	public static final String			MEMUSAGE		= "MEMUSAGE";

	public static final String			CREATETIME		= "CREATETIME";

	public static final String			ADAPTER2		= "ADAPTER2";

	public static final String			PROCESSCNT		= "PROCESSCNT";

	public static final String			ADAPTER1		= "ADAPTER1";

	public static final String			CPUBL			= "CPUBL";

	public static final String			OPERSYS			= "OPERSYS";

	public static final String			HOSTUSESTATUS	= "HOSTUSESTATUS";

	public static final String			SEQUENCE		= "S_LF_MON_SHOST";

	protected static final Map<String, String>	columns			= new HashMap<String, String>();

	static
	{
		columns.put("LfMonShost", TABLE_NAME);

		columns.put("tableId", HOSTID);

		columns.put("sequence", SEQUENCE);

		columns.put("vmemusage", VMEMUSAGE);

		columns.put("diskfreespace", DISKFREESPACE);

		columns.put("hosthd", HOSTHD);

		columns.put("hostmem", HOSTMEM);

		columns.put("hostid", HOSTID);

		columns.put("cpuusage", CPUUSAGE);

		columns.put("hosttype", HOSTTYPE);

		columns.put("oupip", OUPIP);

		columns.put("monstatus", MONSTATUS);

		columns.put("modifytime", MODIFYTIME);

		columns.put("descr", DESCR);

		columns.put("cpusj", CPUSJ);

		columns.put("hostcpu", HOSTCPU);

		columns.put("monphone", MONPHONE);
		
		columns.put("monemail", MONEMAIL);

		columns.put("hostname", HOSTNAME);

		columns.put("monfreq", MONFREQ);

		columns.put("memusage", MEMUSAGE);

		columns.put("createtime", CREATETIME);

		columns.put("adapter2", ADAPTER2);

		columns.put("processcnt", PROCESSCNT);

		columns.put("adapter1", ADAPTER1);

		columns.put("cpubl", CPUBL);

		columns.put("opersys", OPERSYS);

		columns.put("hostusestatus", HOSTUSESTATUS);

	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
