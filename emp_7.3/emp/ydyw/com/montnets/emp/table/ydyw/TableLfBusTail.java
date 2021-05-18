package com.montnets.emp.table.ydyw;

import java.util.HashMap;
import java.util.Map;
/**
 * 短信贴尾Table类
 * @author Administrator
 *
 */
public class TableLfBusTail {

	public static final String TABLE_NAME = "LF_BUSTAIL";
	public static final String BUSTAIL_ID = "BUSTAIL_ID";
	public static final String BUSTAIL_NAME = "BUSTAIL_NAME";
	public static final String CORP_CODE = "CORP_CODE";
	public static final String CONTENT = "CONTENT";
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String UPDATE_TIME = "UPDATE_TIME";
	public static final String USER_ID = "USER_ID";
	public static final String SEQUENCE = "S_LF_BUSTAIL";
	public static final String DEP_ID = "DEP_ID";
	protected static final Map<String, String> columns = new HashMap();
	
	  static
	  {
	    columns.put("LfBusTail", TABLE_NAME);
	    columns.put("tableId", BUSTAIL_ID);
	    columns.put("bustail_id", BUSTAIL_ID);
	    columns.put("sequence", SEQUENCE);
	    columns.put("bustail_name", BUSTAIL_NAME);
	    columns.put("corp_code", CORP_CODE);
	    columns.put("content", CONTENT);
	    columns.put("create_time", CREATE_TIME);
	    columns.put("update_time", UPDATE_TIME);
	    columns.put("user_id", USER_ID);
	    columns.put("dep_id", DEP_ID);
	  }

	  public static Map<String, String> getORM()
	  {
	    return columns;
	  }
	
}
