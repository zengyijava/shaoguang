package com.montnets.emp.table.selfparam;

import java.util.HashMap;
import java.util.Map;

public class TableLfWgParmDefinition
{
	//表名
	public static final String TABLE_NAME="LF_WGPARAMDEFINITION";
	//参数ID(自增ID)
	public static final String PID="PID"; 
	//Param2，Param3，Param4
	public static final String PARAM="PARAM";
	//分段数
	public static final String PARAMSUBNUM="PARAMSUBNUM";
	//分段符
	public static final String PARAMSUBSIGN="PARAMSUBSIGN";
	//参数名称
	public static final String PARAMSUBNAME="PARAMSUBNAME";
	//备注
	public static final String MEMO = "MEMO";	
	//企业编码
	public static final String CORPCODE = "CORPCODE";
	//序列
	public static final String SEQUENCE = "S_LF_WGPARAMDEFINITION";
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfWgParmDefinition", TABLE_NAME);
		columns.put("tableId", PID);
		columns.put("sequence", SEQUENCE);
		columns.put("pid", PID);
		columns.put("param", PARAM);
		columns.put("paramSubNum", PARAMSUBNUM);
		columns.put("paramSubSign", PARAMSUBSIGN);
		columns.put("paramSubName", PARAMSUBNAME);
		columns.put("memo", MEMO);
		columns.put("corpCode", CORPCODE);
	}

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}

}
