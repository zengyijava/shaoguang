package com.montnets.emp.apimanage.biz;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.servmodule.txgl.entity.GwBasePara;
import com.montnets.emp.util.PageInfo;

public class ApiParaBiz extends SuperBiz
{
	 /**
	  * API接口参数管理
	  * @param conditionMap
	  * @param orderbyMap
	  * @param pageInfo
	  * @return
	  */
	public List<GwBasePara> getGwBasePara(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String, String> orderbyMap,PageInfo pageInfo) 
	{
		try 
		{
			 return empDao.findPageListBySymbolsCondition(null, GwBasePara.class, conditionMap, orderbyMap, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询API接口参数管理失败！");
			return null;
		}
		
	}
}
