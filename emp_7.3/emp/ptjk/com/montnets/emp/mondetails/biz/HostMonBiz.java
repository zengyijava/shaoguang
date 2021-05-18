package com.montnets.emp.mondetails.biz;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.mondetails.biz.i.IHostMonBiz;
import com.montnets.emp.mondetails.dao.HostMonDAO;
import com.montnets.emp.util.PageInfo;

/**
 * 主机监控详情biz
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-2 上午11:06:36
 */
public class HostMonBiz extends SuperBiz implements IHostMonBiz
{

	/**
	 * 主机监控详情查询
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-12-2 上午11:09:52
	 */
	public List<DynaBean> getHostMon(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap)
	{
		
		return new HostMonDAO().getHostMon(pageInfo, conditionMap);
	}
}
