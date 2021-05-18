package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableAreaPhNo {
	public static final String MOBLIE = "MOBLIE";
	public static final String AREACODE = "AREACODE";
	public static final String PROVINCE = "PROVINCE";
	public static final String SERVEPRO = "SERVEPRO";
	public static final String CITY = "CITY";
	
	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("moblie", MOBLIE);
		columns.put("areaCode", AREACODE);
		columns.put("province", PROVINCE);
		columns.put("servePro", SERVEPRO);
		columns.put("city", CITY);
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
