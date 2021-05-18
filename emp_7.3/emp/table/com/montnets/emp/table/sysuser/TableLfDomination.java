package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;

/**
 * TableLfDomination对应的实体类
 * 
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午02:24:22
 * @description
 */

public class TableLfDomination {

	//表名  (业务管辖)
	public static final String TABLE_NAME = "LF_DOMINATION";
	//操作员ID
	public static final String DEP_ID = "DEP_ID";
	//机构ID
	public static final String USER_ID = "USER_ID";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfDomination", TABLE_NAME);
		columns.put("depId", DEP_ID);
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
