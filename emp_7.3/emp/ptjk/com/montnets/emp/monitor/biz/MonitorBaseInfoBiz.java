/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-2 下午01:43:08
 */
package com.montnets.emp.monitor.biz;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.LoginBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.MonAlarmDsmParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.GenericEmpTransactionDAO;
import com.montnets.emp.common.entity.LfNodeBaseInfo;
import com.montnets.emp.entity.gateway.AgwAccount;
import com.montnets.emp.entity.monitor.GwClustatus;
import com.montnets.emp.entity.monitor.LfBusareasend;
import com.montnets.emp.entity.monitor.LfMonDGateBuf;
import com.montnets.emp.entity.monitor.LfMonDbstate;
import com.montnets.emp.entity.monitor.LfMonDgtacinfo;
import com.montnets.emp.entity.monitor.LfMonDhost;
import com.montnets.emp.entity.monitor.LfMonDproce;
import com.montnets.emp.entity.monitor.LfMonDspacinfo;
import com.montnets.emp.entity.monitor.LfMonErr;
import com.montnets.emp.entity.monitor.LfMonHostnet;
import com.montnets.emp.entity.monitor.LfMonSgtacinfo;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.entity.monitor.LfMonSspacinfo;
import com.montnets.emp.entity.monitoronline.LfMonOnlcfg;
import com.montnets.emp.entity.monitoronline.LfMonOnluser;
import com.montnets.emp.monitor.biz.i.IMonitorBaseInfoBiz;
import com.montnets.emp.monitor.constant.MonDgateBufParams;
import com.montnets.emp.monitor.constant.MonDgateacParams;
import com.montnets.emp.monitor.constant.MonDhostParams;
import com.montnets.emp.monitor.constant.MonDproceParams;
import com.montnets.emp.monitor.constant.MonDspAccountParams;
import com.montnets.emp.monitor.constant.MonErrorParams;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.monitor.dao.MonitorDAO;
import com.montnets.emp.util.PageInfo;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-2 下午01:43:08
 */

public class MonitorBaseInfoBiz extends SuperBiz implements IMonitorBaseInfoBiz
{

	BaseBiz	baseBiz	= new BaseBiz();

	MonitorDAO monitorDAO = new MonitorDAO();
	
	LoginBiz loginBiz = new LoginBiz();
	/**
	 * 在线用户告警设置表信息写入缓存
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-30 下午06:55:42
	 */
	public void setOnlineCfgInfo()
	{
		try
		{
			monitorDAO.setMonOnlcfgFlag();
			this.setOnlineUser();
			// 查询数据库
			List<LfMonOnlcfg> onlUserNum = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonOnlcfg.class, null, null);
			Iterator<LfMonOnlcfg> itr = onlUserNum.iterator();
			if(itr.hasNext())
			{
				// 在线用户信息写入缓存
				StaticValue.setMonOnlinecfg(itr.next());
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置在线用户基础信息失败，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 设置在线用户详细信息
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-12 下午05:46:34
	 */
	public void setOnlineUser()
	{
		try
		{
			List<LfMonOnluser> lfMonOnlcfgList = new BaseBiz().getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonOnluser.class, null, null);
			Iterator<LfMonOnluser> itr = lfMonOnlcfgList.iterator();
			LfMonOnluser lfMonOnluser;
			StaticValue.mon_OnlineUserMap.clear();
			Map<String, LfMonOnluser> mon_OnlineUser = new ConcurrentHashMap<String, LfMonOnluser>();
			while(itr.hasNext())
			{
				lfMonOnluser = itr.next();
				mon_OnlineUser.put(lfMonOnluser.getSesseionid(), lfMonOnluser);
			}
			// 主机基本信息写入缓存
			StaticValue.mon_OnlineUserMap.putAll(mon_OnlineUser);
			//设置临时缓存
			StaticValue.mon_OnlineUserMapTemp.clear();
			StaticValue.mon_OnlineUserMapTemp.putAll(StaticValue.mon_OnlineUserMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置在线用户详细信息异常!");
		}
	}
	/**
	 * 设置主机基本信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午01:55:38
	 */
	public void setHostBaseInfo()
	{
		try
		{
			//清空缓存
			MonitorStaticValue.getHostBaseMap().clear();
			// 查询数据库
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("hostusestatus&<>", "1");
			List<LfMonShost> hostBaseInfoList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonShost.class, conditionMap, null);
			
			Iterator<LfMonShost> itr = hostBaseInfoList.iterator();
			LfMonShost hostBaseInfo;
			Map<Long, LfMonShost> hostBase = new ConcurrentHashMap<Long, LfMonShost>();
			while(itr.hasNext())
			{
				hostBaseInfo = itr.next();
				hostBase.put(hostBaseInfo.getHostid(), hostBaseInfo);
			}
			// 主机基本信息写入缓存
			MonitorStaticValue.getHostBaseMap().putAll(hostBase);
			//设置临时缓存
			MonitorStaticValue.getHostBaseMapTemp().clear();
			MonitorStaticValue.getHostBaseMapTemp().putAll(MonitorStaticValue.getHostBaseMap());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置主机基础信息失败，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 设置主机动态信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午01:55:38
	 */
	public void setDHostInfo(int flag)
	{
		try
		{
			//清空缓存
			MonitorStaticValue.getHostMap().clear();
			if(flag == 0)
			{
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				objectMap.put("serverNum", StaticValue.getServerNumber());
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("serverNum", "0");
				baseBiz.update(MonDbConnection.getInstance().getConnection(), LfMonDhost.class, objectMap, conditionMap);
			}
			// 查询数据库
			List<LfMonDhost> lfMonDhostList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonDhost.class, null, null);
			
			Iterator<LfMonDhost> itr = lfMonDhostList.iterator();
			LfMonDhost dHostInfo;
			MonDhostParams monDhostParams = null;
			Map<Long, MonDhostParams> host = new ConcurrentHashMap<Long, MonDhostParams>();
			while(itr.hasNext())
			{
				monDhostParams = new MonDhostParams();
				dHostInfo = itr.next();
				monDhostParams.setUpdatetime(dHostInfo.getUpdatetime());
				monDhostParams.setDbservtime(dHostInfo.getDbservtime());
				BeanUtils.copyProperties(monDhostParams,dHostInfo);
				host.put(dHostInfo.getHostid(), monDhostParams);
			}
			// 主机基本信息写入缓存
			MonitorStaticValue.getHostMap().putAll(host);
			//设置临时缓存
			MonitorStaticValue.getHostMapTemp().clear();
			MonitorStaticValue.getHostMapTemp().putAll(MonitorStaticValue.getHostMap());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置主机动态信息失败，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}
	
	/**
	 * 设置程序基础信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午02:14:57
	 */
	public void setProceBaseInfo()
	{
		try
		{
			//清空缓存
			MonitorStaticValue.getProceBaseMap().clear();
			// 查询数据库
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("proceusestatus&<>", "1");
			List<LfMonSproce> proceBaseInfoList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonSproce.class, conditionMap, null);
			
			Iterator<LfMonSproce> itr = proceBaseInfoList.iterator();
			LfMonSproce proceBaseInfo;
			Map<Long, LfMonSproce> proceBase = new ConcurrentHashMap<Long, LfMonSproce>();
			while(itr.hasNext())
			{
				proceBaseInfo = itr.next();
				proceBase.put(proceBaseInfo.getProceid(), proceBaseInfo);
			}
			// 程序基本信息写入缓存
			MonitorStaticValue.getProceBaseMap().putAll(proceBase);
			//设置临时缓存
			MonitorStaticValue.getProceBaseMapTemp().clear();
			MonitorStaticValue.getProceBaseMapTemp().putAll(MonitorStaticValue.getProceBaseMap());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置程序基础信息失败，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 设置程序动态信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午01:55:38
	 */
	public void setDProceInfo(int flag)
	{
		try
		{
			//清空缓存
			MonitorStaticValue.getProceMap().clear();
			if(flag == 0)
			{
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				objectMap.put("serverNum", StaticValue.getServerNumber());
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("serverNum", "0");
				baseBiz.update(MonDbConnection.getInstance().getConnection(), LfMonDproce.class, objectMap, conditionMap);
			}
			// 查询数据库
			List<LfMonDproce> lfMonDproceList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonDproce.class, null, null);
			
			Iterator<LfMonDproce> itr = lfMonDproceList.iterator();
			LfMonDproce dproceInfo;
			MonDproceParams monDproceParams = null;
			Map<Long, MonDproceParams> proce = new ConcurrentHashMap<Long, MonDproceParams>();
			while(itr.hasNext())
			{
				monDproceParams = new MonDproceParams();
				dproceInfo = itr.next();
				monDproceParams.setUpdatetime(dproceInfo.getUpdatetime());
				monDproceParams.setStarttime(dproceInfo.getStarttime());
				monDproceParams.setDbservtime(dproceInfo.getDbservtime());
				BeanUtils.copyProperties(monDproceParams,dproceInfo);
				proce.put(dproceInfo.getProceid(), monDproceParams);
			}
			// 主机基本信息写入缓存
			MonitorStaticValue.getProceMap().putAll(proce);
			//设置临时缓存
			MonitorStaticValue.getProceMapTemp().clear();
			MonitorStaticValue.getProceMapTemp().putAll(MonitorStaticValue.getProceMap());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置程序动态信息失败，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}
	
	/**
	 * 设置SP账号基础信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午02:19:06
	 */
	public void setspAcBaseInfo()
	{
		try
		{
			//清空缓存
			MonitorStaticValue.getSpAccountBaseMap().clear();
			// 查询数据库
			//List<LfMonSspacinfo> spAcBaseInfoList = baseBiz.getEntityList(LfMonSspacinfo.class);
			List<LfMonSspacinfo> spAcBaseInfoList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonSspacinfo.class, null, null);
			Iterator<LfMonSspacinfo> itr = spAcBaseInfoList.iterator();
			LfMonSspacinfo spacBaseInfo;
			Map<String, LfMonSspacinfo>	spAccountBase = new ConcurrentHashMap<String, LfMonSspacinfo>();
			while(itr.hasNext())
			{
				spacBaseInfo = itr.next();
				spAccountBase.put(spacBaseInfo.getSpaccountid(), spacBaseInfo);
			}
			// SP账号基本信息写入缓存
			MonitorStaticValue.getSpAccountBaseMap().putAll(spAccountBase);
			//设置临时缓存
			MonitorStaticValue.getSpAccountBaseMapTemp().clear();
			MonitorStaticValue.getSpAccountBaseMapTemp().putAll(MonitorStaticValue.getSpAccountBaseMap());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置SP账号基础信息失败，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 设置SP账号动态信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午01:55:38
	 */
	public void setDspAcInfo(int flag)
	{
		try
		{
			//清空缓存
			MonitorStaticValue.getSpAccountMap().clear();
			if(flag == 0)
			{
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				objectMap.put("serverNum", StaticValue.getServerNumber());
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("serverNum", "0");
				baseBiz.update(MonDbConnection.getInstance().getConnection(), LfMonDspacinfo.class, objectMap, conditionMap);
			}
			// 查询数据库
			List<LfMonDspacinfo> lfMonDspacinfoList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonDspacinfo.class, null, null);
			
			Iterator<LfMonDspacinfo> itr = lfMonDspacinfoList.iterator();
			//数据库对象
			LfMonDspacinfo dspacinfo;
			//缓存已有的对象
			MonDspAccountParams cacheDspacinfo;
			//设置的缓存对象
			MonDspAccountParams monDspAccountParams = null;
			//SP账号
			String spaccountid = "";
			//时间比较，0：为同一天；-1：时间1小于时间2；1：时间1大于时间2
			int timeCompare = 0;
			//缓存的消息时间
			Timestamp cacheDbservtime;
			//数据库对象的消息时间
			Timestamp dbservtime;
			//节点提交总量
			Map<String, Integer> spNodeMthavesndMap = new HashMap<String, Integer>();
			Map<String, MonDspAccountParams> spAccount = new ConcurrentHashMap<String, MonDspAccountParams>();
			while(itr.hasNext())
			{
				monDspAccountParams = new MonDspAccountParams();
				dspacinfo = itr.next();
				spaccountid = dspacinfo.getSpaccountid();
				//缓存已存在该SP账号信息
				if(spAccount.containsKey(spaccountid))
				{
					//缓存对象
					cacheDspacinfo = spAccount.get(spaccountid);
					spNodeMthavesndMap = cacheDspacinfo.getSpNodeMthavesndMap();
					spNodeMthavesndMap.put(spaccountid+dspacinfo.getGatewayid(), dspacinfo.getMtTotalSnd());
					//缓存对象消息时间
					cacheDbservtime = cacheDspacinfo.getDbservtime();
					//数据库对象消息时间
					dbservtime = dspacinfo.getDbservtime();
					//消息时间比较
					timeCompare = timeStrCompare(cacheDbservtime, dbservtime);
					//消息时间是否为同一天
					if(timeCompare == 0)
					{
						//缓存消息时间大于数据库对象消息时间
						if(cacheDbservtime.after(dbservtime))
						{
							//基础信息以缓存为准
							BeanUtils.copyProperties(monDspAccountParams,cacheDspacinfo);
						}
						else
						{
							//基础信息以数据库为准
							BeanUtils.copyProperties(monDspAccountParams,dspacinfo);
						}
						//监控数据累加
						monDspAccountParams.setMthavesnd(cacheDspacinfo.getMthavesnd() + dspacinfo.getMthavesnd());
						monDspAccountParams.setMtissuedspd(cacheDspacinfo.getMtissuedspd() + dspacinfo.getMtissuedspd());
//						monDspAccountParams.setMtremained(cacheDspacinfo.getMtremained() + dspacinfo.getMtremained());
						monDspAccountParams.setMtsndspd(cacheDspacinfo.getMtsndspd() + dspacinfo.getMtsndspd());
						monDspAccountParams.setMtTotalSnd(cacheDspacinfo.getMtTotalSnd() + dspacinfo.getMtTotalSnd());
						monDspAccountParams.setMototalrecv(cacheDspacinfo.getMototalrecv() + dspacinfo.getMototalrecv());
//						monDspAccountParams.setMoremained(cacheDspacinfo.getMoremained() + dspacinfo.getMoremained());
						monDspAccountParams.setMosndspd(cacheDspacinfo.getMosndspd() + dspacinfo.getMosndspd());
						monDspAccountParams.setMohavesnd(cacheDspacinfo.getMohavesnd() + dspacinfo.getMohavesnd());
						monDspAccountParams.setRptsndspd(cacheDspacinfo.getRptsndspd() + dspacinfo.getRptsndspd());
						monDspAccountParams.setRpttotalrecv(cacheDspacinfo.getRpttotalrecv() + dspacinfo.getRpttotalrecv());
//						monDspAccountParams.setRptremained(cacheDspacinfo.getRptremained() + dspacinfo.getRptremained());
						monDspAccountParams.setRptHaveSnd(cacheDspacinfo.getRptHaveSnd() + dspacinfo.getRptHaveSnd());
						//告警标识以最小ID为准
						if(cacheDspacinfo.getId() > dspacinfo.getId())
						{
							monDspAccountParams.setThresholdflag1(dspacinfo.getThresholdflag1());
							monDspAccountParams.setThresholdflag2(dspacinfo.getThresholdflag2());
							monDspAccountParams.setThresholdflag3(dspacinfo.getThresholdflag3());
							monDspAccountParams.setThresholdflag4(dspacinfo.getThresholdflag4());
							monDspAccountParams.setThresholdflag5(dspacinfo.getThresholdflag5());
							monDspAccountParams.setThresholdflag6(dspacinfo.getThresholdflag6());
							monDspAccountParams.setThresholdflag7(dspacinfo.getThresholdflag7());
							monDspAccountParams.setThresholdflag8(dspacinfo.getThresholdflag8());
							monDspAccountParams.setThresholdflag9(dspacinfo.getThresholdflag9());
							monDspAccountParams.setThresholdflag10(dspacinfo.getThresholdflag10());
							monDspAccountParams.setThresholdflag11(dspacinfo.getThresholdflag11());
							monDspAccountParams.setThresholdflag12(dspacinfo.getThresholdflag12());
							monDspAccountParams.setThresholdflag13(dspacinfo.getThresholdflag13());
							monDspAccountParams.setSendmailflag1(dspacinfo.getSendmailflag1());
							monDspAccountParams.setSendmailflag2(dspacinfo.getSendmailflag2());
							monDspAccountParams.setSendmailflag3(dspacinfo.getSendmailflag3());
							monDspAccountParams.setSendmailflag4(dspacinfo.getSendmailflag4());
							monDspAccountParams.setSendmailflag5(dspacinfo.getSendmailflag5());
							monDspAccountParams.setSendmailflag6(dspacinfo.getSendmailflag6());
							monDspAccountParams.setSendmailflag7(dspacinfo.getSendmailflag7());
							monDspAccountParams.setSendmailflag8(dspacinfo.getSendmailflag8());
							monDspAccountParams.setSendmailflag9(dspacinfo.getSendmailflag9());
							monDspAccountParams.setSendmailflag10(dspacinfo.getSendmailflag10());
							monDspAccountParams.setSendmailflag11(dspacinfo.getSendmailflag11());
							monDspAccountParams.setSendmailflag12(dspacinfo.getSendmailflag12());
							monDspAccountParams.setSendmailflag13(dspacinfo.getSendmailflag13());
							monDspAccountParams.setId(dspacinfo.getId());
						}
						else
						{
							monDspAccountParams.setThresholdflag1(cacheDspacinfo.getThresholdflag1());
							monDspAccountParams.setThresholdflag2(cacheDspacinfo.getThresholdflag2());
							monDspAccountParams.setThresholdflag3(cacheDspacinfo.getThresholdflag3());
							monDspAccountParams.setThresholdflag4(cacheDspacinfo.getThresholdflag4());
							monDspAccountParams.setThresholdflag5(cacheDspacinfo.getThresholdflag5());
							monDspAccountParams.setThresholdflag6(cacheDspacinfo.getThresholdflag6());
							monDspAccountParams.setThresholdflag7(cacheDspacinfo.getThresholdflag7());
							monDspAccountParams.setThresholdflag8(cacheDspacinfo.getThresholdflag8());
							monDspAccountParams.setThresholdflag9(cacheDspacinfo.getThresholdflag9());
							monDspAccountParams.setThresholdflag10(cacheDspacinfo.getThresholdflag10());
							monDspAccountParams.setThresholdflag11(cacheDspacinfo.getThresholdflag11());
							monDspAccountParams.setThresholdflag12(cacheDspacinfo.getThresholdflag12());
							monDspAccountParams.setThresholdflag13(cacheDspacinfo.getThresholdflag13());
							monDspAccountParams.setSendmailflag1(cacheDspacinfo.getSendmailflag1());
							monDspAccountParams.setSendmailflag2(cacheDspacinfo.getSendmailflag2());
							monDspAccountParams.setSendmailflag3(cacheDspacinfo.getSendmailflag3());
							monDspAccountParams.setSendmailflag4(cacheDspacinfo.getSendmailflag4());
							monDspAccountParams.setSendmailflag5(cacheDspacinfo.getSendmailflag5());
							monDspAccountParams.setSendmailflag6(cacheDspacinfo.getSendmailflag6());
							monDspAccountParams.setSendmailflag7(cacheDspacinfo.getSendmailflag7());
							monDspAccountParams.setSendmailflag8(cacheDspacinfo.getSendmailflag8());
							monDspAccountParams.setSendmailflag9(cacheDspacinfo.getSendmailflag9());
							monDspAccountParams.setSendmailflag10(cacheDspacinfo.getSendmailflag10());
							monDspAccountParams.setSendmailflag11(cacheDspacinfo.getSendmailflag11());
							monDspAccountParams.setSendmailflag12(cacheDspacinfo.getSendmailflag12());
							monDspAccountParams.setSendmailflag13(cacheDspacinfo.getSendmailflag13());
							monDspAccountParams.setId(cacheDspacinfo.getId());
						}
					}
					//不是同一天
					else
					{
						//数据库对象消息时间大于缓存消息时间
						if(timeCompare == -1)
						{
							monDspAccountParams.setUpdatetime(dspacinfo.getUpdatetime());
							monDspAccountParams.setLoginintm(dspacinfo.getLoginouttm());
							monDspAccountParams.setLoginouttm(dspacinfo.getLoginouttm());
							monDspAccountParams.setDbservtime(dspacinfo.getDbservtime());
							BeanUtils.copyProperties(monDspAccountParams,dspacinfo);
						}
						else
						{
							continue;
						}
					}
				}
				else
				{
					spNodeMthavesndMap = new HashMap<String, Integer>();
					spNodeMthavesndMap.put(spaccountid+dspacinfo.getGatewayid(), dspacinfo.getMtTotalSnd());
					monDspAccountParams.setUpdatetime(dspacinfo.getUpdatetime());
					monDspAccountParams.setLoginintm(dspacinfo.getLoginouttm());
					monDspAccountParams.setLoginouttm(dspacinfo.getLoginouttm());
					monDspAccountParams.setDbservtime(dspacinfo.getDbservtime());
					BeanUtils.copyProperties(monDspAccountParams,dspacinfo);
				}
				monDspAccountParams.setSpNodeMthavesndMap(spNodeMthavesndMap);
				spAccount.put(spaccountid, monDspAccountParams);
			}
			// 主机基本信息写入缓存
			MonitorStaticValue.getSpAccountMap().putAll(spAccount);
			//设置临时缓存
			MonitorStaticValue.getSpAccountMapTemp().clear();
			MonitorStaticValue.getSpAccountMapTemp().putAll(MonitorStaticValue.getSpAccountMap());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置SP账号动态信息异常，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}
	
	/**
	 * 设置通道账号基础信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午02:22:00
	 */
	public void setGateAcBaseInfo()
	{
		try
		{
			//清空缓存
			MonitorStaticValue.getGateAccountBaseMap().clear();
			// 查询数据库
			//List<LfMonSgtacinfo> gateAcBaseInfoList = baseBiz.getEntityList(LfMonSgtacinfo.class);
			List<LfMonSgtacinfo> gateAcBaseInfoList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonSgtacinfo.class, null, null);
			Iterator<LfMonSgtacinfo> itr = gateAcBaseInfoList.iterator();
			LfMonSgtacinfo gateAcBaseInfo;
			Map<String, LfMonSgtacinfo>	gateAccountBase = new ConcurrentHashMap<String, LfMonSgtacinfo>();
			while(itr.hasNext())
			{
				gateAcBaseInfo = itr.next();
				gateAccountBase.put(gateAcBaseInfo.getGateaccount(), gateAcBaseInfo);
			}
			// 通道账号基本信息写入缓存
			MonitorStaticValue.getGateAccountBaseMap().putAll(gateAccountBase);
			//设置临时缓存
			MonitorStaticValue.getGateAccountBaseMapTemp().clear();
			MonitorStaticValue.getGateAccountBaseMapTemp().putAll(MonitorStaticValue.getGateAccountBaseMap());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置通道账号基础信息失败，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 设置SPGATE动态信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午01:55:38
	 */
	public void setDGateAcInfo(int flag)
	{
		try
		{
			//清空缓存
			MonitorStaticValue.getGateAccountMap().clear();
			if(flag == 0)
			{
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				objectMap.put("serverNum", StaticValue.getServerNumber());
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("serverNum", "0");
				baseBiz.update(MonDbConnection.getInstance().getConnection(), LfMonDgtacinfo.class, objectMap, conditionMap);
			}
			// 查询数据库
			List<LfMonDgtacinfo> lfMonDgtacinfoList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonDgtacinfo.class, null, null);
			
			Iterator<LfMonDgtacinfo> itr = lfMonDgtacinfoList.iterator();
			LfMonDgtacinfo dgtacinfo;
			//设置的缓存对象
			MonDgateacParams monDgateacParams = null;
			//缓存已有的对象
			MonDgateacParams cacheDgtacinfo;
			
			//通道账号
			String gateaccount = "";
			//时间比较，0：为同一天；-1：时间1小于时间2；1：时间1大于时间2
			int timeCompare = 0;
			//缓存的消息时间
			Timestamp cacheDbservtime;
			//数据库对象的消息时间
			Timestamp dbservtime;
			Map<String, MonDgateacParams> gateAccount = new ConcurrentHashMap<String, MonDgateacParams>();
			while(itr.hasNext())
			{
				monDgateacParams = new MonDgateacParams();
				dgtacinfo = itr.next();
				gateaccount = dgtacinfo.getGateaccount();
				//缓存中存在
				if(gateAccount.containsKey(gateaccount))
				{
					//缓存对象
					cacheDgtacinfo = gateAccount.get(gateaccount);
					//缓存对象消息时间
					cacheDbservtime = cacheDgtacinfo.getDbservtime();
					//数据库对象消息时间
					dbservtime = dgtacinfo.getDbservtime();
					//消息时间比较
					timeCompare = timeStrCompare(cacheDbservtime, dbservtime);
					//消息时间是否为同一天
					if(timeCompare == 0)
					{
						//缓存消息时间大于数据库对象消息时间
						if(cacheDbservtime.after(dbservtime))
						{
							//基础信息以缓存为准
							BeanUtils.copyProperties(monDgateacParams,cacheDgtacinfo);
						}
						else
						{
							//基础信息以数据库为准
							BeanUtils.copyProperties(monDgateacParams,dgtacinfo);
						}
						//监控数据累加
						monDgateacParams.setMthavesnd(cacheDgtacinfo.getMthavesnd() + dgtacinfo.getMthavesnd());
						monDgateacParams.setMtrecvspd(cacheDgtacinfo.getMtrecvspd() + dgtacinfo.getMtrecvspd());
//						monDgateacParams.setMtremained(cacheDgtacinfo.getMtremained() + dgtacinfo.getMtremained());
						//网关传过来的提交总量是由已发量+待发，待发所有记录是一样，如果以提交总量进行累加，待发量就会重复增加，以第一条记录的提交总量做为基准，之后以已发量进行累加
						monDgateacParams.setMttotalsnd(cacheDgtacinfo.getMttotalsnd() + dgtacinfo.getMthavesnd());
						monDgateacParams.setMototalrecv(cacheDgtacinfo.getMototalrecv() + dgtacinfo.getMototalrecv());
						monDgateacParams.setMosndspd(cacheDgtacinfo.getMosndspd() + dgtacinfo.getMosndspd());
						monDgateacParams.setMohavesnd(cacheDgtacinfo.getMohavesnd() + dgtacinfo.getMohavesnd());
//						monDgateacParams.setMoremained(cacheDgtacinfo.getMoremained() + dgtacinfo.getMoremained());
						monDgateacParams.setRptsndspd(cacheDgtacinfo.getRptsndspd() + dgtacinfo.getRptsndspd());
						monDgateacParams.setRpthavesnd(cacheDgtacinfo.getRpthavesnd() + dgtacinfo.getRpthavesnd());
						monDgateacParams.setRpttotalrecv(cacheDgtacinfo.getRpttotalrecv() + dgtacinfo.getRpttotalrecv());
//						monDgateacParams.setRptremained(cacheDgtacinfo.getRptremained() + dgtacinfo.getRptremained());
						//告警标识以最小ID为准
						if(cacheDgtacinfo.getId() > dgtacinfo.getId())
						{
							monDgateacParams.setThresholdflag1(dgtacinfo.getThresholdflag1());
							monDgateacParams.setThresholdflag2(dgtacinfo.getThresholdflag2());
							monDgateacParams.setThresholdflag3(dgtacinfo.getThresholdflag3());
							monDgateacParams.setThresholdflag4(dgtacinfo.getThresholdflag4());
							monDgateacParams.setThresholdflag5(dgtacinfo.getThresholdflag5());
							monDgateacParams.setThresholdflag6(dgtacinfo.getThresholdflag6());
							monDgateacParams.setThresholdflag7(dgtacinfo.getThresholdflag7());
							monDgateacParams.setThresholdflag8(dgtacinfo.getThresholdflag8());
							monDgateacParams.setThresholdflag9(dgtacinfo.getThresholdflag9());
							monDgateacParams.setThresholdflag10(dgtacinfo.getThresholdflag10());
							monDgateacParams.setThresholdflag11(dgtacinfo.getThresholdflag11());
							monDgateacParams.setThresholdflag12(dgtacinfo.getThresholdflag12());
							monDgateacParams.setThresholdflag13(dgtacinfo.getThresholdflag13());
							monDgateacParams.setSendmailflag1(dgtacinfo.getSendmailflag1());
							monDgateacParams.setSendmailflag2(dgtacinfo.getSendmailflag2());
							monDgateacParams.setSendmailflag3(dgtacinfo.getSendmailflag3());
							monDgateacParams.setSendmailflag4(dgtacinfo.getSendmailflag4());
							monDgateacParams.setSendmailflag5(dgtacinfo.getSendmailflag5());
							monDgateacParams.setSendmailflag6(dgtacinfo.getSendmailflag6());
							monDgateacParams.setSendmailflag7(dgtacinfo.getSendmailflag7());
							monDgateacParams.setSendmailflag8(dgtacinfo.getSendmailflag8());
							monDgateacParams.setSendmailflag9(dgtacinfo.getSendmailflag9());
							monDgateacParams.setSendmailflag10(dgtacinfo.getSendmailflag10());
							monDgateacParams.setSendmailflag11(dgtacinfo.getSendmailflag11());
							monDgateacParams.setSendmailflag12(dgtacinfo.getSendmailflag12());
							monDgateacParams.setSendmailflag13(dgtacinfo.getSendmailflag13());
							monDgateacParams.setId(dgtacinfo.getId());
						}
						else
						{
							monDgateacParams.setThresholdflag1(cacheDgtacinfo.getThresholdflag1());
							monDgateacParams.setThresholdflag2(cacheDgtacinfo.getThresholdflag2());
							monDgateacParams.setThresholdflag3(cacheDgtacinfo.getThresholdflag3());
							monDgateacParams.setThresholdflag4(cacheDgtacinfo.getThresholdflag4());
							monDgateacParams.setThresholdflag5(cacheDgtacinfo.getThresholdflag5());
							monDgateacParams.setThresholdflag6(cacheDgtacinfo.getThresholdflag6());
							monDgateacParams.setThresholdflag7(cacheDgtacinfo.getThresholdflag7());
							monDgateacParams.setThresholdflag8(cacheDgtacinfo.getThresholdflag8());
							monDgateacParams.setThresholdflag9(cacheDgtacinfo.getThresholdflag9());
							monDgateacParams.setThresholdflag10(cacheDgtacinfo.getThresholdflag10());
							monDgateacParams.setThresholdflag11(cacheDgtacinfo.getThresholdflag11());
							monDgateacParams.setThresholdflag12(cacheDgtacinfo.getThresholdflag12());
							monDgateacParams.setThresholdflag13(cacheDgtacinfo.getThresholdflag13());
							monDgateacParams.setSendmailflag1(cacheDgtacinfo.getSendmailflag1());
							monDgateacParams.setSendmailflag2(cacheDgtacinfo.getSendmailflag2());
							monDgateacParams.setSendmailflag3(cacheDgtacinfo.getSendmailflag3());
							monDgateacParams.setSendmailflag4(cacheDgtacinfo.getSendmailflag4());
							monDgateacParams.setSendmailflag5(cacheDgtacinfo.getSendmailflag5());
							monDgateacParams.setSendmailflag6(cacheDgtacinfo.getSendmailflag6());
							monDgateacParams.setSendmailflag7(cacheDgtacinfo.getSendmailflag7());
							monDgateacParams.setSendmailflag8(cacheDgtacinfo.getSendmailflag8());
							monDgateacParams.setSendmailflag9(cacheDgtacinfo.getSendmailflag9());
							monDgateacParams.setSendmailflag10(cacheDgtacinfo.getSendmailflag10());
							monDgateacParams.setSendmailflag11(cacheDgtacinfo.getSendmailflag11());
							monDgateacParams.setSendmailflag12(cacheDgtacinfo.getSendmailflag12());
							monDgateacParams.setSendmailflag13(cacheDgtacinfo.getSendmailflag13());
							monDgateacParams.setId(cacheDgtacinfo.getId());
						}
					}
					//不是同一天
					else
					{
						//数据库对象消息时间大于缓存消息时间
						if(timeCompare == -1)
						{
							monDgateacParams.setModifytime(dgtacinfo.getModifytime());
							monDgateacParams.setLoginintm(dgtacinfo.getLoginouttm());
							monDgateacParams.setLoginouttm(dgtacinfo.getLoginouttm());
							monDgateacParams.setDbservtime(dgtacinfo.getDbservtime());
							BeanUtils.copyProperties(monDgateacParams,dgtacinfo);
						}
						else
						{
							continue;
						}
					}
				}
				else
				{
					monDgateacParams.setModifytime(dgtacinfo.getModifytime());
					monDgateacParams.setLoginintm(dgtacinfo.getLoginouttm());
					monDgateacParams.setLoginouttm(dgtacinfo.getLoginouttm());
					monDgateacParams.setDbservtime(dgtacinfo.getDbservtime());
					BeanUtils.copyProperties(monDgateacParams,dgtacinfo);
				}
				gateAccount.put(gateaccount, monDgateacParams);
			}
			// 主机基本信息写入缓存
			MonitorStaticValue.getGateAccountMap().putAll(gateAccount);
			//设置临时缓存
			MonitorStaticValue.getGateAccountMapTemp().clear();
			MonitorStaticValue.getGateAccountMapTemp().putAll(MonitorStaticValue.getGateAccountMap());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置SPGATE动态信息异常，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}
	
	/**
	 * 设置SPGATE缓存动态信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午01:55:38
	 */
	public void setDGateBufInfo()
	{
		try
		{
			//清空缓存
			MonitorStaticValue.getSpgateBufInfoMap().clear();
			// 查询数据库
			List<LfMonDGateBuf> lfMonDGateBufinfoList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonDGateBuf.class, null, null);
			
			Iterator<LfMonDGateBuf> itr = lfMonDGateBufinfoList.iterator();
			LfMonDGateBuf lfMonDGateBufinfo;
			//设置的缓存对象
			MonDgateBufParams monDgateBufParams = null;
			//缓存已有的对象
			MonDgateBufParams cachemonDgateBuf;
			//通道账号
			String gateaccount = "";
			//时间比较，0：为同一天；-1：时间1小于时间2；1：时间1大于时间2
			int timeCompare = 0;
			//缓存的消息时间
			Timestamp cacheDbservtime;
			//数据库对象的消息时间
			Timestamp dbservtime;
			Map<String, MonDgateBufParams>	spgateBufInfo = new ConcurrentHashMap<String, MonDgateBufParams>();
			while(itr.hasNext())
			{
				monDgateBufParams = new MonDgateBufParams();
				lfMonDGateBufinfo = itr.next();
				gateaccount = lfMonDGateBufinfo.getGateaccount();
				
				//缓存中存在
				if(spgateBufInfo.containsKey(gateaccount))
				{
					//缓存对象
					cachemonDgateBuf = spgateBufInfo.get(gateaccount);
					//缓存对象消息时间
					cacheDbservtime = cachemonDgateBuf.getDbservtime();
					//数据库对象消息时间
					dbservtime = lfMonDGateBufinfo.getDbservtime();
					//消息时间比较
					timeCompare = timeStrCompare(cacheDbservtime, dbservtime);
					//消息时间是否同一天
					//消息时间是否为同一天
					if(timeCompare == 0)
					{
						//缓存消息时间大于数据库对象消息时间
						if(cacheDbservtime.after(dbservtime))
						{
							//基础信息以缓存为准
							BeanUtils.copyProperties(monDgateBufParams,cachemonDgateBuf);
						}
						else
						{
							//基础信息以数据库为准
							BeanUtils.copyProperties(monDgateBufParams,lfMonDGateBufinfo);
						}
						//监控数据累加
						monDgateBufParams.setMtupdbuf(cachemonDgateBuf.getMtupdbuf() + lfMonDGateBufinfo.getMtupdbuf());
						monDgateBufParams.setMtsdbuf(cachemonDgateBuf.getMtsdbuf() + lfMonDGateBufinfo.getMtsdbuf());
						monDgateBufParams.setMtsdcnt(cachemonDgateBuf.getMtsdcnt() + lfMonDGateBufinfo.getMtsdcnt());
						monDgateBufParams.setMtspd1(cachemonDgateBuf.getMtspd1() + lfMonDGateBufinfo.getMtspd1());
						monDgateBufParams.setMtspd2(cachemonDgateBuf.getMtspd2() + lfMonDGateBufinfo.getMtspd2());
						monDgateBufParams.setMtrvcnt(cachemonDgateBuf.getMtrvcnt() + lfMonDGateBufinfo.getMtrvcnt());
						monDgateBufParams.setMtsdwaitbuf(cachemonDgateBuf.getMtsdwaitbuf() + lfMonDGateBufinfo.getMtsdwaitbuf());
						monDgateBufParams.setMosdbuf(cachemonDgateBuf.getMosdbuf() + lfMonDGateBufinfo.getMosdbuf());
						monDgateBufParams.setMorvbuf(cachemonDgateBuf.getMorvbuf() + lfMonDGateBufinfo.getMorvbuf());
						monDgateBufParams.setMosdwaitbuf(cachemonDgateBuf.getMosdwaitbuf() + lfMonDGateBufinfo.getMosdwaitbuf());
						monDgateBufParams.setMorptspd(cachemonDgateBuf.getMorptspd() + lfMonDGateBufinfo.getMorptspd());
						monDgateBufParams.setMorvcnt(cachemonDgateBuf.getMorvcnt() + lfMonDGateBufinfo.getMorvcnt());
						monDgateBufParams.setRptsdwaitbuf(cachemonDgateBuf.getRptsdwaitbuf() + lfMonDGateBufinfo.getRptsdwaitbuf());
						monDgateBufParams.setRptsdbuf(cachemonDgateBuf.getRptsdbuf() + lfMonDGateBufinfo.getRptsdbuf());
						monDgateBufParams.setRptrvcnt(cachemonDgateBuf.getRptrvcnt() + lfMonDGateBufinfo.getRptrvcnt());
						monDgateBufParams.setRptrvbuf(cachemonDgateBuf.getRptrvbuf() + lfMonDGateBufinfo.getRptrvbuf());
					}
					//不是同一天
					else
					{
						//数据库对象消息时间大于缓存消息时间
						if(timeCompare == -1)
						{
							monDgateBufParams.setUpdatetime(lfMonDGateBufinfo.getUpdatetime());
							monDgateBufParams.setDbservtime(lfMonDGateBufinfo.getDbservtime());
							BeanUtils.copyProperties(monDgateBufParams,lfMonDGateBufinfo);
						}
						else
						{
							continue;
						}
					}
				}
				else
				{
					monDgateBufParams.setUpdatetime(lfMonDGateBufinfo.getUpdatetime());
					monDgateBufParams.setDbservtime(lfMonDGateBufinfo.getDbservtime());
					BeanUtils.copyProperties(monDgateBufParams,lfMonDGateBufinfo);
				}
				spgateBufInfo.put(lfMonDGateBufinfo.getGateaccount(), monDgateBufParams);
			}
			// 主机基本信息写入缓存
			MonitorStaticValue.getSpgateBufInfoMap().putAll(spgateBufInfo);
			//设置临时缓存
			MonitorStaticValue.getSpgateBufInfoMapTemp().clear();
			MonitorStaticValue.getSpgateBufInfoMapTemp().putAll(MonitorStaticValue.getSpgateBufInfoMap());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置SPGATE缓冲动态信息异常，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}
	
	/**
	 * 设置实时告警信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 下午04:37:34
	 */
	public void setMonErrorInfo()
	{
		try
		{
			//显示记录的条数
			int showCount = 100;
			// 查询数据库
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("id", "desc");
			PageInfo pageInfo = new PageInfo();
			//设置一次查询数量
			pageInfo.setPageSize(showCount);
			List<LfMonErr> monErrorList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonErr.class, null, orderbyMap, pageInfo);
			Iterator<LfMonErr> itr = monErrorList.iterator();
			LfMonErr monError;
			synchronized (MonitorStaticValue.getMonError())
			{
				MonitorStaticValue.getMonError().clear();
			}
			TreeMap<Long ,MonErrorParams> error = new TreeMap<Long ,MonErrorParams>();
			// 将数据库数据刷新至缓存
			while(itr.hasNext())
			{
				if(error.size() < showCount)
				{
					// 创建缓存
					MonErrorParams monErrorParams = new MonErrorParams();
					monError = itr.next();
					monErrorParams.setDealflag(monError.getDealflag());
					monErrorParams.setEvttype(monError.getEvttype());
					monErrorParams.setWho(monError.getWho());
					monErrorParams.setEvttime(monError.getEvttime());
					monErrorParams.setMonstatus(monError.getMonstatus());
					monErrorParams.setProceid(monError.getProceid());
					monErrorParams.setMsg(monError.getMsg());
					monErrorParams.setRcvtime(monError.getRcvtime());
					monErrorParams.setHostid(monError.getHostid());
					monErrorParams.setId(monError.getId());
					monErrorParams.setEvtid(monError.getEvtid());
					monErrorParams.setApptype(monError.getApptype());
					monErrorParams.setSpaccountid(monError.getSpaccountid());
					monErrorParams.setGateaccount(monError.getGateaccount());
					monErrorParams.setDealdesc(monError.getDealdesc());
					monErrorParams.setDealpeople(monError.getDealpeople());
					error.put(monError.getId(), monErrorParams);
				}
				else
				{
					break;
				}
			}
			synchronized (MonitorStaticValue.getMonError())
			{
				// 通道账号基本信息写入缓存
				MonitorStaticValue.getMonError().putAll(error);
			}
			//设置临时缓存
			MonitorStaticValue.getMonErrorTemp().clear();
			MonitorStaticValue.getMonErrorTemp().putAll(MonitorStaticValue.getMonError());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置通道账号基础信息失败，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}
	
	/**
	 * 设置消息中心服务器信息至缓存
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-16 上午10:11:23
	 */
	public void setMqServerBaseInfo()
	{
		try
		{
			String[] mqServerInfo = monitorDAO.getMqServerInfo();
			//设置消息中心服务器URL地址
			if(!"".equals(mqServerInfo[0]))
			{
				StaticValue.setMqServerUrl(mqServerInfo[0]);
			}
			//设置消息队列名
			if(!"".equals(mqServerInfo[1]))
			{
				StaticValue.setMqQueueName(mqServerInfo[1]);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置消息中心服务器信息至缓存异常！");
		}
	}
	
	/**
	 * 设置监控配置信息，包括页面刷新时间 、数据分析间隔时间、网络异常时间间隔
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-11 上午10:58:09
	 */
	public void setMonConfig()
	{
		try
		{
			List<DynaBean> monConfigList = monitorDAO.getMonConfig();
			if(monConfigList != null && monConfigList.size() > 0)
			{
				String globalKey = "";
				String globalValue = "";
				for(DynaBean monConfig : monConfigList)
				{
					globalKey = monConfig.get("globalkey") != null ? monConfig.get("globalkey").toString():null;
					globalValue = monConfig.get("globalvalue") != null ? monConfig.get("globalvalue").toString():null;
					if(globalKey != null && globalValue != null)
					{
						//页面刷新时间
						if("refreshTime".equals(globalKey))
						{
							MonitorStaticValue.setRefreshTime(Integer.parseInt(globalValue));
						}
						//数据分析间隔时间
						else if("timeInterval".equals(globalKey))
						{
							MonitorStaticValue.setTimeInterval(Long.parseLong(globalValue));
						}
						//网络异常时间间隔
						else if("networkRrror".equals(globalKey))
						{
							MonitorStaticValue.setNetworkRrror(Integer.parseInt(globalValue));
						}
						//连续达到告警阀值次数
						else if("CONSECUTIVETIMES".equals(globalKey))
						{
							MonitorStaticValue.setConsecutiveTimes(Integer.parseInt(globalValue));
						}
					}
				}
			}
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e ,"设置监控配置信息，包括页面刷新时间 、数据分析间隔时间、网络异常时间间隔异常！");
		}
	}
	
	/**
	 * 设置通道账号余额
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-7 下午03:05:44
	 */
	public void setGateAccountFee()
	{
		try
		{
			monitorDAO.setGateAccoutFee(true);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置通道账号余额异常！");
		}
	}
	
	/**
	 * 获取WEB服务器名称
	 * @description    
	 * @param ServerInfo       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-13 下午03:18:00
	 */
	public void getServerName(String ServerInfo)
	{
		if("".equals(MonitorStaticValue.getServerName()))
		{
			if(ServerInfo != null && !"".equals(ServerInfo))
			{
				if(ServerInfo.indexOf("Tomcat") > -1)
				{
					MonitorStaticValue.setServerName("Tomcat");
				}
				else if(ServerInfo.indexOf("WebLogic") > -1)
				{
					MonitorStaticValue.setServerName("WebLogic");
				}
				else if(ServerInfo.indexOf("WebSphere") > -1)
				{
					MonitorStaticValue.setServerName("WebSphere");
				}
				else
				{
					MonitorStaticValue.setServerName("Tomcat");
				}
			}
		}
	}
	
	/**
	 * 执行监控定时任务
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-28 上午10:04:54
	 */
	public void executeMonTimeJob()
	{
		Date date = null;
		try
		{
			// 系统当前时间
			Calendar cal = Calendar.getInstance();
			// 系统启动第二天执行定时任务
			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 5, 0, 0);
			// 定时器
			Timer timer = new Timer();
			// 定时任务
			TimerTask timerTask = new TimerTask()
			{
				public void run()
				{
					//删除告警短信发送记录半年前的记录
					monitorDAO.delAlarmSmsSendRecode();
					//删除两个月前业务区域发送数据
					monitorDAO.delBusAreaSend();
					//清队业务区域发送缓存中过期
					//removeBusAreaSendTimeOutCache();
				}
			};
			// 启动定时任务
			timer.schedule(timerTask, date, 86400000L);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行监控定时任务异常！");
		}
	}
	
	/**
	 * 删除半年前告警短信记录定时任务
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-13 上午11:23:04
	 */
	public void executedelAlarmSmsTask()
	{
		Date date = null;
		try
		{
			// 系统当前时间
			Calendar cal = Calendar.getInstance();
			// 系统启动第二天执行定时任务
			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 5, 0, 0);
			// 定时器
			Timer timer = new Timer();
			// 定时任务
			TimerTask timerTask = new TimerTask()
			{
				public void run()
				{
					//删除告警短信发送记录半年前的记录
					monitorDAO.delAlarmSmsSendRecode();
				}
			};
			// 启动定时任务
			timer.schedule(timerTask, date, 86400000L);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行删除告警短信发送记录定时任务异常！");
		}
	}
	
	/**
	 * 清除业务区域发送过期缓存数据
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-31 上午01:54:27
	 */
	public void removeBusAreaSendTimeOutCache()
	{
		try
		{
			int count = MonitorStaticValue.getMon_BusAreaMap().size();
			EmpExecutionContext.info("清除业务区域发送过期缓存数据开始，缓存数据总数："+count);
			if(count > 0)
			{
				//设置状态为未初始化
				MonitorStaticValue.setInitBusAreaSend(false);
				//等待10秒
				Thread.sleep(10L*1000);
				//临时集合
				Map<String, String>	mon_BusAreaMapTemp = new HashMap<String, String>();
				mon_BusAreaMapTemp.putAll(MonitorStaticValue.getMon_BusAreaMap());

				Calendar calendar = Calendar.getInstance();
				//消息小时
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				//小时为0，设置为昨天
				if(hour == 0)
				{
					calendar.add(Calendar.DAY_OF_MONTH, -1);
				}
				//查询日期条件
				int currentDate = Integer.parseInt(format.format(calendar.getTimeInMillis()));
				int data_date = 0;
				// 业务区域发送对象KEY值，格式：时间&业务&区域
				String busAreaSendKey = "";
				for (Entry<String, String> monBusAreaList : mon_BusAreaMapTemp.entrySet())
				{
					// 业务区域发送对象KEY值
					busAreaSendKey = monBusAreaList.getKey().trim();
					if(busAreaSendKey.length() > 8)
					{
						// 业务区域发送对象日期值
						data_date = Integer.parseInt(busAreaSendKey.split("&")[0]);
						//当前时间大于业务区域发送对象日期值
						if(currentDate >  data_date)
						{
							//清除
							MonitorStaticValue.getMon_BusAreaMap().remove(monBusAreaList.getKey());
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "清除业务区域发送过期缓存数据!");
		}
		finally
		{
			//设置状态为初始化完成
			MonitorStaticValue.setInitBusAreaSend(true);
			EmpExecutionContext.info("清除业务区域发送过期缓存数据结束，缓存数据总数："+MonitorStaticValue.getMon_BusAreaMap().size());
		}
	}
	
	/**
	 * 设置业务区域发送数据至缓存
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-30 下午04:37:11
	 */
	public void setBusAreaSendCache()
	{
		try
		{
			Calendar calendar = Calendar.getInstance();
			//消息小时
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			//小时为0，设置为昨天
			if(hour == 0)
			{
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
			//查询日期条件
			int messageDates = Integer.parseInt(format.format(calendar.getTimeInMillis()));
			//根据日期获取业务区域发送数据
			List<LfBusareasend> busareasendList= monitorDAO.getBusAreaSendByDate(messageDates);
			if(busareasendList != null && busareasendList.size() > 0)
			{
				// 业务区域发送对象KEY值，格式：时间&业务&区域
				String busAreaSendKey = "";
				for(LfBusareasend busareasend:busareasendList)
				{
					// 业务区域发送对象KEY值
					busAreaSendKey = busareasend.getDatadate() + "&"+  busareasend.getBuscode() + "&"+ busareasend.getAreacode();
					//设置缓存
					MonitorStaticValue.getMon_BusAreaMap().put(busAreaSendKey, busareasend.getMtsendcount());
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置业务区域发送数据至缓存异常！");
		}
		finally
		{
			//设置状态为已完成
			MonitorStaticValue.setInitBusAreaSend(true);
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
	public void updateBusAreaSend(int messageDate, String busCode, String areaCode, String mtSendCount)
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("buscode", busCode);
			conditionMap.put("areacode", areaCode);
			conditionMap.put("datadate", String.valueOf(messageDate));
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("mtsendcount", mtSendCount);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMap.put("updatetime", format.format(System.currentTimeMillis()));
			empDao.update(LfBusareasend.class, objectMap, conditionMap);
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新业务区域发送数据异常！busCode:" + busCode
											+"，areaCode:"+areaCode
											+"，messageDate:"+messageDate
											+"，mtSendCount:"+mtSendCount);
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
	public void updateBusAreaSend(Connection connection, int messageDate, String busCode, String areaCode, String mtSendCount)
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("buscode", busCode);
			conditionMap.put("areacode", areaCode);
			conditionMap.put("datadate", String.valueOf(messageDate));
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("mtsendcount", mtSendCount);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMap.put("updatetime", format.format(System.currentTimeMillis()));
			empDao.update(connection, true, LfBusareasend.class, objectMap, conditionMap);
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新业务区域发送数据异常！busCode:" + busCode
											+"，areaCode:"+areaCode
											+"，messageDate:"+messageDate
											+"，mtSendCount:"+mtSendCount);
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
	public void updateBusAreaSend(Connection connection, int messageDate, String busCode, String areaCode, String mtSendCount, Long appId)
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("buscode", busCode);
			conditionMap.put("areacode", areaCode);
			conditionMap.put("datadate", String.valueOf(messageDate));
			conditionMap.put("gatewayid", String.valueOf(appId));
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("mtsendcount", mtSendCount);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMap.put("updatetime", format.format(System.currentTimeMillis()));
			empDao.update(connection, true, LfBusareasend.class, objectMap, conditionMap);
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新业务区域发送数据异常！busCode:" + busCode
											+"，areaCode:"+areaCode
											+"，messageDate:"+messageDate
											+"，mtSendCount:"+mtSendCount);
		}
	}
	
	/**
	 * 删除本节点在线用户
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-14 下午03:12:39
	 */
	public void resetOnlineUser()
	{
		try
		{
			String sql = "DELETE FROM LF_MON_ONLUSER WHERE SERVERNUM = '"+ StaticValue.getServerNumber() +"'";
			//new SuperDAO().executeBySQL(sql, StaticValue.EMP_POOLNAME);
			new SuperDAO().executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
			//更新在线用户人数
			setOnlineCfgInfo();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统启动删除本节点在线用户异常！");
		}
	}
	
	/**
	 * 设置告警短信对象
	 * @description    
	 * @param phone
	 * @param msg       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-5 下午02:21:42
	 */
	public void setmonAlarmDsm(String phone, String msg)
	{
		MonAlarmDsmParams monAlarmDsm = null;
		String[] monPhone = null;
		try
		{
			if("".equals(phone) || phone.length() == 0)
			{
				return;
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
				return;
			}
			else
			{
				for(int i= 0; i< monPhone.length; i++)
				{
					monAlarmDsm = new MonAlarmDsmParams();
					monAlarmDsm.setPhone(monPhone[i]);
					monAlarmDsm.setMsg(msg);
					MonitorStaticValue.getMonAlarmDsmList().add(monAlarmDsm);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置告警短信对象集合异常，手机号码为：" + phone);
			return;
		}
	}
	
	/**
     * 更新监控信息到数据库
     * @param obj 实体对象
     * @param key update条件中key值
     */
    public void saveOrUpdate(Object obj,String key)
    {
        GenericEmpTransactionDAO genericEmpTransactionDAO = new GenericEmpTransactionDAO();
        Connection connection = null;
        try {
        	connection = MonDbConnection.getInstance().getConnection();
            empTransDao.beginTransaction(connection);
            boolean up = genericEmpTransactionDAO.updateCon(connection,obj,key);
            if(!up)
            {
                genericEmpTransactionDAO.saveCon(connection,obj);
            }
            empTransDao.commitTransaction(connection);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"更新监控信息异常！key:"+key);
            empTransDao.rollBackTransaction(connection);
        }finally
        {
        	MonDbConnection.getInstance().setConnectionStutes();
        }
    }
    
    /**
	 * 时间比较
	 * @description    
	 * @param time1 比较时间1
	 * @param time2 比较时间2
	 * @return  0：为同一天；-1：时间1小于时间2；1：时间1大于时间2
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-1-30 上午10:42:12
	 */
	public int timeStrCompare(Timestamp time1, Timestamp time2)
	{
		try
		{
			//今天时间
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTimeInMillis(time1.getTime());
			//比较时间
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTimeInMillis(time2.getTime());
			
			//年相等且年中的天相等
			if(calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) 
					&& calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR))
			{
				//同一天
				return 0;
			}
			//在当前时间之前
			else if(calendar1.before(calendar2))
			{
				//小于当天
				return -1;
			}

			else
			{
				//大于当天
				return 1;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "进行时间比较，判断两个时间大小异常，time1:"+time1+"，time2:"+time2);
			return 0;
		}
	}
	
	/**
	 * 新增数据库与程序监控信息
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-23 下午05:56:09
	 */
	public void addDbAndProceMonInfo()
	{
		try
		{
			Map<String, LfMonDbstate> newMonDbstateMap = new HashMap<String, LfMonDbstate>();
			LfMonDbstate newMonDbstate = null;
			Timestamp curDbTime = getDbServerTime(null);
			//查询WEB程序节点基础信息表
			MonitorStaticValue.setNodeBaseInfoList(this.getWebProceBaseInfo());
			if(MonitorStaticValue.getNodeBaseInfoList() != null &&  MonitorStaticValue.getNodeBaseInfoList().size() > 0)
			{
				for(LfNodeBaseInfo lfNodeBaseInfo:MonitorStaticValue.getNodeBaseInfoList())
				{
					newMonDbstate = this.setMonDbstateObject();
					newMonDbstate.setProcename("WEB程序"+lfNodeBaseInfo.getNodeId());
					newMonDbstate.setProcenode(Long.parseLong(lfNodeBaseInfo.getNodeId()));
					newMonDbstate.setProcetype(5000);
					newMonDbstate.setCreatetime(curDbTime);
					newMonDbstate.setUpdatetime(curDbTime);
					newMonDbstate.setDbservtime(curDbTime);
					newMonDbstateMap.put(5000 + lfNodeBaseInfo.getNodeId(), newMonDbstate);
				}
			}
			
			//获取网关程序
			List<GwClustatus> gwClustatusList = this.getGWProceBaseInfo();
			if(gwClustatusList != null &&  gwClustatusList.size() > 0)
			{
				for(GwClustatus gwClustatus:gwClustatusList)
				{
					newMonDbstate = this.setMonDbstateObject();
					newMonDbstate.setProcename("EMP网关(EMP_GW)"+gwClustatus.getGwno());
					newMonDbstate.setProcenode((long)gwClustatus.getGwno());
					newMonDbstate.setProcetype(5200);
					newMonDbstate.setCreatetime(curDbTime);
					newMonDbstate.setUpdatetime(curDbTime);
					newMonDbstate.setDbservtime(curDbTime);
					newMonDbstateMap.put("5200" + gwClustatus.getGwno(), newMonDbstate);
				}
			}
			//获取SPGATE程序
			List<DynaBean> spgateList = getSPGateProceBaseInfo();
			if(spgateList != null &&  spgateList.size() > 0)
			{
				String gwno = "0";
				for(DynaBean spgate:spgateList)
				{
					newMonDbstate = this.setMonDbstateObject();
					if(spgate.get("gwno") == null)
					{
						EmpExecutionContext.error("新增数据库与程序监控信息，获取SPGATE程序编码为空");
						continue;
					}
					gwno = spgate.get("gwno").toString();
					newMonDbstate.setProcename("运营商接口(SPGATE)"+gwno);
					newMonDbstate.setProcenode(Long.parseLong(gwno));
					newMonDbstate.setProcetype(5300);
					newMonDbstate.setCreatetime(curDbTime);
					newMonDbstate.setUpdatetime(curDbTime);
					newMonDbstate.setDbservtime(curDbTime);
					newMonDbstateMap.put("5300" + gwno, newMonDbstate);
				}
			}
			
			//查询数据库与程序监控信息表获取已有的程序
			Map<String, LfMonDbstate> lfMonDbstateMap = MonitorStaticValue.getDbMonMap();
			if(lfMonDbstateMap != null && lfMonDbstateMap.size() > 0)
			{
				for(String key : lfMonDbstateMap.keySet())
				{
					if(newMonDbstateMap.containsKey(key))
					{
						newMonDbstateMap.remove(key);
					}
				}
//				String key = "";
//				for(LfMonDbstate lfMonDbstate : lfMonDbstateList)
//				{
//					key = String.valueOf(lfMonDbstate.getProcetype())+lfMonDbstate.getProcenode();
//					if(newMonDbstateMap.containsKey(key))
//					{
//						newMonDbstateMap.remove(key);
//					}
//				}
			}
			//已存在的不新增
			if(newMonDbstateMap != null && newMonDbstateMap.size() > 0)
			{
				List<LfMonDbstate> newMonDbstateList = new ArrayList<LfMonDbstate>();
				 for (LfMonDbstate MonDbstate : newMonDbstateMap.values()) 
				 {
					 newMonDbstateList.add(MonDbstate);
				 }
				 this.addDbProcestate(newMonDbstateList);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "新增数据库与程序监控信息失败！");
		}
		
	}
	
	/**
	 * 设置数据库状态对象
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-23 下午06:22:00
	 */
	public LfMonDbstate setMonDbstateObject()
	{
		try
		{
			LfMonDbstate LfMonDbstate = new LfMonDbstate();
			LfMonDbstate.setMonstatus(1);
			LfMonDbstate.setEvttype(0);
			LfMonDbstate.setDbid(0L);
			LfMonDbstate.setDbname(" ");
			LfMonDbstate.setDbconnectstate(0);
			LfMonDbstate.setAddopr(0);
			LfMonDbstate.setDbaddoprdes(" ");
			LfMonDbstate.setDelopr(0);
			LfMonDbstate.setDbdeloprdes(" ");
			LfMonDbstate.setModiopr(0);
			LfMonDbstate.setDbmodioprdes(" ");
			LfMonDbstate.setDispopr(0);
			LfMonDbstate.setDbdispoprdes(" ");
			LfMonDbstate.setSmsalflag1(0L);
			LfMonDbstate.setSmsalflag2(0L);
			LfMonDbstate.setSmsalflag3(0L);
			LfMonDbstate.setSmsalflag4(0L);
			LfMonDbstate.setSmsalflag5(0L);
			LfMonDbstate.setMailalflag1(0L);
			LfMonDbstate.setMailalflag2(0L);
			LfMonDbstate.setMailalflag3(0L);
			LfMonDbstate.setMailalflag4(0L);
			LfMonDbstate.setMailalflag5(0L);
			LfMonDbstate.setServerNum(StaticValue.getServerNumber());
			return LfMonDbstate;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置数据库状态对象失败");
			return null;
		}
	}
	
	/**
	 * 查询WEB程序节点基础信息表
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-23 下午04:49:46
	 */
	public List<LfNodeBaseInfo> getWebProceBaseInfo()
	{
		List<LfNodeBaseInfo> lfNodeBaseInfoList = null;
		try
		{
			lfNodeBaseInfoList = baseBiz.getByCondition(LfNodeBaseInfo.class, null, null);
			return lfNodeBaseInfoList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询WEB程序节点基础信息失败！");
			return null;
		}
		
	}
	/**
	 * 查询集群网关运行状态表
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-23 下午05:50:56
	 */
	public List<GwClustatus> getGWProceBaseInfo()
	{
		List<GwClustatus> gwClustatusList = null;
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("gwtype", "4000");
			gwClustatusList = baseBiz.getByCondition(GwClustatus.class, conditionMap, null);
			return gwClustatusList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取网关程序节点编号失败！");
			return null;
		}
	}
	
	/**
	 * 获取SPGATE程序
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-8-6 下午03:49:50
	 */
	public List<DynaBean> getSPGateProceBaseInfo()
	{
		try
		{
			List<DynaBean> beanList = null;
			String UID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"";
			String sql = "select bind.gwno  from userdata userdata " +
					"left join gw_cluspbind bind on userdata."+UID+" = bind.ptaccuid " +
							"left join gw_clustatus status on bind.gwno = status.gwno " +
							"where userdata.usertype=1 and userdata.accounttype=1 order by bind.gwno";
			beanList = monitorDAO.getListDynaBeanBySql(sql);
			return beanList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取SPGATE程序节点编号失败！");
			return null;
		}
	}
	
	/**
	 * 查询数据库与程序监控信息表获取已有的程序
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-23 下午05:51:47
	 */
	public List<LfMonDbstate> getDbProcestateInfo()
	{
		List<LfMonDbstate> lfMonDbstateList = null;
		try
		{
			lfMonDbstateList = baseBiz.getByCondition(LfMonDbstate.class, null, null);
			return lfMonDbstateList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询数据库与程序监控信息表获取已有的程序失败！");
			return null;
		}
	}
	
	/**
	 * 新增数据库与程序监控信息
	 * @description    
	 * @param monDbstateList       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-23 下午05:52:03
	 */
	public void addDbProcestate(List<LfMonDbstate>monDbstateList)
	{
		try
		{
			baseBiz.addList(LfMonDbstate.class, monDbstateList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "新增数据库与程序监控信息失败！");
		}
	}
	
	/**
	 * 设置数据库监控状态缓存
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-25 下午04:59:16
	 */
	public void setDataBaseMonInfo()
	{
		try
		{
			//清空缓存
			MonitorStaticValue.getDbMonMap().clear();
			// 查询数据库
			List<LfMonDbstate> lfMonDbstateList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonDbstate.class, null, null);
			
			Iterator<LfMonDbstate> itr = lfMonDbstateList.iterator();
			LfMonDbstate monDbstate;
			while(itr.hasNext())
			{
				monDbstate = itr.next();
				// 主机基本信息写入缓存
				MonitorStaticValue.getDbMonMap().put(String.valueOf(monDbstate.getProcetype())+monDbstate.getProcenode(), monDbstate);
			}
			//设置临时缓存
			MonitorStaticValue.getDbMonMapTemp().clear();
			MonitorStaticValue.getDbMonMapTemp().putAll(MonitorStaticValue.getDbMonMap());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置数据库监控状态缓存异常！");
		}
	}
	
	/**
	 * 设置主机网络监控信息
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-28 上午08:41:32
	 */
	public void setHostNetMonInfo()
	{
		try
		{
			//清空缓存
			MonitorStaticValue.getHostNetMap().clear();
			// 查询数据库
			List<LfMonHostnet> lfMonHostnetList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonHostnet.class, null, null);
			
			Iterator<LfMonHostnet> itr = lfMonHostnetList.iterator();
			LfMonHostnet monHostnet;
			while(itr.hasNext())
			{
				monHostnet = itr.next();
				// 主机基本信息写入缓存
				MonitorStaticValue.getHostNetMap().put(String.valueOf(monHostnet.getWebnode())+monHostnet.getProcenode()+monHostnet.getProcetype(), monHostnet);
			}
			//设置临时缓存
			MonitorStaticValue.getHostNetMapTemp().clear();
			MonitorStaticValue.getHostNetMapTemp().putAll(MonitorStaticValue.getHostNetMap());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置主机网络监控状态缓存异常！");
		}
	}
	
	/**
	 * 获取网络连接状态
	 * @description    
	 * @param ipAddr
	 * @return  true 网络正常；false 网络异常     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-29 下午02:03:12
	 */
	public boolean getHostNetState(String ipAddr)
	{
		boolean isUnobstructed = false;
		try
		{
			String[] ipAddrs = null;
			//多个IP
			if(ipAddr.indexOf(",") > -1)
			{
				ipAddrs = ipAddr.split(",");
			}
			//一个IP
			else
			{
				ipAddrs = new String[1];
				ipAddrs[0] = ipAddr;
			}
			for(int i=0; i<ipAddrs.length; i++)
			{
				//IP合法
				if(isValidIP(ipAddrs[i]))
				{
					//检查网络连接
					isUnobstructed = InetAddress.getByName(ipAddrs[i]).isReachable(2000);
					if(isUnobstructed)
					{
						return isUnobstructed;
					}
				}
			}
			return isUnobstructed;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取网络连接状态失败！ipAddr:"+ipAddr);
			return false;
		}
	}
	
	/**
	 * 获取合法的IP地址
	 * @description    
	 * @param nodeBaseInfo
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-3 上午11:27:51
	 */
	public String getValidIp(LfNodeBaseInfo nodeBaseInfo)
	{
		try
		{
			//IP
			String ip = nodeBaseInfo.getServerIP()==null?"":nodeBaseInfo.getServerIP().trim();
			if("127.0.0.1".equals(ip) || "0.0.0.0".equals(ip))
			{
				ip = "";
			}
			//url
			String url = nodeBaseInfo.getSerLocalURL();
			
			if(url != null && !"".equals(url.trim()) )
			{
				String ipAddr = getIpByUrl(url);
				if(!"".equals(ipAddr) && ip.indexOf(ipAddr) < 0)
				{
					if(!"127.0.0.1".equals(ip) && !"0.0.0.0".equals(ip))
					{
						if(!"".equals(ip))
						{
							ip += ",";
						}
						ip += ipAddr;
					}
				}
			}
			if(ip.length() > 240)
			{
				return ip.substring(0, 240);
			}
			return ip;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取合法的IP地址失败！ip:"+nodeBaseInfo.getServerIP()+"，url:"+nodeBaseInfo.getSerLocalURL());
			return "";
		}
	}
	
	/**
	 * 获取合法的IP地址
	 * @description    
	 * @param ipAddr  消息中的IP
	 * @param ipAddr  缓存中的IP
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-29 下午02:32:27
	 */
	public String getValidIp(String ipAddr, String dIpAddr)
	{
		StringBuffer validIp = new StringBuffer();
		try
		{
			String[] ipAddrs = null;
			//多个IP
			if(ipAddr.indexOf(",") > -1)
			{
				ipAddrs = ipAddr.split(",");
			}
			//一个IP
			else
			{
				ipAddrs = new String[1];
				ipAddrs[0] = ipAddr;
			}
			for(int i=0; i<ipAddrs.length; i++)
			{
				//在缓存中不存在，且IP合法
				if(dIpAddr == null || dIpAddr.indexOf(ipAddrs[i]) < 0)
				{
					if(isValidIP(ipAddrs[i]))
					{
						validIp.append(ipAddrs[i]).append(",");
					}
				}
			}
			if(validIp.length() > 0)
			{
				validIp.deleteCharAt(validIp.lastIndexOf(","));
			}
			return validIp.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取合法的IP地址失败！ipAddr:"+ipAddr);
			return validIp.toString();
		}
	}
	
	/**
	 * IP校验
	 * @description    
	 * @param ipAddr IP地址
	 * @return  true:合法；false:不合法     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-29 下午02:04:12
	 */
	public boolean isValidIP(String ipAddr)
    {
      try
		{
			if(ipAddr == null || ipAddr.length() < 7 || ipAddr.length() > 15 || "".equals(ipAddr) 
					|| "127.0.0.1".equals(ipAddr) || "0.0.0.0".equals(ipAddr))
			  {
				EmpExecutionContext.error("IP地址合法性验证不通过！ipAddr:"+ipAddr);
			    return false;
			  }
			  /**
			   * 判断IP格式和范围
			   */
			  String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
			  
			  Pattern pat = Pattern.compile(rexp);  
			  
			  Matcher mat = pat.matcher(ipAddr);  
			  
			  boolean ipAddress = mat.find();
	
			  return ipAddress;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "IP地址合法性验证失败！ipAddr:"+ipAddr);
			return false;
		}
    }
	
	/**
	 * 设置主机网络监控
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-3 上午11:39:32
	 */
	public void addHostNetMonInfo()
	{
		try
		{
			if(MonitorStaticValue.getNodeBaseInfoList() != null &&  MonitorStaticValue.getNodeBaseInfoList().size() > 0)
			{
				Timestamp curDbTime = getDbServerTime(null);
				List<LfMonHostnet> monHostnetList = new ArrayList<LfMonHostnet>();
				LfMonHostnet monHostnet = null; 
				//查询数据库与程序监控信息表获取已有的程序
				//Map<String, LfMonHostnet> monHostnetMap = MonitorStaticValue.hostNetMap;
				Map<String, LfMonHostnet> monHostnetMap = new HashMap<String, LfMonHostnet>();
				// 查询数据库
				List<LfMonHostnet> lfMonHostnetList = baseBiz.getByCondition(LfMonHostnet.class, null, null);
				Iterator<LfMonHostnet> itr = lfMonHostnetList.iterator();
				while(itr.hasNext())
				{
					monHostnet = itr.next();
					// 主机基本信息写入缓存
					monHostnetMap.put(String.valueOf(monHostnet.getWebnode())+monHostnet.getProcenode()+monHostnet.getProcetype(), monHostnet);
				}
				
				//key值
				String key;
				//ip
				String ipAddr = "";
				for(LfNodeBaseInfo nodeBaseInfo:MonitorStaticValue.getNodeBaseInfoList())
				{
					//节点为不为本机
					if(!StaticValue.getServerNumber().equals(nodeBaseInfo.getNodeId()))
					{
						//获取KEY值
						ipAddr = getValidIp(nodeBaseInfo);
						if(ipAddr != null && !"".equals(ipAddr))
						{
							key = StaticValue.getServerNumber() + nodeBaseInfo.getNodeId() + "5000";
							//缓存中不存在则新增
							if(!monHostnetMap.containsKey(key))
							{
								monHostnet = this.setMonHostnetObject();
								monHostnet.setWebnode(Long.parseLong(StaticValue.getServerNumber()));
								monHostnet.setWebname("WEB服务器主机"+ StaticValue.getServerNumber());
								monHostnet.setProcenode(Long.parseLong(nodeBaseInfo.getNodeId()));
								monHostnet.setHostname("WEB服务器主机"+nodeBaseInfo.getNodeId());
								monHostnet.setIpaddr(ipAddr);
								monHostnet.setProcetype(5000);
								monHostnet.setCreatetime(curDbTime);
								monHostnet.setUpdatetime(curDbTime);
								monHostnet.setDbservtime(curDbTime);
								monHostnetList.add(monHostnet);
							}
							//存在，IP不一致则更新
							else
							{
								if(!ipAddr.equals(monHostnetMap.get(key).getIpaddr().trim()))
								{
									monHostnet = new LfMonHostnet();
									monHostnet.setId(monHostnetMap.get(key).getId());
									monHostnet.setIpaddr(ipAddr);
									monHostnet.setUpdatetime(curDbTime);
									baseBiz.updateObj(monHostnet);
								}
							}
						}
					}
				}
				if(monHostnetList != null && monHostnetList.size() > 0)
				{
					//更新
					baseBiz.addList(LfMonHostnet.class, monHostnetList);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置主机网络监控失败！");
		}
	}
	
	
	/**
	 * 通过URL截取IP
	 * @description    
	 * @param url
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-3 上午10:58:51
	 */
	public String getIpByUrl(String url)
	{
		try
		{
			if(url != null && !"".equals(url.trim()))
			{
				String rexp = "\\d{1,3}(\\.\\d{1,3}){3}";
			    Pattern pat = Pattern.compile(rexp);  
				  
				Matcher mat = pat.matcher(url); 
				mat.find();
				String ip = mat.group();
				if(isValidIP(ip))
				{
					return ip;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过URL获取IP失败！url:"+url);
		}
		return "";
	}
	/**
	 * 设置主机网络监控对象
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-3 上午10:44:09
	 */
	public LfMonHostnet setMonHostnetObject()
	{
		LfMonHostnet monHostnet = new LfMonHostnet();
		monHostnet.setMontype(0);
		monHostnet.setMonstatus(1);
		monHostnet.setEvttype(0);
		monHostnet.setNetstate(0);
		monHostnet.setSmsalflag1(0L);
		monHostnet.setMailalflag1(0L);
		monHostnet.setServernum(StaticValue.getServerNumber());
		return monHostnet;
	}

	/**
	 * 根据节点获取通道账户信息
	 * @description    
	 * @param appId
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-6 下午03:29:00
	 */
	public String[] getGateAccountInfoByGwno(Long appId)
	{
		try
		{
			String[] gateAccountInfo = new String[2];
			List<AgwAccount> agwAccountList = monitorDAO.getGateAccountInfoByGwno(appId);
			if(agwAccountList != null && agwAccountList.size() > 0)
			{
				gateAccountInfo[0] = agwAccountList.get(0).getPtAccId();
				gateAccountInfo[1] = agwAccountList.get(0).getPtAccName();
				return gateAccountInfo;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据节点获取通道账户信息失败，appId:"+appId);
		}
		return null;
	}
	
	/**
	 * 获取数据库当前时间
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-16 下午02:03:17
	 */
	public Timestamp getDbServerTime(Connection connection)
	{
		String dbServerTime = monitorDAO.getDbServerTime(connection);
		return timeChange(dbServerTime);
	}
	
	/**
	 * 字符串转换时间格式
	 *
	 * @description
	 * @param time
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-31 上午11:37:49
	 */
	public Timestamp timeChange(String time)
	{
		Timestamp ts = null;
		try
		{
			Format format;
			if(time.indexOf(".") >= 0)
			{
				time = time.substring(0, time.indexOf("."));
			}
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = (Date) format.parseObject(time);
			ts = new Timestamp(d.getTime());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "字符串转换时间格式异常。time:"+time);
			ts = null;
		}
		return ts;
	}
}
