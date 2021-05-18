/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-15 上午10:37:09
 */
package com.montnets.emp.table.ydyw;

import java.util.HashMap;
import java.util.Map;
/**
 * @description 
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-15 上午10:37:09
 */

public class TableLfContractTaocan
{
	public static final String TABLE_NAME	= "LF_CONTRACT_TAOCAN";

	public static final String CONTRACT_ID = "CONTRACT_ID";

	public static final String GUID = "GUID";

	public static final String USER_ID = "USER_ID";

	public static final String CREATE_TIME = "CREATE_TIME";

	public static final String CONTRACT_USER = "CONTRACT_USER";

	public static final String DEBITACCOUNT = "DEBITACCOUNT";

	public static final String UPDATE_TIME = "UPDATE_TIME";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String TAOCAN_MONEY = "TAOCAN_MONEY";

	public static final String IS_VALID = "IS_VALID";

	public static final String DEP_ID = "DEP_ID";

	public static final String TAOCAN_CODE = "TAOCAN_CODE";

	public static final String ID = "ID";

	public static final String CONTRACT_DEP = "CONTRACT_DEP";

	public static final String TAOCAN_TYPE = "TAOCAN_TYPE";

	public static final String BUCKLEFEE_TIME = "BUCKLEFEE_TIME";
	
	public static final String TAOCAN_NAME = "TAOCAN_NAME";

	public static final String LAST_BUCKLEFEE = "LAST_BUCKLEFEE";

	public static final String SEQUENCE	= "S_LF_CONTRACT_TAOCAN";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfContractTaocan", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("contractid", CONTRACT_ID);
		columns.put("guid", GUID);
		columns.put("userid", USER_ID);
		columns.put("createtime", CREATE_TIME);
		columns.put("contractuser", CONTRACT_USER);
		columns.put("debitaccount", DEBITACCOUNT);
		columns.put("updatetime", UPDATE_TIME);
		columns.put("corpcode", CORP_CODE);
		columns.put("taocanmoney", TAOCAN_MONEY);
		columns.put("isvalid", IS_VALID);
		columns.put("depid", DEP_ID);
		columns.put("taocancode", TAOCAN_CODE);
		columns.put("id", ID);
		columns.put("contractdep", CONTRACT_DEP);
		columns.put("taocantype", TAOCAN_TYPE);
		columns.put("bucklefeetime", BUCKLEFEE_TIME);
		columns.put("taocanname", TAOCAN_NAME);
		columns.put("lastbucklefee", LAST_BUCKLEFEE);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
