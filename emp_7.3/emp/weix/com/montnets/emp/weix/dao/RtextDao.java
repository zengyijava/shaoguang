package com.montnets.emp.weix.dao;

import java.util.List;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.entity.weix.LfWcRtext;
import com.montnets.emp.table.weix.TableLfWcRtext;
import com.montnets.emp.weix.dao.i.IRtextDao;

public class RtextDao extends SuperDAO implements IRtextDao
{

	/**
	 * SELECT * from LF_WC_KEYWORD lfKeyword WHERE ( lfKeyword.A_ID IS NULL OR
	 * lfKeyword.A_ID = '0' OR lfKeyword.A_ID = 1 ) and CORP_CODE = 100001
	 * 根据公众账号查询默认回复
	 * @param acct
	 * @return
	 * @throws Exception
	 */
	public List<LfWcRtext> findDefaultReplyByAccount(LfWcAccount acct) throws Exception
	{
		List<LfWcRtext> rtextList = null;
		String filedSql = "select lfRtext.* ";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfWcRtext.TABLE_NAME).append(" lfRtext ").append(" where");

		StringBuffer conSql = new StringBuffer();
		conSql.append(" (lfRtext.A_ID").append("=").append(String.valueOf(acct.getAId()));
		conSql.append(" OR lfRtext.A_ID = 0)");
		conSql.append(" and lfRtext.CORP_CODE").append("='").append(acct.getCorpCode()).append("'");
		conSql.append(" order by CREATETIME DESC");
		String sql = filedSql + tableSql.toString() + conSql.toString();

		rtextList = findEntityListBySQL(LfWcRtext.class, sql, StaticValue.EMP_POOLNAME);
		return rtextList;
	}
}
