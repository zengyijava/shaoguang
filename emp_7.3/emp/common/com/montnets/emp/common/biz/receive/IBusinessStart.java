package com.montnets.emp.common.biz.receive;

import com.montnets.emp.entity.system.LfMotask;


/**
 * 
 * @author Administrator
 *
 */
public interface IBusinessStart
{
	/**
	 * 
	 * @param moTask
	 * @throws Exception
	 */
	public boolean start(LfMotask moTask) throws Exception ;
}
