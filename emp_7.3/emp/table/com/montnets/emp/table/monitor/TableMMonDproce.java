package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;
/**
 * 监控程序动态信息
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-26 下午03:56:57
 */
public class TableMMonDproce
{
	public static final String TABLE_NAME = "M_MON_DPROCE";

	public static final String UPFLOW = "UPFLOW";

	public static final String VMEMUSAGE = "VMEMUSAGE";

	public static final String DOWNFLOW = "DOWNFLOW";

	public static final String MEMUSAGE = "MEMUSAGE";

	public static final String CPUUSAGE = "CPUUSAGE";

	public static final String PROCEID = "PROCEID";

	public static final String UPDATETIME = "UPDATETIME";

	public static final String DISKFREE = "DISKFREE";

	public static final String SEQUENCE = "S_M_MON_DPROCE";
	
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("MMonDproce", TABLE_NAME);
		
		//columns.put("tableId", PROCEID);
		
		columns.put("sequence", SEQUENCE);

		columns.put("upflow", UPFLOW);

		columns.put("vmemusage", VMEMUSAGE);

		columns.put("downflow", DOWNFLOW);

		columns.put("memusage", MEMUSAGE);

		columns.put("cpuusage", CPUUSAGE);

		columns.put("proceid", PROCEID);

		columns.put("updatetime", UPDATETIME);

		columns.put("diskfree", DISKFREE);

	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
