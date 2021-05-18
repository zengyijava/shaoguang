package com.montnets.emp.weix.dao.i;

import java.util.List;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.entity.weix.LfWcKeyword;

public interface IKeywordDao
{
	/**
	 * SELECT * from LF_WC_KEYWORD lfKeyword WHERE ( lfKeyword.A_ID IS NULL OR
	 * lfKeyword.A_ID = '0' OR lfKeyword.A_ID = 1 ) and CORP_CODE = 100001
	 * 根据公众账号查找关键字
	 * @param acct
	 * @return
	 * @throws Exception
	 */
	public List<LfWcKeyword> findKeyWordsByAccount(LfWcAccount acct) throws Exception;
}
