package com.montnets.emp.engine.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.biz.receive.IBusinessStart;
import com.montnets.emp.entity.system.LfMotask;

/**
 * 
 * @author Administrator
 *
 */
public class MoServiceStart extends SuperBiz implements IBusinessStart
{

	public boolean start(LfMotask moTask) throws Exception
	{
		//上行处理biz
		MoAutoHandleBiz upBiz = new MoAutoHandleBiz();
		//运行并返回结果
		return upBiz.RunUpServiceAccess(moTask);
	}
	
}
