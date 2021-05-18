/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-3-25 上午11:52:04
 */
package com.montnets.emp.wymanage.biz;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.entity.wy.AAreacode;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wymanage.dao.wy_areaCodeManageDAO;

/**
 * @description 
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-3-25 上午11:52:04
 */

public class wy_areaCodeManageBiz
{
	BaseBiz	baseBiz	= new BaseBiz();
	
	wy_areaCodeManageDAO areaCodeManageDAO = new wy_areaCodeManageDAO();
	/**
	 * 
	 * @description    查询国际号码
	 * @param pageInfo  分页
	 * @param conditionMap  查询条件
	 * @return     国际号码列表     	  			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-3-25 下午12:42:56
	 */
	public List<DynaBean> getAreaCode(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap)
	{
		return new wy_areaCodeManageDAO().getAreaCode(pageInfo, conditionMap);
	}
	
	/**
	 * 
	 * @description  是否存在相同的国际区号
	 * @param conditionMap
	 * @return   查询在记录,返回true,否则返回lse    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-3-25 下午05:41:44
	 */
	public boolean isExist(LinkedHashMap< String, String> conditionMap)
	{
		boolean result = false;
		int count = areaCodeManageDAO.getAreaCodeCount(conditionMap);
		if(count > 0)
		{
			result = true;
		}
		return result;
	}
	/**
	 * 获取国家代码字符串
	 */
	public String getAreacodeStrByid(String id)throws Exception
	{
		StringBuffer sb = new StringBuffer();
		// 条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("id&in", id);
//		List<AAreacode> areaCodes = baseBiz.findListByCondition(AAreacode.class, conditionMap, null);
		List<AAreacode> areaCodes = areaCodeManageDAO.findAreaCodeListByCondition(id);
		if (areaCodes!=null&&areaCodes .size()>0 )
		{
			AAreacode areacode = null;
			for (int i = 0; i < areaCodes.size(); i++)
			{
				areacode = areaCodes.get(i);
				sb.append("国家/地区：").append(areacode.getAreaname()).append("，国际区号：").append(areacode.getAreacode()).append("&");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
}
