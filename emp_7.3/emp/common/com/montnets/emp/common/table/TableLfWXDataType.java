package com.montnets.emp.common.table;

import java.util.HashMap;
import java.util.Map;


public class TableLfWXDataType {
	
	public static final String TABLE_NAME ="LF_WX_DATATYPE";
	public static final String ID = "ID";					
	public static final String NAME = "NAME";			
	public static final String UPDATETIME = "UPDATETIME";			
	public static final String CREATDATE = "CREATDATE";
	public static final String CORP_CODE = "CORP_CODE";	
	//序列
	public static final String SEQUENCE = "S_LF_WX_DATATYPE";
	
	//映射集合
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfWXDataType", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("name", NAME);
		columns.put("updateTime", UPDATETIME);
		columns.put("creatDate", CREATDATE);
		columns.put("corpCode", CORP_CODE);
		columns.put("sequence", SEQUENCE);
		
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
