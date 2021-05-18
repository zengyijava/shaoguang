package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableAcmdRoute {

	public static final String TABLE_NAME = "A_CMD_ROUTE";
	public static final String ID = "ID";
	public static final String NAME = "NAME";
	public static final String STRUCTCODE = "STRUCTCODE";
	public static final String SP_ID = "SP_ID";
	public static final String BUSSYSNAME = "BUSSYSNAME";
	public static final String CREATER = "CREATER";
	public static final String CREATTIME = "CREATTIME";
	public static final String STATUS = "STATUS";
	public static final String TRUCTTYPE = "TRUCTTYPE";
	public static final String MATCHMODE = "MATCHMODE";
	public static final String SEQUENCE = "SEQ_A_CMD_ROUTE"; 
	protected final  static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("AcmdRoute", TABLE_NAME);
		columns.put("id", ID);
		columns.put("tableId", ID);
		columns.put("name", NAME);
		columns.put("structcode", STRUCTCODE);
		columns.put("spid", SP_ID);
		columns.put("bussysname", BUSSYSNAME);
		columns.put("creater", CREATER);
		columns.put("creattime", CREATTIME);
		columns.put("status", STATUS);
		columns.put("tructtype", TRUCTTYPE);
		columns.put("matchmode", MATCHMODE);
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
