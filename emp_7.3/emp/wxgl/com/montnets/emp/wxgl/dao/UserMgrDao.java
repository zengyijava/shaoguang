package com.montnets.emp.wxgl.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.wxgl.TableLfWeiUserInfo;
import com.montnets.emp.util.PageInfo;

/**
 * @author chensj
 */
public class UserMgrDao
{
	/**
	 * 查找关注用户
	 * @description    
	 * @param corpCode
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午03:14:30
	 */
	public List<DynaBean> getUserInfoList(String corpCode, LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
	{

		List<DynaBean> beans = null;
		String fieldSql = "SELECT lfuser.WC_ID as wcid,lfuser.NAME as username,lfuser.UNAME as uname," + "lfuser.PHONE as phone,lfuser.CREATETIME as createtime,lfuser.VERIFYTIME as verifytime," + "lfuser.OPEN_ID as openid ";

		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfWeiUserInfo.TABLE_NAME).append(" lfuser ");
		tableSql.append(" WHERE  ");

		StringBuffer conSql = new StringBuffer();
		conSql.append(" lfuser.").append(TableLfWeiUserInfo.CORP_CODE).append("='").append(corpCode).append("'");

		String conditionSql = getConditionSql(conditionMap);
		String orderbySql = " order by lfuser.WC_ID DESC";
		String sql = fieldSql + tableSql + conSql.toString() + conditionSql + orderbySql;
		String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql).append(conSql.toString()).append(conditionSql).toString();
		beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return beans;
	}

	/**
	 * 拼装查询条件
	 * @description    
	 * @param conditionMap
	 * @return       			 
	 * @author Administrator <foyoto@gmail.com>
	 * @datetime 2013-12-10 下午04:37:00
	 */
	public String getConditionSql(LinkedHashMap<String, String> conditionMap)
	{
		StringBuffer conditionSql = new StringBuffer();
		// 开始时间
		String beginTime = conditionMap.get("beginTime");
		// 结束时间
		String endTime = conditionMap.get("endTime");
		// 当前数据库类型
		String type = SystemGlobals.getValue("DBType");

		if(conditionMap.get("wcid") != null && !"".equals(conditionMap.get("wcid")))
		{
			conditionSql.append(" and lfuser.WC_ID" + "=" + conditionMap.get("wcid"));
		}
		if(conditionMap.get("phone") != null && !"".equals(conditionMap.get("phone")))
		{
			conditionSql.append(" and lfuser.PHONE"+ " like '%" + conditionMap.get("phone") + "%' ");
		}
		if(conditionMap.get("uname") != null && !"".equals(conditionMap.get("uname")))
		{
			conditionSql.append(" and lfuser." + TableLfWeiUserInfo.UNAME + " like '%" + conditionMap.get("uname") + "%'");
		}

		if("1".equals(type))
		{
			// oracle
			if(beginTime != null && !"".equals(beginTime))
			{
				conditionSql.append(" and lfuser.VERIFYTIME " + ">= to_date('" + beginTime + "','yyyy-MM-dd HH24:MI:SS')");
			}
			if(endTime != null && !"".equals(endTime))
			{
				conditionSql.append(" and lfuser.VERIFYTIME " + "<= to_date('" + endTime + "','yyyy-MM-dd HH24:MI:SS')");
			}
		}
		else
		{
			// sql server ,db2
			if(beginTime != null && !"".equals(beginTime))
			{
				conditionSql.append(" and lfuser.VERIFYTIME " + ">= '" + beginTime + "'");
			}
			if(endTime != null && !"".equals(endTime))
			{
				conditionSql.append(" and lfuser.VERIFYTIME " + "<= '" + endTime + "'");
			}
		}

		return conditionSql.toString();
	}

}
