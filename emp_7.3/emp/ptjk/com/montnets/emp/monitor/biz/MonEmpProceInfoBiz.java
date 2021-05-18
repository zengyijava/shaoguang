/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 上午11:53:05
 */
package com.montnets.emp.monitor.biz;

import java.io.File;
import java.sql.Connection;
import java.sql.Timestamp;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

import sun.management.ManagementFactory;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.entity.monitor.LfMonDbopr;
import com.montnets.emp.monitor.biz.i.IMonEmpProceInfoBiz;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.monitor.dao.MonitorDAO;
import com.montnets.emp.util.TxtFileUtil;
import com.sun.management.OperatingSystemMXBean;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 上午11:53:05
 */

public class MonEmpProceInfoBiz implements IMonEmpProceInfoBiz
{
	BaseBiz	baseBiz	= new BaseBiz();
	
	Sigar sigar	= new Sigar();
	
	MonitorDAO monitorDAO = new MonitorDAO();

	private String jmxURL;
	private JMXServiceURL serviceURL;
	private JMXConnector connector;
	private MBeanServerConnection mbsc;
	
	public MonEmpProceInfoBiz()
	{
		try
		{
			jmxURL = "service:jmx:rmi:///jndi/rmi://localhost:8050/jmxrmi";// tomcat jmx url
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
			//计算CPU占用率
			double ratio = (endCPU-startCPU)/1000000.0/(end-start)/Processors * 100;
			return (long)ratio;
			
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
		//long total = osmxb.getTotalPhysicalMemorySize();
		// 剩余的物理内存
		//long free = osmxb.getFreePhysicalMemorySize();
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
			//websphere
			if("WebSphere".equals(MonitorStaticValue.getServerName()))
			{
				Swap swap = new Sigar().getSwap();
				usage = swap.getUsed();
			}
			//tomcat、weblogic
			else
			{
				OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
				//虚拟内存使用率
				usage = osmxb.getCommittedVirtualMemorySize();
			}
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
/*			// EMP程序所在的磁盘
			String fdisk = System.getProperty("user.dir").split(":")[0] + ":\\";
			// 获取磁盘分区列表
			FileSystem fslist[] = sigar.getFileSystemList();
			FileSystem fs;
			FileSystemUsage usage = null;
			for (int i = 0; i < fslist.length; i++)
			{
				fs = fslist[i];
				if(fs.getDevName().equals(fdisk))
				{
					// 分区文件
					usage = sigar.getFileSystemUsage(fs.getDirName());
					// 2:磁盘信息
					if(fs.getType() == 2)
					{
						// 磁盘分区剩余
						free = usage.getFree();
						break;
					}
				}
			}*/

			//获取文件所在磁盘剩余
			TxtFileUtil fileUtil=new TxtFileUtil();
			File file = new File(fileUtil.getWebRoot()+"WEB-INF/classes/SystemGlobals.properties");
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
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-24 上午09:50:18
	 */
	public Integer getDbConnectState()
	{
		//数据库连接状态，0：开启；1：关闭
		int dbConnectState = 1;
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			
			Connection connection = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			
			if(connection != null)
			{
				connection.close();
				//设置数据库连接状态为0：开启
				dbConnectState = 0;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取EMP程序数据库连接异常！");
		}
		
		return dbConnectState;
	}
	
	/**
	 * 数据库操作状态
	 * @description    
	 * @return  0：查询；1：修改；2：新增；3：删除			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-23 下午07:07:58
	 */
	public Integer[] getDbOprState()
	{
		Integer[] DbOprState = {0,0,0,0};
		//设置查询状态
		DbOprState[0] = getDbDispOprState();
		//设置更新状态
		DbOprState[1] = getDbModiOprState();
		//设置新增状态
		DbOprState[2] = getDbAddOprState();
		//设置删除状态
		DbOprState[3] = getDbDelOprState();
		
		return DbOprState;
	}
	
	/**
	 * 数据库查询操作状态
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-25 下午03:15:24
	 */
	public Integer getDbDispOprState()
	{
		return monitorDAO.getDbDispOprState();
	}
	
	/**
	 * 数据库更新操作状态
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-25 下午03:19:15
	 */
	public Integer getDbModiOprState()
	{
		return monitorDAO.getDbModiOprState();
	}
	
	/**
	 * 数据库新增操作状态
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-25 下午03:19:41
	 */
	public Integer getDbAddOprState()
	{
		try
		{
			Integer DbAddOprState = 1;
			LfMonDbopr monDbopr = new LfMonDbopr(); 
			monDbopr.setCreatetime(new Timestamp(System.currentTimeMillis()));
			monDbopr.setProcenode(Long.parseLong(StaticValue.getServerNumber()));
			boolean result = baseBiz.addObj(MonDbConnection.getInstance().getConnection(), monDbopr);
			if(result)
			{
				DbAddOprState = 0;
			}
			
			return DbAddOprState;
		}
		catch (Exception e)
		{
			return 1;
		}
	}
	
	/**
	 * 数据库删除操作状态
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-25 下午03:19:55
	 */
	public Integer getDbDelOprState()
	{
		return monitorDAO.getDbDelOprState();
	}
}
