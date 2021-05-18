/**
 * 
 */
package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局变量表
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-2 下午06:36:04
 * @description
 */

public class TableLfGlobalVariable
{
	//表名_全局变量
	public static final String TABLE_NAME = "LF_GLOBAL_VARIABLE";
	//自增ID
	public static final String GLOBAL_ID = "GLOBALID";
	//KEY键
	public static final String GLOBAL_KEY = "GLOBALKEY";
	//VALUE值
	public static final String GLOBAL_VALUE = "GLOBALVALUE";
	//字符串值
	public static final String GLOBALSTRVALUE = "GLOBALSTRVALUE";
	//序列名
	public static final String SEQUENCE = "S_LF_GLOBAL_VARIABLE";
	
 	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfGlobalVariable", TABLE_NAME);
		columns.put("tableId", GLOBAL_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("globalId", GLOBAL_ID);
		columns.put("globalKey", GLOBAL_KEY);
		columns.put("globalValue", GLOBAL_VALUE);
		columns.put("globalStrValue",GLOBALSTRVALUE);
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
