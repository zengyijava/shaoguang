package com.montnets.emp.monmanage.dao.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.util.PageInfo;
/**
 * 程序监控管理dao接口
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-27 上午08:36:21
 */
public interface IHostMonCfgDAO
{

	/**
	 * 获取主机信息
	 * @description    
	 * @param pageInfo 分页信息
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getHost(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap);
	
	/**
	 * 获取主机动态信息
	 * @description    
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getDhost(LinkedHashMap<String, String> conditionMap);
}
