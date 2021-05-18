package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-3-23 下午02:56:29
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class TableLfSpoffctrl
{

	public static final String				TABLE_NAME		= "LF_SPOFFCTRL";

	public static final String			ID			= "ID";
	public static final String			SPACCOUNTID			= "SPACCOUNTID";
	public static final String			MONOFFLINEPRD			= "MONOFFLINEPRD";
	public static final String			ALARMTIME			= "ALARMTIME";
	public static final String			ALARMFLAG			= "ALARMFLAG";
	public static final String			ALARMEMAILFLAG			= "ALARMEMAILFLAG";
	public static final String			CREATETIME			= "CREATETIME";
	public static final String			UPDATETIME			= "UPDATETIME";
	public static final String			CORP_CODE			= "CORP_CODE";

	public static final String			SEQUENCE		= "LF_SPOFFCTRL_S";

	protected static final Map<String, String>	columns			= new HashMap<String, String>();

	static
	{
		columns.put("LfSpoffctrl", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("spaccountid", SPACCOUNTID);
		columns.put("monofflineprd", MONOFFLINEPRD);
		columns.put("alarmtime", ALARMTIME);
		columns.put("alarmflag",ALARMFLAG);
		columns.put("alarmemailflag",ALARMEMAILFLAG);
		columns.put("createtime", CREATETIME);
		columns.put("updatetime",UPDATETIME );
		columns.put("corpcode", CORP_CODE);
	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
