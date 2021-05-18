package com.montnets.emp.table.corp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hsh <lensener@foxmail.com>
 * @project montnets_entity
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2018-10-19 下午02:22:36
 * @description 前端配置表
 */
public class TableLfWebconfig
{
	//表名 (企业表)
	public static final String TABLE_NAME = "LF_WEBCONFIG";
	//标识ID
	public static final String ID = "ID";

	public static final String SEQUENCE = "LF_WEBCONFIG_S";

	public static final String CONFIG_NAME = "CONFIG_NAME";

	public static final String JSON_CONFIG = "JSON_CONFIG";

	public static final String CORP_CODE = "CORP_CODE";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfWebconfig", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("configName", CONFIG_NAME);
		columns.put("jsonConfig", JSON_CONFIG);
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
