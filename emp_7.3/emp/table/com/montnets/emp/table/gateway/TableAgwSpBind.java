/**
 * 
 */
package com.montnets.emp.table.gateway;

import java.util.HashMap;
import java.util.Map;


/**
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-10-17 下午07:05:08
 * @description 
 */

public class TableAgwSpBind
{
	public final static String TABLE_NAME = "A_GWSPBIND";

	public static final String PTACCUID = "PTACCUID";
 
	public static final String GATEID = "GATEID";

 
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("AgwSpBind", TABLE_NAME);
		//columns.put("tableId",PTACCUID );
 		columns.put("ptAccUid", PTACCUID);
		columns.put("gateId", GATEID);
	 
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
