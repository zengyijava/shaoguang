package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfTructType {

	public static final String TABLE_NAME = "LF_TRUCTTYPE";
	public static final String ID = "ID";
	public static final String TYPE = "TYPE";
	public static final String NAME = "NAME";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();
	
	static{
		columns.put("LfTructType", TABLE_NAME);
		columns.put("id", ID);
		columns.put("type", TYPE);
		columns.put("name", NAME);
	}
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
