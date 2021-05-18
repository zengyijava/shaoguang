package com.montnets.emp.table.wxgl;

import java.util.HashMap;
import java.util.Map;

/**
 * 客服人员与微信公众号的绑定关系
 * 
 * @project p_weix
 * @author linzh <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-6-17 上午09:29:21
 * @description
 */
public class TableLfWeiUser2Acc
{

	public static final String				TABLE_NAME	= "LF_WEI_USER2ACC";

	public static final String			A_ID		= "A_ID";

	public static final String			USER_ID		= "USER_ID";

	public static final String			SEQUENCE	= "";

	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfWeiUser2Acc", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
		columns.put("AId", A_ID);
		columns.put("userId", USER_ID);

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
