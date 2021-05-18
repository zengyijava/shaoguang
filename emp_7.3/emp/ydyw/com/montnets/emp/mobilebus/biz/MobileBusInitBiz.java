/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-12-31 上午11:42:10
 */
package com.montnets.emp.mobilebus.biz;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.mobilebus.constant.MobileBusStaticValue;
import com.montnets.emp.mobilebus.dao.MobileBusDAO;
import org.apache.commons.beanutils.DynaBean;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @project p_ydyw
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-12-31 上午11:42:10
 */

public class MobileBusInitBiz
{
	MobileBusDAO mobileBusDAO = new MobileBusDAO();
	
	/**
	 * 执行定时任务
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-7 下午12:14:08
	 */
	public void executeTask()
	{
		try
		{
			BuckleFeeBiz buckleFeeBiz = new BuckleFeeBiz();
			// 获取任务执行时间
			int[][] TaskExecuteTime = buckleFeeBiz.getTaskExecuteTime();
			// 扣费任务执行时间
			int[] buckleFeeTestTime = TaskExecuteTime[0];
			// 欠费补扣任务执行时间
			//int[] buckleUpFeeTestTime = TaskExecuteTime[1];

			// 获取扣费任务执行时间，小时
			int hour = buckleFeeTestTime[0];
			// 获取扣费任务执行时间，分钟
			int minute = buckleFeeTestTime[1];
			if(hour != -1 && minute != -1)
			{
				// 扣费任务第一次执行时间
				Date date = buckleFeeBiz.getTaskExecuteTime(hour, minute);
				if(date != null)
				{
					// 扣费定时任务
					buckleFeeBiz.executeBuckleFeeTask(date);
				}
				else
				{
					EmpExecutionContext.info("获取扣费任务执行时间为NULL");
				}
			}

//			// 获取欠费补扣任务执行时间，小时
//			int buckUpHour = buckleUpFeeTestTime[0];
//			// 获取欠费补扣任务执行时间，小时
//			int buckUpMinute = buckleUpFeeTestTime[1];
//			if(buckUpHour != -1 && buckUpMinute != -1)
//			{
//				// 欠费补扣任务第一次执行时间
//				Date buckUpDate = buckleFeeBiz.getTaskExecuteTime(buckUpHour, buckUpMinute);
//				if(buckUpDate != null)
//				{
//					// 欠费补扣定时任务
//					buckleFeeBiz.executeBuckleUpFeeTask(buckUpDate);
//				}
//				else
//				{
//					EmpExecutionContext.info("获取欠费补扣任务执行时间为NULL");
//				}
//			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "移动业务扣费、欠费补扣定时任务启动异常！");
		}
	}
	
	/**
	 * 设置套餐类型至静态变量
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-7 下午01:45:22
	 */
	public void setTaoCanType()
	{
		try
		{
			List<DynaBean> taoCanTypeList = mobileBusDAO.getTaoCanType();
			if(taoCanTypeList != null && taoCanTypeList.size() > 0)
			{
				String subfieldValue = "";
				String fieldName = "";
				for(DynaBean taoCanType:taoCanTypeList)
				{
					subfieldValue = taoCanType.get("subfieldvalue") == null?"":taoCanType.get("subfieldvalue").toString();
					fieldName = taoCanType.get("fieldname") == null?"":taoCanType.get("fieldname").toString();
					if("".equals(subfieldValue))
					{
						subfieldValue = "-1";
					}
					MobileBusStaticValue.getTaoCanType().put(subfieldValue, fieldName);
					subfieldValue = "";
					fieldName = "";
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置套餐类型至静态变量异常！");
		}
	}
	
	/**
	 * 设置套餐指令分割符
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-23 下午03:58:11
	 */
	public void setRrderDelimiter()
	{
		try
		{
			// 获取套餐指令分割符
			List<DynaBean> rderDelimiterList = mobileBusDAO.getRrderDelimiter();
			if(rderDelimiterList != null && rderDelimiterList.size() > 0)
			{
				DynaBean rderDelimiter = rderDelimiterList.get(0);
				String globalId = rderDelimiter.get("globalid").toString();
				String globalStrValue = rderDelimiter.get("globalstrvalue").toString();
				//查询结果无异常，设置套餐指令分割符
				if(globalId != null && globalStrValue != null)
				{
					MobileBusStaticValue.setOrderDelimiter(globalStrValue);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置套餐指令分割符异常！");
		}
	}
}
