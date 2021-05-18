/**
 * 
 */
package com.montnets.emp.group.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;


public class ViewLfList2groVo
{
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

	static
	{
		columns.put("guid", "guid");
		columns.put("name", "name");
		columns.put("mobile","mobile");
		columns.put("l2gtype", "l2g_type");
		columns.put("sharetype", "share_type");
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
