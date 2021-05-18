/**
 * 
 */
package com.montnets.emp.table.employee;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 上午10:12:02
 * @description
 */

public class TableLfEmployeeType
{
	//表名_职位表
	public static final String TABLE_NAME = "LF_EMPLOYEE_TYPE";
	//标识列ID
	public static final String ID = "ID";
	//操作员名称
	public static final String NAME = "NAME";
	//操作员ID
	public static final String USER_ID = "USER_ID";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	//序列名
	public static final String SEQUENCE = "LF_EMPLOYEE_TYPE_SEQUENCE";


    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfEmployeeType", TABLE_NAME);
		
		columns.put("id", ID);
		
		columns.put("tableId", ID);
		
		columns.put("name", NAME);
		
		columns.put("userId", USER_ID);
		
		columns.put("corpcode", CORP_CODE);
		
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
