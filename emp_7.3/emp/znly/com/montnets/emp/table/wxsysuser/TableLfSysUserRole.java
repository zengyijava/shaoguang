/**
 * 
 */
package com.montnets.emp.table.wxsysuser;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-28 上午09:52:30
 * @description
 */

public class TableLfSysUserRole {
	//表名：角色_用户
	public static final String TABLE_NAME = "LF_SYS_USER_ROLE";
	//角色ID
	public static final String ROLE_ID = "ROLE_ID";
	//操作员ID
	public static final String USER_ID = "USER_ID";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfSysUserRole", TABLE_NAME);
		columns.put("roleId", ROLE_ID);
		columns.put("userId", USER_ID);
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
