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
 * @datetime 2011-5-30 上午11:26:30
 * @description
 */

public class TableLfEmpDepConn
{
	//表名 (员工与员工机构权限表)
	public static final String TABLE_NAME = "LF_EMPDEP_CONN";
	//标识列
	public static final String CONN_ID = "CONN_ID";
	//操作员ID
	public static final String USER_ID = "USER_ID";

	public static final String DEP_CODE_THIRD = "DEP_CODE_THIRD";
	//序列名
	public static final String SEQUENCE = "S_LF_EMPDEP_CONN";
	//部门ID
	public static final String DEP_ID = "DEP_ID";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfEmpDepConn", TABLE_NAME);
		columns.put("tableId", CONN_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("connId", CONN_ID);
		columns.put("userId", USER_ID);
		columns.put("depCodeThird", DEP_CODE_THIRD);
		columns.put("depId", DEP_ID);
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
