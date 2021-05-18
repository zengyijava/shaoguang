package com.montnets.emp.engine.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.biz.receive.IOrderStart;
import com.montnets.emp.entity.system.LfMotask;

/**
 * 
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-19 下午07:31:28
 * @description 指令推送业务类
 */
public class MoOrderStart extends SuperBiz implements IOrderStart
{

	public boolean start(LfMotask moTask) throws Exception
	{
		//上行处理biz
		MoAutoHandleBiz upBiz = new MoAutoHandleBiz();
		//运行并返回结果
		return upBiz.RunMoSerByOrderCode(moTask);
	}
	
}
