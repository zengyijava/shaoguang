/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-15 上午08:45:17
 */
package com.montnets.emp.monitor.biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.monitor.LfMonDbstate;
import com.montnets.emp.entity.monitor.LfMonErr;
import com.montnets.emp.entity.monitor.LfMonSgtacinfo;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.entity.monitor.LfMonSspacinfo;
import com.montnets.emp.monitor.biz.i.IMonParamsAnalysisBiz;
import com.montnets.emp.monitor.constant.MonDgateacParams;
import com.montnets.emp.monitor.constant.MonDhostParams;
import com.montnets.emp.monitor.constant.MonDproceParams;
import com.montnets.emp.monitor.constant.MonDspAccountParams;
import com.montnets.emp.monitor.constant.MonEmailParams;
import com.montnets.emp.monitor.constant.MonitorStaticValue;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-15 上午08:45:17
 */

public class MonParamsAnalysisBiz implements IMonParamsAnalysisBiz
{

	private MonErrorInfo	monErrorInfo	= new MonErrorInfo();
	
	private MonitorBaseInfoBiz monBaseInfo = new MonitorBaseInfoBiz();
	
	private String corpcode ="100001";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	/**
	 * 数据库操作状态数据分析
	 * @description    
	 * @param monDbstate
	 * @param lfMonErr
	 * @param monPhone
	 * @param effEmail
	 * @param monContent
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-26 下午07:43:59
	 */
	public int dbOprStateDataAnalysis(LfMonDbstate monDbstate, LfMonErr lfMonErr, String monPhone, String effEmail, StringBuffer monContent)
	{
		String msg = "";
		// 告警来源 10：数据库
		int AlarmSource = 10;
		// 邮件告警内容
		StringBuffer emailContent = new StringBuffer("");
		int oprStateEvtType = 0;
		try
		{
			//新增操作状态为1：异常
			if(monDbstate.getAddopr() == 1)
			{
				// 设置告警标识为2严重
				oprStateEvtType = 2;
				if(monDbstate.getSmsalflag2() != -1)
				{
					if(monDbstate.getProcetype().equals(5000))
					{
						msg = "数据库新增操作异常。";
					}
					else
					{
						msg = "数据库新增操作异常";
						if(monDbstate.getDbaddoprdes() != null && monDbstate.getDbaddoprdes().trim().length() > 0)
						{
							msg += "，描述："+monDbstate.getDbaddoprdes();
							if(!monDbstate.getDbaddoprdes().endsWith("。"))
							{
								msg += "。";
							}
						}
						else
						{
							msg += "。";
						}
					}
					// 向告警表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 2, 2, AlarmSource, monDbstate.getUpdatetime());
					// 设置告警状态为已发送告警
					if(monPhone != null && monErrorInfo.setAlarmSmsFlag(AlarmSource, monDbstate.getId(), "2", 1, null))
					{
						//设置告警短信信息
						monContent.append(msg);
					}
					// 设置邮件告警状态为已发送告警
					if(effEmail != null && monErrorInfo.setAlarmSmsFlag(20, monDbstate.getId(), "2", 1, null))
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
					EmpExecutionContext.info("程序节点:" + monDbstate.getProcenode() + "，程序名称:" + monDbstate.getProcename() 
							+ "，" + msg + "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
			}
			else
			{
				// 设置短信、邮件告警标识为0正常
				monErrorInfo.setMonStatu(AlarmSource, null, null, "2", monDbstate.getId(), null);
			}
			
			//删除操作状态为1：异常
			if(monDbstate.getDelopr() == 1)
			{
				// 设置告警标识为2严重
				oprStateEvtType = 2;
				if(monDbstate.getSmsalflag3() != -1)
				{
					if(monDbstate.getProcetype().equals(5000))
					{
						msg = "数据库删除操作异常。";
					}
					else
					{
						msg = "数据库删除操作异常";
						if(monDbstate.getDbdeloprdes() != null && monDbstate.getDbdeloprdes().trim().length() > 0)
						{
							msg += "，描述："+monDbstate.getDbdeloprdes();
							if(!monDbstate.getDbdeloprdes().endsWith("。"))
							{
								msg += "。";
							}
						}
						else
						{
							msg += "。";
						}
					}
					// 向告警表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 2, AlarmSource, monDbstate.getUpdatetime());
					// 设置告警状态为已发送告警
					if(monPhone != null && monErrorInfo.setAlarmSmsAndMailFlag(AlarmSource, monDbstate.getId(), "3", 1, true))
					{
						//设置告警短信信息
						monContent.append(msg);
					}
					// 设置邮件告警状态为已发送告警
					if(effEmail != null && monErrorInfo.setAlarmSmsAndMailFlag(20, monDbstate.getId(), "3", 1, true))
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
					EmpExecutionContext.info("程序节点:" + monDbstate.getProcenode() + "，程序名称:" + monDbstate.getProcename() 
							+ "，" + msg + "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
			}
			else
			{
				// 设置短信、邮件告警标识为0正常
				monErrorInfo.setMonStatu(AlarmSource, null, null, "3", monDbstate.getId(), null);
			}
			
			//修改操作状态为1：异常
			if(monDbstate.getModiopr() == 1)
			{
				// 设置告警标识为2严重
				oprStateEvtType = 2;
				if(monDbstate.getSmsalflag4() != -1)
				{
					if(monDbstate.getProcetype().equals(5000))
					{
						msg = "数据库修改操作异常。";
					}
					else
					{
						msg = "数据库修改操作异常";
						if(monDbstate.getDbmodioprdes() != null && monDbstate.getDbmodioprdes().trim().length() > 0)
						{
							msg += "，描述："+monDbstate.getDbmodioprdes();
							if(!monDbstate.getDbmodioprdes().endsWith("。"))
							{
								msg += "。";
							}
						}
						else
						{
							msg += "。";
						}
					}
					// 向告警表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 2, AlarmSource, monDbstate.getUpdatetime());
					// 设置告警状态为已发送告警
					if(monPhone != null && monErrorInfo.setAlarmSmsAndMailFlag(AlarmSource, monDbstate.getId(), "4", 1, true))
					{
						//设置告警短信信息
						monContent.append(msg);
					}
					// 设置邮件告警状态为已发送告警
					if(effEmail != null && monErrorInfo.setAlarmSmsAndMailFlag(20, monDbstate.getId(), "4", 1, true))
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
					EmpExecutionContext.info("程序节点:" + monDbstate.getProcenode() + "，程序名称:" + monDbstate.getProcename() 
							+ "，" + msg + "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
			}
			else
			{
				// 设置短信、邮件告警标识为0正常
				monErrorInfo.setMonStatu(AlarmSource, null, null, "4", monDbstate.getId(), null);
			}
			
			//查询操作状态为1：异常
			if(monDbstate.getDispopr() == 1)
			{
				// 设置告警标识为2严重
				oprStateEvtType = 2;
				if(monDbstate.getSmsalflag5() != -1)
				{
					if(monDbstate.getProcetype().equals(5000))
					{
						msg = "数据库查询操作异常。";
					}
					else
					{
						msg = "数据库查询操作异常";
						if(monDbstate.getDbdispoprdes() != null && monDbstate.getDbdispoprdes().trim().length() > 0)
						{
							msg += "，描述："+monDbstate.getDbdispoprdes();
							if(!monDbstate.getDbdispoprdes().endsWith("。"))
							{
								msg += "。";
							}
						}
						else
						{
							msg += "。";
						}
					}
					// 向告警表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 5, 2, AlarmSource, monDbstate.getUpdatetime());
					// 设置告警状态为已发送告警
					if(monPhone != null && monErrorInfo.setAlarmSmsAndMailFlag(AlarmSource, monDbstate.getId(), "5", 1, true))
					{
						//设置告警短信信息
						monContent.append(msg);
					}
					// 设置邮件告警状态为已发送告警
					if(effEmail != null && monErrorInfo.setAlarmSmsAndMailFlag(20, monDbstate.getId(), "5", 1, true))
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
					EmpExecutionContext.info("程序节点:" + monDbstate.getProcenode() + "，程序名称:" + monDbstate.getProcename() 
							+ "，" + msg + "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
			}
			else
			{
				// 设置短信、邮件告警标识为0正常
				monErrorInfo.setMonStatu(AlarmSource, null, null, "5", monDbstate.getId(), null);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "数据库操作状态数据分析失败，程序名称："+monDbstate.getProcename()+"，程序节点："+monDbstate.getProcenode() 
					+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
		return oprStateEvtType;
	}
	
	
	/**
	 * 主机参数分析
	 * 
	 * @description
	 * @param monShost
	 * @param monDhost
	 * @param shostId
	 * @param lfMonErr
	 * @param monPhone
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 上午09:21:42
	 */
	public void hostParamsAnalysis(LfMonShost monShost, MonDhostParams monDhost, Long shostId, LfMonErr lfMonErr, String effPhone)
	{
		// 告警短信内容
		StringBuffer monContent = new StringBuffer("");
		// 事件内容
		String msg = "";
		//告警状态标识 false 无告警; true 告警
		boolean isMonitorFlag = false;
		// 告警来源 1：主机
		int AlarmSource = 1;
		// 更新缓存数据时间
		Timestamp upateTime = monDhost.getUpdatetime();
		try
		{
			// 告警阀值标识
			Map<Integer, Long> monThresholdFlag = monDhost.getMonThresholdFlag();
			// CPU占用量(3次达到告警阀值时才进行告警)
			int cpuUsage = monDhost.getCpuusage();
			if(monShost.getCpuusage() != 0 && cpuUsage >= monShost.getCpuusage())
			{
				// 处理状态不是处理中
				if(monThresholdFlag.get(2) != -1)
				{
					if(monThresholdFlag.get(2) == 0)
					{
						// 设置告警状态
						monErrorInfo.setAlarmSmsFlag(1, shostId, "2", System.currentTimeMillis(), null);
					}
					else if(monThresholdFlag.get(2) == 1)
					{
						msg = "CPU占用率持续过高，当前占用率：" + cpuUsage + "%，已达到告警阀值：" + monShost.getCpuusage()+"%。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 2, 1, AlarmSource, upateTime);
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，CPU占用量达到告警阀值，当前占用率：" + cpuUsage + "，告警阀值：" + monShost.getCpuusage());
					}
					else if(System.currentTimeMillis() - monThresholdFlag.get(2) >= 3*60*1000)
					{
						msg = "CPU占用率持续过高，当前占用率：" + cpuUsage + "%，已达到告警阀值：" + monShost.getCpuusage()+"%。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 2, 1, AlarmSource, upateTime);
						// 设置告警状态为已发送告警
						if(effPhone != null && monErrorInfo.setAlarmSmsFlag(1, shostId, "2", 1, null))
						{
							monContent.append(msg);
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，CPU占用量达到告警阀值，当前占用率：" 
								+ cpuUsage + "，告警阀值：" + monShost.getCpuusage()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
				else
				{
					//设置告警状态标识为告警
					isMonitorFlag = true;
				}
			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(1, shostId, "2", 0, false, null);
			}
			// 物理内存使用量(两次超过告警阀值时才进行告警)
			if(monShost.getMemusage() != 0 && monDhost.getMemusage() >= monShost.getMemusage())
			{
				// 处理状态不是处理中
				if(monThresholdFlag.get(3) != -1)
				{
					if(monThresholdFlag.get(3) == 0)
					{
						// 设置告警状态
						monErrorInfo.setAlarmSmsFlag(1, shostId, "3", System.currentTimeMillis(), null);
					}
					else if(monThresholdFlag.get(3) == 1)
					{
						msg = "物理内存使用量达到告警阀值：" + monDhost.getMemusage() + "M/" + monShost.getMemusage() + "M(使用量/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 1, AlarmSource, upateTime);
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，物理内存使用量达到告警阀值，当前使用量：" + monDhost.getMemusage() 
								+ "，告警阀值：" + monShost.getMemusage()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
					else if(System.currentTimeMillis() - monThresholdFlag.get(3) >= 3*60*1000)
					{
						msg = "物理内存使用量达到告警阀值：" + monDhost.getMemusage() + "M/" + monShost.getMemusage() + "M(使用量/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 1, AlarmSource, upateTime);
						// 设置告警状态为已发送告警
						if(effPhone != null && monErrorInfo.setAlarmSmsFlag(1, shostId, "3", 1, null))
						{
							monContent.append(msg);
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，物理内存使用量达到告警阀值，当前使用量：" + monDhost.getMemusage() 
								+ "，告警阀值：" + monShost.getMemusage() + "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
				else
				{
					//设置告警状态标识为告警
					isMonitorFlag = true;
				}
			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(1, shostId, "3", 0, false, null);
			}
			// 虚拟内存使用量(两次超过告警阀值时才进行告警)
			if(monShost.getVmemusage() != 0 && monDhost.getVmemusage() >= monShost.getVmemusage())
			{
				// 处理状态不是处理中
				if(monThresholdFlag.get(4) != -1)
				{
					if(monThresholdFlag.get(4) == 0)
					{
						// 设置告警状态
						monErrorInfo.setAlarmSmsFlag(1, shostId, "4", System.currentTimeMillis(), null);
					}
					else if(monThresholdFlag.get(4) == 1)
					{
						msg = "虚拟内存使用量达到告警阀值：" + monDhost.getVmemusage() + "M/" + monShost.getVmemusage() + "M(使用量/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，虚拟内存使用量达到告警阀值，当前使用量：" + monDhost.getVmemusage() 
								+ "，告警阀值：" + monShost.getVmemusage()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
					else if(System.currentTimeMillis() - monThresholdFlag.get(4) >= 3*60*1000)
					{
						msg = "虚拟内存使用量达到告警阀值：" + monDhost.getVmemusage() + "M/" + monShost.getVmemusage() + "M(使用量/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
						// 设置告警状态为已发送告警
						if(effPhone != null && monErrorInfo.setAlarmSmsFlag(1, shostId, "4", 1, null))
						{
							monContent.append(msg);
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，虚拟内存使用量达到告警阀值，当前使用量：" + monDhost.getVmemusage() 
								+ "，告警阀值：" + monShost.getVmemusage()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
				else
				{
					//设置告警状态标识为告警
					isMonitorFlag = true;
				}
			}
			else
			{
				// 设置告警标识为正常
				monErrorInfo.setAlarmSmsFlag(1, shostId, "4", 0, false, null);
			}
			// 磁盘空间使用量
			if(monShost.getDiskfreespace() != 0 && monDhost.getDiskfreespace() <= monShost.getDiskfreespace())
			{
				// 处理状态不是处理中
				if(monThresholdFlag.get(5) != -1)
				{
					msg = "磁盘总容量为" + monDhost.getDiskspace() + "M，可用空间为" + monDhost.getDiskfreespace() + "M，达到告警阀值（" + monShost.getDiskfreespace() + "M）。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 5, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monThresholdFlag.get(5) == 0 && effPhone != null)
					{
						// 设置告警状态为1:已发送告警
						if(monErrorInfo.setAlarmSmsFlag(1, shostId, "5", 1, true, null))
						{
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，磁盘空间使用量达到告警阀值，可用空间：" + monDhost.getDiskfreespace() 
						+ "，告警阀值：" + monShost.getDiskfreespace()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());

			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(1, shostId, "5", 0, false, null);
			}
			// 进程总数据
			if(monShost.getProcesscnt() != 0 && monDhost.getProcesscnt() >= monShost.getProcesscnt())
			{
				// 处理状态不是处理中
				if(monThresholdFlag.get(6) != -1)
				{
					msg = "进程总数达到告警阀值：" + monDhost.getProcesscnt() + "个/" + monShost.getProcesscnt() + "个(总数/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 6, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monThresholdFlag.get(6) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(1, shostId, "6", 1, true, null))
						{
							// 设置告警状态为1:已发送告警
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，虚拟内存使用量达到告警阀值，当前总数：" + monDhost.getProcesscnt() 
						+ "，告警阀值：" + monShost.getProcesscnt()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(1, shostId, "6", 0, false, null);
			}
			// 告警级别
			int evtType = 0;
			if(isMonitorFlag)
			{
				evtType = 1;
			}
			else
			{
				evtType = 0;
			}
			
			// 设置告警级别
			monErrorInfo.setMonEvtType(1, evtType, shostId, null);
			// 发送告警短信
			if(monContent.length() > 0)
			{
				// 告警内容
				String monSendContent = "主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，" + monContent;
				monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
				//new Thread(new sendAlarmSms(null, monSendContent, effPhone)).start();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "主机监控数据参数分析异常，主机编号：" + monShost.getHostid() + "，主机名称：" + monShost.getHostid()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 主机参数分析
	 */
	public void hostParamsAnalysis(LfMonShost monShost, MonDhostParams monDhost, Long shostId, LfMonErr lfMonErr, String effPhone, String effEmail){
		// 告警短信内容

				StringBuffer monContent = new StringBuffer("");
				// 告警邮件内容
				StringBuffer emailMsg = new StringBuffer();
				// 事件内容
				String msg = "";
				//告警状态标识 false 无告警; true 告警
				boolean isMonitorFlag = false;
				// 告警来源 1：主机
				int AlarmSource = 1;
				// 更新缓存数据时间
				Timestamp upateTime = monDhost.getUpdatetime();
				try
				{
					// 告警阀值标识
					Map<Integer, Long> monThresholdFlag = monDhost.getMonThresholdFlag();
					// CPU占用量(3次达到告警阀值时才进行告警)
					int cpuUsage = monDhost.getCpuusage();
					if(monShost.getCpuusage() != 0 && cpuUsage >= monShost.getCpuusage())
					{
						// 处理状态不是处理中
						if(monThresholdFlag.get(2) != -1)
						{
							if(monThresholdFlag.get(2) == 0)
							{
								// 设置告警状态
								monErrorInfo.setAlarmSmsFlag(1, shostId, "2", System.currentTimeMillis(), null);
								// 设置邮件状态
								monErrorInfo.setAlarmSmsFlag(5, shostId, "2", System.currentTimeMillis(), null);
							}
							else if(monThresholdFlag.get(2) == 1)
							{
								msg = "CPU占用率持续过高，当前占用率：" + cpuUsage + "%，已达到告警阀值：" + monShost.getCpuusage()+"%。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 2, 1, AlarmSource, upateTime);
								//设置告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，CPU占用量达到告警阀值，当前占用率：" + cpuUsage 
										+ "，告警阀值：" + monShost.getCpuusage()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
							else if(System.currentTimeMillis() - monThresholdFlag.get(2) >= 3*60*1000)
							{
								msg = "CPU占用率持续过高，当前占用率：" + cpuUsage + "%，已达到告警阀值：" + monShost.getCpuusage()+"%。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 2, 1, AlarmSource, upateTime);
								// 设置告警状态为已发送告警
								if(effPhone != null && monErrorInfo.setAlarmSmsFlag(1, shostId, "2", 1, null))
								{
									monContent.append(msg);
								}
								// 设置邮件告警状态为已发送告警
								if(effEmail != null && monErrorInfo.setAlarmSmsFlag(5, shostId, "2", 1, null))
								{
									emailMsg.append(msg);
								}
								//设置告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，CPU占用量达到告警阀值，当前占用率：" + cpuUsage 
										+ "，告警阀值：" + monShost.getCpuusage()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
						}
						else
						{
							//设置告警状态标识为告警
							isMonitorFlag = true;
						}
					}
					else
					{
						// 设置告警状态为0:正常
						monErrorInfo.setAlarmSmsFlag(1, shostId, "2", 0, false, null);
						// 设置邮件告警状态为0:正常
						monErrorInfo.setAlarmSmsFlag(5, shostId, "2", 0, false, null);
					}
					// 物理内存使用量(两次超过告警阀值时才进行告警)
					if(monShost.getMemusage() != 0 && monDhost.getMemusage() >= monShost.getMemusage())
					{
						// 处理状态不是处理中
						if(monThresholdFlag.get(3) != -1)
						{
							if(monThresholdFlag.get(3) == 0)
							{
								// 设置告警状态
								monErrorInfo.setAlarmSmsFlag(1, shostId, "3", System.currentTimeMillis(), null);
								// 设置邮件告警状态
								monErrorInfo.setAlarmSmsFlag(5, shostId, "3", System.currentTimeMillis(), null);
							}
							else if(monThresholdFlag.get(3) == 1)
							{
								msg = "物理内存使用量达到告警阀值：" + monDhost.getMemusage() + "M/" + monShost.getMemusage() + "M(使用量/告警阀值)。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 1, AlarmSource, upateTime);
								//设置告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，物理内存使用量达到告警阀值，当前使用量：" 
										+ monDhost.getMemusage() + "，告警阀值：" + monShost.getMemusage()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
							else if(System.currentTimeMillis() - monThresholdFlag.get(3) >= 3*60*1000)
							{
								msg = "物理内存使用量达到告警阀值：" + monDhost.getMemusage() + "M/" + monShost.getMemusage() + "M(使用量/告警阀值)。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 1, AlarmSource, upateTime);
								// 设置告警状态为已发送告警
								if(effPhone != null && monErrorInfo.setAlarmSmsFlag(1, shostId, "3", 1, null))
								{
									monContent.append(msg);
								}
								// 设置告警状态为已发送告警
								if(effEmail != null && monErrorInfo.setAlarmSmsFlag(5, shostId, "3", 1, null))
								{
									emailMsg.append(msg);
								}
								//设置邮件告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，物理内存使用量达到告警阀值，当前使用量：" + monDhost.getMemusage() 
										+ "，告警阀值：" + monShost.getMemusage()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
						}
						else
						{
							//设置告警状态标识为告警
							isMonitorFlag = true;
						}
					}
					else
					{
						// 设置告警状态为0:正常
						monErrorInfo.setAlarmSmsFlag(1, shostId, "3", 0, false, null);
						// 设置邮件告警状态为0:正常
						monErrorInfo.setAlarmSmsFlag(5, shostId, "3", 0, false, null);
					}
					// 虚拟内存使用量(两次超过告警阀值时才进行告警)
					if(monShost.getVmemusage() != 0 && monDhost.getVmemusage() >= monShost.getVmemusage())
					{
						// 处理状态不是处理中
						if(monThresholdFlag.get(4) != -1)
						{
							if(monThresholdFlag.get(4) == 0)
							{
								// 设置告警状态
								monErrorInfo.setAlarmSmsFlag(1, shostId, "4", System.currentTimeMillis(), null);
								// 设置邮件告警状态
								monErrorInfo.setAlarmSmsFlag(5, shostId, "4", System.currentTimeMillis(), null);
							}
							else if(monThresholdFlag.get(4) == 1)
							{
								msg = "虚拟内存使用量达到告警阀值：" + monDhost.getVmemusage() + "M/" + monShost.getVmemusage() + "M(使用量/告警阀值)。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
								//设置告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，虚拟内存使用量达到告警阀值，当前使用量：" + monDhost.getVmemusage() 
										+ "，告警阀值：" + monShost.getVmemusage() + "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
							else if(System.currentTimeMillis() - monThresholdFlag.get(4) >= 3*60*1000)
							{
								msg = "虚拟内存使用量达到告警阀值：" + monDhost.getVmemusage() + "M/" + monShost.getVmemusage() + "M(使用量/告警阀值)。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
								// 设置告警状态为已发送告警
								if(effPhone != null && monErrorInfo.setAlarmSmsFlag(1, shostId, "4", 1, null))
								{
									monContent.append(msg);
								}
								// 设置邮件告警状态为已发送告警
								if(effEmail != null && monErrorInfo.setAlarmSmsFlag(5, shostId, "4", 1, null))
								{
									emailMsg.append(msg);
								}
								//设置告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，虚拟内存使用量达到告警阀值，当前使用量：" + monDhost.getVmemusage() 
										+ "，告警阀值：" + monShost.getVmemusage()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
						}
						else
						{
							//设置告警状态标识为告警
							isMonitorFlag = true;
						}
					}
					else
					{
						// 设置告警标识为正常
						monErrorInfo.setAlarmSmsFlag(1, shostId, "4", 0, false, null);
						// 设置邮件告警标识为正常
						monErrorInfo.setAlarmSmsFlag(5, shostId, "4", 0, false, null);
					}
					// 磁盘空间使用量
					if(monShost.getDiskfreespace() != 0 && monDhost.getDiskfreespace() <= monShost.getDiskfreespace())
					{
						// 处理状态不是处理中
						if(monThresholdFlag.get(5) != -1)
						{
							msg = "磁盘总容量为" + monDhost.getDiskspace() + "M，可用空间为" + monDhost.getDiskfreespace() + "M，达到告警阀值（" + monShost.getDiskfreespace() + "M）。";
							// 向告警表和告警历史表写入告警数据
							monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 5, 1, AlarmSource, upateTime);
							// 设置告警短信内容
							if(monThresholdFlag.get(5) == 0 && effPhone != null)
							{
								// 设置告警状态为1:已发送告警
								if(monErrorInfo.setAlarmSmsFlag(1, shostId, "5", 1, true, null))
								{
									monContent.append(msg);
								}						
							}
							// 设置告警短信内容
							if(monThresholdFlag.get(11) == 0 && effEmail!= null)
							{
								// 设置邮件告警状态为1:已发送告警
								if(monErrorInfo.setAlarmSmsFlag(5, shostId, "5", 1, true, null))
								{
									emailMsg.append(msg);
								}
							}
						
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，磁盘空间使用量达到告警阀值，可用空间：" + monDhost.getDiskfreespace() 
								+ "，告警阀值：" + monShost.getDiskfreespace()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());

					}
					else
					{
						// 设置告警状态为0:正常
						monErrorInfo.setAlarmSmsFlag(1, shostId, "5", 0, false, null);
						// 设置邮件告警状态为0:正常
						monErrorInfo.setAlarmSmsFlag(5, shostId, "5", 0, false, null);
					}
					// 进程总数据
					if(monShost.getProcesscnt() != 0 && monDhost.getProcesscnt() >= monShost.getProcesscnt())
					{
						// 处理状态不是处理中
						if(monThresholdFlag.get(6) != -1)
						{
							msg = "进程总数达到告警阀值：" + monDhost.getProcesscnt() + "个/" + monShost.getProcesscnt() + "个(总数/告警阀值)。";
							// 向告警表和告警历史表写入告警数据
							monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 6, 1, AlarmSource, upateTime);
							// 设置告警短信内容
							if(monThresholdFlag.get(6) == 0 && effPhone != null)
							{
								if(monErrorInfo.setAlarmSmsFlag(1, shostId, "6", 1, true, null))
								{
									// 设置告警状态为1:已发送告警
									monContent.append(msg);
								}
							}
							// 设置告警邮件内容
							if(monThresholdFlag.get(12) == 0 && effEmail != null)
							{
								if(monErrorInfo.setAlarmSmsFlag(5, shostId, "6", 1, true, null))
								{
									// 设置邮件告警状态为1:已发送告警
									emailMsg.append(msg);
								}
							}
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，虚拟内存使用量达到告警阀值，当前总数：" + monDhost.getProcesscnt() 
								+ "，告警阀值：" + monShost.getProcesscnt()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
					else
					{
						// 设置告警状态为0:正常
						monErrorInfo.setAlarmSmsFlag(1, shostId, "6", 0, false, null);
						// 设置告警状态为0:正常
						monErrorInfo.setAlarmSmsFlag(5, shostId, "6", 0, false, null);
					}
					// 告警级别
					int evtType = 0;
					if(isMonitorFlag)
					{
						evtType = 1;
					}
					else
					{
						evtType = 0;
					}
					
					// 设置告警级别
					monErrorInfo.setMonEvtType(1, evtType, shostId, null);
					// 发送告警短信
					// 发送告警短信
					if(monContent.length() > 0)
					{
						// 告警内容
						String monSendContent = "主机编号:" + shostId + "，主机名:" + monShost.getHostname() + "，" + monContent;
						monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
						//new Thread(new sendAlarmSms(null, monSendContent, effPhone)).start();
					}
					//发送告警邮件
					if(emailMsg.length() > 0)
					{
						// 告警内容
						String alarmContent = emailMsg.toString();
						StringBuffer emailContent = new StringBuffer();
						emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
						.append("监控类型：主机监控").append("<br/>")
						.append("监控名称："+monShost.getHostname()).append("<br/>")
						.append("告警级别："+((evtType==1) ?" 警告":"严重")).append("<br/>")
						.append("事件描述："+alarmContent).append("<br/>")
						.append("告警时间："+sdf.format(new Date())).append("<br/>");
						//设置邮件内容参数
						MonEmailParams mep = new MonEmailParams();
						mep.setContent(emailContent.toString());
						mep.setCorpCode(corpcode);
						mep.setEmail(effEmail);
						// 发送告警邮件
						new Thread(new sendAlarmEmail( mep)).start();
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "主机监控数据参数分析异常，主机编号：" + monShost.getHostid() + "，主机名称：" + monShost.getHostid()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
	}
	
	/**
	 * 程序参数分析
	 * 
	 * @description
	 * @param monSproce
	 * @param monDproce
	 * @param sproceId
	 * @param lfMonErr
	 * @param monPhone
	 * @param isVaild
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 上午10:07:49
	 */
	public void proceParamsAnalysis(LfMonSproce monSproce, MonDproceParams monDproce, Long sproceId, LfMonErr lfMonErr, String effPhone)
	{
		// 告警短信内容
		StringBuffer monContent = new StringBuffer("");
		// 事件内容
		String msg = "";
		//告警状态标识 false 无告警; true 告警
		boolean isMonitorFlag = false;
		// 告警来源 2：程序
		int AlarmSource = 2;
		// 更新缓存数据时间
		Timestamp upateTime = monDproce.getUpdatetime();
		try
		{
			// CPU占用比率(3次超过告警阀值时才进行告警)
			int cpuUsage = monDproce.getCpuusage();
			// 告警阀值标识
			Map<Integer, Long> monThresholdFlag = monDproce.getMonThresholdFlag();
			if(monSproce.getCpubl() != 0 && cpuUsage >= monSproce.getCpubl())
			{
				if(monThresholdFlag.get(2) != -1)
				{
					if(monThresholdFlag.get(2) == 0)
					{
						// 设置告警状态
						monErrorInfo.setAlarmSmsFlag(2, sproceId, "2", System.currentTimeMillis(), null);
					}
					else if(monThresholdFlag.get(2) == 1)
					{
						msg = "CPU占用率持续过高，当前占用率：" + cpuUsage + "%，已达到告警阀值：" + monSproce.getCpubl()+"%。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 2, 1, AlarmSource, upateTime);
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，CPU占用量达到告警阀值，当前占用率：" + cpuUsage 
								+ "，告警阀值：" + monSproce.getCpubl()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
					else if(System.currentTimeMillis() - monThresholdFlag.get(2) >= 3*60*1000)
					{
						msg = "CPU占用率持续过高，当前占用率：" + cpuUsage + "%，已达到告警阀值：" + monSproce.getCpubl()+"%。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 2, 1, AlarmSource, upateTime);
						// 设置告警状态为已发送告警
						if(effPhone != null && monErrorInfo.setAlarmSmsFlag(2, sproceId, "2", 1, null))
						{
							monContent.append(msg);
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，CPU占用量达到告警阀值，当前占用率：" + cpuUsage 
								+ "，告警阀值：" + monSproce.getCpubl()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
				else
				{
					//设置告警状态标识为告警
					isMonitorFlag = true;
				}
			}
			else
			{
				// 设置告警标识为0：正常
				monErrorInfo.setAlarmSmsFlag(2, sproceId, "2", 0, false, null);
			}
			// 物理内存使用量(两次超过告警阀值时才进行告警)
			if(monSproce.getMemuse() != 0 && monDproce.getMemusage() >= monSproce.getMemuse())
			{
				if(monThresholdFlag.get(3) != -1)
				{
					if(monThresholdFlag.get(3) == 0)
					{
						// 设置告警状态
						monErrorInfo.setAlarmSmsFlag(2, sproceId, "3", System.currentTimeMillis(), null);
					}
					else if(monThresholdFlag.get(3) == 1)
					{
						msg = "物理内存使用量达到告警阀值：" + monDproce.getMemusage() + "M/" + monSproce.getMemuse() + "M(使用量/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 1, AlarmSource, upateTime);
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，物理内存使用量达到告警阀值，当前使用率：" + monDproce.getMemusage() 
								+ "，告警阀值：" + monSproce.getMemuse()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
					else if(System.currentTimeMillis() - monThresholdFlag.get(3) >= 3*60*1000)
					{
						msg = "物理内存使用量达到告警阀值：" + monDproce.getMemusage() + "M/" + monSproce.getMemuse() + "M(使用量/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 1, AlarmSource, upateTime);
						// 设置告警状态为已发送告警
						if(effPhone != null && monErrorInfo.setAlarmSmsFlag(2, sproceId, "3", 1, null))
						{
							monContent.append(msg);
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，物理内存使用量达到告警阀值，当前使用率：" + monDproce.getMemusage() 
								+ "，告警阀值：" + monSproce.getMemuse()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
				else
				{
					//设置告警状态标识为告警
					isMonitorFlag = true;
				}
			}
			else
			{
				// 设置告警标识为0:正常
				monErrorInfo.setAlarmSmsFlag(2, sproceId, "3", 0, false, null);
			}
			// 虚拟内存使用量(两次超过告警阀值时才进行告警)
			if(monSproce.getVmemuse() != 0 && monDproce.getVmemusage() >= monSproce.getVmemuse())
			{
				if(monThresholdFlag.get(4) != -1)
				{
					if(monThresholdFlag.get(4) == 0)
					{
						// 设置告警状态
						monErrorInfo.setAlarmSmsFlag(2, sproceId, "4", System.currentTimeMillis(), null);
					}
					else if(monThresholdFlag.get(4) == 1)
					{
						msg = "虚拟内存使用量达到告警阀值：" + monDproce.getVmemusage() + "M/" + monSproce.getVmemuse() + "M(使用量/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，虚拟内存使用量达到告警阀值，当前使用量：" + monDproce.getVmemusage()
								+ "，告警阀值：" + monSproce.getVmemuse()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
					else if(System.currentTimeMillis() - monThresholdFlag.get(4) >= 3*60*1000)
					{
						msg = "虚拟内存使用量达到告警阀值：" + monDproce.getVmemusage() + "M/" + monSproce.getVmemuse() + "M(使用量/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
						// 设置告警状态为已发送告警
						if(effPhone != null && monErrorInfo.setAlarmSmsFlag(2, sproceId, "4", 1, null))
						{
							monContent.append(msg);
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，虚拟内存使用量达到告警阀值，当前使用量：" + monDproce.getVmemusage() 
								+ "，告警阀值：" + monSproce.getVmemuse()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
				else
				{
					//设置告警状态标识为告警
					isMonitorFlag = true;
				}
			}
			else
			{
				// 设置告警状态0:正常
				monErrorInfo.setAlarmSmsFlag(2, sproceId, "4", 0, false, null);
			}
			// 磁盘空间使用量
			if(monSproce.getHarddiskspace() != 0 && monDproce.getDiskFree() <= monSproce.getHarddiskspace())
			{
				if(monDproce.getMonThresholdFlag().get(5) != -1)
				{
					msg = "磁盘剩余量达到告警阀值：" + monDproce.getDiskFree() + "M/" + monSproce.getHarddiskspace() + "M(剩余量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 5, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDproce.getMonThresholdFlag().get(5) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(2, sproceId, "5", 1, true, null))
						{
							monContent.append(msg);
							// 设置告警状态为1:已发送告警
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，磁盘空间使用量达到告警阀值，当前剩余量：" + monDproce.getDiskFree() 
						+ "，告警阀值：" + monSproce.getHarddiskspace()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态0:正常
				monErrorInfo.setAlarmSmsFlag(2, sproceId, "5", 0, false, null);
			}
			// 告警级别
			int evtType = 0;
			if(isMonitorFlag)
			{
				evtType = 1;
			}
			else
			{
				evtType = 0;
			}
			// 设置告警级别
			monErrorInfo.setMonEvtType(2, evtType, sproceId, null);
			// 发送告警短信
			if(monContent.length() > 0)
			{
				// 告警内容
				String monSendContent = "程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，" + monContent;
				monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
				//new Thread(new sendAlarmSms(null, monSendContent, effPhone)).start();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "程序监控数据参数分析异常，程序编号：" + monSproce.getProceid() + "，程序名称：" + monSproce.getProcename()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 程序参数分析
	 */
	public void proceParamsAnalysis(LfMonSproce monSproce, MonDproceParams monDproce, Long sproceId, LfMonErr lfMonErr, String effPhone,String effEmail){
		// 告警短信内容
				StringBuffer monContent = new StringBuffer("");
				// 告警邮件内容
				StringBuffer emailMsg = new StringBuffer();
				// 事件内容
				String msg = "";
				//告警状态标识 false 无告警; true 告警
				boolean isMonitorFlag = false;
				// 告警来源 2：程序
				int AlarmSource = 2;
				// 更新缓存数据时间
				Timestamp upateTime = monDproce.getUpdatetime();
				try
				{
					// CPU占用比率(3次超过告警阀值时才进行告警)
					int cpuUsage = monDproce.getCpuusage();
					// 告警阀值标识
					Map<Integer, Long> monThresholdFlag = monDproce.getMonThresholdFlag();
					if(monSproce.getCpubl() != 0 && cpuUsage >= monSproce.getCpubl())
					{
						if(monThresholdFlag.get(2) != -1)
						{
							if(monThresholdFlag.get(2) == 0)
							{
								// 设置告警状态
								monErrorInfo.setAlarmSmsFlag(2, sproceId, "2", System.currentTimeMillis(), null);
								// 设置邮件告警状态
								monErrorInfo.setAlarmSmsFlag(6, sproceId, "2", System.currentTimeMillis(), null);
							}
							else if(monThresholdFlag.get(2) == 1)
							{
								msg = "CPU占用率持续过高，当前占用率：" + cpuUsage + "%，已达到告警阀值：" + monSproce.getCpubl()+"%。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 2, 1, AlarmSource, upateTime);
								//设置告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，CPU占用量达到告警阀值，当前占用率：" + cpuUsage 
										+ "，告警阀值：" + monSproce.getCpubl()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
							else if(System.currentTimeMillis() - monThresholdFlag.get(2) >= 3*60*1000)
							{
								msg = "CPU占用率持续过高，当前占用率：" + cpuUsage + "%，已达到告警阀值：" + monSproce.getCpubl()+"%。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 2, 1, AlarmSource, upateTime);
								// 设置告警状态为已发送告警
								if(effPhone != null && monErrorInfo.setAlarmSmsFlag(2, sproceId, "2", 1, null))
								{
									monContent.append(msg);
								}
								// 设置邮件告警状态为已发送告警
								if(effEmail != null && monErrorInfo.setAlarmSmsFlag(6, sproceId, "2", 1, null))
								{
									emailMsg.append(msg);
								}
								//设置告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，CPU占用量达到告警阀值，当前占用率：" + cpuUsage 
										+ "，告警阀值：" + monSproce.getCpubl()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
						}
						else
						{
							//设置告警状态标识为告警
							isMonitorFlag = true;
						}
					}
					else
					{
						// 设置告警标识为0：正常
						monErrorInfo.setAlarmSmsFlag(2, sproceId, "2", 0, false, null);
						// 设置邮件告警标识为0：正常
						monErrorInfo.setAlarmSmsFlag(6, sproceId, "2", 0, false, null);
					}
					// 物理内存使用量(两次超过告警阀值时才进行告警)
					if(monSproce.getMemuse() != 0 && monDproce.getMemusage() >= monSproce.getMemuse())
					{
						if(monThresholdFlag.get(3) != -1)
						{
							if(monThresholdFlag.get(3) == 0)
							{
								// 设置告警状态
								monErrorInfo.setAlarmSmsFlag(2, sproceId, "3", System.currentTimeMillis(), null);
								// 设置邮件告警状态
								monErrorInfo.setAlarmSmsFlag(6, sproceId, "3", System.currentTimeMillis(), null);
							}
							else if(monThresholdFlag.get(3) == 1)
							{
								msg = "物理内存使用量达到告警阀值：" + monDproce.getMemusage() + "M/" + monSproce.getMemuse() + "M(使用量/告警阀值)。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 1, AlarmSource, upateTime);
								//设置告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，物理内存使用量达到告警阀值，当前使用率：" + monDproce.getMemusage() 
										+ "，告警阀值：" + monSproce.getMemuse()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
							else if(System.currentTimeMillis() - monThresholdFlag.get(3) >= 3*60*1000)
							{
								msg = "物理内存使用量达到告警阀值：" + monDproce.getMemusage() + "M/" + monSproce.getMemuse() + "M(使用量/告警阀值)。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 1, AlarmSource, upateTime);
								// 设置告警状态为已发送告警
								if(effPhone != null && monErrorInfo.setAlarmSmsFlag(2, sproceId, "3", 1, null))
								{
									monContent.append(msg);
								}
								// 设置邮件告警状态为已发送告警
								if(effEmail != null && monErrorInfo.setAlarmSmsFlag(6, sproceId, "3", 1, null))
								{
									emailMsg.append(msg);
								}
								//设置告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，物理内存使用量达到告警阀值，当前使用率：" + monDproce.getMemusage() 
										+ "，告警阀值：" + monSproce.getMemuse()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
						}
						else
						{
							//设置告警状态标识为告警
							isMonitorFlag = true;
						}
					}
					else
					{
						// 设置告警标识为0:正常
						monErrorInfo.setAlarmSmsFlag(2, sproceId, "3", 0, false, null);
						// 设置邮件告警标识为0:正常
						monErrorInfo.setAlarmSmsFlag(6, sproceId, "3", 0, false, null);
					}
					// 虚拟内存使用量(两次超过告警阀值时才进行告警)
					if(monSproce.getVmemuse() != 0 && monDproce.getVmemusage() >= monSproce.getVmemuse())
					{
						if(monThresholdFlag.get(4) != -1)
						{
							if(monThresholdFlag.get(4) == 0)
							{
								// 设置告警状态
								monErrorInfo.setAlarmSmsFlag(2, sproceId, "4", System.currentTimeMillis(), null);
								// 设置邮件告警状态
								monErrorInfo.setAlarmSmsFlag(6, sproceId, "4", System.currentTimeMillis(), null);
							}
							else if(monThresholdFlag.get(4) == 1)
							{
								msg = "虚拟内存使用量达到告警阀值：" + monDproce.getVmemusage() + "M/" + monSproce.getVmemuse() + "M(使用量/告警阀值)。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
								//设置告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，虚拟内存使用量达到告警阀值，当前使用量：" + monDproce.getVmemusage() 
										+ "，告警阀值：" + monSproce.getVmemuse()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
							else if(System.currentTimeMillis() - monThresholdFlag.get(4) >= 3*60*1000)
							{
								msg = "虚拟内存使用量达到告警阀值：" + monDproce.getVmemusage() + "M/" + monSproce.getVmemuse() + "M(使用量/告警阀值)。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
								// 设置告警状态为已发送告警
								if(effPhone != null && monErrorInfo.setAlarmSmsFlag(2, sproceId, "4", 1, null))
								{
									monContent.append(msg);
								}
								// 设置告警状态为已发送告警
								if(effEmail!= null && monErrorInfo.setAlarmSmsFlag(6, sproceId, "4", 1, null))
								{
									emailMsg.append(msg);
								}
								//设置告警状态标识为告警
								isMonitorFlag = true;
								EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，虚拟内存使用量达到告警阀值，当前使用量：" + monDproce.getVmemusage() 
										+ "，告警阀值：" + monSproce.getVmemuse()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
							}
						}
						else
						{
							//设置告警状态标识为告警
							isMonitorFlag = true;
						}
					}
					else
					{
						// 设置告警状态0:正常
						monErrorInfo.setAlarmSmsFlag(2, sproceId, "4", 0, false, null);
						// 设置告警状态0:正常
						monErrorInfo.setAlarmSmsFlag(6, sproceId, "4", 0, false, null);
					}
					// 磁盘空间使用量
					if(monSproce.getHarddiskspace() != 0 && monDproce.getDiskFree() <= monSproce.getHarddiskspace())
					{
						if(monDproce.getMonThresholdFlag().get(5) != -1)
						{
							msg = "磁盘剩余量达到告警阀值：" + monDproce.getDiskFree() + "M/" + monSproce.getHarddiskspace() + "M(剩余量/告警阀值)。";
							// 向告警表和告警历史表写入告警数据
							monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 5, 1, AlarmSource, upateTime);
							// 设置告警短信内容
							if(monDproce.getMonThresholdFlag().get(5) == 0 && effPhone != null)
							{
								if(monErrorInfo.setAlarmSmsFlag(2, sproceId, "5", 1, true, null))
								{
									monContent.append(msg);
									// 设置告警状态为1:已发送告警
								}
							}
							// 设置告警邮件内容
							if(monDproce.getMonThresholdFlag().get(11) == 0 && effEmail != null)
							{
								if(monErrorInfo.setAlarmSmsFlag(6, sproceId, "5", 1, true, null))
								{
									emailMsg.append(msg);
									// 设置告警状态为1:已发送告警
								}
							}
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，磁盘空间使用量达到告警阀值，当前剩余量：" + monDproce.getDiskFree() 
								+ "，告警阀值：" + monSproce.getHarddiskspace()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
					else
					{
						// 设置告警状态0:正常
						monErrorInfo.setAlarmSmsFlag(2, sproceId, "5", 0, false, null);
						// 设置邮件告警状态0:正常
						monErrorInfo.setAlarmSmsFlag(6, sproceId, "5", 0, false, null);
					}
					// 告警级别
					int evtType = 0;
					if(isMonitorFlag)
					{
						evtType = 1;
					}
					else
					{
						evtType = 0;
					}
					// 设置告警级别
					monErrorInfo.setMonEvtType(2, evtType, sproceId, null);
					// 发送告警短信
					if(monContent.length() > 0)
					{
						// 告警内容
						String monSendContent = "程序编号:" + sproceId + "，程序名称:" + monSproce.getProcename() + "，" + monContent;
						monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
						//new Thread(new sendAlarmSms(null, monSendContent, effPhone)).start();
					}
					
					// 发送告警短信
					if (emailMsg.length() > 0) {
						// 告警内容
						String alarmContent = emailMsg.toString();
						StringBuffer emailContent = new StringBuffer();
						emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
								.append("监控类型：程序监控").append("<br/>")
								.append("监控名称：" + monSproce.getProcename())
								.append("<br/>").append("告警级别："+(isMonitorFlag?"警告":"严重")).append("<br/>")
								.append("事件描述：" + alarmContent).append("<br/>")
								.append("告警时间：" + sdf.format(new Date()))
								.append("<br/>");
						// 设置邮件内容参数
						MonEmailParams mep = new MonEmailParams();
						mep.setContent(emailContent.toString());
						mep.setCorpCode(corpcode);
						mep.setEmail(effEmail);
						// 发送告警邮件
						new Thread(new sendAlarmEmail(mep)).start();
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "程序监控数据参数分析异常，程序编号：" + monSproce.getProceid() + "，程序名称：" + monSproce.getProcename()
							+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
	}

	/**
	 * SP账号参数分析
	 * 
	 * @description
	 * @param monSspacinfo
	 * @param monDspacinfo
	 * @param sSpAccountId
	 * @param lfMonErr
	 * @param monPhone
	 * @param isVaild
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 下午01:03:55
	 */
	public void spAccountParamsAnalysis(LfMonSspacinfo monSspacinfo, MonDspAccountParams monDspacinfo, String sSpAccountId, LfMonErr lfMonErr, String effPhone)
	{
		// 告警短信内容
		StringBuffer monContent = new StringBuffer("");
		// 事件内容
		String msg = "";
		//告警状态标识 false 无告警; true 告警
		boolean isMonitorFlag = false;
		// 告警来源 3：SP账号
		int AlarmSource = 3;
		// 更新缓存数据时间
		Timestamp upateTime = monDspacinfo.getUpdatetime();
		try
		{
			// MT滞留量(待发量)
			if(monSspacinfo.getMtremained() != 0 && monDspacinfo.getMtremained() >= monSspacinfo.getMtremained())
			{
				// 处理状态不是处理中
				if(monDspacinfo.getMonThresholdFlag().get(4) != -1)
				{
					msg = "MT待发量达到告警阀值：" + monDspacinfo.getMtremained() + "/" + monSspacinfo.getMtremained() + "(MT待发量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDspacinfo.getMonThresholdFlag().get(4) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(3, null, "4", 1, true, sSpAccountId))
						{
							// 设置告警状态为1:已发送告警
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，MT待发量达到告警阀值，当前待发量：" + monDspacinfo.getMtremained() 
						+ "，告警阀值：" + monSspacinfo.getMtremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态0:正常
				monErrorInfo.setAlarmSmsFlag(3, null, "4", 0, false, sSpAccountId);
			}
			// MO滞留量
			if(monSspacinfo.getMoremained() != 0 && monDspacinfo.getMoremained() >= monSspacinfo.getMoremained())
			{
				// 处理状态不是处理中
				if(monDspacinfo.getMonThresholdFlag().get(7) != -1)
				{
					msg = "MO滞留量达到告警阀值：" + monDspacinfo.getMoremained() + "/" + monSspacinfo.getMoremained() + "(MO滞留量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 7, 1, AlarmSource, upateTime);
					// 发送告警短信
					if(monDspacinfo.getMonThresholdFlag().get(7) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(3, null, "7", 1, true, sSpAccountId))
						{
							// 设置告警短信内容
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，MO滞留量达到告警阀值，当前滞留量：" + monDspacinfo.getMoremained() 
						+ "，告警阀值：" + monSspacinfo.getMoremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());

			}
			else
			{
				// 设置告警状态0:正常
				monErrorInfo.setAlarmSmsFlag(3, null, "7", 0, false, sSpAccountId);
			}
			// MO最低接收率( MO转发量/MO接收总量)
			if(monDspacinfo.getMototalrecv() != 0)
			{
				int moRecvratio = (int) (((double) monDspacinfo.getMohavesnd() / (double) monDspacinfo.getMototalrecv()) * 100);
				if(monSspacinfo.getMorecvratio() != 0 && moRecvratio <= monSspacinfo.getMorecvratio())
				{
					// 处理状态不是处理中
					if(monDspacinfo.getMonThresholdFlag().get(8) != -1)
					{
						msg = "MO最低接收率达到告警阀值：" + moRecvratio + "/" + monSspacinfo.getMorecvratio() + "(MO最低接收率/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 8, 1, AlarmSource, upateTime);
						// 设置告警短信内容
						if(monDspacinfo.getMonThresholdFlag().get(8) == 0 && effPhone != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(3, null, "8", 1, true, sSpAccountId))
							{
								// 设置告警状态为1:已发送告警
								monContent.append(msg);
							}
						}
					}
					//设置告警状态标识为告警
					isMonitorFlag = true;
					EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，MO最低接收率达到告警阀值，当前转发量：" + monDspacinfo.getMohavesnd() 
							+ "，当前接收总量：" + monDspacinfo.getMototalrecv() + "，告警阀值：" + monSspacinfo.getMorecvratio()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
				else
				{
					// 设置告警状态0:正常
					monErrorInfo.setAlarmSmsFlag(3, null, "8", 0, false, sSpAccountId);
				}
			}
			// RPT滞留量
			if(monSspacinfo.getRptremained() != 0 && monDspacinfo.getRptremained() >= monSspacinfo.getRptremained())
			{
				// 处理状态不是处理中
				if(monDspacinfo.getMonThresholdFlag().get(9) != -1)
				{
					msg = "RPT滞留量达到告警阀值：" + monDspacinfo.getRptremained() + "/" + monSspacinfo.getRptremained() + "(RPT滞留量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 9, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDspacinfo.getMonThresholdFlag().get(9) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(3, null, "9", 1, true, sSpAccountId))
						{
							// 设置告警状态为1:已发送告警
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，RPT滞留量达到告警阀值，当前滞留量：" + monDspacinfo.getRptremained() 
						+ "，告警阀值：" + monSspacinfo.getRptremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态0:正常
				monErrorInfo.setAlarmSmsFlag(3, null, "9", 0, false, sSpAccountId);
			}
			// RPT最低接收率(转发量/接收总量)
			if(monDspacinfo.getRpttotalrecv() != 0)
			{
				int rptRecvratio = (int) (((double) monDspacinfo.getRptHaveSnd() / (double) monDspacinfo.getRpttotalrecv()) * 100);
				if(monSspacinfo.getRptrecvratio() != 0 && rptRecvratio <= monSspacinfo.getRptrecvratio())
				{
					// 处理状态不是处理中
					if(monDspacinfo.getMonThresholdFlag().get(11) != -1)
					{
						msg = "RPT最低接收率达到告警阀值：" + rptRecvratio + "/" + monSspacinfo.getRptrecvratio() + "( RPT最低接收率/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 11, 1, AlarmSource, upateTime);
						// 设置告警短信内容
						if(monDspacinfo.getMonThresholdFlag().get(11) == 0 && effPhone != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(3, null, "11", 1, true, sSpAccountId))
							{
								// 设置告警状态为1:已发送告警
								monContent.append(msg);
							}
						}
					}
					//设置告警状态标识为告警
					isMonitorFlag = true;
					EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "， RPT最低接收率达到告警阀值，当前转发量：" + monDspacinfo.getRptHaveSnd() 
							+ "，当前接收总量：" + monDspacinfo.getRpttotalrecv() + "，告警阀值：" + monSspacinfo.getRptrecvratio()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
				else
				{
					// 设置告警状态0:正常
					monErrorInfo.setAlarmSmsFlag(3, null, "11", 0, false, sSpAccountId);
				}
			}
			// 告警级别
			int evtType = 0;
			if(isMonitorFlag)
			{
				evtType = 1;
			}
			// 设置告警级别
			monErrorInfo.setMonEvtType(3, evtType, null, sSpAccountId);
			// 发送告警短信
			if(monContent.length() > 0)
			{
				// 告警内容
				String monSendContent = "SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，" + monContent;
				monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
				//new Thread(new sendAlarmSms(null, monSendContent, effPhone)).start();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号数据参数分析异常，SP账号：" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * SP账号参数分析
	 * @description    
	 * @param monSspacinfo
	 * @param monDspacinfo
	 * @param sSpAccountId
	 * @param lfMonErr
	 * @param effPhone
	 * @param evtTypeFlag       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-12 下午02:00:27
	 */
	public void spAccountParamsAnalysis(LfMonSspacinfo monSspacinfo, MonDspAccountParams monDspacinfo, String sSpAccountId, LfMonErr lfMonErr, String effPhone, Integer evtTypeFlag)
	{
		// 告警短信内容
		StringBuffer monContent = new StringBuffer("");
		// 事件内容
		String msg = "";
		//告警状态标识 false 无告警; true 告警
		boolean isMonitorFlag = false;
		// 告警来源 3：SP账号
		int AlarmSource = 3;
		// 更新缓存数据时间
		Timestamp upateTime = monDspacinfo.getUpdatetime();
		try
		{
			// MT滞留量(待发量)
			if(monSspacinfo.getMtremained() != 0 && monDspacinfo.getMtremained() >= monSspacinfo.getMtremained())
			{
				// 处理状态不是处理中
				if(monDspacinfo.getMonThresholdFlag().get(4) != -1)
				{
					msg = "MT待发量达到告警阀值：" + monDspacinfo.getMtremained() + "/" + monSspacinfo.getMtremained() + "(MT待发量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDspacinfo.getMonThresholdFlag().get(4) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(3, null, "4", 1, true, sSpAccountId))
						{
							// 设置告警状态为1:已发送告警
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，MT待发量达到告警阀值，当前待发量：" + monDspacinfo.getMtremained() 
						+ "，告警阀值：" + monSspacinfo.getMtremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态0:正常
				monErrorInfo.setAlarmSmsFlag(3, null, "4", 0, false, sSpAccountId);
			}
			// MO滞留量
			if(monSspacinfo.getMoremained() != 0 && monDspacinfo.getMoremained() >= monSspacinfo.getMoremained())
			{
				// 处理状态不是处理中
				if(monDspacinfo.getMonThresholdFlag().get(7) != -1)
				{
					msg = "MO滞留量达到告警阀值：" + monDspacinfo.getMoremained() + "/" + monSspacinfo.getMoremained() + "(MO滞留量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 7, 1, AlarmSource, upateTime);
					// 发送告警短信
					if(monDspacinfo.getMonThresholdFlag().get(7) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(3, null, "7", 1, true, sSpAccountId))
						{
							// 设置告警短信内容
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，MO滞留量达到告警阀值，当前滞留量：" + monDspacinfo.getMoremained() 
						+ "，告警阀值：" + monSspacinfo.getMoremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());

			}
			else
			{
				// 设置告警状态0:正常
				monErrorInfo.setAlarmSmsFlag(3, null, "7", 0, false, sSpAccountId);
			}
			// MO最低接收率( MO转发量/MO接收总量)
			if(monDspacinfo.getMototalrecv() != 0)
			{
				int moRecvratio = (int) (((double) monDspacinfo.getMohavesnd() / (double) monDspacinfo.getMototalrecv()) * 100);
				if(monSspacinfo.getMorecvratio() != 0 && moRecvratio <= monSspacinfo.getMorecvratio())
				{
					// 处理状态不是处理中
					if(monDspacinfo.getMonThresholdFlag().get(8) != -1)
					{
						msg = "MO最低接收率达到告警阀值：" + moRecvratio + "/" + monSspacinfo.getMorecvratio() + "(MO最低接收率/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 8, 1, AlarmSource, upateTime);
						// 设置告警短信内容
						if(monDspacinfo.getMonThresholdFlag().get(8) == 0 && effPhone != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(3, null, "8", 1, true, sSpAccountId))
							{
								// 设置告警状态为1:已发送告警
								monContent.append(msg);
							}
						}
					}
					//设置告警状态标识为告警
					isMonitorFlag = true;
					EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，MO最低接收率达到告警阀值，当前转发量：" + monDspacinfo.getMohavesnd() 
							+ "，当前接收总量：" + monDspacinfo.getMototalrecv() + "，告警阀值：" + monSspacinfo.getMorecvratio()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
				else
				{
					// 设置告警状态0:正常
					monErrorInfo.setAlarmSmsFlag(3, null, "8", 0, false, sSpAccountId);
				}
			}
			// RPT滞留量
			if(monSspacinfo.getRptremained() != 0 && monDspacinfo.getRptremained() >= monSspacinfo.getRptremained())
			{
				// 处理状态不是处理中
				if(monDspacinfo.getMonThresholdFlag().get(9) != -1)
				{
					msg = "RPT滞留量达到告警阀值：" + monDspacinfo.getRptremained() + "/" + monSspacinfo.getRptremained() + "(RPT滞留量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 9, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDspacinfo.getMonThresholdFlag().get(9) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(3, null, "9", 1, true, sSpAccountId))
						{
							// 设置告警状态为1:已发送告警
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，RPT滞留量达到告警阀值，当前滞留量：" + monDspacinfo.getRptremained() 
						+ "，告警阀值：" + monSspacinfo.getRptremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态0:正常
				monErrorInfo.setAlarmSmsFlag(3, null, "9", 0, false, sSpAccountId);
			}
			// RPT最低接收率(转发量/接收总量)
			if(monDspacinfo.getRpttotalrecv() != 0)
			{
				int rptRecvratio = (int) (((double) monDspacinfo.getRptHaveSnd() / (double) monDspacinfo.getRpttotalrecv()) * 100);
				if(monSspacinfo.getRptrecvratio() != 0 && rptRecvratio <= monSspacinfo.getRptrecvratio())
				{
					// 处理状态不是处理中
					if(monDspacinfo.getMonThresholdFlag().get(11) != -1)
					{
						msg = "RPT最低接收率达到告警阀值：" + rptRecvratio + "/" + monSspacinfo.getRptrecvratio() + "( RPT最低接收率/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 11, 1, AlarmSource, upateTime);
						// 设置告警短信内容
						if(monDspacinfo.getMonThresholdFlag().get(11) == 0 && effPhone != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(3, null, "11", 1, true, sSpAccountId))
							{
								// 设置告警状态为1:已发送告警
								monContent.append(msg);
							}
						}
					}
					//设置告警状态标识为告警
					isMonitorFlag = true;
					EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "， RPT最低接收率达到告警阀值，当前转发量：" 
							+ monDspacinfo.getRptHaveSnd() + "，当前接收总量：" + monDspacinfo.getRpttotalrecv() + "，告警阀值：" + monSspacinfo.getRptrecvratio()
							+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
				else
				{
					// 设置告警状态0:正常
					monErrorInfo.setAlarmSmsFlag(3, null, "11", 0, false, sSpAccountId);
				}
			}
			if(evtTypeFlag == 0)
			{
				// 告警级别
				int evtType = 0;
				if(isMonitorFlag)
				{
					evtType = 1;
				}
				// 设置告警级别
				monErrorInfo.setMonEvtType(3, evtType, null, sSpAccountId);
			}
			// 发送告警短信
			if(monContent.length() > 0)
			{
				// 告警内容
				String monSendContent = "SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，" + monContent;
				monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
				//new Thread(new sendAlarmSms(null, monSendContent, effPhone)).start();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号数据参数分析异常，SP账号：" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}
	
	/**
	 * SP账号参数分析
	 */
	public void spAccountParamsAnalysis(LfMonSspacinfo monSspacinfo, MonDspAccountParams monDspacinfo, String sSpAccountId, LfMonErr lfMonErr, String effPhone, Integer evtTypeFlag , String effEmail){
		// 告警短信内容
				StringBuffer monContent = new StringBuffer("");
				// 告警邮件内容
				StringBuffer emailMsg = new StringBuffer();
				// 事件内容
				String msg = "";
				//告警状态标识 false 无告警; true 告警
				boolean isMonitorFlag = false;
				// 告警来源 3：SP账号
				int AlarmSource = 3;
				// 更新缓存数据时间
				Timestamp upateTime = monDspacinfo.getUpdatetime();
				try
				{
					// MT滞留量(待发量)
					if(monSspacinfo.getMtremained() != 0 && monDspacinfo.getMtremained() >= monSspacinfo.getMtremained())
					{
						// 处理状态不是处理中
						if(monDspacinfo.getMonThresholdFlag().get(4) != -1)
						{
							msg = "MT待发量达到告警阀值：" + monDspacinfo.getMtremained() + "/" + monSspacinfo.getMtremained() + "(MT待发量/告警阀值)。";
							// 向告警表和告警历史表写入告警数据
							monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
							// 设置告警短信内容
							if(monDspacinfo.getMonThresholdFlag().get(4) == 0 && effPhone != null)
							{
								if(monErrorInfo.setAlarmSmsFlag(3, null, "4", 1, true, sSpAccountId, monDspacinfo.getId()))
								{
									// 设置告警状态为1:已发送告警
									monContent.append(msg);
								}
							}
							
							// 设置告警邮件内容
							if(monDspacinfo.getMonThresholdFlag().get(17) == 0 && effEmail != null)
							{
								if(monErrorInfo.setAlarmSmsFlag(7, null, "4", 1, true, sSpAccountId, monDspacinfo.getId()))
								{
									// 设置告警状态为1:已发送告警
									emailMsg.append(msg);
								}
							}
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，MT待发量达到告警阀值，当前待发量：" 
								+ monDspacinfo.getMtremained() + "，告警阀值：" + monSspacinfo.getMtremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
					else
					{
						// 设置告警状态0:正常
						monErrorInfo.setAlarmSmsFlag(3, null, "4", 0, false, sSpAccountId, monDspacinfo.getId());
						// 设置邮件告警状态0:正常
						monErrorInfo.setAlarmSmsFlag(7, null, "4", 0, false, sSpAccountId, monDspacinfo.getId());
					}
					// MO滞留量
					if(monSspacinfo.getMoremained() != 0 && monDspacinfo.getMoremained() >= monSspacinfo.getMoremained())
					{
						// 处理状态不是处理中
						if(monDspacinfo.getMonThresholdFlag().get(7) != -1)
						{
							msg = "MO滞留量达到告警阀值：" + monDspacinfo.getMoremained() + "/" + monSspacinfo.getMoremained() + "(MO滞留量/告警阀值)。";
							// 向告警表和告警历史表写入告警数据
							monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 7, 1, AlarmSource, upateTime);
							// 发送告警短信
							if(monDspacinfo.getMonThresholdFlag().get(7) == 0 && effPhone != null)
							{
								if(monErrorInfo.setAlarmSmsFlag(3, null, "7", 1, true, sSpAccountId, monDspacinfo.getId()))
								{
									// 设置告警短信内容
									monContent.append(msg);
								}
							}
							// 发送告警邮件
							if(monDspacinfo.getMonThresholdFlag().get(20) == 0 && effEmail != null)
							{
								if(monErrorInfo.setAlarmSmsFlag(7, null, "7", 1, true, sSpAccountId, monDspacinfo.getId()))
								{
									// 设置告警邮件内容
									emailMsg.append(msg);
								}
							}
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，MO滞留量达到告警阀值，当前滞留量：" 
								+ monDspacinfo.getMoremained() + "，告警阀值：" + monSspacinfo.getMoremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());

					}
					else
					{
						// 设置告警状态0:正常
						monErrorInfo.setAlarmSmsFlag(3, null, "7", 0, false, sSpAccountId, monDspacinfo.getId());
						// 设置邮件告警状态0:正常
						monErrorInfo.setAlarmSmsFlag(7, null, "7", 0, false, sSpAccountId, monDspacinfo.getId());
					}
					// MO最低接收率( MO转发量/MO接收总量)
					if(monDspacinfo.getMototalrecv() != 0)
					{
						int moRecvratio = (int) (((double) monDspacinfo.getMohavesnd() / (double) monDspacinfo.getMototalrecv()) * 100);
						if(monSspacinfo.getMorecvratio() != 0 && moRecvratio <= monSspacinfo.getMorecvratio())
						{
							// 处理状态不是处理中
							if(monDspacinfo.getMonThresholdFlag().get(8) != -1)
							{
								msg = "MO最低接收率达到告警阀值：" + moRecvratio + "/" + monSspacinfo.getMorecvratio() + "(MO最低接收率/告警阀值)。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 8, 1, AlarmSource, upateTime);
								// 设置告警短信内容
								if(monDspacinfo.getMonThresholdFlag().get(8) == 0 && effPhone != null)
								{
									if(monErrorInfo.setAlarmSmsFlag(3, null, "8", 1, true, sSpAccountId, monDspacinfo.getId()))
									{
										// 设置告警状态为1:已发送告警
										monContent.append(msg);
									}
								}
								// 设置告警邮件内容
								if(monDspacinfo.getMonThresholdFlag().get(21) == 0 && effEmail != null)
								{
									if(monErrorInfo.setAlarmSmsFlag(7, null, "8", 1, true, sSpAccountId, monDspacinfo.getId()))
									{
										// 设置邮件告警状态为1:已发送告警
										emailMsg.append(msg);
									}
								}
							}
							//设置告警状态标识为告警
							isMonitorFlag = true;
							EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，MO最低接收率达到告警阀值，当前转发量：" 
									+ monDspacinfo.getMohavesnd() + "，当前接收总量：" + monDspacinfo.getMototalrecv() + "，告警阀值：" + monSspacinfo.getMorecvratio()
									+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
						}
						else
						{
							// 设置告警状态0:正常
							monErrorInfo.setAlarmSmsFlag(3, null, "8", 0, false, sSpAccountId, monDspacinfo.getId());
							// 设置邮件告警状态0:正常
							monErrorInfo.setAlarmSmsFlag(7, null, "8", 0, false, sSpAccountId, monDspacinfo.getId());
						}
					}
					// RPT滞留量
					if(monSspacinfo.getRptremained() != 0 && monDspacinfo.getRptremained() >= monSspacinfo.getRptremained())
					{
						// 处理状态不是处理中
						if(monDspacinfo.getMonThresholdFlag().get(9) != -1)
						{
							msg = "RPT滞留量达到告警阀值：" + monDspacinfo.getRptremained() + "/" + monSspacinfo.getRptremained() + "(RPT滞留量/告警阀值)。";
							// 向告警表和告警历史表写入告警数据
							monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 9, 1, AlarmSource, upateTime);
							// 设置告警短信内容
							if(monDspacinfo.getMonThresholdFlag().get(9) == 0 && effPhone != null)
							{
								if(monErrorInfo.setAlarmSmsFlag(3, null, "9", 1, true, sSpAccountId, monDspacinfo.getId()))
								{
									// 设置告警状态为1:已发送告警
									monContent.append(msg);
								}
							}
							
							// 设置告警邮件内容
							if(monDspacinfo.getMonThresholdFlag().get(22) == 0 && effEmail != null)
							{
								if(monErrorInfo.setAlarmSmsFlag(7, null, "9", 1, true, sSpAccountId, monDspacinfo.getId()))
								{
									// 设置邮件告警状态为1:已发送告警
									emailMsg.append(msg);
								}
							}
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，RPT滞留量达到告警阀值，当前滞留量：" 
								+ monDspacinfo.getRptremained() + "，告警阀值：" + monSspacinfo.getRptremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
					else
					{
						// 设置告警状态0:正常
						monErrorInfo.setAlarmSmsFlag(3, null, "9", 0, false, sSpAccountId, monDspacinfo.getId());
						// 设置邮件告警状态0:正常
						monErrorInfo.setAlarmSmsFlag(7, null, "9", 0, false, sSpAccountId, monDspacinfo.getId());
					}
					// RPT最低接收率(转发量/接收总量)
					if(monDspacinfo.getRpttotalrecv() != 0)
					{
						int rptRecvratio = (int) (((double) monDspacinfo.getRptHaveSnd() / (double) monDspacinfo.getRpttotalrecv()) * 100);
						if(monSspacinfo.getRptrecvratio() != 0 && rptRecvratio <= monSspacinfo.getRptrecvratio())
						{
							// 处理状态不是处理中
							if(monDspacinfo.getMonThresholdFlag().get(11) != -1)
							{
								msg = "RPT最低接收率达到告警阀值：" + rptRecvratio + "/" + monSspacinfo.getRptrecvratio() + "( RPT最低接收率/告警阀值)。";
								// 向告警表和告警历史表写入告警数据
								monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 11, 1, AlarmSource, upateTime);
								// 设置告警短信内容
								if(monDspacinfo.getMonThresholdFlag().get(11) == 0 && effPhone != null)
								{
									if(monErrorInfo.setAlarmSmsFlag(3, null, "11", 1, true, sSpAccountId, monDspacinfo.getId()))
									{
										// 设置告警状态为1:已发送告警
										monContent.append(msg);
									}
								}
								// 设置告警邮件内容
								if(monDspacinfo.getMonThresholdFlag().get(24) == 0 && effEmail != null)
								{
									if(monErrorInfo.setAlarmSmsFlag(7, null, "11", 1, true, sSpAccountId, monDspacinfo.getId()))
									{
										// 设置邮件告警状态为1:已发送告警
										emailMsg.append(msg);
									}
								}
							}
							//设置告警状态标识为告警
							isMonitorFlag = true;
							EmpExecutionContext.info("SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "， RPT最低接收率达到告警阀值，当前转发量：" 
									+ monDspacinfo.getRptHaveSnd() + "，当前接收总量：" + monDspacinfo.getRpttotalrecv() + "，告警阀值：" + monSspacinfo.getRptrecvratio()
									+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
						}
						else
						{
							// 设置告警状态0:正常
							monErrorInfo.setAlarmSmsFlag(3, null, "11", 0, false, sSpAccountId, monDspacinfo.getId());
							// 设置邮件告警状态0:正常
							monErrorInfo.setAlarmSmsFlag(7, null, "11", 0, false, sSpAccountId, monDspacinfo.getId());
						}
					}
					if(evtTypeFlag == 0)
					{
						// 告警级别
						int evtType = 0;
						if(isMonitorFlag)
						{
							evtType = 1;
						}
						// 设置告警级别
						monErrorInfo.setMonEvtType(3, evtType, null, sSpAccountId);
					}
					// 发送告警短信
					if(monContent.length() > 0)
					{
						// 告警内容
						String monSendContent = "SP账号:" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname() + "，" + monContent;
						monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
						//new Thread(new sendAlarmSms(null, monSendContent, effPhone)).start();
					}
					//发送告警邮件
					if (emailMsg.length() > 0) {
						// 告警内容
						String alarmContent = emailMsg.toString();
						StringBuffer emailContent = new StringBuffer();
						emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
						.append("监控类型：sp监控").append("<br/>")
						.append("监控名称：" + monSspacinfo.getAccountname())
						.append("<br/>").append("告警级别："+((evtTypeFlag==0 && isMonitorFlag)?"警告":"严重")).append("<br/>")
						.append("事件描述：" + alarmContent).append("<br/>")
						.append("告警时间：" + sdf.format(new Date()))
						.append("<br/>");
						// 设置邮件内容参数
						MonEmailParams mep = new MonEmailParams();
						mep.setContent(emailContent.toString());
						mep.setCorpCode(corpcode);
						mep.setEmail(effEmail);
						// 发送告警邮件
						new Thread(new sendAlarmEmail(mep)).start();
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "SP账号数据参数分析异常，SP账号：" + sSpAccountId + "，SP账号名称：" + monSspacinfo.getAccountname()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
	}
	
	/**
	 * 通道账号参数分析
	 * 
	 * @description
	 * @param monSgtacinfo
	 * @param monDgtacinfo
	 * @param sGateAccountId
	 * @param lfMonErr
	 * @param monPhone
	 * @param isVaild
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 下午02:55:10
	 */
	public void gateAccountParamsAnalysis(LfMonSgtacinfo monSgtacinfo, MonDgateacParams monDgtacinfo, String sGateAccountId, LfMonErr lfMonErr, String effPhone)
	{
		// 告警短信内容
		StringBuffer monContent = new StringBuffer("");
		// 事件内容
		String msg = "";
		//告警状态标识 false 无告警; true 告警
		boolean isMonitorFlag = false;
		// 告警来源 4：通道账号
		int AlarmSource = 4;
		// 更新缓存数据时间
		Timestamp upateTime = monDgtacinfo.getModifytime();
		try
		{
			// 后付费账号不处理欠费告警
			if(monDgtacinfo.getFeeflag() != 2)
			{
				// 账号费用
				if(monSgtacinfo.getUserfee() != 0 && monDgtacinfo.getUserfee() <= monSgtacinfo.getUserfee())
				{
					// 告警状态为2说明开启欠费告警并已欠费，不进行余额告警处理
					if(monDgtacinfo.getEvtType() != 2)
					{
						// 处理状态不是处理中
						if(monDgtacinfo.getMonThresholdFlag().get(3) != -1)
						{
							msg = "账号费用达到告警阀值：" + monDgtacinfo.getUserfee() + "/" + monSgtacinfo.getUserfee() + "(账号费用/告警阀值)。";
							// 向告警表和告警历史表写入告警数据
							monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 1, AlarmSource, upateTime);
							// 设置告警短信内容
							if(monDgtacinfo.getMonThresholdFlag().get(3) == 0 && effPhone != null)
							{
								// 设置告警状态为1:已发送告警
								if(monErrorInfo.setAlarmSmsFlag(4, null, "3", 1, true, sGateAccountId))
								{
									monContent.append(msg);
								}
							}
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，账号费用达到告警阀值，当前余额：" 
								+ monDgtacinfo.getUserfee() + "，告警阀值：" + monSgtacinfo.getUserfee()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
				else
				{
					// 设置告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(4, null, "3", 0, false, sGateAccountId);
				}
			}
			// MT滞留量(待发量)
			if(monSgtacinfo.getMtremained() != 0 && monDgtacinfo.getMtremained() >= monSgtacinfo.getMtremained())
			{
				// 处理状态不是处理中
				if(monDgtacinfo.getMonThresholdFlag().get(4) != -1)
				{
					msg = "MT待发量达到告警阀值：" + monDgtacinfo.getMtremained() + "/" + monSgtacinfo.getMtremained() + "(MT待发量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDgtacinfo.getMonThresholdFlag().get(4) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(4, null, "4", 1, true, sGateAccountId))
						{
							// 设置告警状态为1:已发送告警
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，MT待发量达到告警阀值，当前待发量：" 
						+ monDgtacinfo.getMtremained() + "，告警阀值：" + monSgtacinfo.getMtremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());

			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(4, null, "4", 0, false, sGateAccountId);
			}
			// MO滞留量
			if(monSgtacinfo.getMoremained() != 0 && monDgtacinfo.getMoremained() >= monSgtacinfo.getMoremained())
			{
				// 处理状态不是处理中
				if(monDgtacinfo.getMonThresholdFlag().get(6) != -1)
				{
					msg = "MO滞留量达到告警阀值：" + monDgtacinfo.getMoremained() + "/" + monSgtacinfo.getMoremained() + "(MO滞留量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 6, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDgtacinfo.getMonThresholdFlag().get(6) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(4, null, "6", 1, true, sGateAccountId))
						{
							// 设置告警状态为1:已发送告警
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，MO滞留量达到告警阀值，当前滞留量：" 
						+ monDgtacinfo.getMoremained() + "，告警阀值：" + monSgtacinfo.getMoremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(4, null, "6", 0, false, sGateAccountId);
			}
			// MO最低转发率（转发量/接收总量）
			if(monDgtacinfo.getMototalrecv() != 0)
			{
				int moSndRatio = (int) (((double) monDgtacinfo.getMohavesnd() / (double) monDgtacinfo.getMototalrecv()) * 100);
				if(monSgtacinfo.getMosndratio() != 0 && moSndRatio <= monSgtacinfo.getMosndratio())
				{
					// 处理状态不是处理中
					if(monDgtacinfo.getMonThresholdFlag().get(8) != -1)
					{
						msg = "MO最低转发率达到告警阀值：" + moSndRatio + "/" + monSgtacinfo.getMosndratio() + "(MO最低转发率/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 8, 1, AlarmSource, upateTime);
						// 设置告警短信内容
						if(monDgtacinfo.getMonThresholdFlag().get(8) == 0 && effPhone != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(4, null, "8", 1, true, sGateAccountId))
							{
								// 设置告警状态为1:已发送告警
								monContent.append(msg);
							}
							
						}
					}
					//设置告警状态标识为告警
					isMonitorFlag = true;
					EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，MO最低转发率达到告警阀值，当前转发量：" 
							+ monDgtacinfo.getMohavesnd() + "，当前接收总量：" + monDgtacinfo.getMototalrecv() + "，告警阀值：" + monSgtacinfo.getMosndratio()
							+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
				else
				{
					// 设置告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(4, null, "8", 0, false, sGateAccountId);
				}
			}
			// RPT滞留量
			if(monSgtacinfo.getRptremained() != 0 && monDgtacinfo.getRptremained() >= monSgtacinfo.getRptremained())
			{
				// 处理状态不是处理中
				if(monDgtacinfo.getMonThresholdFlag().get(9) != -1)
				{
					msg = "RPT滞留量达到告警阀值：" + monDgtacinfo.getRptremained() + "/" + monSgtacinfo.getRptremained() + "(RPT滞留量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 9, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDgtacinfo.getMonThresholdFlag().get(9) == 0 && effPhone != null)
					{
						// 设置告警状态为1:已发送告警
						if(monErrorInfo.setAlarmSmsFlag(4, null, "9", 1, true, sGateAccountId))
						{
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，RPT滞留量达到告警阀值，当前滞留量：" 
						+ monDgtacinfo.getRptremained() + "，告警阀值：" + monSgtacinfo.getRptremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(4, null, "9", 0, false, sGateAccountId);
			}
			// RPT最低转发率（转发量/接收总量）
			if(monDgtacinfo.getRpttotalrecv() != 0)
			{
				int rptSndRatio = (int) (((double) monDgtacinfo.getRpthavesnd() / (double) monDgtacinfo.getRpttotalrecv()) * 100);
				if(monSgtacinfo.getRptsndratio() != 0 && rptSndRatio <= monSgtacinfo.getRptsndratio())
				{
					// 处理状态不是处理中
					if(monDgtacinfo.getMonThresholdFlag().get(11) != -1)
					{
						msg = "RPT最低转发率达到告警阀值：" + rptSndRatio + "/" + monSgtacinfo.getRptsndratio() + "(RPT最低转发率/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 11, 1, AlarmSource, upateTime);
						// 设置告警短信内容
						if(monDgtacinfo.getMonThresholdFlag().get(11) == 0 && effPhone != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(4, null, "11", 1, true, sGateAccountId))
							{
								// 设置告警状态为1:已发送告警
								monContent.append(msg);
							}
						}
					}
					//设置告警状态标识为告警
					isMonitorFlag = true;
					EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，RPT最低转发率达到告警阀值，当前转发量：" 
							+ monDgtacinfo.getRpthavesnd() + "，当前接收总量：" + monDgtacinfo.getRpttotalrecv()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
				else
				{
					// 设置告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(4, null, "11", 0, false, sGateAccountId);
				}
			}
			// 告警标识不为2:严重，重新设置告警级别
			if(MonitorStaticValue.getGateAccountMap().get(sGateAccountId).getEvtType() != 2)
			{
				// 告警级别
				int evtType = 0;
				if(isMonitorFlag)
				{
					evtType = 1;
				}
				// 设置告警级别
				monErrorInfo.setMonEvtType(4, evtType, null, sGateAccountId);
			}
			// 发送告警短信
			if(monContent.length() > 0)
			{
				// 告警内容
				String monSendContent = "通道账号:" + sGateAccountId + "，" + monContent;
				monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
				//new Thread(new sendAlarmSms(null, monSendContent, effPhone)).start();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通道账号数据参数分析异常，通道账号：" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}
	
	/**
	 * 通道账号参数分析
	 * 
	 * @description
	 * @param monSgtacinfo
	 * @param monDgtacinfo
	 * @param sGateAccountId
	 * @param lfMonErr
	 * @param monPhone
	 * @param isVaild
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 下午02:55:10
	 */
	public void gateAccountParamsAnalysis(LfMonSgtacinfo monSgtacinfo, MonDgateacParams monDgtacinfo, String sGateAccountId, LfMonErr lfMonErr, String effPhone, Integer evtTypeFlag)
	{
		// 告警短信内容
		StringBuffer monContent = new StringBuffer("");
		// 事件内容
		String msg = "";
		//告警状态标识 false 无告警; true 告警
		boolean isMonitorFlag = false;
		// 告警来源 4：通道账号
		int AlarmSource = 4;
		// 更新缓存数据时间
		Timestamp upateTime = monDgtacinfo.getModifytime();
		try
		{
			// 后付费账号不处理欠费告警
			if(monDgtacinfo.getFeeflag() != 2)
			{
				// 账号费用
				if(monSgtacinfo.getUserfee() != 0 && monDgtacinfo.getUserfee() <= monSgtacinfo.getUserfee())
				{
					// 告警状态为2说明开启欠费告警并已欠费，不进行余额告警处理
					if(monDgtacinfo.getEvtType() != 2)
					{
						// 处理状态不是处理中
						if(monDgtacinfo.getMonThresholdFlag().get(3) != -1)
						{
							msg = "账号费用达到告警阀值：" + monDgtacinfo.getUserfee() + "/" + monSgtacinfo.getUserfee() + "(账号费用/告警阀值)。";
							// 向告警表和告警历史表写入告警数据
							monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 1, AlarmSource, upateTime);
							// 设置告警短信内容
							if(monDgtacinfo.getMonThresholdFlag().get(3) == 0 && effPhone != null)
							{
								// 设置告警状态为1:已发送告警
								if(monErrorInfo.setAlarmSmsFlag(4, null, "3", 1, true, sGateAccountId))
								{
									monContent.append(msg);
								}
							}
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，账号费用达到告警阀值，当前余额：" 
								+ monDgtacinfo.getUserfee() + "，告警阀值：" + monSgtacinfo.getUserfee()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
				else
				{
					// 设置告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(4, null, "3", 0, false, sGateAccountId);
				}
			}
			// MT滞留量(待发量)
			if(monSgtacinfo.getMtremained() != 0 && monDgtacinfo.getMtremained() >= monSgtacinfo.getMtremained())
			{
				// 处理状态不是处理中
				if(monDgtacinfo.getMonThresholdFlag().get(4) != -1)
				{
					msg = "MT待发量达到告警阀值：" + monDgtacinfo.getMtremained() + "/" + monSgtacinfo.getMtremained() + "(MT待发量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDgtacinfo.getMonThresholdFlag().get(4) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(4, null, "4", 1, true, sGateAccountId))
						{
							// 设置告警状态为1:已发送告警
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，MT待发量达到告警阀值，当前待发量：" 
						+ monDgtacinfo.getMtremained() + "，告警阀值：" + monSgtacinfo.getMtremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(4, null, "4", 0, false, sGateAccountId);
			}
			// MO滞留量
			if(monSgtacinfo.getMoremained() != 0 && monDgtacinfo.getMoremained() >= monSgtacinfo.getMoremained())
			{
				// 处理状态不是处理中
				if(monDgtacinfo.getMonThresholdFlag().get(6) != -1)
				{
					msg = "MO滞留量达到告警阀值：" + monDgtacinfo.getMoremained() + "/" + monSgtacinfo.getMoremained() + "(MO滞留量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 6, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDgtacinfo.getMonThresholdFlag().get(6) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(4, null, "6", 1, true, sGateAccountId))
						{
							// 设置告警状态为1:已发送告警
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，MO滞留量达到告警阀值，当前滞留量：" 
						+ monDgtacinfo.getMoremained() + "，告警阀值：" + monSgtacinfo.getMoremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(4, null, "6", 0, false, sGateAccountId);
			}
			// MO最低转发率（转发量/接收总量）
			if(monDgtacinfo.getMototalrecv() != 0)
			{
				int moSndRatio = (int) (((double) monDgtacinfo.getMohavesnd() / (double) monDgtacinfo.getMototalrecv()) * 100);
				if(monSgtacinfo.getMosndratio() != 0 && moSndRatio <= monSgtacinfo.getMosndratio())
				{
					// 处理状态不是处理中
					if(monDgtacinfo.getMonThresholdFlag().get(8) != -1)
					{
						msg = "MO最低转发率达到告警阀值：" + moSndRatio + "/" + monSgtacinfo.getMosndratio() + "(MO最低转发率/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 8, 1, AlarmSource, upateTime);
						// 设置告警短信内容
						if(monDgtacinfo.getMonThresholdFlag().get(8) == 0 && effPhone != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(4, null, "8", 1, true, sGateAccountId))
							{
								// 设置告警状态为1:已发送告警
								monContent.append(msg);
							}
							
						}
					}
					//设置告警状态标识为告警
					isMonitorFlag = true;
					EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，MO最低转发率达到告警阀值，当前转发量：" 
							+ monDgtacinfo.getMohavesnd() + "，当前接收总量：" + monDgtacinfo.getMototalrecv() + "，告警阀值：" + monSgtacinfo.getMosndratio()
							+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
				else
				{
					// 设置告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(4, null, "8", 0, false, sGateAccountId);
				}
			}
			// RPT滞留量
			if(monSgtacinfo.getRptremained() != 0 && monDgtacinfo.getRptremained() >= monSgtacinfo.getRptremained())
			{
				// 处理状态不是处理中
				if(monDgtacinfo.getMonThresholdFlag().get(9) != -1)
				{
					msg = "RPT滞留量达到告警阀值：" + monDgtacinfo.getRptremained() + "/" + monSgtacinfo.getRptremained() + "(RPT滞留量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 9, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDgtacinfo.getMonThresholdFlag().get(9) == 0 && effPhone != null)
					{
						// 设置告警状态为1:已发送告警
						if(monErrorInfo.setAlarmSmsFlag(4, null, "9", 1, true, sGateAccountId))
						{
							monContent.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，RPT滞留量达到告警阀值，当前滞留量：" 
						+ monDgtacinfo.getRptremained() + "，告警阀值：" + monSgtacinfo.getRptremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(4, null, "9", 0, false, sGateAccountId);
			}
			// RPT最低转发率（转发量/接收总量）
			if(monDgtacinfo.getRpttotalrecv() != 0)
			{
				int rptSndRatio = (int) (((double) monDgtacinfo.getRpthavesnd() / (double) monDgtacinfo.getRpttotalrecv()) * 100);
				if(monSgtacinfo.getRptsndratio() != 0 && rptSndRatio <= monSgtacinfo.getRptsndratio())
				{
					// 处理状态不是处理中
					if(monDgtacinfo.getMonThresholdFlag().get(11) != -1)
					{
						msg = "RPT最低转发率达到告警阀值：" + rptSndRatio + "/" + monSgtacinfo.getRptsndratio() + "(RPT最低转发率/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 11, 1, AlarmSource, upateTime);
						// 设置告警短信内容
						if(monDgtacinfo.getMonThresholdFlag().get(11) == 0 && effPhone != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(4, null, "11", 1, true, sGateAccountId))
							{
								// 设置告警状态为1:已发送告警
								monContent.append(msg);
							}
						}
					}
					//设置告警状态标识为告警
					isMonitorFlag = true;
					EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，RPT最低转发率达到告警阀值，当前转发量：" 
							+ monDgtacinfo.getRpthavesnd() + "，当前接收总量：" + monDgtacinfo.getRpttotalrecv()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
				else
				{
					// 设置告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(4, null, "11", 0, false, sGateAccountId);
				}
			}
			// 告警标识不为2:严重，重新设置告警级别
			if(evtTypeFlag != 2)
			{
				// 告警级别
				int evtType = 0;
				if(isMonitorFlag)
				{
					evtType = 1;
				}
				// 设置告警级别
				monErrorInfo.setMonEvtType(4, evtType, null, sGateAccountId);
			}
			// 发送告警短信
			if(monContent.length() > 0)
			{
				// 告警内容
				String monSendContent = "通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，" + monContent;
				monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
				//new Thread(new sendAlarmSms(null, monSendContent, effPhone)).start();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通道账号数据参数分析异常，通道账号：" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

	/**
	 * 通道账号参数分析
	 */
	public void gateAccountParamsAnalysis(LfMonSgtacinfo monSgtacinfo, MonDgateacParams monDgtacinfo, String sGateAccountId, LfMonErr lfMonErr, String effPhone, Integer evtTypeFlag ,String effEmail){

		// 告警短信内容
		StringBuffer monContent = new StringBuffer("");
		// 告警邮件内容
		StringBuffer emailMsg = new StringBuffer();
		// 事件内容
		String msg = "";
		//告警状态标识 false 无告警; true 告警
		boolean isMonitorFlag = false;
		// 告警来源 4：通道账号
		int AlarmSource = 4;
		// 更新缓存数据时间
		Timestamp upateTime = monDgtacinfo.getModifytime();
		try
		{
			// 后付费账号不处理欠费告警
			if(monDgtacinfo.getFeeflag() != 2)
			{
				// 账号费用
				if(monSgtacinfo.getUserfee() != 0 && monDgtacinfo.getUserfee() <= monSgtacinfo.getUserfee())
				{
					// 告警状态为2说明开启欠费告警并已欠费，不进行余额告警处理
					if(monDgtacinfo.getEvtType() != 2)
					{
						// 处理状态不是处理中
						if(monDgtacinfo.getMonThresholdFlag().get(3) != -1)
						{
							msg = "账号费用达到告警阀值：" + monDgtacinfo.getUserfee() + "/" + monSgtacinfo.getUserfee() + "(账号费用/告警阀值)。";
							// 向告警表和告警历史表写入告警数据
							monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 3, 1, AlarmSource, upateTime);
							// 设置告警短信内容
							if(monDgtacinfo.getMonThresholdFlag().get(3) == 0 && effPhone != null)
							{
								// 设置告警状态为1:已发送告警
								if(monErrorInfo.setAlarmSmsFlag(4, null, "3", 1, true, sGateAccountId, monDgtacinfo.getId()))
								{
									monContent.append(msg);
								}
							}
							
							// 设置告警邮件内容
							if(monDgtacinfo.getMonThresholdFlag().get(16) == 0 && effEmail != null)
							{
								if(monErrorInfo.setAlarmSmsFlag(8, null, "3", 1, true, sGateAccountId, monDgtacinfo.getId()))
								{
									// 设置告警邮件状态为1:已发送告警
									emailMsg.append(msg);
								}
							}
						}
						//设置告警状态标识为告警
						isMonitorFlag = true;
						EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，账号费用达到告警阀值，当前余额：" 
								+ monDgtacinfo.getUserfee() + "，告警阀值：" + monSgtacinfo.getUserfee()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
					}
				}
				else
				{
					// 设置告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(4, null, "3", 0, false, sGateAccountId, monDgtacinfo.getId());
					// 设置邮件告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(8, null, "3", 0, false, sGateAccountId, monDgtacinfo.getId());
				}
			}
			// MT滞留量(待发量)
			if(monSgtacinfo.getMtremained() != 0 && monDgtacinfo.getMtremained() >= monSgtacinfo.getMtremained())
			{
				// 处理状态不是处理中
				if(monDgtacinfo.getMonThresholdFlag().get(4) != -1)
				{
					msg = "MT待发量达到告警阀值：" + monDgtacinfo.getMtremained() + "/" + monSgtacinfo.getMtremained() + "(MT待发量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 4, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDgtacinfo.getMonThresholdFlag().get(4) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(4, null, "4", 1, true, sGateAccountId, monDgtacinfo.getId()))
						{
							// 设置告警状态为1:已发送告警
							monContent.append(msg);
						}
					}
					

					// 设置告警邮件内容
					if (monDgtacinfo.getMonThresholdFlag().get(17) == 0 && effEmail != null)
					{
						if (monErrorInfo.setAlarmSmsFlag(8, null, "4", 1, true,sGateAccountId, monDgtacinfo.getId()))
						{
							// 设置告警邮件状态为1:已发送告警
							emailMsg.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，MT待发量达到告警阀值，当前待发量：" 
						+ monDgtacinfo.getMtremained() + "，告警阀值：" + monSgtacinfo.getMtremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(4, null, "4", 0, false, sGateAccountId, monDgtacinfo.getId());
				// 设置邮件告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(8, null, "4", 0, false, sGateAccountId, monDgtacinfo.getId());
			}
			// MO滞留量
			if(monSgtacinfo.getMoremained() != 0 && monDgtacinfo.getMoremained() >= monSgtacinfo.getMoremained())
			{
				// 处理状态不是处理中
				if(monDgtacinfo.getMonThresholdFlag().get(6) != -1)
				{
					msg = "MO滞留量达到告警阀值：" + monDgtacinfo.getMoremained() + "/" + monSgtacinfo.getMoremained() + "(MO滞留量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 6, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDgtacinfo.getMonThresholdFlag().get(6) == 0 && effPhone != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(4, null, "6", 1, true, sGateAccountId, monDgtacinfo.getId()))
						{
							// 设置告警状态为1:已发送告警
							monContent.append(msg);
						}
					}
					
					// 设置告警邮件内容
					if(monDgtacinfo.getMonThresholdFlag().get(19) == 0 && effEmail != null)
					{
						if(monErrorInfo.setAlarmSmsFlag(8, null, "6", 1, true, sGateAccountId, monDgtacinfo.getId()))
						{
							// 设置告警邮件状态为1:已发送告警
							emailMsg.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，MO滞留量达到告警阀值，当前滞留量：" 
						+ monDgtacinfo.getMoremained() + "，告警阀值：" + monSgtacinfo.getMoremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(4, null, "6", 0, false, sGateAccountId, monDgtacinfo.getId());
				// 设置邮件告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(8, null, "6", 0, false, sGateAccountId, monDgtacinfo.getId());
			}
			// MO最低转发率（转发量/接收总量）
			if(monDgtacinfo.getMototalrecv() != 0)
			{
				int moSndRatio = (int) (((double) monDgtacinfo.getMohavesnd() / (double) monDgtacinfo.getMototalrecv()) * 100);
				if(monSgtacinfo.getMosndratio() != 0 && moSndRatio <= monSgtacinfo.getMosndratio())
				{
					// 处理状态不是处理中
					if(monDgtacinfo.getMonThresholdFlag().get(8) != -1)
					{
						msg = "MO最低转发率达到告警阀值：" + moSndRatio + "/" + monSgtacinfo.getMosndratio() + "(MO最低转发率/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 8, 1, AlarmSource, upateTime);
						// 设置告警短信内容
						if(monDgtacinfo.getMonThresholdFlag().get(8) == 0 && effPhone != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(4, null, "8", 1, true, sGateAccountId, monDgtacinfo.getId()))
							{
								// 设置告警状态为1:已发送告警
								monContent.append(msg);
							}
							
						}
						// 设置告警邮件内容
						if(monDgtacinfo.getMonThresholdFlag().get(21) == 0 && effEmail != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(8, null, "8", 1, true, sGateAccountId, monDgtacinfo.getId()))
							{
								// 设置告警邮件状态为1:已发送告警
								emailMsg.append(msg);
							}	
						}
					}
					//设置告警状态标识为告警
					isMonitorFlag = true;
					EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，MO最低转发率达到告警阀值，当前转发量：" 
							+ monDgtacinfo.getMohavesnd() + "，当前接收总量：" + monDgtacinfo.getMototalrecv() + "，告警阀值：" + monSgtacinfo.getMosndratio()
							+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
				else
				{
					// 设置告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(4, null, "8", 0, false, sGateAccountId, monDgtacinfo.getId());
					// 设置邮件告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(8, null, "8", 0, false, sGateAccountId, monDgtacinfo.getId());
				}
			}
			// RPT滞留量
			if(monSgtacinfo.getRptremained() != 0 && monDgtacinfo.getRptremained() >= monSgtacinfo.getRptremained())
			{
				// 处理状态不是处理中
				if(monDgtacinfo.getMonThresholdFlag().get(9) != -1)
				{
					msg = "RPT滞留量达到告警阀值：" + monDgtacinfo.getRptremained() + "/" + monSgtacinfo.getRptremained() + "(RPT滞留量/告警阀值)。";
					// 向告警表和告警历史表写入告警数据
					monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 9, 1, AlarmSource, upateTime);
					// 设置告警短信内容
					if(monDgtacinfo.getMonThresholdFlag().get(9) == 0 && effPhone != null)
					{
						// 设置告警状态为1:已发送告警
						if(monErrorInfo.setAlarmSmsFlag(4, null, "9", 1, true, sGateAccountId, monDgtacinfo.getId()))
						{
							monContent.append(msg);
						}
					}
					
					// 设置告警短信内容
					if(monDgtacinfo.getMonThresholdFlag().get(22) == 0 && effEmail != null)
					{
						// 设置告警状态为1:已发送告警
						if(monErrorInfo.setAlarmSmsFlag(8, null, "9", 1, true, sGateAccountId, monDgtacinfo.getId()))
						{
							// 设置告警邮件状态为1:已发送告警
							emailMsg.append(msg);
						}
					}
				}
				//设置告警状态标识为告警
				isMonitorFlag = true;
				EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，RPT滞留量达到告警阀值，当前滞留量：" 
						+ monDgtacinfo.getRptremained() + "，告警阀值：" + monSgtacinfo.getRptremained()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
			}
			else
			{
				// 设置告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(4, null, "9", 0, false, sGateAccountId, monDgtacinfo.getId());
				// 设置邮件告警状态为0:正常
				monErrorInfo.setAlarmSmsFlag(8, null, "9", 0, false, sGateAccountId, monDgtacinfo.getId());
			}
			// RPT最低转发率（转发量/接收总量）
			if(monDgtacinfo.getRpttotalrecv() != 0)
			{
				int rptSndRatio = (int) (((double) monDgtacinfo.getRpthavesnd() / (double) monDgtacinfo.getRpttotalrecv()) * 100);
				if(monSgtacinfo.getRptsndratio() != 0 && rptSndRatio <= monSgtacinfo.getRptsndratio())
				{
					// 处理状态不是处理中
					if(monDgtacinfo.getMonThresholdFlag().get(11) != -1)
					{
						msg = "RPT最低转发率达到告警阀值：" + rptSndRatio + "/" + monSgtacinfo.getRptsndratio() + "(RPT最低转发率/告警阀值)。";
						// 向告警表和告警历史表写入告警数据
						monErrorInfo.setErrorAlarmInfo(lfMonErr, msg, 11, 1, AlarmSource, upateTime);
						// 设置告警短信内容
						if(monDgtacinfo.getMonThresholdFlag().get(11) == 0 && effPhone != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(4, null, "11", 1, true, sGateAccountId, monDgtacinfo.getId()))
							{
								// 设置告警状态为1:已发送告警
								monContent.append(msg);
							}
						}
						
						// 设置告警邮件内容
						if(monDgtacinfo.getMonThresholdFlag().get(24) == 0 && effEmail != null)
						{
							if(monErrorInfo.setAlarmSmsFlag(8, null, "11", 1, true, sGateAccountId, monDgtacinfo.getId()))
							{
								// 设置告警邮件状态为1:已发送告警
								emailMsg.append(msg);
							}
						}
					}
					//设置告警状态标识为告警
					isMonitorFlag = true;
					EmpExecutionContext.info("通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，RPT最低转发率达到告警阀值，当前转发量：" 
							+ monDgtacinfo.getRpthavesnd() + "，当前接收总量：" + monDgtacinfo.getRpttotalrecv()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
				}
				else
				{
					// 设置告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(4, null, "11", 0, false, sGateAccountId, monDgtacinfo.getId());
					// 设置邮件告警状态为0:正常
					monErrorInfo.setAlarmSmsFlag(8, null, "11", 0, false, sGateAccountId, monDgtacinfo.getId());
				}
			}
			// 告警标识不为2:严重，重新设置告警级别
			if(evtTypeFlag != 2)
			{
				// 告警级别
				int evtType = 0;
				if(isMonitorFlag)
				{
					evtType = 1;
				}
				// 设置告警级别
				monErrorInfo.setMonEvtType(4, evtType, null, sGateAccountId);
			}
			// 发送告警短信
			if(monContent.length() > 0)
			{
				// 告警内容
				String monSendContent = "通道账号:" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename() + "，" + monContent;
				monBaseInfo.setmonAlarmDsm(effPhone, monSendContent);
				//new Thread(new sendAlarmSms(null, monSendContent, effPhone)).start();
			}
			
			//发送告警邮件
			if (emailMsg.length() > 0) {
				// 告警内容
				String alarmContent = emailMsg.toString();
				StringBuffer emailContent = new StringBuffer();
				emailContent.append("您有监控告警信息需要处理，请及时确认，谢谢").append("<br/>")
				.append("监控类型：通道账号监控").append("<br/>")
				.append("监控名称："+monSgtacinfo.getGateaccount()).append("<br/>")
				.append("告警级别："+((evtTypeFlag!=2 && isMonitorFlag)?"警告":"严重")).append("<br/>")
				.append("事件描述："+alarmContent).append("<br/>")
				.append("告警时间："+sdf.format(new Date())).append("<br/>");
				//设置邮件内容参数
				MonEmailParams mep = new MonEmailParams();
				mep.setContent(emailContent.toString());
				mep.setCorpCode(corpcode);
				mep.setEmail(effEmail);
				// 发送告警邮件
				new Thread(new sendAlarmEmail(mep)).start();
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通道账号数据参数分析异常，通道账号：" + sGateAccountId + "，通道名称：" + monSgtacinfo.getGatename()+ "，线程启动时间："+MonitorStaticValue.getThreadStartDate());
		}
	}

}
