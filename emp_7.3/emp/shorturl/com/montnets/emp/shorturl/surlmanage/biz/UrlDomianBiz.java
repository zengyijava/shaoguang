package com.montnets.emp.shorturl.surlmanage.biz;



import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;

import com.montnets.emp.shorturl.surlmanage.dao.UrlDomainDao;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomain;
import com.montnets.emp.util.PageInfo;

public class UrlDomianBiz extends SuperBiz{
	BaseBiz baseBiz=new BaseBiz();
	
	public List<LfDomain> getDomains(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo){
		if (conditionMap.get("lguserid")==null) {
			return null;
		}
		List<LfDomain > urlList = null;
		try {
			
			urlList = new UrlDomainDao().getDomains(conditionMap,pageInfo);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询url源地址集合biz异常");
		}
		return urlList;
	}
	
	/**
	 * 新增短域名管理
	 * @param lfDomain
	 * @return
	 * @throws Exception
	 */
	public boolean addLfDomain(LfDomain lfDomain) throws Exception
	{

		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			empTransDao.save(conn, lfDomain);
			empTransDao.commitTransaction(conn);

			result = true;
		} catch (Exception e)
		{
			result = false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"添加账户异常！");
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 修改短域名管理
	 * @param lfDomain
	 * @return
	 * @throws Exception
	 */
	public boolean updateLfDomain(LfDomain lfDomain) throws Exception
	{

		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			empTransDao.update(conn, lfDomain);
			empTransDao.commitTransaction(conn);
			result = true;

		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"修改短域名管理异常！");
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	public boolean changeState(Long id) throws Exception
	{
		boolean flag = false;
		if(id == null)
		{
			return false;
		}
		LfDomain domain = null;
		
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		Connection conn = empTransDao.getConnection();
		try
		{	
			conditionMap.put("id", String.valueOf(id));
			domain = baseBiz.getById(LfDomain.class, id);
			if(domain.getFlag() == 0)
			{
				domain.setFlag(1);
			}
			else if(domain.getFlag() == 1)
			{
				domain.setFlag(0);
			}

			empTransDao.beginTransaction(conn);
			empTransDao.update(conn, domain);

			empTransDao.commitTransaction(conn);
			flag = true;
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "更改域名状态成功。");
			throw e;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return flag;
	}
	
}

