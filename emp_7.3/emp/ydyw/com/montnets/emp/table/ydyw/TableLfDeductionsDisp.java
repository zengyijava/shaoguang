package com.montnets.emp.table.ydyw;

import java.util.HashMap;
import java.util.Map;

public class TableLfDeductionsDisp
{

	public static final String TABLE_NAME	= "LF_DEDUCTIONS_DISP";

	public static final String ID = "ID";

	public static final String CONTRACT_ID = "CONTRACT_ID";
	
	public static final String MOBILE = "MOBILE";
	
	public static final String CUSTOM_NAME = "CUSTOM_NAME";

	public static final String ACCT_NO = "ACCT_NO";

	public static final String DEBITACCOUNT = "DEBITACCOUNT";

	public static final String TAOCAN_CODE = "TAOCAN_CODE";
	
	public static final String TAOCAN_MONEY = "TAOCAN_MONEY";
	
	public static final String TAOCAN_NAME = "TAOCAN_NAME";
	
	public static final String TAOCAN_TYPE = "TAOCAN_TYPE";
	
	public static final String CONTRACT_STATE = "CONTRACT_STATE";
	
	public static final String DEDUCTIONS_TYPE = "DEDUCTIONS_TYPE";
	
	public static final String DEDUCTIONS_MONEY = "DEDUCTIONS_MONEY";
	
	public static final String OPR_STATE = "OPR_STATE";
	
	public static final String OPR_TIME = "OPR_TIME";
	
	public static final String CONTRACT_DEP = "CONTRACT_DEP";
	
	public static final String DEP_NAME = "DEP_NAME";
	
	public static final String CONTRACT_USER = "CONTRACT_USER";
	
	public static final String USER_NAME = "USER_NAME";

	public static final String DEP_ID = "DEP_ID";

	public static final String USER_ID = "USER_ID";
	
	public static final String CORP_CODE = "CORP_CODE";

	public static final String MSG_ID	= "MSG_ID";
	
	public static final String UPDATE_TIME	= "UPDATE_TIME";
	
	public static final String SEQUENCE	= "S_LF_DEDUCTIONS_DISP";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfDeductionsDisp", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("contractid", CONTRACT_ID);
		columns.put("acctno", ACCT_NO);
		columns.put("contractstate", CONTRACT_STATE);
		columns.put("debitaccount", DEBITACCOUNT);
		columns.put("oprstate", OPR_STATE);
		columns.put("corpcode", CORP_CODE);
		columns.put("taocanmoney", TAOCAN_MONEY);
		columns.put("taocanname", TAOCAN_NAME);
		columns.put("customname", CUSTOM_NAME);
		columns.put("depid", DEP_ID);
		columns.put("depname", DEP_NAME);
		columns.put("taocancode", TAOCAN_CODE);
		columns.put("mobile", MOBILE);
		columns.put("deductionsmoney", DEDUCTIONS_MONEY);
		columns.put("id", ID);
		columns.put("oprtime", OPR_TIME);
		columns.put("contractdep", CONTRACT_DEP);
		columns.put("taocantype", TAOCAN_TYPE);
		columns.put("deductionstype", DEDUCTIONS_TYPE);
		columns.put("contractuser", CONTRACT_USER);
		columns.put("username", USER_NAME);
		columns.put("userid", USER_ID);
		columns.put("msgid", MSG_ID);
		columns.put("updatetime", UPDATE_TIME);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

						