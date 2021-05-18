package com.montnets.emp.table.client;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuqw <158030621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2012-1-10
 * @description 
 */
public class TableLfCustFieldValue {

	//表名
	public static final String TABLE_NAME = "LF_CUSTFIELD_VALUE";
	//自增ID
	public static final String ID = "ID";
	//引用字段，关联LF_CUSTFIELD表的ID
	public static final String FIELD_ID = "FIELD_ID";
	//值
	public static final String FIELD_VALUE = "FIELD_VALUE";
	//序列名
	public static final String SEQUENCE="S_LF_CUSTFIELD_VALUE";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfCustFieldValue", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("field_ID", FIELD_ID);
		columns.put("field_Value", FIELD_VALUE);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}

}
