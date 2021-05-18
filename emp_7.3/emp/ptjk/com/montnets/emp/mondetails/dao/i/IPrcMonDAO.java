package com.montnets.emp.mondetails.dao.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.util.PageInfo;
/**
 * 程序监控详情dao接口
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-30 下午07:26:17
 */
public interface IPrcMonDAO
{

	/**
	 * 获取程序监控详情
	 * @description    
	 * @param pageInfo 分页信息
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getPrcMon(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap);
	
	/**
	 * 获取用户名和机构名称
	 * @return
	 */
	public List<DynaBean> getUserAndDep();
}
