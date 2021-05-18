package com.montnets.emp.table.pasgroup;

import java.util.HashMap;
import java.util.Map;

public class TableAcmdPort {

	public static final String TABLE_NAME = "A_CMD_PORT";
	public static final String ID = "ID";
	public static final String GATEID = "GATEID";
	public static final String CMDID = "CMDID";
	public static final String STATUS = "STATUS";
	public static final String CPNO = "CPNO";
	public static final String FAILOPT = "FAILOPT";
	public static final String DEFCMDID = "DEFCMDID";
	
	public static final String SEQUENCE = "SEQ_A_CMD_PORT"; 
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("AcmdPort", TABLE_NAME);
		columns.put("id", ID);
		columns.put("tableId", ID);
		columns.put("gateId", GATEID);
		columns.put("cmdId", CMDID);
		columns.put("status", STATUS);
		columns.put("cpno", CPNO);
		columns.put("failOpt", FAILOPT);
		columns.put("defCmdId", DEFCMDID);
		
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
