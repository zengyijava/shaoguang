package com.montnets.emp.engine.biz;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.engine.LfService;

public class ServiceBiz extends SuperBiz
{
	/**
	 * 通过业务id获取业务对象
	 * @param serId 业务id
	 * @param corpCode 企业编码
	 * @return 返回业务对象
	 */
	public LfService getService(String serId, String corpCode)
	{
		try
		{
			if(serId == null || serId.trim().length() < 1 || corpCode == null || corpCode.trim().length() < 1)
			{
				EmpExecutionContext.error("智能引擎，通过业务id获取业务对象，传入参数为空。serId="+serId+",corpCode="+corpCode);
				return null;
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("serId", serId);
			conditionMap.put("corpCode", corpCode);
			List<LfService> serList = empDao.findListByCondition(LfService.class, conditionMap, null);
			if(serList == null || serList.size() < 1)
			{
				return null;
			}
			return serList.get(0);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "智能引擎，通过业务id获取业务对象，异常。serId="+serId+",corpCode="+corpCode);
			return null;
		}
	}
}
