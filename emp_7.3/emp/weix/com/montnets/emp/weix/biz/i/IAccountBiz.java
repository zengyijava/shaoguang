package com.montnets.emp.weix.biz.i;


import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.util.PageInfo;

public interface IAccountBiz 
{
	/**
	 * 企业公众帐号列表
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> findAllAccountByCorpCode(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo);

	/**
	 * 更新公众帐号信息
	 * @param objectMap
	 * @param conditionMap
	 * @return1
	 */
	public boolean updateAccount(LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap);
}
