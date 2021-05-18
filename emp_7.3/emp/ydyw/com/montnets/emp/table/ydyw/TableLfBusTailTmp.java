package com.montnets.emp.table.ydyw;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务与短信贴尾及模板关系表
 * @author Administrator
 *
 */
public class TableLfBusTailTmp {

	public static final String TABLE_NAME = "LF_BUS_TAILTMP";
	public static final String ID = "ID";
	public static final String BUS_ID = "BUS_ID";
	public static final String CORP_CODE = "CORP_CODE";
	public static final String SMSTAIL_ID = "SMSTAIL_ID";
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String TM_ID = "TM_ID";
	public static final String UPDATE_TIME = "UPDATE_TIME";
	public static final String USER_ID = "USER_ID";
	public static final String ASSOCIATE_TYPE= "ASSOCIATE_TYPE";
	public static final String DEP_ID= "DEP_ID";
	
	public static final String SEQUENCE = "S_LF_BUS_TAILTMP";
	protected static final Map<String, String> columns = new HashMap();
	
	  static
	  {
	    columns.put("LfBusTailTmp", TABLE_NAME);
	    columns.put("tableId", ID);
	    columns.put("id", ID);
	    columns.put("sequence", SEQUENCE);
	    columns.put("bus_id", BUS_ID);
	    columns.put("tm_id", TM_ID);
	    columns.put("corp_code", CORP_CODE);
	    columns.put("smstail_id", SMSTAIL_ID);
	    columns.put("create_time", CREATE_TIME);
	    columns.put("update_time", UPDATE_TIME);
	    columns.put("user_id", USER_ID);
	    columns.put("associate_type", ASSOCIATE_TYPE);
	    columns.put("dep_id", DEP_ID);
	  }

	  public static Map<String, String> getORM()
	  {
	    return columns;
	  }
}
