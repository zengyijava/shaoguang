package com.montnets.emp.weix.dao.i;

import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.util.PageInfo;

public interface IAccountDao
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
	public List<DynaBean> findAllAccountByCorpCode(String corpCode, LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo) throws Exception;

}
