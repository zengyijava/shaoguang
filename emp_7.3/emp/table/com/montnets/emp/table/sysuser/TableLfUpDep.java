package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;

public class TableLfUpDep {
	
	public static final String TABLE_NAME = "LF_UP_DEP";

	public static final String DEP_CODE = "DEP_CODE";
	public static final String DEP_NAME = "DEP_NAME";
	public static final String DEP_SHORTNAME = "DEP_SHORTNAME";
	public static final String DEP_LEVEL = "DEP_LEVEL";
	public static final String DEP_PCODE  = "DEP_PCODE";
	public static final String DEP_CODE_THIRD = "DEP_CODE_THIRD";
	public static final String CORP_CODE = "CORP_CODE";
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String UPDATE_DATE = "UPDATE_DATE";
	public static final String OPT_TYPE = "OPT_TYPE";
	public static final String IS_UPDATE = "IS_UPDATE";
	public static final String SEQUENCE = "S_LF_UP_DEP";
	public static final String UD_ID = "UDID";
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfUpDep", TABLE_NAME);
		columns.put("tableId", UD_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("udId", UD_ID);
		columns.put("depCode", DEP_CODE);
		columns.put("depName", DEP_NAME);
		columns.put("depShortName", DEP_SHORTNAME);
		columns.put("depLevel", DEP_LEVEL);
		columns.put("depPcode", DEP_PCODE);
		columns.put("depCodeThird", DEP_CODE_THIRD);
		columns.put("corpCode", CORP_CODE);
		columns.put("createDate", CREATE_DATE);
		columns.put("updateDate", UPDATE_DATE);
		columns.put("optType", OPT_TYPE);
		columns.put("isUpdate", IS_UPDATE);
	}

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}
}
