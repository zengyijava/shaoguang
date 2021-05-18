package com.montnets.emp.common.timer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.timer.TimerStaticValue;
import com.montnets.emp.common.timer.bean.TimeSerProperty;
import com.montnets.emp.common.timer.entity.LfTimeSerState;

public class TimeSerDAO extends SuperDAO
{
	private TimerCommSQL commSQL = new TimerCommSQL();
	
	/**
	 * 
	 * @description DAO保存定时服务状态
	 * @param property 定时服务属性
	 * @return 成功返回true
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-24 下午04:22:41
	 */
	public boolean SaveSerState(TimeSerProperty property)
	{
		//获取当前时间sql
		String dateSql = commSQL.getCurrentTimeSql();
		if(dateSql == null)
		{
			EmpExecutionContext.error("DAO保存定时服务状态，获取不到当前时间sql。");
			return false;
		}
		
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("insert into LF_TimeSerState(TimeServerID,UpdateTime,DealState,NodeId,ServerIP,ServerPort,ServerURL)")
			.append(" values(?,").append(dateSql).append(",?,?,?,?,?) ");
		
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			String sql = sqlSb.toString();
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, property.getTimeServerID());
			ps.setInt(2, property.getDealState());
			ps.setString(3, property.getNodeId());
			ps.setString(4, property.getServerIp());
			ps.setInt(5, property.getServerPort());
			ps.setString(6, property.getServerURL());
			
			int count = ps.executeUpdate();
			if(count > 0)
			{
				return true;
			}
			return false;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO保存定时服务状态，异常。");
			return false;
		} 
		finally
		{
			// 关闭数据库资源。
			try
			{
				close(null, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "DAO保存定时服务状态，关闭数据库资源，异常。");
			}
		}
	}
	
	/**
	 * 
	 * @description 更新定时服务状态表
	 * @param updateObj 更新的对象
	 * @param conditionObj 更新条件
	 * @return 成功返回true
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-24 下午06:24:14
	 */
	public boolean UpdateTimeSerState(TimeSerProperty updateObj, TimeSerProperty conditionObj)
	{
		try
		{
			//获取当前时间sql
			String dateSql = commSQL.getCurrentTimeSql();
			if(dateSql == null)
			{
				EmpExecutionContext.error("DAO更新定时服务状态表，获取不到当前时间sql。");
				return false;
			}
			StringBuffer sqlSb = new StringBuffer();
			sqlSb.append("update LF_TimeSerState set ").append("UpdateTime=").append(dateSql);
			
			if(updateObj.getTimeServerID() != null && updateObj.getTimeServerID().trim().length() > 0)
			{
				sqlSb.append(",TimeServerID='").append(updateObj.getTimeServerID()).append("'");
			}
			if(updateObj.getNodeId() != null &&  updateObj.getNodeId().length() > 0)
			{
				sqlSb.append(",NodeId='").append(updateObj.getNodeId()).append("'");
			}
			if(updateObj.getServerIp() != null &&  updateObj.getServerIp().length() > 0)
			{
				sqlSb.append(",ServerIP='").append(updateObj.getServerIp()).append("'");
			}
			if(updateObj.getServerPort() != null &&  updateObj.getServerPort() > 0)
			{
				sqlSb.append(",ServerPort=").append(updateObj.getServerPort());
			}
			if(updateObj.getServerURL() != null &&  updateObj.getServerURL().length() > 0)
			{
				sqlSb.append(",ServerURL=").append(updateObj.getServerURL());
			}
			if(updateObj.getDealState() != null)
			{
				sqlSb.append(",DealState=").append(updateObj.getDealState());
			}
			
			sqlSb.append(" where ").append("TimeServerID='").append(conditionObj.getTimeServerID()).append("'");
			
			if(conditionObj.getDealState() != null)
			{
				sqlSb.append("and DealState=").append(conditionObj.getDealState());
			}
			
			boolean res = this.executeBySQL(sqlSb.toString(), StaticValue.EMP_POOLNAME);
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO更新定时服务状态表，异常。");
			return false;
		}
	}
	
	/**
	 * 
	 * @description DAO获取定时服务状态表记录
	 * @return       			 
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-24 下午06:40:49
	 */
	public List<LfTimeSerState> getTimeSerState(String timeServerID)
	{
		try
		{
			//获取当前时间sql
			String dateSql = commSQL.getCurrentTimeSql();
			if(dateSql == null)
			{
				EmpExecutionContext.error("DAO获取定时服务状态表记录，获取不到当前时间sql。");
				return null;
			}
			StringBuffer sqlSb = new StringBuffer();
			sqlSb.append("select serState.TimeServerID,serState.UpdateTime,serState.NodeId,serState.ServerIP,serState.ServerPort,")
			.append(dateSql).append(" as dbCurrentTime,serState.ServerURL,serState.DealState,serCtrl.TimeServerID ctrlTimeSerID,serCtrl.UpdateTime ctrlTime")
			.append(" from LF_TimeSerState serState").append(StaticValue.getWITHNOLOCK())
			.append(",LF_TimeSerCtrl serCtrl").append(StaticValue.getWITHNOLOCK())
			.append(" where serState.TimeServerID='").append(timeServerID).append("'");
			
			List<LfTimeSerState> serCtrlList = findEntityListBySQL(LfTimeSerState.class, sqlSb.toString(), StaticValue.EMP_POOLNAME);
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
	 * @description 获取
	 * @return       			 
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-25 下午02:08:57
	 */
	public List<LfTimeSerState> getSerStateRun() throws Exception
	{
		//获取当前时间sql
		String dateSql = commSQL.getCurrentTimeSql();
		if(dateSql == null)
		{
			EmpExecutionContext.error("DAO获取定时服务状态表为处理中的记录，获取不到当前时间sql。");
			return null;
		}
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("select TimeServerID,UpdateTime,NodeId,ServerIP,ServerPort,")
		.append(dateSql).append(" as dbCurrentTime,ServerURL,DealState,null ctrlTimeSerID,"+dateSql+" ctrlTime")
		.append(" from LF_TimeSerState").append(StaticValue.getWITHNOLOCK())
		.append(" where DealState=1 ");
		
		List<LfTimeSerState> serCtrlList = findEntityListBySQL(LfTimeSerState.class, sqlSb.toString(), StaticValue.EMP_POOLNAME);
		return serCtrlList;
	}
	
	/**
	 * 
	 * @description 更新定时服务状态表状态为处理中
	 * @param timeServerID 定时服务ID
	 * @return 成功返回true
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-25 下午06:16:43
	 */
	public boolean UpdateTimeSerStateRun(String timeServerID)
	{
		try
		{
			String sql;
			//获取当前时间sql
			String timeSql;
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				//oracle数据库
				timeSql = "SYSTIMESTAMP";
				sql = "UPDATE LF_TimeSerState serState SET serState.DealState=1,serState.UpdateTime="+timeSql
					+ " WHERE serState.TimeServerID='"+timeServerID 
					+ "' and EXISTS (SELECT serCtrl.TimeServerID FROM lf_timeserctrl serCtrl WHERE serCtrl.TimeServerID='"+timeServerID+"')";
		   	} 
			else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				//sqlserver数据库
				timeSql = "getdate()";
				sql = "update LF_TimeSerState set DealState=1,UpdateTime="+timeSql
					+ " from LF_TimeSerState serState inner join lf_timeserctrl serCtrl on serState.TimeServerID=serCtrl.TimeServerID " 
					+ "where serState.TimeServerID='"+timeServerID+"'";
			}
			else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
			{
				//db2数据库
				timeSql = "current timestamp";
				sql = "UPDATE LF_TimeSerState serState SET serState.DealState=1,serState.UpdateTime="+timeSql
				+ " WHERE serState.TimeServerID='"+timeServerID 
				+ "' and EXISTS (SELECT serCtrl.TimeServerID FROM lf_timeserctrl serCtrl WHERE serCtrl.TimeServerID='"+timeServerID+"')";
			}
			else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
			{
				//mysql数据库
				timeSql = "current_timestamp";
				sql = "update lf_timeserstate serState inner join lf_timeserctrl serCtrl on serState.TimeServerID=serCtrl.TimeServerID " 
					+ "set serState.DealState=1,serState.UpdateTime="+timeSql
					+ " where serState.TimeServerID='"+timeServerID+"'";
			}
			else
			{
				EmpExecutionContext.error("DAO获取当前时间sql，未知的数据库类型。dbType="+StaticValue.DBTYPE);
				return false;
			}
			
			boolean res = this.executeBySQL(sql, StaticValue.EMP_POOLNAME);
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO更新定时服务状态为处理中，异常。");
			return false;
		}
	}
	
	/**
	 * 
	 * @description DAO更新定时服务状态为未处理
	 * @param timeServerID
	 * @return       			 
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-26 下午02:58:35
	 */
	public boolean UpdateTimeSerStateStop(String timeServerID)
	{
		try
		{
			//获取当前时间sql
			String dateSql = commSQL.getCurrentTimeSql();
			if(dateSql == null)
			{
				EmpExecutionContext.error("DAO更新定时服务状态为未处理，获取不到当前时间sql。");
				return false;
			}
			
			String sql = "update lf_timeserstate " 
				+ "set DealState=0,UpdateTime="+dateSql
				+ " where TimeServerID='"+timeServerID+"'";
			
			boolean res = this.executeBySQL(sql, StaticValue.EMP_POOLNAME);
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO更新定时服务状态为未处理，异常。");
			return false;
		}
	}
	
	/**
	 * 
	 * @description DAO删除过期定时服务状态记录
	 * @return 成功返回true
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-28 上午10:55:37
	 */
	public boolean DelTimeSerState()
	{
		try
		{
			//获取当前时间sql
			String timeSql;
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				//oracle数据库
				timeSql = "SYSTIMESTAMP - INTERVAL '"+TimerStaticValue.TIME_SER_DEL_TIME+"' DAY";
			} 
			else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				//sqlserver数据库
				timeSql = "dateadd(day,-"+TimerStaticValue.TIME_SER_DEL_TIME+",getdate())";
			}
			else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
			{
				//db2数据库
				timeSql = "current timestamp-"+TimerStaticValue.TIME_SER_DEL_TIME+" day";
			}
			else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
			{
				//mysql数据库
				timeSql = "date_sub(current_timestamp,interval "+TimerStaticValue.TIME_SER_DEL_TIME+" day)";
			}
			else
			{
				EmpExecutionContext.error("DAO删除过期定时服务状态记录，未知的数据库类型。dbType="+StaticValue.DBTYPE);
				return false;
			}
			
			//无处理权和失效的，删除掉
			String sql = "delete from lf_timeserstate where DealState = 0 and updatetime<"+timeSql;
			boolean res = this.executeBySQL(sql, StaticValue.EMP_POOLNAME);
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO删除过期定时服务状态记录，异常。");
			return false;
		}
	}
	
	/**
	 * 获取SP账号告警信息
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-14 上午10:09:37
	 */
	public List<DynaBean> getSpFeeAlarmList()
	{
		try
		{
			String sql = "SELECT U.USERID AS SPUSER, U.SENDNUM, U.THRESHOLD, S.NOTICENAME, S.ALARMPHONE, S.ALARMEDCOUNT,S.CORP_CODE AS CORPCODE " +
					"FROM USERFEE U INNER JOIN LF_SPFEEALARM S ON S.SPUSER=U.USERID WHERE S.ALARMEDCOUNT=0 AND U.SENDNUM < U.THRESHOLD " +
					"ORDER BY S.SPUSER DESC";
			return getListDynaBeanBySql(sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取所有未告警的SP账号余额告警信息失败。");
			return null;
		}
	}
	
	/**
	 * 设置SP账号告警次数，为告警次数为0做为条件更新
	 * @description    
	 * @param spUser SP账号
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-14 下午06:14:11
	 */
	public boolean setSpUserAlarmCount(String spUser)
	{
		try
		{
			String sql = "UPDATE LF_SPFEEALARM SET ALARMEDCOUNT = 1 WHERE SPUSER = '"+spUser+"' AND ALARMEDCOUNT = 0";
			return executeBySQL(sql, StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置SP账号余额阀值告警次数失败，spUser:"+spUser);
			return false;
		}
	}
}
