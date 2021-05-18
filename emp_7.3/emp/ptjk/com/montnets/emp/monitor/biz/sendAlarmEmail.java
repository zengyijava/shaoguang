package com.montnets.emp.monitor.biz;

import java.util.LinkedList;
import java.util.List;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.monitor.constant.MonEmailParams;
import com.montnets.emp.monitor.util.EmailSendServlet;

/**
 * @author  quexs
 * @version 1.0.0
 * @2016-4-12 下午16:17:17
 *  company ShenZhen Montnets Technology CO.,LTD.
 */
public class sendAlarmEmail extends Thread
{
	//邮件发送类
	private static EmailSendServlet esl = new EmailSendServlet() ; 
	//待发邮件列表
	private static List<MonEmailParams> emaillist = new LinkedList<MonEmailParams>();

	public sendAlarmEmail(MonEmailParams  mep)
	{
		if(mep!=null){
			emaillist.add(mep);
		}
	}

	/**
	 * 发送邮件线程
	 */
	public void run()
	{
		try
		{
			synchronized (emaillist) {
				if(emaillist!=null && emaillist.size()>0){
					esl.ListSendMail(emaillist);
					emaillist.clear();
//					emaillist.removeAll(emaillist);
				}
			}
			Thread.sleep(20000L);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控告警邮件发送失败！");
			return;
		}
	}

}