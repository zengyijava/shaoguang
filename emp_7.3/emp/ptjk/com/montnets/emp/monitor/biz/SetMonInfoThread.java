/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-4-23 下午06:52:13
 */
package com.montnets.emp.monitor.biz;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.monitor.dao.MonitorDAO;

/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-23 下午06:52:13
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

public class SetMonInfoThread extends Thread
{
	MonitorDAO monitorDAO = new MonitorDAO();
	
	MonitorBaseInfoBiz monitorBaseInfoBiz = new MonitorBaseInfoBiz();
	
	public SetMonInfoThread()
	{
		this.setName("监控信息线程ID"+this.getId());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-23 下午06:52:26
	 */
	public void run()
	{
		EmpExecutionContext.info("启动"+this.getName());
		while(MonitorStaticValue.isMonThreadState())
		{
			try
			{
				Thread.sleep(5*60*1000L);
				//设置通道账号费用信息
				monitorDAO.setGateAccoutFee(false);
				//设置数据库与程序监控
				monitorBaseInfoBiz.addDbAndProceMonInfo();
				//设置主机网络监控
				monitorBaseInfoBiz.addHostNetMonInfo();
			}
			catch (InterruptedException e)
			{
				if(MonitorStaticValue.isMonThreadState())
				{
					EmpExecutionContext.error(e, "监控信息线程序异常!");
				}
				Thread.currentThread().interrupt();
			}
		}
		EmpExecutionContext.info("停止"+this.getName());
	}

}
