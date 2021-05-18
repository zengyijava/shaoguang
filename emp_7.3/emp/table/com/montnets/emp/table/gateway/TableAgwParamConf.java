package com.montnets.emp.table.gateway;

import java.util.HashMap;
import java.util.Map;


public class TableAgwParamConf {

	public static final String TABLE_NAME = "A_GWPARAMCONF";

	public static final String PARAMITEM = "PARAMITEM";
 
	public static final String PARAMNAME = "PARAMNAME";

	public static final String PARAMATTRIBUTE = "PARAMATTRIBUTE";
	
	public static final String PARAMMEMO = "PARAMMEMO";
	
	public static final String DEFAULTVALUE  = "DEFAULTVALUE";
	
	public static final String VALUERANGE  = "VALUERANGE";
	
	public static final String CONTROLTYPE  = "CONTROLTYPE";
	
	public static final String GWTYPE = "GWTYPE";
	
	public static final String HKParamName = "HKPARAMNAME";
	
	public static final String HKParamMemo = "HKPARAMMEMO";
	
	public static final String ENParamName = "ENPARAMNAME";
	
	public static final String ENParamMemo = "ENPARAMMEMO";
	
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("AgwParamConf", TABLE_NAME);
  		//columns.put("tableId", ID);
  		//columns.put("sequence",SEQUENCE);
		columns.put("paramItem", PARAMITEM);
		columns.put("paramName",PARAMNAME);
		columns.put("paramAttribute",PARAMATTRIBUTE);
		columns.put("paramMemo",PARAMMEMO);
		columns.put("defaultValue",DEFAULTVALUE);
		columns.put("valueRange",VALUERANGE);
	    columns.put("controltyp", CONTROLTYPE);
	    columns.put("gwType", GWTYPE);
	    
	    columns.put("HKParamName", "HKPARAMNAME");
	    columns.put("HKParamMemo", "HKPARAMMEMO");
	    columns.put("ENParamName", "ENPARAMNAME");
	    columns.put("ENParamMemo", "ENPARAMMEMO");
	 
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
