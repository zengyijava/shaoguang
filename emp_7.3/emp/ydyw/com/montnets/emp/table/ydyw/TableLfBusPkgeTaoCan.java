package com.montnets.emp.table.ydyw;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务、业务包与套餐关系表(LF_BUSPKGETAOCAN )
 * @author Administrator
 *
 */
public class TableLfBusPkgeTaoCan {

	public static final String TABLE_NAME = "LF_BUSPKGETAOCAN";
	public static final String ID = "ID";
	public static final String TAOCAN_CODE = "TAOCAN_CODE";
	public static final String PACKAGE_CODE = "PACKAGE_CODE";
	public static final String BUS_CODE = "BUS_CODE";
	public static final String ASSOCIATE_TYPE = "ASSOCIATE_TYPE";
	public static final String CORP_CODE = "CORP_CODE";
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String SEQUENCE = "S_LF_BUSPKGETAOCAN";
	protected static final Map<String, String> columns = new HashMap();
	
	  static
	  {
	    columns.put("LfBusPkgeTaoCan", TABLE_NAME);
	    columns.put("tableId", ID);
	    columns.put("id", ID);
	    columns.put("sequence", SEQUENCE);
	    columns.put("taocanCode", TAOCAN_CODE);
	    columns.put("packageCode", PACKAGE_CODE);
	    columns.put("busCode", BUS_CODE);
	    columns.put("associateType", ASSOCIATE_TYPE);
	    columns.put("corpCode", CORP_CODE);
	    columns.put("createTime", CREATE_TIME);
	  }

	  public static Map<String, String> getORM()
	  {
	    return columns;
	  }
}
