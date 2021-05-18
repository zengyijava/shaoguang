package com.montnets.emp.table.corp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hsh <lensener@foxmail.com>
 * @project montnets_entity
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2018-10-19 下午02:53:36
 * @description 前端配置表
 */
public class TableLfThirCorp
{
	//表名 (企业表)
	public final static String TABLE_NAME = "LF_THIR_CORP";
	//标识ID
	public static final String ID = "ID";

	public static final String SEQUENCE = "LF_THIR_CORP_S";

	public static final String MENU_NUM = "MENU_NUM";

	public static final String CORP_CODE = "CORP_CODE";

	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfThirCorp", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("menuNum", MENU_NUM);
		columns.put("corpCode", CORP_CODE);
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
