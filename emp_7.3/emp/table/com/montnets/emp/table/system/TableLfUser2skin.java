package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;

public class TableLfUser2skin {
	// 表名(操作员皮肤关联表)
	public static final String TABLE_NAME = "LF_USER2SKIN";
	// 主键ID
	public static final String USID  = "USID";
	// 皮肤ID
	public static final String SID = "SID";
	// 操作员id
	public static final String USERID = "USERID";
	// 操作员id
	public static final String SKIN_CODE = "SKIN_CODE";
	// 主题编码
	public static final String THEME_CODE = "THEME_CODE";
	//主题使用状态（取值1-使用中,0-未使用）
	public static final String THEME_USE = "THEME_USE";
	//序列
	public static final String SEQUENCE = "S_LF_USER2SKIN";
	
	public static final String			MONVOICE	= "MONVOICE";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfUser2skin", TABLE_NAME);
		columns.put("tableId", USID);
		columns.put("sequence", SEQUENCE);
		columns.put("usid", USID);
		columns.put("sid", SID);
		columns.put("userid", USERID);
		columns.put("skincode", SKIN_CODE);
		columns.put("themecode", THEME_CODE);
		columns.put("themeuse", THEME_USE);
		columns.put("monvoice", MONVOICE);
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
