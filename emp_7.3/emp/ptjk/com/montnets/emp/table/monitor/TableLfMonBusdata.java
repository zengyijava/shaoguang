package com.montnets.emp.table.monitor;

import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;

/**
 * 
*    
* 项目名称：emp标准版   
* 类名称：TableLfMonBusdata   
* 类描述：   业务监控数据信息表
* 创建时间：2015-11-23 上午10:33:34   
* 修改备注：   
* @version V6.0
*
 */
public class TableLfMonBusdata
{



	public static final String TABLE_NAME	= "LF_MON_BUSDATA";

	public static final String END_HOUR = "END_HOUR";

	public static final String CREATE_TIME = "CREATE_TIME";

	public static final String MODI_USERID = "MODI_USERID";

	public static final String DEVIAT_HIGH = "DEVIAT_HIGH";

	public static final String ID = "ID";

	public static final String UPDATE_TIME = "UPDATE_TIME";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String MON_LASTTIME = "MON_LASTTIME";

	public static final String DEVIAT_LOW = "DEVIAT_LOW";

	public static final String MTHAVESND = "MTHAVESND";

	public static final String BEGIN_HOUR = "BEGIN_HOUR";

	public static final String BUSBASE_ID = "BUSBASE_ID";

	public static final String SEQUENCE	= "S_LF_M_BUSDATA";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfMonBusdata", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("endhour", END_HOUR);
		columns.put("createtime", CREATE_TIME);
		columns.put("modiuserid", MODI_USERID);
		columns.put("deviathigh", DEVIAT_HIGH);
		columns.put("id", ID);
		columns.put("updatetime", UPDATE_TIME);
		columns.put("corpcode", CORP_CODE);
		columns.put("monlasttime", MON_LASTTIME);
		columns.put("deviatlow", DEVIAT_LOW);
		columns.put("mthavesnd", MTHAVESND);
		columns.put("beginhour", BEGIN_HOUR);
		columns.put("busbaseid", BUSBASE_ID);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

						