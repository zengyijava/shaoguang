/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-2 下午02:33:56
 */
package com.montnets.emp.monitor.biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.monitor.LfMonDbstate;
import com.montnets.emp.entity.monitor.LfMonDbwarn;
import com.montnets.emp.entity.monitor.LfMonDgtacinfo;
import com.montnets.emp.entity.monitor.LfMonDhost;
import com.montnets.emp.entity.monitor.LfMonDproce;
import com.montnets.emp.entity.monitor.LfMonDspacinfo;
import com.montnets.emp.entity.monitor.LfMonErr;
import com.montnets.emp.entity.monitor.LfMonHnetwarn;
import com.montnets.emp.entity.monitor.LfMonHostnet;
import com.montnets.emp.entity.monitor.LfMonSgtacinfo;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.entity.monitor.LfMonSspacinfo;
import com.montnets.emp.entity.monitor.LfSpoffctrl;
import com.montnets.emp.entity.monitor.LfSpofflineprd;
import com.montnets.emp.entity.monitoronline.LfMonOnlcfg;
import com.montnets.emp.monitor.constant.MonDgateacParams;
import com.montnets.emp.monitor.constant.MonDhostParams;
import com.montnets.emp.monitor.constant.MonDproceParams;
import com.montnets.emp.monitor.constant.MonDspAccountParams;
import com.montnets.emp.monitor.constant.MonEmailParams;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.monitor.dao.MonitorDAO;
import com.montnets.emp.security.blacklist.BlackListAtom;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-2 下午02:33:56
 */

public class MonDataAnalysisBiz extends Thread
{
	// 系统启动后开始分析数据时间,默认为2分钟
	private Long					firstRunTime	= 1 * 60 * 1000L;

	private MonErrorInfo			monErrorInfo	= new MonErrorInfo();

	private MonParamsAnalysisBiz	monParams		= new MonParamsAnalysisBiz();

	private BlackListAtom			blackListAtom	= new BlackListAtom();
	
	private BaseBiz baseBiz = new BaseBiz();
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private MonitorBaseInfoBiz monBaseInfo = new MonitorBaseInfoBiz();
	
	private MonEmpInfoBiz monEmpInfoBiz = new MonEmpInfoBiz();
	
	private AccoutBaseInfoBiz accoutBaseInfoBiz = new AccoutBaseInfoBiz();
	
	private MonitorDAO monitorDAO = new MonitorDAO();
	
	private MonitorBaseInfoBiz monitorBaseInfoBiz = new MonitorBaseInfoBiz();
	
	private String corpcode ="100001";
	
	private long monThreadId = 0;
	
	private boolean isLoadModule = false;
	
	public MonDataAnalysisBiz()
	{
		this.setName("监控数据分析线程ID"+this.getId());
		monThreadId = this.getId();
	}
	
	/**
	 * 数据分析
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-7 下午07:11:44
	 */
	public void monDataAnalysis()
	{
		try
		{
			// 重置告警号码和短信内容
			MonitorStaticValue.getMonAlarmDsmList().clear();
			//获取数据库服务器时间
			MonitorStaticValue.setCurDbServerTime(monBaseInfo.getDbServerTime(MonDbConnection.getInstance().getConnection()));
			// 设置EMP主机程序信息
			monEmpInfoBiz.setEmpHostAndPrecoInfo();
			// 设置SP账号和通道账号信息
			accoutBaseInfoBiz.getSpAndGateInfo();
			// 设置监控缓存信息
			getMonitorBaseInfo(1);
			// 分析主机网络
			hostNetDataAnalysis();
			// 分析数据库状态
			dbStateDataAnalysis();
			// 分析主机数据
			hostDataAnalysis();
			// 分析程序数据
			proceDataAnalysis();
			// 分析SP账号数据
			spAccoutDataAnalysis();
			// 分析通道账号数据
			gateAccoutDataAnalysis();
			// 分析在线用户数
			onlineUserDataAnalysis();
			// 加载网优模块，定时扫库,符合重发条件的记录使用网优发送
			if(isLoadModule)
			{
				monSmsResend();
			}
			// 设置监控缓存信息
			getMonitorBaseInfo(1);
			// 发送告警短信
			if(MonitorStaticValue.getMonAlarmDsmList() != null && MonitorStaticValue.getMonAlarmDsmList().size() > 0)
			{
				new Thread(new sendAlarmSms(null, null, null, 1)).start();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控数据分析异常，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}finally
		{
			//关闭数据库连接
			MonDbConnection.getInstance().closeConnection();
		}
	}

	/**
	 * 主机网络状态分析
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-28 上午11:27:26
	 */
	public void hostNetDataAnalysis()
	{
		try
		{
			// 邮件告警内容
			StringBuffer emailContent = new StringBuffer("");
			//告警短信内容
			String msg = "";
			// 告警来源 11：主机网络
			int alarmSource = 11;
			// 告警状态
			int evtType = 0;
			//接收时间间隔
//			Long receiveInterval = 0L;
			// 从缓存中获取所有主机动态信息
			Map<String, LfMonHostnet> monHostnetinfo = MonitorStaticValue.getHostNetMap();
			if(monHostnetinfo != null && monHostnetinfo.size() > 0)
			{
				List<LfMonHnetwarn> monHnetwarnList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonHnetwarn.class, null, null);
				// 告警手机
				String monPhone = null;
				// 有效的告警邮箱
				String effEmail = null;
				if(monHnetwarnList != null && monHnetwarnList.size() > 0)
				{
					// 有效的告警邮箱
					effEmail = retEffEmail(monHnetwarnList.get(0).getMonemail());
					// 告警手机
					monPhone = monHnetwarnList.get(0).getMonphone();
				}
				// 有效告警手机号
				String effPhone = null;
				// 告警手机
				if(monPhone != null)
				{
					// 有效号码,返回null表示无有效号码
					effPhone = checkMobleVaild(monPhone.trim());
				}
				// 错误告警信息
				LfMonErr lfMonErr = null;
				long curDbTime = MonitorStaticValue.getCurDbServerTime().getTime();
				// 设置告警基本信息
				for(LfMonHostnet monHostnet : monHostnetinfo.values())
				{
					if(monHostnet.getServernum() != null && StaticValue.getServerNumber().equals(String.valueOf(monHostnet.getWebnode())))
					{
						// 告警状态
						evtType = 0;
						lfMonErr = new LfMonErr();
						monErrorInfo.setHostErrObjectInfo(lfMonErr, 0L, 0L, "0", "0", monHostnet.getProcenode(), monHostnet.getWebnode(), 3100, monHostnet.getMonstatus());
						//开启监控
						if(monHostnet.getMonstatus() == 1)
						{
//							// 更新缓存数据时间
//							Timestamp upateTime = monHostnet.getDbservtime();
//							// 设置告警基本信息
//							receiveInterval = (curDbTime - upateTime.getTime()) / 1000;
//							// 超过设置更新缓存数据时间
//							if(receiveInterval >= MonitorStaticValue.networkRrror)
//							{
								String ipAddr = monHostnet.getIpaddr();
								//根据IP检查网络情况,true网络正常
								if(!monitorBaseInfoBiz.getHostNetState(ipAddr))
								{
									// 处理状态不是处理中
									if(monHostnet.getSmsalflag1() != -1)
									{
										msg = "与"+monHostnet.getWebname()+"之间通讯异常！";
										//告警短信
										if(monHostnet.getSmsalflag1() == 0)
										{
											// 设置短信告警及邮件告警标识
											monErrorInfo.setAlarmSmsAndMailFlag(alarmSource, monHostnet.getId(), "1", curDbTime, false);
										}
										else if(monHostnet.getSmsalflag1() == 1)
										{
											// 向告警表写入告警数据
											monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 1, 2, alarmSource, monHostnet.getUpdatetime());
											// 设置告警标识为2严重
											evtType = 2;
										}
										else if(curDbTime - monHostnet.getSmsalflag1() >= MonitorStaticValue.getNetworkRrror()*1000)
										{
											// 向告警表写入告警数据
											monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 1, 2, alarmSource, monHostnet.getUpdatetime());
											// 设置告警状态为已发送告警
											if(effPhone != null && monErrorInfo.setAlarmSmsFlag(alarmSource, monHostnet.getId(), "1", 1, null))
											{
												//设置告警短信信息
												String monSendContent = "主机名称:" +  monHostnet.getHostname() + "，主机运行程序节点:" +  monHostnet.getProcenode() +  "，" + msg;
												monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
											}
											// 设置告警标识为2严重
											evtType = 2;
										}
										if(evtType == 2)
										{
											// 设置告警标识为2严重
											monErrorInfo.setMonStatu(alarmSource, 1, 2, null, monHostnet.getId(), null);
											//邮件告警
											if(curDbTime - monHostnet.getMailalflag1() >= MonitorStaticValue.getNetworkRrror()*1000)
											{
												// 设置邮件告警状态为已发送告警
												if(effEmail != null && monErrorInfo.setAlarmSmsFlag(21, monHostnet.getId(), "1", 1, null))
												{
													emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
													.append("监控类型：主机网络监控").append("<br/>")
													.append("监控名称："+monHostnet.getHostname()).append("<br/>")
													.append("告警级别：严重").append("<br/>")
													.append("事件描述："+msg).append("<br/>")
													.append("告警时间："+sdf.format(new Date())).append("<br/>");
													//设置邮件内容参数
													MonEmailParams mep = new MonEmailParams();
													mep.setContent(emailContent.toString());
													mep.setCorpCode(corpcode);
													mep.setEmail(effEmail);
													// 发送告警邮件
													new Thread(new sendAlarmEmail( mep)).start();
													//清空
													emailContent.setLength(0);
												}
											}
										}
										EmpExecutionContext.info("主机名称:" + monHostnet.getHostname() + "，主机运行程序节点:" + monHostnet.getProcenode() 
												+ "，"+msg+"，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
									}
									
								}
								else
								{
									// 设置短信、邮件告警标识和状态为0正常
									monErrorInfo.setMonStatu(alarmSource, 0, 0, "1", monHostnet.getId(), null);
								}
							}
							else
							{
								// 设置短信、邮件告警标识和状态为0正常
								monErrorInfo.setMonStatu(alarmSource, 0, 0, "1", monHostnet.getId(), null);
							}
						}
					}
//				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "主机网络状态数据分析异常，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	
	}
	
	/**
	 * 数据库状态分析
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-23 下午07:52:23
	 */
	public void dbStateDataAnalysis()
	{
		try
		{
			// 告警短信内容
			StringBuffer monContent = new StringBuffer("");
			// 邮件告警内容
			StringBuffer emailContent = new StringBuffer("");
			//告警短信内容
			String msg = "";
			// 告警来源 10：数据库
			int alarmSource = 10;
			// 告警状态
			int evtType = 0;
			// 操作告警状态
			int oprStateEvtType = 0;
			// 从缓存中获取所有主机动态信息
			Map<String, LfMonDbstate> dbMoninfo = MonitorStaticValue.getDbMonMap();
			if(dbMoninfo != null && dbMoninfo.size() > 0)
			{
				List<LfMonDbwarn> monDbwarnList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonDbwarn.class, null, null);
				// 告警手机
				String monPhone = null;
				// 有效的告警邮箱
				String effEmail = null;
				if(monDbwarnList != null && monDbwarnList.size() > 0)
				{
					// 有效的告警邮箱
					effEmail = retEffEmail(monDbwarnList.get(0).getMonemail());
					// 告警手机
					monPhone = monDbwarnList.get(0).getMonphone();
				}
				// 有效告警手机号
				String effPhone = null;
				// 告警手机
				if(monPhone != null)
				{
					// 有效号码,返回null表示无有效号码
					effPhone = checkMobleVaild(monPhone.trim());
				}
				// 错误告警信息
				LfMonErr lfMonErr = null;
				long curDbTime = MonitorStaticValue.getCurDbServerTime().getTime();
				// 设置告警基本信息
				for(LfMonDbstate monDbstate : dbMoninfo.values())
				{
					if(monDbstate.getServerNum() != null && StaticValue.getServerNumber().equals(monDbstate.getServerNum()))
					{
						monContent.setLength(0);
						// 告警状态
						evtType = 0;
						// 操作告警状态
						oprStateEvtType = 0;
						lfMonErr = new LfMonErr();
						monErrorInfo.setHostErrObjectInfo(lfMonErr, 0L, 0L, "0", "0", monDbstate.getProcenode(), 0L, 5100, monDbstate.getMonstatus());
						//开启监控
						if(monDbstate.getMonstatus() == 1)
						{
							//连接状态为1：断开
							if(monDbstate.getDbconnectstate() == 1)
							{
								// 连接处理状态不是处理中
								if(monDbstate.getSmsalflag1() != -1)
								{
									msg = "数据库连接异常。";
									//告警短信
									if(monDbstate.getSmsalflag1() == 0)
									{
										// 设置短信告警及邮件告警标识
										monErrorInfo.setAlarmSmsAndMailFlag(alarmSource, monDbstate.getId(), "1", curDbTime, false);
									}
									else if(monDbstate.getSmsalflag1() == 1)
									{
										// 向告警表写入告警数据
										monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 1, 2, alarmSource, monDbstate.getUpdatetime());
										// 设置告警标识为2严重
										evtType = 2;
									}
									else if(curDbTime - monDbstate.getSmsalflag1() >= MonitorStaticValue.getNetworkRrror()*1000)
									{
										// 向告警表写入告警数据
										monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 1, 2, alarmSource, monDbstate.getUpdatetime());
										// 设置告警状态为已发送告警
										if(effPhone != null && monErrorInfo.setAlarmSmsFlag(alarmSource, monDbstate.getId(), "1", 1, null))
										{
											//设置告警短信信息
											monContent.append(msg);
										}
										// 设置告警标识为2严重
										evtType = 2;
									}
									//邮件告警
									if(evtType == 2 && curDbTime - monDbstate.getMailalflag1() >= MonitorStaticValue.getNetworkRrror()*1000)
									{
										// 设置邮件告警状态为已发送告警
										if(effEmail != null && monErrorInfo.setAlarmSmsFlag(20, monDbstate.getId(), "1", 1, null))
										{
											emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
											.append("监控类型：数据库监控").append("<br/>")
											.append("监控名称："+monDbstate.getProcename()).append("<br/>")
											.append("告警级别：严重").append("<br/>")
											.append("事件描述："+msg).append("<br/>")
											.append("告警时间："+sdf.format(new Date())).append("<br/>");
											//设置邮件内容参数
											MonEmailParams mep = new MonEmailParams();
											mep.setContent(emailContent.toString());
											mep.setCorpCode(corpcode);
											mep.setEmail(effEmail);
											// 发送告警邮件
											new Thread(new sendAlarmEmail( mep)).start();
											//清空
											emailContent.setLength(0);
										}
									}
									EmpExecutionContext.info("程序名称:" + monDbstate.getProcename() 
											+ "，程序节点:" + monDbstate.getProcenode() + "，"+msg
											+"，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
								}
							}
							else
							{
								// 设置短信、邮件告警标识和状态为0正常
								monErrorInfo.setMonStatu(alarmSource, null, 0, "1", monDbstate.getId(), null);
							}
							//数据库操作数据分析
							oprStateEvtType = monParams.dbOprStateDataAnalysis(monDbstate, lfMonErr, effPhone, effEmail, monContent);
							if(evtType == 2 || oprStateEvtType == 2)
							{
								monErrorInfo.setMonStatu(alarmSource, null, 2, null, monDbstate.getId(), null);
							}
							// 发送告警短信
							if(monContent.length() > 0)
							{
								// 告警内容
								String monSendContent = "程序名称:" + monDbstate.getProcename() + "，程序节点:" + monDbstate.getProcenode() +  "，" + monContent;
								monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "数据库状态数据分析异常，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}
	
	
	/**
	 * 分析主机数据
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-3 上午09:07:27
	 */
	public void hostDataAnalysis()
	{
		// 主机基础信息
		LfMonShost monShost = null;
		// 主机动态信息
		MonDhostParams monDhost = null;
		// 事件内容
		String msg = "";
		// 告警短信内容
		String smsContent = "";
		//告警邮件内容
		StringBuffer emailContent = new StringBuffer();
		// 告警手机
		String monPhone = "";
		//告警邮件
		String monEmail = "";
		// 告警来源 1：主机
		int AlarmSource = 1;
		try
		{
			// 从缓存中获取所有主机基础信息
			Map<Long, LfMonShost> hostBaseInfo = MonitorStaticValue.getHostBaseMap();
			// 从缓存中获取所有主机动态信息
			Map<Long, MonDhostParams> hostInfo = MonitorStaticValue.getHostMap();
			//接收时间间隔
			Long receiveInterval = 0L;
			long curDbTime = MonitorStaticValue.getCurDbServerTime().getTime();
			// 遍历主机基础信息
			for (Long shostId : hostBaseInfo.keySet())
			{
				// 主机基础信息
				monShost = hostBaseInfo.get(shostId);
				// 开启监控并且主机属于启用状态
				if(monShost.getMonstatus() == 1 && monShost.getHostusestatus() == 0)
				{
					try
					{
						// 错误告警信息
						LfMonErr lfMonErr = new LfMonErr();
						// 验证告警手机合法性
						monPhone = monShost.getMonphone();
						// 有效的告警手机
						String effPhone = null;
						if(monPhone != null && !"".equals(monPhone))
						{
							// 有效号码,返回null表示无有效号码
							effPhone = checkMobleVaild(monPhone);
						}
						// 验证告警邮箱合法性
						monEmail = monShost.getMonemail();
						// 有效的告警邮箱
						String effEmail = retEffEmail(monEmail);
						// 主机基础信息主机ID在动态信息中存在
						if(hostInfo.containsKey(shostId))
						{
							// 主机动态信息
							monDhost = hostInfo.get(shostId);
							// 更新缓存数据时间
							Timestamp upateTime = monDhost.getDbservtime();
							// 设置告警基本信息
							monErrorInfo.setHostErrObjectInfo(lfMonErr, shostId, 0L, "0", "0", 0L, 0L, 3000, monShost.getMonstatus());
							receiveInterval = (curDbTime - upateTime.getTime()) / 1000;
							// 超过设置更新缓存数据时间
							if(receiveInterval >= MonitorStaticValue.getNetworkRrror())
							{
								// 处理状态不是处理中
								if(monDhost.getMonThresholdFlag().get(1) != -1)
								{
									msg = "超过"+MonitorStaticValue.getNetworkRrror()/60+"分钟未更新监控数据";
									// 向告警表写入告警数据
									monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 1, 2, AlarmSource, upateTime);
									// 如果未发送过告警且告警手机号码通过合法性校检，发送告警短信
									if(monDhost.getMonThresholdFlag().get(1) == 0 && effPhone != null)
									{
										// 更新告警短信标识成功，发送告警短信
										if(monErrorInfo.setAlarmSmsFlag(1, shostId, "1", 1, true, null))
										{
											smsContent = "主机名:" + monShost.getHostname() + "，" + msg+"。";
											//设置告警短信信息
											monBaseInfo.setmonAlarmDsm(effPhone, smsContent);
											//new Thread(new sendAlarmSms(null, smsContent, effPhone)).start();
										}
									}
									// 如果未发送过告警邮件且告警邮箱通过合法性校检，发送告警邮件
									if(monDhost.getMonThresholdFlag().get(7) == 0 && effEmail != null)
									{
										// 更新告警邮件标识成功，发送告警邮件
										if(monErrorInfo.setAlarmSmsFlag(5, shostId, "1", 1, true, null))
										{
											emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
											.append("监控类型：主机监控").append("<br/>")
											.append("监控名称："+monShost.getHostname()).append("<br/>")
											.append("告警级别：严重").append("<br/>")
											.append("事件描述："+msg).append("<br/>")
											.append("告警时间："+sdf.format(new Date())).append("<br/>");
											//设置邮件内容参数
											MonEmailParams mep = new MonEmailParams();
											mep.setContent(emailContent.toString());
											mep.setCorpCode(corpcode);
											mep.setEmail(effEmail);
											// 发送告警邮件
											new Thread(new sendAlarmEmail( mep)).start();
											//清空
											emailContent.setLength(0);
										}
									}
									//设置告警状态为2:严重，设置主机状态为脱网
									monErrorInfo.setMonStatu(1, 3, 2, null, shostId, null);
									
									EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() 
											+ "，"+msg+"，最后更新时间：" + monDhost.getDbservtime() + "，线程ID："+monThreadId
											+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
								}
								continue;
							}
							//设置告警标识为未发送,设置主机状态为在线
							monErrorInfo.setMonStatu(1, 1, null, "1", shostId, null);
							//动态处理标记为本机
							if(monDhost.getServerNum() != null && StaticValue.getServerNumber().equals(monDhost.getServerNum() ))
							{
								// 分析数据
								monParams.hostParamsAnalysis(monShost, monDhost, shostId, lfMonErr, effPhone,effEmail);
							}
						}
						// 主机基础信息主机ID不存在动态信息中
						else
						{
							// 向缓存写入一条初始数据
							LfMonDhost dhost = new LfMonDhost();
							dhost.setHostid(shostId);
							dhost.setHostName(monShost.getHostname());
							dhost.setAdapter1(monShost.getAdapter1());
							dhost.setHoststatus(1);
							dhost.setUpdatetime(new Timestamp(curDbTime));
							dhost.setEvtType(0);
							//更新数据库
							monitorBaseInfoBiz.saveOrUpdate(dhost,"hostid");
							continue;
						}
					}
					catch (Exception e)
					{
						EmpExecutionContext.error(e, "主机监控数据分析异常，主机编号：" + monShost.getHostid() 
											+ "，主机名称：" + monShost.getHostid() + "，线程ID："+monThreadId
											+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
				else
				{
					if(hostInfo.containsKey(shostId))
					{
						//重置状态,设置告警状态为0:正常,重置告警标识
						monErrorInfo.resetMonStatu(1, shostId, 6, null);
					}
					else
					{
						// 向缓存写入一条初始数据
						LfMonDhost dhost = new LfMonDhost();
						dhost.setHostid(shostId);
						dhost.setHostName(monShost.getHostname());
						dhost.setAdapter1(monShost.getAdapter1());
						dhost.setHoststatus(1);
						dhost.setUpdatetime(new Timestamp(curDbTime));
						dhost.setEvtType(0);
						//更新数据库
						monitorBaseInfoBiz.saveOrUpdate(dhost,"hostid");
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "主机监控数据分析异常，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 分析程序数据
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-4 下午06:13:52
	 */
	public void proceDataAnalysis()
	{
		// 程序基础信息
		LfMonSproce monSproce = null;
		// 程序动态信息
		MonDproceParams monDproce = null;
		// 事件内容
		String msg = "";
		// 告警内容
		String smsContent = "";
		// 告警邮件内容
		StringBuffer emailContent = new StringBuffer();
		// 告警手机
		String monPhone = "";
		// 告警邮箱
		String monEmail = "";
		// 告警来源 2：程序
		int AlarmSource = 2;
		try
		{
			// 从缓存中获取所有程序基础信息
			Map<Long, LfMonSproce> proceBaseInfo = MonitorStaticValue.getProceBaseMap();
			// 从缓存中获取所有程序动态信息
			Map<Long, MonDproceParams> proceInfo = MonitorStaticValue.getProceMap();
			//接收时间间隔
			Long receiveInterval = 0L;
			long curDbTime = MonitorStaticValue.getCurDbServerTime().getTime();
			// 遍历程序基础信息
			for (Long sproceId : proceBaseInfo.keySet())
			{
				// 程序基础信息
				monSproce = proceBaseInfo.get(sproceId);
				// 开启监控而且程序属于启用状态
				if(monSproce.getMonstatus() == 1 && monSproce.getProceusestatus() == 0)
				{
					try
					{
						// 错误告警信息
						LfMonErr lfMonErr = new LfMonErr();
						// 验证告警手机合法性
						monPhone = monSproce.getMonphone();
						// 有效的告警手机
						String effPhone = null;
						if(monPhone != null && !"".equals(monPhone))
						{
							// 有效号码,返回null表示无有效号码
							effPhone = checkMobleVaild(monPhone);
						}
						// 验证告警邮箱合法性
						monEmail = monSproce.getMonemail();
						// 有效的告警邮箱
						String effEmail = retEffEmail(monEmail);
						// 程序基础信息程序ID在动态信息中存在并且程序正常
						if(proceInfo.containsKey(sproceId))
						{
							// 程序动态信息
							monDproce = proceInfo.get(sproceId);
							// 更新缓存数据时间
							Timestamp upateTime = monDproce.getDbservtime();
							// 设置告警基本信息
							monErrorInfo.setHostErrObjectInfo(lfMonErr, monSproce.getHostid(), monSproce.getProceid(), "0", "0", 0L, 0L, monSproce.getProcetype(), monSproce.getMonstatus());
							// 超过设置更新缓存数据时间，程序异常
							receiveInterval = (curDbTime - upateTime.getTime()) / 1000;
							if(receiveInterval >= MonitorStaticValue.getNetworkRrror())
							{
								// 不是处理中状态
								if(monDproce.getMonThresholdFlag().get(1) != -1)
								{
									msg = "超过"+MonitorStaticValue.getNetworkRrror()/60+"分钟未更新监控数据";
									// 向告警表写入告警数据
									monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 1, 2, AlarmSource, upateTime);
									// 如果未发送过告警则发送
									if(monDproce.getMonThresholdFlag().get(1) == 0 && effPhone != null)
									{
										// 设置已发送过告警
										if(monErrorInfo.setAlarmSmsFlag(2, sproceId, "1", 1, true, null))
										{
											smsContent = "程序名称:" + monSproce.getProcename() + "，" + msg +"。";
											//设置告警短信信息
											monBaseInfo.setmonAlarmDsm(effPhone, smsContent);
											//new Thread(new sendAlarmSms(null, smsContent, effPhone)).start();
										}
									}
									
									// 如果未发送过告警邮件则发送
									if(monDproce.getMonThresholdFlag().get(7) == 0 && effEmail != null)
									{
										// 设置已发送过告警
										if(monErrorInfo.setAlarmSmsFlag(6, sproceId, "1", 1, true, null))
										{
											emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
											.append("监控类型：程序监控").append("<br/>")
											.append("监控名称："+monSproce.getProcename()).append("<br/>")
											.append("告警级别：严重").append("<br/>")
											.append("事件描述："+msg).append("<br/>")
											.append("告警时间："+sdf.format(new Date())).append("<br/>");
											//设置邮件内容参数
											MonEmailParams mep = new MonEmailParams();
											mep.setContent(emailContent.toString());
											mep.setCorpCode(corpcode);
											mep.setEmail(effEmail);
											// 发送告警邮件
											new Thread(new sendAlarmEmail( mep)).start();
											//清空
											emailContent.setLength(0);
										}
									}
									//设置告警状态为2:严重,设置程序状态为未启动
									monErrorInfo.setMonStatu(2, 2, 2, null, sproceId, null);
									
									// 程序为EMP_GW，设置状态为异常
									if(proceBaseInfo.get(sproceId).getProcetype() == 5200)
									{
										MonitorStaticValue.setEMP_WG_STATUS(1);
									}
									EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() 
											+  "，"+msg+"，最后更新时间：" + monDproce.getDbservtime() + "，线程ID："+monThreadId
											+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
								}
								continue;
							}
							//设置告警标识为未发送,设置程序状态为正常 
							monErrorInfo.setMonStatu(2, 0, null, "1", sproceId, null);
							// 程序为EMP_GW，设置状态为正常
							if(proceBaseInfo.get(sproceId).getProcetype() == 5200)
							{
								MonitorStaticValue.setEMP_WG_STATUS(0);
							}
							//动态处理标记为本机
							if(monDproce.getServerNum() != null && StaticValue.getServerNumber().equals(monDproce.getServerNum() ))
							{
								// 分析数据
								monParams.proceParamsAnalysis(monSproce, monDproce, sproceId, lfMonErr, effPhone, effEmail);
							}
						}
						// 程序基础信息程序ID不存在动态信息中
						else
						{
							// 向缓存写入一条初始数据
							LfMonDproce dproce = new LfMonDproce();
							dproce.setProceid(sproceId);
							dproce.setProcename(monSproce.getProcename());
							dproce.setUpdatetime(new Timestamp(curDbTime));
							dproce.setGatewayId(monSproce.getGatewayid());
							Long hostId = monSproce.getHostid();
							dproce.setHostid(monSproce.getHostid());
							dproce.setDbconnectstate(0);
							dproce.setProcetype(monSproce.getProcetype());
							// 程序绑定主机，从主机缓存中获取主机名，未绑定赋""
							if(hostId + 1 != 0)
							{
								dproce.setHostname(MonitorStaticValue.getHostBaseMap().get(hostId).getHostname());
							}
							else
							{
								dproce.setHostname("-");
							}
							dproce.setEvttype(0);
							dproce.setVersion(monSproce.getVersion());
							dproce.setProcestatus(0);
							dproce.setStarttime(new Timestamp(curDbTime));
							monitorBaseInfoBiz.saveOrUpdate(dproce,"proceid");
							continue;
						}
					}
					catch (Exception e)
					{
						EmpExecutionContext.error(e, "程序数据分析异常，程序编号：" + monSproce.getProceid() + "，程序名称：" + monSproce.getProcename() 
								+ "，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
				else
				{
					if(proceInfo.containsKey(sproceId))
					{
						//重置状态,设置告警状态为0:正常,重置告警标识
						monErrorInfo.resetMonStatu(2, sproceId, 6, null);
					}
					else
					{
						// 向缓存写入一条初始数据
						LfMonDproce dproce = new LfMonDproce();
						dproce.setProceid(sproceId);
						dproce.setProcename(monSproce.getProcename());
						dproce.setUpdatetime(new Timestamp(curDbTime));
						dproce.setGatewayId(monSproce.getGatewayid());
						Long hostId = monSproce.getHostid();
						dproce.setHostid(monSproce.getHostid());
						dproce.setDbconnectstate(0);
						dproce.setProcetype(monSproce.getProcetype());
						// 程序绑定主机，从主机缓存中获取主机名，未绑定赋""
						if(hostId + 1 != 0)
						{
							dproce.setHostname(MonitorStaticValue.getHostBaseMap().get(hostId).getHostname());
						}
						else
						{
							dproce.setHostname("-");
						}
						dproce.setEvttype(0);
						dproce.setVersion(monSproce.getVersion());
						dproce.setProcestatus(0);
						dproce.setStarttime(new Timestamp(curDbTime));
						monitorBaseInfoBiz.saveOrUpdate(dproce,"proceid");
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "程序监控数据分析异常，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 分析SP账号数据
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-4 下午07:47:14
	 */
	public void spAccoutDataAnalysis()
	{
		try
		{
			// EMP_GW程序正常，进行数据分析
//			if(MonitorStaticValue.EMP_WG_STATUS == 0)
//			{
				// 从缓存中获取所有SP账号基础信息
				Map<String, LfMonSspacinfo> spAccoutBaseInfo = MonitorStaticValue.getSpAccountBaseMap();
				// 从缓存中获取所有SP账号动态信息
				Map<String, MonDspAccountParams> spAccoutInfo = MonitorStaticValue.getSpAccountMap();
				// SP账号基础信息
				LfMonSspacinfo monSspacinfo = null;
				// SP账号动态信息
				MonDspAccountParams monDspacinfo = null;
				// 告警手机
				String monPhone = "";
				// 告警邮件内容
				StringBuffer emailContent = new StringBuffer();
				// 告警邮箱
				String monEmail = "";
				// 离线告警阀值
				Integer offlineThreshd = 0;
				// 离线时长
				long offlineDuration = 0;
				// 事件内容
				String msg = "";
				// 告警短信内容
				String smsContent = "";
				// 告警来源 3：SP账号
				int AlarmSource = 3;
				//开始时间段
				Integer beginHour = 0;
				//结束时间段
				Integer endHour = 0;
				//时长
				Integer duration = 0;
				// 当前时间
				Calendar calendar = null;
				// 未提交数据的时间
				Calendar noMtHaveSndcalendar = Calendar.getInstance();
				// 未提交时间
				Long noMtHaveSnd;
				// SP未提交告警状态, 0:正常；1：告警
				int spNoMtHaveSndState = 0;
				// 开始时间段到未提交时间的分钟数
				long minute = 0;
				//SP离线告警标识,key为告警时间，
				Map<String, LfSpoffctrl> spOfflineMonStatus = null;
				// 离线监控时间段及时长，多条记录以"-"分割
				String multiOfflineprds = "";
				// 单条离线监控时间段及时长，参数以","逗号分割
				String[] singleOfflineprd = null;
				// 单条离线监控时间段及时长，0－开始时间段；1－结束时间段；2－时长
				String[] spMonOfflineprd = null;
				// 离线监控时间段及时长集合
				Map<String, String> spOfflineprdMap = null;
				//提交时间和当前时间的比较结果, -1为小于当天；0为当天；1为大于当天
				int timeCompare = 0;
				StringBuffer spOfflinePrdSB = new StringBuffer();
				// 基础数据和缓存数据中有值，获取SP离线告警时间段
				if(spAccoutBaseInfo != null && spAccoutBaseInfo.size() > 0 
					&& spAccoutInfo != null && spAccoutInfo.size() > 0)
				{
					
					spOfflineprdMap = getSpOfflineprd();
				}
				long curDbTime = MonitorStaticValue.getCurDbServerTime().getTime();
				// 遍历SP账号基础信息
				for (String sSpAccountId : spAccoutBaseInfo.keySet())
				{
					// SP账号基础信息
					monSspacinfo = spAccoutBaseInfo.get(sSpAccountId);
					// 开启监控状态
					if(monSspacinfo.getMonstatus() == 1)
					{
						try
						{
							// 错误告警信息
							LfMonErr lfMonErr = new LfMonErr();
							// 验证告警手机合法性
							monPhone = monSspacinfo.getMonphone();
							// 有效的告警手机
							String effPhone = null;
							if(monPhone != null && !"".equals(monPhone))
							{
								// 有效号码,返回null表示无有效号码
								effPhone = checkMobleVaild(monPhone);
							}
							// 验证告警邮箱合法性
							monEmail = monSspacinfo.getMonemail();
							// 有效的告警邮箱
							String effEmail = retEffEmail(monEmail);
							// SP账号基础信息SP账号在动态信息中存在
							if(spAccoutInfo.containsKey(sSpAccountId))
							{
								// SP账号动态信息
								monDspacinfo = spAccoutInfo.get(sSpAccountId);
								//动态处理标记为本机
								if(monDspacinfo.getServerNum() != null && StaticValue.getServerNumber().equals(monDspacinfo.getServerNum() ))
								{
									// 设置告警基本信息
									monErrorInfo.setHostErrObjectInfo(lfMonErr, monDspacinfo.getHostId(), 0L, sSpAccountId, "0", 0L, 0L, 5400, monSspacinfo.getMonstatus());
									//账号不是未知类型
									if(monSspacinfo.getLoginType() != 0)
									{
										// 登录类型为直连账号，状态为离线
										if(monDspacinfo.getLoginType() == 2 && monDspacinfo.getOnlinestatus() == 1)
										{
											// 离线告警阀值
											offlineThreshd = monSspacinfo.getOfflineThreshd();
											// 离线时长
											offlineDuration = (curDbTime - monDspacinfo.getOfflineDuration()) / (1000*60);
											// 设置告警阀值且达到告警阀值
											if(offlineThreshd != 0 && offlineDuration >= offlineThreshd)
											{
												// 处理状态不是处理中
												if(monDspacinfo.getMonThresholdFlag().get(12) != -1)
												{
													msg = "账号离线时长达到告警阀值："+offlineDuration+"分钟/"+offlineThreshd+"分钟(离线时长/离线时长告警阀值)";
													// 向告警表写入告警数据
													monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 12, 2, AlarmSource, monDspacinfo.getUpdatetime());
													// 未发送过告警信息，发送告警短信
													if(monErrorInfo.setAlarmSmsFlag(3, null, "12", 1, true, sSpAccountId, monDspacinfo.getId()) && effPhone != null)
													{
														smsContent = "SP账号:" + sSpAccountId + "，SP账号名称:" + monDspacinfo.getAccountName() + "，" + msg;
														//设置告警短信信息
														monBaseInfo.setmonAlarmDsm(effPhone, smsContent);
														//new Thread(new sendAlarmSms(null, smsContent, effPhone)).start();
													}
													
													// 如果未发送过告警邮件则发送
													if(monErrorInfo.setAlarmSmsFlag(7, null, "12", 1, true, sSpAccountId, monDspacinfo.getId()) && effEmail != null)
													{
														emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
														.append("监控类型：sp监控").append("<br/>")
														.append("监控名称："+monDspacinfo.getAccountName()).append("<br/>")
														.append("告警级别：严重").append("<br/>")
														.append("事件描述："+msg).append("<br/>")
														.append("告警时间："+sdf.format(new Date())).append("<br/>");
														//设置邮件内容参数
														MonEmailParams mep = new MonEmailParams();
														mep.setContent(emailContent.toString());
														mep.setCorpCode(corpcode);
														mep.setEmail(effEmail);
														// 发送告警邮件
														new Thread(new sendAlarmEmail( mep)).start();
														//清空
														emailContent.setLength(0);
													}
													// 设置告警状态为2:严重
													monErrorInfo.setMonStatu(3, null, 2, null, null, sSpAccountId);
													EmpExecutionContext.info("SP账号:" + sSpAccountId 
															+ "，SP账号名称:" + monDspacinfo.getAccountName() 
															+ "，" + msg+"，最后更新时间："
															+ monDspacinfo.getDbservtime()
															+"，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
												}
												continue;
											}
											else
											{
												// 设置告警标识为未发送
												monErrorInfo.setAlarmSmsFlag(3, null, "12", 0, false, sSpAccountId, monDspacinfo.getId());
												// 设置邮件告警标识为未发送
												monErrorInfo.setAlarmSmsFlag(7, null, "12", 0, false, sSpAccountId, monDspacinfo.getId());
											}
										}
									}
									//未提交时间
									noMtHaveSnd = monDspacinfo.getNoMtHaveSnd();
									//重置未告警状态
									spNoMtHaveSndState = 0;
									//存在未提交时间且设置告警时间段
									if(noMtHaveSnd != 0 && spOfflineprdMap != null && spOfflineprdMap.containsKey(sSpAccountId))
									{
										spOfflineMonStatus = getSpOfflineprdCtrl(sSpAccountId);
										//监控时间段信息
										multiOfflineprds = spOfflineprdMap.get(sSpAccountId);
										// 单条离线监控时间段及时长
										singleOfflineprd = multiOfflineprds.split("-");
										//是否继续处理
										boolean isContinue = true;
										spOfflinePrdSB.setLength(0);
										for(int i=0; i < singleOfflineprd.length; i++)
										{
											spOfflinePrdSB.append("'").append(singleOfflineprd[i]).append("'").append(",");
											if(isContinue)
											{
												String alarmtime = "0";
												String alarmflag = "0";
												String alarmEmailFlag="0";
												spMonOfflineprd = singleOfflineprd[i].split("#");
												//开始时间段
												beginHour = Integer.parseInt(spMonOfflineprd[0]);
												//结束时间段
												endHour = Integer.parseInt(spMonOfflineprd[1]);
												//时长
												duration = Integer.parseInt(spMonOfflineprd[2]);
												if(spOfflineMonStatus != null && spOfflineMonStatus.containsKey(singleOfflineprd[i]))
												{
													alarmtime = spOfflineMonStatus.get(singleOfflineprd[i]).getAlarmtime();
													alarmflag = spOfflineMonStatus.get(singleOfflineprd[i]).getAlarmflag();
													alarmEmailFlag = spOfflineMonStatus.get(singleOfflineprd[i]).getAlarmemailflag();
												}
												//设置未提交时间
												noMtHaveSndcalendar.setTimeInMillis(noMtHaveSnd);
												//未提交的时间和当前时间比较
												timeCompare = timeCompare(noMtHaveSndcalendar);
												
												//数据库当前时间
												calendar = Calendar.getInstance();
												calendar.setTimeInMillis(curDbTime);
												//当前时间在时间段内
												if(calendar.get(Calendar.HOUR_OF_DAY) >= beginHour && calendar.get(Calendar.HOUR_OF_DAY) < endHour) 
												{
													
													//大于当天小时
													if(noMtHaveSndcalendar.get(Calendar.HOUR_OF_DAY) >= beginHour)
													{
														//当天
														if(timeCompare == 0)
														{
															//(当前时间（小时）－未提交时间（小时）)*60
															minute = getTimeInterval(noMtHaveSndcalendar);
														}
														//不是当天
														else
														{
															//当前时间小时-开始时间
															minute = (calendar.get(Calendar.HOUR_OF_DAY) - beginHour) * 60L;
															// 加上剩余的分钟
															minute += calendar.get(Calendar.MINUTE);
														}
														
													}
													//小于当前小时
													else
													{
														//当前时间小时-开始时间
														minute = (calendar.get(Calendar.HOUR_OF_DAY) - beginHour) * 60L;
														// 加上剩余的分钟
														minute += calendar.get(Calendar.MINUTE);
													}
													
													// 超过了设置的时长
													if(minute >= duration)
													{
														msg = beginHour+"点至"+endHour+"点时间段中超过"+duration+"分钟未提交";
														// 向告警表写入告警数据
														monErrorInfo.setSpOfflineErrorAlarmInfo(lfMonErr, msg, 13, 2, monDspacinfo.getUpdatetime(), singleOfflineprd[i]);
														// 未发送过告警信息或发送时间不是当天或为当天但未发送过告警短信，且存在告警手机号，发送告警短信
														if(("0".equals(alarmtime) || timeStrCompare(alarmtime) < 0 
																|| (timeStrCompare(alarmtime) == 0 && "0".equals(alarmflag)))
																&& effPhone != null)
														{
															smsContent = "SP账号:" + sSpAccountId + "，SP账号名称:" + monDspacinfo.getAccountName() + "，" + msg;
															//设置告警短信信息
															monBaseInfo.setmonAlarmDsm(effPhone, smsContent);
															//new Thread(new sendAlarmSms(null, smsContent, effPhone)).start();
															alarmflag = "1";
														}
														// 已告过警且为当天且发送过告警短信，设置告警短信标识为已发送
														if(!"0".equals(alarmtime) && timeStrCompare(alarmtime) == 0 && "1".equals(alarmflag))
														{
															alarmflag = "1";
														}
														
														// 未发送过告警邮件或发送时间不是当天或为当天但未发送过告警邮件，且存在告警邮箱，发送告警邮件
														if(("0".equals(alarmtime) || timeStrCompare(alarmtime) < 0 
																|| (timeStrCompare(alarmtime) == 0 && "0".equals(alarmEmailFlag)))
																&& effEmail != null)
														{
															emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
															.append("监控类型：sp监控").append("<br/>")
															.append("监控名称："+monDspacinfo.getAccountName()).append("<br/>")
															.append("告警级别：严重").append("<br/>")
															.append("事件描述："+msg).append("<br/>")
															.append("告警时间："+sdf.format(new Date())).append("<br/>");
															//设置邮件内容参数
															MonEmailParams mep = new MonEmailParams();
															mep.setContent(emailContent.toString());
															mep.setCorpCode(corpcode);
															mep.setEmail(effEmail);
															// 发送告警邮件
															new Thread(new sendAlarmEmail( mep)).start();
															//清空
															emailContent.setLength(0);
															alarmEmailFlag = "1";
														}
														// 已告过警且为当天且发送过告警邮件，设置告警邮件标识为已发送
														if(!"0".equals(alarmtime) && timeStrCompare(alarmtime) == 0 && "1".equals(alarmEmailFlag))
														{
															alarmEmailFlag = "1";
														}
														
														//设置告警时间和告警标识
														alarmtime = calendar.get(Calendar.YEAR) + "-" 
														+ calendar.get(Calendar.MONTH) + "-" 
														+ calendar.get(Calendar.DAY_OF_MONTH);
														// 设置告警标识为已告警
														setSpOfflineprdCtrl(sSpAccountId, singleOfflineprd[i], alarmtime, alarmflag,alarmEmailFlag);
														//设置状态为告警
														spNoMtHaveSndState = 1;
														EmpExecutionContext.info("SP账号:" + sSpAccountId 
																+ "，SP账号名称:" + monDspacinfo.getAccountName() 
																+ "，" + msg+"，最后更新时间："
																+ monDspacinfo.getDbservtime()
																+"，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
														isContinue = false;
														continue;
													}
												}
												else
												{
													//未提交时间小时大于结束时间
													if(noMtHaveSndcalendar.get(Calendar.HOUR_OF_DAY) >= endHour)
													{
														//未提交时间为当天
														if(timeCompare == 0)
														{
															//设置告警标识为正常
															alarmtime = "0";
															alarmflag = "0";
															alarmEmailFlag="0";
														}
													}
													else
													{
														//未提交时间为当天且告警时间小于当天
														if(timeCompare == 0 && !"0".equals(alarmtime) && timeStrCompare(alarmtime) < 0)
														{
															//设置告警标识为正常
															alarmtime = "0";
															alarmflag = "0";
															alarmEmailFlag = "0";
														}
													}
												}
												// 设置告警标识
												setSpOfflineprdCtrl(sSpAccountId, singleOfflineprd[i], alarmtime, alarmflag,alarmEmailFlag);
												// 状态为正常，且存在告警时间，设置状态为告警
												if(spNoMtHaveSndState == 0 && !"0".equals(alarmtime) && !"0".equals(alarmflag)&& !"0".equals(alarmEmailFlag))
												{
													spNoMtHaveSndState = 1;
												}
											}
										}
										//删除多余时间段
										if(spOfflinePrdSB.length() > 0)
										{
											spOfflinePrdSB.deleteCharAt(spOfflinePrdSB.lastIndexOf(","));
											delFailSpOffline(spOfflinePrdSB.toString(), sSpAccountId);
										}
										//告警标识为告警
										if(spNoMtHaveSndState == 1)
										{
											// 设置告警状态为2:严重
											monErrorInfo.setMonStatu(3, null, 2, null, null, sSpAccountId);
											continue;
										}
										else
										{
											// 设置告警状态为0:正常
											monErrorInfo.setMonStatu(3, null, 0, null, null, sSpAccountId);
										}
									}
									// 分析数据
									monParams.spAccountParamsAnalysis(monSspacinfo, monDspacinfo, sSpAccountId, lfMonErr, effPhone, spNoMtHaveSndState,effEmail);
								}
							}
							// SP账号基础信息SP账号不存在动态信息中
							else
							{
								// 向缓存写入一条初始数据
								LfMonDspacinfo dspacinfo = new LfMonDspacinfo();
								dspacinfo.setSpaccountid(sSpAccountId);
								dspacinfo.setUserfee(monSspacinfo.getUserfee());
								dspacinfo.setLoginintm(new Timestamp(curDbTime));
								dspacinfo.setUpdatetime(new Timestamp(curDbTime));
								dspacinfo.setLoginouttm(new Timestamp(curDbTime));
								dspacinfo.setSendlevel(monSspacinfo.getSendlevel());
								dspacinfo.setHostId(monSspacinfo.getHostid());
								dspacinfo.setEvtType(0);
								dspacinfo.setSpAccountType(monSspacinfo.getSpaccounttype());
								dspacinfo.setAccountName(monSspacinfo.getAccountname());
								dspacinfo.setSubmitStatusStr("未知");
								dspacinfo.setOnlinestatus(2);
								dspacinfo.setOnlinestatusStr("未知");
								dspacinfo.setLoginType(0);
								dspacinfo.setOfflineDuration(0L);
								dspacinfo.setNoMtHaveSnd(0L);
								try {
									monitorBaseInfoBiz.saveOrUpdate(dspacinfo,"spaccountid");
				                } catch (Exception e) {
				                    EmpExecutionContext.error(e,"处理sp账号监控缓存信息入库异常，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				                }
								continue;
							}
						}
						catch (Exception e)
						{
							EmpExecutionContext.error(e, "SP账号数据分析异常，SP账号：" + monSspacinfo.getSpaccountid() + "，SP账号名称：" + monSspacinfo.getAccountname()
									+"，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
						}
					}
					else
					{
						if(spAccoutInfo.containsKey(sSpAccountId))
						{
							//重置状态,设置告警状态为0:正常,重置告警标识
							monErrorInfo.resetMonStatu(3, null, 13, sSpAccountId);
						}
						else
						{
							// 向缓存写入一条初始数据
							LfMonDspacinfo dspacinfo = new LfMonDspacinfo();
							dspacinfo.setSpaccountid(sSpAccountId);
							dspacinfo.setUserfee(monSspacinfo.getUserfee());
							dspacinfo.setLoginintm(new Timestamp(curDbTime));
							dspacinfo.setUpdatetime(new Timestamp(curDbTime));
							dspacinfo.setLoginouttm(new Timestamp(curDbTime));
							dspacinfo.setSendlevel(monSspacinfo.getSendlevel());
							dspacinfo.setHostId(monSspacinfo.getHostid());
							dspacinfo.setEvtType(0);
							dspacinfo.setSpAccountType(monSspacinfo.getSpaccounttype());
							dspacinfo.setAccountName(monSspacinfo.getAccountname());
							
							dspacinfo.setSubmitStatusStr("未知");
							dspacinfo.setOnlinestatus(2);
							dspacinfo.setOnlinestatusStr("未知");
							dspacinfo.setLoginType(0);
							dspacinfo.setOfflineDuration(0L);
							dspacinfo.setNoMtHaveSnd(0L);
							try {
								monitorBaseInfoBiz.saveOrUpdate(dspacinfo,"spaccountid");
			                } catch (Exception e) {
			                    EmpExecutionContext.error(e,"处理sp账号监控缓存信息入库异常，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			                }
						}
					}
				}
//			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号监控数据分析异常，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 分析通道账号数据
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-6 上午11:33:25
	 */
	public void gateAccoutDataAnalysis()
	{
		try
		{
			// EMP_GW程序正常，进行数据分析
//			if(MonitorStaticValue.EMP_WG_STATUS ==0)
//			{
				// 从缓存中获取所有通道账号基础信息
				Map<String, LfMonSgtacinfo> gateAccoutBaseInfo = MonitorStaticValue.getGateAccountBaseMap();
				// 从缓存中获取所有通道账号动态信息
				Map<String, MonDgateacParams> gateAccoutInfo = MonitorStaticValue.getGateAccountMap();
				// 通道账号基础信息
				LfMonSgtacinfo monSgtacinfo = null;
				// 通道账号动态信息
				MonDgateacParams monDgtacinfo = null;
				// 事件内容
				String msg = "";
				// 告警短信内容
				String smsContent = "";
				// 告警邮件内容
				StringBuffer emailContent = new StringBuffer();
				// 告警手机
				String monPhone = "";
				// 告警邮箱
				String monEmail = "";
				// 告警来源 4：通道账号
				int AlarmSource = 4;
				long curDbTime = MonitorStaticValue.getCurDbServerTime().getTime();
				// 遍历通道账号基础信息
				for (String sGateAccountId : gateAccoutBaseInfo.keySet())
				{
					// 通道账号基础信息
					monSgtacinfo = gateAccoutBaseInfo.get(sGateAccountId);
					// 开启监控状态
					if(monSgtacinfo.getMonstatus() == 1)
					{
						try
						{
							// 错误告警信息
							LfMonErr lfMonErr = new LfMonErr();
							// 验证告警手机合法性
							monPhone = monSgtacinfo.getMonphone();
							// 告警手机合法性
							String effPhone = null;
							if(monPhone != null && !"".equals(monPhone))
							{
								// 有效号码,返回null表示无有效号码
								effPhone = checkMobleVaild(monPhone);
							}
							// 验证告警邮箱合法性
							monEmail = monSgtacinfo.getMonemail();
							// 有效的告警邮箱
							String effEmail = retEffEmail(monEmail);
							// 通道账号基础信息通道账号在动态信息中存在
							if(gateAccoutInfo.containsKey(sGateAccountId))
							{
								// 通道账号动态信息
								monDgtacinfo = gateAccoutInfo.get(sGateAccountId);
								// 更新缓存数据时间
								Timestamp updateTime = monDgtacinfo.getDbservtime();
								// 设置告警基本信息
								monErrorInfo.setHostErrObjectInfo(lfMonErr, monDgtacinfo.getHostId(), 0L, "0", sGateAccountId, 0L, 0L, 5500, monSgtacinfo.getMonstatus());
								// 超过设置更新缓存数据时间，写告警信息
								if((curDbTime - updateTime.getTime()) / 1000 >= MonitorStaticValue.getNetworkRrror())
								{
									// 处理状态不是处理中
									if(monDgtacinfo.getMonThresholdFlag().get(1) != -1)
									{
										msg = "超过"+MonitorStaticValue.getNetworkRrror()/60+"分钟未更新监控数据";
										// 向告警表写入告警数据
										monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 1, 2, AlarmSource, updateTime);
										// 未发送过告警信息，发送告警短信
										if(monDgtacinfo.getMonThresholdFlag().get(1) == 0 && effPhone != null)
										{
											// 设置告警标识为已发送
											if(monErrorInfo.setAlarmSmsFlag(4, null, "1", 1, true, sGateAccountId, monDgtacinfo.getId()))
											{
												smsContent = "通道账号名称:" + monSgtacinfo.getGateaccount() + "，" + msg;
												//设置告警短信信息
												monBaseInfo.setmonAlarmDsm(effPhone, smsContent);
												//new Thread(new sendAlarmSms(null, smsContent, effPhone)).start();
											}
										}
										// 未发送过告警邮件，发送告警邮件
										if(monDgtacinfo.getMonThresholdFlag().get(14) == 0 && effEmail != null)
										{
											// 设置告警邮件标识为已发送
											if(monErrorInfo.setAlarmSmsFlag(8, null, "1", 1, true, sGateAccountId, monDgtacinfo.getId()))
											{
												emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
												.append("监控类型：通道账号监控").append("<br/>")
												.append("监控名称："+monSgtacinfo.getGatename()).append("<br/>")
												.append("告警级别：严重").append("<br/>")
												.append("事件描述："+msg).append("<br/>")
												.append("告警时间："+sdf.format(new Date())).append("<br/>");
												//设置邮件内容参数
												MonEmailParams mep = new MonEmailParams();
												mep.setContent(emailContent.toString());
												mep.setCorpCode(corpcode);
												mep.setEmail(effEmail);
												// 发送告警邮件
												new Thread(new sendAlarmEmail( mep)).start();
												//清空
												emailContent.setLength(0);
											}
										}
										// 设置告警状态为2:严重
										monErrorInfo.setMonEvtType(4, 2, null, sGateAccountId);
										EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道账号名称:" + monSgtacinfo.getGatename() 
												+ "，"+msg+"，最后更新时间：" + monDgtacinfo.getDbservtime() + "，线程ID："+monThreadId
												+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
									}
									continue;
								}
								else
								{
									// 设置启动告警标识为正常
									monErrorInfo.setMonStatu(4, null, null, "1", null, sGateAccountId);
								}
								//动态处理标记为本机
								if(monDgtacinfo.getServerNum() != null && StaticValue.getServerNumber().equals(monDgtacinfo.getServerNum() ))
								{
									//告警标识
									Integer evtTypeFlag = 0;
									// 通道账号离线
									if(monDgtacinfo.getOnlinestatus() == 1)
									{
										// 处理状态不是处理中
										if(monDgtacinfo.getMonThresholdFlag().get(12) != -1)
										{
											if(monDgtacinfo.getMonThresholdFlag().get(12) == 0)
											{
												// 设置告警状态
												monErrorInfo.setAlarmSmsFlag(4, null, "12", curDbTime, sGateAccountId);
												// 设置邮件告警状态
												monErrorInfo.setAlarmSmsFlag(8, null, "12", curDbTime, sGateAccountId);
											}
											else if(monDgtacinfo.getMonThresholdFlag().get(12) == 1)
											{
												msg = "通道账号处于离线状态";
												// 向告警表和告警历史表写入告警数据
												monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 12, 2, AlarmSource, updateTime);
												// 设置告警状态为2:严重
												monErrorInfo.setMonStatu(4, null, 2, null, null, sGateAccountId);
												EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道账号名称:" + monSgtacinfo.getGatename()  
														+ "，通道账号处于离线状态，账号状态Onlinestatus：" + monDgtacinfo.getOnlinestatus() + "，线程ID："+monThreadId
														+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
												continue;
											}
											else if(curDbTime - monDgtacinfo.getMonThresholdFlag().get(12) >= MonitorStaticValue.getNetworkRrror()*1000)
											{
												msg = "通道账号处于离线状态";												
												// 向告警表和告警历史表写入告警数据
												monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 12, 2, AlarmSource, updateTime);
												// 设置告警状态为已发送告警
												if(effPhone != null && monErrorInfo.setAlarmSmsFlag(4, null, "12", 1, true, sGateAccountId, monDgtacinfo.getId()))
												{
													smsContent ="通道账号:" + sGateAccountId + "，通道账号名称:" + monSgtacinfo.getGateaccount() + "，" + msg;
													//设置告警短信信息
													monBaseInfo.setmonAlarmDsm(effPhone, smsContent);
													//new Thread(new sendAlarmSms(null, smsContent, effPhone)).start();
												}
												// 设置邮件告警状态为已发送告警
												if(effEmail != null &&monErrorInfo.setAlarmSmsFlag(8, null, "12", 1, true, sGateAccountId, monDgtacinfo.getId()))
												{
													emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
													.append("监控类型：通道账号监控").append("<br/>")
													.append("监控名称："+monSgtacinfo.getGateaccount()).append("<br/>")
													.append("告警级别：严重").append("<br/>")
													.append("事件描述："+msg).append("<br/>")
													.append("告警时间："+sdf.format(new Date())).append("<br/>");
													//设置邮件内容参数
													MonEmailParams mep = new MonEmailParams();
													mep.setContent(emailContent.toString());
													mep.setCorpCode(corpcode);
													mep.setEmail(effEmail);
													// 发送告警邮件
													new Thread(new sendAlarmEmail( mep)).start();
													//清空
													emailContent.setLength(0);
												}
												// 设置告警状态为2:严重
												monErrorInfo.setMonStatu(4, null, 2, null, null, sGateAccountId);
												EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道账号名称:" + monSgtacinfo.getGatename()  
														+ "，通道账号处于离线状态，账号状态Onlinestatus：" + monDgtacinfo.getOnlinestatus() + "，线程ID："+monThreadId
														+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
												continue;	
											}
										}
									}
									else
									{
										// 设置账号在线状态告警标识为正常
										monErrorInfo.setAlarmSmsFlag(4, null, "12", 0, false, sGateAccountId, monDgtacinfo.getId());
										// 设置账号在线状态邮件告警标识为正常
										monErrorInfo.setAlarmSmsFlag(8, null, "12", 0, false, sGateAccountId, monDgtacinfo.getId());
									}
									// 后付费账号不处理欠费告警
									if(monDgtacinfo.getFeeflag() != 2)
									{
										// 欠费
										if(monDgtacinfo.getUserfee() < 0)
										{
											// 开启欠费告警
											if(monSgtacinfo.getIsarrearage() == 1)
											{
												// 处理状态不是处理中
												if(monDgtacinfo.getMonThresholdFlag().get(13) != -1)
												{
													// 告警信息
													msg = "通道账号已欠费";
													// 向告警表写入告警数据
													monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 13, 2, AlarmSource, updateTime);
													// 未发送过告警信息，发送告警短信
													if(monDgtacinfo.getMonThresholdFlag().get(13) == 0 && effPhone != null)
													{
														// 设置告警标识为已发送
														if(monErrorInfo.setAlarmSmsFlag(4, null, "13", 1, true, sGateAccountId, monDgtacinfo.getId()))
														{
															smsContent ="通道账号:" + sGateAccountId + "，通道账号名称:" + monSgtacinfo.getGateaccount() + "，" + msg;
															//设置告警短信信息
															monBaseInfo.setmonAlarmDsm(effPhone, smsContent);
															//new Thread(new sendAlarmSms(null, smsContent, effPhone)).start();
														}
													}
													// 未发送过告警邮件，发送告警邮件
													if(monDgtacinfo.getMonThresholdFlag().get(26) == 0 && effEmail != null)
													{
														// 设置告警标识为已发送
														if(monErrorInfo.setAlarmSmsFlag(8, null, "13", 1, true, sGateAccountId, monDgtacinfo.getId()))
														{
															emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
															.append("监控类型：通道账号监控").append("<br/>")
															.append("监控名称："+monSgtacinfo.getGateaccount()).append("<br/>")
															.append("告警级别：严重").append("<br/>")
															.append("事件描述："+msg).append("<br/>")
															.append("告警时间："+sdf.format(new Date())).append("<br/>");
															//设置邮件内容参数
															MonEmailParams mep = new MonEmailParams();
															mep.setContent(emailContent.toString());
															mep.setCorpCode(corpcode);
															mep.setEmail(effEmail);
															// 发送告警邮件
															new Thread(new sendAlarmEmail( mep)).start();
															//清空
															emailContent.setLength(0);
														}
													}
													// 设置告警状态为2:严重
													monErrorInfo.setMonStatu(4, null, 2, null, null, sGateAccountId);
													evtTypeFlag = 2;
												}
											}
											else
											{
												// 设置告警标识为未发送,设置告警状态为0:正常
												monErrorInfo.setAlarmSmsFlag(4, null, "13", 0, false, sGateAccountId, monDgtacinfo.getId());
												// 设置邮件告警标识为未发送,设置告警状态为0:正常
												monErrorInfo.setAlarmSmsFlag(8, null, "13", 0, false, sGateAccountId, monDgtacinfo.getId());
											}
											// 账号状态不是离线，设置账号状态为2:欠费
											if(monDgtacinfo.getOnlinestatus() != 1)
											{
												monErrorInfo.setGateOnlinestatus(sGateAccountId, 2);
											}
											EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道账号名称:" + monSgtacinfo.getGatename() 
													+ "，通道账号已欠费，余额为：" + monDgtacinfo.getUserfee() + "，线程启动时间："+monThreadId
													+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());										
										}
										else
										{
											// 设置欠费告警标识为正常,设置告警状态为0:正常
											monErrorInfo.setMonStatu(4, null, 0, "13", null, sGateAccountId);
											if(monDgtacinfo.getOnlinestatus() == 2)
											{
												monErrorInfo.setGateOnlinestatus(sGateAccountId, 0);
											}
										}
									}
									// 分析数据
									monParams.gateAccountParamsAnalysis(monSgtacinfo, monDgtacinfo, sGateAccountId, lfMonErr, effPhone, evtTypeFlag,effEmail);
								}
								
							}
							// 通道账号基础信息通道账号不存在动态信息中
							else
							{
								// 向缓存写入一条初始数据
								LfMonDgtacinfo dgtacinfo = new LfMonDgtacinfo();
								dgtacinfo.setLoginintm(new Timestamp(curDbTime));
								dgtacinfo.setModifytime(new Timestamp(curDbTime));
								dgtacinfo.setLoginouttm(new Timestamp(curDbTime));
								dgtacinfo.setGateaccount(sGateAccountId);
								dgtacinfo.setOnlinestatus(0);
								dgtacinfo.setHostId(monSgtacinfo.getHostid());
								dgtacinfo.setGatewayid(monSgtacinfo.getGatewayid());
								dgtacinfo.setGwno(monSgtacinfo.getGatewayid());
								dgtacinfo.setEvtType(0);
								dgtacinfo.setGateName(monSgtacinfo.getGatename());
								//付费类型
								if(MonitorStaticValue.getGATEACCOUTN_INFO().containsKey(sGateAccountId))
								{
									dgtacinfo.setFeeflag(Integer.valueOf(MonitorStaticValue.getGATEACCOUTN_INFO().get(sGateAccountId)[1]));
								}
								else
								{
									dgtacinfo.setFeeflag(2);
								}
								// 帐号费用
								if(MonitorStaticValue.getGateAccoutFee().containsKey(sGateAccountId))
								{
									dgtacinfo.setUserfee(MonitorStaticValue.getGateAccoutFee().get(sGateAccountId));
								}
								else
								{
									dgtacinfo.setUserfee(0);
								}
				                
				                try {
				                	monitorBaseInfoBiz.saveOrUpdate(dgtacinfo,"gateaccount");
				                } catch (Exception e) {
				                    EmpExecutionContext.error(e,"处理通道账号监控缓存信息入库异常！");
				                }
								continue;
							}
						}
						catch (Exception e)
						{
							EmpExecutionContext.error(e, "通道账号数据分析异常，通道账号：" + monSgtacinfo.getGateaccount() + "，通道名称：" + monSgtacinfo.getGatename()
									+"，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
						}
					}
					else
					{
						if(gateAccoutInfo.containsKey(sGateAccountId))
						{
							//重置状态
							monErrorInfo.resetMonStatu(4, null, 13, sGateAccountId);
						}
						else
						{
							// 向缓存写入一条初始数据
							LfMonDgtacinfo dgtacinfo = new LfMonDgtacinfo();
							dgtacinfo.setLoginintm(new Timestamp(curDbTime));
							dgtacinfo.setModifytime(new Timestamp(curDbTime));
							dgtacinfo.setLoginouttm(new Timestamp(curDbTime));
							dgtacinfo.setGateaccount(sGateAccountId);
							dgtacinfo.setOnlinestatus(0);
							dgtacinfo.setHostId(monSgtacinfo.getHostid());
							dgtacinfo.setGatewayid(monSgtacinfo.getGatewayid());
							dgtacinfo.setGwno(monSgtacinfo.getGatewayid());
							dgtacinfo.setEvtType(0);
							dgtacinfo.setGateName(monSgtacinfo.getGatename());
							//付费类型
							if(MonitorStaticValue.getGATEACCOUTN_INFO().containsKey(sGateAccountId))
							{
								dgtacinfo.setFeeflag(Integer.valueOf(MonitorStaticValue.getGATEACCOUTN_INFO().get(sGateAccountId)[1]));
							}
							else
							{
								dgtacinfo.setFeeflag(2);
							}
							// 帐号费用
							if(MonitorStaticValue.getGateAccoutFee().containsKey(sGateAccountId))
							{
								dgtacinfo.setUserfee(MonitorStaticValue.getGateAccoutFee().get(sGateAccountId));
							}
							else
							{
								dgtacinfo.setUserfee(0);
							}
			                
			                try {
			                	monitorBaseInfoBiz.saveOrUpdate(dgtacinfo,"gateaccount");
			                } catch (Exception e) {
			                    EmpExecutionContext.error(e,"处理通道账号监控缓存信息入库异常，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			                }
						}
					}
				}
//			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通道账号监控数据分析异常！" + "，线程启动时间："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 分析在线用户数
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-18 下午03:16:04
	 */
	public void onlineUserDataAnalysis()
	{
		try
		{
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("servernum", StaticValue.getServerNumber());
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("servernum", "0");
			boolean result = baseBiz.update(MonDbConnection.getInstance().getConnection(), LfMonOnlcfg.class, objectMap, conditionMap);
			if(result)
			{
				if(StaticValue.getMonOnlinecfg() != null)
				{
					// 缓存中获取在线用户信息
					LfMonOnlcfg onlineUserInfo = StaticValue.getMonOnlinecfg();
					if(onlineUserInfo.getId() != null)
					{
						// 未监控不分析
						if(onlineUserInfo.getMonstatus() == 1)
						{
							if(onlineUserInfo.getOnlinenum() - onlineUserInfo.getMaxonline() >= 0)
							{
								//处理中
								if(onlineUserInfo != null && onlineUserInfo.getMonThresholdFlag() != null && !"-1".equals(onlineUserInfo.getMonThresholdFlag()))
								{
									// 设置告警基本信息
									LfMonErr lfMonErr = new LfMonErr();
									// EMP程序编号
									Long proceId = 0L;
									// 从缓存中获取所有程序基础信息
									Map<Long, LfMonSproce> proceBaseInfo = MonitorStaticValue.getProceBaseMap();
									// 遍历程序基础信息,获取EMP程序编号
									for (Long sproceId : proceBaseInfo.keySet())
									{
										// 程序属于启用状态
										if(proceBaseInfo.get(sproceId).getProcetype() == 5000)
										{
											proceId = sproceId;
											break;
										}
									}
									monErrorInfo.setHostErrObjectInfo(lfMonErr, 0L, proceId, "0", "0", 0L, 0L, 5700, 1);
									String msg = "当前在线用户数达到告警阀值：" + onlineUserInfo.getOnlinenum() + "/" + onlineUserInfo.getMaxonline() + "(当前在线用户数/告警阀值)。";
									// 告警来源 5：在线人员
									int AlarmSource = 5;
									// 向告警表写入告警数据
									monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 1, 1, AlarmSource, null);
									// 未发送过告警信息，发送告警短信
									if("0".equals(onlineUserInfo.getMonThresholdFlag()))
									{
										// 告警手机号码通过合法性校检，发送告警短信
										String monPhone = onlineUserInfo.getMonphone();
										if(monPhone != null && !"".equals(monPhone))
										{
											// 有效号码,返回null表示无有效号码
											String effPhone = checkMobleVaild(monPhone);
											if(effPhone != null && monErrorInfo.setOnlineAlarmSmsFlag("1", null, true,null))
											{
												//设置告警短信信息
												monBaseInfo.setmonAlarmDsm(effPhone, msg);
												//new Thread(new sendAlarmSms(null, msg, effPhone)).start();
											}
										}
									}
									// 未发送过告警邮件，发送告警邮件
									if("0".equals(onlineUserInfo.getSendmailFlag()))
									{
										// 验证告警邮箱合法性
										String monEmail = onlineUserInfo.getMonemail();
										// 有效的告警邮箱
										String effEmail = retEffEmail(monEmail);
										if(effEmail != null && monErrorInfo.setOnlineAlarmSmsFlag(null, null, true,"1"))
										{
											//告警邮件内容
											StringBuffer emailContent = new StringBuffer();
											emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
											.append("监控类型：在线用户监控").append("<br/>")
											.append("告警级别：警告").append("<br/>")
											.append("事件描述："+msg).append("<br/>")
											.append("告警时间："+sdf.format(new Date())).append("<br/>");
											//设置邮件内容参数
											MonEmailParams mep = new MonEmailParams();
											mep.setContent(emailContent.toString());
											mep.setCorpCode(corpcode);
											mep.setEmail(effEmail);
											// 发送告警邮件
											new Thread(null,new sendAlarmEmail( mep)).start();
										}
									}
									monErrorInfo.setOnlineAlarmSmsFlag(null, "1", false,null);
									EmpExecutionContext.info("当前在线用户数达到告警阀值：" + onlineUserInfo.getOnlinenum() + "/" 
											+ onlineUserInfo.getMaxonline() + "(当前在线用户数/告警阀值) ，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
								}
							}
							else
							{
								monErrorInfo.setOnlineAlarmSmsFlag("0", "0", false,"0");
							}
						}
						else
						{
							monErrorInfo.setOnlineAlarmSmsFlag("0", "0", false,"0");
						}
					}
					else
					{
						EmpExecutionContext.error("在线用户缓存信息异常!" + "，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "在线用户缓存信息异常!" + "，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}

	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-9-26 上午11:23:48
	 */
	public void run()
	{
		EmpExecutionContext.info("启动"+this.getName());
		MonitorStaticValue.setThreadStartDate(sdf.format(new Date()));
		isLoadModule = new SmsBiz().isWyModule("17");
		while(MonitorStaticValue.isMonThreadState())
		{
			try
			{
				// 系统启动默认时间进行数据分析
				Thread.sleep(firstRunTime);
				EmpExecutionContext.info("监控数据分析开始!");
				long start = System.currentTimeMillis();
				// 数据分析
				monDataAnalysis();
				EmpExecutionContext.info("监控数据分析结束!耗时:"+(System.currentTimeMillis() - start)+"ms");
				// 设置数据分析时间间隔
				firstRunTime = MonitorStaticValue.getTimeInterval();
			}
			catch (InterruptedException e)
			{
				if(MonitorStaticValue.isMonThreadState())
				{
					EmpExecutionContext.error(e, "监控数据分析线程异常，线程ID："+monThreadId+"，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
				Thread.currentThread().interrupt();
			}
		}
		EmpExecutionContext.info("停止"+this.getName());
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
	 * 
	 * @description    是否为EMP主机
	 * @param hostId
	 * @return  false 不是EMP主机； true 是EMP主机     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-2-21 上午09:33:08
	 */
	public boolean getIsEempHost(Long hostId)
	{
		boolean result = false;
		
		try
		{
			//程序基础信息
			Map<Long, LfMonSproce> proceBaseInfo = MonitorStaticValue.getProceBaseMap();
			
			for(Long proceId : proceBaseInfo.keySet())
			{
				//主机为EMP主机
				if(proceBaseInfo.get(proceId).getHostid().equals(hostId) && proceBaseInfo.get(proceId).getProcetype()==5000)
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "检查是否为EMP主机异常！");
			result = false;
		}
		return result;
	}
	
	/**
	 * 定时扫库,符合重发条件的记录使用网优发送
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-3 下午02:10:48
	 */
	public void monSmsResend()
	{
		try
		{
			//是否加载网优模块，17：网优
			boolean isWyModule = new SmsBiz().isWyModule("17");
			if(!isWyModule)
			{
				EmpExecutionContext.error("未加载网优模块，告警信息无法使用网优通道发送。");
				return;
			}
			//查询表中是否有状态为0并且发送间在三分钟之前六分钟之后的信息
			AlarmSmsBiz alarmSmsBiz = new AlarmSmsBiz();
			List<DynaBean> sendRecordList = alarmSmsBiz.getAlarmSmsRecord();
			if(sendRecordList != null && sendRecordList.size()>0)
			{
				//获取网优通道及SIM卡信息
				String sendStatus = "1";
				String[] gateInfo = new MonitorDAO().getIpcomAndSimInfo();
				if(gateInfo == null || "0".equals(gateInfo[1]) || "0".equals(gateInfo[2]) || "0".equals(gateInfo[3]))
				{
					gateInfo = gateInfo == null?new String[] {"0","0","0","0"}:gateInfo;
					
					EmpExecutionContext.error("告警短信重发失败，获取网优通道及SIM卡信息异常!IP:" + gateInfo[1]+"，端口："+gateInfo[2]+"，SIM卡："+gateInfo[3]);
					sendStatus = "0";
				}

				for(DynaBean sendRecord:sendRecordList)
				{
					if(sendStatus == "1")
					{
						//网优通道发送
						String result = new SmsSendBiz().wySend(gateInfo, sendRecord.get("phone").toString(), sendRecord.get("message").toString());
						if(!"success".endsWith(result))
						{
							sendStatus = "0";
						}
					}
					//以mtmsgid为条件更新告警短信发送记录状态
					alarmSmsBiz.setAlarmSmsRecord(sendRecord.get("msgid").toString(), 1, "2", sendStatus);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时扫库,符合重发条件的记录使用网优发送异常！");
		}
	}
	
	/**
	 * 获取SP未提交监控时间段及时长
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-1-23 下午06:08:48
	 */
	public Map<String, String> getSpOfflineprd()
	{
		Map<String, String> spOfflineprdMap = null;
		try
		{
			
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("spaccountid", "ASC");
			List<LfSpofflineprd>  spOfflineprdList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfSpofflineprd.class, null, orderbyMap);
			//查询到设置的时间段
			if(spOfflineprdList != null && spOfflineprdList.size() > 0)
			{
				spOfflineprdMap = new HashMap<String, String>();
				//SP账号
				String spAccount = "";
				//时间段和时长拼接字符串，格式：开始时间段,结束时间段,时长，多条记录以－符号分隔
				StringBuffer sb = new StringBuffer();
				for(LfSpofflineprd spofflineprd : spOfflineprdList)
				{
					spAccount = spofflineprd.getSpaccountid();
					sb.append(spofflineprd.getBeginhour()).append("#")
					.append(spofflineprd.getEndhour()).append("#")
					.append(spofflineprd.getDuration()).append("-");
					if(spOfflineprdMap.containsKey(spAccount))
					{
						sb.append(spOfflineprdMap.get(spAccount));
					}
					spOfflineprdMap.put(spAccount, sb.toString());
					//清空
					sb.setLength(0);
				}
			}
			return spOfflineprdMap;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取SP未提交监控时间段及时长异常！");
			return null;
		}
	}
	
	/**
	 * 获取SP离线时间段告警控制信息
	 * @description    
	 * @param sSpAccountId
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-23 下午03:09:25
	 */
	public Map<String, LfSpoffctrl> getSpOfflineprdCtrl(String sSpAccountId)
	{
		Map<String, LfSpoffctrl> spOfflineprdCtrlMap = new HashMap<String, LfSpoffctrl>();
		try
		{
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("spaccountid", sSpAccountId);
			List<LfSpoffctrl> spOfflineprdCtrlList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfSpoffctrl.class, null, null);
			//查询到设置的时间段
			if(spOfflineprdCtrlList != null && spOfflineprdCtrlList.size() > 0)
			{
				for(LfSpoffctrl lfSpoffctrl : spOfflineprdCtrlList)
				{
					spOfflineprdCtrlMap.put(lfSpoffctrl.getMonofflineprd(), lfSpoffctrl);
				}
			}
			return spOfflineprdCtrlMap;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取SP离线时间段告警控制信息异常！sSpAccountId:"+sSpAccountId);
			return null;
		}
	}
	
	/**
	 * 设置SP离线时间段告警控制信息
	 * @description    
	 * @param sSpAccountId
	 * @param singleOfflineprd
	 * @param spMonTime       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-23 下午03:49:52
	 */
	public void setSpOfflineprdCtrl(String sSpAccountId, String singleOfflineprd, String spMonTime, String sendMonSms)
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("spaccountid", sSpAccountId);
			conditionMap.put("monofflineprd", singleOfflineprd);
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("alarmtime", spMonTime);
			objectMap.put("alarmflag", sendMonSms);
			// 时间格式
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 最后一次处理时间
			long curDbTime = MonitorStaticValue.getCurDbServerTime().getTime();
			String updateTime = format.format(curDbTime);
			objectMap.put("updatetime", updateTime);
			//更新行数为0则新增
			if(!baseBiz.update(MonDbConnection.getInstance().getConnection(), true, LfSpoffctrl.class, objectMap, conditionMap))
			{
				LfSpoffctrl lfSpoffctrl = new LfSpoffctrl();
				lfSpoffctrl.setSpaccountid(sSpAccountId);
				lfSpoffctrl.setMonofflineprd(singleOfflineprd);
				lfSpoffctrl.setAlarmtime(spMonTime);
				lfSpoffctrl.setAlarmflag(sendMonSms);
				lfSpoffctrl.setCreatetime(new Timestamp(curDbTime));
				lfSpoffctrl.setUpdatetime(new Timestamp(curDbTime));
				lfSpoffctrl.setCorpcode(" ");
				baseBiz.addObj(MonDbConnection.getInstance().getConnection(), lfSpoffctrl);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置SP离线时间段告警控制信息失败，sSpAccountId:"+sSpAccountId
					+"，singleOfflineprd:"+singleOfflineprd+"，spMonTime:"+spMonTime);
		}
	}
	
	/**
	 * 设置SP离线时间段告警控制信息
	 * @description    
	 * @param sSpAccountId
	 * @param singleOfflineprd
	 * @param spMonTime       		
	 * @param sendMonSms       	
	 * @param sendMonMail       		 
	 * @author quexs
	 * @datetime 2016-4-14
	 */
	public void setSpOfflineprdCtrl(String sSpAccountId, String singleOfflineprd, String spMonTime, String sendMonSms, String sendMonMail)
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("spaccountid", sSpAccountId);
			conditionMap.put("monofflineprd", singleOfflineprd);
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("alarmtime", spMonTime);
			objectMap.put("alarmflag", sendMonSms);
			objectMap.put("alarmemailflag", sendMonMail);
			// 时间格式
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 最后一次处理时间
			long curDbTime = MonitorStaticValue.getCurDbServerTime().getTime();
			String updateTime = format.format(curDbTime);
			objectMap.put("updatetime", updateTime);
			//更新行数为0则新增
			if(!baseBiz.update(MonDbConnection.getInstance().getConnection(), true, LfSpoffctrl.class, objectMap, conditionMap))
			{
				LfSpoffctrl lfSpoffctrl = new LfSpoffctrl();
				lfSpoffctrl.setSpaccountid(sSpAccountId);
				lfSpoffctrl.setMonofflineprd(singleOfflineprd);
				lfSpoffctrl.setAlarmtime(spMonTime);
				lfSpoffctrl.setAlarmflag(sendMonSms);
				lfSpoffctrl.setAlarmemailflag(sendMonMail);
				lfSpoffctrl.setCreatetime(new Timestamp(curDbTime));
				lfSpoffctrl.setUpdatetime(new Timestamp(curDbTime));
				lfSpoffctrl.setCorpcode(" ");
				baseBiz.addObj(MonDbConnection.getInstance().getConnection(), lfSpoffctrl);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置SP离线时间段告警控制信息失败，sSpAccountId:"+sSpAccountId
					+"，singleOfflineprd:"+singleOfflineprd+"，spMonTime:"+spMonTime);
		}
	}
	
	/**
	 * 时间比较
	 * @description    
	 * @param calendar 要比较的时间格式为YYYY-MM-dd&0
	 * @return  0：当天；-1：小于当天；1：大于当天	 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-1-30 上午10:42:12
	 */
	public int timeStrCompare(String calendar)
	{
		try
		{
			//今天时间
			Calendar today = Calendar.getInstance();
			today.setTimeInMillis(MonitorStaticValue.getCurDbServerTime().getTime());
			//比较时间
			Calendar current = Calendar.getInstance();
			current.setTimeInMillis(MonitorStaticValue.getCurDbServerTime().getTime());
			//时间
			String time = calendar;
			//需要比较的时间集合
			String[] timeClump = time.split("-");
			current.set(Calendar.YEAR, Integer.parseInt(timeClump[0]));
			current.set(Calendar.MONTH, Integer.parseInt(timeClump[1]));
			current.set(Calendar.DAY_OF_MONTH,Integer.parseInt(timeClump[2]));
			
			//年相等且年中的天相等
			if(today.get(Calendar.YEAR) == current.get(Calendar.YEAR) 
					&& today.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR))
			{
				//当天
				return 0;
			}
			//在当前时间之前
			else if(today.after(current))
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
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e, "进行时间比较，判断时间是否为当天异常，calendar:"+calendar);
			return 0;
		}
	}
	
	/**
	 * 时间比较
	 * @description    
	 * @param calendar 要比较日历对象
	 * @return    0：当天；-1：小于当天；1：大于当天	    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-1-30 上午10:57:07
	 */
	public int timeCompare(Calendar calendar)
	{
		try
		{
			//今天时间
			Calendar today = Calendar.getInstance();
			today.setTimeInMillis(MonitorStaticValue.getCurDbServerTime().getTime());
			//年相等且年中的天相等
			if(today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) 
					&& today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR))
			{
				//当天
				return 0;
			}
			//在当前时间之前
			else if(today.after(calendar))
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
			EmpExecutionContext.error(e, "进行时间比较，判断时间是否为当天异常，calendar:"+calendar.getTime());
			return 0;
		}
	}
	
	/**
	 * 当前时间比较，相差多少分钟
	 * @description    
	 * @param noMtHaveSndcalendar  时间
	 * @return 相差分钟数      			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-1-30 下午07:44:11
	 */
	public long getTimeInterval(Calendar noMtHaveSndcalendar)
	{
		try
		{
			long timeInterval = 0;
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(MonitorStaticValue.getCurDbServerTime().getTime());
			calendar.set(Calendar.HOUR_OF_DAY, noMtHaveSndcalendar.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, noMtHaveSndcalendar.get(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, noMtHaveSndcalendar.get(Calendar.SECOND));
			timeInterval = (MonitorStaticValue.getCurDbServerTime().getTime() - calendar.getTimeInMillis()) / (1000*60);
			
			return timeInterval;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "当前时间比较，相差多少分钟异常！");
			return 0;
		}
	}
	
	/**
	 * 删除失效的SP告警时间段
	 * @description    
	 * @param spOfflinePrd       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-24 下午01:44:05
	 */
	public void delFailSpOffline(String spOfflinePrd, String spAccountId)
	{
		monitorDAO.delFailSpOffline(spOfflinePrd, spAccountId);
	}
	
	/**
	 * 设置监控基础信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午02:26:47
	 */
	private void getMonitorBaseInfo(int flag)
	{
		// 设置监控配置信息，包括页面刷新时间 、数据分析间隔时间、网络异常时间间隔
		monBaseInfo.setMonConfig();
		// 在线用户告警信息
		monBaseInfo.setOnlineCfgInfo();
		// 主机监控基本信息
		monBaseInfo.setHostBaseInfo();
		//设置主机动态信息
		monBaseInfo.setDHostInfo(flag);
		// 程序监控基本信息
		monBaseInfo.setProceBaseInfo();
		// 设置程序动态信息
		monBaseInfo.setDProceInfo(flag);
		// SP账号监控基本信息
		monBaseInfo.setspAcBaseInfo();
		// 设置SP账号监控动态信息
		monBaseInfo.setDspAcInfo(flag);
		// 通道账号监控基本信息
		monBaseInfo.setGateAcBaseInfo();
		//设置通道账号监控动态信息
		monBaseInfo.setDGateAcInfo(flag);
		//设置SPGATE缓存动态信息
		monBaseInfo.setDGateBufInfo();
		// 实时告警信息
		monBaseInfo.setMonErrorInfo();
		// 设置通道账号余额
//		monBaseInfo.setGateAccountFee();
		// 设置数据库监控动态信息
		monBaseInfo.setDataBaseMonInfo();
		// 设置主机网络监控信息
		monBaseInfo.setHostNetMonInfo();
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
