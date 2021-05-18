package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;

public class TableLfUpUserInfo {
	public static final String TABLE_NAME = "LF_UP_USERINFO";

	public static final String GUID = "GUID";
	public static final String DEP_CODE = "DEP_CODE";
	public static final String USER_TYPE = "USER_TYPE";
	public static final String USER_ROLE = "USER_ROLE";
	public static final String USER_NAME  = "USER_NAME";
	public static final String NAME = "NAME";
	public static final String SEX = "SEX";
	public static final String BIRTHDAY = "BIRTHDAY";
	public static final String MOBILE = "MOBILE";
	public static final String OPT_TYPE = "OPT_TYPE";
	public static final String IS_UPDATE = "IS_UPDATE";
	public static final String TELEPHONE = "TELEPHONE";
	public static final String QQ = "QQ";
	public static final String E_MAIL = "E_MAIL";
	public static final String MSN = "MSN";
	public static final String PASSW = "PASSWORD";
	public static final String USER_STATE = "USER_STATE";
	public static final String REG_TIME = "REG_TIME";
	public static final String USER_CODE_THIRD = "USER_CODE_THIRD";
	public static final String CORP_CODE = "CORP_CODE";
	public static final String STATUS = "STATUS";
	public static final String UPDATE_TIME = "UPDATE_TIME";
	public static final String SEQUENCE = "S_LfUpUserInfo";
	public static final String UPID = "UPID";
	
	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfUpUserInfo", TABLE_NAME);
		columns.put("tableId", UPID);
		columns.put("sequence", SEQUENCE);
		columns.put("upId", UPID);
		columns.put("guid", GUID);
		columns.put("depCode", DEP_CODE);
		columns.put("userType", USER_TYPE);
		columns.put("userRole", USER_ROLE);
		columns.put("userName", USER_NAME);
		columns.put("name", NAME);
		columns.put("sex", SEX);
		columns.put("birthday", BIRTHDAY);
		columns.put("mobile", MOBILE);
		columns.put("optType", OPT_TYPE);
		columns.put("isUpdate", IS_UPDATE);
		columns.put("telephone", TELEPHONE);
		columns.put("qq", QQ);
		columns.put("email", E_MAIL);
		columns.put("msn", MSN);
		columns.put("password", PASSW);
		columns.put("userState", USER_STATE);
		columns.put("regtime", REG_TIME);
		columns.put("userCodeThird", USER_CODE_THIRD);
		columns.put("corpCode", CORP_CODE);
		columns.put("status", STATUS);
		columns.put("updateTime", UPDATE_TIME);
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
