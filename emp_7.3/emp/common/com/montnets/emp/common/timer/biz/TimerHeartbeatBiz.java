package com.montnets.emp.common.timer.biz;

import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.dao.TimerHeartbeatDAO;
import com.montnets.emp.common.timer.entity.LfTimeSerCtrl;

public class TimerHeartbeatBiz extends SuperBiz
{
	private TimerHeartbeatDAO heartbeatDAO = new TimerHeartbeatDAO();
	
	/**
	 * 
	 * @description 获取定时服务控制记录
	 * @return 定时服务控制记录对象
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-23 下午05:57:25
	 */
	public LfTimeSerCtrl getTimeSerCtrl()
	{
		try
		{
			List<LfTimeSerCtrl> serCtrlList = heartbeatDAO.getTimeSerCtrl();
			if(serCtrlList == null || serCtrlList.size() < 1)
			{
				EmpExecutionContext.error("获取定时服务控制记录，无记录。");
				return null;
			}
			return serCtrlList.get(0);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取定时服务控制记录，异常。");
			return null;
		}
	}
	
	/**
	 * 
	 * @description 更新定时服务控制表记录
	 * @param updateObj 更新的对象
	 * @param conditionObj 更新条件
	 * @return 成功返回true
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-23 下午07:31:06
	 */
	public boolean UpdateTimeSerCtrl(LfTimeSerCtrl updateObj, LfTimeSerCtrl conditionObj)
	{
		try
		{
			if(updateObj == null || conditionObj == null)
			{
				EmpExecutionContext.error("更新定时服务控制表记录，传入参数为空。");
				return false;
			}
			else if(conditionObj.getTimeServerID() == null || conditionObj.getTimeServerID().trim().length() < 1)
			{
				EmpExecutionContext.error("更新定时服务控制表记录，更新条件TimeServerID为空。。");
				return false;
			}
			boolean res = heartbeatDAO.UpdateTimeSerCtrl(updateObj, conditionObj);
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新定时服务控制表记录，异常。");
			return false;
		}
	}
}
