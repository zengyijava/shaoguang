/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-15 上午08:46:09
 */
package com.montnets.emp.table.ydyw;


import java.util.HashMap;
import java.util.Map;
/**
 * @description 
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-15 上午08:46:09
 */

public class TableLfContract
{
	public static final String TABLE_NAME	= "LF_CONTRACT";

	public static final String GUID = "GUID";

	public static final String ACCT_IDENTTYPE = "ACCT_IDENTTYPE";

	public static final String CONTRACT_USER = "CONTRACT_USER";

	public static final String CANCEL_CONTYPE = "CANCEL_CONTYPE";

	public static final String CUSTOM_TYPE = "CUSTOM_TYPE";

	public static final String CONTRACT_STATE = "CONTRACT_STATE";

	public static final String DEBITACCOUNT = "DEBITACCOUNT";

	public static final String CLIENT_CODE = "CLIENT_CODE";

	public static final String IS_VALID = "IS_VALID";

	public static final String CONTRACT_TYPE = "CONTRACT_TYPE";

	public static final String ACCT_NAME = "ACCT_NAME";

	public static final String DEBITACCT_NAME = "DEBITACCT_NAME";

	public static final String ADD_ORDER_DATE = "ADD_ORDER_DATE";

	public static final String CUSTOM_NAME = "CUSTOM_NAME";

	public static final String DEBITACCT_DEP = "DEBITACCT_DEP";

	public static final String CONTRACT_SOURCE = "CONTRACT_SOURCE";

	public static final String ACCT_IDENTNO = "ACCT_IDENTNO";

	public static final String ACCT_ADDRESS = "ACCT_ADDRESS";

	public static final String CONTRACT_DEP = "CONTRACT_DEP";

	public static final String CONTRACT_ID = "CONTRACT_ID";

	public static final String CONTRACT_DATE = "CONTRACT_DATE";

	public static final String IDENT_NO = "IDENT_NO";

	public static final String ACCT_COMNAME = "ACCT_COMNAME";

	public static final String IDENT_TYPE = "IDENT_TYPE";

	public static final String USER_ID = "USER_ID";

	public static final String ACCT_NO = "ACCT_NO";

	public static final String ADDRESS = "ADDRESS";

	public static final String CANCEL_ORDER_DATE = "CANCEL_ORDER_DATE";

	public static final String UPDATE_TIME = "UPDATE_TIME";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String DEP_ID = "DEP_ID";

	public static final String CANCEL_CONTIME = "CANCEL_CONTIME";

	public static final String MOBILE = "MOBILE";

	public static final String SEQUENCE	= "S_LF_CONTRACT";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfContract", TABLE_NAME);
		columns.put("tableId", CONTRACT_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("guid", GUID);
		columns.put("acctidenttype", ACCT_IDENTTYPE);
		columns.put("contractuser", CONTRACT_USER);
		columns.put("cancelcontype", CANCEL_CONTYPE);
		columns.put("customtype", CUSTOM_TYPE);
		columns.put("contractstate", CONTRACT_STATE);
		columns.put("debitaccount", DEBITACCOUNT);
		columns.put("clientcode", CLIENT_CODE);
		columns.put("isvalid", IS_VALID);
		columns.put("contracttype", CONTRACT_TYPE);
		columns.put("acctname", ACCT_NAME);
		columns.put("debitacctname", DEBITACCT_NAME);
		columns.put("addorderdate", ADD_ORDER_DATE);
		columns.put("customname", CUSTOM_NAME);
		columns.put("debitacctdep", DEBITACCT_DEP);
		columns.put("contractsource", CONTRACT_SOURCE);
		columns.put("acctidentno", ACCT_IDENTNO);
		columns.put("acctaddress", ACCT_ADDRESS);
		columns.put("contractdep", CONTRACT_DEP);
		columns.put("contractid", CONTRACT_ID);
		columns.put("contractdate", CONTRACT_DATE);
		columns.put("identno", IDENT_NO);
		columns.put("acctcomname", ACCT_COMNAME);
		columns.put("identtype", IDENT_TYPE);
		columns.put("userid", USER_ID);
		columns.put("acctno", ACCT_NO);
		columns.put("address", ADDRESS);
		columns.put("cancelorderdate", CANCEL_ORDER_DATE);
		columns.put("updatetime", UPDATE_TIME);
		columns.put("corpcode", CORP_CODE);
		columns.put("depid", DEP_ID);
		columns.put("cancelcontime", CANCEL_CONTIME);
		columns.put("mobile", MOBILE);
	};


	public static Map<String , String> getORM(){

		return columns;

	}

}
