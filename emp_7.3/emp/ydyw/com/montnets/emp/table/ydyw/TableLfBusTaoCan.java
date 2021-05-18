package com.montnets.emp.table.ydyw;

import java.util.HashMap;
import java.util.Map;

/***
 *  套餐基本信息表(LF_BUSTAOCAN)
 * @author Administrator
 *
 */
public class TableLfBusTaoCan {
	
	public static final String TABLE_NAME = "LF_BUS_TAOCAN";
	public static final String TAOCAN_ID = "TAOCAN_ID";
	public static final String TAOCAN_CODE = "TAOCAN_CODE";
	public static final String TAOCAN_NAME = "TAOCAN_NAME";
	public static final String TAOCAN_DES = "TAOCAN_DES";
	public static final String STATE = "STATE";
	public static final String CORP_CODE = "CORP_CODE";
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String UPDATE_TIME = "UPDATE_TIME";
	public static final String USER_ID = "USER_ID";
	public static final String START_DATE = "START_DATE";
	public static final String END_DATE = "END_DATE";
	public static final String TAOCAN_TYPE = "TAOCAN_TYPE";
	public static final String TAOCAN_MONEY = "TAOCAN_MONEY";
	public static final String DEP_ID = "DEP_ID";
	public static final String SEQUENCE = "S_LF_BUSTAOCAN";
	protected static final Map<String, String> columns = new HashMap();
	
	  static
	  {
	    columns.put("LfBusTaoCan", TABLE_NAME);
	    columns.put("sequence", SEQUENCE);
	    columns.put("tableId", TAOCAN_ID);
	    columns.put("taocan_id", TAOCAN_ID);
	    columns.put("taocan_code", TAOCAN_CODE);
	    columns.put("taocan_name", TAOCAN_NAME);
	    columns.put("taocan_des", TAOCAN_DES);
	    columns.put("state", STATE);
	    columns.put("corp_code", CORP_CODE);
	    columns.put("create_time", CREATE_TIME);
	    columns.put("update_time", UPDATE_TIME);
	    columns.put("user_id", USER_ID);
	    columns.put("start_date", START_DATE);
	    columns.put("end_date", END_DATE);
	    columns.put("taocan_type", TAOCAN_TYPE);
	    columns.put("taocan_money", TAOCAN_MONEY);
	    columns.put("dep_id", DEP_ID);
	  }

	  public static Map<String, String> getORM()
	  {
	    return columns;
	  }
}
