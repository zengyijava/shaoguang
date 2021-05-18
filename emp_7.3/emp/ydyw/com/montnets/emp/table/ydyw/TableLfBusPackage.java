package com.montnets.emp.table.ydyw;

import java.util.HashMap;
import java.util.Map;
/**
 * 业务包管理Table类
 * @todo TODO
 * @project	emp
 * @author WANGRUBIN
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-12 上午10:51:22
 * @description
 */
public class TableLfBusPackage {

	public static final String TABLE_NAME = "LF_BUS_PACKAGE";
	public static final String PACKAGE_ID = "PACKAGE_ID";
	public static final String PACKAGE_CODE = "PACKAGE_CODE";
	public static final String PACKAGE_NAME = "PACKAGE_NAME";
	public static final String PACKAGE_DES = "PACKAGE_DES";
	public static final String PACKAGE_STATE = "PACKAGE_STATE";
	public static final String CORP_CODE = "CORP_CODE";
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String UPDATE_TIME = "UPDATE_TIME";
	public static final String DEP_ID = "DEP_ID";
	public static final String USER_ID = "USER_ID";
	public static final String SEQUENCE = "S_LF_BUS_PACKAGE";
	protected static final Map<String, String> columns = new HashMap();
	
	static
	{
		columns.put("LfBusPackage", TABLE_NAME);
		columns.put("tableId", PACKAGE_ID);
		columns.put("packageId", PACKAGE_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("packageCode", PACKAGE_CODE);
		columns.put("packageName", PACKAGE_NAME);
		columns.put("packageDes", PACKAGE_DES);
		columns.put("packageState", PACKAGE_STATE);
		columns.put("corpCode", CORP_CODE);
		columns.put("createTime", CREATE_TIME);
		columns.put("updateTime", UPDATE_TIME);
		columns.put("depId", DEP_ID);
		columns.put("userId", USER_ID);
	}

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
