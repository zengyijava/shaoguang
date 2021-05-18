package com.montnets.emp.znly.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiRtext;
import com.montnets.emp.table.wxgl.TableLfWeiRtext;

public class RtextDao extends SuperDAO
{

	/**
	 * SELECT * from LF_WEI_KEYWORD lfKeyword WHERE ( lfKeyword.A_ID IS NULL OR
	 * lfKeyword.A_ID = '0' OR lfKeyword.A_ID = 1 ) and CORP_CODE = 100001
	 * 根据公众账号查询默认回复
	 * @param acct
	 * @return
	 * @throws Exception
	 */
	public List<LfWeiRtext> findDefaultReplyByAccount(LfWeiAccount acct) throws Exception
	{
		List<LfWeiRtext> rtextList = null;
		String filedSql = "select lfRtext.* ";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfWeiRtext.TABLE_NAME).append(" lfRtext ");

		StringBuffer conSql = new StringBuffer();
		conSql.append(" where (lfRtext.A_ID").append("=").append(String.valueOf(acct.getAId()));
		conSql.append(" OR lfRtext.A_ID = 0)");
		conSql.append(" and lfRtext.CORP_CODE").append("='").append(acct.getCorpCode()).append("'");
		conSql.append(" order by CREATETIME DESC");
		String sql = filedSql + tableSql.toString() + conSql.toString();

		rtextList = findEntityListBySQL(LfWeiRtext.class, sql, StaticValue.EMP_POOLNAME);
		return rtextList;
	}
}
