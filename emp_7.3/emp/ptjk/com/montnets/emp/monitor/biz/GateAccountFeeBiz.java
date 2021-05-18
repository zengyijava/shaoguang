/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-17 下午03:56:55
 */
package com.montnets.emp.monitor.biz;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.monitor.dao.MonitorDAO;

/**
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-17 下午03:56:55
 */

public class GateAccountFeeBiz implements Runnable
{

	MonitorDAO monitorDAO = new MonitorDAO();
	private boolean isShutdown = false;
	
	public void shutdown(){
		this.isShutdown = true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-9-26 上午11:23:48
	 */
	public void run()
	{
		while(!isShutdown)
		{
			try
			{
				//设置通道账号费用信息
				Thread.sleep(5*60*1000L);
				monitorDAO.setGateAccoutFee(false);
			}
			catch (InterruptedException e)
			{
				EmpExecutionContext.error(e, "设置通道账号费用信息异常!");
				Thread.currentThread().interrupt();
			}
		}
	}

}
