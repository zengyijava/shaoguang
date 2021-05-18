package com.montnets.emp.servmodule.txgl.table;

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
	private static final String MODLEID = "MODLEID";

	private static final String PAGEID = "PAGEID";

	private static final String FIELDID = "FIELDID";

	private static final String FIELDNAME = "FIELDNAME";

	private static final String FIELD = "FIELD";

	private static final String FIELDTYPE = "FIELDTYPE";

	private static final String SUBFIELDVALUE = "SUBFIELDVALUE";

	private static final String SUBFIELDNAME = "SUBFIELDNAME";

	private static final String FILEDSHOW = "FILEDSHOW";

	private static final String SUBFIELD = "SUBFIELD";

	private static final String DEFAULTVALUE = "DEFAULTVALUE";

	private static final String SORTVALUE = "SORTVALUE";
	
	private static final String ISFIELD = "ISFIELD";
	

	//序列
	//private static final String SEQUENCE = "19";
	

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
