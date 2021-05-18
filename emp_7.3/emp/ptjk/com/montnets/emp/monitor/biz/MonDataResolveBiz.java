/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-11-21 下午05:46:08
 */
package com.montnets.emp.monitor.biz;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.monitor.constant.MonitorStaticValue;

/**
 * @description
 * @project emp_std_189
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-11-21 下午05:46:08
 */

public class MonDataResolveBiz implements Runnable
{
	ParsetMqXmlBiz	parsetMqXmlBiz	= new ParsetMqXmlBiz();

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-9-26 上午11:23:48
	 */
	public void run()
	{
		String monitorMsg = "";
		while(MonitorStaticValue.isMonThreadState())
		{
			try
			{
				// 监控消息队列当前大小
				int count = MonitorStaticValue.getMonitorMsgQueue().size();
				if(count > 0)
				{
					for (int i = 0; i < count; i++)
					{
						// 移除并返回头部消息
						monitorMsg = MonitorStaticValue.getMonitorMsgQueue().poll();
						if(monitorMsg != null && !"".equals(monitorMsg))
						{
							// 解析数据
							parsetMqXmlBiz.paresetStringXml(monitorMsg);
						}
						// 清空
						monitorMsg = "";
					}
				}
				else
				{
					parsetMqXmlBiz.closeConnection();
					Thread.sleep(2000L);
				}
			}
			catch (InterruptedException e)
			{
				if(MonitorStaticValue.isMonThreadState())
				{
					EmpExecutionContext.error(e, "解析监控消息队列异常！");
				}
				Thread.currentThread().interrupt();
			}
		}
	}

}
