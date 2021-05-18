/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-14 上午08:38:28
 */
package com.montnets.emp.mobilebus.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.biz.receive.IOrderStart;
import com.montnets.emp.entity.system.LfMotask;

/**
 * @description 
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-14 上午08:38:28
 */

public class MobileBusMoOrderStart extends SuperBiz implements IOrderStart  
{

	public boolean start(LfMotask moTask) throws Exception
	{
		//上行指令处理并返回结果
		return new MobileBusMoOrderHandleBiz().OrderHandle(moTask);
	}
}
