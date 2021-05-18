package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableMobileArea {
	public static final String TABLE_NAME = "A_MOBILEAREA";
	private static final String ID = "ID";
	private static final String MOBILE = "MOBILE";
	private static final String AREACODE = "AREACODE";
	private static final String CREATETIME = "CREATETIME";
	
	private static final String SEQUENCE = "SEQ_A_MOBILEAREA"; 
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("MobileArea", TABLE_NAME);
		columns.put("id", ID);
		columns.put("tableId", ID);
		columns.put("mobile", MOBILE);
		columns.put("areacode", AREACODE);
		columns.put("createtime", CREATETIME);
		
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
