package com.montnets.emp.weix.biz.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.util.PageInfo;

public interface IUserMgrBiz 
{

	/**
	 * 获取关注用户列表
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return1
	 */
	public List<DynaBean> getUserInfoList(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo);
}
