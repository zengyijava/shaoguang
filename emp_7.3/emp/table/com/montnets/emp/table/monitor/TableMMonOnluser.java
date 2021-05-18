package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-2 下午03:37:21
 */
public class TableMMonOnluser
{
	public static final String				TABLE_NAME	= "M_MON_ONLUSER";

	public static final String			LOGINADDR	= "LOGINADDR";

	public static final String			DEP_ID		= "DEP_ID";

	public static final String			NAME		= "NAME";

	public static final String			USER_ID		= "USER_ID";

	public static final String			USER_NAME	= "USER_NAME";

	public static final String			SESSEION_ID	= "SESSEION_ID";

	public static final String			LOGINTIME	= "LOGINTIME";

	public static final String			CORP_CODE	= "CORP_CODE";
	
	public static final String			SERVERNUM	= "SERVERNUM";

	public static final String			SEQUENCE	= "S_M_MON_ONLUSER";

	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("MMonOnluser", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
		columns.put("loginaddr", LOGINADDR);
		columns.put("depid", DEP_ID);
		columns.put("name", NAME);
		columns.put("userid", USER_ID);
		columns.put("username", USER_NAME);
		columns.put("sesseionid", SESSEION_ID);
		columns.put("logintime", LOGINTIME);
		columns.put("corpcode", CORP_CODE);
		columns.put("serverNum", SERVERNUM);
	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
