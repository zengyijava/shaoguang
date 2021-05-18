package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

public class TableMMonShost
{
	public static final String TABLE_NAME = "M_MON_SHOST";

	public static final String CABINETID = "CABINETID";

	public static final String HOSTCODE = "HOSTCODE";

	public static final String PASS = "PWD";

	public static final String HOSTHD = "HOSTHD";

	public static final String HOSTMEM = "HOSTMEM";

	public static final String HOSTID = "HOSTID";

	public static final String HOSTTYPE = "HOSTTYPE";

	public static final String MONFREQ = "MONFREQ";

	public static final String OUPIP = "OUPIP";

	public static final String HOSTSIZE = "HOSTSIZE";

	public static final String SVRCODE = "SVRCODE";

	public static final String HOSTPOS = "HOSTPOS";

	public static final String MONNUM = "MONNUM";

	public static final String MONSTATUS = "MONSTATUS";

	public static final String MODIFYTIME = "MODIFYTIME";

	public static final String DESCR = "DESCR";

	public static final String HOSTNAME = "HOSTNAME";

	public static final String HOSTCPU = "HOSTCPU";

	public static final String BAKHOSTID = "BAKHOSTID";

	public static final String SRVBH = "SRVBH";

	public static final String CREATETIME = "CREATETIME";

	public static final String ADAPTER2 = "ADAPTER2";

	public static final String ADAPTER1 = "ADAPTER1";

	public static final String OPERSYS = "OPERSYS";

	public static final String ADAPTER4 = "ADAPTER4";

	public static final String ADAPTER3 = "ADAPTER3";

	public static final String SEQUENCE = "S_M_MON_SHOST";
	
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static 
	{
		columns.put("MMonShost", TABLE_NAME);
		columns.put("tableId", HOSTID);
		columns.put("sequence", SEQUENCE);

		columns.put("cabinetid", CABINETID);

		columns.put("hostcode", HOSTCODE);

		columns.put("pwd", PASS);

		columns.put("hosthd", HOSTHD);

		columns.put("hostmem", HOSTMEM);

		columns.put("hostid", HOSTID);

		columns.put("hosttype", HOSTTYPE);

		columns.put("monfreq", MONFREQ);

		columns.put("oupip", OUPIP);

		columns.put("hostsize", HOSTSIZE);

		columns.put("svrcode", SVRCODE);

		columns.put("hostpos", HOSTPOS);

		columns.put("monnum", MONNUM);

		columns.put("monstatus", MONSTATUS);

		columns.put("modifytime", MODIFYTIME);

		columns.put("descr", DESCR);

		columns.put("hostname", HOSTNAME);

		columns.put("hostcpu", HOSTCPU);

		columns.put("bakhostid", BAKHOSTID);

		columns.put("srvbh", SRVBH);

		columns.put("createtime", CREATETIME);

		columns.put("adapter2", ADAPTER2);

		columns.put("adapter1", ADAPTER1);

		columns.put("opersys", OPERSYS);

		columns.put("adapter4", ADAPTER4);

		columns.put("adapter3", ADAPTER3);

	};

	public static Map<String, String> getORM() 
	{
		return columns;
	}
}
