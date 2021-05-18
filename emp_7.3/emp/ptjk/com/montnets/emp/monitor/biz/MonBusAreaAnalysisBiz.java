package com.montnets.emp.monitor.biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.monitor.LfBusareasend;
import com.montnets.emp.entity.monitor.LfMonBusinfo;
import com.montnets.emp.monitor.constant.MonEmailParams;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.monitor.dao.MonBusAreaDAO;
import com.montnets.emp.monitor.vo.MonBusAreaInfoVo;
import com.montnets.emp.security.blacklist.BlackListAtom;

public class MonBusAreaAnalysisBiz extends Thread
{

	BaseBiz				baseBiz				= new BaseBiz();

	MonBusAreaDAO		monBusAreaDAO		= new MonBusAreaDAO();

	BlackListAtom			blackListAtom	= new BlackListAtom();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	Timestamp curDbServerTime;
	private String corpcode ="100001";
	
	public MonBusAreaAnalysisBiz()
	{
		this.setName("业务发送监控数据分析线程ID"+this.getId());
	}
	
	public void run()
	{
		EmpExecutionContext.info("启动"+this.getName());
		while(MonitorStaticValue.isMonThreadState())
		{
			try
			{
				Thread.sleep(3*60*1000L);
				EmpExecutionContext.info("业务发送监控数据分析开始，线程ID:"+this.getId());
				long start = System.currentTimeMillis();
				// 执行业务监控数据分析
				busAreaDataAnalysis();
				EmpExecutionContext.info("业务发送监控数据分析结束，线程ID:"+this.getId()+"，耗时："+(System.currentTimeMillis() - start)+"ms");
			}
			catch (InterruptedException e)
			{
				if(MonitorStaticValue.isMonThreadState())
				{
					EmpExecutionContext.error(e, "业务发送监控数据分析线程异常！");
				}
				Thread.currentThread().interrupt();
			}
		}
		EmpExecutionContext.info("停止"+this.getName());
	}

	/**
	 * 业务监控数据分析
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-27 下午04:27:42
	 */
	public void busAreaDataAnalysis()
	{
		try
		{
			// 获取业务区域监控信息
			List<MonBusAreaInfoVo> monBusAreaList = this.getBusAreaInfo();
			// 存在业务监控数据
			if(monBusAreaList != null && monBusAreaList.size() > 0)
			{
				// 查询业务区域发送信息
				Map<String, LfBusareasend> busareasendMap = getBusAreaSendByBusCodeMap(monBusAreaList);
				if(busareasendMap != null && busareasendMap.size() > 0)
				{
					// 业务监控数据分析处理
					dataAnalysis(monBusAreaList, busareasendMap);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "业务监控数据分析异常！");
		}
	}

	/**
	 * 获取业务区域监控信息
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-26 下午04:49:52
	 */
	public List<MonBusAreaInfoVo> getBusAreaInfo()
	{
		try
		{
			Calendar calendar = Calendar.getInstance();
			// 当前时间
			long timeMillis = calendar.getTimeInMillis();
			// 最后一次处理时间格式
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			// 最后一次处理时间
			int lastTime = Integer.valueOf(format.format(timeMillis));
			// 当前时间格式
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 当前时间
			String TimestampStart = new DataAccessDriver().getGenericDAO().getTimeCondition(format.format(timeMillis));
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			//当前为0点，设置小时为24
			if(hour == 0)
			{
				hour = 24;
			}
			return monBusAreaDAO.getBusAreaInfo(lastTime, TimestampStart, hour);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取业务区域监控信息失败！");
			return null;
		}
	}

/**
	 * 根据需要监控的业务编码获取业务区域发送记录
	 * 
	 * @description
	 * @param monBusAreaList
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-27 下午04:32:59
	 */
	private List<LfBusareasend> getBusAreaSendByBusCode(List<MonBusAreaInfoVo> monBusAreaList)
	{
		try
		{
			// 业务编码
			String busCode = "";
			// 业务编码集合
			StringBuffer busCodes = new StringBuffer("");
			Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			//当前为0点，获取昨天的数据
			if(hour == 0)
			{
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
			// 当前时间
			long timeMillis = calendar.getTimeInMillis();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			// 当天开始时间
			int TimestampStart = Integer.parseInt(format.format(timeMillis));
			//查询条件的业务编码
			String conditionBusCode = "";
			// 拼接所有需要监控的业务编码
			for (MonBusAreaInfoVo monBusArea : monBusAreaList)
			{
				// 获取业务编码
				busCode = monBusArea.getBusCode();

				if(busCode != null && busCode.trim().length() > 0)
				{
					//查询条件的业务编码
					conditionBusCode = "'" + busCode + "'";
					//查询条件不存在此业务编码
					if(busCodes.indexOf(conditionBusCode) < 0)
					{
						// 拼接
						busCodes.append(conditionBusCode).append(",");
					}
				}
				conditionBusCode = "";
			}
			if(busCodes.length() > 0)
			{
				// 截掉最后一个逗号
				busCodes.deleteCharAt(busCodes.length() - 1);
				// 获取当天业务区域发送数据
				List<LfBusareasend> busareasendList = monBusAreaDAO.getBusAreaSend(busCodes.toString(), TimestampStart);
				if(busareasendList == null || busareasendList.size() < 1)
				{
					EmpExecutionContext.error("根据需要监控的业务编码获取业务区域发送记录失败！业务编码" + busCodes.toString()+"无数据");
				}
				return busareasendList;
			}
			else
			{
				EmpExecutionContext.error("根据需要监控的业务编码获取业务区域发送记录失败，业务编码获取为空！");
				return null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据需要监控的业务编码获取业务区域发送记录异常！");
			return null;
		}
	}
	
	/**
	 * 根据需要监控的业务编码获取业务区域发送记录
	 * 
	 * @description
	 * @param monBusAreaList
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-27 下午04:32:59
	 */
	private Map<String, LfBusareasend> getBusAreaSendByBusCodeMap(List<MonBusAreaInfoVo> monBusAreaList)
	{
		try
		{
			// 业务编码
			String busCode = "";
			// 业务编码集合
			StringBuffer busCodes = new StringBuffer("");
			Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			//当前为0点，获取昨天的数据
			if(hour == 0)
			{
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
			// 当前时间
			long timeMillis = calendar.getTimeInMillis();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			// 当天开始时间
			int TimestampStart = Integer.parseInt(format.format(timeMillis));
			//查询条件的业务编码
			String conditionBusCode = "";
			// 拼接所有需要监控的业务编码
			for (MonBusAreaInfoVo monBusArea : monBusAreaList)
			{
				// 获取业务编码
				busCode = monBusArea.getBusCode();

				if(busCode != null && busCode.trim().length() > 0)
				{
					//查询条件的业务编码
					conditionBusCode = "'" + busCode + "'";
					//查询条件不存在此业务编码
					if(busCodes.indexOf(conditionBusCode) < 0)
					{
						// 拼接
						busCodes.append(conditionBusCode).append(",");
					}
				}
				conditionBusCode = "";
			}
			if(busCodes.length() > 0)
			{
				// 截掉最后一个逗号
				busCodes.deleteCharAt(busCodes.length() - 1);
				// 获取当天业务区域发送数据
				List<LfBusareasend> busareasendList = monBusAreaDAO.getBusAreaSend(busCodes.toString(), TimestampStart);
				if(busareasendList == null || busareasendList.size() < 1)
				{
					EmpExecutionContext.error("根据需要监控的业务编码获取业务区域发送记录失败！业务编码" + busCodes.toString()+"无数据");
				}
				//设置业务区域发送数据并返回
				return setBusareasend(busareasendList);
			}
			else
			{
				EmpExecutionContext.error("根据需要监控的业务编码获取业务区域发送记录失败，业务编码获取为空！");
				return null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据需要监控的业务编码获取业务区域发送记录异常！");
			return null;
		}
	}

	/**
	 * 设置业务区域发送数据
	 * @description    
	 * @param busareasendList
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-27 下午03:01:16
	 */
	public Map<String, LfBusareasend> setBusareasend(List<LfBusareasend> busareasendList)
	{
		try
		{
			//	业务区域发送数据对象
			Map<String, LfBusareasend>	mon_BusAreaSendMap = new HashMap<String, LfBusareasend>();
			//	缓存已有的对象
			LfBusareasend cacheBusareasend;
			//	缓存数据中的发送量
			String cacheBusAreaSend = "";
			// 缓存数据中的发送量的json格式
			JSONObject cacheBusAreaSendJson = null;
			//	数据库列表中的发送量
			String busAreaSend = "";
			// 数据库列表中的发送量的json格式
			JSONObject busAreaSendJson = null;
			// 最新的发送数据
			String newBusAreaSend = "";
			// 发送小时
			String key = "";
			// 缓存的小时发送量
			Integer cacheValue = 0;
			// 数据库对象的小时发送量
			Integer value = 0;
			if(busareasendList != null && busareasendList.size() > 0)
			{
				// 业务区域发送对象KEY值，格式：时间&业务&区域
				String busAreaSendKey = "";
				for(LfBusareasend busareasend:busareasendList)
				{
					// 业务区域发送对象KEY值
					busAreaSendKey = new StringBuffer(busareasend.getDatadate().toString()).append("&")
										.append(busareasend.getBuscode()).append("&")
										.append(busareasend.getAreacode()).toString();
					newBusAreaSend = busareasend.getMtsendcount();
					if(mon_BusAreaSendMap.containsKey(busAreaSendKey))
					{
						//缓存中的对象
						cacheBusareasend = mon_BusAreaSendMap.get(busAreaSendKey);
						// 缓存数据中的发送量
						cacheBusAreaSend = cacheBusareasend.getMtsendcount();
						//数据库列表中的发送量
						busAreaSend = busareasend.getMtsendcount();
						// 缓存数据中的发送量的json格式
						cacheBusAreaSendJson = new JSONObject(cacheBusAreaSend);
						// 缓存数据中的发送量的json格式
						busAreaSendJson = new JSONObject(busAreaSend);
						// 遍历缓存JSON
						Iterator it = cacheBusAreaSendJson.keys();
						while(it.hasNext())
						{
							key = (String) it.next();
							cacheValue = Integer.parseInt(cacheBusAreaSendJson.getString(key));
							// Key存在，发送数据累加
							if(busAreaSendJson.has(key))
							{
								value = Integer.parseInt((String)busAreaSendJson.get(key));
								busAreaSendJson.put(key, cacheValue+value);
							}
							// Key不存在，设置发送数据
							else
							{
								busAreaSendJson.put(key, cacheValue);
							}
						}
						busareasend.setMtsendcount(busAreaSendJson.toString());
					}
					
					busareasend.setMtsendcount(newBusAreaSend);
					mon_BusAreaSendMap.put(busAreaSendKey, busareasend);
				}
			}
			return mon_BusAreaSendMap;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置业务区域发送数据异常！");
			return null;
		}
	}
	
		/**
	 * 监控数据分析
	 * 
	 * @description
	 * @param monBusAreaList
	 *        业务监控信息
	 * @param busareasendList
	 *        业务区域发送数据
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-27 下午04:42:29
	 */
	private void dataAnalysis(List<MonBusAreaInfoVo> monBusAreaList, List<LfBusareasend> busareasendList)
	{
		// 业务编码
		String busCode;
		// 监控业务区域MT已送总数
		Integer monBusAreaCount;
		//区域时间发送总数
		Integer AreaTimeSendtotal;
		try
		{
			// 遍历业务监控信息
			for (MonBusAreaInfoVo monBusArea : monBusAreaList)
			{
				// 数据重置
				monBusAreaCount = 0;
				AreaTimeSendtotal = 0;
				// 监控业务编码
				busCode = monBusArea.getBusCode();
				
				// 业务区域发送数据
				for (LfBusareasend busareasend : busareasendList)
				{
					// 业务区域发送业务编码在监控信息中存在
					if(busCode.equals(busareasend.getBuscode()))
					{
						// 计算所有区域的已发送总量
						AreaTimeSendtotal = countAreaTimeSendtotal(monBusArea, busareasend);
						// 为null表示数据异常
						if(AreaTimeSendtotal != null)
						{
							//最后一小时无数据并且当前时间小于6分钟，有可能为数据还未上传
							if(AreaTimeSendtotal == -100)
							{
								EmpExecutionContext.info("业务区域监控最后一小时数据不存在并且当前时间小于6分钟，busCode:"+busCode
														+"，monBusArea:"+monBusArea.getId()
														+"，busareasendId:"+busareasend.getId());
								break;
							}
							else
							{
								//累加
								monBusAreaCount += AreaTimeSendtotal;
							}
						}
						else
						{
							EmpExecutionContext.error("业务区域监控数据异常，业务区域时间发送总数为空，busCode:"+busCode
													+"，monBusArea:"+monBusArea.getId()
													+"，busareasendId:"+busareasend.getId());
							break;
						}
					}
				}
				//区域时间发送总数为null表示数据异常，为-100表示最后一小时无数据并且当前时间小于6分钟的情况
				if(AreaTimeSendtotal != null && AreaTimeSendtotal != -100)
				{
					//更新状态成功
					if(updateMonState(monBusArea.getId()))
					{
						// 数据分析，记录监控详情，发送告警短信
						AnalysisAndAlarm(monBusArea, monBusAreaCount);
					}
					else
					{
						EmpExecutionContext.info("业务区域数据分析，更新告警状态影响条数为0，Id："+monBusArea.getId()
								+"，业务编码："+monBusArea.getBusCode()+"，实际发送条数："+monBusAreaCount);
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "业务监控数据分析处理异常！");
		}
	}
	
	/**
	 * 监控数据分析
	 * 
	 * @description
	 * @param monBusAreaList
	 *        业务监控信息
	 * @param busareasendMap
	 *        业务区域发送数据
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-27 下午04:42:29
	 */
	private void dataAnalysis(List<MonBusAreaInfoVo> monBusAreaList, Map<String, LfBusareasend> busareasendMap)
	{
		// 业务编码
		String busCode;
		// 监控业务区域MT已送总数
		Integer monBusAreaCount;
		//区域时间发送总数
		Integer AreaTimeSendtotal;
		try
		{
			// 遍历业务监控信息
			for (MonBusAreaInfoVo monBusArea : monBusAreaList)
			{
				// 数据重置
				monBusAreaCount = 0;
				AreaTimeSendtotal = 0;
				// 监控业务编码
				busCode = monBusArea.getBusCode();
				
				// 业务区域发送数据
				for (LfBusareasend busareasend : busareasendMap.values()) 
				{
					// 业务区域发送业务编码在监控信息中存在
					if(busCode.equals(busareasend.getBuscode()))
					{
						// 计算所有区域的已发送总量
						AreaTimeSendtotal = countAreaTimeSendtotal(monBusArea, busareasend);
						// 为null表示数据异常
						if(AreaTimeSendtotal != null)
						{
							//最后一小时无数据并且当前时间小于6分钟，有可能为数据还未上传
							if(AreaTimeSendtotal == -100)
							{
								EmpExecutionContext.info("业务区域监控最后一小时数据不存在并且当前时间小于6分钟，busCode:"+busCode
														+"，monBusArea:"+monBusArea.getId()
														+"，busareasendId:"+busareasend.getId());
								break;
							}
							else
							{
								//累加
								monBusAreaCount += AreaTimeSendtotal;
							}
						}
						else
						{
							EmpExecutionContext.error("业务区域监控数据异常，业务区域时间发送总数为空，busCode:"+busCode
													+"，monBusArea:"+monBusArea.getId()
													+"，busareasendId:"+busareasend.getId());
							break;
						}
					}
				}
				//区域时间发送总数为null表示数据异常，为-100表示最后一小时无数据并且当前时间小于6分钟的情况
				if(AreaTimeSendtotal != null && AreaTimeSendtotal != -100)
				{
					//更新状态成功
					if(updateMonState(monBusArea.getId()))
					{
						// 数据分析，记录监控详情，发送告警短信
						AnalysisAndAlarm(monBusArea, monBusAreaCount);
					}
					else
					{
						EmpExecutionContext.info("业务区域数据分析，更新告警状态影响条数为0，Id："+monBusArea.getId()
								+"，业务编码："+monBusArea.getBusCode()+"，实际发送条数："+monBusAreaCount);
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "业务监控数据分析处理异常！");
		}
	}

	/**
	 * 检查监控时间
	 * 
	 * @description
	 * @param beginHour
	 *        开始监牢时间
	 * @param createTime
	 *        创建时间
	 * @return 如果监控记录为当天创建，创建时间大于开始监控时间（小时）返回false，否则返回true
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-27 下午05:16:33
	 */
	public boolean checkMonTime(Integer beginHour, Timestamp createTime)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(createTime);
			// 当前时间
			String newTime = sdf.format(System.currentTimeMillis());
			// 创建时间
			String create = sdf.format(calendar.getTime());
			// 判断是否是同一天
			if(newTime.equals(create))
			{
				//创建时间大于开始监控时间
				if(calendar.get(Calendar.HOUR_OF_DAY) > beginHour)
				{
					return false;
				}
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "检查业务区域监控时间失败！");
			return false;
		}
	}

	/**
	 * 计算区域时间段发送总数
	 * 
	 * @description
	 * @param monBusAreaList
	 *        业务监控信息
	 * @param busareasendList
	 *        业务区域发送数据
	 * @return 区域时间段发送的总数，null为数据异常，－100为最后一小时无数据并且当前时间小于6分钟的情况（监控数据5分钟上传一次，有可能还未上传）
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-27 下午05:34:29
	 */
	private Integer countAreaTimeSendtotal(MonBusAreaInfoVo monBusArea, LfBusareasend busareasend)
	{
		// 区域时间段发送总数
		Integer areaTimeSendTotal = 0;
		try
		{
			// 监控信息区域编码
			String areaCode = monBusArea.getAreaCode();
			// 不是所有区域
			if(!"-1".equals(areaCode))
			{
				// 如果发送业务区域不存在监控信息区域中，直接返回0
				if(areaCode.indexOf("," + busareasend.getAreacode() + ",") < 0)
				{
					return areaTimeSendTotal;
				}
			}
			// 监控开始小时，对应数据库数据需要+1
			Integer beginHour = monBusArea.getBeginHour() + 1;
			// 监控结束小时，对应数据库数据需要+1
			Integer endHour = monBusArea.getEndHour() + 1;
			// 区域发送量
			String mtSendCount = busareasend.getMtsendcount();
			if(mtSendCount != null && mtSendCount.trim().length() > 0)
			{
				Calendar calendar = Calendar.getInstance();
				int minute = calendar.get(Calendar.MINUTE);
				// 数据为JSON格式
				JSONObject json = new JSONObject(mtSendCount);
				// 累加监控时间段数据
				for (int i = beginHour; i < endHour; i++)
				{
					//时间段数据存在
					if(json.has(String.valueOf(i)))
					{
						// 时间段累加发送量
						areaTimeSendTotal += Integer.parseInt(json.get(String.valueOf(i)).toString());
					}
					//时间段数据不存在，如果是最后一小时无数据，并且当前时间小于6分钟（监控数据5分钟上传一次，有可能还未上传）
					else if(i == endHour-1 && minute < 6)
					{
						EmpExecutionContext.info("业务区域监控最后一小时"+(endHour-1)+"数据不存在并且当前时间小于6分钟，业务区域发送数据ID:"+busareasend.getId()+"，mtSendCount:"+json.toString());
						return -100;
					}
					else
					{
						// 如果json中KEY不存在，说明发送数据异常，缺少此时间的数据
						EmpExecutionContext.error("业务区域监控数据异常，数据时间："+i+"，业务区域发送数据ID:"+busareasend.getId()+"，mtSendCount:"+json.toString());
						return null;
					}
				}
			}
			else
			{
				EmpExecutionContext.error("业务区域监控数据异常，mtSendCount为null,业务区域发送数据ID:"+busareasend.getId());
				return null;
			}
			return areaTimeSendTotal;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("业务区域监控数据异常，busCode:"+busareasend.getBuscode()
									+"monBusArea:"+monBusArea.getId()
									+"busareasend:"+busareasend.getId());
			return null;
		}
	}

	/**
	 * 数据分析，记录监控详情，发送告警短信
	 * 
	 * @description
	 * @param monBusArea
	 * @param monBusAreaCount
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-30 下午12:08:33
	 */
	private void AnalysisAndAlarm(MonBusAreaInfoVo monBusArea, Integer monBusAreaCount)
	{
		try
		{
			// 告警内容
			StringBuffer smsContent;
			// 告警邮件内容
			StringBuffer emailContent = new StringBuffer();
			// 告警尾部内容
			String tailContent;
			// 是否告警，true:是；false:否
			boolean isAlarm;
			// MT已发告警值
			Integer mtHaveSnd = monBusArea.getMtHaveSnd();
			// 偏离率（高）
			Integer deviatHigh = monBusArea.getDeviatHigh();
			// 偏离率（低）
			Integer deviatLow = monBusArea.getDeviatLow();
			//数据初始化
			smsContent = new StringBuffer();
			tailContent = " ";
			isAlarm = false;
			try
			{
				//偏离率高低都设置为0
				if(deviatHigh == 0 && deviatLow == 0)
				{
					if(!monBusAreaCount.equals(mtHaveSnd))
					{
						// 告警尾部内容
						tailContent = "MT实际已发送量不等于MT已发告警值";
						isAlarm = true;
					}
				}
				else
				{
					//设置偏离率（高）
					if(deviatHigh != 0)
					{
						if(monBusAreaCount > (mtHaveSnd * (100 + deviatHigh) / 100))
						{
							// 告警尾部内容
							tailContent = "高于偏离率" + deviatHigh + "%";
							isAlarm = true;
						}
					}
					//设置偏离率（低）且为正常
					if(deviatLow != 0 && !isAlarm)
					{
						if(monBusAreaCount < (mtHaveSnd * (100 - deviatLow) / 100))
						{
							// 告警尾部内容
							tailContent = "低于偏离率" + deviatLow + "%";
							isAlarm = true;
						}
					}
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "业务监控数据分析异常，业务监控数据ID:" + monBusArea.getId() + "，业务编码：" + monBusArea.getBusCode());
				return;
			}

			// 告警手机号
			String monPhone = monBusArea.getMonPhone();
			// 有效号码
			String effPhone = " ";
			if(monPhone != null && !"".equals(monPhone.trim()))
			{
				// 有效号码,返回null表示无有效号码
				effPhone = checkMobleVaild(monPhone);
			}
			
			// 告警邮箱
			String monEmail = monBusArea.getMonemail();
			// 有效的告警邮箱
			String effEmail = retEffEmail(monEmail);
						
			try
			{
				// 监控详情对象
				LfMonBusinfo monBusinfo = new LfMonBusinfo();
				monBusinfo.setBusname(monBusArea.getBusName());
				monBusinfo.setBuscode(monBusArea.getBusCode());
				monBusinfo.setAreacode(monBusArea.getAreaCode());
				monBusinfo.setBegintime(monBusArea.getBeginTime());
				monBusinfo.setEndtime(monBusArea.getEndTime());
				monBusinfo.setBeginhour(monBusArea.getBeginHour());
				monBusinfo.setEndhour(monBusArea.getEndHour());
				monBusinfo.setEvttype(isAlarm ? 1 : 0);
				monBusinfo.setMtsendcount(monBusAreaCount);
				monBusinfo.setMthavesnd(monBusArea.getMtHaveSnd());
				monBusinfo.setMondeviat("高于"+String.valueOf(deviatHigh)+"%，低于"+String.valueOf(deviatLow)+"%");
				monBusinfo.setMondes(tailContent);
				monBusinfo.setMonphone(effPhone != null && effPhone.trim().length() > 0 ? effPhone : " ");
//				monBusinfo.setMonemail(effEmail != null && effEmail.trim().length() > 0 ? effEmail : " ");
				monBusinfo.setCorpcode(monBusArea.getCorpCode());
				monBusinfo.setCreatetime(new Timestamp(System.currentTimeMillis()));
				// 新增告警详情记录
				if(!baseBiz.addObj(monBusinfo))
				{
					EmpExecutionContext.error("新增告警详情记录失败！业务监控数据ID:" + monBusArea.getId() + "，业务编码：" + monBusArea.getBusCode());
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "新增告警详情记录异常！业务监控数据ID:" + monBusArea.getId() + "，业务编码：" + monBusArea.getBusCode());
			}

			// 告警
			if(isAlarm)
			{
				// 发送告警短信
				if(effPhone != null && effPhone.trim().length() > 0)
				{
					try
					{
						// 告警内容
						smsContent.append("业务名称:").append(monBusArea.getBusName()).append("，业务编码:").append(monBusArea.getBusCode())
						.append("，MT已发告警值:").append(monBusArea.getMtHaveSnd()).append("，MT实际已发送量:")
						.append(monBusAreaCount).append("，").append(tailContent);
						// 发送告警短信线程
						new Thread(new sendAlarmSms(null, smsContent.toString(), effPhone)).start();
					}
					catch (Exception e)
					{
						EmpExecutionContext.error(e, "发送业务监控告警短信失败，业务监控数据ID:" + monBusArea.getId() + "，业务编码：" + monBusArea.getBusCode());
						return;
					}
				}
				
				// 发送告警短信
				if(effEmail != null && effEmail.trim().length() > 0)
				{
					try
					{
						// 告警内容
						emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
												.append("监控类型：业务监控").append("<br/>")
												.append("监控名称："+monBusArea.getBusName()).append("<br/>")
												.append("告警级别：告警").append("<br/>")
												.append("事件描述："+tailContent).append("<br/>")
												.append("告警时间："+sdf.format(new Date())).append("<br/>");
						//设置邮件内容参数
						MonEmailParams mep = new MonEmailParams();
						mep.setContent(emailContent.toString());
						mep.setCorpCode(corpcode);
						mep.setEmail(effEmail);
						// 发送告警邮件
						new Thread(new sendAlarmEmail( mep)).start();
					}
					catch (Exception e)
					{
						EmpExecutionContext.error(e, "发送业务监控告警邮件失败，业务监控数据ID:" + monBusArea.getId() + "，业务编码：" + monBusArea.getBusCode());
						return;
					}
				}
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "业务监控数据分析，记录监控详情，发送告警异常！");
		}
	}
	
	
	/**
	 * 更新业务区域监控状态
	 * @description    
	 * @param id 业务监控数据信息表自增ID      			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-1 下午02:17:46
	 */
	private boolean updateMonState(long id)
	{
		try
		{
			// 时间格式
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			// 最后一次处理时间
			String lastTime = format.format(System.currentTimeMillis());
//			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//			conditionMap.put("id", String.valueOf(id));
//			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
//			objectMap.put("monlasttime", lastTime);
//			baseBiz.update(LfMonBusdata.class, objectMap, conditionMap);
			return monBusAreaDAO.updateMonState(id, Integer.parseInt(lastTime));
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新业务区域监控状态失败！id:"+id);
			return false;
		}
	}
	/**
	 * 告警手机号码有效性检查
	 * 
	 * @description
	 * @param phone
	 * @return true 有效; false无效
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 下午06:41:07
	 */
	public String checkMobleVaild(String phone)
	{
		String[] monPhone = null;
		//有效的号码
		StringBuffer effPhone = new StringBuffer();
		try
		{
			if("".equals(phone) || phone.length() == 0)
			{
				return null;
			}
			//多个手机号码
			if(phone.indexOf(",") != -1)
			{
				monPhone = phone.split(",");
			}
			//一个手机号码
			else
			{
				monPhone = new String[1];
				monPhone[0] = phone;
			}

			if(monPhone == null)
			{
				return null;
			}
			else
			{
				for(int i= 0; i< monPhone.length; i++)
				{
					// 检查是否为黑名单
					if(blackListAtom.monPhonecheckBlack(monPhone[i]))
					{
						EmpExecutionContext.error("短信告警手机号码为黑名单，告警短信无法发送，告警手机号为：" + monPhone[i]);
						continue;
					}
					effPhone.append(monPhone[i] + ",");
				}
			}
			//去除最后一个逗号
			if(effPhone.length() > 1&& effPhone.lastIndexOf(",")>-1 )
			{
				return effPhone.substring(0, effPhone.lastIndexOf(",")).toString();
			}
			else
			{
				return effPhone.toString();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "验证告警手机号码合法性异常，手机号码为：" + phone);
			return null;
		}
	}
	
	/**
	 * 验证邮箱是否有效
	 * @param email
	 * @return
	 */
	public String retEffEmail(String email){
		String effEmail = null;
		if(email != null && !"".equals(email.trim()))
		{
			// 有效邮箱,返回null表示无效邮箱
			effEmail = checkEmail(email);
		}
		return effEmail;
	}
	
	/**
	 * 告警邮箱的有效性
	 * @param monEmail
	 * @return
	 */
	public String checkEmail(String monEmail)
	{
		String result=null;
		try
		{
			String[] arr = { "ac", "com", "net", "org", "edu", "gov", "mil", "ac\\.cn",
				"com\\.cn", "net\\.cn", "org\\.cn", "edu\\.cn" };
			String temp_arr="";
			for(int i = 0 ; i<arr.length ; i++){
				temp_arr += arr[i]+"|";
			}

			// reg
			String reg_str = "^[0-9a-zA-Z](\\w|-)*@\\w+\\.(" + temp_arr + ")$";
			Pattern pattern = Pattern.compile(reg_str);
			Matcher matcher = pattern.matcher(monEmail);
			if (matcher.find()) {
				result=monEmail;
			}		
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"验证告警邮箱异常！");
		}
		return result;
	}
}
