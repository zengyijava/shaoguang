package com.montnets.emp.weix.dao;

import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.weix.dao.i.IFocusrepDao;
import com.montnets.emp.table.weix.TableLfWcAccount;
import com.montnets.emp.table.weix.TableLfWcRevent;

public class FocusrepDao extends SuperDAO implements IFocusrepDao
{
	/**
	 * 默认回复查询(分页和不分页两种情况)
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findFocusReply(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
	{
		// 拼SQL语句
		String fieldSql = "SELECT revent.evt_id,revent.msg_text,revent.t_id,revent.evt_type,revent.a_id,revent.corp_code,revent.createtime," + "revent.title,revent.msg_xml,revent.modifytime,account.name accountname,account.code code";

		String tableSql = " from LF_WC_REVENT revent " + StaticValue.getWITHNOLOCK() + " left join LF_WC_ACCOUNT account" + StaticValue.getWITHNOLOCK() + " on revent.a_id=account.a_id ";
		// 查询条件
		StringBuffer conSql = new StringBuffer();
		conSql.append(" where revent.evt_type = 1");
		if(conditionMap != null && !conditionMap.entrySet().isEmpty())
		{

			if(conditionMap.get("corpCode") != null && !"".equals(conditionMap.get("corpCode")))
			{
				conSql.append(" and revent.corp_code='").append(conditionMap.get("corpCode")).append("'");
			}

			if(conditionMap.get("title") != null && !"".equals(conditionMap.get("title")))
			{
				conSql.append(" and revent.title like '%" + conditionMap.get("title") + "%' ");
			}

			if(conditionMap.get("a_id") != null && !"".equals(conditionMap.get("a_id")))
			{
				conSql.append(" and revent.a_id = " + conditionMap.get("a_id"));
			}

			if(conditionMap.get("evt_id") != null && !"".equals(conditionMap.get("evt_id")))
			{
				conSql.append(" and revent.evt_id = " + conditionMap.get("evt_id"));
			}
		}
		// 排序
		String orderbySql = " order by revent.EVT_ID DESC";
		// 返回结果
		String sql = fieldSql + tableSql.toString() + conSql.toString() + orderbySql;

		// 分页和不分页两种情况
		if(pageInfo == null)
		{
			return this.getListDynaBeanBySql(sql);
		}
		else
		{
			String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
			return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
	}
	
	/**
	 * 根据机构编号查找绑定公众账号
	 * @description    
	 * @param corpCode 机构编号
	 * @param status 状态
	 * @return
	 * @throws Exception       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午03:04:54
	 */
	public List<LfWcAccount> findBindAccountByCorpCode(String corpCode, String status) throws Exception
	{
		List<LfWcAccount> acctList = null;
		String filedSql = "select account.* ";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfWcAccount.TABLE_NAME).append(" account ").append(" where");

		StringBuffer conSql = new StringBuffer();
		conSql.append(" account.").append(TableLfWcAccount.CORP_CODE).append("='").append(corpCode).append("'");
		if("0".equals(status))
		{
			conSql.append(" and (account.A_ID").append(" not in ").append('(');
		}
		else
		{
			conSql.append(" and (account.A_ID").append(" in ").append('(');
		}

		conSql.append("select revent.A_ID from " + TableLfWcRevent.TABLE_NAME + " revent where revent.CORP_CODE = " + "'" + corpCode + "'");
		conSql.append(")");
		conSql.append(")");
		conSql.append(" order by CREATETIME DESC");

		String sql = filedSql + tableSql.toString() + conSql.toString();
		acctList = findEntityListBySQL(LfWcAccount.class, sql, StaticValue.EMP_POOLNAME);
		return acctList;
	}
}
