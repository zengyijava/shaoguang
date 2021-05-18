package com.montnets.emp.monmanage.biz.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.util.PageInfo;

/**
 * sp账号监控管理接口
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-03 下午04:57:00
 */
public interface ISpAcctMonCfgBiz
{

	/**
	 * 获取sp账号监控管理
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin <ljr0300@163.com>
	 * @datetime 2013-12-03 下午04:58:00
	 */
	public List<DynaBean> getspAcctMonCfg(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap);
	
	/**
	 * 设置sp账号阀值信息
	 * @description    
	 * @param objectMap 修改map
	 * @param spaccountid sp账号
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午03:08:36
	 */
	public boolean editSpAcct(LinkedHashMap<String, String> objectMap,String spaccountid);
}
