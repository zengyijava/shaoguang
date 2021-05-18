package com.montnets.emp.table.selfparam;

import java.util.HashMap;
import java.util.Map;

public class TableLfWgParamConfig {

	//网关动态参数表配置表的列清单
	public static final String TABLE_NAME = "LF_WGPARAMCONFIG";

	//参数ID(自增ID)
	public static final String PID = "PID";
	
	//Param2，Param3，Param4
	public static final String PARAM = "PARAM";
	
	//参数值
	public static final String PARAMVALUE = "PARAMVALUE";
	
	//该动态参数所代表的含义
	public static final String PARAMNAME = "PARAMNAME";
	
	//备注
	public static final String MEMO = "MEMO";
	
	//所属企业编码
	public static final String CORPCODE = "CORPCODE";
	
	//分段数
	public static final String PARAMSUBNUM="PARAMSUBNUM";
	
	//序列
	public static final String SEQUENCE = "S_LF_WGPARAMCONFIG";

	//映射集合
    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfWgParamConfig", TABLE_NAME);
		columns.put("tableId", PID);
		columns.put("sequence", SEQUENCE);
		columns.put("pid", PID);
		columns.put("param",PARAM );
		columns.put("paramValue", PARAMVALUE);
		columns.put("paramName",PARAMNAME );
		columns.put("memo", MEMO);
		columns.put("corpCode", CORPCODE);
		columns.put("paramSubNum", PARAMSUBNUM);
 		
		
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
