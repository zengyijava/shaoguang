package com.montnets.emp.table.wxsysuser;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 上午10:50:58
 * @description
 */
public class TableLfSysImpower {

	public static final String TABLE_NAME = "LF_SYS_IMPOWER";
	public static final String ROLE_ID = "ROLE_ID";
	public static final String PRIVILEGE_ID = "PRIVILEGE_ID";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfSysImpower", TABLE_NAME);
 		columns.put("roleId", ROLE_ID);
		columns.put("privilegeId", PRIVILEGE_ID);
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
