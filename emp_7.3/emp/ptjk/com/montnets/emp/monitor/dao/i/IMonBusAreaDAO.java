package com.montnets.emp.monitor.dao.i;

import java.util.List;

import com.montnets.emp.entity.monitor.LfBusareasend;
import com.montnets.emp.monitor.vo.MonBusAreaInfoVo;

public interface IMonBusAreaDAO
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
	public List<MonBusAreaInfoVo> getBusAreaInfo(int lastTime, String TimestampStart, int hour);

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
	public List<LfBusareasend> getBusAreaSend(String busCodes, int TimestampStart);
	
	/**
	 * 根据消息时间获取业务区域数据
	 * @description    
	 * @param messageDates 消息时间
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-2 下午04:37:39
	 */
	public List<LfBusareasend> getBusAreaSendByDate(String messageDates);
	
	/**
	 * 更新业务区域短信告警状态
	 * @description    
	 * @param id
	 * @param lastTime       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-24 下午01:58:33
	 */
	public boolean updateMonState(long id, Integer lastTime);
}
