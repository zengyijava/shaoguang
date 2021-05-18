/**
 * Program : IUpWSService.java
 * Author : zousy
 * Create : 2014-6-9 下午03:19:52
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.sysuser.util;

import com.montnets.emp.sysuser.util.UpWS;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-9 下午03:19:52
 */
public interface IUpWSService
{

	public java.lang.String getESAIAddress();

	public UpWS getESAI() throws javax.xml.rpc.ServiceException;

	public UpWS getESAI(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;

}
