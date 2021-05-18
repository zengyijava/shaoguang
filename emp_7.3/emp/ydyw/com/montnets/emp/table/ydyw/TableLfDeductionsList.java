package com.montnets.emp.table.ydyw;

import java.util.HashMap;
import java.util.Map;

public class TableLfDeductionsList
{
	public static final String TABLE_NAME	= "LF_DEDUCTIONS_LIST";
	
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
	
	public static final String IYEAR = "IYEAR";
	
	public static final String IMONTH = "IMONTH";
	
	public static final String BUPSUM_MONEY = "BUPSUM_MONEY";
	
	public static final String BUPTIMER = "BUPTIMER";
	
	public static final String CONTRACT_STATE = "CONTRACT_STATE";
	
	public static final String CONTRACT_TYPE = "CONTRACT_TYPE";
	
	public static final String DEDUCTIONS_TYPE = "DEDUCTIONS_TYPE";
	
	public static final String BUCKUP_TIMER = "BUCKUP_TIMER";
	
	public static final String UPDATE_TIME = "UPDATE_TIME";
	
	public static final String CONTRACT_USER = "CONTRACT_USER";

	public static final String CONTRACT_DEP = "CONTRACT_DEP";

	public static final String BUCKLEFEE_TIME = "BUCKLEFEE_TIME";

	public static final String CORP_CODE = "CORP_CODE";

	public static final String UDPATE_TIME = "UDPATE_TIME";

	public static final String DEP_ID = "DEP_ID";

	public static final String DEP_NAME = "DEP_NAME";

	public static final String BUCKLEUPFEE_TIME = "BUCKLEUPFEE_TIME";

	public static final String SEQUENCE	= "S_LF_DEDUCTIONS_LIST";
	
	public static final String USER_ID	= "USER_ID";
	
	public static final String MSG_ID	= "MSG_ID";
	
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfDeductionsList", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("contractuser", CONTRACT_USER);
		columns.put("contractstate", CONTRACT_STATE);
		columns.put("debitaccount", DEBITACCOUNT);
		columns.put("taocanmoney", TAOCAN_MONEY);
		columns.put("contracttype", CONTRACT_TYPE);
		columns.put("customname", CUSTOM_NAME);
		columns.put("contractdep", CONTRACT_DEP);
		columns.put("taocantype", TAOCAN_TYPE);
		columns.put("bucklefeetime", BUCKLEFEE_TIME);
		columns.put("imonth", IMONTH);
		columns.put("contractid", CONTRACT_ID);
		columns.put("iyear", IYEAR);
		columns.put("buckuptimer", BUCKUP_TIMER);
		columns.put("acctno", ACCT_NO);
		columns.put("corpcode", CORP_CODE);
		columns.put("buptimer", BUPTIMER);
		columns.put("udpatetime", UDPATE_TIME);
		columns.put("taocanname", TAOCAN_NAME);
		columns.put("bupsummoney", BUPSUM_MONEY);
		columns.put("depid", DEP_ID);
		columns.put("updatetime", UPDATE_TIME);
		columns.put("depname", DEP_NAME);
		columns.put("taocancode", TAOCAN_CODE);
		columns.put("mobile", MOBILE);
		columns.put("deductionstype", DEDUCTIONS_TYPE);
		columns.put("buckleupfeetime", BUCKLEUPFEE_TIME);
		columns.put("userid", USER_ID);
		columns.put("msgid", MSG_ID);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

						