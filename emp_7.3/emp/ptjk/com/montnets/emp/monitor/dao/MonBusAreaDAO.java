package com.montnets.emp.monitor.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.monitor.LfBusareasend;
import com.montnets.emp.monitor.dao.i.IMonBusAreaDAO;
import com.montnets.emp.monitor.vo.MonBusAreaInfoVo;

public class MonBusAreaDAO extends SuperDAO implements IMonBusAreaDAO
{

	/**
	 * 获取业务区域监控信息
	 * 
	 * @description
	 * @param lastTime
	 *        最后一次处理时间
	 * @param TimestampStart
	 *        当前时间
	 * @param hour
	 *        当前时间（小时）
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-26 下午04:48:27
	 */
	public List<MonBusAreaInfoVo> getBusAreaInfo(int lastTime, String TimestampStart, int hour)
	{
		try
		{
			//查询语句
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT BB.BUS_NAME,BB.BUS_CODE,BB.AREA_CODE,BB.MONPHONE,BB.MONEMAIL, BB.BEGIN_TIME, BB.END_TIME, BD.*")
			.append(" FROM LF_MON_BUSBASE BB ")		
			.append("LEFT JOIN ").append("LF_MON_BUSDATA BD ON BB.ID=BD.BUSBASE_ID ").append("WHERE ")
			.append("BB.MON_STATE = 1 AND BB.BEGIN_TIME <= ").append(TimestampStart)
			.append(" AND BB.END_TIME >= ").append(TimestampStart).append(" AND BD.END_HOUR = ").append(hour)
			.append(" AND BD.MON_LASTTIME < ").append(lastTime);
			//查询结果
			List<MonBusAreaInfoVo> monBusAreaList = findVoListBySQL(MonBusAreaInfoVo.class, sql.toString(), StaticValue.EMP_POOLNAME);
			return monBusAreaList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取业务区域监控信息DAO失败！");
			return null;
		}
	}

	/**
	 * 通过业务编码获取当天业务区域发送数据
	 * 
	 * @description
	 * @param busCodes
	 *        业务编码
	 * @param TimestampStart
	 *        查询开始时间
	 * @param TimestampEnd
	 *        查询结束时间
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-26 下午05:31:22
	 */
	public List<LfBusareasend> getBusAreaSend(String busCodes, int TimestampStart)
	{
		try
		{
			//查询语句
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM LF_BUSAREASEND WHERE DATA_DATE = ")
			.append(TimestampStart).append(" AND BUS_CODE IN(").append(busCodes).append(")");
			//查询结果
			List<LfBusareasend> busareasendList = findEntityListBySQL(LfBusareasend.class, sql.toString(), StaticValue.EMP_POOLNAME);
			return busareasendList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取当天业务区域发送数据DAO异常！busCodes:" + busCodes + "，TimestampStart:" + TimestampStart);
			return null;
		}
	}
	
	/**
	 * 根据消息时间获取业务区域数据
	 * @description    
	 * @param messageDates 消息时间
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-2 下午04:37:39
	 */
	public List<LfBusareasend> getBusAreaSendByDate(String messageDates)
	{
		try
		{
			//查询语句
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM LF_BUSAREASEND WHERE DATA_DATE IN(").append(messageDates).append(")");
			//查询结果
			List<LfBusareasend> busareasendList = findEntityListBySQL(LfBusareasend.class, sql.toString(), StaticValue.EMP_POOLNAME);
			return busareasendList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据消息时间获取业务区域数据DAO异常！messageDates:" + messageDates);
			return null;
		}
	}
	
	/**
	 * 更新业务区域短信告警状态
	 * @description    
	 * @param id
	 * @param lastTime       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-24 下午01:58:33
	 */
	public boolean updateMonState(long id, Integer lastTime)
	{
		try
		{
			String sql = "UPDATE LF_MON_BUSDATA SET MON_LASTTIME = "+lastTime+" WHERE ID = " +id+ " AND MON_LASTTIME <> " +lastTime;
			return executeBySQL(sql, StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新业务区域短信告警状态异常！id:"+id+"，lastTime:"+lastTime);
			return false;
		}
	}
}
