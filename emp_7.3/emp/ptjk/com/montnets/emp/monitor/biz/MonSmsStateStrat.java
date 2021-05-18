/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-4-2 下午05:39:19
 */
package com.montnets.emp.monitor.biz;

import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.biz.receive.IReportStart;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfReport;
import com.montnets.emp.monitor.dao.MonitorDAO;

/**
 * @description 告警短信状态报告
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-4-2 下午05:39:19
 */

public class MonSmsStateStrat implements IReportStart
{

	AlarmSmsBiz alarmSmsBiz = new AlarmSmsBiz();
	
	/* (non-Javadoc)
	 * @see com.montnets.emp.common.biz.receive.IReportStart#start(com.montnets.emp.entity.system.LfReport)
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-9-26 上午11:23:48
	 */
	public boolean start(LfReport report) throws Exception
	{
		//状态
		String mtStat = report.getMtstat();
		//消息流水号
		String msgId = report.getMtmsgid();
		//手机号
		String phone = report.getPhone();
		//短信内容
		String msg = "";
		//等待2秒，防止数据还未写库成功就对数据进行更新
		Thread.sleep(2000L);
		if(mtStat != null && "DELIVRD".equals(mtStat))
		{
			//以MSGID为条件更新告警短信发送记录为1:成功
			alarmSmsBiz.setAlarmSmsRecord(msgId, 1, "1", "1");
		}
		else
		{
			//是否加载网优模块，17：网优
			if(!new SmsBiz().isWyModule("17"))
			{
				EmpExecutionContext.error("未加载网优模块，告警信息无法使用网优通道发送。");
				return false;
			}
			//短信发送时间是否大于三分钟
			msg = alarmSmsBiz.getAlarmSmsRecord(msgId);
			if(msg != null && !"".equals(msg))
			{
				String sendStatus = "0";
				//获取网优通道及SIM卡信息
				String[] gateInfo = new MonitorDAO().getIpcomAndSimInfo();
				if(gateInfo == null || "0".equals(gateInfo[1]) || "0".equals(gateInfo[2]) || "0".equals(gateInfo[3]))
				{
					gateInfo = gateInfo == null?new String[] {"0","0","0","0"}:gateInfo;
					EmpExecutionContext.error("告警短信重发失败，获取网优通道及SIM卡信息异常!IP:" + gateInfo[1]+"，端口："+gateInfo[2]+"，SIM卡："+gateInfo[3]);
					sendStatus = "0";
				}
				else
				{
					//调用网优短信发送接口
					String result = new SmsSendBiz().wySend(gateInfo, phone, msg);
					//发送状态,0:失败;1:成功
					if("success".equals(result))
					{
						sendStatus = "1";
					}
				}
				//以MSGID为条件更新告警短信发送记录
				alarmSmsBiz.setAlarmSmsRecord(msgId, 1, "2", sendStatus);
			}
		}
		return true;
	}

}
