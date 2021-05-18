package com.montnets.emp.appmage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.appmage.TableLfAppAccount;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

public class app_acctManagerDao extends SuperDAO
{
	/**
	 * 根据条件获取企业公众帐号列表
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
		String fieldSql = "SELECT lfAccount.*,lfuser.name as username,lfuser.user_name as lfname";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfAppAccount.TABLE_NAME).append(" lfAccount, ").append(TableLfSysuser.TABLE_NAME).append(" lfuser ").append(" WHERE lfAccount.creater=lfuser.user_id ");
		// 查询条件
		StringBuffer conSql = new StringBuffer();
		conSql.append(" and lfAccount.").append(TableLfAppAccount.CORP_CODE).append("='").append(corpCode).append("'");
		if(conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			if(conditionMap.get("name") != null && !"".equals(conditionMap.get("name")))
			{
				conSql.append(" and lfAccount." + TableLfAppAccount.NAME + " like '%" + conditionMap.get("name") + "%' ");
			}
			if(conditionMap.get("code") != null && !"".equals(conditionMap.get("code")))
			{
				conSql.append(" and lfAccount." + TableLfAppAccount.FAKE_ID + " like '%" + conditionMap.get("code") + "%' ");
			}
			if(conditionMap.get("account") != null && !"".equals(conditionMap.get("account")))
			{
				conSql.append(" and lfAccount." + TableLfAppAccount.CODE + " like '%" + conditionMap.get("account") + "%' ");
			}
			if(conditionMap.get("username") != null && !"".equals(conditionMap.get("username")))
			{
				conSql.append(" and lfuser.name like '%" + conditionMap.get("username") + "%' ");
			}
		}
		// 排序
		String orderbySql = " order by lfAccount.CREATETIME DESC";
		// 返回结果
		String sql = fieldSql + tableSql.toString() + conSql.toString() + orderbySql;
		String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	}
	
	/***
	 * 查询数据总共记录数
	 * @param corpCode 公司编码
	 * @return
	 */
	public int getRecordCount(String corpCode){
		// 拼SQL语句
		int count=0;
		String fieldSql = "SELECT lfAccount.*";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfAppAccount.TABLE_NAME).append(" lfAccount, ").append(TableLfSysuser.TABLE_NAME).append(" lfuser ").append(" WHERE lfAccount.creater=lfuser.user_id ");
		// 查询条件
		StringBuffer conSql = new StringBuffer();
		conSql.append(" and lfAccount.").append(TableLfAppAccount.CORP_CODE).append("='").append(corpCode).append("'");
		String countSql = fieldSql +  tableSql.toString() + conSql.toString();
		List<DynaBean> returnList = null;
		 returnList = this.getListDynaBeanBySql(countSql.toString());
		 if(returnList!=null){
			 count=returnList.size();
		 }
		 return count;
	}

}
