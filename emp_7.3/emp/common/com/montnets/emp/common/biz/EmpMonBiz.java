package com.montnets.emp.common.biz;

import org.json.simple.JSONObject;

import com.montnets.emp.common.context.EmpExecutionContext;

public class EmpMonBiz implements Runnable
{
	//服务信息BIZ
	EmpMonUtil empMonUtil = new EmpMonUtil();
	
	public void run()
	{
		try
		{
			//报文时间
			String evtTm = empMonUtil.getEvtTm();
			if(evtTm != null)
			{
				//报文类型，90：周期性采集
				String type = "90";
				//设置1303报文内容
				JSONObject json = empMonUtil.setEmpServerMonInfo();
				if(json != null)
				{
					//设置报文信息
					String message = empMonUtil.setMessage(json, evtTm, "1303", type);
					//System.out.println(message);
					if(message != null)
					{
						//写入文件
						//empMonUtil.writeMonFile(message, type);
						EmpExecutionContext.proCycleLog(message);
					}
					else
					{
						EmpExecutionContext.error("设置EMP服务器周期信息监控报文信息失败，message为null");
					}
				}
				else
				{
					EmpExecutionContext.error("设置EMP服务器周期信息监控报文信息失败，json为null");
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行EMP服务器监控周期信息信息定时任务异常!");
		}
	}
}
