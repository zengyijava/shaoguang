package com.montnets.emp.table.ydyw;

import java.util.HashMap;
import java.util.Map;

/***
 * 套餐计费规则表(LF_PROCHARGES)
 * @author Administrator
 *
 */
public class TableLfProCharges {

	public static final String TABLE_NAME = "LF_PROCHARGES";
	public static final String RULE_ID = "RULE_ID";
	public static final String TRYSTART_DATE = "TRYSTART_DATE";
	public static final String TRYEND_DATE = "TRYEND_DATE";
	public static final String TRY_DAYS = "TRY_DAYS";
	public static final String TRY_TYPE = "TRY_TYPE";
	public static final String BUCKLE_TYPE = "BUCKLE_TYPE";
	public static final String BUCKLE_DATE = "BUCKLE_DATE";
	public static final String BUCKUP_MAXTIMER = "BUCKUP_MAXTIMER";
	public static final String BUCKUP_INTERVALDAY = "BUCKUP_INTERVALDAY";
	public static final String TAOCAN_CODE = "TAOCAN_CODE";
	public static final String COMMENTS = "COMMENTS";
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String UPDATE_TIME = "UPDATE_TIME";
	public static final String CORP_CODE = "CORP_CODE";
	public static final String USER_ID = "USER_ID";
	public static final String SEQUENCE = "S_LF_PROCHARGES";
	public static final String DEP_ID = "DEP_ID";
	protected static final Map<String, String> columns = new HashMap();
	
	  static
	  {
	    columns.put("LfProCharges", TABLE_NAME);
	    columns.put("tableId", RULE_ID);
	    columns.put("ruleid", RULE_ID);
	    columns.put("trystartdate", TRYSTART_DATE);
	    columns.put("tryenddate", TRYEND_DATE);
	    columns.put("trydays", TRY_DAYS);
	    columns.put("trytype", TRY_TYPE);
	    columns.put("buckletype", BUCKLE_TYPE);
	    columns.put("buckledate", BUCKLE_DATE);
	    columns.put("sequence", SEQUENCE);
	    columns.put("buckupmaxtimer", BUCKUP_MAXTIMER);
	    columns.put("buckupintervalday", BUCKUP_INTERVALDAY);
	    columns.put("taocancode", TAOCAN_CODE);
	    columns.put("comments", COMMENTS);
	    columns.put("createtime", CREATE_TIME);
	    columns.put("updatetime", UPDATE_TIME);
	    columns.put("corpcode", CORP_CODE);
	    columns.put("userid", USER_ID);
	    columns.put("depid", DEP_ID);
	  }

	  public static Map<String, String> getORM()
	  {
	    return columns;
	  }
	
}
