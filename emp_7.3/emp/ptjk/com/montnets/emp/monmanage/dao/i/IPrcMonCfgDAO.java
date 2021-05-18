package com.montnets.emp.monmanage.dao.i;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.util.PageInfo;
/**
 * 主机监控管理接口
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-27 上午08:36:21
 */
public interface IPrcMonCfgDAO
{

	/**
	 * 获取程序信息
	 * @description    
	 * @param pageInfo 分页信息
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getSprc(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap);
	
	
	/**
	 * 获取主机名称及id信息
	 * @description    
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getHost();
	
	/**
	 * 获取mq的配置信息
	 * @description    
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-1-17 下午08:21:55
	 */
	public List<DynaBean> getMqMsg();
	
	public void deleteErr(Connection conn,String hostid) throws SQLException;
}
