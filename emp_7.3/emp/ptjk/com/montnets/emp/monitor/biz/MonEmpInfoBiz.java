/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 下午05:52:45
 */
package com.montnets.emp.monitor.biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.monitor.LfMonDbstate;
import com.montnets.emp.entity.monitor.LfMonDhost;
import com.montnets.emp.entity.monitor.LfMonDproce;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.monitor.constant.MonitorStaticValue;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 下午05:52:45
 */

public class MonEmpInfoBiz
{

	MonEmpHostInfoBiz	monEmpHostInfo	= new MonEmpHostInfoBiz();

	MonEmpProceInfoBiz	monEmpProceInfo	= new MonEmpProceInfoBiz();

	MonErrorInfo		monErrorInfo	= new MonErrorInfo();
	
	BaseBiz baseBiz = new BaseBiz();
	
	private MonitorBaseInfoBiz MonitorBaseInfoBiz = new MonitorBaseInfoBiz();
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 收集EMP主机信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午08:00:22
	 */
	private void setEmpHostInfo(Long proceId)
	{
		// 从EMP系统获取主机ID
		try
		{
			Long hostId = MonitorStaticValue.getProceBaseMap().get(proceId).getHostid();
			// 从缓存中获取所有主机基础信息
			Map<Long, LfMonShost> hostBaseInfoMap = MonitorStaticValue.getHostBaseMap();
			// 遍历主机基础信息
			for (Long shostId : hostBaseInfoMap.keySet())
			{
				if(shostId.equals(hostId))
				{
					// 主机基础信息
					LfMonShost monShost = hostBaseInfoMap.get(shostId);
					// CPU占用量
					long CpuUsage = monEmpHostInfo.getHostCpuUsage();
					// 物理内存信息，0:总量;1:使用率
					long[] memry = monEmpHostInfo.getHostMemUsage();
					// 虚拟内存信息，0:总量;1:使用率
					long[] vMemry = monEmpHostInfo.getHostVmemUsage();
					// 磁盘空间信息，0:磁盘总量;1:磁盘剩余
					long[] diskSpace = monEmpHostInfo.getHostDiskFree();
					// 进程总数
					long processCnt = monEmpHostInfo.getHostProcessCount();
					
					//更新数据库
					LfMonDhost dhost = new LfMonDhost();
					dhost.setHostid(shostId);
					dhost.setHostName(monShost.getHostname());
					dhost.setAdapter1(monShost.getAdapter1());
					dhost.setMemuse((int) memry[0]);
					dhost.setVmemuse((int) vMemry[0]);
					dhost.setDiskspace((int) diskSpace[0]);
					dhost.setCpuusage((int) CpuUsage);
					dhost.setMemusage((int) memry[1]);
					dhost.setVmemusage((int) vMemry[1]);
					dhost.setDiskfreespace((int) diskSpace[1]);
					dhost.setProcesscnt((int) processCnt);
					dhost.setUpdatetime(new Timestamp(MonitorStaticValue.getCurDbServerTime().getTime()));
					dhost.setServerNum(StaticValue.getServerNumber());
					MonitorBaseInfoBiz.saveOrUpdate(dhost,"hostid");
					break;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "收集EMP主机信息异常！");
		}
	}

	/**
	 * 收集EMP程序信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午08:41:28
	 */
	private void setEmpPrecoInfo(Map<Long, LfMonSproce> proceBaseInfo, Long proceId)
	{
		// 主机基础信息
		try
		{
			LfMonSproce monSproce = proceBaseInfo.get(proceId);
			// CPU占用量
			long CpuUsage = monEmpProceInfo.getProceCpuUsage();
			// 物理内存使用量
			long memry = monEmpProceInfo.getProceMemUsage();
			// 虚拟内存使用量
			long vMemry = monEmpProceInfo.getProceVmemUsage();
			// 磁盘空间使用量
			long diskSpace = monEmpProceInfo.getProceDiskFree();
			// 数据库连接状态
			Integer dbConnectState = monEmpProceInfo.getDbConnectState();
			// 设置缓存
			String hostName = "";
			long hostId = monSproce.getHostid();
			if(hostId + 1 != 0)
			{
				hostName = MonitorStaticValue.getHostBaseMap().get(hostId).getHostname();
				if("".equals(hostName) || hostName == null)
				{
					hostName = "-";
				}
			}
			else
			{
				hostName = "-";
			}
			LfMonDproce dproce = new LfMonDproce();
			dproce.setProceid(proceId);
			dproce.setHostname(hostName);
			dproce.setProcename(monSproce.getProcename());
			dproce.setHostid(monSproce.getHostid());
			dproce.setVersion(monSproce.getVersion());
			dproce.setProcetype(5000);
			dproce.setCpuusage((int) CpuUsage);
			dproce.setMemusage((int) memry);
			dproce.setVmemusage((int) vMemry);
			dproce.setDiskFree((int) diskSpace);
			dproce.setDbconnectstate(dbConnectState);
			dproce.setUpdatetime(new Timestamp(MonitorStaticValue.getCurDbServerTime().getTime()));
			dproce.setStarttime(new Timestamp(MonitorStaticValue.getCurDbServerTime().getTime()));
			dproce.setServerNum(StaticValue.getServerNumber());
			
			MonitorBaseInfoBiz.saveOrUpdate(dproce,"proceid");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "收集EMP程序信息异常！");
		}
	}

	/**
	 * 收集EMP主机、程序信息
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-15 下午01:47:51
	 */
	public void setEmpHostAndPrecoInfo()
	{
		try
		{
			
			// 数据库操作状态
			Integer[] DbOprState = monEmpProceInfo.getDbOprState();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("procetype", String.valueOf(5000));
			conditionMap.put("procenode", StaticValue.getServerNumber());
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("dbconnectstate", String.valueOf(monEmpProceInfo.getDbConnectState()));
			objectMap.put("dispopr", String.valueOf(DbOprState[0]));
			objectMap.put("modiopr", String.valueOf(DbOprState[1]));
			objectMap.put("addopr", String.valueOf(DbOprState[2]));
			objectMap.put("delopr", String.valueOf(DbOprState[3]));
			objectMap.put("serverNum", StaticValue.getServerNumber());
			objectMap.put("updatetime", format.format(MonitorStaticValue.getCurDbServerTime().getTime()));
			objectMap.put("dbservtime", format.format(MonitorStaticValue.getCurDbServerTime().getTime()));
			baseBiz.update(MonDbConnection.getInstance().getConnection(), true, LfMonDbstate.class, objectMap, conditionMap);
			
			// 从缓存中获取所有程序基础信息
			Map<Long, LfMonSproce> proceBaseInfo = MonitorStaticValue.getProceBaseMap();
			// 遍历程序基础信息,获取程序编号
			Long proceId = -1L;
			//是否更新主机信息，false 不更新 ；true 更新
			boolean isOnlyEmpHost = false;
			//主机中的程序个数
			int proceCount = 1;
			for (Long sproceId : proceBaseInfo.keySet())
			{
				if(proceBaseInfo.get(sproceId).getProcetype() == 5000 
						&& StaticValue.getServerNumber().equals(proceBaseInfo.get(sproceId).getServernum()))
				{
					proceId = sproceId;
					//主机编号
					Long hostId = proceBaseInfo.get(proceId).getHostid();
					//EMP程序未指定主机,不获取主机信息
					if(hostId != -1)
					{
						for(Long proce : proceBaseInfo.keySet())
						{
							//除EMP程序还存在其他程序
							if(proceBaseInfo.get(proce).getHostid().equals(hostId) && proceBaseInfo.get(proce).getProcetype()!=5000)
							{
								proceCount += 1;
								//主机信息大于1分钟无更新
								if(MonitorStaticValue.getHostMap().containsKey(hostId))
								{
									Timestamp dbservtime = MonitorStaticValue.getHostMap().get(hostId).getDbservtime();
									if((MonitorStaticValue.getCurDbServerTime().getTime() - dbservtime.getTime()) / 1000 >= 60)
									{
										isOnlyEmpHost = true;
										break;
									}
								}
							}
						}
					}
					else
					{
						isOnlyEmpHost = false;
						proceCount = 0;
					}
					break;
				}
			}
			// 存在EMP程序
			if(proceId + 1 != 0)
			{
				// 主机只存在EMP程序或是1分钟无更新,收集EMP主机信息
				if(isOnlyEmpHost || proceCount == 1)
				{
					setEmpHostInfo(proceId);
				}
				// 收集EMP程序信息
				setEmpPrecoInfo(proceBaseInfo, proceId);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "收集EMP主机、程序线程异常!");
		}
	}
}
