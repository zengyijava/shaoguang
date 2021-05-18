package com.montnets.emp.netnews.table;

import java.util.HashMap;
import java.util.Map;


public class TableLfWXDataChos{
	
	public static final String TABLE_NAME ="LF_WX_DATA_CHOS";
	public static final String DID = "DID";					
	public static final String NAME = "NAME";			
	
	//映射集合
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfWXDataChos", TABLE_NAME);
		columns.put("dId", DID);
		columns.put("name", NAME);
		
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
