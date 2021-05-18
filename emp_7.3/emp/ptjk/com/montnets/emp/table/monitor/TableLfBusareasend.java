package com.montnets.emp.table.monitor;

import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;

public class TableLfBusareasend
{

	public static final String TABLE_NAME	= "LF_BUSAREASEND";

	public static final String BUS_CODE = "BUS_CODE";

	public static final String MTSENDCOUNT = "MTSENDCOUNT";

	public static final String BUS_NAME = "BUS_NAME";

	public static final String DATA_DATE = "DATA_DATE";

	public static final String ID = "ID";

	public static final String AREA_CODE = "AREA_CODE";

	public static final String UPDATE_TIME = "UPDATE_TIME";

	public static final String CORP_CODE = "CORP_CODE";
	
	public static final String 			GATEWAYID 	    		= "GATEWAYID";
	
	public static final String SEQUENCE	= "S_LF_B_AR_SEND";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfBusareasend", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("buscode", BUS_CODE);
		columns.put("mtsendcount", MTSENDCOUNT);
		columns.put("busname", BUS_NAME);
		columns.put("datadate", DATA_DATE);
		columns.put("id", ID);
		columns.put("areacode", AREA_CODE);
		columns.put("updatetime", UPDATE_TIME);
		columns.put("corpcode", CORP_CODE);
		columns.put("gatewayid", GATEWAYID);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

