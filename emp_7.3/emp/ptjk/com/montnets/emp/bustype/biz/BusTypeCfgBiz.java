package com.montnets.emp.bustype.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.bustype.biz.i.IBusTypeCfgBiz;
import com.montnets.emp.bustype.dao.BusTypeCfgDAO;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.monitor.LfMonBusbase;
import com.montnets.emp.entity.monitor.LfMonBusdata;
import com.montnets.emp.util.PageInfo;

public class BusTypeCfgBiz extends SuperBiz implements IBusTypeCfgBiz
{

	BusTypeCfgDAO	dao	= new BusTypeCfgDAO();

	/**
	 * 获得业务列表
	 * 
	 * @Description: TODO
	 * @param @param conditionMap
	 * @param @return
	 * @param @throws Exception
	 * @return List<LfBusManager>
	 */
	public List<LfBusManager> getLfBusManager(LinkedHashMap<String, String> conditionMap) throws Exception
	{
		return empDao.findListByCondition(LfBusManager.class, conditionMap, null);
	}

	/**
	 * 获得业务监控基础信息表
	 * 
	 * @Description: TODO
	 * @param @param conditionMap
	 * @param @return
	 * @param @throws Exception
	 * @return List<LfMonBusbase>
	 */
	public List<LfMonBusbase> getLfMonBusbase(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
	{
		return dao.getLfMonBusbase(conditionMap, pageInfo);
	}

	/**
	 * 获得业务监控数据信息表
	 * 
	 * @Description: TODO
	 * @param @param conditionMap
	 * @param @return
	 * @param @throws Exception
	 * @return List<LfMonBusbase>
	 */
	public List<LfMonBusdata> getLfMonBusdata(LinkedHashMap<String, String> conditionMap) throws Exception
	{
		return empDao.findListByCondition(LfMonBusdata.class, conditionMap, null);
	}

	/**
	 * 保存业务监控基础信息与业务监控数据信息
	 * 
	 * @Description: TODO
	 * @param @param base 业务监控基础信息
	 * @param @param list 业务监控数据信息
	 * @param @return
	 * @return boolean 返回是否成功
	 */
	public boolean save(LfMonBusbase base, List<LfMonBusdata> list)
	{
		boolean ret = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			long baseid = empTransDao.saveObjectReturnID(conn, base);
			for (int k = 0; k < list.size(); k++)
			{
				list.get(k).setBusbaseid(baseid);
			}
			int num = empTransDao.save(conn, list, LfMonBusdata.class);
			empTransDao.commitTransaction(conn);
			if(num > 0)
			{
				ret = true;
			}
		}
		catch (Exception e)
		{
			ret = false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, " 保存业务监控基础信息失败");
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return ret;
	}

	/**
	 * 修改业务监控基础信息与业务监控数据信息
	 * 
	 * @Description: TODO
	 * @param @param base 业务监控基础信息
	 * @param @param list 业务监控数据信息
	 * @param @return
	 * @return boolean 返回是否成功
	 */
	public boolean update(LfMonBusbase base, List<LfMonBusdata> list)
	{
		boolean ret = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			empTransDao.update(conn, base);
			// 先删除子表信息，然后增加信息
			LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
			condition.put("corpcode", base.getCorpcode());
			condition.put("busbaseid", base.getId() + "");
			int del = empTransDao.delete(conn, LfMonBusdata.class, condition);
			if(del > 0)
			{
				for (int k = 0; k < list.size(); k++)
				{
					list.get(k).setBusbaseid(base.getId());
				}
				int num = empTransDao.save(conn, list, LfMonBusdata.class);
				empTransDao.commitTransaction(conn);
				if(num > 0)
				{
					ret = true;
				}
			}
		}
		catch (Exception e)
		{
			ret = false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, " 修改业务监控基础信息失败");
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return ret;
	}

	/**
	 * 获得省份信息
	 * 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getProvinceList(String code)
	{

		return dao.getProvinceList(code);
	}

	/***
	 * 单个删除
	 * 
	 * @Description: TODO
	 * @param @param id 传入业务id
	 * @param @param lgcorpcode 公司编码
	 * @param @return
	 * @return boolean
	 */
	public boolean delete(String id, String lgcorpcode)
	{
		boolean result = false;
		Connection conn = empTransDao.getConnection();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			empTransDao.beginTransaction(conn);
			conditionMap.put("id", id);
			conditionMap.put("corpcode", lgcorpcode);
			empTransDao.delete(conn, LfMonBusbase.class, conditionMap);
			conditionMap.clear();
			conditionMap.put("busbaseid", id);
			conditionMap.put("corpcode", lgcorpcode);
			int num = empTransDao.delete(conn, LfMonBusdata.class, conditionMap);
			empTransDao.commitTransaction(conn);
			if(num > 0)
			{
				result = true;
			}
		}
		catch (Exception e)
		{
			result = false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, " 删除业务监控基础信息失败");
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}

		return result;
	}

	/***
	 * 修改监控基础信息状态
	 * 
	 * @Description: TODO
	 * @param @param id 传入业务id
	 * @param @param lgcorpcode 公司编码
	 * @return boolean
	 */
	public boolean changeStatus(String id, String status, String lgcorpcode)
	{
		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			LinkedHashMap<String, Object> conditionMap = new LinkedHashMap<String, Object>();
			conditionMap.put("monstate", Integer.parseInt(status));
			conditionMap.put("corpcode", lgcorpcode);
			empTransDao.update(conn, LfMonBusbase.class, conditionMap, id);
		}
		catch (Exception e)
		{
			result = false;
			EmpExecutionContext.error(e, " 修改监控基础信息状态失败");
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}

}
