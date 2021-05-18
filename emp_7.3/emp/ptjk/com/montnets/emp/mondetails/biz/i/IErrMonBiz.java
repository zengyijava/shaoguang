package com.montnets.emp.mondetails.biz.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.util.PageInfo;

/**
 * 实时告警监控详情biz接口
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-2 上午11:06:36
 */
public interface IErrMonBiz
{

	/**
	 * 实时告警监控详情查询
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-12-2 上午11:09:52
	 */
	public List<DynaBean> getErrMon(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap);
	
	/**
	 * 告警处理
	 * @description    
	 * @param userid 用户名
	 * @param id 告警信息id
	 * @param dealflag 处理状态
	 * @param dealdesc 描述
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-1-12 下午04:46:10
	 */
	public boolean dealMon(String username,String ids,String dealflag, String dealdesc);
}
