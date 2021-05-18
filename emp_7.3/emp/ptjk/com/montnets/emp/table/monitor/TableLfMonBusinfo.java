package com.montnets.emp.table.monitor;

import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;
/**
 * 
*    
* 项目名称：emp标准版   
* 类名称：TableLfMonBusinfo   
* 类描述：   业务监控告警详情表
* 创建时间：2015-11-23 上午10:33:34   
* 修改备注：   
* @version V6.0
*
 */
public class TableLfMonBusinfo
{

	public static final String TABLE_NAME	= "LF_MON_BUSINFO";

	public static final String BUS_CODE = "BUS_CODE";

	public static final String MTSENDCOUNT = "MTSENDCOUNT";

	public static final String BEGIN_TIME = "BEGIN_TIME";

	public static final String BUS_NAME = "BUS_NAME";

	public static final String ID = "ID";

	public static final String AREA_CODE = "AREA_CODE";

	public static final String END_TIME = "END_TIME";

	public static final String MON_DEVIAT = "MON_DEVIAT";

	public static final String MONPHONE = "MONPHONE";
//	
//	public static final String MONEMAIL = "MONEMAIL";	

	public static final String MTHAVESND = "MTHAVESND";
	
	public static final String EVTTYPE = "EVTTYPE";
	
	public static final String CORP_CODE = "CORP_CODE";
	
	public static final String CREATE_TIME = "CREATE_TIME";
	
	public static final String MON_DES = "MON_DES";
	
	public static final String BEGIN_HOUR = "BEGIN_HOUR";
	
	public static final String END_HOUR = "END_HOUR";
	
	public static final String SEQUENCE	= "S_LF_M_BUSINFO";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfMonBusinfo", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("buscode", BUS_CODE);
		columns.put("mtsendcount", MTSENDCOUNT);
		columns.put("begintime", BEGIN_TIME);
		columns.put("busname", BUS_NAME);
		columns.put("mondes", MON_DES);
		columns.put("beginhour", BEGIN_HOUR);
		columns.put("endhour", END_HOUR);
		columns.put("id", ID);
		columns.put("areacode", AREA_CODE);
		columns.put("endtime", END_TIME);
		columns.put("mondeviat", MON_DEVIAT);
		columns.put("monphone", MONPHONE);
//		columns.put("monemail", MONEMAIL);
		columns.put("mthavesnd", MTHAVESND);
		columns.put("evttype", EVTTYPE);
		columns.put("corpcode", CORP_CODE);
		columns.put("createtime", CREATE_TIME);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}