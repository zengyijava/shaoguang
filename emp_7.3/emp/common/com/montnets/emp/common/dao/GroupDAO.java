package com.montnets.emp.common.dao;

import java.util.List;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;

public class GroupDAO extends SuperDAO
{
	/**
	 * 根据群组的ID和群组的类型查询群组的成员数量
	 * @param udgIds 群组ID
	 * @param type 群组类型 1是员工群组 2是客户群组
	 * @return 返回动态Bean membercount 群组成员数量  groupid群组ID
	 */
	public List<DynaBean> getGroupMemberCount(String udgIds,int type, String corpCode){
		List<DynaBean> countBeans =null;
		try
		{
			String sql="";
			if(type==1){
                sql = "select count(L2G_ID) membercount,UDG_ID groupid from LF_LIST2GRO where UDG_ID in ("+udgIds+")" +
                        " and L2G_TYPE in (0,2) group by UDG_ID";
			}else if(type==2){
                sql = "select count(L2G_ID) membercount,UDG_ID groupid from LF_LIST2GRO where UDG_ID in ("+udgIds+")" +
                        " and L2G_TYPE in (1,2) group by UDG_ID";
			}else{
				return null;
			}
			countBeans=getListDynaBeanBySql(sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "类型查询群组的成员数量异常!");
		}
		return countBeans;
	}
}
