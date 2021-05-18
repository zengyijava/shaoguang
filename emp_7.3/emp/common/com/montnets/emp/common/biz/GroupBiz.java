package com.montnets.emp.common.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.GroupDAO;

/**
 * @description 群组
 * @project emp_std_183
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-7-25 上午10:51:27
 */

public class GroupBiz extends SuperBiz
{
	/**
	 * 根据群组类型和群组ID集合，查询出群组的成员数量
	 * @param udgIds 群组ID集合，以逗号隔开的
	 * @param groupType 群组类型 1是员工群组  2是客户群组
	 * @param corpCode 企业编码
	 * @return 返回值是一个MAP  key群组ID value群组对应的成员数量
	 */
	public Map<String,String> getGroupMemberCount(String udgIds, int groupType, String corpCode)
	{
		//定义一个Map  key群组ID value群组对应的成员数量
		HashMap<String, String> countMap = new HashMap<String, String>();
		try
		{
			//查询出一个动态bean
			List<DynaBean>  groupMenberCountList = new GroupDAO().getGroupMemberCount(udgIds, groupType, corpCode);
			//动态bean不为空并且动态bean的size大于0，则进行循环，否则countMap就为空的map了。
			if(groupMenberCountList != null && groupMenberCountList.size()>0)
			{
				for(DynaBean groupMenberCount:groupMenberCountList)
				{
					//key为群组ID,Value为群组成员数量
					countMap.put(String.valueOf(groupMenberCount.get("groupid")),String.valueOf(groupMenberCount.get("membercount")));
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询出群组的成员数量异常！udgIds:" + udgIds + "，groupType:" + groupType);
		}
		return countMap;
	}
}
