package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:22
 * @description
 */
public class TableLfPageField
{

	
	//表名：
	public static final String TABLE_NAME = "LF_PAGEFIELD";
	//模块编号
	public static final String MODLEID = "MODLEID";

	public static final String PAGEID = "PAGEID";

	public static final String FIELDID = "FIELDID";

	public static final String FIELDNAME = "FIELDNAME";

	public static final String FIELD = "FIELD";

	public static final String FIELDTYPE = "FIELDTYPE";

	public static final String SUBFIELDVALUE = "SUBFIELDVALUE";

	public static final String SUBFIELDNAME = "SUBFIELDNAME";

	public static final String FILEDSHOW = "FILEDSHOW";

	public static final String SUBFIELD = "SUBFIELD";

	public static final String DEFAULTVALUE = "DEFAULTVALUE";

	public static final String SORTVALUE = "SORTVALUE";
	
	public static final String ISFIELD = "ISFIELD";
	

	//序列
	//public static final String SEQUENCE = "19";
	

	//映射集合
    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		
		columns.put("LfPageField", TABLE_NAME);
		//columns.put("tableId", USER_ID);
		//columns.put("sequence", SEQUENCE);
		columns.put("modleId", MODLEID);
		columns.put("pageId", PAGEID);
		columns.put("fieldId", FIELDID);
		columns.put("fieldName", FIELDNAME);
		columns.put("field", FIELD);
		columns.put("fieldType", FIELDTYPE);
		columns.put("subFieldValue", SUBFIELDVALUE);
		columns.put("subFieldName", SUBFIELDNAME);
		columns.put("filedShow", FILEDSHOW);
		columns.put("subField", SUBFIELD);
		columns.put("defaultValue", DEFAULTVALUE);
		columns.put("sortValue", SORTVALUE);
		columns.put("isField", ISFIELD);
		
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
