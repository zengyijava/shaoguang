package com.montnets.emp.monmanage.dao.i;

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
 * @datetime 2013-12-03 上午08:36:21
 */
public interface IGateAcctMonCfgDAO
{

	/**
	 * 获取sp账号监控管理
	 * @description    
	 * @param pageInfo 分页信息
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-12-03 上午08:38:39
	 */
	public List<DynaBean> getGateAcctMonCfg(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap);
	
}
