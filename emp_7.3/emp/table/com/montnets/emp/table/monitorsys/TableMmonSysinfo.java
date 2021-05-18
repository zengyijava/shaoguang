package com.montnets.emp.table.monitorsys;

import java.util.HashMap;
import java.util.Map;

public class TableMmonSysinfo {

	public static final String TABLE_NAME = "M_MON_SYSINFO";

	public static final String PTCODE = "PTCODE";
	 
	public static final String CPUUSAGE = "CPUUSAGE";

	public static final String MEMUSAGE = "MEMUSAGE";

	public static final String VMEMUSAGE = "VMEMUSAGE";

	public static final String DISKFREESPACE = "DISKFREESPACE";

	public static final String UPDATETIME = "UPDATETIME";

    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("MmonSysinfo", TABLE_NAME);
		/*columns.put("sequence", SEQUENCE);*/
		columns.put("ptcode", PTCODE);
		columns.put("cpuUsage", CPUUSAGE);
		columns.put("memUsage", MEMUSAGE);
		columns.put("vmemUsage", VMEMUSAGE);
		columns.put("diskFreeSpace", DISKFREESPACE);
		columns.put("updateTime", UPDATETIME);	 
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM()
	{
		return columns;
	}
}
