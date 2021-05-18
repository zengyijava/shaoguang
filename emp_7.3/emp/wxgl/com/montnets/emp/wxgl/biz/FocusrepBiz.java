package com.montnets.emp.wxgl.biz;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxgl.dao.FocusrepDao;
import com.montnets.emp.wxgl.dao.TempleDao;

public class FocusrepBiz extends SuperBiz
{

	private TempleDao	temDao		= null;

	private FocusrepDao	focusrepDao	= null;

	public FocusrepBiz()
	{
		temDao = new TempleDao();
		focusrepDao = new FocusrepDao();
	}

	/**
	 * 关注时回复列表及其查询
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findFocusReply(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
	{
		List<DynaBean> beans = null;
		try
		{
			beans = new FocusrepDao().findFocusReply(conditionMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "关注时回复查询失败！");
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
			beans = temDao.getBaseTempInfos(conditionMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取回复模板失败！");
		}
		return beans;
	}

	/**
	 * 获取已绑定关注时回复的公众帐号
	 * 
	 * @param corpCode
	 * @param status
	 * @return
	 * @throws Exception1
	 */
	public List<LfWeiAccount> findBindAccountByCorpCode(String corpCode, String status) throws Exception
	{
		List<LfWeiAccount> acctList = null;
		try
		{
			acctList = focusrepDao.findBindAccountByCorpCode(corpCode, status);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取已绑定关注时回复的公众帐号！");
		}
		return acctList;
	}
}
