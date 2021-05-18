package com.montnets.emp.netnews.common;

import java.util.TimerTask;

/**
 * 
*     
* 类名称：ChkScheduleTask   
* 类描述： 定时调度方法，用于网讯访问历史记录定时汇总
* 修改时间：2015-10-15 下午02:41:01   
* 修改备注：   
* @version    
*
 */
public class ChkScheduleTask extends TimerTask
{
	public void run()
	{
		FileJsp.mainTimer(false);
	}

}
