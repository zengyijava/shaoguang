package com.montnets.emp.table.gateway;

import java.util.HashMap;
import java.util.Map;


public class TableAgwParamValue {

	public static final String TABLE_NAME = "A_GWPARAMVALUE";

	public static final String GWNO = "GWNO";
 
	public static final String GWTYPE = "GWTYPE";

	public static final String PARAMITEM = "PARAMITEM";
	
	public static final String PARAMVALUE = "PARAMVALUE";
	 
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("AgwParamValue", TABLE_NAME);
  		//columns.put("tableId", ID);
  		//columns.put("sequence",SEQUENCE);
		columns.put("gwNo", GWNO);
		columns.put("gwType",GWTYPE);
		columns.put("paramItem",PARAMITEM);
		columns.put("paramValue", PARAMVALUE);
		 
	 
	 
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
