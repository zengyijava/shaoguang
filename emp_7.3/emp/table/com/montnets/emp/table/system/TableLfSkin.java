package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;

public class TableLfSkin {
	// 表名(皮肤管理表)
	public static final String TABLE_NAME = "LF_SKIN";
	// 唯一标示
	public static final String SID  = "SID";
	// 皮肤名称
	public static final String SKIN_NAMNE = "SKIN_NAMNE";
	// 皮肤编码
	public static final String SKIN_CODE = "SKIN_CODE";
	// 皮肤路径
	public static final String SKIN_SRC = "SKIN_SRC";
	// 主题编码
	public static final String THEME_CODE = "THEME_CODE";
	//序列
	public static final String SEQUENCE = "S_LF_SKIN";

	protected static final Map<String , String> columns = new HashMap<String, String>();

	static {
		columns.put("LfSkin", TABLE_NAME);
		columns.put("tableId", SID);
		columns.put("sequence", SEQUENCE);
		columns.put("sid", SID);
		columns.put("skinname", SKIN_NAMNE);
		columns.put("skincode", SKIN_CODE);
		columns.put("skinsrc", SKIN_SRC);
		columns.put("themecode", THEME_CODE);
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
