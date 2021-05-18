package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableProvinceCity {

	public static final String TABLE_NAME = "A_PROVINCECITY";
	public static final String ID = "ID";
    public static final String PROVINCE = "PROVINCE";
    public static final String CITY = "CITY";
    public static final String AREACODE = "AREACODE";
    public static final String PROVINCECODE = "PROVINCECODE";

    public static final String SEQUENCE = "SEQ_A_PROVINCECITY";
    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("ProvinceCity", TABLE_NAME);
		columns.put("id", ID);
		columns.put("province", PROVINCE);
		columns.put("city", CITY);
		columns.put("areacode", AREACODE);
		columns.put("provincecode", PROVINCECODE);
		
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
