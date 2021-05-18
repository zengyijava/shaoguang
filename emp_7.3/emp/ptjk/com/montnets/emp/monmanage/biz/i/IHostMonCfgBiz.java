package com.montnets.emp.monmanage.biz.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.monitor.constant.MonDhostParams;
import com.montnets.emp.util.PageInfo;

/**
 * 主机监控管理biz接口
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-26 下午04:57:00
 */
public interface IHostMonCfgBiz
{

	/**
	 * 获取主机信息
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin <ljr0300@163.com>
	 * @datetime 2013-11-27 上午09:48:17
	 */
	public List<DynaBean> getHost(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap);
	
	/**
	 * 获取主机动态信息
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin <ljr0300@163.com>
	 * @datetime 2013-11-27 上午09:48:17
	 */
	public List<DynaBean> getDhost(LinkedHashMap< String, String> conditionMap);
	
	/**
	 * 添加主机信息
	 * @description    
	 * @param shost 主机基本信息
	 * @param dhost 主机动态信息
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午03:08:36
	 */
	public boolean addHost(LfMonShost shost,String proceid);
	
	/**
	 * 逻辑删除主机
	 * @description    
	 * @param hostid
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-1-17 下午05:05:11
	 */
	public boolean delete(Long hostid);
	
	public MonDhostParams copyBy(LfMonShost shost);
	
	public void updateBy(MonDhostParams dhost,LfMonShost shost);
}
