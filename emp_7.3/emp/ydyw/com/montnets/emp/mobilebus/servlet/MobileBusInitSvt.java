/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-31 下午05:15:05
 */
package com.montnets.emp.mobilebus.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.mobilebus.biz.MobileBusInitBiz;

/**
 * @description
 * @project p_ydyw
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-12-31 上午11:17:31
 */
public class MobileBusInitSvt extends HttpServlet
{
	/**
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-31 下午05:16:14
	 */
	private static final long	serialVersionUID	= 1L;

	MobileBusInitBiz			mobileBusInitBiz	= new MobileBusInitBiz();

	/**
	 * 移动业务初始化方法
	 */
	public void init() throws ServletException
	{
		try
		{
			//设置计费类型控件标签到静态变量中
			mobileBusInitBiz.setTaoCanType();
			//设置套餐指令分割符
			mobileBusInitBiz.setRrderDelimiter();
			// 是否加载移动业务模块，22：移动业务
			if(new SmsBiz().isWyModule("22"))
			{
				// 是否运行定时任务，1代表运行，0代表不运行
				if(StaticValue.ISRUNTIMEJOB == 1)
				{
					//执行定时任务
					mobileBusInitBiz.executeTask();
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "移动业务初始化SVT异常！");
		}
	}
}
