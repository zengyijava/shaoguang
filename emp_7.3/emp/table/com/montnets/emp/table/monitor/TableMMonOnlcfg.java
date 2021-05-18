package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-2 下午04:02:54
 */

public class TableMMonOnlcfg
{
		public static final String TABLE_NAME	= "M_MON_ONLCFG";

	   public static final String MAX_ONLINE= "MAX_ONLINE";

	   public static final String ID= "ID";

	   public static final String MONFREQ= "MONFREQ";

	   public static final String ONLINENUM= "ONLINENUM";

	   public static final String SEQUENCE	= "S_M_MON_ONLCFG";
	   protected final static Map<String , String> columns = new HashMap<String, String>();

		static
		{
			columns.put("MMonOnlcfg", TABLE_NAME);
			columns.put("tableId", ID);
			columns.put("sequence", SEQUENCE);
			columns.put("maxonline", MAX_ONLINE);
			columns.put("id", ID);
			columns.put("monfreq", MONFREQ);
			columns.put("onlinenum", ONLINENUM);
		};


		public static Map<String , String> getORM()
		{
			return columns;
		}
	}
