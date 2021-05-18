package com.montnets.emp.monitor.biz;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.monitor.LfBusareasend;
import com.montnets.emp.monitor.constant.MonBusAreaParams;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.monitor.dao.MonBusAreaDAO;

public class MonBusAreaSendBiz extends SuperBiz implements Runnable
{
	MonBusAreaDAO	monBusAreaDAO	= new MonBusAreaDAO();

	BaseBiz				baseBiz				= new BaseBiz();
	
	public void run()
	{
		while(true)
		{
			// 间隔2分钟
			try
			{
				Thread.sleep(2 * 60 * 1000L);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "定时更新业务区域发送数据线程设置间隔时间异常！");
			}
			try
			{
				//缓存数据总数
				int count = MonitorStaticValue.getMon_BusAreaMap().size();
				// 缓内中有数据
				if(count > 0)
				{
					Map<Integer, Map<String, MonBusAreaParams>> mon_BusArea = new ConcurrentHashMap<Integer, Map<String, MonBusAreaParams>>();
					// 从缓存中获取业务区域发送数据
//					mon_BusArea.putAll(MonitorStaticValue.mon_BusAreaMap);
					// 清空缓存对象
					MonitorStaticValue.getMon_BusAreaMap().clear();
					// 获取消息时间
					StringBuffer messageDates = new StringBuffer();
					// 获取所有消息时间
					for (Map.Entry<Integer, Map<String, MonBusAreaParams>> entry : mon_BusArea.entrySet())
					{
						// 消息时间
						messageDates.append(entry.getKey()).append(",");
					}
					if(messageDates.length() > 1)
					{
						// 去掉最后一个逗号
						messageDates.deleteCharAt(messageDates.lastIndexOf(","));
						// 根据消息时间从表中获取业务区域数据
						List<LfBusareasend> busareasendList = this.getBusAreaSendByDate(messageDates.toString());
						// 保存业务区域发送数据
						saveBusAreaSend(busareasendList, mon_BusArea);
					}
					EmpExecutionContext.info("定时更新业务区域发送数据，共处理缓存总数："+count+"，时间对象："+messageDates);
				}
				else
				{
					EmpExecutionContext.info("定时更新业务区域发送数据，缓存中无数据！");
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "定时更新业务区域发送数据失败！");
			}
		}
	}

	/**
	 * 根据消息时间获取业务区域数据
	 * 
	 * @description
	 * @param messageDates
	 *        消息时间
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-2 下午04:51:11
	 */
	private List<LfBusareasend> getBusAreaSendByDate(String messageDates)
	{
		// 根据消息时间获取业务区域数据
		return monBusAreaDAO.getBusAreaSendByDate(messageDates);
	}

	/**
	 * 保存业务区域发送数据
	 * 
	 * @description
	 * @param busareasendList
	 *        当天已存在的发送数据
	 * @param mon_BusArea
	 *        缓存数据
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-2 下午06:46:11
	 */
	private void saveBusAreaSend(List<LfBusareasend> busareasendList, Map<Integer, Map<String, MonBusAreaParams>> mon_BusArea)
	{
		// 业务编码
		String busCode = "";
		// 发送总数
		String mtSendCount = "";
		// 区域
		String areaCode = "";
		// 消息时间
		Integer dataDate;
		// 数据库是否已存在当天数据 true:存在；false:不存在
		boolean isExist = false;
		// 发送数据JSON格式
		JSONObject json = null;
		// 消息时间KEY
		Integer monBusAreaKey;
		// 消息时间VALUE
		Map<String, MonBusAreaParams> monBusArea = new HashMap<String, MonBusAreaParams>();
		// 业务编码、区域编码KEY
		String monBusAreaParamskey;
		// 业务编码、区域编码VALUE
		MonBusAreaParams monBusAreaParams = new MonBusAreaParams();
		// 新增数据集合
		List<LfBusareasend> addList = new ArrayList<LfBusareasend>();
		// 业务区域发送对象
		LfBusareasend busareasend = null;
		try
		{
			for (Map.Entry<Integer, Map<String, MonBusAreaParams>> monBusAreaMap : mon_BusArea.entrySet())
			{
				// 消息时间KEY
				monBusAreaKey = monBusAreaMap.getKey();
				// 消息时间VALUE
				monBusArea = monBusAreaMap.getValue();
				for (Map.Entry<String, MonBusAreaParams> monBusAreaList : monBusArea.entrySet())
				{
					// 业务编码、区域编码KEY
					monBusAreaParamskey = monBusAreaList.getKey();
					// 业务编码、区域编码VALUE
					monBusAreaParams = monBusAreaList.getValue();
					if(busareasendList != null && busareasendList.size() > 0)
					{
						for (LfBusareasend lfBusareasend : busareasendList)
						{
							// 业务编码
							busCode = lfBusareasend.getBuscode();
							// 区域编码
							areaCode = lfBusareasend.getAreacode();
							// 消息时间
							dataDate = lfBusareasend.getDatadate();
							// 存在当天数据则进行更新
							if(monBusAreaParamskey.equals(busCode + "&" + areaCode) && monBusAreaKey.equals(dataDate))
							{
								// 发送记录
								mtSendCount = lfBusareasend.getMtsendcount();
								json = new JSONObject(mtSendCount);
								// 时间段数据不存在，更新记录
								if(!json.has(String.valueOf(monBusAreaParams.getHour())))
								{
									// 更新数据库
									updateBusAreaSend(lfBusareasend, json, monBusAreaParams);
								}
								// 设置状态为存在
								isExist = true;
								break;
							}
						}
					}
					// 不存在当天数据则新增
					if(!isExist)
					{
						busareasend = new LfBusareasend();
						busareasend.setBusname(" ");
						busareasend.setBuscode(monBusAreaParams.getBusCode());
						busareasend.setAreacode(monBusAreaParams.getAreaCode());
						json = new JSONObject();
						json.put(String.valueOf(monBusAreaParams.getHour()), String.valueOf(monBusAreaParams.getMtHaveSnd()));
						busareasend.setMtsendcount(json.toString());
						busareasend.setCorpcode(" ");
						busareasend.setUpdatetime(new Timestamp(System.currentTimeMillis()));
						busareasend.setDatadate(monBusAreaParams.getMessageDate());
						addList.add(busareasend);
					}
					// 重置状态为不存在
					isExist = false;
				}
			}
			// 批量更新
			if(addList.size() > 0)
			{
				empDao.save(addList, LfBusareasend.class);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "保存业务区域发送数据失败！");
		}
	}
	
	/**
	 * 更新业务区域发送数据
	 * @description    
	 * @param lfBusareasend
	 * @param json
	 * @param monBusAreaParams       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-3 上午10:11:14
	 */
	private void updateBusAreaSend(LfBusareasend lfBusareasend, JSONObject json, MonBusAreaParams monBusAreaParams)
	{
		try
		{
			LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
			json.put(String.valueOf(monBusAreaParams.getHour()), String.valueOf(monBusAreaParams.getMtHaveSnd()));
			objectMap.put("mtsendcount", json.toString());
			objectMap.put("updatetime", new Timestamp(System.currentTimeMillis()));
			empDao.update(LfBusareasend.class, objectMap, String.valueOf(lfBusareasend.getId()));
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新业务区域发送数据异常！ID:" + lfBusareasend.getId());
		}
	}
}
