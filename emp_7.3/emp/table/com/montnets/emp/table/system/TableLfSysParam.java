package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;

public class TableLfSysParam {

	public static final String TABLE_NAME = "LF_SYS_PARAM";

	//自增ID
	public static final String PARAM_ID = "PARAM_ID";
	
	//参数组
	public static final String PARAM_GROUP = "PARAM_GROUP";
	
	//参数项
	public static final String PARAM_ITEM = "PARAM_ITEM";
	
	//参数值
	public static final String PARAM_VALUE = "PARAM_VALUE";
	
	//含义说明
	public static final String MEMO = "MEMO";
	
	//序列
	public static final String SEQUENCE = "S_LF_SYS_PARAM";

	//映射集合
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfSysParam", TABLE_NAME);
		columns.put("tableId", PARAM_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("paramID", PARAM_ID);
		columns.put("paramGroup",PARAM_GROUP );
		columns.put("paramItem", PARAM_ITEM);
		columns.put("paramValue",PARAM_VALUE );
		columns.put("memo", MEMO);
		
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
