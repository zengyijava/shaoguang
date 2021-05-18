package com.montnets.emp.netnews.biz;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.netnews.common.FileJsp;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXPAGE;


public class WxManagerBiz extends SuperBiz
{

	/**根据网讯ID 查询 网讯有效时间
	 * @param pageid页面ID
	 * @return
	 */
	public String getPdByTimeOut(int pageid)
	{
		try
		{
			LfWXPAGE page= empDao.findObjectByID(LfWXPAGE.class, pageid);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			List<LfWXBASEINFO> baseInfos  = null;
			if(page!=null)
			{
				conditionMap.put("NETID", String.valueOf(page.getNETID()));
				baseInfos = empDao.findListByCondition(LfWXBASEINFO.class, conditionMap, null);
			}
			if(baseInfos==null)
			{
				return null;
			}
			
			String timeOut =baseInfos.get(0).getTIMEOUT().toString();
			Long netid = baseInfos.get(0).getNETID();
			
			FileJsp.savendpdMap(netid, pageid + "");
			FileJsp.savendtimeMap(netid, timeOut);
			return timeOut;
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"根据网讯ID查询 网讯有效时间异常");
			return null;
		}
		
	}
	
}
