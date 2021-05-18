package com.montnets.emp.rms.meditor.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfHfive {
	public final static String TABLE_NAME = "LF_HFIVE";

	public static final String HID = "HID";
	
	public static final String TITLE = "TITLE";
	
	public static final String AUTHOR = "AUTHOR";
	
	public static final String CREATETIME = "CREATETIME";
	
	public static final String COMMITTIME = "COMMITTIME";
	
	public static final String URL = "URL";
	
	public static final String UPDATETIME = "UPDATETIME";
	
	public static final String USETIME = "USETIME";
	
	public static final String STAUS = "STAUS";

	protected final static Map<String, String> columns = new HashMap<String, String>();
	
	static
	{
		columns.put("LfHfive", TABLE_NAME);
		columns.put("tableId", HID);
		columns.put("hId", HID);
		columns.put("title", TITLE);
		columns.put("author", AUTHOR);
		columns.put("createTime", CREATETIME);
		columns.put("commitTime", COMMITTIME);
		columns.put("url", URL);
		columns.put("updateTime", UPDATETIME);
		columns.put("useTime", USETIME);
		columns.put("staus", STAUS);
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
