package com.montnets.emp.monmanage.biz.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.util.PageInfo;

/**
 * 通道账号监控管理biz接口
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-03 下午04:57:00
 */
public interface IGateAcctMonCfgBiz
{

	/**
	 * 获取通道账号监控管理
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin <ljr0300@163.com>
	 * @datetime 2013-12-03 下午04:58:00
	 */
	public List<DynaBean> getGateAcctMonCfg(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap);
	
	/**
	 * 设置通道账号阀值信息
	 * @description    
	 * @param objectMap 修改map
	 * @param spaccountid sp账号
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午03:08:36
	 */
	public boolean editGateAcct(LinkedHashMap<String, String> objectMap,String gateaccount);
	
	/**
	 * 检查告警手机号码是否合法
	 * @description    
	 * @param monPhone
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-3-31 下午05:13:07
	 */
	public String checkPhone(String monPhone);
}
