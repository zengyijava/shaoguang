package com.montnets.emp.wxgl.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxgl.dao.AccountDao;

public class AccountBiz extends BaseBiz
{
	/**
	 * 企业公众帐号列表
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> findAllAccountByCorpCode(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		List<DynaBean> accounts = null;
		try
		{
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			return new AccountDao().findAllAccountByCorpCode(corpCode, conditionMap, orderbyMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "公众帐号查询失败！");
		}
		return accounts;
	}

	/**
	 * 更新公众帐号信息
	 * @param objectMap
	 * @param conditionMap
	 * @return1
	 */
	public boolean updateAccount(LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap)
	{
		Connection conn = empTransDao.getConnection();
		boolean result = true;
		try
		{
			empTransDao.update(conn, LfWeiAccount.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			result = false;
			EmpExecutionContext.error(e, "更新公众帐号信息出现错误！");
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
}
