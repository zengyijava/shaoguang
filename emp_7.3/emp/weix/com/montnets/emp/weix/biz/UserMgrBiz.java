package com.montnets.emp.weix.biz;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.weix.biz.i.IUserMgrBiz;
import com.montnets.emp.weix.dao.UserMgrDao;

public class UserMgrBiz extends SuperBiz implements IUserMgrBiz
{

	/**
	 * 获取关注用户列表
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return1
	 */
	public List<DynaBean> getUserInfoList(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		List<DynaBean> beans = null;
		try
		{
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			return new UserMgrDao().getUserInfoList(corpCode, conditionMap, orderbyMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取关注用户列表失败！");
		}
		return beans;
	}
}
