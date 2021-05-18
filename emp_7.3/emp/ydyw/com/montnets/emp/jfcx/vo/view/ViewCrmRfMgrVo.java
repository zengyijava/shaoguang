package com.montnets.emp.jfcx.vo.view;

import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.ydyw.TableLfDeductionsList;

import java.util.HashMap;
import java.util.Map;

public class ViewCrmRfMgrVo {

	protected static final Map<String , String> columns = new HashMap<String, String>();
	
	static
	{
		columns.put("id", TableLfDeductionsList.ID);
		columns.put("mobile", TableLfDeductionsList.MOBILE);
		columns.put("customname", TableLfDeductionsList.CUSTOM_NAME);
		columns.put("contractid", TableLfDeductionsList.CONTRACT_ID);
		columns.put("acctno", TableLfDeductionsList.ACCT_NO);
		columns.put("debitaccount", TableLfDeductionsList.DEBITACCOUNT);
		columns.put("taocantype", TableLfDeductionsList.TAOCAN_TYPE);
		columns.put("taocanname", TableLfDeductionsList.TAOCAN_NAME);
		columns.put("taocancode", TableLfDeductionsList.TAOCAN_CODE);
		columns.put("taocanmoney", TableLfDeductionsList.TAOCAN_MONEY);
		columns.put("deductionstype", TableLfDeductionsList.DEDUCTIONS_TYPE);
		columns.put("contractstate", TableLfDeductionsList.CONTRACT_STATE);
		columns.put("depId", TableLfDeductionsList.DEP_ID);
		columns.put("depname", TableLfDep.DEP_NAME);
		columns.put("updatetime", TableLfDeductionsList.UDPATE_TIME);
		columns.put("bucklefeetime", TableLfDeductionsList.BUCKLEFEE_TIME);
		columns.put("buckleupfeetime", TableLfDeductionsList.BUCKLEUPFEE_TIME);
		columns.put("username",TableLfSysuser.USER_NAME);
		columns.put("name",TableLfSysuser.NAME);
		columns.put("bupsummoney",TableLfDeductionsList.BUPSUM_MONEY);
	}
	
	public static Map<String , String> getORM()
	{
		return columns;
	}
}
