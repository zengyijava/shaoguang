package com.montnets.emp.pasgroup.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.pasgroup.dao.ProxyMageDao;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.pasgroup.entity.Userdata;
import com.montnets.emp.util.PageInfo;

public class ProxyMageBiz extends SuperBiz{

	ErrorLoger errorLoger = new ErrorLoger();
	public List<Userdata> findSpUser(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception{
		List<Userdata> userdatas = null;
		try
		{
			userdatas = new ProxyMageDao().findSpUser(conditionMap,
					pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找账户异常！"));
			throw e;
		}
		return userdatas;
	}

	/**
	 * 通过账号获取SP账户信息
	 * @description    
	 * @param userid
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-9-9 上午08:46:27
	 */
	public boolean getUserdataByUserid(String userid) throws Exception
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//以账号为条件查询
			conditionMap.put("userId", userid);
			List<Userdata> tempList = empDao.findListByCondition(
					Userdata.class, conditionMap, null);
			//查询到记录，返回TRUE
			if (tempList != null && tempList.size() > 0)
			{
				return true;
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通过账号获取SP账户信息异常！userid:"+userid));
			throw e;
		}
		return false;
	}
	/**
	 * 添加sp账户
	 * @description    
	 * @param user userdata实体类
	 * @param useIP 用户ip
	 * @param lfSpFee 运营商余额表（多企业时才会设值）
	 * @return
	 * @throws Exception       			 
	 * @author zhangmin
	 * @datetime 2013-10-29 下午01:50:56
	 */
	public boolean addUserdata(Userdata user, String useIP) throws Exception
	{

		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			empTransDao.save(conn, user);
			empTransDao.commitTransaction(conn);

			result = true;
		} catch (Exception e)
		{
			result = false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"添加账户异常！"));
			throw e;
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param userdata
	 * @return
	 * @throws Exception
	 */
	public boolean updateUserdata(Userdata userdata) throws Exception
	{

		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", userdata.getUserId());
			empTransDao.update(conn, userdata);

			empTransDao.commitTransaction(conn);
			result = true;

		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"修改账户异常！"));
			throw e;
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 通过账号获取SP账户信息
	 * @description    
	 * @param userid
	 * @return
	 * @throws Exception       			 
	 */
	public Userdata getUserdataByMap(LinkedHashMap<String, String> conditionMap)
	{
		Userdata userdate=new Userdata();
		try
		{
			//以账号为条件查询
			List<Userdata> tempList = empDao.findListByCondition(
					Userdata.class, conditionMap, null);
			//查询到记录，返回TRUE
			if (tempList != null && tempList.size() > 0)
			{
				userdate=tempList.get(0);
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通过账号获取账户信息异常!"));
		}
		return userdate;
	}
	
}
