package com.montnets.emp.common.timer.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.timer.entity.LfTimeSerCtrl;

public class TimerHeartbeatDAO extends SuperDAO
{
	private TimerCommSQL commSQL = new TimerCommSQL();
	
	/**
	 * 
	 * @description DAO获取定时服务控制表记录
	 * @return 定时服务控制表记录集合
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-23 下午06:49:32
	 */
	public List<LfTimeSerCtrl> getTimeSerCtrl()
	{
		try
		{
			//获取当前时间sql
			String dateSql = commSQL.getCurrentTimeSql();
			if(dateSql == null)
			{
				EmpExecutionContext.error("DAO获取定时服务控制表记录，获取不到当前时间sql。");
				return null;
			}
			StringBuffer sqlSb = new StringBuffer();
			sqlSb.append("select TimeServerID,UpdateTime,NodeId,ServerIP,ServerPort,").append(dateSql).append(" as dbCurrentTime from LF_TimeSerCtrl").append(StaticValue.getWITHNOLOCK());
			
			List<LfTimeSerCtrl> serCtrlList = findEntityListBySQL(LfTimeSerCtrl.class, sqlSb.toString(), StaticValue.EMP_POOLNAME);
			return serCtrlList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO获取定时服务控制表记录，异常。");
			return null;
		}
	}
	
	/**
	 * 
	 * @description DAO更新定时服务控制表记录
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
			//获取当前时间sql
			String dateSql = commSQL.getCurrentTimeSql();
			if(dateSql == null)
			{
				EmpExecutionContext.error("DAO更新定时服务控制表记录，获取不到当前时间sql。");
				return false;
			}
			StringBuffer sqlSb = new StringBuffer();
			sqlSb.append("update LF_TimeSerCtrl set ").append("UpdateTime=").append(dateSql);
			
			if(updateObj.getTimeServerID() != null && updateObj.getTimeServerID().trim().length() > 0)
			{
				sqlSb.append(",TimeServerID='").append(updateObj.getTimeServerID()).append("'");
			}
			if(updateObj.getNodeId() != null &&  updateObj.getNodeId().length() > 0)
			{
				sqlSb.append(",NodeId='").append(updateObj.getNodeId()).append("'");
			}
			if(updateObj.getServerIP() != null &&  updateObj.getServerIP().length() > 0)
			{
				sqlSb.append(",ServerIP='").append(updateObj.getServerIP()).append("'");
			}
			if(updateObj.getServerPort() != null &&  updateObj.getServerPort() > 0)
			{
				sqlSb.append(",ServerPort=").append(updateObj.getServerPort());
			}
			
			sqlSb.append(" where ").append("TimeServerID='").append(conditionObj.getTimeServerID()).append("'");
			
			boolean res = this.executeBySQL(sqlSb.toString(), StaticValue.EMP_POOLNAME);
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO更新定时服务控制表记录，异常。");
			return false;
		}
	}
	
	
}
