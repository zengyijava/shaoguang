/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 上午10:29:22
 */
package com.montnets.emp.monitor.biz.i;

import org.hyperic.sigar.SigarException;

/**EMP主机监控信息BIZ接口
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 上午10:29:22
 */

public interface IMonEmpHostInfoBiz
{


	/**
	 * 获取EMP主机CPU使用率
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 上午10:35:57
	 */
	public long getHostCpuUsage();

	/**
	 * 内存使用率
	 * 
	 * @description
	 * @return
	 * @throws SigarException
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 上午10:44:36
	 */
	public long[] getHostMemUsage();

	/**
	 * 虚拟内存使用率
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午01:14:02
	 */
	public long[] getHostVmemUsage();

	/**
	 * 主机进程数
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 上午11:34:07
	 */
	public int getHostProcessCount();

	/**
	 * 磁盘信息
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 上午11:05:33
	 */
	public long[] getHostDiskFree();

}
