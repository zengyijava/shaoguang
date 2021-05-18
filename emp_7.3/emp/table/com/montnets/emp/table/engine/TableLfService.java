/**
 * 
 */
package com.montnets.emp.table.engine;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-28 上午08:49:09
 * @description
 */

public class TableLfService
{
	public static final String TABLE_NAME = "LF_SERVICE";

	public static final String SER_ID = "SER_ID";

	public static final String USER_ID = "USER_ID";

	public static final String SER_NAME = "SER_NAME";

	public static final String COMMENTS = "COMMENTS";

	public static final String ORDER_CODE = "ORDER_CODE";

	public static final String SUB_NO = "SUBNO";

	public static final String SP_USER = "SP_USER";

	public static final String SP_PASS = "SP_PWD";

	public static final String RUN_STATE = "RUN_STATE";

	public static final String SER_TYPE = "SER_TYPE";

	public static final String MSG_SEPARATED = "MSG_SEPARATED";

	public static final String OWNER_ID = "OWNER_ID";

	public static final String CREATE_TIME = "CREATE_TIME";

	public static final String BUS_ID = "BUS_ID";

	public static final String CPNO = "CPNO";

	public static final String BUS_CODE = "BUS_CODE";
	
	public static final String MENU_CODE = "MENU_CODE";
	
	public static final String CORP_CODE = "CORP_CODE";
	
	public static final String IDENTIFY_MODE = "IDENTIFY_MODE";
	
	public static final String SEQUENCE = "S_LF_SERVICE";
	
	public static final String ORDER_TYPE = "ORDER_TYPE";
	
	public static final String STRUCTCODE = "STRUCTCODE";




    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfService", TABLE_NAME);
		columns.put("tableId", SER_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("serId", SER_ID);
		columns.put("userId", USER_ID);
		columns.put("serName", SER_NAME);
		columns.put("commnets", COMMENTS);
		columns.put("orderCode", ORDER_CODE);
		columns.put("subNo", SUB_NO);
		columns.put("spUser", SP_USER);
		columns.put("spPwd", SP_PASS);
		columns.put("runState", RUN_STATE);
		columns.put("serType", SER_TYPE);
		columns.put("msgSeparated", MSG_SEPARATED);
		columns.put("ownerId", OWNER_ID);
		columns.put("createTime", CREATE_TIME);
		columns.put("busId", BUS_ID);
		columns.put("cpno", CPNO);
		columns.put("busCode", BUS_CODE);
		columns.put("menuCode", MENU_CODE);
		columns.put("corpCode", CORP_CODE);
		columns.put("identifyMode", IDENTIFY_MODE);
		columns.put("orderType", ORDER_TYPE);
		columns.put("structcode", STRUCTCODE);
		
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM()
	{
		return columns;
	}
}
