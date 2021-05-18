package com.montnets.emp.mondetails.biz.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.util.PageInfo;

/**
 * 告警历史biz接口
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-2 上午11:06:36
 */
public interface IHisMonBiz
{

	/**
	 * 告警历史监控详情查询
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-12-2 上午11:09:52
	 */
	public List<DynaBean> getErrMon(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap);
}
