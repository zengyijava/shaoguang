package com.montnets.emp.wxgl.biz;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxgl.dao.DefaultrepDao;
import com.montnets.emp.wxgl.dao.TempleDao;

public class DefaultrepBiz extends SuperBiz
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
	public List<DynaBean> findDefaltReply(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
	{
		List<DynaBean> beans = null;
		try
		{
			beans = new DefaultrepDao().findDefaltReply(conditionMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "默认回复查询失败！");
		}
		return beans;
	}

	/**
	 * 获取回复模板（默认回复和关注时回复用到）
	 * List<DynaBean>
	 * zm
	 * 
	 * @throws Exception
	 */
	public List<DynaBean> getBaseTempInfos(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
	{
		List<DynaBean> beans = null;
		try
		{
			beans = new TempleDao().getBaseTempInfos(conditionMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取回复模板失败！");
		}
		return beans;
	}
}
