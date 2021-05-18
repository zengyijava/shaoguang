package com.montnets.emp.weix.biz.i;

import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.util.PageInfo;

public interface IMsgBiz
{
	/**
	 * 上行历史消息
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> findListMsgByCondition(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo);
}
