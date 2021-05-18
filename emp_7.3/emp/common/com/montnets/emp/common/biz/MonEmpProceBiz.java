/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 上午11:53:05
 */
package com.montnets.emp.common.biz;

import java.io.File;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import sun.management.ManagementFactory;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.util.TxtFileUtil;
import com.sun.management.OperatingSystemMXBean;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 上午11:53:05
 */

public class MonEmpProceBiz
{
	BaseBiz							baseBiz	= new BaseBiz();

	Sigar							sigar	= new Sigar();

	private String					jmxURL;

	private JMXServiceURL			serviceURL;

	private JMXConnector			connector;

	private MBeanServerConnection	mbsc;

	public MonEmpProceBiz()
	{
		try
		{
			jmxURL = "service:jmx:rmi:///jndi/rmi://localhost:8050/jmxrmi";// tomcat
																			// jmx
																			// url
			serviceURL = new JMXServiceURL(jmxURL);
			connector = JMXConnectorFactory.connect(serviceURL, null);
			mbsc = connector.getMBeanServerConnection();
		}
		catch (Exception e)
		{
			EmpExecutionContext.info("服务器未配置JMX，使用sigar方式获取程序监控信息。");
		}

	}

	/**
	 * @description 获取EMP程序CPU使用率
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午12:01:39
	 */
	public long getProceCpuUsage()
	{
		try
		{
			ObjectName OperatingSystemObjName = new ObjectName("java.lang:type=OperatingSystem");
			int Processors = (Integer) mbsc.getAttribute(OperatingSystemObjName, "AvailableProcessors");
			long start = System.currentTimeMillis();
			long startCPU = (Long) mbsc.getAttribute(OperatingSystemObjName, "ProcessCpuTime");
			Thread.sleep(1000);
			long end = System.currentTimeMillis();
			long endCPU = (Long) mbsc.getAttribute(OperatingSystemObjName, "ProcessCpuTime");
			// 计算CPU占用率
			double ratio = (endCPU - startCPU) / 1000000.0 / (end - start) / Processors * 100;
			return (long) ratio;

		}
		catch (Exception e)
		{
			// 所有CPU信息集合
			CpuPerc cpuList[];
			// 占用率
			double Combined = 0;
			try
			{
				// 主机所有CPU信息
				cpuList = sigar.getCpuPercList();
				sigar.getCpu().getTotal();
				for (int i = 0; i < cpuList.length; i++)
				{
					// 累加CPU使用率
					Combined = Combined + cpuList[i].getUser();
				}
				// 返回CPU使用率 单位:百分比
				return (long) (Combined * 100) / cpuList.length;
			}
			catch (SigarException e1)
			{
				EmpExecutionContext.error(e1, "获取EMP程序CPU占用率异常!");
				return 0;
			}
		}

	}

	/**
	 * @description 获取EMP程序物理内存使用率
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午12:01:39
	 */
	public long getProceMemUsage()
	{
		// 总的物理内存
		// long total = osmxb.getTotalPhysicalMemorySize();
		// 剩余的物理内存
		// long free = osmxb.getFreePhysicalMemorySize();
		try
		{
			Runtime runtime = Runtime.getRuntime();
			// 物理内存使用率
			long usage = runtime.totalMemory();
			// 返回物理内存使用率 单位:M
			return usage / 1024 / 1024;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取EMP程序物理内存使用率异常！");
			return 0;
		}
	}

	/**
	 * @description 获取EMP程序虚拟内存使用率
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午12:01:39
	 */
	public long getProceVmemUsage()
	{
		long usage = 0L;
		try
		{

			OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
			// 虚拟内存使用率
			usage = osmxb.getCommittedVirtualMemorySize();
			return usage / 1024 / 1024;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取EMP程序虚拟内存使用率异常！");
			return 0;
		}

	}

	/**
	 * EMP程序所在磁盘剩余量
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午01:34:17
	 */
	public long getProceDiskFree()
	{
		// 磁盘剩余
		long free = 0;
		try
		{
			/*
			 * // EMP程序所在的磁盘
			 * String fdisk = System.getProperty("user.dir").split(":")[0] +
			 * ":\\";
			 * // 获取磁盘分区列表
			 * FileSystem fslist[] = sigar.getFileSystemList();
			 * FileSystem fs;
			 * FileSystemUsage usage = null;
			 * for (int i = 0; i < fslist.length; i++)
			 * {
			 * fs = fslist[i];
			 * if(fs.getDevName().equals(fdisk))
			 * {
			 * // 分区文件
			 * usage = sigar.getFileSystemUsage(fs.getDirName());
			 * // 2:磁盘信息
			 * if(fs.getType() == 2)
			 * {
			 * // 磁盘分区剩余
			 * free = usage.getFree();
			 * break;
			 * }
			 * }
			 * }
			 */

			// 获取文件所在磁盘剩余
			TxtFileUtil fileUtil = new TxtFileUtil();
			File file = new File(fileUtil.getWebRoot() + "WEB-INF/classes/SystemGlobals.properties");
			// 磁盘剩余 单位为M
			free = file.getFreeSpace() / 1024 / 1024;
			return free;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取EMP程序磁盘剩余量异常!");
			return free;
		}
	}

	/**
	 * 获取EMP程序数据库连接状态
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-24 上午09:50:18
	 */
	public Integer getDbConnectState()
	{
		// 数据库连接状态，0：开启；1：关闭
		int dbConnectState = 1;

		try
		{
			// 查询数据库
			List<LfGlobalVariable> lfGlobalVariable = baseBiz.getByCondition(LfGlobalVariable.class, null, null);
			if(lfGlobalVariable != null && lfGlobalVariable.size() > 0)
			{
				// 设置数据库连接状态为0：开启
				dbConnectState = 0;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取EMP程序数据库连接异常！");
		}

		return dbConnectState;
	}
}
