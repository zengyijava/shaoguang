package com.montnets.emp.jfcx.vo.view;

import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.ydyw.TableLfDeductionsDisp;

import java.util.HashMap;
import java.util.Map;

public class ViewCrmBillQueryVo 
{
	protected static final Map<String , String> columns = new HashMap<String, String>();
	
	static
	{
		columns.put("mobile", TableLfDeductionsDisp.MOBILE);
		columns.put("customname", TableLfDeductionsDisp.CUSTOM_NAME);
		columns.put("contractid", TableLfDeductionsDisp.CONTRACT_ID);
		columns.put("acctno", TableLfDeductionsDisp.ACCT_NO);
		columns.put("debitaccount", TableLfDeductionsDisp.DEBITACCOUNT);
		columns.put("taocanname", TableLfDeductionsDisp.TAOCAN_NAME);
		columns.put("taocantype", TableLfDeductionsDisp.TAOCAN_TYPE);
		columns.put("taocancode", TableLfDeductionsDisp.TAOCAN_CODE);
		columns.put("taocanmoney", TableLfDeductionsDisp.TAOCAN_MONEY);
		columns.put("contractstate", TableLfDeductionsDisp.CONTRACT_STATE);
		columns.put("deductionsmoney", TableLfDeductionsDisp.DEDUCTIONS_MONEY);
		columns.put("deductionsmoney", TableLfDeductionsDisp.DEDUCTIONS_MONEY);
		columns.put("oprtime", TableLfDeductionsDisp.OPR_TIME);
		columns.put("oprstate", TableLfDeductionsDisp.OPR_STATE);
		columns.put("depname", TableLfDep.DEP_NAME);
	}
	
	public static Map<String , String> getORM()
	{
		return columns;
	}
}
