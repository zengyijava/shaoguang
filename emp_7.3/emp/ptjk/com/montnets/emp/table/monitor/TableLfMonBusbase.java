package com.montnets.emp.table.monitor;

import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;

/**
 * 
*    
* 项目名称：emp标准版   
* 类名称：TableLfMonBusbase   
* 类描述：   业务监控基础信息表
* 创建时间：2015-11-23 上午10:33:34   
* 修改备注：   
* @version V6.0
*
 */
public class TableLfMonBusbase
{

	public static final String TABLE_NAME	= "LF_MON_BUSBASE";

	public static final String BUS_CODE = "BUS_CODE";

	public static final String MON_STATE = "MON_STATE";

	public static final String BEGIN_TIME = "BEGIN_TIME";

	public static final String BUS_NAME = "BUS_NAME";

	public static final String CREATE_TIME = "CREATE_TIME";

	public static final String MODI_USERID = "MODI_USERID";

	public static final String ID = "ID";

	public static final String AREA_CODE = "AREA_CODE";

	public static final String UPDATE_TIME = "UPDATE_TIME";

	public static final String END_TIME = "END_TIME";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String MONPHONE = "MONPHONE";
	
	public static final String MONEMAIL	= "MONEMAIL";

	public static final String SEQUENCE	= "S_LF_M_BUSBASE";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfMonBusbase", TABLE_NAME);
		columns.put("tableId",ID);
		columns.put("sequence", SEQUENCE);
		columns.put("buscode", BUS_CODE);
		columns.put("monstate", MON_STATE);
		columns.put("begintime", BEGIN_TIME);
		columns.put("busname", BUS_NAME);
		columns.put("createtime", CREATE_TIME);
		columns.put("modiuserid", MODI_USERID);
		columns.put("id", ID);
		columns.put("areacode", AREA_CODE);
		columns.put("updatetime", UPDATE_TIME);
		columns.put("endtime", END_TIME);
		columns.put("corpcode", CORP_CODE);
		columns.put("monphone", MONPHONE);
		columns.put("monemail", MONEMAIL);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}