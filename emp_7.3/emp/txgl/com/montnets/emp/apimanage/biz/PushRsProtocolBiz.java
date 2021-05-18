package com.montnets.emp.apimanage.biz;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.servmodule.txgl.entity.GwPushRsProtocol;
import com.montnets.emp.util.PageInfo;

public class PushRsProtocolBiz extends SuperBiz
{
	 /**
	  * 查询API推送用户回应
	  * @param conditionMap
	  * @param orderbyMap
	  * @param pageInfo
	  * @return
	  */
	public List<GwPushRsProtocol> getGwPushRsProtocol(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String, String> orderbyMap,PageInfo pageInfo) 
	{
		try 
		{
			 return empDao.findPageListBySymbolsCondition(null, GwPushRsProtocol.class, conditionMap, orderbyMap, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询API推送用户回应管理失败！");
			return null;
		}
		
	}
	
	 /**
	  * 查询API推送用户回应
	  * @return
	  */
	public List<DynaBean> getGwPushRsProtocolList(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception
	{
		String sql = "select gprp.*,corp.corp_name  corp_name from GW_PUSHRSPROTOCOL gprp left join LF_CORP corp on gprp.ecid= corp.corp_code where 1=1 ";
		String condition ="";
		
        String crspfmt = conditionMap.get("crspfmt");
        if(crspfmt!=null&&!"".equals(crspfmt))
        {
        	condition += " and  gprp.crspfmt ="+crspfmt+" ";
        }
        
        String rspStatus = conditionMap.get("rspStatus");
        if(rspStatus!=null&&!"".equals(rspStatus))
        {
        	condition += " and  gprp.rspStatus = "+rspStatus+" ";
        }
        
        String rspCmd = conditionMap.get("rspCmd");
        if(rspCmd!=null&&!"".equals(rspCmd))
        {
        	condition += " and  gprp.rspCmd = "+rspCmd+" ";
        }
		
		String cargName = conditionMap.get("cargName");
        if(cargName!=null&&!"".equals(cargName))
        {
        	condition += " and  gprp.cargname like '%"+cargName+"%' ";
        }
        
		 String cargValue = conditionMap.get("cargValue");
	     if(cargValue!=null&&!"".equals(cargValue))
	     {
	        condition += " and  gprp.cargvalue like '%"+cargValue+"%' ";
	     }
	     
		 String corpName = conditionMap.get("corpName");
	     if(corpName!=null&&!"".equals(corpName))
	     {
	        condition += " and  corp.corp_name like '%"+corpName+"%' ";
	     }
        
		
		//记录数sql拼接
		String countSql = new StringBuffer(
				"select count(*) totalcount from (").append(sql)
				.append(")  total ").toString();

		sql=sql+condition+" order by gprp.id asc ";
		
        return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME,null);

	}
}
