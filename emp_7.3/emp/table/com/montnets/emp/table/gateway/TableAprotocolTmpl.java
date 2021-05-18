package com.montnets.emp.table.gateway;

import java.util.HashMap;
import java.util.Map;


public class TableAprotocolTmpl {

	public static final String TABLE_NAME = "A_PROTOCOLTMPL";

	public static final String PROTOCOLCODE = "PROTOCOLCODE";
 
	public static final String PROTOCOL = "PROTOCOL";

	public static final String PROTOCOLPARAM = "PROTOCOLPARAM";
	
 	 
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("AprotocolTmpl", TABLE_NAME);
  		//columns.put("tableId", ID);
  		//columns.put("sequence",SEQUENCE);
		columns.put("protocolCode", PROTOCOLCODE);
		columns.put("protocol",PROTOCOL);
		columns.put("protocolParam",PROTOCOLPARAM); 
	 
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
