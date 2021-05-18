package com.montnets.emp.wxgl.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.wxgl.TableLfWeiAccount;
import com.montnets.emp.util.PageInfo;

public class AccountDao extends SuperDAO
{
	/**
	 * 根据条件获取企业微信公众帐号列表
	 * 
	 * @param conditionMap
	 *        查询条件
	 * @param orderbyMap
	 *        排序条件
	 * @param pageInfo
	 *        分页信息，无需分析时传入null
	 * @return 微信公众帐号的集合
	 * @throws Exception
	 */
	public List<DynaBean> findAllAccountByCorpCode(String corpCode, LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo) throws Exception
	{
		// 拼SQL语句
		String fieldSql = "SELECT lfAccount.*";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfWeiAccount.TABLE_NAME).append(" lfAccount " + StaticValue.getWITHNOLOCK() + " ").append(" WHERE  ");
		// 查询条件
		StringBuffer conSql = new StringBuffer();
		conSql.append(" lfAccount.").append(TableLfWeiAccount.CORP_CODE).append("='").append(corpCode).append("'");
		if(conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			if(conditionMap.get("name") != null && !"".equals(conditionMap.get("name")))
			{
				conSql.append(" and lfAccount." + TableLfWeiAccount.NAME + " like '%" + conditionMap.get("name") + "%' ");
			}
			if(conditionMap.get("ghname") != null && !"".equals(conditionMap.get("ghname")))
			{
				conSql.append(" and lfAccount." + TableLfWeiAccount.OPEN_ID + " like '%" + conditionMap.get("ghname") + "%' ");
			}
		}
		// 排序
		String orderbySql = " order by lfAccount.CREATETIME DESC";
		// 返回结果
		String sql = fieldSql + tableSql.toString() + conSql.toString() + orderbySql;
		String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	}

}
