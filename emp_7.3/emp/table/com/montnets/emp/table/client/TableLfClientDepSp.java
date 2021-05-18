package com.montnets.emp.table.client;

import java.util.HashMap;
import java.util.Map;

/**
 * @project sinolife
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-16 下午03:18:12
 * @description 
 */
public class TableLfClientDepSp
{
	//表名(客户机构表)
	public static final String  TABLE_NAME = "LF_ClIENT_DEP_SP";
	//标识列ID
	public static final String  DEP_ID = "DEP_ID";
	
	public static final String  CLIENT_ID = "CLIENT_ID";




    protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfClientDepSp", TABLE_NAME);
		columns.put("depId", DEP_ID);
		columns.put("clientId", CLIENT_ID);

 
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
