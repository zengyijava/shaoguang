package com.montnets.emp.table.gateway;

import java.util.HashMap;
import java.util.Map;


public class TableAProInfo 
{
	public static final String TABLE_NAME="A_PROINFO";
	
	public static final String SERIAL_NUM="SERIALNUM";
	public static final String PRO_TYPE="PROTYPE";
	public static final String PRO_STATUS="PROSTATUS";
	public static final String STATUS_INFO="STATUSINFO";
	public static final String VALID_DAYS="VALIDDAYS";
	public static final String CORP_NAME="CORPNAME"; 
	public static final String SEND_SPEED="SENDSPEED";
	protected static final Map<String , String> columns = new HashMap<String, String>();
	
	static {
		columns.put("AProInfo", TABLE_NAME);
		columns.put("serialNum", SERIAL_NUM);
		columns.put("proType", PRO_TYPE);
		columns.put("proStatus", PRO_STATUS);
		columns.put("statusInfo", STATUS_INFO);
		columns.put("validDays", VALID_DAYS);
		columns.put("corpName", CORP_NAME);
		columns.put("sendSpeed", SEND_SPEED);
	}

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}

}
