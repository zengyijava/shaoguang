package com.montnets.emp.znly.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiKeyword;
import com.montnets.emp.table.wxgl.TableLfWeiKeyword;

public class KeywordDao extends SuperDAO
{

	/**
	 * SELECT * from LF_WEI_KEYWORD lfKeyword WHERE ( lfKeyword.A_ID IS NULL OR
	 * lfKeyword.A_ID = '0' OR lfKeyword.A_ID = 1 ) and CORP_CODE = 100001
	 * 根据公众账号查找关键字
	 * @param acct
	 * @return
	 * @throws Exception
	 */
	public List<LfWeiKeyword> findKeyWordsByAccount(LfWeiAccount acct) throws Exception
	{
		List<LfWeiKeyword> keywordsList = null;
		String filedSql = "select lfKeyword.* ";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfWeiKeyword.TABLE_NAME).append(" lfKeyword ");

		StringBuffer conSql = new StringBuffer();
		conSql.append(" where (lfKeyword.A_ID").append("=").append(String.valueOf(acct.getAId()));
		conSql.append(" OR lfKeyword.A_ID = 0)");
		conSql.append(" and lfKeyword.CORP_CODE").append("='").append(acct.getCorpCode()).append("'");
		conSql.append(" order by CREATETIME DESC");
		String sql = filedSql + tableSql.toString() + conSql.toString();

		keywordsList = findEntityListBySQL(LfWeiKeyword.class, sql, StaticValue.EMP_POOLNAME);
		return keywordsList;
	}
}
