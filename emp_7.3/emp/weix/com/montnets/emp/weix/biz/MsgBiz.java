package com.montnets.emp.weix.biz;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.weix.biz.i.IMsgBiz;
import com.montnets.emp.weix.dao.MsgDao;

public class MsgBiz extends SuperBiz implements IMsgBiz
{
	/**
	 * 上行历史消息
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> findListMsgByCondition(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		List<DynaBean> msgs = null;
		try
		{
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			return new MsgDao().findListMsgByCondition(corpCode, conditionMap, orderbyMap, pageInfo);
		}
		catch (SQLException e)
		{
			try
			{
				return new MsgDao().findListMsgDefault(corpCode, conditionMap, null, pageInfo);
			}
			catch (Exception e1)
			{
				EmpExecutionContext.error(e1, "获取上行历史消息记录失败！");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取上行历史消息记录失败！");
		}
		return msgs;
	}
}
