package com.montnets.emp.weix.dao.i;

import java.util.List;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.entity.weix.LfWcRtext;
import com.montnets.emp.table.weix.TableLfWcRtext;

public interface IRtextDao 
{

	/**
	 * SELECT * from LF_WC_KEYWORD lfKeyword WHERE ( lfKeyword.A_ID IS NULL OR
	 * lfKeyword.A_ID = '0' OR lfKeyword.A_ID = 1 ) and CORP_CODE = 100001
	 * 根据公众账号查询默认回复
	 * @param acct
	 * @return
	 * @throws Exception
	 */
	public List<LfWcRtext> findDefaultReplyByAccount(LfWcAccount acct) throws Exception;
}
