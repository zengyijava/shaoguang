/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 上午11:53:05
 */
package com.montnets.emp.monitor.biz.i;


/**EMP程序信息BIZ接口
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 上午11:53:05
 */

public interface IMonEmpProceInfoBiz
{
	public long getProceCpuUsage();

	/**
	 * @description 获取EMP程序物理内存使用率
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午12:01:39
	 */
	public long getProceMemUsage();

	/**
	 * @description 获取EMP程序虚拟内存使用率
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午12:01:39
	 */
	public long getProceVmemUsage();

	/**
	 * EMP程序所在磁盘剩余量
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午01:34:17
	 */
	public long getProceDiskFree();

}
