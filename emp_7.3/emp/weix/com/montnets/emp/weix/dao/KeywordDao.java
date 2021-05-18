package com.montnets.emp.weix.dao;

import java.util.List;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.entity.weix.LfWcKeyword;
import com.montnets.emp.table.weix.TableLfWcKeyword;
import com.montnets.emp.weix.dao.i.IKeywordDao;

public class KeywordDao extends SuperDAO implements IKeywordDao
{

	/**
	 * SELECT * from LF_WC_KEYWORD lfKeyword WHERE ( lfKeyword.A_ID IS NULL OR
	 * lfKeyword.A_ID = '0' OR lfKeyword.A_ID = 1 ) and CORP_CODE = 100001
	 * 根据公众账号查找关键字
	 * @param acct
	 * @return
	 * @throws Exception
	 */
	public List<LfWcKeyword> findKeyWordsByAccount(LfWcAccount acct) throws Exception
	{
		List<LfWcKeyword> keywordsList = null;
		String filedSql = "select lfKeyword.* ";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfWcKeyword.TABLE_NAME).append(" lfKeyword ").append(" where ");

		StringBuffer conSql = new StringBuffer();
		conSql.append("(lfKeyword.A_ID").append("=").append(String.valueOf(acct.getAId()));
		conSql.append(" OR lfKeyword.A_ID = 0)");
		conSql.append(" and lfKeyword.CORP_CODE").append("='").append(acct.getCorpCode()).append("'");
		conSql.append(" order by CREATETIME DESC");
		String sql = filedSql + tableSql.toString() + conSql.toString();

		keywordsList = findEntityListBySQL(LfWcKeyword.class, sql, StaticValue.EMP_POOLNAME);
		return keywordsList;
	}
}
