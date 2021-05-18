package com.montnets.emp.table.wxgl;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信用户与客户关联关系表（LF_WEI_ULINK）
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWeiUlink
{
	// 表名：微信关键字与回复模板关联表
	public static final String				TABLE_NAME	= "LF_WEI_ULINK";

	// EMP用户ID
	public static final String			U_ID		= "U_ID";

	// 微信ID
	public static final String			WC_ID		= "WC_ID";

	// 序列
	public static final String			SEQUENCE	= "S_LF_WEI_ULINK";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfWeiUlink", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
		columns.put("wcId", WC_ID);
		columns.put("UId", U_ID);
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
