package com.montnets.emp.table.wxsystem;

import java.util.HashMap;
import java.util.Map;

public class TableLfSysTheme {
	
	// 表名(主题管理表)
	public static final String TABLE_NAME = "LF_SYS_THEME";
	// 唯一标示
	public static final String TID = "TID";
	// 皮肤名称
	public static final String THEME_NAME = "THEME_NAME";
	// 皮肤编码
	public static final String THEME_CODE = "THEME_CODE";
	// 皮肤路径
	public static final String THEME_SRC = "THEME_SRC";
	// 序列
	public static final String SEQUENCE = "S_LF_THEME";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfSysTheme", TABLE_NAME);
		columns.put("tableId", TID);
		columns.put("sequence", SEQUENCE);
		columns.put("tid", TID);
		columns.put("themename", THEME_NAME);
		columns.put("themecode", THEME_CODE);
		columns.put("themesrc", THEME_SRC);

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
