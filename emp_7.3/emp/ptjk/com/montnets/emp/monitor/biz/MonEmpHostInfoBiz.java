/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 上午10:29:22
 */
package com.montnets.emp.monitor.biz;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.monitor.biz.i.IMonEmpHostInfoBiz;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 上午10:29:22
 */

public class MonEmpHostInfoBiz implements IMonEmpHostInfoBiz
{

	Sigar	sigar	= new Sigar();
	
	//光驱、软驱等盘符
	private static int[] diskList = null;

	/**
	 * 获取EMP主机CPU使用率
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 上午10:35:57
	 */
	public long getHostCpuUsage()
	{
		// 所有CPU信息集合
		CpuPerc cpuList[];
		// 使用率
		double Combined = 0;
		try
		{
			// 主机所有CPU信息
			cpuList = sigar.getCpuPercList();
			sigar.getCpu().getTotal();
			for (int i = 0; i < cpuList.length; i++)
			{
				// 累加CPU使用率
				Combined = Combined + cpuList[i].getCombined();
			}
			// 返回CPU使用率 单位:百分比
			return (long) (Combined * 100) / cpuList.length;
		}
		catch (SigarException e)
		{
			EmpExecutionContext.error(e, "获取EMP程序主机CPU使用率异常!");
			return 0;
		}
	}

	/**
	 * 内存使用率
	 * 
	 * @description
	 * @return
	 * @throws SigarException
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 上午10:44:36
	 */
	public long[] getHostMemUsage()
	{
		Mem mem;
		//内存总量
		long memTotal = 0;
		// 内存使用率
		long memUsage = 0;
		//内存信息 0:总量;1:使用率
		long[] memry = {0,0};
		try
		{
			// 内存对象信息
			mem = sigar.getMem();
			// 内存总量
			memTotal = mem.getTotal() / 1024 / 1024;
			memry[0] = memTotal;
			// 内存使用率 单位:M
			memUsage = mem.getUsed() / 1024 / 1024;
			memry[1] = memUsage;
			return memry;
			
		}
		catch (SigarException e)
		{
			EmpExecutionContext.error(e, "获取EMP程序主机内存使用率异常!");
			return memry;
		}
	}

	/**
	 * 虚拟内存使用率
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午01:14:02
	 */
	public long[] getHostVmemUsage()
	{
		Swap swap;
		//虚拟内存总量
		long vmemTotal = 0;
		//虚拟内存使用率
		long vmemUsage;
		//虚拟内存信息
		long[] vmemry = {0,0};
		try
		{
			swap = sigar.getSwap();
			// 虚拟内存总量 单位:M
			vmemTotal = swap.getTotal()/ 1024 / 1024;
			vmemry[0] = vmemTotal;
			// 虚拟内存使用率 单位:M
			vmemUsage = swap.getUsed() / 1024 / 1024;
			vmemry[1] = vmemUsage;
			return vmemry;
		}
		catch (SigarException e)
		{
			EmpExecutionContext.error(e, "获取EMP程序主机虚拟内存使用率异常!");
			return vmemry;
		}
	}

	/**
	 * 主机进程数
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 上午11:34:07
	 */
	public int getHostProcessCount()
	{
		long[] proList;
		try
		{
			// 所有进程对象
			proList = sigar.getProcList();
			// 返回进程总数
			return proList.length + 1;
		}
		catch (SigarException e)
		{
			EmpExecutionContext.error(e, "获取EMP程序主机进程数异常!");
			return 0;
		}
	}

	/**
	 * 磁盘信息
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 上午11:05:33
	 */
	public long[] getHostDiskFree()
	{
		//磁盘信息 0:磁盘总量;1:磁盘剩余
		long[] diskInfo = {0,0};
		try
		{
			// 获取磁盘分区列表
			FileSystem fslist[] = sigar.getFileSystemList();
			FileSystem fs;
			FileSystemUsage usage = null;
			// 总容量
			long total = 0;
			// 磁盘剩余
			long free = 0;
			//为空时,初始化盘符列表,默认值为0
			if(diskList == null)
			{
				diskList = new int[fslist.length];
			}
			for (int i = 0; i < fslist.length; i++)
			{
				//为1表示光驱等获取不到空间的盘符
				if(diskList[i] == 1)
				{
					continue;
				}
				fs = fslist[i];
				// 分区文件
				try
				{
					usage = sigar.getFileSystemUsage(fs.getDirName());
				}
				catch (Exception e)
				{
					EmpExecutionContext.error("获取EMP程序主机磁盘"+fs.getDirName()+"剩余量异常，"+fs.getDirName()+"盘为光驱");
					//设置盘符为1,表示光驱等获取不到空间的盘符
					if(i <= diskList.length)
					{
						diskList[i] = 1;
					}
					continue;
				}
				// 2:磁盘信息
				if(fs.getType() == 2)
				{
					// 各磁盘分区剩余累加
					free = free + usage.getFree();
					// 各磁盘分区总容量累加
					total = total + usage.getTotal();
				}
			}
			// 磁盘总量单位为M
			total = total / 1024;
			diskInfo[0] = total;
			// 磁盘剩余单位为M
			free = free / 1024;
			diskInfo[1] = free;
			return diskInfo;
		}
		catch (SigarException e)
		{
			EmpExecutionContext.error(e, "获取EMP程序主机磁盘剩余量异常!");
			return diskInfo;
		}
	}

}
