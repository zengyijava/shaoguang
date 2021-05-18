package com.montnets.emp.weix.biz.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.util.PageInfo;

public interface IDefaultrepBiz
{
	/**
	 * 默认回复列表及其查询
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findDefaltReply(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception;

	/**
	 * 获取回复模板（默认回复和关注时回复用到）
	 * List<DynaBean>
	 * zm
	 * 
	 * @throws Exception
	 */
	public List<DynaBean> getBaseTempInfos(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception;
}
